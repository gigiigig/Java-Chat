package facebookchat.ui.common;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class ResizeListener implements MouseListener, MouseMotionListener {
	JFrame frame;
	JComponent resizeArea;
	Point start_drag;

	// Cursor defaultCursor;

	public ResizeListener(JFrame parent, JComponent resizeArea) {
		this.frame = parent;
		this.resizeArea = resizeArea;
	}
	public void mouseClicked(MouseEvent arg0) {
	}
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		// defaultCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
		Cursor cur = Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR);
		frame.setCursor(cur);
	}
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	public void mousePressed(MouseEvent e) {
		start_drag = new Point((int) e.getX(), (int) e.getY());
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseDragged(MouseEvent e) {
		Point frameLoc = frame.getLocationOnScreen();
		Point buttLoc = resizeArea.getLocationOnScreen();
		Dimension newSize = new Dimension(
				(int) (e.getX() + buttLoc.getX() - frameLoc.getX()), (int) (e
						.getY()
						+ (int) buttLoc.getY() - frameLoc.getY()));
		frame.setSize(newSize);
		/*
		 * log.debug("e: " + (int)e.getX() + " : " + (int)e.getY() + " : " +
		 * (int)buttLoc.getX() + " : " + (int)buttLoc.getY() );
		 */
	}

	public void mouseMoved(MouseEvent arg0) {
	}

}