/*
 * EmoticonsManageFrame.java
 *
 * Created on 15 ottobre 2008, 12.30
 */
package emoticon;

import emoticon.Util;
import emoticon.Emoticon;
import emoticon.EmoticonsManger;
import emoticon.xml.EMOTIONXML;
import emoticon.xml.EmotionType;
import emoticon.xml.ObjectFactory;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author  Luigi
 */
public class EmoticonsManageFrame extends javax.swing.JFrame {

    private EmoticonsManger emotionsManger;
    private List<Emoticon> emotions;
    private Emoticon toRender;
    Log log = LogFactory.getLog(this.getClass());

    /** Creates new form EmoticonsManageFrame */
    public EmoticonsManageFrame() {
        initComponents();

        emotionsManger = new EmoticonsManger();
        emotions = emotionsManger.getEmotions();
        loadEmoticonsOnList();
        emoticonslist.setCellRenderer(new EmoticonsListRender());
        emoticonslist.setFixedCellHeight(30);
        emoticonslist.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                refreshValues();
            }
        });

        fileButton.setVisible(false);
        filePathLabel.setVisible(false);
        filePathText.setVisible(false);
        cancelButton.setVisible(false);

    }

    /**
     * Aggiorna tutti i valori in base alla emoticon selezionata
     * nella lista
     */
    private void refreshValues() {

        Emoticon emotion = (Emoticon) emoticonslist.getSelectedValue();
        if (emotion != null) {
            nameText.setText(emotion.getName());
            shortcutText.setText(emotion.getShortcut());
            fileName.setText(emotion.getFileName());
            toRender = null;
            previewPanel.repaint();
        } else {
            nameText.setText("");
            shortcutText.setText("");
            File file = new File(filePathText.getText());
            if (file.isFile()) {
                fileName.setText(file.getName());
            }
            previewPanel.repaint();
        }

    }

    // <editor-fold defaultstate="collapsed" desc=" Getter and setter ">
    public JList getEmoticonslist() {
        return emoticonslist;
    }

    public void setEmoticonslist(JList emoticonslist) {
        this.emoticonslist = emoticonslist;
    }

    public List<Emoticon> getEmotions() {
        return emotions;
    }

    public void setEmotions(List<Emoticon> emotions) {
        this.emotions = emotions;
    }

    public EmoticonsManger getEmotionsManger() {
        return emotionsManger;
    }

    public void setEmotionsManger(EmoticonsManger emotionsManger) {
        this.emotionsManger = emotionsManger;
    }

    public JLabel getJLabel1() {
        return jLabel1;
    }

    public void setJLabel1(JLabel jLabel1) {
        this.jLabel1 = jLabel1;
    }

    public JLabel getJLabel2() {
        return jLabel2;
    }

    public void setJLabel2(JLabel jLabel2) {
        this.jLabel2 = jLabel2;
    }

    public JLabel getJLabel3() {
        return jLabel3;
    }

    public void setJLabel3(JLabel jLabel3) {
        this.jLabel3 = jLabel3;
    }

    public JPanel getJPanel1() {
        return previewPanel;
    }

    public void setJPanel1(JPanel jPanel1) {
        this.previewPanel = jPanel1;
    }

    public JScrollPane getJScrollPane1() {
        return jScrollPane1;
    }

    public void setJScrollPane1(JScrollPane jScrollPane1) {
        this.jScrollPane1 = jScrollPane1;
    }

    public JTextField getJTextField1() {
        return nameText;
    }

    public void setJTextField1(JTextField jTextField1) {
        this.nameText = jTextField1;
    }

    public JTextField getJTextField2() {
        return shortcutText;
    }

    public void setJTextField2(JTextField jTextField2) {
        this.shortcutText = jTextField2;
    }

    public Log getLog() {
        return log;
    }

    public void setLog(Log log) {
        this.log = log;
    }

    public JButton getFileButton() {
        return fileButton;
    }

    public void setFileButton(JButton fileButton) {
        this.fileButton = fileButton;
    }

    public JTextField getFileName() {
        return fileName;
    }

    public void setFileName(JTextField fileName) {
        this.fileName = fileName;
    }

    public JLabel getFilePathLabel() {
        return filePathLabel;
    }

    public void setFilePathLabel(JLabel filePathLabel) {
        this.filePathLabel = filePathLabel;
    }

    public JTextField getFilePathText() {
        return filePathText;
    }

    public void setFilePathText(JTextField filePathText) {
        this.filePathText = filePathText;
    }

    public JLabel getJLabel4() {
        return jLabel4;
    }

    public void setJLabel4(JLabel jLabel4) {
        this.jLabel4 = jLabel4;
    }

    public JTextField getNameText() {
        return nameText;
    }

    public void setNameText(JTextField nameText) {
        this.nameText = nameText;
    }

    public JButton getNewEmoticonButton() {
        return newEmoticonButton;
    }

    public void setNewEmoticonButton(JButton newEmoticonButton) {
        this.newEmoticonButton = newEmoticonButton;
    }

    public JPanel getPreviewPanel() {
        return previewPanel;
    }

    public void setPreviewPanel(JPanel previewPanel) {
        this.previewPanel = previewPanel;
    }

    public JTextField getShortcutText() {
        return shortcutText;
    }

    public void setShortcutText(JTextField shortcutText) {
        this.shortcutText = shortcutText;
    }

    public Emoticon getToRender() {
        return toRender;
    }

    public void setToRender(Emoticon toRender) {
        this.toRender = toRender;
    }
