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
  class ExpectedInPlaceGenMatRowIndColIndRangeGenColVec : public Expected {
    public:
      ExpectedInPlaceGenMatRowIndColIndRangeGenColVec() {
        cout << "Compute ExpectedInPlaceGenMatRowIndColIndRangeGenColVec(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::GenMat,
            InputClass::RowInd,
            InputClass::ColIndRange,
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
                  _rowInd = *static_cast<int*>(value.second);
                  break;
                case 2:
                  _fileSuffix += "," + value.first;
                  _colIndRange = *static_cast<span*>(value.second);
                  break;
                case 3:
                  _fileSuffix += "," + value.first;
                  _genColVec = *static_cast<Col<double>*>(value.second);
                  break;
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            _copyOfGenMat = _genMat;
            copyOfRowInd = _rowInd;
            _copyOfColIndRange = _colIndRange;
            _copyOfGenColVec = _genColVec;

            expectedMatRowEqual();

            _genMat = _copyOfGenMat;
            _rowInd = copyOfRowInd;
            _colIndRange = _copyOfColIndRange;
            _genColVec = _copyOfGenColVec;
            expectedMatRowPlus();

            _genMat = _copyOfGenMat;
            _rowInd = copyOfRowInd;
            _colIndRange = _copyOfColIndRange;
            _genColVec = _copyOfGenColVec;
            expectedMatRowMinus();

            _genMat = _copyOfGenMat;
            _rowInd = copyOfRowInd;
            _colIndRange = _copyOfColIndRange;
            _genColVec = _copyOfGenColVec;
            expectedMatRowElemTimes();

            _genMat = _copyOfGenMat;
            _rowInd = copyOfRowInd;
            _colIndRange = _copyOfColIndRange;
            _genColVec = _copyOfGenColVec;
            expectedMatRowElemDivide();
          }

          cout << "done." << endl;
        }

    protected:
      Mat<double> _genMat;
      Mat<double> _copyOfGenMat;

      int _rowInd;
      int copyOfRowInd;

      span _colIndRange;
      span _copyOfColIndRange;

      Col<double> _genColVec;
      Col<double> _copyOfGenColVec;

      void expectedMatRowEqual() {
        if(_rowInd >= _genMat.n_rows) {
          return;
        }

        if(!_genColVec.is_rowvec()) {
          return;
        }

        if(!_colIndRange.whole) {
          if(_colIndRange.b >= _genMat.n_cols) {
            return;
          }

          if(_colIndRange.b - _colIndRange.a + 1 != 1) {
            return;
          }
        } else {
          if(_genMat.n_cols != 1) {
            return;
          }
        }

        cout << "- Compute expectedMatRowEqual() ... ";

        _genMat(_rowInd, _colIndRange) = _genColVec;
        save<double>("Mat.rowEqual", _genMat);

        cout << "done." << endl;
      }

      void expectedMatRowPlus() {
        if(_rowInd >= _genMat.n_rows) {
          return;
        }

        if(!_genColVec.is_rowvec()) {
          return;
        }

        if(!_colIndRange.whole) {
          if(_colIndRange.b >= _genMat.n_cols) {
            return;
          }

          if(_colIndRange.b - _colIndRange.a + 1 != 1) {
            return;
          }
        } else {
          if(_genMat.n_cols != 1) {
            return;
          }
        }

        cout << "- Compute expectedMatRowPlus() ... ";

        _genMat(_rowInd, _colIndRange) += _genColVec;
        save<double>("Mat.rowPlus", _genMat);

        cout << "done." << endl;
      }

      void expectedMatRowMinus() {
        if(_rowInd >= _genMat.n_rows) {
          return;
        }

        if(!_genColVec.is_rowvec()) {
          return;
        }

        if(!_colIndRange.whole) {
          if(_colIndRange.b >= _genMat.n_cols) {
            return;
          }

          if(_colIndRange.b - _colIndRange.a + 1 != 1) {
            return;
          }
        } else {
          if(_genMat.n_cols != 1) {
            return;
          }
        }

        cout << "- Compute expectedMatRowMinus() ... ";

        _genMat(_rowInd, _colIndRange) -= _genColVec;
        save<double>("Mat.rowMinus", _genMat);

        cout << "done." << endl;
      }

      void expectedMatRowElemTimes() {
        if(_rowInd >= _genMat.n_rows) {
          return;
        }

        if(!_genColVec.is_rowvec()) {
          return;
        }

        if(!_colIndRange.whole) {
          if(_colIndRange.b >= _genMat.n_cols) {
            return;
          }

          if(_colIndRange.b - _colIndRange.a + 1 != 1) {
            return;
          }
        } else {
          if(_genMat.n_cols != 1) {
            return;
          }
        }

        cout << "- Compute expectedMatRowElemTimes() ... ";

        _genMat(_rowInd, _colIndRange) %= _genColVec;
        save<double>("Mat.rowElemTimes", _genMat);

        cout << "done." << endl;
      }

      void expectedMatRowElemDivide() {
        if(_rowInd >= _genMat.n_rows) {
          return;
        }

        if(!_genColVec.is_rowvec()) {
          return;
        }

        if(!_colIndRange.whole) {
          if(_colIndRange.b >= _genMat.n_cols) {
            return;
          }

          if(_colIndRange.b - _colIndRange.a + 1 != 1) {
            return;
          }
        } else {
          if(_genMat.n_cols != 1) {
            return;
          }
        }

        cout << "- Compute expectedMatRowElemDivide() ... ";

        _genMat(_rowInd, _colIndRange) /= _genColVec;
        save<double>("Mat.rowElemDivide", _genMat);

        cout << "done." << endl;
      }
  };
}
