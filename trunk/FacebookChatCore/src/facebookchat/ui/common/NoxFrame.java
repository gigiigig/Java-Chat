package facebookchat.ui.common;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.RoundRectangle2D;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSlider;
import javax.swing.MenuElement;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.sun.jna.examples.WindowUtils;

import facebookchat.common.Launcher;
import facebookchat.common.SystemPath;
import facebookchat.ui.main.AboutDialog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author shinysky <a href="mailto: shinysky1986@gmail.com"/>
 * 
 */
public class NoxFrame extends JFrame {

    private static Log log = LogFactory.getLog(NoxFrame.class);
    /**
     *
     */
    private static final long serialVersionUID = -4611481416817988409L;
    /**
     * 默认尺寸常量
     */
    public static final int WIDTH_MIN = 120;
    public static final int HEIGHT_MIN = 60;
    public static final int TITLE_HEIGHT = 20;
    public static final int FOOT_HEIGHT = 15;
    /**
     * 前景背景颜色
     */
    private Color foregrdColor = Color.WHITE;
    private Color backgrdColor = Color.BLACK;
    /**
     * 窗口透明度
     */
    private float opacity = 100;
    /**
     * 边框
     */
    private MatteBorder paneEdge;
    /**
     * 用来获取图片
     */
    private Toolkit tk;
    private Image background;
    private Image img_logo;
    /**
     * 各JPanel
     */
    private JImgPanel rootpane;
    private JPanel fakeFace;
    private Titlebar titlebar;
    protected JComponent container;
    private FootPane footpane;

    /**
     * NoxFrame 基类, 具有标题栏和状态栏; 内置窗口移动/最小化/关闭/窗口缩放功能
     *
     * @param title
     *            窗口标题
     * @param path_background
     *            背景图片路径
     * @param path_logo
     *            logo图片路径
     * @param path_title
     *            标题图片路径
     * @param path_minimize
     *            最小化按钮图片路径
     * @param path_minimize_rollover
     *            鼠标经过最小化按钮图片路径
     * @param path_maximize
     *            最大化按钮图片路径
     * @param path_maximize_rollover
     *            鼠标经过最大化按钮图片路径
     * @param path_normalimize
     *            恢复窗口按钮图片路径
     * @param path_normalimize_rollover
     *            鼠标经过恢复窗口按钮图片路径
     * @param path_close
     *            关闭窗口按钮图片路径
     * @param path_close_rollover
     *            鼠标经过关闭窗口按钮图片路径
     * @param IAmBase
     *            true: 是根窗口, 关闭按钮推出整个系统; false: 不是根窗口, 关闭按钮只关闭本窗口
     *
     * @see Titlebar
     * @see JFrame
     */
    protected NoxFrame(String title, String path_background,
            String path_logo, String path_logo_big,
            String path_title, final boolean IAmBase) {
        super(title);
        /*
         * try{
         * UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
         * }catch(Exception e) { log.debug(e.toString()); }
         */

        this.setUndecorated(true);

        /*
         * try { UIManager.setLookAndFeel(new SubstanceLookAndFeel()); } catch
         * (UnsupportedLookAndFeelException ex) { ex.printStackTrace(); }
         */

        tk = Toolkit.getDefaultToolkit();
        background = tk.getImage(path_background);
        img_logo = tk.getImage(path_logo_big);
        //程序图标
        this.setIconImage(img_logo);

        // 准备图片
        this.prepareImage(background, rootpane);

        // Container contentPane = getContentPane();

        this.setMinimumSize(new Dimension(WIDTH_MIN, HEIGHT_MIN));
        /**
         * 标题栏组件
         */
        titlebar = new Titlebar(this, path_logo, path_title, IAmBase);
        /**
         * 中部容器
         */
        container = new JPanel();
        container.setOpaque(false);

        /**
         * 底部类状态栏组件, 用于安放resize按钮
         */
        footpane = new FootPane(this);

        /**
         * 安放 标题栏, miniprofile, 列表窗口, 状态栏 的组件
         */
        rootpane = new JImgPanel(background);
        rootpane.setLayout(new BoxLayout(rootpane, BoxLayout.Y_AXIS));
        rootpane.setDoubleBuffered(true);
        rootpane.add(titlebar);
        rootpane.add(container);
        rootpane.add(footpane);
        // rootpane.setBackground(Color.BLACK);
        rootpane.setOpaque(false);

        /*
         * 设置图片边框代码 ImageBorder image_border = new ImageBorder( new
         * ImageIcon("resrc/upper_left.png").getImage(), new
         * ImageIcon("resrc/upper.png").getImage(), new
         * ImageIcon("resrc/upper_right.png").getImage(),
         *
         * new ImageIcon("resrc/left_center.png").getImage(), new
         * ImageIcon("resrc/right_center.png").getImage(),
         *
         * new ImageIcon("resrc/bottom_left.png").getImage(), new
         * ImageIcon("resrc/bottom_center.png").getImage(), new
         * ImageIcon("resrc/bottom_right.png").getImage() );
         */

        paneEdge = BorderFactory.createMatteBorder(2, 2, 2, 2, backgrdColor);// 颜色考虑作为参数设置
        /**
         * 处于最底层的JPanel, 含宽度为2的黑色边框 其上是rootpane
         */
        fakeFace = new JPanel();
        fakeFace.setBorder(paneEdge);
        fakeFace.setLayout(new BoxLayout(fakeFace, BoxLayout.Y_AXIS));
        fakeFace.add(rootpane);
        fakeFace.setBackground(backgrdColor);

        // rootpane.setBorder(paneEdge);
        this.setContentPane(fakeFace);
        /**
         * 添加鼠标移动监听器以实现窗口移动
         */
        MoveMouseListener mml = new MoveMouseListener(this.getRootPane(), this);
        this.getRootPane().addMouseListener(mml);
        this.getRootPane().addMouseMotionListener(mml);

        /**
         * 设置默认窗口操作(ALT+F4) TODO: 如果使用托盘图标, 则应将关闭操作设为dispose
         */
        if (IAmBase) {
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        } else {
            this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }
    }