// </editor-fold>

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        emoticonslist = new javax.swing.JList();
        nameText = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        shortcutText = new javax.swing.JTextField();
        previewPanel = new EmotionPreviewPanel(this);
        jLabel3 = new javax.swing.JLabel();
        fileName = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        newEmoticonButton = new javax.swing.JButton();
        filePathText = new javax.swing.JTextField();
        filePathLabel = new javax.swing.JLabel();
        fileButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();

        setName("Form"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        emoticonslist.setModel(new DefaultListModel());
        emoticonslist.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        emoticonslist.setName("emoticonslist"); // NOI18N
        jScrollPane1.setViewportView(emoticonslist);

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(EmoticonsManageFrame.class);
        nameText.setText(resourceMap.getString("nameText.text")); // NOI18N
        nameText.setName("nameText"); // NOI18N

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        shortcutText.setText(resourceMap.getString("shortcutText.text")); // NOI18N
        shortcutText.setName("shortcutText"); // NOI18N

        previewPanel.setBackground(resourceMap.getColor("previewPanel.background")); // NOI18N
        previewPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        previewPanel.setName("previewPanel"); // NOI18N

        javax.swing.GroupLayout previewPanelLayout = new javax.swing.GroupLayout(previewPanel);
        previewPanel.setLayout(previewPanelLayout);
        previewPanelLayout.setHorizontalGroup(
            previewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 87, Short.MAX_VALUE)
        );
        previewPanelLayout.setVerticalGroup(
            previewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 90, Short.MAX_VALUE)
        );

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        fileName.setName("fileName"); // NOI18N

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        newEmoticonButton.setText(resourceMap.getString("newEmoticonButton.text")); // NOI18N
        newEmoticonButton.setName("newEmoticonButton"); // NOI18N
        newEmoticonButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newEmoticonButtonActionPerformed(evt);
            }
        });

        filePathText.setName("filePathText"); // NOI18N

        filePathLabel.setText(resourceMap.getString("filePathLabel.text")); // NOI18N
        filePathLabel.setName("filePathLabel"); // NOI18N

        fileButton.setText(resourceMap.getString("fileButton.text")); // NOI18N
        fileButton.setName("fileButton"); // NOI18N
        fileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileButtonActionPerformed(evt);
            }
        });

        cancelButton.setText(resourceMap.getString("cancelButton.text")); // NOI18N
        cancelButton.setName("cancelButton"); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        deleteButton.setText(resourceMap.getString("deleteButton.text")); // NOI18N
        deleteButton.setName("deleteButton"); // NOI18N
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(fileName, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3)
                                    .addComponent(previewPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2)
                                    .addComponent(nameText, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(shortcutText, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)))
                                .addGap(52, 52, 52))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(filePathLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 215, Short.MAX_VALUE)
                                        .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(filePathText, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(fileButton, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(53, 53, 53)))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(newEmoticonButton, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(53, 53, 53))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nameText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(shortcutText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fileName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(previewPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(filePathLabel)
                    .addComponent(deleteButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(filePathText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fileButton, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newEmoticonButton)
                    .addComponent(cancelButton))
                .addContainerGap())
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-418)/2, (screenSize.height-419)/2, 418, 419);
    }// </editor-fold>//GEN-END:initComponents

private void fileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileButtonActionPerformed
// TODO add your handling code here:
    showFileDialog();
}//GEN-LAST:event_fileButtonActionPerformed

private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
// TODO add your handling code here:
    cancel();
}//GEN-LAST:event_cancelButtonActionPerformed

private void newEmoticonButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newEmoticonButtonActionPerformed
// TODO add your handling code here:
    creteNewEmoticon();
}//GEN-LAST:event_newEmoticonButtonActionPerformed

