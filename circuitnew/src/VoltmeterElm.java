import java.awt.*;
import java.util.StringTokenizer;

class VoltmeterElm extends CircuitElm {
	double resistance;
	double refAngle;
	double range = 5;
	double avgReading, rmsReading, t;

	public VoltmeterElm(int xx, int yy) {
		super(xx, yy);
		resistance = 1e4;
	}

	public VoltmeterElm(int xa, int ya, int xb, int yb, int f,
			StringTokenizer st) {
		super(xa, ya, xb, yb, f);
		resistance = new Double(st.nextToken()).doubleValue();
	}

	int getDumpType() {
		return 'q';
	}

	String dump() {
		return super.dump() + " " + resistance;
	}

	Point ledLead1, ledLead2, ledCenter;
	Point ps3, ps4;

	void setPoints() {
		reset();
		super.setPoints();
		Point p = new Point(point2.y - point1.y, point1.x - point2.x);
		double l1 = p.distance(new Point(0, 0));
		// l1=l1;
		double l2 = l1 * 0.6;
		if (l2 > 60)
			l2 = 60;
		if (l2 < 35)
			l2 = 35;
		p = new Point((int) (p.x * l2 / l1), (int) (p.y * l2 / l1));
		ps3 = new Point(point1.x + p.x, point1.y + p.y);
		ps4 = new Point(point2.x + p.x, point2.y + p.y);
		int cr = 14;
		ledLead1 = interpPoint(ps3, ps4, .5 - cr / dn);
		ledLead2 = interpPoint(ps3, ps4, .5 + cr / dn);
		ledCenter = interpPoint(ps3, ps4, .5);
		refAngle = Math.atan2(point2.y - point1.y, point2.x - point1.x);
	}

	void draw(Graphics g) {
		double v1 = volts[0];
		double v2 = volts[1];
		setBbox(ps3, ps4, 14);
		// draw2Leads(g);
		setPowerColor(g, true);
		setVoltageColor(g, volts[0]);
		drawThickLine(g, ps3, point1);
		drawThickLine(g, ps3, ledLead1);
		setVoltageColor(g, volts[1]);

		drawThickLine(g, ledLead2, ps4);
		drawThickLine(g, ps4, point2);
		// g.setColor(needsHighlight()?Color.gray:selectColor);
		// int cr = 14;
		// drawThickCircle(g, ledCenter.x, ledCenter.y, cr);
		// g.drawOval(ledCenter.x - cr, ledCenter.y - cr, cr * 2, cr * 2);

		// cr -= 4;
		// double w = 255 * current / .01;
		// if (w > 255)
		// w = 255;
		// g.fillOval(ledCenter.x - cr, ledCenter.y - cr, cr * 2, cr * 2);
		// drawValues(g, getReading()+"V", hs);
		int px, py, px2, py2;
		px = x;
		py = y;
		px2 = x2;
		py2 = y2;
		x = ps3.x;
		y = ps3.y;
		x2 = ps4.x;
		y2 = ps4.y;

		if (sim.showValuesCheckItem.getState()) {
			String s = getVoltageText(volts[0] - volts[1]);
			drawValues(g, s, 15);
		}
		x = px;
		y = py;
		x2 = px2;
		y2 = py2;
		int cr = 14;
		g.setColor(new Color(100, 100, 100, 50));
		g.fillOval(ledCenter.x - cr, ledCenter.y - cr, cr * 2, cr * 2);

		Point p = interpPoint(ledLead1, ledLead2, .1);
		g.setColor(Color.red);
		g.drawLine(ledLead1.x, ledLead1.y, p.x, p.y);
		p = interpPoint(ledLead2, ledLead1, .1);
		g.drawLine(ledLead2.x, ledLead2.y, p.x, p.y);
		g.setColor(needsHighlight() ? selectColor : Color.gray);
		// cr = 12;
		// drawThickCircle(g, ledCenter.x, ledCenter.y, cr);
		g.drawOval(ledCenter.x - cr, ledCenter.y - cr, cr * 2, cr * 2);
		cr -= 4;
		double theta = (Math.PI / 2) * (volts[0] - volts[1]) / range;
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
		// if (Math.abs(getVoltageDiff())>1e-3)
		drawPolarity(g, false);
		doDots(g);
		// drawPosts(g);
	}

