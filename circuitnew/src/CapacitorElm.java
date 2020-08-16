import java.awt.*;
import java.util.StringTokenizer;

class CapacitorElm extends CircuitElm {
	double capacitance;
	double compResistance, voltdiff;
	Point plate1[], plate2[];
	public static final int FLAG_BACK_EULER = 2;
	/**
	 * polygon for arrow in variable capacitor
	 */
	Polygon arrowPoly;
	Point ptArrow1,ptArrow2;
	
	public CapacitorElm(int xx, int yy) {
		super(xx, yy);
		capacitance = 1e-5;
		value=capacitance;
		minValue=value/5;
		maxValue=value*5;
	}

	public CapacitorElm(int xa, int ya, int xb, int yb, int f,
			StringTokenizer st) {
		super(xa, ya, xb, yb, f);
		try {
			capacitance = new Double(st.nextToken()).doubleValue();
			voltdiff = new Double(st.nextToken()).doubleValue();
			value = capacitance;

			variable = new Boolean(st.nextToken()).booleanValue();
			minValue = new Double(st.nextToken()).doubleValue();
			maxValue = new Double(st.nextToken()).doubleValue();
		} catch (Exception e) {
		}

		if (minValue>value)minValue=value/5;
		if (maxValue<value)maxValue=value*5;
		//minValue=value/5;
		//maxValue=value*5;
	}

	boolean isTrapezoidal() {
		return (flags & FLAG_BACK_EULER) == 0;
	}

	void setNodeVoltage(int n, double c) {
		super.setNodeVoltage(n, c);
		voltdiff = volts[0] - volts[1];
	}

	void reset() {
		current = curcount = 0;
		// put small charge on caps when reset to start oscillators
		voltdiff = 1e-3;
	}

	int getDumpType() {
		return 'c';
	}

	String dump() {
		return super.dump() + " " + capacitance + " " + voltdiff + " " + variable + " " + minValue + " " + maxValue;
	}

	void setPoints() {
		super.setPoints();
		//prepare arrow 
		if (variable){
			calcLeads(30);
			Point p1=new Point();
			ptArrow1=new Point();
			Point p2=new Point();
			ptArrow2=new Point();
			interpPoint2(lead1, lead2, p1, ptArrow1, 0, 10);
			interpPoint2(lead2, lead1, p2,ptArrow2, 0, 12);
			arrowPoly = calcArrow(ptArrow1,ptArrow2, 6, 4);
		}
		
		double f = (dn / 2 - 4) / dn;
		// calc leads
		lead1 = interpPoint(point1, point2, f);
		lead2 = interpPoint(point1, point2, 1 - f);
		// calc plates
		plate1 = newPointArray(2);
		plate2 = newPointArray(2);
		interpPoint2(point1, point2, plate1[0], plate1[1], f, 12);
		interpPoint2(point1, point2, plate2[0], plate2[1], 1 - f, 12);
		
	}

	void draw(Graphics g) {
		int hs = 12;
		setBbox(point1, point2, hs);

		// draw first lead and plate
		setVoltageColor(g, volts[0]);
		drawThickLine(g, point1, lead1);
		setPowerColor(g, false);
		drawThickLine(g, plate1[0], plate1[1]);
		if (sim.powerCheckItem.getState())
			g.setColor(Color.gray);

		// draw second lead and plate
		setVoltageColor(g, volts[1]);
		drawThickLine(g, point2, lead2);
		setPowerColor(g, false);
		drawThickLine(g, plate2[0], plate2[1]);
		if (variable){
			g.drawLine(ptArrow1.x,ptArrow1.y,ptArrow2.x,ptArrow2.y);
			g.fillPolygon(arrowPoly);
		}
		//g.drawLine( plate2[1].x, plate2[1].y)
		updateDotCount();
		if (sim.dragElm != this) {
			drawDots(g, point1, lead1, curcount);
			drawDots(g, point2, lead2, -curcount);
		}
		drawPosts(g);
		if (sim.showValuesCheckItem.getState()) {
			String s = getShortUnitText(capacitance, "F");
			drawValues(g, s, hs+4);
			
		}
		if (Math.abs(getVoltageDiff())>1e-3)drawPolarity(g,getVoltageDiff()>0);
	}

