package gg.msn.facebook.core;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.MalformedURLException;
import java.net.URL;


import java.util.LinkedList;
import java.util.List;
import javax.swing.JOptionPane;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ResponseParser {

    private static Log log = LogFactory.getLog(ResponseParser.class);

    /**
     * parse the buddylist, store it in to FacebookBuddyList object.
     * @param response
     * @throws JSONException
     */
    public static void buddylistParser(String response) throws JSONException {
        if (response == null) {
            return;
        }
        String prefix = "for (;;);";
        if (response.startsWith(prefix)) {
            response = response.substring(prefix.length());
        }

        JSONObject respObjs = new JSONObject(response);
        if (respObjs == null) {
            return;
        }
        log.debug("error: " + respObjs.getInt("error"));
        if (respObjs.get("error") != null) {
            /*kError_Global_ValidationError = 1346001,
            kError_Login_GenericError = 1348009,
            kError_Chat_NotAvailable = 1356002,
            kError_Chat_SendOtherNotAvailable = 1356003,
            kError_Async_NotLoggedIn = 1357001,
            kError_Async_LoginChanged = 1357003,
            kError_Async_CSRFCheckFailed = 1357004,
            kError_Chat_TooManyMessages = 1356008,
            kError_Platform_CallbackValidationFailure = 1349007,
            kError_Platform_ApplicationResponseInvalid = 1349008;*/
            try {
                if (respObjs.getInt("error") == 0) {
                    //no error

                    JSONObject payload = (JSONObject) respObjs.get("payload");
                    if (payload != null) {
                        JSONObject buddyList = (JSONObject) payload.get("buddy_list");
                        if (buddyList != null) {
                            FacebookUserList.updateBuddyList(buddyList);
                            log.debug("buddy lisst updated");
                        }
                    }
                }else{
                    
                }
            } catch (ClassCastException cce) {
                log.error(cce);
                JOptionPane.showMessageDialog(null, cce, "Errore", JOptionPane.ERROR_MESSAGE);
                //for (;;);{"error":0,"errorSummary":"","errorDescription":"No error.","payload":[],"bootload":[{"name":"js\/common.js.pkg.php","type":"js","src":"http:\/\/static.ak.fbcdn.net\/rsrc.php\/pkg\/60\/106715\/js\/common.js.pkg.php"}]}
                //"payload":[]
                //not "{}"
                //we do nothing
                }

        }
    }

    /**
     * parse the buddylist, store it in to FacebookBuddyList object.
     * @param response
     */
    public static void notificationParser(String response) {
        if (response == null) {
            return;
        }
        String prefix = "for (;;);";
        if (response.startsWith(prefix)) {
            response = response.substring(prefix.length());
        }
    }

    /**
     * parse the message posting response, and doing some corresponding things
     * e.g. if it succeeds, we do nothing;
     * else if we get some error, we print them.
     *
     * @param response
     * @throws JSONException
     */
    public static int messagePostingResultParser(String uid, String msg, String response) throws JSONException {

        int toReturn = -1;
//        if (response == null) {
//            return -1;
//        }
        String prefix = "for (;;);";
        if (response.startsWith(prefix)) {
            response = response.substring(prefix.length());
        }

        //for (;;);{"error":0,"errorSummary":"","errorDescription":"No error.","payload":[],"bootload":[{"name":"js\/common.js.pkg.php","type":"js","src":"http:\/\/static.ak.fbcdn.net\/rsrc.php\/pkg\/60\/106715\/js\/common.js.pkg.php"}]}
        //for (;;);{"error":1356003,"errorSummary":"Send destination not online","errorDescription":"This person is no longer online.","payload":null,"bootload":[{"name":"js\/common.js.pkg.php","type":"js","src":"http:\/\/static.ak.fbcdn.net\/rsrc.php\/pkg\/60\/106715\/js\/common.js.pkg.php"}]}
        JSONObject respObjs = new JSONObject(response);
//        if (respObjs == null) {
//            return -1;
//        }
        log.debug("error: " + respObjs.getInt("error"));
        if (respObjs.get("error") != null) {
            /*kError_Global_ValidationError = 1346001,
            kError_Login_GenericError = 1348009,
            kError_Chat_NotAvailable = 1356002,
            kError_Chat_SendOtherNotAvailable = 1356003,
            kError_Async_NotLoggedIn = 1357001,
            kError_Async_LoginChanged = 1357003,
            kError_Async_CSRFCheckFailed = 1357004,
            kError_Chat_TooManyMessages = 1356008,
            kError_Platform_CallbackValidationFailure = 1349007,
            kError_Platform_ApplicationResponseInvalid = 1349008;*/

            int errorCode = respObjs.getInt("error");
            String errorString = "Error(" + errorCode + "): " + (String) respObjs.get("errorSummary") + ";" + (String) respObjs.get("errorDescription");

            log.debug(errorString);

            toReturn = errorCode;
//            if (errorCode == ErrorCode.Error_Global_NoError) {
//                //do nothing
////            } else {
////                if (FacebookManager.isChatroomExist(uid)) {
////                    FacebookManager.getChatroomAnyway(uid).showFeedbackMsg(msg, errorString);
////                }
////            }
//            }
        }
        return toReturn;
    }

    /**
     * parse the message request response, and doing some corresponding things<br>
     * e.g. get the message text, put it in the corresponding chatroom.
     *
     * @param response
     * @throws JSONException
     */
    public synchronized static List<FacebookMessage> messageRequestResultParser(String response, FacebookManager manager) throws Exception {
        List<FacebookMessage> toReturn = new LinkedList<FacebookMessage>();


        String prefix = "for (;;);";
        if (response.startsWith(prefix)) {
            response = response.substring(prefix.length());
        }

        JSONObject respObjs = new JSONObject(response);
        log.debug("t: " + (String) respObjs.get("t"));
        if (respObjs.get("t") != null) {
            if (((String) respObjs.get("t")).equals("msg")) {
                JSONArray ms = (JSONArray) respObjs.get("ms");
                log.debug("NO of msges: " + ms.length());
                //Iterator<JSONObject> it = ms..iterator();
                int index = 0;

                //parso ogni messaggio 
                while (index < ms.length()) {
                    JSONObject msg = ms.getJSONObject(index);
                    index++;
                    log.debug("message [ " + msg + " ]");
                    if (msg.get("type").equals("typ")) {
                        //do nothing
                        log.debug("typing message");
                        FacebookManager.incrementMessage();
                        FacebookMessage fm = new FacebookMessage();
                        fm.type = "typ";
                        fm.from = (Number) msg.get("from");

                        if (!fm.from.toString().equals(FacebookManager.uid)) {
                            toReturn.add(fm);
                        }

                    } else if (msg.get("type").equals("msg")) {
                        //the message itself
                        JSONObject realmsg = (JSONObject) msg.get("msg");


                        /*{"text":"FINE",
                        "time":1214614165139,
                        "clientTime":1214614163774,
                        "msgID":"1809311570"},
                        "from":1190346972,
                        "to":1386786477,
                        "from_name":"David Willer",
                        "to_name":"\u5341\u4e00",
                        "from_first_name":"David",
                        "to_first_name":"\u4e00"}

                        {"t":"msg",
                        "c":"p_100000191774044",
                        "ms":[{
                        "type":"msg",
                        "msg":{
                        "text":"erge8i",
                        "time":1252585945364,
                        "clientTime":1252585943938,
                        "msgID":"2771645192"},
                        "from":1567835536,
                        "to":100000191774044,
                        "from_name":"Luigi Antonini",
                        "to_name":"Gigi Antonini",
                        "from_first_name":"Luigi",
                        "to_first_name":"Gigi"}]}

                         */
                        FacebookMessage fm = new FacebookMessage();
                        fm.text = (String) realmsg.get("text");
                        fm.time = (Number) realmsg.get("time");
                        fm.clientTime = (Number) realmsg.get("clientTime");
                        fm.msgID = (String) realmsg.get("msgID");
                        fm.type = "msg";

                        //the attributes of the message
                        fm.from = (Number) msg.get("from");
                        fm.to = (Number) msg.get("to");
                        fm.fromName = (String) msg.get("from_name");
                        fm.toName = (String) msg.get("to_name");
                        fm.fromFirstName = (String) msg.get("from_first_name");
                        fm.toFirstName = (String) msg.get("to_first_name");

                        FacebookManager.incrementMessage();

                        if (FacebookManager.msgIDCollection.contains(fm.msgID)) {
                            log.debug("Omitting a already handled message: msgIDCollection.contains(msgID)");
                            //FacebookManager.seq = manager.getSeq();

                            continue;
                        } else {
                            FacebookManager.msgIDCollection.add(fm.msgID);

                            log.debug("Size of msgIDCollection:" + FacebookManager.msgIDCollection.size());
                            logMessage(fm);

                            //se il messaggio è mio nn lo stampo
                            if (!fm.from.toString().equals(FacebookManager.uid)) {
                                toReturn.add(fm);
                            }
                        }
                    } else {
                        //do nothing
                        /*
                        {"t":"msg",
                        "c":"p_100000191774044",
                        "ms":[{"type":"typ","st":0,"from":1567835536,"to":100000191774044}]}
                         */
                        log.debug("type [" + msg.get("type") + "]");
                        FacebookManager.incrementMessage();


                    }
                }
            } else if (((String) respObjs.get("t")).equals("refresh")) {
                log.debug("t : refresh  , esco e rifaccio il login");//do nothing
//                if (((String) respObjs.get("seq")) != null) {
//                    log.debug("refresh seq: " + (String) respObjs.get("seq"));
//                }
                //riprovo il login
                throw new Exception("t : refresh  , esco e rifaccio il login");
            } else if (((String) respObjs.get("t")).equals("continue")) {
                log.debug("Time out, reconcect...");//do nothing
            } else {
                log.debug("Unrecognized response type: " + (String) respObjs.get("t"));
            }

        }
        return toReturn;
    }

    public static void logMessage(FacebookMessage msg) {
        log.debug("text:\t" + msg.text);
        log.debug("time:\t" + msg.time);
        log.debug("clientTime:\t" + msg.clientTime);
        log.debug("msgID:\t" + msg.msgID);
        log.debug("from:\t" + msg.from);
        log.debug("to:\t" + msg.to);
        log.debug("from_name:\t" + msg.fromName);
        log.debug("to_name:\t" + msg.toName);
        log.debug("from_first_name:\t" + msg.fromFirstName);
        log.debug("to_first_name:\t" + msg.toFirstName);
    }
}