    public void setTitleStr(String title) {
        titlebar.setTitleStr(title);
    }

    /**
     * 设置窗口前景颜色
     */
    public void setForegroundColor() {
        if ((float) (backgrdColor.getRed() * 0.3f + backgrdColor.getGreen() * 0.59f + backgrdColor.getBlue() * 0.11f) < 128) {
            foregrdColor = Color.WHITE;
        } else {
            foregrdColor = Color.BLACK;
        }
        // 似乎只有标题栏需要更新
        titlebar.setForegroundColor(foregrdColor.equals(Color.WHITE));
    }

    /**
     * 获取窗口前景颜色
     *
     * @return 前景色
     */
    public Color getForegroundColor() {
        return foregrdColor;
    }

    /**
     * 设置窗口背景颜色, 然后根据背景色设置前景色
     *
     * @param color
     *            背景色
     */
    public void setBackgroundColor(Color color) {
        fakeFace.setBackground(color);
        paneEdge = BorderFactory.createMatteBorder(2, 2, 2, 2, color);// 颜色考虑作为参数设置
        fakeFace.setBorder(paneEdge);
        backgrdColor = color;
        // 同时更新前景色
        setForegroundColor();
    }

    /**
     * 获取窗口背景颜色
     *
     * @return 背景色
     */
    public Color getBackgroundColor() {
        return backgrdColor;
    }

    /**
     * 设置不透明度
     *
     * @return 是否设置成功
     */
    public boolean setOpacity(float alpha) {
        if (WindowUtils.isWindowAlphaSupported()) {
            WindowUtils.setWindowAlpha(this, alpha);
            opacity = alpha;
            return true;
        } else {
            log.debug("Sorry, WindowAlpha is not Supported");
            return false;
        }
    }

    /**
     * 获取不透明度
     *
     * @return 不透明度值
     */
    public float getOpacity() {
        return opacity;
    }

    /**
     * 将JFrame设为圆角 如果每次resize都重设则会增加CPU占用率; 目前没找到较好的解决方法, 所以该方法暂时闲置.
     */
    public void setFrameMask() {
        // 圆角Mask
        RoundRectangle2D.Float mask = new RoundRectangle2D.Float(0, 0, this.getWidth(), this.getHeight(), 5, 5);
        WindowUtils.setWindowMask(this, mask);
    }

    public JComponent getContainer() {
        return container;
    }

