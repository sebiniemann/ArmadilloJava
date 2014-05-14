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
  class ExpectedInPlaceGenMatRowIndColIndRangeGenMat : public Expected {
    public:
      ExpectedInPlaceGenMatRowIndColIndRangeGenMat() {
        cout << "Compute ExpectedInPlaceGenMatRowIndColIndRangeGenMat(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::GenMat,
            InputClass::RowInd,
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
                  _rowInd = *static_cast<int*>(value.second);
                  break;
                case 2:
                  _fileSuffix += "," + value.first;
                  _colIndRange = *static_cast<span*>(value.second);
                  break;
                case 3:
                  _fileSuffix += "," + value.first;
                  _genMatB = *static_cast<Mat<double>*>(value.second);
                  break;
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            _copyOfGenMatA = _genMatA;
            copyOfRowInd = _rowInd;
            _copyOfColIndRange = _colIndRange;
            _copyOfGenMatB = _genMatB;

            expectedMatRowEqual();

            _genMatA = _copyOfGenMatA;
            _rowInd = copyOfRowInd;
            _colIndRange = _copyOfColIndRange;
            _genMatB = _copyOfGenMatB;
            expectedMatRowPlus();

            _genMatA = _copyOfGenMatA;
            _rowInd = copyOfRowInd;
            _colIndRange = _copyOfColIndRange;
            _genMatB = _copyOfGenMatB;
            expectedMatRowMinus();

            _genMatA = _copyOfGenMatA;
            _rowInd = copyOfRowInd;
            _colIndRange = _copyOfColIndRange;
            _genMatB = _copyOfGenMatB;
            expectedMatRowElemTimes();

            _genMatA = _copyOfGenMatA;
            _rowInd = copyOfRowInd;
            _colIndRange = _copyOfColIndRange;
            _genMatB = _copyOfGenMatB;
            expectedMatRowElemDivide();
          }

          cout << "done." << endl;
        }

    protected:
      Mat<double> _genMatA;
      Mat<double> _copyOfGenMatA;

      int _rowInd;
      int copyOfRowInd;

      span _colIndRange;
      span _copyOfColIndRange;

      Mat<double> _genMatB;
      Mat<double> _copyOfGenMatB;

      void expectedMatRowEqual() {
        if(_rowInd >= _genMatA.n_rows) {
          return;
        }

        if(!_genMatB.is_rowvec()) {
          return;
        }

        if(!_colIndRange.whole) {
          if(_colIndRange.b >= _genMatA.n_cols) {
            return;
          }

          if(_genMatB.n_cols != _colIndRange.b - _colIndRange.a + 1) {
            return;
          }
        } else {
          if(_genMatB.n_cols != _genMatA.n_cols) {
            return;
          }
        }

        cout << "- Compute expectedMatRowEqual() ... ";

        _genMatA(_rowInd, _colIndRange) = _genMatB;
        save<double>("Mat.rowEqual", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatRowPlus() {
        if(_rowInd >= _genMatA.n_rows) {
          return;
        }

        if(!_genMatB.is_rowvec()) {
          return;
        }

        if(!_colIndRange.whole) {
          if(_colIndRange.b >= _genMatA.n_cols) {
            return;
          }

          if(_genMatB.n_cols != _colIndRange.b - _colIndRange.a + 1) {
            return;
          }
        } else {
          if(_genMatB.n_cols != _genMatA.n_cols) {
            return;
          }
        }

        cout << "- Compute expectedMatRowPlus() ... ";

        _genMatA(_rowInd, _colIndRange) += _genMatB;
        save<double>("Mat.rowPlus", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatRowMinus() {
        if(_rowInd >= _genMatA.n_rows) {
          return;
        }

        if(!_genMatB.is_rowvec()) {
          return;
        }

        if(!_colIndRange.whole) {
          if(_colIndRange.b >= _genMatA.n_cols) {
            return;
          }

          if(_genMatB.n_cols != _colIndRange.b - _colIndRange.a + 1) {
            return;
          }
        } else {
          if(_genMatB.n_cols != _genMatA.n_cols) {
            return;
          }
        }

        cout << "- Compute expectedMatRowMinus() ... ";

        _genMatA(_rowInd, _colIndRange) -= _genMatB;
        save<double>("Mat.rowMinus", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatRowElemTimes() {
        if(_rowInd >= _genMatA.n_rows) {
          return;
        }

        if(!_genMatB.is_rowvec()) {
          return;
        }

        if(!_colIndRange.whole) {
          if(_colIndRange.b >= _genMatA.n_cols) {
            return;
          }

          if(_genMatB.n_cols != _colIndRange.b - _colIndRange.a + 1) {
            return;
          }
        } else {
          if(_genMatB.n_cols != _genMatA.n_cols) {
            return;
          }
        }

        cout << "- Compute expectedMatRowElemTimes() ... ";

        _genMatA(_rowInd, _colIndRange) %= _genMatB;
        save<double>("Mat.rowElemTimes", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatRowElemDivide() {
        if(_rowInd >= _genMatA.n_rows) {
          return;
        }

        if(!_genMatB.is_rowvec()) {
          return;
        }

        if(!_colIndRange.whole) {
          if(_colIndRange.b >= _genMatA.n_cols) {
            return;
          }

          if(_genMatB.n_cols != _colIndRange.b - _colIndRange.a + 1) {
            return;
          }
        } else {
          if(_genMatB.n_cols != _genMatA.n_cols) {
            return;
          }
        }

        cout << "- Compute expectedMatRowElemDivide() ... ";

        _genMatA(_rowInd, _colIndRange) /= _genMatB;
        save<double>("Mat.rowElemDivide", _genMatA);

        cout << "done." << endl;
      }
  };
}
