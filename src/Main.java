import java.io.*;
import ir.ast.*;
import ir.ASTVisitor;
import ir.semcheck.*;



public class Main {
    static public void main(String argv[]) throws Exception {
            ASTVisitor visitor = new PrintVisitor();
            /* Instantiate the scanner and open input file argv[0] */
            CtdsLexer scanner = new CtdsLexer( new FileReader(argv[0]) ); 
            /* Instantiate the parser */
            parser pr = new parser( scanner );
       try {
            /* Start the parser */
            Program result = (Program) pr.parse().value;
            
            visitor.visit(result);

            System.out.println("Syntaxis correct!");
        } 
        catch (Exception e) {
            e.printStackTrace();
            //System.out.println("Syntaxis problem!");
        }
    }
}
