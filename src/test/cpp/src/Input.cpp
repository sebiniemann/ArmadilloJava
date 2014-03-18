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
// http://arma.sourceforge.net/docs.html#config_hpp
#define ARMA_USE_CXX11 // Use C++11 features, such as initialiser lists

#include <Input.hpp>
using armadilloJava::Input;

#include <algorithm>
using std::sort;
using std::set_union;

#include <string>
using std::to_string;

#include <stdexcept>
using std::runtime_error;

#include <armadillo>
using arma::Mat;
using arma::Col;
using arma::Row;
using arma::datum;
using arma::span;
using arma::SizeMat;
using arma::distr_param;

namespace armadilloJava {
  vector<vector<pair<string, void*>>> Input::getTestParameters(const vector<InputClass>& testClasses) {
    vector<vector<pair<string, void*>>> inputs;

    for (InputClass testClass : testClasses) {
      switch (testClass) {
        case InputClass::ElemInd:
          inputs.push_back(getElemInd());
          break;
        case InputClass::GenDouble:
          inputs.push_back(getGenDouble());
          break;
        case InputClass::OOMat:
          inputs.push_back(getOOMat());
          break;
        default:
          throw new runtime_error("Unsupported test class requested.");
      }
    }

    return cartesianProduct(inputs);
  }

  vector<vector<pair<string, void*>>> Input::cartesianProduct(const vector<vector<pair<string, void*>>>& inputs) {
    vector<vector<pair<string, void*>>> cartesianProduct = {{}};
    for (vector<pair<string, void*>> input : inputs) {
      vector<vector<pair<string, void*>>> tempProduct;
      for (vector<pair<string, void*>> tempInput : cartesianProduct) {
        for (pair<string, void*> value : input) {
          tempProduct.push_back(tempInput);
          tempProduct.back().push_back(value);
        }
      }
      cartesianProduct.swap(tempProduct);
    }
    return cartesianProduct;
  }

  vector<pair<string, void*>> Input::vectorUnion(const vector<vector<pair<string, void*>>>& inputs) {
    vector<pair<string, void*>> vectorUnion = inputs.at(0);
    sort(vectorUnion.begin(), vectorUnion.end(), [](const pair<string, void*>& lhs, const pair<string, void*>& rhs) {return lhs.first < rhs.first;});

    for (int n = 1; n < inputs.size(); n++) {
      vector<pair<string, void*>> input = inputs.at(n);
      sort(input.begin(), input.end(), [](const pair<string, void*>& lhs, const pair<string, void*>& rhs) {return lhs.first < rhs.first;});

      vector<pair<string, void*>> temp(vectorUnion.size() + input.size());
      vector<pair<string, void*>>::iterator end = set_union(vectorUnion.begin(), vectorUnion.end(), input.begin(), input.end(), temp.begin(), [](const pair<string, void*>& lhs, const pair<string, void*>& rhs) {return lhs.first < rhs.first;});
      temp.resize(end - temp.begin());

      vectorUnion = temp;
    }

    return vectorUnion;
  }

  Mat<double> Input::getHilbertMatrix(int n_rows, int n_cols) {
    Mat<double> hilbertMatrix(n_rows, n_cols);

    for (int j = 0; j < n_cols; j++) {
      for (int i = 0; i < n_rows; i++) {
        hilbertMatrix.at(i, j) = 1.0 / (i + j + 1);
      }
    }

    return hilbertMatrix;
  }

  Mat<double> Input::getKMSMatrix(int n_rows, int n_cols) {
    Mat<double> KMSMatrix(n_rows, n_cols);

    for (int j = 0; j < n_cols; j++) {
      for (int i = 0; i < n_rows; i++) {
        KMSMatrix.at(i, j) = pow(2, abs(i - j));
      }
    }

    return KMSMatrix;
  }

