import java.io.*;
import java.util.LinkedList;
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
    private static String inputName = "";


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
                            System.err.println("Ilegal arguments");
                            i=argv.length;
                            break;
                        }
                    case "-target" :
                        if ((i+1 < argv.length) && (argv[i+1].charAt(0) != '-') && (argv[i+1].equals("scan") || argv[i+1].equals("parse") || argv[i+1].equals("semantic"))){
                            target = argv[i+1];
                            i+=2;
                            break;
                        }
                        else{
                            System.err.println("Ilegal arguments");
                            i=argv.length;
                            break;
                        }
                    default:
                        if ((i+1 < argv.length) || (argv[i].charAt(0) == '-')){
                            System.err.println("Ilegal arguments");
                            i=argv.length;
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
                    Scan(scanner);
                    break;
                case "parse":
                    Parse(pr);
                    break;
                case "semantic":
                    semCheck(pr);
                    break;
                default :
                    System.err.println("Ilegal arguments");
                    break;
            }
        } catch (FileNotFoundException ex) {
            System.err.println("Input file not found");
        }
    }

    private static void Scan(CtdsLexer scanner){
        Symbol s;
        String res = "";
        try {
        do {  
            s = scanner.next_token();
            if (s.sym != sym.EOF)
                res += s.toString() + " " + s.value + "\n";
        } while ((s.sym != sym.EOF));
        } catch (IOException ex) {
              Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        res = res.substring(0, res.length()-1);
        System.out.println(res);
    }

    private static void Parse(CtdsParser pr){
        try {
            pr.parse();
            System.out.println("Not errors");
        }
        catch(Exception e){}
        
    }
    
    private static void semCheck(CtdsParser pr){
        try{
            /* Start the parser */
            Program program = (Program) pr.parse().value;
        
            /* Initialize the visitors */
            CheckMainVisitor mainVisitor = new CheckMainVisitor();
            CheckSemVisitor semVisitor = new CheckSemVisitor();
            CheckCycleSentencesVisitor cycleVisitor = new CheckCycleSentencesVisitor();
        
            /*Initialize the list of errors */
            List<Error> errors = new LinkedList();

            /*Visit the ast*/
            mainVisitor.visit(program);
            cycleVisitor.visit(program);
            semVisitor.visit(program);
            
            /*append errors*/
            errors.addAll(mainVisitor.getErrors());
            errors.addAll(cycleVisitor.getErrors());
            errors.addAll(semVisitor.getErrors());
            
            /*Informate*/
            if (errors.isEmpty()){
                System.out.println("Not errors");
            }
            else{
                /*Print errors */
                for(Error e : errors){
                    System.out.println(e);
                }
            }
        }
        catch(Exception ex){
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
