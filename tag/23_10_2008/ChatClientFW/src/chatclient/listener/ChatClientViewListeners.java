/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient.listener;

import chatclient.ChatClientApp;
import chatclient.ChatClientView;
import chatclient.commons.Util;
import chatclient.forms.OptionsDialog;

import chatclient.forms.SendFileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Properties;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;
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

    public MainViewListerner getMainViewListerner() {
        return new MainViewListerner(ccv);
    }

    public GeneralButtonListener generalButtonListener() {
        return new GeneralButtonListener();
    }

    /**
     * Contiene le operazioni da efffettuare in NICKTABLE in caso di rigthclick 
     */
    public class ClientsListRightClickListener extends MouseInputAdapter {

        private Log log = LogFactory.getLog(this.getClass());
        private ChatClientView ccv;
        private JPopupMenu jpm;

        public ClientsListRightClickListener(ChatClientView ccv) {
            super();
            this.ccv = ccv;

            //creo il popup menù
            jpm = new JPopupMenu();
            JMenuItem startChat = new JMenuItem();
            startChat.setAction(ChatClientApp.getApplication().getContext().getActionMap(ccv).get("addChatWithSelected"));
            startChat.setText("Chiama in chat");
            JMenuItem startDama = new JMenuItem();
            startDama.setAction(new AbstractAction() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    ChatClientView ccv = (ChatClientView) (ChatClientApp.getApplication().getMainView());
                    ccv.getHelper().startGameWithSelected();
                }
            });
            startDama.setText("Gioca a Dama");
            JMenuItem startSendFile = new JMenuItem();
            startSendFile.setAction(new AbstractAction() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    ChatClientView ccv = (ChatClientView) (ChatClientApp.getApplication().getMainView());
                    String nickSelected = (String) ccv.getClientsList().getSelectedValue();

                    SendFileDialog sendFileDialog = new SendFileDialog(nickSelected, ccv);
                    sendFileDialog.setLocationRelativeTo(ccv.getFrame());
                    sendFileDialog.setVisible(true);
                }
            });
            startSendFile.setText("Invia un file");
            jpm.add(startChat);
            JMenu games = new JMenu("Giochi");
            games.add(startDama);
            jpm.add(games);
            jpm.add(startSendFile);


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
                    ccv.addChatWithSelected();
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

            JList clientsList = ccv.getClientsList();
//            clientsList.setEnabled(false);
            clientsList.getSelectionModel().clearSelection();

        }
    }

    /**
     * Operazioni per gli eventi dell'applicazione
     */
    public class MainViewListerner extends WindowAdapter {

        private ChatClientView ccv;

        public MainViewListerner(ChatClientView ccv) {
            super();
            this.ccv = ccv;
        }

        @Override
        public void windowClosing(WindowEvent arg0) {
            super.windowClosing(arg0);
            log.info("window closing");
            if (ccv.getOutputStream() != null) {
                log.info("nell'if");
                ccv.disconnetti();
            }
        }

        @Override
        public void windowOpened(WindowEvent arg0) {
            super.windowOpened(arg0);
            //Crea proprietà
            Properties properties = Util.readProperties();

            if (properties == null) {
                try {
                    OptionsDialog optionsFrame = new OptionsDialog(ccv.getFrame(), true);
                    optionsFrame.setLocationRelativeTo(ccv.getFrame());
                    optionsFrame.getPortaText().setText("3434");
                    optionsFrame.getIpText().setText("localhost");
                    optionsFrame.getThemeFolderText().setText(Util.getPath() + Util.DEFAULT_THEME_FOLDER);
                    optionsFrame.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
                    optionsFrame.getCancel().setVisible(false);
                    optionsFrame.setVisible(true);
                } catch (Exception e) {
                    log.error(e);
                }
            }
            if (properties != null && !properties.getProperty("nick").equals("")) {
                ccv.getNickText().setText(properties.getProperty("nick"));
            }
            if (properties != null && !properties.getProperty(Util.IP).equals("")) {
                ccv.setIp(properties.getProperty(Util.IP));
            }

            try {
                if (properties != null && !properties.getProperty(Util.PORT).equals("")) {
                    ccv.setPort(Integer.parseInt(properties.getProperty(Util.PORT)));
                }
            } catch (NumberFormatException numberFormatException) {
                log.error(numberFormatException);
            }
        }
    }

    public class GeneralButtonListener extends MouseInputAdapter {

        public GeneralButtonListener() {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            JButton button = (JButton) e.getComponent();
            button.setContentAreaFilled(true);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            JButton button = (JButton) e.getComponent();
            button.setContentAreaFilled(false);
        }
    }
}

