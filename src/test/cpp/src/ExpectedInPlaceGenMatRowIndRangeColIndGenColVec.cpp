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
  class ExpectedInPlaceGenMatRowIndRangeColIndGenColVec: public Expected {
    public:
      ExpectedInPlaceGenMatRowIndRangeColIndGenColVec() {
        cout << "Compute ExpectedInPlaceGenMatRowIndRangeColIndGenColVec(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters( {
            InputClass::GenMat,
            InputClass::RowIndRange,
            InputClass::ColInd,
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
                _colInd = *static_cast<int*>(value.second);
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
          copyOfRowIndRange = _rowIndRange;
          _copyOfColInd = _colInd;
          _copyOfGenColVec = _genColVec;

          expectedMatColEqual();

          _genMat = _copyOfGenMat;
          _rowIndRange = copyOfRowIndRange;
          _colInd = _copyOfColInd;
          _genColVec = _copyOfGenColVec;
          expectedMatColPlus();

          _genMat = _copyOfGenMat;
          _rowIndRange = copyOfRowIndRange;
          _colInd = _copyOfColInd;
          _genColVec = _copyOfGenColVec;
          expectedMatColMinus();

          _genMat = _copyOfGenMat;
          _rowIndRange = copyOfRowIndRange;
          _colInd = _copyOfColInd;
          _genColVec = _copyOfGenColVec;
          expectedMatColElemTimes();

          _genMat = _copyOfGenMat;
          _rowIndRange = copyOfRowIndRange;
          _colInd = _copyOfColInd;
          _genColVec = _copyOfGenColVec;
          expectedMatColElemDivide();
        }

        cout << "done." << endl;
      }

    protected:
      Mat<double> _genMat;
      Mat<double> _copyOfGenMat;

      span _rowIndRange;
      span copyOfRowIndRange;

      int _colInd;
      int _copyOfColInd;

      Col<double> _genColVec;
      Col<double> _copyOfGenColVec;

      void expectedMatColEqual() {
        if (_colInd >= _genMat.n_cols) {
          return;
        }

        if (!_rowIndRange.whole) {
          if (_rowIndRange.b >= _genMat.n_rows) {
            return;
          }

          if (_genColVec.n_rows != _rowIndRange.b - _rowIndRange.a + 1) {
            return;
          }
        } else {
          if (_genColVec.n_rows != _genMat.n_rows) {
            return;
          }
        }

        cout << "- Compute expectedMatColEqual() ... ";

        _genMat(_rowIndRange, _colInd) = _genColVec;
        save<double>("Mat.colEqual", _genMat);

        cout << "done." << endl;
      }

      void expectedMatColPlus() {
        if (_colInd >= _genMat.n_cols) {
          return;
        }

        if (!_rowIndRange.whole) {
          if (_rowIndRange.b >= _genMat.n_rows) {
            return;
          }

          if (_genColVec.n_rows != _rowIndRange.b - _rowIndRange.a + 1) {
            return;
          }
        } else {
          if (_genColVec.n_rows != _genMat.n_rows) {
            return;
          }
        }

        cout << "- Compute expectedMatColPlus() ... ";

        _genMat(_rowIndRange, _colInd) += _genColVec;
        save<double>("Mat.colPlus", _genMat);

        cout << "done." << endl;
      }

      void expectedMatColMinus() {
        if (_colInd >= _genMat.n_cols) {
          return;
        }

        if (!_rowIndRange.whole) {
          if (_rowIndRange.b >= _genMat.n_rows) {
            return;
          }

          if (_genColVec.n_rows != _rowIndRange.b - _rowIndRange.a + 1) {
            return;
          }
        } else {
          if (_genColVec.n_rows != _genMat.n_rows) {
            return;
          }
        }

        cout << "- Compute expectedMatColMinus() ... ";

        _genMat(_rowIndRange, _colInd) -= _genColVec;
        save<double>("Mat.colMinus", _genMat);

        cout << "done." << endl;
      }

      void expectedMatColElemTimes() {
        if (_colInd >= _genMat.n_cols) {
          return;
        }

        if (!_rowIndRange.whole) {
          if (_rowIndRange.b >= _genMat.n_rows) {
            return;
          }

          if (_genColVec.n_rows != _rowIndRange.b - _rowIndRange.a + 1) {
            return;
          }
        } else {
          if (_genColVec.n_rows != _genMat.n_rows) {
            return;
          }
        }

        cout << "- Compute expectedMatColElemTimes() ... ";

        _genMat(_rowIndRange, _colInd) %= _genColVec;
        save<double>("Mat.colElemTimes", _genMat);

        cout << "done." << endl;
      }

      void expectedMatColElemDivide() {
        if (_colInd >= _genMat.n_cols) {
          return;
        }

        if (!_rowIndRange.whole) {
          if (_rowIndRange.b >= _genMat.n_rows) {
            return;
          }

          if (_genColVec.n_rows != _rowIndRange.b - _rowIndRange.a + 1) {
            return;
          }
        } else {
          if (_genColVec.n_rows != _genMat.n_rows) {
            return;
          }
        }

        cout << "- Compute expectedMatColElemDivide() ... ";

        _genMat(_rowIndRange, _colInd) /= _genColVec;
        save<double>("Mat.colElemDivide", _genMat);

        cout << "done." << endl;
      }
  };
}
