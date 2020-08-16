import java.awt.*;
import java.util.StringTokenizer;

class InductorElm extends CircuitElm {
	Inductor ind;
	double inductance;
	/**
	 * polygon for arrow in variable capacitor
	 */
	Polygon arrowPoly;
	Point ptArrow1, ptArrow2;

	public InductorElm(int xx, int yy) {
		super(xx, yy);
		ind = new Inductor(sim);
		inductance = 1;
		ind.setup(inductance, current, flags);
		value = inductance;
		minValue = value / 5;
		maxValue = value * 5;
	}

	public InductorElm(int xa, int ya, int xb, int yb, int f, StringTokenizer st) {
		super(xa, ya, xb, yb, f);
		ind = new Inductor(sim);
		try {
			inductance = new Double(st.nextToken()).doubleValue();
			current = new Double(st.nextToken()).doubleValue();
			variable = new Boolean(st.nextToken()).booleanValue();
			minValue = new Double(st.nextToken()).doubleValue();
			maxValue = new Double(st.nextToken()).doubleValue();
		} catch (Exception e) {
		}

		value = inductance;
		if (minValue > value)
			minValue = value / 5;
		if (maxValue < value)
			maxValue = value * 5;
		ind.setup(inductance, current, flags);
	}

	int getDumpType() {
		return 'l';
	}

	String dump() {
		return super.dump() + " " + inductance + " " + current + " " + variable
				+ " " + minValue + " " + maxValue;
	}

	void setPoints() {
		super.setPoints();
		if (variable) {
			calcLeads(36);
			Point p1 = new Point();
			ptArrow1 = new Point();
			Point p2 = new Point();
			ptArrow2 = new Point();
			interpPoint2(lead1, lead2, p1, ptArrow1, 0, 6);
			interpPoint2(lead2, lead1, p2, ptArrow2, 0, 17);
			arrowPoly = calcArrow(ptArrow1, ptArrow2, 6, 4);
		}

		calcLeads(32);
	}

	void draw(Graphics g) {
		double v1 = volts[0];
		double v2 = volts[1];
		int i;
		int hs = 8;
		setBbox(point1, point2, hs);
		draw2Leads(g);
		setPowerColor(g, false);
		drawCoil(g, 8, lead1, lead2, v1, v2);
		if (sim.showValuesCheckItem.getState()) {
			String s = getShortUnitText(inductance, "H");
			drawValues(g, s, hs + 2);
		}
		if (variable) {
			g.drawLine(ptArrow1.x, ptArrow1.y, ptArrow2.x, ptArrow2.y);
			g.fillPolygon(arrowPoly);
		}
		doDots(g);
		drawPosts(g);
	}

	void reset() {
		current = volts[0] = volts[1] = curcount = 0;
		ind.reset();
	}

	void stamp() {
		ind.stamp(nodes[0], nodes[1]);
	}

	void startIteration() {
		ind.startIteration(volts[0] - volts[1]);
	}

	boolean nonLinear() {
		return ind.nonLinear();
	}

	void calculateCurrent() {
		double voltdiff = volts[0] - volts[1];
		current = ind.calculateCurrent(voltdiff);
	}

	void doStep() {
		double voltdiff = volts[0] - volts[1];
		ind.doStep(voltdiff);
	}

	void getInfo(String arr[]) {
		arr[0] = "inductor";
		getBasicInfo(arr);
		arr[3] = "L = " + getUnitText(inductance, "H");
		arr[4] = "P = " + getUnitText(getPower(), "W");
	}

	public EditInfo getEditInfo(int n) {
		if (n == 0)
			return new EditInfo("Inductance (H)", inductance, 0, 0);
		if (n == 1) {
			EditInfo ei = new EditInfo("", 0, -1, -1);
			ei.checkbox = new Checkbox("Trapezoidal Approximation",
					ind.isTrapezoidal());
			return ei;
		}
		if (n == 2) {
			EditInfo ei = new EditInfo("", 0, -1, -1);
			ei.checkbox = new Checkbox("Variable", variable);
			return ei;
		}
		if (variable) {
			if (n == 3)
				return new EditInfo("Min Inductance", minValue,
						inductance / 10, inductance * 0.99);
			if (n == 4)
				return new EditInfo("Max Inductance", maxValue,
						inductance * 1.01, inductance * 10);
			else
				return null;
		}
		return null;
	}

	public void setEditValue(int n, EditInfo ei) {
		if (n == 0)
			inductance = ei.value;
		if (n == 1) {
			if (ei.checkbox.getState())
				flags &= ~Inductor.FLAG_BACK_EULER;
			else
				flags |= Inductor.FLAG_BACK_EULER;
		}
		if (n == 2) {
			variable = ei.checkbox.getState();
			ei.newDialog = true;
			setPoints();
		}
		if (variable) {
			if (n == 3)
				setMinValue(ei.value);
			if (n == 4)
				setMaxValue(ei.value);
		}
		ind.setup(inductance, current, flags);
	}

	void setValue(double val) {
		super.setValue(val);
		inductance = val;
		sim.analyzeFlag = true;
		// setPoints();
	}

	int getShortcut() {
		return 'l';
	}
}
