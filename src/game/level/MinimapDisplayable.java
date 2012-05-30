package game.level;

public interface MinimapDisplayable {
	/**
	 * Gets the average color of this object, or what color it wants to be
	 * displayed as on the minimap.
	 * 
	 * @return RGBA int representing the color
	 */
	public int getMinimapColor();
	
	/**
	 * Gets the width of this object in tiles so that its size will be displayed
	 * in the proper proportions in the minimap.
	 * 
	 * @return the width in tiles
	 */
	public int getTileWidth();
	
	/**
	 * Gets the height of this object in tiles so that its size will be displayed
	 * in the proper proportions in the minimap.
	 * 
	 * @return the height in tiles
	 */
	public int getTileHeight();
	
	/**
	 * A name with which to identify this object on the minimap.
	 * @return
	 */
	public String getName();
}