package facebookchat.common;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;

public class FacebookBuddyList {

    private static Log log = LogFactory.getLog(FacebookBuddyList.class);
    public static  FacebookUser me;
    public static  Map<String, FacebookUser> buddies = new Hashtable<String, FacebookUser>();

    static {
        buddies.clear();
    }
    public static Boolean listChanged;
    public static Number availableCount;

    public static void updateBuddyList(JSONObject buddyList) throws JSONException {
        //JSONObject buddyList = (JSONObject) payload.get("buddy_list");
        listChanged = (Boolean) buddyList.get("listChanged");
        availableCount = (Number) buddyList.get("availableCount");

        log.debug("listChanged: " + (Boolean) buddyList.get("listChanged"));
        log.debug("availableCount: " + (Number) buddyList.get("availableCount"));

        JSONObject nowAvailableList = (JSONObject) buddyList.get("nowAvailableList");
        log.debug("utenti presenti : " + nowAvailableList);
        JSONObject userInfos = (JSONObject) buddyList.get("userInfos");
        log.debug("utenti presenti INFO : " + userInfos);

        if (nowAvailableList == null) {
            return;
        }

//        JSONObject user = (JSONObject) userInfos.get(Launcher.uid);
//        me = new FacebookUser(Launcher.uid, user);
        //update my profile
//        if (Launcher.fbc != null) {
//            Launcher.fbc.updateMyStatus();
//        }

        //tag all the buddies as offline
        Iterator<String> oldIt = buddies.keySet().iterator();
        while (oldIt.hasNext()) {
            String key = oldIt.next();
            log.debug("userID: " + key);
            buddies.get(key).onlineStatus = OnlineStatus.OFFLINE;
        }

        Iterator<String> it = nowAvailableList.keys();
        //log.debug("first element :" +it.next());
        while (it.hasNext()) {
            String key = it.next();
            log.debug("userID: " + key);
            JSONObject user = (JSONObject) userInfos.get(key);
            log.debug("user : " + user);
            FacebookUser fu = null;
            try {
                fu = new FacebookUser(key, user);
                buddies.put(key, fu);
            //Launcher.getChatroomAnyway(key).setRoomName(fu.name);
            printUserInfo(fu);
            } catch (JSONException jSONException) {
                
                log.error("error parsing user : "+jSONException);
            }
            
        }
    }

    private static void printUserInfo(FacebookUser user) {
        log.debug("name:\t" + user.name);
        log.debug("firstName:\t" + user.firstName);
        log.debug("thumbSrc:\t" + user.thumbSrc);
        log.debug("status:\t" + user.status);
        log.debug("statusTime:\t" + user.statusTime);
        log.debug("statusTimeRel:\t" + user.statusTimeRel);
        log.debug("OnlineStatus:\t" + user.onlineStatus);
    }
}
