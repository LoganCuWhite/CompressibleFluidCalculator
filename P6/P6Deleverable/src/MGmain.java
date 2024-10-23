
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
		
		runUI = new NozzleUI();
		runUI.show();


	}







}


