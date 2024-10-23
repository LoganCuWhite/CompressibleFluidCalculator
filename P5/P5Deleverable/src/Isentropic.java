
/*
 * Class:		CS225-01DB
 * Author: 		Logan White
 * Created: 	3/22/2023
 * Modified:	
 * 
 * Purpose:	provides static to stagnation ratios baised on mach and gamma
 * 
 * Methods: 	+Isentropic(): void 
 *				+IsentropicRelations(double Mach, double gamma): void 
 *
 * Attributes: 	-Mach: double
 *				-gamma: double 
 *				-ISENTROPICP0toP: double 
 *				-ISENTROPICT0toT: double 
 *				-ISENTROPICRho0toRho: double 
 */

public class Isentropic{

	//Attributes
	private double Mach;
	private double gamma;

	//most of the time the program will only work in ratios
	private double ISENTROPICP0toP;
	private double ISENTROPICT0toT;
	private double ISENTROPICRho0toRho;


	public Isentropic() {

	}
	
	public void IsentropicRelations(double Mach, double gamma) {
		
		//all conversions between stagnation and static are done at a point, 
		//that point is assumed to be isentropic
		double PstagtoP;
		double TstagtoT;
		double RhostagtoRho;


		//Setting it equal to define it
		PstagtoP = Math.pow((1+(((gamma-1)/2)*Math.pow(Mach,2))),(gamma)/(gamma-1));

		TstagtoT = (1+(((gamma-1)/2)*Math.pow(Mach,2)));

		RhostagtoRho = Math.pow((1+(((gamma-1)/2)*Math.pow(Mach,2))),(1)/(gamma-1));


		//setting the values;
		setISENTROPICP0toP(PstagtoP);
		setISENTROPICT0toT(TstagtoT);
		setISENTROPICRho0toRho(RhostagtoRho);
	}

	
	
	public double getMach() {
		return Mach;
	}

	public void setMach(double mach) {
		Mach = mach;
	}

	public double getGamma() {
		return gamma;
	}

	public void setGamma(double gamma) {
		this.gamma = gamma;
	}

	public double getISENTROPICP0toP() {
		return ISENTROPICP0toP;
	}

	public void setISENTROPICP0toP(double iSENTROPICP0toP) {
		ISENTROPICP0toP = iSENTROPICP0toP;
	}

	public double getISENTROPICT0toT() {
		return ISENTROPICT0toT;
	}

	public void setISENTROPICT0toT(double iSENTROPICT0toT) {
		ISENTROPICT0toT = iSENTROPICT0toT;
	}

	public double getISENTROPICRho0toRho() {
		return ISENTROPICRho0toRho;
	}

	public void setISENTROPICRho0toRho(double iSENTROPICRho0toRho) {
		ISENTROPICRho0toRho = iSENTROPICRho0toRho;
	}
 
	//Getters and setters. 



	

}





