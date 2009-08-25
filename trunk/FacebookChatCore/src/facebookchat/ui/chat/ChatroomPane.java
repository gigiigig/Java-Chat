package facebookchat.ui.chat;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import facebookchat.common.SystemPath;
import facebookchat.ui.common.DialogEarthquakeCenter;
import facebookchat.ui.common.JNABalloon;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
 * Created on 2006-9-9
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
/**
 * 更加"完美"的聊天窗口: 可以选择字体及颜色; 可以插入图片
 * 
 * @author shinysky
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 * 
 * (要时刻准备着从服务器接收信息) 信息格式: 第一部分:who sayTo who at time 第二部分:the message
 * 将消息添加到消息记录, 然后通过检查发信人和noDisturb变量来决定是否添加该信息到历史消息窗口
 */
public class ChatroomPane extends JSplitPane implements ActionListener// ,MouseListener
{

    private static Log log = LogFactory.getLog(Chatroom.class);
    /**
     * we don't know much about it
     */
    private static final long serialVersionUID = -1915394855935441419L;
    /**
     * 历史消息JScrollPane
     */
    private JScrollPane sp_historymsg;
    /**
     * 历史消息JTextPane
     */
    private JTextPane tp_historymsg;
    /**
     * 消息输入窗口及零件
     */
    private JPanel p_inputpaneAndButtons;
    /**
     * 消息JScrollPane
     */
    private JScrollPane sp_input;
    /**
     * 消息输入框 JTextPane
     */
    private JTextPane tp_input;
    /**
     * 按钮JPanel, 含插入表情按钮/闪屏按钮/.../发送按钮
     */
    private JPanel p_buttons;
    /**
     * 插入表情JButton
     */
    private JButton b_emotion;
    /**
     * 表情选择对话框
     */
    private FaceDialog selFace;
    /**
     * 闪屏振动
     */
    private JButton b_nudge;
    private static final String nudgeMsg = "[F:999]";
    /**
     * 截屏按钮
     */
    private JButton b_snapshot;
    private JButton b_snapconfig;
    JPopupMenu menuSnap;
    JMenuItem doSnap;
    JCheckBoxMenuItem hideFrame;
    /**
     * 图片索引格式化处理
     */
    public static final DecimalFormat fmNum = new DecimalFormat("000");
    /**
     * 消息发送JButton
     */
    private JButton b_send;
    /**
     * 历史消息,用于保存操作
     */
    String historymsg_save;
    /**
     * 当前处于输入框中的消息, 用于保存操作
     */
    String currentmsg_save;
    /**
     * 文本风格模型
     */
    StyledDocument styledDoc;
    /**
     * 字体风格
     */
    /**
     * 普通
     */
    Style normal;
    /**
     * 蓝色
     */
    Style blue;
    /**
     * 绿色
     */
    Style green;
    /**
     * 灰色
     */
    Style gray;
    /**
     * 红色
     */
    Style red;
    /**
     * 黑体
     */
    Style bold;
    /**
     * 斜体
     */
    Style italic;
    /**
     * 大号
     */
    Style bigSize;
    /**
     * 其它变量
     */
    /**
     * 日期标签格式
     */
    private Format fmDate = new SimpleDateFormat("yyyy/MM/dd E HH:mm:ss");
    /**
     * 字符串处理中间变量
     */
    // private int position;
    /**
     * 字符串长度
     */
    // private int strLength;
    /**
     * 欢迎消息
     */
    private String sayHello;
    /***************************************************************************
     * //
     */
    /**
     * 字体清晰设置(通过重载paintComponent()) (以下三段代码) ...But CPU占用率达到100% !Faint! 宣告失败;
     *
     * 在一些可能导致屏幕刷新的按钮响应或键盘响应中添加 setRenderingHints(hints); 会解决部分问题 但不是根本的方法,
     * 甚至用户的选择文本操作也可以轻易使清晰属性"丢失" 另外,如果文本中包含动态图片,也会使属性丢失
     */
    /*
     * public void paintComponent(Graphics g) { super.paintComponent(g);
     * Graphics2D g2 = (Graphics2D)g; g2.setRenderingHints(hints); //
     * RenderingHints hints_in = new RenderingHints(null); //
     * hints_in.put(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON); //
     * ChatRoomPane.this.setRenderingHints(hints_in); }
     *
     */
    /**
     * 设置字体清晰并重绘组件
     *
     * @param h
     *            RenderingHints
     */
    /*
     * public void setRenderingHints(RenderingHints h) { hints = h; repaint(); }
     * private RenderingHints hints = new RenderingHints(null);
     */
    // ********************************************************
    Chatroom parent;