private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
// TODO add your handling code here:
    deleteSelectedEmoticon();
}//GEN-LAST:event_deleteButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JList emoticonslist;
    private javax.swing.JButton fileButton;
    private javax.swing.JTextField fileName;
    private javax.swing.JLabel filePathLabel;
    private javax.swing.JTextField filePathText;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField nameText;
    private javax.swing.JButton newEmoticonButton;
    private javax.swing.JPanel previewPanel;
    private javax.swing.JTextField shortcutText;
    // End of variables declaration//GEN-END:variables

    private void loadEmoticonsOnList() {
        DefaultListModel model = (DefaultListModel) emoticonslist.getModel();
        try {
            model.clear();
        } catch (Exception e) {
            log.warn(e);
        }
        for (Emoticon emotion : emotions) {
            model.addElement(emotion);
        }
    }

    public void creteNewEmoticon() {
        if (newEmoticonButton.getText().contains("Salva")) {
            saveEmoticon();
        }
        fileButton.setVisible(true);
        filePathLabel.setVisible(true);
        filePathText.setVisible(true);
        cancelButton.setVisible(true);
        deleteButton.setVisible(false);

        newEmoticonButton.setText("Salva");
    }

    private void saveEmoticon() {

        File destination = new File(Util.getInstance().getPath() + EmoticonsManger.EMOTICONSPATH + fileName.getText());
        if (destination.isFile()) {
            JOptionPane.showMessageDialog(this, "<html><font color=blue>Il file scelto è già esistente<html>", "Attenzione", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (shortcutText.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "<html><font color=blue>La scorciatoia non può essere vuota<html>", "Attenzione", JOptionPane.WARNING_MESSAGE);
            return;
        }
        SwingWorker worker = new SwingWorker() {

            @Override
            protected Object doInBackground() throws Exception {
                try {

                    Emoticon emotion = toRender;
                    emotion.setName(nameText.getText());
                    emotion.setShortcut(shortcutText.getText());
                    File inputFile = new File(filePathText.getText());
                    if (inputFile.isFile()) {
                        emotion.setFileName(inputFile.getName());
                    }

                    EmoticonsManger manger = new EmoticonsManger();
                    EMOTIONXML emoticonsxml = manger.loadEmotionXML();

                    EmotionType type = new ObjectFactory().createEmotionType();
                    type.setImage(fileName.getText());
                    type.setName(emotion.getName());
                    type.setShortcut(emotion.getShortcut());

                    emoticonsxml.getEmotions().add(type);
                    //scrivo i file
                    manger.copyEmoticonFile(inputFile, fileName.getText());
                    manger.writeEmoticonXML(emoticonsxml);

                    log.debug("saved emoticon");

                    newEmoticonButton.setText("Nuova");

                    fileButton.setVisible(false);
                    filePathLabel.setVisible(false);
                    filePathText.setVisible(false);
                    cancelButton.setVisible(false);
                    deleteButton.setVisible(true);
                    toRender = null;
                    
                    filePathText.setText("");
                    refreshValues();
                    

                    log.debug("graphic changes applieds");
                    manger.loadEmotionsList();
                    emotions = manger.getEmotions();
                    loadEmoticonsOnList();

                } catch (Exception e) {
                    log.error(e);
                }
                return null;
            }
        };
        worker.execute();

    }

    public void showFileDialog() {
        JFileChooser jFileChooser = new JFileChooser();

        File file = new File(filePathText.getText());
        if (file.isDirectory()) {
            jFileChooser.setCurrentDirectory(file);
        }


        jFileChooser.showDialog(this, "OK");
        if (new File(jFileChooser.getSelectedFile().getPath()).isFile()) {

            filePathText.setText(jFileChooser.getSelectedFile().getPath());

            try {
                emoticonslist.clearSelection();
            } catch (Exception e) {
            }

            Emoticon emoticon = new Emoticon();
            ImageIcon icon = new ImageIcon(filePathText.getText());
            if (!filePathText.getText().endsWith(".gif") && !filePathText.getText().endsWith(".gif")) {


                int width = icon.getIconWidth();
                int height = icon.getIconHeight();
                float scaleFactor = 1;
                int widthMax = previewPanel.getWidth() - 2;
                int heightMax = previewPanel.getHeight() - 2;

                if (width > widthMax || height > heightMax) {
                    if (width > height) {
                        scaleFactor = (float) widthMax / width;
                    } else {
                        scaleFactor = (float) heightMax / height;
                    }
                }

                log.debug("scale factor : " + scaleFactor);

                width = (int) (width * scaleFactor);
                height = (int) (height * scaleFactor);

                emoticon.setImageIcon(new ImageIcon(icon.getImage().getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING)));
            } else {
                emoticon.setImageIcon(icon);
            }

            toRender = emoticon;

            refreshValues();

        }
    }

    public void cancel() {
        newEmoticonButton.setText("Nuova");
        fileButton.setVisible(false);
        filePathLabel.setVisible(false);
        filePathText.setVisible(false);
        cancelButton.setVisible(false);
        deleteButton.setVisible(false);

    }

    private void deleteSelectedEmoticon() {
        Emoticon emotion = (Emoticon) emoticonslist.getSelectedValue();
        if (emotion != null) {

            int response = JOptionPane.showConfirmDialog(this, "<html><font color=red>Vuoi eliminare l'Emoticon selezionata<html>", "Attenzione", JOptionPane.OK_CANCEL_OPTION);
            if (response == JOptionPane.CANCEL_OPTION) {
                return;
            }

            EmoticonsManger manger = new EmoticonsManger();
            EMOTIONXML emoticonsxml = manger.loadEmotionXML();

            List<EmotionType> emotionTypes = emoticonsxml.getEmotions();
            for (EmotionType emotionType : emotionTypes) {
                if (emotionType.getShortcut().equals(emotion.getShortcut())) {
                    emotionTypes.remove(emotionType);
                    break;
                }
            }

            manger.writeEmoticonXML(emoticonsxml);
            log.debug("updated XML");
            File toRemove = new File(Util.getInstance().getPath() + EmoticonsManger.EMOTICONSPATH + emotion.getFileName());

//            log.debug("to remove : " + toRemove.getPath() + " is file : " + toRemove.isFile());

            try {
                if (toRemove.canWrite()) {
                    toRemove.delete();
                    log.debug("deleted file : " + toRemove.getName());
                } else {
                    log.warn("unable to delete file : " + toRemove.getName());
                }
            } catch (Exception e) {
                log.error(e);
            }


            manger.loadEmotionsList();
            emotions = manger.getEmotions();
            loadEmoticonsOnList();
            shortcutText.setText("");
            nameText.setText("");
            fileName.setText("");

        } else {
            JOptionPane.showMessageDialog(this, "<html><font color=blue>Non è stata selezionata nessuna emoticon!<html>", "Attenzione", JOptionPane.WARNING_MESSAGE);
            return;
        }
    }
}

