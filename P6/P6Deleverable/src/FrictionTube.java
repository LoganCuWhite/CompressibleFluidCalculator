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
 * Methods: 	+FrictionTube(double MachIN, double Pin, double Pback, double T, String Nodetype): void 
 *				#Mach2toMach1(double MachIN, double Pin, double Pback, double T): void 
 *				#isChoked(double MachIN, double length, double Lstar1, double Diameter, double friction ): void 
 *				#NotChoked(double MachIN, double length, double Lstar1, double Diameter, double friction): void 
 *
 * Attributes: 	-Isen: Isentropic[]
 * 				-NS: NormalShock[]
 *				-Mach: boolean[]
 *
 */


public class FrictionTube extends NodeManager {

	private Isentropic[] Isen = new Isentropic[4];
	private NormalShock[] NS = new NormalShock[4];
	private double[] Mach = new double[4];


	public FrictionTube() {

	}

	@Override
	protected void Mach2toMach1(double MachIN, double Pin, double Pback, double T) {

		Isen[0] = new Isentropic();

		//dimensions
		double Diameter;
		double friction;

		//temp variables
		double[] T0;
		double Lstar1;

		
		
		//array initialization
		T0 = new double[2];


		//Area2 is used to store length since the dimensions of this piece are constant, and it saves on variables.
		friction = Heat;

		//diameter is reqired for these calculations
		Diameter = Math.sqrt(getArea()/Math.PI)*2;

		//setting the initial mach
		Mach[0] = MachIN;

		//getting the values of the inital mach, at the entrance for only that point flow is assumed to be isentropic.
		Isen[0].IsentropicRelations(Mach[0], getGamma());
		T0[0] = Isen[0].getISENTROPICT0toT() * T;

		//L2* = L1* - L, used to find mach
		Lstar1 = (Diameter/(4*friction))*(((1-Math.pow(Mach[0], 2))/(getGamma()*Math.pow(Mach[0], 2)))+(((getGamma()-1)/(2*getGamma()))
				*Math.log(((getGamma()+1)*Math.pow(Mach[0], 2))/(2+((getGamma()+1)*Math.pow(Mach[0], 2))))));

		
		if (Lstar1 <= getLength()) {
			isChoked(Mach[0],  getLength(), Lstar1,  Diameter,  friction);
		}
		else {
			NotChoked(Mach[0],  getLength(), Lstar1,  Diameter,  friction);
		}
		
		
		System.out.println("Exit Mach "+Mach[1]);


		setMach(Mach);
	}

	protected void isChoked(double MachIN, double length, double Lstar1, double Diameter, double friction) {
		//this checks for shock flow is already choked at this point
		setChoked(true);
		
		if(MachIN > 1) {
			setShock(true);
			setAreaSHOCK(Lstar1);
			Mach[1] = 1;
		}
		else {
			
			Mach[1] = 1;
		}
		
	}
	
	protected void NotChoked(double MachIN, double length, double Lstar1, double Diameter, double friction) {
		
		double Lstar2;
		double L;

		double Mguess;
		
		setChoked(false);
		
		//this checks for shocks
		Lstar2 = Lstar1-length;

		//if Lstar1 is less then length, shock needs to be placed at Lstar 1, and then values recalculated back up to length
		
		L = 0;
		Mguess = 0;
		while(Math.round(L*1000)/1000D != Math.round(Lstar2*1000)/1000D) {
			Mguess = Mguess + 0.001;
			L = (Diameter/(4*friction))*(((1-Math.pow(Mguess, 2))/(getGamma()*Math.pow(Mguess, 2)))+(((getGamma()-1)/(2*getGamma()))
					*Math.log(((getGamma()+1)*Math.pow(Mguess, 2))/(2+((getGamma()+1)*Math.pow(Mguess, 2))))));
		}
		
		Mach[1]=Mguess;
		
	}

	@Override
	protected void ExitCond() {
		
	}
	
}