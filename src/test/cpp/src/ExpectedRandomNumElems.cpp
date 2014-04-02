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
using arma::randi;
using arma::randu;
using arma::randn;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedRandomNumElems : public Expected {
    public:
      ExpectedRandomNumElems() {
        cout << "Compute ExpectedRandomNumElems(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({InputClass::Random, InputClass::NumElems});

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
                _numElems = *static_cast<int*>(value.second);
                break;
            }
            ++n;
          }

          cout << "Using input: " << _fileSuffix << endl;

          expectedRandi();
          expectedRandu();
          expectedRandn();
        }

        cout << "done." << endl;
      }

    protected:
      int _random;
      int _numElems;

      void expectedRandi() {
        cout << "- Compute expectedRandi() ... ";

        Mat<double> result = randi<Col<double>>(_numElems) / arma::arma_rng::randi<int>::max_val();
        for(int n = 2; n <= _random; n++) {
          result = (result * n + (randi<Col<double>>(_numElems) / arma::arma_rng::randi<int>::max_val())) / (n + 1);
        }
        save("randi", result);

        cout << "done." << endl;
      }

      void expectedRandu() {
        cout << "- Compute expectedRandu() ... ";

        Mat<double> result = randu<Col<double>>(_numElems);
        for(int n = 2; n <= _random; n++) {
          result = (result * n + randu<Col<double>>(_numElems)) / (n + 1);
        }
        save("randu", result);

        cout << "done." << endl;
      }

      void expectedRandn() {
        cout << "- Compute expectedRandn() ... ";

        Mat<double> result = randn<Col<double>>(_numElems);
        for(int n = 2; n <= _random; n++) {
          result = (result * n + randn<Col<double>>(_numElems)) / (n + 1);
        }
        save("randn", result);

        cout << "done." << endl;
      }

  };
}
