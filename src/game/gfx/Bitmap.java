package game.gfx;

import game.math.Rect;

import java.util.Arrays;

/**
 * Represents a Bitmap image (or pixel grid image) and provides methods for
 * manipulating them.
 * 
 * @author Kyle Brodie
 * @author Catacomb-Snatch Project (http://www.catacombsnatch.net/)
 * 
 */
public class Bitmap implements Cloneable {

	protected int[] pixels;
	protected int w, h;

	public Bitmap(int w, int h) {
		this.w = w;
		this.h = h;
		pixels = new int[w * h];
	}

	public Bitmap(int w, int h, int[] pixels) {
		this.w = w;
		this.h = h;
		this.pixels = pixels;
	}

	public Bitmap(int[][] pixels2D) {
		w = pixels2D.length;
		if (w > 0) {
			h = pixels2D[0].length;
			pixels = new int[w * h];
			for (int y = 0; y < h; y++) {
				for (int x = 0; x < w; x++) {
					pixels[y * w + x] = pixels2D[x][y];
				}
			}
		} else {
			h = 0;
			pixels = new int[0];
		}
	}
	
	@Override
	public Bitmap clone() {
		Bitmap copy = new Bitmap(this.w, this.h);
		copy.pixels = this.pixels.clone();
		return copy;
	}
	
	public void clear() {
		clear(0xffffffff);
	}

	public void clear(int color) {
		Arrays.fill(pixels, color);
	}
	
	public int getWidth() {
		return w;
	}

	public int getHeight() {
		return h;
	}

	public int getPixel(int i) {
		if (i >= 0 && i < pixels.length) {
			return pixels[i];
		}
		return -1;
	}

	public void setPixel(int i, int color) {
		if (i >= 0 && i < pixels.length) {
			pixels[i] = color;
		}
	}

	public int size() {
		return pixels.length;
	}
	
	private void adjustDrawArea(Rect drawArea) {

		if (drawArea.x0 < 0) {
			drawArea.x0 = 0;
		}
		if (drawArea.y0 < 0) {
			drawArea.y0 = 0;
		}
		if (drawArea.x1 > w) {
			drawArea.x1 = w;
		}
		if (drawArea.y1 > h) {
			drawArea.y1 = h;
		}
	}

	public int blendPixels(int backgroundColor, int pixelToBlendColor) {

		int alpha_blend = (pixelToBlendColor >> 24) & 0xff;

		int alpha_background = 256 - alpha_blend;

		int rr = backgroundColor & 0xff0000;
		int gg = backgroundColor & 0xff00;
		int bb = backgroundColor & 0xff;

		int r = (pixelToBlendColor & 0xff0000);
		int g = (pixelToBlendColor & 0xff00);
		int b = (pixelToBlendColor & 0xff);

		r = ((r * alpha_blend + rr * alpha_background) >> 8) & 0xff0000;
		g = ((g * alpha_blend + gg * alpha_background) >> 8) & 0xff00;
		b = ((b * alpha_blend + bb * alpha_background) >> 8) & 0xff;

		return 0xff000000 | r | g | b;
	}

	public void draw(Bitmap bm, int x, int y) {
		Rect drawArea = new Rect(x, y, bm.w, bm.h);
		adjustDrawArea(drawArea);
		int drawWidth = drawArea.x1 - drawArea.x0;

		for (int yy = drawArea.y0; yy < drawArea.y1; yy++) {
			int tp = yy * w + drawArea.x0;
			int sp = (yy - y) * bm.w + (drawArea.x0 - x);
			tp -= sp;
			for (int xx = sp; xx < sp + drawWidth; xx++) {
				int col = bm.pixels[xx];
				int alpha = (col >> 24) & 0xff;

				if (alpha == 255) {
					pixels[tp + xx] = col;
				} else {
					pixels[tp + xx] = blendPixels(pixels[tp + xx], col);
				}
			}
		}
	}

	public void draw(Bitmap bm, int x, int y, int width, int height) {
		Rect drawArea = new Rect(x, y, width, height);
		adjustDrawArea(drawArea);
		int drawWidth = drawArea.x1 - drawArea.x0;

		for (int yy = drawArea.y0; yy < drawArea.y1; yy++) {
			int tp = yy * w + drawArea.x0;
			int sp = (yy - y) * bm.w + (drawArea.x0 - x);
			tp -= sp;
			for (int xx = sp; xx < sp + drawWidth; xx++) {
				int col = bm.pixels[xx];
				int alpha = (col >> 24) & 0xff;

				if (alpha == 255) {
					pixels[tp + xx] = col;
				} else {
					pixels[tp + xx] = blendPixels(pixels[tp + xx], col);
				}
			}
		}
	}

	public void drawAlpha(Bitmap bm, int x, int y, int alpha) {
		if (alpha == 255) {
			draw(bm, x, y);
			return;
		}

		Rect drawArea = new Rect(x, y, bm.w, bm.h);
		adjustDrawArea(drawArea);

		int drawWidth = drawArea.x1 - drawArea.x0;

		for (int yy = drawArea.y0; yy < drawArea.y1; yy++) {
			int tp = yy * w + drawArea.x0;
			int sp = (yy - y) * bm.w + (drawArea.x0 - x);
			for (int xx = 0; xx < drawWidth; xx++) {
				int col = bm.pixels[sp + xx];
				if (col < 0) {

					int r = (col & 0xff0000);
					int g = (col & 0xff00);
					int b = (col & 0xff);
					col = (alpha << 24) | r | g | b;
					int color = pixels[tp + xx];
					pixels[tp + xx] = this.blendPixels(color, col);
				}
			}
		}
	}

