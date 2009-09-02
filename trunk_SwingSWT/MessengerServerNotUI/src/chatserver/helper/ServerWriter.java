/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver.helper;

import chatserver.Main;
import chatcommons.Client;
import chatcommons.datamessage.MESSAGE;
import chatcommons.datamessage.MessageManger;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static chatcommons.Commands.*;

/**
 *
 * @author Administrator
 */
public class ServerWriter {

    private Main ccv;
    private Log log = LogFactory.getLog(this.getClass());

    public ServerWriter(Main ccv) {
        this.ccv = ccv;
    }

    public void writeAll(MESSAGE message) {

        //scrivo sulla chat del server
//        writeToChatText("write all : " + MessageManger.messageToStringFormatted(message));
        log.debug("write all : " + MessageManger.messageToStringFormatted(message));

        ConcurrentHashMap<String, Client> clientsList = ccv.getClients();
        synchronized (clientsList) {
            //invio a tutti il messaggio

            //in qiesta lista saòvo tutti gli utenti che hanno dato errore nel socket
            List<Client> toRemoves = new LinkedList<Client>();

            for (Client client : clientsList.values()) {
                if (client.getMainSocket() != null) {
                    try {
                        OutputStream outputStream = client.getMainSocket().getOutputStream();
                        MessageManger.directWriteMessage(message, outputStream);

                        //se il client non risponde lo levo dalla lista dei connessi
                    } catch (Exception ex) {
                        log.warn(ex + " " + ex.getMessage());

                        //aggiungo alla lista il cliet che ha doto errore
                        toRemoves.add(client);
                    }
                }
            }

            //rimuovo tutti i client che risultno cascati
            //e invio il comando a tutti
            for (Client client : toRemoves) {
                clientsList.remove(client.getNick());
            }

            //avviso tutti di eliminare gli utenti che hanno dato errore
            for (Client toRemove : toRemoves) {
                ServerWriter sw = new ServerWriter(ccv);
                MESSAGE toSend = MessageManger.createCommand(Command.REMOVEUSER, null);
                MessageManger.addParameter(toSend, "nick", toRemove.getNick());
                sw.writeAll(toSend);
            }
        }

    }

    public void writeToClient(String nick, MESSAGE message) {

        if (!message.getType().equals(MESSAGE)) {
//            writeToChatText("write to \"" + nick + " : " + MessageManger.messageToStringFormatted(message));
        }
        log.debug("write to \"" + nick + " : " + MessageManger.messageToStringFormatted(message));

        //cerco il client in base al nick e lo gli invio il messaggio
        ConcurrentHashMap<String, Client> clientsList = ccv.getClients();
        Client toRemove = null;

//        synchronized (clientsList) {

        Client client = clientsList.get(nick);
        OutputStream outputStream = null;
        try {
//                    System.out.println("Server writer sendToClient   "+ client.getNick()+" : " +line);
            outputStream = client.getMainSocket().getOutputStream();
            MessageManger.directWriteMessage(message, outputStream);

        } catch (SocketException ex) {
            toRemove = client;
            log.error(ex);

        } catch (Exception ex) {
            log.warn(ex);
            log.debug("Resend message : " + MessageManger.messageToStringFormatted(message) + " to client " + nick);
            writeToClient(nick, message);
        }

        //se c'è stato un errrore rimuovo il client
        if (toRemove != null) {
            synchronized (clientsList) {
                clientsList.remove(toRemove.getNick());
            }
            //comunico a tutti di eliminare l'utente
            ServerWriter sw = new ServerWriter(ccv);
            MESSAGE toSend = MessageManger.createCommand(Command.REMOVEUSER, null);
            MessageManger.addParameter(toSend, "nick", toRemove.getNick());
            sw.writeAll(toSend);
        }
    }

    public void writeToClientFile(String nick, MESSAGE message) {

        //invio a tutti gli altri
        ConcurrentHashMap<String, Client> clientsList = ccv.getClients();
        Client client = clientsList.get(nick);
        OutputStream outputStream = null;
        try {
//                    System.out.println("Server writer sendToClient   "+ client.getNick()+" : " +line);
            outputStream = client.getFileSenderSocket().getOutputStream();
            MessageManger.directWriteMessage(message, outputStream);
        } catch (SocketException ex) {
            synchronized (clientsList) {
                clientsList.remove(client.getNick());
            }
            //comunico a tutti di eliminare l'utente
            ServerWriter sw = new ServerWriter(ccv);
            MESSAGE toSend = MessageManger.createCommand(Command.REMOVEUSER, null);
            MessageManger.addParameter(toSend, "nick", client.getNick());
            sw.writeAll(toSend);
            log.error(ex);
        } catch (Exception ex) {
            log.warn(ex);
            log.debug("Resend message");
            writeToClientFile(nick, message);
        }

    }
}

