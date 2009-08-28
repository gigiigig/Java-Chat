/*
 * ChatClientView.java
 */
package gg.msn.ui;

import emoticon.EmoticonsManageFrame;
import gg.msn.core.manager.PersistentDataManager;
import gg.msn.ui.listener.ChatClientViewListeners;
import gg.msn.ui.form.OptionsDialog;
import gg.msn.ui.helper.ChatClientViewHelper;
import gg.msn.ui.panel.MainPanel;
import gg.msn.ui.theme.ThemeManager;
import java.awt.AWTException;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.RenderingHints;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.jdesktop.application.Action;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.application.Application;

/**
 * The application's main frame.
 */
public class ChatClientView extends FrameView {

    /* varibili generali */
    private Log log = LogFactory.getLog(ChatClientView.class);
    private ChatClientViewHelper helper;// = new ChatClientViewHelper();
    private MainPanel mainPanel;
    public static String protocol;
    public static final String GIGIMSN_PROTOCOL = "ggmsn";
    public static final String FACEBOOK_PROTOCOL = "facebook";

    // <editor-fold defaultstate="collapsed" desc="Costruttore">                          
    public ChatClientView(SingleFrameApplication app) {
        super(app);

        // <editor-fold defaultstate="collapsed" desc="Window Icon"> 
        try {
            getFrame().setIconImage(getResourceMap().getImageIcon("trayIcon").getImage());
        } catch (Exception e) {
            log.warn(e);
        }
        // </editor-fold>

        log.info("init");

        initComponents();
        //TEST
//        ImageIcon icon = new ImageIcon(getResourceMap().getIcon("mainBackground").getImage());
//        log.info("icon " + icon.getDescription() + " loaded!!! ");

        mainPanel = new MainPanel(this);
        helper = new ChatClientViewHelper(this);
        statusPanel.setVisible(false);

        //<editor-fold defaultstate="collapsed" desc="System tray control">             
        //creo una system tray    

        if (SystemTray.isSupported()) {
            try {
                SystemTray systemTray = SystemTray.getSystemTray();
                TrayIcon trayIcon = new TrayIcon(getResourceMap().getImageIcon("trayIcon").getImage());
//            TrayIcon trayIcon = new TrayIcon(new ImageIcon("/entman/resources/tray.png").getImage());
                trayIcon.addMouseListener(new MouseAdapter() {

                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (e.getClickCount() == 2) {
                            getFrame().setVisible(true);
//                        getFrame().setState(JFrame.n;
                            getFrame().setExtendedState(JFrame.NORMAL);
                        }
                    }
                });
                systemTray.add(trayIcon);
                trayIcon.setToolTip("Gigi Messenger - non connesso");
                trayIcon.setImageAutoSize(true);

                getFrame().addWindowListener(new WindowAdapter() {

                    @Override
                    public void windowIconified(WindowEvent e) {
//                super.windowIconified(e);
                        getFrame().setVisible(false);
                    }
                });

                PopupMenu trayPopupMenu = new PopupMenu();
                MenuItem open = new MenuItem("Apri Gigi Messenger");
                open.addActionListener(new AbstractAction() {

                    public void actionPerformed(ActionEvent e) {
                        ChatClientApp.getApplication().getMainFrame().setVisible(true);
                        getFrame().setVisible(true);
                        getFrame().setState(JFrame.NORMAL);
                    }
                });

                MenuItem close = new MenuItem("Chiudi Gigi Messenger");
                close.addActionListener(new AbstractAction() {

                    public void actionPerformed(ActionEvent e) {
                        if (PersistentDataManager.getOutputStream() != null) {
                            log.info("nell'if");
                            helper.disconnetti();
                        }
                        ChatClientApp.getApplication().exit();

                    }
                });


                trayPopupMenu.add(open);
                trayPopupMenu.add(close);
                trayIcon.setPopupMenu(trayPopupMenu);

                tray = trayIcon;

            } catch (AWTException ex) {
                log.debug(ex);
            }
        }

        //</editor-fold> 

        /*ipostazioni grafiche iniziali*/

