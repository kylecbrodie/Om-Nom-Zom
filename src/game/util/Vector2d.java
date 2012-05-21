package game.util;

public class Vector2d {
	
	public double dx,dy;
	
	public Vector2d(double x, double y) {
		dx = x;
		dy = y;
	}

	public static Vector2d add(Vector2d v1, Vector2d v2) {
		return new Vector2d(v1.dx + v2.dx,v1.dy + v2.dy);
	}

	public void add(Vector2d v) {
		dx += v.dx;
		dy += v.dy;
	}

	public void sub(Vector2d v) {
		dx -= v.dx;
		dy -= v.dy;
	}

	public void dot(Vector2d v) {
		dx *= v.dx;
		dy *= v.dy;
	}
}
