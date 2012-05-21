package game.util;

public class Vector2f {
	
	public float dx,dy;
	
	public Vector2f(float x, float y) {
		dx = x;
		dy = y;
	}

	public static Vector2f add(Vector2f v1, Vector2f v2) {
		return new Vector2f(v1.dx + v2.dx,v1.dy + v2.dy);
	}

	public void add(Vector2f v) {
		dx += v.dx;
		dy += v.dy;
	}

	public void sub(Vector2f v) {
		dx -= v.dx;
		dy -= v.dy;
	}

	public void dot(Vector2f v) {
		dx *= v.dx;
		dy *= v.dy;
	}
}
