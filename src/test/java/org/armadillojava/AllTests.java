/*******************************************************************************
 * Copyright 2013-2014 Sebastian Niemann <niemann@sra.uni-hannover.de>.
 * 
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://opensource.org/licenses/MIT
 * 
 * Developers:
 * Sebastian Niemann - Lead developer
 * Daniel Kiechle - Unit testing
 ******************************************************************************/
package org.armadillojava;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author Sebastian Niemann <niemann@sra.uni-hannovr.de>
 */
@RunWith(Suite.class)
@SuiteClasses({
  TestDatum.class,
  TestGenColVec.class,
  TestGenColVecGenColVec.class,
  TestGenColVecGenColVecNormal.class,
  TestGenColVecGenMat.class,
  TestGenColVecGenRowVec.class,
  TestGenColVecGenRowVecNormal.class,
  TestGenColVecMonColVec.class,
  TestGenColVecMonRowVec.class,
  TestGenColVecNormal.class,
  TestGenColVecNumElems.class,
  TestGenColVecNumRowsNumCols.class,
  TestGenColVecSort.class,
  TestGenColVecVecNormInt.class,
  TestGenColVecVecNormString.class,
  TestGenDouble.class,
  TestGenDoubleGenDouble.class,
  TestGenDoubleGenDoubleNumElems.class,
  TestGenMat.class,
  TestGenMatColInd.class,
  TestGenMatDim.class,
  TestGenMatExp.class,
  TestGenMatGenColVec.class,
  TestGenMatGenMat.class,
  TestGenMatGenMatNormal.class,
  TestGenMatGenRowVec.class,
  TestGenMatMatNormInt.class,
  TestGenMatMatNormString.class,
  TestGenMatMonColVec.class,
  TestGenMatMonColVecDim.class,
  TestGenMatMonRowVec.class,
  TestGenMatMonRowVecDim.class,
  TestGenMatNormal.class,
  TestGenMatNormalDim.class,
  TestGenMatNumElems.class,
  TestGenMatNumElemsDim.class,
  TestGenMatNumRowsNumCols.class,
  TestGenMatRowInd.class,
  TestGenMatSinValSel.class,
  TestGenMatSinValTol.class,
  TestGenMatSort.class,
  TestGenMatSortDim.class,
  TestGenRowVec.class,
  TestGenRowVecGenColVec.class,
  TestGenRowVecGenColVecNormal.class,
  TestGenRowVecGenMat.class,
  TestGenRowVecGenRowVec.class,
  TestGenRowVecGenRowVecNormal.class,
  TestGenRowVecMonColVec.class,
  TestGenRowVecMonRowVec.class,
  TestGenRowVecNormal.class,
  TestGenRowVecNumElems.class,
  TestGenRowVecNumRowsNumCols.class,
  TestGenRowVecSort.class,
  TestGenRowVecVecNormInt.class,
  TestGenRowVecVecNormString.class,
  TestInvMat.class,
  TestInvMatGenMat.class,
  TestLogicColVec.class,
  TestLogicColVecNumElems.class,
  TestLogicMat.class,
  TestLogicMatDim.class,
  TestLogicMatNumElems.class,
  TestLogicMatNumElemsSearch.class,
  TestLogicRowVec.class,
  TestLogicRowVecNumElems.class,
  TestNumElems.class,
  TestNumRowsNumCols.class,
  TestOOColVec.class,
  TestOOMat.class,
  TestOORowVec.class,
  TestSquMat.class,
  TestSquMatSquMatGenMat.class,
  TestSymMat.class,
  TestSymPDMat.class,
  TestWallClock.class
})
public class AllTests {}
