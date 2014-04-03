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
using arma::rank;
using arma::pinv;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedGenMatSinValTol : public Expected {
    public:
      ExpectedGenMatSinValTol() {
        cout << "Compute ExpectedGenMatSinValTol(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({InputClass::GenMat, InputClass::SinValTol});

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
                  _sinValTol = *static_cast<double*>(value.second);
                  break;
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            expectedArmaRank();
            expectedArmaPinv();
          }

          cout << "done." << endl;
        }

    protected:
      Mat<double> _genMat;
      double _sinValTol;

      void expectedArmaRank() {
        cout << "- Compute expectedArmaRank() ... ";
        save<double>("Arma.rank", Mat<double>({static_cast<double>(rank(_genMat, _sinValTol))}));
        cout << "done." << endl;
      }

      void expectedArmaPinv() {
        cout << "- Compute expectedArmaPinv() ... ";
        save<double>("Arma.pinv", pinv(_genMat, _sinValTol));
        cout << "done." << endl;
      }
  };
}
