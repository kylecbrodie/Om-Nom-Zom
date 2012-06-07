package game.math;

/**
 * Helper class for defining and interacting and an 4 directional movement
 * system
 * 
 * @author Kyle Brodie
 * 
 */
public final class Direction {
	
	public static final int NORTH = 0;
	public static final int EAST = 1;
	public static final int SOUTH = 2;
	public static final int WEST = 3;
	
	/**
	 * Returns a unit vector in the specified direction
	 * 
	 * @param dir
	 *            direction of the vector as defined in this class's constants
	 * @return a vector or null
	 */
	public final static DirectionalVector getVector(int dir) {
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
	public final static DirectionalVector getVector(int dir, double length) {
		return new DirectionalVector(dir, length);
	}
	
	/**
	 * Returns the direction that is 180 degrees opposite to the one provided
	 * 
	 * @param dir
	 * @return the opposite direction or -1 if the provided dir is invalid
	 */
	public final static int getOpposite(int dir) {
		switch (dir) {
		case NORTH:
			return Direction.SOUTH;
		case EAST:
			return Direction.WEST;
		case SOUTH:
			return Direction.NORTH;
		case WEST:
			return Direction.EAST;
		}
		return -1;
	}
}