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
using arma::uword;
using arma::all;
using arma::any;
using arma::find;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedLogicMat : public Expected {
    public:
      ExpectedLogicMat() {
        cout << "Compute ExpectedLogicMat(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({InputClass::LogicMat});

        for (vector<pair<string, void*>> input : inputs) {
          _fileSuffix = "";

          int n = 0;
          for (pair<string, void*> value : input) {
            switch (n) {
              case 0:
                _fileSuffix += value.first;
                _logicMat = *static_cast<Mat<double>*>(value.second);
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
      Mat<double> _logicMat;

      void expectedArmaAll() {
        cout << "- Compute expectedArmaAll() ... ";
        save<uword>("Arma.all", all(_logicMat));
        cout << "done." << endl;
      }

      void expectedArmaAny() {
        cout << "- Compute expectedArmaAny() ... ";
        save<uword>("Arma.any", any(_logicMat));
        cout << "done." << endl;
      }

      void expectedArmaFind() {
        cout << "- Compute expectedArmaFind() ... ";
        save<uword>("Arma.find", find(_logicMat));
        cout << "done." << endl;
      }

  };
}
