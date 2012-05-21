package game.gfx;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Screen extends Sprite {
	
    public BufferedImage image;
    private int xOffset, yOffset;

    public Screen(int w, int h) {
        super(w, h);
        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    }

    public void setOffset(int xOffset, int yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public void blit(Sprite sprite, double x, double y) {
        blit(sprite, (int) x, (int) y);
    }

    public void blit(Sprite sprite, int x, int y) {
        super.blit(sprite, x + xOffset, y + yOffset);
    }

    public void blit(Sprite sprite, int x, int y, int w, int h) {
        super.blit(sprite, x + xOffset, y + yOffset, w, h);
    }

    public void colorBlit(Sprite sprite, double x, double y, int color) {
        colorBlit(sprite, (int) x, (int) y, color);
    }

    public void colorBlit(Sprite sprite, int x, int y, int color) {
        super.colorBlit(sprite, x + xOffset, y + yOffset, color);
    }

    public void fill(int x, int y, int width, int height, int color) {
        super.fill(x + xOffset, y + yOffset, width, height, color);
    }
}