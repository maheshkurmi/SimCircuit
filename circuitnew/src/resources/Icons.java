
package resources;

import java.awt.Color;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import utils.ImageUtils;

/**
 * Class storing all the icons used by the application.
 */
public class Icons {
	// sandbox icons
	public static Icon getColorIcon(Color color){
		return new ColorIcon(color);
	}
	
		
	/** New icon */
	public static final ImageIcon NEW = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/new-icon.png");
	
	/** Open icon */
	public static final ImageIcon OPEN = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/open-icon.png");
	
	/** Save icon */
	public static final ImageIcon SAVE = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/save-icon.png");

	/** Save As.. icon */
	public static final ImageIcon SAVEAS = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/saveas-icon.png");
	
	/** Export as Image icon */
	public static final ImageIcon EXPORT = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/export-icon.png");
	
	/** Print icon */
	public static final ImageIcon PRINT = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/print-icon.png");
	
	/** Look and feel icon */
	public static final ImageIcon LOOKANDFEEL = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/lookandfeel-icon.png");
	
	
	/** toolbar icon */
	public static final ImageIcon TOOLBAR = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/toolbar-icon.png");
	
	/** statusbar icon */
	public static final ImageIcon STATUSBAR = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/statusbar-icon.png");
	
	/** Prefereneces icon */
	public static final ImageIcon PREFERENCES = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/preferences-icon.png");
	
	/** Check icon */
    public static final ImageIcon CHECK = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/check-icon.png");

    /** Content icon */
    public static final ImageIcon CONTENT = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/content-icon.png");

      /** Goto Home Page icon */
    public static final ImageIcon GOTOHOMEPAGE = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/gotohomepage-icon.png");

    /** CheckforUpdates icon */
    public static final ImageIcon CHECKUPDATE = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/checkupdate-icon.png");
    
    /** About icon */
    public static final ImageIcon ABOUT = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/about-icon.png");

    /** edit icon */
	public static final ImageIcon EDIT = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/edit-icon.png");
	
	/** Generic remove icon */
	public static final ImageIcon DELETE = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/delete-icon.png");
	
	/** Generic remove icon */
	public static final ImageIcon CUT = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/cut-icon.png");
	
	/** Generic remove icon */
	public static final ImageIcon COPY = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/copy-icon.png");
	
	/** Generic remove icon */
	public static final ImageIcon PASTE = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/paste-icon.png");
	
    /** Zoom in icon */
	public static final ImageIcon ZOOM_IN = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/zoom-in-icon.png");
	
	/** Zoom out icon */
	public static final ImageIcon ZOOM_OUT = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/zoom-out-icon.png");
	
	/** undo icon */
	public static final ImageIcon UNDO = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/undo-icon.png");
	
	/** redo icon */
	public static final ImageIcon REDO = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/redo-icon.png");
	
	/** Start/resume icon */
	public static final ImageIcon START = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/start-icon.png");
	
	/** Step icon */
	public static final ImageIcon STEP = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/step-icon.png");
	
	/** Stop icon */
	public static final ImageIcon STOP = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/stop-icon.png");

	/** Reset icon */
	public static final ImageIcon RESET = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/reset-icon.png");
	
	/**Toggle GridXY  icon */
	public static final ImageIcon GRIDXY = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/gridxy-icon.png");
	
	/**Edit_Antialias  Button */
	public static final ImageIcon ANTIALIAS = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/antialias-icon.png");
	
	/** Zoom in icon */
	public static final ImageIcon SPEED_UP = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/speed-up-icon.png");
		
	/** Zoom out icon */
	public static final ImageIcon SPEED_DOWN = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/speed-down-icon.png");
		
	/** Scope icon */
	public static final ImageIcon SCOPE = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/scope-icon.png");
		
	
	/**Home Icon */
	public static final ImageIcon HOME = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/home-icon.png");

	/**Back navigation Icon */
	public static final ImageIcon BACK = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/back-icon.png");

	/**Forward navigation Icon */
	public static final ImageIcon FORWARD = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/forward-icon.png");

	/** Move Tool  icon */
	public static final ImageIcon TOOL_MOVE = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/move-icon.png");

	/**Resistor Icon */
	public static final ImageIcon WIRE = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/wire-icon.png");

	/**Resistor Icon */
	public static final ImageIcon RESISTOR = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/resistor-icon.png");

	/**Resistor Icon */
	public static final ImageIcon CAPACITOR = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/capacitor-icon.png");

	/**Resistor Icon */
	public static final ImageIcon INDUCTOR = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/inductor-icon.png");

	/**Resistor Icon */
	public static final ImageIcon BATTERY = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/battery-icon.png");

	/**Resistor Icon */
	public static final ImageIcon CURRENTSOURCE = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/currentsource-icon.png");

	/**Resistor Icon */
	public static final ImageIcon ACSOURCE = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/acsource-icon.png");

	/**Resistor Icon */
	public static final ImageIcon GROUND = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/ground-icon.png");

	/**Resistor Icon */
	public static final ImageIcon SWITCH1 = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/switch1-icon.png");
	
	/**Resistor Icon */
	public static final ImageIcon SWITCH2 = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/switch2-icon.png");

	/**Resistor Icon */
	public static final ImageIcon VIN = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/vin-icon.png");

	/**Resistor Icon */
	public static final ImageIcon VOUT = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/vout-icon.png");

	/**Resistor Icon */
	public static final ImageIcon AMMETER = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/ammeter-icon.png");

	/**Galv. Icon */
	public static final ImageIcon GALVANOMETER = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/galvanometer-icon.png");

	/**Resistor Icon */
	public static final ImageIcon VOLTMETER = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/voltmeter-icon.png");

	/**Resistor Icon */
	public static final ImageIcon RHOESTAT = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/potentiometer-icon.png");

	/**Resistor Icon */
	public static final ImageIcon TRANSFORMER = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/transformer-icon.png");

	/**Resistor Icon */
	public static final ImageIcon DIODE = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/diode-icon.png");
	/**Resistor Icon */
	public static final ImageIcon BULB = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/light-bulb-icon.png");

	
	/**Resistor Icon */
	//public static final ImageIcon VOUT = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/resistor-icon.png");

	/**\gate Icon */
	public static final ImageIcon OR_GATE = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/or-gate-icon.png");
	/**\and gate Icon */
	public static final ImageIcon AND_GATE = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/and-gate-icon.png");
	/**\gate Icon */
	public static final ImageIcon NOT_GATE = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/not-gate-icon.png");
	/**\gate Icon */
	public static final ImageIcon NOR_GATE = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/nor-gate-icon.png");
	/**\gate Icon */
	public static final ImageIcon NAND_GATE = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/nand-gate-icon.png");
	/**\gate Icon */
	public static final ImageIcon XOR_GATE = ImageUtils.getIconFromClassPathSuppressExceptions("/resources/xor-gate-icon.png");

	
}
