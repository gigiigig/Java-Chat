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
   private String nick;
   
   public Client(){
      
   }
   public Client(Socket socket,String nick){
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

    
   
   

}
