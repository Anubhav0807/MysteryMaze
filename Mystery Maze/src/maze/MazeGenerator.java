package maze;

import static utilz.Constants.SizeConsts.*;
import static utilz.Constants.MapConsts.*;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class MazeGenerator {
	
	private int[][] maze;
	private int[] oddRow, oddCol;
	private Random random;
	
	private Vector2 doorPos;
	private Vector2 keyPos;
	private Vector2 treasurePos;
	
	public MazeGenerator() {
		maze = new int[TILES_IN_WIDTH][TILES_IN_HEIGHT];
		oddRow = new int[TILES_IN_WIDTH  / 2];
		oddCol = new int[TILES_IN_HEIGHT / 2];
		random = new Random();
		
		for (int i=0, j=1; j<TILES_IN_WIDTH ; i++, j+=2) {
			oddRow[i] = j;
		}
		for (int i=0, j=1; j<TILES_IN_HEIGHT; i++, j+=2) {
			oddCol[i] = j;
		}
	}
	
	public void generateMaze() {
		
		for (int x=0; x<TILES_IN_WIDTH; x++) {
			for (int y=0; y<TILES_IN_HEIGHT; y++) {
				if (x==0 || x==TILES_IN_WIDTH-1 || y==0 || y==TILES_IN_HEIGHT-1) {
					maze[x][y] = 1;
				} else if (x%2==0 || y%2==0) {
					maze[x][y] = 1;
				} else {
					maze[x][y] = EMPTY;
				}
			}
		}
		
		generateDoor(); // Door acts as the starting point of path generation
		generatePath(doorPos);
		generateSpikes();
		generateKey();
		generateTreasure();
		
		set(doorPos, DOOR);
		set(keyPos, KEY);
		set(treasurePos, TREASURE);
	}
	
	private void generatePath(Vector2 curPos) {
		if (!isEmpty(curPos)) {
			return;
		}
		set(curPos, PATH);
		
		Vector2 up    = new Vector2(curPos.x, curPos.y+2);
		Vector2 left  = new Vector2(curPos.x-2, curPos.y);
		Vector2 down  = new Vector2(curPos.x, curPos.y-2);
		Vector2 right = new Vector2(curPos.x+2, curPos.y);		
		
		final Vector2[] arr = new Vector2[4];
		int options;
		
		do {
		options = 0;
		if (up.isValid() && isEmpty(up)) {
			arr[options++] = up;
		}
		if (left.isValid() && isEmpty(left)) {
			arr[options++] = left;
		}
		if (down.isValid() && isEmpty(down)) {
			arr[options++] = down;
		}
		if (right.isValid() && isEmpty(right)) {
			arr[options++] = right;
		}
		
		if (options > 0) {
			int randomIdx = random.nextInt(options);
			Vector2 nextPos = arr[randomIdx];
			Vector2 midPos = new Vector2((curPos.x + nextPos.x)/2, (curPos.y + nextPos.y)/2);
			set(midPos, PATH);
			generatePath(nextPos);
			del(arr, randomIdx);
		}
		
		} while (options > 0);
	}
	
	private void generateSpikes() {
		Vector2 spikePos;
		for (int i=0; i<((TILES_IN_WIDTH-2) * (TILES_IN_HEIGHT-2) * SPIKE_PROBABILITY); i++) {
			spikePos = getRandomTile();
			set(spikePos, SPIKE);
		}
	}
	
	private void generateDoor() {
		doorPos = getRandomTile();
	}
	
	private void generateKey() {
		do {
			keyPos = getRandomTile();
		} while (keyPos.distance(doorPos) < TILES_IN_WIDTH / 2);
	}
	
	private void generateTreasure() {
		do {
			treasurePos = getRandomTile();
		} while (treasurePos.distance(doorPos) < TILES_IN_HEIGHT / 2 || treasurePos.distance(keyPos) < TILES_IN_HEIGHT / 2);
	}
	
	private Vector2 getRandomTile() {
		return new Vector2(oddRow[random.nextInt(oddRow.length)], oddCol[random.nextInt(oddCol.length)]);
	}
	
	public int[][] getMaze() {
		return maze;
	}
	
	public void render(Graphics g) {
		for (int x=0; x<TILES_IN_WIDTH; x++) {
			for (int y=0; y<TILES_IN_HEIGHT; y++) {
				switch (maze[x][y]) {
				case WALL:
					g.setColor(new Color(0, 0, 0));
					g.fillRect(x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE-2, TILE_SIZE-2);
					break;
				case SPIKE:
					g.setColor(new Color(255, 0, 0));
					g.fillRect(x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE-2, TILE_SIZE-2);
					break;
				case DOOR:
					g.setColor(new Color(150, 75, 0));
					g.fillRect(x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE-2, TILE_SIZE-2);
					break;
				case KEY:
					g.setColor(new Color(255, 215, 0));
					g.fillRect(x*TILE_SIZE, y*TILE_SIZE + TILE_SIZE/2, TILE_SIZE-2, TILE_SIZE/4);
					break;
				case TREASURE:
					g.setColor(new Color(255, 215, 0));
					g.fillRect(x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE-2, TILE_SIZE-2);
					break;
				}
			}
		}
	}
	
	private boolean isEmpty(Vector2 pos) {
		return maze[pos.x][pos.y] == EMPTY;
	}
	
	private void set(Vector2 pos, int val) {
		maze[pos.x][pos.y] = val;
	}
	
	private void del(Vector2[] arr, int idx) {
		// Left shift
		for (int i=idx; i<arr.length-1; i++) {
			arr[i] = arr[i+1];
		}
		arr[arr.length-1] = null;
	}

}
