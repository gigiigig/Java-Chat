/*
 * ChatClientView.java
 */
package gg.msn.ui;

import emoticon.EmoticonsManageFrame;
import gg.msn.core.manager.PersistentDataManager;
import gg.msn.ui.listener.ChatClientViewListeners;
import gg.msn.ui.form.OptionsDialog;
import gg.msn.ui.helper.ChatClientViewHelper;
import gg.msn.ui.panel.LoginPanel;
import gg.msn.ui.panel.MainPanel;
import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.WindowEvent;
import org.jdesktop.application.Action;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JFrame;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The application's main frame.
 */
public class ChatClientView extends FrameView{

    /* varibili generali */
    private Log log = LogFactory.getLog(ChatClientView.class);
    private ChatClientViewHelper helper;// = new ChatClientViewHelper();
    private MainPanel mainPanel;
    private LoginPanel loginPanel;
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
        loginPanel = new LoginPanel(this);

        helper = new ChatClientViewHelper(this);
        helper.initializeProperties();
       
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



        //setto visilbile il panello di login
        helper.showFacebookLoginPanel();

        /*aggiungo agli oggetti i vari componenti personalizzati*/



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
        OptionsDialog of = new OptionsDialog(this.getFrame(), true,loginPanel);
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

        setMenuBar(menuBar);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuBar menuBar;
    // End of variables declaration//GEN-END:variables
    private JDialog aboutBox;
    private TrayIcon tray;
//    private int nextPort;

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

    public LoginPanel getLoginPanel() {
        return loginPanel;
    }

    public void setLoginPanel(LoginPanel loginPanel) {
        this.loginPanel = loginPanel;
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
    @Action
    public void showLogin() {
        helper.showLoginPanel();
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
        
