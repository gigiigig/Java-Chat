package facebookchat.ui.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import facebookchat.common.Launcher;
import facebookchat.common.SystemPath;

public class MiniProfilePane extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6265273413794252382L;

	// JMapPanel myPortraitPane;
	JButton myPortrait;
	JPanel miniProfilePane;
	JPanel nickAndStat;
	JLabel myNick;
	JTextField mySignField;
	public static Dimension portriatSize = new Dimension(50, 50);
	ImageIcon lastPortrait = null;
	
	String lastSignStr = "";

	/**
	 * mini profile 组件
	 * 
	 * @param path_portrait
	 *            头像图片路径
	 * @param nickname
	 *            昵称
	 * @param sign
	 *            签名档
	 */
	public MiniProfilePane(final Cheyenne parent, ImageIcon portrait, String nickname, String sign) {
		if(portrait == null)
			lastPortrait = new ImageIcon(SystemPath.PORTRAIT_RESOURCE_PATH + "portrait.png");
		else
			lastPortrait = portrait;
		if(nickname == null)
			nickname = "(NULL)";
		if(sign == null)
			sign = "";
		myPortrait = new JButton(lastPortrait);
		myPortrait.setToolTipText(getHtmlText("This is Me"));
		myPortrait.setSize(portriatSize);
		myPortrait.setPreferredSize(portriatSize);
		myPortrait.setMaximumSize(portriatSize);
		myPortrait.setMinimumSize(portriatSize);
		// myPortrait.setBorderPainted(true);

		/*
		 * myPortrait.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED,
		 * Color.RED, Color.BLUE));
		 */
		myPortrait.setContentAreaFilled(false);
		myPortrait.setOpaque(false);

		myPortrait.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				;
			}
		});
		
		/*
		 * JButton statSign = new JButton(new
		 * ImageIcon("resrc\\portrait\\busy.png")); statSign.setSize(new
		 * Dimension(50, 50)); statSign.setPreferredSize(new Dimension(50, 50));
		 * statSign.setMaximumSize(new Dimension(50, 50));
		 * statSign.setMinimumSize(new Dimension(50, 50));
		 * //statSign.setBorderPainted(false);
		 * //statSign.setContentAreaFilled(false); //statSign.setOpaque(false);
		 * 
		 * myPortrait.setLayout(new BoxLayout(myPortrait, BoxLayout.X_AXIS));
		 * myPortrait.add(statSign);
		 */

		/*
		 * myPortraitPane = new JMapPanel(myportrImg, new Point (5, 10), new
		 * Dimension(40, 40)); myPortraitPane.setSize(new Dimension(50, 50));
		 * myPortraitPane.setPreferredSize(new Dimension(50, 50));
		 * myPortraitPane.setMaximumSize(new Dimension(50, 50));
		 * myPortraitPane.setMinimumSize(new Dimension(50, 50));
		 * myPortraitPane.add(myPortrait);
		 */

		miniProfilePane = new JPanel();
		nickAndStat = new JPanel();
		myNick = new JLabel(nickname);
		myNick.setToolTipText(getHtmlText("My nickname"));

		// myStatus.setOpaque(false);
		mySignField = new JTextField(sign);
		
		lastSignStr = sign;

		nickAndStat.setOpaque(false);
		// nickAndStat.setBackground(new Color(0, 255, 0));
		nickAndStat.setSize(new Dimension(150, 20));
		nickAndStat.setPreferredSize(new Dimension(150, 20));
		nickAndStat.setMaximumSize(new Dimension(1000, 20));
		nickAndStat.setMinimumSize(new Dimension(150, 20));
		// nickAndStat.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		nickAndStat.setLayout(new BoxLayout(nickAndStat, BoxLayout.X_AXIS));
		nickAndStat.add(myNick);
		nickAndStat.add(Box.createHorizontalStrut(10));

		mySignField.setOpaque(false);
		mySignField.setToolTipText(getHtmlText("My staus message"));
		mySignField.setSize(new Dimension(Cheyenne.WIDTH_DEFLT, 20));
		mySignField.setPreferredSize(new Dimension(Cheyenne.WIDTH_PREF, 20));
		mySignField.setMaximumSize(new Dimension(Cheyenne.WIDTH_MAX, 20));
		mySignField.setMinimumSize(new Dimension(Cheyenne.WIDTH_MIN, 20));
		//mySign.setEnabled(false);
		mySignField.setEditable(false);
		mySignField.addFocusListener(new FocusListener(){
			public void focusGained(FocusEvent arg0) {
			    mySignField.setText(lastSignStr);
				mySignField.setEditable(true);
				mySignField.setOpaque(true);
				if(mySignField.getForeground().equals(Color.WHITE))
						mySignField.setForeground(Color.BLACK);
			}
			public void focusLost(FocusEvent arg0) {
				mySignField.setEditable(false);
				mySignField.setOpaque(false);
				mySignField.setForeground(myNick.getForeground());
				if(!mySignField.getText().trim().equals(lastSignStr.trim()))
				    Launcher.setStatusMessage(mySignField.getText());
			}
		});
		
		// miniProfilePane.setAlignmentX(JComponent.TOP_ALIGNMENT);
		miniProfilePane.setLayout(new BoxLayout(miniProfilePane,
				BoxLayout.Y_AXIS));
		miniProfilePane.add(nickAndStat);
		miniProfilePane.add(mySignField);
		miniProfilePane.setOpaque(false);

		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.add(myPortrait);
		this.add(Box.createHorizontalStrut(5));
		this.add(miniProfilePane);
		this.setOpaque(false);
	}

	public void setPortrait(Icon icon){
		myPortrait.setIcon(icon);
	}
	
	/**
	 * 设置昵称
	 * @param name
	 */
	public void setNickName(String name){
		myNick.setText(name);
	}
	
	public void setSign(String newSign, String timeRel){
	    if(newSign == null || newSign.trim().equals(""))
	        mySignField.setText("");
	    else
	        mySignField.setText(newSign + "(" + timeRel + " )");
		lastSignStr = newSign;
	}

	public void setForegroundColor(Color color){
		myNick.setForeground(color);
		mySignField.setForeground(color);
	}
	/**
	 * 返回TooltipTxt的html形式
	 * @param text
	 * @return
	 */
	public static String getHtmlText(String text) {
		return ("<html><BODY bgColor=#ffffff><Font color=black>" + text + "</Font></BODY></html>");
	}
}