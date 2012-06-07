package game.math;

public class Vector2d implements Cloneable {

		public final double x, y;

		public Vector2d() {
			x = y = 0;
		}

		public Vector2d(double x, double y) {
			this.x = x;
			this.y = y;
			validate();
		}

		public Vector2d floor() {
			return new Vector2d(Math.floor(x), Math.floor(y));
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Vector2d) {
				Vector2d p = (Vector2d) obj;
				return p.x == x && p.y == y;
			} else {
				return false;
			}
		}
		
		@Override
		public int hashCode() {
			int res = 17;
			res = res * 17 + ((Double)x).hashCode();
			res = res * 17 + ((Double)y).hashCode();
			return res;
		}

		public double distSquared(Vector2d to) {
			double xd = x - to.x;
			double yd = y - to.y;
			return xd * xd + yd * yd;
		}

		public double dist(Vector2d pos) {
			return Math.sqrt(distSquared(pos));
		}

		@Override
		public Vector2d clone() {
			return new Vector2d(x, y);
		}

		public Vector2d add(Vector2d v) {
			return new Vector2d(x + v.x, y + v.y);
		}
		
		public Vector2d add(double x, double y) {
			return new Vector2d(this.x + x, this.y + y);
		}

		public Vector2d sub(Vector2d v) {
			return new Vector2d(x - v.x, y - v.y);
		}

		public String toString() {
			return "[" + x + ", " + y + "]";
		}

		public double dot(Vector2d v) {
			return x * v.x + y * v.y;
		}

		public double lengthSquared() {
			return x * x + y * y;
		}

		public double length() {
			return Math.sqrt(lengthSquared());
		}

		public Vector2d normalize() {
			double nf = 1 / length();
			return new Vector2d(x * nf, y * nf);
		}

		public Vector2d rescale(double newLen) {
			double nf = newLen / length();
			return new Vector2d(x * nf, y * nf);
		}

		public Vector2d scale(double s) {
			return new Vector2d(x * s, y * s);
		}

		public Vector2d mul(Vector2d v) {
			return new Vector2d(x * v.x, y * v.y);
		}

		public void validate() {
			if (Double.isInfinite(x) || Double.isInfinite(y) || Double.isNaN(x) || Double.isNaN(y)) {
				System.err.println("Invaild Vector: " + toString());
			}
		}

}
