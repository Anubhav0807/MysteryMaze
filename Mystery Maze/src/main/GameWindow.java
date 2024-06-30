package main;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class GameWindow extends JFrame {
	
	public GameWindow(Game game, GamePanel gamePanel) {
		setTitle("Mystery Maze");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		add(gamePanel);
		pack();
		setLocationRelativeTo(null); // Center the window
		setVisible(true);
		addWindowFocusListener(new WindowFocusListener() {

			@Override
			public void windowLostFocus(WindowEvent e) {
				game.windowFocusLost();
			}

			@Override
			public void windowGainedFocus(WindowEvent e) {
				// TODO Auto-generated method stub

			}
		});
	}

}
