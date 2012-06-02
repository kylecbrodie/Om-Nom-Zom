package game.math;

/**
 * Helper class for defining and interacting and an 8 directional movement
 * system
 * 
 * @author Kyle Brodie
 * 
 */
public final class Facing {
	
	public static final int NORTH = 0;
	public static final int NORTHEAST = 1;
	public static final int EAST = 2;
	public static final int SOUTHEAST = 3;
	public static final int SOUTH = 4;
	public static final int SOUTHWEST = 5;
	public static final int WEST = 6;
	public static final int NORTHWEST = 7;
	
	/**
	 * Returns a unit vector in the specified direction
	 * 
	 * @param dir
	 *            direction of the vector as defined in this class's constants
	 * @return a vector or null
	 */
	public final static Vector2d getVector(int dir) {
		return getVector(dir, 1.0);
	}

	/**
	 * Returns a vector of the specified length in the specified direction
	 * 
	 * @param dir
	 *            direction of the vector as defined in this class's constants
	 * @param length
	 *            length of the vector
	 * @return a vector or null
	 */
	public final static Vector2d getVector(int dir, double length) {
		switch (dir) {
			case NORTH:
				return new Vector2d(0, -length);
			case NORTHEAST:
				return new Vector2d(+length, -length);
			case EAST:
				return new Vector2d(+length, 0);
			case SOUTHEAST:
				return new Vector2d(+length, +length);
			case SOUTH:
				return new Vector2d(0, +length);
			case SOUTHWEST:
				return new Vector2d(-length, +length);
			case WEST:
				return new Vector2d(-length, 0);
			case NORTHWEST:
				return new Vector2d(-length, -length);
		}
		return null;
	}
}