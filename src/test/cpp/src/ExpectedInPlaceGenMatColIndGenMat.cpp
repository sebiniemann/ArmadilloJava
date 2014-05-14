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

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedInPlaceGenMatColIndGenMat : public Expected {
    public:
      ExpectedInPlaceGenMatColIndGenMat() {
        cout << "Compute ExpectedInPlaceGenMatColIndGenMat(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::GenMat,
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
                  _colInd = *static_cast<int*>(value.second);
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
            _copyOfColInd = _colInd;
            _copyOfGenMatB = _genMatB;

            expectedMatDiagEqual();

            _genMatA = _copyOfGenMatA;
            _colInd = _copyOfColInd;
            _genMatB = _copyOfGenMatB;
            expectedMatDiagPlus();

            _genMatA = _copyOfGenMatA;
            _colInd = _copyOfColInd;
            _genMatB = _copyOfGenMatB;
            expectedMatDiagMinus();

            _genMatA = _copyOfGenMatA;
            _colInd = _copyOfColInd;
            _genMatB = _copyOfGenMatB;
            expectedMatDiagElemTimes();

            _genMatA = _copyOfGenMatA;
            _colInd = _copyOfColInd;
            _genMatB = _copyOfGenMatB;
            expectedMatDiagElemDivide();

            _genMatA = _copyOfGenMatA;
            _colInd = _copyOfColInd;
            _genMatB = _copyOfGenMatB;
            expectedMatColEqual();

            _genMatA = _copyOfGenMatA;
            _colInd = _copyOfColInd;
            _genMatB = _copyOfGenMatB;
            expectedMatColPlus();

            _genMatA = _copyOfGenMatA;
            _colInd = _copyOfColInd;
            _genMatB = _copyOfGenMatB;
            expectedMatColMinus();

            _genMatA = _copyOfGenMatA;
            _colInd = _copyOfColInd;
            _genMatB = _copyOfGenMatB;
            expectedMatColElemTimes();

            _genMatA = _copyOfGenMatA;
            _colInd = _copyOfColInd;
            _genMatB = _copyOfGenMatB;
            expectedMatColElemDivide();
          }

          cout << "done." << endl;
        }

    protected:
      Mat<double> _genMatA;
      Mat<double> _copyOfGenMatA;

      int _colInd;
      int _copyOfColInd;

      Mat<double> _genMatB;
      Mat<double> _copyOfGenMatB;

      void expectedMatDiagEqual() {
        if(_colInd >= _genMatA.n_cols) {
          return;
        }

        if (!_genMatB.is_vec() || _genMatB.n_elem != min(_genMatA.n_rows, _genMatA.n_cols - _colInd)) {
          return;
        }

        cout << "- Compute expectedMatDiagEqual() ... ";

        _genMatA.diag(_colInd) = _genMatB;
        save<double>("Mat.diagSuperEqual", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatDiagPlus() {
        if(_colInd >= _genMatA.n_cols) {
          return;
        }

        if (!_genMatB.is_vec() || _genMatB.n_elem != min(_genMatA.n_rows, _genMatA.n_cols - _colInd)) {
          return;
        }

        cout << "- Compute expectedMatDiagPlus() ... ";

        _genMatA.diag(_colInd) += _genMatB;
        save<double>("Mat.diagSuperPlus", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatDiagMinus() {
        if(_colInd >= _genMatA.n_cols) {
          return;
        }

        if (!_genMatB.is_vec() || _genMatB.n_elem != min(_genMatA.n_rows, _genMatA.n_cols - _colInd)) {
          return;
        }

        cout << "- Compute expectedMatDiagMinus() ... ";

        _genMatA.diag(_colInd) -= _genMatB;
        save<double>("Mat.diagSuperMinus", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatDiagElemTimes() {
        if(_colInd >= _genMatA.n_cols) {
          return;
        }

        if (!_genMatB.is_vec() || _genMatB.n_elem != min(_genMatA.n_rows, _genMatA.n_cols - _colInd)) {
          return;
        }

        cout << "- Compute expectedMatDiagElemTimes() ... ";

        _genMatA.diag(_colInd) %= _genMatB;
        save<double>("Mat.diagSuperElemTimes", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatDiagElemDivide() {
        if(_colInd >= _genMatA.n_cols) {
          return;
        }

        if (!_genMatB.is_vec() || _genMatB.n_elem != min(_genMatA.n_rows, _genMatA.n_cols - _colInd)) {
          return;
        }

        cout << "- Compute expectedMatDiagElemDivide() ... ";

        _genMatA.diag(_colInd) /= _genMatB;
        save<double>("Mat.diagSuperElemDivide", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatColEqual() {
        if(_colInd >= _genMatA.n_cols) {
          return;
        }

        if(!_genMatB.is_colvec() || _genMatA.n_rows != _genMatB.n_rows) {
          return;
        }

        cout << "- Compute expectedMatColEqual() ... ";

        _genMatA.col(_colInd) = _genMatB;
        save<double>("Mat.colEqual", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatColPlus() {
        if(_colInd >= _genMatA.n_cols) {
          return;
        }

        if(!_genMatB.is_colvec() || _genMatA.n_rows != _genMatB.n_rows) {
          return;
        }

        cout << "- Compute expectedMatColPlus() ... ";

        _genMatA.col(_colInd) += _genMatB;
        save<double>("Mat.colPlus", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatColMinus() {
        if(_colInd >= _genMatA.n_cols) {
          return;
        }

        if(!_genMatB.is_colvec() || _genMatA.n_rows != _genMatB.n_rows) {
          return;
        }

        cout << "- Compute expectedMatColMinus() ... ";

        _genMatA.col(_colInd) -= _genMatB;
        save<double>("Mat.colMinus", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatColElemTimes() {
        if(_colInd >= _genMatA.n_cols) {
          return;
        }

        if(!_genMatB.is_colvec() || _genMatA.n_rows != _genMatB.n_rows) {
          return;
        }

        cout << "- Compute expectedMatColElemTimes() ... ";

        _genMatA.col(_colInd) %= _genMatB;
        save<double>("Mat.colElemTimes", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatColElemDivide() {
        if(_colInd >= _genMatA.n_cols) {
          return;
        }

        if(!_genMatB.is_colvec() || _genMatA.n_rows != _genMatB.n_rows) {
          return;
        }

        cout << "- Compute expectedMatColElemDivide() ... ";

        _genMatA.col(_colInd) /= _genMatB;
        save<double>("Mat.colElemDivide", _genMatA);

        cout << "done." << endl;
      }
  };
}
