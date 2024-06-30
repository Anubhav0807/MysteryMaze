package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class Entity {

	protected int x, y;
	protected int offsetX, offsetY;
	protected int width, height;
	protected Rectangle hitbox;

	public Entity(int width, int height) {
		this(0, 0, width, height, 0, 0);
		hitbox = new Rectangle(x, y, width, height);
	}
	
	public Entity(int x, int y, int width, int height) {
		this(x, y, width, height, 0, 0);
		hitbox = new Rectangle(x, y, width, height);
	}
	
	public Entity(int x, int y, int width, int height, int offsetX, int offsetY) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		hitbox = new Rectangle(x + offsetX, y + offsetY, width, height);
	}
	
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
		hitbox.x = x + offsetX;
		hitbox.y = y + offsetY;
	}
	
	public void setOffset(int offsetX, int offsetY) {
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		hitbox.x = x + offsetX;
		hitbox.y = y + offsetY;
	}
	
	protected void renderHitbox(Graphics g) {
		// For debugging
		g.setColor(Color.RED);
		g.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
	}

}
