package main;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class GameWindow extends JFrame {
	
	public GameWindow(GamePanel gamePanel) {
		setTitle("Mystery Maze");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		add(gamePanel);
		pack();
		setLocationRelativeTo(null); // Center the window
		setVisible(true);
	}

}
