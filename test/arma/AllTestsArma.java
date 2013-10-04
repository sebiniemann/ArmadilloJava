package arma;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Executes all test cases for {@link Arma}.
 * 
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 */
@RunWith(Suite.class)
@SuiteClasses({TestArmaElementWise.class, TestArmaIndividual.class, TestArmaMatrix.class})
public class AllTestsArma {

}
