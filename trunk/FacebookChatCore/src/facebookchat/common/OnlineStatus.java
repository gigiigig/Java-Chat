package facebookchat.common;

import javax.swing.ImageIcon;

public enum OnlineStatus {
	ONLINE(),//在线
	OFFLINE(),//离线
	INVISIBLE(),//隐身
	UNAVAILABLE(),//离开
	BUSY(),//忙碌
	UNKNOWN;//未知
	
	public static final String OnlineStr = "Online";
	public static final String OfflineStr = "Offline";
	public static final String InvisibleStr = "Invisible";
	public static final String UnavailableStr = "Unavailable";
	public static final String BusyStr = "Busy";
	public static final String UnknownStr = "Unknown";
	
	public static ImageIcon OnlineIndicator = new ImageIcon(SystemPath.STATUS_RESOURCE_PATH + "Online.png");
	public static ImageIcon OfflineIndicator = new ImageIcon(SystemPath.STATUS_RESOURCE_PATH + "Offline.png");
}