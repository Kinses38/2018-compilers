/*******************************
 ***** SECTION 1 - OPTIONS *****
 *******************************/

options { JAVA_UNICODE_ESCAPE = true; 
		  IGNORE_CASE = true;
		  DEBUG_TOKEN_MANAGER= false;}
/*********************************
 ***** SECTION 2 - USER CODE *****
 *********************************/

PARSER_BEGIN(CalParser)
public class CalParser {
	public static void main(String args[]){
		CalParser parser;
		if (args.length < 1){
			System.out.println("CAL parser: Reading from input: ");
			parser = new CalParser(System.in);
		} else if (args.length == 1) {
			System.out.println("CAL Parser: Reading from file " + args[0] + "...");
		
		try {
			parser = new CalParser(new java.io.FileInputStream(args[0]));
		} catch (java.io.FileNotFoundException e) {
			System.out.println("CAL Parser: File " + args[0] + "not found.");
			return;
			}
		}
		else {
			System.out.println("CAL Parser: Usage is one of:");
			System.out.println("		java CalParser < inputfile");
			System.out.println("OR");
			System.out.println("		java CalParser inputfile");
			return;
		}
		
		try {
			parser.Prog();
			System.out.println("CAL Parser: CAL program parsed successfully");
			}catch (ParseException e) {
				System.out.println(e.getMessage());
				System.out.println("CAL Parser: Encountered errors during parse.");

			}
	}
}
PARSER_END(CalParser)

/*****************************************
 ***** SECTION 3 - TOKEN DEFINITIONS *****
 *****************************************/

TOKEN_MGR_DECLS:
{
	static int commentNesting = 0;
}

SKIP : /*** Ignoring spaces/tabs/newlines ***/
{
	  " "
	| "\t"
	| "\n"
	| "\r"
	| "\f"
}

SKIP : /* Comments */
{
	"/*" { commentNesting ++;} : IN_COMMENT
}

<IN_COMMENT> SKIP :
{
	  "/*" { commentNesting++; }
	| "*/" { commentNesting --;
			 if (commentNesting == 0)
					SwitchTo(DEFAULT);
			}
	| <~[]>
}

TOKEN : /*Keywords and Punctuation */
{
	< SEMIC : ";">	
}