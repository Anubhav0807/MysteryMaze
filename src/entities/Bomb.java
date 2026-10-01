package entities;

import static utilz.Constants.GameConsts.UPS_SET;
import static utilz.LoadImage.*;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.Game;
import maze.Vector2;

import static utilz.Constants.MapConsts.*;
import static utilz.Constants.SizeConsts.TILE_SIZE;
import static utilz.Constants.AudioConsts.EXPLOSION_SOUND;;

public class Bomb extends Entity {
	
	private long igniteTime;
	private final long timer = 2000; // In milli seconds
	private boolean blasted = false;
	private boolean isPlayerOnTop = true;
	private final BufferedImage bombImg;
	private BufferedImage sparkImg;
	private final BufferedImage shadowImg;
	private int animationTick = 0;
	private int stepTime = (int) (0.2f * UPS_SET);
	
	private final ArrayList<BombFlash> explosion;
	private final ArrayList<Enemy> enemies;
	private final Player player;
	private final Game game;
	private final int[][] map;
	
	public boolean toBeDeleted = false;
	
	public Bomb(int width, int height, Game game) {
		this(0, 0, width, height, game);
	}

	public Bomb(int x, int y, int width, int height, Game game) {
		super(x, y, width, height);
		this.game = game;
		player = game.getMazeState().getPlayer();
		enemies = game.getMazeState().getMazeGenerator().getEnemies();
		map = game.getMazeState().getMazeGenerator().getMaze();
		explosion = new ArrayList<BombFlash>();
		igniteTime = System.currentTimeMillis();
		bombImg = GetSprite("Bomb");
		sparkImg = GetSprite("Bomb_Spark");
		shadowImg = GetSprite("Shadow");
	}
	
	public void update() {
		if (blasted) {
			for (int i=0; i < explosion.size(); i++) {
				explosion.get(i).update();
				if (explosion.get(i).toBeDeleted) explosion.remove(i);
			}
			toBeDeleted = explosion.isEmpty();
		}
		else {
			if (System.currentTimeMillis() - igniteTime >= timer) {
				blast();
			}
			updateAnimationTick();
			super.update();
			checkCollision();
		}
	}
	
	private void updateAnimationTick() {
		animationTick++;
		if (animationTick >= stepTime) {
			animationTick -= stepTime;
			sparkImg = GetRotatedImage(sparkImg, 30);
		}
	}
	
	private void checkCollision() {
		for (Enemy enemy: enemies) {
			if (isColliding(enemy)) {
				blast();
				break;
			}
		}
		if (isColliding(player)) {
			if (!isPlayerOnTop) blast();
		} else if (isPlayerOnTop) {
			isPlayerOnTop = false;
		}
	}
	
	public void render(Graphics g) {
		if (blasted) {
			for (int i=0; i < explosion.size(); i++) {
				explosion.get(i).render(g);
			}
		}
		else {
			g.drawImage(shadowImg, x, (int) (y + height/2.1), width, height, null);
			g.drawImage(bombImg, x, y, width, height, null);
			g.drawImage(sparkImg, x+width/2, y-height/2, width, height, null);
		}
	}
	
	private void blast() {
		blasted = true;
		game.audioPlayer.play(EXPLOSION_SOUND);
		Vector2 up, left, down, right;
		boolean upBlocked, leftBlocked, downBlocked, rightBlocked;
		upBlocked = leftBlocked = downBlocked = rightBlocked = false;
		
		explosion.add(new BombFlash(xIdx * TILE_SIZE, yIdx * TILE_SIZE, TILE_SIZE, TILE_SIZE, 0, game));
		
		for (int i=1; i<=BOMB_RANGE; i++) {
			up     = new Vector2(xIdx, yIdx-i);
			left   = new Vector2(xIdx-i, yIdx);
			down   = new Vector2(xIdx, yIdx+i);
			right  = new Vector2(xIdx+i, yIdx);
			
			if (up.isValid() && map[up.x][up.y] != WALL && !upBlocked) {
				explosion.add(new BombFlash(up.x * TILE_SIZE, up.y * TILE_SIZE, TILE_SIZE, TILE_SIZE, i, game));
			} else {
				upBlocked = true;
			}
			
			if (left.isValid() && map[left.x][left.y] != WALL && !leftBlocked) {
				explosion.add(new BombFlash(left.x * TILE_SIZE, left.y * TILE_SIZE, TILE_SIZE, TILE_SIZE, i, game));
			} else {
				leftBlocked = true;
			}
			
			if (down.isValid() && map[down.x][down.y] != WALL && !downBlocked) {
				explosion.add(new BombFlash(down.x * TILE_SIZE, down.y * TILE_SIZE, TILE_SIZE, TILE_SIZE, i, game));
			} else {
				downBlocked = true;
			}
			
			if (right.isValid() && map[right.x][right.y] != WALL && !rightBlocked) {
				explosion.add(new BombFlash(right.x * TILE_SIZE, right.y * TILE_SIZE, TILE_SIZE, TILE_SIZE, i, game));
			} else {
				rightBlocked = true;
			}
		}
	}

}
