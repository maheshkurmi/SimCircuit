import java.awt.*;
import java.util.StringTokenizer;

import utils.HSLColor;

class BulbElm extends ResistorElm {
	/**rated voltage and power*/
	double nom_pow, nom_v;
	/**rgb components*/
	int col_R,col_G,col_B;
	/**Color of bulb*/
    HSLColor bulbColor;
    
	public BulbElm(int xx, int yy) {
		super(xx, yy);
		//set default values to 100W and 220V
		nom_pow = 100;
		nom_v = 2.5;
		//calculate resistance of bulb R=V2/P
		resistance=nom_v*nom_v/nom_pow;
		bulbColor=new HSLColor(new Color(200,200,20));
		col_R=bulbColor.getRGB().getRed();
		col_G=bulbColor.getRGB().getGreen();
		col_B=bulbColor.getRGB().getBlue();
		
		//initiate bulb in off state
		bulbColor.adjustLuminance(0);
	}

	public BulbElm(int xa, int ya, int xb, int yb, int f, StringTokenizer st) {
		super(xa, ya, xb, yb, f,st);
		nom_pow = new Double(st.nextToken()).doubleValue();
		nom_v = new Double(st.nextToken()).doubleValue();
		//obtain color
		col_R = new Integer(st.nextToken()).intValue();
		col_G = new Integer(st.nextToken()).intValue();
		col_B = new Integer(st.nextToken()).intValue();
		//calculate resistance
		resistance=nom_v*nom_v/nom_pow;
		bulbColor=new HSLColor(new Color(col_R,col_G,col_B));
		bulbColor.adjustLuminance(5);
	}

	String dump() {
		return super.dump() + " " + nom_pow + " " + nom_v + " "
				+ col_R + " " + col_G + " " + col_B;
	}

	int getDumpType() {
		return 181;
	}

	Point bulbLead[], filament[], bulb;
	int bulbR;

	void reset() {
		super.reset();
	}

	final int filament_len = 16;

	void setPoints() {
		super.setPoints();
		int llen = 8;
		calcLeads(llen);
		bulbLead = newPointArray(2);
		filament = newPointArray(2);
		bulbR = 9;
		filament[0] = interpPoint(lead1, lead2, 0, filament_len/2);
		filament[1] = interpPoint(lead1, lead2, 1, filament_len/2);
		double br = filament_len - Math.sqrt(bulbR * bulbR - llen * llen)-2;
		bulbLead[0] = interpPoint(lead1, lead2, 0, br);
		bulbLead[1] = interpPoint(lead1, lead2, 1, br);
		bulb = interpPoint(interpPoint(lead1, lead2, 0, filament_len),interpPoint(lead1, lead2, 1, filament_len), .5);
	}

	void draw(Graphics g) {
		double v1 = volts[0];
		double v2 = volts[1];
		setBbox(point1, point2, 4);
		adjustBbox(bulb.x - bulbR, bulb.y - bulbR, bulb.x + bulbR, bulb.y
				+ bulbR);
		// adjustbbox
		draw2Leads(g);
		setPowerColor(g, true);
		double f=0.9*getPower()/nom_pow;
		if (f>0.95) f=0.95;
		if(f<0) f=0;
		
		g.setColor(bulbColor.adjustLuminance((float) (f*100)));
		g.fillOval(bulb.x - bulbR, bulb.y - bulbR, 2*bulbR , bulbR * 2);
		//float[] dashf={2,2};
		//((Graphics2D)g).setStroke(new BasicStroke(1f,0,0,1f,dashf,0f));  // set dash pattern of width 1
		double r1=bulbR+3;
		double r2=bulbR+3+3*getPower()/nom_pow;
		int x1,y1,x2,y2;
		for (double a = -Math.PI / 8; a < 2 * Math.PI; a += Math.PI / 8) {
			x1 = (int) (bulb.x + r1 * Math.cos(a));
			y1 = (int) (bulb.y - r1 * Math.sin(a));
			x2 = (int) (bulb.x + r2 * Math.cos(a));
			y2 = (int) (bulb.y - r2 * Math.sin(a));
			g.drawLine(x1, y1, x2, y2);
		}
		//((Graphics2D)g).setStroke(new BasicStroke(1f));  // set stroke width of 1
		//g.setColor(lightGrayColor);
		//drawThickCircle(g, bulb.x, bulb.y, bulbR);
		//g.drawOval(bulb.x - bulbR, bulb.y - bulbR, 2*bulbR , bulbR * 2);
		Polygon poly=new Polygon();
		poly.addPoint(lead1.x, lead1.y);
		poly.addPoint(filament[0].x, filament[0].y);
		poly.addPoint(filament[1].x, filament[1].y);	
		poly.addPoint(lead2.x, lead2.y);
		//g.setColor(lightGrayColor);
		g.setColor(Color.gray);//lightGrayColor);
		g.fillPolygon(poly);
		
		g.setColor(lightGrayColor);
		g.setColor(needsHighlight() ? selectColor : lightGrayColor);
		g.drawPolygon(poly);
		g.drawOval(bulb.x - bulbR, bulb.y - bulbR, 2*bulbR , bulbR * 2);
		updateDotCount();
		if (sim.dragElm != this) {
			drawDots(g, point1, lead1, curcount);
			drawDots(g, lead2, point2, curcount);
		}
		drawPosts(g);
	}


	void getInfo(String arr[]) {
		arr[0] = "bulb" +" ( "+getShortUnitText(nom_pow,"W")+" , "+getVoltageText(nom_v) +" ) ";
		getBasicInfo(arr);
		arr[3] = "R = " + getUnitText(resistance, sim.ohmString);
		arr[4] = "P = " + getUnitText(getPower(), "W");
	}

	public EditInfo getEditInfo(int n) {
		// ohmString doesn't work here on linux
		if (n == 0)
			return new EditInfo("Nominal Power", nom_pow, 0, 0);
		if (n == 1)
			return new EditInfo("Nominal Voltage", nom_v, 0, 0);
		if (n == 2){
			EditInfo ei=new EditInfo("Red ", col_R, 1, 255);
			ei.setDimensionless();
			ei.textf=null;
			return ei;
		}
		if (n == 3)
			return new EditInfo("Green ", col_G, 1, 255);
		if (n == 4)
			return new EditInfo("Blue ", col_B, 1, 255);
		return null;
	}

	public void setEditValue(int n, EditInfo ei) {
		if (n == 0 && ei.value > 0)
			nom_pow = ei.value;
		if (n == 1 && ei.value > 0)
			nom_v = ei.value;
		if (n == 2 )
			col_R = (int) ei.value;
		if (n == 3 )
			col_G = (int) ei.value;
		if (n == 4)
			col_B = (int) ei.value;
		bulbColor=new HSLColor(new Color(col_R,col_G,col_B));
		resistance=nom_v*nom_v/nom_pow;
	}
	
	int getShortcut() { return '8'; }
}
