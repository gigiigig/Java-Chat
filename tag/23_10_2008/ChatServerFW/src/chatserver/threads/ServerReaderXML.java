/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver.threads;

import chatserver.ChatServerView;
import chatcommons.Client;
import chatcommons.datamessage.MESSAGE;
import chatcommons.datamessage.MessageManger;
import chatserver.commons.Util;
import static chatcommons.Commands.*;
import chatserver.helper.ServerWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import javax.swing.SwingWorker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Administrator
 */
public class ServerReaderXML extends SwingWorker {

    public static String MAINREADER = "MAINREADER";
    public static String FILEREADER = "FILEREADER";
    private Client client;
    private Log log = LogFactory.getLog(this.getClass());
    private ChatServerView cv;
    private boolean active;
    private String type;

    public ServerReaderXML(Client client, ChatServerView cv, String type) {
        this.client = client;
        this.cv = cv;
        active = true;
        this.type = type;

    }

    @Override
    protected Object doInBackground() throws Exception {
        try {
            log.info("Start new reder for : " + client.getNick());

            //leggo l'input stream e lo sscrivo sulla chat riga per riga
            InputStream is = null;

            if (type.equals(MAINREADER)) {
                is = client.getMainSocket().getInputStream();
            } else if (type.equals(FILEREADER)) {
                is = client.getFileSenderSocket().getInputStream();
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(is, Util.DEFAULTENCODING));

            //StringBuffer xmlBuffer = new StringBuffer();
            String line = "";
            ServerWriter sw = new ServerWriter(cv);

            //leggo le strigne e le stampo a tutti clients

            StringBuffer buffer = new StringBuffer();
            while (active && (line = in.readLine().trim()) != null) {
                try {

                    MESSAGE messObj = MessageManger.parseXML(line);
                    if (messObj.getType().equals(COMMAND) && messObj.getName().equals(Command.DISCONNECT)) {

                        client.getMainSocket().close();

                        cv.getClients().remove(client);
                        cv.refreshTable();

                        MESSAGE toSend = MessageManger.createCommand(Command.REMOVEUSER, null);
                        MessageManger.addParameter(toSend, "nick", client.getNick());

                        log.debug("writeAll : " + toSend);
                        sw.writeAll(toSend);
                        active = false;

                    } else {

                        if (!messObj.getName().equals(Request.FILETRANSFER)) {
                            log.debug("received on : \"" + client.getNick() + " " + type + " \" : " + MessageManger.messageToStringFormatted(messObj));
                            if (!messObj.getType().equals(MESSAGE)) {
                                sw.writeToChatText("received on : \"" + client.getNick() + " " + type + " \" : " + MessageManger.messageToStringFormatted(messObj));
                            }
                        } else {
                            log.debug("received on: \"" + client.getNick() + " " + type + " \" : " + Request.FILETRANSFER + " for " + messObj.getReceivers().getReceiver().get(0));
                            sw.writeToChatText("received on: \"" + client.getNick() + " " + type + " \" : " + Request.FILETRANSFER + " for " + messObj.getReceivers().getReceiver().get(0));

                        }
//                    log.debug(MessageManger.messageToString(messObj));
                        messObj.setSender(client.getNick());

                        List receivers = messObj.getReceivers().getReceiver();
                        for (Object receiver : receivers) {
                            if (messObj.getName().equals(Request.FILETRANSFER)) {
                                sw.writeToClientFile((String) receiver, messObj);
                            } else {
                                sw.writeToClient((String) receiver, messObj);
                            }
                        }
                    }
//               
                } catch (Exception ex) {
                    log.debug(ex);
                }
            }
            return null;
        } catch (IOException ex) {
            log.error(client.getNick() + " reader : " + ex);
            cv.getClients().remove(client);
            ServerWriter sw = new ServerWriter(cv);
            MESSAGE toSend = MessageManger.createCommand(Command.REMOVEUSER, null);
            MessageManger.addParameter(toSend, "nick", client.getNick());
            sw.writeAll(toSend);
            return null;
        } catch (Exception ex) {
            log.error(client.getNick() + " reader : " + ex);
            return null;
        }

    }
}
