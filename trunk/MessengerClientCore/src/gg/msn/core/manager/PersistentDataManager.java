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
public class PersistentDataManager {

    private static Hashtable<String,Client> clients;
    private static Socket socket;
    private static OutputStream outputStream;
    private static Socket fileSocket;
    private static OutputStream fileOutputStream;
    private static int port;
    private static String ip;
    private static String nick;
    private static String uid;

    static {
        if(clients == null){
            clients = new Hashtable<String,Client>();
        }
    }

    public static Hashtable<String, Client> getClients() {
        return clients;
    }

    public static void setClients(Hashtable<String, Client> clients) {
        PersistentDataManager.clients = clients;
    }


    public static OutputStream getFileOutputStream() {
        return fileOutputStream;
    }

    public static void setFileOutputStream(OutputStream fileOutputStream) {
        PersistentDataManager.fileOutputStream = fileOutputStream;
    }

    public static Socket getFileSocket() {
        return fileSocket;
    }

    public static void setFileSocket(Socket fileSocket) {
        PersistentDataManager.fileSocket = fileSocket;
    }

    public static String getIp() {
        return ip;
    }

    public static void setIp(String ip) {
        PersistentDataManager.ip = ip;
    }

    public static String getNick() {
        return nick;
    }

    public static void setNick(String nick) {
        PersistentDataManager.nick = nick;
    }

    public static OutputStream getOutputStream() {
        return outputStream;
    }

    public static void setOutputStream(OutputStream outputStream) {
        PersistentDataManager.outputStream = outputStream;
    }

    public static int getPort() {
        return port;
    }

    public static void setPort(int port) {
        PersistentDataManager.port = port;
    }

    public static Socket getSocket() {
        return socket;
    }

    public static void setSocket(Socket socket) {
        PersistentDataManager.socket = socket;
    }

    public static String getUid() {
        return uid;
    }

    public static void setUid(String uid) {
        PersistentDataManager.uid = uid;
    }



}
