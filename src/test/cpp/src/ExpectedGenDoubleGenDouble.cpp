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
using arma::linspace;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedGenDoubleGenDouble : public Expected {
    public:
      ExpectedGenDoubleGenDouble() {
        cout << "Compute ExpectedGenDoubleGenDouble(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({InputClass::GenDouble, InputClass::GenDouble});

        for (vector<pair<string, void*>> input : inputs) {
          _fileSuffix = "";

          int n = 0;
          for (pair<string, void*> value : input) {
            switch (n) {
              case 0:
                _fileSuffix += value.first;
                _genDoubleA = *static_cast<double*>(value.second);
                break;
              case 1:
                _fileSuffix += "," + value.first;
                _genDoubleB = *static_cast<double*>(value.second);
                break;
            }
            ++n;
          }

          cout << "Using input: " << _fileSuffix << endl;

          expectedArmaLinspace();
        }

        cout << "done." << endl;
      }

    protected:
      double _genDoubleA;
      double _genDoubleB;

      void expectedArmaLinspace() {
        cout << "- Compute expectedArmaLinspace() ... ";
        save<double>("Arma.linspace", linspace(_genDoubleA, _genDoubleB));
        cout << "done." << endl;
      }

  };
}