    public void resetMaximizeIcon() {
        titlebar.setToMaximizeIcon();
    }

    public void resetNormalizeIcon() {
        titlebar.setToNormalizeIcon();
    }

    /**
     * 该操作暂时不可逆转; 即不能重新添加监听器
     */
    public void removeResizeListener() {
        footpane.removeResizeListener();
    }
}

/**
 * 标题栏组件 应当至少具备两种功能: 1. 如果是主窗口, 则标题栏为图片 2. 如果是分支窗口, 则标题栏为文字
 * 
 * @author shinysky
 * 
 */
class Titlebar extends JPanel {

    /**
     * what's this svID?
     */
    private static final long serialVersionUID = -538877811148092522L;
    JButton blogo;
    JLabel lab_title;
    //JButton bconfig;
    // JSlider slider;
    JButton bminimize;
    JButton bmaximize;
    JButton bclose;
    FrameConfigDialog transparencyConfigBar;
    String ttl;

    // boolean windowStateIsMax;
    /**
     * 标题栏JPanel类, 高度20;
     *
     * @param parent
     *            父组件, 用于控制其最大最小化
     * @param path_logo
     *            logo图片路径
     * @param title
     *            标题图片路径(IAmBase==true)/标题文字(IAmBase==false)
     * @param path_minimize
     *            最小化按钮图片路径
     * @param path_minimize_rollover
     *            鼠标经过最小化按钮图片路径
     * @param path_maximize
     *            最大化按钮图片路径
     * @param path_maximize_rollover
     *            鼠标经过最大化按钮图片路径
     * @param path_normalimize
     *            恢复窗口按钮图片路径
     * @param path_normalimize_rollover
     *            鼠标经过恢复窗口按钮图片路径
     * @param path_close
     *            关闭窗口按钮图片路径
     * @param path_close_rollover
     *            鼠标经过关闭窗口按钮图片路径
     * @param IAmBase
     *            true: 是根窗口, 关闭按钮推出整个系统并且标题栏显示图片; false: 不是根窗口,
     *            关闭按钮只关闭本窗口并且标题栏显示文字.
     */
    Titlebar(final NoxFrame parent, String path_logo, String title, final boolean IAmBase) {
        transparencyConfigBar = new FrameConfigDialog(parent);

        Dimension btnsize = new Dimension(NoxFrame.TITLE_HEIGHT, NoxFrame.TITLE_HEIGHT);
        blogo = new JButton(new ImageIcon(path_logo));
        blogo.setToolTipText(getHtmlText("About NoX"));
        blogo.setSize(btnsize);
        blogo.setPreferredSize(btnsize);
        blogo.setMaximumSize(btnsize);
        blogo.setMinimumSize(btnsize);
        blogo.setBorderPainted(false);
        blogo.setContentAreaFilled(false);
        blogo.setOpaque(false);

        /*
         * if (IAmBase) { bconfig = new JButton(new
         * ImageIcon("resrc\\buttons\\config.png")); bconfig.setRolloverIcon(new
         * ImageIcon( "resrc\\buttons\\config_rollover.png"));
         * bconfig.setSize(new Dimension(20, 20)); bconfig.setPreferredSize(new
         * Dimension(20, 20)); bconfig.setMaximumSize(new Dimension(20, 20));
         * bconfig.setMinimumSize(new Dimension(20, 20));
         * bconfig.setBorderPainted(false); bconfig.setContentAreaFilled(false);
         * bconfig.setOpaque(false); bconfig.addActionListener(new
         * ActionListener() { public void actionPerformed(ActionEvent e) {
         * log.debug("You just clicked the config button"); //
         * parent.setBackground(Color.BLUE); // slider.setValue(100);
         *  } }); }
         */

        blogo.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                final JPopupMenu m = new JPopupMenu();
                // use a heavyweight popup to avoid having it clipped
                // by the window mask
                if (IAmBase) {
                    m.add(new AbstractAction("Set Window's Color") {

                        /**
                         *
                         */
                        private static final long serialVersionUID = -729947600305959488L;

                        public void actionPerformed(ActionEvent e) {
                            Color color = JColorChooser.showDialog(parent,
                                    "Select a color for the GUI", Color.orange);
                            if (color != null) {
                                parent.setBackgroundColor(color);
                            }
                        }
                    });
                    m.add(new AbstractAction("Set Window's Transparency") {

                        /**
                         *
                         */
                        private static final long serialVersionUID = 8141980952424431845L;

                        public void actionPerformed(ActionEvent e) {
                            transparencyConfigBar.setLocation(blogo.getLocationOnScreen().x, blogo.getLocationOnScreen().y + NoxFrame.TITLE_HEIGHT);
                            transparencyConfigBar.Show();
                        }
                    });
                    m.addSeparator();
                }
                m.add(new AbstractAction("About NoX") {

                    /**
                     *
                     */
                    private static final long serialVersionUID = 1L;

                    public void actionPerformed(ActionEvent e) {
                        AboutDialog about = new AboutDialog();
                        DialogEarthquakeCenter dec = new DialogEarthquakeCenter(
                                about);
                        about.pack();
                        about.setModal(false);
                        about.setSize(new Dimension(500, 350));
                        about.setPreferredSize(new Dimension(500, 350));
                        about.setLocation(new Point(300, 150));
                        about.setVisible(true);
                        dec.startNudging();// 对话框必须setModal (false)才可以抖动, 否则不行
                    }
                });
                m.add(new AbstractAction("Exit") {

                    /**
                     *
                     */
                    private static final long serialVersionUID = 1L;

                    public void actionPerformed(ActionEvent e) {
                        if (IAmBase) {
                            System.exit(0);
                        } else {
                            parent.dispose();
                        }
                    }
                });
                MenuElement els[] = m.getSubElements();
                for (int i = 0; i < els.length; i++) {
                    els[i].getComponent().setBackground(Color.WHITE);
                }
                m.setLightWeightPopupEnabled(true);
                //m.setBackground(Color.YELLOW);
                //m.setForeground(Color.RED);
                m.pack();
                // 位置应该是相对于JButton的位置
                m.show((Component) e.getSource(), 0, NoxFrame.TITLE_HEIGHT);
            }
        });

        ttl = title;
        if (IAmBase) {// 如果是主窗口
            lab_title = new JLabel(new ImageIcon(title));
        } else {// 如果不是主窗口
            // lab_title = new JLabel("NoX");
            lab_title = new JLabel(title);
            Font font = new Font("宋体-方正超大字符集", Font.BOLD, 24);
            // Font font = new Font("Times New Roman", Font.BOLD, 24);
            lab_title.setForeground(Color.WHITE);
            lab_title.setFont(font);
        }

        /*
         * btitle = new JButton(new ImageIcon("resrc\\nox.png"));
         * btitle.setSize(new Dimension(60,20)); btitle.setPreferredSize(new
         * Dimension(60,20)); btitle.setMaximumSize(new Dimension(60,20));
         * btitle.setMinimumSize(new Dimension(60,20));
         * btitle.setBorderPainted(false); btitle.setContentAreaFilled(false);
         * btitle.setOpaque(false);
         */
        bminimize = new JButton(new ImageIcon(SystemPath.BUTTONS_RESOURCE_PATH + "minimize.png"));
        bminimize.setRolloverIcon(new ImageIcon(SystemPath.BUTTONS_RESOURCE_PATH + "minimize_rollover.png"));
        bminimize.setToolTipText(getHtmlText("Minimize"));
        bminimize.setSize(btnsize);
        bminimize.setPreferredSize(btnsize);
        bminimize.setMaximumSize(btnsize);
        bminimize.setMinimumSize(btnsize);
        bminimize.setBorderPainted(false);
        bminimize.setContentAreaFilled(false);
        bminimize.setOpaque(false);
        bminimize.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int state = parent.getExtendedState();
                // 设置图标化(iconifies)位
                // Set the iconified bit
                state |= JFrame.ICONIFIED;

                // 图标化Frame
                // Iconify the frame
                parent.setExtendedState(state);
            }
        });

        bmaximize = new JButton(new ImageIcon(SystemPath.BUTTONS_RESOURCE_PATH + "maximize.png"));
        bmaximize.setRolloverIcon(new ImageIcon(SystemPath.BUTTONS_RESOURCE_PATH + "maximize_rollover.png"));
        bmaximize.setToolTipText(getHtmlText("Maximize"));
        bmaximize.setSize(btnsize);
        bmaximize.setPreferredSize(btnsize);
        bmaximize.setMaximumSize(btnsize);
        bmaximize.setMinimumSize(btnsize);
        bmaximize.setBorderPainted(false);
        bmaximize.setContentAreaFilled(false);
        bmaximize.setOpaque(false);
        bmaximize.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int state = parent.getExtendedState();

                // 设置图标化(iconifies)位
                // Set the iconified bit
                // log.debug("window state: " + state);
                switch (state) {
                    // 如果当前是最大状态, 则正常化
                    case JFrame.MAXIMIZED_BOTH:
                        state &= JFrame.NORMAL;// '&', not '|'
                        // log.debug("max->normal");
                        setToMaximizeIcon();
                        break;
                    // 如果当前不是最大状态, 则最大化
                    default:
                        state |= JFrame.MAXIMIZED_BOTH;
                        // log.debug("normal->max");
                        setToNormalizeIcon();
                        // Dimension dim =
                        // Toolkit.getDefaultToolkit().getScreenSize();
                        // parent.setBounds(0, 0, dim.width, dim.height );
                        break;
                }
                // log.debug("window state: " + state);
                // log.debug("system: " +
                // Toolkit.getDefaultToolkit().isFrameStateSupported(JFrame.MAXIMIZED_BOTH)
                // );
                // 设置窗口状态
                parent.setExtendedState(state);
            }
        });

        bclose = new JButton(new ImageIcon(SystemPath.BUTTONS_RESOURCE_PATH + "close.png"));
        bclose.setRolloverIcon(new ImageIcon(SystemPath.BUTTONS_RESOURCE_PATH + "close_rollover.png"));
        bclose.setToolTipText(getHtmlText("Close"));
        bclose.setSize(btnsize);
        bclose.setPreferredSize(btnsize);
        bclose.setMaximumSize(btnsize);
        bclose.setMinimumSize(btnsize);
        bclose.setBorderPainted(false);
        bclose.setContentAreaFilled(false);
        bclose.setOpaque(false);
        bclose.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                //parent.dispose();
                parent.setVisible(false);
                if (IAmBase)// 如果是根窗口
                {
                    Launcher.shutdown();
                    System.exit(0);// .............
                }
            }
        });

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setAlignmentX(JComponent.CENTER_ALIGNMENT);// 设置对齐方式
        this.add(blogo);
        this.add(Box.createHorizontalGlue());
        this.add(lab_title);
        // this.add(btitle);
        this.add(Box.createHorizontalGlue());
        /*
         * if (IAmBase) { this.add(bconfig); // this.add(slider); }
         */
        this.add(bminimize);
        this.add(bmaximize);
        this.add(bclose);
        this.setOpaque(false);
    }

    /**
     * 返回TooltipTxt的html形式
     * @param text
     * @return
     */
    private String getHtmlText(String text) {
        return ("<html><BODY bgColor=#ffffff><Font color=black>" + text + "</Font></BODY></html>");
    }

    public void setToMaximizeIcon() {
        bmaximize.setIcon(new ImageIcon(SystemPath.BUTTONS_RESOURCE_PATH + "maximize.png"));
        bmaximize.setRolloverIcon(new ImageIcon(SystemPath.BUTTONS_RESOURCE_PATH + "maximize_rollover.png"));
    }

    public void setToNormalizeIcon() {
        bmaximize.setIcon(new ImageIcon(SystemPath.BUTTONS_RESOURCE_PATH + "normalize.png"));
        bmaximize.setRolloverIcon(new ImageIcon(SystemPath.BUTTONS_RESOURCE_PATH + "normalize_rollover.png"));
    }

    public void setForegroundColor(boolean white) {
        // 还需要改!!!!!!!!!!!!!!!
        if (white) {
            lab_title.setIcon(new ImageIcon(ttl));
        } else {
            lab_title.setIcon(new ImageIcon(ttl + ".png"));
        }
    }

    public void setTitleStr(String title) {
        lab_title.setText(title);
    }
}

