package entities;

import static utilz.Constants.SizeConsts.TILE_SIZE;

public abstract class Entity {

	protected int x, y;
	protected int xIdx, yIdx;
	protected int width, height;

	public Entity(int width, int height) {
		this(0, 0, width, height);
	}
	
	public Entity(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		updateIndex();
	}
	
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
		
		updateIndex();
	}
	
	public void update() {
		updateIndex();
	}
	
	public boolean onSingleTile() {
		return (xIdx * TILE_SIZE <= x && yIdx * TILE_SIZE <= y && (x+width) < (xIdx+1) * TILE_SIZE && (y+height) < (yIdx+1) * TILE_SIZE);
	}
	
	private void updateIndex() {
		xIdx = Math.round((float)x / TILE_SIZE);
		yIdx = Math.round((float)y / TILE_SIZE);
	}

}
