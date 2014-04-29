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

#include <string>
using std::string;

#include <utility>
using std::pair;

#include <armadillo>
using arma::Col;
using arma::norm;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedGenColVecVecNormString : public Expected {
    public:
      ExpectedGenColVecVecNormString() {
        cout << "Compute ExpectedGenColVecVecNormString(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
          InputClass::GenColVec,
          InputClass::VecNormString
        });

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
                _vecNormString = *static_cast<string*>(value.second);
                break;
            }
            ++n;
          }

          cout << "Using input: " << _fileSuffix << endl;

          expectedArmaNorm();
        }

        cout << "done." << endl;
      }

    protected:
      Col<double> _genColVec;
      string _vecNormString;

      void expectedArmaNorm() {
        cout << "- Compute expectedArmaNorm() ... ";
        save<double>("Arma.norm", Mat<double>({norm(_genColVec, _vecNormString.c_str())}));
        cout << "done." << endl;
      }

  };
}
