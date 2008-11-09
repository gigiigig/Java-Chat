/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient.thread;

import chatclient.ChatClientView;
import chatclient.chatwindow.ChatWindow;
import chatclient.commons.Util;
import chatclient.forms.ReceiveFileDialog;
import chatclient.forms.SendFileDialog;
import chatcommons.Client;
import chatcommons.datamessage.MESSAGE;
import chatcommons.datamessage.MessageManger;
import chatclient.game.GameHome;
import chatclient.game.dama.DamaCanvas;
import emoticon.Emoticon;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.SwingWorker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static chatcommons.Commands.*;

/**
 *
 * @author Administrator
 */
public class ClientReader extends SwingWorker {

    private Socket so;
    private Log log = LogFactory.getLog(this.getClass());
    private ChatClientView ccv;
    private String type;
    public static String MAINREADER = "MAINREADER";
    public static String FILEREADER = "FILEREADER";

    public ClientReader(Socket so, ChatClientView ccv, String type) {
        this.ccv = ccv;
        this.so = so;
        this.type = type;
    }

    @Override
    protected Object doInBackground() throws Exception {

        //leggo l'input stream e lo sscrivo sulla chat riga per riga
        InputStream is = null;

        MESSAGE responseConn = MessageManger.createCommand(Command.CONNECT, null);
        MessageManger.addParameter(responseConn, "response", Command.OK);
        MessageManger.directWriteMessage(responseConn, ccv.getOutputStream());

//            ccv.getHelper().sendRequest("~OK\n");
        is = so.getInputStream();

        BufferedReader in = new BufferedReader(new InputStreamReader(is, Util.DEFAULTENCODING));
        String line = "";

        while ((line = in.readLine()) != null) {

            try {
                if (line.startsWith("<")) {

                    MESSAGE message = MessageManger.parseXML(line);

                    //non loggo gli xml di dati perchè so troppo lunghi
                    if (!message.getName().equals(Request.FILETRANSFER)) {
                        log.debug(ccv.getNick() + " " + type + " reder receive MESSAGE : " + MessageManger.messageToStringFormatted(message));
                    } else {
                        log.debug(ccv.getNick() + " " + type + " FILETRANSFER RECEIVED");
                    }

                    // <editor-fold defaultstate="collapsed" desc=" MESSAGE ">
                    if (message.getType().equals(MESSAGE)) {

                        String messageSt = message.getParameters().getParameter().get(0).getValue();
                        Font font = Font.decode(message.getParameters().getParameter().get(1).getValue());
                        Color color = Color.decode(message.getParameters().getParameter().get(2).getValue());

                        //prendo le emoticons dai contents 
                        List<Emoticon> emoticons = new LinkedList<Emoticon>();

                        try {
                            if (message.getContents() != null) {
                                List<MESSAGE.Contents.Content> contents = message.getContents().getContent();

                                for (MESSAGE.Contents.Content content : contents) {

                                    log.debug("received emoticon : " + content.getName());
                                    byte[] bs = content.getValue();
                                    //decomprimo l'immagine
                                    byte[] imageBytes = chatclient.commons.Util.decompress(bs);
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
                                if (!string.equals(ccv.getNick())) {
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
                                String fileurl = Util.readProperties().getProperty("downloadFolder") + "/" + fileName;
                                int packNum = Integer.parseInt(message.getParameters().getParameter().get(3).getValue());

                                File outputFile = new File(fileurl);

                                FileOutputStream fos = new FileOutputStream(outputFile, true);
                                byte[] bArr = message.getData();

                                int readed = Integer.parseInt(message.getParameters().getParameter().get(1).getValue());
                                log.debug(ccv.getNick() + "reader readed bytes : " + readed);
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

                                    if (!string.equals(ccv.getNick())) {
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

                            ArrayList<Client> clients = ccv.getClients();
                            ListIterator<Client> li = clients.listIterator();
                            boolean continua = true;
                            while (li.hasNext() && continua) {
                                Client elem = li.next();
                                if (elem.getNick().equals(clientToRemove)) {
                                    log.info("remove element : " + elem);
                                    clients.remove(elem);
                                    DefaultListModel listModel = (DefaultListModel) ccv.getClientsList().getModel();
                                    synchronized (listModel) {
                                        listModel.removeElement(elem.getNick());
                                        ccv.getClientsList().validate();
                                        ccv.getFrame().pack();
                                        continua = false;
                                    }
                                }
                            }

                        /*aggiungo un nuovo utente che si è connesso*/
                        } else if (message.getName().equals(Command.ADDUSER)) {
                            String clientToAdd = message.getParameters().getParameter().get(0).getValue();

                            ccv.getClients().add(new Client(null, clientToAdd));
//                    JList list = ccv.getClientsList();
                            int position = ccv.getClientsList().getModel().getSize() - 1;
                            DefaultListModel listModel = (DefaultListModel) ccv.getClientsList().getModel();
                            synchronized (listModel) {
//                                    listModel.add(position, clientToAdd);

//                                    listModel.remove(listModel.size() - 1);
                                listModel.addElement(clientToAdd);
//                                    listModel.addElement(null);

                                ccv.getClientsList().validate();
                                ccv.getFrame().validate();

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
            } catch (Exception e) {
                log.error(e);
            }
        }


        try {
            is.close();
        } catch (IOException ex) {
            log.error(ex);
        }
        return null;


    }
}


        

  

