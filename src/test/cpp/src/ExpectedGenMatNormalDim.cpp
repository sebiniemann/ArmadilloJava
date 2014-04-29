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
using arma::stddev;
using arma::var;
using arma::cor;
using arma::cov;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedGenMatNormalDim : public Expected {
    public:
      ExpectedGenMatNormalDim() {
        cout << "Compute ExpectedGenMatNormalDim(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
          InputClass::GenMat,
          InputClass::Normal,
          InputClass::Dim
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
                _normal = *static_cast<int*>(value.second);
                break;
              case 2:
                _fileSuffix += "," + value.first;
                _dim = *static_cast<int*>(value.second);
                break;
            }
            ++n;
          }

          cout << "Using input: " << _fileSuffix << endl;

          expectedArmaStddev();
          expectedArmaVar();
        }

        cout << "done." << endl;
      }

    protected:
      Mat<double> _genMat;
      int _normal;
      int _dim;

      void expectedArmaStddev() {
        cout << "- Compute expectedArmaStddev() ... ";
        save<double>("Arma.stddev", stddev(_genMat, _normal, _dim));
        cout << "done." << endl;
      }

      void expectedArmaVar() {
        cout << "- Compute expectedArmaVar() ... ";
        save<double>("Arma.var", var(_genMat, _normal, _dim));
        cout << "done." << endl;
      }

  };
}