/**
 * Renderer delle caselle della list 
 * @author Luigi
 */
class EmoticonsListRender extends JLabel implements ListCellRenderer {

    Log log = LogFactory.getLog(this.getClass());

    public EmoticonsListRender() {
        setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        Emoticon emotion = (Emoticon) value;
        BufferedImage bufferedImage;
        ImageIcon statciGif = null;

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        try {
            //se è un gif devo a una uinstanza statica
            if (emotion.getFileName().endsWith(".gif")) {
                String iconPath = Util.getInstance().getPath() + EmoticonsManger.EMOTICONSPATH + emotion.getFileName();
                bufferedImage = ImageIO.read(new File(iconPath));
                statciGif = new ImageIcon(bufferedImage.getScaledInstance(30, 30, Image.SCALE_AREA_AVERAGING));
            } else {
                //altrimenti la scalo e basta    
                statciGif = new ImageIcon(emotion.getImageIcon().getImage().getScaledInstance(30, 30, Image.SCALE_AREA_AVERAGING));
            }

        } catch (IOException ex) {
            log.error(ex);
        } catch (Exception e) {
            log.error(e);
        }

        setIcon(statciGif);
        setText(emotion.getName() + "  " + emotion.getShortcut());
        return this;

//        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    }
}

/**
 * Il pannello di preview delle emotions
 * @author Luigi
 */
class EmotionPreviewPanel extends JPanel {

    EmoticonsManageFrame manageFrame;
    Log log = LogFactory.getLog(this.getClass());

    public EmotionPreviewPanel(EmoticonsManageFrame manageFrame) {
        this.manageFrame = manageFrame;

    }

    @Override
    public void paint(Graphics g) {

        if (manageFrame.getToRender() != null) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.setColor(Color.GRAY);
            g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            ImageIcon image = manageFrame.getToRender().getImageIcon();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            g2d.drawImage(image.getImage(), (getWidth() / 2) - (image.getIconWidth() / 2), (getHeight() / 2) - (image.getIconHeight() / 2), this);

        } else {
            JList emoticonList = manageFrame.getEmoticonslist();
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.setColor(Color.GRAY);
            g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            Emoticon emotion = (Emoticon) emoticonList.getSelectedValue();
            if (emotion != null) {
                ImageIcon image = emotion.getImageIcon();

                g2d.drawImage(image.getImage(), (getWidth() / 2) - (image.getIconWidth() / 2), (getHeight() / 2) - (image.getIconHeight() / 2), this);
            }
        }
    }
}


