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
 * Methods: 	+FrictionTube(): void 
 *				#Mach2toMach1(double MachIN, double Pin, double T): void 
 *				#isChoked(double Lstar1, double Diameter): void
 *				#NotChoked(double Lstar1, double Diameter): void
 *
 * Attributes: 	
 *				
 *
 */


public class FrictionTube extends NodeManager {

	public FrictionTube() {

	}

	@Override
	protected void Mach2toMach1(double MachIN, double Pin,  double T) {

		Isen[0] = new Isentropic();

		double Diameter, Lstar1;
		double[] T0;

		T0 = new double[2];

		Diameter = Math.sqrt(getArea()/Math.PI)*2;

		Mach[0] = MachIN;

		Isen[0].IsentropicRelations(Mach[0], getGamma());

		T0[0] = Isen[0].getISENTROPICT0toT() * T;

		Lstar1 = (Diameter/(4*getHeat()))*(((1-Math.pow(Mach[0], 2))/(getGamma()*Math.pow(Mach[0], 2)))+(((getGamma()+1)/(2*getGamma()))
				*Math.log(((getGamma()+1)*Math.pow(Mach[0], 2))/(2+((getGamma()-1)*Math.pow(Mach[0], 2))))));

		if (Lstar1 < getLength()) {
			isChoked( Lstar1,  Diameter);
		}
		else {
			NotChoked( Lstar1,  Diameter);
		}

		setMach(Mach);
		Ratios( Pin,  T);
	}

	protected void isChoked( double Lstar1, double Diameter ) {
		
		setChoked(true);

		if(Mach[0] > 1) {
			setShock(true);
			setAreaSHOCK(Lstar1);
		}

		Mach[1] = 1;
	}

	protected void NotChoked( double Lstar1, double Diameter) {

		double Lstar2, L, Mguess;
		int modifier;

		Lstar2 = Lstar1-getLength();

		L = 0;
		Mguess = 1;
		modifier = 1;
		if (Mach[0] > 1) {
			modifier = 1;
		}
		else if(Mach[0] < 1) {
			modifier = -1;
		}
		
		while(Math.round(L*10)/10 != Math.round(Lstar2*10)/10 && Mguess>= 0) {
			
			Mguess = Mguess + (0.0001* modifier);

			L = (Diameter/(4*getHeat()))*(((1-Math.pow(Mguess, 2))/(getGamma()*Math.pow(Mguess, 2)))+(((getGamma()+1)/(2*getGamma()))
					*Math.log(((getGamma()+1)*Math.pow(Mguess, 2))/(2+((getGamma()-1)*Math.pow(Mguess, 2))))));
			
		}
		
		Mach[1]=Mguess;
	}

}
	