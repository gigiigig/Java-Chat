/*
 * ChatView.java
 *


public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
throw new UnsupportedOperationException("Not supported yet.");
}
} Created on 1 maggio 2008, 16.54
 */
package gg.msn.ui.chatwindow;

import gg.msn.ui.ChatClientApp;
import gg.msn.ui.ChatClientView;
import gg.msn.ui.form.SelectClientsToAdd;
import gg.msn.ui.theme.ThemeManager;
import chatcommons.Client;
import chatcommons.datamessage.MESSAGE;
import chatcommons.datamessage.MessageManger;
import emoticon.Emoticon;
import emoticon.EmoticonAddDialog;
import emoticon.EmoticonsManger;
import emoticon.Util;
import gg.msn.core.manager.PersistentDataManager;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import static chatcommons.Commands.*;

/**
 *
 * @author  Administrator
 */
public class ChatWindow extends javax.swing.JFrame {

    public static String BACKGROUNDIMAGE = "/chatwindowbackground.png";
    private Log log = LogFactory.getLog(this.getClass());

    /** Creates new form ChatView */
    public ChatWindow() {
        super();
    }

    public ChatWindow(boolean isServer, ChatClientView ccv) {
        initComponents();

        // <editor-fold defaultstate="collapsed" desc="Window Icon"> 
        try {
            setIconImage(ccv.getResourceMap().getImageIcon("trayIcon").getImage());
        } catch (Exception e) {
            log.warn(e);
        }
        // </editor-fold>

        //traspÃ¨arenza testo chat
        jScrollPane2.getViewport().setOpaque(false);
        //trasparenza lista utenti
        jScrollPane3.getViewport().setOpaque(false);

        this.isServer = isServer;
        this.nick = PersistentDataManager.getNick();
        nickLabel.setText(nick);
        clients = new ArrayList<Client>();
        this.ccv = ccv;
        this.outputStream = PersistentDataManager.getOutputStream();

        //imposto l'evento per ENTER
        inputText.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "Enter");
        javax.swing.ActionMap actionMap = Application.getInstance(ChatClientApp.class).getContext().getActionMap(ChatWindow.class, this);
        inputText.getActionMap().put("Enter", actionMap.get("invia"));
        inputText.setDocument(new DefaultStyledDocument());
        doc = chatText.getStyledDocument();
        addStylesToDocument(doc);

        emoctionsManger = new EmoticonsManger();
        emoctionsManger.loadEmoticonsOnDocument(chatText.getStyledDocument());
        emoctionsManger.loadEmoticonsOnDocument(inputText.getStyledDocument());
        inputText.addKeyListener(new InputTextListener(this));

        this.addComponentListener(new MotionListener());
        this.addMouseListener(new EmotionPanelCloser());

        //grafica
        try {
            ImageIcon userIcon = ThemeManager.getTheme().get(ThemeManager.USER_ICON);
            if (userIcon != null) {
                userLabel.setIcon(userIcon);
            }

            ImageIcon addUserIcon =  ThemeManager.getTheme().get(ThemeManager.ADD_USER_ICON);
            if (userIcon != null) {
                addChatButton.setIcon(addUserIcon);
            }
        } catch (NullPointerException e) {
            log.warn(e);
        }

        Properties properties = gg.msn.core.commons.Util.readProperties();
        String fontSt = properties.getProperty(gg.msn.core.commons.Util.PROPERTY_FONT);
        if (fontSt != null && !fontSt.equals("")) {
            font = Font.decode(fontSt);
        } else {
            font = doc.getFont(doc.getStyle("regular"));
        }
        log.debug("font : " + font);

        String colorSt = properties.getProperty(gg.msn.core.commons.Util.PROPERTY_COLOR);
        if (colorSt != null && !colorSt.equals("")) {
            color = Color.decode(colorSt);
        } else {
            color = doc.getForeground(doc.getStyle("regular"));
        }
        log.debug("color : " + color);

        refreshInputTextFont();
        receivedEmoticons = new LinkedList<Emoticon>();

        chatText.addMouseListener(new ChatTextListener(this));

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new MainPanel();
        nickLabel = new javax.swing.JLabel();
        sendButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        inputText = new javax.swing.JTextPane();
        userLabel = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        addChatButton = new javax.swing.JButton();
        chatToolbar = new javax.swing.JToolBar();
        emotionsButton = new javax.swing.JButton();
        fontButton = new javax.swing.JButton();
        colorButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        chatText = new javax.swing.JTextPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        clientList = new javax.swing.JList();

