/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gg.msn.ui.helper;

import gg.msn.ui.ChatClientView;
import gg.msn.ui.chatwindow.ChatWindow;
import gg.msn.ui.form.ReceiveFileDialog;
import gg.msn.ui.form.SendFileDialog;
import chatcommons.Client;
import gg.msn.core.commons.Util;
import gg.msn.ui.game.Canvas;
import gg.msn.ui.game.GameHome;
import gg.msn.ui.game.dama.DamaCanvas;
import gg.msn.core.thread.ClientReader;
import chatcommons.datamessage.MESSAGE;
import chatcommons.datamessage.MessageManger;
import facebookchat.common.FacebookUser;
import gg.msn.core.manager.ConnectionManager;
import gg.msn.core.manager.PersistentDataManager;
import gg.msn.ui.ChatClientApp;
import gg.msn.ui.facebook.FBLoginPanel;
import gg.msn.ui.listener.MessageReceivedListener;
import java.awt.HeadlessException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Properties;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.swing.JFrame;
import org.jdesktop.application.Application;
import static chatcommons.Commands.*;

/**
 *
 * @author Administrator
 */
public class ChatClientViewHelper {

    private static Log log = LogFactory.getLog(ChatClientViewHelper.class);
    private ChatClientView ccv;
    private ArrayList<ChatWindow> chatWindows;
    private ArrayList<Canvas> canvases;
    private ArrayList<JFrame> fileDialogs;

    public ChatClientViewHelper(ChatClientView ccv) {
        this.ccv = ccv;
        chatWindows = new ArrayList<ChatWindow>();
        canvases = new ArrayList<Canvas>();
        fileDialogs = new ArrayList<JFrame>();

    }

    /**
     * rende visibile il MainPanel e nasconde loginPanel
     */
    public void showMainPanel() {
        ccv.getFrame().setContentPane(ccv.getMainPanel());
        ccv.getFrame().repaint();
        ccv.getFrame().validate();
    }

    /**
     * rende visibile il loginPanel e nasconde mainPanel
     */
    public void showLoginPanel() {
        ccv.getFrame().setContentPane(ccv.getLoginPanel());
        ccv.getFrame().repaint();
        ccv.getFrame().validate();
    }

    public void showFacebookLoginPanel() {
        ccv.getFrame().remove(ccv.getMainPanel());
        ccv.getFrame().setContentPane(new FBLoginPanel(ccv));
        ccv.getFrame().repaint();
        ccv.getFrame().validate();
    }

    /**
     * Connette al server principle 
     * @return
     */
    public void connect() {
        new Thread(new Runnable() {

            public void run() {
                try {
                    connects();
                    return;
                    /*catch for some exception*/
                } catch (ConnectException e) {
                    showErrorDialog("Impossibile connettersi al server");
                    resetLoginPanel();
                    log.error(e);
                } catch (SocketException e) {
                    showErrorDialog(e.toString());
                    resetLoginPanel();
                    log.error(e);
                } catch (UnknownHostException ex) {
                    showErrorDialog(ex.toString());
                    resetLoginPanel();
                    log.error(ex);
                } catch (IOException ex) {
                    showErrorDialog(ex.toString());
                    resetLoginPanel();
                    log.error(ex);
                }

            }

            private void connects() throws SocketException, IOException, UnknownHostException, ConnectException {

                //blocco input text e bottone
                ccv.getNickText().setEnabled(false);
                ccv.getLogin().setEnabled(false);

                //leggo il nick
                String nick = ccv.getNickText().getText().trim();

                //connetto il socket
                Socket socket = new Socket();
                String ip = PersistentDataManager.getIp();
                int port = PersistentDataManager.getPort();

                log.debug("address ip [" + ip + "] port [" + port + "]");

                InetSocketAddress inetSocketAddress = new InetSocketAddress(ip, port);
                String response = new ConnectionManager().connect(socket, inetSocketAddress, nick);

                log.debug("server response [" + response + "]");

                if (response.equals(Command.KO)) {
                    JOptionPane.showMessageDialog(ccv.getFrame(), "<html><font color=blue>Il nick scelto è già connesso<html>", "Attenzione", JOptionPane.WARNING_MESSAGE);
                    resetLoginPanel();
                    return;

                } else {

                    //setto il protocolllo
                    ChatClientView.protocol = ChatClientView.GIGIMSN_PROTOCOL;
                    //messaggio di stato
                    //setMessage("Connecting to server");
                    //creo il socket
                    PersistentDataManager.setSocket(socket);
                    log.info("client : connect on port : " + PersistentDataManager.getPort());
                    //connetto il socket
                    //-PersistentDataManager.getSocket().connect(new InetSocketAddress(PersistentDataManager.getIp(), PersistentDataManager.getPort()));
                    PersistentDataManager.setOutputStream(PersistentDataManager.getSocket().getOutputStream());
                    PersistentDataManager.setNick(nick);
                }


                //lancio il thread
                new Thread(new ClientReader(new MessageReceivedListener(ccv), ClientReader.MAINREADER)).start();
                ccv.getMainPanel().getNickLabel().setText(PersistentDataManager.getNick());
                showMainPanel();
                //riattivo inputtext e bottone di login
                ccv.getNickText().setEnabled(true);
                ccv.getLogin().setEnabled(true);
                //setMessage("Connected");
                //aggiorno il messaggio nella systemtray
                if (ccv.getTray() != null) {
                    ccv.getTray().setToolTip("Gigi Messenger - connesso : " + PersistentDataManager.getNick());
                }
                //nel prperties salvo il nick impostato per  la connessione
                Properties properties = Util.readProperties();
                if (properties != null) {
                    properties.setProperty("nick", nick);
                    Util.writeProperties(properties);
                } else {
                    properties = new Properties();
                    properties.setProperty("nick", nick);
                    Util.writeProperties(properties);
                }
                /*catch for some exception*/
            }

            private void resetLoginPanel() {
                ccv.getNickText().setEnabled(true);
                ccv.getLogin().setEnabled(true);
            }
        }).start();

        //return new Connect(Application.getInstance());
    }

