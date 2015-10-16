import java.io.*;
import java.util.LinkedList;
import lib.ir.ast.*;
import lib.ir.semcheck.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java_cup.runtime.Symbol;
import lib.error.Error;
import lib.ir.interpreter.InterpreterVisitor;



public class Main {

    private static String outputName = "";
    private static String target = "parse";
    private static String inputName = "";
    /*Initialize the list of errors */
    private static List<Error> errors = new LinkedList();


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
                        if ((i+1 < argv.length) && (argv[i+1].charAt(0) != '-') && (argv[i+1].equals("scan") || argv[i+1].equals("parse") || argv[i+1].equals("semantic") || argv[i+1].equals("interpreter"))){
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
                    reports();
                    break;
                case "semantic":
                    semCheck(Parse(pr));
                    reports();
                    break;
                case "interpreter":
                    Program p = Parse(pr);
                    semCheck(p);
                    interpreter(p);
                    reports();
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
              //Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Unrecoverable syntax error");
        }
        res = res.substring(0, res.length()-1);
        System.out.println(res);
    }

    private static Program Parse(CtdsParser pr){
        try {
            /* Start the parser */
            Program program = (Program) pr.parse().value;
            return program;
        }
        catch(Exception e){
            //Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Unrecoverable syntax error");
            return null;
        }
        
    }

    private static void semCheck(Program program){
        try{        
            /* Initialize the visitors */
            CheckMainVisitor mainVisitor = new CheckMainVisitor();
            CheckSemVisitor semVisitor = new CheckSemVisitor();
            CheckCycleSentencesVisitor cycleVisitor = new CheckCycleSentencesVisitor();

            /*Visit the ast*/
            mainVisitor.visit(program);
            cycleVisitor.visit(program);
            semVisitor.visit(program);
            
            /*append errors*/
            errors.addAll(mainVisitor.getErrors());
            errors.addAll(cycleVisitor.getErrors());
            errors.addAll(semVisitor.getErrors());
            
        }
        catch(Exception ex){
            System.out.println("Unrecoverable syntax error");
            //Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            
        }
    }
    
    private static void interpreter(Program program){
        InterpreterVisitor interpreter = new InterpreterVisitor();
        interpreter.visit(program);
        errors.addAll(interpreter.getErrors());
    }
    
    private static void reports(){
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
}


