import java.awt.*;
import java.util.StringTokenizer;

class GalvanometerElm extends CircuitElm {
	double resistance;
	double refAngle;
	double Ig = 0.001;
	double avgReading, rmsReading, t;
	double scale = 1;

	public GalvanometerElm(int xx, int yy) {
		super(xx, yy);
		resistance = 1;
	}

	public GalvanometerElm(int xa, int ya, int xb, int yb, int f,
			StringTokenizer st) {
		super(xa, ya, xb, yb, f);
		resistance = new Double(st.nextToken()).doubleValue();
	}

	int getDumpType() {
		return ')';
	}

	String dump() {
		return super.dump() + " " + resistance;
	}

	void reset() {
		super.reset();
		curcount = 0;
		current = 0;
		avgReading = 0;
		rmsReading = 0;
		t = 0;
	}

	Point ledLead1, ledLead2, ledCenter;

	void setPoints() {
		reset();
		super.setPoints();
		int cr = 14;
		ledLead1 = interpPoint(point1, point2, .5 - cr / dn);
		ledLead2 = interpPoint(point1, point2, .5 + cr / dn);
		ledCenter = interpPoint(point1, point2, .5);
		refAngle = Math.atan2(point2.y - point1.y, point2.x - point1.x);
	}

	void draw(Graphics g) {

		double v1 = volts[0];
		double v2 = volts[1];
		setBbox(point1, point2, 10);
		// draw2Leads(g);
		setPowerColor(g, true);
		setVoltageColor(g, volts[0]);
		drawThickLine(g, point1, ledLead1);
		setVoltageColor(g, volts[1]);

		drawThickLine(g, ledLead2, point2);
		g.setColor(Color.gray);
		int cr = 14;
		// drawThickCircle(g, ledCenter.x, ledCenter.y, cr);
		g.drawOval(ledCenter.x - cr, ledCenter.y - cr, cr * 2, cr * 2);

		cr -= 2;
		double w = 255 * current / .01;
		if (w > 255)
			w = 255;
		// g.fillOval(ledCenter.x - cr, ledCenter.y - cr, cr * 2, cr * 2);
		// drawValues(g, getReading()+"V", hs);

		if (sim.showValuesCheckItem.getState()) {
			String s = getCurrentText(Math.abs(getCurrent()));
			drawValues(g, s, 15);
		}

		doDots(g);
		cr = 14;
		g.setColor(new Color(100, 100, 100, 50));
		g.fillOval(ledCenter.x - cr, ledCenter.y - cr, cr * 2, cr * 2);
		Point p = interpPoint(ledLead1, ledLead2, .1);
		g.setColor(Color.red);
		g.drawLine(ledLead1.x, ledLead1.y, p.x, p.y);
		p = interpPoint(ledLead2, ledLead1, .1);
		g.drawLine(ledLead2.x, ledLead2.y, p.x, p.y);
		g.setColor(Color.gray);
		cr = 14;
		g.setColor(needsHighlight() ? selectColor : Color.gray);

		// drawThickCircle(g, ledCenter.x, ledCenter.y, cr);
		g.drawOval(ledCenter.x - cr, ledCenter.y - cr, cr * 2, cr * 2);
		cr -= 4;
		double theta = (Math.PI / 2) * current / Ig;
		if (theta > Math.PI / 2)
			theta = Math.PI / 2;
		if (theta < -Math.PI / 2)
			theta = -Math.PI / 2;
		theta = refAngle - Math.PI / 2 + theta;
		g.setColor(Color.red);
		g.drawLine(ledCenter.x, ledCenter.y,
				(int) (ledCenter.x + cr * Math.cos(theta)),
				(int) (ledCenter.y + cr * Math.sin(theta)));
		cr = 2;
		g.fillOval(ledCenter.x - cr, ledCenter.y - cr, cr * 2, cr * 2);

		drawPosts(g);
	}

	void doDots(Graphics g) {
		updateDotCount();
		if (sim.dragElm != this)
			drawDots(g, point1, ledLead1, curcount);
		drawDots(g, ledLead2, point2, curcount);
	}

	void calculateCurrent() {
		current = (volts[0] - volts[1]) / resistance;
		// System.out.print(this + " res current set to " + current + "\n");
	}

	/**
	 * Calculates avge and rms values
	 */
	void doStep() {
		double i = (volts[0] - volts[1]) / resistance;
		if (i == 0)
			return;
		avgReading = (avgReading * t + i * sim.timeStep);
		rmsReading = (rmsReading * rmsReading * t + i * i * sim.timeStep);
		t += sim.timeStep;
		avgReading /= t;
		rmsReading /= t;
		rmsReading = Math.sqrt(rmsReading);
	}

	void stamp() {
		sim.stampResistor(nodes[0], nodes[1], resistance);
	}

	void getInfo(String arr[]) {
		arr[0] = "Galvanometer (Ig = " + getCurrentText(Ig) + ")";
		arr[1] = "I = " + getCurrentDText(getCurrent() * scale);
		arr[2] = "R = " + getUnitText(resistance, CirSim.ohmString);
		arr[3] = "Scale = " +scale;
		// arr[4] = "Irms = " + getCurrentText(rmsReading);
	}

	public EditInfo getEditInfo(int n) {
		// ohmString doesn't work here on linux
		if (n == 0)
			return new EditInfo("Resistance (ohms)", resistance, 0, 0);
		if (n == 1)
			return new EditInfo("Max. Deflection Current (Amperes)", Ig, 0.001,
					100);
		if (n == 2)
			return new EditInfo("Scale Factor", scale, 1, 1000);
		return null;
	}

	public void setEditValue(int n, EditInfo ei) {
		if (n == 0) {
			if (ei.value > 0)
				resistance = ei.value;
		}
		if (n == 1) {
			if (ei.value > 0)
				Ig = ei.value;
		}

		if (n == 2) {
			if (ei.value > 0)
				scale = ei.value;
		}
	}

	int getShortcut() {
		return ')';
	}
}
