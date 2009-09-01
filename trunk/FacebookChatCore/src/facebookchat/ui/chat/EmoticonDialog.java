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
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author shinysky
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
class EmoticonDialog extends JDialog implements ActionListener, MouseListener {

    private static Log log = LogFactory.getLog(EmoticonDialog.class);
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
     * @param title
     * @param modal
     */
    public EmoticonDialog(String title, boolean modal, String path_faces) {
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

            private static final long serialVersionUID = -212708255423640437L;

            public Component prepareRenderer(TableCellRenderer renderer,
                    int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                // We want renderer component to be transparent so background
                // image is visible
                if (c instanceof JComponent) {
                    ((JComponent) c).setOpaque(false);
                }
                return c;
            }
        };
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
        tb_cr_faces.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tb_cr_faces.setCellSelectionEnabled(true);

        tb_cr_faces.addMouseListener(this);
        // tb_cr_faces.setSelectionBackground(Color.LIGHT_GRAY);

        b_cr_insert = new JButton("Insert");
        b_cr_insert.setMnemonic('I');
        b_cr_insert.setActionCommand("Insert");
        b_cr_insert.addActionListener(EmoticonDialog.this);

        b_cr_cancel = new JButton("Cancel");
        b_cr_cancel.setMnemonic('C');
        b_cr_cancel.setActionCommand("Cancel");
        b_cr_cancel.addActionListener(EmoticonDialog.this);
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
     *
     * @return
     */
    public int getSelectedFaceIndex() {
        return selectedFaceIndex;
    }

    public ImageIcon getSelectedFace() {
        return selectedFace;
    }
    private JImgPanel rootpane;
    private JTable tb_cr_faces;
    private JPanel p_buttons;
    private JButton b_cr_insert;
    private JButton b_cr_cancel;
    Toolkit tk = Toolkit.getDefaultToolkit();
    private int selectedFaceIndex = -1;
    private ImageIcon selectedFace = null;

    /**
     * MyModel
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
         * constructor
         * @param data
         * @param columns
         */
        public MyModel(Object[][] data, Object[] columns) {
            super(data, columns);
        }

        /**

         *
         * @param row

         * @param col

         * @return
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
