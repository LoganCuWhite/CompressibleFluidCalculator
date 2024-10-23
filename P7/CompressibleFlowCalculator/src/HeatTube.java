/*
 * Class:		CS225-01DB
 * Author: 		Logan White
 * Created: 	3/22/2023
 * Modified:	
 * 
 * Purpose:
 * 
 * 
 * Methods: 	#HeatTube(): void 
 *				#Mach2toMach1(double MachIN, double Pin, double T,): void 
 *				#IsChoked(double MachIn, double T01, double T02): void
 *				#NotChoked(double MachIn, double T01, double T02): void
 *
 * Attributes: 	-T0: double[]
 *
 */

public class HeatTube extends NodeManager {

	private double[] T0 = new double[2];

	public HeatTube() {

	}

	@Override
	protected void Mach2toMach1(double MachIN, double Pin, double T) {

		Isen[0] = new Isentropic();

		double Cp;

		Cp = (getR()*(getGamma()/(getGamma()-1)))/1000;

		Mach[0] = MachIN;
		
		Isen[0].IsentropicRelations(Mach[0], getGamma());
		T0[0] = Isen[0].getISENTROPICT0toT() * T;

		T0[1] = (getHeat()/Cp)+T0[0];

		isChoked(Mach[0], T0[0], T0[1]);


		Ratios( Pin,  T);

	}

	protected void isChoked(double MachIn, double T01, double T02) {

		double T0star;

		T0star = Math.pow((((getGamma()+1)*Math.pow(MachIn, 2))/Math.pow(1+getGamma()*Math.pow(MachIn, 2), 2))
				*(2+((getGamma()-1)*Math.pow(MachIn, 2))), -1) * T01;


		if(T02>T0star) {
			System.out.println(T02);
			System.out.println("Flow is chocked");
			Mach[1] = 1;
			setMach(Mach);

			setT02toT01(T0star/T01);
		}

		else {
			setT02toT01(T02/T01);
			NotChoked( MachIn,  T01,  T02);

		}

	}

	protected void NotChoked(double MachIn, double T01, double T02) {

		double Mguess;

		double T0ratio;
		double staticTempRatioeq;
		int modifier;

		T0ratio = T02/T01;

		modifier = 1;
		Mguess = MachIn;

		if (MachIn > 1 && Heat >0) {
			modifier = -1;
		}
		else if (MachIn < 1 && Heat < 0) {
			modifier = -1;
		}
		staticTempRatioeq = 0;

		while(Math.round(T0ratio*1000)/1000D != Math.round(staticTempRatioeq*1000)/1000D) {

			Mguess = Mguess+(0.0001*modifier);
 
			staticTempRatioeq = Math.pow((1+(getGamma()*Math.pow(MachIn, 2)))/(1+(getGamma()*Math.pow(Mguess, 2))),2)
					*Math.pow(Mguess/MachIn,2)
					*((1+(((getGamma()-1)/2) *Math.pow(Mguess, 2)))/(1+ (((getGamma()-1)/2) * Math.pow(MachIn, 2))));

		}
		Mach[1] = Mguess;

		setMach(Mach);
	}


}

