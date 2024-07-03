package entities;

import static utilz.Constants.GameConsts.UPS_SET;
import static utilz.LoadImage.*;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Bomb extends Entity {
	
	private long igniteTime;
	private final long timer = 2000; // In milli seconds
	private boolean blasted = false;
	private BufferedImage bombImg;
	private BufferedImage sparkImg;
	private BufferedImage shadowImg;
	private int animationTick = 0;
	private int stepTime = (int) (0.2f * UPS_SET);
	
	public Bomb(int width, int height) {
		this(0, 0, width, height, 0, 0);
	}
	
	public Bomb(int x, int y, int width, int height) {
		this(x, y, width, height, 0, 0);
	}

	public Bomb(int x, int y, int width, int height, int offsetX, int offsetY) {
		super(x, y, width, height, offsetX, offsetY);
		igniteTime = System.currentTimeMillis();
		bombImg = GetSprite("Bomb");
		sparkImg = GetSprite("Bomb_Spark");
		shadowImg = GetSprite("Shadow");
	}
	
	public void update() {
		if (!blasted) {
			if (System.currentTimeMillis() - igniteTime >= timer) {
				blast();
			}
			updateAnimationTick();
		}
	}
	
	private void updateAnimationTick() {
		animationTick++;
		if (animationTick >= stepTime) {
			animationTick -= stepTime;
			sparkImg = GetRotatedImage(sparkImg, 30);
		}
	}
	
	public void render(Graphics g) {
		if (!blasted) {
			g.drawImage(shadowImg, x, (int) (y + height/2.1), width, height, null);
			g.drawImage(bombImg, x, y, width, height, null);
			g.drawImage(sparkImg, x+width/2, y-height/2, width, height, null);
		}
	}
	
	private void blast() {
		// Do blast animation, destroy spikes, enemy, player in range.
		blasted = true;
	}

}
