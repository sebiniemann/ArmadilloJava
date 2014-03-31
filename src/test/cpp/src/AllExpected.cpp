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

#include <ExpectedGenColVecGenMat.cpp>
using armadilloJava::ExpectedGenColVecGenMat;

#include <ExpectedGenColVecGenRowVec.cpp>
using armadilloJava::ExpectedGenColVecGenRowVec;

#include <ExpectedGenColVecMonColVec.cpp>
using armadilloJava::ExpectedGenColVecMonColVec;

#include <ExpectedGenColVecMonRowVec.cpp>
using armadilloJava::ExpectedGenColVecMonRowVec;

#include <ExpectedGenColVecSort.cpp>
using armadilloJava::ExpectedGenColVecSort;

#include <ExpectedGenDouble.cpp>
using armadilloJava::ExpectedGenDouble;

#include <ExpectedGenDoubleGenDouble.cpp>
using armadilloJava::ExpectedGenDoubleGenDouble;

#include <ExpectedGenDoubleGenDoubleNumElems.cpp>
using armadilloJava::ExpectedGenDoubleGenDoubleNumElems;

#include <ExpectedGenMat.cpp>
using armadilloJava::ExpectedGenMat;

#include <ExpectedGenMatDim.cpp>
using armadilloJava::ExpectedGenMatDim;

#include <ExpectedGenMatExp.cpp>
using armadilloJava::ExpectedGenMatExp;

#include <ExpectedGenMatGenColVec.cpp>
using armadilloJava::ExpectedGenMatGenColVec;

#include <ExpectedGenMatGenMat.cpp>
using armadilloJava::ExpectedGenMatGenMat;

#include <ExpectedGenMatGenRowVec.cpp>
using armadilloJava::ExpectedGenMatGenRowVec;

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

#include <ExpectedGenMatSort.cpp>
using armadilloJava::ExpectedGenMatSort;

#include <ExpectedGenMatSortDim.cpp>
using armadilloJava::ExpectedGenMatSortDim;

#include <ExpectedGenRowVec.cpp>
using armadilloJava::ExpectedGenRowVec;

#include <ExpectedGenRowVecGenColVec.cpp>
using armadilloJava::ExpectedGenRowVecGenColVec;

#include <ExpectedGenRowVecGenMat.cpp>
using armadilloJava::ExpectedGenRowVecGenMat;

#include <ExpectedGenRowVecGenRowVec.cpp>
using armadilloJava::ExpectedGenRowVecGenRowVec;

#include <ExpectedGenRowVecMonColVec.cpp>
using armadilloJava::ExpectedGenRowVecMonColVec;

#include <ExpectedGenRowVecMonRowVec.cpp>
using armadilloJava::ExpectedGenRowVecMonRowVec;

#include <ExpectedGenRowVecSort.cpp>
using armadilloJava::ExpectedGenRowVecSort;

#include <ExpectedLogicColVec.cpp>
using armadilloJava::ExpectedLogicColVec;

#include <ExpectedLogicMat.cpp>
using armadilloJava::ExpectedLogicMat;

#include <ExpectedLogicMatDim.cpp>
using armadilloJava::ExpectedLogicMatDim;

#include <ExpectedLogicRowVec.cpp>
using armadilloJava::ExpectedLogicRowVec;

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

#include <ExpectedSquMat.cpp>
using armadilloJava::ExpectedSquMat;

int main() {
  ofstream logfile("./expected.log");
  cout.rdbuf(logfile.rdbuf());
  set_stream_err1(logfile);
  set_stream_err2(logfile);

  ExpectedDatum();
  ExpectedGenColVec();
  ExpectedGenColVecGenColVec();
  ExpectedGenColVecGenMat();
  ExpectedGenColVecGenRowVec();
  ExpectedGenColVecMonColVec();
  ExpectedGenColVecMonRowVec();
  ExpectedGenColVecSort();
  ExpectedGenDouble();
  ExpectedGenDoubleGenDouble();
  ExpectedGenDoubleGenDoubleNumElems();
  ExpectedGenMat();
  ExpectedGenMatDim();
  ExpectedGenMatExp();
  ExpectedGenMatGenColVec();
  ExpectedGenMatGenMat();
  ExpectedGenMatGenRowVec();
  ExpectedGenMatMonColVec();
  ExpectedGenMatMonColVecDim();
  ExpectedGenMatMonRowVec();
  ExpectedGenMatMonRowVecDim();
  ExpectedGenMatNormal();
  ExpectedGenMatSort();
  ExpectedGenMatSortDim();
  ExpectedGenRowVec();
  ExpectedGenRowVecGenColVec();
  ExpectedGenRowVecGenMat();
  ExpectedGenRowVecGenRowVec();
  ExpectedGenRowVecMonColVec();
  ExpectedGenRowVecMonRowVec();
  ExpectedGenRowVecSort();
  ExpectedLogicColVec();
  ExpectedLogicMat();
  ExpectedLogicMatDim();
  ExpectedLogicRowVec();
  ExpectedNumElems();
  ExpectedNumRowsNumCols();
  ExpectedOOColVec();
  ExpectedOOMat();
  ExpectedOORowVec();
  ExpectedSquMat();

  return EXIT_SUCCESS;
}
