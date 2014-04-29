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
 *   Sebastian Niemann - Lead developer
 *   Daniel Kiechle - Unit testing
 ******************************************************************************/
#include <iostream>
// EXIT_SUCCESS
using std::cout;
using std::endl;

#include <fstream>
using std::ofstream;

#include <armadillo>
using arma::set_stream_err1;
using arma::set_stream_err2;
using arma::mean;
using arma::svd;
using arma::Mat;
using arma::zeros;

#include <ExpectedDatum.cpp>
using armadilloJava::ExpectedDatum;

#include <ExpectedGenColVec.cpp>
using armadilloJava::ExpectedGenColVec;

#include <ExpectedGenColVecGenColVec.cpp>
using armadilloJava::ExpectedGenColVecGenColVec;

#include <ExpectedGenColVecGenColVecNormal.cpp>
using armadilloJava::ExpectedGenColVecGenColVecNormal;

#include <ExpectedGenColVecGenMat.cpp>
using armadilloJava::ExpectedGenColVecGenMat;

#include <ExpectedGenColVecGenRowVec.cpp>
using armadilloJava::ExpectedGenColVecGenRowVec;

#include <ExpectedGenColVecGenRowVecNormal.cpp>
using armadilloJava::ExpectedGenColVecGenRowVecNormal;

#include <ExpectedGenColVecMonColVec.cpp>
using armadilloJava::ExpectedGenColVecMonColVec;

#include <ExpectedGenColVecMonRowVec.cpp>
using armadilloJava::ExpectedGenColVecMonRowVec;

#include <ExpectedGenColVecNormal.cpp>
using armadilloJava::ExpectedGenColVecNormal;

#include <ExpectedGenColVecNumElems.cpp>
using armadilloJava::ExpectedGenColVecNumElems;

#include <ExpectedGenColVecNumRowsNumCols.cpp>
using armadilloJava::ExpectedGenColVecNumRowsNumCols;

#include <ExpectedGenColVecSort.cpp>
using armadilloJava::ExpectedGenColVecSort;

#include <ExpectedGenColVecVecNormInt.cpp>
using armadilloJava::ExpectedGenColVecVecNormInt;

#include <ExpectedGenColVecVecNormString.cpp>
using armadilloJava::ExpectedGenColVecVecNormString;

#include <ExpectedGenDouble.cpp>
using armadilloJava::ExpectedGenDouble;

#include <ExpectedGenDoubleGenDouble.cpp>
using armadilloJava::ExpectedGenDoubleGenDouble;

#include <ExpectedGenDoubleGenDoubleNumElems.cpp>
using armadilloJava::ExpectedGenDoubleGenDoubleNumElems;

#include <ExpectedGenMat.cpp>
using armadilloJava::ExpectedGenMat;

#include <ExpectedGenMatColInd.cpp>
using armadilloJava::ExpectedGenMatColInd;

#include <ExpectedGenMatColIndRange.cpp>
using armadilloJava::ExpectedGenMatColIndRange;

#include <ExpectedGenMatDim.cpp>
using armadilloJava::ExpectedGenMatDim;

#include <ExpectedGenMatElemInd.cpp>
using armadilloJava::ExpectedGenMatElemInd;

#include <ExpectedGenMatElemIndRange.cpp>
using armadilloJava::ExpectedGenMatElemIndRange;

#include <ExpectedGenMatExp.cpp>
using armadilloJava::ExpectedGenMatExp;

#include <ExpectedGenMatGenColVec.cpp>
using armadilloJava::ExpectedGenMatGenColVec;

#include <ExpectedGenMatGenDouble.cpp>
using armadilloJava::ExpectedGenMatGenDouble;

#include <ExpectedGenMatGenMat.cpp>
using armadilloJava::ExpectedGenMatGenMat;

#include <ExpectedGenMatGenMatNormal.cpp>
using armadilloJava::ExpectedGenMatGenMatNormal;

#include <ExpectedGenMatGenRowVec.cpp>
using armadilloJava::ExpectedGenMatGenRowVec;

#include <ExpectedGenMatMatNormInt.cpp>
using armadilloJava::ExpectedGenMatMatNormInt;

#include <ExpectedGenMatMatNormString.cpp>
using armadilloJava::ExpectedGenMatMatNormString;

#include <ExpectedGenMatMonColVec.cpp>
using armadilloJava::ExpectedGenMatMonColVec;

#include <ExpectedGenMatMonColVecDim.cpp>
using armadilloJava::ExpectedGenMatMonColVecDim;

