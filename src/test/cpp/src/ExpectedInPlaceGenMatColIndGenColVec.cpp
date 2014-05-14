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
using arma::Col;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedInPlaceGenMatColIndGenColVec : public Expected {
    public:
      ExpectedInPlaceGenMatColIndGenColVec() {
        cout << "Compute ExpectedInPlaceGenMatColIndGenColVec(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::GenMat,
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
                  _colInd = *static_cast<int*>(value.second);
                  break;
                case 2:
                  _fileSuffix += "," + value.first;
                  _genColVec = *static_cast<Col<double>*>(value.second);
                  break;
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            _copyOfGenMat = _genMat;
            _copyOfColInd = _colInd;
            _copyOfGenColVec = _genColVec;

            expectedMatDiagEqual();

            _genMat = _copyOfGenMat;
            _colInd = _copyOfColInd;
            _genColVec = _copyOfGenColVec;
            expectedMatDiagPlus();

            _genMat = _copyOfGenMat;
            _colInd = _copyOfColInd;
            _genColVec = _copyOfGenColVec;
            expectedMatDiagMinus();

            _genMat = _copyOfGenMat;
            _colInd = _copyOfColInd;
            _genColVec = _copyOfGenColVec;
            expectedMatDiagElemTimes();

            _genMat = _copyOfGenMat;
            _colInd = _copyOfColInd;
            _genColVec = _copyOfGenColVec;
            expectedMatDiagElemDivide();

            _genMat = _copyOfGenMat;
            _colInd = _copyOfColInd;
            _genColVec = _copyOfGenColVec;
            expectedMatColEqual();

            _genMat = _copyOfGenMat;
            _colInd = _copyOfColInd;
            _genColVec = _copyOfGenColVec;
            expectedMatColPlus();

            _genMat = _copyOfGenMat;
            _colInd = _copyOfColInd;
            _genColVec = _copyOfGenColVec;
            expectedMatColMinus();

            _genMat = _copyOfGenMat;
            _colInd = _copyOfColInd;
            _genColVec = _copyOfGenColVec;
            expectedMatColElemTimes();

            _genMat = _copyOfGenMat;
            _colInd = _copyOfColInd;
            _genColVec = _copyOfGenColVec;
            expectedMatColElemDivide();
          }

          cout << "done." << endl;
        }

    protected:
      Mat<double> _genMat;
      Mat<double> _copyOfGenMat;

      int _colInd;
      int _copyOfColInd;

      Col<double> _genColVec;
      Col<double> _copyOfGenColVec;

      void expectedMatDiagEqual() {
        if(_colInd >= _genMat.n_cols) {
          return;
        }

        if (_genColVec.n_elem != min(_genMat.n_rows, _genMat.n_cols - _colInd)) {
          return;
        }

        cout << "- Compute expectedMatDiagEqual() ... ";

        _genMat.diag(_colInd) = _genColVec;
        save<double>("Mat.diagSuperEqual", _genMat);

        cout << "done." << endl;
      }

      void expectedMatDiagPlus() {
        if(_colInd >= _genMat.n_cols) {
          return;
        }

        if (_genColVec.n_elem != min(_genMat.n_rows, _genMat.n_cols - _colInd)) {
          return;
        }

        cout << "- Compute expectedMatDiagPlus() ... ";

        _genMat.diag(_colInd) += _genColVec;
        save<double>("Mat.diagSuperPlus", _genMat);

        cout << "done." << endl;
      }

      void expectedMatDiagMinus() {
        if(_colInd >= _genMat.n_cols) {
          return;
        }

        if (_genColVec.n_elem != min(_genMat.n_rows, _genMat.n_cols - _colInd)) {
          return;
        }

        cout << "- Compute expectedMatDiagMinus() ... ";

        _genMat.diag(_colInd) -= _genColVec;
        save<double>("Mat.diagSuperMinus", _genMat);

        cout << "done." << endl;
      }

      void expectedMatDiagElemTimes() {
        if(_colInd >= _genMat.n_cols) {
          return;
        }

        if (_genColVec.n_elem != min(_genMat.n_rows, _genMat.n_cols - _colInd)) {
          return;
        }

        cout << "- Compute expectedMatDiagElemTimes() ... ";

        _genMat.diag(_colInd) %= _genColVec;
        save<double>("Mat.diagSuperElemTimes", _genMat);

        cout << "done." << endl;
      }

      void expectedMatDiagElemDivide() {
        if(_colInd >= _genMat.n_cols) {
          return;
        }

        if (_genColVec.n_elem != min(_genMat.n_rows, _genMat.n_cols - _colInd)) {
          return;
        }

        cout << "- Compute expectedMatDiagElemDivide() ... ";

        _genMat.diag(_colInd) /= _genColVec;
        save<double>("Mat.diagSuperElemDivide", _genMat);

        cout << "done." << endl;
      }

      void expectedMatColEqual() {
        if(_colInd >= _genMat.n_cols) {
          return;
        }

        if(_genMat.n_rows != _genColVec.n_rows) {
          return;
        }

        cout << "- Compute expectedMatColEqual() ... ";

        _genMat.col(_colInd) = _genColVec;
        save<double>("Mat.colEqual", _genMat);

        cout << "done." << endl;
      }

      void expectedMatColPlus() {
        if(_colInd >= _genMat.n_cols) {
          return;
        }

        if(_genMat.n_rows != _genColVec.n_rows) {
          return;
        }

        cout << "- Compute expectedMatColPlus() ... ";

        _genMat.col(_colInd) += _genColVec;
        save<double>("Mat.colPlus", _genMat);

        cout << "done." << endl;
      }

      void expectedMatColMinus() {
        if(_colInd >= _genMat.n_cols) {
          return;
        }

        if(_genMat.n_rows != _genColVec.n_rows) {
          return;
        }

        cout << "- Compute expectedMatColMinus() ... ";

        _genMat.col(_colInd) -= _genColVec;
        save<double>("Mat.colMinus", _genMat);

        cout << "done." << endl;
      }

      void expectedMatColElemTimes() {
        if(_colInd >= _genMat.n_cols) {
          return;
        }

        if(_genMat.n_rows != _genColVec.n_rows) {
          return;
        }

        cout << "- Compute expectedMatColElemTimes() ... ";

        _genMat.col(_colInd) %= _genColVec;
        save<double>("Mat.colElemTimes", _genMat);

        cout << "done." << endl;
      }

      void expectedMatColElemDivide() {
        if(_colInd >= _genMat.n_cols) {
          return;
        }

        if(_genMat.n_rows != _genColVec.n_rows) {
          return;
        }

        cout << "- Compute expectedMatColElemDivide() ... ";

        _genMat.col(_colInd) /= _genColVec;
        save<double>("Mat.colElemDivide", _genMat);

        cout << "done." << endl;
      }
  };
}
