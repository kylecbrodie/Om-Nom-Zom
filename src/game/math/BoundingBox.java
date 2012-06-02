package game.math;

/**
 * Handles collision detection
 * @Immutable
 * 
 * @author Kyle Brodie
 *
 */
public final class BoundingBox {
	
	public final double x0, y0;
	public final double x1, y1;
	public final BBOwner owner;

	public BoundingBox(BBOwner owner, double x0, double y0, double x1, double y1) {
		this.owner = owner;
		this.x0 = x0;
		this.y0 = y0;
		this.x1 = x1;
		this.y1 = y1;
	}
	
	public boolean intersects(BoundingBox bb) {
		if (bb.x0 >= x1 || bb.y0 >= y1 || bb.x1 <= x0 || bb.y1 <= y0) {
			return false;
		}
		return true;
	}

	public boolean intersects(double xx0, double yy0, double xx1, double yy1) {
		if (xx0 >= x1 || yy0 >= y1 || xx1 <= x0 || yy1 <= y0) {
			return false;
		}
		return true;
	}
	
	/**
	 * grows the BoundingBox by increases each side length by 2 * s
	 * @param s
	 * @return a larger boundingbox
	 */
	public BoundingBox grow(double s) {
		return new BoundingBox(owner, x0 - s, y0 - s, x1 + s, y1 + s);
	}
}