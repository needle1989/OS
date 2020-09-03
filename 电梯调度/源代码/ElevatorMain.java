package sse.os.imperialElevator;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

/*初始化界面
 *
 * 
 */

/**
 * 
 */
public class ElevatorMain {

	public static void main(String[] args) {
		JFrame frame = new ElevatorFrame();
		frame.setTitle("Imperial Elevator");
		frame.setLocation(250, 120);
		frame.setSize(970, 560);
		frame.setResizable(false);
		frame.setVisible(true);
		ImageIcon imageIcon=new ImageIcon("./ow.png");
		frame.setIconImage(imageIcon.getImage());
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}
}
