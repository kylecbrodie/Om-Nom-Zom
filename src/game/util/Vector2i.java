package game.util;

public class Vector2i {
	
	public int x,y;
	
	public Vector2i(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public static Vector2i add(Vector2i v1, Vector2i v2) {
		return new Vector2i(v1.x + v2.x,v1.y + v2.y);
	}

	public void add(Vector2i v) {
		x += v.x;
		y += v.y;
	}

	public void sub(Vector2i v) {
		x -= v.x;
		y -= v.y;
	}

	public void dot(Vector2i v) {
		x *= v.x;
		y *= v.y;
	}
}