        setName("Form"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        mainPanel.setName("mainPanel"); // NOI18N
        mainPanel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                mainPanelMouseDragged(evt);
            }
        });

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(gg.msn.ui.ChatClientApp.class).getContext().getResourceMap(ChatWindow.class);
        nickLabel.setFont(resourceMap.getFont("nickLabel.font")); // NOI18N
        nickLabel.setForeground(resourceMap.getColor("nickLabel.foreground")); // NOI18N
        nickLabel.setText(resourceMap.getString("nickLabel.text")); // NOI18N
        nickLabel.setName("nickLabel"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(gg.msn.ui.ChatClientApp.class).getContext().getActionMap(ChatWindow.class, this);
        sendButton.setAction(actionMap.get("invia")); // NOI18N
        sendButton.setBackground(new Color(255, 255, 255, 100));
        sendButton.setIcon(resourceMap.getIcon("sendButton.icon")); // NOI18N
        sendButton.setText(resourceMap.getString("sendButton.text")); // NOI18N
        sendButton.setBorderPainted(false);
        sendButton.setName("sendButton"); // NOI18N
        sendButton.setOpaque(false);

        jScrollPane1.setBackground(resourceMap.getColor("jScrollPane1.background")); // NOI18N
        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), resourceMap.getColor("jScrollPane1.border.titleColor"))); // NOI18N
        jScrollPane1.setName("jScrollPane1"); // NOI18N

        inputText.setBackground(resourceMap.getColor("inputText.background")); // NOI18N
        inputText.setBorder(null);
        inputText.setName("inputText"); // NOI18N
        jScrollPane1.setViewportView(inputText);

        userLabel.setIcon(resourceMap.getIcon("userLabel.icon")); // NOI18N
        userLabel.setText(resourceMap.getString("userLabel.text")); // NOI18N
        userLabel.setName("userLabel"); // NOI18N

        jToolBar1.setFloatable(false);
        jToolBar1.setAutoscrolls(true);
        jToolBar1.setBorderPainted(false);
        jToolBar1.setName("jToolBar1"); // NOI18N
        jToolBar1.setOpaque(false);

        addChatButton.setAction(actionMap.get("showSelectClientToAdd")); // NOI18N
        addChatButton.setText(resourceMap.getString("addChatButton.text")); // NOI18N
        addChatButton.setFocusable(false);
        addChatButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        addChatButton.setName("addChatButton"); // NOI18N
        addChatButton.setOpaque(false);
        jToolBar1.add(addChatButton);

        chatToolbar.setFloatable(false);
        chatToolbar.setMinimumSize(new java.awt.Dimension(50, 31));
        chatToolbar.setName("chatToolbar"); // NOI18N
        chatToolbar.setOpaque(false);

        emotionsButton.setAction(actionMap.get("showEmotionsPanel")); // NOI18N
        emotionsButton.setIcon(resourceMap.getIcon("emotionsButton.icon")); // NOI18N
        emotionsButton.setFocusable(false);
        emotionsButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        emotionsButton.setName("emotionsButton"); // NOI18N
        emotionsButton.setOpaque(false);
        emotionsButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        chatToolbar.add(emotionsButton);

        fontButton.setAction(actionMap.get("showFontDialog")); // NOI18N
        fontButton.setFont(resourceMap.getFont("fontButton.font")); // NOI18N
        fontButton.setIcon(resourceMap.getIcon("fontButton.icon")); // NOI18N
        fontButton.setText(resourceMap.getString("fontButton.text")); // NOI18N
        fontButton.setFocusable(false);
        fontButton.setName("fontButton"); // NOI18N
        fontButton.setOpaque(false);
        chatToolbar.add(fontButton);

        colorButton.setAction(actionMap.get("showColorDialog")); // NOI18N
        colorButton.setFont(resourceMap.getFont("colorButton.font")); // NOI18N
        colorButton.setIcon(resourceMap.getIcon("colorButton.icon")); // NOI18N
        colorButton.setText(resourceMap.getString("colorButton.text")); // NOI18N
        colorButton.setFocusable(false);
        colorButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        colorButton.setName("colorButton"); // NOI18N
        colorButton.setOpaque(false);
        colorButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        chatToolbar.add(colorButton);

        jScrollPane2.setBackground(new Color(255, 255, 255, 100));
        jScrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), resourceMap.getColor("jScrollPane2.border.titleColor"))); // NOI18N
        jScrollPane2.setName("jScrollPane2"); // NOI18N
        jScrollPane2.setOpaque(false);

        chatText.setBackground(new Color(0, 0, 0, 100));
        chatText.setBorder(null);
        chatText.setEditable(false);
        chatText.setAlignmentX(0.0F);
        chatText.setAlignmentY(0.0F);
        chatText.setAutoscrolls(false);
        chatText.setName("chatText"); // NOI18N
        chatText.setOpaque(false);
        chatText.setPreferredSize(new java.awt.Dimension(0, 0));
        chatText.setRequestFocusEnabled(false);
        chatText.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                chatTextMouseReleased(evt);
            }
        });
        jScrollPane2.setViewportView(chatText);

        jScrollPane3.setBackground(new Color(0, 0, 0, 255));
        jScrollPane3.setBorder(null);
        jScrollPane3.setName("jScrollPane3"); // NOI18N
        jScrollPane3.setOpaque(false);

        clientList.setBackground(new Color(255, 255, 255, 255));
        clientList.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), resourceMap.getColor("clientList.border.titleColor"))); // NOI18N
        clientList.setModel(new DefaultListModel());
        clientList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        clientList.setCellRenderer(new ClientsListRenderer());
        clientList.setName("clientList"); // NOI18N
        clientList.setOpaque(false);
        jScrollPane3.setViewportView(clientList);

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(userLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(nickLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 206, Short.MAX_VALUE))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chatToolbar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sendButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)))
                .addGap(5, 5, 5)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(userLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nickLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(chatToolbar, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(sendButton, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10))))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 361, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void chatTextMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_chatTextMouseReleased
