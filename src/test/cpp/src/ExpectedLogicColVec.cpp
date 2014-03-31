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
using arma::uword;
using arma::raw_ascii;
using arma::all;
using arma::any;
using arma::find;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedLogicColVec : public Expected {
    public:
      ExpectedLogicColVec() {
        cout << "Compute ExpectedLogicColVec(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({InputClass::LogicColVec});

        for (vector<pair<string, void*>> input : inputs) {
          _fileSuffix = "";

          int n = 0;
          for (pair<string, void*> value : input) {
            switch (n) {
              case 0:
                _fileSuffix += value.first;
                _logicColVec = *static_cast<Col<double>*>(value.second);
                break;
            }
            ++n;
          }

          cout << "Using input: " << _fileSuffix << endl;

          expectedAll();
          expectedAny();
          expectedFind();
        }

        cout << "done." << endl;
      }

    protected:
      Col<double> _logicColVec;

      void expectedAll() {
        cout << "- Compute expectedAll() ... ";

        if(all(_logicColVec)) {
          save("all", Mat<double>({1.0}));
        } else {
          save("all", Mat<double>({0.0}));
        }

        cout << "done." << endl;
      }

      void expectedAny() {
        cout << "- Compute expectedAny() ... ";

        if(any(_logicColVec)) {
          save("any", Mat<double>({1.0}));
        } else {
          save("any", Mat<double>({0.0}));
        }

        cout << "done." << endl;
      }

      void expectedFind() {
        cout << "- Compute expectedFind() ... ";
        // Unable to convert the result of find(...) to Mat<double>
        Mat<uword> expected = find(_logicColVec);
        expected.save("../data/expected/find" + _fileSuffix + ".mat", raw_ascii);
        cout << "done." << endl;
      }

  };
}
