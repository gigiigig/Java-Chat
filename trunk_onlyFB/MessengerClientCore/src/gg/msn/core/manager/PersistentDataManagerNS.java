/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gg.msn.core.manager;

import chatcommons.Client;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Hashtable;

/**
 *
 * @author Luigi
 */
public class PersistentDataManagerNS {

    private  Hashtable<String,Client> clients;
    private  Socket socket;
    private  OutputStream outputStream;
    private  Socket fileSocket;
    private  OutputStream fileOutputStream;
    private  int port;
    private  String ip;
    private  String nick;

    public PersistentDataManagerNS() {
        clients = new Hashtable<String,Client>();
    }

    public  Hashtable<String, Client> getClients() {
        return clients;
    }

    public  void setClients(Hashtable<String, Client> clients) {
        this.clients = clients;
    }


    public  OutputStream getFileOutputStream() {
        return fileOutputStream;
    }

    public  void setFileOutputStream(OutputStream fileOutputStream) {
        this.fileOutputStream = fileOutputStream;
    }

    public  Socket getFileSocket() {
        return fileSocket;
    }

    public  void setFileSocket(Socket fileSocket) {
        this.fileSocket = fileSocket;
    }

    public  String getIp() {
        return ip;
    }

    public  void setIp(String ip) {
        this.ip = ip;
    }

    public  String getNick() {
        return nick;
    }

    public  void setNick(String nick) {
        this.nick = nick;
    }

    public  OutputStream getOutputStream() {
        return outputStream;
    }

    public  void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public  int getPort() {
        return port;
    }

    public  void setPort(int port) {
        this.port = port;
    }

    public  Socket getSocket() {
        return socket;
    }

    public  void setSocket(Socket socket) {
        this.socket = socket;
    }



}
