import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

public class KnobElm {

	private static int radius = 40;
	private static int spotRadius = 5;

	private double theta;
	private Color knobColor = Color.gray;
	private Color knobFillColor = new Color(100, 100, 100, 50);
	private Color spotColor = new Color(190, 60, 40, 190);
	private Color spotFillColor = new Color(90, 90, 90, 80);
	private boolean pressedOnSpot = false;
	private boolean multiRotate = false;
	/**
	 * max angle knob can rotate on either side
	 */
	private final double AMP=3*Math.PI/4; 
	/**
	 * Circuit Element associated with knob
	 */
	CircuitElm element;
	Rectangle rect;
	CirSim sim;
	double val, min,max;
	/**
	 * Constructor that initializes the position of the knob to the specified
	 * position and also allows the colors of the knob and spot to be specified.
	 * 
	 * @param initAngle
	 *            the initial angle of the knob.
	 * @param sim
	 *            Circuit Simulator.
	 */
	public KnobElm(double initTheta, CirSim sim) {
		theta = initTheta;
		this.sim = sim;
		rect = new Rectangle(100, 100, 2 * radius + 12, 2 * radius + 12);
	}

	public void setRect(Rectangle r) {
		rect = r;
		radius = rect.height / 2;
	}

	public void setElement(CircuitElm e) {
		if (e != null) {
			if (e.variable == true) {
				element = e;
				val=e.value;
				min=e.minValue;
				max=e.maxValue;
				//if (e instanceof PotElm){
				multiRotate = false;
				theta=-AMP+((val-min)/(max-min))*2*AMP;
				sim.repaint();
				return;
				//}else{
				//multiRotate = true;
				//theta = 0;
				//}
			} 
		}else {
			element = null;
			SetIsOnSpot(false);
		}
	}

	public Rectangle getRect() {
		return rect;
	}

	public int getWidth() {
		return rect.width;
	}

	public int getHeight() {
		return rect.height;
	}

	public int getLeftEdge() {
		return rect.x;
	}

	public int getLightEdge() {
		return rect.x + rect.width;
	}

	/**
	 * Paint the JKnob on the graphics context given. The knob is a filled
	 * circle with a small filled circle offset within it to show the current
	 * angular position of the knob.
	 * 
	 * @param g
	 *            The graphics context on which to paint the knob.
	 */
	public void paint(Graphics g) {
		if (element == null)
			return;
		setElement(element);
		// Draw the knob.
		g.translate(rect.x, rect.y);
		g.setColor(knobFillColor);
		g.fillOval(0, 0, 2 * radius, 2 * radius);
		g.setColor(knobColor);
		g.drawOval(0, 0, 2 * radius, 2 * radius);

		// Find the center of the spot.
		Point pt = getSpotCenter();
		int xc = (int) pt.getX();
		int yc = (int) pt.getY();
		g.setColor(spotFillColor);
		// Draw the spot.
		g.fillOval(xc - spotRadius, yc - spotRadius, 2 * spotRadius,
				2 * spotRadius);
		// g.setColor(spotColor);
		g.setColor(pressedOnSpot ? CircuitElm.selectColor : spotColor);

		g.drawOval(xc - spotRadius, yc - spotRadius, 2 * spotRadius,
				2 * spotRadius);
		int r = radius - spotRadius;
		if (multiRotate == false) {
			int x, y, x1, y1;
			for (double a = -Math.PI / 4; a <= 5 * Math.PI / 4; a += Math.PI / 8) {
				x = (int) (radius + r * Math.cos(a));
				y = (int) (radius - r * Math.sin(a));
				g.drawLine(x, y, x, y);
			}
			x = (int) (radius + r * Math.cos(-Math.PI / 4));
			y = (int) (radius - r * Math.sin(-Math.PI / 4));
			x1 = (int) (radius + radius * Math.cos(-Math.PI / 4));
			y1 = (int) (radius - radius * Math.sin(-Math.PI / 4));
			g.drawLine(x, y, x1, y1);
			x = (int) (radius - r * Math.cos(-Math.PI / 4));
			y = (int) (radius - r * Math.sin(-Math.PI / 4));
			x1 = (int) (radius - radius * Math.cos(-Math.PI / 4));
			y1 = (int) (radius - radius * Math.sin(-Math.PI / 4));
			g.drawLine(x, y, x1, y1);
		} else {
			int x, y;
			for (double a = 0; a <= 2 * Math.PI; a += Math.PI / 8) {
				x = (int) (radius + r * Math.cos(a));
				y = (int) (radius - r * Math.sin(a));
				g.drawLine(x, y, x, y);
			}
		}
		g.translate(-rect.x, -rect.y);
	}

	/**
	 * Get the current anglular position of the knob.
	 * 
	 * @return the current anglular position of the knob.
	 */
	public double getAngle() {
		return theta;
	}

	/**
	 * Calculate the x, y coordinates of the center of the spot.
	 * 
	 * @return a Point containing the x,y position of the center of the spot.
	 */
	private Point getSpotCenter() {
		// Calculate the center point of the spot RELATIVE to the
		// center of the of the circle.
		int r = radius - spotRadius;
		int xcp = (int) (r * Math.sin(theta));
		int ycp = (int) (r * Math.cos(theta));
		// Adjust the center point of the spot so that it is offset
		// from the center of the circle. This is necessary becasue
		// 0,0 is not actually the center of the circle, it is the
		// upper left corner of the component!
		int xc = radius + xcp;
		int yc = radius - ycp;

		// Create a new Point to return since we can't
		// return 2 values!
		return new Point(xc, yc);
	}

	/**
	 * Determine if the mouse click was on the spot or not. If it was return
	 * true, otherwise return false.
	 * 
	 * @return true if x,y is on the spot and false if not.
	 */
	public boolean isOnSpot(MouseEvent e) {
		if (element == null)
			return false;
		if (rect.contains(e.getPoint()))
			sim.mouseElm = element;
		else
			return false;
		Point mouseLoc = e.getPoint();
		mouseLoc.translate(-rect.x, -rect.y);
		pressedOnSpot = (mouseLoc.distance(getSpotCenter()) < spotRadius);

		return pressedOnSpot;
	}

	public void SetIsOnSpot(boolean val) {
		pressedOnSpot = val;
	}

	/**
	 * Compute the new angle for the spot and repaint the knob. The new angle is
	 * computed based on the new mouse position.
	 * 
	 * @param e
	 *            reference to a MouseEvent object describing the mouse drag.
	 */
	public void processMouseDragged(MouseEvent e) {
		if (pressedOnSpot) {

			int mx = e.getX() - rect.x;
			int my = e.getY() - rect.y;

			// Compute the x, y position of the mouse RELATIVE
			// to the center of the knob.
			int mxp = mx - radius;
			int myp = radius - my;

			// Compute the new angle of the knob from the
			// new x and y position of the mouse.
			// Math.atan2(...) computes the angle at which
			// x,y lies from the positive y axis with cw rotations
			// being positive and ccw being negative.
			double t = Math.atan2(mxp, myp);
			if (multiRotate == false) {
				if (t < -3 * Math.PI / 4 && t >= -Math.PI)
					return;
				if (t > 3 * Math.PI / 4 && t <= Math.PI)
					return;
				theta = t;
				System.out.println(t );
				if (element != null) {
					element.setInterpolatedValue((theta+AMP)/(2*AMP));
				}
				//element.changeValue(t / (3 * Math.PI / 4));
			} 
			sim.repaint();
		}
	}
}
