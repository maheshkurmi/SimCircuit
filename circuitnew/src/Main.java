import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.CheckboxMenuItem;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.Scrollbar;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import java.awt.*;
import java.util.Random;
import java.util.Vector;

import javax.swing.*;

import resources.Icons;

public class Main extends JFrame {
	Thread engine = null;

	Dimension winSize;
	Image dbimage;

	Random random;
	public static final int sourceRadius = 7;
	public static final double freqMult = 3.14159265 * 2 * 4;

	public String getAppletInfo() {
		return "Circuit Simulator";
	}

	static Container main;
	JPanel pnlMain ;
	Label titleLabel;
	Button resetButton;
	Button dumpMatrixButton;
	MenuItem exportItem, exportLinkItem, importItem, exitItem, undoItem,
			redoItem, cutItem, copyItem, pasteItem, selectAllItem, optionsItem;
	Menu optionsMenu;
	Checkbox stoppedCheck;
	CheckboxMenuItem dotsCheckItem;
	CheckboxMenuItem voltsCheckItem;
	CheckboxMenuItem powerCheckItem;
	CheckboxMenuItem smallGridCheckItem;
	CheckboxMenuItem showValuesCheckItem;
	CheckboxMenuItem conductanceCheckItem;
	CheckboxMenuItem euroResistorCheckItem;
	CheckboxMenuItem printableCheckItem;
	CheckboxMenuItem conventionCheckItem;
	Scrollbar speedBar;
	Scrollbar currentBar;
	Label powerLabel;
	Scrollbar powerBar;
	PopupMenu elmMenu;
	MenuItem elmEditMenuItem;
	MenuItem elmCutMenuItem;
	MenuItem elmCopyMenuItem;
	MenuItem elmDeleteMenuItem;
	MenuItem elmScopeMenuItem;
	PopupMenu scopeMenu;
	PopupMenu transScopeMenu;
	PopupMenu mainMenu;
	CheckboxMenuItem scopeVMenuItem;
	CheckboxMenuItem scopeIMenuItem;
	CheckboxMenuItem scopeMaxMenuItem;
	CheckboxMenuItem scopeMinMenuItem;
	CheckboxMenuItem scopeFreqMenuItem;
	CheckboxMenuItem scopePowerMenuItem;
	CheckboxMenuItem scopeIbMenuItem;
	CheckboxMenuItem scopeIcMenuItem;
	CheckboxMenuItem scopeIeMenuItem;
	CheckboxMenuItem scopeVbeMenuItem;
	CheckboxMenuItem scopeVbcMenuItem;
	CheckboxMenuItem scopeVceMenuItem;
	CheckboxMenuItem scopeVIMenuItem;
	CheckboxMenuItem scopeXYMenuItem;
	CheckboxMenuItem scopeResistMenuItem;
	CheckboxMenuItem scopeVceIcMenuItem;
	MenuItem scopeSelectYMenuItem;
	Class addingClass;
	int mouseMode = MODE_SELECT;
	int tempMouseMode = MODE_SELECT;
	String mouseModeStr = "Select";
	static final double pi = 3.14159265358979323846;
	static final int MODE_ADD_ELM = 0;
	static final int MODE_DRAG_ALL = 1;
	static final int MODE_DRAG_ROW = 2;
	static final int MODE_DRAG_COLUMN = 3;
	static final int MODE_DRAG_SELECTED = 4;
	static final int MODE_DRAG_POST = 5;
	static final int MODE_SELECT = 6;
	static final int infoWidth = 120;
	int dragX, dragY, initDragX, initDragY;
	int selectedSource;
	Rectangle selectedArea;
	int gridSize, gridMask, gridRound;
	boolean dragging;
	boolean analyzeFlag;
	boolean dumpMatrix;
	boolean useBufferedImage;
	boolean isMac;
	String ctrlMetaKey;
	double t;
	int pause = 10;
	int scopeSelected = -1;
	int menuScope = -1;
	int hintType = -1, hintItem1, hintItem2;
	String stopMessage;
	double timeStep;
	static final int HINT_LC = 1;
	static final int HINT_RC = 2;
	static final int HINT_3DB_C = 3;
	static final int HINT_TWINT = 4;
	static final int HINT_3DB_L = 5;
	Vector<CircuitElm> elmList;
	// Vector setupList;
	CircuitElm dragElm, menuElm, mouseElm, stopElm;
	boolean didSwitch = false;
	int mousePost = -1;
	CircuitElm plotXElm, plotYElm;
	int draggingPost;
	SwitchElm heldSwitchElm;
	double circuitMatrix[][], circuitRightSide[], origRightSide[],
			origMatrix[][];
	RowInfo circuitRowInfo[];
	int circuitPermute[];
	boolean circuitNonLinear;
	int voltageSourceCount;
	int circuitMatrixSize, circuitMatrixFullSize;
	boolean circuitNeedsMap;
	public boolean useFrame;
	int scopeCount;
	Scope scopes[];
	int scopeColCount[];
	static EditDialog editDialog;
	static ImportExportDialog impDialog, expDialog;
	Class dumpTypes[], shortcuts[];
	static String muString = "u";
	static String ohmString = "ohm";
	String clipboard;
	Rectangle circuitArea;
	int circuitBottom;
	Vector<String> undoStack, redoStack;
   Chess chessPanel = new Chess();
   JButton newGameButton = new JButton("New Game");
   JButton loadGameButton = new JButton("Load Game");
   JButton saveGameButton = new JButton("Save Game");
   JButton exitButton = new JButton("Exit");