  vector<pair<string, void*>> Input::getElemInd() {
    vector<vector<pair<string, void*>>> inputs = {};
    inputs.push_back({
      pair<string, void*>("0", new int(0)),
      pair<string, void*>("1", new int(1))
    });

    for (pair<string, void*> value : getNumElems()) {
      int numElems = *static_cast<int*>(value.second);

      inputs.push_back({
        pair<string, void*>(to_string(numElems / 2), new int(numElems / 2)),
        pair<string, void*>(to_string(numElems - 1), new int(numElems - 1))
      });
    }

    return vectorUnion(inputs);
  }

  vector<pair<string, void*>> Input::getColInd() {
    return getRowInd();
  }

  vector<pair<string, void*>> Input::getExtColInd() {
    return getExtRowInd();
  }

  vector<pair<string, void*>> Input::getRowInd() {
    vector<vector<pair<string, void*>>> inputs = {};
    inputs.push_back({
      pair<string, void*>("0", new int(0)),
      pair<string, void*>("1", new int(1))
    });

    for (pair<string, void*> value : getNumRows()) {
      int numRows = *static_cast<int*>(value.second);

      inputs.push_back({
        pair<string, void*>(to_string(numRows / 2), new int(numRows / 2)),
        pair<string, void*>(to_string(numRows - 1), new int(numRows - 1))
      });
    }

    return vectorUnion(inputs);
  }

  vector<pair<string, void*>> Input::getExtRowInd() {
    return vectorUnion({
      getRowInd(),
      getNumRows()
    });
  }

  vector<pair<string, void*>> Input::getNumElems() {
    return {
      pair<string, void*>("1", new int(1)),
      pair<string, void*>("2", new int(2)),
      pair<string, void*>("3", new int(3)),
      pair<string, void*>("4", new int(4)),
      pair<string, void*>("5", new int(5)),
      pair<string, void*>("10", new int(10)),
      pair<string, void*>("100", new int(100)),
      pair<string, void*>("1000", new int(1000))
    };
  }

  vector<pair<string, void*>> Input::getNumCols() {
    return getNumRows();
  }

  vector<pair<string, void*>> Input::getNumRows() {
    return {
      pair<string, void*>("1", new int(1)),
      pair<string, void*>("2", new int(2)),
      pair<string, void*>("3", new int(3)),
      pair<string, void*>("4", new int(4)),
      pair<string, void*>("5", new int(5)),
      pair<string, void*>("10", new int(10))
    };
  }

  vector<pair<string, void*>> Input::getNormal() {
    return {
      pair<string, void*>("0", new int(0)),
      pair<string, void*>("1", new int(1))
    };
  }

  vector<pair<string, void*>> Input::getDim() {
    return {
      pair<string, void*>("0", new int(0)),
      pair<string, void*>("1", new int(1))
    };
  }

  vector<pair<string, void*>> Input::getMatNormInt() {
    return {
      pair<string, void*>("1", new int(1)),
      pair<string, void*>("2", new int(2))
    };
  }

  vector<pair<string, void*>> Input::getVecNormInt() {
    return {
      pair<string, void*>("1", new int(1)),
      pair<string, void*>("2", new int(2)),
      pair<string, void*>("3", new int(3)),
      pair<string, void*>("4", new int(4))
    };
  }

