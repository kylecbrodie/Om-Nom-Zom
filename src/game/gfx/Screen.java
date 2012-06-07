package game.gfx;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Screen extends Bitmap {

	public final BufferedImage image;
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
	
	@Override
	public int getPixel(int x, int y) {
		return super.getPixel(x + xOffset, y + yOffset);
	}
	
	public void draw(Bitmap bm, double x, double y) {
		draw(bm, (int) x, (int) y);
	}
	
	@Override
	public void draw(Bitmap bm, int x, int y) {
		super.draw(bm, x + xOffset, y + yOffset);
	}
	
	@Override
	public void draw(Bitmap bm, int x, int y, int width, int height) {
		super.draw(bm, x + xOffset, y + yOffset, width, height);
	}

	public void drawColor(Bitmap bm, double x, double y, int color) {
		drawColor(bm, (int) x, (int) y, color);
	}
	
	@Override
	public void drawColor(Bitmap bm, int x, int y, int color) {
		super.drawColor(bm, x + xOffset, y + yOffset, color);
	}
	
	@Override
	public void drawAlpha(Bitmap bm, int x, int y, int alpha) {
		super.drawAlpha(bm, x + xOffset, y + yOffset, alpha);
	}
	
	@Override
	public void drawHorizonalLine(int x1, int x2, int y, int color) {
		super.drawHorizonalLine(x1 + xOffset, x2 + xOffset, y + yOffset, color);
	}
	
	@Override
	public void drawRect(int x, int y, int bw, int bh, int color) {
		super.drawRect(x + xOffset, y + yOffset, bw, bh, color);
	}
	
	@Override
	public void drawCircle(int centerX, int centerY, int radius, int color) {
		super.drawCircle(centerX + xOffset, centerY + yOffset, radius, color);
	}
	
	@Override
	public void fill(int x, int y, int width, int height, int color) {
		super.fill(x + xOffset, y + yOffset, width, height, color);
	}
	
	@Override
	public void fillAlpha(int x, int y, int width, int height, int color, int alpha) {
		super.fillAlpha(x + xOffset, y + yOffset, width, height, color, alpha);
	}
	
	@Override
	public void fillCircle(int centerX, int centerY, int radius, int color) {
		super.fillCircle(centerX + xOffset, centerY + yOffset, radius, color);
	}

	public Bitmap rectangleBitmap(int w, int h, int color) {
		Bitmap rect = new Bitmap(w, h);
		rect.drawRect(0, 0, w, h, color);
		return rect;
	}

	public Bitmap tooltipBitmap(int width, int height) {
		int cRadius = 3;
		int color = Color.black.getRGB();
		Bitmap tooltip = new Bitmap(width + 3, height + 3);
		tooltip.fill(0, cRadius, width, height - 2 * cRadius, color);
		tooltip.fill(cRadius, 0, width - 2 * cRadius, height, color);
		// draw corner circles (clockwise, starting at top-left)
		tooltip.fillCircle(cRadius, cRadius, cRadius, color);
		tooltip.fillCircle(width - cRadius - 1, cRadius, cRadius, color);
		tooltip.fillCircle(width - cRadius - 1, height - cRadius - 1, cRadius, color);
		tooltip.fillCircle(cRadius, height - cRadius - 1, cRadius, color);

		return tooltip;
	}
}