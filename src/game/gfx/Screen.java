package game.gfx;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Screen extends Bitmap {

	public BufferedImage image;
	protected int xOffset, yOffset;

	public Screen(int w, int h) {
		super(w, h);
		image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	}

	public void setOffset(int xOffset, int yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

	public void draw(Bitmap bm, double x, double y) {
		draw(bm, (int) x, (int) y);
	}

	public void draw(Bitmap bitmap, int x, int y) {
		super.draw(bitmap, x + xOffset, y + yOffset);
	}

	public void draw(Bitmap bitmap, int x, int y, int w, int h) {
		super.draw(bitmap, x + xOffset, y + yOffset, w, h);
	}

	public void drawAlpha(Bitmap bitmap, int x, int y, int alpha) {
		super.drawAlpha(bitmap, x + xOffset, y + yOffset, alpha);
	}

	public void drawColor(Bitmap bitmap, double x, double y, int color) {
		drawColor(bitmap, (int) x, (int) y, color);
	}

	public void drawColor(Bitmap bitmap, int x, int y, int color) {
		super.drawColor(bitmap, x + xOffset, y + yOffset, color);
	}

	public void fill(int x, int y, int width, int height, int color) {
		super.fill(x + xOffset, y + yOffset, width, height, color);
	}

	public void drawRect(int x, int y, int width, int height, int color) {
		super.drawRect(x + xOffset, y + yOffset, width, height, color);
	}

	public Bitmap rectangleBitmap(int x, int y, int x2, int y2, int color) {
		Bitmap rect = new Bitmap(x2, y2);
		rect.drawRect(x, y, x2, y2, color);
		return rect;
	}

	public Bitmap rangeBitmap(int radius, int color) {
		Bitmap circle = new Bitmap(radius * 2 + 100, radius * 2 + 100);

		circle.fillCircle(radius, radius, radius, color);
		return circle;
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