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
  class ExpectedInPlaceGenMatRowIndRangeColIndRangeGenMat : public Expected {
    public:
      ExpectedInPlaceGenMatRowIndRangeColIndRangeGenMat() {
        cout << "Compute ExpectedInPlaceGenMatRowIndRangeColIndRangeGenMat(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::GenMat,
            InputClass::RowIndRange,
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
                  _rowIndRange = *static_cast<span*>(value.second);
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
            _copyOfRowIndRange = _rowIndRange;
            _copyOfIndRange = _colIndRange;
            _copyOfGenMatB = _genMatB;

            expectedMatSubmatEqual();

            _genMatA = _copyOfGenMatA;
            _rowIndRange = _copyOfRowIndRange;
            _colIndRange = _copyOfIndRange;
            _genMatB = _copyOfGenMatB;
            expectedMatSubmatPlus();

            _genMatA = _copyOfGenMatA;
            _rowIndRange = _copyOfRowIndRange;
            _colIndRange = _copyOfIndRange;
            _genMatB = _copyOfGenMatB;
            expectedMatSubmatMinus();

            _genMatA = _copyOfGenMatA;
            _rowIndRange = _copyOfRowIndRange;
            _colIndRange = _copyOfIndRange;
            _genMatB = _copyOfGenMatB;
            expectedMatSubmatElemTimes();

            _genMatA = _copyOfGenMatA;
            _rowIndRange = _copyOfRowIndRange;
            _colIndRange = _copyOfIndRange;
            _genMatB = _copyOfGenMatB;
            expectedMatSubmatElemDivide();
          }

          cout << "done." << endl;
        }

    protected:
      Mat<double> _genMatA;
      Mat<double> _copyOfGenMatA;

      span _rowIndRange;
      span _copyOfRowIndRange;

      span _colIndRange;
      span _copyOfIndRange;

      Mat<double> _genMatB;
      Mat<double> _copyOfGenMatB;

      void expectedMatSubmatEqual() {
        if(!_genMatA.in_range(_rowIndRange, _colIndRange)) {
          return;
        }

        if(!_rowIndRange.whole) {
          if(_genMatB.n_rows != _rowIndRange.b - _rowIndRange.a + 1) {
            return;
          }
        } else {
          if(_genMatB.n_rows != _genMatA.n_rows) {
            return;
          }
        }

        if(!_colIndRange.whole) {
          if(_genMatB.n_cols != _colIndRange.b - _colIndRange.a + 1) {
            return;
          }
        } else {
          if(_genMatB.n_cols != _genMatA.n_cols) {
            return;
          }
        }

        cout << "- Compute expectedMatSubmatEqual() ... ";

        _genMatA.submat(_rowIndRange, _colIndRange) = _genMatB;
        save<double>("Mat.submatEqual", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatSubmatPlus() {
        if(!_genMatA.in_range(_rowIndRange, _colIndRange)) {
          return;
        }

        if(!_rowIndRange.whole) {
          if(_genMatB.n_rows != _rowIndRange.b - _rowIndRange.a + 1) {
            return;
          }
        } else {
          if(_genMatB.n_rows != _genMatA.n_rows) {
            return;
          }
        }

        if(!_colIndRange.whole) {
          if(_genMatB.n_cols != _colIndRange.b - _colIndRange.a + 1) {
            return;
          }
        } else {
          if(_genMatB.n_cols != _genMatA.n_cols) {
            return;
          }
        }

        cout << "- Compute expectedMatSubmatPlus() ... ";

        _genMatA.submat(_rowIndRange, _colIndRange) += _genMatB;
        save<double>("Mat.submatPlus", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatSubmatMinus() {
        if(!_genMatA.in_range(_rowIndRange, _colIndRange)) {
          return;
        }

        if(!_rowIndRange.whole) {
          if(_genMatB.n_rows != _rowIndRange.b - _rowIndRange.a + 1) {
            return;
          }
        } else {
          if(_genMatB.n_rows != _genMatA.n_rows) {
            return;
          }
        }

        if(!_colIndRange.whole) {
          if(_genMatB.n_cols != _colIndRange.b - _colIndRange.a + 1) {
            return;
          }
        } else {
          if(_genMatB.n_cols != _genMatA.n_cols) {
            return;
          }
        }

        cout << "- Compute expectedMatSubmatMinus() ... ";

        _genMatA.submat(_rowIndRange, _colIndRange) -= _genMatB;
        save<double>("Mat.submatMinus", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatSubmatElemTimes() {
        if(!_genMatA.in_range(_rowIndRange, _colIndRange)) {
          return;
        }

        if(!_rowIndRange.whole) {
          if(_genMatB.n_rows != _rowIndRange.b - _rowIndRange.a + 1) {
            return;
          }
        } else {
          if(_genMatB.n_rows != _genMatA.n_rows) {
            return;
          }
        }

        if(!_colIndRange.whole) {
          if(_genMatB.n_cols != _colIndRange.b - _colIndRange.a + 1) {
            return;
          }
        } else {
          if(_genMatB.n_cols != _genMatA.n_cols) {
            return;
          }
        }

        cout << "- Compute expectedMatSubmatElemTimes() ... ";

        _genMatA.submat(_rowIndRange, _colIndRange) %= _genMatB;
        save<double>("Mat.submatElemTimes", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatSubmatElemDivide() {
        if(!_genMatA.in_range(_rowIndRange, _colIndRange)) {
          return;
        }

        if(!_rowIndRange.whole) {
          if(_genMatB.n_rows != _rowIndRange.b - _rowIndRange.a + 1) {
            return;
          }
        } else {
          if(_genMatB.n_rows != _genMatA.n_rows) {
            return;
          }
        }

        if(!_colIndRange.whole) {
          if(_genMatB.n_cols != _colIndRange.b - _colIndRange.a + 1) {
            return;
          }
        } else {
          if(_genMatB.n_cols != _genMatA.n_cols) {
            return;
          }
        }

        cout << "- Compute expectedMatSubmatElemDivide() ... ";

        _genMatA.submat(_rowIndRange, _colIndRange) /= _genMatB;
        save<double>("Mat.submatElemDivide", _genMatA);

        cout << "done." << endl;
      }
  };
}
