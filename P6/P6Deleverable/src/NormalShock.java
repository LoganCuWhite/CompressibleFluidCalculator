
/*
 * Class:		CS225-01DB
 * Author: 		Logan White
 * Created: 	3/22/2023
 * Modified:	
 * 
 * Purpose:	provides static and stagnation over a shock ratios baised on mach and gamma
 * 
 * Methods: 	+NormalShock(): void 
 *				+NormalShockRelations(double MachIN, double gamma): void 
 *
 * Attributes: 	-Isen: Isentropic[]
 *				-Mach: double[]
 *				-gamma: double
 *				-SHOCKT02toT01, SHOCKP02toP01, SHOCKRho02toRho01, SHOCKT2toT1, SHOCKP2toP1, SHOCKRho2toRho1: double	
 */

public class NormalShock{

	//for when isentro
	private Isentropic[] Isen = new Isentropic[2];

	//Attributes
	private double[] Mach = new double[2];

	//all the data is saved as ratios, 
	private double SHOCKT02toT01, SHOCKP02toP01, SHOCKRho02toRho01;
	private double SHOCKT2toT1, SHOCKP2toP1, SHOCKRho2toRho1;	

	
	public NormalShock() {

	}

	//calculates all the ratios of values over the shock
	public void NormalShockRelations(double MachIN, double gamma) {

		Isen[0] = new Isentropic();
		Isen[1] = new Isentropic();

		//Temporary vars to store data
		double[] MachHERE; 

		double StaticPressureRatio, StaticTempRatio, StaticDensityRatio;
		double StagnationPressureRatio, StagnationTempRatio, StagnationDensityRatio;

		MachHERE = new double[2];

		//because this project is for nozzle flow pieces all shocks are assumed to be Normal to the flow cutting out the required trig.
		//Mach[0] is the Mach at the permiable layer in front of the shock, Mach[1] is the Mach immediately after the shock.
		MachHERE[0] = MachIN;
		MachHERE[1] = 0;
		//the 2nd Mach value is biased only on the gamma value of air, and the Mach entering the shock.
		MachHERE[1] = Math.sqrt((((gamma - 1)*Math.pow(MachHERE[0], 2))+2)
				/((2*gamma*Math.pow(MachHERE[0], 2))-(gamma-1)));


		//Flow is isentropic directly before and after the shock so Isentropic values must be called upon,
		Isen[0].IsentropicRelations(MachHERE[0], gamma);

		Isen[1].IsentropicRelations(MachHERE[1], gamma);

		//static ratios all in the form region 2/1
		StaticPressureRatio = 1+((2*gamma)/(gamma+1)*((Math.pow(MachHERE[0],2 )-1)));

		StaticTempRatio = (1+((2*gamma)/(gamma+1)*((Math.pow(MachHERE[0],2 )-1))))*((2+(gamma-1)
				*Math.pow(MachHERE[0], 2 ))/((gamma+1)*Math.pow(MachHERE[0], 2 )));

		StaticDensityRatio = (((gamma+1)*Math.pow(MachHERE[0], 2 ))/(2+(gamma-1)*Math.pow(MachHERE[0], 2 )));


		//Stagnation ratios
		//the math behind stagnation ratios over a shock never looks pretty it looks like: P02/P01 = P2/P1 * P1/P01 * P02/P2
		StagnationPressureRatio = StaticPressureRatio 																 	// P2/P1
				* Math.pow(Isen[0].getISENTROPICP0toP()	,-1) 	// P1/P01	
				* Isen[1].getISENTROPICP0toP();				 	// P02/P2

		StagnationTempRatio = StaticTempRatio 			// T2/T1
				* Math.pow(Isen[0].getISENTROPICT0toT(),-1) 		// T1/T01
				* Isen[1].getISENTROPICT0toT();			   		// T02/T2

		StagnationDensityRatio = StaticDensityRatio 	 // Rho2/Rho1
				* Math.pow(Isen[0].getISENTROPICRho0toRho(), -1) 	 // Rho1/Rho01
				* Isen[1].getISENTROPICRho0toRho();				 // Rho2/Rho02


		//Placing values in the setters
		setSHOCKP2toP1(StaticPressureRatio);
		setSHOCKT2toT1(StaticTempRatio);
		setSHOCKRho2toRho1(StaticDensityRatio);

		setSHOCKP02toP01(StagnationPressureRatio);
		setSHOCKT02toT01(StagnationTempRatio);
		setSHOCKRho02toRho01(StagnationDensityRatio);

		setMach(MachHERE);
	}


	//Setters and Getters

	public double getMach(int i) {
		return Mach[i];
	}

	public void setMach(double[] Mach) {
		this.Mach = Mach;
	}

	public double getSHOCKT02toT01() {
		return SHOCKT02toT01;
	}

	public void setSHOCKT02toT01(double sHOCKT02toT01) {
		this.SHOCKT02toT01 = sHOCKT02toT01;
	}

	public double getSHOCKP02toP01() {
		return SHOCKP02toP01;
	}

	public void setSHOCKP02toP01(double sHOCKP02toP01) {
		SHOCKP02toP01 = sHOCKP02toP01;
	}

	public double getSHOCKRho02toRho01() {
		return SHOCKRho02toRho01;
	}

	public void setSHOCKRho02toRho01(double sHOCKRho02toP01) {
		SHOCKRho02toRho01 = sHOCKRho02toP01;
	}

	public double getSHOCKT2toT1() {
		return SHOCKT2toT1;
	}

	public void setSHOCKT2toT1(double sHOCKT2toT1) {
		SHOCKT2toT1 = sHOCKT2toT1;
	}

	public double getSHOCKP2toP1() {
		return SHOCKP2toP1;
	}

	public void setSHOCKP2toP1(double sHOCKP2toP1) {
		SHOCKP2toP1 = sHOCKP2toP1;
	}

	public double getSHOCKRho2toRho1() {
		return SHOCKRho2toRho1;
	}

	public void setSHOCKRho2toRho1(double sHOCKRho2toRho1) {
		SHOCKRho2toRho1 = sHOCKRho2toRho1;
	}

}