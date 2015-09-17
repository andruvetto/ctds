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
            for (File proof : proofs) {
                FileReader fr = new FileReader(proof);
                CtdsLexer scanner = new CtdsLexer(fr);
                parser pr = new parser( scanner );
                try {
                    pr.parse();
                    if (ifCorrects) {
                        System.out.println(proof.getName() + "------------------" + " Test OK!");
                    } else {
                        System.out.println(proof.getName() + "------------------" + " Test Fail");
                    }
                } catch (Exception e) {
                    if (ifCorrects) {
                        System.out.println(proof.getName() + "------------------" + " Test Fail");
                    } else {
                        System.out.println(proof.getName() + "------------------" + " Test OK!");
                    }
                }
            }
                }
   		else{
   			System.out.println("Directory " + directory + " does not exist");
   		}
   		
 	}
 	
}  
