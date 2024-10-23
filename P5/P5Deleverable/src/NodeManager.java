
/*
 * Class:		CS225-01DB
 * Author: 		Logan White
 * Created: 	3/22/2023
 * Modified:	
 * 
 * Purpose:	controlls node types
 * 
 * Methods: 	+NodeManager(double MachIN, double Area1, double Area2, double Pin, double Pback, double T, double Heat, double gamma, double R, String Nodetype): void 
 *				#Mach2toMach1(double MachIN, double Pin, double Pback, double T, double gamma, double R)): void 
 *				#Ratios(double Mach): void
 *
 * Attributes: 	#Mach: double[]
 * 				#Area1: double
 * 				#Area2: double
 * 				#Heat: double
 *				#NodeType: String
 *				#shock: boolean
 *			 	#shock: boolean
 *				#AreaSHOCK: double
 *				#SHOCKT02toT01: double	
 *				#SHOCKP02toP01: double
 *				#SHOCKRho02toRho01: double
 *				#SHOCKT2toT1: double
 *				#SHOCKP2toP1: double
 *				#SHOCKRho2toRho1: double	
 */

public class NodeManager{

	//Attributes
	protected double Mach[] = new double[2];
	protected double Area1;
	protected double Area2;
	protected double Heat;

	protected String NodeType;

	protected boolean choked;
	protected boolean shock;
	protected double AreaSHOCK;

	//all the data is saved as ratios, 
	protected double T02toT01;	
	protected double P02toP01;
	protected double Rho02toRho01;
	protected double T2toT1;
	protected double P2toP1;
	protected double Rho1toRho2;	



	public NodeManager(double MachIN, double Area1, double Area2, double Pin, double Pback, double T, double Heat, double gamma, double R, String Nodetype) {

		//type of Node 
		this.NodeType = NodeType;
		//setting all the inputs gained from controller
		this.Mach[1] = MachIN;
		this.Area1 = Area1;
		this.Area2 = Area2;
		this.Heat = Heat;


	}

	protected void Mach2toMach1(double MachIN, double Pin, double Pback, double T,  double gamma,double R) {

	}
	

	protected void Ratios(double Mach) {

	}

	
	//getters and setters;
	public double getMach(int i) {
		return Mach[i];
	}


	public void setMach(double[] mach) {
		Mach = mach;
	}

	public boolean getChoked() {
		return shock;
	}

	public void setChoked(boolean shock) {
		this.shock = shock;
	}
	
	public boolean getShock() {
		return shock;
	}

	public void setShock(boolean shock) {
		this.shock = shock;
	}

	public double getAreaSHOCK() {
		return AreaSHOCK;
	}

	public void setAreaSHOCK(double astar) {
		AreaSHOCK = astar;
	}

	public void setP02toP01(double p02toP01) {
		P02toP01 = p02toP01;
	}

	public double getT02toT01() {
		return T02toT01;
	}


	public void setT02toT01(double t02tot01) {
		T02toT01 = t02tot01;
	}


	public double getP02toP01() {
		return P02toP01;
	}


	public void setP01toP02(double p02toP01) {
		P02toP01 = p02toP01;
	}


	public double getRho02toRho01() {
		return Rho02toRho01;
	}


	public void setRho02toRho01(double rho02torho02) {
		Rho02toRho01 = rho02torho02;
	}


	public double getT2toT1() {
		return T2toT1;
	}


	public void setT2toT1(double t2toT1) {
		T2toT1 = t2toT1;
	}


	public double getP2toP1() {
		return P2toP1;
	}


	public void setP2toP1(double p2toP1) {
		P2toP1 = p2toP1;
	}


	public double getRho1toRho2() {
		return Rho1toRho2;
	}


	public void setRho1toRho2(double rho2toRho1) {
		Rho1toRho2 = rho2toRho1;
	}






}