package gg.msn.ui.chatwindow;

/*
 * JFontChooser.java
 *
 * The JFontChooser class is in the Public Domain, the code may be used
 * for any purpose.  It is provided as is with no warranty.
 *
 * Author:			James Bardsley (torasin@torasin.com)
 * Last Modfied:	29th September 2005
 */
 
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

public class JFontChooser extends JDialog implements ActionListener, ListSelectionListener
{
	public static final long serialVersionUID = 62256323L;
	
	private static String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
	private static String[] style = {"Regular", "Bold", "Italic", "Bold Italic"};
	private static String[] size = {"8", "9", "10", "11", "12", "14", "16", "18", "20", "22", "24", "26", "28", "36", "48", "72" };
	private Font font;
	private int option;
	private String textType;
	private int textStyle;
	private int textSize;
	
	private JList fList = new JList(fonts);
	private JList stList = new JList(style);
	private JList sizeList = new JList(size);
	private JTextField jtfFonts = new JTextField();
	private JTextField jtfStyle = new JTextField();
	private JTextField jtfSize = new JTextField();
	private JLabel jlbFonts = new JLabel("Font:");
	private JLabel jlbStyle = new JLabel("Style:");
	private JLabel jlbSize = new JLabel("Size:");
	private JScrollPane jspFont = new JScrollPane(fList);
	private JScrollPane jspStyle = new JScrollPane(stList);
	private JScrollPane jspSize = new JScrollPane(sizeList);
	private JButton jbtOK = new JButton("OK");
	private JButton jbtCancel = new JButton("Cancel");
	private JTextField jtfTest = new JTextField("AaBbYyZz");
	
	public static final int OK_OPTION = 1;
	public static final int CANCEL_OPTION = 2;
	
	/**
	 * Constructs a JFontChooser that uses the default font.
	 */
	 
	public JFontChooser()
	{	
		this(new Font("Courier New", Font.PLAIN, 12));
	}
	
	/**
	 * Constructs a JFontChooser using the given font.
	 */
	
	public JFontChooser(Font aFont)
	{
		Container container = getContentPane();
		JPanel panel = new JPanel();
		TitledBorder panelBorder = new TitledBorder("Demo");
		font = aFont;
		boolean found = false;
		textType = font.getFontName();
		textStyle = font.getStyle();
		textSize = font.getSize();
		fList.setSelectionMode(0);
		stList.setSelectionMode(0);
		sizeList.setSelectionMode(0);
		jspFont.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jspStyle.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jspSize.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		panel.setBorder(panelBorder);
		
		jtfFonts.setBounds(8, 32, 121, 20);
		jspFont.setBounds(8, 56, 121, 82);

		jtfStyle.setBounds(136, 32, 121, 20);
		jspStyle.setBounds(136, 56, 121, 82);
		
		jtfSize.setBounds(264, 32, 41, 20);
		jspSize.setBounds(264, 56, 41, 82);
		
		jbtOK.setBounds(320, 8, 89, 17);
		jbtCancel.setBounds(320, 32, 89, 17);
		
		panel.setBounds(320, 64, 89, 73);
		
		container.add(jlbFonts);
		container.add(jtfFonts);
		container.add(jspFont);
		
		container.add(jlbStyle);
		container.add(jtfStyle);
		container.add(jspStyle);
		
		container.add(jlbSize);
		container.add(jtfSize);
		container.add(jspSize);
		
		container.add(jbtOK);
		container.add(jbtCancel);
		
		container.add(panel);
		
		jtfTest.setBounds(8, 25, 73, 30);
		
		panel.add(jtfTest);
		
		container.setLayout(null);
		panel.setLayout(null);
		
		setSize(424, 177);
		setResizable(false);
		setModal(true);
		
		jtfFonts.addActionListener(this);
		jtfSize.addActionListener(this);
		jtfStyle.addActionListener(this);
		jbtCancel.addActionListener(this);
		jbtOK.addActionListener(this);
		fList.addListSelectionListener(this);
		stList.addListSelectionListener(this);
		sizeList.addListSelectionListener(this);
                
                //my addings
                jbtOK.setSize(70, 24);
                jbtCancel.setSize(70, 24);
	}
	
	/**
	 * Displays the font dialog on the screen positioned relative to
	 * the parent and blocks until the dialog is hidden.
	 */
	
	public int showDialog(Component parent, String title)
	{
		boolean found = false;
		option = CANCEL_OPTION;
		
		this.setTitle(title);
		jtfTest.setFont(new Font(textType, textStyle, textSize));
		
		/*
		 * Traverse through the lists and find the values that correspond
		 * to the selected font.  If it can't find the values then clear the
		 * selection.
		 */
		
		for (int i = 0; i < fList.getModel().getSize(); i++)
		{
			fList.setSelectedIndex(i);
			
			if (font.getName().equals((String)fList.getSelectedValue()))
			{
				found = true;
				setScrollPos(jspFont, fList, i);
				
				break;
			}
		}
		
		if (!found)
		{
			fList.clearSelection();
		}
		
		stList.setSelectedIndex(font.getStyle());
		
		found = false;
		
		for (int i = 0; i < sizeList.getModel().getSize(); i++)
		{
			sizeList.setSelectedIndex(i);
			
			if (font.getSize() == Integer.parseInt((String)sizeList.getSelectedValue()))
			{
				found = true;
				setScrollPos(jspSize, sizeList, i);
				
				break;
			}
		}
		
		if (!found)
		{
			sizeList.clearSelection();
		}
		
		this.setLocationRelativeTo(parent);
		this.setVisible(true);
		
		return option;
	}
	
