/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gg.msn.core.thread;

import chatcommons.datamessage.MESSAGE;
import chatcommons.datamessage.MessageManger;
import static chatcommons.Commands.*;
import gg.msn.ui.form.SendFileDialog;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.SwingWorker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Luigi
 */
public class FileSender extends SwingWorker {

    Log log = LogFactory.getLog(this.getClass());
    private File toSend;
    private String receiver;
    private OutputStream os;
    private SendFileDialog fileDialog;
    private boolean accepted;
    private boolean active;
    public static int BYTESFORMESSAGE = 102400;

    public FileSender(File toSend, String receiver, OutputStream os, SendFileDialog fileDialog) {
        this.toSend = toSend;
        this.receiver = receiver;
        this.os = os;
        this.fileDialog = fileDialog;
        this.accepted = false;
        this.active = true;
    }

    @Override
    protected Object doInBackground() throws Exception {
        log.debug("init");

        try {

            fileDialog.setTitle("Invio - " + toSend.getName());

            //ivio la richiesta di invio e aspetto che arrivi l'OK
            List<String> receivers = new LinkedList<String>();
            receivers.add(receiver);

//            Map<String,String> parameters = new HashMap<String, String>();
//            parameters.put("file", toSend.getName());
//            parameters.put("filesize", toSend.length() + "");

            MESSAGE requestAccpet = MessageManger.createRequest(Request.FILEACCEPTREQUEST, receivers, null);
            MessageManger.addParameter(requestAccpet,"file", toSend.getName());
            MessageManger.addParameter(requestAccpet,"filesize", toSend.length() + "");
            
            log.debug("send message to server : "+MessageManger.messageToStringFormatted(requestAccpet));
            MessageManger.directWriteMessage(requestAccpet, os);

            while (!accepted && active) {
                Thread.sleep(1000);
                log.trace("wait to accept receive : " + toSend.getName() + " from : " + receiver);
            }

            MESSAGE dataMessage = MessageManger.createFiletransfer(receivers, null, null);
            FileInputStream fis = new FileInputStream(toSend.getPath());

            //numero dei pèacchetti totali attesi
            int packNum = (int) ((toSend.length() / BYTESFORMESSAGE));
            if (toSend.length() % BYTESFORMESSAGE != 0) {
                packNum++;
            }
            log.debug("packcets num : " + packNum);

            double totPoints = 1000;
            byte[] byteArr = new byte[BYTESFORMESSAGE];

            //variables
            int percent = 0;
            int readed = 0;
            int cont = 1;
            int kbTot = 0;

            //graphics
            fileDialog.getProgressBar().setVisible(true);
            fileDialog.getProgressBar().setMaximum((int) totPoints);
            fileDialog.getPercentLabel().setVisible(true);

            while ((readed = fis.read(byteArr)) != -1 && active) {
                Calendar init = Calendar.getInstance();
                dataMessage.setData(byteArr);
                //nome del file
                MessageManger.addParameterAt(dataMessage,"file", toSend.getName(), 0);
                //numero dei bytes da scrivere 
                MessageManger.addParameterAt(dataMessage,"bytesreaded", readed + "", 1);
                //dimansione tottale del file
                MessageManger.addParameterAt(dataMessage, "filesize",toSend.length() + "", 2);
                //numero del pacchetto
                MessageManger.addParameterAt(dataMessage,"packnum", cont + "", 3);

                MessageManger.directWriteMessage(dataMessage, os);

                Calendar stop = Calendar.getInstance();

                //tempo di invio di un pacchetto in millisecondi
                double time = stop.getTimeInMillis() - init.getTimeInMillis();
                log.trace(time);
                //tempo di invio di un pacchetto in secondi
                double seconds = time / 1000;
                log.debug("seconds " + seconds);

                // kB/sec instantanei 
                int kbSecInst = (int) ((BYTESFORMESSAGE / 1024) / seconds);
                kbTot = (kbTot + kbSecInst);

                // kB/sec medi
                int averageKbsec = kbTot / cont;

                //progresso della stausBar
                double pointsToShow = (totPoints / packNum) * cont;
                log.trace("pointsToShow : " + pointsToShow);
                fileDialog.getProgressBar().setValue((int) pointsToShow);

                //percentuale di trasferimento
                percent = (int) pointsToShow / 10;
                log.trace("percent : " + percent);

                //setting of textLabels
                fileDialog.getPercentLabel().setText(percent + "%");
                fileDialog.getStatusLabel().setText("Invio file a " + averageKbsec + " kB/sec  ");

                log.debug("sended data messaege N. " + cont);
                log.debug("N byets : " + readed);
                cont++;

            }

            fis.close();
            fileDialog.destroy();

        } catch (Exception e) {
            log.error(e);
        }

        return null;
    }

    /**
     * set false this boolean to sto the tread
     * @param active
     */
    public void setActive(boolean active) {
        this.active = active;
    }
    // <editor-fold defaultstate="collapsed" desc=" Getter and Setter ">
    public OutputStream getOs() {
        return os;
    }

    public void setOs(OutputStream os) {
        this.os = os;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public File getToSend() {
        return toSend;
    }

    public void setToSend(File toSend) {
        this.toSend = toSend;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public boolean isActive() {
        return active;
    }    // </editor-fold>
}
