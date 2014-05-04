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
using arma::span;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedInPlaceGenMatColIndRangeGenDouble : public Expected {
    public:
      ExpectedInPlaceGenMatColIndRangeGenDouble() {
        cout << "Compute ExpectedInPlaceGenMatColIndRangeGenDouble(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::GenMat,
            InputClass::ColIndRange,
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
                  _colIndRange = *static_cast<span*>(value.second);
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
            _copyOfColIndRange = _colIndRange;
            _copyOfGenDouble = _genDouble;

            expectedMatColsPlus();

            _genMat = _copyOfGenMat;
            _colIndRange = _copyOfColIndRange;
            _genDouble = _copyOfGenDouble;
            expectedMatColsMinus();

            _genMat = _copyOfGenMat;
            _colIndRange = _copyOfColIndRange;
            _genDouble = _copyOfGenDouble;
            expectedMatColsTimes();

            _genMat = _copyOfGenMat;
            _colIndRange = _copyOfColIndRange;
            _genDouble = _copyOfGenDouble;
            expectedMatColsDivide();
          }

          cout << "done." << endl;
        }

    protected:
      Mat<double> _genMat;
      Mat<double> _copyOfGenMat;

      span _colIndRange;
      span _copyOfColIndRange;

      double _genDouble;
      double _copyOfGenDouble;

      void expectedMatColsPlus() {
        if(_colIndRange.b >= _genMat.n_cols) {
          return;
        }

        cout << "- Compute expectedMatColsPlus() ... ";

        _genMat.cols(_colIndRange.a, _colIndRange.b) += _genDouble;
        save<double>("Mat.colsPlus", _genMat);

        cout << "done." << endl;
      }

      void expectedMatColsMinus() {
        if(_colIndRange.b >= _genMat.n_cols) {
          return;
        }

        cout << "- Compute expectedMatColsMinus() ... ";

        _genMat.cols(_colIndRange.a, _colIndRange.b) -= _genDouble;
        save<double>("Mat.colsMinus", _genMat);

        cout << "done." << endl;
      }

      void expectedMatColsTimes() {
        if(_colIndRange.b >= _genMat.n_cols) {
          return;
        }

        cout << "- Compute expectedMatColsTimes() ... ";

        _genMat.cols(_colIndRange.a, _colIndRange.b) *= _genDouble;
        save<double>("Mat.colsTimes", _genMat);

        cout << "done." << endl;
      }

      void expectedMatColsDivide() {
        if(_colIndRange.b >= _genMat.n_cols) {
          return;
        }

        cout << "- Compute expectedMatColsDivide() ... ";

        _genMat.cols(_colIndRange.a, _colIndRange.b) /= _genDouble;
        save<double>("Mat.colsDivide", _genMat);

        cout << "done." << endl;
      }
  };
}
