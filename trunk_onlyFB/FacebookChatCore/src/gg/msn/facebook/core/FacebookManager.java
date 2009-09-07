package gg.msn.facebook.core;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import gg.msn.core.commons.Util;
import java.net.URL;
import javax.swing.ImageIcon;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FacebookManager {

    public static final String FACEBOOK_LAST_CHANNEL = "facebookLastChannel";
    public static final int MAX_CONNECTION_TRY = 3;
    public static final int MAX_FACEBOOK_CHANNELS = 99;
    private static Log log = LogFactory.getLog(FacebookManager.class);
    private static HttpClient httpClient;
    public static String loginPageUrl = "http://www.facebook.com/login.php";
    public static String homePageUrl = "http://www.facebook.com/home.php";
    public static String uid = null;
    public static String channel = "35";
    public static String post_form_id = null;
    public static long seq = -1;
    public static HashSet<String> msgIDCollection;
    //public static Queue<Object> requestQ;
    private String Proxy_Host = "ISASRV";
    private int Proxy_Port = 80;
    private String Proxy_Username = "daizw";
    private String Proxy_Password = "xxxxxx";
    private String email;
    private String passw;
    /**
     * The default parameters.
     * Instantiated in {@link #setup setup}.
     */
    private static HttpParams defaultParameters = null;
    /**
     * The scheme registry.
     * Instantiated in {@link #setup setup}.
     */
    private static SchemeRegistry supportedSchemes;

    // <editor-fold defaultstate="collapsed" desc="Old Code">

    /*public static Chatroom getChatroomAnyway(String uid) {
    uid = uid.trim();
    log.debug("%%%%%%>" + uid + "<%%%%%%");
    if (chatroomCache.containsKey(uid)) {
    log.debug("%%%%%%contains key:>" + uid + "<%%%%%%");
    return chatroomCache.get(uid);
    } else {
    log.debug("%%%%%%new chatroom:>" + uid + "<%%%%%%");
    Chatroom chatroom = new Chatroom(uid);
    chatroomCache.put(uid, chatroom);
    log.debug("registing chatroom...");
    return chatroom;
    }
    }

    public static boolean isChatroomExist(String uid) {
    uid = uid.trim();
    if (chatroomCache.containsKey(uid)) {
    return true;
    }
    return false;
    }
    /*public static boolean registerChatroom(String uid, Chatroom room){
    if(uid != null && room != null
    && !chatroomCache.containsKey(uid)){
    chatroomCache.put(uid, room);
    return true;
    }
    return false;
    }*/

    /* public static void main(String[] args) {
    //System.setProperty("sun.java2d.noddraw", "true");
    System.setProperty("java.util.logging.config.file",
    "logging.properties");

    FacebookManager laucher = new FacebookManager();
    laucher.go();
    }*/// </editor-fold>
    public FacebookManager(String email, String passw) {
        this.email = email;
        this.passw = passw;
        msgIDCollection = new HashSet<String>();
        msgIDCollection.clear();

//        chatroomCache = new Hashtable<String, Chatroom>();
//        chatroomCache.clear();

        // make sure to use a proxy that supports CONNECT
        final HttpHost target =
                new HttpHost("www.google.com", 80, "http");
        setup(); // some general setup

        //httpClient = createHttpClient();

        // <editor-fold defaultstate="collapsed" desc="Old Coded">
/*
        HttpRequest req = createRequest();
        HttpEntity entity = null;
        try {
        HttpResponse rsp = httpClient.execute(target, req);
        entity = rsp.getEntity();

        log.debug("========================================");
        log.debug(rsp.getStatusLine());
        Header[] headers = rsp.getAllHeaders();
        for (int i=0; i<headers.length; i++) {
        log.debug(headers[i]);
        }
        log.debug("----------------------------------------");

        if (rsp.getEntity() != null) {
        log.debug(EntityUtils.toString(rsp.getEntity()));
        }

        }
        catch (IOException e)
        {
        // TODO Auto-generated catch block
        e.printStackTrace();
        } finally {
        // If we could be sure that the stream of the entity has been
        // closed, we wouldn't need this code to release the connection.
        // However, EntityUtils.toString(...) can throw an exception.

        // if there is no entity, the connection is already released
        if (entity != null)
        try
        {
        entity.consumeContent();
        }
        catch (IOException e)
        {
        e.printStackTrace();
        } // release connection gracefully
        }
        log.debug("------Constructor END-------");
         */
        // </editor-fold>
    }

    private final HttpClient createHttpClient() {

        ClientConnectionManager ccm =
                new ThreadSafeClientConnManager(getParams(), supportedSchemes);
        //  new SingleClientConnManager(getParams(), supportedSchemes);

        DefaultHttpClient dhc =
                new DefaultHttpClient(ccm, getParams());

        dhc.getParams().setParameter(
                ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);

        // ------------------- Proxy Setting Block BEGINE -------------------
        // If we needn't a proxy, just comment this block
        //setUpProxy(dhc);
        // --------------------- Proxy Setting Block END --------------------

        return dhc;
    }

    private void setUpProxy(DefaultHttpClient dhc) {
        final HttpHost proxy =
                new HttpHost(Proxy_Host, Proxy_Port, "http");

        dhc.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        AuthState authState = new AuthState();
        authState.setAuthScope(new AuthScope(proxy.getHostName(),
                proxy.getPort()));
        AuthScope authScope = authState.getAuthScope();

        Credentials creds = new UsernamePasswordCredentials(Proxy_Username, Proxy_Password);
        dhc.getCredentialsProvider().setCredentials(authScope, creds);
        log.debug("executing request via " + proxy);
    }

    /**
     * Performs general setup.
     * This should be called only once.
     */
    private final static void setup() {

        supportedSchemes = new SchemeRegistry();

        // Register the "http" and "https" protocol schemes, they are
        // required by the default operator to look up socket factories.
        SocketFactory sf = PlainSocketFactory.getSocketFactory();
        supportedSchemes.register(new Scheme("http", sf, 80));
        sf = SSLSocketFactory.getSocketFactory();
        supportedSchemes.register(new Scheme("https", sf, 80));

        // prepare parameters
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, "UTF-8");
        HttpProtocolParams.setUseExpectContinue(params, true);
        HttpProtocolParams.setHttpElementCharset(params, "UTF-8");
        HttpProtocolParams.setUserAgent(params, "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9) Gecko/2008052906 Firefox/3.0");
        defaultParameters = params;
    } // setup

    private final static HttpParams getParams() {
        return defaultParameters;
    }

    /**
     * Creates a request to execute in this example.
     *
     * @return  a request without an entity
     */
    private final static HttpRequest createRequest() {

        HttpRequest req = new BasicHttpRequest("GET", "/", HttpVersion.HTTP_1_1);
        //("OPTIONS", "*", HttpVersion.HTTP_1_1);

        return req;
    }

    // <editor-fold defaultstate="collapsed" desc="Old Code">
