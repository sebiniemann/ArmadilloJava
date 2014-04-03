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
using arma::uword;
using arma::all;
using arma::any;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedLogicRowVec : public Expected {
    public:
      ExpectedLogicRowVec() {
        cout << "Compute ExpectedLogicRowVec(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({InputClass::LogicRowVec});

        for (vector<pair<string, void*>> input : inputs) {
          _fileSuffix = "";

          int n = 0;
          for (pair<string, void*> value : input) {
            switch (n) {
              case 0:
                _fileSuffix += value.first;
                _logicRowVec = *static_cast<Row<double>*>(value.second);
                break;
            }
            ++n;
          }

          cout << "Using input: " << _fileSuffix << endl;

          expectedArmaAll();
          expectedArmaAny();
          expectedArmaFind();
        }

        cout << "done." << endl;
      }

    protected:
      Row<double> _logicRowVec;

      void expectedArmaAll() {
        cout << "- Compute expectedArmaAll() ... ";

        if(all(_logicRowVec)) {
          save<double>("Arma.all", Mat<double>({1.0}));
        } else {
          save<double>("Arma.all", Mat<double>({0.0}));
        }

        cout << "done." << endl;
      }

      void expectedArmaAny() {
        cout << "- Compute expectedArmaAny() ... ";

        if(any(_logicRowVec)) {
          save<double>("Arma.any", Mat<double>({1.0}));
        } else {
          save<double>("Arma.any", Mat<double>({0.0}));
        }

        cout << "done." << endl;
      }

      void expectedArmaFind() {
        cout << "- Compute expectedArmaFind() ... ";
        save<uword>("Arma.find", find(_logicRowVec));
        cout << "done." << endl;
      }

  };
}
