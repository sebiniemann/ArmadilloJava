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
using arma::Row;
using arma::span;
using arma::Mat;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedInPlaceGenRowVecElemIndRangeGenMat: public Expected {
    public:
      ExpectedInPlaceGenRowVecElemIndRangeGenMat() {
        cout << "Compute ExpectedInPlaceGenRowVecElemIndRangeGenMat(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters( {
            InputClass::GenRowVec,
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
                _genRowVec = *static_cast<Row<double>*>(value.second);
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

          _copyOfGenRowVec = _genRowVec;
          _copyOfElemIndRange = _elemIndRange;
          _copyOfGenMat = _genMat;

          expectedRowVecColsEqual();

          _genRowVec = _copyOfGenRowVec;
          _elemIndRange = _copyOfElemIndRange;
          _genMat = _copyOfGenMat;
          expectedRowVecColsPlus();

          _genRowVec = _copyOfGenRowVec;
          _elemIndRange = _copyOfElemIndRange;
          _genMat = _copyOfGenMat;
          expectedRowVecColsMinus();

          _genRowVec = _copyOfGenRowVec;
          _elemIndRange = _copyOfElemIndRange;
          _genMat = _copyOfGenMat;
          expectedRowVecColsElemTimes();

          _genRowVec = _copyOfGenRowVec;
          _elemIndRange = _copyOfElemIndRange;
          _genMat = _copyOfGenMat;
          expectedRowVecColsElemDivide();

          _genRowVec = _copyOfGenRowVec;
          _elemIndRange = _copyOfElemIndRange;
          _genMat = _copyOfGenMat;
          expectedRowVecSubvecEqual();

          _genRowVec = _copyOfGenRowVec;
          _elemIndRange = _copyOfElemIndRange;
          _genMat = _copyOfGenMat;
          expectedRowVecSubvecPlus();

          _genRowVec = _copyOfGenRowVec;
          _elemIndRange = _copyOfElemIndRange;
          _genMat = _copyOfGenMat;
          expectedRowVecSubvecMinus();

          _genRowVec = _copyOfGenRowVec;
          _elemIndRange = _copyOfElemIndRange;
          _genMat = _copyOfGenMat;
          expectedRowVecSubvecElemTimes();

          _genRowVec = _copyOfGenRowVec;
          _elemIndRange = _copyOfElemIndRange;
          _genMat = _copyOfGenMat;
          expectedRowVecSubvecElemDivide();
        }

        cout << "done." << endl;
      }

    protected:
      Row<double> _genRowVec;
      Row<double> _copyOfGenRowVec;

      span _elemIndRange;
      span _copyOfElemIndRange;

      Mat<double> _genMat;
      Mat<double> _copyOfGenMat;

      void expectedRowVecColsEqual() {
        if(_elemIndRange.whole) {
          return;
        }

        if (!_genRowVec.in_range(_elemIndRange)) {
          return;

        }

        if(_genMat.n_rows != _genRowVec.n_rows || _genMat.n_cols != _elemIndRange.b - _elemIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedRowVecColsEqual() ... ";

        _genRowVec.cols(_elemIndRange.a, _elemIndRange.b) = _genMat;

        save<double>("Row.colsEqual", _genRowVec);
        cout << "done." << endl;
      }

      void expectedRowVecColsPlus() {
        if(_elemIndRange.whole) {
          return;
        }

        if (!_genRowVec.in_range(_elemIndRange)) {
          return;

        }

        if(_genMat.n_rows != _genRowVec.n_rows || _genMat.n_cols != _elemIndRange.b - _elemIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedRowVecColsPlus() ... ";

        _genRowVec.cols(_elemIndRange.a, _elemIndRange.b) += _genMat;

        save<double>("Row.colsPlus", _genRowVec);
        cout << "done." << endl;
      }

      void expectedRowVecColsMinus() {
        if(_elemIndRange.whole) {
          return;
        }

        if (!_genRowVec.in_range(_elemIndRange)) {
          return;

        }

        if(_genMat.n_rows != _genRowVec.n_rows || _genMat.n_cols != _elemIndRange.b - _elemIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedRowVecColsMinus() ... ";

        _genRowVec.cols(_elemIndRange.a, _elemIndRange.b) -= _genMat;

        save<double>("Row.colsMinus", _genRowVec);
        cout << "done." << endl;
      }

      void expectedRowVecColsElemTimes() {
        if(_elemIndRange.whole) {
          return;
        }

        if (!_genRowVec.in_range(_elemIndRange)) {
          return;

        }

        if(_genMat.n_rows != _genRowVec.n_rows || _genMat.n_cols != _elemIndRange.b - _elemIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedRowVecColsElemTimes() ... ";

        _genRowVec.cols(_elemIndRange.a, _elemIndRange.b) %= _genMat;

        save<double>("Row.colsElemTimes", _genRowVec);
        cout << "done." << endl;
      }

      void expectedRowVecColsElemDivide() {
        if(_elemIndRange.whole) {
          return;
        }

        if (!_genRowVec.in_range(_elemIndRange)) {
          return;

        }

        if(_genMat.n_rows != _genRowVec.n_rows || _genMat.n_cols != _elemIndRange.b - _elemIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedRowVecColsElemDivide() ... ";

        _genRowVec.cols(_elemIndRange.a, _elemIndRange.b) /= _genMat;

        save<double>("Row.colsElemDivide", _genRowVec);
        cout << "done." << endl;
      }

      void expectedRowVecSubvecEqual() {
        if(_elemIndRange.whole) {
          return;
        }

        if (!_genRowVec.in_range(_elemIndRange)) {
          return;

        }

        if(_genMat.n_rows != _genRowVec.n_rows || _genMat.n_cols != _elemIndRange.b - _elemIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedRowVecSubvecEqual() ... ";

        _genRowVec.subvec(_elemIndRange.a, _elemIndRange.b) = _genMat;

        save<double>("Row.subvecEqual", _genRowVec);
        cout << "done." << endl;
      }

      void expectedRowVecSubvecPlus() {
        if(_elemIndRange.whole) {
          return;
        }

        if (!_genRowVec.in_range(_elemIndRange)) {
          return;

        }

        if(_genMat.n_rows != _genRowVec.n_rows || _genMat.n_cols != _elemIndRange.b - _elemIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedRowVecSubvecPlus() ... ";

        _genRowVec.subvec(_elemIndRange.a, _elemIndRange.b) += _genMat;

        save<double>("Row.subvecPlus", _genRowVec);
        cout << "done." << endl;
      }

      void expectedRowVecSubvecMinus() {
        if(_elemIndRange.whole) {
          return;
        }

        if (!_genRowVec.in_range(_elemIndRange)) {
          return;

        }

        if(_genMat.n_rows != _genRowVec.n_rows || _genMat.n_cols != _elemIndRange.b - _elemIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedRowVecSubvecMinus() ... ";

        _genRowVec.subvec(_elemIndRange.a, _elemIndRange.b) -= _genMat;

        save<double>("Row.subvecMinus", _genRowVec);
        cout << "done." << endl;
      }

      void expectedRowVecSubvecElemTimes() {
        if(_elemIndRange.whole) {
          return;
        }

        if (!_genRowVec.in_range(_elemIndRange)) {
          return;

        }

        if(_genMat.n_rows != _genRowVec.n_rows || _genMat.n_cols != _elemIndRange.b - _elemIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedRowVecSubvecElemTimes() ... ";

        _genRowVec.subvec(_elemIndRange.a, _elemIndRange.b) %= _genMat;

        save<double>("Row.subvecElemTimes", _genRowVec);
        cout << "done." << endl;
      }

      void expectedRowVecSubvecElemDivide() {
        if(_elemIndRange.whole) {
          return;
        }

        if (!_genRowVec.in_range(_elemIndRange)) {
          return;

        }

        if(_genMat.n_rows != _genRowVec.n_rows || _genMat.n_cols != _elemIndRange.b - _elemIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedRowVecSubvecElemDivide() ... ";

        _genRowVec.subvec(_elemIndRange.a, _elemIndRange.b) /= _genMat;

        save<double>("Row.subvecElemDivide", _genRowVec);
        cout << "done." << endl;
      }

  };
}