  vector<pair<string, void*>> Input::getGenDouble() {
    return vectorUnion({{
      pair<string, void*>("-inf", new double(-datum::inf)),
      pair<string, void*>("-1000.0", new double(-1000)),
      pair<string, void*>("-100.0", new double(-100)),
      pair<string, void*>("-10.0", new double(-10)),
      pair<string, void*>("-5.0", new double(-5)),
      pair<string, void*>("-4.0", new double(-4)),
      pair<string, void*>("-3.0", new double(-3)),
      pair<string, void*>("-euler number", new double(-datum::e)),
      pair<string, void*>("-2.0", new double(-2)),
      pair<string, void*>("-1.0", new double(-1)),
      pair<string, void*>("-0.75", new double(-0.75)),
      pair<string, void*>("-0.5", new double(-0.5)),
      pair<string, void*>("-0.25", new double(-0.25)),
      pair<string, void*>("-0.1", new double(-0.1)),
      pair<string, void*>("-0.01", new double(-0.01)),
      pair<string, void*>("-machine epsilon", new double(-datum::eps)),
      pair<string, void*>("0.0", new double(0)),
      pair<string, void*>("machine epsilon", new double(datum::eps)),
      pair<string, void*>("0.01", new double(0.01)),
      pair<string, void*>("0.1", new double(0.1)),
      pair<string, void*>("0.25", new double(0.25)),
      pair<string, void*>("0.5", new double(0.5)),
      pair<string, void*>("0.75", new double(0.75)),
      pair<string, void*>("1.0", new double(1)),
      pair<string, void*>("2.0", new double(2)),
      pair<string, void*>("euler number", new double(datum::e)),
      pair<string, void*>("3.0", new double(3)),
      pair<string, void*>("4.0", new double(4)),
      pair<string, void*>("5.0", new double(5)),
      pair<string, void*>("10.0", new double(10)),
      pair<string, void*>("100.0", new double(100)),
      pair<string, void*>("1000.0", new double(1000)),
      pair<string, void*>("inf", new double(datum::inf))
    }, getTriDouble()});
  }

  vector<pair<string, void*>> Input::getTriDouble() {
    return {
      pair<string, void*>("-inf", new double(-datum::inf)),
      pair<string, void*>("-3*pi", new double(-3*datum::pi)),
      pair<string, void*>("-2*pi", new double(-2*datum::pi)),
      pair<string, void*>("-3/2*pi", new double(-3/2*datum::pi)),
      pair<string, void*>("-pi", new double(-datum::pi)),
      pair<string, void*>("-2.0", new double(-2)),
      pair<string, void*>("-pi/2", new double(-datum::pi/2)),
      pair<string, void*>("-1.0", new double(-1)),
      pair<string, void*>("-pi/4", new double(-datum::pi/4)),
      pair<string, void*>("-pi/6", new double(-datum::pi/6)),
      pair<string, void*>("-pi/8", new double(-datum::pi/8)),
      pair<string, void*>("-pi/10", new double(-datum::pi/10)),
      pair<string, void*>("-pi/12", new double(-datum::pi/12)),
      pair<string, void*>("-machine epsilon", new double(-datum::eps)),
      pair<string, void*>("0.0", new double(0)),
      pair<string, void*>("machine epsilon", new double(datum::eps)),
      pair<string, void*>("pi/12", new double(datum::pi/12)),
      pair<string, void*>("pi/10", new double(datum::pi/10)),
      pair<string, void*>("pi/8", new double(datum::pi/8)),
      pair<string, void*>("pi/6", new double(datum::pi/6)),
      pair<string, void*>("pi/4", new double(datum::pi/4)),
      pair<string, void*>("1.0", new double(1)),
      pair<string, void*>("pi/2", new double(datum::pi/2)),
      pair<string, void*>("2.0", new double(2)),
      pair<string, void*>("pi", new double(datum::pi)),
      pair<string, void*>("3/2*pi", new double(3/2*datum::pi)),
      pair<string, void*>("2*pi", new double(2*datum::pi)),
      pair<string, void*>("3*pi", new double(3*datum::pi)),
      pair<string, void*>("inf", new double(datum::inf))
    };
  }

  vector<pair<string, void*>> Input::getSinValTol() {
    return {
      pair<string, void*>("0", new int(0)),
      pair<string, void*>("1", new int(1)),
      pair<string, void*>("-1", new int(-1))
    };
  }

  vector<pair<string, void*>> Input::getElemIndRange() {
    vector<vector<pair<string, void*>>> inputs = {};
    inputs.push_back({
      pair<string, void*>("span(0, 0)", new span(0, 0))
    });

    for (pair<string, void*> value : getNumElems()) {
      int numElems = *static_cast<int*>(value.second);

      inputs.push_back({
        pair<string, void*>("span(0, " + to_string(numElems - 1) + ")", new span(0, numElems - 1)),
        pair<string, void*>("span(" + to_string(numElems - 1) + ", " + to_string(numElems - 1) + ")", new span(numElems - 1, numElems - 1)),
        pair<string, void*>("span(" + to_string(numElems/2 - 1) + ", " + to_string(numElems/2 + 1) + ")", new span(numElems/2 - 1, numElems/2 + 1))
      });
    }

    return vectorUnion(inputs);
  }

