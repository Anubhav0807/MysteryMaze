package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static utilz.Constants.SizeConsts.*;
import static utilz.Constants.AudioConsts.*;

public class EndScreenOverlay {
	
	private HUD hud;
	private Font font1, font2;
	private int textX;
	
	private CustomButton restartButton;
	private CustomButton exitButton;
	private int buttonWidth = (int) (120 * SCALE);
	private int buttonHeight = (int) (35 * SCALE);
	
	public EndScreenOverlay(HUD hud) {
		this.hud = hud;
		font1 = new Font("Lucida Console", Font.BOLD, (int) (52 * SCALE));
		font2 = new Font("Lucida Console", Font.BOLD, (int) (32 * SCALE));
		
		restartButton = new CustomButton("Restart", 24);
		restartButton.setBounds(GAME_WIDTH/2 - buttonWidth/2, (int) (GAME_HEIGHT * 0.6f), buttonWidth, buttonHeight);
		hud.getGame().getGamePanel().add(restartButton);
		restartButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				hud.getGame().getMazeState().getMazeGenerator().generateMaze();
				hud.getGame().getMazeState().getPlayer().reset();
				hud.getGame().getMazeState().gameNotOver = true;
				hud.getGame().getMazeState().getHud().initTimer();
				hud.getGame().getMazeState().getPlayer().score = 0;
				hud.getGame().audioPlayer.playOnLoop(BG_MUSIC);
				setVisible(false);
			}
		});
		
		exitButton = new CustomButton("Exit", 24);
		exitButton.setBounds(GAME_WIDTH/2 - buttonWidth/2, (int) (GAME_HEIGHT * 0.7f), buttonWidth, buttonHeight);
		hud.getGame().getGamePanel().add(exitButton);
		exitButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				hud.getGame().closeWindow();
			}
		});
		
		setVisible(false); // Only for buttons
	}
	
	public void render(Graphics g) {
		g.setColor(new Color(0, 0, 0, 0.6f));
		g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		
		g.setColor(Color.red);
		g.setFont(font1);
		textX = GAME_WIDTH/2 - g.getFontMetrics().stringWidth("GAME OVER")/2;
		g.drawString("GAME OVER", textX, GAME_HEIGHT/4);
		
		g.setColor(Color.white);
		g.setFont(font2);
		textX = GAME_WIDTH/2 - g.getFontMetrics().stringWidth(hud.getScore())/2;
		g.drawString(hud.getScore(), textX, (int) (GAME_HEIGHT * 0.4f));
		g.drawString("Time:  " + hud.getTimer(), textX, (int) (GAME_HEIGHT * 0.5f));
	}
	
	public void setVisible(boolean visible) {
		restartButton.setVisible(visible);
		exitButton.setVisible(visible);
	}

}
