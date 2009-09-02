/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gg.msn.ui.facebook.thread;

import chatcommons.Client;
import gg.msn.core.facebook.common.FacebookManager;
import gg.msn.core.facebook.common.FacebookMessage;
import gg.msn.core.facebook.common.ResponseParser;
import gg.msn.ui.ChatClientView;
import gg.msn.ui.chatwindow.ChatWindow;
import gg.msn.ui.helper.ChatClientViewHelper;
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
            fbManger.doLogin(email, pass);
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
                    FacebookMessage fm = ResponseParser.messageRequestResultParser(msgResponseBody);
                    if (fm != null) {
                        ChatClientViewHelper helper = ccv.getHelper();
                        log.debug("helper [ " + helper + " ]");
                        Client client = new Client(fm.fromName);
                        client.setUid(fm.from + "");
                        ChatWindow chatWith = helper.getChatWith(client);
                        log.debug("chatWith [ " + chatWith + " ]");
                        chatWith.writeMessage(fm.fromName, fm.text);
                        FacebookManager.seq++;
                    }
                } catch (Exception e) {
                    log.error(e);
                    fbManger.findChannel();
                    //FacebookManager.seq = fbManger.getSeq();
                }
                
            }
        }
    }
}
