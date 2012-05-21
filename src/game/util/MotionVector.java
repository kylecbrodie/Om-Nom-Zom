package game.util;

//import game.Game;
import game.util.DirectionalVector.Direction;

import java.awt.Point;

public class MotionVector {
	
	public Vector2i pos;
	public DirectionalVector dir;
	private DirectionalVector nextDir;
	public Vector2f vel;
	public Vector2f speed;
	public Vector2d accel;
	
	//private double autoDeccelTime;
	//private boolean decelerating,autoDecelerating;
	
	private int X_BOUNDS,Y_BOUNDS,xOffset,yOffset;
	
	public MotionVector() {
		pos = new Vector2i(0,0);
		vel = new Vector2f(0.0f,0.0f);
		speed = new Vector2f(0.0f,0.0f);
		accel = new Vector2d(0.0,0.0);
		dir = new DirectionalVector(DirectionalVector.Direction.NONE);
		nextDir = new DirectionalVector(DirectionalVector.Direction.NONE);
	}
	
	public void tick() {
		/*if(!decelerating)
			dir.setDirection(nextDir.getDirection());
		if(decelerating) {
			if(vel.dx <= 0 || vel.dy <= 0) {
				vel.dx = speed.dx;
				vel.dy = speed.dy;
				accel.dx = 0;
				accel.dy = 0;
				dir.reverse();
				decelerating = false;
			}
		} else if(autoDecelerating)
			if(dir.isDirection(DirectionalVector.Direction.NONE))
				deccelerate(autoDeccelTime);
		*/
		dir = nextDir;
		moveInBounds();
	}
	
	public void moveInBounds() {
		vel.dx += accel.dx;
		vel.dy += accel.dy;
		pos.x += Math.abs(vel.dx) * dir.x;
		pos.y += Math.abs(vel.dy) * dir.y;
		
		if(pos.x < 0)
			pos.x = 0;
		if(pos.x + xOffset > X_BOUNDS)
			pos.x = X_BOUNDS - xOffset;
		if(pos.y < 0)
			pos.y = 0;
		if(pos.y + yOffset > Y_BOUNDS)
			pos.y = Y_BOUNDS - yOffset;
	}
	
	/*public void autoDeccelerate(double seconds) {
		autoDeccelTime = seconds;
		autoDecelerating = true;
	}
	
	public void deccelerate(double seconds) {
		accel.dx = -(vel.dx / Game.TICKS_PER_SEC * seconds);
		accel.dy = -(vel.dy / Game.TICKS_PER_SEC * seconds);
		decelerating = true;
	}*/
	
	public void reverse() {
		dir.reverse();
	}
	
	public void setSpeed(float dx, float dy) {
		speed.dx = dx;
		speed.dy = dy;
		vel.dx = dx;
		vel.dy = dy;
	}
	
	public void setSpeed(Vector2f vel) {
		speed.dx = vel.dx;
		speed.dy = vel.dy;
	}
	
	public void setDirection(Direction d) {
		dir.setDirection(d);
	}
	
	public void setNextDirection(Direction d) {
		nextDir.setDirection(d);
	}
	
	public void setNextDirection(DirectionalVector d) {
		nextDir = d;
	}
	
	public void setDirection(DirectionalVector d) {
		dir = d;
	}
	
	public void setAcceleration(double dx, double dy) {
		accel.dx = dx;
		accel.dy = dy;
	}
	
	public void setAcceleration(Vector2d acc) {
		accel = acc;
	}
	
	public void setVelocity(float dx, float dy) {
		vel.dx = dx;
		vel.dy = dy;
	}
	
	public void setVelocity(Vector2f vel) {
		this.vel = vel;
	}
	
	public void setPosition(int x, int y) {
		
	}
	
	public void setPosition(Vector2i pos) {
		this.pos = pos;
	}

	public void setPosition(Point p) {
		pos.x = p.x;
		pos.y = p.y;
	}

	public void setBounds(int width, int height) {
		X_BOUNDS = width;
		Y_BOUNDS = height;
	}
	
	public void setOffset(int x, int y) {
		xOffset = x;
		yOffset = y;
	}
}