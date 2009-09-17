/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gg.msn.ui.helper;

import gg.msn.ui.ChatClientView;
import gg.msn.ui.chatwindow.ChatWindow;

import chatcommons.Client;
import gg.msn.core.commons.Util;

import emoticon.EmoticonsManger;
import gg.msn.facebook.core.FacebookManager;
import gg.msn.facebook.core.FacebookUser;
import gg.msn.core.manager.PersistentDataManager;
import gg.msn.ui.ChatClientApp;
import gg.msn.ui.facebook.panel.FBLoginPanel;
import gg.msn.ui.theme.ThemeManager;
import java.awt.HeadlessException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.swing.JFrame;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jdesktop.application.ResourceMap;

/**
 *
 * @author Administrator
 */
public class ChatClientViewHelper {

    private static Log log = LogFactory.getLog(ChatClientViewHelper.class);
    private ChatClientView ccv;
    private ArrayList<ChatWindow> chatWindows;
    private ArrayList<JFrame> fileDialogs;

    public ChatClientViewHelper(ChatClientView ccv) {
        this.ccv = ccv;
        chatWindows = new ArrayList<ChatWindow>();
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

    public void showFacebookLoginPanel() {
        ccv.getFrame().remove(ccv.getMainPanel());
        ccv.getFrame().setContentPane(new FBLoginPanel(ccv));
        ccv.getFrame().repaint();
        ccv.getFrame().validate();
    }

    //return new Connect(Application.getInstance());
    public void showErrorDialog(String message) throws HeadlessException {
        JOptionPane.showMessageDialog(ccv.getFrame(), "<html><font color=red>" + message + "</html>", "Errore", JOptionPane.ERROR_MESSAGE);
    }

    public void showWarnDialog(String message) throws HeadlessException {
        JOptionPane.showMessageDialog(ccv.getFrame(), "<html><font color=red>" + message + "</html>", "Attenzione", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Disconnettte dal serever principale
     */
    public void disconnetti() {
        if (StringUtils.equals(ChatClientView.protocol, ChatClientView.FACEBOOK_PROTOCOL)) {
            new Thread(new Runnable() {

                public void run() {
                    FacebookManager.shutdown();
                }
            }).start();

            showFacebookLoginPanel();
        }
    }

    /**
     * Azione che lancia una chat con l'utente selezionato sulla tabella nick
     */
    public void startChatWithSelected() {

        Client nickSelected = null;

        if (ChatClientView.protocol.equals(ChatClientView.FACEBOOK_PROTOCOL)) {
            nickSelected = clientFromFacebookUser((FacebookUser) ccv.getMainPanel().getClientsList().getSelectedValue());
        } else {
            nickSelected = ((Client) ccv.getMainPanel().getClientsList().getSelectedValue());
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
    private ChatWindow startChatWith(Client selected) {

        log.info("creo una nuova chat con [" + selected + "]");
        ChatWindow cv = new ChatWindow(true, ccv, selected);
        cv.setLocationRelativeTo(ccv.getFrame());
        //cv.getClients().add(selected);
        cv.refreshTable();
        cv.setVisible(true);
        cv.toFront();
        cv.setTitle(selected.getNick() + " - Conversazione");
        chatWindows.add(cv);

        return cv;
    }

    /**
     * Restituisce una ChatWindow col nick passato come paramtro,
     * se c'è già restituisce quella sennò una nuova
     * @param nick 
     * Il nick da chiamare in chat
     */
    public ChatWindow getChatWith(Client selected) {
//        ChatClientApp.getApplication().show(new ChatView(true, getNick(), "localhost", 3636));

        ChatWindow toReturn = getChatWithNullable(selected);


        if (toReturn != null) {
            //se c'è già una chat con l'utente scelto la apro
            //toReturn.setVisible(true);
            //toReturn.toFront();
            return toReturn;
        } else {
            //altrimenti ne creo una nuova
            log.info("nessuna chat già aperta con [" + selected + "]");
            return startChatWith(selected);
        }
    }

    public ChatWindow getChatFromUid(String toFind) {
        ChatWindow toReturn = null;
        for (ChatWindow chatWindow : chatWindows) {
            //se c'è un sollo utente è una chat
            //altrimenti nn va bemne perchè è una conferenza
            if (chatWindow.getClients().size() == 1 && chatWindow.getClients().get(0).getUid().equals(toFind)) {
                toReturn = chatWindow;
                log.info("trovata chat già aperta con [" + toReturn + "]");
                break;
            }
        }
        return toReturn;
    }

    public ChatWindow getChatWithNullable(Client selected) {
//        ChatClientApp.getApplication().show(new ChatView(true, getNick(), "localhost", 3636));

        ChatWindow toReturn = null;

        //cerco una chat con l'uetente selezionato
        for (ChatWindow chatWindow : chatWindows) {
            //se c'è un sollo utente è una chat
            //altrimenti nn va bemne perchè è una conferenza
            if (chatWindow.getClients().size() == 1 && chatWindow.getClients().get(0).getNick().equals(selected.getNick())) {
                toReturn = chatWindow;
                log.info("trovata chat già aperta con [" + selected + "]");
                break;
            }
        }

        return toReturn;
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
            ChatWindow cv = new ChatWindow(true, ccv, new Client(null, nicks[0]));
            cv.setLocationRelativeTo(ccv.getFrame());

            for (int i = 1; i < nicks.length; i++) {
                String nick = nicks[i];
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

    public Client clientFromFacebookUser(FacebookUser fu) {
        Client client = new Client();
        client.setNick(fu.name);
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(EmoticonsManger.toBufferedImage(fu.portrait.getImage()), "jpg", baos);
            client.setImage(baos.toByteArray());
        } catch (Exception ex) {
            log.error(ex);
        }
        client.setUid(fu.uid);
        return client;
    }

    public void initializeProperties() {
        //Crea proprietà
        Properties properties = Util.readProperties();

        if (properties == null) {
            properties = new Properties();
            ThemeManager.loadTheme(Util.getPath() + Util.VALUE_DEFAULT_THEME_FOLDER);
            properties.setProperty(Util.PROPERTY_THEME_FOLDER, Util.getPath() + Util.VALUE_DEFAULT_THEME_FOLDER);
            Util.writeProperties(properties);
            ccv.getFrame().repaint();
        } else {

            if (properties != null && !StringUtils.equals(properties.getProperty(Util.PROPERTY_IP), "")) {
                PersistentDataManager.setIp(properties.getProperty(Util.PROPERTY_IP));
            }

            try {
                if (properties != null && !StringUtils.equals(properties.getProperty(Util.PROPERTY_PORT), "")) {
                    PersistentDataManager.setPort(NumberUtils.toInt(properties.getProperty(Util.PROPERTY_PORT)));
                }
            } catch (NumberFormatException numberFormatException) {
                log.error(numberFormatException);
            }


            if (properties == null || properties.getProperty(Util.PROPERTY_THEME_FOLDER) == null || StringUtils.equals(properties.getProperty(Util.PROPERTY_THEME_FOLDER), "")) {
                ThemeManager.loadTheme(Util.getPath() + Util.VALUE_DEFAULT_THEME_FOLDER);
                properties.setProperty(Util.PROPERTY_THEME_FOLDER, Util.getPath() + Util.VALUE_DEFAULT_THEME_FOLDER);
                Util.writeProperties(properties);
                ccv.getFrame().repaint();
            }

        }
    }

    public void verifyUpdates() {
        try {
            ResourceMap resourceMap = ChatClientApp.getInstance().getContext().getResourceMap(ChatClientView.class);

            log.debug("resource map [" + resourceMap + "]");
            String version = resourceMap.getString("version");
            String url = resourceMap.getString("update.url");

            log.debug("version [" + version + "]");
            log.debug("url [" + url + "]");

            url = url.concat("?version=" + version);
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(url);

            log.debug("executing request " + httpget.getURI());
            // Create a response handler
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = httpclient.execute(httpget, responseHandler);

            log.debug("response [" + responseBody + "]");
            if (BooleanUtils.toBoolean(responseBody)) {
                String message = "<html>" +
                        "E' disponibile una nuova versione di Giff ,scaricala all'indirizzo " +
                        "<a href=\"www.luigiantonini.it\">www.luigiantonini.it</a>!<html> ";
                JOptionPane.showMessageDialog(ccv.getFrame(), message, "Aggiornamento disponibile", JOptionPane.INFORMATION_MESSAGE);

            }

            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
        } catch (ClientProtocolException ex) {
            log.error(ex);
        } catch (IOException ex) {
            log.error(ex);
        } catch (Exception ex) {
            log.error(ex);
        }

    }
    // <editor-fold defaultstate="collapsed" desc=" Getter and Setter ">

    public ArrayList<ChatWindow> getChatWindows() {
        return chatWindows;
    }

    public void setChatWindows(ArrayList<ChatWindow> chatWindows) {
        this.chatWindows = chatWindows;
    }

    public ArrayList<JFrame> getFileDialogs() {
        return fileDialogs;
    }

    public void setFileDialogs(ArrayList<JFrame> fileDialogs) {
        this.fileDialogs = fileDialogs;
    }
    // </editor-fold>
}
