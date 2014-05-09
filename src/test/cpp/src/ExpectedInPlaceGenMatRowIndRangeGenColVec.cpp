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
using arma::Mat;
using arma::Col;
using arma::span;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedInPlaceGenMatRowIndRangeGenColVec : public Expected {
    public:
      ExpectedInPlaceGenMatRowIndRangeGenColVec() {
        cout << "Compute ExpectedInPlaceGenMatRowIndRangeGenColVec(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::GenMat,
            InputClass::RowIndRange,
            InputClass::GenColVec
          });

          for (vector<pair<string, void*>> input : inputs) {
            _fileSuffix = "";

            int n = 0;
            for (pair<string, void*> value : input) {
              switch (n) {
                case 0:
                  _fileSuffix += value.first;
                  _genMat = *static_cast<Mat<double>*>(value.second);
                  break;
                case 1:
                  _fileSuffix += "," + value.first;
                  _rowIndRange = *static_cast<span*>(value.second);
                  break;
                case 2:
                  _fileSuffix += "," + value.first;
                  _genColVec = *static_cast<Col<double>*>(value.second);
                  break;
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            _copyOfGenMat = _genMat;
            _copyOfRowIndRange = _rowIndRange;
            _copyOfGenColVec = _genColVec;

            expectedMatRowsEqual();

            _genMat = _copyOfGenMat;
            _rowIndRange = _copyOfRowIndRange;
            _genColVec = _copyOfGenColVec;
            expectedMatRowsPlus();

            _genMat = _copyOfGenMat;
            _rowIndRange = _copyOfRowIndRange;
            _genColVec = _copyOfGenColVec;
            expectedMatRowsMinus();

            _genMat = _copyOfGenMat;
            _rowIndRange = _copyOfRowIndRange;
            _genColVec = _copyOfGenColVec;
            expectedMatRowsElemTimes();

            _genMat = _copyOfGenMat;
            _rowIndRange = _copyOfRowIndRange;
            _genColVec = _copyOfGenColVec;
            expectedMatRowsElemDivide();
          }

          cout << "done." << endl;
        }

    protected:
      Mat<double> _genMat;
      Mat<double> _copyOfGenMat;

      span _rowIndRange;
      span _copyOfRowIndRange;

      Col<double> _genColVec;
      Col<double> _copyOfGenColVec;

      void expectedMatRowsEqual() {
        if(_rowIndRange.b >= _genMat.n_rows) {
          return;
        }

        if(_genMat.n_cols != 1) {
          return;
        }

        if(_genColVec.n_rows != _rowIndRange.b - _rowIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedMatRowsEqual() ... ";

        _genMat.rows(_rowIndRange.a, _rowIndRange.b) = _genColVec;
        save<double>("Mat.rowsEqual", _genMat);

        cout << "done." << endl;
      }

      void expectedMatRowsPlus() {
        if(_rowIndRange.b >= _genMat.n_rows) {
          return;
        }

        if(_genMat.n_cols != 1) {
          return;
        }

        if(_genColVec.n_rows != _rowIndRange.b - _rowIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedMatRowsPlus() ... ";

        _genMat.rows(_rowIndRange.a, _rowIndRange.b) += _genColVec;
        save<double>("Mat.rowsPlus", _genMat);

        cout << "done." << endl;
      }

      void expectedMatRowsMinus() {
        if(_rowIndRange.b >= _genMat.n_rows) {
          return;
        }

        if(_genMat.n_cols != 1) {
          return;
        }

        if(_genColVec.n_rows != _rowIndRange.b - _rowIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedMatRowsMinus() ... ";

        _genMat.rows(_rowIndRange.a, _rowIndRange.b) -= _genColVec;
        save<double>("Mat.rowsMinus", _genMat);

        cout << "done." << endl;
      }

      void expectedMatRowsElemTimes() {
        if(_rowIndRange.b >= _genMat.n_rows) {
          return;
        }

        if(_genMat.n_cols != 1) {
          return;
        }

        if(_genColVec.n_rows != _rowIndRange.b - _rowIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedMatRowsElemTimes() ... ";

        _genMat.rows(_rowIndRange.a, _rowIndRange.b) %= _genColVec;
        save<double>("Mat.rowsElemTimes", _genMat);

        cout << "done." << endl;
      }

      void expectedMatRowsElemDivide() {
        if(_rowIndRange.b >= _genMat.n_rows) {
          return;
        }

        if(_genMat.n_cols != 1) {
          return;
        }

        if(_genColVec.n_rows != _rowIndRange.b - _rowIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedMatRowsElemDivide() ... ";

        _genMat.rows(_rowIndRange.a, _rowIndRange.b) /= _genColVec;
        save<double>("Mat.rowsElemDivide", _genMat);

        cout << "done." << endl;
      }
  };
}
