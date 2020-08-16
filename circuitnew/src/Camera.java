import java.awt.Point;
import java.awt.geom.AffineTransform;


/**
 * Class used to encapsulate camera properties.
 * @author mahesh kurrmi
 * @version 1.0.0
 * @since 1.0.0
 */
public class Camera {
	/** The zoom factor */
	protected double scale=1.0;
	
	/** The translation from 0,0 */
	protected int transX=0, transY=0;
	
	/** The Simulator*/
	private CirSim simulation;
	/**
	 * Default constructor.
	 * <p>
	 * Defaults to a 1 to 1 scale and zero translation.
	 */
	public Camera(CirSim simulation) {
		this.simulation=simulation;
	}
	
	
	/**
	 * Full constructor.
	 * @param simulation
	 * @param scale the zoom factor 
	 * @param transX
	 * @param transY
	 */
	
	public Camera(CirSim simulation,double scale, int transX,int transY) {
		this(simulation);
		this.scale = scale;
		this.transX = transX;
		this.transY = transY;
	}
	
	/**
	 * Zooms out the camera.
	 */
	public void zoomOut() {
		if(scale>10)return;
		this.scale *= 1.041666667;
	}
	
	/**
	 * Zooms in the camera.
	 */
	public void zoomIn() {
		if(scale<0.1)return;
		this.scale *= 0.96;
	}
	
	/**
	 * Moves the camera back to the origin.
	 */
	public void toOrigin() {
		this.transX=0;
		this.transY=0;
	}
	
	/**
	 * Translates the camera the given amount along the x and y axes.
	 * @param tx the translation
	 */
	public void translate(double tx, double ty) {
		//this.transX+= tx;
		//this.transY += ty;

	}
	
	
	public void inverseTransform(Point pt){
		pt.setLocation((pt.x/scale-transX),(pt.y/scale-transY));
	}
	
	// getter/setters
	
	public AffineTransform getTransform(){
		AffineTransform at=new AffineTransform();
		at.setToTranslation(transX, transY);		
		at.scale(scale, scale);

		return at;
	}
	/**
	 * Returns the scale factor.
	 * @return double
	 */
	public double getScale() {
		return scale;
	}
	
	/**
	 * Sets the scale factor in pixels per meter.
	 * @param scale the desired scale factor
	 */
	public void setScale(double scale) {
		this.scale = scale;
	}
	
	/**
	 * Returns the translation.
	 * @return Vector2
	 */
	public double getTranslationX() {
		return transX;
	}
	
	public double getTranslationY() {
		return transY;
	}
	/**
	 * Sets the translation from the origin in world coordinates.
	 * @param translation the translation
	 */
	public void setTranslation(int tx, int ty) {
		this.transX= tx;
		this.transY = ty;
	}
	


}
