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
  class ExpectedInPlaceGenMatNumRowsNumCols : public Expected {
    public:
      ExpectedInPlaceGenMatNumRowsNumCols() {
        cout << "Compute ExpectedInPlaceGenMatNumRowsNumCols(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
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
                  _genMat = *static_cast<Mat<double>*>(value.second);
                  break;
                case 1:
                  _fileSuffix += "," + value.first;
                  _numRows = *static_cast<int*>(value.second);
                  break;
                case 2:
                  _fileSuffix += "," + value.first;
                  _numCols = *static_cast<int*>(value.second);
                  break;
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            _copyOfGenMat = _genMat;
            _copyOfNumRows = _numRows;
            _copyOfNumCols = _numCols;

            expectedMatOnes();

            _genMat = _copyOfGenMat;
            _numRows = _copyOfNumRows;
            _numCols = _copyOfNumCols;
            expectedMatZeros();

            _genMat = _copyOfGenMat;
            _numRows = _copyOfNumRows;
            _numCols = _copyOfNumCols;
            expectedMatEye();

            _genMat = _copyOfGenMat;
            _numRows = _copyOfNumRows;
            _numCols = _copyOfNumCols;
            expectedMatResize();

            _genMat = _copyOfGenMat;
            _numRows = _copyOfNumRows;
            _numCols = _copyOfNumCols;
            expectedMatReshape();
          }

          cout << "done." << endl;
        }

    protected:
      Mat<double> _genMat;
      Mat<double> _copyOfGenMat;

      int _numRows;
      int _copyOfNumRows;

      int _numCols;
      int _copyOfNumCols;

      void expectedMatOnes() {
        cout << "- Compute expectedMatOnes() ... ";

        _genMat.ones(_numRows, _numCols);
        save<double>("Mat.ones", _genMat);

        cout << "done." << endl;
      }

      void expectedMatZeros() {
        cout << "- Compute expectedMatZeros() ... ";

        _genMat.zeros(_numRows, _numCols);
        save<double>("Mat.zeros", _genMat);

        cout << "done." << endl;
      }

      void expectedMatEye() {
        cout << "- Compute expectedMatEye() ... ";

        _genMat.eye(_numRows, _numCols);
        save<double>("Mat.eye", _genMat);

        cout << "done." << endl;
      }

      void expectedMatResize() {
        cout << "- Compute expectedMatResize() ... ";

        _genMat.resize(_numRows, _numCols);
        save<double>("Mat.resize", _genMat);

        cout << "done." << endl;
      }

      void expectedMatReshape() {
        cout << "- Compute expectedMatReshape() ... ";

        _genMat.reshape(_numRows, _numCols);
        save<double>("Mat.reshape", _genMat);

        cout << "done." << endl;
      }
  };
}
