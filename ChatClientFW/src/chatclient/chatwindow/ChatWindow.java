/*
 * ChatView.java
 *
 * Created on 1 maggio 2008, 16.54
 */
package chatclient.chatwindow;

import chatclient.*;
import chatclient.forms.SelectClientsToAdd;
import chatclient.theme.ThemeManager;
import chatcommons.Client;
import chatcommons.datamessage.MESSAGE;
import chatcommons.datamessage.MessageManger;
import emoticon.Emoticon;
import emoticon.EmoticonAddDialog;
import emoticon.EmoticonsManger;
import emoticon.Util;
import java.awt.Color;
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
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
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

        this.isServer = isServer;
        this.nick = ccv.getNick();
        nickLabel.setText(nick);
        clients = new ArrayList<Client>();
        this.ccv = ccv;
        this.outputStream = ccv.getOutputStream();

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
            ImageIcon userIcon = ccv.getHelper().getTheme().get(ThemeManager.USER_ICON);
            if (userIcon != null) {
                userLabel.setIcon(userIcon);
            }

            ImageIcon addUserIcon = ccv.getHelper().getTheme().get(ThemeManager.ADD_USER_ICON);
            if (userIcon != null) {
                addChatButton.setIcon(addUserIcon);
            }
        } catch (NullPointerException e) {
            log.warn(e);
        }

        Properties properties = chatclient.commons.Util.readProperties();
        String fontSt = properties.getProperty(chatclient.commons.Util.PROPERTY_FONT);
        if (fontSt != null && !fontSt.equals("")) {
            font = Font.decode(fontSt);
        } else {
            font = doc.getFont(doc.getStyle("regular"));
        }
        log.debug("font : " + font);

        String colorSt = properties.getProperty(chatclient.commons.Util.PROPERTY_COLOR);
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
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        mainPanel = new MainPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        nickTable = new javax.swing.JTable();
        chatTextScroller = new javax.swing.JScrollPane();
        chatText = new javax.swing.JTextPane();
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

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        nickTable.setAutoCreateRowSorter(true);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(chatclient.ChatClientApp.class).getContext().getResourceMap(ChatWindow.class);
        nickTable.setFont(resourceMap.getFont("nickTable.font")); // NOI18N
        nickTable.setEnabled(false);
        nickTable.setGridColor(resourceMap.getColor("nickTable.gridColor")); // NOI18N
        nickTable.setIntercellSpacing(new java.awt.Dimension(0, 0));
        nickTable.setName("nickTable"); // NOI18N
        nickTable.setRowHeight(20);

        org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create("${clients}");
        org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, eLProperty, nickTable);
        org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${nick}"));
        columnBinding.setColumnName("Nick");
        columnBinding.setColumnClass(String.class);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();
        jScrollPane3.setViewportView(nickTable);

        chatTextScroller.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        chatTextScroller.setAutoscrolls(true);
        chatTextScroller.setName("chatTextScroller"); // NOI18N

        chatText.setEditable(false);
        chatText.setAlignmentX(0.0F);
        chatText.setAlignmentY(0.0F);
        chatText.setAutoscrolls(false);
        chatText.setName("chatText"); // NOI18N
        chatText.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                chatTextMouseReleased(evt);
            }
        });
        chatTextScroller.setViewportView(chatText);

        nickLabel.setFont(resourceMap.getFont("nickLabel.font")); // NOI18N
        nickLabel.setForeground(resourceMap.getColor("nickLabel.foreground")); // NOI18N
        nickLabel.setText(resourceMap.getString("nickLabel.text")); // NOI18N
        nickLabel.setName("nickLabel"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(chatclient.ChatClientApp.class).getContext().getActionMap(ChatWindow.class, this);
        sendButton.setAction(actionMap.get("invia")); // NOI18N
        sendButton.setText(resourceMap.getString("sendButton.text")); // NOI18N
        sendButton.setBorderPainted(false);
        sendButton.setName("sendButton"); // NOI18N
        sendButton.setOpaque(false);

        jScrollPane1.setName("jScrollPane1"); // NOI18N

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

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(userLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(nickLabel))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chatTextScroller, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(chatToolbar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(sendButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(userLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(nickLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chatTextScroller, javax.swing.GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(chatToolbar, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(sendButton, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10))))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE))
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

        bindingGroup.bind();

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
    private javax.swing.JScrollPane chatTextScroller;
    private javax.swing.JToolBar chatToolbar;
    private javax.swing.JButton colorButton;
    private javax.swing.JButton emotionsButton;
    private javax.swing.JButton fontButton;
    private javax.swing.JTextPane inputText;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JLabel nickLabel;
    private javax.swing.JTable nickTable;
    private javax.swing.JButton sendButton;
    private javax.swing.JLabel userLabel;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
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

            new SwingWorker() {

                @Override
                protected Object doInBackground() throws Exception {

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
                                byte[] compressed = chatclient.commons.Util.compress(data);
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
                    return null;
                }
            }.execute();

        } else {
            JOptionPane.showMessageDialog(this, "<html><font color=blue>Il testo da inviare non può essere vuoto<html>", "Attenzione", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * aggrorna la tabella dei nick in conefrenza
     */
    public void refreshTable() {
        org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create("${clients}");
        org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, eLProperty, nickTable);
        org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${nick}"));
        columnBinding.setColumnName("Nick");
        columnBinding.setColumnClass(String.class);
        jTableBinding.bind();
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

            Properties properties = chatclient.commons.Util.readProperties();
            properties.setProperty(chatclient.commons.Util.PROPERTY_FONT, fontToString(font));
            chatclient.commons.Util.writeProperties(properties);
        }
    }

    @Action
    public void showColorDialog() {

        Color newcolor = JColorChooser.showDialog(this, "Colore", color);
        if (newcolor != null) {
            color = newcolor;
            refreshInputTextFont();

            Properties properties = chatclient.commons.Util.readProperties();
            properties.setProperty(chatclient.commons.Util.PROPERTY_COLOR, color.getRGB() + "");
            chatclient.commons.Util.writeProperties(properties);
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

            ImageIcon icon = ((ChatClientView) ChatClientApp.getApplication().getMainView()).getHelper().getTheme().get(ThemeManager.CHAT_BACKGROUND);

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

                //controllo che sia un emoticon se nn lo è tento dal carattere precendente
                if (!elementTemp.getName().equals("icon")) {
                    log.trace("actual position : " + pos);
                    pos--;
                    elementTemp = text.getStyledDocument().getCharacterElement(pos);
                    log.trace("new position : " + pos);
                }

                final Element element = elementTemp;

                //se è stata cliccata un icona
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

                    //se l'emoticon nn è nullla significa che è una di quelle ricevute e continuo
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

                    //in questo caso nn è prprio un emoticon
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