//
}//GEN-LAST:event_chatTextMouseReleased

private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
// TODO add your handling code here:
    if (emotionsPanel != null) {
        emotionsPanel.setVisible(false);
    }
//    try {

//        chatText.setContentType("text/html");
//        HTMLDocument hd = (HTMLDocument) chatText.getDocument();
//        HTMLWriter hw = new HTMLWriter(new FileWriter("C:/Users/Luigi/Desktop/conv.html"), hd);
//        hw.write();

//    ccv.getHelper().getChatWindows().remove(this);
//    } catch (BadLocationException ex) {
//        log.error(ex);
//    } catch (FileNotFoundException ex) {
//        log.error(ex);
//    }catch (IOException ex) {
//        log.error(ex);
//    } 




//    ccv.getHelper().getChatWindows().remove(this);
}//GEN-LAST:event_formWindowClosing

private void mainPanelMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mainPanelMouseDragged
// TODO add your handling code here:

    emotionsPanel.setVisible(false);
}//GEN-LAST:event_mainPanelMouseDragged
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addChatButton;
    private javax.swing.JTextPane chatText;
    private javax.swing.JToolBar chatToolbar;
    private javax.swing.JList clientList;
    private javax.swing.JButton colorButton;
    private javax.swing.JButton emotionsButton;
    private javax.swing.JButton fontButton;
    private javax.swing.JTextPane inputText;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JLabel nickLabel;
    private javax.swing.JButton sendButton;
    private javax.swing.JLabel userLabel;
    // End of variables declaration//GEN-END:variables
    private ArrayList<Client> clients;
    private Socket socket;
    private String nick;
    private boolean isServer;
    private int port;
    private String ip;
    private OutputStream outputStream;
    private ChatClientView ccv;
    private StyledDocument doc;
    private JDialog emotionsPanel;
    private Font font;
    private Color color;
    private List<Emoticon> receivedEmoticons;
    private EmoticonsManger emoctionsManger;

    // <editor-fold defaultstate="collapsed" desc="Getter and setter">        
    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public javax.swing.JTextPane getChatText() {
        return chatText;
    }

    public void setChatText(String text) {
        this.getChatText().setText(text);
    }

    public javax.swing.JScrollPane getJScrollPane1() {
        return jScrollPane1;
    }

    public void setJScrollPane1(javax.swing.JScrollPane jScrollPane1) {
        this.jScrollPane1 = jScrollPane1;
    }

    public void setChatText(javax.swing.JTextPane chatText) {
        this.chatText = chatText;
    }

    public javax.swing.JTextPane getInputText() {
        return inputText;
    }

    public void setInputText(javax.swing.JTextPane inputText) {
        this.inputText = inputText;
    }

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

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
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

    public boolean isIsServer() {
        return isServer;
    }

    public void setIsServer(boolean isServer) {
        this.isServer = isServer;
    }

    public ChatClientView getCcv() {
        return ccv;
    }

    public EmoticonsManger getEmoctionsManger() {
        return emoctionsManger;
    }

    public void setEmoctionsManger(EmoticonsManger emoctionsManger) {
        this.emoctionsManger = emoctionsManger;
    }

    public JDialog getEmotionsPanel() {
        return emotionsPanel;
    }

    public void setEmotionsPanel(JDialog emotionsPanel) {
        this.emotionsPanel = emotionsPanel;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public List<Emoticon> getReceivedEmoticons() {
        return receivedEmoticons;
    }

    public void setReceivedEmoticons(List<Emoticon> emoticons) {
        this.receivedEmoticons = emoticons;
    }
    //</editor-fold>

    /**
     * invia il messaggio scritto nell'inputText
     */
    @Action
    public void invia() {

        log.debug("invia");
        final String newText = getInputText().getText();
        if (newText != null && !newText.equals("")) {

            //scivo il testo nella chat locale
            writeMessage(getNick(), newText, font, color);
            inputText.setText("");
            inputText.requestFocus();

            new Thread(new Runnable() {

                public void run() {

                    MESSAGE message = null;
                    //parametri
                    List<String> receivers = new LinkedList<String>();
                    if (clients.size() == 1) {
                        receivers.add(clients.get(0).getNick());
                        message = MessageManger.createMessage(Message.SINGLE, receivers, null);
                    } else {
                        for (Client client : clients) {
                            receivers.add(client.getNick());
                        }
                        message = MessageManger.createMessage(Message.CONFERENCE, receivers, null);
                    }

                    //paramtri
                    MessageManger.addParameter(message, "text", newText);
                    String fontSt = fontToString(font);

                    log.debug("font : " + fontSt);

                    MessageManger.addParameter(message, "font", fontSt);
                    MessageManger.addParameter(message, "color", color.getRGB() + "");

                    EmoticonsManger emotionsManger = new EmoticonsManger();

                    try {
                        List<Emoticon> emotocinsToAdd = emotionsManger.emoticonsInDoc(newText);
                        log.debug("emotocinsToAdd : " + emotocinsToAdd.size());
                        for (Emoticon emotion : emotocinsToAdd) {

                            File source = new File(Util.getInstance().getPath() + EmoticonsManger.EMOTICONSPATH + emotion.getFileName());

                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            FileInputStream fis = new FileInputStream(source);

                            byte[] temp = new byte[1024];
                            while (fis.read(temp) != -1) {
                                baos.write(temp);
                            }

                            baos.close();

                            try {

                                baos.close();
                                log.trace("ByteArrayOutputStream = " + baos.size());
                                byte[] data = baos.toByteArray();
                                byte[] compressed = gg.msn.core.commons.Util.compress(data);
                                log.debug("initial size = " + data.length);
                                log.debug("final size = " + compressed.length);
                                MessageManger.addContent(message, emotion.getShortcut(), compressed);

                            } catch (IOException ex) {
                                log.error(ex);
                            }

                        }

                        try {
                            //invio il messaggio
                            MessageManger.directWriteMessage(message, outputStream);
                        } catch (SocketException socketException) {
                            // se il server n risponde chiudo tutte le finestre
                            log.warn("server not respond : " + socketException);
                            List<ChatWindow> chatWindows = ccv.getHelper().getChatWindows();

                            for (ChatWindow chatWindow : chatWindows) {
                                chatWindow.setVisible(false);
                            }
                            JOptionPane.showMessageDialog(ccv.getFrame(), "<html><font color=red>Il server non risponde<html>", "Errore", JOptionPane.ERROR_MESSAGE);

                            ccv.getHelper().showLoginPanel();
                        }

                    } catch (Exception e) {
                        log.error(e);
                    }
                    return;
                }
            }).start();

        } else {
            JOptionPane.showMessageDialog(this, "<html><font color=blue>Il testo da inviare non puÃ² essere vuoto<html>", "Attenzione", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * aggrorna la tabella dei nick in conefrenza
     */
    public void refreshTable() {
        clientList.removeAll();
        DefaultListModel listModel = (DefaultListModel) (clientList.getModel());
        for (Client client : clients) {
            listModel.addElement(client.getNick());
        }
    }

    /**
     * Visualizza il pannello pwer aggingere utenti alla chat
     */
    @Action
    public void showSelectClientToAdd() {
//    
        SelectClientsToAdd scta = new SelectClientsToAdd(this, (ChatClientView) ChatClientApp.getApplication().getMainView());
        scta.setLocationRelativeTo(this);
        scta.setVisible(true);


    }

    /**
     * aggiunge l'utente alla chat
     * @param nick
     */
    @Action
    public void addClientToChat(String nick) {
        clients.add(new Client(null, nick));
        refreshTable();
        validate();
    }

    /**
     * chiude la finestra
     */
    @Action
    public void disconnect() {
        this.setVisible(false);
    }

    /**
     * Aggiunge al documento gli stili 
     * @param doc
     */
    protected void addStylesToDocument(StyledDocument doc) {
        //Initialize some styles.
        Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);

        Style regular = doc.addStyle("regular", def);
        StyleConstants.setFontFamily(regular, "SansSerif");
        StyleConstants.setFontSize(regular, 14);
        StyleConstants.setForeground(regular, Color.BLACK);
        StyleConstants.setBold(regular, false);
        StyleConstants.setItalic(regular, false);

        // My Format

        Style s = doc.addStyle("headerName", regular);
        StyleConstants.setFontFamily(s, "SansSerif");
        StyleConstants.setBold(s, true);
        StyleConstants.setItalic(s, false);
        StyleConstants.setFontSize(s, 14);
        StyleConstants.setForeground(s, Color.BLUE);

        s = doc.addStyle("headerData", regular);
        StyleConstants.setFontFamily(s, "SansSerif");
        StyleConstants.setBold(s, true);
        StyleConstants.setFontSize(s, 13);
        StyleConstants.setItalic(s, true);
        StyleConstants.setForeground(s, Color.BLACK);

    }

    /**
     * scrive il messaggio nel testo della chat con tutta la formatttazione
     * @param sender - il nick cha ha inviato il messaggio
     * @param message
     */
    public void writeMessage(String sender, String message) {
        try {

            SimpleDateFormat format = new SimpleDateFormat("HH-mm-ss");
            Calendar calendar = Calendar.getInstance();
            String headerData = format.format(calendar.getTime()) + " : ";
            String headerName = sender + "  ";
            doc.insertString(doc.getLength(), headerData, doc.getStyle("headerData"));
            doc.insertString(doc.getLength(), headerName, doc.getStyle("headerName"));
            doc.insertString(doc.getLength(), "\n", doc.getStyle("regular"));

//            doc.insertString(doc.getLength(), message + "\n", doc.getStyle("regular"));
            emoctionsManger.insertChatTextWithEmoticons(doc, message, sender, null);
            doc.insertString(doc.getLength(), "\n", doc.getStyle("regular"));

            chatText.setCaretPosition(chatText.getStyledDocument().getLength());
            this.toFront();
            inputText.requestFocus();
        } catch (BadLocationException ex) {
            log.error(ex);
        }

    }

    /**
     * scrive il messaggio nel testo della chat con tutta la formatttazione
     * @param sender - il nick cha ha inviato il messaggio
     * @param message
     */
    public void writeMessage(String sender, String message, Font font, Color color) {
        try {

            String styleName = fontToString(font) + color.getRGB();
            log.debug("style name = " + styleName);

            //aggiungo al documento il nuovo stile
            Style senderStyle = doc.addStyle(styleName, doc.getStyle("regular"));
            StyleConstants.setFontFamily(senderStyle, font.getFamily());
            StyleConstants.setBold(senderStyle, font.isBold());
            StyleConstants.setItalic(senderStyle, font.isItalic());
            StyleConstants.setFontSize(senderStyle, font.getSize());
            StyleConstants.setForeground(senderStyle, color);

            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            String headerData = format.format(calendar.getTime()) + " - ";
            String headerName = sender + "  ";

            //inserisco heder e user name
            doc.insertString(doc.getLength(), headerData, doc.getStyle("headerData"));
            doc.insertString(doc.getLength(), headerName, doc.getStyle("headerName"));
            doc.insertString(doc.getLength(), "\n", doc.getStyle("regular"));

            //scrivo il nuovo testo con le emoticon e il suo stile
            emoctionsManger.insertChatTextWithEmoticons(doc, message, styleName, null);
            doc.insertString(doc.getLength(), "\n", doc.getStyle("regular"));

            //aggiorno la posizione dello scroll
            chatText.setCaretPosition(chatText.getStyledDocument().getLength());
            this.toFront();
            inputText.requestFocus();
        } catch (BadLocationException ex) {
            log.error(ex);
        }

    }

    /**
     * scrive il messaggio nel testo della chat con tutta la formatttazione
     * @param sender - il nick cha ha inviato il messaggio
     * @param message
     */
    public void writeMessage(String sender, String message, Font font, Color color, List<Emoticon> emotions) {
        try {

            String styleName = fontToString(font) + color.getRGB();
            log.debug("style name = " + styleName);

            //aggiungo al documento il nuovo stile
            Style senderStyle = doc.addStyle(styleName, doc.getStyle("regular"));
            StyleConstants.setFontFamily(senderStyle, font.getFamily());
            StyleConstants.setBold(senderStyle, font.isBold());
            StyleConstants.setItalic(senderStyle, font.isItalic());
            StyleConstants.setFontSize(senderStyle, font.getSize());
            StyleConstants.setForeground(senderStyle, color);

            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            String headerData = format.format(calendar.getTime()) + " - ";
            String headerName = sender + "  ";

            //inserisco heder e user name
            doc.insertString(doc.getLength(), headerData, doc.getStyle("headerData"));
            doc.insertString(doc.getLength(), headerName, doc.getStyle("headerName"));
            doc.insertString(doc.getLength(), "\n", doc.getStyle("regular"));

            //aggiungo emoticons ricevute
            emoctionsManger.loadEmotionsOnDocument(doc, emotions);

            //scrivo il nuovo testo con le emoticon e il suo stile
            emoctionsManger.insertChatTextWithEmoticons(doc, message, styleName, emotions);

            doc.insertString(doc.getLength(), "\n", doc.getStyle("regular"));


            //aggiorno la posizione dello scroll
            chatText.setCaretPosition(chatText.getStyledDocument().getLength());
            this.toFront();
            inputText.requestFocus();
        } catch (BadLocationException ex) {
            log.error(ex);
        }

    }

    /**
     * visualizza il pannello delle emotions
     */
    @Action
    public void showEmotionsPanel() {

        if (emotionsPanel != null && emotionsPanel.isVisible()) {
            emotionsPanel.setVisible(false);
        } else if (emotionsPanel != null && !emotionsPanel.isVisible()) {
            emotionsPanel.setLocationRelativeTo(emotionsButton);
            int locX = (int) emotionsPanel.getLocation().getX();
            int locY = (int) emotionsPanel.getLocation().getY();
            locX = locX + (EmotionsPanel.LARGHEZZATOT / 2) - (emotionsButton.getWidth() / 2);
            locY = locY - ((int) emotionsPanel.getSize().getHeight() / 2) - (emotionsButton.getHeight() / 2);
            emotionsPanel.setLocation(locX, locY);
            emotionsPanel.toFront();
            emotionsPanel.setVisible(true);
        } else {
            emotionsPanel = new JDialog(this, false);
            EmotionsPanel panel = new EmotionsPanel(this);
            emotionsPanel.setContentPane(panel);
            emotionsPanel.setSize(EmotionsPanel.LARGHEZZATOT + EmotionsPanel.WINDOWX, panel.NUMIMGY * EmotionsPanel.LATOCASELLA + EmotionsPanel.WINDOWY);
            emotionsPanel.setResizable(false);
            emotionsPanel.validate();
            emotionsPanel.setUndecorated(true);
            emotionsPanel.setLocationRelativeTo(emotionsButton);
            int locX = (int) emotionsPanel.getLocation().getX();
            int locY = (int) emotionsPanel.getLocation().getY();
            locX = locX + (EmotionsPanel.LARGHEZZATOT / 2) - (emotionsButton.getWidth() / 2);
            locY = locY - ((panel.NUMIMGY * EmotionsPanel.LATOCASELLA) / 2) - (emotionsButton.getHeight() / 2);
            emotionsPanel.setLocation(locX, locY);
            emotionsPanel.toFront();
            emotionsPanel.setVisible(true);
        }

    }

    @Action
    public void showFontDialog() {
//        FontChooser fontChooser = new FontChooser(this);
//        fontChooser.setLocationRelativeTo(this);
//        fontChooser.setVisible(true);
//
//        Font newFont = fontChooser.getNewFont();
//
//        font = fontChooser.getNewFont();
//        log.debug("font : " + font.toString());
//        color = fontChooser.getNewColor();
//        refreshTextsFont();

//        NwFontChooserS.showDialog(this, "test", font);
        JFontChooser fontChooser = new JFontChooser(font);
        fontChooser.showDialog(this, "Font");
        Font newFont = fontChooser.getFont();
        if (newFont != null) {
            font = newFont;
            refreshInputTextFont();

            Properties properties = gg.msn.core.commons.Util.readProperties();
            properties.setProperty(gg.msn.core.commons.Util.PROPERTY_FONT, fontToString(font));
            gg.msn.core.commons.Util.writeProperties(properties);
        }
    }

    @Action
    public void showColorDialog() {

        Color newcolor = JColorChooser.showDialog(this, "Colore", color);
        if (newcolor != null) {
            color = newcolor;
            refreshInputTextFont();

            Properties properties = gg.msn.core.commons.Util.readProperties();
            properties.setProperty(gg.msn.core.commons.Util.PROPERTY_COLOR, color.getRGB() + "");
            gg.msn.core.commons.Util.writeProperties(properties);
        }

    }

    public static void main(String[] args) {
        Map<String, ChatWindow> map = new HashMap<String, ChatWindow>();

        ChatWindow s = map.get("2");
    }

    public void refreshInputTextFont() {

        Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);

        StyleConstants.setFontFamily(def, font.getFamily());
        StyleConstants.setBold(def, font.isBold());
        StyleConstants.setItalic(def, font.isItalic());
        StyleConstants.setFontSize(def, font.getSize());
        StyleConstants.setForeground(def, color);

        inputText.setLogicalStyle(def);
        inputText.getStyledDocument().addStyle("regular", def);

    }

    private String fontToString(Font font) {

        String fontSt = "";
        fontSt += font.getFontName();
        if (font.getStyle() == Font.BOLD) {
            fontSt += "-bold";
        } else if (font.getStyle() == Font.ITALIC) {
            fontSt += "-italic";
        } else if (font.getStyle() == Font.BOLD + Font.ITALIC) {
            fontSt += "-bolditalic";
        }
        fontSt += "-" + font.getSize();

        return fontSt;
    }
}

/**
 * Classe che serve per creare lo sfondo della fuiestra di chat
 * @author Luigi
 */
class MainPanel extends JPanel {

    private Log log = LogFactory.getLog(this.getClass());

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g2d);
        g2d.setRenderingHints(new RenderingHints(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR));

        try {

            ImageIcon icon =  ThemeManager.getTheme().get(ThemeManager.CHAT_BACKGROUND);

            if (icon != null) {
                Image image = icon.getImage();
                g2d.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            } else {
                log.warn("image " + icon.getDescription() + " not Found");
            }

        } catch (Exception e) {
            log.debug(e.getStackTrace());
        }
    }
}

