package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import inputs.KeyboardInputs;
import inputs.MouseInputs;

import static utilz.Constants.SizeConsts.*;

@SuppressWarnings("serial")
public class GamePanel extends JPanel {
	
	private Game game;
	private KeyboardInputs keyboardInputs;
	private MouseInputs mouseInputs;
	
	public GamePanel(Game game) {
		this.game = game;
		keyboardInputs = new KeyboardInputs(game);
		mouseInputs = new MouseInputs(game);
		
		addKeyListener(keyboardInputs);
		addMouseListener(mouseInputs);
		addMouseMotionListener(mouseInputs);
		
		setLayout(null);
		setBackground(new Color(0x344155));
		setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g); // Clear screen
		game.render(g);
	}

}
