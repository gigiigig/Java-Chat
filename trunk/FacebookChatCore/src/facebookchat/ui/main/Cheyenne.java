package facebookchat.ui.main;

import java.awt.Color;
import java.awt.Dimension;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

import facebookchat.common.FacebookBuddyList;
import facebookchat.common.FacebookManager;
import facebookchat.common.SystemPath;
import facebookchat.ui.common.NoxFrame;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author shinysky
 *
 */
public class Cheyenne extends NoxFrame {

    private static Log log = LogFactory.getLog(Cheyenne.class);
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     * 默认尺寸常量
     */
    public static final int WIDTH_DEFLT = 260;
    public static final int WIDTH_PREF = 260;
    public static final int WIDTH_MAX = 2000;
    public static final int WIDTH_MIN = 200;
    public static final int HEIGHT_DEFLT = 500;
    public static final int HEIGHT_PREF = 500;
    public static final int HEIGHT_MAX = 2000;
    public static final int HEIGHT_MIN = 300;
    public static final int InterStatusCheckingsSleepTime = 60 * 1000;
    /**
     * 各JPanel
     */
    private MiniProfilePane profile;
    private ListsPane buddyListPane;
    /**
     * 轮流ping所有好友获取其状态信息
     */
    Thread onlineBuddyListChecker;

    /**
     *
     */
    public Cheyenne() {
        super("NoX: a IM system", SystemPath.IMAGES_RESOURCE_PATH + "bkgrd.png",
                SystemPath.LOGO_RESOURCE_PATH + "NoXlogo_20.png",
                SystemPath.LOGO_RESOURCE_PATH + "NoXlogo_48.png",
                SystemPath.LOGO_RESOURCE_PATH + "nox.png", true);

        JComponent contentPane = this.getContainer();
        Cheyenne.this.setBounds(600, 80, WIDTH_DEFLT, HEIGHT_DEFLT);
        Cheyenne.this.setSize(new Dimension(WIDTH_DEFLT, HEIGHT_DEFLT));
        Cheyenne.this.setPreferredSize(new Dimension(WIDTH_PREF, HEIGHT_PREF));
        Cheyenne.this.setMaximumSize(new Dimension(WIDTH_MAX, HEIGHT_MAX));
        Cheyenne.this.setMinimumSize(new Dimension(WIDTH_MIN, HEIGHT_MIN));

        initMyStatus();

        buddyListPane = new ListsPane(this);

        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        contentPane.add(profile);
        contentPane.add(buddyListPane);

        setForegroundColor();

        //onlineBuddyListChecker = new Thread(new NoOneLivesForeverExceptMe(), "OnlineBuddyListChecker");
        //onlineBuddyListChecker.start();
    }

    /**
     * 初始化个人设置
     * @param conn
     * @param meSqltableName
     */
    private void initMyStatus() {
        ImageIcon portrait;
        try {
            portrait = new ImageIcon(
                    new URL(FacebookBuddyList.me.thumbSrc));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            portrait = new ImageIcon(SystemPath.PORTRAIT_RESOURCE_PATH + "q_silhouette.gif");
        } catch (Exception e) {
            log.error(e);
            portrait = new ImageIcon(SystemPath.PORTRAIT_RESOURCE_PATH + "q_silhouette.gif");
        }
        try {
            String name = FacebookBuddyList.me.name;
            String sign = FacebookBuddyList.me.status + "(" + FacebookBuddyList.me.statusTimeRel + ")";
            /**
             * mini profile 组件 含: 头像, 昵称, 状态, 签名
             */
            profile = new MiniProfilePane(this, portrait, name, sign);
            // profile.setBackground(new Color(0, 255, 0));
            profile.setSize(new Dimension(WIDTH_DEFLT, 50));
            profile.setPreferredSize(new Dimension(WIDTH_PREF, 50));
            profile.setMaximumSize(new Dimension(WIDTH_MAX, 50));
            profile.setMinimumSize(new Dimension(WIDTH_MIN, 50));
        } catch (Exception e) {
            log.error(e);
        }
    }

    public void updateMyStatus() {
        ImageIcon portrait;
        try {
            portrait = new ImageIcon(
                    new URL(FacebookBuddyList.me.thumbSrc));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            portrait = new ImageIcon(SystemPath.PORTRAIT_RESOURCE_PATH + "q_silhouette.gif");
        }

        if (profile != null && FacebookBuddyList.me != null) {
            profile.setPortrait(portrait);
            profile.setNickName(FacebookBuddyList.me.name);
            profile.setSign(FacebookBuddyList.me.status, FacebookBuddyList.me.statusTimeRel);
        }
    }

    /**
     * request buddylist every 3 mins
     *
     * 与Launcher中go()中的某个线程功能重复，故弃用
     * @author shinysky
     * @deprecated
     */
    private class NoOneLivesForeverExceptMe implements Runnable {

        NoOneLivesForeverExceptMe() {
        }

        public void run() {
            while (true) {
                //一轮轮询之后休息一段时间(InterStatusCheckingsSleepTime)
                FacebookManager.getBuddyList();
                log.debug("Now refresh the buddy list");
                buddyListPane.refresh();
                try {
                    Thread.sleep(Cheyenne.InterStatusCheckingsSleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    continue;
                }
            }
        }
    }

    public void updateBuddyListPane() {
        buddyListPane.refresh();
    }

    /**
     * 设置窗口前景颜色
     */
    public void setForegroundColor() {
        super.setForegroundColor();
        Color color = super.getForegroundColor();
        profile.setForegroundColor(color);
    }

    /**
     * 设置窗口背景颜色
     */
    public void setBackgroundColor(Color color) {
        super.setBackgroundColor(color);
        if (color.equals(Color.WHITE)) {
            buddyListPane.setBackground(Color.GRAY);
        } else {
            buddyListPane.setBackground(color);
        }
    }
}
