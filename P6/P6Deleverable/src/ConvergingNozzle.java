/*
 * Class:		CS225-01DB
 * Author: 		Logan White
 * Created: 	3/22/2023
 * Modified:	
 * 
 * Purpose:	converging nozzle math
 * 
 * Methods: 	#ConvergingNozzle(double MachIN, double Pin, double Pback, double T, String Nodetype): void 
 *				#Mach2toMach1(double MachIN, double Pin, double Pback, double T): void 
 *				#isChoked(double MachIN, double ASHOCK): void
 *				#NotChoked(double MachIN, double ASHOCK, double Area2): void
 *				#isShock(double MachIN, double Area1, double Area2, double P, double T): void 
 *				#ExitCond(): void

 *
 * Attributes: 	-Isen: Isentropic[]
 *				-Mach: NormalShock
 *
 */

public class ConvergingNozzle extends NodeManager {

	private Isentropic[] Isen = new Isentropic[4];
	private double[] Mach = new double[2];


	// the extends is commented out rn for testing. 
	public ConvergingNozzle() {
		//the Variable name Heat is used for both friction and heat tubes for convenience

	
	}


	@Override
	protected void Mach2toMach1(double MachIN, double Pin, double Pback, double T) {

		double ASHOCKtoA;
		double ASHOCK;
		double Area2;
		
		Mach[0] = MachIN; 


		//the normal equation for this gives a value in terms of A/Ashock so it has to be inverted 

		ASHOCKtoA = Math.pow((1/MachIN)* Math.pow((1+(((getGamma()-1)*Math.pow(MachIN,2))/2))/(1+(getGamma()-1)/2),(getGamma()+1)/(2*(getGamma()-1))),-1);

		ASHOCK = ASHOCKtoA * getArea();
		Area2 = getArea() * getArea2toArea1();

		if(ASHOCK > Area2) {
			
			isChoked(Mach[0], ASHOCK);
		}
		else if(ASHOCK < Area2){
			
			NotChoked( Mach[0],  ASHOCK,  Area2);
		}
		else if(ASHOCK == Area2) {
			//this value technically the flow is choked here, but massflow is not reduced because it is still contstant
			Mach[1] = 1;
		}

	}

	protected void isChoked(double MachIN, double ASHOCK) {
		//this checks for shock flow is already choked at this point
		setChoked(true);

		if(MachIN > 1) {
			setShock(true);
			setAreaSHOCK(ASHOCK);
			Mach[1] = 1;
		}
		else {

			Mach[1] = 1;
		}

	}

	protected void NotChoked(double MachIN, double ASHOCK, double Area2) {

		double ASHOCKtoA2;
		double Guesser;
		
		double MachOut;


		ASHOCKtoA2 = ASHOCK/Area2;
		Guesser = 0;

		//Iterates to find exit mach;
		if(Mach[0] > 1) {
			MachOut = 1;
			while(Guesser != ASHOCKtoA2) {
				MachOut = MachOut + 0.001;
				Guesser = Math.pow((1/MachOut)* Math.pow((1+(((getGamma()-1)*Math.pow(MachOut,2))/2))/(1+(getGamma()-1)/2),(getGamma()+1)/(2*(getGamma()-1))),-1);
			}
		}
		//Area equation can have 2 mach numbers, so it iterates down if inlet mach is less than 1;
		else {
			MachOut = 1;
			while(Guesser != ASHOCKtoA2) {
				MachOut = MachOut - 0.001;
				Guesser = Math.pow((1/MachOut)* Math.pow((1+(((getGamma()-1)*Math.pow(MachOut,2))/2))/(1+(getGamma()-1)/2),(getGamma()+1)/(2*(getGamma()-1))),-1);
			}
		}
		
		Mach[1] = MachOut;

	}

	protected void isShock(double MachIN, double Area1, double Area2, double P, double T) {

	}

	@Override
	protected void ExitCond() {
		
	}
	
}