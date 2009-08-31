/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatcommons;

import java.net.Socket;

/**
 *
 * @author Administrator
 */
public class Client {

    private Socket mainSocket;
    private Socket fileSenderSocket;
    private String uid;
    private String nick;
    private String status;
    private String message;
    private byte[] image;

    public Client() {
    }

    public Client(String nick) {
        this.nick = nick;
    }

    public Client(Socket socket, String nick) {
        this.mainSocket = socket;
        this.nick = nick;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public Socket getMainSocket() {
        return mainSocket;
    }

    public void setMainSocket(Socket socket) {
        this.mainSocket = socket;
    }

    public Socket getFileSenderSocket() {
        return fileSenderSocket;
    }

    public void setFileSenderSocket(Socket fileSenderSocket) {
        this.fileSenderSocket = fileSenderSocket;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