  vector<pair<string, void*>> Input::getColIndRange() {
    return getRowIndRange();
  }

  vector<pair<string, void*>> Input::getRowIndRange() {
    vector<vector<pair<string, void*>>> inputs = {};
    inputs.push_back({
      pair<string, void*>("span(0, 0)", new span(0, 0))
    });

    for (pair<string, void*> value : getNumRows()) {
      int numRows = *static_cast<int*>(value.second);

      inputs.push_back({
        pair<string, void*>("span(0, " + to_string(numRows - 1) + ")", new span(0, numRows - 1)),
        pair<string, void*>("span(" + to_string(numRows - 1) + ", " + to_string(numRows - 1) + ")", new span(numRows - 1, numRows - 1)),
        pair<string, void*>("span(" + to_string(numRows/2 - 1) + ", " + to_string(numRows/2 + 1) + ")", new span(numRows/2 - 1, numRows/2 + 1))
      });
    }

    return vectorUnion(inputs);
  }

  vector<pair<string, void*>> Input::getMatSize() {
    vector<pair<string, void*>> input = {};

    for (pair<string, void*> value : getNumRows()) {
      int numRows = *static_cast<int*>(value.second);

      for (pair<string, void*> value : getNumCols()) {
        int numCols = *static_cast<int*>(value.second);

        input.push_back(pair<string, void*>("size(" + to_string(numRows) + ", " + to_string(numCols) + ")", new SizeMat(numRows, numCols)));
      }
    }

    return input;
  }

  vector<pair<string, void*>> Input::getColVecSize() {
    vector<pair<string, void*>> input = {};

    for (pair<string, void*> value : getNumElems()) {
      int numElems = *static_cast<int*>(value.second);

      input.push_back(pair<string, void*>("size(" + to_string(numElems) + ", 1)", new SizeMat(numElems, 1)));
    }

    return input;
  }

  vector<pair<string, void*>> Input::getRowVecSize() {
    vector<pair<string, void*>> input = {};

    for (pair<string, void*> value : getNumElems()) {
      int numElems = *static_cast<int*>(value.second);

      input.push_back(pair<string, void*>("size(1, " + to_string(numElems) + ")", new SizeMat(1, numElems)));
    }

    return input;
  }

  vector<pair<string, void*>> Input::getGenMatVec() {
    return vectorUnion({
      getGenMat(),
      getGenVec(),
      getLogicMatVec(),
      getOOMatVec()
    });
  }

  vector<pair<string, void*>> Input::getLogicMatVec() {
    return vectorUnion({
      getLogicMat(),
      getLogicVec()
    });
  }

  vector<pair<string, void*>> Input::getOOMatVec() {
    return vectorUnion({
      getOOMat(),
      getOOVec()
    });
  }

  vector<pair<string, void*>> Input::getGenMat() {
    return {

    };
  }

  vector<pair<string, void*>> Input::getSquMat() {
    return {

    };
  }

  vector<pair<string, void*>> Input::getInvMat() {
    return {

    };
  }

  vector<pair<string, void*>> Input::getSymMat() {
    return {

    };
  }

  vector<pair<string, void*>> Input::getSymPDMat() {
    return {

    };
  }

  vector<pair<string, void*>> Input::getLogicMat() {
    return {

    };
  }

  vector<pair<string, void*>> Input::getOOMat() {
    return {

    };
  }

  vector<pair<string, void*>> Input::getGenVec() {
    return vectorUnion({
      getGenColVec(),
      getGenRowVec(),
      getMonVec(),
      getLogicVec(),
      getOOVec()
    });
  }

  vector<pair<string, void*>> Input::getMonVec() {
    return vectorUnion({
      getMonColVec(),
      getMonRowVec()
    });
  }

