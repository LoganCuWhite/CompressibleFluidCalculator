/*
 * Class:		CS225-01DB
 * Author: 		Logan White
 * Created: 	3/22/2023
 * Modified:	
 * 
 * Purpose:	converging nozzle math
 * 
 * Methods: 	+ConvergingNozzle(): void 
 *				#Mach2toMach1(double MachIN, double Pin, double T): void 
 *				#isChoked(double MachIN, double ASHOCK): void
 *				#NotChoked(double MachIN, double T1, double ASHOCK, double Area2): void
 *
 * Attributes: 				
 *
 */

public class ConvergingNozzle extends NodeManager {


	public ConvergingNozzle() {
	}


	@Override
	protected void Mach2toMach1(double MachIN, double Pin, double T) {

		double ASHOCKtoA,ASHOCK, Area2;

		Mach[0] = MachIN; 

		ASHOCKtoA = Math.pow((1/MachIN)* Math.pow((1+(((getGamma()-1)*Math.pow(MachIN,2))/2))/(1+(getGamma()-1)/2),(getGamma()+1)/(2*(getGamma()-1))),-1);

		ASHOCK = ASHOCKtoA * getArea();
		Area2 = getArea() * getArea2toArea1();

		if(ASHOCK < Area2) {
			
			isChoked(Mach[0], ASHOCK);
			setChoked(true);
			if(Mach[0] > 1) {
				setShock(true);
				setAreaSHOCK(ASHOCK);
			}
		}
		else if(ASHOCK > Area2){
			NotChoked( Mach[0], T,  ASHOCK,  Area2);
		}
		else if(ASHOCK == Area2) {

			setChoked(true);
			if(Mach[0] > 1) {
				setShock(true);
				setAreaSHOCK(ASHOCK);
			}
		}

		Ratios( Pin,  T);
	}

	protected void isChoked(double MachIN, double ASHOCK) {
		
		setChoked(true);
		Mach[1] = 1;
		if(MachIN > 1) {
			setShock(true);
			setAreaSHOCK(ASHOCK);
		}
	}

	protected void NotChoked(double MachIN, double T1, double ASHOCK, double Area2) {
		double Guesser, Mguess, T01;
		Guesser = 0;

		T01  = T1*(1 +( (gamma-1)/2)*Math.pow(MachIN,2));
		Mguess = 0;
		
		while(Math.round(Guesser*10)/10D != Math.round(Mach[0]*10)/10D ) {
			Mguess = Mguess + (0.0001  );
			Guesser = getArea2toArea1()* Mguess * Math.sqrt((T01/(1 +( (gamma-1)/2)*Math.pow(MachIN,2)))/T1);

		}
		Mach[1] = Mguess;

	}
}