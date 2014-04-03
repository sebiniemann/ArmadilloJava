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

#include <stdexcept>
using std::runtime_error;

#include <armadillo>
using arma::Mat;
using arma::Col;
using arma::syl;
using arma::join_cols;
using arma::unique;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedSquMatSquMatGenMat : public Expected {
    public:
      ExpectedSquMatSquMatGenMat() {
        cout << "Compute ExpectedSquMatSquMatGenMat(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({InputClass::SquMat, InputClass::SquMat, InputClass::GenMat});

        for (vector<pair<string, void*>> input : inputs) {
          _fileSuffix = "";

          int n = 0;
          for (pair<string, void*> value : input) {
            switch (n) {
              case 0:
                _fileSuffix += value.first;
                _squMatA = *static_cast<Mat<double>*>(value.second);
                break;
              case 1:
                _fileSuffix += "," + value.first;
                _squMatB = *static_cast<Mat<double>*>(value.second);
                break;
              case 2:
                _fileSuffix += "," + value.first;
                _genMat = *static_cast<Mat<double>*>(value.second);
                break;
            }
            ++n;
          }

          cout << "Using input: " << _fileSuffix << endl;

          expectedArmaSyl();
        }

        cout << "done." << endl;
      }

    protected:
      Mat<double> _squMatA;
      Mat<double> _squMatB;
      Mat<double> _genMat;

      void expectedArmaSyl() {
        if (_genMat.n_rows != _squMatA.n_rows) {
          return;
        }

        if (_genMat.n_cols != _squMatB.n_cols) {
          return;
        }

        cout << "- Compute expectedArmaSyl() ... ";
        save<double>("Arma.syl", syl(_squMatA, _squMatB, _genMat));
        cout << "done." << endl;
      }

  };
}
