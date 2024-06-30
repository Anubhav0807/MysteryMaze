package utilz;

public class HelpMethods {
	
	public static boolean CanMoveHere(int x, int y, int width, int height, int[][] map) {
		if (!IsSolid(x, y, map)) // Top left
			if (!IsSolid(x + width, y + height, map)) // Bottom right
				if (!IsSolid(x + width, y, map)) // Top right
					if (!IsSolid(x, y + height, map)) // Bottom left
						return true;
		return false;
	}
	
	public static boolean IsSolid(int x, int y, int[][] map) {
		int xIdx = x / Constants.SizeConsts.TILE_SIZE;
		int yIdx = y / Constants.SizeConsts.TILE_SIZE;
		
		int value = map[xIdx][yIdx];
		return (value == Constants.MapConsts.WALL);
	}

}
