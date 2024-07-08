package entities;

import static utilz.Constants.MapConsts.*;
import static utilz.Constants.SizeConsts.*;
import static utilz.Constants.GameConsts.UPS_SET;
import static utilz.Constants.AudioConsts.*;
import static utilz.HelpMethods.*;
import static utilz.LoadImage.GetSprite;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.Game;

public class Player extends Entity {
	
	private final int moveSpeed = 4;
	private int[][] map;
	
	private final Game game;
	private final ArrayList<Bomb> bombs;
	private final BufferedImage[] sprites;
	private int spriteIdx = 0;
	private int animationTick = 0;
	private int stepTime = 4 * UPS_SET;
	private float stepTimeBlink = 0.2f * UPS_SET;
	private float gameOverIn = 1.5f; // Seconds
	
	// Door lock message
	private final String msg = "Door is locked.";
	private final Font font;
	private boolean showMsg;
	
	public boolean up, left, down, right;
	public boolean W, A, S, D;
	
	public boolean isAlive = true;
	public boolean isVisible = true;
	
	public boolean canDrop = true;
	public boolean isChestOpened = false;
	public boolean isKeyCollected = false;
	
	public int bombsLeft = 10;
	public int coinsCollected = 0;
	public int score = 0;
	
	public Player(Game game, int width, int height) {
		this(game, 0, 0, width, height);
	}
	
	public Player(Game game, int x, int y, int width, int height) {
		super(x, y, width, height);
		this.game = game;
		this.bombs = new ArrayList<Bomb>();
		font = new Font("Dialog", Font.PLAIN, (int) (12 * SCALE));
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
			
			updateAnimationTick();
			checkCollision();
			super.update();
			
		} else {
			if (isAlive) {
				game.getMazeState().levelCleared = true;
			}
			else {
				spriteIdx = 2;
				game.getMazeState().levelCleared = false;
			}
			gameOverIn -= 1.0f/UPS_SET;
			if (gameOverIn <= 0.0f && game.getMazeState().gameNotOver) {
				game.getMazeState().gameNotOver = false;
				if (!game.getMazeState().levelCleared) {
					game.audioPlayer.stop(BG_MUSIC);
					game.audioPlayer.play(GAME_OVER_SOUND);
				}
	        	if (!isAlive) {
	        		game.getMazeState().getEndScreenOverlay().setVisible(true);
	        	}
			}
		}
		for (int i=0; i < bombs.size(); i++) {
			bombs.get(i).update();
			if (bombs.get(i).toBeDeleted) bombs.remove(i);
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
		// Checks for collison with objects
		showMsg = false;
		
		switch (map[xIdx][yIdx]) {
		case TREASURE:
			if (!isChestOpened) {
				game.audioPlayer.play(VALUABLE_SOUND);
				isChestOpened = true;
				score += 100;
			}
			break;
		case KEY:
			game.audioPlayer.play(VALUABLE_SOUND);
			map[xIdx][yIdx] = PATH;
			isKeyCollected = true;
			break;
		case COIN:
			game.audioPlayer.play(COIN_SOUND);
			map[xIdx][yIdx] = PATH;
			coinsCollected++;
			score += 10;
			break;
		case DOOR:
			if (isKeyCollected) {
				game.audioPlayer.play(DOOR_OPEN_SOUND);
				isVisible = false;
			} else {
				showMsg = true;
			}
			break;
		case SPIKE:
			game.audioPlayer.play(DIE_SOUND);
			isAlive = false;
			break;
		}
	}
	
	public void render(Graphics g) {
		for (int i=0; i < bombs.size(); i++) {
			bombs.get(i).render(g);
		}
		if (isVisible) g.drawImage(sprites[spriteIdx], x, y, width, height, null);
		if (showMsg) {
			g.setFont(font);
			int msgWidth = g.getFontMetrics().stringWidth(msg);
			g.setColor(Color.gray);
			g.fillRect(x + width/2 - msgWidth/2, (int) (y - 15*SCALE), msgWidth, (int) (12 * SCALE));
			g.setColor(Color.white);
			g.drawString(msg, x + width/2 - msgWidth/2, (int) (y - 5*SCALE));
		}
	}
	
	public void dropBomb() {
		if (canDrop && bombsLeft > 0 && isAlive && isVisible) {
			bombs.add(new Bomb(xIdx * TILE_SIZE + 2, yIdx * TILE_SIZE + 2, TILE_SIZE - 4, TILE_SIZE - 4, game));
			canDrop = false;
			bombsLeft--;
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
		bombs.clear();
		canDrop = true;
		isAlive = true;
		isVisible = true;
		isKeyCollected = false;
		isChestOpened = false;
		coinsCollected = 0;
		bombsLeft = 10;
		spriteIdx = 0;
		gameOverIn = 1.5f;
	}
	
	public Game getGame() {
		return game;
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
	}

}
