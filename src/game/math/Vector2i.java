package game.math;

public class Vector2i implements Cloneable {

		public final int x, y;

		public Vector2i() {
			x = y = 0;
		}

		public Vector2i(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Vector2i) {
				Vector2i p = (Vector2i) obj;
				return p.x == x && p.y == y;
			} else {
				return false;
			}
		}
		
		@Override
		public int hashCode() {
			int res = 17;
			res = res * 17 + ((Integer)x).hashCode();
			res = res * 17 + ((Integer)y).hashCode();
			return res;
		}

		public int distSquared(Vector2i to) {
			int xd = x - to.x;
			int yd = y - to.y;
			return xd * xd + yd * yd;
		}

		public double dist(Vector2i pos) {
			return Math.sqrt(distSquared(pos));
		}

		@Override
		public Vector2i clone() {
			return new Vector2i(x, y);
		}

		public Vector2i add(Vector2i v) {
			return new Vector2i(x + v.x, y + v.y);
		}
		
		public Vector2i add(int x, int y) {
			return new Vector2i(this.x + x, this.y + y);
		}

		public Vector2i sub(Vector2i v) {
			return new Vector2i(x - v.x, y - v.y);
		}

		public String toString() {
			return "[" + x + ", " + y + "]";
		}

		public int dot(Vector2i v) {
			return x * v.x + y * v.y;
		}

		public int lengthSquared() {
			return x * x + y * y;
		}

		public double length() {
			return Math.sqrt(lengthSquared());
		}

		public Vector2i normalize() {
			double nf = 1 / length();
			return new Vector2i((int)(x * nf), (int)(y * nf));
		}

		public Vector2i rescale(double newLen) {
			double nf = newLen / length();
			return new Vector2i((int)(x * nf), (int)(y * nf));
		}

		public Vector2i scale(double s) {
			return new Vector2i((int)(x * s), (int)(y * s));
		}

		public Vector2i mul(Vector2i v) {
			return new Vector2i(x * v.x, y * v.y);
		}
}