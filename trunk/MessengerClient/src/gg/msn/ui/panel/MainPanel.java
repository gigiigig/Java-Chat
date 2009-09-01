/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MainPanel.java
 *
 * Created on 26-ago-2009, 14.22.12
 */
package gg.msn.ui.panel;

import chatcommons.Client;
import facebookchat.common.FacebookBuddyList;
import facebookchat.common.FacebookUser;
import gg.msn.ui.ChatClientView;
import gg.msn.ui.listener.ChatClientViewListeners;
import gg.msn.ui.theme.ThemeManager;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.util.Iterator;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.application.Action;

/**
 *
 * @author Luigi
 */
public class MainPanel extends javax.swing.JPanel {

    private ChatClientView ccv;
    private Log log = LogFactory.getLog(this.getClass());

    /** Creates new form MainPanel */
    public MainPanel(ChatClientView ccv) {
        initComponents();
        this.ccv = ccv;

        //trasparenza lista
        clientListScrollPane.getViewport().setOpaque(false);

        ChatClientViewListeners listeners = new ChatClientViewListeners(ccv);

        clientsList.addMouseListener(listeners.getClientsListRightClickListener());
        clientsList.setCellRenderer(new ClientsListCellRenderer());
        clientsList.setFixedCellHeight(30);
        //mainpanel
        //nickTable.addMouseListener(new nickTableOutClickListener());
        this.addMouseListener(listeners.getClientsListOutClickListener());
        jToolBar1.addMouseListener(listeners.getClientsListOutClickListener());

        insertIcons();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        nickLabel = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        disconnect = new javax.swing.JButton();
        chat = new javax.swing.JButton();
        clientListScrollPane = new javax.swing.JScrollPane();
        clientsList = new javax.swing.JList();
        nickIcon = new javax.swing.JLabel();

        setName("Form"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(gg.msn.ui.ChatClientApp.class).getContext().getResourceMap(MainPanel.class);
        nickLabel.setFont(resourceMap.getFont("nickLabel.font")); // NOI18N
        nickLabel.setText(resourceMap.getString("nickLabel.text")); // NOI18N
        nickLabel.setName("nickLabel"); // NOI18N

        jToolBar1.setFloatable(false);
        jToolBar1.setBorderPainted(false);
        jToolBar1.setName("jToolBar1"); // NOI18N
        jToolBar1.setOpaque(false);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(gg.msn.ui.ChatClientApp.class).getContext().getActionMap(MainPanel.class, this);
        disconnect.setAction(actionMap.get("disconnetti")); // NOI18N
        disconnect.setIcon(resourceMap.getIcon("disconnect.icon")); // NOI18N
        disconnect.setFocusable(false);
        disconnect.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        disconnect.setName("disconnect"); // NOI18N
        disconnect.setOpaque(false);
        disconnect.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(disconnect);

        chat.setAction(actionMap.get("addChatWithSelected")); // NOI18N
        chat.setIcon(resourceMap.getIcon("chat.icon")); // NOI18N
        chat.setFocusable(false);
        chat.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        chat.setName("chat"); // NOI18N
        chat.setOpaque(false);
        chat.setPreferredSize(new java.awt.Dimension(59, 59));
        chat.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(chat);

        clientListScrollPane.setBackground(new Color(255,255,255,100)
        );
        clientListScrollPane.setBorder(null);
        clientListScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        clientListScrollPane.setName("clientListScrollPane"); // NOI18N
        clientListScrollPane.setOpaque(false);

        clientsList.setBackground(new Color(255, 255, 255, 100));
        clientsList.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("clientsList.border.title"), javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("clientsList.border.titleFont"))); // NOI18N
        clientsList.setModel(new DefaultListModel()
        );
        clientsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        clientsList.setCellRenderer(new ClientsListCellRenderer());
        clientsList.setFocusable(false);
        clientsList.setInheritsPopupMenu(true);
        clientsList.setName("clientsList"); // NOI18N
        clientsList.setOpaque(false);
        clientsList.setSelectionBackground(new Color(204, 204, 204, 150));
        clientsList.setVisibleRowCount(1);
        clientListScrollPane.setViewportView(clientsList);