/**
 * Listener per gestire l'immissione dinamica delle emotions 
 * nell'input text
 * @author Luigi
 */
class InputTextListener extends KeyAdapter {

    ChatWindow chatWindow;

    public InputTextListener(ChatWindow chatWindow) {
        this.chatWindow = chatWindow;
    }
    private Log log = LogFactory.getLog(this.getClass());

    /**
     * per aggiungere le emoticons nel testo mentre si scrive
     * @param arg0
     */
    @Override
    public void keyReleased(KeyEvent arg0) {
        super.keyPressed(arg0);
        try {
            JTextPane inputText = (JTextPane) arg0.getComponent();
            chatWindow.getEmoctionsManger().repleceIEmotionsInText(inputText.getStyledDocument());
        } catch (Exception e) {
            log.error(e);
        }
    }

    /**
     * Per evitare che l'imput text perde il font
     * @param arg0
     */
    @Override
    public void keyPressed(KeyEvent arg0) {
        chatWindow.refreshInputTextFont();
    }
}

class EmotionPanelCloser extends MouseInputAdapter {

    @Override
    public void mousePressed(MouseEvent e) {
        ChatWindow chatWindow = (ChatWindow) e.getComponent();
        chatWindow.getEmotionsPanel().setVisible(false);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        ChatWindow chatWindow = (ChatWindow) e.getComponent();
        chatWindow.getEmotionsPanel().setVisible(false);
    }
}