	void drawPolarity(Graphics g, boolean reverse) {
		g.setColor(lightGrayColor);
		int dx, dy;

		Point p1 = new Point();
		Point p2 = new Point();// =interpPoint(interpPoint(plate1[0],
								// plate2[0],-0.6),interpPoint(plate1[1],
								// plate2[1],-0.6),0.15);
		interpPoint2(ps3, ps4, p1, p2, .5 - 17 / dn, -8);
		dx = (int) ((p1.x - p2.x) * 0.15);
		dy = (int) ((p1.y - p2.y) * 0.15);
		if (reverse)
			g.drawLine(p2.x - dx, p2.y - dy, p2.x + dx, p2.y + dy);
		g.drawLine(p2.x - dy, p2.y + dx, p2.x + dy, p2.y - dx);
		// dx=(int) ((plate1[1].x-plate1[0].x)*0.08f);
		// dy=(int) ((plate2[1].y-plate2[0].y)*0.08f);
		interpPoint2(ps3, ps4, p1, p2, .5 + 17 / dn, -8);
		if (!reverse)
			g.drawLine(p2.x - dx, p2.y - dy, p2.x + dx, p2.y + dy);
		g.drawLine(p2.x - dy, p2.y + dx, p2.x + dy, p2.y - dx);

	}

	void doDots(Graphics g) {
		updateDotCount();
		if (sim.dragElm != this)
			drawDots(g, ps3, ledLead1, curcount);
		drawDots(g, ledLead2, ps4, curcount);
		drawDots(g, point1, ps3, curcount);
		drawDots(g, ps4, point2, curcount);

	}

	void calculateCurrent() {
		current = (volts[0] - volts[1]) / resistance;
		// System.out.print(this + " res current set to " + current + "\n");
	}

	/**
	 * Calculates avge and rms values
	 */
	void doStep() {
		double v = (volts[0] - volts[1]);
		if (v == 0)
			return;
		avgReading = (avgReading * t + v * sim.timeStep);
		rmsReading = (rmsReading * rmsReading * t + v * v * sim.timeStep);
		t += sim.timeStep;
		avgReading /= t;
		rmsReading /= t;
		rmsReading = Math.sqrt(rmsReading);
	}

	void reset() {
		super.reset();
		curcount = 0;
		current = 0;
		avgReading = 0;
		rmsReading = 0;
		t = 0;
	}

	void stamp() {
		sim.stampResistor(nodes[0], nodes[1], resistance);
	}

	void getInfo(String arr[]) {
		arr[0] = "VoltMeter (range = " + getVoltageDText(range) + ")";
		arr[1] = "Vd = " + getVoltageDText(getVoltageDiff());
		arr[2] = "R = " + getUnitText(resistance, sim.ohmString);
		arr[3] = "Vav = " + getVoltageText(avgReading);
		arr[4] = "Vrms = " + getVoltageText(rmsReading);
	}

	public EditInfo getEditInfo(int n) {
		// ohmString doesn't work here on linux
		if (n == 0)
			return new EditInfo("Resistance (ohms)", resistance, 0, 0);
		if (n == 1)
			return new EditInfo("VoltageRange (Volts)", range, 0.001, 1000);
		return null;
	}

	public void setEditValue(int n, EditInfo ei) {
		if (n == 0) {
			if (ei.value > 0)
				resistance = ei.value;
		}
		if (n == 1) {
			if (ei.value > 0)
				range = ei.value;
		}
	}

	int getShortcut() {
		return 'q';
	}
}
