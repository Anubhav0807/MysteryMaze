package main;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import inputs.KeyboardInputs;

import static utilz.Constants.SizeConsts.*;

@SuppressWarnings("serial")
public class GamePanel extends JPanel {
	
	private Game game;
	
	public GamePanel(Game game) {
		this.game = game;
		addKeyListener(new KeyboardInputs(game));
		setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g); // Clear screen
		game.render(g);
	}

}
