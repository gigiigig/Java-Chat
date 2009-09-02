/*
 * ScreenCapturer.java
 *
 * Created on 2007年8月30日, 下午12:46
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package facebookchat.ui.chat;

/**
 * derived from online source code
 * 
 * @author lbf, ruislan
 * @author shinysky
 */
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import facebookchat.common.SystemPath;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

// 一个暂时类，用于显示当前的屏幕图像
public class ScreenCapturer extends JPanel implements MouseListener,
        MouseMotionListener {

    private static Log log = LogFactory.getLog(ScreenCapturer.class);
    /**
     *
     */
    private static final long serialVersionUID = 6818414590924411239L;
    private BufferedImage bi;
    private BufferedImage get = null;
    private int width, height;
    private int startX, startY, endX, endY, tempX, tempY;
    private JDialog fakewindow;
    private Rectangle select = new Rectangle(0, 0, 0, 0);// 表示选中的区域
    private Cursor cs = new Cursor(Cursor.CROSSHAIR_CURSOR);// 表示一般情况下的鼠标状态
    private States current = States.DEFAULT;// 表示当前的编辑状态
    private Rectangle[] rec;// 表示八个编辑点的区域
    // 下面四个常量,分别表示谁是被选中的那条线上的端点
    public static final int START_X = 1;
    public static final int START_Y = 2;
    public static final int END_X = 3;
    public static final int END_Y = 4;
    private int currentX, currentY;// 当前被选中的X和Y,只有这两个需要改变
    private Point p = new Point();// 当前鼠标移的地点
    private boolean showTip = true;// 是否显示提示.如果鼠标左键一按,则提示不再显了
    //边界工具条
    JPanel editPane = new JPanel();
    JButton b_send = new JButton(new ImageIcon(SystemPath.BUTTONS_RESOURCE_PATH + "send.png"));
    JButton b_copy = new JButton(new ImageIcon(SystemPath.BUTTONS_RESOURCE_PATH + "copy.png"));
    JButton b_save = new JButton(new ImageIcon(SystemPath.BUTTONS_RESOURCE_PATH + "save.png"));
    JButton b_cancel = new JButton(new ImageIcon(SystemPath.BUTTONS_RESOURCE_PATH + "cancel.png"));
    // 右键菜单
    JPopupMenu menuSnap = new JPopupMenu();
    JMenuItem send = new JMenuItem("Send");
    JMenuItem copy = new JMenuItem("Copy");
    JMenuItem save = new JMenuItem("Save");
    JMenuItem cacel = new JMenuItem("Cancel");

    public ScreenCapturer(JDialog fakeWindow, BufferedImage bi, int width,
            int height) {
        this.fakewindow = fakeWindow;
        this.bi = bi;
        this.width = width;
        this.height = height;
        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        Dimension bsize = new Dimension(20, 20);
        b_send.setSize(bsize);
        b_send.setPreferredSize(bsize);
        b_send.setMaximumSize(bsize);
        b_send.setMinimumSize(bsize);
        b_copy.setSize(bsize);
        b_copy.setPreferredSize(bsize);
        b_copy.setMaximumSize(bsize);
        b_copy.setMinimumSize(bsize);
        b_save.setSize(bsize);
        b_save.setPreferredSize(bsize);
        b_save.setMaximumSize(bsize);
        b_save.setMinimumSize(bsize);
        b_cancel.setSize(bsize);
        b_cancel.setPreferredSize(bsize);
        b_cancel.setMaximumSize(bsize);
        b_cancel.setMinimumSize(bsize);

        b_send.setToolTipText(getHtmlText("Send"));
        b_copy.setToolTipText(getHtmlText("Copy"));
        b_save.setToolTipText(getHtmlText("Save"));
        b_cancel.setToolTipText(getHtmlText("Cancel"));

        b_send.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                whatWeGet();
            }

            ;
        });
        b_copy.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                whatWeGet();
                doCopy(get);
                //get清空
                get = null;
            }

            ;
        });
        b_save.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                whatWeGet();
                //文件保存对话框
                doSave(get);
                //get清空
                get = null;
            }

            ;
        });
        b_cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                HideEditPane();
                showTip = true;
                // p = getPoint();
                startX = 0;
                startY = 0;
                endX = 0;
                endY = 0;
                repaint();
            }

            ;
        });
        editPane.setLayout(new BoxLayout(editPane, BoxLayout.X_AXIS));
        editPane.add(Box.createHorizontalGlue());
        editPane.add(b_send);
        editPane.add(b_copy);
        editPane.add(b_save);
        editPane.add(b_cancel);
        editPane.add(Box.createHorizontalGlue());
        editPane.addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
                editPane.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

            public void mouseExited(MouseEvent e) {
                doMouseMoved(e);
            }

            public void mousePressed(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
            }
        });

        editPane.setBackground(new Color(204, 255, 255));
        editPane.setSize(new Dimension(84, 24));
        editPane.setPreferredSize(new Dimension(84, 24));
        editPane.setMaximumSize(new Dimension(84, 24));
        editPane.setMinimumSize(new Dimension(84, 24));
        editPane.setBounds(1020, 765, 84, 24);

        this.setLayout(null);
        this.add(editPane);
        HideEditPane();

        menuSnap.add(send);
        menuSnap.add(copy);
        menuSnap.add(save);
        menuSnap.add(cacel);
        send.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                whatWeGet();
            }
        });
        copy.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                whatWeGet();
                doCopy(get);
                //get清空
                get = null;
            }
        });
        save.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                whatWeGet();
                //文件保存对话框
                doSave(get);
                //get清空
                get = null;
            }
        });
        cacel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                HideEditPane();
                showTip = true;
                // p = getPoint();
                startX = 0;
                startY = 0;
                endX = 0;
                endY = 0;
                repaint();
            }
        });

        initRecs();
    }

    private String getHtmlText(String text) {
        return ("<html><BODY bgColor=#ffffff><Font color=black>" + text + "</Font></BODY></html>");
    }

    public BufferedImage getWhatWeGot() {
        return get;
    }

    public void doSave(BufferedImage get) {
        try {
            if (get == null) {
                JOptionPane.showMessageDialog(this, "图片不能为空!!", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            JFileChooser jfc = new JFileChooser(".");
            jfc.addChoosableFileFilter(new GIFfilter());
            jfc.addChoosableFileFilter(new BMPfilter());
            jfc.addChoosableFileFilter(new JPGfilter());
            jfc.addChoosableFileFilter(new PNGfilter());
            int i = jfc.showSaveDialog(this);
            if (i == JFileChooser.APPROVE_OPTION) {
                File file = jfc.getSelectedFile();
                String about = "PNG";
                String ext = file.toString().toLowerCase();
                javax.swing.filechooser.FileFilter ff = jfc.getFileFilter();
                if (ff instanceof JPGfilter) {
                    about = "JPG";
                    if (!ext.endsWith(".jpg")) {
                        String ns = ext + ".jpg";
                        file = new File(ns);
                    }
                } else if (ff instanceof PNGfilter) {
                    about = "PNG";
                    if (!ext.endsWith(".png")) {
                        String ns = ext + ".png";
                        file = new File(ns);

                    }
                } else if (ff instanceof BMPfilter) {
                    about = "BMP";
                    if (!ext.endsWith(".bmp")) {
                        String ns = ext + ".bmp";
                        file = new File(ns);

                    }
                } else if (ff instanceof GIFfilter) {
                    about = "GIF";
                    if (!ext.endsWith(".gif")) {
                        String ns = ext + ".gif";
                        file = new File(ns);

                    }
                }
                if (ImageIO.write(get, about, file)) {
                    JOptionPane.showMessageDialog(this, "保存成功！");
                } else {
                    JOptionPane.showMessageDialog(this, "保存失败！");
                }
            }
        } catch (Exception exe) {
            exe.printStackTrace();
        }
    }

    /**
     *公共的处理把当前的图片加入剪帖板的方法
     */
    public void doCopy(final BufferedImage image) {
        try {
            if (get == null) {
                JOptionPane.showMessageDialog(this, "图片不能为空!!", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Transferable trans = new Transferable() {

                public DataFlavor[] getTransferDataFlavors() {
                    return new DataFlavor[]{DataFlavor.imageFlavor};
                }

                public boolean isDataFlavorSupported(DataFlavor flavor) {
                    return DataFlavor.imageFlavor.equals(flavor);
                }

                public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
                    if (isDataFlavorSupported(flavor)) {
                        return image;
                    }
                    throw new UnsupportedFlavorException(flavor);
                }
            };
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(trans, null);
            JOptionPane.showMessageDialog(this, "已复制到系统粘帖板!!");
        } catch (Exception exe) {
            exe.printStackTrace();
            JOptionPane.showMessageDialog(this, "复制到系统粘帖板出错!!", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initRecs() {
        rec = new Rectangle[8];
        for (int i = 0; i < rec.length; i++) {
            rec[i] = new Rectangle();
        }
    }

    public void paintComponent(Graphics g) {
        // 画背景
        g.drawImage(bi, 0, 0, width, height, this);
        g.setColor(Color.BLUE.darker().darker());
        // 画四条线
        g.drawLine(startX, startY, endX, startY);
        g.drawLine(startX, endY, endX, endY);
        g.drawLine(startX, startY, startX, endY);
        g.drawLine(endX, startY, endX, endY);
        int x = startX < endX ? startX : endX;
        int y = startY < endY ? startY : endY;
        // 选区
        select = new Rectangle(x, y, Math.abs(endX - startX), Math.abs(endY - startY));
        // 中点
        int x1 = (startX + endX) / 2;
        int y1 = (startY + endY) / 2;
        // 画8个实心小正方形, 用于调节选取大小
        g.fillRect(x1 - 2, startY - 2, 5, 5);
        g.fillRect(x1 - 2, endY - 2, 5, 5);
        g.fillRect(startX - 2, y1 - 2, 5, 5);
        g.fillRect(endX - 2, y1 - 2, 5, 5);
        g.fillRect(startX - 2, startY - 2, 5, 5);
        g.fillRect(startX - 2, endY - 2, 5, 5);
        g.fillRect(endX - 2, startY - 2, 5, 5);
        g.fillRect(endX - 2, endY - 2, 5, 5);
        // 记录8个小方块位置, 用于判断是否处于调节尺寸状态
        rec[0] = new Rectangle(x - 5, y - 5, 10, 10);
        rec[1] = new Rectangle(x1 - 5, y - 5, 10, 10);
        rec[2] = new Rectangle((startX > endX ? startX : endX) - 5, y - 5, 10,
                10);
        rec[3] = new Rectangle((startX > endX ? startX : endX) - 5, y1 - 5, 10,
                10);
        rec[4] = new Rectangle((startX > endX ? startX : endX) - 5,
                (startY > endY ? startY : endY) - 5, 10, 10);
        rec[5] = new Rectangle(x1 - 5, (startY > endY ? startY : endY) - 5, 10,
                10);
        rec[6] = new Rectangle(x - 5, (startY > endY ? startY : endY) - 5, 10,
                10);
        rec[7] = new Rectangle(x - 5, y1 - 5, 10, 10);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                0.3F));
        g2d.setColor(Color.CYAN);
        // 选取开始位置
        int sX = Math.min(startX, endX);
        int sY = Math.min(endY, startY);
        // 填充选区
        g2d.fillRect(sX, sY, Math.abs(endX - startX), Math.abs(endY - startY));
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1F));

        // 画尺寸提示
        boolean drawSizeTip = endX - startX != 0 && endY - startY != 0;
        if (drawSizeTip) {
            String cTip = String.format("%dX%d", Math.abs(endX - startX), Math.abs(endY - startY));
            int cTipH = 20;
            Font cTipFont = new Font("system", Font.BOLD, 16);
            g2d.setFont(cTipFont);
            int cTipW = SwingUtilities.computeStringWidth(
                    getFontMetrics(cTipFont), cTip);
            g2d.setPaint(Color.BLACK);
            int cStartY = sY - cTipH > 0 ? sY - cTipH : sY;
            g2d.fillRect(sX, cStartY, cTipW, cTipH);
            g2d.setPaint(Color.WHITE);
            g2d.drawString(cTip, sX, cStartY == sY ? sY + cTipH - 3 : sY - 3);
        }
        g2d.dispose();
        if (showTip) {
            // 填充矩形
            g.setColor(Color.CYAN);
            g.fillRect(p.x, p.y, 225, 20);
            // 画矩形框
            g.setColor(Color.BLUE);
            g.drawRect(p.x, p.y, 225, 20);
            // 画文字
            g.setColor(Color.BLACK);
            g.drawString(" 请按住鼠标左键不放选择截图区, 右键退出", p.x, p.y + 15);
        }
    }

    // 根据东南西北等八个方向决定选中的要修改的X和Y的座标
    private void initSelect(States state) {
        switch (state) {
            case DEFAULT:
                currentX = 0;
                currentY = 0;
                break;
            case EAST:
                currentX = (endX > startX ? END_X : START_X);
                currentY = 0;
                break;
            case WEST:
                currentX = (endX > startX ? START_X : END_X);
                currentY = 0;
                break;
            case NORTH:
                currentX = 0;
                currentY = (startY > endY ? END_Y : START_Y);
                break;
            case SOUTH:
                currentX = 0;
                currentY = (startY > endY ? START_Y : END_Y);
                break;
            case NORTH_EAST:
                currentY = (startY > endY ? END_Y : START_Y);
                currentX = (endX > startX ? END_X : START_X);
                break;
            case NORTH_WEST:
                currentY = (startY > endY ? END_Y : START_Y);
                currentX = (endX > startX ? START_X : END_X);
                break;
            case SOUTH_EAST:
                currentY = (startY > endY ? START_Y : END_Y);
                currentX = (endX > startX ? END_X : START_X);
                break;
            case SOUTH_WEST:
                currentY = (startY > endY ? START_Y : END_Y);
                currentX = (endX > startX ? START_X : END_X);
                break;
            default:
                currentX = 0;
                currentY = 0;
                break;
        }
    }

    public void mouseMoved(MouseEvent me) {
        doMouseMoved(me);
        initSelect(current);
        if (showTip) {
            p = me.getPoint();
            repaint();
        }
    }

    // 特意定义一个方法处理鼠标移动,是为了每次都能初始化一下所要选择的地区
    private void doMouseMoved(MouseEvent me) {
        // 如果处于选区内, 则改变光标形状
        if (select.contains(me.getPoint())) {
            this.setCursor(new Cursor(Cursor.MOVE_CURSOR));
            current = States.MOVE;
        } else {
            States[] st = States.values();
            for (int i = 0; i < rec.length; i++) {
                // 如果鼠标处于某个小框内, 则设为可调节状态(改变光标)
                if (rec[i].contains(me.getPoint())) {
                    current = st[i];
                    this.setCursor(st[i].getCursor());
                    return;
                }
            }
            this.setCursor(cs);
            current = States.DEFAULT;
        }
    }

    public void mouseExited(MouseEvent me) {
    }

    public void mouseEntered(MouseEvent me) {
    }

    public void mouseDragged(MouseEvent me) {
        int x = me.getX();
        int y = me.getY();
        if (current == States.MOVE) {
            startX += (x - tempX);
            startY += (y - tempY);
            endX += (x - tempX);
            endY += (y - tempY);
            tempX = x;
            tempY = y;
        } else if (current == States.EAST || current == States.WEST) {
            if (currentX == START_X) {
                startX += (x - tempX);
                tempX = x;
            } else {
                endX += (x - tempX);
                tempX = x;
            }
        } else if (current == States.NORTH || current == States.SOUTH) {
            if (currentY == START_Y) {
                startY += (y - tempY);
                tempY = y;
            } else {
                endY += (y - tempY);
                tempY = y;
            }
        } else if (current == States.NORTH_EAST || current == States.NORTH_WEST || current == States.SOUTH_EAST || current == States.SOUTH_WEST) {
            if (currentY == START_Y) {
                startY += (y - tempY);
                tempY = y;
            } else {
                endY += (y - tempY);
                tempY = y;
            }
            if (currentX == START_X) {
                startX += (x - tempX);
                tempX = x;
            } else {
                endX += (x - tempX);
                tempX = x;
            }

        } else {
            startX = tempX;
            startY = tempY;
            endX = me.getX();
            endY = me.getY();
        }
        UpdateEditPane();
        this.repaint();
    }

    public void mousePressed(MouseEvent me) {
        showTip = false;
        tempX = me.getX();
        tempY = me.getY();
    }

    public void mouseReleased(MouseEvent me) {
        // 意思似乎是: 如果是右击
        if (me.isPopupTrigger()) {
            log.debug("It is a popup trigger!");
            // 如果处于选择区, 则xxx
            if (current == States.MOVE) {
                // show menu here
                menuSnap.show(fakewindow, me.getPoint().x, me.getPoint().y);
            } else if ((select.getHeight() + select.getWidth()) != 0 && !select.contains(me.getPoint())) {
                // 如果选区存在, 并且右击选区外的地方
                HideEditPane();
                showTip = true;
                p = me.getPoint();
                startX = 0;
                startY = 0;
                endX = 0;
                endY = 0;
                repaint();
            } else {
                fakewindow.dispose();
            }
        } else if (select.height + select.width > 0) {
            UpdateEditPane();
        } else if (select.height + select.width <= 0) {
            showTip = true;
            p = me.getPoint();
            startX = 0;
            startY = 0;
            endX = 0;
            endY = 0;
            repaint();
        }
    }

    private void UpdateEditPane() {
        Point loc = new Point(select.x + select.width - editPane.getWidth(),
                select.y + select.height + 1);

        if (loc.x + editPane.getWidth() > bi.getWidth())//超出右边界
        {
            loc.x = bi.getWidth() - editPane.getWidth();
        } else if (loc.x < 0)//超出左边界
        {
            loc.x = 0;
        }
        if (loc.y + editPane.getHeight() > bi.getHeight())//超出下界
        {
            //在上边显示
            loc.y = select.y - editPane.getHeight() - 1;
            if (loc.y < 0)//如果上边也显示不了...
            {
                loc.y = 0;
            }
        }
        editPane.setLocation(loc);
        editPane.setVisible(true);
    }

    private void HideEditPane() {
        editPane.setVisible(false);
    }

    public void mouseClicked(MouseEvent me) {
        if (me.getClickCount() == 2) {
            // Rectangle rec=new
            // Rectangle(startX,startY,Math.abs(endX-startX),Math.abs(endY-startY));
            Point p = me.getPoint();
            if (select.contains(p)) {
                whatWeGet();
            }
        }
    }

    private void whatWeGet() {
        if (select.x + select.width < this.getWidth() && select.y + select.height < this.getHeight()) {
            get = bi.getSubimage(select.x, select.y, select.width,
                    select.height);
            fakewindow.dispose();
            // updates();
        } else {
            int wid = select.width, het = select.height;
            if (select.x + select.width >= this.getWidth()) {
                wid = this.getWidth() - select.x;
            }
            if (select.y + select.height >= this.getHeight()) {
                het = this.getHeight() - select.y;
            }
            /**
             * 处理选择区超出屏幕范围的"变态情况"
             *
             * @author shinysky
             */
            if (select.x < 0) {
                select.x = 0;
            }
            if (select.y < 0) {
                select.y = 0;
            }
            if (wid + select.x > bi.getWidth()) {
                wid = bi.getWidth() - select.x;
            }
            if (het + select.y > bi.getWidth()) {
                het = bi.getHeight() - select.y;
            }
            get = bi.getSubimage(select.x, select.y, wid, het);
            fakewindow.dispose();
        }
    }
    //保存BMP格式的过滤器

    private class BMPfilter extends javax.swing.filechooser.FileFilter {

        public BMPfilter() {
        }

        public boolean accept(File file) {
            if (file.toString().toLowerCase().endsWith(".bmp") ||
                    file.isDirectory()) {
                return true;
            } else {
                return false;
            }
        }

        public String getDescription() {
            return "*.BMP(BMP图像)";
        }
    }
    //保存JPG格式的过滤器

    private class JPGfilter extends javax.swing.filechooser.FileFilter {

        public JPGfilter() {
        }

        public boolean accept(File file) {
            if (file.toString().toLowerCase().endsWith(".jpg") ||
                    file.isDirectory()) {
                return true;
            } else {
                return false;
            }
        }

        public String getDescription() {
            return "*.JPG(JPG图像)";
        }
    }
    //保存GIF格式的过滤器

    private class GIFfilter extends javax.swing.filechooser.FileFilter {

        public GIFfilter() {
        }

        public boolean accept(File file) {
            if (file.toString().toLowerCase().endsWith(".gif") ||
                    file.isDirectory()) {
                return true;
            } else {
                return false;
            }
        }

        public String getDescription() {
            return "*.GIF(GIF图像)";
        }
    }

    //保存PNG格式的过滤器
    private class PNGfilter extends javax.swing.filechooser.FileFilter {

        public boolean accept(File file) {
            if (file.toString().toLowerCase().endsWith(".png") ||
                    file.isDirectory()) {
                return true;
            } else {
                return false;
            }
        }

        public String getDescription() {
            return "*.PNG(PNG图像)";
        }
    }
}
//一些表示状态的枚举
enum States {

    NORTH_WEST(new Cursor(Cursor.NW_RESIZE_CURSOR)), // 表示西北角
    NORTH(new Cursor(Cursor.N_RESIZE_CURSOR)),
    NORTH_EAST(new Cursor(Cursor.NE_RESIZE_CURSOR)),
    EAST(new Cursor(Cursor.E_RESIZE_CURSOR)),
    SOUTH_EAST(new Cursor(Cursor.SE_RESIZE_CURSOR)),
    SOUTH(new Cursor(Cursor.S_RESIZE_CURSOR)),
    SOUTH_WEST(new Cursor(Cursor.SW_RESIZE_CURSOR)),
    WEST(new Cursor(Cursor.W_RESIZE_CURSOR)),
    MOVE(new Cursor(Cursor.MOVE_CURSOR)),
    DEFAULT(new Cursor(Cursor.DEFAULT_CURSOR));
    private Cursor cs;

    States(Cursor cs) {
        this.cs = cs;
    }

    public Cursor getCursor() {
        return cs;
    }
}
