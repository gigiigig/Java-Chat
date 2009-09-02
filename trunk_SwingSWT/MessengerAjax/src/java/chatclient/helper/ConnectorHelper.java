/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient.helper;

import chatclient.commons.Util;
import chatclient.thread.ClientReaders;
import chatcommons.datamessage.MESSAGE;
import chatcommons.datamessage.MessageManger;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import static chatcommons.Commands.*;

/**
 *
 * @author Administrator
 */
public class ConnectorHelper {

    private Log log = LogFactory.getLog(ConnectorHelper.class);

    //dati connessione
    private String ip = "localhost";
    private int port = 3434;

    //dati dell'utente
    private String nick = "gigiAjax";
    private HttpSession session;

    //dati interni della classe
    private Socket socket;

    public ConnectorHelper(HttpSession session) {
        this.session = session;
    }

    public ConnectorHelper(HttpSession session, String ip, int port) {
        this.session = session;
        this.ip = ip;
        this.port = port;
    }

    public ConnectorHelper(HttpSession session, String nick) {
        this.session = session;
        this.nick = nick;
    }

    public ConnectorHelper(HttpSession session, String ip, int port, String nick) {
        this.session = session;
        this.ip = ip;
        this.port = port;
        this.nick = nick;
    }

    public boolean connect() {
        try {

            socket = new Socket();
            log.info("client : connect on port : " + port);

            //connetto il socket
            socket.connect(new InetSocketAddress(ip, port));
            OutputStream os = socket.getOutputStream();

            //leggo il nick
            
            //invio al server il nick name
            MESSAGE request = MessageManger.createCommand(Command.CONNECT, new HashMap<String, String>());
            MessageManger.addParameter(request, "nick", nick);

            MessageManger.directWriteMessage(request, os);

            //leggo se il nick è stato accettato
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String accptedNick = reader.readLine();

            MESSAGE message = MessageManger.parseXML(accptedNick);
            log.debug(nick + " read response : " + MessageManger.messageToStringFormatted(message));

            String response = message.getParameters().getParameter().get(0).getValue();
            log.debug("response value :" + response);

            if (response.equals(Command.KO)) {
//                        JOptionPane.showMessageDialog(ccv.getFrame(), "<html><font color=blue>Il nick scelto è già connesso<html>", "Attenzione", JOptionPane.WARNING_MESSAGE);
//                        ccv.getSocket().close();
//                        ccv.setSocket(null);
//                        ccv.setOutputStream(null);
//                        ccv.getNickText().setEnabled(true);
//                        ccv.getLogin().setEnabled(true);

                return false;
            }

            //creo nella session una lista concorrente di messaggi
            ConcurrentLinkedQueue<String> messagesQueue = new ConcurrentLinkedQueue();
            session.setAttribute("messages", messagesQueue);
            session.setAttribute("nick", nick);
            session.setAttribute("socket", socket);

            //lancio il thread
            new Thread(new ClientReaders(socket, session, ClientReaders.MAINREADER)).start();
//  
        /*catch for some exception*/
        } catch (ConnectException e) {
//                    ccv.ShowMessageFrame("<html><font color=red>Impossibile connettersi al server<html>");
            log.error(e);
        } catch (SocketException e) {
            log.error(e);
        } catch (UnknownHostException ex) {
            log.error(ex);
        } catch (IOException ex) {
            log.error(ex);
        }
        return true;
    }

    /**
     * Disconnettte dal serever principale
     */
    public void disconnect() {
        try {
            MESSAGE toSend = MessageManger.createCommand(Command.DISCONNECT, null);
            log.debug("send : " + MessageManger.messageToStringFormatted(toSend));
            OutputStream os = socket.getOutputStream();
            MessageManger.directWriteMessage(toSend, os);

            //TODO metti nel finalli i comendi da eseguire per forza

            socket.close();
        } catch (SocketException se) {
            log.error(se);

        } catch (IOException ex) {
            log.error(ex);
        }
    }

    /**
     * 
     * @param request
     */
    public void sendRequest(String request) {
        try {
            socket.getOutputStream().write(request.getBytes(Util.DEFAULTENCODING));
        } catch (SocketException se) {
            log.error(se);

        } catch (IOException ex) {
            log.error(ex);
        }
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
