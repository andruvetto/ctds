import java.io.*; 
class Main { 
   public static void main(String argv[]) throws Exception { 
      CtdsLexer scanner = new CtdsLexer( new FileReader(argv[0]) ); 
      parser pr = new parser( scanner ); 
      pr.parse();
      if ((pr.scan().toString().equals("#0"))){
        System.out.println("Syntaxis correct!" );
      }
    } 
 } 
