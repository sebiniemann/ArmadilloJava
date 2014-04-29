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
using arma::eye;
using arma::ones;
using arma::zeros;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedNumRowsNumCols : public Expected {
    public:
      ExpectedNumRowsNumCols() {
        cout << "Compute ExpectedNumRowsNumCols(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
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
                _numRows = *static_cast<int*>(value.second);
                break;
              case 1:
                _fileSuffix += "," + value.first;
                _numCols = *static_cast<int*>(value.second);
                break;
            }
            ++n;
          }

          cout << "Using input: " << _fileSuffix << endl;

          expectedArmaEye();
          expectedArmaOnes();
          expectedArmaZeros();
        }

        cout << "done." << endl;
      }

    protected:
      int _numRows;
      int _numCols;

      void expectedArmaEye() {
        cout << "- Compute expectedArmaEye() ... ";
        save<double>("Arma.eye", eye<Mat<double>>(_numRows, _numCols));
        cout << "done." << endl;
      }

      void expectedArmaOnes() {
        cout << "- Compute expectedArmaOnes() ... ";
        save<double>("Arma.ones", ones<Mat<double>>(_numRows, _numCols));
        cout << "done." << endl;
      }

      void expectedArmaZeros() {
        cout << "- Compute expectedArmaZeros() ... ";
        save<double>("Arma.zeros", zeros<Mat<double>>(_numRows, _numCols));
        cout << "done." << endl;
      }

  };
}
