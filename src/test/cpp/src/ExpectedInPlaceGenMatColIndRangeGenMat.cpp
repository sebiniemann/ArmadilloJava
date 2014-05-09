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
using arma::span;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedInPlaceGenMatColIndRangeGenMat : public Expected {
    public:
      ExpectedInPlaceGenMatColIndRangeGenMat() {
        cout << "Compute ExpectedInPlaceGenMatColIndRangeGenMat(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::GenMat,
            InputClass::ColIndRange,
            InputClass::GenMat
          });

          for (vector<pair<string, void*>> input : inputs) {
            _fileSuffix = "";

            int n = 0;
            for (pair<string, void*> value : input) {
              switch (n) {
                case 0:
                  _fileSuffix += value.first;
                  _genMatA = *static_cast<Mat<double>*>(value.second);
                  break;
                case 1:
                  _fileSuffix += "," + value.first;
                  _colIndRange = *static_cast<span*>(value.second);
                  break;
                case 2:
                  _fileSuffix += "," + value.first;
                  _genMatB = *static_cast<Mat<double>*>(value.second);
                  break;
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            _copyOfGenMatA = _genMatA;
            _copyOfColIndRange = _colIndRange;
            _copyOfGenMatB = _genMatB;

            expectedMatColsEqual();

            _genMatA = _copyOfGenMatA;
            _colIndRange = _copyOfColIndRange;
            _genMatB = _copyOfGenMatB;
            expectedMatColsPlus();

            _genMatA = _copyOfGenMatA;
            _colIndRange = _copyOfColIndRange;
            _genMatB = _copyOfGenMatB;
            expectedMatColsMinus();

            _genMatA = _copyOfGenMatA;
            _colIndRange = _copyOfColIndRange;
            _genMatB = _copyOfGenMatB;
            expectedMatColsElemTimes();

            _genMatA = _copyOfGenMatA;
            _colIndRange = _copyOfColIndRange;
            _genMatB = _copyOfGenMatB;
            expectedMatColsElemDivide();
          }

          cout << "done." << endl;
        }

    protected:
      Mat<double> _genMatA;
      Mat<double> _copyOfGenMatA;

      span _colIndRange;
      span _copyOfColIndRange;

      Mat<double> _genMatB;
      Mat<double> _copyOfGenMatB;

      void expectedMatColsEqual() {
        if(_colIndRange.whole) {
          return;
        }

        if(_colIndRange.b >= _genMatA.n_cols) {
          return;
        }

        if(_genMatB.n_rows != _genMatA.n_rows) {
          return;
        }

        if(_genMatB.n_cols != _colIndRange.b - _colIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedMatColsEqual() ... ";

        _genMatA.cols(_colIndRange.a, _colIndRange.b) = _genMatB;
        save<double>("Mat.colsEqual", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatColsPlus() {
        if(_colIndRange.whole) {
          return;
        }

        if(_colIndRange.b >= _genMatA.n_cols) {
          return;
        }

        if(_genMatB.n_rows != _genMatA.n_rows) {
          return;
        }

        if(_genMatB.n_cols != _colIndRange.b - _colIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedMatColsPlus() ... ";

        _genMatA.cols(_colIndRange.a, _colIndRange.b) += _genMatB;
        save<double>("Mat.colsPlus", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatColsMinus() {
        if(_colIndRange.whole) {
          return;
        }

        if(_colIndRange.b >= _genMatA.n_cols) {
          return;
        }

        if(_genMatB.n_rows != _genMatA.n_rows) {
          return;
        }

        if(_genMatB.n_cols != _colIndRange.b - _colIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedMatColsMinus() ... ";

        _genMatA.cols(_colIndRange.a, _colIndRange.b) -= _genMatB;
        save<double>("Mat.colsMinus", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatColsElemTimes() {
        if(_colIndRange.whole) {
          return;
        }

        if(_colIndRange.b >= _genMatA.n_cols) {
          return;
        }

        if(_genMatB.n_rows != _genMatA.n_rows) {
          return;
        }

        if(_genMatB.n_cols != _colIndRange.b - _colIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedMatColsElemTimes() ... ";

        _genMatA.cols(_colIndRange.a, _colIndRange.b) %= _genMatB;
        save<double>("Mat.colsElemTimes", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatColsElemDivide() {
        if(_colIndRange.whole) {
          return;
        }

        if(_colIndRange.b >= _genMatA.n_cols) {
          return;
        }

        if(_genMatB.n_rows != _genMatA.n_rows) {
          return;
        }

        if(_genMatB.n_cols != _colIndRange.b - _colIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedMatColsElemDivide() ... ";

        _genMatA.cols(_colIndRange.a, _colIndRange.b) /= _genMatB;
        save<double>("Mat.colsElemDivide", _genMatA);

        cout << "done." << endl;
      }
  };
}
