package utilz;

public class Constants {
	
	public static class GameConsts {
		public static final int FPS_SET = 90; // Minimum: 10 FPS
		public static final int UPS_SET = 50; // Minimum: 30 UPS
	}
	
	public static class SizeConsts {
		public static final float SCALE = 1.0f; // SCALE * moveSpeed must result in a whole number
		public static final int TILES_DEFAULT_SIZE = 32;
		public static final int TILES_IN_WIDTH  = 27; // Must be odd
		public static final int TILES_IN_HEIGHT = 15; // Must be odd
		public static final int TILE_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
		public static final int GAME_WIDTH = TILE_SIZE * TILES_IN_WIDTH;
		public static final int GAME_HEIGHT = (int) (TILE_SIZE * (TILES_IN_HEIGHT+1.5f));
	}
	
	public static class MapConsts {
		public static final int EMPTY = -1;
		public static final int PATH = 0;
		public static final int WALL = 1;
		public static final int SPIKE = 2;
		public static final int DOOR = 3;
		public static final int KEY = 4;
		public static final int TREASURE = 5;
		public static final int COIN = 6;		
		public static final int PLAYER = 7;
		public static final int ENEMY = 8;
		
		public static final int NO_OF_SPIKES = 6;
		public static final int NO_OF_COINS = 30;
		public static final int NO_OF_ENEMIES = 5;
	}

}