   // controls
	JPanel pnlToolBar;
	private JToolBar pnlToolSettings;
	private JToolBar barPreferences;
	private JToolBar barSimulation; 
		
	/** The New File button  */
	private JButton btnNew;
	
	/** The Open File button  */
	private JButton btnOpen;
	
	/** The Save File button  */
	private JButton btnSave;
	
	/** The Print File button  */
	private JButton btnPrint;
	
	/** The start button for the simulation */
	private JButton btnStart;
	
	/** The single step button for the simulation */
	private JButton btnStep;
	
	/** The stop button for the simulation */
	private JButton btnStop;
	
	/** The reset button (only for compiled simulations) */
	private JButton btnReset;
	
	/** The anti-aliasing toggle button */
	private JToggleButton tglAntiAliasing;
	
	/** The bounds toggle button */
	private JToggleButton tglBounds;
	
	private JToggleButton tglMoveBody;
	
	/** The zoom in button */
	private JButton btnZoomIn;
	
	/** The zoom out button */
	private JButton btnZoomOut;
	
	/** The move camera to origin button */
	private JButton btnToOrigin;
	private JToggleButton[] tglEditBody;
   public static void main(String[] args) {
      new Main();
   }

   Main() {
      super("Chess");
      add(chessPanel, BorderLayout.CENTER);

      JPanel buttonPanel = new JPanel();
      buttonPanel.setLayout(new FlowLayout());

      buttonPanel.add(newGameButton);
      buttonPanel.add(loadGameButton);
      buttonPanel.add(saveGameButton);
      buttonPanel.add(exitButton);

      System.out.printf("chessPanel Size before rendering: %s%n", chessPanel.getSize());
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      add(buttonPanel, BorderLayout.SOUTH);
      createToolBar();		
    	 add(this.pnlToolBar,BorderLayout.NORTH);
     // pack();
      System.out.printf("chessPanel Size after rendering: %s%n", chessPanel.getSize());
      setLocationRelativeTo(null);
      setVisible(true);
   }

