import java.io.*; 
public class Test { 

        
        
	public static void main(String argv[]) throws Exception { 
            String directory = argv[0];
            System.out.println("-----------------------------------PROVING CORRECTS -----------------------------------------------------");
            test(directory+"/corrects",true);
            System.out.println("----------------------------------PROVING INCORRECTS ---------------------------------------------------");
            test(directory+"/incorrects",false);
 	}
 	
 	public static void test(String directory, Boolean ifCorrects) throws Exception{
                

                
		File f = new File(directory);
   		if(f.exists()){
   			File[] proofs = f.listFiles();
   			for(int i=0; i<proofs.length; i++){
   				FileReader fr = new FileReader(proofs[i]);
   				CtdsLexer scanner = new CtdsLexer(fr);  
                                parser pr = new parser( scanner );
                                try {
                                    pr.parse();
                                    if (ifCorrects){
                                        System.out.println(proofs[i].getName() + "------------------" + " Test OK!" );
                                    }
                                    else{
                                        System.out.println(proofs[i].getName() + "------------------" + " Test Fail" );
                                    }
                                }
                                catch (Exception e) {
                                    if (ifCorrects){
                                        System.out.println(proofs[i].getName() + "------------------" + " Test Fail" );
                                    }
                                    else{
                                        System.out.println(proofs[i].getName() + "------------------" + " Test OK!" );
                                    }
                                }
                        }
                }
   		else{
   			System.out.println("Directory " + directory + " does not exist");
   		}
   		
 	}
 	
}  
