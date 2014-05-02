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

            expectedMatRowEqual();

            _genMat = _copyOfGenMat;
            _colInd = _copyOfColInd;
            _genRowVec = _copyOfGenRowVec;
            expectedMatRowPlus();

            _genMat = _copyOfGenMat;
            _colInd = _copyOfColInd;
            _genRowVec = _copyOfGenRowVec;
            expectedMatRowMinus();

            _genMat = _copyOfGenMat;
            _colInd = _copyOfColInd;
            _genRowVec = _copyOfGenRowVec;
            expectedMatRowElemTimes();

            _genMat = _copyOfGenMat;
            _colInd = _copyOfColInd;
            _genRowVec = _copyOfGenRowVec;
            expectedMatRowElemDivide();
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

      void expectedMatRowEqual() {
        if(_colInd >= _genMat.n_cols) {
          return;
        }

        if(!_genRowVec.is_colvec() || _genMat.n_rows != _genRowVec.n_rows) {
          return;
        }

        cout << "- Compute expectedMatRowEqual() ... ";

        _genMat.col(_colInd) = _genRowVec;
        save<double>("Mat.colEqual", _genMat);

        cout << "done." << endl;
      }

      void expectedMatRowPlus() {
        if(_colInd >= _genMat.n_cols) {
          return;
        }

        if(!_genRowVec.is_colvec() || _genMat.n_rows != _genRowVec.n_rows) {
          return;
        }

        cout << "- Compute expectedMatRowPlus() ... ";

        _genMat.col(_colInd) += _genRowVec;
        save<double>("Mat.colPlus", _genMat);

        cout << "done." << endl;
      }

      void expectedMatRowMinus() {
        if(_colInd >= _genMat.n_cols) {
          return;
        }

        if(!_genRowVec.is_colvec() || _genMat.n_rows != _genRowVec.n_rows) {
          return;
        }

        cout << "- Compute expectedMatRowMinus() ... ";

        _genMat.col(_colInd) -= _genRowVec;
        save<double>("Mat.colMinus", _genMat);

        cout << "done." << endl;
      }

      void expectedMatRowElemTimes() {
        if(_colInd >= _genMat.n_cols) {
          return;
        }

        if(!_genRowVec.is_colvec() || _genMat.n_rows != _genRowVec.n_rows) {
          return;
        }

        cout << "- Compute expectedMatRowElemTimes() ... ";

        _genMat.col(_colInd) %= _genRowVec;
        save<double>("Mat.colElemTimes", _genMat);

        cout << "done." << endl;
      }

      void expectedMatRowElemDivide() {
        if(_colInd >= _genMat.n_cols) {
          return;
        }

        if(!_genRowVec.is_colvec() || _genMat.n_rows != _genRowVec.n_rows) {
          return;
        }

        cout << "- Compute expectedMatRowElemDivide() ... ";

        _genMat.col(_colInd) /= _genRowVec;
        save<double>("Mat.colElemDivide", _genMat);

        cout << "done." << endl;
      }
  };
}
