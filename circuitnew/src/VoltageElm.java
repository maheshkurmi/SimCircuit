import java.awt.*;
import java.util.StringTokenizer;

import mathparser.Calculable;
import mathparser.ExpressionBuilder;
import mathparser.UnknownFunctionException;
import mathparser.UnparsableExpressionException;

class VoltageElm extends CircuitElm {
	static final int FLAG_COS = 2;
	int waveform;
	static final int WF_DC = 0;
	static final int WF_AC = 1;
	static final int WF_SQUARE = 2;
	static final int WF_TRIANGLE = 3;
	static final int WF_SAWTOOTH = 4;
	static final int WF_PULSE = 5;
	static final int WF_VAR = 6;
	
	double frequency, maxVoltage, freqTimeZero, bias, phaseShift, dutyCycle;
	/**
	 * parser for variable voltage
	 */
	private  Calculable calc;
	/**
	 * expression for variable voltage
	 */
	private  String exprt="5*sin(100*t)";
	
	/**
	 * polygon for arrow in variable battery
	 */
	Polygon arrowPoly;
	Point ptArrow1,ptArrow2;
	
	VoltageElm(int xx, int yy, int wf) {
		super(xx, yy);
		waveform = wf;
		maxVoltage = 5;
		frequency = 40;
		dutyCycle = .5;
		value=maxVoltage;
		minValue=maxVoltage-5;
		maxValue=maxVoltage+5;
		reset();
	}

	public VoltageElm(int xa, int ya, int xb, int yb, int f, StringTokenizer st) {
		super(xa, ya, xb, yb, f);
		maxVoltage = 5;
		frequency = 40;
		waveform = WF_DC;
		dutyCycle = .5;
		try {
			waveform = new Integer(st.nextToken()).intValue();
			frequency = new Double(st.nextToken()).doubleValue();
			maxVoltage = new Double(st.nextToken()).doubleValue();
			bias = new Double(st.nextToken()).doubleValue();
			phaseShift = new Double(st.nextToken()).doubleValue();
			dutyCycle = new Double(st.nextToken()).doubleValue();
			variable=new Boolean(st.nextToken()).booleanValue();
			minValue = new Double(st.nextToken()).doubleValue();
			maxValue = new Double(st.nextToken()).doubleValue();
			exprt=st.nextToken();
		} catch (Exception e) {
		}
		if ((flags & FLAG_COS) != 0) {
			flags &= ~FLAG_COS;
			phaseShift = pi / 2;
		}
		value=maxVoltage;	
		if (minValue>value)minValue=maxVoltage-5;
		if (maxValue<value)maxValue=maxVoltage+5;
		if (waveform == WF_VAR){
			//parse expression in t
			try {
				calc = new ExpressionBuilder(exprt).withVariableNames("t")
							.build();
			} catch (Exception e) {
				waveform=WF_DC;
			}
		}
		reset();
	}

	int getDumpType() {
		return 'v';
	}

	String dump() {
		return super.dump() + " " + waveform + " " + frequency + " "
				+ maxVoltage + " " + bias + " " + phaseShift + " " + dutyCycle + " " + variable + " " + minValue + " " + maxValue+ " " + exprt;
	}

	/*
	 * void setCurrent(double c) { current = c;
	 * System.out.print("v current set to " + c + "\n"); }
	 */

	void reset() {
		freqTimeZero = 0;
		curcount = 0;
	}

	double triangleFunc(double x) {
		if (x < pi)
			return x * (2 / pi) - 1;
		return 1 - (x - pi) * (2 / pi);
	}

	void stamp() {
		if (waveform == WF_DC)
			sim.stampVoltageSource(nodes[0], nodes[1], voltSource, getVoltage());
		else
			sim.stampVoltageSource(nodes[0], nodes[1], voltSource);
	}

	void doStep() {
		if (waveform != WF_DC)
			sim.updateVoltageSource(nodes[0], nodes[1], voltSource,
					getVoltage());
	}

