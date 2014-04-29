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
  class ExpectedInPlaceGenMatRowIndRangeGenMat : public Expected {
    public:
      ExpectedInPlaceGenMatRowIndRangeGenMat() {
        cout << "Compute ExpectedInPlaceGenMatRowIndRangeGenMat(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::GenMat,
            InputClass::RowIndRange,
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
                  _rowIndRange = *static_cast<span*>(value.second);
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
            _copyOfRowIndRange = _rowIndRange;
            _copyOfGenMatB = _genMatB;

            expectedMatRowsEqual();

            _genMatA = _copyOfGenMatA;
            _rowIndRange = _copyOfRowIndRange;
            _genMatB = _copyOfGenMatB;
            expectedMatRowsPlus();

            _genMatA = _copyOfGenMatA;
            _rowIndRange = _copyOfRowIndRange;
            _genMatB = _copyOfGenMatB;
            expectedMatRowsMinus();

            _genMatA = _copyOfGenMatA;
            _rowIndRange = _copyOfRowIndRange;
            _genMatB = _copyOfGenMatB;
            expectedMatRowsElemTimes();

            _genMatA = _copyOfGenMatA;
            _rowIndRange = _copyOfRowIndRange;
            _genMatB = _copyOfGenMatB;
            expectedMatRowsElemDivide();
          }

          cout << "done." << endl;
        }

    protected:
      Mat<double> _genMatA;
      Mat<double> _copyOfGenMatA;

      span _rowIndRange;
      span _copyOfRowIndRange;

      Mat<double> _genMatB;
      Mat<double> _copyOfGenMatB;

      void expectedMatRowsEqual() {
        if(_rowIndRange.b >= _genMatA.n_rows) {
          return;
        }

        if(_genMatB.n_cols != _genMatA.n_cols) {
          return;
        }

        if(_genMatB.n_rows != _rowIndRange.b - _rowIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedMatRowsEqual() ... ";

        _genMatA.rows(_rowIndRange.a, _rowIndRange.b) = _genMatB;
        save<double>("Mat.rowsEqual", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatRowsPlus() {
        if(_rowIndRange.b >= _genMatA.n_rows) {
          return;
        }

        if(_genMatB.n_cols != _genMatA.n_cols) {
          return;
        }

        if(_genMatB.n_rows != _rowIndRange.b - _rowIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedMatRowsPlus() ... ";

        _genMatA.rows(_rowIndRange.a, _rowIndRange.b) += _genMatB;
        save<double>("Mat.rowsPlus", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatRowsMinus() {
        if(_rowIndRange.b >= _genMatA.n_rows) {
          return;
        }

        if(_genMatB.n_cols != _genMatA.n_cols) {
          return;
        }

        if(_genMatB.n_rows != _rowIndRange.b - _rowIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedMatRowsMinus() ... ";

        _genMatA.rows(_rowIndRange.a, _rowIndRange.b) -= _genMatB;
        save<double>("Mat.rowsMinus", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatRowsElemTimes() {
        if(_rowIndRange.b >= _genMatA.n_rows) {
          return;
        }

        if(_genMatB.n_cols != _genMatA.n_cols) {
          return;
        }

        if(_genMatB.n_rows != _rowIndRange.b - _rowIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedMatRowsElemTimes() ... ";

        _genMatA.rows(_rowIndRange.a, _rowIndRange.b) %= _genMatB;
        save<double>("Mat.rowsElemTimes", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatRowsElemDivide() {
        if(_rowIndRange.b >= _genMatA.n_rows) {
          return;
        }

        if(_genMatB.n_cols != _genMatA.n_cols) {
          return;
        }

        if(_genMatB.n_rows != _rowIndRange.b - _rowIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedMatRowsElemDivide() ... ";

        _genMatA.rows(_rowIndRange.a, _rowIndRange.b) /= _genMatB;
        save<double>("Mat.rowsElemDivide", _genMatA);

        cout << "done." << endl;
      }
  };
}
