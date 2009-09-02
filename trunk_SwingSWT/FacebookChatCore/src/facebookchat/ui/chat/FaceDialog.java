package facebookchat.ui.chat;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import facebookchat.common.SystemPath;
import facebookchat.ui.common.JImgPanel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 显示表情图片窗口
 * 
 * @author shinysky
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
class FaceDialog extends JDialog implements ActionListener, MouseListener {

    private static Log log = LogFactory.getLog(FaceDialog.class);
    /**
     *
     */
    private static final long serialVersionUID = 1839504987273483189L;
    public static final int FACEROWS = 8;
    public static final int FACECOLUMNS = 15;
    public static final int FACECELLWIDTH = 30;
    public static final int FACECELLHEIGHT = 30;
    public static final int CACELBUTTONHEIGHT = 25;

    /**
     * 表情对话框构造函数
     *
     * @param title
     *            对话框标题
     * @param modal
     *            是否禁止其它焦点
     */
    public FaceDialog(String title, boolean modal, String path_faces) {
        super((JFrame) null, title, modal);

        this.setUndecorated(true);
        this.setSize(new Dimension(FACECELLWIDTH * FACECOLUMNS, FACECELLHEIGHT * FACEROWS + CACELBUTTONHEIGHT));
        this.setPreferredSize(new Dimension(FACECELLWIDTH * FACECOLUMNS,
                FACECELLHEIGHT * FACEROWS + CACELBUTTONHEIGHT));
        this.setMaximumSize(new Dimension(FACECELLWIDTH * FACECOLUMNS,
                FACECELLHEIGHT * FACEROWS + CACELBUTTONHEIGHT));
        this.setMinimumSize(new Dimension(FACECELLWIDTH * FACECOLUMNS,
                FACECELLHEIGHT * FACEROWS + CACELBUTTONHEIGHT));

        Container contentPane = getContentPane();

        String[] columns = new String[FACECOLUMNS];
        for (int i = 0; i < FACECOLUMNS; i++) {
            columns[i] = "";
        }

        Object[][] data = new Object[FACEROWS][FACECOLUMNS];
        /**
         * 不需要将表情图片实际插入到表格中
         */
        tb_cr_faces = new JTable(new MyModel(data, columns)) {

            /**
             * 自定义JTable Cell Renderer
             */
            private static final long serialVersionUID = -212708255423640437L;

            public Component prepareRenderer(TableCellRenderer renderer,
                    int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                // We want renderer component to be transparent so background
                // image is visible
                // 设置单元格表现形式为透明
                if (c instanceof JComponent) {
                    ((JComponent) c).setOpaque(false);
                }
                return c;
            }
        };
        // 设置表格为透明
        tb_cr_faces.setOpaque(false);

        tb_cr_faces.setRowHeight(FACECELLHEIGHT);
        tb_cr_faces.setSize(new Dimension(FACECELLWIDTH * FACECOLUMNS,
                FACECELLHEIGHT * FACEROWS));
        tb_cr_faces.setPreferredSize(new Dimension(FACECELLWIDTH * FACECOLUMNS,
                FACECELLHEIGHT * FACEROWS));
        tb_cr_faces.setMaximumSize(new Dimension(FACECELLWIDTH * FACECOLUMNS,
                FACECELLHEIGHT * FACEROWS));
        tb_cr_faces.setMinimumSize(new Dimension(FACECELLWIDTH * FACECOLUMNS,
                FACECELLHEIGHT * FACEROWS));
        /**
         * 此二行可保证可选且只可选一个
         */
        tb_cr_faces.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tb_cr_faces.setCellSelectionEnabled(true);

        /**
         * 添加鼠标事件监听器,以实现单击插入
         */
        tb_cr_faces.addMouseListener(this);
        // tb_cr_faces.setSelectionBackground(Color.LIGHT_GRAY);

        b_cr_insert = new JButton("Insert");
        b_cr_insert.setMnemonic('I');
        b_cr_insert.setActionCommand("Insert");
        b_cr_insert.addActionListener(FaceDialog.this);

        b_cr_cancel = new JButton("Cancel");
        b_cr_cancel.setMnemonic('C');
        b_cr_cancel.setActionCommand("Cancel");
        b_cr_cancel.addActionListener(FaceDialog.this);
        b_cr_cancel.setSize(new Dimension(80, 25));
        b_cr_cancel.setPreferredSize(new Dimension(80, CACELBUTTONHEIGHT));
        b_cr_cancel.setMaximumSize(new Dimension(80, CACELBUTTONHEIGHT));
        b_cr_cancel.setMinimumSize(new Dimension(80, CACELBUTTONHEIGHT));
        b_cr_cancel.setContentAreaFilled(false);

        p_buttons = new JPanel();
        p_buttons.setLayout(new BoxLayout(p_buttons, BoxLayout.X_AXIS));
        p_buttons.add(Box.createHorizontalGlue());
        p_buttons.add(b_cr_cancel);
        p_buttons.setOpaque(false);

        rootpane = new JImgPanel(tk.getImage(SystemPath.IMAGES_RESOURCE_PATH + "faces.PNG"));

        rootpane.setLayout(new BoxLayout(rootpane, BoxLayout.Y_AXIS));
        rootpane.add(tb_cr_faces);
        rootpane.add(p_buttons);

        contentPane.add(rootpane);
    }

