import java.io.*;
import lib.ir.ast.*;
import lib.ir.ASTVisitor;
import lib.ir.semcheck.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java_cup.runtime.Symbol;
import lib.error.Error;



public class Main {

    private static String outputName = "nombreArchivo";
    private static String target = "parse";
    private static String inputName = "nombreArchivo";


    static public void main(String argv[]) {
        try {
            int i = 0;
            while (i<argv.length){
                switch (argv[i]){
                    case "-o" :
                        if ((i+1 < argv.length) && (argv[i+1].charAt(0) != '-')){
                            outputName = argv[i+1];
                            i+=2;
                            break;
                        }
                        else{
                            //throw new Exception("Ilegal arguments");
                            System.err.println("Ilegal arguments");
                            break;
                            
                        }
                    case "-target" :
                        if ((i+1 < argv.length) && (argv[i+1].charAt(0) != '-') && (argv[i+1].equals("scan") || argv[i+1].equals("parse"))){
                            target = argv[i+1];
                            i+=2;
                            break;
                        }
                        else{
                            //throw new Exception("Ilegal arguments");
                            System.err.println("Ilegal arguments");
                            break;
                            
                        }
                    
                    default:
                        if ((i+1 < argv.length) || (argv[i].charAt(0) == '-')){
                            System.out.println(argv);
                            
                            //throw new Exception("Ilegal arguments");
                            System.err.println("Ilegal arguments");
                            break;
                        }
                        else{
                            inputName = argv[i];
                            i++;
                            break;
                        }    
                    
                }
            }
            
            
            /* Instantiate the scanner and open input file argv[0] */
            CtdsLexer scanner = new CtdsLexer(new FileReader(inputName));
            /* Instantiate the parser */
            CtdsParser pr = new CtdsParser( scanner );
            
            switch (target){
                case "scan":
            {
                try {
                    Scan(scanner);
                } catch (Exception ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
                    break;
                case "parse":
            {
                try {
                    Parse(pr);
                } catch (Exception ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
                    break;
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void Scan(CtdsLexer scanner) throws Exception{
        Symbol s;
        String res = "";
        do {
          s = scanner.next_token();
          if (s.sym != sym.EOF)
            res += s.toString() + " " + s.value + "\n";
        } while ((s.sym != sym.EOF)); //&& (s.sym != sym.ERROR));
        res = res.substring(0, res.length()-1);
        //if (s.sym == sym.EOF) res += "No errors.";
        //else res = "Syntax error in line " + (s.left+1) + " column " + (s.right+1) + " value : " + (s.value);
        System.out.println(res);
    }

    private static void Parse(CtdsParser pr) throws Exception{
        
        CheckSemVisitor visitor = new CheckSemVisitor();
        /* Start the parser */
        
        Program result = (Program) pr.parse().value;
        visitor.visit(result);
        List<Error> errors = visitor.getErrors();
        
        if (errors.isEmpty()) System.out.println("No errors.");
        else System.out.println(errors);

        //return result;
    }
}





//            ASTVisitor visitor = new PrintVisitor();
            /* Instantiate the scanner and open input file argv[0] */
//            CtdsLexer scanner = new CtdsLexer( new FileReader(argv[0]) ); 
            /* Instantiate the parser */
//            parser pr = new parser( scanner );
//       try {
            //Scan(scanner);
//            Program p = Parse(pr);
            //visitor.visit(p);
 //       } 
 //       catch (Exception e) {
            //e.printStackTrace();
            //System.out.println("Syntaxis problem!");
 //       }
