/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gg.msn.ui.facebook.thread;

import chatcommons.Client;
import facebookchat.common.FacebookManager;
import facebookchat.common.FacebookMessage;
import facebookchat.common.ResponseParser;
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
    public MessageRequester(ChatClientView ccv,FacebookManager facebookManager) {
        this.ccv = ccv;
        this.fbManger = facebookManager;
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

        //go seq
        while (true) {
            //PostMessage("1190346972", "SEQ:"+seq);
            int currentSeq = fbManger.getSeq();
            log.debug("My seq:" + FacebookManager.seq + " | Current seq:" + currentSeq + '\n');
            if (FacebookManager.seq > currentSeq) {
                FacebookManager.seq = currentSeq;
            }

            while (FacebookManager.seq <= currentSeq) {
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
                    }
                } catch (Exception e) {
                    log.error(e);
                }
                FacebookManager.seq++;
            }
        }
    }
}
