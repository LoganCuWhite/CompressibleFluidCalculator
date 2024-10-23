/*
 * Class:		CS225-01DB
 * Author: 		Logan White
 * Created: 	3/22/2023
 * Modified:	
 * 
 * Purpose:	controlls the program and manages all of the flow pieces and user values. 
 * 
 * Methods: 	+runProgram(): void 
 *				+generateUI(): void 
 *				+getFlowValues(): void 
 *				+UpdateUI(): void 
 *
 * Attributes: 	-NodePiece: NodeManager[]
 */

import java.util.Scanner;


public class Controller{

	//gamma is assumed to be 1.4 as this is the value for air
	private NodeManager[] NodePiece = new NodeManager[4];


	public void runProgram() {

		Scanner scanner;

		double BackPressure;
		double mach;
		double pressure;
		double temp;
		double T0;
		double Heat;
		double MuF;
		double length;
		double R;
		double gamma;
		int NozzleType;

		double Area1;
		double Area2;


		int TotalNodes;
		TotalNodes =1;

		NodePiece = new NodeManager[TotalNodes];

		Area1 = 1;
		Area2 = 4;
		BackPressure = 1.041;
		gamma = 1.4;
		R = 273;
		mach = 1;
		pressure=15; 
		Heat =4.46*Math.pow(10, 5);
		MuF = 0.005;
		length = 30;

		temp = 300;



		NozzleType = 1;
		System.out.println("what Nozzzle Type 1-diverging, 2-converging, 3-heattube, 4-frictiontube: ");
		try {
			scanner = new Scanner(System.in);
			//R2
			NozzleType = scanner.nextInt();

		} catch (Exception e) {
			System.out.println("Is that Even a choice???! ");
			e.printStackTrace();
		}

		if(NozzleType == 1) {

			System.out.println("Area 1: ");
			try {
				scanner = new Scanner(System.in);
				//R2
				Area1 = scanner.nextInt();

			} catch (Exception e) {
				System.out.println("Is that Even A number???! ");
				e.printStackTrace();
			}

			System.out.println("Area 2: ");
			try {
				scanner = new Scanner(System.in);
				//R2
				Area2 = scanner.nextInt();

			} catch (Exception e) {
				System.out.println("Is that Even A number???! ");
				e.printStackTrace();
			}

			System.out.println("Atmospheric Pressure: ");
			try {
				scanner = new Scanner(System.in);
				//R2
				BackPressure = scanner.nextInt();

			} catch (Exception e) {
				System.out.println("Is that Even A number???! ");
				e.printStackTrace();
			}

			System.out.println("Pressure in: ");
			try {
				scanner = new Scanner(System.in);
				//R2
				pressure = scanner.nextInt();

			} catch (Exception e) {
				System.out.println("Is that Even A number???! ");
				e.printStackTrace();
			}

			System.out.println("Temp in: ");
			try {
				scanner = new Scanner(System.in);
				//R2
				temp = scanner.nextInt();

			} catch (Exception e) {
				System.out.println("Is that Even A number???! ");
				e.printStackTrace();
			}

			switch (NozzleType) {
		case 1:
			NodePiece[0] = new DivergingNozzle( mach, Area1,  Area2,  pressure,  BackPressure,  temp,  Heat,  gamma,  R,  "Diverging Nozzle" );
			System.out.println("Mach After Nozzle:  " + NodePiece[0].getMach(1));
			break;
		case 2:
			NodePiece[0] = new ConvergingNozzle( mach, Area1,  Area2,  pressure,  BackPressure,  temp,  Heat,  gamma,  R,  "Diverging Nozzle" );
			System.out.println("Mach After Nozzle:  " + NodePiece[0].getMach(1));
			break;
		case 3:
			NodePiece[0] = new HeatTube( mach, Area1,  Area2,  pressure,  BackPressure,  temp,  Heat,  gamma,  R,  "Diverging Nozzle" );
			System.out.println("Mach After Nozzle:  " + NodePiece[0].getMach(1));
			break;
		case 4:
			NodePiece[0] = new FrictionTube( mach, Area1,  Area2,  pressure,  BackPressure,  temp,  Heat,  gamma,  R,  "Diverging Nozzle" );
			System.out.println("Mach After Nozzle:  " + NodePiece[0].getMach(1));
			break;
			
		}
	}



	}

	public void generateUI() {

	}

	public void getFlowValues() {

	}


	public void UpdateUI() {

	}






}