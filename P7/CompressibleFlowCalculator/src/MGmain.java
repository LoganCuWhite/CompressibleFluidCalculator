
/*R2
 * Class:		CS225-01DB
 * Author: 		Logan White
 * Created: 	3/22/2023
 * Modified:	
 * 
 * Purpose:	starts the program
 * 
 * Methods: 	+main(String[] args): static void
 * 				+runProgram(): void
 *
 * Attributes: -runUI: NozzleUI
 *
 */


public class MGmain{


	private NozzleUI runUI;

	public static void main(String[] args) {

		MGmain starter = new MGmain();

		starter.runProgram();
	}
	

	public void runProgram() {
		
		//assumption print
		System.out.println("Converging and divering nozzles, are isentropic wherever a shock is not pressent, Adiabadic, constant density, and are fricitonless with no heat added");
		System.out.println("Heat tubes, are isentropic at the very begining and end, they have constant massflow, and conserve energy, and are frictionless");
		System.out.println("Friction tubes, are isentropic at the very begining and end, they have consttant massflow, and conserve energy, and have no added heat other than friction");
		runUI = new NozzleUI();
		runUI.show();


	}







}


