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
 * @author Sebastian Niemann <niemann@sra.uni-hannovr.de>
 */
@RunWith(Suite.class)
@SuiteClasses({
  TestArmaAs_scalar.class,
  TestArmaDecomposition.class,
  TestArmaDotAndNorm_dot.class,
  TestArmaMatrixGenerationLinspace.class,
  TestArmaMatrixGenerationMatrix.class,
  TestArmaMatrixGenerationToeplitz.class,
  TestArmaMatrixGenerationVector.class,
  TestArmaMatrixValuedElementWiseFunctionsMiscellaneous.class,
  TestArmaMatrixValuedElementWiseFunctionsTrigonometric.class,
  TestArmaMatrixValuedFunctionsOfVectorsMatricesHistogramm.class,
  TestArmaMatrixValuedFunctionsOfVectorsMatricesMatrixJoins.class,
  TestArmaMatrixValuedFunctionsOfVectorsMatricesMiscellaneous.class,
  TestArmaMatrixValuedFunctionsOfVectorsMatricesMiscellaneousFind.class,
  TestArmaMatrixValuedFunctionsOfVectorsMatricesMiscellaneousParameterised.class,
  TestArmaMatrixValuedFunctionsOfVectorsMatricesMiscellaneousShuffle.class,
  TestArmaMatrixValuedFunctionsOfVectorsMatricesReinterpret.class,
  TestArmaMatrixValuedFunctionsOfVectorsMatricesReshapeResize.class,
  TestArmaMatrixValuedFunctionsOfVectorsMatricesSort.class,
  TestArmaMatrixValuedFunctionsOfVectorsMatricesStatistic.class,
  TestArmaMatrixValuedFunctionsOfVectorsMatricesSwap.class,
  TestArmaMiscellaneous.class,
  TestArmaScalarValuedFunctionsOfVectorsMatrices.class,
  TestArmaScalarVectorValuedFunctionsOfVectorsMatricesMiscellaneous.class,
  TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic.class,
  TestDatum.class,
  TestWallClock.class
})
public class AllTests {

}
