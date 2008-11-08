/*
 * ReceiveFileDialog.java
 *
 * Created on 5 ottobre 2008, 23.21
 */
package chatclient.forms;

import chatclient.ChatClientView;
import chatclient.commons.Util;
import chatclient.thread.ClientReader;
import chatclient.thread.FileSender;
import chatclient.thread.SocketFileConnector;
import chatcommons.datamessage.MESSAGE;
import chatcommons.datamessage.MessageManger;
import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.application.Action;
import static chatcommons.Commands.*;

/**
 *
 * @author  Administrator
 */
public class ReceiveFileDialog extends javax.swing.JFrame {

    public static final String FERMATEXT = " Ferma ";
    private Log log = LogFactory.getLog(SelectClientsToAdd.class);
    private String file;
    private String sender;
    private long fileSize;
    private int packReceived;
    private final int transferid;
    private ChatClientView ccv;
    private int packNumTot;
    private Calendar old;

    /** Creates new form ReceiveFileDialog */
    public ReceiveFileDialog(ChatClientView ccv) {
        super();
        initComponents();
        transferid = new Random(100000000).nextInt(1000000);
        this.ccv = ccv;
        progressBar.setVisible(false);
        percenLabel.setVisible(false);
        ccv.getHelper().getFileDialogs().add(this);
        // <editor-fold defaultstate="collapsed" desc="Window Icon"> 
        try {
            setIconImage(ccv.getResourceMap().getImageIcon("trayIcon").getImage());
        } catch (Exception e) {
            log.warn(e);
        }
    // </editor-fold>

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        percenLabel = new javax.swing.JLabel();
        infoLabel = new javax.swing.JLabel();
        acceptButton = new javax.swing.JButton();
        progressBar = new javax.swing.JProgressBar();
        refuseButton = new javax.swing.JButton();

        setName("Form"); // NOI18N
        setResizable(false);

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(chatclient.ChatClientApp.class).getContext().getResourceMap(ReceiveFileDialog.class);
        percenLabel.setText(resourceMap.getString("percenLabel.text")); // NOI18N
        percenLabel.setName("percenLabel"); // NOI18N

        infoLabel.setText(resourceMap.getString("infoLabel.text")); // NOI18N
        infoLabel.setName("infoLabel"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(chatclient.ChatClientApp.class).getContext().getActionMap(ReceiveFileDialog.class, this);
        acceptButton.setAction(actionMap.get("acceptReceive")); // NOI18N
        acceptButton.setText(resourceMap.getString("acceptButton.text")); // NOI18N
        acceptButton.setName("acceptButton"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        refuseButton.setAction(actionMap.get("refuse")); // NOI18N
        refuseButton.setName("refuseButton"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(infoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)
                            .addContainerGap())
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                            .addGap(18, 18, 18)
                            .addComponent(percenLabel)
                            .addGap(107, 107, 107)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(refuseButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(acceptButton)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(infoLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(percenLabel))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(acceptButton)
                    .addComponent(refuseButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton acceptButton;
    private javax.swing.JLabel infoLabel;
    private javax.swing.JLabel percenLabel;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JButton refuseButton;
    // End of variables declaration//GEN-END:variables
    // <editor-fold defaultstate="collapsed" desc="Getter and setter">        
    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Log getLog() {
        return log;
    }

    public void setLog(Log log) {
        this.log = log;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public JLabel getStatusLabel() {
        return percenLabel;
    }

    public void setStatusLabel(JLabel statusLabel) {
        this.percenLabel = statusLabel;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public JLabel getInfoLabel() {
        return infoLabel;
    }

    public void setInfoLabel(JLabel infoLabel) {
        this.infoLabel = infoLabel;
    }

    public int getPackReceived() {
        return packReceived;
    }
    // </editor-fold>
    /**
     * this is not only set method, contains receive logic
     * @param packReceived
     */
    public void setPackReceived(int packReceived) {

        Calendar now = Calendar.getInstance();

        //variables
        int percent = 0;
        int cont = 1;
        int kbTot = 0;

        //numero dei pèacchetti totali attesi
        double totPoints = 1000;
        progressBar.setMaximum((int) totPoints);

        packNumTot = (int) ((fileSize / FileSender.BYTESFORMESSAGE));
        if (fileSize % FileSender.BYTESFORMESSAGE != 0) {
            packNumTot++;
        }
        this.packReceived = packReceived;
        log.debug("packReceived : " + packReceived);

        //tempo di invio di un pacchetto in millisecondi
        double time = now.getTimeInMillis() - old.getTimeInMillis();
        //tempo di invio di un pacchetto in secondi
        double seconds = time / 1000;
        log.debug("seconds " + seconds);

        // kB/sec instantanei 
        int kbSecInst = (int) ((FileSender.BYTESFORMESSAGE / 1024) / seconds);
        kbTot = (kbTot + kbSecInst);

        // kB/sec medi
        int averageKbsec = kbTot / cont;

        //progresso della stausBar
        double pointsToShow = (totPoints / packNumTot) * packReceived;
        log.trace("pointsToShow : " + pointsToShow);
        progressBar.setValue((int) pointsToShow);

        //percentuale di trasferimento
        percent = (int) pointsToShow / 10;
        log.trace("percent : " + percent);

        percenLabel.setText(percent + "%");
        infoLabel.setText("Ricezione a " + averageKbsec + " kB/sec");

        old = now;
        if (packReceived == packNumTot) {
            destroy();
        }
    }

    /**
     * accetta la richiesta di invio file
     */
    @Action
    public void acceptReceive() {

        if (acceptButton.getText().equals(FERMATEXT)) {
            refuse();
        }

        refuseButton.setVisible(false);

        log.debug("accpted request FILETRANSFR");

        //verifico che la cartella di download sia valida sennò gliela faccio impostare


        String download_folder = Util.readProperties().getProperty(Util.DOWNLOAD_FOLDER);
        File folder = new File(download_folder);

        if (!folder.isDirectory()) {
            JOptionPane.showMessageDialog(this, "La certella per il dowload non è vailda!", "Cartella non valida", JOptionPane.WARNING_MESSAGE);
            OptionsDialog dialog = new OptionsDialog(ccv.getFrame(), false);
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);

            return;
        }

        //creo un fileSocketse nn esiste e gli assegno un client reader
        if (ccv.getFileSocket() == null || !ccv.getFileSocket().isConnected()) {
            SocketFileConnector connector = new SocketFileConnector(ccv);
            connector.execute();

            while (!connector.isConnected()) {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException ex) {
                    log.error(ex);
                }
            }

            ccv.setFileSocket(connector.getSocket());
            try {
                ccv.setFileOutputStream(connector.getSocket().getOutputStream());
                ClientReader clientReader = new ClientReader(ccv.getFileSocket(), ccv, ClientReader.FILEREADER);
                clientReader.execute();
            } catch (IOException ex) {
                log.error(ex);
            }
        }

        //ivio l'OK di ricezione
        List<String> receivers = new LinkedList<String>();
        receivers.add(sender);

//        Map<String,String> paramters = new HashMap<String,String>();
//        paramters.put("file", file);
//        paramters.put("filesize", fileSize + "");
//        paramters.put("response", Command.OK);

        MESSAGE requestAccpet = MessageManger.createRequest(Request.FILEACCEPTRESPONSE, receivers, null);
        MessageManger.addParameter(requestAccpet,"file", file);
        MessageManger.addParameter(requestAccpet,"filesize", fileSize + "");
        MessageManger.addParameter(requestAccpet,"response", Command.OK);
        
        try {
            MessageManger.directWriteMessage(requestAccpet, ccv.getOutputStream());
        } catch (SocketException socketException) {
            log.error(socketException);
        }

        //grafica
        acceptButton.setText(FERMATEXT);
        progressBar.setVisible(true);
        percenLabel.setVisible(true);
        percenLabel.setText("0%");
        infoLabel.setText("Ricezione a ...");
        old = Calendar.getInstance();

    }

    /**
     * Chiude la finestra e la elimina dalla coda dei trasferimanti attivi
     */
    public void destroy() {
        ccv.getHelper().getFileDialogs().remove(this);
        this.setVisible(false);
    }

    /**
     * Chiude la finestra e invia il messaggio no
     */
    @Action
    public void refuse() {
        //ivio la risopsta negativa 
        //ivio l'OK di ricezione
        List<String> receivers = new LinkedList<String>();
        receivers.add(sender);

//        Map<String,String> paramters = new HashMap<String, String>();
//        paramters.put("file", file);
//        paramters.put("filesize", fileSize + "");
//        paramters.put("response", Command.KO);

        MESSAGE requestAccpet = MessageManger.createRequest(Request.FILEACCEPTRESPONSE, receivers, null);
        MessageManger.addParameter(requestAccpet,"file", file);
        MessageManger.addParameter(requestAccpet,"filesize", fileSize + "");
        MessageManger.addParameter(requestAccpet,"response", Command.KO);
        
        try {
            MessageManger.directWriteMessage(requestAccpet, ccv.getOutputStream());
        } catch (SocketException socketException) {
            log.error(socketException);
        }
        //chiudo la finestra
        destroy();
        
        //cancello il file
        File toDelete = new File(file);
        if(toDelete.isFile()){
            toDelete.delete();
        }
    }
}
