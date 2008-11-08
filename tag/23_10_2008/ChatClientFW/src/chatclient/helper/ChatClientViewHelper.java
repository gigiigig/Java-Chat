/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient.helper;

import chatclient.ChatClientView;
import chatclient.chatwindow.ChatWindow;
import chatclient.forms.ReceiveFileDialog;
import chatclient.forms.SendFileDialog;
import chatcommons.Client;
import chatclient.commons.Util;
import chatclient.game.Canvas;
import chatclient.game.GameHome;
import chatclient.game.dama.DamaCanvas;
import chatclient.theme.ThemeManager;
import chatclient.thread.ClientReader;
import chatcommons.datamessage.MESSAGE;
import chatcommons.datamessage.MessageManger;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.application.Application;
import org.jdesktop.application.Task;
import javax.swing.JDialog;
import javax.swing.JFrame;
import static chatcommons.Commands.*;

/**
 *
 * @author Administrator
 */
public class ChatClientViewHelper {

    private Log log = LogFactory.getLog(this.getClass());
    private ChatClientView ccv;
    private ArrayList<ChatWindow> chatWindows;
    private ArrayList<Canvas> canvases;
    private ArrayList<JFrame> fileDialogs;
    private Map<String, ImageIcon> theme;

    public ChatClientViewHelper(ChatClientView ccv) {
        this.ccv = ccv;
        chatWindows = new ArrayList<ChatWindow>();
        canvases = new ArrayList<Canvas>();
        fileDialogs = new ArrayList<JFrame>();
        Properties properties = Util.readProperties();
        if (properties != null) {
            theme = ThemeManager.loadTheme(properties.getProperty(Util.THEME_FOLDER));
        } else {
            theme = new HashMap<String, ImageIcon>();
        }
    }

    /**
     * rende visibile il MainPanel e nasconde loginPanel
     */
    public void showMainPanel() {
        ccv.getFrame().remove(ccv.getLoginPanel());
        ccv.getFrame().add(ccv.getMainPanel());
//        ccv.getFrame().add(ccv.getStatusPanel());
//        ccv.getConnect().setContentAreaFilled(false);
        ccv.getFrame().repaint();
        ccv.getFrame().validate();
    }

    /**
     * rende visibile il loginPanel e nasconde mainPanel
     */
    public void showLoginPanel() {
        ccv.getFrame().remove(ccv.getMainPanel());
        ccv.getFrame().add(ccv.getLoginPanel());
//        ccv.getLogin().setContentAreaFilled(false);
//        ccv.getNickText().setEnabled(true);
        ccv.getFrame().repaint();
        ccv.getFrame().validate();
    }

    /**
     * Connette al server principle utilizzando un task
     * @return
     */
    public Task connettiTask() {

        class Connect extends Task {

            public Connect(Application a) {
                super(a);
            }

            @Override
            protected Object doInBackground() throws Exception {
                try {

                    ccv.getNickText().setEnabled(false);
                    setMessage("Connecting to server");

                    //creo il socket
                    ccv.setSocket(new Socket());
                    log.info("client : connect on port : " + ccv.getPort());

                    //connetto il socket
                    ccv.getSocket().connect(new InetSocketAddress(ccv.getIp(), ccv.getPort()));
                    ccv.setOutputStream(ccv.getSocket().getOutputStream());

                    //leggo il nick
                    String nick = ccv.getNickText().getText().trim();
                    ccv.setNick(nick);

                    //invio al server il nick name
                    MESSAGE request = MessageManger.createCommand(Command.CONNECT, new HashMap<String, String>());
                    MessageManger.addParameter(request, "nick", nick);

                    MessageManger.directWriteMessage(request, ccv.getOutputStream());

                    //leggo se il nick è stato accettato
                    BufferedReader reader = new BufferedReader(new InputStreamReader(ccv.getSocket().getInputStream()));
                    String accptedNick = reader.readLine();

                    MESSAGE message = MessageManger.parseXML(accptedNick);
                    log.debug(nick+" read response : "+MessageManger.messageToStringFormatted(message));
                    
                    String response = message.getParameters().getParameter().get(0).getValue();
                    log.debug("response value :"+response);
                    
                    if (response.equals(Command.KO)) {
                        JOptionPane.showMessageDialog(ccv.getFrame(), "<html><font color=blue>Il nick scelto è già connesso<html>", "Attenzione", JOptionPane.WARNING_MESSAGE);
                        ccv.getSocket().close();
                        ccv.setSocket(null);
                        ccv.setOutputStream(null);
                        ccv.getNickText().setEnabled(true);

                        return null;
                    }

                    //lancio il thread
                    new ClientReader(ccv.getSocket(), ccv, ClientReader.MAINREADER).execute();

                    ccv.getNickLabel().setText(ccv.getNick());
                    showMainPanel();
                    ccv.getNickText().setEnabled(true);
                    setMessage("Connected");
                    if (ccv.getTray() != null) {
                        ccv.getTray().setToolTip("Gigi Messenger - connesso : " + ccv.getNick());
                    }
                    Properties properties = Util.readProperties();
                    if (properties != null) {
                        properties.setProperty("nick", nick);
                        Util.writeProperties(properties);
                    } else {
                        properties = new Properties();
                        properties.setProperty("nick", nick);
                        Util.writeProperties(properties);
                    }

//  
        /*catch for some exception*/
                } catch (ConnectException e) {
//                    ccv.ShowMessageFrame("<html><font color=red>Impossibile connettersi al server<html>");
                    JOptionPane.showMessageDialog(ccv.getFrame(), "<html><font color=red>Impossibile connettersi al server<html>", "Errore", JOptionPane.ERROR_MESSAGE);
                    ccv.getNickText().setEnabled(true);
                } catch (SocketException e) {
                    JOptionPane.showMessageDialog(ccv.getFrame(), "<html><font color=red>" + e.getMessage() + "<html>", "Errore", JOptionPane.ERROR_MESSAGE);
                } catch (UnknownHostException ex) {
                    log.error(ex);
                } catch (IOException ex) {
                    log.error(ex);
                }
                return null;
            }

            @Override
            protected void finished() {
                setMessage("done");
            }
        }

        return new Connect(Application.getInstance());
    }

