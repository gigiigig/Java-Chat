/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient.thread;

import chatclient.commons.Util;
import chatclient.servlet.SessionReaderServlet;
import chatcommons.datamessage.MESSAGE;
import chatcommons.datamessage.MessageManger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static chatcommons.Commands.*;

/**
 *
 * @author Administrator
 */
public class ClientReaders implements Runnable {

    private Socket so;
    private Log log = LogFactory.getLog(this.getClass());
    private String type;
    private HttpSession session;
    private String nick;
    public static String MAINREADER = "MAINREADER";
    public static String FILEREADER = "FILEREADER";
    public static int CLOSING_OFFSET = 20000;
    boolean status;

    public ClientReaders(Socket so, HttpSession session, String type) {
        this.session = session;
        this.so = so;
        this.type = type;
        this.status = true;

        nick = (String) session.getAttribute("nick");
    }

    @Override
    public void run() {
        try {
            //leggo l'input stream e lo sscrivo sulla chat riga per riga
            log.debug("start");
            InputStream is = null;
            MESSAGE responseConn = MessageManger.createCommand(Command.CONNECT, null);
            MessageManger.addParameter(responseConn, "response", Command.OK);
            MessageManger.directWriteMessage(responseConn, so.getOutputStream());
//            ccv.getHelper().sendRequest("~OK\n");
            log.debug("inviato l'OK mi metto in attesa");
            is = so.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is, Util.DEFAULTENCODING));
            String line = "";

            new Thread(new Runnable() {

                public void run() {
                    log.debug("inizio verifica se l'utente è connesso nel browser");
                    while (true) {
                        Date current = new Date();
                        Date start = (Date) session.getAttribute("lastStart");
                        long elapsedTime = current.getTime() - start.getTime();
                        int waitTime = SessionReaderServlet.CONNESSION_CLOSING_TIME + CLOSING_OFFSET;
                        log.debug("tempo passato dall'ultima richiesta [" + elapsedTime + "]");
                        log.debug("tempo da attendere [" + waitTime + "]");
                        if (elapsedTime >= waitTime) {
                            status = false;
                            MESSAGE message = MessageManger.createCommand(Command.DISCONNECT, null);
                            try {
                                MessageManger.directWriteMessage(message, so.getOutputStream());
                            } catch (IOException ex) {
                                log.error(ex);                            }
                            break;
                        }
                        try {
                            Thread.sleep(SessionReaderServlet.CONNESSION_CLOSING_TIME);
                        } catch (InterruptedException interruptedException) {
                        }
                    }
                }
                
            }).start();

