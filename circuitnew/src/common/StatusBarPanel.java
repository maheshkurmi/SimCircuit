package common;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/**
 *  Status Bar
 *
 *  @author 	Mahesh kurmi
 */
public class StatusBarPanel extends JPanel
{
	/**
	 *	Standard Status Bar
	 */
	public StatusBarPanel()
	{
		this(false);
	}	//	StatusBar

	/**
	 *	Status Bar with additional info
	 *  @param withInfo with info
	 */
	public StatusBarPanel (boolean withInfo)
	{
		super();
		jbInit();
		this.setName("statusBar");
		if ( withInfo)
			infoLine.setVisible(false);
		// this.setMaximumSize(getPreferredSize());
		this.setBorder(BorderFactory.createEtchedBorder());

	//	 this.setMinimumSize(getPreferredSize());
			
	}	//	StatusBar

	private BorderLayout mainLayout = new BorderLayout();
	private statusLabel statusLine = new statusLabel();
	private JLabel statusDB = new JLabel();
	private JLabel infoLine = new JLabel();
	//
	private boolean		mt_error;
	private String		mt_text;
	//
	private String      m_text;
	
	/**
	 *	Static Init
	 *  @throws Exception
	 */
	private void jbInit()  
	{
		// statusLine.setBorder(BorderFactory.createEtchedBorder());//BorderFactory.createLineBorder(Color.gray));
		statusLine.setText("For any query  : maheshkurmi2010@gmail.com");
		statusLine.setOpaque(false);
		statusDB.setForeground(Color.blue);
		//statusDB.setBorder(BorderFactory.createEtchedBorder());
		statusDB.setText("# Mahesh");
		statusDB.setOpaque(false);
		//infoLine.setBorder(BorderFactory.createRaisedBevelBorder());
		//infoLine.setHorizontalAlignment(SwingConstants.CENTER);
		//infoLine.setHorizontalTextPosition(SwingConstants.CENTER);
		infoLine.setText("info");
		infoLine.setOpaque(false);
		this.setLayout(mainLayout);
		mainLayout.setHgap(1);
		mainLayout.setVgap(1);
	
		//this.add(statusDB, BorderLayout.EAST);
		//this.add(infoLine, BorderLayout.WEST);	
		this.add(statusLine, BorderLayout.CENTER);
	}	//	jbInit

	
	/**************************************************************************
	 *	Set Standard Status Line (non error)
	 *  @param text text
	 */
	public void setStatusLine (String text)
	{
		if (text == null)
			setStatusLine("", false);
		else
			setStatusLine(text, false);
	}	//	setStatusLine

	/**
	 *	Set Status Line
	 *  @param text text
	 *  @param error error
	 */
	public void setStatusLine (String text, boolean error)
	{
		mt_error = error;
		mt_text = text;
		if (mt_error)
			statusLine.setForeground(Color.red);
		else
			statusLine.setForeground(Color.black);
		statusLine.setText(" " + mt_text);
		//
		Thread.yield();
	}	//	setStatusLine

	/**
	 *	Get Status Line text
	 *  @return StatusLine text
	 */
	public String getStatusLine ()
	{
		return statusLine.getText().trim();
	}	//	setStatusLine

	/**
	 *  Set ToolTip of StatusLine
	 *  @param tip tip
	 */
	public void setStatusToolTip (String tip)
	{
		statusLine.setToolTipText(tip);
	}   //  setStatusToolTip

	/**
	 *	Set Status DB Info
	 *  @param text text
	 *  @param dse data status event
	 */
	public void setStatusDB (String text)
	{
	//	log.config( "StatusBar.setStatusDB - " + text + " - " + created + "/" + createdBy);
		if (text == null || text.length() == 0)
		{
			statusDB.setText("");
			statusDB.setVisible(false);
		}
		else
		{
			StringBuffer sb = new StringBuffer ("45/57/657 ");
			sb.append(text).append(" ");
			statusDB.setText(sb.toString());
			if (!statusDB.isVisible())
				statusDB.setVisible(true);
		}

		//  Save
		m_text = text;
	}	//	setStatusDB

	
	/**
	 *	Set Info Line
	 *  @param text text
	 */
	public void setInfo (String text)
	{
		if (!infoLine.isVisible())
			infoLine.setVisible(true);
		infoLine.setText(text);
	}	//	setInfo

	/**
	 *	Add Component to East of StatusBar
	 *  @param component component
	 */
	public void addStatusComponent (JComponent component)
	{
		this.add(component, BorderLayout.EAST);
	}   //  addStatusComponent

	
	class statusLabel extends JLabel {
		public statusLabel(){
			super();
			setBorder(null);
			setForeground(Color.DARK_GRAY);
		}
		
		public void setText(String s){
			super.setText(s);
			setForeground(Color.DARK_GRAY);
		}
		@Override 
		public void paint(Graphics g){
			super.paint(g);
			g.setColor(Color.GRAY);
			g.drawLine(getWidth()-2, 2, getWidth()-2, getHeight()-3);
			g.setColor(Color.white);
			g.drawLine(getWidth()-1, 1, getWidth()-1, getHeight()-2);
			
		}
	}
		

}	//	StatusBar

 
