/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * FBLoginPanel.java
 *
 * Created on Aug 25, 2009, 11:37:42 PM
 */
package gg.msn.ui.facebook.panel;

import gg.msn.core.commons.DesEncrypter;
import gg.msn.facebook.core.ErrorCode;
import gg.msn.facebook.core.FacebookUserList;
import gg.msn.facebook.core.FacebookManager;
import gg.msn.core.commons.Util;
import gg.msn.core.manager.PersistentDataManager;
import gg.msn.ui.ChatClientView;
import gg.msn.ui.AnotherLinkButton;
import gg.msn.ui.ChatClientApp;
import gg.msn.ui.facebook.thread.BuddyListRequester;
import gg.msn.ui.facebook.thread.MessageRequester;
import gg.msn.ui.theme.ThemeManager;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Properties;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.application.Action;

/**
 *
 * @author Luigi
 */
public class FBLoginPanel extends javax.swing.JPanel {

    private static Log log = LogFactory.getLog(FBLoginPanel.class);
    /** Creates new form FBLoginPanel */
    private final ChatClientView ccv;

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        emailLabel = new javax.swing.JLabel();
        psswLabel = new javax.swing.JLabel();
        emailText = new javax.swing.JTextField();
        loginButton = new javax.swing.JButton();
        connectionStatusLabel = new javax.swing.JLabel();
        passwText = new javax.swing.JPasswordField();
        saveMailCheck = new javax.swing.JCheckBox();
        savePswCheck = new javax.swing.JCheckBox();