	void drawPolarity(Graphics g,boolean reverse) {
		g.setColor(lightGrayColor);
		int dx,dy;
		dx=(int) ((plate1[1].x-plate1[0].x)*0.09f);
		dy=(int) ((plate1[1].y-plate1[0].y)*0.09f);
		Point p=interpPoint(interpPoint(plate1[0], plate2[0],-0.6),interpPoint(plate1[1], plate2[1],-0.6),0.15);
		if (reverse)g.drawLine(p.x-dx, p.y-dy, p.x+dx, p.y+dy);
		g.drawLine(p.x-dy, p.y+dx, p.x+dy, p.y-dx);
		//dx=(int) ((plate1[1].x-plate1[0].x)*0.08f);
		//dy=(int) ((plate2[1].y-plate2[0].y)*0.08f);
		p=interpPoint(interpPoint(plate2[0], plate1[0],-0.6),interpPoint(plate2[1], plate1[1],-0.6),0.15);
		if (!reverse)g.drawLine(p.x-dx, p.y-dy, p.x+dx, p.y+dy);
		g.drawLine(p.x-dy, p.y+dx, p.x+dy, p.y-dx);
	
	}
	
	void stamp() {
		// capacitor companion model using trapezoidal approximation
		// (Norton equivalent) consists of a current source in
		// parallel with a resistor. Trapezoidal is more accurate
		// than backward euler but can cause oscillatory behavior
		// if RC is small relative to the timestep.
		if (isTrapezoidal())
			compResistance = sim.timeStep / (2 * capacitance);
		else
			compResistance = sim.timeStep / capacitance;
		sim.stampResistor(nodes[0], nodes[1], compResistance);
		sim.stampRightSide(nodes[0]);
		sim.stampRightSide(nodes[1]);
	}

	void startIteration() {
		if (isTrapezoidal())
			curSourceValue = -voltdiff / compResistance - current;
		else
			curSourceValue = -voltdiff / compResistance;
		// System.out.println("cap " + compResistance + " " + curSourceValue +
		// " " + current + " " + voltdiff);
	}

	void calculateCurrent() {
		double voltdiff = volts[0] - volts[1];
		// we check compResistance because this might get called
		// before stamp(), which sets compResistance, causing
		// infinite current
		if (compResistance > 0)
			current = voltdiff / compResistance + curSourceValue;
	}

	double curSourceValue;

	void doStep() {
		sim.stampCurrentSource(nodes[0], nodes[1], curSourceValue);
	}

	void getInfo(String arr[]) {
		arr[0] = "capacitor";
		// getBasicInfo(arr);
		arr[1] = "C = " + getUnitText(capacitance, "F");
		arr[2] = "Vd = " + getVoltageDText(getVoltageDiff());
		double v = getVoltageDiff();
		arr[3] = "Q = " + getUnitText(capacitance * v, "C");
		// arr[4] = "Q = " + getUnitText(getPower(), "W");
		arr[4] = "U = " + getUnitText(.5 * capacitance * v * v, "J");
	}

	public EditInfo getEditInfo(int n) {
		if (n == 0)
			return new EditInfo("Capacitance (F)", capacitance, 0, 0);
		if (n == 1) {
			EditInfo ei = new EditInfo("", 0, -1, -1);
			ei.checkbox = new Checkbox("Trapezoidal Approximation",
					isTrapezoidal());
			return ei;
		}
		if (n == 2) {
			EditInfo ei = new EditInfo("", 0, -1, -1);
			ei.checkbox = new Checkbox("Variable", variable);
			return ei;
		}
		if (variable){
			if (n == 3)
				return new EditInfo("Min Capacitance", minValue, capacitance/10, capacitance*0.99);
			if (n == 4)
				return new EditInfo("Max Capacitance", maxValue, capacitance*1.01, capacitance*10);
			else 
				return null;
		}
		return null;
	}

	public void setEditValue(int n, EditInfo ei) {
		if (n == 0 && ei.value > 0)
			capacitance = ei.value;
		if (n == 1) {
			if (ei.checkbox.getState())
				flags &= ~FLAG_BACK_EULER;
			else
				flags |= FLAG_BACK_EULER;
		}
		if (n==2){
			variable =ei.checkbox.getState();
			ei.newDialog=true;
			setPoints();
		}
		if (variable){
			 if (n == 3)
				setMinValue(ei.value);
			 if (n == 4)
				setMaxValue(ei.value);
		}
	}
	void setValue(double val){
		super.setValue(val);
		capacitance=val;
		sim.analyzeFlag = true;
		//reset();
		//setPoints();
	}
	int getShortcut() {
		return 'c';
	}
}
