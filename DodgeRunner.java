import java.awt.Color;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Runner class for the Dodge improvement game project
 * @author Blue
 */

public class DodgeRunner {

	private final int FRAME_WIDTH = 1200;
	private final int FRAME_HEIGHT = 900;
	private final int INIT_HEALTH = 100;
	private final int INIT_SCORE = 0;
	private final int COL_RADIUS = 30;

	// default constructor
	public DodgeRunner() {
		Frame mf = new Frame("W00T! GRAPHICS!");
		mf.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		mf.setVisible(true);
		mf.setBackground(Color.BLACK);
		DodgeCanvas myCanvas = new DodgeCanvas(FRAME_WIDTH, FRAME_HEIGHT, INIT_HEALTH, INIT_SCORE, COL_RADIUS, 25);
		mf.add(myCanvas);
		/*
		 * Allows you to close the window using exit buttons
		 */
		mf.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	} // end default constructor

	public static void main(String args[]) {
		new DodgeRunner();
	} // end main

} // end class DodgeRunner
