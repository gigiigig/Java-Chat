/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver.threads;

import chatcommons.Client;
import chatcommons.datamessage.MESSAGE;
import chatcommons.datamessage.MessageManger;
import chatserver.*;
import chatserver.helper.ServerWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JTextPane;
import javax.swing.SwingWorker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static chatcommons.Commands.*;

/**
 *
 * @author Administrator
 */
public class ServerAcceptor extends SwingWorker {

    private int numPort;
    private ChatServerView cv;
    private boolean active;
    private ServerSocket ss;
    private Log log = LogFactory.getLog(this.getClass());

    public ServerAcceptor(int numPort, ChatServerView cv, boolean active) {
        super();
        this.numPort = numPort;
        this.cv = cv;
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        try {
//            System.out.println("Active = false");
            if (!active) {
                ss.close();
            }
        } catch (IOException ex) {
            log.error(ex);
        }
    }

    /**
     * Accetta le connessioni dai client
     * @return
     * @throws java.lang.Exception
     */
    @Override
    protected Object doInBackground() throws Exception {
        try {

            ServerSocket ss2 = new ServerSocket(numPort);
            this.ss = ss2;
//            cv.setServerSocket(ss);
            Socket clientSocket = null;
            while (active) {
                if (!active) {
                    break;
                }
                try {
                    clientSocket = ss.accept();
                } catch (SocketException e) {
                    log.error(e.getMessage());
                    log.debug("continuo l'esecuzione del server acceptor");
                    continue;
                }

                new Thread(new Connector(clientSocket, cv)).start();

            // <editor-fold defaultstate="collapsed" desc=" Old Code ">
//                try {
//
//                    cv.getChatText().setText(cv.getChatText().getText().concat("Server - Accepted new Socket  on port : " + clientSocket.getPort() + " from ip : " + clientSocket.getInetAddress() + "\n"));
//                    log.debug("Server - Accepted new Socket  on port : " + clientSocket.getPort() + " from ip : " + clientSocket.getInetAddress() + "\n");
//
//                    //variabili
//                    String nick = "";
//                    String line = "";
//                    ServerWriter sw = new ServerWriter(cv);
//
//                    //leggo il nick e scrivo che s'è connesso sulla chat
//                    InputStream is = clientSocket.getInputStream();
//                    BufferedReader in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                    line = in.readLine();
//
//                    log.trace("received line [" + line + "]");
//
//                    //prenso l'xml ricevuto
//                    MESSAGE message = MessageManger.parseXML(line);
//                    log.debug("received MESSAGE : " + MessageManger.messageToStringFormatted(message));
//
//                    //verifico se è una richiesta di aggiunta socket per l'invio dei files
//                    if (message.getType().equals(COMMAND) && message.getName().equals(Command.ADDFILESOCKET)) {
//
//                        String sender = message.getParameters().getParameter().get(0).getValue();
//
//                        log.debug("add socket to client : " + sender);
//                        Client client = null;
//                        for (Client elem : cv.getClients()) {
//                            if (elem.getNick().equals(sender)) {
//                                client = elem;
//                            }
//                        }
//
//                        client.setFileSenderSocket(clientSocket);
//                        ServerReaderXML srxml = new ServerReaderXML(client, cv, ServerReaderXML.FILEREADER);
////                    sr.execute();
//                        srxml.execute();
//
//                        message.getParameters().getParameter().get(0).setValue(Command.OK);
//                        MessageManger.directWriteMessage(message, clientSocket.getOutputStream());
////                        continue;
//
//                    //altrimenti è una nuova connessione
//                    } else {
//
//                        nick = message.getParameters().getParameter().get(0).getValue();
//
//                        //verifico se il nick è connesso
//                        boolean connesso = false;
//                        List<Client> clients = cv.getClients();
//
//                        synchronized (clients) {
//                            for (Client client : clients) {
//                                if (client.getNick().equals(nick)) {
//                                    //se c'è già invio NOK
//                                    connesso = true;
//                                    log.debug("il client è già connesso");
//                                    break;
//                                }
//                            }
//                        }
//
//                        //invio la rispsta d3el controllo al client
//                        //se è connesso scrivo nock e finisco
//                        MESSAGE response = MessageManger.createCommand(Command.CONNECT, null);
//
//                        if (connesso) {
//                            MessageManger.addParameter(response, "response", Command.KO);
//                            MessageManger.directWriteMessage(response, clientSocket.getOutputStream());
//                            clientSocket.close();
//
//                        //ok starto il colient
//                        } else {
//
//                            //il nick è disponibile e scrivo OK
//                            MessageManger.addParameter(response, "response", Command.OK);
//                            MessageManger.directWriteMessage(response, clientSocket.getOutputStream());
//
//
//                            //aspetto che il client dica di aver avviato il tred reader
//                            //impedisce che vengano persi dei nick connessi
//                            String respSt = in.readLine();
//                            response = MessageManger.parseXML(respSt);
//
//                            log.debug(nick + " response = " + MessageManger.messageToStringFormatted(response));
//                            if (response.getParameters().getParameter().get(0).getValue().equals(Command.OK)) {
//
//                                //dico a tutti di aggiungerlo
//                                MESSAGE toSend = MessageManger.createCommand(Command.ADDUSER, null);
//                                MessageManger.addParameter(toSend, "nick", nick);
//                                log.debug("writeAll : " + toSend);
//                                sw.writeAll(toSend);
//
//
//                                //creo l'oggetto client e lo aggiungo alla lista
//                                Client client = new Client(clientSocket, nick);
//                                synchronized (cv.getClients()) {
//                                    cv.getClients().add(client);
//                                }
//
//                                //invio al client la lista di utenti connessi
//                                for (Client elem : cv.getClients()) {
//                                    if (elem != client) {
//                                        toSend = MessageManger.createCommand(Command.ADDUSER, null);
//                                        MessageManger.addParameter(toSend, "nick", elem.getNick());
//
//                                        sw.writeToClient(nick, toSend);
//                                    }
//                                }
//
//                                //aggiorno la tabella
//                                cv.refreshTable();
//
////                    ServerReader sr = new ServerReader(client, cv);
//                                ServerReaderXML srxml = new ServerReaderXML(client, cv, ServerReaderXML.MAINREADER);
////                    sr.execute();
//                                srxml.execute();
//                            }
//                        }
//                    }
//                } catch (Exception e) {
//                    log.debug(e);
//                }

// </editor-fold>
            }

            log.info("Chiusura ServerAccptor Thread");
            return null;

        } catch (IOException ex) {
            log.error(ex);
            return null;
        } catch (Exception ex) {
            log.error(ex);
            return null;
        }
    }
}

