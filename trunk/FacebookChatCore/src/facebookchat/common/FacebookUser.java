package facebookchat.common;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import javax.swing.ImageIcon;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;

public class FacebookUser {

    private static Log log = LogFactory.getLog(FacebookUser.class);
    /*{"listChanged":true,
    "availableCount":2,

    "nowAvailableList":
    {"1355527894":{"i":false},
    "1386786477":{"i":false}},

    "wasAvailableIDs":[],

    "userInfos":{
    "1355527894":
    {"name":"Dai Zhiwei",
    "firstName":"Dai",
    "thumbSrc":"http:\/\/profile.ak.facebook.com\/v225\/1132\/119\/q1355527894_6497.jpg",
    "status":null,
    "statusTime":0,
    "statusTimeRel":""},
    "1386786477":
    {"name":"\u5341\u4e00",
    "firstName":"\u4e00",
    "thumbSrc":"http:\/\/static.ak.fbcdn.net\/pics\/q_silhouette.gif",
    "status":null,
    "statusTime":0,
    "statusTimeRel":""},
    "1190346972":
    {"name":"David Willer",
    "firstName":"David",
    "thumbSrc":"http:\/\/profile.ak.facebook.com\/profile5\/54\/96\/q1190346972_3586.jpg",
    "status":null,
    "statusTime":0,
    "statusTimeRel":""}},

    "forcedRender":true,
    "flMode":false,
    "flData":{}}*/
    public String uid;
    public String name;
    public String firstName;
    public String thumbSrc;
    public String status;
    public Number statusTime;
    public String statusTimeRel;
    public ImageIcon portrait;
    public long lastSeen;
    public OnlineStatus onlineStatus;

    public FacebookUser(String id, JSONObject user) throws JSONException {

        uid = id;
        if (user == null) {
            throw new JSONException("Param user is null when init FacebookUser");
        }
        name = (String) user.get("name");
        firstName = (String) user.get("firstName");
        thumbSrc = (String) user.get("thumbSrc");
        //Object temp = user.get("status");
        //log.debug(temp.toString());
        //log.debug(temp.getClass());
//        if (!temp.equals(org.json.JSONObject.NULL)) {
//            status = (String) temp;
//        } else {
            status = "";
//        }
        //statusTime = (Number) user.get("statusTime");
        //statusTimeRel = (String) user.get("statusTimeRel");

        try {
            portrait = new ImageIcon(new URL(thumbSrc));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            portrait = new ImageIcon(SystemPath.PORTRAIT_RESOURCE_PATH + "q_silhouette.gif");
        }

        lastSeen = new Date().getTime();
        onlineStatus = OnlineStatus.ONLINE;
    }

    public void copy(FacebookUser fub) {
        if (fub == null) {
            return;
        }
        this.uid = fub.uid;
        this.name = fub.name;
        this.firstName = fub.firstName;
        this.thumbSrc = fub.thumbSrc;
        this.status = fub.status;
        this.statusTime = fub.statusTime;
        this.statusTimeRel = fub.statusTimeRel;
        this.portrait = fub.portrait;
        this.lastSeen = fub.lastSeen;
        this.onlineStatus = fub.onlineStatus;
    }
}
