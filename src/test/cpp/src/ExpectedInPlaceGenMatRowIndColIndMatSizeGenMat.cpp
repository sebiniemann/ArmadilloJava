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
using arma::size;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedInPlaceGenMatRowIndColIndMatSizeGenMat : public Expected {
    public:
      ExpectedInPlaceGenMatRowIndColIndMatSizeGenMat() {
        cout << "Compute ExpectedInPlaceGenMatRowIndColIndMatSizeGenMat(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::GenMat,
            InputClass::RowInd,
            InputClass::ColInd,
            InputClass::MatSize,
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
                  _genMatB = *static_cast<Mat<double>*>(value.second);
                  break;
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            _copyOfGenMatA = _genMatA;
            _copyOfRowInd = _rowInd;
            _copyOfColInd = _colInd;
            _copyOfMatSize = _matSize;
            _copyOfGenMatB = _genMatB;

            expectedMatSubmatEqual();

            _genMatA = _copyOfGenMatA;
            _rowInd = _copyOfRowInd;
            _colInd = _copyOfColInd;
            _matSize = _copyOfMatSize;
            _genMatB = _copyOfGenMatB;
            expectedMatSubmatPlus();

            _genMatA = _copyOfGenMatA;
            _rowInd = _copyOfRowInd;
            _colInd = _copyOfColInd;
            _matSize = _copyOfMatSize;
            _genMatB = _copyOfGenMatB;
            expectedMatSubmatMinus();

            _genMatA = _copyOfGenMatA;
            _rowInd = _copyOfRowInd;
            _colInd = _copyOfColInd;
            _matSize = _copyOfMatSize;
            _genMatB = _copyOfGenMatB;
            expectedMatSubmatElemTimes();

            _genMatA = _copyOfGenMatA;
            _rowInd = _copyOfRowInd;
            _colInd = _copyOfColInd;
            _matSize = _copyOfMatSize;
            _genMatB = _copyOfGenMatB;
            expectedMatSubmatElemDivide();
          }

          cout << "done." << endl;
        }

    protected:
      Mat<double> _genMatA;
      Mat<double> _copyOfGenMatA;

      int _rowInd;
      int _copyOfRowInd;

      int _colInd;
      int _copyOfColInd;

      pair<int, int> _matSize;
      pair<int, int> _copyOfMatSize;

      Mat<double> _genMatB;
      Mat<double> _copyOfGenMatB;

      void expectedMatSubmatEqual() {
        if(!_genMatA.in_range(_rowInd, _colInd, size(_matSize.first, _matSize.second))) {
          return;
        }

        if(_genMatB.n_rows != _matSize.first) {
          return;
        }

        if(_genMatB.n_cols != _matSize.second) {
          return;
        }

        _genMatA.submat(_rowInd, _colInd, size(_matSize.first, _matSize.second)) = _genMatB;

        cout << "- Compute expectedMatSubmatEqual() ... ";
        save<double>("Mat.submatEqual", _genMatA);
        cout << "done." << endl;
      }

      void expectedMatSubmatPlus() {
        if(!_genMatA.in_range(_rowInd, _colInd, size(_matSize.first, _matSize.second))) {
          return;
        }

        if(_genMatB.n_rows != _matSize.first) {
          return;
        }

        if(_genMatB.n_cols != _matSize.second) {
          return;
        }

        _genMatA.submat(_rowInd, _colInd, size(_matSize.first, _matSize.second)) += _genMatB;

        cout << "- Compute expectedMatSubmatPlus() ... ";
        save<double>("Mat.submatPlus", _genMatA);
        cout << "done." << endl;
      }

      void expectedMatSubmatMinus() {
        if(!_genMatA.in_range(_rowInd, _colInd, size(_matSize.first, _matSize.second))) {
          return;
        }

        if(_genMatB.n_rows != _matSize.first) {
          return;
        }

        if(_genMatB.n_cols != _matSize.second) {
          return;
        }

        _genMatA.submat(_rowInd, _colInd, size(_matSize.first, _matSize.second)) -= _genMatB;

        cout << "- Compute expectedMatSubmatMinus() ... ";
        save<double>("Mat.submatMinus", _genMatA);
        cout << "done." << endl;
      }

      void expectedMatSubmatElemTimes() {
        if(!_genMatA.in_range(_rowInd, _colInd, size(_matSize.first, _matSize.second))) {
          return;
        }

        if(_genMatB.n_rows != _matSize.first) {
          return;
        }

        if(_genMatB.n_cols != _matSize.second) {
          return;
        }

        _genMatA.submat(_rowInd, _colInd, size(_matSize.first, _matSize.second)) %= _genMatB;

        cout << "- Compute expectedMatSubmatElemTimes() ... ";
        save<double>("Mat.submatElemTimes", _genMatA);
        cout << "done." << endl;
      }

      void expectedMatSubmatElemDivide() {
        if(!_genMatA.in_range(_rowInd, _colInd, size(_matSize.first, _matSize.second))) {
          return;
        }

        if(_genMatB.n_rows != _matSize.first) {
          return;
        }

        if(_genMatB.n_cols != _matSize.second) {
          return;
        }

        _genMatA.submat(_rowInd, _colInd, size(_matSize.first, _matSize.second)) /= _genMatB;

        cout << "- Compute expectedMatSubmatElemDivide() ... ";
        save<double>("Mat.submatElemDivide", _genMatA);
        cout << "done." << endl;
      }
  };
}