class FootPane extends JPanel {

    /**
     *
     */
    private static final long serialVersionUID = 9009828062432005570L;
    // 位于窗口右下角用于resize
    JLabel resizeArea;
    ResizeListener resizer;

    /**
     * 底部组件, 用于安放重设尺寸按钮
     *
     * @param parent
     *            父组件, 用于重设尺寸
     */
    FootPane(JFrame parent) {
        resizeArea = new JLabel(new AngledLinesWindowsCornerIcon());
        //resizeButn.setBorderPainted(false);
        //resizeButn.setContentAreaFilled(false);
        Dimension btnsize = new Dimension(NoxFrame.FOOT_HEIGHT, NoxFrame.FOOT_HEIGHT);
        resizeArea.setSize(btnsize);
        resizeArea.setPreferredSize(btnsize);
        resizeArea.setMaximumSize(btnsize);
        resizeArea.setMinimumSize(btnsize);
        // resizeButn.setOpaque(false);
        //resizeButn.setMargin(new Insets(0,0,0,0));

        resizer = new ResizeListener(parent, resizeArea);
        resizeArea.addMouseListener(resizer);
        resizeArea.addMouseMotionListener(resizer);

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(Box.createHorizontalGlue());
        this.add(resizeArea);
        this.setOpaque(false);
    }

