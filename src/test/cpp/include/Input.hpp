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
#pragma once

#include <string>
using std::string;

#include <utility>
using std::pair;

#include <vector>
using std::vector;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <armadillo>
using arma::Mat;

namespace armadilloJava {
  class Input {
    public:
      static vector<vector<pair<string, void*>>> getTestParameters(const vector<InputClass>& testClasses);

    protected:
      static vector<vector<pair<string, void*>>> cartesianProduct(const vector<vector<pair<string, void*>>>& inputs);
      static vector<pair<string, void*>> vectorUnion(const vector<vector<pair<string, void*>>>& inputs);

      static Mat<double> getHilbertMatrix(int n_rows, int n_cols);
      static Mat<double> getKMSMatrix(int n_rows, int n_cols);

      static vector<pair<string, void*>> getElemInd();
      static vector<pair<string, void*>> getColInd();
      static vector<pair<string, void*>> getExtColInd();
      static vector<pair<string, void*>> getRowInd();
      static vector<pair<string, void*>> getExtRowInd();
      static vector<pair<string, void*>> getNumElems();
      static vector<pair<string, void*>> getNumCols();
      static vector<pair<string, void*>> getNumRows();
      static vector<pair<string, void*>> getNormal();
      static vector<pair<string, void*>> getDim();
      static vector<pair<string, void*>> getExp();
      static vector<pair<string, void*>> getMatNormInt();
      static vector<pair<string, void*>> getVecNormInt();
      static vector<pair<string, void*>> getGenDouble();
      static vector<pair<string, void*>> getTriDouble();
      static vector<pair<string, void*>> getSinValTol();
      static vector<pair<string, void*>> getElemIndRange();
      static vector<pair<string, void*>> getColIndRange();
      static vector<pair<string, void*>> getRowIndRange();
      static vector<pair<string, void*>> getMatSize();
      static vector<pair<string, void*>> getColVecSize();
      static vector<pair<string, void*>> getRowVecSize();
      static vector<pair<string, void*>> getGenMatVec();
      static vector<pair<string, void*>> getLogicMatVec();
      static vector<pair<string, void*>> getOOMatVec();
      static vector<pair<string, void*>> getGenMat();
      static vector<pair<string, void*>> getSquMat();
      static vector<pair<string, void*>> getInvMat();
      static vector<pair<string, void*>> getSymMat();
      static vector<pair<string, void*>> getSymPDMat();
      static vector<pair<string, void*>> getLogicMat();
      static vector<pair<string, void*>> getOOMat();
      static vector<pair<string, void*>> getGenVec();
      static vector<pair<string, void*>> getMonVec();
      static vector<pair<string, void*>> getLogicVec();
      static vector<pair<string, void*>> getOOVec();
      static vector<pair<string, void*>> getGenColVec();
      static vector<pair<string, void*>> getMonColVec();
      static vector<pair<string, void*>> getLogicColVec();
      static vector<pair<string, void*>> getOOColVec();
      static vector<pair<string, void*>> getGenRowVec();
      static vector<pair<string, void*>> getMonRowVec();
      static vector<pair<string, void*>> getLogicRowVec();
      static vector<pair<string, void*>> getOORowVec();
      static vector<pair<string, void*>> getElemInds();
      static vector<pair<string, void*>> getColInds();
      static vector<pair<string, void*>> getRowInds();
      static vector<pair<string, void*>> getMatNormString();
      static vector<pair<string, void*>> getVecNormString();
      static vector<pair<string, void*>> getSort();
      static vector<pair<string, void*>> getSinValSel();
      static vector<pair<string, void*>> getDistrParam();
      static vector<pair<string, void*>> getFill();
  };
}
