package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import entities.Player;
import main.Game;

import static utilz.Constants.SizeConsts.*;

public class HUD {
	
	private Game game;
	private Player player;
	private Font font;
	
	private long startTime, currentTime;
	private int totalTimeInSecs;
	private int minutes, seconds;
	
	private String timer;
	private String score;
	
	public HUD(Game game, Player player) {
		this.game = game;
		this.player = player;
		font = new Font("Lucida Console", Font.PLAIN, 32);
	}
	
	public void initTimer() {
		startTime = System.currentTimeMillis();
	}
	
	public void update() {
		if (player.isAlive && player.isVisible) {
			currentTime = System.currentTimeMillis();
			totalTimeInSecs = (int) (currentTime - startTime) / 1000;
			minutes = totalTimeInSecs / 60;
			seconds = totalTimeInSecs % 60;
			timer = String.format("%02d:%02d", minutes, seconds);
			score = "Score: " + player.score;
		}
	}
	
	public void render(Graphics g) {
		g.setColor(Color.white);
		g.setFont(font);
		g.drawString(timer, GAME_WIDTH/2 - g.getFontMetrics().stringWidth(timer)/2, (int) (GAME_HEIGHT - 18 * SCALE));
		g.drawString(score, GAME_WIDTH - g.getFontMetrics().stringWidth(score)*7/4, (int) (GAME_HEIGHT - 18 * SCALE));
	}
	
	public String getTimer() {
		return timer;
	}
	
	public String getScore() {
		return score;
	}
	
	public Game getGame() {
		return game;
	}

}