    public void showErrorDialog(String message) throws HeadlessException {
        JOptionPane.showMessageDialog(ccv.getFrame(), "<html><font color=red>" + message + "</html>", "Errore", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Disconnettte dal serever principale
     */
    public void disconnetti() {
        try {
            //TODO metti nel finalli i comendi da eseguire per forza

            new ConnectionManager().disconnect(PersistentDataManager.getSocket());
            PersistentDataManager.setOutputStream(null);
            PersistentDataManager.setClients(new Hashtable<String, Client>());

            ((DefaultListModel) ccv.getMainPanel().getClientsList().getModel()).removeAllElements();

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
     * Azione che lancia una chat con l'utente selezionato sulla tabella nick
     */
    public void addChatWithSelected() {

        String nickSelected = "";

        if (ChatClientView.protocol.equals(ChatClientView.FACEBOOK_PROTOCOL)) {
            nickSelected = ((FacebookUser) ccv.getMainPanel().getClientsList().getSelectedValue()).name;
        } else {
            nickSelected = ((Client) ccv.getMainPanel().getClientsList().getSelectedValue()).getNick();
        }
        if (nickSelected != null && !nickSelected.equals("")) {
            log.info("nick selected : " + nickSelected);
            ChatWindow chatWith = getChatWith(nickSelected);
        }
    }

    /**
     * Avvia una nuova chat con il nick passato come paramtero
     * @param nick 
     * Il nick da chiamare in chat
     */
    private ChatWindow startChatWith(String nick) {

        log.info("creo una nuova chat con [" + nick + "]");
        ChatWindow cv = new ChatWindow(true, ccv);
        cv.setLocationRelativeTo(ccv.getFrame());
        cv.getClients().add(new Client(null, nick));
        cv.refreshTable();
        cv.setVisible(true);
        cv.toFront();
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

        //cerco una chat con l'uetente selezionato
        for (ChatWindow chatWindow : chatWindows) {
            //se c'è un sollo utente è una chat
            //altrimenti nn va bemne perchè è una conferenza
            if (chatWindow.getClients().size() == 1 && chatWindow.getClients().get(0).getNick().equals(nick)) {
                toReturn = chatWindow;
                log.info("trovata chat già aperta con [" + nick + "]");
                break;
            }
        }

        if (toReturn != null) {
            //se c'è già una chat con l'utente scelto la apro
            toReturn.setVisible(true);
            toReturn.toFront();
            return toReturn;
        } else {
            //altrimenti ne creo una nuova
            log.info("nessuna chat già aperta con [" + nick + "]");
            return startChatWith(nick);
        }
    }

    public ChatWindow getConferenceWith(String[] nicks) {
        try {
            boolean present = false;
            log.debug("chatwindows : " + chatWindows.size());
            for (ChatWindow chatWindow : chatWindows) {
                if (chatWindow.getClients().size() == nicks.length) {
                    //se clients e nock hanno las stessa dimensione inizio la verifica sennò
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

        String nickSelected = (String) ccv.getMainPanel().getClientsList().getSelectedValue();
        GameHome home = new GameHome(ccv);
        home.setLocationRelativeTo(ccv.getFrame());
        home.setVisible(true);
        home.startDamaServer(nickSelected);

        MESSAGE message = MessageManger.createRequest(Request.STARTGAME, null, null);
        MessageManger.addReceiver(message, nickSelected);

        try {
            MessageManger.directWriteMessage(message, PersistentDataManager.getOutputStream());
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
    // </editor-fold>
}
