package maze;

import static utilz.Constants.SizeConsts.*;

public class Vector2 {
	
	public int x, y;
	
	public Vector2(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public boolean isValid() {
		if (1 <= x && x <= TILES_IN_WIDTH-2 && 1 <= y && y <= TILES_IN_HEIGHT-2) {
			return true;
		}
		return false;
	}
	
	public double distance(Vector2 other) {
		return Math.sqrt(sq(x - other.x) + sq(y - other.y));
	}
	
	@Override
	public String toString() {
		return "Vector(" + x + ", " + y + ")";
	}
	
	@Override
	public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vector2 vector2 = (Vector2) obj;
        return x == vector2.x && y == vector2.y;
    }
	
	private int sq(int val) {
		return val * val;
	}

}
