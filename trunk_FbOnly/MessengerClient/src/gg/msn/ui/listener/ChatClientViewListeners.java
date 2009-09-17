/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gg.msn.ui.listener;

import gg.msn.ui.ChatClientApp;
import gg.msn.ui.ChatClientView;
import gg.msn.core.commons.Util;
import gg.msn.core.manager.PersistentDataManager;

import gg.msn.ui.theme.ThemeManager;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Properties;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Administrator
 */
public class ChatClientViewListeners {

    Log log = LogFactory.getLog(this.getClass());
    private ChatClientView ccv;

    public ChatClientViewListeners(ChatClientView ccv) {
        this.ccv = ccv;
    }

    //creator per le classi interne   
    public ClientsListRightClickListener getClientsListRightClickListener() {
        return new ClientsListRightClickListener(ccv);
    }

    public ClietsListOutClickListener getClientsListOutClickListener() {
        return new ClietsListOutClickListener(ccv);
    }

    /**
     * Contiene le operazioni da efffettuare in NICKTABLE in caso di rigthclick 
     */
    public class ClientsListRightClickListener extends MouseInputAdapter {

        public static final int POPUPMENU_IMAGE_LATE = 20;
        private Log log = LogFactory.getLog(ClietsListOutClickListener.class);
        private ChatClientView ccv;
        private JPopupMenu jpm;

        public ClientsListRightClickListener(ChatClientView ccv) {
            super();
//            this.ccv = (ChatClientView) ChatClientApp.getApplication().getMainView();
            this.ccv = ccv;
            //creo il popup menù
            jpm = new JPopupMenu();
            JMenuItem startChat = new JMenuItem();
            startChat.setAction(ChatClientApp.getApplication().getContext().getActionMap(ccv).get("addChatWithSelected"));
            startChat.setText("Chiama in chat");
            try {
                ImageIcon icon = new ImageIcon(getClass().getResource("/gg/msn/ui/panel/resources/chatIcon.png"));
                icon = new ImageIcon(icon.getImage().getScaledInstance(POPUPMENU_IMAGE_LATE, POPUPMENU_IMAGE_LATE, Image.SCALE_AREA_AVERAGING));
                startChat.setIcon(icon);
            } catch (Exception e) {
                log.error(e);
            }
            jpm.add(startChat);
            if (StringUtils.equals(ChatClientView.protocol, ChatClientView.GIGIMSN_PROTOCOL)) {
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

            /*caso click mouse destro*/
            if (SwingUtilities.isRightMouseButton(e)) {

                JList listPressed = ((JList) e.getComponent());
                int row = listPressed.locationToIndex(e.getPoint());

                log.info("locationToIndex : " + row);
                if (row != -1 && row < listPressed.getModel().getSize()) {
                    jpm.show(e.getComponent(), e.getX(), e.getY());
                    listPressed.setSelectedIndex(row);
                } else {
                    listPressed.removeSelectionInterval(0, listPressed.getComponentCount() - 1);
                }
                log.debug("selected row of the list : " + row);

                /*caso due click sinistro*/
            } else if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
                JList listPressed = ((JList) e.getComponent());
                int row = listPressed.locationToIndex(e.getPoint());

                if (row != -1 && row < listPressed.getModel().getSize()) {

                    //chiama in chat l'elemento selezionato
                    listPressed.setSelectedIndex(row);
                    ccv.getHelper().startChatWithSelected();
                }
            }
        }
    }

    /**
     * Contiene le operazioni da efffettuare in NICKTABLE in caso di click Fuori
     */
    public class ClietsListOutClickListener extends MouseInputAdapter {

        private ChatClientView ccv;

        public ClietsListOutClickListener(ChatClientView ccv) {
            super();
            this.ccv = ccv;

        }

        @Override
        public void mousePressed(MouseEvent e) {

            JList clientsList = ccv.getMainPanel().getClientsList();
//            clientsList.setEnabled(false);
            clientsList.getSelectionModel().clearSelection();

        }
    }
}

