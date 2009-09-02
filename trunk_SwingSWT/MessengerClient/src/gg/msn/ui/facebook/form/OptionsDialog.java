/*
 * OptionsFrame.java
 *
 * Created on 6 aprile 2008, 18.02
 */
package gg.msn.ui.facebook.form;

import gg.msn.ui.ChatClientView;
import gg.msn.core.commons.Util;
import gg.msn.ui.theme.ThemeManager;
import emoticon.EmoticonsManageFrame;
import gg.msn.ui.ChatClientApp;
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


            String themeFolder = properties.getProperty(Util.PROPERTY_THEME_FOLDER);

            if (themeFolder == null || themeFolder.equals("")) {
                themeFolder = Util.getPath() + Util.VALUE_DEFAULT_THEME_FOLDER;
            }

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
        themeFolderText = new javax.swing.JTextField();
        themeFolderLabel = new javax.swing.JLabel();
        themeFolderButton = new javax.swing.JButton();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(gg.msn.ui.ChatClientApp.class).getContext().getResourceMap(OptionsDialog.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(gg.msn.ui.ChatClientApp.class).getContext().getActionMap(OptionsDialog.class, this);
        save.setAction(actionMap.get("saveProperties")); // NOI18N
        save.setName("save"); // NOI18N

        cancel.setAction(actionMap.get("annulla")); // NOI18N
        cancel.setName("cancel"); // NOI18N

        optionTabPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        optionTabPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        optionTabPane.setName("o"); // NOI18N
        optionTabPane.setRequestFocusEnabled(false);

        generalPanel.setName("generalPanel"); // NOI18N

        themeFolderText.setName("themeFolderText"); // NOI18N

        themeFolderLabel.setText(resourceMap.getString("themeFolderLabel.text")); // NOI18N
        themeFolderLabel.setName("themeFolderLabel"); // NOI18N

        themeFolderButton.setAction(actionMap.get("selectThemeFolder")); // NOI18N
        themeFolderButton.setName("themeFolderButton"); // NOI18N

        javax.swing.GroupLayout generalPanelLayout = new javax.swing.GroupLayout(generalPanel);
        generalPanel.setLayout(generalPanelLayout);
        generalPanelLayout.setHorizontalGroup(
            generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, generalPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(generalPanelLayout.createSequentialGroup()
                        .addComponent(themeFolderLabel)
                        .addGap(164, 164, 164))
                    .addGroup(generalPanelLayout.createSequentialGroup()
                        .addComponent(themeFolderText, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(themeFolderButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        generalPanelLayout.setVerticalGroup(
            generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(generalPanelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(themeFolderLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(themeFolderButton)
                    .addComponent(themeFolderText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(33, Short.MAX_VALUE))
        );

        optionTabPane.addTab(resourceMap.getString("generalPanel.TabConstraints.tabTitle"), generalPanel); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(225, Short.MAX_VALUE)
                .addComponent(cancel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(save, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(optionTabPane, javax.swing.GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(optionTabPane, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
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

        properties.setProperty(Util.PROPERTY_THEME_FOLDER, themeFolderText.getText());
        Util.writeProperties(properties);
        ChatClientView ccv = (ChatClientView) ChatClientApp.getApplication().getMainView();

        ThemeManager.loadTheme(properties.getProperty(Util.PROPERTY_THEME_FOLDER));
        log.info("property saved");
        ccv.getMainPanel().insertIcons();
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
    private javax.swing.JPanel generalPanel;
    private javax.swing.JTabbedPane optionTabPane;
    private javax.swing.JButton save;
    private javax.swing.JButton themeFolderButton;
    private javax.swing.JLabel themeFolderLabel;
    private javax.swing.JTextField themeFolderText;
    // End of variables declaration//GEN-END:variables

    // <editor-fold defaultstate="collapsed" desc=" Getter and Setter ">
    public JPanel getGeneralPanel() {
        return generalPanel;
    }

    public void setGeneralPanel(JPanel generalPanel) {
        this.generalPanel = generalPanel;
    }

    public Log getLog() {
        return log;
    }

    public void setLog(Log log) {
        this.log = log;
    }

    public JTabbedPane getOptionTabPane() {
        return optionTabPane;
    }

    public void setOptionTabPane(JTabbedPane optionTabPane) {
        this.optionTabPane = optionTabPane;
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
    public void selectThemeFolder() {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        File file = new File(themeFolderText.getText());
        if (file.isDirectory()) {
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