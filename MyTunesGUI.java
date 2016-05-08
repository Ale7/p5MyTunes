import java.awt.Dimension;
import javax.swing.JFrame;

/**
 * GUI for MyTunes
 * Creates the frame for the application and adds MyTunesGUIPanel to it.
 *
 * @author Alec Wooding
 */
public class MyTunesGUI {
	/**
	 * Create the JFrame and add the main panel.
	 * 
	 * @param args 
	 * 		  not used 
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame("MyTunes");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(new MyTunesGUIPanel());
		frame.setPreferredSize(new Dimension(1280, 720));
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}
}