/* public void go() {
    LoginDialog login = new LoginDialog();
    String action = (String) login.showDialog();
    if (action.equals(LoginDialog.CANCELCMD)) {
    System.exit(0);
    }

    String email = login.getUsername();
    String pass = new String(login.getPassword());

    /*String email = "username@email.com.cn";
    String pass = "password";

    log.debug(email + ":" + pass);

    int loginErrorCode = doLogin(email, pass);
    if (loginErrorCode == ErrorCode.Error_Global_NoError) {
    if (doParseHomePage() == ErrorCode.Error_Global_NoError) {
    getBuddyList();

    //keep requesting message from the server
    Thread msgRequester = new Thread(new Runnable() {

    public void run() {
    log.debug("Keep requesting...");
    while (true) {
    try {
    keepRequesting();
    } catch (Exception e) {
    e.printStackTrace();
    }
    }
    }
    });
    msgRequester.start();

    //requests buddy list every 90 seconds
    Thread buddyListRequester = new Thread(new Runnable() {

    public void run() {
    log.debug("Keep requesting buddylist...");
    while (true) {
    try {
    getBuddyList();
    } catch (Exception e) {
    e.printStackTrace();
    }
    if (fbc != null) {
    fbc.updateBuddyListPane();
    }
    // it's said that the buddy list is updated every 3 minutes at the server end.
    // we refresh the buddy list every 1.5 minutes
    try {
    Thread.sleep(60 * 1000);
    } catch (InterruptedException e) {
    e.printStackTrace();
    }
    }
    }
    });
    buddyListRequester.start();
    //TODO post
    //Init GUI
    log.debug("Init GUI...");
    //必须在getbuddylist之后
    //                fbc = new Cheyenne();
    //                fbc.setVisible(true);
    }
    } else if (loginErrorCode == ErrorCode.kError_Async_NotLoggedIn) {
    //TODO handle the error derived from this login
    JOptionPane.showMessageDialog(
    null, "Not logged in, please check your input!",
    "Not Logged In",
    JOptionPane.ERROR_MESSAGE);
    } else {
    JOptionPane.showMessageDialog(
    null, "Not logged in, please check your internet connection!",
    "Not Logged In",
    JOptionPane.ERROR_MESSAGE);
    }
    }
     */// </editor-fold>
    /**
     * Effettua il login
     * @return
     */
    public int doLogin() {
        //creo il client ad ogni login
        httpClient = createHttpClient();
        log.debug("Target URL: " + loginPageUrl);
        try {
            HttpGet loginGet = new HttpGet(loginPageUrl);
            HttpResponse response = httpClient.execute(loginGet);
            HttpEntity entity = response.getEntity();

            log.debug("Login form get: " + response.getStatusLine());
            if (entity != null) {
                //log.debug(EntityUtils.toString(entity));
                entity.consumeContent();
            }
            log.debug("Initial set of cookies:");
            List<Cookie> cookies = ((DefaultHttpClient) httpClient).getCookieStore().getCookies();
            if (cookies.isEmpty()) {
                log.debug("None");
            } else {
                for (int i = 0; i < cookies.size(); i++) {
                    log.debug("- " + cookies.get(i).toString());
                }
            }

            HttpPost httpost = new HttpPost(loginPageUrl);

            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("email", email));
            nvps.add(new BasicNameValuePair("pass", passw));

            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

            HttpResponse responsePost = httpClient.execute(httpost);
            entity = responsePost.getEntity();

            log.debug("Login form get: " + responsePost.getStatusLine());
            if (entity != null) {
                //log.debug(EntityUtils.toString(entity));
                entity.consumeContent();
            }

            log.debug("Post logon cookies:");
            cookies = ((DefaultHttpClient) httpClient).getCookieStore().getCookies();
            if (cookies.isEmpty()) {
                log.debug("None");
            } else {
                for (int i = 0; i < cookies.size(); i++) {
                    log.debug("- " + cookies.get(i).toString());
                }
            }
        } catch (IOException ioe) {
            System.err.print("IOException");
            System.err.println(ioe.getMessage());
            ioe.printStackTrace();
            return ErrorCode.kError_Global_ValidationError;
        }

        return ErrorCode.Error_Global_NoError;
    }

    /**
     * Parsa l'home page e imposta l'uid,l'url della foto e il nome utente
     * @return un numero corrispondete a un errore o a successo
     */
    public int doParseHomePage() {


        String getMethodResponseBody = facebookGetMethod(homePageUrl);
        log.debug("=========HomePage: getMethodResponseBody begin=========");
        //System.out.print(getMethodResponseBody);
        log.debug("+++++++++HomePage: getMethodResponseBody end+++++++++");

        //deal with the cookies
        log.debug("The final cookies:");
        List<Cookie> finalCookies = ((DefaultHttpClient) httpClient).getCookieStore().getCookies();
        if (finalCookies.isEmpty()) {
            log.debug("None");
        } else {
            for (int i = 0; i < finalCookies.size(); i++) {
                log.debug("- " + finalCookies.get(i).toString());
                //looking for our uid
                if (finalCookies.get(i).getName().equals("c_user")) {
                    uid = finalCookies.get(i).getValue();
                }
            }
        }

        if (getMethodResponseBody == null) {
            log.debug("Can't get the home page! Exit.");
            return ErrorCode.Error_Async_UnexpectedNullResponse;
        } else {
//            log.debug("home : "+getMethodResponseBody);
        }
        if (uid == null) {
            log.debug("Can't get the user's id! Exit.");
            return ErrorCode.Error_System_UIDNotFound;
        }
        //<a href="http://www.facebook.com/profile.php?id=xxxxxxxxx" class="profile_nav_link">
        /*String uidPrefix = "<a href=\"http://www.facebook.com/profile.php?id=";
        String uidPostfix = "\" class=\"profile_nav_link\">";
        //getMethodResponseBody.lastIndexOf(str, fromIndex)
        int uidPostFixPos = getMethodResponseBody.indexOf(uidPostfix);
        if(uidPostFixPos >= 0){
        int uidBeginPos = getMethodResponseBody.lastIndexOf(uidPrefix, uidPostFixPos) + uidPrefix.length();
        if(uidBeginPos < uidPrefix.length()){
        logger.error("Can't get the user's id! Exit.");
        return FacebookErrorCode.Error_System_UIDNotFound;
        }
        uid = getMethodResponseBody.substring(uidBeginPos, uidPostFixPos);
        logger.info("UID: " + uid);
        }else{
        logger.error("Can't get the user's id! Exit.");
        return FacebookErrorCode.Error_System_UIDNotFound;
        }*/

        //find the channel
        String channelPrefix = ",\"channel";

        //rimuovo i spazi bicnhi per evitare che ce ne sia un dopo la virgola
        int channelBeginPos = StringUtils.strip(getMethodResponseBody).indexOf(channelPrefix) + channelPrefix.length();
        log.debug("channel position [ " + channelBeginPos + " ]");
//        if (channelBeginPos < channelPrefix.length()) {
        if (channelBeginPos < channelPrefix.length()) {
            log.debug("Error: Can't find channel!");
//            log.debug("page : \n "+getMethodResponseBody);
//            return ErrorCode.Error_System_ChannelNotFound;

            channel = null;
        } else {
            channel = getMethodResponseBody.substring(channelBeginPos,
                    channelBeginPos + 2);
            log.debug("Channel: " + channel);
        }

        //find the post form id
        // <input type="hidden" id="post_form_id" name="post_form_id"
        // value="3414c0f2db19233221ad8c2374398ed6" />
        String postFormIDPrefix = "<input type=\"hidden\" id=\"post_form_id\" name=\"post_form_id\" value=\"";
        int formIdBeginPos = getMethodResponseBody.indexOf(postFormIDPrefix) + postFormIDPrefix.length();
        if (formIdBeginPos < postFormIDPrefix.length()) {
            log.debug("Error: Can't find post form ID!");
            return ErrorCode.Error_System_PostFormIDNotFound;
        } else {
            post_form_id = getMethodResponseBody.substring(formIdBeginPos,
                    formIdBeginPos + 32);
            log.debug("post_form_id: " + post_form_id);
        }

        //riempo i campi dell'utente

        FacebookUserList.me = new FacebookUser(uid);
        //cerco il mio nome utente
        String userNameLiPrefix = "<li class=\"fb_menu\" id=\"fb_menu_account\">";
        int userNameLiPos = getMethodResponseBody.indexOf(userNameLiPrefix);
        String userNamePrefix = "class=\"fb_menu_link\">";
        int userNamePos = getMethodResponseBody.indexOf(userNamePrefix, userNameLiPos) + userNamePrefix.length();
        if (userNamePos == -1) {
            log.debug("Error: Can't find user name!");
            return ErrorCode.Error_System_UserNameNotFound;
        } else {
            FacebookUserList.me.name = getMethodResponseBody.substring(userNamePos,
                    getMethodResponseBody.indexOf("<", userNamePos));
            log.debug("user_name: " + FacebookUserList.me.name);
        }

        //ulr. della foto
        String userPhotoPrefix = "class=\"UIProfileImage UIProfileImage_LARGE\"  src=\"";
        int userPhotoPos = getMethodResponseBody.indexOf(userPhotoPrefix) + userPhotoPrefix.length();
        if (userPhotoPos == -1) {
            log.debug("Error: Can't find photo user!");
            // return ErrorCode.Error_System_UserNameNotFound;
        } else {
            FacebookUserList.me.thumbSrc = getMethodResponseBody.substring(userPhotoPos,
                    getMethodResponseBody.indexOf("\"", userPhotoPos));
            log.debug("photo uaser: " + FacebookUserList.me.thumbSrc);
            if (FacebookUserList.me.thumbSrc != null && !FacebookUserList.me.thumbSrc.equals("")) {
                try {
                    FacebookUserList.me.portrait = new ImageIcon(new URL(FacebookUserList.me.thumbSrc));
                } catch (MalformedURLException ex) {
                    log.error(ex);
                }
            }
        }

        return ErrorCode.Error_Global_NoError;
    }

    /**
     * Invia il messaggio all'utente con l'id passsato
     * @param uid
     * @param msg
     */
    public static void PostMessage(String uid, String msg) {
//        if (uid.equals(Launcher.uid)) {
//            return;
//        }

        log.debug("====== PostMessage begin======");

        log.debug("to:" + uid);
        log.debug("msg:" + msg);

        String url = "http://www.facebook.com/ajax/chat/send.php";

        // 
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("msg_text", (msg == null) ? "" : msg));
        nvps.add(new BasicNameValuePair("msg_id", new Random().nextInt(999999999) + ""));
        nvps.add(new BasicNameValuePair("client_time", new Date().getTime() + ""));
        nvps.add(new BasicNameValuePair("to", uid));
        nvps.add(new BasicNameValuePair("post_form_id", post_form_id));

        log.debug("executeMethod ing...");
        try {
            // postMethod
            String responseStr = facebookPostMethod("http://www.facebook.com", "/ajax/chat/send.php", nvps);
            //for (;;);{"t":"continue"}
            //for (;;);{"t":"refresh"}
            //for (;;);{"t":"refresh", "seq":0}
            //for (;;);{"error":0,"errorSummary":"","errorDescription":"No error.","payload":[],"bootload":[{"name":"js\/common.js.pkg.php","type":"js","src":"http:\/\/static.ak.fbcdn.net\/rsrc.php\/pkg\/60\/106715\/js\/common.js.pkg.php"}]}
            //for (;;);{"error":1356003,"errorSummary":"Send destination not online","errorDescription":"This person is no longer online.","payload":null,"bootload":[{"name":"js\/common.js.pkg.php","type":"js","src":"http:\/\/static.ak.fbcdn.net\/rsrc.php\/pkg\/60\/106715\/js\/common.js.pkg.php"}]}
            log.debug("+++++++++ PostMessage end +++++++++");
            // testHttpClient("http://www.facebook.com/home.php?");

            //incremento il messaggio
            incrementMessage();
            ResponseParser.messagePostingResultParser(uid, msg, responseStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void incrementMessage() {
        FacebookManager.seq++;
    }

    // <editor-fold defaultstate="collapsed" desc="Old Code">
/*public void keepRequesting() throws Exception {
    seq = getSeq();

    //go seq
    while (true) {
    //PostMessage("1190346972", "SEQ:"+seq);
    int currentSeq = getSeq();
    log.debug("My seq:" + seq + " | Current seq:" + currentSeq + '\n');
    if (seq > currentSeq) {
    seq = currentSeq;
    }

    while (seq <= currentSeq) {
    //get the old message between oldseq and seq
    String msgResponseBody = facebookGetMethod(getMessageRequestingUrl(seq));

    log.debug("=========msgResponseBody begin=========");
    //log.debug(msgResponseBody);
    log.debug("+++++++++msgResponseBody end+++++++++");

    try {

    ResponseParser.messageRequestResultParser(msgResponseBody);
    } catch (JSONException e) {
    e.printStackTrace();
    }
    seq++;
    }
    }
    }*/// </editor-fold>
    /**
     * Resituitsce il sequenz number dei messaggi
     * @return
     */
    public int getSeq() {
        //int tempSeq = -1;
        //for (;;);{"t":"refresh", "seq":0}
        if (channel != null) {
            try {
                String seqResponseBody = facebookGetMethod(getMessageRequestingUrl(Integer.parseInt(channel), -1));
                int tempSeq = parseSeq(seqResponseBody);
                log.debug("Channel [ " + channel + " ]  SEQ [ " + tempSeq + " ]");

                return tempSeq;
            } catch (JSONException e) {
                log.error(e);
                return -1;
            }
        } else {
            return -1;
        }

    }

    /**
     * Setta il canale sulla variabile channel e restituisce il sequenz number
     * @return il sequenz number trovato -1 se non è stato trovato un canale
     */
    public int findChannel() {
        List<Cookie> cookies = ((DefaultHttpClient) httpClient).getCookieStore().getCookies();

        log.debug("Post logon cookies:");
        cookies = ((DefaultHttpClient) httpClient).getCookieStore().getCookies();
        if (cookies.isEmpty()) {
            log.debug("None");
        } else {
            for (int i = 0; i < cookies.size(); i++) {
                log.debug("- " + cookies.get(i).toString());
            }
        }
        String lastChannel = Util.readProperties().getProperty(FACEBOOK_LAST_CHANNEL);
        try {
            log.debug("last cannel [ " + lastChannel + " ]");
            if (lastChannel != null && !lastChannel.equals("")) {
                String seqResponseBody = facebookGetMethod(getMessageRequestingUrl(Integer.parseInt(lastChannel), -1));
                int tempSeq = parseSeq(seqResponseBody);
                log.debug("Last Channel [ " + lastChannel + " ]  SEQ [ " + tempSeq + " ]");
                if (tempSeq >= 0) {
                    channel = lastChannel;
                    return tempSeq;
                }
            }
        } catch (JSONException ex) {
            log.error(ex);
        }

        int i = 1;
        int tryCoun = 1;
        while (true) {
            try {
                String seqResponseBody = facebookGetMethod(getMessageRequestingUrl(i, -1));
                int tempSeq = parseSeq(seqResponseBody);
                log.debug("Channel [ " + i + " ]  SEQ [ " + tempSeq + " ]");
                if (tempSeq >= 0) {
                    channel = "" + i;
                    //aggiorno la prprioetà last channel
                    Properties properties = Util.readProperties();
                    properties.setProperty(FACEBOOK_LAST_CHANNEL, i + "");
                    Util.writeProperties(properties);
                    return tempSeq;
                }
                i++;
                if (i == MAX_FACEBOOK_CHANNELS && tryCoun < MAX_CONNECTION_TRY) {
                    i = 0;
                    tryCoun++;

                    //riprovo il login
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException interruptedException) {
                    }
                    doLogin();

                } else if (i == MAX_FACEBOOK_CHANNELS && tryCoun == 3) {
                    return -1;
                }
            } catch (JSONException e) {
                log.error(e);
            }
        }
    }

    private int parseSeq(String msgResponseBody) throws JSONException {
        if (msgResponseBody == null) {
            return -1;
        }
        String prefix = "for (;;);";
        if (msgResponseBody.startsWith(prefix)) {
            msgResponseBody = msgResponseBody.substring(prefix.length());
        }

        //JSONObject body =(JSONObject) JSONValue.parse(msgResponseBody);
        JSONObject body = new JSONObject(msgResponseBody);
        if (body != null) {
            try {
                return body.getInt("seq");
            } catch (JSONException jSONException) {
                log.warn(jSONException);
                return -1;
            }
        } else {
            return -1;
        }
    }

    public String getMessageRequestingUrl(int channel, long seq) {
        //http://0.channel06.facebook.com/x/0/false/p_MYID=-1

        String value = "";
        if (channel < 10) {
            value = "0" + channel;
        } else {
            value = "" + channel;
        }
        String url = "http://0.channel" + value + ".facebook.com/x/0/false/p_" + uid + "=" + seq;
        log.debug("url [ " + url + " ]");
        return url;
    }

    /**
     * fetch user's info<br>
     * fetch buddy list<br>
     * store them in the BuddyList object
     */
    public static void getBuddyList() {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("buddy_list", "1"));
        nvps.add(new BasicNameValuePair("notifications", "1"));
        nvps.add(new BasicNameValuePair("force_render", "true"));
        //nvps.add(new BasicNameValuePair("popped_out", "false"));
        nvps.add(new BasicNameValuePair("post_form_id", post_form_id));
        nvps.add(new BasicNameValuePair("user", uid));

        try {
            String responseStr = facebookPostMethod("http://www.facebook.com", "/ajax/presence/update.php", nvps);
            log.debug(responseStr);
            //for (;;);{"error":0,"errorSummary":"","errorDescription":"No error.","payload":{"buddy_list":{"listChanged":true,"availableCount":1,"nowAvailableList":{"UID1":{"i":false}},"wasAvailableIDs":[],"userInfos":{"UID1":{"name":"Buddy 1","firstName":"Buddy","thumbSrc":"http:\/\/static.ak.fbcdn.net\/pics\/q_default.gif","status":null,"statusTime":0,"statusTimeRel":""},"UID2":{"name":"Buddi 2","firstName":"Buddi","thumbSrc":"http:\/\/static.ak.fbcdn.net\/pics\/q_default.gif","status":null,"statusTime":0,"statusTimeRel":""}},"forcedRender":true},"time":1209560380000}}  
            //for (;;);{"error":0,"errorSummary":"","errorDescription":"No error.","payload":{"time":1214626375000,"buddy_list":{"listChanged":true,"availableCount":1,"nowAvailableList":{},"wasAvailableIDs":[],"userInfos":{"1386786477":{"name":"\u5341\u4e00","firstName":"\u4e00","thumbSrc":"http:\/\/static.ak.fbcdn.net\/pics\/q_silhouette.gif","status":null,"statusTime":0,"statusTimeRel":""}},"forcedRender":null,"flMode":false,"flData":{}},"notifications":{"countNew":0,"count":1,"app_names":{"2356318349":"\u670b\u53cb"},"latest_notif":1214502420,"latest_read_notif":1214502420,"markup":"<div id=\"presence_no_notifications\" style=\"display:none\" class=\"no_notifications\">\u65e0\u65b0\u901a\u77e5\u3002<\/div><div class=\"notification clearfix notif_2356318349\" onmouseover=\"CSS.addClass(this, 'hover');\" onmouseout=\"CSS.removeClass(this, 'hover');\"><div class=\"icon\"><img src=\"http:\/\/static.ak.fbcdn.net\/images\/icons\/friend.gif?0:41046\" alt=\"\" \/><\/div><div class=\"notif_del\" onclick=\"return presenceNotifications.showHideDialog(this, 2356318349)\"><\/div><div class=\"body\"><a href=\"http:\/\/www.facebook.com\/profile.php?id=1190346972\"   >David Willer<\/a>\u63a5\u53d7\u4e86\u60a8\u7684\u670b\u53cb\u8bf7\u6c42\u3002 <span class=\"time\">\u661f\u671f\u56db<\/span><\/div><\/div>","inboxCount":"0"}},"bootload":[{"name":"js\/common.js.pkg.php","type":"js","src":"http:\/\/static.ak.fbcdn.net\/rsrc.php\/pkg\/60\/106715\/js\/common.js.pkg.php"}]}
            // testHttpClient("http://www.facebook.com/home.php?");
            ResponseParser.buddylistParser(responseStr);
        } catch (JSONException e) {
            log.error(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Set status message
     * 
     * @param statusMsg status message
     */
    public static void setStatusMessage(String statusMsg) {
        //post("www.facebook.com", "/updatestatus.php", "status=%s&post_form_id=%s");
        //post("www.facebook.com", "/updatestatus.php", "clear=1&post_form_id=%s");
        //new format:
        //profile_id=1190346972
        //&status=is%20hacking%20again
        //&home_tab_id=1
        //&test_name=INLINE_STATUS_EDITOR
        //&action=HOME_UPDATE
        //&post_form_id=3f1ee64144470cd29f28fb8b0354ef65
        //&_ecdc=false
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        if (statusMsg.length() < 1) {
            nvps.add(new BasicNameValuePair("clear", "1"));
        } else {
            nvps.add(new BasicNameValuePair("status", statusMsg));
        }

        nvps.add(new BasicNameValuePair("profile_id", uid));
        nvps.add(new BasicNameValuePair("home_tab_id", "1"));
        nvps.add(new BasicNameValuePair("test_name", "INLINE_STATUS_EDITOR"));
        nvps.add(new BasicNameValuePair("action", "HOME_UPDATE"));
        nvps.add(new BasicNameValuePair("post_form_id", post_form_id));
        log.debug("@executeMethod setStatusMessage() ing ... : " + statusMsg);
        // we don't care the response string now
        String respStr = facebookPostMethod("http://www.facebook.com", "/updatestatus.php", nvps);
        log.debug(respStr);
    }

    public static void shutdown() {
        httpClient.getConnectionManager().shutdown();
        httpClient = null;
    }

    /**
     * The general facebook post method.
     * @param host the host
     * @param urlPostfix the post fix of the URL
     * @param data the parameter
     * @return the response string
     */
    private static String facebookPostMethod(String host, String urlPostfix, List<NameValuePair> nvps) {
        log.debug(host + urlPostfix);
        String responseStr = null;
        try {
            HttpPost httpost = new HttpPost(host + urlPostfix);
            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

            // execute postMethod
            HttpResponse postResponse = httpClient.execute(httpost);
            HttpEntity entity = postResponse.getEntity();

            log.debug("facebookPostMethod: " + postResponse.getStatusLine());
            if (entity != null) {
                responseStr = EntityUtils.toString(entity);
                //log.debug(responseStr);
                entity.consumeContent();
            }
            log.debug("Post Method done(" + postResponse.getStatusLine().getStatusCode() + "), response string length: " + (responseStr == null ? 0 : responseStr.length()));
        } catch (IOException e) {
            log.debug(e.getMessage());
        }
        //TODO process the respons string
        //if statusCode == 200: no error;(responsStr contains "errorDescription":"No error.")
        //else retry?
        return responseStr;
    }

    /**
     * The general facebook get method.
     * @param url the URL of the page we wanna get
     * @return the response string
     */
    public static String facebookGetMethod(String url) {
        log.debug("url [ " + url + " ]");

        String responseStr = null;

        try {
            HttpGet loginGet = new HttpGet(url);
            HttpResponse response = httpClient.execute(loginGet);
            HttpEntity entity = response.getEntity();

            log.debug("response status [ " + response.getStatusLine() + " ]");
            if (entity != null) {
                responseStr = EntityUtils.toString(entity);
                entity.consumeContent();
            }

            int statusCode = response.getStatusLine().getStatusCode();

            /**
             * @fixme I am not sure if 200 is the only code that 
             *  means "success"
             */
            if (statusCode != 200) {
                //error occured
                log.debug("Error Occured! Status Code = " + statusCode);
                log.debug("response [ " + statusCode + " ]");
                responseStr = null;
            }
            log.debug("Get Method done(" + statusCode + "), response string length: " + (responseStr == null ? 0 : responseStr.length()));
        } catch (IOException e) {
            log.debug(e.getMessage());
        }

        return responseStr;
    }

    public static void main(String[] args) {
//        new FacebookManager();
////        System.out.println(facebookGetMethod("http://0.channel35.facebook.com/x/0/false/p_1567835536=-1"));
//        System.out.println(facebookGetMethod("http://0.channel35.facebook.com/x/0/false/p_1567835536=-1"));
    }
}


