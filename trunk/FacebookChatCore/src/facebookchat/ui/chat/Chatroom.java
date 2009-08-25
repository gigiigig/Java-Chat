package facebookchat.ui.chat;

import java.awt.Dimension;

import javax.swing.BoxLayout;

import facebookchat.common.FacebookBuddyList;
import facebookchat.common.Launcher;
import facebookchat.common.SystemPath;
import facebookchat.ui.common.NoxFrame;

@SuppressWarnings("serial")
public class Chatroom extends NoxFrame{
	/**
	 * 默认尺寸常量
	 */
	public static final int WIDTH_DEFLT = 400;
	public static final int WIDTH_PREF = 400;
	public static final int WIDTH_MAX = 2000;
	public static final int WIDTH_MIN = 300;
	public static final int HEIGHT_DEFLT = 300;
	public static final int HEIGHT_PREF = 300;
	public static final int HEIGHT_MAX = 2000;
	public static final int HEIGHT_MIN = 200;

	public static final int PRIVATE_CHATROOM = 0;
	public static final int GROUP_CHATROOM = 1;
	
	protected ChatroomPane chatroompane;
	
	/**
	 * 私聊: 该值为对方ID;
	 * 群聊:为组ID
	 */
	protected String roomID;
	protected String roomname;
	
	public Chatroom(String uid){
		super((FacebookBuddyList.buddies.get(uid) == null)?uid:FacebookBuddyList.buddies.get(uid).name,
				SystemPath.IMAGES_RESOURCE_PATH
				+ "bkgrd.png", SystemPath.ICONS_RESOURCE_PATH
				+ "chat_green_20.png", SystemPath.ICONS_RESOURCE_PATH
				+ "chat_green_48.png",
				(FacebookBuddyList.buddies.get(uid) == null)?uid:FacebookBuddyList.buddies.get(uid).name,
				false);
		roomID = uid;
		roomname = (FacebookBuddyList.buddies.get(uid) == null)?uid:FacebookBuddyList.buddies.get(uid).name;
		this.setBounds(100, 80, WIDTH_DEFLT, HEIGHT_DEFLT);
		this.setSize(new Dimension(WIDTH_DEFLT, HEIGHT_DEFLT));
		this.setPreferredSize(new Dimension(WIDTH_PREF, HEIGHT_PREF));
		this.setMaximumSize(new Dimension(WIDTH_MAX, HEIGHT_MAX));
		this.setMinimumSize(new Dimension(WIDTH_MIN, HEIGHT_MIN));

		chatroompane = new ChatroomPane(this);

		this.getContainer().setLayout(new BoxLayout(this.getContainer(), BoxLayout.X_AXIS));
		this.getContainer().add(chatroompane);
	}
/*	private void setContainer(ChatroomPane chatroompane2) {
		super.container = chatroompane;
	}*/
	/**
	 * 获取聊天室名称(对方昵称或组名)
	 * @return 对方昵称或组名
	 */
	public String getRoomName(){
		return roomname;
	}
	public String getRoomID(){
		return roomID;
	}
	public void setRoomName(String name){
		roomname = name;
		this.setTitle(name);
		this.setTitleStr(name);
	}
	/**
	 * 向外发送文本消息
	 * 
	 * @param strmsg
	 *            string msg
	 * @return succeed or not
	 */
	public boolean SendMsg(String strmsg){
		//TODO send msg
		Launcher.PostMessage(roomID, strmsg);
		return true;
	}
	public void incomingMsgProcessor(String sender, String time,
			Object msgdata){
		chatroompane.incomingMsgProcessor(sender, time, msgdata);
	}
	public void showFeedbackMsg(String msg, String errorString){
		chatroompane.showFeedbackMsg(msg, errorString);
	}
}