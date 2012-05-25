package game.level;

import game.gfx.Bitmap;

public interface Editable {
	
	public int getColor();
	public int getMiniMapColor();
	public String getName();
	public Bitmap getBitMapForEditor();
}
