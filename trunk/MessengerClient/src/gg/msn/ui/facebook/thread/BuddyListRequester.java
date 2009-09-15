/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gg.msn.ui.facebook.thread;

import gg.msn.facebook.core.FacebookManager;
import gg.msn.ui.ChatClientView;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Luigi
 */
public class BuddyListRequester implements Runnable {

    private static Log log = LogFactory.getLog(BuddyListRequester.class);
    private ChatClientView ccv;

    public BuddyListRequester(ChatClientView ccv) {
        this.ccv = ccv;
    }

    public void run() {
        log.debug("Keep requesting buddylist...");
        while (true) {
            log.debug("refresh buddies");
            try {
                FacebookManager.getBuddyList();
            } catch (Exception e) {
                log.error(e);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                log.error(ex);
            }

            ccv.getMainPanel().updateListWithFACEBOOKContacts();

            // it's said that the buddy list is updated every 3 minutes at the server end.
            // we refresh the buddy list every 1.5 minutes
            try {
                Thread.sleep(60 * 1000);
            } catch (InterruptedException e) {
                log.error(e);
            }
        }
    }
}
