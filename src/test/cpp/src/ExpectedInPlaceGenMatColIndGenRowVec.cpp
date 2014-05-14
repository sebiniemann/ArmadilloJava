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

#include <algorithm>
using std::min;

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
  class ExpectedInPlaceGenMatColIndGenRowVec : public Expected {
    public:
      ExpectedInPlaceGenMatColIndGenRowVec() {
        cout << "Compute ExpectedInPlaceGenMatColIndGenRowVec(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::GenMat,
            InputClass::ColInd,
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
                  _colInd = *static_cast<int*>(value.second);
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
            _copyOfColInd = _colInd;
            _copyOfGenRowVec = _genRowVec;

            expectedMatDiagEqual();

            _genMat = _copyOfGenMat;
            _colInd = _copyOfColInd;
            _genRowVec = _copyOfGenRowVec;
            expectedMatDiagPlus();

            _genMat = _copyOfGenMat;
            _colInd = _copyOfColInd;
            _genRowVec = _copyOfGenRowVec;
            expectedMatDiagMinus();

            _genMat = _copyOfGenMat;
            _colInd = _copyOfColInd;
            _genRowVec = _copyOfGenRowVec;
            expectedMatDiagElemTimes();

            _genMat = _copyOfGenMat;
            _colInd = _copyOfColInd;
            _genRowVec = _copyOfGenRowVec;
            expectedMatDiagElemDivide();

            _genMat = _copyOfGenMat;
            _colInd = _copyOfColInd;
            _genRowVec = _copyOfGenRowVec;
            expectedMatColEqual();

            _genMat = _copyOfGenMat;
            _colInd = _copyOfColInd;
            _genRowVec = _copyOfGenRowVec;
            expectedMatColPlus();

            _genMat = _copyOfGenMat;
            _colInd = _copyOfColInd;
            _genRowVec = _copyOfGenRowVec;
            expectedMatColMinus();

            _genMat = _copyOfGenMat;
            _colInd = _copyOfColInd;
            _genRowVec = _copyOfGenRowVec;
            expectedMatColElemTimes();

            _genMat = _copyOfGenMat;
            _colInd = _copyOfColInd;
            _genRowVec = _copyOfGenRowVec;
            expectedMatColElemDivide();
          }

          cout << "done." << endl;
        }

    protected:
      Mat<double> _genMat;
      Mat<double> _copyOfGenMat;

      int _colInd;
      int _copyOfColInd;

      Row<double> _genRowVec;
      Row<double> _copyOfGenRowVec;

      void expectedMatDiagEqual() {
        if(_colInd >= _genMat.n_cols) {
          return;
        }

        if (_genRowVec.n_elem != min(_genMat.n_rows, _genMat.n_cols - _colInd)) {
          return;
        }

        cout << "- Compute expectedMatDiagEqual() ... ";

        _genMat.diag(_colInd) = _genRowVec;
        save<double>("Mat.diagSuperEqual", _genMat);

        cout << "done." << endl;
      }

      void expectedMatDiagPlus() {
        if(_colInd >= _genMat.n_cols) {
          return;
        }

        if (_genRowVec.n_elem != min(_genMat.n_rows, _genMat.n_cols - _colInd)) {
          return;
        }

        cout << "- Compute expectedMatDiagPlus() ... ";

        _genMat.diag(_colInd) += _genRowVec;
        save<double>("Mat.diagSuperPlus", _genMat);

        cout << "done." << endl;
      }

      void expectedMatDiagMinus() {
        if(_colInd >= _genMat.n_cols) {
          return;
        }

        if (_genRowVec.n_elem != min(_genMat.n_rows, _genMat.n_cols - _colInd)) {
          return;
        }

        cout << "- Compute expectedMatDiagMinus() ... ";

        _genMat.diag(_colInd) -= _genRowVec;
        save<double>("Mat.diagSuperMinus", _genMat);

        cout << "done." << endl;
      }

      void expectedMatDiagElemTimes() {
        if(_colInd >= _genMat.n_cols) {
          return;
        }

        if (_genRowVec.n_elem != min(_genMat.n_rows, _genMat.n_cols - _colInd)) {
          return;
        }

        cout << "- Compute expectedMatDiagElemTimes() ... ";

        _genMat.diag(_colInd) %= _genRowVec;
        save<double>("Mat.diagSuperElemTimes", _genMat);

        cout << "done." << endl;
      }

      void expectedMatDiagElemDivide() {
        if(_colInd >= _genMat.n_cols) {
          return;
        }

        if (_genRowVec.n_elem != min(_genMat.n_rows, _genMat.n_cols - _colInd)) {
          return;
        }

        cout << "- Compute expectedMatDiagElemDivide() ... ";

        _genMat.diag(_colInd) /= _genRowVec;
        save<double>("Mat.diagSuperElemDivide", _genMat);

        cout << "done." << endl;
      }

      void expectedMatColEqual() {
        if(_colInd >= _genMat.n_cols) {
          return;
        }

        if(!_genRowVec.is_colvec() || _genMat.n_rows != _genRowVec.n_rows) {
          return;
        }

        cout << "- Compute expectedMatColEqual() ... ";

        _genMat.col(_colInd) = _genRowVec;
        save<double>("Mat.colEqual", _genMat);

        cout << "done." << endl;
      }

      void expectedMatColPlus() {
        if(_colInd >= _genMat.n_cols) {
          return;
        }

        if(!_genRowVec.is_colvec() || _genMat.n_rows != _genRowVec.n_rows) {
          return;
        }

        cout << "- Compute expectedMatColPlus() ... ";

        _genMat.col(_colInd) += _genRowVec;
        save<double>("Mat.colPlus", _genMat);

        cout << "done." << endl;
      }

      void expectedMatColMinus() {
        if(_colInd >= _genMat.n_cols) {
          return;
        }

        if(!_genRowVec.is_colvec() || _genMat.n_rows != _genRowVec.n_rows) {
          return;
        }

        cout << "- Compute expectedMatColMinus() ... ";

        _genMat.col(_colInd) -= _genRowVec;
        save<double>("Mat.colMinus", _genMat);

        cout << "done." << endl;
      }

      void expectedMatColElemTimes() {
        if(_colInd >= _genMat.n_cols) {
          return;
        }

        if(!_genRowVec.is_colvec() || _genMat.n_rows != _genRowVec.n_rows) {
          return;
        }

        cout << "- Compute expectedMatColElemTimes() ... ";

        _genMat.col(_colInd) %= _genRowVec;
        save<double>("Mat.colElemTimes", _genMat);

        cout << "done." << endl;
      }

      void expectedMatColElemDivide() {
        if(_colInd >= _genMat.n_cols) {
          return;
        }

        if(!_genRowVec.is_colvec() || _genMat.n_rows != _genRowVec.n_rows) {
          return;
        }

        cout << "- Compute expectedMatColElemDivide() ... ";

        _genMat.col(_colInd) /= _genRowVec;
        save<double>("Mat.colElemDivide", _genMat);

        cout << "done." << endl;
      }
  };
}