        //imposto il cursore nel inputText
        nickText.selectAll();

        //setto visilbile il panello di login
        helper.showLoginPanel();

        /*aggiungo agli oggetti i vari componenti personalizzati*/

        //add nickText ENTER keystroke
        nickText.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "Enter");
        javax.swing.ActionMap actionMap = Application.getInstance(ChatClientApp.class).getContext().getActionMap(ChatClientView.class, this);
        nickText.getActionMap().put("Enter", actionMap.get("connetti"));

        //aggiungo tutti i listeners dei vari componenti
        ChatClientViewListeners listeners = new ChatClientViewListeners(this);

        //this frame
        this.getFrame().addWindowListener(listeners.getMainViewListerner());
    }
    // </editor-fold>  
    /*Show metods for Frames*/

    /**
     * Show About Box
     */
    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = ChatClientApp.getApplication().getMainFrame();
            aboutBox = new ChatClientAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        ChatClientApp.getApplication().show(aboutBox);
    }

    /**
     * Show Option Frame
     */
    @Action
    public void showOptionFrame() {
        OptionsDialog of = new OptionsDialog(this.getFrame(), true);
        of.setLocationRelativeTo(this.getFrame());
        of.setVisible(true);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        loginPanel = new LoginPanel();
        nickText = new javax.swing.JTextField();
        jToolBar2 = new javax.swing.JToolBar();
        login = new javax.swing.JButton();
        userLabel = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(gg.msn.ui.ChatClientApp.class).getContext().getResourceMap(ChatClientView.class);
        menuBar.setBackground(resourceMap.getColor("menuBar.background")); // NOI18N

        fileMenu.setBackground(resourceMap.getColor("fileMenu.background")); // NOI18N
        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(gg.msn.ui.ChatClientApp.class).getContext().getActionMap(ChatClientView.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setText(resourceMap.getString("exitMenuItem.text")); // NOI18N
        fileMenu.add(exitMenuItem);

        jMenuItem1.setAction(actionMap.get("showOptionFrame")); // NOI18N
        fileMenu.add(jMenuItem1);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setBackground(resourceMap.getColor("statusPanel.background")); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 128, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(statusAnimationLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4))
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(statusAnimationLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(statusMessageLabel)))
        );

        nickText.setText(resourceMap.getString("nickText.text")); // NOI18N

        jToolBar2.setFloatable(false);
        jToolBar2.setAutoscrolls(true);
        jToolBar2.setBorderPainted(false);
        jToolBar2.setOpaque(false);

        login.setAction(actionMap.get("connetti")); // NOI18N
        login.setIcon(resourceMap.getIcon("login.icon")); // NOI18N
        login.setText(resourceMap.getString("login.text")); // NOI18N
        login.setFocusable(false);
        login.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        login.setOpaque(false);
        login.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar2.add(login);

        userLabel.setIcon(resourceMap.getIcon("userLabel.icon")); // NOI18N
        userLabel.setText(resourceMap.getString("userLabel.text")); // NOI18N

        jButton1.setAction(actionMap.get("showFacebookLogin")); // NOI18N
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N

        javax.swing.GroupLayout loginPanelLayout = new javax.swing.GroupLayout(loginPanel);
        loginPanel.setLayout(loginPanelLayout);
        loginPanelLayout.setHorizontalGroup(
            loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loginPanelLayout.createSequentialGroup()
                .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(loginPanelLayout.createSequentialGroup()
                        .addGap(134, 134, 134)
                        .addComponent(userLabel))
                    .addGroup(loginPanelLayout.createSequentialGroup()
                        .addGap(109, 109, 109)
                        .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1)
                            .addComponent(nickText, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(loginPanelLayout.createSequentialGroup()
                        .addGap(126, 126, 126)
                        .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(105, Short.MAX_VALUE))
        );
        loginPanelLayout.setVerticalGroup(
            loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loginPanelLayout.createSequentialGroup()
                .addGap(138, 138, 138)
                .addComponent(userLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nickText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jButton1)
                .addContainerGap(59, Short.MAX_VALUE))
        );

        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JButton login;
    private javax.swing.JPanel loginPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JTextField nickText;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JLabel userLabel;
    // End of variables declaration//GEN-END:variables
    private JDialog aboutBox;
    private TrayIcon tray;
