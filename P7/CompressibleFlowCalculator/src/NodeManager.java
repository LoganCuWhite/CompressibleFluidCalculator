
/*
 * Class:		CS225-01DB
 * Author: 		Logan White
 * Created: 	3/22/2023
 * Modified:
 *
 * Purpose:	controlls node types
 *
 * Methods: 	+NodeManager(): void
 *				#Mach2toMach1(double MachIN, double Pin, double T): void
 *				#shockinside(double MachOut, double P01): void
 *				#Ratios(double Pin, double T): void
 *				#ExitCond(double Pout): void
 *
 * Attributes: 	#Isen: Isentropic[]
 *				#NsS: NormalShock[]
 * 				#Mach: double[]
 * 				-graphDimensions: int[]
 * 				#Area2toArea1, Area, AreaSHOCK: double
 * 				#length, Heat, Pback, gamma, R: double
 *				#NodeType, ExitCondition: String
 *			 	#choked, shock: boolean
 *				#T02toT01, P02toP01, T2toT1, P2toP1: double
 */

public class NodeManager{

	//Attributes
	protected Isentropic[] Isen = new Isentropic[4];
	protected NormalShock[] NS = new NormalShock[4];
	
	protected double[] Mach = new double[4];
	private int graphDimensions[] = new int[4];
	protected double Area2toArea1, Area, AreaSHOCK;
	protected double length, Heat, Pback, gamma, R;

	protected String NodeType, ExitCondition;

	protected boolean choked, shock = false;

	//all the data is saved as ratios,
	protected double T02toT01, P02toP01,  T2toT1, P2toP1;

	
	public NodeManager() {
	}

	protected void Mach2toMach1(double MachIN, double Pin, double T) {

	}

	protected void shockinside(double MachOut, double P01) {
		
	}
	
	protected void Ratios( double Pin, double T) {
		
		Isen[1] = new Isentropic();

		Isen[1].IsentropicRelations(Mach[1], getGamma());		
		Isen[1].getISENTROPICP0toP();
		//Heat tubes have const P0
		
			setP02toP01(1);
			if(!NodeType.equals("Heat Tube")) {
			setT02toT01(1);
			}
		


		setP2toP1(getP02toP01()	* Isen[0].getISENTROPICP0toP() * Math.pow(Isen[1].getISENTROPICP0toP(), -1));
		setT2toT1(getT02toT01() * Isen[0].getISENTROPICT0toT()	* Math.pow(Isen[1].getISENTROPICT0toT(), -1));
	}

	protected void ExitCond(double P2) {

		double P02;
		double PexitSHOCKratio;

		Isen[1] = new Isentropic();
		NS[0] = new NormalShock();

		Isen[1].IsentropicRelations(Mach[1], getGamma());
		
		P02 =   (Isen[1].getISENTROPICP0toP()*P2);
		NS[0].NormalShockRelations(Mach[1], getGamma());
		PexitSHOCKratio =  P2* NS[0].getSHOCKP02toP01() *Isen[1].getISENTROPICP0toP() *Math.pow(NS[0].getSHOCKP2toP1(), -1);
		
		//if a shock occurs in a friction tube the flow comming out would not have an exit contition with a shock anyways
		if (Mach[1] > 1) {
			if (getPback() ==  PexitSHOCKratio) {
				setExitCondition("Shock at Exit");
				Mach[1] = NS[0].getMach(1);
			}
			else if (getPback() < PexitSHOCKratio && getPback() < P02) {
				setExitCondition("Over Expanded");
			}
			else if (getPback() == P02){
				setExitCondition("Perfectly Expanded");
			}
			else if (getPback() > P02 && P02 > PexitSHOCKratio){
				setExitCondition("Under Expanded");
			}
			else if ( getPback() > PexitSHOCKratio) {
				setExitCondition("Shock in Node");
				
				Mach[1] =Math.sqrt( (Math.pow(P02/getPback(),(gamma-1)/(gamma))-1) *2 /(gamma -1));

				shockinside( Mach[1], Isen[0].getISENTROPICP0toP()*getPback()*getP2toP1());
				setMach(Mach);
			}
			else {
				setExitCondition("Problem has occured: Bad Flow ");
			}

		}

		else if(Mach[1] <= 1) {
			setExitCondition("Subsonic at Exit");
		}

	}
	

	//getters and setters;
	public String getNodeType() {
		return NodeType;
	}
	public void setNodeType(String nodeType) {
		NodeType = nodeType;
	}
	
	public int getGraphDimensions(int i) {
		return graphDimensions[i];
	}
	public void setGraphDimensions(int[] graphDimensions) {
		this.graphDimensions = graphDimensions;
	}

	public double getGamma() {
		return gamma;
	}
	public void setGamma(double gamma) {
		this.gamma = gamma;
	}

	public double getPback() {
		return Pback;
	}
	public void setPback(double pback) {
		Pback = pback;
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

	public double getAreaSHOCK() {
		return AreaSHOCK;
	}
	public void setAreaSHOCK(double astar) {
		AreaSHOCK = astar;
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

	public double getR() {
		return R;
	}
	public void setR(double r) {
		R = r;
	}

	public String getExitCondition() {
		return ExitCondition;
	}
	public void setExitCondition(String exitCondition) {
		ExitCondition = exitCondition;
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

	public double getP02toP01() {
		return P02toP01;
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
	
	public double getP2toP1() {
		return P2toP1;
	}
	public void setP2toP1(double p2toP1) {
		P2toP1 = p2toP1;
	}

	public double getT2toT1() {
		return T2toT1;
	}
	public void setT2toT1(double t2toT1) {
		T2toT1 = t2toT1;
	}










}