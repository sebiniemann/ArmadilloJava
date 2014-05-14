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
using arma::size;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedInPlaceGenMatRowIndColIndMatSizeGenRowVec : public Expected {
    public:
      ExpectedInPlaceGenMatRowIndColIndMatSizeGenRowVec() {
        cout << "Compute ExpectedInPlaceGenMatRowIndColIndMatSizeGenRowVec(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::GenMat,
            InputClass::RowInd,
            InputClass::ColInd,
            InputClass::MatSize,
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
                  _colInd = *static_cast<int*>(value.second);
                  break;
                case 3:
                  _fileSuffix += "," + value.first;
                  _matSize = *static_cast<pair<int, int>*>(value.second);
                  break;
                case 4:
                  _fileSuffix += "," + value.first;
                  _genRowVec = *static_cast<Row<double>*>(value.second);
                  break;
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            _copyOfGenMat = _genMat;
            _copyOfRowInd = _rowInd;
            _copyOfColInd = _colInd;
            _copyOfMatSize = _matSize;
            _copyOfGenRowVec = _genRowVec;

            expectedMatSubmatEqual();

            _genMat = _copyOfGenMat;
            _rowInd = _copyOfRowInd;
            _colInd = _copyOfColInd;
            _matSize = _copyOfMatSize;
            _genRowVec = _copyOfGenRowVec;
            expectedMatSubmatPlus();

            _genMat = _copyOfGenMat;
            _rowInd = _copyOfRowInd;
            _colInd = _copyOfColInd;
            _matSize = _copyOfMatSize;
            _genRowVec = _copyOfGenRowVec;
            expectedMatSubmatMinus();

            _genMat = _copyOfGenMat;
            _rowInd = _copyOfRowInd;
            _colInd = _copyOfColInd;
            _matSize = _copyOfMatSize;
            _genRowVec = _copyOfGenRowVec;
            expectedMatSubmatElemTimes();

            _genMat = _copyOfGenMat;
            _rowInd = _copyOfRowInd;
            _colInd = _copyOfColInd;
            _matSize = _copyOfMatSize;
            _genRowVec = _copyOfGenRowVec;
            expectedMatSubmatElemDivide();
          }

          cout << "done." << endl;
        }

    protected:
      Mat<double> _genMat;
      Mat<double> _copyOfGenMat;

      int _rowInd;
      int _copyOfRowInd;

      int _colInd;
      int _copyOfColInd;

      pair<int, int> _matSize;
      pair<int, int> _copyOfMatSize;

      Row<double> _genRowVec;
      Row<double> _copyOfGenRowVec;

      void expectedMatSubmatEqual() {
        if(!_genMat.in_range(_rowInd, _colInd, size(_matSize.first, _matSize.second))) {
          return;
        }

        if(_matSize.first != 1) {
          return;
        }

        if(_genRowVec.n_cols != _matSize.second) {
          return;
        }

        _genMat.submat(_rowInd, _colInd, size(_matSize.first, _matSize.second)) = _genRowVec;

        cout << "- Compute expectedMatSubmatEqual() ... ";
        save<double>("Mat.submatEqual", _genMat);
        cout << "done." << endl;
      }

      void expectedMatSubmatPlus() {
        if(!_genMat.in_range(_rowInd, _colInd, size(_matSize.first, _matSize.second))) {
          return;
        }

        if(_matSize.first != 1) {
          return;
        }

        if(_genRowVec.n_cols != _matSize.second) {
          return;
        }

        _genMat.submat(_rowInd, _colInd, size(_matSize.first, _matSize.second)) += _genRowVec;

        cout << "- Compute expectedMatSubmatPlus() ... ";
        save<double>("Mat.submatPlus", _genMat);
        cout << "done." << endl;
      }

      void expectedMatSubmatMinus() {
        if(!_genMat.in_range(_rowInd, _colInd, size(_matSize.first, _matSize.second))) {
          return;
        }

        if(_matSize.first != 1) {
          return;
        }

        if(_genRowVec.n_cols != _matSize.second) {
          return;
        }

        _genMat.submat(_rowInd, _colInd, size(_matSize.first, _matSize.second)) -= _genRowVec;

        cout << "- Compute expectedMatSubmatMinus() ... ";
        save<double>("Mat.submatMinus", _genMat);
        cout << "done." << endl;
      }

      void expectedMatSubmatElemTimes() {
        if(!_genMat.in_range(_rowInd, _colInd, size(_matSize.first, _matSize.second))) {
          return;
        }

        if(_matSize.first != 1) {
          return;
        }

        if(_genRowVec.n_cols != _matSize.second) {
          return;
        }

        _genMat.submat(_rowInd, _colInd, size(_matSize.first, _matSize.second)) %= _genRowVec;

        cout << "- Compute expectedMatSubmatElemTimes() ... ";
        save<double>("Mat.submatElemTimes", _genMat);
        cout << "done." << endl;
      }

      void expectedMatSubmatElemDivide() {
        if(!_genMat.in_range(_rowInd, _colInd, size(_matSize.first, _matSize.second))) {
          return;
        }

        if(_matSize.first != 1) {
          return;
        }

        if(_genRowVec.n_cols != _matSize.second) {
          return;
        }

        _genMat.submat(_rowInd, _colInd, size(_matSize.first, _matSize.second)) /= _genRowVec;

        cout << "- Compute expectedMatSubmatElemDivide() ... ";
        save<double>("Mat.submatElemDivide", _genMat);
        cout << "done." << endl;
      }
  };
}
