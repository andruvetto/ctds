import java.io.*;
public class Main {
    static public void main(String argv[]) throws Exception {
        
            /* Instantiate the scanner and open input file argv[0] */
            CtdsLexer scanner = new CtdsLexer( new FileReader(argv[0]) ); 
            /* Instantiate the parser */
            parser pr = new parser( scanner );
       try {
            /* Start the parser */
            Object result = pr.parse();
            System.out.println("Syntaxis correct!");
        } 
        catch (Exception e) {
            e.printStackTrace();
            //System.out.println("Syntaxis problem!");
        }
    }
}
