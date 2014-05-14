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
using std::max;
using std::sort;
using std::set_union;

#include <string>
using std::to_string;

#include <stdexcept>
using std::runtime_error;

#include <vector>
using std::vector;

#include <initializer_list>
using std::initializer_list;

#include <armadillo>
using arma::Mat;
using arma::Col;
using arma::Row;
using arma::uword;
using arma::zeros;
using arma::ones;
using arma::eye;
using arma::datum;
using arma::span;
using arma::SizeMat;
using arma::distr_param;

#include <iostream>
using std::cout;
using std::endl;

namespace armadilloJava {
  vector<vector<pair<string, void*>>> Input::getTestParameters(const vector<InputClass>& inputClasses) {
    vector<vector<pair<string, void*>>> inputs;

    for (InputClass inputClass : inputClasses) {
      switch (inputClass) {
        case InputClass::ElemInd:
          inputs.push_back(getElemInd());
          break;
        case InputClass::ColInd:
          inputs.push_back(getColInd());
          break;
        case InputClass::ExtColInd:
          inputs.push_back(getExtColInd());
          break;
        case InputClass::RowInd:
          inputs.push_back(getRowInd());
          break;
        case InputClass::ExtRowInd:
          inputs.push_back(getExtRowInd());
          break;
        case InputClass::NumElems:
          inputs.push_back(getNumElems());
          break;
        case InputClass::NumCols:
          inputs.push_back(getNumCols());
          break;
        case InputClass::NumRows:
          inputs.push_back(getNumRows());
          break;
        case InputClass::Normal:
          inputs.push_back(getNormal());
          break;
        case InputClass::Dim:
          inputs.push_back(getDim());
          break;
        case InputClass::Exp:
          inputs.push_back(getExp());
          break;
        case InputClass::MatNormInt:
          inputs.push_back(getMatNormInt());
          break;
        case InputClass::VecNormInt:
          inputs.push_back(getVecNormInt());
          break;
        case InputClass::GenDouble:
          inputs.push_back(getGenDouble());
          break;
        case InputClass::SinValTol:
          inputs.push_back(getSinValTol());
          break;
        case InputClass::ElemIndRange:
          inputs.push_back(getElemIndRange());
          break;
        case InputClass::ColIndRange:
          inputs.push_back(getColIndRange());
          break;
        case InputClass::RowIndRange:
          inputs.push_back(getRowIndRange());
          break;
        case InputClass::MatSize:
          inputs.push_back(getMatSize());
          break;
        case InputClass::GenMat:
          inputs.push_back(getGenMat());
          break;
        case InputClass::SquMat:
          inputs.push_back(getSquMat());
          break;
        case InputClass::InvMat:
          inputs.push_back(getInvMat());
          break;
        case InputClass::SymMat:
          inputs.push_back(getSymMat());
          break;
        case InputClass::SymPDMat:
          inputs.push_back(getSymPDMat());
          break;
        case InputClass::LogicMat:
          inputs.push_back(getLogicMat());
          break;
        case InputClass::OOMat:
          inputs.push_back(getOOMat());
          break;
        case InputClass::GenColVec:
          inputs.push_back(getGenColVec());
          break;
        case InputClass::MonColVec:
          inputs.push_back(getMonColVec());
          break;
        case InputClass::LogicColVec:
          inputs.push_back(getLogicColVec());
          break;
        case InputClass::OOColVec:
          inputs.push_back(getOOColVec());
          break;
        case InputClass::GenRowVec:
          inputs.push_back(getGenRowVec());
          break;
        case InputClass::MonRowVec:
          inputs.push_back(getMonRowVec());
          break;
        case InputClass::LogicRowVec:
          inputs.push_back(getLogicRowVec());
          break;
        case InputClass::OORowVec:
          inputs.push_back(getOORowVec());
          break;
        case InputClass::ElemInds:
          inputs.push_back(getElemInds());
          break;
        case InputClass::ColInds:
          inputs.push_back(getColInds());
          break;
        case InputClass::RowInds:
          inputs.push_back(getRowInds());
          break;
        case InputClass::Text:
          inputs.push_back(getText());
          break;
        case InputClass::MatNormString:
          inputs.push_back(getMatNormString());
          break;
        case InputClass::VecNormString:
          inputs.push_back(getVecNormString());
          break;
        case InputClass::Sort:
          inputs.push_back(getSort());
          break;
        case InputClass::Search:
          inputs.push_back(getSearch());
          break;
        case InputClass::SinValSel:
          inputs.push_back(getSinValSel());
          break;
        case InputClass::DistrParam:
          inputs.push_back(getDistrParam());
          break;
        case InputClass::Fill:
          inputs.push_back(getFill());
          break;
        case InputClass::Random:
          inputs.push_back(getRandom());
          break;
        default:
          throw new runtime_error("Unsupported input class requested.");
      }
    }

    return cartesianProduct(inputs);
  }

