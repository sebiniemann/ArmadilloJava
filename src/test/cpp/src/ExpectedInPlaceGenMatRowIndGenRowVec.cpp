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
using arma::Row;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedInPlaceGenMatRowIndGenRowVec : public Expected {
    public:
      ExpectedInPlaceGenMatRowIndGenRowVec() {
        cout << "Compute ExpectedInPlaceGenMatRowIndGenRowVec(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::GenMat,
            InputClass::RowInd,
            InputClass::GenRowVec
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
                  _genRowVec = *static_cast<Row<double>*>(value.second);
                  break;
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            _copyOfGenMat = _genMat;
            _copyOfRowInd = _rowInd;
            _copyOfGenRowVec = _genRowVec;

            expectedMatRowEqual();

            _genMat = _copyOfGenMat;
            _rowInd = _copyOfRowInd;
            _genRowVec = _copyOfGenRowVec;
            expectedMatRowPlus();

            _genMat = _copyOfGenMat;
            _rowInd = _copyOfRowInd;
            _genRowVec = _copyOfGenRowVec;
            expectedMatRowMinus();

            _genMat = _copyOfGenMat;
            _rowInd = _copyOfRowInd;
            _genRowVec = _copyOfGenRowVec;
            expectedMatRowElemTimes();

            _genMat = _copyOfGenMat;
            _rowInd = _copyOfRowInd;
            _genRowVec = _copyOfGenRowVec;
            expectedMatRowElemDivide();
          }

          cout << "done." << endl;
        }

    protected:
      Mat<double> _genMat;
      Mat<double> _copyOfGenMat;

      int _rowInd;
      int _copyOfRowInd;

      Row<double> _genRowVec;
      Row<double> _copyOfGenRowVec;

      void expectedMatRowEqual() {
        if(_rowInd >= _genMat.n_rows) {
          return;
        }

        if(_genMat.n_cols != _genRowVec.n_cols) {
          return;
        }

        cout << "- Compute expectedMatRowEqual() ... ";

        _genMat.row(_rowInd) = _genRowVec;
        save<double>("Mat.rowEqual", _genMat);

        cout << "done." << endl;
      }

      void expectedMatRowPlus() {
        if(_rowInd >= _genMat.n_rows) {
          return;
        }

        if(_genMat.n_cols != _genRowVec.n_cols) {
          return;
        }

        cout << "- Compute expectedMatRowPlus() ... ";

        _genMat.row(_rowInd) += _genRowVec;
        save<double>("Mat.rowPlus", _genMat);

        cout << "done." << endl;
      }

      void expectedMatRowMinus() {
        if(_rowInd >= _genMat.n_rows) {
          return;
        }

        if(_genMat.n_cols != _genRowVec.n_cols) {
          return;
        }

        cout << "- Compute expectedMatRowMinus() ... ";

        _genMat.row(_rowInd) -= _genRowVec;
        save<double>("Mat.rowMinus", _genMat);

        cout << "done." << endl;
      }

      void expectedMatRowElemTimes() {
        if(_rowInd >= _genMat.n_rows) {
          return;
        }

        if(_genMat.n_cols != _genRowVec.n_cols) {
          return;
        }

        cout << "- Compute expectedMatRowElemTimes() ... ";

        _genMat.row(_rowInd) %= _genRowVec;
        save<double>("Mat.rowElemTimes", _genMat);

        cout << "done." << endl;
      }

      void expectedMatRowElemDivide() {
        if(_rowInd >= _genMat.n_rows) {
          return;
        }

        if(_genMat.n_cols != _genRowVec.n_cols) {
          return;
        }

        cout << "- Compute expectedMatRowElemDivide() ... ";

        _genMat.row(_rowInd) /= _genRowVec;
        save<double>("Mat.rowElemDivide", _genMat);

        cout << "done." << endl;
      }
  };
}
