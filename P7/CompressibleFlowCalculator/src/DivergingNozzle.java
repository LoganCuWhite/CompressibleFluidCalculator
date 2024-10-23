/*
 * Class:		CS225-01DB
 * Author: 		Logan White
 * Created: 	3/22/2023
 * Modified:	
 * 
 * Purpose:	diverging nozzle math
 * 
 * Methods: 	#DivergingNozzle(double MachIN, double Pin, double getPback(), double T, String Nodetype): void 
 *				#Mach2toMach1(double MachIN, double Pin, double getPback(), double T): void 
 *				#shockinside(double MachOut, double P01):void
 *
 * Attributes: 	
 *
 */

public class DivergingNozzle extends NodeManager {


	public DivergingNozzle() {

	}

	@Override
	protected void Mach2toMach1(double MachIN, double Pin, double T ) {
		Isen[0] = new Isentropic();

		double Guesser, Mguess, T01;
		double AstartoA1,Astar, AstartoA2, Area2;
		
		Mach[0] = MachIN; 
		Isen[0].IsentropicRelations(Mach[0], getGamma());
		AstartoA1 = Math.pow((1/Mach[0])* Math.pow((1+(((getGamma()-1)*Math.pow(Mach[0],2))/2))/(1+(getGamma()-1)/2),(getGamma()+1)/(2*(getGamma()-1))),-1);

		Astar = AstartoA1 * getArea();
		Area2 = getArea() *getArea2toArea1();
		AstartoA2 = Astar/Area2;
		T01  = T*(1 +( (gamma-1)/2)*Math.pow(MachIN,2));
		
		Guesser = 0;

		if(Mach[0] < 1) {

			Mguess = 0;
			while(Math.round(Guesser*10)/10D != Math.round(Mach[0]*10)/10D ) {
				Mguess = Mguess + (0.0001);
				Guesser = getArea2toArea1()* Mguess * Math.sqrt((T01/(1 +( (gamma-1)/2)*Math.pow(MachIN,2)))/T);
			}
		}
		else {

			Mguess = Mach[0];
			while(Math.round(Guesser*1000)/1000D != Math.round(AstartoA2*1000)/1000D ) {
				Mguess = Mguess + 0.001;
				Guesser = Math.pow((1/Mguess)* Math.pow((1+(((getGamma()-1)*Math.pow(Mguess,2))/2))/(1+(getGamma()-1)/2),(getGamma()+1)/(2*(getGamma()-1))),-1);
			}
		}

		Mach[1] = Mguess;
		setMach(Mach);
		Ratios( Pin,  T);

	}

	protected void shockinside(double MachOut, double P01) {
		Isen[2] = new Isentropic();
		NS[1] = new NormalShock();

		double P02, P02toP01, MatShock, AstartoAshock, Astar, ASHOCK;

		Astar = Math.pow((1/Mach[0])* Math.pow((1+(((getGamma()-1)*Math.pow(Mach[0],2))/2))/(1+(getGamma()-1)/2),(getGamma()+1)/(2*(getGamma()-1))),-1) * getArea();

		Isen[2].IsentropicRelations(MachOut, getGamma());

		P02 = Isen[2].getISENTROPICP0toP()*getPback();

		P02toP01 = P01/P02;
		setP02toP01(P02toP01);
		
		MatShock = 1;
		NS[1].NormalShockRelations(MatShock, getGamma());
		while(Math.round(P02toP01*1000)/1000D != Math.round(NS[1].getSHOCKP02toP01()*1000)/1000D ) {
			MatShock = MatShock + 0.001;
			NS[1].NormalShockRelations(MatShock, getGamma());
		}

		AstartoAshock = Math.pow((1/MatShock)* Math.pow((1+(((getGamma()-1)*Math.pow(MatShock,2))/2))/(1+(getGamma()-1)/2),(getGamma()+1)/(2*(getGamma()-1))),-1);

		ASHOCK = Astar/AstartoAshock;

		if (ASHOCK > getArea()) {
			setAreaSHOCK(ASHOCK);
		}
	}
	

}