package game.math;

public class DirectionalVector {
	
	public int dir = Direction.NORTH;
	public double vel = 0.0;
	
	public DirectionalVector(int direction, double velocity) {
		dir = direction;
		vel = velocity;
	}
	
	public void setDirection(int direction) {
		dir = direction;
	}
	
	public void setVelocity(double velocity) {
		vel = velocity;
	}
	
	public double getX() {
		switch (dir) {
		case Direction.NORTH:
			return 0;
		case Direction.EAST:
			return +vel;
		case Direction.SOUTH:
			return 0;
		case Direction.WEST:
			return -vel;
		}
		return Double.NaN;
	}
	
	public double getY() {
		switch (dir) {
		case Direction.NORTH:
			return -vel;
		case Direction.EAST:
			return 0;
		case Direction.SOUTH:
			return +vel;
		case Direction.WEST:
			return 0;
		}
		return Double.NaN;
	}
	
	/**
	 * Returns the offset the denotes the direction of this vector or
	 * Integer.MIN_NUMBER if this vector has an invalid direction.
	 * 
	 * @return ^ see above
	 */
	public int getXOffset() {
		switch (dir) {
		case Direction.NORTH:
			return 0;
		case Direction.EAST:
			return +1;
		case Direction.SOUTH:
			return 0;
		case Direction.WEST:
			return -1;
		}
		return Integer.MIN_VALUE;
	}
	
	/**
	 * Returns the offset the denotes the direction of this vector or
	 * Integer.MIN_NUMBER if this vector has an invalid direction.
	 * 
	 * @return ^ see above
	 */
	public int getYOffset() {
		switch (dir) {
		case Direction.NORTH:
			return -1;
		case Direction.EAST:
			return 0;
		case Direction.SOUTH:
			return +1;
		case Direction.WEST:
			return 0;
		}
		return Integer.MIN_VALUE;
	}
}