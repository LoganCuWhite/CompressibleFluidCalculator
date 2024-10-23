
/*
 * Class:		CS225-01DB
 * Author: 		Logan White
 * Created: 	3/22/2023
 * Modified:
 *
 * Purpose:	controlls node types
 *
 * Methods: 	+NodeManager(double MachIN, double Pin, double Pback, double T, String Nodetype): void
 *				#Mach2toMach1(double MachIN, double Pin, double Pback, double T): void
 *				#Ratios(): void
 *				#ExitCond(double Mach): void
 *
 * Attributes: 	#Mach: double[]
 * 				#Area2toArea1, Area, length, Heat,  gamma, R : double
 *				#NodeType: String
 *			 	#choked, shock: boolean
 *				#AreaSHOCK: double
 *				#String ExitCondition
 *				#T02toT01, P02toP01, Rho02toRho01, T2toT1, P2toP1, Rho1toRho2: double
 */

public class NodeManager{

	//Attributes
	protected double Mach[] = new double[2];
	protected double Area2toArea1, Area;
	protected double length;
	protected double Heat;
	protected double gamma, R;


	protected String NodeType;

	protected boolean choked, shock;
	protected double AreaSHOCK;
	protected String ExitCondition;


	//all the data is saved as ratios,
	protected double T02toT01, P02toP01, Rho02toRho01, T2toT1, P2toP1, Rho1toRho2;


	public NodeManager() {
	}

	protected void Mach2toMach1(double MachIN, double Pin, double Pback, double T) {

	}


	protected void Ratios(double Mach) {

	}

	protected void ExitCond() {

	}


	//getters and setters;

	public double getGamma() {
		return gamma;
	}

	public double getArea() {
		return Area;
	}

	public void setArea(double area) {
		Area = area;
	}

	public double getArea2toArea1() {
		return Area2toArea1;
	}

	public void setArea2toArea1(double area2toArea1) {
		Area2toArea1 = area2toArea1;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public double getHeat() {
		return Heat;
	}

	public void setHeat(double heat) {
		Heat = heat;
	}

	public void setGamma(double gamma) {
		this.gamma = gamma;
	}

	public double getR() {
		return R;
	}

	public void setR(double r) {
		R = r;
	}

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

	public String getExitCondition() {
		return ExitCondition;
	}

	public void setExitCondition(String exitCondition) {
		ExitCondition = exitCondition;
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