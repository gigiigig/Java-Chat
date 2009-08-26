/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gg.msn.core.manager;

import chatcommons.Client;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Luigi
 */
public class PersistentDataManager {

     private ArrayList<Client> clients;
    private Socket socket;
    private OutputStream outputStream;
    private Socket fileSocket;
    private OutputStream fileOutputStream;
    private int port;
    private String ip;
    private String nick;

}