    /**
     * JSplitPane 聊天组件, 含输入框/消息窗口/表情按钮/闪屏按钮/.../发送按钮 等
     *
     * @param par
     *            父组件, 用于使窗口par振动
     */
    public ChatroomPane(Chatroom par) {
        super(JSplitPane.VERTICAL_SPLIT);
        parent = par;

        sayHello = new String(
                "\t------====  Welcome to the Chat Room  ====------\n" + "\t  ------====     What do U wanna say ?   ====------\n");
        // strLength = sayHello.length();
        // position = 0;
        /**
         * 历史消息窗口
         */
        tp_historymsg = new JTextPane();
        historymsg_save = new String();
        historymsg_save += sayHello;
        styledDoc = tp_historymsg.getStyledDocument();
        /**
         * 新建风格
         */
        normal = styledDoc.addStyle("normal", null);
        StyleConstants.setFontFamily(normal, "SansSerif");

        blue = styledDoc.addStyle("blue", normal);
        StyleConstants.setForeground(blue, Color.blue);

        green = styledDoc.addStyle("green", normal);
        StyleConstants.setForeground(green, Color.GREEN.darker());

        gray = styledDoc.addStyle("gray", normal);
        StyleConstants.setForeground(gray, Color.GRAY);

        red = styledDoc.addStyle("red", normal);
        StyleConstants.setForeground(red, Color.red);

        bold = styledDoc.addStyle("bold", normal);
        StyleConstants.setBold(bold, true);

        italic = styledDoc.addStyle("italic", normal);
        StyleConstants.setItalic(italic, true);

        bigSize = styledDoc.addStyle("bigSize", normal);
        StyleConstants.setFontSize(bigSize, 24);
        /**
         * 添加风格化文本(欢迎消息)
         */
        styledDoc.setLogicalStyle(0, red);

        tp_historymsg.replaceSelection(sayHello);

        // tp_historymsg.setToolTipText("History Messages");
        tp_historymsg.setBackground(new Color(180, 250, 250));
        tp_historymsg.setSelectionColor(Color.YELLOW);
        /**
         * 如果设置为可编辑:用户则可以自由编辑 如果设置为不可编辑:程序无法向其中添加文本.怎么办??
         *
         * ...已解决(不知是否足够安全): 通常情况下设为不可编辑 当向其中添加文本时,临时设为可编辑;
         * 并重新设置插入符位置(保证插入符位置在文本尾) (如果不校正插入符位置,用户尽管不能编辑,但还是可以改变插入符位置)
         * 添加文本后,重新设置为不可编辑. 搞定!^-^ 详见appendToHMsg();
         */
        tp_historymsg.setEditable(false);
        sp_historymsg = new JScrollPane(tp_historymsg);
        sp_historymsg.setAutoscrolls(true);

        p_inputpaneAndButtons = new JPanel();

        /**
         * 消息输入窗口
         */
        tp_input = new JTextPane();
        // tp_msg.setText(sayHello);
        tp_input.setToolTipText(getHtmlText("Input your message and press \"Send\" <br>or press Enter"));

        /**
         * 键盘事件监听器/事件处理
         */
        tp_input.addKeyListener(new KeyListener() {

            public void keyPressed(KeyEvent event) {
                int keyCode = event.getKeyCode();
                /**
                 * 如果按键为Enter则发送信息
                 * 如果是Ctrl+Enter发送消息则 && event.isControlDown()
                 */
                if (keyCode == KeyEvent.VK_ENTER) {
                    log.debug("You press the key : Enter");
                    sendMessage();
                }
            }

            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
            }

            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
            }
        });
        sp_input = new JScrollPane(tp_input);

        /**
         * 插入表情按钮 及 发送按钮
         */
        p_buttons = new JPanel();

        Dimension buttonSize = new Dimension(26, 26);

        b_emotion = new JButton(new ImageIcon(SystemPath.ICONS_RESOURCE_PATH + "emotion.png"));
        b_emotion.setToolTipText(getHtmlText("Insert a emotion image"));
        b_emotion.setActionCommand("Emotion");
        b_emotion.addActionListener(this);
        // b_InsertImg.setContentAreaFilled(false);
        b_emotion.setSize(buttonSize);
        b_emotion.setPreferredSize(buttonSize);
        b_emotion.setMaximumSize(buttonSize);
        b_emotion.setMinimumSize(buttonSize);

        /**
         * 如果不阻塞其他窗口的话, 会出现无法获取已选择的表情的情况.
         */
        selFace = new FaceDialog("Insert a face", true, SystemPath.FACES_RESOURCE_PATH);
        // 和FaceDialog.setDefaultLookAndFeelDecorated(true);不能同时使用
        selFace.setBounds(450, 350, FaceDialog.FACECELLWIDTH * FaceDialog.FACECOLUMNS, FaceDialog.FACECELLHEIGHT * FaceDialog.FACEROWS + 30);// 30为b_cr_cancel的高度
        selFace.pack();

        b_nudge = new JButton(new ImageIcon(SystemPath.ICONS_RESOURCE_PATH + "shake.png"));
        b_nudge.setToolTipText(getHtmlText("Give a nudge!"));
        b_nudge.setActionCommand("Nudge");
        b_nudge.addActionListener(this);
        b_nudge.setSize(buttonSize);
        b_nudge.setPreferredSize(buttonSize);
        b_nudge.setMaximumSize(buttonSize);
        b_nudge.setMinimumSize(buttonSize);

        b_snapshot = new JButton(new ImageIcon(SystemPath.ICONS_RESOURCE_PATH + "snapshot.png"));
        b_snapshot.setToolTipText(getHtmlText("Snap it!"));
        b_snapshot.setActionCommand("Snapshot");
        b_snapshot.addActionListener(this);
        b_snapshot.setSize(buttonSize);
        b_snapshot.setPreferredSize(buttonSize);
        b_snapshot.setMaximumSize(buttonSize);
        b_snapshot.setMinimumSize(buttonSize);

        b_snapconfig = new JButton(new ImageIcon(SystemPath.ICONS_RESOURCE_PATH + "snapconfig.png"));
        b_snapconfig.setMargin(new Insets(0, 0, 0, 0));
        b_snapconfig.setToolTipText(getHtmlText("Snap Config"));
        b_snapconfig.setActionCommand("SnapshotConfig");
        b_snapconfig.addActionListener(this);
        b_snapconfig.setSize(new Dimension(buttonSize.width / 2, buttonSize.height));
        b_snapconfig.setPreferredSize(new Dimension(buttonSize.width / 2, buttonSize.height));
        b_snapconfig.setMaximumSize(new Dimension(buttonSize.width / 2, buttonSize.height));
        b_snapconfig.setMinimumSize(new Dimension(buttonSize.width / 2, buttonSize.height));

        menuSnap = new JPopupMenu();
        doSnap = new JMenuItem("Let's GO!");
        hideFrame = new JCheckBoxMenuItem("Hide this window while snapping",
                true);
        doSnap.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (hideFrame.getState())// 如果用户选择隐藏窗口, 则隐藏
                {
                    // parent.setVisible(false);
                    parent.setState(JFrame.ICONIFIED);
                    // log.debug("in if: What the hell is wrong with
                    // you!" + hideFrame.isSelected());
                }
                try {
                    // 因为需要执行完事件处理程序菜单才能消失, 所以下面这句无用.
                    // menuSnap.setVisible(false);
                    Thread.sleep(300);// 睡500毫秒是为了让主窗完全不见
                    Robot ro = new Robot();
                    Toolkit tk = Toolkit.getDefaultToolkit();
                    Dimension screenSize = tk.getScreenSize();
                    Rectangle rec = new Rectangle(0, 0, screenSize.width,
                            screenSize.height);
                    BufferedImage buffImg = ro.createScreenCapture(rec);
                    final JDialog fakeWin = new JDialog(parent, true);
                    fakeWin.addKeyListener(new KeyListener() {

                        public void keyPressed(KeyEvent event) {
                            int keyCode = event.getKeyCode();
                            /**
                             * 如果按键为ESC则退出截屏
                             */
                            if (keyCode == KeyEvent.VK_ESCAPE) {
                                fakeWin.dispose();
                            }
                        }

                        public void keyTyped(KeyEvent e) {
                            // TODO Auto-generated method stub
                        }

                        public void keyReleased(KeyEvent e) {
                            // TODO Auto-generated method stub
                        }
                    });
                    ScreenCapturer temp = new ScreenCapturer(fakeWin, buffImg,
                            screenSize.width, screenSize.height);
                    fakeWin.getContentPane().add(temp, BorderLayout.CENTER);
                    fakeWin.setUndecorated(true);
                    fakeWin.setSize(screenSize);
                    fakeWin.setVisible(true);
                    fakeWin.setAlwaysOnTop(true);

                    parent.setState(JFrame.NORMAL);
                    buffImg = temp.getWhatWeGot();
                    if (buffImg != null) {
                        ;//ChatroomPane.this.sendAPicture(buffImg);
                    } else {
                        log.debug("phew~we got nothing.");
                    }
                } catch (Exception exe) {
                    exe.printStackTrace();
                }
            }
        });

        menuSnap.add(doSnap);
        menuSnap.addSeparator();
        menuSnap.add(hideFrame);
        menuSnap.pack();

        // b_send = new JButton("Send");
        b_send = new JButton(new ImageIcon(SystemPath.ICONS_RESOURCE_PATH + "send.png"));
        b_send.setMnemonic('S');
        // b_send.setPreferredSize(new Dimension(100,40));
        b_send.setActionCommand("Send");
        b_send.setToolTipText(getHtmlText("Send"));
        b_send.addActionListener(this);
        // b_send.setContentAreaFilled(false);
        b_send.setSize(buttonSize);
        b_send.setPreferredSize(buttonSize);
        b_send.setMaximumSize(buttonSize);
        b_send.setMinimumSize(buttonSize);

        p_buttons.setOpaque(false);
        p_buttons.setLayout(new BoxLayout(p_buttons, BoxLayout.X_AXIS));
        p_buttons.add(b_emotion);
        p_buttons.add(b_nudge);
        p_buttons.add(b_snapshot);
        p_buttons.add(b_snapconfig);
        p_buttons.add(Box.createHorizontalGlue());
        p_buttons.add(b_send);
        // p_buttons.add(p_side, BorderLayout.CENTER);
        // p_buttons.add(b_send, BorderLayout.WEST);

        p_inputpaneAndButtons.setOpaque(false);
        // p_msgAndButtons.setBackground(Color.blue);
        p_inputpaneAndButtons.setLayout(new BorderLayout());
        p_inputpaneAndButtons.add(p_buttons, BorderLayout.NORTH);
        p_inputpaneAndButtons.add(sp_input, BorderLayout.CENTER);

        /*
         * msgPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, sp_hmsg,
         * p_msgAndButtons); msgPane.setOneTouchExpandable(true);
         * msgPane.setOpaque(false);
         */
        // msgPane.setContinuousLayout(true);
        this.setSize(new Dimension(Chatroom.WIDTH_DEFLT,
                Chatroom.HEIGHT_DEFLT - 35));
        this.setDividerLocation(0.65);// 必须先指定尺寸才有效
        this.setResizeWeight(0.62d);
        this.setDividerSize(3);
        // this.setOneTouchExpandable(true);
        this.add(sp_historymsg);
        this.add(p_inputpaneAndButtons);
        // this.setLayout(new BorderLayout());
        // this.add(msgPane, BorderLayout.CENTER);
        // this.setOpaque(false);//在当前使用的背景下, 设为透明似乎不太好看...
    }

    private String getHtmlText(String text) {
        return ("<html><BODY bgColor=#ffffff><Font color=black>" + text + "</Font></BODY></html>");
    }

    /**
     * 显示系统消息
     *
     * @param msg
     *            系统消息
     */
    public void showMsgDialog(String msg) {
        JOptionPane.showMessageDialog((Component) null, msg,
                "Message form the Server", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showFailedSendingMsg(String msg) {
        tp_historymsg.setEditable(true);
        tp_historymsg.setCaretPosition(styledDoc.getLength());// !!!
        styledDoc.setLogicalStyle(tp_historymsg.getCaretPosition(), italic);
        tp_historymsg.replaceSelection("Sorry, failed to send out the msg:\n" + msg + '\n');
        tp_historymsg.setEditable(false);
    }

    public void showFeedbackMsg(String msg, String errorString) {
        tp_historymsg.setEditable(true);
        tp_historymsg.setCaretPosition(styledDoc.getLength());// !!!
        styledDoc.setLogicalStyle(tp_historymsg.getCaretPosition(), red);
        tp_historymsg.replaceSelection(errorString + "\n");
        tp_historymsg.setEditable(false);
    }

    /**
     * 用于外部程序调用,以显示消息
     *
     * @param strs
     *            1:sender;2:receiver;3:time;4:msg
     * @param incomingPic 外来图片
     */
    public void incomingMsgProcessor(String sender, String time,
            Object msgdata) {
        if (msgdata instanceof String) {
            incomingMsgProcessor(sender, time, (String) msgdata, null);
        } else if (msgdata instanceof ImageIcon) {
            incomingMsgProcessor(sender, time, null, (ImageIcon) msgdata);
        }
    }

    public void incomingMsgProcessor(String sender, String time,
            final String strmsg, ImageIcon incomingPic) {
        log.debug("playAudio()...");
        Thread playThd = new Thread(new Runnable() {

            public void run() {
                if (strmsg != null && strmsg.equals(nudgeMsg)) {
                    playNudgeAudio();
                } else {
                    playAudio();
                }
            }
        }, "Beeper");
        playThd.start();

        /**
         * 处理外部传来的消息字符串
         */
        String label = "[" + sender + "@" + time + "]";

        StringBuffer strbuf_msg = null;
        if (strmsg != null) {
            strbuf_msg = new StringBuffer(strmsg);
            int caretPos = -1;

            for (; (caretPos = strbuf_msg.indexOf("^n", caretPos + 1)) >= 0;) {
                // 把"^n"替换为"\n"
                strbuf_msg.replace(caretPos, caretPos + 2, "\n");
            }
        }

        appendToHMsg(label, (strbuf_msg != null) ? strbuf_msg.toString() : null, incomingPic, true, false);
    }

    /**
     * 接收消息时播放提示音
     *
     */
    public void playAudio() {
        final AudioClip msgBeep;
        try {
            // AudioClip audioClip = Applet.newAudioClip(completeURL)
            // codeBase = new URL("file:" + System.getProperty("user.dir") +
            // "/");
            URL url = new URL("file:/" + System.getProperty("user.dir") + System.getProperty("file.separator") + SystemPath.AUDIO_RESOURCE_PATH + "typewpcm.wav");
            msgBeep = Applet.newAudioClip(url);
            msgBeep.play();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            log.debug(e.toString());
        }
    }

    /**
     * 接收/发送闪屏时播放提示音
     */
    public void playNudgeAudio() {
        final AudioClip msgBeep;
        try {
            // AudioClip audioClip = Applet.newAudioClip(completeURL)
            // codeBase = new URL("file:" + System.getProperty("user.dir") +
            // "/");
            URL url = new URL("file:/" + System.getProperty("user.dir") + System.getProperty("file.separator") + SystemPath.AUDIO_RESOURCE_PATH + "nudgewpcm.wav");
            msgBeep = Applet.newAudioClip(url);
            msgBeep.play();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            log.debug(e.toString());
        }
    }

    /**
     * 向历史消息窗口添加文本
     *
     * 注意:如果ckb_nodisturb 为 true,表示防打扰模式开启, 此时从服务器传来的群聊消息只会被添加到历史消息字符串
     * 而不会被添加到窗口中 只有私聊对象的消息才会被添加到窗口中
     *
     * @param label
     *            发送者/接收者/发送时间 标签
     * @param msg
     *            要添加到消息记录的字符串
     * @param incomingPic 外来图片
     * @param visible
     *            是否要添加到历史消息窗口中(可见)
     * @param isFromMe
     *            是否是自己向外发送的消息
     *
     * 原来这个函数的功能是将消息输入框的字符串插入到历史消息窗口, 显然这是不够的.
     * 添加参数后,从服务器接受到的历史消息可以通过调用这个函数来插入到历史消息窗口.
     * 简言之,增强了这个函数的通用性.将消息添加到消息记录的功能从中移出.
     */
    public void appendToHMsg(String label, String msg, ImageIcon incomingPic, boolean visible,
            boolean isFromMe) {
        StringBuffer label_buf = new StringBuffer(label);

        // log.debug("label_buf :" + label_buf);
        // log.debug("msg_buf :" + msg_buf);

        // playAudio();
        // 如果是将自己的消息添加到窗口则为绿标签, 否则为蓝色.
        Style labelStyle = isFromMe ? green : blue;

        /**
         * 将消息添加到消息记录
         */
        historymsg_save += (label_buf + "\n");

        /**
         * 是否是闪屏振动消息
         */
        if (msg != null && msg.equals(nudgeMsg)) {
            // 使用灰色标签
            labelStyle = gray;

            DialogEarthquakeCenter dec = new DialogEarthquakeCenter(parent);
            dec.startNudging();
            tp_historymsg.setEditable(true);
            tp_historymsg.setCaretPosition(styledDoc.getLength());
            styledDoc.setLogicalStyle(tp_historymsg.getCaretPosition(),
                    labelStyle);
            tp_historymsg.replaceSelection(label + '\n');
            tp_historymsg.setCaretPosition(styledDoc.getLength());// !!!
            styledDoc.setLogicalStyle(tp_historymsg.getCaretPosition(), italic);
            tp_historymsg.replaceSelection("YOU JUST RECEIVED A NUDGE MESSAGE\n");
            tp_historymsg.setEditable(false);
            return;
        }

        /**
         * if 判断是否应该添加此消息到历史消息窗口
         */
        if (visible)// 应该添加此消息到历史消息窗口
        {
            tp_historymsg.setEditable(true);

            // 消息发送人/对象/发送时间 信息
            // 插入信息标签
            tp_historymsg.setCaretPosition(styledDoc.getLength());
            styledDoc.setLogicalStyle(tp_historymsg.getCaretPosition(),
                    labelStyle);
            tp_historymsg.replaceSelection(label + '\n');
            log.debug("label :" + label);

            if (msg != null && !msg.equals("")) {
                StringBuffer msg_buf = new StringBuffer(msg);
                historymsg_save += (msg_buf + "\n");
                //用户发送空消息已被禁止, 所以如果发过来的是空消息,
                //则说明发送过来的是图片.
                //所以当msg不为空的时候需要显示消息
                log.debug("msg :" + msg);

                tp_historymsg.setCaretPosition(styledDoc.getLength());// !!!
                styledDoc.setLogicalStyle(tp_historymsg.getCaretPosition(), bold);
                // *****************************************************
                /**
                 * 将字符串表情图片化,然后插入HMsg中 同时维护一个字符串HMsg.
                 */
                int position = 0, caretPos = 0;

                // 从position开始寻找字符串"[F:",找到返回'['的位置,找不到"[F:"返回-1
                for (; (caretPos = msg_buf.indexOf("[F:", position)) >= 0;) {
                    // log.debug("caretPos : " + caretPos);
                    // StringBuffer msgpiece = new
                    // StringBuffer(msg_buf.substring(caretPos, caretPos + 6));
                    // log.debug("msgpiece : " + msgpiece);

                    // 7: 代表表情的字符串的长度
                    if (msg_buf.substring(caretPos, caretPos + 7).matches(
                            "\\[F\\:[0-9][0-9][0-9]\\]")) {// 如果符合正则表达式
                        // 插入从position到caretPos前一个字符的子字符串
                        tp_historymsg.setCaretPosition(styledDoc.getLength());
                        tp_historymsg.replaceSelection(msg_buf.substring(position,
                                caretPos));
                        // 插入接下来的长度为7的子字符串所表示的表情图片
                        tp_historymsg.setCaretPosition(styledDoc.getLength());
                        int faceindex = Integer.parseInt(msg_buf.substring(
                                caretPos + 3, caretPos + 6));
                        tp_historymsg.insertIcon(getImageIconFace(faceindex));
                        // 后移position
                        position = caretPos + 7;
                    } else {// 如果不符合正则表达式
                        // 插入从position到caretPos+3前一个字符的子字符串
                        tp_historymsg.setCaretPosition(styledDoc.getLength());
                        tp_historymsg.replaceSelection(msg_buf.substring(position,
                                caretPos + 3));
                        // 后移position
                        position = caretPos + 3;
                    }
                }
                // 插入剩余子字符串
                tp_historymsg.setCaretPosition(styledDoc.getLength());
                tp_historymsg.replaceSelection(msg_buf.substring(position) + '\n');
            }
            if (incomingPic != null) {
                tp_historymsg.setCaretPosition(styledDoc.getLength());
                tp_historymsg.insertIcon(incomingPic);
                tp_historymsg.setCaretPosition(styledDoc.getLength());
                tp_historymsg.replaceSelection("\n");
            }
            // log.debug("msg_buf.substring(position) :" +
            // msg_buf.substring(position));
            // *****************************************************
            tp_historymsg.setEditable(false);// 重新设为不可编辑
        }
        this.repaint();
    }

    /**
     * 添加表情图片到msg输入窗口中
     *
     * @param selectedFace
     *            被选择的图片的索引
     */
    private void appendFaceToInputPane(int selectedFace) {
        tp_input.setEditable(true);
        /**
         * 以插入字符串代替直接插入图片
         */
        // log.debug("fmNum.format(selectedFace) :" +
        tp_input.replaceSelection("[F:" + fmNum.format(selectedFace) + ']');
    }

    /**
     * 添加表情图片到msg输入窗口中
     *
     * @param selectedFace
     *            被选择的图片
     */
    @SuppressWarnings("unused")
    private void appendFaceToInputPane(ImageIcon selectedFace) {
        tp_input.setEditable(true);
        /**
         * 直接插入图片
         */
        tp_input.insertIcon(selectedFace);
    }

    /**
     * 获取历史消息
     *
     * @return history messages
     */
    public String getHistoryMsgs() {
        // return tp_hmsg.getText();
        return historymsg_save;// 返回所维护的版本(含有字符串化表情)
    }

    /**
     * 将用户输入的消息发送到历史消息窗口 (还有,要发送到服务器)
     */
    private void sendMessage() {
        Thread sender = new Thread(new Runnable() {

            public void run() {
                //log.debug("sendMessage(): >" + tp_input.getText() + "<");
                String msg = tp_input.getText();
                tp_input.setText("");// 输入框清空

                if (msg.equals("")) {
                    log.debug("You're trying to send a empty message, it's not suggested.");
                    //TODO 可以尝试弹出气泡提示
                    final String BALLOON_TEXT = "<html><center>" + "You're trying to send an empty message<br>" + "which is not suggested/supported.<br>" + "(Click to dismiss this balloon)</center></html>";
                    JNABalloon balloon = new JNABalloon(BALLOON_TEXT, tp_input, 100, 20);
                    balloon.showBalloon();

                    return;
                }
                /**
                 * 格式化日期
                 */
                Date date = new Date();
                // fmDate = new SimpleDateFormat("yyyy/MM/dd E HH:mm:ss");
                String label = "I say@" + fmDate.format(date) + ":";

                appendToHMsg(label, msg, null, true, true);

                /**
                 * 向对方发送消息
                 */
                StringBuffer strbuf_msg = new StringBuffer(msg);
                int caretPos = -1;

                // 从position开始寻找字符串"[F:",找到返回'['的位置,找不到"[F:"返回-1
                for (; (caretPos = strbuf_msg.indexOf("\r\n", caretPos + 1)) >= 0;) {
                    // 把"\n"替换为"^n"
                    strbuf_msg.replace(caretPos, caretPos + 2, "^n");
                }
                log.debug("strbuf_msg :" + strbuf_msg);

                boolean succeed = parent.SendMsg(new String(strbuf_msg));
                if (!succeed) {
                    //TODO tell user what happend.
                    showFailedSendingMsg(new String(strbuf_msg));
                }
            }
        });
        sender.start();
    }

    private void sendANudge() {
        /**
         * 格式化日期
         */
        Date date = new Date();
        // fmDate = new SimpleDateFormat("yyyy/MM/dd E HH:mm:ss");
        String label = "Sending a nudge to " + parent.getRoomName() + "@" + fmDate.format(date) + ":";

        //appendToHMsg(label, tp_input.getText(), null, true, true);
        appendToHMsg(label, null, null, true, true);
        /**
         * 向对方发送消息 999:表示表情索引
         */
        boolean succeed = parent.SendMsg(nudgeMsg);
        if (!succeed) {
            //TODO tell user what happend.
            showFailedSendingMsg("(It's a nudge actually.)");
        }
    }

    /**
     * 根据索引获取表情图片
     *
     * @param index
     *            图片索引
     * @return 表情图片
     */
    private ImageIcon getImageIconFace(int index) {
        if (index < 105) {
            return new ImageIcon(SystemPath.FACES_RESOURCE_PATH + index + ".gif");
        } else {
            return new ImageIcon(SystemPath.FACES_RESOURCE_PATH + "newFace\\" + (int) (index - 105) + ".png");
        }
    }

    /**
     * (按钮)事件响应
     */
    public void actionPerformed(ActionEvent e) {
        JButton srcButton = (JButton) e.getSource();
        if (srcButton.getActionCommand().equals("Send")) {
            log.debug("You clicked the button : Send");
            // insert(getText(), null);
            sendMessage();
        } else if (srcButton.getActionCommand().equals("Emotion")) {
            /**
             * 插入表情: 目前只能插入到输入框中, 插入到历史消息框中还没有实现. 如果没有直接拷贝的方法, 那么只好用另外一个方法:
             * 就是把表情用字符表示,在插入到历史消息框中时进行字符过滤 不过这样比较麻烦
             *
             * ...还不是太麻烦,已解决,用舍车保帅的办法
             */
            log.debug("You clicked the button : InsertImage");
            // 显示表情选择窗口
            selFace.setLocationRelativeTo(b_emotion);
            selFace.setVisible(true);

            int selectedfaceIndex = selFace.getSelectedFaceIndex();
            if (selectedfaceIndex != -1) {
                log.debug("You selected the face : " + selectedfaceIndex + ".gif");
                appendFaceToInputPane(selectedfaceIndex);
            }
            /*
             * ImageIcon selectedface = getImageIconFace(selectedfaceIndex); if
             * (selectedface != null) { log.debug("You selected the
             * face : " + selectedfaceIndex + ".gif");
             * appendFaceToInputPane(selectedface); }
             */
        } else if (srcButton.getActionCommand().equals("Nudge")) {
            DialogEarthquakeCenter dec = new DialogEarthquakeCenter(parent);
            dec.startNudging();// 对话框必须setModal (false)才可以抖动, 否则不行
            //TODO 这里应该播放声音, 调试时禁止, 防止重叠, 冲突.
            // playNudgeAudio();
            /**
             * 发送一个闪屏振动
             */
            sendANudge();
        } else if (srcButton.getActionCommand().equals("SendPic")) {
        } else if (srcButton.getActionCommand().equals("SendFile")) {
        } else if (srcButton.getActionCommand().equals("Snapshot")) {
            // 位置应该是相对于JButton的位置
            //menuSnap.show((Component) e.getSource(), 0, 26);
            doSnap.doClick();
        } else if (srcButton.getActionCommand().equals("SnapshotConfig")) {
            // 位置应该是相对于JButton的位置
            menuSnap.show((Component) e.getSource(), 0, 26);
        }
    }
}
