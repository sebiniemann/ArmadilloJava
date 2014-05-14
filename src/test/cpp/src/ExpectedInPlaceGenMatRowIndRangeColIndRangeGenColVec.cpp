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
  class ExpectedInPlaceGenMatRowIndRangeColIndRangeGenColVec : public Expected {
    public:
      ExpectedInPlaceGenMatRowIndRangeColIndRangeGenColVec() {
        cout << "Compute ExpectedInPlaceGenMatRowIndRangeColIndRangeGenColVec(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::GenMat,
            InputClass::RowIndRange,
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
                  _rowIndRange = *static_cast<span*>(value.second);
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
            _copyOfRowIndRange = _rowIndRange;
            _copyOfColIndRange = _colIndRange;
            _copyOfGenColVec = _genColVec;

            expectedMatSubmatEqual();

            _genMat = _copyOfGenMat;
            _rowIndRange = _copyOfRowIndRange;
            _colIndRange = _copyOfColIndRange;
            _genColVec = _copyOfGenColVec;
            expectedMatSubmatPlus();

            _genMat = _copyOfGenMat;
            _rowIndRange = _copyOfRowIndRange;
            _colIndRange = _copyOfColIndRange;
            _genColVec = _copyOfGenColVec;
            expectedMatSubmatMinus();

            _genMat = _copyOfGenMat;
            _rowIndRange = _copyOfRowIndRange;
            _colIndRange = _copyOfColIndRange;
            _genColVec = _copyOfGenColVec;
            expectedMatSubmatElemTimes();

            _genMat = _copyOfGenMat;
            _rowIndRange = _copyOfRowIndRange;
            _colIndRange = _copyOfColIndRange;
            _genColVec = _copyOfGenColVec;
            expectedMatSubmatElemDivide();
          }

          cout << "done." << endl;
        }

    protected:
      Mat<double> _genMat;
      Mat<double> _copyOfGenMat;

      span _rowIndRange;
      span _copyOfRowIndRange;

      span _colIndRange;
      span _copyOfColIndRange;

      Col<double> _genColVec;
      Col<double> _copyOfGenColVec;

      void expectedMatSubmatEqual() {
        if(!_genMat.in_range(_rowIndRange, _colIndRange)) {
          return;
        }

        if(!_rowIndRange.whole) {
          if(_genColVec.n_rows != _rowIndRange.b - _rowIndRange.a + 1) {
            return;
          }
        } else {
          if(_genColVec.n_rows != _genMat.n_rows) {
            return;
          }
        }

        if(!_colIndRange.whole) {
          if(_genColVec.n_cols != _colIndRange.b - _colIndRange.a + 1) {
            return;
          }
        } else {
          if(_genColVec.n_cols != _genMat.n_cols) {
            return;
          }
        }

        cout << "- Compute expectedMatSubmatEqual() ... ";

        _genMat.submat(_rowIndRange, _colIndRange) = _genColVec;
        save<double>("Mat.submatEqual", _genMat);

        cout << "done." << endl;
      }

      void expectedMatSubmatPlus() {
        if(!_genMat.in_range(_rowIndRange, _colIndRange)) {
          return;
        }

        if(!_rowIndRange.whole) {
          if(_genColVec.n_rows != _rowIndRange.b - _rowIndRange.a + 1) {
            return;
          }
        } else {
          if(_genColVec.n_rows != _genMat.n_rows) {
            return;
          }
        }

        if(!_colIndRange.whole) {
          if(_genColVec.n_cols != _colIndRange.b - _colIndRange.a + 1) {
            return;
          }
        } else {
          if(_genColVec.n_cols != _genMat.n_cols) {
            return;
          }
        }

        cout << "- Compute expectedMatSubmatPlus() ... ";

        _genMat.submat(_rowIndRange, _colIndRange) += _genColVec;
        save<double>("Mat.submatPlus", _genMat);

        cout << "done." << endl;
      }

      void expectedMatSubmatMinus() {
        if(!_genMat.in_range(_rowIndRange, _colIndRange)) {
          return;
        }

        if(!_rowIndRange.whole) {
          if(_genColVec.n_rows != _rowIndRange.b - _rowIndRange.a + 1) {
            return;
          }
        } else {
          if(_genColVec.n_rows != _genMat.n_rows) {
            return;
          }
        }

        if(!_colIndRange.whole) {
          if(_genColVec.n_cols != _colIndRange.b - _colIndRange.a + 1) {
            return;
          }
        } else {
          if(_genColVec.n_cols != _genMat.n_cols) {
            return;
          }
        }

        cout << "- Compute expectedMatSubmatMinus() ... ";

        _genMat.submat(_rowIndRange, _colIndRange) -= _genColVec;
        save<double>("Mat.submatMinus", _genMat);

        cout << "done." << endl;
      }

      void expectedMatSubmatElemTimes() {
        if(!_genMat.in_range(_rowIndRange, _colIndRange)) {
          return;
        }

        if(!_rowIndRange.whole) {
          if(_genColVec.n_rows != _rowIndRange.b - _rowIndRange.a + 1) {
            return;
          }
        } else {
          if(_genColVec.n_rows != _genMat.n_rows) {
            return;
          }
        }

        if(!_colIndRange.whole) {
          if(_genColVec.n_cols != _colIndRange.b - _colIndRange.a + 1) {
            return;
          }
        } else {
          if(_genColVec.n_cols != _genMat.n_cols) {
            return;
          }
        }

        cout << "- Compute expectedMatSubmatElemTimes() ... ";

        _genMat.submat(_rowIndRange, _colIndRange) %= _genColVec;
        save<double>("Mat.submatElemTimes", _genMat);

        cout << "done." << endl;
      }

      void expectedMatSubmatElemDivide() {
        if(!_genMat.in_range(_rowIndRange, _colIndRange)) {
          return;
        }

        if(!_rowIndRange.whole) {
          if(_genColVec.n_rows != _rowIndRange.b - _rowIndRange.a + 1) {
            return;
          }
        } else {
          if(_genColVec.n_rows != _genMat.n_rows) {
            return;
          }
        }

        if(!_colIndRange.whole) {
          if(_genColVec.n_cols != _colIndRange.b - _colIndRange.a + 1) {
            return;
          }
        } else {
          if(_genColVec.n_cols != _genMat.n_cols) {
            return;
          }
        }

        cout << "- Compute expectedMatSubmatElemDivide() ... ";

        _genMat.submat(_rowIndRange, _colIndRange) /= _genColVec;
        save<double>("Mat.submatElemDivide", _genMat);

        cout << "done." << endl;
      }
  };
}
