import java.io.*;
import ir.ast.*;
import ir.ASTVisitor;
import ir.semcheck.*;
import java_cup.runtime.Symbol;



public class Main {

    private static String outputName = "nombreArchivo";
    private static String target = "parse";
    private static String inputName = "nombreArchivo";


    static public void main(String argv[]) throws Exception {
       int i = 0;
        while (i<argv.length){
            switch (argv[i]){
                case "-o" : 
                    if ((i+1 < argv.length) && (argv[i+1].charAt(0) != '-')){
                        outputName = argv[i+1];
                        i+=2;
                        break;
                    }
                    else throw new Exception("Ilegal arguments");
                case "-target" :
                    if ((i+1 < argv.length) && (argv[i+1].charAt(0) != '-') && (argv[i+1].equals("scan") || argv[i+1].equals("parse"))){
                        target = argv[i+1];
                        i+=2;
                        break;
                    }
                    else throw new Exception("Ilegal arguments");
                    
                default:
                    if ((i+1 < argv.length) || (argv[i].charAt(0) == '-')) throw new Exception("Ilegal arguments");
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
        parser pr = new parser( scanner );
        
        switch (target){
            case "scan": 
                Scan(scanner);
                break;
            case "parse":
                Parse(pr);
                break;
        }
    }

    private static void Scan(CtdsLexer scanner) throws Exception{
        Symbol s;
        String res = "";
        do {
          s = scanner.next_token(); 
          res += "token: "+ s.value + "\n";
        } while ((s.sym != sym.EOF) && (s.sym != sym.ERROR));
        if (s.sym == sym.EOF) res += "No errors.";
        else res = "Syntax error in line " + (s.left+1) + " column " + (s.right+1) + " value : " + (s.value);
        System.out.println(res);
    }

    private static Program Parse(parser pr) throws Exception{
        
        ASTVisitor visitor = new CheckSemVisitor();
        /* Start the parser */
        Program result = (Program) pr.parse().value;
        visitor.visit(result);
        System.out.println("No errors.");

        return result;
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
