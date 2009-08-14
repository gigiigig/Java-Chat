/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;

import chatcommons.Client;
import chatserver.commons.Util;
import chatserver.threads.ServerAcceptor;
import java.awt.Desktop.Action;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFrame;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Luigi
 */
public class Main {

    private Log log = LogFactory.getLog(Main.class);
    //Mie Variabili
    //TUTTE LE OPERAZIONI SU QUESTA LISTA VANNO SINCRONIZZATE!!!!!!
    private ArrayList<Client> clients;
//    private ServerSocket serverSocket;
    private ServerAcceptor serverAccptor;
    private int port;

    public static void main(String[] args) {
        Main main = new Main();
        main.StartServer();

    }

    public Main() {
        port = 3434;
        clients = new ArrayList<Client>();
    }

    /**
     * Avvia il server
     */
    public void StartServer() {

        //operazioni per l'avvio del server
        log.info("Start server");
        serverAccptor = new ServerAcceptor(port, this, true);
        new Thread(serverAccptor).start();
//        SwingUtilities.invokeLater(serverAccptor);

        Util util = new Util();
        log.debug(util.getPath());

    }

    /**
     * Stoppa  il server
     */
    public void stopServer() {

        //stoppa il server


        for (Client client : clients) {
            try {
                client.getMainSocket().close();
            } catch (IOException ex) {
                log.error(ex);
            }
        }

        serverAccptor.setActive(false);
        clients = new ArrayList<Client>();

    }


    //*** Getter and Setter
    public ArrayList<Client> getClients() {
        return clients;
    }

    /**
     *
     * @param clients
     */
    public void setClients(ArrayList<Client> clients) {
        this.clients = clients;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