    /**
     * Disconnettte dal serever principale
     */
    public void disconnetti() {
        try {
            MESSAGE toSend = MessageManger.createCommand(Command.DISCONNECT, null);
            log.debug("send : " + MessageManger.messageToStringFormatted(toSend));
            MessageManger.directWriteMessage(toSend, ccv.getOutputStream());

            //TODO metti nel finalli i comendi da eseguire per forza

            ccv.getSocket().close();
            ccv.setOutputStream(null);
            ccv.setClients(new ArrayList<Client>());

            ((DefaultListModel) ccv.getClientsList().getModel()).removeAllElements();
//            ((DefaultListModel) ccv.getClientsList().getModel()).addElement(null);

            chatWindows = new ArrayList<ChatWindow>();
            canvases = new ArrayList<Canvas>();
            fileDialogs = new ArrayList<JFrame>();

            showLoginPanel();
        } catch (SocketException se) {
            log.error(se);
//            ccv.ShowMessageFrame("Il server è disconnesso");
            JOptionPane.showMessageDialog(ccv.getFrame(), "<html><font color=red>Il server non risponde<html>", "Errore", JOptionPane.ERROR_MESSAGE);
            showLoginPanel();

        } catch (IOException ex) {
            log.error(ex);
        }
    }

    /**
     * 
     * @param request
     */
    public void sendRequest(String request) {
        try {
            ccv.getOutputStream().write(request.getBytes(Util.DEFAULTENCODING));
        } catch (SocketException se) {
            log.error(se);
            JOptionPane.showMessageDialog(ccv.getFrame(), "<html><font color=red>El server non risponde<html>", "Errore", JOptionPane.ERROR_MESSAGE);
            showLoginPanel();
        } catch (IOException ex) {
            log.error(ex);
        }
    }

    /**
     * Azione che lancia una chat con l'utente selezionato sulla tabella nick
     */
    public void addChatWithSelected() {

        String nickSelected = (String) ccv.getClientsList().getSelectedValue();
        if (nickSelected != null && !nickSelected.equals("")) {
            log.info("nick selected : " + nickSelected);
             getChatWith(nickSelected).toFront();
        }
    }

    /**
     * Chaiama in chat il nick passato come paramtero
     * @param nick 
     * Il nick da chiamare in chat
     */
    private ChatWindow startChatWith(String nick) {

        ChatWindow cv = new ChatWindow(true, ccv);
        cv.setLocationRelativeTo(ccv.getFrame());
        cv.getClients().add(new Client(null, nick));
        cv.refreshTable();
        cv.setVisible(true);
        cv.setTitle(nick + " - Conversazione");
        chatWindows.add(cv);

        return cv;
    }

    /**
     * Restituisce una ChatWindow col nick passato come paramtro,
     * se c'è già restituisce quella sennò una nuova
     * @param nick 
     * Il nick da chiamare in chat
     */
    public ChatWindow getChatWith(String nick) {
//        ChatClientApp.getApplication().show(new ChatView(true, getNick(), "localhost", 3636));

        ChatWindow toReturn = null;

        for (ChatWindow chatWindow : chatWindows) {
            if (chatWindow.getClients().get(0).getNick().equals(nick)) {
                toReturn = chatWindow;
                break;
            }
        }

        if (toReturn != null) {
            return toReturn;
        } else {
            return startChatWith(nick);
        }
    }

