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

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedInPlaceGenMatColIndGenDouble : public Expected {
    public:
      ExpectedInPlaceGenMatColIndGenDouble() {
        cout << "Compute ExpectedInPlaceGenMatColIndGenDouble(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::GenMat,
            InputClass::ColInd,
            InputClass::GenDouble
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
                  _genDouble = *static_cast<double*>(value.second);
                  break;
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            _copyOfGenMat = _genMat;
            _copyOfColInd = _colInd;
            _copyOfGenDouble = _genDouble;

            expectedMatDiagPlus();

            _genMat = _copyOfGenMat;
            _colInd = _copyOfColInd;
            _genDouble = _copyOfGenDouble;
            expectedMatDiagMinus();

            _genMat = _copyOfGenMat;
            _colInd = _copyOfColInd;
            _genDouble = _copyOfGenDouble;
            expectedMatDiagTimes();

            _genMat = _copyOfGenMat;
            _colInd = _copyOfColInd;
            _genDouble = _copyOfGenDouble;
            expectedMatDiagDivide();

            _genMat = _copyOfGenMat;
            _colInd = _copyOfColInd;
            _genDouble = _copyOfGenDouble;
            expectedMatColPlus();

            _genMat = _copyOfGenMat;
            _colInd = _copyOfColInd;
            _genDouble = _copyOfGenDouble;
            expectedMatColMinus();

            _genMat = _copyOfGenMat;
            _colInd = _copyOfColInd;
            _genDouble = _copyOfGenDouble;
            expectedMatColTimes();

            _genMat = _copyOfGenMat;
            _colInd = _copyOfColInd;
            _genDouble = _copyOfGenDouble;
            expectedMatColDivide();
          }

          cout << "done." << endl;
        }

    protected:
      Mat<double> _genMat;
      Mat<double> _copyOfGenMat;

      int _colInd;
      int _copyOfColInd;

      double _genDouble;
      double _copyOfGenDouble;

      void expectedMatDiagPlus() {
        if(_colInd >= _genMat.n_cols) {
          return;
        }

        cout << "- Compute expectedMatDiagPlus() ... ";

        _genMat.diag(_colInd) += _genDouble;
        save<double>("Mat.diagSuperPlus", _genMat);

        cout << "done." << endl;
      }

      void expectedMatDiagMinus() {
        if(_colInd >= _genMat.n_cols) {
          return;
        }

        cout << "- Compute expectedMatDiagMinus() ... ";

        _genMat.diag(_colInd) -= _genDouble;
        save<double>("Mat.diagSuperMinus", _genMat);

        cout << "done." << endl;
      }

      void expectedMatDiagTimes() {
        if(_colInd >= _genMat.n_cols) {
          return;
        }

        cout << "- Compute expectedMatDiagTimes() ... ";

        _genMat.diag(_colInd) *= _genDouble;
        save<double>("Mat.diagSuperTimes", _genMat);

        cout << "done." << endl;
      }

      void expectedMatDiagDivide() {
        if(_colInd >= _genMat.n_cols) {
          return;
        }

        cout << "- Compute expectedMatDiagDivide() ... ";

        _genMat.diag(_colInd) /= _genDouble;
        save<double>("Mat.diagSuperDivide", _genMat);

        cout << "done." << endl;
      }

      void expectedMatColPlus() {
        if(_colInd >= _genMat.n_cols) {
          return;
        }

        cout << "- Compute expectedMatColPlus() ... ";

        _genMat.col(_colInd) += _genDouble;
        save<double>("Mat.colPlus", _genMat);

        cout << "done." << endl;
      }

      void expectedMatColMinus() {
        if(_colInd >= _genMat.n_cols) {
          return;
        }

        cout << "- Compute expectedMatColMinus() ... ";

        _genMat.col(_colInd) -= _genDouble;
        save<double>("Mat.colMinus", _genMat);

        cout << "done." << endl;
      }

      void expectedMatColTimes() {
        if(_colInd >= _genMat.n_cols) {
          return;
        }

        cout << "- Compute expectedMatColTimes() ... ";

        _genMat.col(_colInd) *= _genDouble;
        save<double>("Mat.colTimes", _genMat);

        cout << "done." << endl;
      }

      void expectedMatColDivide() {
        if(_colInd >= _genMat.n_cols) {
          return;
        }

        cout << "- Compute expectedMatColDivide() ... ";

        _genMat.col(_colInd) /= _genDouble;
        save<double>("Mat.colDivide", _genMat);

        cout << "done." << endl;
      }
  };
}
