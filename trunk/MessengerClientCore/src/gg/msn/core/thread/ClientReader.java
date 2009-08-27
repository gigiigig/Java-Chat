/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gg.msn.core.thread;

import gg.msn.core.commons.Util;
import chatcommons.datamessage.MESSAGE;
import chatcommons.datamessage.MessageManger;
import gg.msn.core.listener.AbstractMessageListener;
import gg.msn.core.manager.PersistentDataManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static chatcommons.Commands.*;

/**
 *
 * @author Administrator
 */
public class ClientReader implements Runnable {

    private Log log = LogFactory.getLog(this.getClass());
    // private ChatClientView ccv;
    private AbstractMessageListener listener;
    private String type;
    public static String MAINREADER = "MAINREADER";
    public static String FILEREADER = "FILEREADER";

    public ClientReader(AbstractMessageListener listener, String type) {
        //this.ccv = ccv;
        this.listener = listener;
        //this.so = so;
        this.type = type;
    }

    public void run() {
        try {
            startRead();
        } catch (UnsupportedEncodingException ex) {
            log.error(ex);
        } catch (SocketException ex) {
            log.error(ex);
        } catch (IOException ex) {
            log.error(ex);
        }


    }

    private void startRead() throws UnsupportedEncodingException, IOException, SocketException {
        //leggo l'input stream e lo sscrivo sulla chat riga per riga
        InputStream is = null;
        MESSAGE responseConn = MessageManger.createCommand(Command.CONNECT, null);
        MessageManger.addParameter(responseConn, "response", Command.OK);
        BufferedReader in;
        MessageManger.directWriteMessage(responseConn, listener.getOutputStream());
//            ccv.getHelper().sendRequest("~OK\n");
        is = PersistentDataManager.getSocket().getInputStream();
        in = new BufferedReader(new InputStreamReader(is, Util.DEFAULTENCODING));
        String line = "";
        
        while ((line = in.readLine()) != null) {
            try {
                if (line.startsWith("<")) {
                    MESSAGE message = MessageManger.parseXML(line);
                    //non loggo gli xml di dati perchè so troppo lunghi
                    if (!message.getName().equals(Request.FILETRANSFER)) {
                        log.debug(listener.getNick() + " " + type + " reder receive MESSAGE : " + MessageManger.messageToStringFormatted(message));
                    } else {
                        log.debug(listener.getNick() + " " + type + " FILETRANSFER RECEIVED");
                    }

                    listener.parseMessage(message);
                }
            } catch (Exception e) {
                log.error(e);
            }
        }

        is.close();

    }
}


        

  

