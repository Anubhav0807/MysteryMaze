package gamestates;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.Game;
import ui.CustomButton;

import static utilz.LoadImage.GetSprite;
import static utilz.Constants.SizeConsts.*;
import static utilz.Constants.GameConsts.UPS_SET;

public class MenuState extends State implements StateMethods {
	
	private BufferedImage blurBg;
	private CustomButton playButton;
	private CustomButton exitButton;
	private int buttonWidth = (int) (200 * SCALE);
	private int buttonHeight = (int) (60 * SCALE);
	
	private String title;
	private Font titleFont;
	private double titleY = 0;
	private double angle = 0;

	public MenuState(Game game) {
		super(game);
		initClasses();
	}

	@Override
	public void initClasses() {
		blurBg = GetSprite("Maze_Blur");
		title = "Mystery Maze";
		titleFont = new Font("Lucida Console", Font.BOLD, (int) (72 * SCALE));
		
		playButton = new CustomButton("Play", 36);
		playButton.setBounds(GAME_WIDTH/2 - buttonWidth/2, GAME_HEIGHT*3/4 - buttonHeight*3/2, buttonWidth, buttonHeight);
		game.getGamePanel().add(playButton);
		
		playButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				GameState.state = GameState.MAZE;
				game.getMazeState().getHud().initTimer();
				game.getGamePanel().remove(playButton);
				game.getGamePanel().remove(exitButton);
				game.getGamePanel().requestFocus();
			}
		});
		
		exitButton = new CustomButton("Exit", 36);
		exitButton.setBounds(GAME_WIDTH/2 - buttonWidth/2, GAME_HEIGHT*3/4, buttonWidth, buttonHeight);
		game.getGamePanel().add(exitButton);
		
		exitButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				game.closeWindow();
			}
		});
	}

	@Override
	public void update() {
		titleY = Math.sin(angle) * 7 + 140;
		angle += Math.PI/UPS_SET/1.5;
		if (angle > 2*Math.PI) angle = 0.0;
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(blurBg, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
		g.setColor(Color.white);
		g.setFont(titleFont);
		g.drawString("Mystery Maze", GAME_WIDTH/2 - g.getFontMetrics().stringWidth(title)/2, (int) (titleY * SCALE));
	}

	@Override
	public void windowFocusLost() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
