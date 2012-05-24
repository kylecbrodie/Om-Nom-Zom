package game.entity.animation;

import game.gfx.Bitmap;
import game.gfx.Screen;
import game.level.tile.Tile;

public class ItemFallAnimation extends Animation {
    Bitmap fallingImage;
    boolean isHarvester = false;
    
    public ItemFallAnimation(double x, double y, Bitmap fallingImage) {
        super(x, y, 60); // @random
        this.fallingImage = fallingImage;
    }

    public void render(Screen screen) {
   
        int anim;
        anim = life * 12 / duration;
        double posY = pos.y;
        if (!isHarvester) posY += Tile.HEIGHT;
        screen.draw(fallingImage, pos.x, posY - anim*3);
        
        Tile tileBelow;
        for(int i = 1; i <= 2; i++) {
        	tileBelow = level.getTile((int)pos.x/Tile.WIDTH, (int)pos.y/Tile.WIDTH+i);
        	if (tileBelow.getName() != ""/*HoleTile.NAME*/) tileBelow.render(screen); //TODO: add hole tiles (maybe potholes)
        }
    }
    
    public void setHarvester(){
        isHarvester = true;
    }
}
