package arma;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author niemann
 *
 */
@RunWith(Suite.class)
@SuiteClasses({TestArmaAs_scalar.class, TestArmaDecomposition.class, TestArmaDotAndNorm_dot.class, TestArmaMatrixValuedElementWiseFunctionsMiscellaneous.class, TestArmaMatrixValuedElementWiseFunctionsTrigonometric.class, TestArmaScalarValuedFunctionsOfVectorsMatrices.class, TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic.class})
public class AllTests {

}
