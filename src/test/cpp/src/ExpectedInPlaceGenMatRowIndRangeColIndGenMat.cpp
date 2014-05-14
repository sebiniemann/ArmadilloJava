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
  class ExpectedInPlaceGenMatRowIndRangeColIndGenMat : public Expected {
    public:
      ExpectedInPlaceGenMatRowIndRangeColIndGenMat() {
        cout << "Compute ExpectedInPlaceGenMatRowIndRangeColIndGenMat(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::GenMat,
            InputClass::RowIndRange,
            InputClass::ColInd,
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
                  _colInd = *static_cast<int*>(value.second);
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
            copyOfRowIndRange = _rowIndRange;
            _copyOfColInd = _colInd;
            _copyOfGenMatB = _genMatB;

            expectedMatColEqual();

            _genMatA = _copyOfGenMatA;
            _rowIndRange = copyOfRowIndRange;
            _colInd = _copyOfColInd;
            _genMatB = _copyOfGenMatB;
            expectedMatColPlus();

            _genMatA = _copyOfGenMatA;
            _rowIndRange = copyOfRowIndRange;
            _colInd = _copyOfColInd;
            _genMatB = _copyOfGenMatB;
            expectedMatColMinus();

            _genMatA = _copyOfGenMatA;
            _rowIndRange = copyOfRowIndRange;
            _colInd = _copyOfColInd;
            _genMatB = _copyOfGenMatB;
            expectedMatColElemTimes();

            _genMatA = _copyOfGenMatA;
            _rowIndRange = copyOfRowIndRange;
            _colInd = _copyOfColInd;
            _genMatB = _copyOfGenMatB;
            expectedMatColElemDivide();
          }

          cout << "done." << endl;
        }

    protected:
      Mat<double> _genMatA;
      Mat<double> _copyOfGenMatA;

      span _rowIndRange;
      span copyOfRowIndRange;

      int _colInd;
      int _copyOfColInd;

      Mat<double> _genMatB;
      Mat<double> _copyOfGenMatB;

      void expectedMatColEqual() {
        if(_colInd >= _genMatA.n_cols) {
          return;
        }

        if(!_genMatB.is_colvec()) {
          return;
        }

        if(!_rowIndRange.whole) {
          if(_rowIndRange.b >= _genMatA.n_rows) {
            return;
          }

          if(_genMatB.n_rows != _rowIndRange.b - _rowIndRange.a + 1) {
            return;
          }
        } else {
          if(_genMatB.n_rows != _genMatA.n_rows) {
            return;
          }
        }

        cout << "- Compute expectedMatColEqual() ... ";

        _genMatA(_rowIndRange, _colInd) = _genMatB;
        save<double>("Mat.colEqual", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatColPlus() {
        if(_colInd >= _genMatA.n_cols) {
          return;
        }

        if(!_genMatB.is_colvec()) {
          return;
        }

        if(!_rowIndRange.whole) {
          if(_rowIndRange.b >= _genMatA.n_rows) {
            return;
          }

          if(_genMatB.n_rows != _rowIndRange.b - _rowIndRange.a + 1) {
            return;
          }
        } else {
          if(_genMatB.n_rows != _genMatA.n_rows) {
            return;
          }
        }

        cout << "- Compute expectedMatColPlus() ... ";

        _genMatA(_rowIndRange, _colInd) += _genMatB;
        save<double>("Mat.colPlus", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatColMinus() {
        if(_colInd >= _genMatA.n_cols) {
          return;
        }

        if(!_genMatB.is_colvec()) {
          return;
        }

        if(!_rowIndRange.whole) {
          if(_rowIndRange.b >= _genMatA.n_rows) {
            return;
          }

          if(_genMatB.n_rows != _rowIndRange.b - _rowIndRange.a + 1) {
            return;
          }
        } else {
          if(_genMatB.n_rows != _genMatA.n_rows) {
            return;
          }
        }

        cout << "- Compute expectedMatColMinus() ... ";

        _genMatA(_rowIndRange, _colInd) -= _genMatB;
        save<double>("Mat.colMinus", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatColElemTimes() {
        if(_colInd >= _genMatA.n_cols) {
          return;
        }

        if(!_genMatB.is_colvec()) {
          return;
        }

        if(!_rowIndRange.whole) {
          if(_rowIndRange.b >= _genMatA.n_rows) {
            return;
          }

          if(_genMatB.n_rows != _rowIndRange.b - _rowIndRange.a + 1) {
            return;
          }
        } else {
          if(_genMatB.n_rows != _genMatA.n_rows) {
            return;
          }
        }

        cout << "- Compute expectedMatColElemTimes() ... ";

        _genMatA(_rowIndRange, _colInd) %= _genMatB;
        save<double>("Mat.colElemTimes", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatColElemDivide() {
        if(_colInd >= _genMatA.n_cols) {
          return;
        }

        if(!_genMatB.is_colvec()) {
          return;
        }

        if(!_rowIndRange.whole) {
          if(_rowIndRange.b >= _genMatA.n_rows) {
            return;
          }

          if(_genMatB.n_rows != _rowIndRange.b - _rowIndRange.a + 1) {
            return;
          }
        } else {
          if(_genMatB.n_rows != _genMatA.n_rows) {
            return;
          }
        }

        cout << "- Compute expectedMatColElemDivide() ... ";

        _genMatA(_rowIndRange, _colInd) /= _genMatB;
        save<double>("Mat.colElemDivide", _genMatA);

        cout << "done." << endl;
      }
  };
}
