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

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedGenColVecElemInd : public Expected {
    public:
      ExpectedGenColVecElemInd() {
        cout << "Compute ExpectedGenColVecElemInd(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::GenColVec,
            InputClass::ElemInd
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
                  _elemInd = *static_cast<int*>(value.second);
                  break;
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            expectedColVecAt();
            expectedColVecIn_range();
          }

          cout << "done." << endl;
        }

    protected:
      Col<double> _genColVec;
      int _elemInd;

      void expectedColVecAt() {
        if(_elemInd >= _genColVec.n_elem) {
          return;
        }

        cout << "- Compute expectedColVecAt() ... ";
        save<double>("ColVec.at", Col<double>({_genColVec.at(_elemInd)}));
        cout << "done." << endl;
      }

      void expectedColVecIn_range() {
        cout << "- Compute expectedColVecIn_range() ... ";

        if(_genColVec.in_range(_elemInd)) {
          save<double>("ColVec.in_range", Col<double>({1}));
        } else {
          save<double>("ColVec.in_range", Col<double>({0}));
        }

        cout << "done." << endl;
      }
  };
}
