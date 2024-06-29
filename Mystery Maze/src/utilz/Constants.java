package utilz;

public class Constants {
	
	public static class GameConsts {
		public static final int FPS_SET = 120;
		public static final int UPS_SET =  60;
	}
	
	public static class SizeConsts {
		public static final float SCALE = 1.25f;
		public static final int TILES_DEFAULT_SIZE = 32;
		public static final int TILES_IN_WIDTH  = 27; // Must be odd
		public static final int TILES_IN_HEIGHT = 15; // Must be odd
		public static final int TILE_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
		public static final int GAME_WIDTH = TILE_SIZE * TILES_IN_WIDTH - 2;
		public static final int GAME_HEIGHT = TILE_SIZE * TILES_IN_HEIGHT - 2;
	}
	
	public static class MapConsts {
		public static final int EMPTY = -1;
		public static final int PATH = 0;
		public static final int WALL = 1;
		public static final int SPIKE = 2;
		public static final int DOOR = 3;
		public static final int KEY = 4;
		public static final int TREASURE = 5;
		
		public static final float SPIKE_PROBABILITY = 0.02f;
	}

}