    /**
     * 获取所选表情索引,如果未选则返回-1
     *
     * @return 所选表情索引
     */
    public int getSelectedFaceIndex() {
        return selectedFaceIndex;
    }

    public ImageIcon getSelectedFace() {
        return selectedFace;
    }
    /**
     * 以下为 窗口组件
     */
    private JImgPanel rootpane;
    /**
     * 表情JTable
     */
    private JTable tb_cr_faces;
    /**
     * 按钮面板
     */
    private JPanel p_buttons;
    /**
     * 插入按钮
     */
    private JButton b_cr_insert;
    /**
     * 取消按钮
     */
    private JButton b_cr_cancel;
    /**
     * 其它变量
     */
    /**
     * 用于显示图片
     */
    Toolkit tk = Toolkit.getDefaultToolkit();
    /**
     * 所选表情索引
     */
    private int selectedFaceIndex = -1;
    private ImageIcon selectedFace = null;

    // 如果不赋初值,在用户未选择图标时,会默认为0,从而外部函数会认为选择了第一个:"0.gif"
    /**
     * MyModel 表格格式模型
     *
     * @author shinysky
     *
     * TODO To change the template for this generated type comment go to Window -
     * Preferences - Java - Code Style - Code Templates
     */
    class MyModel extends DefaultTableModel {

        /**
         *
         */
        private static final long serialVersionUID = 5422161155449098626L;

        /**
         * constructor 构造函数
         *
         * @param data
         *            数据
         * @param columns
         *            列标题
         */
        public MyModel(Object[][] data, Object[] columns) {
            super(data, columns);
        }

        /**
         * 指定单元格是否可编辑
         *
         * @param row
         *            单元格行索引
         * @param col
         *            单元格列索引
         * @return 表明是否可编辑的布尔值 (false)
         */
        public boolean isCellEditable(int row, int col) {
            return false;
        }
        /**
         * 获得列类
         */
        /*
         * public Class getColumnClass(int column) { Vector v = (Vector)
         * dataVector.elementAt(0); return v.elementAt(column).getClass(); }
         */
    }

    /**
     * 表格单元格渲染器 怎么用??
     */
    class TransparentRenderer extends DefaultTableCellRenderer {

        /**
         *
         */
        private static final long serialVersionUID = -2391045500764600550L;

        TransparentRenderer() {
            this.setOpaque(false);
        }

        public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus, int row,
                int col) {
            Component returnMe = super.getTableCellRendererComponent(table,
                    value, isSelected, hasFocus, row, col);

            // background only version

            // ((JComponent) value).setOpaque(false);
            // ((JComponent) returnMe).setOpaque(false);
            // log.debug(returnMe.getClass().toString());

            return returnMe;
        }
    }

    /**
     * 按钮事件响应
     *
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        // if(! tb_cr_faces.isSelectionEmpty() )
        JButton srcButton = (JButton) e.getSource();
        if (srcButton.getActionCommand().equals("Insert")) {
            if (tb_cr_faces.getSelectedRow() != -1) {
                int selected_r = tb_cr_faces.getSelectedRow();
                int selected_c = tb_cr_faces.getSelectedColumn();
                selectedFaceIndex = selected_r * FACECOLUMNS + selected_c;
                selectedFace = (ImageIcon) (tb_cr_faces.getValueAt(selected_r,
                        selected_c));
                log.debug("selectedFace : " + selectedFaceIndex);
            } else {
                selectedFaceIndex = -1;
                selectedFace = null;
                log.debug("Please select a face !");
            }
        } else {
            selectedFaceIndex = -1;
            selectedFace = null;
            log.debug("Didn't select a face !");
        }

        this.setVisible(false);// 隐藏,而不是销毁,考虑到可能会多次打开此对话框的问题
    }

    /**
     * 鼠标事件响应
     *
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent e) {
        int clickcount = e.getClickCount();
        /**
         * 处理单击事件,等同于单击b_cr_insert按钮
         */
        if (clickcount == 1) {
            b_cr_insert.doClick();
            log.debug("You just click the face.");
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent e) {
    }

    /*
     * (non-Javadoc)
     *
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent e) {
    }

    /*
     * (non-Javadoc)
     *
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    public void mouseEntered(MouseEvent e) {
    }

    /*
     * (non-Javadoc)
     *
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        // this.setVisible(false);
    }
}
