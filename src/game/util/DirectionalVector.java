package game.util;

public class DirectionalVector extends UnitVector {
	
	public enum Direction { NORTH((byte)0,(byte)-1),
							SOUTH((byte)0,(byte)1),
							EAST((byte)-1,(byte)0),
							WEST((byte)1,(byte)0),
							NORTHEAST((byte)1,(byte)-1),
							NORTHWEST((byte)-1,(byte)-1),
							SOUTHEAST((byte)1,(byte)1),
							SOUTHWEST((byte)-1,(byte)1),
							NONE((byte)0,(byte)0);
		byte x;
		byte y;
		Direction opp;
		Direction(byte x, byte y) {
			this.x = x;
			this.y = y;
		}
	};
	
	static {
		Direction.NORTH.opp = Direction.SOUTH;
		Direction.SOUTH.opp = Direction.NORTH;
		Direction.EAST.opp = Direction.WEST;
		Direction.WEST.opp = Direction.EAST;
		Direction.NORTHEAST.opp = Direction.SOUTHWEST;
		Direction.NORTHWEST.opp = Direction.SOUTHEAST;
		Direction.SOUTHEAST.opp = Direction.NORTHWEST;
		Direction.SOUTHWEST.opp = Direction.NORTHEAST;
		Direction.NONE.opp = Direction.NONE;
	}
	
	private Direction d;

	public DirectionalVector(Direction d) {
		super();
		setDirection(d);
	}
	
	public boolean equals(Object o) {
		if(o instanceof DirectionalVector) {
			DirectionalVector other = (DirectionalVector) o;
			if(d.equals(other.getDirection()))
				return true;
		}
		return false;
	}
	
	public boolean isDirection(Direction d) {
		return this.d.equals(d);
	}
	
	public Direction getDirection() {
		return d;
	}
	
	public void setDirection(Direction d) {
		this.d = d;
		x = d.x;
		y = d.y;
	}
	
	public void reverse() {
		setDirection(d.opp);
	}
	
	public Direction getOppositeDirection() {
		return d.opp;
	}

	public static Direction getDirection(boolean up, boolean down, boolean left, boolean right) {
		int x = 0;
		int y = 0;
		if(up)
			y--;
		if(down)
			y++;
		if(left)
			x--;
		if(right)
			x++;
		for(Direction d : Direction.values())
			if(d.x == x && d.y == y)
				return d;
		return Direction.NONE;
	}
	
	public static DirectionalVector getDirectionalVector(boolean up, boolean down, boolean left, boolean right) {
		return new DirectionalVector(getDirection(up,down,left,right));
	}
}
