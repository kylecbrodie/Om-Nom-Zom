package game.level;

/**
 * All implementing objects are a part of the map.
 * Examples: Buildings, Tiles, Spawn Points.
 * 
 * @author Kyle Brodie
 * 
 */
public interface Mapable extends MinimapDisplayable {
	
	/**
	 * Gets the UNIQUE color with which this object will be represented with on a map.
	 * 
	 * @return RGBA int represent the UNIQUE color
	 */
	public int getMapColor();
	
	
}