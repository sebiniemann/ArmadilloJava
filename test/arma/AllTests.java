package arma;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Executes all test cases.
 * 
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 */
@RunWith(Suite.class)
@SuiteClasses({AllTestsArma.class, AllTestsMat.class})
public class AllTests {

}
