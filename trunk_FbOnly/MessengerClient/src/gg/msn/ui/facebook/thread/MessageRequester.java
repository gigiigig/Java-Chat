/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gg.msn.ui.facebook.thread;

import chatcommons.Client;
import emoticon.EmoticonsManger;
import gg.msn.facebook.core.ErrorCode;
import gg.msn.facebook.core.FacebookManager;
import gg.msn.facebook.core.FacebookMessage;
import gg.msn.facebook.core.FacebookUserList;
import gg.msn.facebook.core.ResponseParser;
import gg.msn.ui.ChatClientView;
import gg.msn.ui.chatwindow.ChatWindow;
import gg.msn.ui.helper.ChatClientViewHelper;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

/**
 *
 * @author Luigi
 */
public class MessageRequester implements Runnable {

    private static Log log = LogFactory.getLog(MessageRequester.class);
    private ChatClientView ccv;
    private FacebookManager fbManger;
    private String email;
    private String pass;
    public static final int TYPING_TIME = 3000;
    private static volatile boolean online;

    public MessageRequester(ChatClientView ccv, FacebookManager facebookManager, String email, String pass) {
        this.ccv = ccv;
        this.fbManger = facebookManager;
        this.email = email;
        this.pass = pass;
    }

    public void run() {
        log.debug("Keep requesting...");
        while (true) {
            try {
                keepRequesting();
            } catch (Exception e) {
                log.error(e);
            }
        }
    }

    private void keepRequesting(){

        FacebookManager.seq = fbManger.getSeq();

        while (FacebookManager.seq == -1) {
            // fbManger.doLogin();
            fbManger.findChannel();
            FacebookManager.seq = fbManger.getSeq();
        }

        //go seq
        //  while (true) {
        //PostMessage("1190346972", "SEQ:"+seq);
//            int currentSeq = fbManger.getSeq();
//            log.debug("My seq:" + FacebookManager.seq + " | Current seq:" + currentSeq + '\n');
//            if (FacebookManager.seq > currentSeq) {
//                FacebookManager.seq = currentSeq;
//            }

//            while (FacebookManager.seq <= currentSeq) {
        log.debug("online  [ " + online+" ] ");
        while (true) {

            if (online) {
                //get the old message between oldseq and seq
                String msgResponseBody = FacebookManager.facebookGetMethod(fbManger.getMessageRequestingUrl(Integer.parseInt(FacebookManager.channel), FacebookManager.seq));

                try {
                    List<FacebookMessage> facebookMessages = ResponseParser.messageRequestResultParser(msgResponseBody, fbManger);

                    for (FacebookMessage fm : facebookMessages) {

                        if (fm != null) {
                            ChatClientViewHelper helper = ccv.getHelper();

                            log.debug("message type  [ " + fm.type + " ] ");
                            if (StringUtils.equals(fm.type, "typ")) {
                                log.debug("received typing message");

                                final ChatWindow chat = helper.getChatFromUid(fm.from + "");
                                log.debug("finded chat [" + chat + "]");
                                if (chat != null) {
                                    Runnable runnable = new Runnable() {

                                        public void run() {
                                            try {
                                                JLabel nickLabel = chat.getNickLabel();
                                                synchronized (nickLabel) {
                                                    String nickText = nickLabel.getText();
                                                    nickLabel.setText("...");
                                                    Thread.sleep(TYPING_TIME);
                                                    nickLabel.setText(nickText);
                                                }
                                            } catch (InterruptedException ex) {
                                                log.error(ex);
                                            }
                                        }
                                    };
                                    new Thread(runnable).start();
                                }

                            } else if (StringUtils.equals(fm.type, "msg")) {
                                log.debug("writw message from  [ " + fm.fromName + " ] ");
                                Client client = new Client(fm.fromName);
                                client.setUid(fm.from + "");
                                try {
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    ImageIcon imageIcon = FacebookUserList.buddies.get(client.getUid()).portrait;
                                    ImageIO.write(EmoticonsManger.toBufferedImage(imageIcon.getImage()), "jpg", baos);
                                    client.setImage(baos.toByteArray());
                                } catch (Exception ex) {
                                    log.error(ex);
                                }
                                ChatWindow chatWith = helper.getChatWith(client);
                                chatWith.writeMessage(fm.fromName, fm.text);
                                log.debug("senquenz number [" + FacebookManager.seq + "]");
                            } else if (StringUtils.equals(fm.type, "focus_chat") || StringUtils.equals(fm.type, "focus_chat")) {
                                log.debug("receyed  [ " + fm.type + " ] message ");
                            }
                        }
                    }
                } catch (Exception e) {
                    log.error(e);
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException interruptedException) {
                    }
                    int loginErrorCode = fbManger.doLogin();
                    if (loginErrorCode == ErrorCode.Error_Global_NoError) {
                        if (fbManger.doParseHomePage() == ErrorCode.Error_Global_NoError) {
                            fbManger.findChannel();

                            //resetto il contatopre solo se i messggi sono stati rinumerati;
                            //altrimenti continuo la sequnza e recupero i messaggi persi
                            if (fbManger.getSeq() < FacebookManager.seq) {


                                FacebookManager.seq = fbManger.getSeq();
                                final ArrayList<ChatWindow> chatWindows = ccv.getHelper().getChatWindows();
                                for (ChatWindow chatWindow : chatWindows) {
                                    log.debug("");
                                    Client client = chatWindow.getClients().get(0);
                                    log.debug("retrive messages for  [ " + client + " ] ");
                                    String uid = client.getUid();
                                    if (uid != null) {
                                        JSONObject[] history = fbManger.getHistory(uid);
                                        log.debug("messages for client  [ " + history.length + " ] ");
                                        HashSet<String> msgIDCollection = FacebookManager.msgIDCollection;
                                        for (int i = history.length - 1; i == 0; i--) {
                                        }
                                    }
                                }

                            }
                        }
                    }
//                FacebookManager.seq = fbManger.getSeq();
                }
            }else{
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException interruptedException) {
                }
            }
        }
    }

    public static boolean isOnline() {
        return online;
    }

    public static synchronized void setOnline(boolean online) {
        MessageRequester.online = online;
    }
//    }
}
