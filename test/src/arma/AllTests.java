/*******************************************************************************
 * Copyright 2013 Sebastian Niemann <niemann@sra.uni-hannover.de> and contributors.
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package arma;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author niemann
 * 
 */
@RunWith(Suite.class)
@SuiteClasses({TestArmaAs_scalar.class, TestArmaDecomposition.class, TestArmaDotAndNorm_dot.class, TestArmaMatrixValuedElementWiseFunctionsMiscellaneous.class, TestArmaMatrixValuedElementWiseFunctionsTrigonometric.class, TestArmaMiscellaneous.class, TestArmaScalarValuedFunctionsOfVectorsMatrices.class, TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic.class})
public class AllTests {

}
