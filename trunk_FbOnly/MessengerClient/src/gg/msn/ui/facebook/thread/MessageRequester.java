/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gg.msn.ui.facebook.thread;

import chatcommons.Client;
import emoticon.EmoticonsManger;
import gg.msn.facebook.core.FacebookManager;
import gg.msn.facebook.core.FacebookMessage;
import gg.msn.facebook.core.FacebookUserList;
import gg.msn.facebook.core.ResponseParser;
import gg.msn.ui.ChatClientView;
import gg.msn.ui.chatwindow.ChatWindow;
import gg.msn.ui.helper.ChatClientViewHelper;
import java.awt.Image;
import java.io.ByteArrayOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

    private void keepRequesting() throws Exception {

        FacebookManager.seq = fbManger.getSeq();

        while (FacebookManager.seq == -1) {
            fbManger.doLogin();
            fbManger.findChannel();
            FacebookManager.seq = fbManger.getSeq();
        }

        //go seq
        while (true) {
            //PostMessage("1190346972", "SEQ:"+seq);
//            int currentSeq = fbManger.getSeq();
//            log.debug("My seq:" + FacebookManager.seq + " | Current seq:" + currentSeq + '\n');
//            if (FacebookManager.seq > currentSeq) {
//                FacebookManager.seq = currentSeq;
//            }

//            while (FacebookManager.seq <= currentSeq) {
            while (true) {
                //get the old message between oldseq and seq
                String msgResponseBody = FacebookManager.facebookGetMethod(fbManger.getMessageRequestingUrl(Integer.parseInt(FacebookManager.channel), FacebookManager.seq));

                try {
                    FacebookMessage fm = ResponseParser.messageRequestResultParser(msgResponseBody, fbManger);
                    if (fm != null) {
                        ChatClientViewHelper helper = ccv.getHelper();

                        if (StringUtils.equals(fm.type, "typ")) {
                            log.debug("received typing message");

                            final ChatWindow chat = helper.getChatFromUid(fm.from + "");
                            log.debug("finded chat [" + chat + "]");
                            if (chat != null) {
                                Runnable runnable = new Runnable() {

                                    public void run() {
                                        try {
                                            JLabel nickLabel = chat.getNickLabel();
                                            String nickText = nickLabel.getText();
                                            nickLabel.setText("...");
                                            Thread.sleep(TYPING_TIME);
                                            nickLabel.setText(nickText);
                                        } catch (InterruptedException ex) {
                                            log.error(ex);
                                        }
                                    }
                                };
                                new Thread(runnable).start();
                            }

                        } else {
                            log.debug("helper [ " + helper + " ]");
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
                            log.debug("chatWith [ " + chatWith + " ]");
                            chatWith.writeMessage(fm.fromName, fm.text);
                            FacebookManager.incrementMessage();
                            log.debug("senquenz number [" + FacebookManager.seq + "]");
                        }
                    }
                } catch (Exception e) {
                    log.error(e);
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException interruptedException) {
                    }
                    fbManger.doLogin();
                    fbManger.findChannel();
                    FacebookManager.seq = fbManger.getSeq();
                }

            }
        }
    }
}
