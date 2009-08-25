package facebookchat.ui.common;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class DialogEarthquakeCenter extends Object {

	public static final int SHAKE_DISTANCE = 10;
	public static final double SHAKE_CYCLE = 50;
	public static final int SHAKE_DURATION = 500;
	public static final int SHAKE_UPDATE = 5;

	private Window dialog;
	private Point naturalLocation;
	private long startTime;
	private Timer shakeTimer;
	private final double TWO_PI = Math.PI * 2.0;

	public DialogEarthquakeCenter(Window parent) {
		dialog = parent;
	}

	public void startNudging() {
		naturalLocation = dialog.getLocation();
		startTime = System.currentTimeMillis();
		shakeTimer = new Timer(SHAKE_UPDATE, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// calculate elapsed time
				long elapsed = System.currentTimeMillis() - startTime;
				// use sin to calculate an x-offset
				double waveOffset = (elapsed % SHAKE_CYCLE) / SHAKE_CYCLE;
				double angle = waveOffset * TWO_PI;

				// offset the x-location by an amount
				// proportional to the sine, up to
				// shake_distance
				int shakenX = (int) ((Math.sin(angle) * SHAKE_DISTANCE) + naturalLocation.x);
				dialog.setLocation(shakenX, naturalLocation.y);
				dialog.repaint();

				// should we stop timer?
				if (elapsed >= SHAKE_DURATION)
					stopShake();
			}
		});
		shakeTimer.start();
	}

	public void stopShake() {
		shakeTimer.stop();
		dialog.setLocation(naturalLocation);
		dialog.repaint();
	}
}