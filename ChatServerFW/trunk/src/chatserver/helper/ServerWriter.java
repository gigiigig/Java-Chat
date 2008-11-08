/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver.helper;

import chatserver.ChatServerView;
import chatcommons.Client;
import chatcommons.datamessage.MESSAGE;
import chatcommons.datamessage.MessageManger;
import chatserver.commons.Util;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static chatcommons.Commands.*;

/**
 *
 * @author Administrator
 */
public class ServerWriter {

    private ChatServerView ccv;
    private Log log = LogFactory.getLog(this.getClass());

    public ServerWriter(ChatServerView ccv) {
        this.ccv = ccv;
    }

    public void writeAll(MESSAGE message) {
        //scrivo sulla chat del server
        writeToChatText("write all : " + MessageManger.messageToStringFormatted(message));
        log.debug("write all : " + MessageManger.messageToStringFormatted(message));


        //invio a tutti gli altri
        List<Client> clientsList = ccv.getClients();
        ListIterator<Client> li = clientsList.listIterator();
        List<Client> toRemoves = new LinkedList<Client>();
        while (li.hasNext()) {
            Client client = li.next();
            if (client.getMainSocket() != null) {
                try {
                    OutputStream outputStream = client.getMainSocket().getOutputStream();
                    MessageManger.directWriteMessage(message, outputStream);

                //se il client non risponde lo levo dalla lista dei connessi
                } catch (Exception ex) {
                    log.warn(ex + " " + ex.getMessage());
//                    System.out.println("Server redaer : " + ex.getMessage());

                    toRemoves.add(client);
//                    //elimino il client e riallineo l'iteratore
//                    clientsList.remove(client);
//                    ccv.refreshTable();
//                    log.debug("li.hasNext() : " + li.hasNext());
//                    if (li.hasNext()) {
//                        int pos = li.nextIndex();
//                        li = clientsList.listIterator(pos - 1);
//                        log.debug("li.hasNext() : " + li.hasNext());
//
//                    }
//
//                     //comunico a tutti di eliminare l'utente
//                    writeAll(COMMAND + Command.REMOVEUSER + client.getNick());

                }
            }
        }

        //rimuovo tutti i client che risultno cascati
        //e invio il comando a tutti
        synchronized (clientsList) {
            clientsList.removeAll(toRemoves);

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
            writeToChatText("write to \"" + nick + " : " + MessageManger.messageToStringFormatted(message));
        }
        log.debug("write to \"" + nick + " : " + MessageManger.messageToStringFormatted(message));

        //invio a tutti gli altri
        List<Client> clientsList = ccv.getClients();
        ListIterator<Client> li = clientsList.listIterator();
        while (li.hasNext()) {
            Client client = li.next();
            if (client.getNick().equals(nick)) {
                OutputStream outputStream = null;
                try {
//                    System.out.println("Server writer sendToClient   "+ client.getNick()+" : " +line);
                    outputStream = client.getMainSocket().getOutputStream();
                    MessageManger.directWriteMessage(message, outputStream);

                    break;
                } catch (SocketException ex) {
                    synchronized (clientsList) {
                        clientsList.remove(client);
                    }
                    //comunico a tutti di eliminare l'utente
                    ServerWriter sw = new ServerWriter(ccv);
                    MESSAGE toSend = MessageManger.createCommand(Command.REMOVEUSER, null);
                    MessageManger.addParameter(toSend, "nick", client.getNick());
                    sw.writeAll(toSend);
                    log.error(ex);
                } catch (Exception ex) {
                    log.warn(ex);
                    log.debug("Resend message : " + MessageManger.messageToStringFormatted(message) + " to client " + nick);
                    writeToClient(nick, message);
                }
            }
        }
    }

    public void writeToClientFile(String nick, MESSAGE message) {

        //invio a tutti gli altri
        List<Client> clientsList = ccv.getClients();
        ListIterator<Client> li = clientsList.listIterator();
        while (li.hasNext()) {
            Client client = li.next();
            if (client.getNick().equals(nick)) {
                OutputStream outputStream = null;
                try {
//                    System.out.println("Server writer sendToClient   "+ client.getNick()+" : " +line);
                    outputStream = client.getFileSenderSocket().getOutputStream();

                    MessageManger.directWriteMessage(message, outputStream);

                    break;
                } catch (SocketException ex) {
                    synchronized (clientsList) {
                        clientsList.remove(client);
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
    }

    public String writeToChatText(String line) {
        //scrivo sulla chat del server

        JTextPane chatText = ccv.getChatText();
        StyledDocument doc = chatText.getStyledDocument();
        try {
            doc.insertString(doc.getLength(), line + "\n", null);
        } catch (BadLocationException ex) {
            Logger.getLogger(ServerWriter.class.getName()).log(Level.SEVERE, null, ex);
        }

        chatText.setCaretPosition(chatText.getStyledDocument().getLength());

//         ccv.getChatText().setText(ccv.getChatText().getText().concat(line));
        return line;
    }
}