#include <ExpectedGenMatMonRowVec.cpp>
using armadilloJava::ExpectedGenMatMonRowVec;

#include <ExpectedGenMatMonRowVecDim.cpp>
using armadilloJava::ExpectedGenMatMonRowVecDim;

#include <ExpectedGenMatNormal.cpp>
using armadilloJava::ExpectedGenMatNormal;

#include <ExpectedGenMatNormalDim.cpp>
using armadilloJava::ExpectedGenMatNormalDim;

#include <ExpectedGenMatNumElems.cpp>
using armadilloJava::ExpectedGenMatNumElems;

#include <ExpectedGenMatNumElemsDim.cpp>
using armadilloJava::ExpectedGenMatNumElemsDim;

#include <ExpectedGenMatNumRowsNumCols.cpp>
using armadilloJava::ExpectedGenMatNumRowsNumCols;

#include <ExpectedGenMatRowInd.cpp>
using armadilloJava::ExpectedGenMatRowInd;

#include <ExpectedGenMatRowIndColInd.cpp>
using armadilloJava::ExpectedGenMatRowIndColInd;

#include <ExpectedGenMatRowIndRange.cpp>
using armadilloJava::ExpectedGenMatRowIndRange;

#include <ExpectedGenMatSinValSel.cpp>
using armadilloJava::ExpectedGenMatSinValSel;

#include <ExpectedGenMatSinValTol.cpp>
using armadilloJava::ExpectedGenMatSinValTol;

#include <ExpectedGenMatSort.cpp>
using armadilloJava::ExpectedGenMatSort;

#include <ExpectedGenMatSortDim.cpp>
using armadilloJava::ExpectedGenMatSortDim;

#include <ExpectedGenRowVec.cpp>
using armadilloJava::ExpectedGenRowVec;

#include <ExpectedGenRowVecGenColVec.cpp>
using armadilloJava::ExpectedGenRowVecGenColVec;

#include <ExpectedGenRowVecGenColVecNormal.cpp>
using armadilloJava::ExpectedGenRowVecGenColVecNormal;

#include <ExpectedGenRowVecGenMat.cpp>
using armadilloJava::ExpectedGenRowVecGenMat;

#include <ExpectedGenRowVecGenRowVec.cpp>
using armadilloJava::ExpectedGenRowVecGenRowVec;

#include <ExpectedGenRowVecGenRowVecNormal.cpp>
using armadilloJava::ExpectedGenRowVecGenRowVecNormal;

#include <ExpectedGenRowVecMonColVec.cpp>
using armadilloJava::ExpectedGenRowVecMonColVec;

#include <ExpectedGenRowVecMonRowVec.cpp>
using armadilloJava::ExpectedGenRowVecMonRowVec;

#include <ExpectedGenRowVecNormal.cpp>
using armadilloJava::ExpectedGenRowVecNormal;

#include <ExpectedGenRowVecNumElems.cpp>
using armadilloJava::ExpectedGenRowVecNumElems;

#include <ExpectedGenRowVecNumRowsNumCols.cpp>
using armadilloJava::ExpectedGenRowVecNumRowsNumCols;

#include <ExpectedGenRowVecSort.cpp>
using armadilloJava::ExpectedGenRowVecSort;

#include <ExpectedGenRowVecVecNormInt.cpp>
using armadilloJava::ExpectedGenRowVecVecNormInt;

#include <ExpectedGenRowVecVecNormString.cpp>
using armadilloJava::ExpectedGenRowVecVecNormString;

#include <ExpectedInPlaceGenMat.cpp>
using armadilloJava::ExpectedInPlaceGenMat;

#include <ExpectedInPlaceGenMatColIndGenDouble.cpp>
using armadilloJava::ExpectedInPlaceGenMatColIndGenDouble;

#include <ExpectedInPlaceGenMatColIndGenMat.cpp>
using armadilloJava::ExpectedInPlaceGenMatColIndGenMat;

#include <ExpectedInPlaceGenMatColIndRangeGenDouble.cpp>
using armadilloJava::ExpectedInPlaceGenMatColIndRangeGenDouble;

#include <ExpectedInPlaceGenMatColIndRangeGenMat.cpp>
using armadilloJava::ExpectedInPlaceGenMatColIndRangeGenMat;

#include <ExpectedInPlaceGenMatElemInd.cpp>
using armadilloJava::ExpectedInPlaceGenMatElemInd;

#include <ExpectedInPlaceGenMatElemIndGenDouble.cpp>
using armadilloJava::ExpectedInPlaceGenMatElemIndGenDouble;

