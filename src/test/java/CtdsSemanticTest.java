import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import lib.ir.ast.Program;
import lib.ir.semcheck.CheckCycleSentencesVisitor;
import lib.ir.semcheck.CheckMainVisitor;
import lib.ir.semcheck.CheckSemVisitor;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.junit.BeforeClass;
import org.junit.After;
import org.junit.Rule;
import org.junit.FixMethodOrder;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/* 
 * This class provides a set of tests for the CTds Scanner 
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CtdsSemanticTest {

	@BeforeClass
    public static void initTest() {
        System.out.println("----------------------------------- Testing CTdsSemantic ---------------------------------");;
    	System.out.println();
    }

	@Rule
	public TestRule watcher = new TestWatcher() {
   		protected void starting(Description description) {
    		System.out.println("----------Starting test: " + description.getMethodName() + " ----------");
   		}
	};

	@After
	public void after() {
		System.out.println();
	}

	@Test
	public void test_arreglos() throws IOException {
		assertEquals(genericTest("test_arreglos.ctds"),true);	
	}
        
        @Test
	public void test_bloques() throws IOException {
		assertEquals(genericTest("test_bloques.ctds"),true);	
	}
        
        @Test
	public void test_bloques02() throws IOException {
		assertEquals(genericTest("test_bloques02.ctds"),true);	
	}
        
        @Test
	public void test_break() throws IOException {
		assertEquals(genericTest("test_break.ctds"),true);	
	}
        
        @Test
	public void test_float01() throws IOException {
		assertEquals(genericTest("test_float01.ctds"),true);	
	}
        
        @Test
	public void test_float02() throws IOException {
		assertEquals(genericTest("test_float02.ctds"),true);	
	}
        
        
        @Test
	public void test_float04() throws IOException {
		assertEquals(genericTest("test_float04.ctds"),true);	
	}
        
        
        @Test
	public void test_for02() throws IOException {
		assertEquals(genericTest("test_for02.ctds"),true);	
	}
        
        @Test
	public void test_for03() throws IOException {
		assertEquals(genericTest("test_for03.ctds"),true);	
	}
        
        @Test
	public void test_for04() throws IOException {
		assertEquals(genericTest("test_for04.ctds"),true);	
	}
        
        @Test
	public void test_for05() throws IOException {
		assertEquals(genericTest("test_for05.ctds"),true);	
	}
        
        @Test
	public void test_metodos01() throws IOException {
		assertEquals(genericTest("test_metodos01.ctds"),true);	
	}
        
        @Test
	public void test_metodos02() throws IOException {
		assertEquals(genericTest("test_metodos02.ctds"),true);	
	}
        
        @Test
	public void test_metodos03() throws IOException {
		assertEquals(genericTest("test_metodos03.ctds"),true);	
	}
        
        @Test
	public void test_logicos() throws IOException {
		assertEquals(genericTest("test_logicos.ctds"),true);	
	}
        
        @Test
	public void error_2class() throws IOException {
		assertEquals(genericTest("error_2class.ctds"),false);	
	}
        
        @Test
	public void error_2classb() throws IOException {
		assertEquals(genericTest("error_2classb.ctds"),false);	
	}
        
        @Test
	public void error_arreglos() throws IOException {
		assertEquals(genericTest("error_arreglos.ctds"),false);	
	}
        
        @Test
	public void error_arreglos02() throws IOException {
		assertEquals(genericTest("error_arreglos02.ctds"),false);	
	}
        @Test
	public void error_arreglos03() throws IOException {
		assertEquals(genericTest("error_arreglos03.ctds"),false);	
	}
        
        @Test
	public void error_arreglos04() throws IOException {
		assertEquals(genericTest("error_arreglos04.ctds"),false);	
	}
        
        @Test
	public void error_arreglos05() throws IOException {
		assertEquals(genericTest("error_arreglos05.ctds"),false);	
	}
        
        @Test
	public void error_bloques() throws IOException {
		assertEquals(genericTest("error_bloques.ctds"),false);	
	}
       
        @Test
	public void error_bloques02() throws IOException {
		assertEquals(genericTest("error_bloques02.ctds"),false);	
	}
        
        @Test
	public void error_break() throws IOException {
		assertEquals(genericTest("error_break.ctds"),false);	
	}
       
        @Test
	public void error_extern() throws IOException {
		assertEquals(genericTest("error_extern.ctds"),false);	
	}
       
        @Test
	public void error_extern02() throws IOException {
		assertEquals(genericTest("error_extern02.ctds"),false);	
	}
       
        @Test
	public void error_float01() throws IOException {
		assertEquals(genericTest("error_float01.ctds"),false);	
	}
        
        @Test
	public void error_float02() throws IOException {
		assertEquals(genericTest("error_float02.ctds"),false);	
	}
        
        @Test
	public void error_float03() throws IOException {
		assertEquals(genericTest("error_float03.ctds"),false);	
	}
        
        @Test
	public void error_float04() throws IOException {
		assertEquals(genericTest("error_float04.ctds"),false);	
	}
        
        @Test
	public void error_for01() throws IOException {
		assertEquals(genericTest("error_for01.ctds"),false);	
	}
        
        @Test
	public void error_for02() throws IOException {
		assertEquals(genericTest("error_for02.ctds"),false);	
	}
        
        @Test
	public void error_for03() throws IOException {
		assertEquals(genericTest("error_for03.ctds"),false);	
	}
        
        @Test
	public void error_for04() throws IOException {
		assertEquals(genericTest("error_for04.ctds"),false);	
	}
        
        @Test
	public void error_for05() throws IOException {
		assertEquals(genericTest("error_for05.ctds"),false);	
	}
        
        @Test
	public void error_if01() throws IOException {
		assertEquals(genericTest("error_if01.ctds"),false);	
	}
        
        @Test
	public void error_if02() throws IOException {
		assertEquals(genericTest("error_if02.ctds"),false);	
	}
        
        @Test
	public void error_logicos() throws IOException {
		assertEquals(genericTest("error_logicos.ctds"),false);	
	}
        
        @Test
	public void error_logicos02() throws IOException {
		assertEquals(genericTest("error_logicos02.ctds"),false);	
	}
        
        @Test
	public void error_main01() throws IOException {
		assertEquals(genericTest("error_main01.ctds"),false);	
	}
        
        @Test
	public void error_main02() throws IOException {
		assertEquals(genericTest("error_main02.ctds"),false);	
	}
        
        @Test
	public void error_main03() throws IOException {
		assertEquals(genericTest("error_main03.ctds"),false);	
	}
        
        @Test
	public void error_main04() throws IOException {
		assertEquals(genericTest("error_main04.ctds"),false);	
	}
        
        @Test
	public void error_metodos01() throws IOException {
		assertEquals(genericTest("error_metodos01.ctds"),false);	
	}
        
        @Test
	public void error_metodos02() throws IOException {
		assertEquals(genericTest("error_metodos02.ctds"),false);	
	}
        
        @Test
	public void error_metodos03() throws IOException {
		assertEquals(genericTest("error_metodos03.ctds"),false);	
	}
        
        @Test
	public void error_metodos04() throws IOException {
		assertEquals(genericTest("error_metodos04.ctds"),false);	
	}
        
        @Test
	public void error_metodos05() throws IOException {
		assertEquals(genericTest("error_metodos05.ctds"),false);	
	}
        
        @Test
	public void error_metodos06() throws IOException {
		assertEquals(genericTest("error_metodos06.ctds"),false);	
	}
        
        @Test
	public void error_metodos07() throws IOException {
		assertEquals(genericTest("error_metodos07.ctds"),false);	
	}
        
        @Test
	public void error_metodos08() throws IOException {
		assertEquals(genericTest("error_metodos08.ctds"),false);	
	}
        
        @Test
	public void error_multiplesMain() throws IOException {
		assertEquals(genericTest("error_multiplesMain.ctds"),false);	
	}
        
        @Test
	public void error_vbles01() throws IOException {
		assertEquals(genericTest("error_vbles01.ctds"),false);	
	}
        
        @Test
	public void error_vbles02() throws IOException {
		assertEquals(genericTest("error_vbles02.ctds"),false);	
	}
        
        @Test
	public void error_vbles03() throws IOException {
		assertEquals(genericTest("error_vbles03.ctds"),false);	
	}
        
        @Test
	public void errores() throws IOException {
		assertEquals(genericTest("errores.ctds"),false);	
	}



	/* Scan a file and returns true if the scanned file and the expected file are equals */
	private boolean genericTest(String inputFile) throws IOException {
            inputFile = "../src/test/resource/semantic/" + inputFile;
            CtdsParser pr = new CtdsParser(new CtdsLexer(new FileReader(inputFile)));
            
            /* Initialize the visitors */
            CheckMainVisitor mainVisitor = new CheckMainVisitor();
            CheckSemVisitor semVisitor = new CheckSemVisitor();
            CheckCycleSentencesVisitor cycleVisitor = new CheckCycleSentencesVisitor();
            
          
            /*Initialize the list of errors */
            List<lib.error.Error> errors = new LinkedList();
            
            /*Parse*/
            Program program;
            try {
                program = (Program) pr.parse().value;
                
                /*Visit the ast*/
                mainVisitor.visit(program);
                cycleVisitor.visit(program);
                semVisitor.visit(program);
            
                /*append errors*/
                errors.addAll(mainVisitor.getErrors());
                errors.addAll(cycleVisitor.getErrors());
                errors.addAll(semVisitor.getErrors());
                
                if (errors.isEmpty()){
                    return true;
                }
                else{
                    for(lib.error.Error e : errors){
                        System.out.println(e);
                    }
                    return false;
                }
                
                
            } catch (Exception ex) {
                //Logger.getLogger(CtdsSemanticTest.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
	}
}