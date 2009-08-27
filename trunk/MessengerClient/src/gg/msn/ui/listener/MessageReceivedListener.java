/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gg.msn.ui.listener;

import static chatcommons.Commands.*;
import chatcommons.Client;
import chatcommons.Commands.Command;
import chatcommons.Commands.Message;
import chatcommons.Commands.Request;
import chatcommons.datamessage.MESSAGE;
import chatcommons.datamessage.MESSAGE.Parameters.Parameter;
import emoticon.Emoticon;
import gg.msn.core.commons.Util;
import gg.msn.core.listener.AbstractMessageListener;
import gg.msn.core.manager.PersistentDataManager;
import gg.msn.ui.ChatClientView;
import gg.msn.ui.chatwindow.ChatWindow;
import gg.msn.ui.form.ReceiveFileDialog;
import gg.msn.ui.form.SendFileDialog;
import gg.msn.ui.game.GameHome;
import gg.msn.ui.game.dama.DamaCanvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Luigi
 */
public class MessageReceivedListener extends AbstractMessageListener {

    private ChatClientView ccv;
    private Log log = LogFactory.getLog(MessageReceivedListener.class);

    public MessageReceivedListener(ChatClientView ccv) {
        this.ccv = ccv;
    }

    @Override
    public void parseMessage(MESSAGE message) {
        // <editor-fold defaultstate="collapsed" desc=" MESSAGE ">
        if (message.getType().equals(MESSAGE)) {

            String messageSt = message.getParameters().getParameter().get(0).getValue();
            String fontSt = null;
            String colorSt = null;

            try {
                fontSt = message.getParameters().getParameter().get(1).getValue();
                colorSt = message.getParameters().getParameter().get(2).getValue();
            } catch (IndexOutOfBoundsException e) {
                log.debug("il messaggio non contiene dati per font e colore");
            }
            Font font = null;
            if (fontSt != null) {
                font = Font.decode(fontSt);
            } else {
                font = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
            }
            Color color = null;
            if (colorSt != null) {
                color = Color.decode(colorSt);
            } else {
                color = Color.BLACK;
            }
            //prendo le emoticons dai contents
            List<Emoticon> emoticons = new LinkedList<Emoticon>();

            try {
                if (message.getContents() != null) {
                    List<MESSAGE.Contents.Content> contents = message.getContents().getContent();

                    for (MESSAGE.Contents.Content content : contents) {

                        log.debug("received emoticon : " + content.getName());
                        byte[] bs = content.getValue();
                        //decomprimo l'immagine
                        byte[] imageBytes = gg.msn.core.commons.Util.decompress(bs);
                        log.trace("data[] : " + bs);
//                                        ByteArrayInputStream bais = new ByteArrayInputStream(bs);

//                                        BufferedImage bufferedImage = ImageIO.read(bais);
//                                        log.trace("BufferedImage : " + bufferedImage);
                        Emoticon emotion = new Emoticon();
                        emotion.setShortcut(content.getName());
                        emotion.setName(content.getName());
                        emotion.setImageIcon(new ImageIcon(imageBytes, ""));
                        emotion.setData(imageBytes);
                        log.trace("emoticon ImageIcon : " + emotion.getImageIcon());
                        emoticons.add(emotion);

                    }
                }
            } catch (Exception e) {
                log.warn(e);

            }

            if (message.getName().equals(Message.SINGLE)) {
                String sender = message.getSender();

                ChatWindow cv = ccv.getHelper().getChatWith(sender);
                cv.getReceivedEmoticons().addAll(emoticons);
                cv.writeMessage(sender, messageSt, font, color, emoticons);

            } else if (message.getName().equals(Message.CONFERENCE)) {
                String sender = message.getSender();

                //dalla lista di partecipanti alla conferenza
                //rcavo l'arrey di quelli che servono in questo client
                List<String> componentsOfConferenz = new LinkedList<String>();
                for (String string : message.getReceivers().getReceiver()) {
                    if (!string.equals(PersistentDataManager.getNick())) {
                        componentsOfConferenz.add(string);
                    }
                }
                componentsOfConferenz.add(sender);
                log.debug(componentsOfConferenz);

                ChatWindow cv = ccv.getHelper().getConferenceWith(componentsOfConferenz.toArray(new String[componentsOfConferenz.size()]));
                cv.getReceivedEmoticons().addAll(emoticons);
                cv.writeMessage(sender, messageSt, font, color, emoticons);

            }

// </editor-fold>

            // <editor-fold defaultstate="collapsed" desc=" REQUEST ">
        } else if (message.getType().equals(REQUEST)) {

            if (message.getName().equals(Request.FILETRANSFER)) {

                try {
                    String fileName = message.getParameters().getParameter().get(0).getValue();
                    String fileurl = Util.readProperties().getProperty("downloadFolder") + IOUtils.DIR_SEPARATOR + fileName;
                    int packNum = Integer.parseInt(message.getParameters().getParameter().get(3).getValue());

                    File outputFile = new File(fileurl);
                    FileOutputStream fos = new FileOutputStream(outputFile, true);
                    byte[] bArr = message.getData();

                    int readed = Integer.parseInt(message.getParameters().getParameter().get(1).getValue());
                    log.debug(PersistentDataManager.getNick() + "reader readed bytes : " + readed);
                    log.debug("write bytes to file : " + fileurl);
                    fos.write(bArr, 0, readed);

                    fos.close();

                    ReceiveFileDialog receiveFileDialog = ccv.getHelper().getReceiveFileDialog(fileName, message.getSender());
                    receiveFileDialog.setPackReceived(packNum);

                } catch (Exception e) {
                    log.error(e);
                }

            } else if (message.getName().equals(Request.FILEACCEPTREQUEST)) {

                try {
                    //richiesta di ricezione file
                    String sender = message.getSender();
                    String fileName = message.getParameters().getParameter().get(0).getValue();
                    long fileSize = Long.parseLong(message.getParameters().getParameter().get(1).getValue());

                    ReceiveFileDialog receiveFileDialog = new ReceiveFileDialog(ccv);
                    receiveFileDialog.setSender(sender);
                    receiveFileDialog.setFile(fileName);
                    receiveFileDialog.setFileSize(fileSize);

                    //grafica
                    String infoText = "<html>File : " + fileName + " <br>" + "Da : &nbsp&nbsp&nbsp&nbsp<b>" + sender + "</b>" + " &nbsp&nbsp&nbsp&nbsp Dimensione : " + (fileSize / 1024) + "k </html>";
                    receiveFileDialog.getInfoLabel().setText(infoText);
                    receiveFileDialog.setTitle("Ricezione - " + fileName);
                    receiveFileDialog.getStatusLabel().setText("");

                    receiveFileDialog.setLocationRelativeTo(ccv.getFrame());
                    receiveFileDialog.setVisible(true);
                    receiveFileDialog.toFront();

                } catch (Exception e) {
                    log.error(e);
                }

            } else if (message.getName().equals(Request.FILEACCEPTRESPONSE)) {

                try {
                    //richiesta di ricezione file
                    String sender = message.getSender();
                    log.debug("sender : " + sender);
                    String fileName = message.getParameters().getParameter().get(0).getValue();
                    log.debug("fileName : " + fileName);
                    long fileSize = Long.parseLong(message.getParameters().getParameter().get(1).getValue());
                    log.debug("fileSize : " + fileSize);
                    String response = message.getParameters().getParameter().get(2).getValue();
                    log.debug("response : " + response);
                    SendFileDialog sendFileDialog = ccv.getHelper().getSendFileDialog(fileName, sender);

                    if (response.equals(Command.OK)) {
                        sendFileDialog.getFileSender().setAccepted(true);
                    } else {
                        if (sendFileDialog.getFileSender().isAccepted() && sendFileDialog.getFileSender().isActive()) {
                            sendFileDialog.transferStoppedFromReceiver();
                        } else if (!sendFileDialog.getFileSender().isAccepted()) {
                            sendFileDialog.transferRefusedFromReceiver();
                        }
                    }

                } catch (Exception e) {
                    log.error(e);
                }

            } else if (message.getName().equals(Request.ADDTOCONFERENCE)) {

                try {
                    //il priomo elemento è il nick da aggiungere gli alri i membri della conferenza
                    String nickToAdd = message.getParameters().getParameter().get(0).getValue();

                    //dalla lista di partecipanti alla conferenza
                    //rcavo l'arrey di quelli che servono in questo client
                    List<String> componentsOfConferenz = new LinkedList<String>();
                    for (String string : message.getReceivers().getReceiver()) {

                        if (!string.equals(PersistentDataManager.getNick())) {
                            componentsOfConferenz.add(string);
                        }
                    }
                    componentsOfConferenz.add(message.getSender());

                    ChatWindow cw = ccv.getHelper().getConferenceWith(componentsOfConferenz.toArray(new String[componentsOfConferenz.size()]));
                    cw.addClientToChat(nickToAdd);

                } catch (Exception e) {
                    log.error(e);
                }

            } else if (message.getName().equals(Request.STARTGAME)) {
                log.debug("start dama game");
                try {

                    GameHome home = new GameHome(ccv);
                    home.setLocationRelativeTo(ccv.getFrame());
                    home.setVisible(true);
                    home.startDamaClient(message.getSender());

                } catch (Exception e) {
                    log.error(e);
                }

            } else if (message.getName().equals(Request.DAMAPOSITION)) {
                try {
                    log.debug("position for dama");

                    int posX = (int) Double.parseDouble(message.getParameters().getParameter().get(0).getValue());
                    log.debug("posX = " + posX);
                    int posY = (int) Double.parseDouble(message.getParameters().getParameter().get(1).getValue());
                    log.debug("posY = " + posY);

                    DamaCanvas canvas = (DamaCanvas) ccv.getHelper().getGameWith(message.getSender(), DamaCanvas.class);
                    log.debug(canvas);
                    canvas.setClick(new Point(posX, posY));
                    canvas.setMyClick(false);
                    canvas.repaint();

                } catch (Exception e) {
                    //potrebbe da nullpointer
                    log.error(e);
                }
            }

// </editor-fold>

            // <editor-fold defaultstate="collapsed" desc=" COMMAND ">
        } else if (message.getType().equals(COMMAND)) {

            if (message.getName().equals(Command.REMOVEUSER)) {

                String clientToRemove = message.getParameters().getParameter().get(0).getValue();

                Hashtable<String, Client> clients = PersistentDataManager.getClients();
                Iterator<Client> li = clients.values().iterator();
                boolean continua = true;
                while (li.hasNext() && continua) {
                    Client elem = li.next();
                    if (elem.getNick().equals(clientToRemove)) {
                        log.info("remove element : " + elem);
                        clients.remove(elem.getNick());
                        DefaultListModel listModel = (DefaultListModel) ccv.getMainPanel().getClientsList().getModel();
                        synchronized (listModel) {
                            listModel.removeElement(elem.getNick());
                            ccv.getMainPanel().getClientsList().validate();
                            ccv.getFrame().pack();
                            continua = false;
                        }
                    }
                }

                /*aggiungo un nuovo utente che si è connesso*/
            } else if (message.getName().equals(Command.ADDUSER)) {
                //i paramtri contengono i clints da aggiungere
                List<Parameter> parameters = message.getParameters().getParameter();

                log.debug("nick da aggiungere [" + parameters.size() + "]");

//                            int position = ccv.getClientsList().getModel().getSize() - 1;
                DefaultListModel listModel = (DefaultListModel) ccv.getMainPanel().getClientsList().getModel();
                synchronized (listModel) {
                    for (Parameter parameter : parameters) {
                        PersistentDataManager.getClients().put(parameter.getValue(), new Client(null, parameter.getValue()));
                        listModel.addElement(parameter.getValue());
                        log.debug("aggiunto  client [" + parameter.getValue() + "]");
                    }

                    ccv.getMainPanel().validate();

                    log.debug("utenti connessi : ");
                    for (Object elem : listModel.toArray()) {

                        try {
                            log.debug("utente : " + elem.toString());
                        } catch (Exception e) {
                            log.debug("elemento NULLO");
                        }
                    }
                }
            }
        }
        // </editor-fold>
    }

    @Override
    public String getNick() {
        return PersistentDataManager.getNick();
    }

    @Override
    public OutputStream getOutputStream() {
        return PersistentDataManager.getOutputStream();
    }
}

