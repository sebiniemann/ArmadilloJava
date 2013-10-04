package arma;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Executes all test cases for {@link Mat}.
 * 
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 */
@RunWith(Suite.class)
@SuiteClasses({TestMatElementWise.class, TestMatIndividual.class, TestMatMatrix.class})
public class AllTestsMat {

}
