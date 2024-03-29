/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gg.msn.core.thread;

import gg.msn.core.commons.Util;
import chatcommons.datamessage.MESSAGE;
import chatcommons.datamessage.MessageManger;
import gg.msn.core.manager.PersistentDataManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static chatcommons.Commands.*;

/**
 *
 * @author Luigi
 */
public class SocketFileConnector implements Runnable {

    private Log log = LogFactory.getLog(SocketFileConnector.class);
    private boolean connected;
    private Socket socket;

    public SocketFileConnector() {

        connected = false;
    }

    public void run() {
        try {
            Socket newSocket = new Socket();
            newSocket.connect(new InetSocketAddress(PersistentDataManager.getIp(), PersistentDataManager.getPort()));
            OutputStream os = newSocket.getOutputStream();
            MESSAGE message = MessageManger.createCommand(Command.ADDFILESOCKET, null);
//        message.getParameters().add(0, ccv.getNick());
            MessageManger.addParameterAt(message, "nick", PersistentDataManager.getNick(), 0);
            log.debug("send message : " + MessageManger.messageToStringFormatted(message));
            MessageManger.directWriteMessage(message, os);
            InputStream is = newSocket.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is, Util.DEFAULTENCODING));
            String line = in.readLine();
            MESSAGE serverResponse = MessageManger.parseXML(line);
            log.debug("xml received : " + MessageManger.messageToStringFormatted(serverResponse));
            if (serverResponse.getType().equals(COMMAND) && serverResponse.getName().equals(Command.ADDFILESOCKET) && serverResponse.getParameters().getParameter().get(0).getValue().equals(Command.OK)) {
                socket = newSocket;
                connected = true;
            }
        } catch (IOException ex) {
            log.error(ex);
        }

    }

    // <editor-fold defaultstate="collapsed" desc=" Getter and setter ">
    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }// </editor-fold>
}
