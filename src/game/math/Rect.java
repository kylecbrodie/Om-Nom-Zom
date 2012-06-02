package game.math;

/**
 * A very barebones rectangle for use in defining draw areas.
 * 
 * @author Kyle Brodie
 * 
 */
public class Rect {
	
	/**
	 * Top left point of the rectangle.
	 */
	public int x0, y0;
	
	/**
	 * Bottom right point of the rectangle.
	 */
	public int x1, y1;
	
	public int width, height;
	
	public Rect(int x, int y, int w, int h) {
		x0 = x;
		y0 = y;
		width = w;
		height = h;
		x1 = x + w;
		y1 = y + h;
	}
}