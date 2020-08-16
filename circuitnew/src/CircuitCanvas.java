import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import utils.ColorUtils;

class CircuitCanvas extends Canvas implements Printable {
	CirSim pg;

	CircuitCanvas(CirSim p) {
		pg = p;
	}

	public Dimension getPreferredSize() {
		return new Dimension(300, 400);
	}

	public void update(Graphics g) {
		pg.updateCircuit(g);
	}

	public void paint(Graphics g) {
		//pg.updateCircuit(g);
	}

	@Override
	public int print(Graphics g, PageFormat pageFormat, int pageIndex)
			throws PrinterException {
		if (pageIndex > 0) { /* We have only one page, and 'page' is zero-based */
			return NO_SUCH_PAGE;
		}
		/*
		 * User (0,0) is typically outside the imageable area, so we must
		 * translate by the X and Y values in the PageFormat to avoid clipping
		 */
		Graphics2D g2d = (Graphics2D) g;
		g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
		g2d.scale(0.6, 0.6);
		pg.updateCircuit(g2d);
		/* tell the caller that this page is part of the printed document */
		return PAGE_EXISTS;
	}
	
	public void saveImage(String fileName) throws IOException {
		BufferedImage img = new BufferedImage(getWidth(), getHeight(),
				BufferedImage.TYPE_INT_RGB);
		Graphics g =  img.getGraphics();
		pg.updateCircuit(g);
		int width = img.getWidth(), height = img.getHeight();
		Color bg = getBackground();
		g.setColor(ColorUtils.blendColors(Color.BLACK, bg, .5));
		g.drawRect(1, 1, width - 3, height - 3);
		g.setColor(ColorUtils.blendColors(Color.WHITE, bg, .5));
		g.drawRect(0, 0, width - 1, height - 1);
		ImageIO.write(img, "png", new File(fileName));
	}

};
