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

#include <string>
using std::string;

#include <utility>
using std::pair;

#include <armadillo>
using arma::Mat;
using arma::Col;
using arma::svd_econ;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedGenMatSinValSel : public Expected {
    public:
      ExpectedGenMatSinValSel() {
        cout << "Compute ExpectedGenMatSinValSel(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({InputClass::GenMat, InputClass::SinValSel});

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
                  _sinValSel = *static_cast<string*>(value.second);
                  break;
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            expectedSvd_econ();
          }

          cout << "done." << endl;
        }

    protected:
      Mat<double> _genMat;
      string _sinValSel;

      void expectedSvd_econ() {
        cout << "- Compute expectedSvd_econ() ... ";

        Mat<double> U, V;
        Col<double> s;

        svd_econ(U, s, V, _genMat, _sinValSel.c_str());
        save("svd_econ", Mat<double>(s));

        cout << "done." << endl;
      }
  };
}
