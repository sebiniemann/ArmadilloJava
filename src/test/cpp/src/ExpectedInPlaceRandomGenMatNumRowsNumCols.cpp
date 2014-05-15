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
  class ExpectedInPlaceRandomGenMatNumRowsNumCols : public Expected {
    public:
      ExpectedInPlaceRandomGenMatNumRowsNumCols() {
        cout << "Compute ExpectedInPlaceRandomGenMatNumRowsNumCols(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::Random,
            InputClass::GenMat,
            InputClass::NumRows,
            InputClass::NumCols
          });

          for (vector<pair<string, void*>> input : inputs) {
            _fileSuffix = "";

            int n = 0;
            for (pair<string, void*> value : input) {
              switch (n) {
                case 0:
                  _fileSuffix += value.first;
                  _random = *static_cast<int*>(value.second);
                  break;
                case 1:
                  _fileSuffix += "," + value.first;
                  _genMat = *static_cast<Mat<double>*>(value.second);
                  break;
                case 2:
                  _fileSuffix += "," + value.first;
                  _numRows = *static_cast<int*>(value.second);
                  break;
                case 3:
                  _fileSuffix += "," + value.first;
                  _numCols = *static_cast<int*>(value.second);
                  break;
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            _copyOfRandom = _random;
            _copyOfGenMat = _genMat;
            _copyOfNumRows = _numRows;
            _copyOfNumCols = _numCols;

            expectedMatRandu();

            _random = _copyOfRandom;
            _genMat = _copyOfGenMat;
            _numRows = _copyOfNumRows;
            _numCols = _copyOfNumCols;
            expectedMatRandn();
          }

          cout << "done." << endl;
        }

    protected:
      int _random;
      int _copyOfRandom;

      Mat<double> _genMat;
      Mat<double> _copyOfGenMat;

      int _numRows;
      int _copyOfNumRows;

      int _numCols;
      int _copyOfNumCols;

      void expectedMatRandu() {
        cout << "- Compute expectedMatRandu() ... ";

        _genMat.randu(_numRows, _numCols);
        Mat<double> result = _genMat;

        for(int n = 2; n <= _random; n++) {
          _genMat.randu(_numRows, _numCols);
          result = (result * n + _genMat) / (n + 1);
        }

        save<double>("Mat.randu", result);
      }

      void expectedMatRandn() {
        cout << "- Compute expectedMatRandn() ... ";

        _genMat.randn(_numRows, _numCols);
        Mat<double> result = _genMat;

        for(int n = 2; n <= _random; n++) {
          _genMat.randn(_numRows, _numCols);
          result = (result * n + _genMat) / (n + 1);
        }

        save<double>("Mat.randn", result);

        cout << "done." << endl;
      }
  };
}
