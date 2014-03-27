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

#include <ExpectedGenDouble.cpp>
using armadilloJava::ExpectedGenDouble;

#include <ExpectedGenMat.cpp>
using armadilloJava::ExpectedGenMat;

#include <ExpectedGenMatDim.cpp>
using armadilloJava::ExpectedGenMatDim;

#include <ExpectedGenMatExp.cpp>
using armadilloJava::ExpectedGenMatExp;

#include <ExpectedGenMatNormal.cpp>
using armadilloJava::ExpectedGenMatNormal;

#include <ExpectedNumElems.cpp>
using armadilloJava::ExpectedNumElems;

#include <ExpectedNumRowsNumCols.cpp>
using armadilloJava::ExpectedNumRowsNumCols;

#include <ExpectedGenRowVec.cpp>
using armadilloJava::ExpectedGenRowVec;

#include <ExpectedOOMat.cpp>
using armadilloJava::ExpectedOOMat;

int main() {
  ofstream logfile("./expected.log");
  cout.rdbuf(logfile.rdbuf());
  set_stream_err1(logfile);
  set_stream_err2(logfile);

  ExpectedDatum();
  ExpectedGenColVec();
  ExpectedGenDouble();
  ExpectedGenMat();
  ExpectedGenMatDim();
  ExpectedGenMatExp();
  ExpectedGenMatNormal();
  ExpectedGenRowVec();
  ExpectedNumElems();
  ExpectedNumRowsNumCols();
  ExpectedOOMat();

  return EXIT_SUCCESS;
}
