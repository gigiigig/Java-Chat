package gg.msn.facebook.core;

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

    public static String STATUS_ONLINE = "online";
    public static String STATUS_OFFLINE = "offline";
    public String uid;
    public String name;
    public String firstName;
    public String thumbSrc;
    public String status;
    public ImageIcon portrait;
    public long lastSeen;
   

    public FacebookUser(String uid) {
        this.uid = uid;
    }

    
    public FacebookUser(String id, JSONObject user, JSONObject statusObj) throws JSONException {

        log.debug("status obj [ " + statusObj+" ]");
        uid = id;
        if (user == null) {
            throw new JSONException("Param user is null when init FacebookUser");
        }

        //nome completo
        name = (String) user.get("name");
        //nome
        firstName = (String) user.get("firstName");
        //link immmagine
        thumbSrc = (String) user.get("thumbSrc");
    
        try {
            portrait = new ImageIcon(new URL(thumbSrc));
        } catch (MalformedURLException e) {
            log.error(e);
            //cobtrollare nel renderer che non sia null
            portrait = null;
        }

        lastSeen = new Date().getTime();
        //{"i" = "false"} significa che è verde
       try {
            boolean aBoolean = statusObj.getBoolean("i");
            log.debug(" status  boolean [ " + aBoolean+" ]");
            if (!aBoolean) {
                status = STATUS_ONLINE;
            } else {
                status = STATUS_OFFLINE;
            }
        } catch (JSONException jSONException) {
            status = STATUS_OFFLINE;
        }
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
        this.portrait = fub.portrait;
        this.lastSeen = fub.lastSeen;
      
    }
}