class ChatTextListener extends MouseInputAdapter {

    private Log log = LogFactory.getLog(this.getClass());
    private ChatWindow cw;

    public ChatTextListener(ChatWindow cw) {
        this.cw = cw;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            try {

                final JTextPane text = (JTextPane) e.getComponent();
                int pos = text.viewToModel(e.getPoint());
                log.trace("selected text : " + text.getText(pos, 1));

                //recupero l'elemento del testo selezioanto
                Element elementTemp = text.getStyledDocument().getCharacterElement(pos);
                log.trace("style : " + elementTemp.getName());

                //controllo che sia un emoticon se nn lo Ã¨ tento dal carattere precendente
                if (!elementTemp.getName().equals("icon")) {
                    log.trace("actual position : " + pos);
                    pos--;
                    elementTemp = text.getStyledDocument().getCharacterElement(pos);
                    log.trace("new position : " + pos);
                }

                final Element element = elementTemp;

                //se Ã¨ stata cliccata un icona
                log.trace("elemnt name : " + element.getName());
                if (element.getName().equals("icon")) {

                    int start = element.getStartOffset();
                    log.trace("start : " + start);
                    int stop = element.getEndOffset();
                    log.trace("stop : " + stop);

                    int length = stop - start;

                    //recupero la shortcut delll'icona selezionata
                    String shortcutTmp = "";
                    try {
                        shortcutTmp = text.getStyledDocument().getText(start, length);
                    } catch (BadLocationException badLocationException) {
                        log.error(badLocationException);
                    }
                    final String shortcut = shortcutTmp;

                    log.debug("selected shortcut : " + shortcut);

                    //dalla shortcut prendo l'Emoticon corrispondente
                    Emoticon tempEmo = null;

                    for (Emoticon emoticon : cw.getReceivedEmoticons()) {
                        if (emoticon.getShortcut().equals(shortcut)) {
                            tempEmo = emoticon;
                        }
                    }

                    //se l'emoticon nn Ã¨ nullla significa che Ã¨ una di quelle ricevute e continuo
                    if (tempEmo != null) {
                        final Emoticon toStore = tempEmo;

                        JPopupMenu menu = new JPopupMenu();
                        JMenuItem item = new JMenuItem();

                        item.setAction(new AbstractAction() {

                            @Override
                            public void actionPerformed(ActionEvent e) {

                                toStore.setFileName(shortcut + ".gif");
                                EmoticonAddDialog addDialog = new EmoticonAddDialog(cw, true, toStore);
                                addDialog.setLocationRelativeTo(cw);
                                addDialog.setVisible(true);

                                cw.setEmoctionsManger(new EmoticonsManger());
                                cw.setEmotionsPanel(null);
                            }
                        });

                        item.setText("Aggiungi emoticon");
                        menu.add(item);
                        menu.setLocation(e.getPoint());
                        menu.show(text, e.getX(), e.getY());

                    }

                    //in questo caso nn Ã¨ prprio un emoticon
                    if (element.getName().equals("content")) {
                        log.debug("not selected emoticon");
                    }
                }
            } catch (BadLocationException ex) {
                log.error(ex);
            }
        }
    }
}

class MotionListener implements ComponentListener {

    @Override
    public void componentResized(ComponentEvent e) {
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        ChatWindow chatWindow = (ChatWindow) e.getComponent();
        if (chatWindow.getEmotionsPanel() != null) {
            chatWindow.getEmotionsPanel().setVisible(false);
        }
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }
}

class ClientsListRenderer extends JLabel implements ListCellRenderer {

    Log log = LogFactory.getLog(this.getClass());

    public ClientsListRenderer() {
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        /*se il valore non Ã¨ nullo impossto il renderig altrimenti lo lascio nascosto,
        questo impedisce che lo spazio bianco selweziona sempre l'utlimo elemento*/

        setOpaque(false);
        setBackground(list.getBackground());
        setForeground(list.getForeground());

        setText((String) value.toString());

        return this;

    }
}

