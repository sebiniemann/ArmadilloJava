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
  class ExpectedInPlaceGenMatRowIndColIndMatSizeGenDouble : public Expected {
    public:
      ExpectedInPlaceGenMatRowIndColIndMatSizeGenDouble() {
        cout << "Compute ExpectedInPlaceGenMatRowIndColIndMatSizeGenDouble(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::GenMat,
            InputClass::RowInd,
            InputClass::ColInd,
            InputClass::MatSize,
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
                  _genDouble = *static_cast<double*>(value.second);
                  break;
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            _copyOfGenMat = _genMat;
            _copyOfRowInd = _rowInd;
            _copyOfColInd = _colInd;
            _copyOfMatSize = _matSize;
            _copyOfGenDouble = _genDouble;

            expectedMatSubmatPlus();

            _genMat = _copyOfGenMat;
            _rowInd = _copyOfRowInd;
            _colInd = _copyOfColInd;
            _matSize = _copyOfMatSize;
            _genDouble = _copyOfGenDouble;
            expectedMatSubmatMinus();

            _genMat = _copyOfGenMat;
            _rowInd = _copyOfRowInd;
            _colInd = _copyOfColInd;
            _matSize = _copyOfMatSize;
            _genDouble = _copyOfGenDouble;
            expectedMatSubmatTimes();

            _genMat = _copyOfGenMat;
            _rowInd = _copyOfRowInd;
            _colInd = _copyOfColInd;
            _matSize = _copyOfMatSize;
            _genDouble = _copyOfGenDouble;
            expectedMatSubmatDivide();
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

      double _genDouble;
      double _copyOfGenDouble;

      void expectedMatSubmatPlus() {
        if(!_genMat.in_range(_rowInd, _colInd, size(_matSize.first, _matSize.second))) {
          return;
        }

        _genMat.submat(_rowInd, _colInd, size(_matSize.first, _matSize.second)) += _genDouble;

        cout << "- Compute expectedMatSubmatPlus() ... ";
        save<double>("Mat.submatPlus", _genMat);
        cout << "done." << endl;
      }

      void expectedMatSubmatMinus() {
        if(!_genMat.in_range(_rowInd, _colInd, size(_matSize.first, _matSize.second))) {
          return;
        }

        _genMat.submat(_rowInd, _colInd, size(_matSize.first, _matSize.second)) -= _genDouble;

        cout << "- Compute expectedMatSubmatMinus() ... ";
        save<double>("Mat.submatMinus", _genMat);
        cout << "done." << endl;
      }

      void expectedMatSubmatTimes() {
        if(!_genMat.in_range(_rowInd, _colInd, size(_matSize.first, _matSize.second))) {
          return;
        }

        _genMat.submat(_rowInd, _colInd, size(_matSize.first, _matSize.second)) *= _genDouble;

        cout << "- Compute expectedMatSubmatTimes() ... ";
        save<double>("Mat.submatTimes", _genMat);
        cout << "done." << endl;
      }

      void expectedMatSubmatDivide() {
        if(!_genMat.in_range(_rowInd, _colInd, size(_matSize.first, _matSize.second))) {
          return;
        }

        _genMat.submat(_rowInd, _colInd, size(_matSize.first, _matSize.second)) /= _genDouble;

        cout << "- Compute expectedMatSubmatDivide() ... ";
        save<double>("Mat.submatDivide", _genMat);
        cout << "done." << endl;
      }
  };
}
