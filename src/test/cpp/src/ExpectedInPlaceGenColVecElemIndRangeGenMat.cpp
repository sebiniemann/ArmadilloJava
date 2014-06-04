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
#include <Expected.hpp>
using armadilloJava::Expected;

#include <iostream>
using std::cout;
using std::endl;

#include <utility>
using std::pair;

#include <armadillo>
using arma::Col;
using arma::span;
using arma::Mat;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedInPlaceGenColVecElemIndRangeGenMat: public Expected {
    public:
      ExpectedInPlaceGenColVecElemIndRangeGenMat() {
        cout << "Compute ExpectedInPlaceGenColVecElemIndRangeGenMat(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters( {
            InputClass::GenColVec,
            InputClass::ElemIndRange,
            InputClass::GenMat
          });

        for (vector<pair<string, void*>> input : inputs) {
          _fileSuffix = "";

          int n = 0;
          for (pair<string, void*> value : input) {
            switch (n) {
              case 0:
                _fileSuffix += value.first;
                _genColVec = *static_cast<Col<double>*>(value.second);
                break;
              case 1:
                _fileSuffix += "," + value.first;
                _elemIndRange = *static_cast<span*>(value.second);
                break;
              case 2:
                _fileSuffix += "," + value.first;
                _genMat = *static_cast<Mat<double>*>(value.second);
                break;
            }
            ++n;
          }

          cout << "Using input: " << _fileSuffix << endl;

          _copyOfGenColVec = _genColVec;
          _copyOfElemIndRange = _elemIndRange;
          _copyOfGenMat = _genMat;

          expectedColVecRowsEqual();

          _genColVec = _copyOfGenColVec;
          _elemIndRange = _copyOfElemIndRange;
          _genMat = _copyOfGenMat;
          expectedColVecRowsPlus();

          _genColVec = _copyOfGenColVec;
          _elemIndRange = _copyOfElemIndRange;
          _genMat = _copyOfGenMat;
          expectedColVecRowsMinus();

          _genColVec = _copyOfGenColVec;
          _elemIndRange = _copyOfElemIndRange;
          _genMat = _copyOfGenMat;
          expectedColVecRowsTimes();

          _genColVec = _copyOfGenColVec;
          _elemIndRange = _copyOfElemIndRange;
          _genMat = _copyOfGenMat;
          expectedColVecRowsDivide();

          _genColVec = _copyOfGenColVec;
          _elemIndRange = _copyOfElemIndRange;
          _genMat = _copyOfGenMat;
          expectedColVecSubvecEqual();

          _genColVec = _copyOfGenColVec;
          _elemIndRange = _copyOfElemIndRange;
          _genMat = _copyOfGenMat;
          expectedColVecSubvecPlus();

          _genColVec = _copyOfGenColVec;
          _elemIndRange = _copyOfElemIndRange;
          _genMat = _copyOfGenMat;
          expectedColVecSubvecMinus();

          _genColVec = _copyOfGenColVec;
          _elemIndRange = _copyOfElemIndRange;
          _genMat = _copyOfGenMat;
          expectedColVecSubvecTimes();

          _genColVec = _copyOfGenColVec;
          _elemIndRange = _copyOfElemIndRange;
          _genMat = _copyOfGenMat;
          expectedColVecSubvecDivide();
        }

        cout << "done." << endl;
      }

    protected:
      Col<double> _genColVec;
      Col<double> _copyOfGenColVec;

      span _elemIndRange;
      span _copyOfElemIndRange;

      Mat<double> _genMat;
      Mat<double> _copyOfGenMat;

      void expectedColVecRowsEqual() {
        if (!_genColVec.in_range(_elemIndRange)) {
          return;

        }

        if(_genMat.n_cols != _genColVec.n_cols || _genMat.n_rows != _elemIndRange.b - _elemIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedColVecRowsEqual() ... ";

        _genColVec.rows(_elemIndRange.a, _elemIndRange.b) = _genMat;

        save<double>("Col.rowsEqual", _genColVec);
        cout << "done." << endl;
      }

      void expectedColVecRowsPlus() {
        if (!_genColVec.in_range(_elemIndRange)) {
          return;

        }

        if(_genMat.n_cols != _genColVec.n_cols || _genMat.n_rows != _elemIndRange.b - _elemIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedColVecRowsPlus() ... ";

        _genColVec.rows(_elemIndRange.a, _elemIndRange.b) += _genMat;

        save<double>("Col.rowsPlus", _genColVec);
        cout << "done." << endl;
      }

      void expectedColVecRowsMinus() {
        if (!_genColVec.in_range(_elemIndRange)) {
          return;

        }

        if(_genMat.n_cols != _genColVec.n_cols || _genMat.n_rows != _elemIndRange.b - _elemIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedColVecRowsMinus() ... ";

        _genColVec.rows(_elemIndRange.a, _elemIndRange.b) -= _genMat;

        save<double>("Col.rowsMinus", _genColVec);
        cout << "done." << endl;
      }

      void expectedColVecRowsTimes() {
        if (!_genColVec.in_range(_elemIndRange)) {
          return;

        }

        if(_genMat.n_cols != _genColVec.n_cols || _genMat.n_rows != _elemIndRange.b - _elemIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedColVecRowsTimes() ... ";

        _genColVec.rows(_elemIndRange.a, _elemIndRange.b) %= _genMat;

        save<double>("Col.rowsElemTimes", _genColVec);
        cout << "done." << endl;
      }

      void expectedColVecRowsDivide() {
        if (!_genColVec.in_range(_elemIndRange)) {
          return;

        }

        if(_genMat.n_cols != _genColVec.n_cols || _genMat.n_rows != _elemIndRange.b - _elemIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedColVecRowsDivide() ... ";

        _genColVec.rows(_elemIndRange.a, _elemIndRange.b) /= _genMat;

        save<double>("Col.rowsElemDivide", _genColVec);
        cout << "done." << endl;
      }

      void expectedColVecSubvecEqual() {
        if (!_genColVec.in_range(_elemIndRange)) {
          return;

        }

        if(_genMat.n_cols != _genColVec.n_cols || _genMat.n_rows != _elemIndRange.b - _elemIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedColVecSubvecEqual() ... ";

        _genColVec.subvec(_elemIndRange.a, _elemIndRange.b) = _genMat;

        save<double>("Col.subvecEqual", _genColVec);
        cout << "done." << endl;
      }

      void expectedColVecSubvecPlus() {
        if (!_genColVec.in_range(_elemIndRange)) {
          return;

        }

        if(_genMat.n_cols != _genColVec.n_cols || _genMat.n_rows != _elemIndRange.b - _elemIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedColVecSubvecPlus() ... ";

        _genColVec.subvec(_elemIndRange.a, _elemIndRange.b) += _genMat;

        save<double>("Col.subvecPlus", _genColVec);
        cout << "done." << endl;
      }

      void expectedColVecSubvecMinus() {
        if (!_genColVec.in_range(_elemIndRange)) {
          return;

        }

        if(_genMat.n_cols != _genColVec.n_cols || _genMat.n_rows != _elemIndRange.b - _elemIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedColVecSubvecMinus() ... ";

        _genColVec.subvec(_elemIndRange.a, _elemIndRange.b) -= _genMat;

        save<double>("Col.subvecMinus", _genColVec);
        cout << "done." << endl;
      }

      void expectedColVecSubvecTimes() {
        if (!_genColVec.in_range(_elemIndRange)) {
          return;

        }

        if(_genMat.n_cols != _genColVec.n_cols || _genMat.n_rows != _elemIndRange.b - _elemIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedColVecSubvecTimes() ... ";

        _genColVec.subvec(_elemIndRange.a, _elemIndRange.b) %= _genMat;

        save<double>("Col.subvecElemTimes", _genColVec);
        cout << "done." << endl;
      }

      void expectedColVecSubvecDivide() {
        if (!_genColVec.in_range(_elemIndRange)) {
          return;

        }

        if(_genMat.n_cols != _genColVec.n_cols || _genMat.n_rows != _elemIndRange.b - _elemIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedColVecSubvecDivide() ... ";

        _genColVec.subvec(_elemIndRange.a, _elemIndRange.b) /= _genMat;

        save<double>("Col.subvecElemDivide", _genColVec);
        cout << "done." << endl;
      }

  };
}
