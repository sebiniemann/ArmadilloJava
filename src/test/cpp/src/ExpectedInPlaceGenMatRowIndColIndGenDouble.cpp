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
  class ExpectedInPlaceGenMatRowIndColIndGenDouble : public Expected {
    public:
      ExpectedInPlaceGenMatRowIndColIndGenDouble() {
        cout << "Compute ExpectedInPlaceGenMatRowIndColIndGenDouble(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::GenMat,
            InputClass::RowInd,
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
                  _rowInd = *static_cast<int*>(value.second);
                  break;
                case 2:
                  _fileSuffix += "," + value.first;
                  _colInd = *static_cast<int*>(value.second);
                  break;
                case 3:
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
            _copyOfGenDouble = _genDouble;

            expectedMatAtEqual();

            _genMat = _copyOfGenMat;
            _rowInd = _copyOfRowInd;
            _colInd = _copyOfColInd;
            _genDouble = _copyOfGenDouble;
            expectedMatAtPlus();

            _genMat = _copyOfGenMat;
            _rowInd = _copyOfRowInd;
            _colInd = _copyOfColInd;
            _genDouble = _copyOfGenDouble;
            expectedMatAtMinus();

            _genMat = _copyOfGenMat;
            _rowInd = _copyOfRowInd;
            _colInd = _copyOfColInd;
            _genDouble = _copyOfGenDouble;
            expectedMatAtTimes();

            _genMat = _copyOfGenMat;
            _rowInd = _copyOfRowInd;
            _colInd = _copyOfColInd;
            _genDouble = _copyOfGenDouble;
            expectedMatAtDivide();
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

      double _genDouble;
      double _copyOfGenDouble;

      void expectedMatAtEqual() {
        if(!_genMat.in_range(_rowInd, _colInd)) {
          return;
        }

        _genMat.at(_rowInd, _colInd) = _genDouble;

        cout << "- Compute expectedMatAtEqual() ... ";
        save<double>("Mat.atEqual", _genMat);
        cout << "done." << endl;
      }

      void expectedMatAtPlus() {
        if(!_genMat.in_range(_rowInd, _colInd)) {
          return;
        }

        _genMat.at(_rowInd, _colInd) += _genDouble;

        cout << "- Compute expectedMatAtPlus() ... ";
        save<double>("Mat.atPlus", _genMat);
        cout << "done." << endl;
      }

      void expectedMatAtMinus() {
        if(!_genMat.in_range(_rowInd, _colInd)) {
          return;
        }

        _genMat.at(_rowInd, _colInd) -= _genDouble;

        cout << "- Compute expectedMatAtMinus() ... ";
        save<double>("Mat.atMinus", _genMat);
        cout << "done." << endl;
      }

      void expectedMatAtTimes() {
        if(!_genMat.in_range(_rowInd, _colInd)) {
          return;
        }

        _genMat.at(_rowInd, _colInd) *= _genDouble;

        cout << "- Compute expectedMatAtTimes() ... ";
        save<double>("Mat.atTimes", _genMat);
        cout << "done." << endl;
      }

      void expectedMatAtDivide() {
        if(!_genMat.in_range(_rowInd, _colInd)) {
          return;
        }

        _genMat.at(_rowInd, _colInd) /= _genDouble;

        cout << "- Compute expectedMatAtDivide() ... ";
        save<double>("Mat.atDivide", _genMat);
        cout << "done." << endl;
      }
  };
}
