package facebookchat.ui.main;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.MenuElement;
import javax.swing.ScrollPaneConstants;

import facebookchat.common.FacebookUser;
import facebookchat.common.FacebookManager;
import facebookchat.ui.common.ObjectList;

@SuppressWarnings("serial")
public class ListsPane extends JPanel {
	private JScrollPane buddyListScrPane;

	ObjectList buddyList;
	Cheyenne parent;
	
	FacebookUser buddy;
	
	public ListsPane(Cheyenne par) {
		parent = par;
		
		buddyList = new ObjectList();
		
		// add to gui
		buddyListScrPane = new JScrollPane(buddyList,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		this.add(buddyList.getFilterField());
		this.add(buddyListScrPane);
		
		buddyList.addMouseListener(new MouseListener(){
			@SuppressWarnings("serial")
			public void mouseClicked(MouseEvent me) {
				buddy = (FacebookUser)buddyList.getSelectedValue();
				if(buddy == null)
					return;
				if(me.getClickCount() == 2){
					//TODO 判断所点击的cell的在线状态进行对应处理, 暂时直接弹出弹出聊天窗口.
					/**
					 * TODO 应该对每一个对象只开一个窗口, 可以设定标记, 如果已经打开了一个则显示之, 否则开新窗口
					 */
					ListsPane.this.showChatroom(buddy.uid);
				}else if(me.getButton() == MouseEvent.BUTTON3){
					final JPopupMenu friendOprMenu = new JPopupMenu();
					//log.debug("You just Right Click the List Item!");
					friendOprMenu.add(new AbstractAction("Talk to him/her") {
						public void actionPerformed(ActionEvent e) {
							ListsPane.this.showChatroom(buddy.uid);
						}
					});
					friendOprMenu.add(new AbstractAction("His/Her information") {
						public void actionPerformed(ActionEvent e) {
							JOptionPane.showMessageDialog((Component) null, 
									"<html>"//<BODY bgColor=#ffffff>"
									+ "<img width=64 height=64 src=\""
									+ buddy.thumbSrc
									+ "\"><br>"
									+"<Font color=black>Name:</Font> <Font color=blue>"
									+ buddy.name
									+ "<br></Font>"
									+ "<Font color=black>UID:</Font> <Font color=blue>"
									+ buddy.uid
									+ "<br></Font>"
									+ "<Font color=black>Status:</Font> <Font color=blue>"
									+ buddy.onlineStatus.toString()
									+ "<br></Font>"
									+ "<a href=\"http://www.facebook.com/profile.php?id=" 
									+ buddy.uid
									+ "\"   >"
									+ "http://www.facebook.com/profile.php?id="
									+ buddy.uid
									+ "</a>"
									+ "</BODY></html>",
									"User Information", JOptionPane.INFORMATION_MESSAGE);
						}//http://www.facebook.com/profile.php?id=1190346972
					});
					MenuElement els[] = friendOprMenu.getSubElements();
					for(int i = 0; i < els.length; i++)
						els[i].getComponent().setBackground(Color.WHITE);
					friendOprMenu.setLightWeightPopupEnabled(true);
					friendOprMenu.pack();
					// 位置应该是相对于源的位置
					friendOprMenu.show((Component) me.getSource(), me.getPoint().x, me.getPoint().y);
				}
			}
			public void mouseEntered(MouseEvent arg0) {
			}
			public void mouseExited(MouseEvent arg0) {
			}
			public void mousePressed(MouseEvent arg0) {
			}
			public void mouseReleased(MouseEvent arg0) {
			}
		});
		
		this.setBackground(Color.BLACK);
		this.setForeground(Color.WHITE);

		this.setOpaque(false);
	}
	/**
	 * 返回TooltipTxt的html形式
	 * @param text
	 * @return
	 */
	public static String getHtmlText(String text) {
		return ("<html><BODY bgColor=#ffffff><Font color=black>" + text + "</Font></BODY></html>");
	}
	/**
	 * (在主界面双击好友或者组时被调用)弹出聊天窗口.
	 * @param listItem
	 */
	private void showChatroom(String uid) {
		FacebookManager.getChatroomAnyway(uid).setVisible(true);
	}
	public void refresh() {
		buddyList.update();
	}
}