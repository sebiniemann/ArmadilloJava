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
using arma::repmat;
using arma::reshape;
using arma::resize;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedGenMatNumRowsNumCols : public Expected {
    public:
      ExpectedGenMatNumRowsNumCols() {
        cout << "Compute ExpectedGenMatNumRowsNumCols(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({InputClass::GenMat, InputClass::NumRows, InputClass::NumCols});

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

          expectedRepmat();
          expectedReshape();
          expectedResize();
        }

        cout << "done." << endl;
      }

    protected:
      Mat<double> _genMat;
      int _numRows;
      int _numCols;

      void expectedRepmat() {
        cout << "- Compute expectedRepmat() ... ";
        save("repmat", repmat(_genMat, _numRows, _numCols));
        cout << "done." << endl;
      }

      void expectedReshape() {
        cout << "- Compute expectedReshape() ... ";
        save("reshape", reshape(_genMat, _numRows, _numCols));
        cout << "done." << endl;
      }

      void expectedResize() {
        cout << "- Compute expectedResize() ... ";
        save("resize", resize(_genMat, _numRows, _numCols));
        cout << "done." << endl;
      }

  };
}
