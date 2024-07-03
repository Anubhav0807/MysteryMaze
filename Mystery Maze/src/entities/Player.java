package entities;

import static utilz.Constants.MapConsts.*;
import static utilz.Constants.SizeConsts.*;
import static utilz.Constants.GameConsts.UPS_SET;
import static utilz.HelpMethods.*;
import static utilz.LoadImage.GetSprite;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.Game;

public class Player extends Entity {
	
	private int moveSpeed = (int) (4.0f * SCALE * 60 / UPS_SET);
	private int[][] map;
	
	private Game game;
	private ArrayList<Bomb> bombs;
	private BufferedImage[] sprites;
	private int spriteIdx = 0;
	private int animationTick = 0;
	private int stepTime = 4 * UPS_SET;
	private int stepTimeBlink = (int) (0.2f * UPS_SET);
	private float gameOverIn = 1.5f; // Seconds
	
	public boolean up, left, down, right;
	public boolean W, A, S, D;
	
	public boolean isAlive = true;
	public boolean isVisible = true;
	
	public boolean canDrop = true;
	public boolean isChestOpened = false;
	public boolean isKeyCollected = false;
	
	public int coinsCollected = 0;
	public int score = 0;
	
	public Player(Game game, int width, int height) {
		this(game, 0, 0, width, height, 0, 0);
	}
	
	public Player(Game game, int x, int y, int width, int height) {
		this(game, x, y, width, height, 0, 0);
	}
	
	public Player(Game game, int x, int y, int width, int height, int offsetX, int offsetY) {
		super(x, y, width, height, offsetX, offsetY);
		this.game = game;
		this.bombs = new ArrayList<>();
		sprites = new BufferedImage[] {
				GetSprite("MainCharacter"), 
				GetSprite("MainCharacter_Blink"), 
				GetSprite("MainCharacter_Dead")
		};
	}
	
	public void update() {
		if (isAlive && isVisible) {
			int dx = 0, dy = 0;
			if (up    || W) dy -= moveSpeed;
			if (left  || A) dx -= moveSpeed;
			if (down  || S) dy += moveSpeed;
			if (right || D) dx += moveSpeed;
			
			if (dx!=0 || dy!=0) {		
				autoAlign(dx, dy);
				move(dx, dy);
			}
			
			for (Bomb bomb: bombs) {
				bomb.update();
			}
			
			updateAnimationTick();
			checkCollision();
		} else if (isAlive) {
			gameOverIn -= 1.0f/UPS_SET;
			if (gameOverIn <= 0.0f) {
				game.getMazeState().gameNotOver = false;
	        	game.getMazeState().getEndScreenOverlay().setVisible(true);
			}
		}
	}
	
	public void updateAnimationTick() {
		animationTick++;
		if (spriteIdx == 0) {
			if (animationTick >= stepTime) {
				animationTick -= stepTime;
				spriteIdx = 1;
			}
		} else if (spriteIdx == 1) {
			if (animationTick >= stepTimeBlink) {
				animationTick -= stepTimeBlink;
				spriteIdx = 0;
			}
		}
	}
	
	public void checkCollision() {
		// Checks for collison with enemy and objects (not walls)
		int xIdx = Math.round((float)x / TILE_SIZE);
		int yIdx = Math.round((float)y / TILE_SIZE);
		
		switch (map[xIdx][yIdx]) {
		case TREASURE:
			if (!isChestOpened) {
				isChestOpened = true;
			}
			break;
		case KEY:
			isKeyCollected = true;
			break;
		case COIN:
			map[xIdx][yIdx] = PATH;
			coinsCollected++;
			break;
		case DOOR:
			if (isKeyCollected) isVisible = false;
			break;
		}
	}
	
	public void render(Graphics g) {
		if (isVisible) g.drawImage(sprites[spriteIdx], x, y, width, height, null);
		for (Bomb bomb: bombs) {
			bomb.render(g);
		}
	}
	
	public void dropBomb() {
		if (canDrop) {
			int xIdx = Math.round((float)x / TILE_SIZE);
			int yIdx = Math.round((float)y / TILE_SIZE);
			bombs.add(new Bomb(xIdx * TILE_SIZE + 2, yIdx * TILE_SIZE + 2, TILE_SIZE - 4, TILE_SIZE - 4));
			canDrop = false;
		}
	}
	
	public void setMap(int[][] map) {
		this.map = map;
	}
	
	public void stop() {
		up = left = down = right = false;
		W = A = S = D = false;
	}
	
	public void reset() {
		stop();
		canDrop = true;
		isAlive = true;
		isVisible = true;
		isKeyCollected = false;
		isChestOpened = false;
		coinsCollected = 0;
	}
	
	private void autoAlign(int dx, int dy) {
		if (dy == 0) {
			if( dx > 0) { // Trying to go right
				if ((IsSolid(x + dx + width, y, map) && !IsSolid(x + dx + width, y + height, map))) {
					// Top right is solid but bottom right is not solid
					move(0, +moveSpeed); // Go down
				} else if (!IsSolid(x + dx + width, y, map) && IsSolid(x + dx + width, y + height, map)) {
					// Top right is not solid but bottom right is solid
					move(0, -moveSpeed); // Go up
				}
			} else if (dx < 0) { // Trying to go left
				if ((IsSolid(x + dx, y, map) && !IsSolid(x + dx, y + height, map))) {
					// Top left is solid but bottom left is not solid
					move(0, +moveSpeed); // Go down
				} else if (!IsSolid(x + dx, y, map) && IsSolid(x + dx, y + height, map)) {
					// Top left is not solid but bottom left is solid
					move(0, -moveSpeed); // Go up
				}
			}
		} else if (dx == 0) {
			if( dy < 0) { // Trying to go up
				if ((IsSolid(x, y + dy , map) && !IsSolid(x + width, y + dy , map))) {
					// Top left is solid but top right is not solid
					move(+moveSpeed, 0); // Go right
				} else if (!IsSolid(x, y + dy , map) && IsSolid(x + width, y + dy , map)) {
					// Top left is not solid but top right is solid
					move(-moveSpeed, 0); // Go left
				}
			} else if (dy > 0) { // Trying to go down
				if ((IsSolid(x, y + dy  + height, map) && !IsSolid(x + width, y + dy  + height, map))) {
					// Bottom left is solid but bottom right is not solid
					move(+moveSpeed, 0); // Go right
				} else if (!IsSolid(x, y + dy  + height, map) && IsSolid(x + width, y + dy  + height, map)) {
					// Bottom left is not solid but bottom right is solid
					move(-moveSpeed, 0); // Go left
				}
			}
		}
	}
	
	private void move(int dx, int dy) {
	    int stepX = (dx != 0) ? dx / Math.abs(dx) : 0;
	    int stepY = (dy != 0) ? dy / Math.abs(dy) : 0;

	    while (dx != 0 || dy != 0) {
	        if (dx != 0 && CanMoveHere(x + stepX, y, width, height, map)) {
	            x += stepX;
	            dx -= stepX;
	        } else if (dx != 0) {
	            dx = 0; // Stop horizontal movement if blocked
	        }

	        if (dy != 0 && CanMoveHere(x, y + stepY, width, height, map)) {
	            y += stepY;
	            dy -= stepY;
	        } else if (dy != 0) {
	            dy = 0; // Stop vertical movement if blocked
	        }
	    }
		
		// Move the hitbox
		hitbox.x = x;
		hitbox.y = y;
	}

}
