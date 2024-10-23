/*
 * Class:		CS225-01DB
 * Author: 		Logan White
 * Created: 	3/22/2023
 * Modified:	
 * 
 * Purpose:	diverging nozzle math
 * 
 * Methods: 	#DivergingNozzle(double MachIN, double Area1, double Area2, double Pin, double Pback, double T, double Heat, double gamma, double R, String Nodetype): void 
 *				#Mach2toMach1(double MachIN, double Pin, double Pback, double T, double gamma, double R)): void 
 *				#Exit(double MachOut, double Pback, double P01, double Astar,  double gamma): void
 *				#isShock(double MachIN, double Area1, double Area2, double P, double T, double gamma): void 
 *
 * Attributes: 	-Isen: Isentropic[]
 * 				-NS: NormalShock[]
 *				-Mach: boolean[]
 *
 */

public class DivergingNozzle extends NodeManager {

	//this are length four because the likelihood that a nozzle has more than 4 sections (AKA 1 shock) is EXTREMELY low, 
	private Isentropic[] Isen = new Isentropic[4];
	private NormalShock[] NS = new NormalShock[4];
	private double[] Mach = new double[2];


	public DivergingNozzle(double MachIN, double Area1, double Area2, double Pin, double Pback, double T, double Heat, double gamma, double R, String Nodetype) {
		//the Variable name Heat is used for both friction and heat tubes for convenience
		super(MachIN, Area1, Area2, Pin, Pback, T, Heat, gamma, R, Nodetype);
		Mach2toMach1(MachIN,Pin,Pback,T,gamma,R);
	}

	@Override
	protected void Mach2toMach1(double MachIN, double Pin, double Pback, double T, double gamma, double R) {
		Isen[0] = new Isentropic();


		double AstartoA1;
		double Astar;
		double AstartoA2;
		double Guesser;

		double P0;

		double MachSub;
		double MachSup;

		Mach[0] = MachIN; 
		Isen[0].IsentropicRelations(Mach[0], gamma);
		P0 = Isen[0].getISENTROPICP0toP()*Pin;

		AstartoA1 = Math.pow((1/Mach[0])* Math.pow((1+(((gamma-1)*Math.pow(Mach[0],2))/2))/(1+(gamma-1)/2),(gamma+1)/(2*(gamma-1))),-1);

		Astar = AstartoA1 * Area1;
		AstartoA2 = Astar/Area2;

		//AtoAstar equation has 2 outputs a low and high mach, to figure out which is correct check against back pressure for exit conditions..
		Guesser = 0;
		MachSup = 1;
		while(Math.round(Guesser*1000)/1000D != Math.round(AstartoA2*1000)/1000D ) {
			MachSup = MachSup + 0.001;
			Guesser = Math.pow((1/MachSup)* Math.pow((1+(((gamma-1)*Math.pow(MachSup,2))/2))/(1+(gamma-1)/2),(gamma+1)/(2*(gamma-1))),-1);
		}
		//Pexit inital guess, this helps with rest of program
		Exit( MachSup,  Pback,  P0, Astar, gamma);
		setMach(Mach);
		/*
					Guesser = 0;
					MachSub = 1;
					while(Math.round(Guesser*1000)/1000D != Math.round(AstartoA2*1000)/1000D ) {
						MachSub = MachSub - 0.001;
						Guesser = Math.pow((1/MachSub)* Math.pow((1+(((gamma-1)*Math.pow(MachSub,2))/2))/(1+(gamma-1)/2),(gamma+1)/(2*(gamma-1))),-1);
					}
		 */

	}

	protected void Exit(double MachOut, double Pback, double P01, double Astar,  double gamma) {
		Isen[1] = new Isentropic();
		NS[0] = new NormalShock();

		double Pexit;
		double PexitSHOCK;

		Isen[1].IsentropicRelations(MachOut, gamma);
		Pexit = P01/ Isen[1].getISENTROPICP0toP();

		if(Math.round(Pexit*1000)/1000D == Math.round(Pback*1000)/1000D ) {
			System.out.println("Nozzle is Perfectly Expanded");
			Mach[1] = MachOut;

		}
		else if(Math.round(Pexit*1000)/1000D > Math.round(Pback*1000)/1000D ) {
			System.out.println("Nozzle is Over Expanded");
			Mach[1] = MachOut;
		}
		else {

			//testing if other conditions now
			NS[0].NormalShockRelations(MachOut, gamma);
			PexitSHOCK = Pexit * NS[0].getSHOCKP2toP1();

			if(Math.round(PexitSHOCK*1000)/1000D == Math.round(Pback*1000)/1000D ) {
				System.out.println("Nozzle has Shock at Exit");
				Mach[1] = NS[0].getMach(1);
			}
			else if(Math.round(PexitSHOCK*1000)/1000D < Math.round(Pback*1000)/1000D 
					&&  Math.round(Pback*1000)/1000D < Math.round(Pexit*1000)/1000D) {
				System.out.println("Nozzle is UnderExpanded");
				Mach[1] = MachOut;

			}
			else if(Math.round(PexitSHOCK*1000)/1000D > Math.round(Pback*1000)/1000D) {
				System.out.println("Shock is in the Nozzle, or its just small mach");
				setShock(true);

				isShock( MachOut,  Pback, P01, Astar, gamma);
				Mach[1] = MachOut;

			}
			else {
				System.out.println("Problem has Occured");
			}

		}

	}



	protected void isShock(double MachOut, double Pback, double P01, double Astar, double gamma) {

		Isen[2] = new Isentropic();
		NS[1] = new NormalShock();

		double P02;
		double P02toP01;
		double MatShock;
		double AstartoAshock;
		double ASHOCK;

		Isen[2].IsentropicRelations(MachOut, gamma);


		P02 = Isen[2].getISENTROPICP0toP()*Pback;

		P02toP01 = P01/P02;

		MatShock = 1;
		NS[1].NormalShockRelations(MatShock, gamma);
		while(Math.round(P02toP01*1000)/1000D != Math.round(NS[1].getSHOCKP02toP01()*1000)/1000D ) {
			MatShock = MatShock + 0.001;
			NS[1].NormalShockRelations(MatShock, gamma);
		}


		AstartoAshock = Math.pow((1/MatShock)* Math.pow((1+(((gamma-1)*Math.pow(MatShock,2))/2))/(1+(gamma-1)/2),(gamma+1)/(2*(gamma-1))),-1);

		ASHOCK = Astar/AstartoAshock;
		System.out.println("Shock occur at" + ASHOCK);

		if (ASHOCK > Area1) {
			setAreaSHOCK(ASHOCK);
		}
		else {
			System.out.println("Flow Piece Invalid");
		}

	}


}