	double getVoltage() {
		double w = 2 * pi * (sim.t - freqTimeZero) * frequency + phaseShift;
		switch (waveform) {
		case WF_DC:
			return maxVoltage + bias;
		case WF_AC:
			return Math.sin(w) * maxVoltage + bias;
		case WF_SQUARE:
			return bias
					+ ((w % (2 * pi) > (2 * pi * dutyCycle)) ? -maxVoltage
							: maxVoltage);
		case WF_TRIANGLE:
			return bias + triangleFunc(w % (2 * pi)) * maxVoltage;
		case WF_SAWTOOTH:
			return bias + (w % (2 * pi)) * (maxVoltage / pi) - maxVoltage;
		case WF_PULSE:
			return ((w % (2 * pi)) < 1) ? maxVoltage + bias : bias;
		case WF_VAR:
			return getCustomVoltage((sim.t - freqTimeZero));
		
		default:
			return 0;
		}
	}

	double getCustomVoltage(double time){
		calc.setVariable("t", time);
		return calc.calculate();
	}
	
	final int circleSize = 17;

	void setPoints() {
		super.setPoints();
		if (variable){
			calcLeads(30);
			Point p1=new Point();
			ptArrow1=new Point();
			Point p2=new Point();
			ptArrow2=new Point();
			interpPoint2(lead1, lead2, p1, ptArrow1, 0, 12);
			interpPoint2(lead2, lead1, p2,ptArrow2, 0, 12);
			arrowPoly = calcArrow(ptArrow1,ptArrow2, 6, 4);
		}
		calcLeads((waveform == WF_DC ) ? 8
				: circleSize * 2);
	}

	void draw(Graphics g) {
		setBbox(x, y, x2, y2);
		draw2Leads(g);
		if (waveform == WF_DC) {
			setPowerColor(g, false);
			setVoltageColor(g, volts[0]);
			interpPoint2(lead1, lead2, ps1, ps2, 0, 10);
			drawThickLine(g, ps1, ps2);
			setVoltageColor(g, volts[1]);
			int hs = 16;
			setBbox(point1, point2, hs);
			interpPoint2(lead1, lead2, ps1, ps2, 1, hs);
			drawThickLine(g, ps1, ps2);
			if (variable){
				g.drawLine(ptArrow1.x,ptArrow1.y,ptArrow2.x,ptArrow2.y);
				g.fillPolygon(arrowPoly);
			}
			if (sim.showValuesCheckItem.getState()) {
				String s = getUnitText(getVoltage(), "V");
				drawValues(g, s, hs+1);
			}
		} else {
			setBbox(point1, point2, circleSize);
			interpPoint(lead1, lead2, ps1, .5);
			drawWaveform(g, ps1);
		}
		updateDotCount();
		if (sim.dragElm != this) {
			if (waveform == WF_DC)
				drawDots(g, point1, point2, curcount);
			else {
				drawDots(g, point1, lead1, curcount);
				drawDots(g, point2, lead2, -curcount);
			}
		}
		drawPosts(g);
	}

