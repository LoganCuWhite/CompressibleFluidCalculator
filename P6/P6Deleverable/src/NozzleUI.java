/*
 * Class:		CS225-01DB
 * Author: 		Logan White
 * Created: 	3/22/2023
 * Modified:	3/24/2023 Mathmatical Mehtoods added, checks for choked flow added.
 * 
 * Purpose:	
 * 
 * Methods: 	+actionPerformed(ActionEvent e): void
 * 				+show(): void
 * 				+listeners(): void
 * 				-openFile(File f): void
 * 				-saveFile(File f): void
 *
 * Attributes: 	-clearBtn, addNodeBtn, inputDataBtn, openBtn, saveBtn: JButton
 * 				-workArea: WorkArea
 *				-choices: String[]
 *				-cb: JComboBox
 *				-file: File
 *				-nodeCount: int
 *				-fc: JFileChooser
 *				-actionListener: ActionListener
 *
 */

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class NozzleUI{

	JButton clearBtn, addNodeBtn, inputDataBtn;
	JButton openBtn, saveBtn;

	WorkArea workArea;
	
	String[] choices = {"Converging Nozzle","Divering Nozzle","Friction Tube","Heat Tube"};
	JComboBox cb = new JComboBox(choices);

	private int nodeCount;

	private File file;
	private JFileChooser fc;

	/*
	public static void main(String[] args) {
		NozzleUI starter = new NozzleUI();

		starter.show();

	}
	*/

	ActionListener actionListener = new ActionListener() {
		
		public void actionPerformed(ActionEvent e) {
			
			String Selected;

			if (e.getSource() == clearBtn) {
				workArea.clear();
				nodeCount = 0;
			} 

			//Button to add the node
			if (e.getSource() == addNodeBtn) {

				Selected = (String)cb.getSelectedItem();

				if(getNodeCount() <4) {
					
					setNodeCount(getNodeCount() + 1);
					
					switch (Selected){
					case "Converging Nozzle":
						workArea.AddConverg();
						workArea.repaint();
						break; 
					case "Divering Nozzle":
						workArea.AddDiverg();
						workArea.repaint();
						break;
					case "Friction Tube":
						workArea.AddFricTub();
						workArea.repaint();
						break;
					case "Heat Tube":
						workArea.AddHeatTub();
						workArea.repaint();
						nodeCount++;
					}
				}
			} 
			
			if (e.getSource() == inputDataBtn) {
				workArea.inputInlet();
				workArea.repaint();
			}
			
			//open and save files here;
			if (e.getSource() == openBtn) {
				if (fc.showOpenDialog(workArea) == JFileChooser.APPROVE_OPTION) {
					file = fc.getSelectedFile();
					openFile(file);
				}
			} 
			else if (e.getSource() == saveBtn) {
				if (fc.showSaveDialog(workArea) == JFileChooser.APPROVE_OPTION) {
					file = new File(fc.getSelectedFile() + ".png");									
					try {
						saveFile(file);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}


			}
		}

	};


	public void show() {
		
		setNodeCount(0);
		
		workArea = new WorkArea();
		JPanel controls = new JPanel();
		JPanel inputs = new JPanel();
		JFrame frame = new JFrame("Compressible Flow Calculator");
		JLabel lbl = new JLabel("Select node type to Add: ");

		frame.setLayout(new BoxLayout(frame, BoxLayout.Y_AXIS));
		Container content = frame.getContentPane();

		content.setLayout(new BorderLayout());
		content.add(workArea, BorderLayout.CENTER);

		fc = new JFileChooser(new File("."));
		fc.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png"));


		lbl.setAlignmentX(Component.CENTER_ALIGNMENT);


		cb.setSelectedIndex(3);

		listeners();
		controls.add(Box.createHorizontalStrut(350));
		controls.add(lbl);
		controls.add(cb);
		controls.add(addNodeBtn);
		controls.add(inputDataBtn);
		controls.add(clearBtn);
		controls.add(Box.createHorizontalStrut(300));
		controls.add(saveBtn);
		controls.add(openBtn);


		content.add(inputs, BorderLayout.NORTH);
		content.add(controls, BorderLayout.SOUTH);
		frame.setSize(1400,800);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		cb.setVisible(true);
		frame.setVisible(true);
	}

	public void listeners() {

		addNodeBtn = new JButton("Add Node");
		inputDataBtn = new JButton("Run Program");
		clearBtn = new JButton("Clear");
		saveBtn = new JButton("Open");
		openBtn = new JButton("Save");



		addNodeBtn.addActionListener(actionListener);
		inputDataBtn.addActionListener(actionListener);
		clearBtn.addActionListener(actionListener);
		saveBtn.addActionListener(actionListener);
		openBtn.addActionListener(actionListener);



	}

	private void openFile(File f) {

		try {
			new Dimension(ImageIO.read(f).getWidth(), ImageIO.read(f).getHeight());

		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	private void saveFile(File f) throws IOException {

	}


	//getters and setters


	public WorkArea getWorkArea() {
		return workArea;
	}

	public void setWorkArea(WorkArea workArea) {
		this.workArea = workArea;
	}
	
	public int getNodeCount() {
		return nodeCount;
	}

	public void setNodeCount(int nodeCount) {
		this.nodeCount = nodeCount;
	}
}