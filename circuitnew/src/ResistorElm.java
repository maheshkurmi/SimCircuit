import java.awt.*;
import java.util.StringTokenizer;

class ResistorElm extends CircuitElm {
	double resistance;
	/**
	 * polygon for arrow in variable capacitor
	 */
	Polygon arrowPoly;
	Point ptArrow1, ptArrow2;

	public ResistorElm(int xx, int yy) {
		super(xx, yy);
		resistance = 100;
		value = resistance;
		minValue = value / 5;
		maxValue = value * 5;
	}

	public ResistorElm(int xa, int ya, int xb, int yb, int f, StringTokenizer st) {
		super(xa, ya, xb, yb, f);
		resistance = new Double(st.nextToken()).doubleValue();
		value = resistance;
		try {
			variable = new Boolean(st.nextToken()).booleanValue();
			minValue = new Double(st.nextToken()).doubleValue();
			maxValue = new Double(st.nextToken()).doubleValue();
		} catch (Exception e) {
		}

		if (minValue > value)
			minValue = value / 5;
		if (maxValue < value)
			maxValue = value * 5;
		// minValue=value/5;
		// maxValue=value*5;
	}

	int getDumpType() {
		return 'r';
	}

	String dump() {
		return super.dump() + " " + resistance + " " + variable + " "
				+ minValue + " " + maxValue;
	}

	Point ps3, ps4;

	void setPoints() {
		super.setPoints();
		// prepare arrow
		if (variable) {
			calcLeads(30);
			Point p1 = new Point();
			ptArrow1 = new Point();
			Point p2 = new Point();
			ptArrow2 = new Point();
			interpPoint2(lead1, lead2, p1, ptArrow1, 0, 15);
			interpPoint2(lead2, lead1, p2, ptArrow2, 0, 17);
			arrowPoly = calcArrow(ptArrow1, ptArrow2, 6, 4);
		}

		calcLeads(32);
		ps3 = new Point();
		ps4 = new Point();
	}

	void draw(Graphics g) {
		int segments = 16;
		int i;
		int ox = 0;
		int hs = sim.euroResistorCheckItem.getState() ? 6 : 8;
		double v1 = volts[0];
		double v2 = volts[1];
		setBbox(point1, point2, hs);
		draw2Leads(g);
		setPowerColor(g, true);
		double segf = 1. / segments;
		if (!sim.euroResistorCheckItem.getState()) {
			// draw zigzag
			for (i = 0; i != segments; i++) {
				int nx = 0;
				switch (i & 3) {
				case 0:
					nx = 1;
					break;
				case 2:
					nx = -1;
					break;
				default:
					nx = 0;
					break;
				}
				double v = v1 + (v2 - v1) * i / segments;
				setVoltageColor(g, v);
				interpPoint(lead1, lead2, ps1, i * segf, hs * ox);
				interpPoint(lead1, lead2, ps2, (i + 1) * segf, hs * nx);
				drawThickLine(g, ps1, ps2);
				ox = nx;
			}
		} else {
			// draw rectangle
			setVoltageColor(g, v1);
			interpPoint2(lead1, lead2, ps1, ps2, 0, hs);
			drawThickLine(g, ps1, ps2);
			for (i = 0; i != segments; i++) {
				double v = v1 + (v2 - v1) * i / segments;
				setVoltageColor(g, v);
				interpPoint2(lead1, lead2, ps1, ps2, i * segf, hs);
				interpPoint2(lead1, lead2, ps3, ps4, (i + 1) * segf, hs);
				drawThickLine(g, ps1, ps3);
				drawThickLine(g, ps2, ps4);
			}
			interpPoint2(lead1, lead2, ps1, ps2, 1, hs);
			drawThickLine(g, ps1, ps2);
		}
		if (variable) {
			g.drawLine(ptArrow1.x, ptArrow1.y, ptArrow2.x, ptArrow2.y);
			g.fillPolygon(arrowPoly);
		}
		if (sim.showValuesCheckItem.getState()) {
			String s = getShortUnitText(resistance, sim.ohmString);
			drawValues(g, s, hs);
		}
		doDots(g);
		drawPosts(g);
	}

	void calculateCurrent() {
		current = (volts[0] - volts[1]) / resistance;
		// System.out.print(this + " res current set to " + current + "\n");
	}

	void stamp() {
		sim.stampResistor(nodes[0], nodes[1], resistance);
	}

	void getInfo(String arr[]) {
		arr[0] = "resistor";
		getBasicInfo(arr);
		arr[3] = "R = " + getUnitText(resistance, sim.ohmString);
		arr[4] = "P = " + getUnitText(getPower(), "W");
	}

	public EditInfo getEditInfo(int n) {
		// ohmString doesn't work here on linux
		if (n == 0)
			return new EditInfo("Resistance (ohms)", resistance, 0, 0);
		if (n == 1) {
			EditInfo ei = new EditInfo("", 0, -1, -1);
			ei.checkbox = new Checkbox("Variable", variable);
			return ei;
		}
		if (variable) {
			if (n == 2)
				return new EditInfo("Min Resistance", minValue,
						resistance / 10, resistance * 0.99);
			if (n == 3)
				return new EditInfo("Max Resistance", maxValue,
						resistance * 1.01, resistance * 10);
			else
				return null;
		}
		return null;
	}

	void setValue(double val) {
		super.setValue(val);
		resistance = val;
		sim.analyzeFlag = true;
		// setPoints();
	}

	public void setEditValue(int n, EditInfo ei) {
		if (n == 0)
			setValue(ei.value);
		if (n == 1) {
			variable = ei.checkbox.getState();
			ei.newDialog = true;
			setPoints();
		}
		if (variable) {
			if (n == 2)
				setMinValue(ei.value);
			if (n == 3)
				setMaxValue(ei.value);
		}
	}

	int getShortcut() {
		return 'r';
	}
}
