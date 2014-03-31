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
using arma::Col;
using arma::Mat;
using arma::stddev;
using arma::var;
using arma::cor;
using arma::cov;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedGenColVecNormal : public Expected {
    public:
      ExpectedGenColVecNormal() {
        cout << "Compute ExpectedGenColVecNormal(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({InputClass::GenColVec, InputClass::Normal});

        for (vector<pair<string, void*>> input : inputs) {
          _fileSuffix = "";

          int n = 0;
          for (pair<string, void*> value : input) {
            switch (n) {
              case 0:
                _fileSuffix += value.first;
                _genColVec = *static_cast<Col<double>*>(value.second);
                break;
              case 1:
                _fileSuffix += "," + value.first;
                _normal = *static_cast<int*>(value.second);
                break;
            }
            ++n;
          }

          cout << "Using input: " << _fileSuffix << endl;

          expectedStddev();
          expectedVar();
          expectedCor();
          expectedCov();
        }

        cout << "done." << endl;
      }

    protected:
      Col<double> _genColVec;
      int _normal;

      void expectedStddev() {
        cout << "- Compute expectedStddev() ... ";
        save("stddev", Mat<double>({stddev(_genColVec, _normal)}));
        cout << "done." << endl;
      }

      void expectedVar() {
        cout << "- Compute expectedVar() ... ";
        save("var", Mat<double>({var(_genColVec, _normal)}));
        cout << "done." << endl;
      }

      void expectedCor() {
        cout << "- Compute expectedVar() ... ";
        save("cor", Mat<double>({cor(_genColVec, _normal)}));
        cout << "done." << endl;
      }

      void expectedCov() {
        cout << "- Compute expectedVar() ... ";
        save("cov", Mat<double>({cov(_genColVec, _normal)}));
        cout << "done." << endl;
      }

  };
}