#include <ExpectedInPlaceGenMatGenDouble.cpp>
using armadilloJava::ExpectedInPlaceGenMatGenDouble;

#include <ExpectedInPlaceGenMatGenMat.cpp>
using armadilloJava::ExpectedInPlaceGenMatGenMat;

#include <ExpectedInPlaceGenMatRowIndGenDouble.cpp>
using armadilloJava::ExpectedInPlaceGenMatRowIndGenDouble;

#include <ExpectedInPlaceGenMatRowIndGenMat.cpp>
using armadilloJava::ExpectedInPlaceGenMatRowIndGenMat;

#include <ExpectedInPlaceGenMatRowIndRangeGenDouble.cpp>
using armadilloJava::ExpectedInPlaceGenMatRowIndRangeGenDouble;

#include <ExpectedInPlaceGenMatRowIndRangeGenMat.cpp>
using armadilloJava::ExpectedInPlaceGenMatRowIndRangeGenMat;

#include <ExpectedInPlaceRandomGenMat.cpp>
using armadilloJava::ExpectedInPlaceRandomGenMat;

#include <ExpectedInvMat.cpp>
using armadilloJava::ExpectedInvMat;

#include <ExpectedLogicColVec.cpp>
using armadilloJava::ExpectedLogicColVec;

#include <ExpectedLogicColVecNumElems.cpp>
using armadilloJava::ExpectedLogicColVecNumElems;

#include <ExpectedLogicMat.cpp>
using armadilloJava::ExpectedLogicMat;

#include <ExpectedLogicMatDim.cpp>
using armadilloJava::ExpectedLogicMatDim;

#include <ExpectedLogicMatNumElems.cpp>
using armadilloJava::ExpectedLogicMatNumElems;

#include <ExpectedLogicMatNumElemsSearch.cpp>
using armadilloJava::ExpectedLogicMatNumElemsSearch;

#include <ExpectedLogicRowVec.cpp>
using armadilloJava::ExpectedLogicRowVec;

#include <ExpectedLogicRowVecNumElems.cpp>
using armadilloJava::ExpectedLogicRowVecNumElems;

#include <ExpectedNumElems.cpp>
using armadilloJava::ExpectedNumElems;

#include <ExpectedNumRowsNumCols.cpp>
using armadilloJava::ExpectedNumRowsNumCols;

#include <ExpectedOOColVec.cpp>
using armadilloJava::ExpectedOOColVec;

#include <ExpectedOOMat.cpp>
using armadilloJava::ExpectedOOMat;

#include <ExpectedOORowVec.cpp>
using armadilloJava::ExpectedOORowVec;

#include <ExpectedRandomGenColVec.cpp>
using armadilloJava::ExpectedRandomGenColVec;

#include <ExpectedRandomGenMat.cpp>
using armadilloJava::ExpectedRandomGenMat;

#include <ExpectedRandomGenMatDim.cpp>
using armadilloJava::ExpectedRandomGenMatDim;

#include <ExpectedRandomGenRowVec.cpp>
using armadilloJava::ExpectedRandomGenRowVec;

#include <ExpectedRandomNumElems.cpp>
using armadilloJava::ExpectedRandomNumElems;

#include <ExpectedRandomNumElemsDistrParam.cpp>
using armadilloJava::ExpectedRandomNumElemsDistrParam;

#include <ExpectedRandomNumRowsNumCols.cpp>
using armadilloJava::ExpectedRandomNumRowsNumCols;

#include <ExpectedRandomNumRowsNumColsDistrParam.cpp>
using armadilloJava::ExpectedRandomNumRowsNumColsDistrParam;

#include <ExpectedSquMat.cpp>
using armadilloJava::ExpectedSquMat;

#include <ExpectedSquMatSquMatGenMat.cpp>
using armadilloJava::ExpectedSquMatSquMatGenMat;

#include <ExpectedSymMat.cpp>
using armadilloJava::ExpectedSymMat;

#include <ExpectedSymPDMat.cpp>
using armadilloJava::ExpectedSymPDMat;

