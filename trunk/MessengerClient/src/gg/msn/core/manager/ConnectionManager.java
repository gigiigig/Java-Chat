/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gg.msn.core.manager;

import chatcommons.Commands.Command;
import chatcommons.datamessage.MESSAGE;
import chatcommons.datamessage.MessageManger;
import gg.msn.ui.ChatClientView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Luigi
 */
public class ConnectionManager {

    private Log log = LogFactory.getLog(ConnectionManager.class);

    public String connect(Socket socket, InetSocketAddress inetSocketAddress, String nick) throws IOException, SocketException {
        socket.connect(inetSocketAddress);
        log.info("client : connect on port : " + inetSocketAddress.getPort());
        //invio al server il nick name
        MESSAGE request = MessageManger.createCommand(Command.CONNECT, new HashMap<String, String>());
        MessageManger.addParameter(request, "nick", nick);
        MessageManger.directWriteMessage(request, socket.getOutputStream());
        //leggo se il nick è stato accettato
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String accptedNick = reader.readLine();
        MESSAGE message = MessageManger.parseXML(accptedNick);
        log.debug(nick + " read response : " + MessageManger.messageToStringFormatted(message));
        String response = message.getParameters().getParameter().get(0).getValue();
        log.debug("response value :" + response);
        return response;
    }

     public  void disconnect(Socket socket) throws IOException, SocketException {
        MESSAGE toSend = MessageManger.createCommand(Command.DISCONNECT, null);
        log.debug("send : " + MessageManger.messageToStringFormatted(toSend));
        MessageManger.directWriteMessage(toSend, socket.getOutputStream());
        //TODO metti nel finalli i comendi da eseguire per forza
        socket.close();
    }
}