  vector<vector<pair<string, void*>>> Input::cartesianProduct(const vector<vector<pair<string, void*>>>& inputs) {
    vector<vector<pair<string, void*>>> cartesianProduct = {{}};
    for (vector<pair<string, void*>> input : inputs) {
      vector<vector<pair<string, void*>>> tempProduct;
      for (vector<pair<string, void*>> tempInput : cartesianProduct) {
        for (pair<string, void*> keyValuePair : input) {
          tempProduct.push_back(tempInput);
          tempProduct.back().push_back(keyValuePair);
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
    Mat<double> kmsMatrix(n_rows, n_cols);

    for (int j = 0; j < n_cols; j++) {
      for (int i = 0; i < n_rows; i++) {
        kmsMatrix.at(i, j) = pow(2, abs(i - j));
      }
    }

    return kmsMatrix;
  }

  vector<pair<string, void*>> Input::getElemInd() {
    return {
      pair<string, void*>("0", new int(0)),
      pair<string, void*>("9", new int(9))
    };
  }

  vector<pair<string, void*>> Input::getColInd() {
    return getRowInd();
  }

  vector<pair<string, void*>> Input::getExtColInd() {
    return getExtRowInd();
  }

  vector<pair<string, void*>> Input::getRowInd() {
    return {
      pair<string, void*>("0", new int(0)),
      pair<string, void*>("1", new int(1))
    };
  }

  vector<pair<string, void*>> Input::getExtRowInd() {
    return {
      pair<string, void*>("0", new int(0)),
      pair<string, void*>("1", new int(1)),
      pair<string, void*>("2", new int(2))
    };
  }

  vector<pair<string, void*>> Input::getNumElems() {
    return {
      pair<string, void*>("1", new int(1)),
      pair<string, void*>("10", new int(10))
    };
  }

  vector<pair<string, void*>> Input::getNumCols() {
    return getNumRows();
  }

  vector<pair<string, void*>> Input::getNumRows() {
    return {
      pair<string, void*>("1", new int(1)),
      pair<string, void*>("5", new int(5))
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

  vector<pair<string, void*>> Input::getExp() {
    return {
      pair<string, void*>("0.5", new double(0.5)),
      pair<string, void*>("1.0", new double(1)),
      pair<string, void*>("2.0", new double(2)),
      pair<string, void*>("3.0", new double(3))
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
    return {
      pair<string, void*>("-inf", new double(-datum::inf)),
      pair<string, void*>("-2.0", new double(-2)),
      pair<string, void*>("0.0", new double(0)),
      pair<string, void*>("machine_epsilon", new double(datum::eps)),
      pair<string, void*>("0.5", new double(0.5)),
      pair<string, void*>("1.0", new double(1)),
      pair<string, void*>("euler_number", new double(datum::e)),
      pair<string, void*>("pi", new double(datum::pi)),
      pair<string, void*>("inf", new double(datum::inf))
    };
  }

  vector<pair<string, void*>> Input::getSinValTol() {
    return {
      pair<string, void*>("0.0", new double(0)),
      pair<string, void*>("1.0", new double(1))
    };
  }

  vector<pair<string, void*>> Input::getElemIndRange() {
    return {
      pair<string, void*>("span(0)", new span(0)),
      pair<string, void*>("span(0,9)", new span(0, 9)),
      pair<string, void*>("span(1,4)", new span(1, 4))
    };
  }

  vector<pair<string, void*>> Input::getColIndRange() {
    return getRowIndRange();
  }

  vector<pair<string, void*>> Input::getRowIndRange() {
    return {
      pair<string, void*>("span.all", new span(span::all)),
      pair<string, void*>("span(0)", new span(0)),
      pair<string, void*>("span(0,4)", new span(0, 4)),
      pair<string, void*>("span(1,2)", new span(1, 2))
    };
  }

  vector<pair<string, void*>> Input::getMatSize() {
    return {
      pair<string, void*>("size(1,1)", new pair<int, int>(1, 1)),
      pair<string, void*>("size(1,3)", new pair<int, int>(1, 3)),
      pair<string, void*>("size(1,5)", new pair<int, int>(1, 5)),
      pair<string, void*>("size(2,5)", new pair<int, int>(2, 5)),
      pair<string, void*>("size(3,1)", new pair<int, int>(3, 1)),
      pair<string, void*>("size(5,1)", new pair<int, int>(5, 1)),
      pair<string, void*>("size(5,2)", new pair<int, int>(5, 2)),
      pair<string, void*>("size(5,5)", new pair<int, int>(5, 5))
    };
  }

  vector<pair<string, void*>> Input::getGenMat() {
    vector<pair<string, void*>> input = {};

    for (pair<string, void*> keyValuePair : getMatSize()) {
      SizeMat size = *static_cast<SizeMat*>(keyValuePair.second);

      input.push_back(pair<string, void*>("Mat(zeros(" + to_string(size.n_rows) + "," + to_string(size.n_cols) + "))", new Mat<double>(zeros<Mat<double>>(size.n_rows, size.n_cols))));
      input.push_back(pair<string, void*>("Mat(ones(" + to_string(size.n_rows) + "," + to_string(size.n_cols) + "))", new Mat<double>(ones<Mat<double>>(size.n_rows, size.n_cols))));
      input.push_back(pair<string, void*>("Mat(eye(" + to_string(size.n_rows) + "," + to_string(size.n_cols) + "))", new Mat<double>(eye<Mat<double>>(size.n_rows, size.n_cols))));
      input.push_back(pair<string, void*>("Mat(hilbert(" + to_string(size.n_rows) + "," + to_string(size.n_cols) + "))", new Mat<double>(getHilbertMatrix(size.n_rows, size.n_cols))));
      input.push_back(pair<string, void*>("Mat(kms(" + to_string(size.n_rows) + "," + to_string(size.n_cols) + "))", new Mat<double>(getKMSMatrix(size.n_rows, size.n_cols))));
    }

    Mat<double> infinite = {datum::inf, -datum::inf, -datum::inf, datum::inf};
    infinite.reshape(2, 2);
    input.push_back(pair<string, void*>("Mat({{inf,-inf},{-inf,inf}})", new Mat<double>(infinite)));

    return input;
  }

  vector<pair<string, void*>> Input::getSquMat() {
    return vectorUnion({
      getInvMat(),
      getSymMat()
    });
  }

  vector<pair<string, void*>> Input::getInvMat() {
    vector<pair<string, void*>> input = {};

    for (pair<string, void*> keyValuePair : getMatSize()) {
      SizeMat size = *static_cast<SizeMat*>(keyValuePair.second);

      if(size.n_rows == size.n_cols) {
        input.push_back(pair<string, void*>("Mat(eye(" + to_string(size.n_rows) + "," + to_string(size.n_cols) + "))", new Mat<double>(eye<Mat<double>>(size.n_rows, size.n_cols))));
        input.push_back(pair<string, void*>("Mat(kms(" + to_string(size.n_rows) + "," + to_string(size.n_cols) + "))", new Mat<double>(getKMSMatrix(size.n_rows, size.n_cols))));
      }
    }

    return input;
  }

  vector<pair<string, void*>> Input::getSymMat() {
    vector<vector<pair<string, void*>>> inputs = {};
    inputs.push_back(getSymPDMat());

    for (pair<string, void*> keyValuePair : getMatSize()) {
      SizeMat size = *static_cast<SizeMat*>(keyValuePair.second);

      if(size.n_rows == size.n_cols) {
        inputs.push_back({
          pair<string, void*>("Mat(zeros(" + to_string(size.n_rows) + "," + to_string(size.n_cols) + "))", new Mat<double>(zeros<Mat<double>>(size.n_rows, size.n_cols))),
          pair<string, void*>("Mat(ones(" + to_string(size.n_rows) + "," + to_string(size.n_cols) + "))", new Mat<double>(ones<Mat<double>>(size.n_rows, size.n_cols)))
        });
      }
    }

    return vectorUnion(inputs);
  }

  vector<pair<string, void*>> Input::getSymPDMat() {
    vector<pair<string, void*>> input = {};

    for (pair<string, void*> keyValuePair : getMatSize()) {
      SizeMat size = *static_cast<SizeMat*>(keyValuePair.second);

      if(size.n_rows == size.n_cols) {
        input.push_back(pair<string, void*>("Mat(eye(" + to_string(size.n_rows) + "," + to_string(size.n_cols) + "))", new Mat<double>(eye<Mat<double>>(size.n_rows, size.n_cols))));
        input.push_back(pair<string, void*>("Mat(hilbert(" + to_string(size.n_rows) + "," + to_string(size.n_cols) + "))", new Mat<double>(getHilbertMatrix(size.n_rows, size.n_cols))));
      }
    }

    return input;
  }

  vector<pair<string, void*>> Input::getLogicMat() {
    vector<pair<string, void*>> input = {};

    for (pair<string, void*> keyValuePair : getMatSize()) {
      SizeMat size = *static_cast<SizeMat*>(keyValuePair.second);

      input.push_back(pair<string, void*>("Mat(zeros(" + to_string(size.n_rows) + "," + to_string(size.n_cols) + "))", new Mat<double>(zeros<Mat<double>>(size.n_rows, size.n_cols))));
      input.push_back(pair<string, void*>("Mat(ones(" + to_string(size.n_rows) + "," + to_string(size.n_cols) + "))", new Mat<double>(ones<Mat<double>>(size.n_rows, size.n_cols))));
      input.push_back(pair<string, void*>("Mat(eye(" + to_string(size.n_rows) + "," + to_string(size.n_cols) + "))", new Mat<double>(eye<Mat<double>>(size.n_rows, size.n_cols))));
    }

    return input;
  }

  vector<pair<string, void*>> Input::getOOMat() {
    vector<pair<string, void*>> input = {};

    for (pair<string, void*> keyValuePair : getGenDouble()) {
      double genDouble = *static_cast<double*>(keyValuePair.second);

      input.push_back(pair<string, void*>("Mat({" + keyValuePair.first + "})", new Mat<double>({genDouble})));
    }

    return input;
  }

  vector<pair<string, void*>> Input::getGenColVec() {
    vector<pair<string, void*>> input = {};

    for (pair<string, void*> keyValuePair : getMatSize()) {
      SizeMat size = *static_cast<SizeMat*>(keyValuePair.second);

      if(size.n_cols == 1) {
        input.push_back(pair<string, void*>("Col(zeros(" + to_string(size.n_rows) + ",1))", new Col<double>(zeros<Col<double>>(size.n_rows))));
        input.push_back(pair<string, void*>("Col(ones(" + to_string(size.n_rows) + ",1))", new Col<double>(ones<Col<double>>(size.n_rows))));
        input.push_back(pair<string, void*>("Col(eye(" + to_string(size.n_rows) + ",1))", new Col<double>(eye<Col<double>>(size.n_rows, 1))));
        input.push_back(pair<string, void*>("Col(hilbert(" + to_string(size.n_rows) + ",1))", new Col<double>(getHilbertMatrix(size.n_rows, 1))));
        input.push_back(pair<string, void*>("Col(kms(" + to_string(size.n_rows) + ",1))", new Col<double>(getKMSMatrix(size.n_rows, 1))));
      }
    }
    input.push_back(pair<string, void*>("Col({inf,-inf})", new Col<double>({datum::inf, -datum::inf})));

    return input;
  }

  vector<pair<string, void*>> Input::getMonColVec() {
    return {
      pair<string, void*>("Col({-10,-5,0.5,10})", new Col<double>({-10, -5, 0.5, 10})),
      pair<string, void*>("Col({-inf,0,inf})", new Col<double>({datum::inf, 0, -datum::inf})),
      pair<string, void*>("Col({0})", new Col<double>({0.0})),
      pair<string, void*>("Col({-inf})", new Col<double>({-datum::inf})),
      pair<string, void*>("Col({inf})", new Col<double>({datum::inf}))
    };
  }

  vector<pair<string, void*>> Input::getLogicColVec() {
    vector<pair<string, void*>> input = {};

    for (pair<string, void*> keyValuePair : getMatSize()) {
      SizeMat size = *static_cast<SizeMat*>(keyValuePair.second);

      if(size.n_cols == 1) {
        input.push_back(pair<string, void*>("Col(zeros(" + to_string(size.n_rows) + ",1))", new Col<double>(zeros<Col<double>>(size.n_rows))));
        input.push_back(pair<string, void*>("Col(ones(" + to_string(size.n_rows) + ",1))", new Col<double>(ones<Col<double>>(size.n_rows))));
        input.push_back(pair<string, void*>("Col(eye(" + to_string(size.n_rows) + ",1))", new Col<double>(eye<Col<double>>(size.n_rows, 1))));
      }
    }

    return input;
  }

  vector<pair<string, void*>> Input::getOOColVec() {
    vector<pair<string, void*>> input = {};

    for (pair<string, void*> keyValuePair : getGenDouble()) {
      double genDouble = *static_cast<double*>(keyValuePair.second);

      input.push_back(pair<string, void*>("Col({" + keyValuePair.first + "})", new Col<double>({genDouble})));
    }

    return input;
  }

  vector<pair<string, void*>> Input::getGenRowVec() {
    vector<pair<string, void*>> input = {};

    for (pair<string, void*> keyValuePair : getMatSize()) {
      SizeMat size = *static_cast<SizeMat*>(keyValuePair.second);

      if(size.n_rows == 1) {
        input.push_back(pair<string, void*>("Row(zeros(1," + to_string(size.n_cols) + "))", new Row<double>(zeros<Row<double>>(size.n_cols))));
        input.push_back(pair<string, void*>("Row(ones(1," + to_string(size.n_cols) + "))", new Row<double>(ones<Row<double>>(size.n_cols))));
        input.push_back(pair<string, void*>("Row(eye(1," + to_string(size.n_cols) + "))", new Row<double>(eye<Row<double>>(1, size.n_cols))));
        input.push_back(pair<string, void*>("Row(hilbert(1," + to_string(size.n_cols) + "))", new Row<double>(getHilbertMatrix(1, size.n_cols))));
        input.push_back(pair<string, void*>("Row(kms(1," + to_string(size.n_cols) + "))", new Row<double>(getKMSMatrix(1, size.n_cols))));
      }
    }
    input.push_back(pair<string, void*>("Row({inf,-inf})", new Row<double>({datum::inf, -datum::inf})));

    return input;
  }

  vector<pair<string, void*>> Input::getMonRowVec() {
    return {
      pair<string, void*>("Row({-10,-5,0.5,10})", new Row<double>({-10, -5, 0.5, 10})),
      pair<string, void*>("Row({-inf,0,inf})", new Row<double>({datum::inf, 0, -datum::inf})),
      pair<string, void*>("Row({0})", new Row<double>({0.0})),
      pair<string, void*>("Row({-inf})", new Row<double>({-datum::inf})),
      pair<string, void*>("Row({inf})", new Row<double>({datum::inf}))
    };
  }

  vector<pair<string, void*>> Input::getLogicRowVec() {
    vector<pair<string, void*>> input = {};

    for (pair<string, void*> keyValuePair : getMatSize()) {
      SizeMat size = *static_cast<SizeMat*>(keyValuePair.second);

      if(size.n_rows == 1) {
        input.push_back(pair<string, void*>("Row(zeros(1," + to_string(size.n_cols) + "))", new Row<double>(zeros<Row<double>>(size.n_cols))));
        input.push_back(pair<string, void*>("Row(ones(1," + to_string(size.n_cols) + "))", new Row<double>(ones<Row<double>>(size.n_cols))));
        input.push_back(pair<string, void*>("Row(eye(1," + to_string(size.n_cols) + "))", new Row<double>(eye<Row<double>>(1, size.n_cols))));
      }
    }

    return input;
  }

  vector<pair<string, void*>> Input::getOORowVec() {
    vector<pair<string, void*>> input = {};

    for (pair<string, void*> keyValuePair : getGenDouble()) {
      double genDouble = *static_cast<double*>(keyValuePair.second);

      input.push_back(pair<string, void*>("Row({" + keyValuePair.first + "})", new Row<double>({genDouble})));
    }

    return input;
  }

  vector<pair<string, void*>> Input::getElemInds() {
    return {
      pair<string, void*>("Col({0})", new Col<uword>(initializer_list<unsigned int>({0}))),
      pair<string, void*>("Col({9})", new Col<uword>(initializer_list<unsigned int>({9}))),
      pair<string, void*>("Col({9,0,1})", new Col<uword>({9, 0, 1})),
      pair<string, void*>("Col({1,1,1})", new Col<uword>({1, 1, 1}))
    };
  }

  vector<pair<string, void*>> Input::getColInds() {
    return getRowInds();
  }

  vector<pair<string, void*>> Input::getRowInds() {
    return {
      pair<string, void*>("Col({0})", new Col<uword>(initializer_list<unsigned int>({0}))),
      pair<string, void*>("Col({4})", new Col<uword>(initializer_list<unsigned int>({4}))),
      pair<string, void*>("Col({4,0,1})", new Col<uword>({4, 0, 1})),
      pair<string, void*>("Col({1,1,1})", new Col<uword>({1, 1, 1}))
    };
  }

  vector<pair<string, void*>> Input::getText() {
    return {
      pair<string, void*>("'Test_string'", new string("Test string"))
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

  vector<pair<string, void*>> Input::getSearch() {
    return {
      pair<string, void*>("'first'", new string("first")),
      pair<string, void*>("'last'", new string("last"))
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
      pair<string, void*>("distr_param(0,10)", new distr_param(0, 10)),
      pair<string, void*>("distr_param(1,1)", new distr_param(1, 1)),
      pair<string, void*>("distr_param(-5,6)", new distr_param(-5, 6))
    };
  }

  vector<pair<string, void*>> Input::getFill() {
    return {

    };
  }

  vector<pair<string, void*>> Input::getRandom() {
    return {
      pair<string, void*>("1000", new int(1000))
    };
  }
}