int main() {
  ofstream logfile("./expected.log");
  cout.rdbuf(logfile.rdbuf());
  set_stream_err1(logfile);
  set_stream_err2(logfile);

//  ExpectedDatum();
//  ExpectedGenColVec();
//  ExpectedGenColVecGenColVec();
//  ExpectedGenColVecGenColVecNormal();
//  ExpectedGenColVecGenMat();
//  ExpectedGenColVecGenRowVec();
//  ExpectedGenColVecGenRowVecNormal();
//  ExpectedGenColVecMonColVec();
//  ExpectedGenColVecMonRowVec();
//  ExpectedGenColVecNormal();
//  ExpectedGenColVecNumElems();
//  ExpectedGenColVecNumRowsNumCols();
//  ExpectedGenColVecSort();
//  ExpectedGenColVecVecNormInt();
//  ExpectedGenColVecVecNormString();
//  ExpectedGenDouble();
//  ExpectedGenDoubleGenDouble();
//  ExpectedGenDoubleGenDoubleNumElems();
//  ExpectedGenMat();
//  ExpectedGenMatColInd();
//  ExpectedGenMatColIndRange();
//  ExpectedGenMatDim();
//  ExpectedGenMatElemInd();
//  ExpectedGenMatElemIndRange();
//  ExpectedGenMatExp();
//  ExpectedGenMatGenColVec();
//  ExpectedGenMatGenDouble();
//  ExpectedGenMatGenMat();
//  ExpectedGenMatGenMatNormal();
//  ExpectedGenMatGenRowVec();
//  ExpectedGenMatMatNormInt();
//  ExpectedGenMatMatNormString();
//  ExpectedGenMatMonColVec();
//  ExpectedGenMatMonColVecDim();
//  ExpectedGenMatMonRowVec();
//  ExpectedGenMatMonRowVecDim();
//  ExpectedGenMatNormal();
//  ExpectedGenMatNormalDim();
//  ExpectedGenMatNumElems();
//  ExpectedGenMatNumElemsDim();
//  ExpectedGenMatNumRowsNumCols();
//  ExpectedGenMatRowInd();
//  ExpectedGenMatRowIndColInd();
//  ExpectedGenMatRowIndRange();
//  ExpectedGenMatSinValSel();
//  ExpectedGenMatSinValTol();
//  ExpectedGenMatSort();
//  ExpectedGenMatSortDim();
//  ExpectedGenRowVec();
//  ExpectedGenRowVecGenColVec();
//  ExpectedGenRowVecGenColVecNormal();
//  ExpectedGenRowVecGenMat();
//  ExpectedGenRowVecGenRowVec();
//  ExpectedGenRowVecGenRowVecNormal();
//  ExpectedGenRowVecMonColVec();
//  ExpectedGenRowVecMonRowVec();
//  ExpectedGenRowVecNormal();
//  ExpectedGenRowVecNumElems();
//  ExpectedGenRowVecNumRowsNumCols();
//  ExpectedGenRowVecSort();
//  ExpectedGenRowVecVecNormInt();
//  ExpectedGenRowVecVecNormString();
//  ExpectedInPlaceGenMat();
//  ExpectedInPlaceGenMatColIndGenDouble();
//  ExpectedInPlaceGenMatColIndGenMat();
//  ExpectedInPlaceGenMatColIndRangeGenDouble();
//  ExpectedInPlaceGenMatColIndRangeGenMat();
//  ExpectedInPlaceGenMatElemInd();
//  ExpectedInPlaceGenMatElemIndGenDouble();
//  ExpectedInPlaceGenMatGenDouble();
//  ExpectedInPlaceGenMatGenMat();
//  ExpectedInPlaceGenMatRowIndGenDouble();
//  ExpectedInPlaceGenMatRowIndGenMat();
//  ExpectedInPlaceGenMatRowIndRangeGenDouble();
  ExpectedInPlaceGenMatRowIndRangeGenMat();
//  ExpectedInPlaceRandomGenMat();
//  ExpectedInvMat();
//  ExpectedLogicColVec();
//  ExpectedLogicColVecNumElems();
//  ExpectedLogicMat();
//  ExpectedLogicMatDim();
//  ExpectedLogicMatNumElems();
//  ExpectedLogicMatNumElemsSearch();
//  ExpectedLogicRowVec();
//  ExpectedLogicRowVecNumElems();
//  ExpectedNumElems();
//  ExpectedNumRowsNumCols();
//  ExpectedOOColVec();
//  ExpectedOOMat();
//  ExpectedOORowVec();
//  ExpectedRandomGenColVec();
//  ExpectedRandomGenMat();
//  ExpectedRandomGenMatDim();
//  ExpectedRandomGenRowVec();
//  ExpectedRandomNumElems();
//  ExpectedRandomNumElemsDistrParam();
//  ExpectedRandomNumRowsNumCols();
//  ExpectedRandomNumRowsNumColsDistrParam();
//  ExpectedSquMat();
//  ExpectedSquMatSquMatGenMat();
//  ExpectedSymMat();
//  ExpectedSymPDMat();

  return EXIT_SUCCESS;
}