  vector<pair<string, void*>> Input::getLogicVec() {
    return vectorUnion({
      getLogicColVec(),
      getLogicRowVec()
    });
  }

  vector<pair<string, void*>> Input::getOOVec() {
    return vectorUnion({
      getOOColVec(),
      getOORowVec()
    });
  }

  vector<pair<string, void*>> Input::getGenColVec() {
    return {

    };
  }

  vector<pair<string, void*>> Input::getMonColVec() {
    return {
      pair<string, void*>("Col(0, 1, ..., n)", new Col<double>({0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10})),
      pair<string, void*>("Col(0, 0.1, ..., 1)", new Col<double>({0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1})),
      pair<string, void*>("Col(-10, -5, 10)", new Col<double>({-10, -5, 10})),
      pair<string, void*>("Col(-inf, 0, inf)", new Col<double>({datum::inf, 0, -datum::inf})),
      pair<string, void*>("Col(0)", new Col<double>({0})),
      pair<string, void*>("Col(-inf)", new Col<double>({-datum::inf})),
      pair<string, void*>("Col(inf)", new Col<double>({datum::inf}))
    };
  }

  vector<pair<string, void*>> Input::getLogicColVec() {
    return {

    };
  }

  vector<pair<string, void*>> Input::getOOColVec() {
    return {

    };
  }

  vector<pair<string, void*>> Input::getGenRowVec() {
    return {

    };
  }

  vector<pair<string, void*>> Input::getMonRowVec() {
    return {
      pair<string, void*>("Row(0, 1, ..., n)", new Row<double>({0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10})),
      pair<string, void*>("Row(0, 0.1, ..., 1)", new Row<double>({0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1})),
      pair<string, void*>("Row(-10, -5, 10)", new Row<double>({-10, -5, 10})),
      pair<string, void*>("Row(-inf, 0, inf)", new Row<double>({datum::inf, 0, -datum::inf})),
      pair<string, void*>("Row(0)", new Row<double>({0})),
      pair<string, void*>("Row(-inf)", new Row<double>({-datum::inf})),
      pair<string, void*>("Row(inf)", new Row<double>({datum::inf}))
    };
  }

  vector<pair<string, void*>> Input::getLogicRowVec() {
    return {

    };
  }

  vector<pair<string, void*>> Input::getOORowVec() {
    return {

    };
  }

  vector<pair<string, void*>> Input::getElemInds() {
    return {

    };
  }

  vector<pair<string, void*>> Input::getColInds() {
    return {

    };
  }

  vector<pair<string, void*>> Input::getRowInds() {
    return {

    };
  }

  vector<pair<string, void*>> Input::getMatNormString() {
    return {
      pair<string, void*>("'inf'", new string("inf")),
      pair<string, void*>("'fro'", new string("fro"))
    };
  }

  vector<pair<string, void*>> Input::getVecNormString() {
    return {
      pair<string, void*>("'inf'", new string("inf")),
      pair<string, void*>("'-inf'", new string("-inf")),
      pair<string, void*>("'fro'", new string("fro"))
    };
  }

  vector<pair<string, void*>> Input::getSort() {
    return {
      pair<string, void*>("'ascend'", new string("ascend")),
      pair<string, void*>("'descend'", new string("descend"))
    };
  }

  vector<pair<string, void*>> Input::getSinValSel() {
    return {
      pair<string, void*>("'left'", new string("left")),
      pair<string, void*>("'right'", new string("right")),
      pair<string, void*>("'both'", new string("both"))
    };
  }

  vector<pair<string, void*>> Input::getDistrParam() {
    return {
      pair<string, void*>("distr_param(0, 10)", new distr_param(0, 10)),
      pair<string, void*>("distr_param(1, 1)", new distr_param(1, 1)),
      pair<string, void*>("distr_param(-5, 6)", new distr_param(-5, 6))
    };
  }

  vector<pair<string, void*>> Input::getFill() {
    return {

    };
  }
}
