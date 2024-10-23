/*
 * Class:		CS225-01DB
 * Author: 		Logan White
 * Created: 	3/22/2023
 * Modified:	3/24/2023 -- Completed all mathmatical requirments for Heattubes
 * 
 * Purpose:	heat and friction tubes both make mach aproach 1, for both hypersonics and subsonics. Shocks do not occur within heat tubes,
 *			instead they just become choked
 * 			Flow is not Isentropic in a heat tube, because T0 is not constant, P0 still constant
 * 			VERIFIED values, Exit conditions not completed
 * 
 * 
 * Methods: 	#HeatTube(double MachIN, double Pin, double Pback, double T, String Nodetype): void 
 *				#Mach2toMach1(double MachIN, double Pin, double Pback, double T,): void 
 *				#IsChoked(double MachIn, double T01, double T02, ): void
 *				#NotChoked(double MachIn, double T01, double T02, ): void
 *				#ExitCond(): void 

 *
 * Attributes: 	-Isen: Isentropic[]
 *				-Mach: double[]
 *
 */

public class HeatTube extends NodeManager {

	private Isentropic[] Isen = new Isentropic[2];
	private double[] Mach = new double[2];

	
	public HeatTube() {

	}


	@Override
	protected void Mach2toMach1(double MachIN, double Pin, double Pback, double T) {


		Isen[0] = new Isentropic();

		//Pressure Coefficient
		double Cp;

		//temp variables
		double[] T0;

		//array initialization
		T0 = new double[2];

		//Cp is pressrue coefficient
		Cp = getR()*(getGamma()/(getGamma()-1));

		//setting the initial mach
		Mach[0] = MachIN;

		//getting the values of the inital mach, at the entrance for only that point flow is assumed to be isentropic.
		Isen[0].IsentropicRelations(Mach[0], getGamma());
		T0[0] = Isen[0].getISENTROPICT0toT() * T;

		//since this is a heat tube it is equivalent to the variable q for heat addition
		//Temp value after flow is Accelerated.
		T0[1] = (getHeat()/Cp)+T0[0];

		//ischoked generates the second mach value and determines is the flow is choked
		isChoked(Mach[0], T0[0], T0[1]);

		System.out.println("Exit Mach " + Mach[1]);


		setMach(Mach);


	}

	protected void isChoked(double MachIn, double T01, double T02) {

		double Mguess;

		double T0star;

		//T0* or T0star is a test case, it is the amount of heat needed to accelerate/decellerate the flow to Mach of 1
		T0star = Math.pow((((getGamma()+1)*Math.pow(MachIn, 2))/Math.pow(1+getGamma()*Math.pow(MachIn, 2), 2))
				*(2+((getGamma()-1)*Math.pow(MachIn, 2))), -1) * T01;

		//flow is choked
		if(T02>T0star) {

			Mguess = 1;
			System.out.println("Flow is chocked");
			//ADD BOOLEAN FLAG HERE LATER
			//choked flow affects all prevous pieces, they must be re-evaluated. 
			Mach[1] = Mguess;

		}

		//flow is not choked;
		else {

			System.out.println("Flow is not chocked");
			NotChoked( MachIn,  T01,  T02);

		}

	}
	
	protected void NotChoked(double MachIn, double T01, double T02) {

		double Mguess;

		double T0ratio;
		double staticTempRatioeq;

		//for use in the while loop, fomula must equal this ratio.
		T0ratio = T02/T01;

		//values set to zero just to start the loop
		Mguess = 0;
		staticTempRatioeq = 0;

		//the While loop that iterates the formula until it finds the correct Mach number;
		while(Math.round(T0ratio*1000)/1000D != Math.round(staticTempRatioeq*1000)/1000D) {

			//This is to increase mach each time untill correct one is found
			Mguess = Mguess+0.0001;

			//that equation that should be equal to the stagnation temp ratio. 
			staticTempRatioeq = Math.pow((1+getGamma()*Math.pow(MachIn, 2))/(1+getGamma() * Math.pow(Mguess, 2)),2)
					*Math.pow(Mguess/MachIn,2)
					*((1+((getGamma()-1)/2) *Math.pow(Mguess, 2))/(1+ ((getGamma()-1)/2) * Math.pow(MachIn, 2)));

		}
		Mach[1] = Mguess;

	}

	@Override
	protected void ExitCond() {
		
	}
	
}