/*
 * ChatClientView.java
 */
package gg.msn.ui;


import emoticon.EmoticonsManageFrame;
import gg.msn.ui.listener.ChatClientViewListeners;
import gg.msn.ui.form.OptionsDialog;
import gg.msn.ui.helper.ChatClientViewHelper;
import gg.msn.ui.theme.ThemeManager;
import chatcommons.Client;
import java.awt.AWTException;
import java.awt.Color;
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
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.Timer;
import javax.swing.Icon;
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
    private Log log = LogFactory.getLog(this.getClass());
    private ChatClientViewHelper helper = new ChatClientViewHelper(this);

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
        clients = new ArrayList<Client>();
        initComponents();
        //TEST
//        ImageIcon icon = new ImageIcon(getResourceMap().getIcon("mainBackground").getImage());
//        log.info("icon " + icon.getDescription() + " loaded!!! ");

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
                        if (getOutputStream() != null) {
                            log.info("nell'if");
                            disconnetti();
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
        insertIcons();

        //setto visilbile il panello di login
        helper.showLoginPanel();

        //trasparenza lista
        clientListScrollPane.getViewport().setOpaque(false);

        /*aggiungo agli oggetti i vari componenti personalizzati*/

        //add nickText ENTER keystroke
        nickText.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "Enter");
        javax.swing.ActionMap actionMap = Application.getInstance(ChatClientApp.class).getContext().getActionMap(ChatClientView.class, this);
        nickText.getActionMap().put("Enter", actionMap.get("connetti"));

        //aggiungo tutti i listeners dei vari componenti
        ChatClientViewListeners listeners = new ChatClientViewListeners(this);

        //clientsList
        clientsList.addMouseListener(listeners.getClientsListRightClickListener());
        clientsList.setCellRenderer(new ClientsListRenderer());
        clientsList.setFixedCellHeight(30);
        //elemento vuoto per eviatre che lo dallo spazio vuoto si selezioni l'ultimo elemento
//        ((DefaultListModel) clientsList.getModel()).addElement(null);