   private void createToolBar(){
		// create the simulation tool bar
			barSimulation = new JToolBar("toolbar.simulation", JToolBar.HORIZONTAL);
			barSimulation.setFloatable(false);
			this.btnNew = new JButton(Icons.NEW);
			//this.btnNew.addActionListener(this);
			this.btnNew.setActionCommand("new");
			this.btnNew.setToolTipText( "toolbar.simulation.start" );
			
			this.btnOpen = new JButton(Icons.OPEN);
			//this.btnOpen.addActionListener(this);
			this.btnOpen.setActionCommand("open");
			this.btnOpen.setToolTipText( "toolbar.simulation.start" );
			
			this.btnSave = new JButton(Icons.SAVE);
			//this.btnSave.addActionListener(this);
			this.btnSave.setActionCommand("save");
			this.btnSave.setToolTipText( "toolbar.simulation.start" );
			
			this.btnPrint = new JButton(Icons.PRINT);
			//this.btnPrint.addActionListener(this);
			this.btnPrint.setActionCommand("print");
			this.btnPrint.setToolTipText( "toolbar.simulation.start" );
			
			barSimulation.add(this.btnNew);
			barSimulation.add(this.btnOpen);
			barSimulation.add(this.btnSave);
			barSimulation.add(this.btnPrint);
			barSimulation.addSeparator();
			
			this.btnStart = new JButton(Icons.START);
			//this.btnStart.addActionListener(this);
			this.btnStart.setActionCommand("start");
			this.btnStart.setToolTipText("toolbar.simulation.start" );
			
			this.btnStep = new JButton(Icons.STEP);
			//this.btnStep.addActionListener(this);
			this.btnStep.setActionCommand("step");
			this.btnStep.setToolTipText( "toolbar.simulation.step" );
			
			this.btnStop = new JButton(Icons.STOP);
			//this.btnStop.addActionListener(this);
			this.btnStop.setActionCommand("stop");
			this.btnStop.setToolTipText( "toolbar.simulation.stop" );
			
			this.btnReset = new JButton(Icons.RESET);
			//this.btnReset.addActionListener(this);
			this.btnReset.setActionCommand("reset");
			this.btnReset.setToolTipText( "toolbar.simulation.reset" );
			
			this.btnStart.setEnabled(true);
			this.btnStop.setEnabled(false);
			this.btnReset.setEnabled(false);
			
			this.btnZoomIn = new JButton(Icons.ZOOM_IN);
			this.btnZoomIn.setToolTipText( "toolbar.simulation.zoomOut" );
			this.btnZoomIn.setActionCommand("zoom-in");
			//this.btnZoomIn.addActionListener(this);
			
			this.btnZoomOut = new JButton(Icons.ZOOM_OUT);
			this.btnZoomOut.setToolTipText( "toolbar.simulation.zoomIn" );
			this.btnZoomOut.setActionCommand("zoom-out");
			//this.btnZoomOut.addActionListener(this);
			
			this.btnToOrigin = new JButton(Icons.RESET);
			this.btnToOrigin.setToolTipText( "toolbar.simulation.toOrigin" );
			this.btnToOrigin.setActionCommand("to-origin");
			//this.btnToOrigin.addActionListener(this);
			
			barSimulation.add(this.btnZoomIn);
			barSimulation.add(this.btnZoomOut);
			barSimulation.add(this.btnToOrigin);
			barSimulation.addSeparator();
			
			// create the preferences toolbar
			this.tglAntiAliasing = new JToggleButton(Icons.ANTIALIAS);
			this.tglAntiAliasing.setToolTipText( "toolbar.preferences.antialiasing" );
			this.tglAntiAliasing.setActionCommand("aa");
			//this.tglAntiAliasing.addActionListener(this);
			this.tglAntiAliasing.setSelected( true );
			
			barSimulation.add(this.tglAntiAliasing);
			barSimulation.addSeparator();

			this.tglMoveBody= new JToggleButton(Icons.TOOL_MOVE);
			this.tglMoveBody.setActionCommand("tool-move");
			this.tglMoveBody.setToolTipText( "toolbar.preferences.tglMoveBody" );
			//this.tglMoveBody.addActionListener(this);
			this.tglMoveBody.setSelected(true);
			
			barSimulation.add(this.btnStart);
			barSimulation.add(this.btnStep);
			barSimulation.add(this.btnStop);
			barSimulation.add(this.btnReset);
			barSimulation.addSeparator();

			barSimulation.add(this.tglMoveBody);

			//Create Object edit bar
			 barPreferences = new JToolBar("toolbar.preferences", JToolBar.HORIZONTAL);
			barPreferences.setFloatable(false);
			
			this.tglEditBody=new JToggleButton[12];
			for (int i=0; i<12;i++){
				this.tglEditBody[i]=new JToggleButton();
				switch(i){
				case 0:
					this.tglEditBody[i].setIcon(Icons.WIRE);
					break;
				case 1:	
					this.tglEditBody[i].setIcon(Icons.RESISTOR);
					break;
				case 2:	
					this.tglEditBody[i].setIcon(Icons.CAPACITOR);
					break;
				case 3:	
					this.tglEditBody[i].setIcon(Icons.INDUCTOR);
					break;
				case 4:	
					this.tglEditBody[i].setIcon(Icons.BATTERY);
					break;
				case 5:	
					this.tglEditBody[i].setIcon(Icons.CURRENTSOURCE);
					break;
				case 6:	
					this.tglEditBody[i].setIcon(Icons.ACSOURCE);
					break;
				case 7:	
					this.tglEditBody[i].setIcon(Icons.GROUND);
					break;
				case 8:	
					this.tglEditBody[i].setIcon(Icons.SWITCH1);
					break;
				case 9:	
					this.tglEditBody[i].setIcon(Icons.SWITCH2);
					break;
				default:
					this.tglEditBody[i].setIcon(Icons.WIRE);
					break;
				}
			}
			
			for (int i=0;i<12;i++){
				barPreferences.add(this.tglEditBody[i]);
				if (i==3||i==6||i==8)barPreferences.addSeparator();
			}
			
			 pnlToolBar = new JPanel();
				pnlToolBar.setLayout(new GridLayout(2, 1));
				pnlToolBar.add(barSimulation);
				pnlToolBar.add(barPreferences);

		}
   // ... Code ...
}

@SuppressWarnings("serial")
class Chess extends JPanel {
   private static final int CHESS_WIDTH = 600;
   private static final int CHESS_HEIGHT = CHESS_WIDTH;
   private static final int MAX_ROW = 8;
   private static final int MAX_COL = 8;
   private static final Color LIGHT_COLOR = new Color(240, 190, 40);
   private static final Color DARK_COLOR = new Color(180, 50, 0);

   @Override
   public Dimension getPreferredSize() {
      return new Dimension(CHESS_WIDTH, CHESS_HEIGHT);
   }

   @Override
   protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      int panelWidth = getWidth();
      int panelHeight = getHeight();
      int sqrWidth = panelWidth / MAX_ROW;
      int sqrHeight = panelHeight / MAX_COL;
      for (int row = 0; row < MAX_ROW; row++) {
         for (int col = 0; col < MAX_COL; col++) {
            Color c = (row % 2 == col % 2) ? LIGHT_COLOR : DARK_COLOR;
            g.setColor(c);
            int x = (row * panelWidth) / MAX_ROW;
            int y = (col * panelHeight) / MAX_COL;
            g.fillRect(x, y, sqrWidth, sqrHeight);
         }
      }
   }
   
   
   
}