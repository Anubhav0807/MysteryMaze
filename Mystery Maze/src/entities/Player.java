package entities;

import static utilz.Constants.SizeConsts.SCALE;
import static utilz.Constants.GameConsts.UPS_SET;
import static utilz.HelpMethods.*;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Player extends Entity {
	
	private int moveSpeed = (int) (4.0f * SCALE * 60 / UPS_SET);
	private int[][] map;
	private ArrayList<Bomb> bombs;
	
	public boolean up, left, down, right;
	public boolean W, A, S, D;
	public boolean canDrop = true;
	
	public Player(int width, int height) {
		this(0, 0, width, height, 0, 0);
	}
	
	public Player(int x, int y, int width, int height) {
		this(x, y, width, height, 0, 0);
	}
	
	public Player(int x, int y, int width, int height, int offsetX, int offsetY) {
		super(x, y, width, height, offsetX, offsetY);
		this.bombs = new ArrayList<>();
	}
	
	public void update() {
		for (Bomb bomb: bombs) {
			bomb.update();
		}
		
		int dx = 0, dy = 0;
		if (up    || W) dy -= moveSpeed;
		if (left  || A) dx -= moveSpeed;
		if (down  || S) dy += moveSpeed;
		if (right || D) dx += moveSpeed;
		
		if (dx==0 && dy==0) return;
		
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
		
		move(dx, dy);
	}
	
	public void render(Graphics g) {
		g.setColor(new Color(0, 100, 250));
		g.fillRect(x, y, width, height);
		for (Bomb bomb: bombs) {
			bomb.render(g);
		}
	}
	
	public void dropBomb() {
		if (canDrop) {
			bombs.add(new Bomb(x, y, width, height));
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