	/**
	 * Sets the current font of the font chooser.
	 */
	
	public void setFont(Font aFont)
	{
		font = aFont;
	}
	
	/**
	 * Gets the current font of the font chooser.
	 */
	 
	public Font getFont()
	{
		return font;
	}
	
	/**
	 * Gets the name of the font chooser's current font.
	 */
	 
	public String getFontName()
	{
		return font.getFontName();
	}
	
	/**
	 * Gets the style of the font chooser's current font.
	 */
	 
	public int getFontStyle()
	{
		return font.getStyle();
	}
	
	/**
	 * Gets the size of the font chooser's current font.
	 */
	 
	public int getFontSize()
	{
		return font.getSize();
	}
	
	public void actionPerformed(ActionEvent e)
	{
		boolean found = false;
		
		if (e.getSource() == jtfFonts)
		{
			textType = jtfFonts.getText();
			
			for (int i = 0; i < fList.getModel().getSize(); i++)
			{				
				if (((String)fList.getModel().getElementAt(i)).startsWith(jtfFonts.getText().trim()))
				{
					fList.setSelectedIndex(i);
					setScrollPos(jspFont, fList, i);
					found = true;
					
					break;
				}
			}
			
			if (!found)
			{
				fList.clearSelection();
			}
			else
			{
				jtfTest.setFont(new Font(textType, textStyle, textSize));
			}
			
			found = false;
		}
		else if (e.getSource() == jtfSize)
		{
			textSize = (Integer.parseInt(jtfSize.getText().trim()));
			jtfTest.setFont(new Font(textType, textStyle, textSize));
			
			for (int i = 0; i < sizeList.getModel().getSize(); i++)
			{	
				if (jtfSize.getText().trim().equals((String)sizeList.getModel().getElementAt(i)))
				{
					sizeList.setSelectedIndex(i);
					setScrollPos(jspSize, sizeList, i);
					found = true;
					
					break;
				}
			}
			
			if (!found)
			{
				sizeList.clearSelection();
			}
			
			found = false;
		}
		else if (e.getSource() == jtfStyle)
		{
			if (jtfStyle.getText().equals("Regular"))
			{
				textStyle = Font.PLAIN;
			}
			else if (jtfStyle.getText().equals("Bold"))
			{
				textStyle = Font.BOLD;
			}
			else if (jtfStyle.getText().equals("Italic"))
			{
				textStyle = Font.ITALIC;
			}
			else if (jtfStyle.getText().equals("Bold Italic"))
			{
				textStyle = Font.BOLD & Font.ITALIC;
			}
			
			stList.setSelectedIndex(textStyle);
			
			jtfTest.setFont(new Font(textType, textStyle, textSize));
		}
		else if (e.getSource() == jbtOK)
		{
			option = OK_OPTION;
			font = new Font(textType, textStyle, textSize);
			this.setVisible(false);
		}
		else if (e.getSource() == jbtCancel)
		{
			option = CANCEL_OPTION;
			this.setVisible(false);
		}
	}
	
	public void valueChanged(ListSelectionEvent e)
	{
		if (e.getSource() == fList)
		{
			if (fList.getSelectedValue() != null)
			{
				jtfFonts.setText(((String)(fList.getSelectedValue())));
			}
			
			textType = jtfFonts.getText();
			jtfTest.setFont(new Font(textType, textStyle, textSize));
		}
		else if (e.getSource() == stList)
		{
			jtfStyle.setText(((String)(stList.getSelectedValue())));
			
			if (jtfStyle.getText().equals("Regular"))
			{
				textStyle = 0;
			}
			else if (jtfStyle.getText().equals("Bold"))
			{
				textStyle = 1;
			}
			else if (jtfStyle.getText().equals("Italic"))
			{
				textStyle = 2;
			}
			else if (jtfStyle.getText().equals("Bold Italic"))
			{
				textStyle = 3;
			}
			
			jtfTest.setFont(new Font(textType, textStyle, textSize));
		}
		else if (e.getSource() == sizeList)
		{	
			if (sizeList.getSelectedValue() != null)
			{
				jtfSize.setText(((String)(sizeList.getSelectedValue())));
			}
			
			textSize = (Integer.parseInt(jtfSize.getText().trim()));
			jtfTest.setFont(new Font(textType, textStyle, textSize));
		}
	}
	
	/*
	 * Takes a scrollPane, a JList and an index in the JList and sets the scrollPane's
	 * scrollbar so that the selected item in the JList is in about the middle of the
	 * scrollPane.
	 */
	
	private void setScrollPos(JScrollPane sp, JList list, int index)
	{
		int unitSize = sp.getVerticalScrollBar().getMaximum() / list.getModel().getSize();
		
		sp.getVerticalScrollBar().setValue((index - 2) * unitSize);
	}
}
