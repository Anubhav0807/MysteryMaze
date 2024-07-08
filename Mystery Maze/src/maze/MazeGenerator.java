package maze;

import static utilz.Constants.SizeConsts.*;
import static utilz.Constants.MapConsts.*;
import static utilz.LoadImage.GetSprite;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import entities.Enemy;
import entities.Player;

public class MazeGenerator {
	
	private int[][] maze;
	private int[] oddRow, oddCol;
	private Random random;
	
	private Vector2 doorPos;
	private Vector2 keyPos;
	private Vector2 treasurePos;
	private Vector2 playerPos;
	
	private BufferedImage wallImg1;
	private BufferedImage wallImg2;
	private BufferedImage pathImg;
	private BufferedImage spikeImg;
	private BufferedImage doorImg;
	private BufferedImage keyImg;
	private BufferedImage shadowImg;
	private BufferedImage chestImg;
	private BufferedImage chestOpenImg;
	private BufferedImage coinImg;
	
	private Player player;
	private ArrayList<Enemy> enemies;
	
	public MazeGenerator(Player player) {
		this.player = player;
		enemies = new ArrayList<Enemy>();
		
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
		
		generateMaze();
		
		wallImg1 = GetSprite("Tile1");
		wallImg2 = GetSprite("Tile2");
		pathImg = GetSprite("Tile3");
		spikeImg = GetSprite("Obstacle");
		doorImg = GetSprite("Door");
		keyImg = GetSprite("Key");
		shadowImg = GetSprite("Shadow");
		chestImg = GetSprite("Chest");
		chestOpenImg = GetSprite("Chest_Open");
		coinImg = GetSprite("Coin");
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
		
		// THE ORDER MATTERS!!!
		generateDoor(); // Door acts as the starting point of path generation
		generatePath(doorPos);
		generateSpikes();
		generateKey();
		generateTreasure();
		
		set(doorPos, DOOR);
		set(keyPos, KEY);
		set(treasurePos, TREASURE);
		
		generateCoins();
		spawnPlayer();
		spawnEnemies();
		player.setPosition(playerPos.x * TILE_SIZE + 2, playerPos.y * TILE_SIZE + 2);
	}
	
	private void generatePath(Vector2 curPos) {
		if (!isEmpty(curPos)) {
			return;
		}
		set(curPos, PATH);
		
		final Vector2 up    = new Vector2(curPos.x, curPos.y+2);
		final Vector2 left  = new Vector2(curPos.x-2, curPos.y);
		final Vector2 down  = new Vector2(curPos.x, curPos.y-2);
		final Vector2 right = new Vector2(curPos.x+2, curPos.y);		
		
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
		for (int i=0; i<NO_OF_SPIKES; i++) {
			spikePos = getRandomTileOnPath();
			set(spikePos, SPIKE);
		}
	}
	
	private void generateDoor() {
		doorPos = getRandomTile();
	}
	
	private void generateKey() {
		do {
			keyPos = getRandomTileOnPath();
		} while (keyPos.distance(doorPos) < TILES_IN_WIDTH / 2);
	}
	
	private void generateTreasure() {
		do {
			treasurePos = getRandomTileOnPath();
		} while ((treasurePos.distance(doorPos) < TILES_IN_HEIGHT / 2 || 
				treasurePos.distance(keyPos) < TILES_IN_HEIGHT / 2));
	}
	
	private void spawnPlayer() {
		do {
			playerPos = getRandomTileOnPath();
		} while ((playerPos.distance(treasurePos) < TILES_IN_HEIGHT / 2 || 
				playerPos.distance(keyPos) < TILES_IN_HEIGHT / 2));
	}
	
	private void spawnEnemies() {
		Vector2 enemyPos;
		enemies.clear(); // In case of restart
		for (int i=0; i<NO_OF_ENEMIES; i++) {
			do {
				enemyPos = getRandomTileOnPath();
			} while (enemyPos.distance(playerPos) < 5);
			enemies.add(new Enemy(enemyPos.x*TILE_SIZE+2, enemyPos.y*TILE_SIZE+2, TILE_SIZE-4, TILE_SIZE-4, this));
		}
	}
	
