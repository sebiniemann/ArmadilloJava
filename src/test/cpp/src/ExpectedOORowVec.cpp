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
using arma::Row;
using arma::as_scalar;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedOORowVec : public Expected {
    public:
      ExpectedOORowVec() {
        cout << "Compute ExpectedOORowVec(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({InputClass::OORowVec});

        for (vector<pair<string, void*>> input : inputs) {
          _fileSuffix = "";

          int n = 0;
          for (pair<string, void*> value : input) {
            switch (n) {
              case 0:
                _fileSuffix += value.first;
                _ooRowVec = *static_cast<Row<double>*>(value.second);
                break;
            }
          }

          cout << "Using input: " << _fileSuffix << endl;

          expectedArmaAs_scalar();
        }

        cout << "done." << endl;
      }

    protected:
      Mat<double> _ooRowVec;

      void expectedArmaAs_scalar() {
        cout << "- Compute expectedArmaAs_scalar() ... ";
        save<double>("Arma.as_scalar", Mat<double>({as_scalar(_ooRowVec)}));
        cout << "done." << endl;
      }
  };
}
