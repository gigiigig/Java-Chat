/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatcommons;

/**
 *
 * @author Administrator
 */
public class Commands {

//    public static String SEPARATOR = "~";
    public static String DIVISOR = "&";
    //messages for chat
    public static String MESSAGE = "MESSAGE";
    //all generic request
    public static String REQUEST = "REQUEST";
    //commands to server
    public static String COMMAND = "COMMAND";

    /**
     * Use request to send general reqeust to server,this is forwarded
     * based on STANDARDREQUEST and STANDARDRESPONSE structure .
     */
    public static class Request {

        //STANDARD REQUESTS
//        public static String STANDARDREQUEST = REQUEST + "receiver" + SEPARATOR + "nameofrequest" + SEPARATOR + "parameter";
//        public static String STANDARDRSPONSE = REQUEST + "sender" + SEPARATOR + "nameofrequest" + SEPARATOR + "parameter";
        
        //GANERIC REQUEST
        public static String ALL = "all";
        public static String ADDTOCONFERENCE = "ADDTOCONFERENCE";
        
        //GAME REQUESTS
        public static String STARTGAME = "STARTGAME";
        public static String DAMAPOSITION = "DAMAPOSITION";
        
        //FILE SHAKE REQUESTS
        public static String FILETRANSFER = "FILETRANSFER";
        public static String FILEACCEPTREQUEST = "FILEACCEPTREQUEST";
        public static String FILEACCEPTRESPONSE = "FILEACCEPTRESPONSE";
        
    }

    public static class Message {

        public static String SINGLE = "SINGLE";
        public static String CONFERENCE = "CONFERENCE";
    }

    public static class Command {

        public static String REMOVEUSER = "REMOVEUSER";
        public static String ADDUSER = "ADDUSER";
        public static String DISCONNECT = "DISCONNECT";
        public static String CONNECT = "CONNECT";
        public static String ADDFILESOCKET = "ADDFILESOCKET";
        public static String OK = "OK";
        public static String KO = "KO";

    }

    @Deprecated
    public static void standardMESSAGEandCOMMANDS() {
//        System.out.println("MESSAGE for SINGLE :");
//        System.out.println("client SEND : " + MESSAGE + Message.SINGLE + "receiver" + SEPARATOR + "message");
//        System.out.println("server FORWARD to receiver : " + MESSAGE + Message.SINGLE + "sender" + SEPARATOR + "message");
//        System.out.println("");
//
//        System.out.println("MESSAGE for CONFERNCE :");
//        System.out.println("client SEND : " + MESSAGE + Message.CONFERENCE + "nick1" + DIVISOR + "nick2" + DIVISOR + "nick3" + SEPARATOR + "message");
//        System.out.println("server FORWARD to all : " + MESSAGE + Message.CONFERENCE + "sender" + SEPARATOR + "nick1" + DIVISOR + "nick2" + DIVISOR + "nick3" + SEPARATOR + "message");
//        System.out.println("");
//
//        System.out.println("COMMAND for DISCONNECT : ");
//        System.out.println("client SEND : " + COMMAND + Command.DISCONNECT);
//        System.out.println("server FORWARD all : " + COMMAND + Command.REMOVEUSER + "nick");
//        System.out.println("");
//
//        System.out.println("GENERIC REQUEST for SINGLE");
//        System.out.println("client SEND : " + Request.STANDARDREQUEST);
//        System.out.println("server FORWARD to receiver : " + Request.STANDARDRSPONSE);
//        System.out.println("");
//
//        System.out.println("GENERIC REQUEST for ALL");
//        System.out.println("client SEND : " + REQUEST + Request.ALL + SEPARATOR + "nameofrequest" + SEPARATOR + "parameter");
//        System.out.println("server FORWARD to all : " + Request.STANDARDRSPONSE);
//        System.out.println("");

    }

    /**
     * start this main to view on consle standard
     * messages exchanged between clent end server
     * @param args
     */
    public static void main(String[] args) {
        standardMESSAGEandCOMMANDS();

        System.out.println("test".substring(0, "test".length() - 1));

    }
} 
