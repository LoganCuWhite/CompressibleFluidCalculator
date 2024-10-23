/*
 * Class:		CS225-01DB
 * Author: 		Logan White
 * Created: 	3/22/2023
 * Modified:	3/24/2023 
 * 
 * Purpose:	
 * 
 * Methods: 	+actionPerformed(ActionEvent e): void
 * 				+show(): void
 * 				+listeners(): void
 *
 * Attributes: 	-clearBtn, addNodeBtn, inputDataBtn, openBtn, saveBtn: JButton
 * 				-workArea: WorkArea
 *				-choices: String[]
 *				-cb: JComboBox
 *				-file: File
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

	private JButton clearBtn, addNodeBtn, inputDataBtn, openBtn, saveBtn;
	private WorkArea workArea;
	private File file;
	private JFileChooser fc;

	String[] choices = {"Converging Nozzle","Divering Nozzle","Friction Tube","Heat Tube"};
	JComboBox cb = new JComboBox(choices);


	ActionListener actionListener = new ActionListener() {

		public void actionPerformed(ActionEvent e) {

			if (e.getSource() == clearBtn) {
				workArea.clear();
			} 

			//Button to add the node
			if (e.getSource() == addNodeBtn) {

				if(workArea.getNodeCount() <4) {
					switch ( (String)cb.getSelectedItem()){
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
						break;
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
					workArea.openFile(file);
				}
			} 
			else if (e.getSource() == saveBtn) {
				if (fc.showSaveDialog(workArea) == JFileChooser.APPROVE_OPTION) {
					file = new File(fc.getSelectedFile() + ".csv");									
					workArea.saveFile(file);
				}


			}
		}

	};

	public void show() {

		//setNodeCount(0);

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
		fc.setFileFilter(new FileNameExtensionFilter(".CSV", "csv"));
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
		openBtn = new JButton("Open");
		saveBtn = new JButton("Save");



		addNodeBtn.addActionListener(actionListener);
		inputDataBtn.addActionListener(actionListener);
		clearBtn.addActionListener(actionListener);
		saveBtn.addActionListener(actionListener);
		openBtn.addActionListener(actionListener);



	}

}