	void drawWaveform(Graphics g, Point center) {
		g.setColor(needsHighlight() ? selectColor : Color.gray);
		setPowerColor(g, false);
		int xc = center.x;
		int yc = center.y;
		drawThickCircle(g, xc, yc, circleSize);
		int wl = 8;
		adjustBbox(xc - circleSize, yc - circleSize, xc + circleSize, yc
				+ circleSize);
		int xc2;
		switch (waveform) {
		case WF_DC: {
			break;
		}
		case WF_SQUARE:
			xc2 = (int) (wl * 2 * dutyCycle - wl + xc);
			xc2 = max(xc - wl + 3, min(xc + wl - 3, xc2));
			drawThickLine(g, xc - wl, yc - wl, xc - wl, yc);
			drawThickLine(g, xc - wl, yc - wl, xc2, yc - wl);
			drawThickLine(g, xc2, yc - wl, xc2, yc + wl);
			drawThickLine(g, xc + wl, yc + wl, xc2, yc + wl);
			drawThickLine(g, xc + wl, yc, xc + wl, yc + wl);
			break;
		case WF_PULSE:
			yc += wl / 2;
			drawThickLine(g, xc - wl, yc - wl, xc - wl, yc);
			drawThickLine(g, xc - wl, yc - wl, xc - wl / 2, yc - wl);
			drawThickLine(g, xc - wl / 2, yc - wl, xc - wl / 2, yc);
			drawThickLine(g, xc - wl / 2, yc, xc + wl, yc);
			break;
		case WF_SAWTOOTH:
			drawThickLine(g, xc, yc - wl, xc - wl, yc);
			drawThickLine(g, xc, yc - wl, xc, yc + wl);
			drawThickLine(g, xc, yc + wl, xc + wl, yc);
			break;
		case WF_TRIANGLE: {
			int xl = 5;
			drawThickLine(g, xc - xl * 2, yc, xc - xl, yc - wl);
			drawThickLine(g, xc - xl, yc - wl, xc, yc);
			drawThickLine(g, xc, yc, xc + xl, yc + wl);
			drawThickLine(g, xc + xl, yc + wl, xc + xl * 2, yc);
			break;
		}
		case WF_AC: {
			int i;
			int xl = 10;
			int ox = -1, oy = -1;
			for (i = -xl; i <= xl; i++) {
				int yy = yc + (int) (.95 * Math.sin(i * pi / xl) * wl);
				if (ox != -1)
					drawThickLine(g, ox, oy, xc + i, yy);
				ox = xc + i;
				oy = yy;
			}
			break;
		}
		case WF_VAR: {
			drawThickLine(g, xc-3 , yc+5, xc -7, yc -6);
			drawThickLine(g, xc-3, yc+5, xc +1, yc -6);
			g.drawString("(t)",xc+2,yc+8);
		//	drawValues(g, "V(t)", 5);
			break;
		}
		}
		if (sim.showValuesCheckItem.getState()) {
			String s = (waveform==WF_VAR?exprt:getShortUnitText(frequency, "Hz"));
			if (dx == 0 || dy == 0)
				drawValues(g, s, circleSize);
		}
	}

	int getVoltageSourceCount() {
		return 1;
	}

	double getPower() {
		return -getVoltageDiff() * current;
	}

	double getVoltageDiff() {
		return volts[1] - volts[0];
	}

	void getInfo(String arr[]) {
		switch (waveform) {
		case WF_DC:
			arr[0] = "voltage source";
			break;
		case WF_VAR:
			arr[0] = "V(t) = " +exprt;
			break;
		case WF_AC:
			arr[0] = "A/C source";
			break;
		case WF_SQUARE:
			arr[0] = "square wave gen";
			break;
		case WF_PULSE:
			arr[0] = "pulse gen";
			break;
		case WF_SAWTOOTH:
			arr[0] = "sawtooth gen";
			break;
		case WF_TRIANGLE:
			arr[0] = "triangle gen";
			break;
		}
		arr[1] = "I = " + getCurrentText(getCurrent());
		arr[2] = ((this instanceof RailElm) ? "V = " : "Vd = ")
				+ getVoltageText(getVoltageDiff());
		if (waveform != WF_DC && waveform != WF_VAR ) {
			arr[3] = "f = " + getUnitText(frequency, "Hz");
			arr[4] = "Vmax = " + getVoltageText(maxVoltage);
			int i = 5;
			if (bias != 0)
				arr[i++] = "Voff = " + getVoltageText(bias);
			else if (frequency > 500)
				arr[i++] = "wavelength = "
						+ getUnitText(2.9979e8 / frequency, "m");
			arr[i++] = "P = " + getUnitText(getPower(), "W");
		}
	}

