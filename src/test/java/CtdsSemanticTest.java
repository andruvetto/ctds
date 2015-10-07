import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        System.out.println("----------------------------------- Testing CTdsScanner ---------------------------------");;
    	System.out.println();
    }

	@Rule
	public TestRule watcher = new TestWatcher() {
   		protected void starting(Description description) {
    		System.out.println("Starting test: " + description.getMethodName());
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
	public void test_float03() throws IOException {
		assertEquals(genericTest("test_float03.ctds"),false);	
	}
        
        @Test
	public void test_float04() throws IOException {
		assertEquals(genericTest("test_float04.ctds"),true);	
	}
        
        @Test
	public void test_for01() throws IOException {
		assertEquals(genericTest("test_for01.ctds"),false);	
	}
        
        @Test
	public void test_for02() throws IOException {
		assertEquals(genericTest("test_for02.ctds"),true);	
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
                
                if (errors.isEmpty()) return true;
                else{
                    System.err.println(errors);
                    return false;
                }
                
                
            } catch (Exception ex) {
                Logger.getLogger(CtdsSemanticTest.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
	}
}