    public ChatWindow getConferenceWith(String[] nicks) {
        try {
            boolean present = false;
            log.debug("chatwindows : " + chatWindows.size());
            for (ChatWindow chatWindow : chatWindows) {
                if (chatWindow.getClients().size() == nicks.length) {
                    //se clients e nock hanno las stessa diensione inizio la verifica sennò
                    //è sicuramente nn valida
                    int trovati = 0;
                    for (String nick : nicks) {
                        for (Client client : chatWindow.getClients()) {
                            if (nick.equals(client.getNick())) {
                                trovati++;
                            }
                        }
                    }
                    if (trovati == nicks.length) {
                        return chatWindow;
                    }
                }
            }


            //se nn trovo la conferenza ne creo una
            ChatWindow cv = new ChatWindow(true, ccv);
            cv.setLocationRelativeTo(ccv.getFrame());
            for (String nick : nicks) {
                cv.getClients().add(new Client(null, nick));
            }

            cv.refreshTable();
            cv.setVisible(true);
            chatWindows.add(cv);

            return cv;
        } catch (Exception e) {
            log.debug(e);
        }
        return null;
    }

    public Canvas getGameWith(String nick, Class typeOfGame) {

        for (Canvas elem : canvases) {
            log.debug("canvases pos : " + elem);
            log.debug("elem instanceof DamaCanvas" + (elem instanceof DamaCanvas));
            if (elem instanceof DamaCanvas && ((DamaCanvas) elem).getNickAdversar().equals(nick)) {
                log.debug("finded existing games");
                return elem;
            }
        }

        return null;


//        GameHome home = new GameHome(ccv, nick);
//        home.startDamaClient();
//        canvases.add(home.getCanvas());
//        log.debug(home.getCanvas());
//        return (home.getCanvas());



    }

    public SendFileDialog getSendFileDialog(String fileName, String receiver) {
        log.debug("fileDialogs.size() : " + fileDialogs.size());
        for (JFrame fileDialog : fileDialogs) {
            if (fileDialog instanceof SendFileDialog) {
                String elemReceiver = ((SendFileDialog) fileDialog).getReceiver();
                String elemFileName = ((SendFileDialog) fileDialog).getFileSender().getToSend().getName();
                log.debug(" elem : " + elemReceiver);
                log.debug(" elem : " + elemFileName);
                log.debug(" params : " + receiver);
                log.debug(" params : " + fileName);
                if (elemReceiver.equals(receiver) && elemFileName.equals(fileName)) {

                    return (SendFileDialog) fileDialog;
                }
            }
        }

        return null;
    }

    public ReceiveFileDialog getReceiveFileDialog(String fileName, String sender) {
        for (JFrame fileDialog : fileDialogs) {
            if (fileDialog instanceof ReceiveFileDialog) {
                String elemSender = ((ReceiveFileDialog) fileDialog).getSender();
                String elemFileName = ((ReceiveFileDialog) fileDialog).getFile();

//                log.debug(" elem : "+elemFileName +" "+elemSender);
//                log.debug(" params : "+fileName +" "+sender);
                if (elemSender.equals(sender) && elemFileName.equals(fileName)) {
                    return (ReceiveFileDialog) fileDialog;
                }
            }
        }
        return null;
    }

    /**
     * Inizia una partita con l'utente selezionato nella lista di utenti
     */
    public void startGameWithSelected() {

        String nickSelected = (String) ccv.getClientsList().getSelectedValue();
        GameHome home = new GameHome(ccv);
        home.setLocationRelativeTo(ccv.getFrame());
        home.setVisible(true);
        home.startDamaServer(nickSelected);

        MESSAGE message = MessageManger.createRequest(Request.STARTGAME, null, null);
        MessageManger.addReceiver(message, nickSelected);

        try {
            MessageManger.directWriteMessage(message, ccv.getOutputStream());
        } catch (SocketException socketException) {
            log.error(socketException);
        }
    }
    // <editor-fold defaultstate="collapsed" desc=" Getter and Setter ">
    public ArrayList<ChatWindow> getChatWindows() {
        return chatWindows;
    }

    public void setChatWindows(ArrayList<ChatWindow> chatWindows) {
        this.chatWindows = chatWindows;
    }

    public ArrayList<Canvas> getCanvases() {
        return canvases;
    }

    public void setCanvases(ArrayList<Canvas> canvases) {
        this.canvases = canvases;
    }

    public ArrayList<JFrame> getFileDialogs() {
        return fileDialogs;
    }

    public void setFileDialogs(ArrayList<JFrame> fileDialogs) {
        this.fileDialogs = fileDialogs;
    }

    public Map<String, ImageIcon> getTheme() {
        return theme;
    }

    public void setTheme(Map<String, ImageIcon> theme) {
        this.theme = theme;
    }    // </editor-fold>
}
