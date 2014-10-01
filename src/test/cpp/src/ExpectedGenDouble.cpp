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
using arma::is_finite;
using arma::rowvec;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

#include <vector>

namespace armadilloJava {
  class ExpectedGenDouble : public Expected {
    public:
      ExpectedGenDouble() {
        cout << "Compute ExpectedGenDouble(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
          InputClass::GenDouble
        });

        int i = 0;

        for (vector<pair<string, void*>> input : inputs) {
          _fileSuffix = "";

          int n = 0;
          for (pair<string, void*> value : input) {
            switch (n) {
              case 0:
                _fileSuffix += value.first;
                _genDouble = *static_cast<double*>(value.second);
                break;
            }
            ++n;
          }
          _genDoubleArray.push_back(_genDouble);
          ++i;

          cout << "Using input: " << _fileSuffix << endl;

          expectedArmaIs_finite();
        }

        expectedRowVecDoubleArray();
        cout << "done." << endl;
      }

    protected:
      double _genDouble;
      std::vector<double> _genDoubleArray;

      void expectedArmaIs_finite() {
        cout << "- Compute expectedArmaIs_finite() ... ";

        if(is_finite(_genDouble)) {
          save<double>("Arma.is_finite", Mat<double>({1.0}));
        } else {
          save<double>("Arma.is_finite", Mat<double>({0.0}));
        }

        cout << "done." << endl;
      }

      void expectedRowVecDoubleArray() {
        cout << "- Compute expectedRowVecDoubleArray() ... ";

        save<double>("Row.doubleArray", rowvec(_genDoubleArray));

        cout << "done." << endl;
      }

  };
}
