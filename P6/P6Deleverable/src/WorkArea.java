/*
 * Class:		CS225-01DB
 * Author: 		Logan White
 * Created: 	3/22/2023
 * Modified:	3/24/2023 Mathmatical Mehtoods added, checks for choked flow added.
 * 
 * Purpose:	Mathmatics for constant area tubes with friction and no added heat
 * 			Flow is Isentropic in a friction tube,
 * 			VERIFIED, no exit conditions;
 * 
 * Methods: 	+WorkArea()
 * 				#paintComponent(Graphics g): void
 * 				+runFinishedCalcs(): void
 * 				+graphExitCondition(): void
 * 				+clear(): void
 * 				+inputInlet(): void
 * 				+AddDiverg(): void
 * 				+AddConverg(): void
 * 				+AddHeatTub(): void
 * 				+addFricTub(): void
 * 
 *
 * Attributes: 	- PressureIN, TempIN, MachIN, PressureBACK, R, gamma: double
 * 				-nodeX, nodeY: int
 * 				-drawNodes: Graphics2D
 * 				-UI: NozzleUI
 * 				-NodePiece: NodeManager[]
 * 				-image: Image
 *
 */

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class WorkArea extends JComponent{

	//It is all Drawn on an immage for saving later:
	private Image image;
	//grafics 2d objects
	private NodeManager[] NodePiece =  new NodeManager[4];;
	private NozzleUI UI = new NozzleUI();

	private Graphics2D drawNodes;

	private int nodeX, nodeY;
	private double PressureIN, TempIN, MachIN, PressureBACK, R, gamma;
	
	public WorkArea() {
		Toolkit.getDefaultToolkit().getScreenSize();
		setDoubleBuffered(false);

	}

	protected void paintComponent(Graphics g) {

		if (image == null) {

			image = createImage(getSize().width, getSize().height);
			drawNodes = (Graphics2D) image.getGraphics();
			drawNodes.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			//starts by immidiately clearing the area;
			clear();
		}

		g.drawImage(image, 0 , 0 , null);
	}

	public void runFinishedCalcs() {

		double P, T;
		//NodePiece[UI.getNodeCount()].setArea();
		NodePiece[0].setArea(1);
		NodePiece[0].Mach2toMach1(MachIN, PressureIN, PressureBACK, TempIN);
		
		P =  NodePiece[0].getP2toP1() * PressureIN;
		T = NodePiece[0].getT2toT1() * TempIN; 
		
		for(int n =1; n < UI.getNodeCount(); n++) {
			
			
			NodePiece[n].Mach2toMach1(NodePiece[n-1].getMach(1), P, PressureBACK, T);
			
			P =  NodePiece[n].getP2toP1() * P;
			T = NodePiece[n].getT2toT1() * T; 
		}



		drawNodes.drawString("Outlet Conditions:  Exit Pressure: " + P + "  Exit Temp: " + T + "  Exit Mach " + NodePiece[UI.getNodeCount()].getMach(4), 100 , 60 );
		drawNodes.setFont((drawNodes.getFont()).deriveFont((drawNodes.getFont()).getSize() / 1.4F));
		
		graphExitCondition();

	}

	public void graphExitCondition() {
		NodePiece[UI.getNodeCount()].ExitCond();

		drawNodes.setPaint(Color.blue);
		drawNodes.setStroke(new BasicStroke(2));
		//only these 4 require graphing to be shown NodePiece[UI.getNodeCount()].getExitCondition()
		switch(NodePiece[UI.getNodeCount()].getExitCondition()) {
		case "Over Expanded":
			drawNodes.drawLine(getNodeX(), 350 + getNodeY()+3, getNodeX() + 300, 350 + (int) Math.round(getNodeY()*2)+3);
			drawNodes.drawLine(getNodeX(), 350 - getNodeY()-3, getNodeX() + 300, 350 - (int) Math.round(getNodeY()*2)-3);
			drawNodes.drawLine(getNodeX(), 350 + getNodeY()-3, getNodeX() + 300, 350 + (int) Math.round(getNodeY()*2)-3);
			drawNodes.drawLine(getNodeX(), 350 - getNodeY()+3, getNodeX() + 300, 350 - (int) Math.round(getNodeY()*2)+3);
			break;
		case "Perfectly Expanded":
			drawNodes.drawLine(getNodeX(), 350 + getNodeY()+3, getNodeX() + 300, 350 + getNodeY()+3);
			drawNodes.drawLine(getNodeX(), 350 - getNodeY()-3, getNodeX() + 300, 350 - getNodeY()-3);
			drawNodes.drawLine(getNodeX(), 350 + getNodeY()-3, getNodeX() + 300, 350 + getNodeY()-3);
			drawNodes.drawLine(getNodeX(), 350 - getNodeY()+3, getNodeX() + 300, 350 - getNodeY()+3);
			break;
		case "Under Expanded":
			drawNodes.drawLine(getNodeX(), 350 + getNodeY()+3, getNodeX() + 300, 350 - (int) Math.round(getNodeY()*2)+3);
			drawNodes.drawLine(getNodeX(), 350 - getNodeY()-3, getNodeX() + 300, 350 + (int) Math.round(getNodeY()*2)-3);
			drawNodes.drawLine(getNodeX(), 350 + getNodeY()-3, getNodeX() + 300, 350 - (int) Math.round(getNodeY()*2)-3);
			drawNodes.drawLine(getNodeX(), 350 - getNodeY()+3, getNodeX() + 300, 350 + (int) Math.round(getNodeY()*2)+3);
			break;	
		case "Shock at Exit":
			drawNodes.drawLine(getNodeX()+6, 350 + getNodeY()+3, getNodeX() + 21, 350);
			drawNodes.drawLine(getNodeX(), 350 - getNodeY()-3, getNodeX() + 15, 350);
			drawNodes.drawLine(getNodeX(), 350 + getNodeY()-3, getNodeX() + 15, 350);
			drawNodes.drawLine(getNodeX()+6, 350 - getNodeY()+3, getNodeX() + 21, 350);
			break;

		}

		drawNodes.setPaint(Color.black);

	}

	//buttons:
	public void clear() {

		drawNodes.setPaint(Color.white);
		//just fills the board with white to clear it:
		drawNodes.fillRect(0, 0, getSize().width, getSize().height);
		drawNodes.setPaint(Color.white);

		repaint();

		//sets where the nodes start graphing:
		setNodeX(100);
		setNodeY(50);

	}

	public void inputInlet() {

		drawNodes.setStroke(new BasicStroke(10));
		drawNodes.setPaint(Color.black);

		GridLayout experimentLayout = new GridLayout(2,0);
		JPanel getData = new JPanel();

		JTextField PressureINBOX = new JTextField(5);
		JTextField TempINBOX = new JTextField(5);
		JTextField MachINBOX = new JTextField(5);
		JTextField PressureBACKBOX = new JTextField(5);
		JTextField RBOX = new JTextField(5);
		JTextField gammaBOX = new JTextField(5);

		boolean passed;

		getData.setLayout(experimentLayout);
		experimentLayout.setHgap(10);
		experimentLayout.setVgap(15);
		experimentLayout.layoutContainer(getData);
		getData.add(new JLabel("Inlet Static Pressure:"));
		getData.add(PressureINBOX);
		getData.add(new JLabel("Inlet Static Temp:"));
		getData.add(TempINBOX);
		getData.add(new JLabel("Inlet Mach:"));
		getData.add(MachINBOX);		
		getData.add(new JLabel("Atmospheric Pressure:"));
		getData.add(PressureBACKBOX);
		getData.add(new JLabel("R constant:"));
		getData.add(RBOX);
		getData.add(new JLabel("Gamma value:"));
		getData.add(gammaBOX);

		//stock values:
		passed = false;
		PressureIN = 1;
		TempIN = 1;
		MachIN = 1;
		PressureBACK = 1;
		R = 1;
		gamma = 1;

		while (!passed) {

			try {
				JOptionPane.showConfirmDialog(null, getData, "Input data: ", JOptionPane.OK_CANCEL_OPTION);

				PressureIN = Double.parseDouble(PressureINBOX.getText());
				TempIN = Double.parseDouble(TempINBOX.getText());
				MachIN = Double.parseDouble(MachINBOX.getText());
				PressureBACK = Double.parseDouble(PressureBACKBOX.getText());
				R = Double.parseDouble(RBOX.getText());
				gamma = Double.parseDouble(gammaBOX.getText());

				passed = true;
			}
			catch(Exception e){}
		}

		drawNodes.setFont((drawNodes.getFont()).deriveFont((drawNodes.getFont()).getSize() * 1.4F));
		drawNodes.drawString("Atmosphiric Conditions:  Atmospheric Pressure: " + PressureBACK + "  R: " + R + "  Gamma " + gamma, 100,20 );
		drawNodes.drawString("Inlet Conditions:  Inlet Pressure: " + PressureIN + "  Inlet Temp: " + TempIN + "  Inlet Mach " + MachIN, 100 ,40 );
		drawNodes.setFont((drawNodes.getFont()).deriveFont((drawNodes.getFont()).getSize() / 1.4F));
		
		//runs the program after all the final inputs are in:
		runFinishedCalcs();
	}

	//graphing Nozzle Types,
	public void AddDiverg() {

		drawNodes.setStroke(new BasicStroke(10));
		drawNodes.setPaint(Color.black);

		JTextField ratioBOX = new JTextField(5);
		JPanel getData = new JPanel();

		double ratio;
		boolean passed;

		getData.add(new JLabel("Diverging Area Ratio (ratio>1):"));
		getData.add(ratioBOX);

		passed = false;
		ratio = .2;

		while (!passed && ratio < 1) {

			try {

				JOptionPane.showConfirmDialog(null, getData, "Input data: ", JOptionPane.OK_CANCEL_OPTION);

				ratio = Double.parseDouble(ratioBOX.getText());

				passed = true;
			}
			catch(Exception e){}
		}

		drawNodes.drawString("Diverging Nozzle ", getNodeX(),  350 +((int) Math.round(getNodeY()*Math.sqrt(ratio)))+70 );
		drawNodes.drawString("Area Ratio : " + ((double)Math.round(ratio*100)/100), getNodeX(), 350 +((int) Math.round(getNodeY()*Math.sqrt(ratio))) +90 );

		NodePiece[UI.getNodeCount()] = new DivergingNozzle();
		NodePiece[UI.getNodeCount()].setArea2toArea1(ratio);


		drawNodes.drawLine(getNodeX(), 350 + getNodeY(), getNodeX() + 250, 350 + (int) Math.round(getNodeY()*Math.sqrt(ratio)));
		drawNodes.drawLine(getNodeX(), 350 - getNodeY(), getNodeX() + 250, 350 - (int) Math.round(getNodeY()*Math.sqrt(ratio)));


		setNodeX(getNodeX()+250);
		setNodeY( (int) Math.round(getNodeY()*Math.sqrt(ratio)));		

	}

	public void AddConverg() {

		drawNodes.setStroke(new BasicStroke(10));
		drawNodes.setPaint(Color.black);

		JTextField ratioBOX = new JTextField(5);
		JPanel getData = new JPanel();

		double ratio;
		boolean passed;

		getData.add(new JLabel("Converging Area Ratio (ratio<1):"));
		getData.add(ratioBOX);

		passed = false;
		ratio = 2;

		while (!passed ) {

			try {
				JOptionPane.showConfirmDialog(null, getData, "Input data: ", JOptionPane.OK_CANCEL_OPTION);

				ratio = Double.parseDouble(ratioBOX.getText());

				if(ratio < 1) {
					passed = true;
				}
			}
			catch(Exception e){}
		}
		
		NodePiece[UI.getNodeCount()] = new ConvergingNozzle();
		NodePiece[UI.getNodeCount()].setArea2toArea1(ratio);

		drawNodes.drawString("Converging Nozzle ", getNodeX(), 350+getNodeY() );
		drawNodes.drawString("Area Ratio : " + ((double)Math.round(Math.sqrt(ratio)*100)/100), getNodeX(), 350+getNodeY()+20 );

		drawNodes.drawLine(getNodeX(), 350 - getNodeY(), getNodeX() + 250, 350 - (int) Math.round(getNodeY()*Math.sqrt(ratio)));
		drawNodes.drawLine(getNodeX(), 350 + getNodeY(), getNodeX() + 250, 350 + (int) Math.round(getNodeY()*Math.sqrt(ratio)));


		setNodeX(getNodeX()+250);
		setNodeY( (int) Math.round(getNodeY()*Math.sqrt(ratio)));		
	}

	public void AddHeatTub() {

		drawNodes.setStroke(new BasicStroke(10));
		drawNodes.setPaint(Color.black);

		JTextField HeatBOX = new JTextField(5);
		JPanel getData = new JPanel();

		double Heatadd;
		boolean passed;

		getData.add(new JLabel("Rate of Heat Addition:"));
		getData.add(HeatBOX);

		Heatadd = 1;
		passed = false;

		while (!passed) {

			try {
				JOptionPane.showConfirmDialog(null, getData, "Input data: ", JOptionPane.OK_CANCEL_OPTION);

				Heatadd = Double.parseDouble(HeatBOX.getText());

				passed = true;
			}
			catch(Exception e){}
		}
		NodePiece[UI.getNodeCount()] = new HeatTube();
		NodePiece[UI.getNodeCount()].setHeat(Heatadd);
		
		drawNodes.drawString("Heat Tube ", getNodeX(), 350+getNodeY()+50 );
		drawNodes.drawString("Heat Addition Rate: " + ((double)Math.round(Heatadd*100)/100), getNodeX(), 350+getNodeY()+70 );


		
		drawNodes.drawLine(getNodeX(), 350 - getNodeY(), getNodeX()+200, 350 - getNodeY());
		drawNodes.drawLine(getNodeX(), 350 + getNodeY(), getNodeX()+200, 350 + getNodeY());

		setNodeX(getNodeX()+200);
	}

	public void AddFricTub() {

		drawNodes.setStroke(new BasicStroke(10));
		drawNodes.setPaint(Color.black);

		JTextField MufBOX = new JTextField(5);
		JTextField lengthBOX = new JTextField(5);
		JPanel getData = new JPanel();

		double Muf, length;
		boolean passed;

		getData.add(new JLabel("Friction Coefficient:"));
		getData.add(MufBOX);
		getData.add(Box.createHorizontalStrut(15));
		getData.add(new JLabel("Length:"));
		getData.add(lengthBOX);

		passed = false;
		Muf = -2;
		length = -2;

		while (!passed  && length < 0  ) {
			try {
				JOptionPane.showConfirmDialog(null, getData, "Input data: ", JOptionPane.OK_CANCEL_OPTION);

				Muf = Double.parseDouble(MufBOX.getText());
				length = Double.parseDouble(lengthBOX.getText());

				//wouldnt work with this in while loop
				if(Muf > 1 && Muf>0) {
					passed = true;
				}
			}
			catch(Exception e){}
		}
		
		NodePiece[UI.getNodeCount()] = new FrictionTube();
		
		NodePiece[UI.getNodeCount()].setLength(length);
		NodePiece[UI.getNodeCount()].setHeat(Muf);

		
		
		drawNodes.drawString("Friction Tube ", getNodeX(), 350+getNodeY()+50 );
		drawNodes.drawString("Friction Coefficient: " + ((double)Math.round(Muf*100)/100), getNodeX(), 350+getNodeY()+70 );
		drawNodes.drawString("Length: " + ((double)Math.round(length*100)/100), getNodeX(), 350+getNodeY()+90 );


		//draws the node:
		drawNodes.drawLine(getNodeX(), 350 - getNodeY(), getNodeX()+200, 350 - getNodeY());
		drawNodes.drawLine(getNodeX(), 350 + getNodeY(), getNodeX()+200, 350 + getNodeY());



		setNodeX(getNodeX()+200);


	}


	public int getNodeX() {
		return nodeX;
	}

	public void setNodeX(int nodeX) {
		this.nodeX = nodeX;
	}

	public int getNodeY() {
		return nodeY;
	}

	public void setNodeY(int nodeY) {
		this.nodeY = nodeY;
	}

}