//    private int nextPort;

    /**
     * Connette al server principale 
     */
    @Action
    public void connetti() {
        if (!nickText.getText().equals("")) {
            helper.connect();
        } else {
            JOptionPane.showMessageDialog(getFrame(), "<html><font color=red>Il nick non può essere vuoto<html>", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    /**
     * Azione che lancia una chat con l'utente selezionato sulla tabella nick
     */
    //<editor-fold defaultstate="collapsed" desc="Getter & Setter">

    /* Getter and setter for varibles */
    public ChatClientViewHelper getHelper() {
        return helper;
    }

    public void setHelper(ChatClientViewHelper helper) {
        this.helper = helper;
    }

    public JTextField getNickText() {
        return nickText;
    }

    public void setNickText(JTextField nickText) {
        this.nickText = nickText;
    }

    public JPanel getLoginPanel() {
        return loginPanel;
    }

    public void setLoginPanel(JPanel loginPanel) {
        this.loginPanel = loginPanel;
    }

    public JButton getLogin() {
        return login;
    }

    public void setLogin(JButton login) {
        this.login = login;
    }

    public JLabel getStatusMessageLabel() {
        return statusMessageLabel;
    }

    public JLabel getStatusAnimationLabel() {
        return statusAnimationLabel;
    }

    public void setStatusAnimationLabel(JLabel statusAnimationLabel) {
        this.statusAnimationLabel = statusAnimationLabel;
    }

    public JPanel getStatusPanel() {
        return statusPanel;
    }

    public TrayIcon getTray() {
        return tray;
    }

    public void setTray(TrayIcon tray) {
        this.tray = tray;
    }

    public MainPanel getMainPanel() {
        return mainPanel;
    }

    public void setMainPanel(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    //</editor-fold>

    @Action
    public void showNewEmotionManage() {
        EmoticonsManageFrame manageFrame = new EmoticonsManageFrame();
        manageFrame.setLocationRelativeTo(getFrame());
        manageFrame.setVisible(true);
    }

    @Action
    public void showFacebookLogin() {
        helper.showFacebookLoginPanel();
    }
}

class LoginPanel extends JPanel {

    private Log log = LogFactory.getLog(this.getClass());

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g2d);
        try {
            g2d.setRenderingHints(new RenderingHints(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR));
//            log.debug("theme : " + ((ChatClientView) ChatClientApp.getApplication().getMainView()).getHelper().getTheme());
            ImageIcon icon = ThemeManager.getTheme().get(ThemeManager.LOGIN_BACKGROUND);

            if (icon != null) {
                Image image = icon.getImage();
                g2d.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            } else {
                log.warn("image " + icon.getDescription() + " not Found");
            }

        } catch (Exception e) {
//            log.debug(e);
        }
    }
}
// <editor-fold defaultstate="collapsed" desc="Old MainPanel">
/*class MainPanel extends JPanel {

private Log log = LogFactory.getLog(this.getClass());

@Override
public void paintComponent(Graphics g) {
Graphics2D g2d = (Graphics2D) g;
super.paintComponent(g2d);
g2d.setRenderingHints(new RenderingHints(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR));

try {

ImageIcon icon = ThemeManager.getTheme().get(ThemeManager.MAIN_BACKGROUND);

if (icon != null) {
Image image = icon.getImage();
g2d.drawImage(image, 0, 0, getWidth(), getHeight(), this);
} else {
log.warn("image " + icon.getDescription() + " not Found");
}

icon = ThemeManager.getTheme().get(ThemeManager.MAIN_IMAGE);

if (icon != null) {

Image cartel = icon.getImage();
g2d.drawImage(cartel, 0 - cartel.getWidth(this) / 10, getHeight() - cartel.getHeight(this), this);
} else {
log.warn("image " + icon.getDescription() + " not Found");
}
} catch (Exception e) {
//            log.error(e);
}
}
}
 */// </editor-fold>
        
