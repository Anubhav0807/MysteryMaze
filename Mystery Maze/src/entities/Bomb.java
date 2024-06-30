package entities;

import java.awt.Color;
import java.awt.Graphics;

public class Bomb extends Entity {
	
	private long igniteTime;
	private final long timer = 2000; // In milli seconds
	private boolean blasted = false;
	
	public Bomb(int width, int height) {
		this(0, 0, width, height, 0, 0);
	}
	
	public Bomb(int x, int y, int width, int height) {
		this(x, y, width, height, 0, 0);
	}

	public Bomb(int x, int y, int width, int height, int offsetX, int offsetY) {
		super(x, y, width, height, offsetX, offsetY);
		igniteTime = System.currentTimeMillis();
	}
	
	public void update() {
		if (System.currentTimeMillis() - igniteTime >= timer) {
			blast();
		}
	}
	
	public void render(Graphics g) {
		if (!blasted) {
			g.setColor(Color.black);
			g.fillOval(x, y, width, height);
		}
	}
	
	private void blast() {
		// Do blast animation, destroy spikes, enemy, player in range.
		blasted = true;
	}

}
