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
using arma::find;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedLogicColVecNumElems : public Expected {
    public:
      ExpectedLogicColVecNumElems() {
        cout << "Compute ExpectedLogicColVecNumElems(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({InputClass::LogicColVec, InputClass::NumElems});

        for (vector<pair<string, void*>> input : inputs) {
          _fileSuffix = "";

          int n = 0;
          for (pair<string, void*> value : input) {
            switch (n) {
              case 0:
                _fileSuffix += value.first;
                _logicColVec = *static_cast<Col<double>*>(value.second);
                break;
              case 1:
                _fileSuffix += "," + value.first;
                _numElems = *static_cast<int*>(value.second);
                break;
            }
            ++n;
          }

          cout << "Using input: " << _fileSuffix << endl;

          expectedFind();
        }

        cout << "done." << endl;
      }

    protected:
      Col<double> _logicColVec;
      int _numElems;

      void expectedFind() {
        cout << "- Compute expectedFind() ... ";
        // Unable to convert the result of find(...) to Mat<double>
        Col<uword> expected = find(_logicColVec, _numElems);
        expected.save("../data/expected/find" + _fileSuffix + ".mat", raw_ascii);
        cout << "done." << endl;
      }

  };
}