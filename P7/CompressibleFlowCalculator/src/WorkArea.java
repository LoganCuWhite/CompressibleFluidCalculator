/*
 * Class:		CS225-01DB
 * Author: 		Logan White
 * Created: 	3/22/2023
 * Modified:	3/24/2023 Mathmatical Mehtoods added, checks for choked flow added.
 *
 * Purpose:	Main controller of the project, creates the work area, and runs through all of the caluclations.
 *
 * Methods: 	+WorkArea()
 * 				#paintComponent(Graphics g): void
 * 				+clear(): void
 * 				+inputInlet(): void
 * 				+runFinishedCalcs(): void
 * 				+graphExitCondition(double P2): void
 * 				+graphshock(int n): void
 * 				+ErrorMessage(): void
 * 				+regraph(): void
 * 				+AddDiverg(): void
 * 				+AddConverg(): void
 * 				+AddHeatTub(): void
 * 				+addFricTub(): void
 * 				+openFile(File file): void
 * 				+saveFile(File file): void
 *
 *
 * Attributes: 	-PressureIN, TempIN, MachIN, PressureBACK, R, gamma: double
 * 				-nodeX, nodeY, nodecount: int
 * 				-cleartry, haveRan: boolean
 *  			-drawNodes: Graphics2D
 * 				-image: Image
 * 				-NodePiece: NodeManager[]
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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class WorkArea extends JComponent{

	private NodeManager[] NodePiece =  new NodeManager[5];
	private Image image;
	private Graphics2D drawNodes;

	private boolean cleartry, haveRan = false;
	private int nodeX, nodeY, nodeCount;
	private double PressureIN, TempIN, MachIN, PressureBACK, R, gamma;

	public WorkArea() {
		Toolkit.getDefaultToolkit().getScreenSize();
		setDoubleBuffered(false);
	}

	@Override
	protected void paintComponent(Graphics g) {

		if (image == null) {
			image = createImage(getSize().width, getSize().height);
			drawNodes = (Graphics2D) image.getGraphics();
			drawNodes.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			clear();
		}

		g.drawImage(image, 0 , 0 , null);
	}

	//clears the board
	public void clear() {

		drawNodes.setPaint(Color.white);
		drawNodes.fillRect(0, 0, getSize().width, getSize().height);

		if (cleartry) {
			regraph();
			cleartry = false;
			repaint();
		}
		else if (!cleartry ) {
			cleartry = true;
			setNodeCount(0);
			repaint();
			setNodeX(100);
			setNodeY(50);

		}




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

		getData.setLayout(experimentLayout);
		experimentLayout.setHgap(10);
		experimentLayout.setVgap(15);
		experimentLayout.layoutContainer(getData);
		getData.add(new JLabel("Inlet Static Pressure [KPa]:"));
		getData.add(PressureINBOX);
		getData.add(new JLabel("Inlet Static Temp [K]:"));
		getData.add(TempINBOX);
		getData.add(new JLabel("Inlet Mach:"));
		getData.add(MachINBOX);
		getData.add(new JLabel("Atmospheric Pressure [KPa]:"));
		getData.add(PressureBACKBOX);
		getData.add(new JLabel("R constant [J/KJK]:"));
		getData.add(RBOX);
		getData.add(new JLabel("Gamma value:"));
		getData.add(gammaBOX);

		try {
			JOptionPane.showConfirmDialog(null, getData, "Input data: ", JOptionPane.OK_CANCEL_OPTION);
			PressureIN = Double.parseDouble(PressureINBOX.getText());
			TempIN = Double.parseDouble(TempINBOX.getText());
			MachIN = Double.parseDouble(MachINBOX.getText());
			PressureBACK = Double.parseDouble(PressureBACKBOX.getText());
			gamma = Double.parseDouble(gammaBOX.getText());
			R = 287;

			for(int n = 0; n < getNodeCount(); n++) {
				NodePiece[n].setGamma(gamma);
				NodePiece[n].setR(R);
				NodePiece[n].setPback(PressureBACK);
			}

			runFinishedCalcs();
			cleartry = true;
		}
		catch(Exception e){
			ErrorMessage();
		}
	}

	public void runFinishedCalcs() {

		double P, T;

		NodePiece[0].setArea(1);
		NodePiece[0].Mach2toMach1(MachIN, PressureIN,  TempIN);
		
		if(NodePiece[0].getShock()) {
			graphshock(0);
		}

		P =  NodePiece[0].getP2toP1() * PressureIN;
		T = NodePiece[0].getT2toT1() * TempIN;

		for(int n =1; n < getNodeCount(); n++) {

			NodePiece[n].setArea(NodePiece[n-1].getArea()*NodePiece[n-1].getArea2toArea1());
			NodePiece[n].Mach2toMach1(NodePiece[n-1].getMach(1), P, T);

			if(NodePiece[n].getShock()) {
				graphshock(n);
			}

			P =  NodePiece[n].getP2toP1() * P;
			T = NodePiece[n].getT2toT1() * T;
		}

		graphExitCondition(P);
		haveRan = true;
	
		drawNodes.setFont((drawNodes.getFont()).deriveFont((drawNodes.getFont()).getSize() * 1.8F));
		drawNodes.drawString("Atmosphiric Conditions:  Atmospheric Pressure: " + PressureBACK + "  R: " + R + "  Gamma " + gamma, 100,20 );
		drawNodes.drawString("Inlet Conditions:  Inlet Pressure: " + PressureIN + "  Inlet Temp: " + TempIN + "  Inlet Mach " + MachIN, 100 ,40 );
		drawNodes.drawString("Outlet Conditions:  Exit Pressure: "
				+ (double) Math.round(P*100)/100
				+ "  Exit Temp: " + (double) Math.round(T*100)/100  +
				"  Exit Mach " + (double) Math.round(NodePiece[getNodeCount()-1].getMach(1)*100)/100 , 100 , 60 );
		drawNodes.drawString("Outlet Exit Condition: " + NodePiece[getNodeCount()-1].getExitCondition() ,100 , 80 );
		
		if(NodePiece[getNodeCount()-1].getMach(1) > 5 ) {
			drawNodes.drawString("Math is invalid for a flow of this speed Mach is greater than 5",100 , 100 );
		}
		drawNodes.setFont((drawNodes.getFont()).deriveFont((drawNodes.getFont()).getSize() / 1.8F));
	}

	public void graphExitCondition(double P2) {
		NodePiece[getNodeCount()-1].ExitCond(P2);

		drawNodes.setPaint(Color.blue);
		drawNodes.setStroke(new BasicStroke(2));
		//only these 4 require graphing to be shown
		switch(NodePiece[getNodeCount()-1].getExitCondition()) {
		case "Over Expanded":
			drawNodes.drawLine(getNodeX(), 350 + getNodeY()+3, getNodeX() + 300, 350 + Math.round(getNodeY()*2)+3);
			drawNodes.drawLine(getNodeX(), 350 - getNodeY()-3, getNodeX() + 300, 350 - Math.round(getNodeY()*2)-3);
			drawNodes.drawLine(getNodeX(), 350 + getNodeY()-3, getNodeX() + 300, 350 + Math.round(getNodeY()*2)-3);
			drawNodes.drawLine(getNodeX(), 350 - getNodeY()+3, getNodeX() + 300, 350 - Math.round(getNodeY()*2)+3);
			break;
		case "Perfectly Expanded":
			drawNodes.drawLine(getNodeX(), 350 + getNodeY()+3, getNodeX() + 300, 350 + getNodeY()+3);
			drawNodes.drawLine(getNodeX(), 350 - getNodeY()-3, getNodeX() + 300, 350 - getNodeY()-3);
			drawNodes.drawLine(getNodeX(), 350 + getNodeY()-3, getNodeX() + 300, 350 + getNodeY()-3);
			drawNodes.drawLine(getNodeX(), 350 - getNodeY()+3, getNodeX() + 300, 350 - getNodeY()+3);
			break;
		case "Under Expanded":
			drawNodes.drawLine(getNodeX(), 350 + getNodeY()+3, getNodeX() + 300, 350 - Math.round(getNodeY()*2)+3);
			drawNodes.drawLine(getNodeX(), 350 - getNodeY()-3, getNodeX() + 300, 350 + Math.round(getNodeY()*2)-3);
			drawNodes.drawLine(getNodeX(), 350 + getNodeY()-3, getNodeX() + 300, 350 - Math.round(getNodeY()*2)-3);
			drawNodes.drawLine(getNodeX(), 350 - getNodeY()+3, getNodeX() + 300, 350 + Math.round(getNodeY()*2)+3);
			break;
		case "Shock at Exit":
			drawNodes.drawLine(getNodeX()+6, 350 + getNodeY()+3, getNodeX() + 21, 350);
			drawNodes.drawLine(getNodeX(), 350 - getNodeY()-3, getNodeX() + 15, 350);
			drawNodes.drawLine(getNodeX(), 350 + getNodeY()-3, getNodeX() + 15, 350);
			drawNodes.drawLine(getNodeX()+6, 350 - getNodeY()+3, getNodeX() + 21, 350);
			break;
		case "Shock in Node":
			graphshock(getNodeCount()-1);

			break;

		}

		drawNodes.setPaint(Color.black);

	}

	public void graphshock(int n){
		
		drawNodes.setPaint(Color.blue);
		drawNodes.setStroke(new BasicStroke(2));

		switch (NodePiece[n].getNodeType()) {
		case "Heat Tube":
			drawNodes.drawLine(NodePiece[n].getGraphDimensions(2)-50,
					350 +  NodePiece[n].getGraphDimensions(1)-3,
					NodePiece[n].getGraphDimensions(2)-50 + 21, 350);

			drawNodes.drawLine(NodePiece[n].getGraphDimensions(2)-56,
					350 -  NodePiece[n].getGraphDimensions(1)+3,
					NodePiece[n].getGraphDimensions(2)-50 + 15, 350);

			drawNodes.drawLine(NodePiece[n].getGraphDimensions(2)-56,
					350 +  NodePiece[n].getGraphDimensions(1)-3,
					NodePiece[n].getGraphDimensions(2)-50 + 15, 350);

			drawNodes.drawLine(NodePiece[n].getGraphDimensions(2)-50,
					350 -  NodePiece[n].getGraphDimensions(1)+3,
					NodePiece[n].getGraphDimensions(2)-50 + 21, 350);
			break;
		case "Friction Tube":
			drawNodes.drawLine(NodePiece[n].getGraphDimensions(0) +  (int)Math.round(200*NodePiece[n].getAreaSHOCK()/NodePiece[n].getLength()),
					350 +  NodePiece[n].getGraphDimensions(1)-3,
					NodePiece[n].getGraphDimensions(0)+  (int)Math.round(200*NodePiece[n].getAreaSHOCK()/NodePiece[n].getLength()) + 21, 350);
			drawNodes.drawLine(NodePiece[n].getGraphDimensions(0)+ (int)Math.round(200*NodePiece[n].getAreaSHOCK()/NodePiece[n].getLength())-6,
					350 -  NodePiece[n].getGraphDimensions(1)+3,
					NodePiece[n].getGraphDimensions(0)+  (int)Math.round(200*NodePiece[n].getAreaSHOCK()/NodePiece[n].getLength()) + 15, 350);
			drawNodes.drawLine(NodePiece[n].getGraphDimensions(0)+ (int)Math.round(200*NodePiece[n].getAreaSHOCK()/NodePiece[n].getLength())-6,
					350 +  NodePiece[n].getGraphDimensions(1)-3,
					NodePiece[n].getGraphDimensions(0)+  (int)Math.round(200*NodePiece[n].getAreaSHOCK()/NodePiece[n].getLength()) + 15, 350);
			drawNodes.drawLine(NodePiece[n].getGraphDimensions(0)+  (int)Math.round(200*NodePiece[n].getAreaSHOCK()/NodePiece[n].getLength()),
					350 -  NodePiece[n].getGraphDimensions(1)+3,
					NodePiece[n].getGraphDimensions(0)+  (int)Math.round(200*NodePiece[n].getAreaSHOCK()/NodePiece[n].getLength()) + 21, 350);
			break;
		case "Converging Nozzle":
		case "Diverging Nozzle":
			//much much more difficult
			double alpha = Math.atan(NodePiece[n].getGraphDimensions(3)/240);
			int shocklocation = (int)Math.round(Math.sqrt(NodePiece[n].getAreaSHOCK()/Math.sqrt(NodePiece[n].getArea()))/Math.tan(alpha));

			drawNodes.drawLine(NodePiece[n].getGraphDimensions(0) +  shocklocation,
					350 +  NodePiece[n].getGraphDimensions(1)-3,
					NodePiece[n].getGraphDimensions(0)+ (int)Math.round(NodePiece[n].getAreaSHOCK()/Math.sqrt(NodePiece[n].getArea())) + 21, 350);
			drawNodes.drawLine(NodePiece[n].getGraphDimensions(0)+ shocklocation-6,
					350 -  NodePiece[n].getGraphDimensions(1)+3,
					NodePiece[n].getGraphDimensions(0)+  (int)Math.round(NodePiece[n].getAreaSHOCK()/Math.sqrt(NodePiece[n].getArea())) + 15, 350);
			drawNodes.drawLine(NodePiece[n].getGraphDimensions(0)+ shocklocation-6,
					350 +  NodePiece[n].getGraphDimensions(1)-3,
					NodePiece[n].getGraphDimensions(0)+  (int)Math.round(NodePiece[n].getAreaSHOCK()/Math.sqrt(NodePiece[n].getArea())) + 15, 350);
			drawNodes.drawLine(NodePiece[n].getGraphDimensions(0)+  shocklocation,
					350 -  NodePiece[n].getGraphDimensions(1)+3,
					NodePiece[n].getGraphDimensions(0)+  (int)Math.round(NodePiece[n].getAreaSHOCK()/Math.sqrt(NodePiece[n].getArea())) + 21, 350);

			break;
		}
		drawNodes.setPaint(Color.black);
	}

	public void ErrorMessage() {
		JPanel ErrorPopup = new JPanel();
		ErrorPopup.add(new JLabel("Improper Inputs "));
		JOptionPane.showConfirmDialog(null, ErrorPopup, "Input data: ", JOptionPane.OK_CANCEL_OPTION);
	}

	public void regraph() {

		drawNodes.setStroke(new BasicStroke(10));
		drawNodes.setPaint(Color.black);

		for(int n = 0; n < getNodeCount(); n++) {
			drawNodes.drawLine(NodePiece[n].getGraphDimensions(0),
					350 - NodePiece[n].getGraphDimensions(1),
					NodePiece[n].getGraphDimensions(2),
					350 - NodePiece[n].getGraphDimensions(3));
			drawNodes.drawLine(NodePiece[n].getGraphDimensions(0),
					350 + NodePiece[n].getGraphDimensions(1),
					NodePiece[n].getGraphDimensions(2),
					350 + NodePiece[n].getGraphDimensions(3));
			drawNodes.drawString(NodePiece[n].getNodeType() + " ",
					NodePiece[n].getGraphDimensions(0),
					350+ NodePiece[n].getGraphDimensions(3)+50 );

			switch (NodePiece[n].getNodeType()) {
			case "Diverging Nozzle":
				drawNodes.drawString("Area Ratio : " + ((double)Math.round(NodePiece[n].getArea2toArea1()*100)/100),
						NodePiece[n].getGraphDimensions(0),
						350+ NodePiece[n].getGraphDimensions(3)+70 );
				break;
			case "Converging Nozzle":
				drawNodes.drawString("Area Ratio : " + ((double)Math.round(NodePiece[n].getArea2toArea1()*100)/100),
						NodePiece[n].getGraphDimensions(0),
						350+ NodePiece[n].getGraphDimensions(3)+70 );
				break;
			case "Heat Tube":
				drawNodes.drawString("Heat Addition: " + ((double)Math.round(NodePiece[n].getHeat()*100)/100),
						NodePiece[n].getGraphDimensions(0),
						350+ NodePiece[n].getGraphDimensions(3)+70 );
				break;
			case "Friction Tube":
				drawNodes.drawString("Friction Coefficient: " + ((double)Math.round(NodePiece[n].getHeat()*100)/100),
						NodePiece[n].getGraphDimensions(0),
						350+ NodePiece[n].getGraphDimensions(3)+70 );
				drawNodes.drawString("Length: " + ((double)Math.round(NodePiece[n].getLength()*100)/100),
						NodePiece[n].getGraphDimensions(0),
						350+ NodePiece[n].getGraphDimensions(3)+90 );
				break;

			}
			repaint();
		}

	}

	//gets inputs for each node,
	public void AddDiverg() {

		JTextField ratioBOX = new JTextField(5);
		JPanel getData = new JPanel();

		double ratio;

		getData.add(new JLabel("Diverging Area Ratio (ratio>1):"));
		getData.add(ratioBOX);

		ratio = .2;

		try {
			JOptionPane.showConfirmDialog(null, getData, "Input data: ", JOptionPane.OK_CANCEL_OPTION);

			ratio = Double.parseDouble(ratioBOX.getText());

			if(ratio > 1) {

				NodePiece[getNodeCount()] = new DivergingNozzle();

				int [] dimensions = {getNodeX(), getNodeY(), getNodeX() + 250, (int) Math.round(getNodeY()*Math.sqrt(ratio))};
				NodePiece[getNodeCount()].setGraphDimensions(dimensions);
				NodePiece[getNodeCount()].setArea2toArea1(ratio);
				NodePiece[getNodeCount()].setNodeType("Diverging Nozzle");

				setNodeX(getNodeX()+250);
				setNodeY( (int) Math.round(getNodeY()*Math.sqrt(ratio)));
				setNodeCount(getNodeCount() + 1);
			}
			else {
				ErrorMessage();
			}
		}
		catch(Exception e){
			ErrorMessage();
		}
		regraph();
	}
	public void AddConverg() {

		JTextField ratioBOX = new JTextField(5);
		JPanel getData = new JPanel();

		double ratio;

		getData.add(new JLabel("Converging Area Ratio (ratio<1):"));
		getData.add(ratioBOX);

		ratio = 2;

		try {
			JOptionPane.showConfirmDialog(null, getData, "Input data: ", JOptionPane.OK_CANCEL_OPTION);
			ratio = Double.parseDouble(ratioBOX.getText());

			if(ratio < 1) {

				NodePiece[getNodeCount()] = new ConvergingNozzle();
				
				int [] dimensions = {getNodeX(), getNodeY(), getNodeX() + 250, (int) Math.round(getNodeY()*Math.sqrt(ratio))};
				NodePiece[getNodeCount()].setGraphDimensions(dimensions);
				NodePiece[getNodeCount()].setArea2toArea1(ratio);
				NodePiece[getNodeCount()].setNodeType("Converging Nozzle");

				setNodeX(getNodeX()+250);
				setNodeY( (int) Math.round(getNodeY()*Math.sqrt(ratio)));
				setNodeCount(getNodeCount() + 1);
			}
			else {
				ErrorMessage();
			}
		}
		catch(Exception e){
			ErrorMessage();

		}
		regraph();
	}
	public void AddHeatTub() {

		JTextField HeatBOX = new JTextField(5);
		JPanel getData = new JPanel();

		double Heatadd;

		getData.add(new JLabel("Rate of Heat Addition [KJ/KG]:"));
		getData.add(HeatBOX);

		Heatadd = 1;

		try {
			JOptionPane.showConfirmDialog(null, getData, "Input data: ", JOptionPane.OK_CANCEL_OPTION);

			Heatadd = Double.parseDouble(HeatBOX.getText());

			NodePiece[getNodeCount()] = new HeatTube();
			
			int [] dimensions = {getNodeX(), getNodeY(), getNodeX() + 200, getNodeY()};
			NodePiece[getNodeCount()].setGraphDimensions(dimensions);
			NodePiece[getNodeCount()].setHeat(Heatadd);
			NodePiece[getNodeCount()].setArea2toArea1(1);
			NodePiece[getNodeCount()].setNodeType("Heat Tube");

			setNodeX(getNodeX()+200);
			setNodeCount(getNodeCount() + 1);

		}
		catch(Exception e){
			ErrorMessage();
		}
		regraph();
	}
	public void AddFricTub() {

		JTextField MufBOX = new JTextField(5);
		JTextField lengthBOX = new JTextField(5);
		JPanel getData = new JPanel();

		double Muf, length;

		getData.add(new JLabel("Friction Coefficient:"));
		getData.add(MufBOX);
		getData.add(Box.createHorizontalStrut(15));
		getData.add(new JLabel("Length [m]:"));
		getData.add(lengthBOX);

		try {

			JOptionPane.showConfirmDialog(null, getData, "Input data: ", JOptionPane.OK_CANCEL_OPTION);

			Muf = Double.parseDouble(MufBOX.getText());
			length = Double.parseDouble(lengthBOX.getText());

			if(Muf < 1 && Muf > 0 && length > 0) {

				NodePiece[getNodeCount()] = new FrictionTube();

				int [] dimensions = {getNodeX(), getNodeY(), getNodeX() + 200, getNodeY()};
				NodePiece[getNodeCount()].setGraphDimensions(dimensions);
				NodePiece[getNodeCount()].setLength(length);
				NodePiece[getNodeCount()].setHeat(Muf);
				NodePiece[getNodeCount()].setArea2toArea1(1);
				NodePiece[getNodeCount()].setNodeType("Friction Tube");

				setNodeX(getNodeX()+200);
				setNodeCount(getNodeCount() + 1);

			}
			else {
				ErrorMessage();
			}

		}
		catch(Exception e){
			ErrorMessage();
		}
		regraph();
	}

	public void openFile(File file) {
		
		double ratio, Heat, length, Muf;
		String row, nodeType;
		
		cleartry =false;
		clear();
		
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			
			row = br.readLine();

			String[] columns = row.split(",", 8);
			
			PressureBACK = Double.parseDouble(columns[1]);
			gamma = Double.parseDouble(columns[3]);
			R = Double.parseDouble(columns[5]);

			row = br.readLine();
			MachIN = Double.parseDouble(columns[1]);
			PressureIN = Double.parseDouble(columns[3]);
			TempIN = Double.parseDouble(columns[5]);

			row = br.readLine();
			
			while (  ( row = br.readLine() ) != null     ) {
				
				nodeType =columns[0];
				
				switch(nodeType) {
				case "Diverging Nozzle":
					ratio =  Double.parseDouble(columns[4]);

					NodePiece[getNodeCount()] = new DivergingNozzle();

					int [] dimensions = {getNodeX(), getNodeY(), getNodeX() + 250, (int) Math.round(getNodeY()*Math.sqrt(ratio))};
					NodePiece[getNodeCount()].setGraphDimensions(dimensions);
					NodePiece[getNodeCount()].setArea2toArea1(ratio);
					NodePiece[getNodeCount()].setNodeType("Diverging Nozzle");

					setNodeX(getNodeX()+250);
					setNodeY( (int) Math.round(getNodeY()*Math.sqrt(ratio)));

					break;
				case "Converging Nozzle":
					ratio =  Double.parseDouble(columns[4]);

					NodePiece[getNodeCount()] = new ConvergingNozzle();

					int [] dimensions1 = {getNodeX(), getNodeY(), getNodeX() + 250, (int) Math.round(getNodeY()*Math.sqrt(ratio))};
					NodePiece[getNodeCount()].setGraphDimensions(dimensions1);
					NodePiece[getNodeCount()].setArea2toArea1(ratio);
					NodePiece[getNodeCount()].setNodeType("Converging Nozzle");

					setNodeX(getNodeX()+250);
					setNodeY( (int) Math.round(getNodeY()*Math.sqrt(ratio)));

					break;
				case "Heat Tube":
					Heat =  Double.parseDouble(columns[5]);

					NodePiece[getNodeCount()] = new HeatTube();

					int [] dimensions2 = {getNodeX(), getNodeY(), getNodeX() + 200, getNodeY()};
					NodePiece[getNodeCount()].setGraphDimensions(dimensions2);
					NodePiece[getNodeCount()].setHeat(Heat);
					NodePiece[getNodeCount()].setArea2toArea1(1);


					setNodeX(getNodeX()+200);

					break;
				case "Friction Tube":
					length =  Double.parseDouble(columns[6]);
					Muf =  Double.parseDouble(columns[7]);

					NodePiece[getNodeCount()] = new FrictionTube();

					int [] dimensions3 = {getNodeX(), getNodeY(), getNodeX() + 200, getNodeY()};
					NodePiece[getNodeCount()].setGraphDimensions(dimensions3);
					NodePiece[getNodeCount()].setLength(length);
					NodePiece[getNodeCount()].setHeat(Muf);
					NodePiece[getNodeCount()].setArea2toArea1(1);
					NodePiece[getNodeCount()].setNodeType("Friction Tube");

					setNodeX(getNodeX()+200);
					break;
				}
				setNodeCount(getNodeCount() + 1);
			}

			br.close();
			fr.close();

			regraph();
			runFinishedCalcs();
			cleartry = true;
		}
		catch (Exception e) {
			e.printStackTrace();

		}
	}

	public void saveFile(File file){

		try {
			double P, T;

			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);

			bw.write("P_ATM:" + "," + PressureBACK + "," +"gamma:" + "," + gamma + "," +"R:" + ","+ R  +"\n");
			bw.write("MachIN:" + "," + MachIN + "," +"Pressure IN:" + "," + PressureIN + "," +"Temp IN:" + ","+ TempIN +"\n");
			bw.write("NodeType" + "," +"Mach" + "," + "Pressure [KPa]" + "," + "Temp [K]" + "," +"Area Rato" + ","
					+ "Heat [KJ/KG]"+"," +"Length[Unitless]"+","+"Friction Coefficient"+"\n");

			P = PressureIN;
			T = TempIN;

			if (haveRan) {
				P = PressureIN;
				T = TempIN;

				for(int n = 0; n < getNodeCount(); n++) {

					P =  NodePiece[n].getP2toP1() * P;
					T = NodePiece[n].getT2toT1() * T;
					bw.write(NodePiece[n].getNodeType() + "," + NodePiece[n].getMach(1) + "," + P + "," + T + "," +NodePiece[n].getArea2toArea1() + ","
							+ NodePiece[n].getHeat() +"," +NodePiece[n].getLength()+","+NodePiece[n].getHeat()+"\n");
				}
			}
			else {
				for(int n = 0; n < getNodeCount(); n++) {
					bw.write(NodePiece[n].getNodeType() + "," +"null" + "," + "null" + "," + "null" + "," +NodePiece[n].getArea2toArea1() + ","
							+ NodePiece[n].getHeat() +"," +NodePiece[n].getLength()+","+NodePiece[n].getHeat()+"\n");

				}
			}

			bw.close();
			fw.close();

			regraph();
			runFinishedCalcs();
			cleartry = true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	//getters and setters
	public int getNodeCount() {
		return nodeCount;
	}
	public void setNodeCount(int nodeCount) {
		this.nodeCount = nodeCount;
	}
	public int getNodeX() {
		return nodeX;
	}
	public void setNodeX(int nodeX) {
		this.nodeX = nodeX;
	}
	public int  getNodeY() {
		return nodeY;
	}
	public void setNodeY(int nodeY) {
		this.nodeY = nodeY;
	}

}