	public EditInfo getEditInfo(int n) {
		if (n == 0){
			if (waveform == WF_VAR){
				EditInfo ei = new EditInfo("V(t)=", 0, -1, -1);
				ei.text = exprt;
				return ei;
			}
	
			return new EditInfo(waveform == WF_DC ? "Voltage" : "Max Voltage",
					maxVoltage, -20, 20);
		}
		if (n == 1) {
			EditInfo ei = new EditInfo("Waveform", waveform, -1, -1);
			ei.choice = new Choice();
			ei.choice.add("D/C");
			ei.choice.add("A/C");
			ei.choice.add("Square Wave");
			ei.choice.add("Triangle");
			ei.choice.add("Sawtooth");
			ei.choice.add("Pulse");
			ei.choice.add("Custom");
			ei.choice.select(waveform);
			return ei;
		}
		if (waveform == WF_DC){
			if (n==2){
				EditInfo ei = new EditInfo("", 0, -1, -1);
				ei.checkbox = new Checkbox("Variable", variable);
				return ei;
			}else if (variable){
				if (n == 3)
					return new EditInfo("Min Voltage", minValue, maxVoltage-10, maxVoltage*0.99);
				if (n == 4)
					return new EditInfo("Max Voltage", maxValue, maxVoltage, maxVoltage+10);
				else 
					return null;
				
			}
			    return null;
		}
		if (waveform==WF_VAR){
			return null;
		}
	
		if (n == 2)
			return new EditInfo("Frequency (Hz)", frequency, 4, 500);
		if (n == 3)
			return new EditInfo("DC Offset (V)", bias, -20, 20);
		if (n == 4)
			return new EditInfo("Phase Offset (degrees)",
					phaseShift * 180 / pi, -180, 180).setDimensionless();
		if (n == 5 && waveform == WF_SQUARE)
			return new EditInfo("Duty Cycle", dutyCycle * 100, 0, 100)
					.setDimensionless();
		return null;
	}

	public void setEditValue(int n, EditInfo ei) {
		if (n == 0){
			if (waveform == WF_VAR){
				//parse expression in t
				try {
					calc = new ExpressionBuilder(ei.textf.getText()).withVariableNames("t")
								.build();
					exprt=ei.textf.getText();
					reset();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					ei.text=exprt;
				}
			}else{
				setValue(ei.value);
			}
		}
		if (n == 2) {
			if (waveform == WF_DC){
				variable =ei.checkbox.getState();
				ei.newDialog = true;
				setPoints();
			}
			// adjust time zero to maintain continuity in the waveform
			// even though the frequency has changed.
			double oldfreq = frequency;
			frequency = ei.value;
			double maxfreq = 1 / (8 * sim.timeStep);
			if (frequency > maxfreq)
				frequency = maxfreq;
			double adj = frequency - oldfreq;
			freqTimeZero = sim.t - oldfreq * (sim.t - freqTimeZero) / frequency;
		}
		if (n == 1) {
			int ow = waveform;
			waveform = ei.choice.getSelectedIndex();
			if (waveform == WF_DC  && ow != WF_DC) {
				ei.newDialog = true;
				bias = 0;
			} else if (waveform != WF_DC && ow == WF_DC) {
				ei.newDialog = true;
			}
			if ((waveform == WF_SQUARE || ow == WF_SQUARE ) && waveform != ow)
				ei.newDialog = true;
			if ((waveform == WF_VAR|| ow == WF_VAR ) && waveform != ow){
				ei.newDialog = true;
			}
			
			if (waveform == WF_VAR){
				try {
					calc = new ExpressionBuilder(exprt).withVariableNames("t")
							.build();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
			setPoints();
		}
		if (variable){
			 if (n == 3)
				setMinValue(ei.value);
			 if (n == 4)
				setMaxValue(ei.value);
			 return;
		}
		if (n == 3)
			bias = ei.value;

		if (n == 4)
			phaseShift = ei.value * pi / 180;
		if (n == 5)
			dutyCycle = ei.value * .01;
	}
	
	void setValue(double val){
		super.setValue(val);
		maxVoltage=val;
		sim.analyzeFlag = true;
		reset();
		//setPoints();
	}
}
