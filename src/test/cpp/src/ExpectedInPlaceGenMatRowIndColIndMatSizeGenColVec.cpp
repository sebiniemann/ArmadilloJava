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
using arma::size;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedInPlaceGenMatRowIndColIndMatSizeGenColVec : public Expected {
    public:
      ExpectedInPlaceGenMatRowIndColIndMatSizeGenColVec() {
        cout << "Compute ExpectedInPlaceGenMatRowIndColIndMatSizeGenColVec(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::GenMat,
            InputClass::RowInd,
            InputClass::ColInd,
            InputClass::MatSize,
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
                  _genColVec = *static_cast<Col<double>*>(value.second);
                  break;
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            _copyOfGenMat = _genMat;
            _copyOfRowInd = _rowInd;
            _copyOfColInd = _colInd;
            _copyOfMatSize = _matSize;
            _copyOfGenColVec = _genColVec;

            expectedMatSubmatEqual();

            _genMat = _copyOfGenMat;
            _rowInd = _copyOfRowInd;
            _colInd = _copyOfColInd;
            _matSize = _copyOfMatSize;
            _genColVec = _copyOfGenColVec;
            expectedMatSubmatPlus();

            _genMat = _copyOfGenMat;
            _rowInd = _copyOfRowInd;
            _colInd = _copyOfColInd;
            _matSize = _copyOfMatSize;
            _genColVec = _copyOfGenColVec;
            expectedMatSubmatMinus();

            _genMat = _copyOfGenMat;
            _rowInd = _copyOfRowInd;
            _colInd = _copyOfColInd;
            _matSize = _copyOfMatSize;
            _genColVec = _copyOfGenColVec;
            expectedMatSubmatElemTimes();

            _genMat = _copyOfGenMat;
            _rowInd = _copyOfRowInd;
            _colInd = _copyOfColInd;
            _matSize = _copyOfMatSize;
            _genColVec = _copyOfGenColVec;
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

      Col<double> _genColVec;
      Col<double> _copyOfGenColVec;

      void expectedMatSubmatEqual() {
        if(!_genMat.in_range(_rowInd, _colInd, size(_matSize.first, _matSize.second))) {
          return;
        }

        if(_genColVec.n_rows != _matSize.first) {
          return;
        }

        if(_matSize.second != 1) {
          return;
        }

        _genMat.submat(_rowInd, _colInd, size(_matSize.first, _matSize.second)) = _genColVec;

        cout << "- Compute expectedMatSubmatEqual() ... ";
        save<double>("Mat.submatEqual", _genMat);
        cout << "done." << endl;
      }

      void expectedMatSubmatPlus() {
        if(!_genMat.in_range(_rowInd, _colInd, size(_matSize.first, _matSize.second))) {
          return;
        }

        if(_genColVec.n_rows != _matSize.first) {
          return;
        }

        if(_matSize.second != 1) {
          return;
        }

        _genMat.submat(_rowInd, _colInd, size(_matSize.first, _matSize.second)) += _genColVec;

        cout << "- Compute expectedMatSubmatPlus() ... ";
        save<double>("Mat.submatPlus", _genMat);
        cout << "done." << endl;
      }

      void expectedMatSubmatMinus() {
        if(!_genMat.in_range(_rowInd, _colInd, size(_matSize.first, _matSize.second))) {
          return;
        }

        if(_genColVec.n_rows != _matSize.first) {
          return;
        }

        if(_matSize.second != 1) {
          return;
        }

        _genMat.submat(_rowInd, _colInd, size(_matSize.first, _matSize.second)) -= _genColVec;

        cout << "- Compute expectedMatSubmatMinus() ... ";
        save<double>("Mat.submatMinus", _genMat);
        cout << "done." << endl;
      }

      void expectedMatSubmatElemTimes() {
        if(!_genMat.in_range(_rowInd, _colInd, size(_matSize.first, _matSize.second))) {
          return;
        }

        if(_genColVec.n_rows != _matSize.first) {
          return;
        }

        if(_matSize.second != 1) {
          return;
        }

        _genMat.submat(_rowInd, _colInd, size(_matSize.first, _matSize.second)) %= _genColVec;

        cout << "- Compute expectedMatSubmatElemTimes() ... ";
        save<double>("Mat.submatElemTimes", _genMat);
        cout << "done." << endl;
      }

      void expectedMatSubmatElemDivide() {
        if(!_genMat.in_range(_rowInd, _colInd, size(_matSize.first, _matSize.second))) {
          return;
        }

        if(_genColVec.n_rows != _matSize.first) {
          return;
        }

        if(_matSize.second != 1) {
          return;
        }

        _genMat.submat(_rowInd, _colInd, size(_matSize.first, _matSize.second)) /= _genColVec;

        cout << "- Compute expectedMatSubmatDivide() ... ";
        save<double>("Mat.submatElemDivide", _genMat);
        cout << "done." << endl;
      }
  };
}