            while ((line = in.readLine()) != null && status) {

                try {
                    if (line.startsWith("<")) {
                        MESSAGE message = MessageManger.parseXML(line);
//
                        //non loggo gli xml di dati perchè so troppo lunghi
                        if (!message.getName().equals(Request.FILETRANSFER)) {
                            log.debug(nick + " " + type + " reder receive MESSAGE : " + MessageManger.messageToStringFormatted(message));
                        } else {
                            log.debug(nick + " " + type + " FILETRANSFER RECEIVED");
                        }
                        //scrivo il messaggio cosi com'è nella session
                        ConcurrentLinkedQueue<String> messagesQueue = (ConcurrentLinkedQueue<String>) session.getAttribute("messages");
//                        log.debug("messaggi nella session PRIMA[" + messagesQueue.size() + "]");
                        messagesQueue.add(MessageManger.messageToString(message));
                        messagesQueue = (ConcurrentLinkedQueue<String>) session.getAttribute("messages");
//                        log.debug("messaggi nella session DOPO[" + messagesQueue.size() + "]");
                        log.debug("scritto il  nuovo messaggio nella session");
                        //
                        //                    // <editor-fold defaultstate="collapsed" desc=" MESSAGE ">
                        //                    if (message.getType().equals(MESSAGE)) {
                        //
                        //                        String messageSt = message.getParameters().getParameter().get(0).getValue();
                        //                        Font font = Font.decode(message.getParameters().getParameter().get(1).getValue());
                        //                        Color color = Color.decode(message.getParameters().getParameter().get(2).getValue());
                        //
                        //                        //prendo le emoticons dai contents
                        ////                        List<Emoticon> emoticons = new LinkedList<Emoticon>();
                        //
                        ////                        try {
                        ////                            if (message.getContents() != null) {
                        ////                                List<MESSAGE.Contents.Content> contents = message.getContents().getContent();
                        ////
                        ////                                for (MESSAGE.Contents.Content content : contents) {
                        ////
                        ////                                    log.debug("received emoticon : " + content.getName());
                        ////                                    byte[] bs = content.getValue();
                        ////                                    //decomprimo l'immagine
                        ////                                    byte[] imageBytes = chatclient.commons.Util.decompress(bs);
                        ////                                    log.trace("data[] : " + bs);
                        ////                                        ByteArrayInputStream bais = new ByteArrayInputStream(bs);
                        //
                        ////                                        BufferedImage bufferedImage = ImageIO.read(bais);
                        ////                                        log.trace("BufferedImage : " + bufferedImage);
                        ////                                    Emoticon emotion = new Emoticon();
                        ////                                    emotion.setShortcut(content.getName());
                        ////                                    emotion.setName(content.getName());
                        ////                                    emotion.setImageIcon(new ImageIcon(imageBytes, ""));
                        ////                                    emotion.setData(imageBytes);
                        ////                                    log.trace("emoticon ImageIcon : " + emotion.getImageIcon());
                        ////                                    emoticons.add(emotion);
                        //
                        ////                                }
                        ////                            }
                        ////                        } catch (Exception e) {
                        ////                            log.warn(e);
                        ////
                        ////                        }
                        //
                        //
                        //
                        //                        if (message.getName().equals(Message.SINGLE)) {
                        //                            String sender = message.getSender();
                        //
                        ////                            ChatWindow cv = ccv.getHelper().getChatWith(sender);
                        ////                            cv.getReceivedEmoticons().addAll(emoticons);
                        ////                            cv.writeMessage(sender, messageSt, font, color, emoticons);
                        //
                        //                        } else if (message.getName().equals(Message.CONFERENCE)) {
                        //                            String sender = message.getSender();
                        //
                        //                            //dalla lista di partecipanti alla conferenza
                        //                            //rcavo l'arrey di quelli che servono in questo client
                        //                            List<String> componentsOfConferenz = new LinkedList<String>();
                        //                            for (String string : message.getReceivers().getReceiver()) {
                        //                                if (!string.equals(nick)) {
                        //                                    componentsOfConferenz.add(string);
                        //                                }
                        //                            }
                        //                            componentsOfConferenz.add(sender);
                        //                            log.debug(componentsOfConferenz);
                        ////
                        ////                            ChatWindow cv = ccv.getHelper().getConferenceWith(componentsOfConferenz.toArray(new String[componentsOfConferenz.size()]));
                        ////                            cv.getReceivedEmoticons().addAll(emoticons);
                        ////                            cv.writeMessage(sender, messageSt, font, color, emoticons);
                        //
                        //                        }
                        //
                        //// </editor-fold>
                        //
                        //                    // <editor-fold defaultstate="collapsed" desc=" REQUEST ">
                        //                    } else if (message.getType().equals(REQUEST)) {
                        //
                        //                        if (message.getName().equals(Request.ADDTOCONFERENCE)) {
                        //
                        //                            try {
                        //                                //il priomo elemento è il nick da aggiungere gli alri i membri della conferenza
                        //                                String nickToAdd = message.getParameters().getParameter().get(0).getValue();
                        //
                        //                                //dalla lista di partecipanti alla conferenza
                        //                                //rcavo l'arrey di quelli che servono in questo client
                        //                                List<String> componentsOfConferenz = new LinkedList<String>();
                        //                                for (String string : message.getReceivers().getReceiver()) {
                        //
                        //                                    if (!string.equals(nick)) {
                        //                                        componentsOfConferenz.add(string);
                        //                                    }
                        //                                }
                        //                                componentsOfConferenz.add(message.getSender());
                        //
                        ////                                ChatWindow cw = ccv.getHelper().getConferenceWith(componentsOfConferenz.toArray(new String[componentsOfConferenz.size()]));
                        ////                                cw.addClientToChat(nickToAdd);
                        //
                        //                            } catch (Exception e) {
                        //                                log.error(e);
                        //                            }
                        //
                        //                        }
                        //
                        //// </editor-fold>
                        //
                        //                    // <editor-fold defaultstate="collapsed" desc=" COMMAND ">
                        //                    } else if (message.getType().equals(COMMAND)) {
                        //
                        //                        if (message.getName().equals(Command.REMOVEUSER)) {
                        //
                        //                            String clientToRemove = message.getParameters().getParameter().get(0).getValue();
                        //
                        //                            ArrayList<Client> clients = null;
                        //                            ListIterator<Client> li = clients.listIterator();
                        //                            boolean continua = true;
                        //                            while (li.hasNext() && continua) {
                        //                                Client elem = li.next();
                        //                                if (elem.getNick().equals(clientToRemove)) {
                        //                                    log.info("remove element : " + elem);
                        //                                    clients.remove(elem);
                        ////                                    DefaultListModel listModel = (DefaultListModel) ccv.getClientsList().getModel();
                        ////                                    synchronized (listModel) {
                        ////                                        listModel.removeElement(elem.getNick());
                        //////                                        ccv.getClientsList().validate();
                        //////                                        ccv.getFrame().pack();
                        ////                                        continua = false;
                        ////                                    }
                        //                                }
                        //                            }
                        //
                        //                        /*aggiungo un nuovo utente che si è connesso*/
                        //                        } else if (message.getName().equals(Command.ADDUSER)) {
                        //                            //i paramtri contengono i clints da aggiungere
                        //                            List<Parameter> parameters = message.getParameters().getParameter();
                        //
                        //                            log.debug("nick da aggiungere [" + parameters.size() + "]");
                        //
                        ////                            int position = ccv.getClientsList().getModel().getSize() - 1;
                        ////                            DefaultListModel listModel = (DefaultListModel) ccv.getClientsList().getModel();
                        ////                            synchronized (listModel) {
                        ////
                        ////                                for (Parameter parameter : parameters) {
                        ////                                    ccv.getClients().add(new Client(null, parameter.getValue()));
                        ////                                    listModel.addElement(parameter.getValue());
                        ////                                    log.debug("aggiunto  client ["+parameter.getValue()+"]");
                        ////                                }
                        ////
                        ////                                ccv.getClientsList().validate();
                        ////                                ccv.getFrame().validate();
                        ////
                        ////                                log.debug("utenti connessi : ");
                        ////                                for (Object elem : listModel.toArray()) {
                        ////
                        ////                                    try {
                        ////                                        log.debug("utente : " + elem.toString());
                        ////                                    } catch (Exception e) {
                        ////                                        log.debug("elemento NULLO");
                        ////                                    }
                        ////                                }
                        ////                            }
                        //                        }
                        //                    }
                        //                // </editor-fold>
//
//
                    }
                } catch (Exception e) {
                    log.error(e);
                }
            }
            try {
                is.close();
            } catch (IOException ex) {
                log.error(ex);
            }
            return;
        } catch (IOException ex) {
            log.error(ex);
        }


    }
}



        

  

