
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
 * Attributes: -Calculator: Controller
 *
 */


public class MGmain{


	private Controller Calculator;


	public static void main(String[] args) {

		MGmain starter = new MGmain();

		starter.runProgram();
	}
	

	public void runProgram() {
		
		Calculator = new Controller();
		Calculator.runProgram();
		
		
	}







}