    /**
     * 该操作暂时不可逆转; 即不能重新添加监听器
     */
    public void removeResizeListener() {
        resizeArea.removeMouseListener(resizer);
        resizeArea.removeMouseMotionListener(resizer);
    }
}

/**
 * 窗口透明度调节对话框
 * 
 * @author shinysky
 * 
 */
class FrameConfigDialog extends JDialog {

    private static Log log = LogFactory.getLog(FrameConfigDialog.class);
    /**
     *
     */
    private static final long serialVersionUID = -5405688695983281310L;
    NoxFrame parent;
    JLabel transparent;
    JLabel opaque;
    JSlider slider;
    JButton close;
    JPanel root;

    FrameConfigDialog(NoxFrame nf) {
        // super(nf, "", true);
        parent = nf;
        this.setUndecorated(true);
        transparent = new JLabel("Transparent");
        opaque = new JLabel("Opaque");

        slider = new JSlider(20, 100);
        slider.setValue(100);
        slider.setOpaque(false);
        slider.requestFocus();
        slider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                float value = slider.getValue();
                parent.setOpacity(value * 0.01f);
                //new NoxToolkit().setOpacity(value * 0.01f);
            }
        });
        close = new JButton(new ImageIcon(SystemPath.BUTTONS_RESOURCE_PATH + "close.png"));
        close.setPressedIcon(new ImageIcon(SystemPath.BUTTONS_RESOURCE_PATH + "close_rollover.png"));
        close.setOpaque(false);
        close.setContentAreaFilled(false);
        Dimension bnsize = new Dimension(20, 20);
        close.setSize(bnsize);
        close.setPreferredSize(bnsize);
        close.setMaximumSize(bnsize);
        close.setMinimumSize(bnsize);
        close.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ((JDialog) FrameConfigDialog.this).setVisible(false);
            }
        });

        root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.X_AXIS));
        root.add(transparent);
        root.add(slider);
        root.add(opaque);
        root.add(close);
        root.setBackground(parent.getBackgroundColor());

        this.setContentPane(root);
        Dimension size = new Dimension(250, 20);
        this.setSize(size);
        this.setPreferredSize(size);
        this.setMaximumSize(size);
        this.setMinimumSize(size);
        slider.addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent e) {
                log.debug("slider get focusssssssssss");
            }

            public void focusLost(FocusEvent e) {
                log.debug("slider lost focusssssssssss");
                FrameConfigDialog.this.setVisible(false);
            }
        });
    }

    public void Show() {
        Color color = parent.getBackgroundColor();
        // 先把背景色RGB值转为灰度值, 判断是否是深色,
        // 如果是深色则把标签文字设为白色, 否则设为黑色
        // ----------真人性化, ft
        if ((float) (color.getRed() * 0.3f + color.getGreen() * 0.59f + color.getBlue() * 0.11f) < 128) {
            System.out.println("GRAY: " + (float) (color.getRed() * 0.3f + color.getGreen() * 0.59f + color.getBlue() * 0.11f));
            transparent.setForeground(Color.WHITE);
            opaque.setForeground(Color.WHITE);
        } else {
            transparent.setForeground(Color.BLACK);
            opaque.setForeground(Color.BLACK);
        }
        root.setBackground(parent.getBackgroundColor());
        this.setVisible(true);
    }
}