        setName("Form"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(gg.msn.ui.ChatClientApp.class).getContext().getResourceMap(FBLoginPanel.class);
        emailLabel.setText(resourceMap.getString("emailLabel.text")); // NOI18N
        emailLabel.setName("emailLabel"); // NOI18N

        psswLabel.setText(resourceMap.getString("psswLabel.text")); // NOI18N
        psswLabel.setName("psswLabel"); // NOI18N

        emailText.setText(resourceMap.getString("emailText.text")); // NOI18N
        emailText.setName("emailText"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(gg.msn.ui.ChatClientApp.class).getContext().getActionMap(FBLoginPanel.class, this);
        loginButton.setAction(actionMap.get("connect")); // NOI18N
        loginButton.setText(resourceMap.getString("loginButton.text")); // NOI18N
        loginButton.setName("loginButton"); // NOI18N

        connectionStatusLabel.setText(resourceMap.getString("connectionStatusLabel.text")); // NOI18N
        connectionStatusLabel.setName("connectionStatusLabel"); // NOI18N

        passwText.setText(resourceMap.getString("passwText.text")); // NOI18N
        passwText.setName("passwText"); // NOI18N

        saveMailCheck.setText(resourceMap.getString("saveMailCheck.text")); // NOI18N
        saveMailCheck.setName("saveMailCheck"); // NOI18N
        saveMailCheck.setOpaque(false);

        savePswCheck.setText(resourceMap.getString("savePswCheck.text")); // NOI18N
        savePswCheck.setName("savePswCheck"); // NOI18N
        savePswCheck.setOpaque(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(psswLabel)
                            .addComponent(emailLabel))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(saveMailCheck)
                            .addComponent(savePswCheck)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(passwText, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(emailText, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(112, 112, 112)
                        .addComponent(loginButton, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(connectionStatusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(32, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(156, 156, 156)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(emailLabel)
                    .addComponent(emailText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(psswLabel)
                    .addComponent(passwText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(saveMailCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(savePswCheck)
                .addGap(13, 13, 13)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(connectionStatusLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(loginButton))
                .addContainerGap(75, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel connectionStatusLabel;
    private javax.swing.JLabel emailLabel;
    private javax.swing.JTextField emailText;
    private javax.swing.JButton loginButton;
    private javax.swing.JPasswordField passwText;
    private javax.swing.JLabel psswLabel;
    private javax.swing.JCheckBox saveMailCheck;
    private javax.swing.JCheckBox savePswCheck;
    // End of variables declaration//GEN-END:variables

    public FBLoginPanel(ChatClientView ccv) {
        super();
        initComponents();
        this.ccv = ccv;


        //add nickText ENTER keystroke
//        emailText.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "Enter");
//        emailText.getActionMap().put("Enter", getActionMap().get("connect"));
        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        emailText.getInputMap().put(enter, "Enter");
        javax.swing.Action action = getActionMap().get("connect");
        log.debug("action "+action);
        passwText.getActionMap().put("Enter",action);
        passwText.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "Enter");
        passwText.getActionMap().put("Enter", getActionMap().get("connect"));

        //carico i valori salvati se ci sono
        String email = Util.readProperties().getProperty(Util.PROPERTY_FACEBOOK_EMAIL);
        if (email != null && !email.equals("")) {
            emailText.setText(email);
        }
        String psw = Util.readProperties().getProperty(Util.PROPERTY_FACEBOOK_PSW);
        log.debug("crypted passworda [" + psw + "]");
        psw = decryptPass(psw);
        log.debug("decrypted psw [" + psw + "]");
        if (psw != null && !psw.equals("")) {
            passwText.setText(psw);
        }

        //quando dechekko l'email dechekko anche la password
        saveMailCheck.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (!saveMailCheck.isSelected()) {
                    savePswCheck.setSelected(saveMailCheck.isSelected());
//                }            }
//            public void stateChanged(ChangeEvent e) {
//
//            }
                }
            }
        });

        //quando chekko l'email chekko anche la password
        savePswCheck.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                if (savePswCheck.isSelected()) {
                    saveMailCheck.setSelected(savePswCheck.isSelected());
                }
            }
        });
        String propertySaveEmail = Util.readProperties().getProperty(Util.PROPERTY_SAVE_FACEBOOK_EMAIL);
        log.debug("property saveEmail [" + propertySaveEmail + "]");
        boolean saveEmail = BooleanUtils.toBoolean(propertySaveEmail);
        log.debug("saveEmail [" + saveEmail + "]");
        saveMailCheck.setSelected(saveEmail);
        boolean savePsw = BooleanUtils.toBoolean(Util.readProperties().getProperty(Util.PROPERTY_SAVE_FACEBOOK_PSW));
        log.debug("savePsw [" + savePsw + "]");
        savePswCheck.setSelected(savePsw);

    }

    private void connectStart() {
        String email = emailText.getText().trim();
        char[] passArr = passwText.getPassword();
        String pass = new String(passArr);
        //salvo i valori inserioti su email e password
        Properties prop = Util.readProperties();
        boolean saveEmail = saveMailCheck.isSelected();
        boolean savePsw = savePswCheck.isSelected();

        if (saveEmail) {
            prop.setProperty(Util.PROPERTY_FACEBOOK_EMAIL, email);
        } else {
            prop.setProperty(Util.PROPERTY_FACEBOOK_EMAIL, "");
        }
        if (savePsw) {
            prop.setProperty(Util.PROPERTY_FACEBOOK_PSW, encryptPass(passArr));
        } else {
            prop.setProperty(Util.PROPERTY_FACEBOOK_PSW, "");
        }

        prop.setProperty(Util.PROPERTY_SAVE_FACEBOOK_EMAIL, saveMailCheck.isSelected() + "");
        prop.setProperty(Util.PROPERTY_SAVE_FACEBOOK_PSW, savePswCheck.isSelected() + "");


        Util.writeProperties(prop);
        /*String email = "username@email.com.cn";
        String pass = "password";*/

        log.debug(email + ":" + pass);
        final FacebookManager fbManger = new FacebookManager(email, pass);

        int loginErrorCode = fbManger.doLogin();
        if (loginErrorCode == ErrorCode.Error_Global_NoError) {
            if (fbManger.doParseHomePage() == ErrorCode.Error_Global_NoError) {
                int seqNumber = fbManger.findChannel();
                if (seqNumber < 0) {
                    ccv.getHelper().showErrorDialog("Impossibile connettersi, provare più tardi!");
                    return;
                }
                PersistentDataManager.setUid(FacebookManager.uid);
                FacebookManager.getBuddyList();
                ChatClientView.protocol = ChatClientView.FACEBOOK_PROTOCOL;
                PersistentDataManager.setNick(FacebookUserList.me.name);

                //keep requesting message from the server
                new Thread(new MessageRequester(ccv, fbManger, email, pass)).start();

                //requests buddy list every 90 seconds
                new Thread(new BuddyListRequester(ccv)).start();

                //TODO post
                //Init GUI
                log.debug("Init GUI...");

                ccv.getMainPanel().getNickLabel().setText(FacebookUserList.me.name);
                ccv.getMainPanel().insertIcons();
                ccv.getHelper().showMainPanel();
                ccv.getMenuBar().setVisible(false);
                if (ccv.getTray() != null) {
                    ccv.getTray().setToolTip("Facebook - connesso : " + PersistentDataManager.getNick());
                }
                log.debug("showed main panel");
                //必须在getbuddylist之后
//                fbc = new Cheyenne();
//                fbc.setVisible(true);
            } else {
                ccv.getHelper().showWarnDialog("Non è stato possibile effettuare il login.<br>" +
                        "Ricontrolla email e password!");
                connectionStatusLabel.setVisible(false);
                loginButton.setVisible(true);
            }
        } else if (loginErrorCode == ErrorCode.kError_Async_NotLoggedIn) {
            //TODO handle the error derived from this login
            JOptionPane.showMessageDialog(
                    ccv.getFrame(), "Not logged in, please check your input!",
                    "Not Logged In",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(
                    ccv.getFrame(), "Not logged in, please check your internet connection!",
                    "Not Logged In",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Action
    public void connect() {
        loginButton.setVisible(false);
        connectionStatusLabel.setText("Connessione...");
        connectionStatusLabel.validate();

        new Thread(new Runnable() {

            public void run() {
                connectStart();
            }
        }).start();
    }

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

    @Action
    public void showFacebookLogin() {
        ccv.getHelper().showFacebookLoginPanel();
    }

    public static void main(String[] args) {
//        FacebookManager facebookManager = new FacebookManager();
//        facebookManager.doLogin("luigi.ant@email.it", "03021984");
//        System.out.println(FacebookManager.facebookGetMethod("http://0.channel35.facebook.com/x/0/false/p_1567835536=-1"));
//        System.out.println(FacebookManager.facebookGetMethod("http://www.google.com"));
    }

    String encryptPass(char[] passArr) {
        String pass = "";
        try {
            // Generate a temporary key. In practice, you would save this key.
            // See also e464 Encrypting with DES Using a Pass Phrase.
            SecretKey key = KeyGenerator.getInstance("DES").generateKey();
            // Create encrypter/decrypter class
            DesEncrypter encrypter = new DesEncrypter("flac");
            // Encrypt
            pass = encrypter.encrypt(new String(passArr));
            // Decrypt
        } catch (Exception e) {
            log.error(e);
        }
        return pass;
    }

    private String decryptPass(String pass) {

        try {
            // Generate a temporary key. In practice, you would save this key.
            // See also e464 Encrypting with DES Using a Pass Phrase.
            SecretKey key = KeyGenerator.getInstance("DES").generateKey();
            // Create encrypter/decrypter class
            DesEncrypter encrypter = new DesEncrypter("flac");
            // Encrypt
            pass = encrypter.decrypt(pass);
            // Decrypt
        } catch (Exception e) {
            log.error(e);
        }
        return pass;
    }
}
