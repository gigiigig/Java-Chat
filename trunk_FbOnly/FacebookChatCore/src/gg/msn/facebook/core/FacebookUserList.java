package gg.msn.facebook.core;

import java.util.Hashtable;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;

public class FacebookUserList {

    private static Log log = LogFactory.getLog(FacebookUserList.class);
    public static FacebookUser me;
    public static Hashtable<String, FacebookUser> buddies = new Hashtable<String, FacebookUser>();

    static {
        buddies.clear();
    }
    public static Boolean listChanged;
    public static Number availableCount;

    public static void updateBuddyList(JSONObject buddyList) throws JSONException {
        //JSONObject buddyList = (JSONObject) payload.get("buddy_list");

        log.debug("full JSON objec [ " + buddyList + " ]t");

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
//        Iterator<String> oldIt = buddies.keySet().iterator();
//        while (oldIt.hasNext()) {
//            String key = oldIt.next();
//            log.debug("userID: " + key);
//            buddies.get(key).onlineStatus = OnlineStatus.OFFLINE;
//        }

        Iterator<String> it = nowAvailableList.keys();
        //log.debug("first element :" +it.next());
        buddies.clear();
        while (it.hasNext()) {
            String key = it.next();
            log.debug("userID: " + key);
            JSONObject user = (JSONObject) userInfos.get(key);
            log.debug("user : " + user);
            FacebookUser fu = null;
            try {

                fu = new FacebookUser(key, user, (JSONObject) nowAvailableList.get(key));
                /*
                if(StringUtils.equals(fu.status, FacebookUser.STATUS_ONLINE)){
                onlines.put(key, fu);
                }else{
                offlines.put(key, fu);
                }

                buddies.putAll(onlines);
                buddies.putAll(offlines);
                 */
                buddies.put(key, fu);

                //Launcher.getChatroomAnyway(key).setRoomName(fu.name);
                //printUserInfo(fu);
            } catch (JSONException jSONException) {
                log.error("error parsing user : " + jSONException);
            }

        }
    }

    private static void printUserInfo(FacebookUser user) {
        log.debug("name:\t" + user.name);
        log.debug("firstName:\t" + user.firstName);
        log.debug("thumbSrc:\t" + user.thumbSrc);
        log.debug("status:\t" + user.status);

    }
}
