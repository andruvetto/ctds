import java.io.*;
import java.util.LinkedList;
import lib.ir.ast.*;
import lib.ir.semcheck.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java_cup.runtime.Symbol;
import lib.error.Error;
import lib.ir.icode.ICodeVisitor;
import lib.ir.icode.Instruction;
import lib.ir.interpreter.InterpreterVisitor;



public class Main {

    private static String outputName = "";
    private static String target = "icode";
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
                        if ((i+1 < argv.length) && (argv[i+1].charAt(0) != '-') && (argv[i+1].equals("scan") || argv[i+1].equals("parse") || argv[i+1].equals("semantic") || argv[i+1].equals("interpreter") || argv[i+1].equals("icode"))){
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
                    {
                        Scan(scanner);
                    }
                    break;
                case "parse":
                    {
                        Program p = Parse(pr);
                        reports(p);
                    }
                    break;
                case "semantic":
                    {
                        Program p = Parse(pr);
                        semCheck(p);
                        reports(p);
                    }
                    break;
                case "interpreter":
                    {
                        Program p = Parse(pr);
                        semCheck(p);
                        interpreter(p);
                    }
                    break;
                case "icode":
                    {
                        Program p = Parse(pr);
                        semCheck(p);
                        iCode(p);
                    }
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

    private static Program Parse(CtdsParser pr){
        try {
            /* Start the parser */
            Program program = (Program) pr.parse().value;
            return program;
        }
        catch(Exception e){
            return null;
        }
        
    }

    private static void semCheck(Program program){
        if (program != null){
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
    }

    
    private static void interpreter(Program program){
        if ((program != null) && errors.isEmpty()){
            InterpreterVisitor interpreter = new InterpreterVisitor();
            interpreter.visit(program);
            errors.addAll(interpreter.getErrors());
        }
        if (!errors.isEmpty()){
            reports(program);
        }
    }
    
    
    private static void iCode(Program program){
        if (program != null){
            if (errors.isEmpty()){
                ICodeVisitor icode = new ICodeVisitor();
                for (Instruction i : icode.visit(program)){
                    System.out.println(i);
                }
            }
            else{
                reports(program);
            }
        }
    }
    
    private static void reports(Program program){
        if (program != null){
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
}


