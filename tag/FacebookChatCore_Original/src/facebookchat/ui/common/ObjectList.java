package facebookchat.ui.common;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import facebookchat.common.FacebookBuddyList;
import facebookchat.common.FacebookUser;
import facebookchat.common.OnlineStatus;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * buddy list, actually<br>
 * it has a input field(JTextField) and a filter, which is useful when we try to search some buddy<br>
 * @author shinysky
 *
 */
public class ObjectList extends JList {
    private static Log log = LogFactory.getLog(ObjectList.class);

	private static final long serialVersionUID = 1L;

	private FilterModel fmod;
	private FilterField filterField;
	private int DEFAULT_FIELD_WIDTH = 20;

	/**
	 * 如果变量为render类内部变量, 则会出现列表元素被添加到同一行, 并且该行重复N次的情况
	 */
	JLabel portrait;
	JLabel nick;
	JLabel otherInfo;
	
	Dimension portraitsize = new Dimension(40, 40);

	/**
	 * 具有自定义列表元素和过滤功能的列表
	 * 
	 * @param objs
	 *            列表元素(FriendItem类型)数组
	 */
	public ObjectList() {
		// super(objs);
		fmod = new FilterModel();
		this.setModel(fmod);
		
		portrait = new JLabel();
		nick = new JLabel();
		otherInfo = new JLabel();
		this.setCellRenderer(new NoxJListCellRender());
		filterField = new FilterField(DEFAULT_FIELD_WIDTH);
		filterField.setSize(new Dimension(100, 20));
		filterField.setPreferredSize(new Dimension(100, 20));
		filterField.setMaximumSize(new Dimension(10000, 20));
		filterField.setMinimumSize(new Dimension(20, 20));

		update();
		
		this.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent me) {
				if (me.getButton() == MouseEvent.BUTTON3) {
					/*
					 * 实现右键可选取JListItem
					 */
					int index = ObjectList.this.locationToIndex(me.getPoint());
					ObjectList.this.setSelectedIndex(index);
				}
			}
			public void mouseEntered(MouseEvent menter) {
				// TODO 自动改变背景色
				// int index =
				// ObjectList.this.locationToIndex(menter.getPoint());
				// ObjectList.this.set
			}
			public void mouseExited(MouseEvent arg0) {
			}
			public void mousePressed(MouseEvent arg0) {
			}
			public void mouseReleased(MouseEvent arg0) {
			}
		});
	}

	public void setModel(ListModel m) {
		if (!(m instanceof FilterModel))
			throw new IllegalArgumentException();
		super.setModel(m);
	}
	/**
	 * 系统初始化时调用, 用于将数据库中数据添加到列表<br>
	 * if item already exists, replace it with the new one.<br>
	 *  
	 * @param o
	 * @return
	 */
	private Object addItem(Object o){
		int size =  fmod.getRealSize();
		FacebookUser newItem = (FacebookUser)o;
		for(int index = 0; index <  size; index++){
			//if it already exist, update
			FacebookUser curItem = (FacebookUser)(fmod.getRealElementAt(index));
			log.debug("curItem: " + curItem.onlineStatus.toString());
			log.debug("newItem: " + newItem.onlineStatus.toString());
			if(newItem.uid.equals(curItem.uid)){
				//fmod.deleteElementAt(index);
				fmod.replaceElement(index, newItem);
				//curItem = newItem;
				log.debug("new curItem: " + ((FacebookUser)fmod.getRealElementAt(index)).onlineStatus.toString());
				return o;
			}
		}
		fmod.addElement((FacebookUser) o);		
		return o;
	}


	public void update() {
		//fmod.removeAll();
		
		Iterator<String> it = FacebookBuddyList.buddies.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			log.debug("userID: " + key);
			FacebookUser fu = FacebookBuddyList.buddies.get(key);
			log.debug("status: " + fu.onlineStatus.toString());
			addItem(fu);
		}
		this.repaint();
		this.revalidate();
	}
	
	public class NoxJListCellRender extends JPanel implements ListCellRenderer {
		/**
		 * JList单元格渲染器
		 */
		private static final long serialVersionUID = 1L;

		public Component getListCellRendererComponent(final JList list,
				Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			FacebookUser item = (FacebookUser) value;

			portrait.setIcon((Icon) item.portrait);
			
			portrait.setSize(portraitsize);
			portrait.setPreferredSize(portraitsize);
			portrait.setMaximumSize(portraitsize);
			portrait.setMinimumSize(portraitsize);
			
			nick.setText(item.name
					+ '('
					+ item.uid
					+ ')');
			if(item.onlineStatus.equals(OnlineStatus.ONLINE))
				nick.setIcon(OnlineStatus.OnlineIndicator);
			else if(item.onlineStatus.equals(OnlineStatus.OFFLINE))
				nick.setIcon(OnlineStatus.OfflineIndicator);
			
			/*long between=(new Date().getTime()-item.lastSeen)/1000;
	        long day=between/(24*3600);
	        long hour=between%(24*3600)/3600;
	        long minute=between%3600/60;
	        long second=between%60/60;
	        String lastSeen = "";
	        if(day != 0)
	        	lastSeen += (day + " days ");
	        if(hour != 0)
	        	lastSeen += (hour + " hours ");
	        if(minute != 0)
	        	lastSeen += (minute + " minutes ");
	        if(second != 0)
	        	lastSeen += (second + " seconds ");*/
	        
	        if(item.onlineStatus.equals(OnlineStatus.OFFLINE))
	        	otherInfo.setText("<html><Font color=gray>"
						+ " Last seen: "
						+ new Date(item.lastSeen).toString()
						+ "</Font></html>");
	        else
	        	otherInfo.setText(item.status + "(" + item.statusTimeRel + ")");
			

			/*
			 * portrait.setOpaque(false); nick.setOpaque(false);
			 * sign.setOpaque(false); setOpaque(true);
			 */
			Font defaultFont = otherInfo.getFont();
			Font nameFont = defaultFont.deriveFont(Font.BOLD, defaultFont
					.getSize() + 1);
			nick.setFont(nameFont);

			NoxJListCellRender.this.setLayout(new GridBagLayout());
			addWithGridBag(portrait, NoxJListCellRender.this, 0, 0, 1, 2,
					GridBagConstraints.WEST, GridBagConstraints.BOTH, 0, 0);
			addWithGridBag(nick, NoxJListCellRender.this, 1, 0, 1, 1,
					GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, 1,
					0);
			addWithGridBag(otherInfo, NoxJListCellRender.this, 1, 1, 1, 1,
					GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL, 1,
					0);

			setBackground(isSelected ? list.getSelectionBackground() : list
					.getBackground());
			setForeground(isSelected ? list.getSelectionForeground() : list
					.getForeground());

			this
			.setToolTipText("<html><BODY bgColor=#ffffff>"
					+ "<img width=64 height=64 src=\""
					+ item.thumbSrc
					+ "\"><br>"
					+ "<Font color=black>Name:</Font> <Font color=blue>"
					+ item.name
					+ "<br></Font>"
					+ "<Font color=black>UID:</Font> <Font color=blue>"
					+ item.uid
					+ "<br></Font></BODY></html>");
			
			return this;
		}

		private void addWithGridBag(Component comp, Container cont, int x,
				int y, int width, int height, int anchor, int fill,
				int weightx, int weighty) {
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = x;
			gbc.gridy = y;
			gbc.gridwidth = width;
			gbc.gridheight = height;
			gbc.anchor = anchor;
			gbc.fill = fill;
			gbc.weightx = weightx;
			gbc.weighty = weighty;
			cont.add(comp, gbc);
		}
	}

	public JTextField getFilterField() {
		return filterField;
	}

	// inner class to provide filtered model
	@SuppressWarnings("serial")
	public class FilterModel extends DefaultListModel {
		ArrayList<FacebookUser> items;
		ArrayList<FacebookUser> filterItems;
		/**
		 * the corresponding relationship between the fake index and the real index
		 */
		Vector<Integer> indexes;

		public FilterModel() {
			super();
			items = new ArrayList<FacebookUser>();
			filterItems = new ArrayList<FacebookUser>();
			indexes = new Vector<Integer>();
		}

		public void removeAll() {
			items.clear();
		}

		public void replaceElement(int index, FacebookUser newItem) {
			//items.set(index, newItem);
			items.get(index).copy(newItem);
		}

		public Object getElementAt(int index) {
			if (index < filterItems.size())
				return filterItems.get(index);
			else
				return null;
		}
		/**
		 * get the real element which has a fake index "index".
		 * 
		 * @param index
		 * @return
		 */
		public Object getRealElementAt(int index) {
			if (index < filterItems.size() && indexes.elementAt(index) < items.size())
				return items.get(indexes.elementAt(index));
			else
				return null;
		}

		public int getSize() {
			return filterItems.size();
		}
		
		public int getRealSize() {
			return items.size();
		}

		public void addElement(Object o) {
			items.add((FacebookUser) o);
			// log.debug("addElement...");
			refilter();
		}
		
		public Object deleteElementAt(int index){
			//真正删除之
			FacebookUser ob = items.remove((int)indexes.elementAt(index));
			refilter();
			
			return ob;
		}

		/**
		 * maintaining the corresponding relationship between the fake index and the real index
		 */
		private void refilter() {
			// log.debug("refiltering...");
			filterItems.clear();
			indexes.clear();
			int index = 0;
			String term = getFilterField().getText();
			for (int i = 0; i < items.size(); i++)
				if (items.get(i).name.toLowerCase().indexOf(term.toLowerCase(), 0) != -1) {
					// log.debug(items.get(i).getNick());
					filterItems.add(items.get(i));
					index++;
					indexes.add(i);
				}
			fireContentsChanged(this, 0, getSize());
		}
	}

	// inner class provides filter-by-keystroke field
	public class FilterField extends JTextField implements DocumentListener {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public FilterField(int width) {
			super(width);
			this.setToolTipText(getHtmlText("输入关键字以搜索列表"));
			getDocument().addDocumentListener(this);
		}

		public String getHtmlText(String text) {
			return ("<html><BODY bgColor=#ffffff><Font color=black>"
					+ text + "</Font></BODY></html>");
		}

		public void changedUpdate(DocumentEvent e) {
			((FilterModel) getModel()).refilter();
		}

		public void insertUpdate(DocumentEvent e) {
			((FilterModel) getModel()).refilter();
		}

		public void removeUpdate(DocumentEvent e) {
			((FilterModel) getModel()).refilter();
		}
	}
}