class Connector implements Runnable {

    Log log = LogFactory.getLog(this.getClass());
    private Socket clientSocket;
    private ChatServerView cv;

    public Connector(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public Connector(Socket clientSocket, ChatServerView cv) {
        this.clientSocket = clientSocket;
        this.cv = cv;
    }

    /**
     * Gestisce la connessione al server e il loghin
     * del client che ha richiesto la connessione
     * @return
     * @throws java.lang.Exception
     */
    @Override
    public void run() {
        try {
            JTextPane chatText = cv.getChatText();
            synchronized (chatText) {
                chatText.setText(cv.getChatText().getText().concat("Server - Accepted new Socket  on port : " + clientSocket.getPort() + " from ip : " + clientSocket.getInetAddress() + "\n"));
            }
            log.debug("Server - Accepted new Socket  on port : " + clientSocket.getPort() + " from ip : " + clientSocket.getInetAddress() + "\n");

            //variabili
            String nick = "";
            String line = "";
            ServerWriter sw = new ServerWriter(cv);

            //leggo il nick e scrivo che s'è connesso sulla chat
            InputStream is = clientSocket.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            line = in.readLine();

            log.trace("received line [" + line + "]");

            //prenso l'xml ricevuto
            MESSAGE message = MessageManger.parseXML(line);
            log.debug("received MESSAGE : " + MessageManger.messageToStringFormatted(message));

            //verifico se è una richiesta di aggiunta socket per l'invio dei files
            if (message.getType().equals(COMMAND) && message.getName().equals(Command.ADDFILESOCKET)) {

                String sender = message.getParameters().getParameter().get(0).getValue();

                log.debug("add socket to client : " + sender);
                Client client = null;

                synchronized (cv.getClients()) {
                    for (Client elem : cv.getClients()) {
                        if (elem.getNick().equals(sender)) {
                            client = elem;
                        }
                    }
                }

                client.setFileSenderSocket(clientSocket);
                ServerReaderXML srxml = new ServerReaderXML(client, cv, ServerReaderXML.FILEREADER);
//                    sr.execute();
                new Thread(srxml).start();

                message.getParameters().getParameter().get(0).setValue(Command.OK);
                MessageManger.directWriteMessage(message, clientSocket.getOutputStream());
//                        continue;

            //altrimenti è una nuova connessione
            } else {

                nick = message.getParameters().getParameter().get(0).getValue();

                //verifico se il nick è connesso
                boolean connesso = false;
                synchronized (cv.getClients()) {
                    for (Client client : cv.getClients()) {
                        if (client.getNick().equals(nick)) {
                            //se c'è già invio NOK
                            connesso = true;
                            break;
                        }
                    }
                }


                //invio la rispsta d3el controllo al client
                //se è connesso scrivo nock e finisco
                MESSAGE response = MessageManger.createCommand(Command.CONNECT, null);

                if (connesso) {
                    MessageManger.addParameter(response, "response", Command.KO);
                    MessageManger.directWriteMessage(response, clientSocket.getOutputStream());
                    clientSocket.close();

                //ok starto il colient
                } else {

                    //il nick è disponibile e scrivo OK
                    MessageManger.addParameter(response, "response", Command.OK);
                    MessageManger.directWriteMessage(response, clientSocket.getOutputStream());


                    //aspetto che il client dica di aver avviato il tred reader
                    //impedisce che vengano persi dei nick connessi
                    String respSt = in.readLine();
                    response = MessageManger.parseXML(respSt);

                    log.debug(nick + " response = " + MessageManger.messageToStringFormatted(response));
                    String responseValue = response.getParameters().getParameter().get(0).getValue();
                    if (responseValue.equals(Command.OK)) {
                        log.debug("response [" + responseValue + "]");

                        //creo l'oggetto client
                        Client client = new Client(clientSocket, nick);

                        //avvio il thread di lettura xml per il client
                        ServerReaderXML srxml = new ServerReaderXML(client, cv, ServerReaderXML.MAINREADER);
                        new Thread(srxml).start();


                        //attendo che la lettura sia pronta
                        while (!srxml.isActive()) {
                        }

                        log.debug("started serverReader for " + nick);


                        //dico a tutti di aggiungerlo
                        MESSAGE toSend = MessageManger.createCommand(Command.ADDUSER, null);
                        MessageManger.addParameter(toSend, "nick", nick);
                        log.debug("writeAll : " + toSend);
                        sw.writeAll(toSend);
                        log.debug("inviato a tutti il comnando add user");

                        //aggiungo il client alla lista
                        synchronized (cv.getClients()) {
                            cv.getClients().add(client);
                        }
                        log.debug("aggiunto il nuovo client [" + nick + "] alla lista dei clients");

                        //ora creo un command con tutti i clients da agiuungere
                        toSend = MessageManger.createCommand(Command.ADDUSER, null);
                        //invio al client la lista di utenti connessi
                        synchronized (cv.getClients()) {
                            for (Client elem : cv.getClients()) {
                                if (elem != client) {
                                    MessageManger.addParameter(toSend, "nick", elem.getNick());
                                }
                            }
                        }
                        sw.writeToClient(nick, toSend);

                        //aggiorno la tabella
                        cv.refreshTable();

                        log.debug("aggiornata la lista dei clients");

                    }
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
    }
}


