package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import entities.Player;
import main.Game;

import static utilz.Constants.SizeConsts.*;
import static utilz.LoadImage.*;

public class HUD {
	
	private final Game game;
	private final Player player;
	private final Font font;	
	private final BufferedImage bombImg;
	private final BufferedImage keyImg;
	
	private long startTime, currentTime;
	private int totalTimeInSecs;
	private int minutes, seconds;
	
	private String bombCount;
	private String timer;
	private String score;
	
	public HUD(Game game, Player player) {
		this.game = game;
		this.player = player;
		font = new Font("Lucida Console", Font.PLAIN, 32);
		bombImg = GetSprite("Bomb");
		keyImg = GetRotatedImage(GetSprite("Key"), 90);
		bombCount = "x 0";
		timer = "00:00";
		score = "Score: 0";
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
			bombCount = "x " + player.bombsLeft;
			timer = String.format("%02d:%02d", minutes, seconds);
			score = "Score: " + player.score;
		}
	}
	
	public void render(Graphics g) {
		g.drawImage(bombImg, TILE_SIZE, (int) (GAME_HEIGHT-TILE_SIZE*1.35f), TILE_SIZE, TILE_SIZE, null);
		g.setColor(Color.white);
		g.setFont(font);
		g.drawString(bombCount, TILE_SIZE*5/2, (int) (GAME_HEIGHT - 18 * SCALE));
		g.drawString(timer, GAME_WIDTH/2 - g.getFontMetrics().stringWidth(timer)/2, (int) (GAME_HEIGHT - 18 * SCALE));
		g.drawString(score, GAME_WIDTH - g.getFontMetrics().stringWidth("Score: 0")*7/4, (int) (GAME_HEIGHT - 18 * SCALE));
		
		if (player.isKeyCollected && player.isVisible) {
			g.drawImage(keyImg, TILE_SIZE*13/2, (int) (GAME_HEIGHT-TILE_SIZE*1.35f), TILE_SIZE, TILE_SIZE, null);
			g.drawString("x 1", TILE_SIZE*8, (int) (GAME_HEIGHT - 18 * SCALE));
		}
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
