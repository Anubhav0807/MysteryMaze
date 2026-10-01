package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.Game;

import static utilz.LoadImage.GetSprite;
import static utilz.Constants.GameConsts.UPS_SET;
import static utilz.Constants.MapConsts.*;

public class BombFlash extends Entity {
	
	private final Player player;
	private final ArrayList<Enemy> enemies;
	private final int[][] map;
	private final BufferedImage flashImg;
	
	private float delayAppearance, delayDisappearance;
	private float sizePercent = 0.25f;
	private boolean flashGrowing = true;
	public boolean toBeDeleted = false;
	
	public BombFlash(int width, int height, int wave, Game game) {
		this(0, 0, width, height, wave, game);
	}

	public BombFlash(int x, int y, int width, int height, int wave, Game game) {
		super(x, y, width, height);
		
		player = game.getMazeState().getPlayer();
		enemies = game.getMazeState().getMazeGenerator().getEnemies();
		map = game.getMazeState().getMazeGenerator().getMaze();
		flashImg = GetSprite("Bomb_Flash");
		
		delayAppearance = wave;
		delayDisappearance = BOMB_RANGE + 1.0f - wave;
	}
	
	public void update() {
		// Destroy enemies, spikes and player on contact
		for (Enemy enemy: enemies) {
			if (isColliding(enemy)) enemy.isAlive = false;
		}
		if (isColliding(player)) player.isAlive = false;
		if (map[xIdx][yIdx] == SPIKE) map[xIdx][yIdx] = PATH;
		
		updateAnimation();
	}
	
	public void render(Graphics g) {
		if (delayAppearance <= 0) {
			g.drawImage(flashImg, (int) (x+(1-sizePercent)*width/2), (int) (y+(1-sizePercent)*height/2), 
					(int) (width * sizePercent), (int) (height * sizePercent), null);
		}
	}
	
	private void updateAnimation() {
		if (delayAppearance > 0) {
			delayAppearance -= 15.0f / UPS_SET;
		} else if (sizePercent < 1.0f && flashGrowing) {
			sizePercent += 5.0f / UPS_SET;
			if (sizePercent > 1.0f) sizePercent = 1.0f;
		} else if (delayDisappearance > 0) {
			delayDisappearance -= 15.0f / UPS_SET;
			flashGrowing = false;
		} else if (sizePercent > 0.25f) {
			sizePercent -= 5.0f / UPS_SET;
		} else {
			toBeDeleted = true;			
		}
	}

}