	private void generateCoins() {
		Vector2 coinPos = getRandomTileOnPath();
		int coinsInGrp = 0;
		
		for (int i=0; i<NO_OF_COINS; i++) {			
			if ((coinsInGrp >= 3+random.nextInt(3)) ) {
				coinPos = getRandomTileOnPath();
				coinsInGrp = 0;
			}
			
			set(coinPos, COIN);
			coinsInGrp++;
			
			if (maze[coinPos.x][coinPos.y-1] == PATH) {        // Up
				coinPos = new Vector2(coinPos.x, coinPos.y-1);
			} else if (maze[coinPos.x-1][coinPos.y] == PATH) { // Left
				coinPos = new Vector2(coinPos.x-1, coinPos.y);
			} else if (maze[coinPos.x][coinPos.y+1] == PATH) { // Down
				coinPos = new Vector2(coinPos.x, coinPos.y+1);
			} else if (maze[coinPos.x+1][coinPos.y] == PATH) { // Right
				coinPos = new Vector2(coinPos.x+1, coinPos.y);
			} else {
				// If no adjacent PATH found, start a new group
				coinPos = getRandomTileOnPath();
				coinsInGrp = 0;
			}
		}
	}
	
	private Vector2 getRandomTile() {
		return new Vector2(oddRow[random.nextInt(oddRow.length)], oddCol[random.nextInt(oddCol.length)]);
	}
	
	private Vector2 getRandomTileOnPath() {
		Vector2 randomPos;
		do {
			randomPos = getRandomTile();
		} while (get(randomPos) != PATH);
		return randomPos;
	}
	
	public int[][] getMaze() {
		return maze;
	}
	
	public Player gePlayer() {
		return player;
	}
	
	public ArrayList<Enemy> getEnemies() {
		return enemies;
	}
	
	public void update() {
		for (int i=0; i < enemies.size(); i++) {
			enemies.get(i).update();
			if (enemies.get(i).toBeDeleted) enemies.remove(i);
		}
	}
	
	public void render(Graphics g) {
		for (int x=0; x<TILES_IN_WIDTH; x++) {
			for (int y=0; y<TILES_IN_HEIGHT; y++) {
				switch (maze[x][y]) {
				case WALL:
					if (y == TILES_IN_HEIGHT-1 || maze[x][y+1] != WALL) {
						g.drawImage(wallImg2, x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
					} else {
						g.drawImage(wallImg1, x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
					}
					break;
				case SPIKE:
					g.drawImage(pathImg, x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
					g.drawImage(spikeImg, x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
					break;
				case PATH:
					g.drawImage(pathImg, x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
					break;
				case DOOR:
					g.drawImage(doorImg, x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
					break;
				case KEY:
					g.drawImage(pathImg, x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
					if (!player.isKeyCollected) {
						g.drawImage(shadowImg, x*TILE_SIZE, (int) (y*TILE_SIZE + TILE_SIZE/2.5), TILE_SIZE, TILE_SIZE, null);
						g.drawImage(keyImg, x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
					}
					break;
				case TREASURE:
					g.drawImage(pathImg, x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
					if (player.isChestOpened) {
						g.drawImage(chestOpenImg, x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
					} else {
						g.drawImage(chestImg, x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
					}
					break;
				case COIN:
					g.drawImage(pathImg, x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
					g.drawImage(shadowImg, (int)(x*TILE_SIZE + TILE_SIZE*0.188), (int) (y*TILE_SIZE + TILE_SIZE/2.8), (int)(TILE_SIZE/1.5), TILE_SIZE, null);
					g.drawImage(coinImg, x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
					break;
				}
			}
		}		
	}
	
	public void renderEnemies(Graphics g) {
		for (Enemy enemy: enemies) {
			enemy.render(g);
		}
	}
	
	public void printMaze() {
		for (int y=0; y < TILES_IN_HEIGHT; y++) {
			System.out.print("{");
			for (int x=0; x < TILES_IN_WIDTH; x++) {
				System.out.print(maze[x][y] + ", ");
			}
			System.out.println("},");
		}
		System.out.println("[Note: Maze is stored as the transpose of the above matrix.]");
	}
	
	private boolean isEmpty(Vector2 pos) {
		return maze[pos.x][pos.y] == EMPTY;
	}
	
	public boolean isPath(Vector2 pos) {
		return get(pos) != WALL && get(pos) != SPIKE;
	}
	
	private int get(Vector2 pos) {
		return maze[pos.x][pos.y];
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