//        clientsList.addComponentListener(listeners.getClientsListRightClickListener());
        //mainpanel
        //nickTable.addMouseListener(new nickTableOutClickListener());
        mainPanel.addMouseListener(listeners.getClientsListOutClickListener());
        jToolBar1.addMouseListener(listeners.getClientsListOutClickListener());

        //this frame
        this.getFrame().addWindowListener(listeners.getMainViewListerner());

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String) (evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer) (evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });

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
        mainPanel = new MainPanel();
        nickLabel = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        disconnect = new javax.swing.JButton();
        chat = new javax.swing.JButton();
        clientListScrollPane = new javax.swing.JScrollPane();
        clientsList = new javax.swing.JList();
        nickIcon = new javax.swing.JLabel();
        loginPanel = new LoginPanel();
        nickText = new javax.swing.JTextField();
        jToolBar2 = new javax.swing.JToolBar();
        login = new javax.swing.JButton();
        userLabel = new javax.swing.JLabel();

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

        mainPanel.setBackground(resourceMap.getColor("mainPanel.background")); // NOI18N

        nickLabel.setFont(resourceMap.getFont("nickLabel.font")); // NOI18N
        nickLabel.setForeground(resourceMap.getColor("nickLabel.foreground")); // NOI18N
        nickLabel.setText(resourceMap.getString("nickLabel.text")); // NOI18N

        jToolBar1.setBackground(resourceMap.getColor("jToolBar1.background")); // NOI18N
        jToolBar1.setFloatable(false);
        jToolBar1.setBorderPainted(false);
        jToolBar1.setOpaque(false);

        disconnect.setAction(actionMap.get("disconnetti")); // NOI18N
        disconnect.setBackground(resourceMap.getColor("disconnect.background")); // NOI18N
        disconnect.setIcon(resourceMap.getIcon("disconnect.icon")); // NOI18N
        disconnect.setToolTipText(resourceMap.getString("disconnect.toolTipText")); // NOI18N
        disconnect.setFocusable(false);
        disconnect.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        disconnect.setOpaque(false);
        disconnect.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(disconnect);

        chat.setAction(actionMap.get("addChatWithSelected")); // NOI18N
        chat.setIcon(resourceMap.getIcon("chat.icon")); // NOI18N
        chat.setText(resourceMap.getString("chat.text")); // NOI18N
        chat.setToolTipText(resourceMap.getString("chat.toolTipText")); // NOI18N
        chat.setFocusable(false);
        chat.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        chat.setOpaque(false);
        chat.setPreferredSize(new java.awt.Dimension(59, 59));
        chat.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(chat);

        clientListScrollPane.setBackground(new Color(255,255,255,100)
        );
        clientListScrollPane.setBorder(null);
        clientListScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        clientListScrollPane.setOpaque(false);

        clientsList.setBackground(new Color(255, 255, 255, 100));
        clientsList.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("clientsList.border.title"), javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("clientsList.border.titleFont"))); // NOI18N
        clientsList.setFont(resourceMap.getFont("clientsList.font")); // NOI18N
        clientsList.setModel(new DefaultListModel()
        );
        clientsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        clientsList.setFocusable(false);
        clientsList.setInheritsPopupMenu(true);
        clientsList.setOpaque(false);
        clientsList.setSelectionBackground(new Color(204, 204, 204, 150));
        clientsList.setVisibleRowCount(1);
        clientListScrollPane.setViewportView(clientsList);

        nickIcon.setIcon(resourceMap.getIcon("nickIcon.icon")); // NOI18N

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(clientListScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, mainPanelLayout.createSequentialGroup()
                        .addComponent(nickIcon)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(nickLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 148, Short.MAX_VALUE)
                        .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(nickIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nickLabel)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(clientListScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE)
                .addContainerGap())
        );

        nickText.setText(resourceMap.getString("nickText.text")); // NOI18N

        jToolBar2.setFloatable(false);
        jToolBar2.setAutoscrolls(true);
        jToolBar2.setBorderPainted(false);
        jToolBar2.setOpaque(false);

        login.setAction(actionMap.get("connetti")); // NOI18N
        login.setText(resourceMap.getString("login.text")); // NOI18N
        login.setFocusable(false);
        login.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        login.setOpaque(false);
        login.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar2.add(login);

        userLabel.setIcon(resourceMap.getIcon("userLabel.icon")); // NOI18N
        userLabel.setText(resourceMap.getString("userLabel.text")); // NOI18N

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
                        .addComponent(nickText, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(loginPanelLayout.createSequentialGroup()
                        .addGap(126, 126, 126)
                        .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(137, Short.MAX_VALUE))
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
                .addContainerGap(112, Short.MAX_VALUE))
        );

        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton chat;
    private javax.swing.JScrollPane clientListScrollPane;
    private javax.swing.JList clientsList;
    private javax.swing.JButton disconnect;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JButton login;
    private javax.swing.JPanel loginPanel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JLabel nickIcon;
    private javax.swing.JLabel nickLabel;
    private javax.swing.JTextField nickText;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JLabel userLabel;
    // End of variables declaration//GEN-END:variables
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private JDialog aboutBox;
    private ArrayList<Client> clients;
    private Socket socket;
    private OutputStream outputStream;
    private Socket fileSocket;
    private OutputStream fileOutputStream;
    private int port;
    private String ip;
    private String nick;
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
            JOptionPane.showMessageDialog(getFrame(), "<html><font color=red>Il nick non puÃ² essere vuoto<html>", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    /**
     * Disconnettte dal serever principale
     */
    @Action
    public void disconnetti() {
        helper.disconnetti();
    }

    /**
     * Azione che lancia una chat con l'utente selezionato sulla tabella nick
     */
    @Action
    public void addChatWithSelected() {
        helper.addChatWithSelected();
    }

    //<editor-fold defaultstate="collapsed" desc="Getter & Setter">

    /* Getter and setter for varibles */
    public ArrayList<Client> getClients() {
        return clients;
    }

    public void setClients(ArrayList<Client> clients) {
        this.clients = clients;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public JList getClientsList() {
        return clientsList;
    }

    public void setClientsList(JList clientsList) {
        this.clientsList = clientsList;
    }

    public ChatClientViewHelper getHelper() {
        return helper;
    }

    public void setHelper(ChatClientViewHelper helper) {
        this.helper = helper;
    }


    /* get set for swing components  */
    public javax.swing.JPanel getMainPanel() {
        return mainPanel;
    }

    public void setMainPanel(javax.swing.JPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public JButton getConnect() {
        return disconnect;
    }

    public void setConnect(JButton connect) {
        this.disconnect = connect;
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

    public JLabel getNickLabel() {
        return nickLabel;
    }

    public void setNickLabel(JLabel nickLabel) {
        this.nickLabel = nickLabel;
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

    public Timer getBusyIconTimer() {
        return busyIconTimer;
    }

    public JLabel getStatusAnimationLabel() {
        return statusAnimationLabel;
    }

    public void setStatusAnimationLabel(JLabel statusAnimationLabel) {
        this.statusAnimationLabel = statusAnimationLabel;
    }

    public Icon getIdleIcon() {
        return idleIcon;
    }

    public JPanel getStatusPanel() {
        return statusPanel;
    }

    public OutputStream getFileOutputStream() {
        return fileOutputStream;
    }

    public void setFileOutputStream(OutputStream fileOutputStream) {
        this.fileOutputStream = fileOutputStream;
    }

    public Socket getFileSocket() {
        return fileSocket;
    }

    public void setFileSocket(Socket fileSocket) {
        this.fileSocket = fileSocket;
    }

    public TrayIcon getTray() {
        return tray;
    }

    public void setTray(TrayIcon tray) {
        this.tray = tray;
    }
    //</editor-fold>

    /**
     * Un renderer personalizzato per la jList di clients
     */    //GRAPHICS METHODS
    public void insertIcons() {

        try {
            //imposto le icone
            ImageIcon userIcon = helper.getTheme().get(ThemeManager.USER_ICON);
            if (userIcon != null) {
                userLabel.setIcon(userIcon);
                nickIcon.setIcon(userIcon);
            }
        } catch (NullPointerException e) {
        }
    }

    @Action
    public void showNewEmotionManage() {
        EmoticonsManageFrame manageFrame = new EmoticonsManageFrame();
        manageFrame.setLocationRelativeTo(getFrame());
        manageFrame.setVisible(true);
    }

    class ClientsListRenderer extends JLabel implements ListCellRenderer {

        Log log = LogFactory.getLog(this.getClass());

        public ClientsListRenderer() {
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

            /*se il valore non Ã¨ nullo impossto il renderig altrimenti lo lascio nascosto,
            questo impedisce che lo spazio bianco selweziona sempre l'utlimo elemento*/


//                log.debug("Render lement : " + value);

            if (isSelected) {
                setOpaque(true);
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setOpaque(false);
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            setText((String) value.toString());

            try {
                ImageIcon icon = helper.getTheme().get(ThemeManager.USER_ICON);
                ImageIcon scaledIcon = new ImageIcon(icon.getImage().getScaledInstance(24, 24, Image.SCALE_AREA_AVERAGING));
                setFont(list.getFont());
                setIcon(scaledIcon);
            } catch (Exception e) {
                log.warn(e);
            }

            return this;

        }
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
            ImageIcon icon = ((ChatClientView) ChatClientApp.getApplication().getMainView()).getHelper().getTheme().get(ThemeManager.LOGIN_BACKGROUND);

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

class MainPanel extends JPanel {

    private Log log = LogFactory.getLog(this.getClass());

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g2d);
        g2d.setRenderingHints(new RenderingHints(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR));

        try {

            ImageIcon icon = ((ChatClientView) ChatClientApp.getApplication().getMainView()).getHelper().getTheme().get(ThemeManager.MAIN_BACKGROUND);

            if (icon != null) {
                Image image = icon.getImage();
                g2d.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            } else {
                log.warn("image " + icon.getDescription() + " not Found");
            }

            icon = ((ChatClientView) ChatClientApp.getApplication().getMainView()).getHelper().getTheme().get(ThemeManager.MAIN_IMAGE);

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

        