        nickIcon.setIcon(resourceMap.getIcon("nickIcon.icon")); // NOI18N
        nickIcon.setName("nickIcon"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(clientListScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(nickIcon)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(nickLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 140, Short.MAX_VALUE)
                        .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(nickIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nickLabel)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(clientListScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton chat;
    private javax.swing.JScrollPane clientListScrollPane;
    private javax.swing.JList clientsList;
    private javax.swing.JButton disconnect;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel nickIcon;
    private javax.swing.JLabel nickLabel;
    // End of variables declaration//GEN-END:variables

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

    /**
     * Inserisce l'immagine dll'utente o l'icona User
     */
    public void insertIcons() {

        try {
            //imposto le icone
            ImageIcon userIcon = null;
            if (ChatClientView.protocol.equals(ChatClientView.FACEBOOK_PROTOCOL)) {
                userIcon = FacebookBuddyList.me.portrait;
                log.debug("user icon [ " + userIcon + " ]");
            }
            if (userIcon == null) {
                userIcon = ThemeManager.getTheme().get(ThemeManager.USER_ICON);
            }
            if (userIcon != null) {
                nickIcon.setIcon(userIcon);
            }
        } catch (NullPointerException e) {
        }
    }

    public void updateListWithFACEBOOKContacts() {
        //fmod.removeAll();
        ((DefaultListModel) clientsList.getModel()).removeAllElements();
        log.debug("utenti presenti [" + FacebookBuddyList.buddies.size() + "]");
        Iterator<String> it = FacebookBuddyList.buddies.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            log.debug("userID: " + key);
            FacebookUser fu = FacebookBuddyList.buddies.get(key);
            log.debug("status: " + fu.onlineStatus.toString());
            ((DefaultListModel) clientsList.getModel()).addElement(fu);
        }
        clientsList.repaint();
        clientsList.revalidate();

    }

    public JList getClientsList() {
        return clientsList;
    }

    public void setClientsList(JList clientsList) {
        this.clientsList = clientsList;
    }

    public JLabel getNickLabel() {
        return nickLabel;
    }

    public void setNickLabel(JLabel nickLabel) {
        this.nickLabel = nickLabel;
    }

    /**
     * Disconnettte dal serever principale
     */
    @Action
    public void disconnetti() {
        ccv.getHelper().disconnetti();
    }

    @Action
    public void addChatWithSelected() {
        ccv.getHelper().startChatWithSelected();
    }
}

class ClientsListCellRenderer extends JLabel implements ListCellRenderer {

    Log log = LogFactory.getLog(this.getClass());

    public ClientsListCellRenderer() {
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

        try {
            //render per utenti Facebook
            if (ChatClientView.protocol.equals(ChatClientView.FACEBOOK_PROTOCOL)) {
                final FacebookUser user = (FacebookUser) value;
                setText(user.name);
                ImageIcon icon = user.portrait;
                //log.debug("icon [" + icon + "]");
                ImageIcon scaledIcon = new ImageIcon(icon.getImage().getScaledInstance(24, 24, Image.SCALE_AREA_AVERAGING));
                setFont(list.getFont());
                setIcon(scaledIcon);
                //render per utenti Client
            } else {
                Client client = (Client) value;
                setText((client).getNick());
                ImageIcon icon = null;
                try {
                    new ImageIcon(client.getImage());
                } catch (Exception e) {
//                    log.debug("immgine non presente");
                    icon = ThemeManager.getTheme().get(ThemeManager.USER_ICON);
                }
                ImageIcon scaledIcon = new ImageIcon(icon.getImage().getScaledInstance(24, 24, Image.SCALE_AREA_AVERAGING));
                setFont(list.getFont());
                setIcon(scaledIcon);
            }
        } catch (Exception e) {
            log.warn(e);
        }

        return this;

    }
}
