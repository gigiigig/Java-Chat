/*
 * OptionsFrame.java
 *
 * Created on 6 aprile 2008, 18.02
 */
package chatclient.forms;

import chatclient.*;
import chatclient.commons.Util;
import chatclient.theme.ThemeManager;
import emoticon.EmoticonsManageFrame;
import java.io.File;
import java.util.Properties;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.application.Action;

/**
 *
 * @author  Luigi
 */
public class OptionsDialog extends javax.swing.JDialog {

    private Log log = LogFactory.getLog(this.getClass());

    /** Creates new form OptionsFrame */
    public OptionsDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        Properties properties = Util.readProperties();
        if (properties != null) {

            String porta = properties.getProperty(Util.PROPERTY_PORT);
            String ip = properties.getProperty(Util.PROPERTY_IP);
            String nick = properties.getProperty(Util.PROPERTY_NICK);
            String downloadFolder = properties.getProperty(Util.PROPERTY_DOWNLOAD_FOLDER);
            String themeFolder = properties.getProperty(Util.PROPERTY_THEME_FOLDER);
            if (!porta.equals("")) {
                portaText.setText(porta);
            } else {
                porta = "3434";
            }
            if (!ip.equals("")) {
                ipText.setText(ip);
            } else {
                ip = "localhost";
            }
            if (themeFolder == null || themeFolder.equals("")) {
                themeFolder = Util.getPath() + Util.VALUE_DEFAULT_THEME_FOLDER;
            }
            nickText.setText(nick);
            downloadFolderText.setText(downloadFolder);
            themeFolderText.setText(themeFolder);

        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        save = new javax.swing.JButton();
        cancel = new javax.swing.JButton();
        optionTabPane = new javax.swing.JTabbedPane();
        generalPanel = new javax.swing.JPanel();
        downloadFolderText = new javax.swing.JTextField();
        downloadLabel = new javax.swing.JLabel();
        nickText = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        nickLabel = new javax.swing.JLabel();
        themeFolderText = new javax.swing.JTextField();
        themeFolderLabel = new javax.swing.JLabel();
        themeFolderButton = new javax.swing.JButton();
        startEmoticonsManager = new javax.swing.JButton();
        connectionPanel = new javax.swing.JPanel();
        portLabel = new javax.swing.JLabel();
        ipText = new javax.swing.JTextField();
        ipLabel = new javax.swing.JLabel();
        portaText = new javax.swing.JTextField();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(chatclient.ChatClientApp.class).getContext().getResourceMap(OptionsDialog.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(chatclient.ChatClientApp.class).getContext().getActionMap(OptionsDialog.class, this);
        save.setAction(actionMap.get("saveProperties")); // NOI18N
        save.setName("save"); // NOI18N

        cancel.setAction(actionMap.get("annulla")); // NOI18N
        cancel.setName("cancel"); // NOI18N

        optionTabPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        optionTabPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        optionTabPane.setName("o"); // NOI18N
        optionTabPane.setRequestFocusEnabled(false);

        generalPanel.setName("generalPanel"); // NOI18N

        downloadFolderText.setName("downloadFolderText"); // NOI18N

        downloadLabel.setText(resourceMap.getString("downloadLabel.text")); // NOI18N
        downloadLabel.setName("downloadLabel"); // NOI18N

        nickText.setText(resourceMap.getString("nickText.text")); // NOI18N
        nickText.setName("nickText"); // NOI18N

        jButton1.setAction(actionMap.get("selectDownloadFolder")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N

        nickLabel.setText(resourceMap.getString("nickLabel.text")); // NOI18N
        nickLabel.setName("nickLabel"); // NOI18N

        themeFolderText.setName("themeFolderText"); // NOI18N

        themeFolderLabel.setText(resourceMap.getString("themeFolderLabel.text")); // NOI18N
        themeFolderLabel.setName("themeFolderLabel"); // NOI18N

        themeFolderButton.setAction(actionMap.get("selectThemeFolder")); // NOI18N
        themeFolderButton.setName("themeFolderButton"); // NOI18N

        startEmoticonsManager.setAction(actionMap.get("startEmoticonsManager")); // NOI18N
        startEmoticonsManager.setName("startEmoticonsManager"); // NOI18N

        javax.swing.GroupLayout generalPanelLayout = new javax.swing.GroupLayout(generalPanel);
        generalPanel.setLayout(generalPanelLayout);
        generalPanelLayout.setHorizontalGroup(
            generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(generalPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(generalPanelLayout.createSequentialGroup()
                        .addGroup(generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(generalPanelLayout.createSequentialGroup()
                                .addGroup(generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(nickLabel)
                                    .addComponent(downloadLabel)
                                    .addComponent(nickText, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(themeFolderLabel))
                                .addGap(122, 122, 122))
                            .addGroup(generalPanelLayout.createSequentialGroup()
                                .addGroup(generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(downloadFolderText, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
                                    .addComponent(themeFolderText, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(themeFolderButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12))
                    .addGroup(generalPanelLayout.createSequentialGroup()
                        .addComponent(startEmoticonsManager)
                        .addContainerGap(246, Short.MAX_VALUE))))
        );
        generalPanelLayout.setVerticalGroup(
            generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(generalPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(nickLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nickText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(downloadLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1)
                    .addGroup(generalPanelLayout.createSequentialGroup()
                        .addComponent(downloadFolderText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(themeFolderLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(themeFolderButton)
                            .addComponent(themeFolderText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(startEmoticonsManager)
                .addGap(136, 136, 136))
        );

        optionTabPane.addTab(resourceMap.getString("generalPanel.TabConstraints.tabTitle"), generalPanel); // NOI18N

        connectionPanel.setName("connectionPanel"); // NOI18N

        portLabel.setText(resourceMap.getString("portLabel.text")); // NOI18N
        portLabel.setName("portLabel"); // NOI18N

        ipText.setText(resourceMap.getString("ipText.text")); // NOI18N
        ipText.setName("ipText"); // NOI18N

        ipLabel.setText(resourceMap.getString("ipLabel.text")); // NOI18N
        ipLabel.setName("ipLabel"); // NOI18N

        portaText.setText(resourceMap.getString("portaText.text")); // NOI18N
        portaText.setName("portaText"); // NOI18N

        javax.swing.GroupLayout connectionPanelLayout = new javax.swing.GroupLayout(connectionPanel);
        connectionPanel.setLayout(connectionPanelLayout);
        connectionPanelLayout.setHorizontalGroup(
            connectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(connectionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(connectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(ipLabel, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ipText, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                    .addComponent(portaText, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                    .addComponent(portLabel, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );
        connectionPanelLayout.setVerticalGroup(
            connectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(connectionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(portLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(portaText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ipLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ipText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        optionTabPane.addTab(resourceMap.getString("connectionPanel.TabConstraints.tabTitle"), connectionPanel); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(226, Short.MAX_VALUE)
                .addComponent(cancel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(save, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(optionTabPane, javax.swing.GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(optionTabPane, javax.swing.GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancel)
                    .addComponent(save))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    @Action
    public void saveProperties() {
        Properties properties = new Properties();
        
        properties.setProperty(Util.PROPERTY_PORT, portaText.getText());
        properties.setProperty(Util.PROPERTY_IP, ipText.getText());
        properties.setProperty(Util.PROPERTY_NICK, nickText.getText());
        properties.setProperty(Util.PROPERTY_DOWNLOAD_FOLDER, downloadFolderText.getText());
        properties.setProperty(Util.PROPERTY_THEME_FOLDER, themeFolderText.getText());
        
        Util.writeProperties(properties);

        ChatClientView ccv = (ChatClientView) ChatClientApp.getApplication().getMainView();
        ccv.setPort(Integer.parseInt(portaText.getText()));
        ccv.setIp(ipText.getText());
//        ccv.setNick(nickText.getText());
        ccv.getNickText().setText(nickText.getText());
        ccv.getHelper().setTheme(ThemeManager.loadTheme(properties.getProperty(Util.PROPERTY_THEME_FOLDER)));
        log.info("property saved");
        ccv.insertIcons();        
        setVisible(false);
        ccv.getFrame().validate();
        ccv.getFrame().repaint();
       

    }

    @Action
    public void annulla() {
        setVisible(false);
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancel;
    private javax.swing.JPanel connectionPanel;
    private javax.swing.JTextField downloadFolderText;
    private javax.swing.JLabel downloadLabel;
    private javax.swing.JPanel generalPanel;
    private javax.swing.JLabel ipLabel;
    private javax.swing.JTextField ipText;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel nickLabel;
    private javax.swing.JTextField nickText;
    private javax.swing.JTabbedPane optionTabPane;
    private javax.swing.JLabel portLabel;
    private javax.swing.JTextField portaText;
    private javax.swing.JButton save;
    private javax.swing.JButton startEmoticonsManager;
    private javax.swing.JButton themeFolderButton;
    private javax.swing.JLabel themeFolderLabel;
    private javax.swing.JTextField themeFolderText;
    // End of variables declaration//GEN-END:variables

    // <editor-fold defaultstate="collapsed" desc=" Getter and Setter ">
    public JTextField getPortaText() {
        return portaText;
    }

    public JTextField getIpText() {
        return ipText;
    }

    public JTextField getDownloadFolderText() {
        return downloadFolderText;
    }

    public void setDownloadFolderText(JTextField downloadFolderText) {
        this.downloadFolderText = downloadFolderText;
    }

    public JLabel getDownloadLabel() {
        return downloadLabel;
    }

    public void setDownloadLabel(JLabel downloadLabel) {
        this.downloadLabel = downloadLabel;
    }

    public JPanel getGeneralPanel() {
        return generalPanel;
    }

    public void setGeneralPanel(JPanel generalPanel) {
        this.generalPanel = generalPanel;
    }

    public JLabel getIpLabel() {
        return ipLabel;
    }

    public void setIpLabel(JLabel ipLabel) {
        this.ipLabel = ipLabel;
    }

    public JButton getJButton1() {
        return jButton1;
    }

    public void setJButton1(JButton jButton1) {
        this.jButton1 = jButton1;
    }

    public Log getLog() {
        return log;
    }

    public void setLog(Log log) {
        this.log = log;
    }

    public JLabel getNickLabel() {
        return nickLabel;
    }

    public void setNickLabel(JLabel nickLabel) {
        this.nickLabel = nickLabel;
    }

    public JTextField getNickText() {
        return nickText;
    }

    public void setNickText(JTextField nickText) {
        this.nickText = nickText;
    }

    public JTabbedPane getOptionTabPane() {
        return optionTabPane;
    }

    public void setOptionTabPane(JTabbedPane optionTabPane) {
        this.optionTabPane = optionTabPane;
    }

    public JLabel getPortLabel() {
        return portLabel;
    }

    public void setPortLabel(JLabel portLabel) {
        this.portLabel = portLabel;
    }

    public JButton getSave() {
        return save;
    }

    public void setSave(JButton save) {
        this.save = save;
    }

    public JButton getThemeFolderButton() {
        return themeFolderButton;
    }

    public void setThemeFolderButton(JButton themeFolderButton) {
        this.themeFolderButton = themeFolderButton;
    }

    public JLabel getThemeFolderLabel() {
        return themeFolderLabel;
    }

    public void setThemeFolderLabel(JLabel themeFolderLabel) {
        this.themeFolderLabel = themeFolderLabel;
    }

    public JTextField getThemeFolderText() {
        return themeFolderText;
    }

    public void setThemeFolderText(JTextField themeFolderText) {
        this.themeFolderText = themeFolderText;
    }

    public JButton getCancel() {
        return cancel;
    }

    public void setCancel(JButton cancel) {
        this.cancel = cancel;
    }

    
// </editor-fold>


    @Action
    public void selectDownloadFolder() {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        jFileChooser.showDialog(this, "OK");
        downloadFolderText.setText(jFileChooser.getSelectedFile().getPath());
    }
    
    @Action
    public void selectThemeFolder() {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
               
        File file = new File(themeFolderText.getText());
        if(file.isDirectory()){
            jFileChooser.setCurrentDirectory(file);
        }
        
        jFileChooser.showDialog(this, "OK");
        themeFolderText.setText(jFileChooser.getSelectedFile().getPath());
    }

    @Action
    public void startEmoticonsManager() {
        EmoticonsManageFrame manageFrame = new EmoticonsManageFrame();
        manageFrame.setLocationRelativeTo(this);
        manageFrame.setVisible(true);
       
        this.setVisible(false);
         manageFrame.toFront();
    }


            

}