	public void drawColor(Bitmap bm, int x, int y, int color) {
		Rect drawArea = new Rect(x, y, bm.w, bm.h);
		adjustDrawArea(drawArea);

		int drawWidth = drawArea.x1 - drawArea.x0;

		int a2 = (color >> 24) & 0xff;
		int a1 = 256 - a2;

		int rr = color & 0xff0000;
		int gg = color & 0xff00;
		int bb = color & 0xff;

		for (int yy = drawArea.y0; yy < drawArea.y1; yy++) {
			int tp = yy * w + drawArea.x0;
			int sp = (yy - y) * bm.w + (drawArea.x0 - x);
			for (int xx = 0; xx < drawWidth; xx++) {
				int col = bm.pixels[sp + xx];
				if (col < 0) {
					int r = (col & 0xff0000);
					int g = (col & 0xff00);
					int b = (col & 0xff);

					r = ((r * a1 + rr * a2) >> 8) & 0xff0000;
					g = ((g * a1 + gg * a2) >> 8) & 0xff00;
					b = ((b * a1 + bb * a2) >> 8) & 0xff;
					pixels[tp + xx] = 0xff000000 | r | g | b;
				}
			}
		}
	}
	
	public void fill(int x, int y, int width, int height, int color) {

		Rect drawArea = new Rect(x, y, width, height);
		adjustDrawArea(drawArea);

		int drawWidth = drawArea.x1 - drawArea.x0;

		for (int yy = drawArea.y0; yy < drawArea.y1; yy++) {
			int tp = yy * w + drawArea.x0;
			for (int xx = 0; xx < drawWidth; xx++) {
				pixels[tp + xx] = color;
			}
		}
	}

	public void fillAlpha(int x, int y, int width, int height, int color, int alpha) {
		if (alpha == 255) {
			fill(x, y, width, height, color);
			return;
		}

		Bitmap bmp = new Bitmap(width, height);
		bmp.fill(0, 0, width, height, color);

		this.drawAlpha(bmp, x, y, alpha);
	}

	public void drawRect(int x, int y, int bw, int bh, int color) {
		int x0 = x;
		int x1 = x + bw;
		int y0 = y;
		int y1 = y + bh;
		if (x0 < 0) {
			x0 = 0;
		}
		if (y0 < 0) {
			y0 = 0;
		}
		if (x1 > w) {
			x1 = w;
		}
		if (y1 > h) {
			y1 = h;
		}

		for (int yy = y0; yy < y1; yy++) {
			setPixel(x0, yy, color);
			setPixel(x1 - 1, yy, color);
		}

		for (int xx = x0; xx < x1; xx++) {
			setPixel(xx, y0, color);
			setPixel(xx, y1 - 1, color);
		}
	}

	private void setPixel(int x, int y, int color) {
		pixels[x + y * w] = color;

	}

	@SuppressWarnings("unused")
	private void drawCircle(int centerX, int centerY, int radius, int color) {
		int d = 3 - (2 * radius);
		int x = 0;
		int y = radius;

		do {
			setPixel(centerX + x, centerY + y, color);
			setPixel(centerX + x, centerY - y, color);
			setPixel(centerX - x, centerY + y, color);
			setPixel(centerX - x, centerY - y, color);
			setPixel(centerX + y, centerY + x, color);
			setPixel(centerX + y, centerY - x, color);
			setPixel(centerX - y, centerY + x, color);
			setPixel(centerX - y, centerY - x, color);

			if (d < 0) {
				d = d + (4 * x) + 6;
			} else {
				d = d + 4 * (x - y) + 10;
				y--;
			}
			x++;
		} while (x <= y);
	}

	public void fillCircle(int centerX, int centerY, int radius, int color) {
		int d = 3 - (2 * radius);
		int x = 0;
		int y = radius;

		do {
			drawHorizonalLine(centerX + x, centerX - x, centerY + y, color);
			drawHorizonalLine(centerX + x, centerX - x, centerY - y, color);
			drawHorizonalLine(centerX + y, centerX - y, centerY + x, color);
			drawHorizonalLine(centerX + y, centerX - y, centerY - x, color);

			if (d < 0) {
				d = d + (4 * x) + 6;
			} else {
				d = d + 4 * (x - y) + 10;
				y--;
			}
			x++;
		} while (x <= y);
	}

	private void drawHorizonalLine(int x1, int x2, int y, int color) {
		if (x1 > x2) {
			int xx = x1;
			x1 = x2;
			x2 = xx;
		}

		for (int xx = x1; xx <= x2; xx++) {
			setPixel(xx, y, color);
		}
	}

	public Bitmap shrink() {
		Bitmap newbmp = new Bitmap(w / 2, h / 2);
		int[] pix = pixels;
		int blarg = 0;
		for (int i = 0; i < pix.length; i++) {
			if (blarg >= newbmp.pixels.length) {
				break;
			}
			if (i % 2 == 0) {
				newbmp.pixels[blarg] = pix[i];
				blarg++;
			}
			if (i % w == 0) {
				i += w;
			}
		}

		return newbmp;
	}

	public Bitmap scaleBitmap(int width, int height) {
		Bitmap scaledBitmap = new Bitmap(width, height);

		int scaleRatioWidth = ((w << 16) / width);
		int scaleRatioHeight = ((h << 16) / height);

		int i = 0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				scaledBitmap.pixels[i++] = pixels[(w * ((y * scaleRatioHeight) >> 16)) + ((x * scaleRatioWidth) >> 16)];
			}
		}

		return scaledBitmap;
	}
}