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
using arma::shuffle;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedInPlaceRandomGenColVecNumElems : public Expected {
    public:
      ExpectedInPlaceRandomGenColVecNumElems() {
        cout << "Compute ExpectedInPlaceRandomGenColVecNumElems(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::Random,
            InputClass::GenColVec,
            InputClass::NumElems
          });

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
                  _genColVec = *static_cast<Col<double>*>(value.second);
                  break;
                case 2:
                  _fileSuffix += "," + value.first;
                  _numElems = *static_cast<int*>(value.second);
                  break;
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            _copyOfRandom = _random;
            _copyOfGenColVec = _genColVec;
            _copyOfNumElems = _numElems;

            expectedColRandu();

            _random = _copyOfRandom;
            _genColVec = _copyOfGenColVec;
            _numElems = _copyOfNumElems;
            expectedColRandn();
          }

          cout << "done." << endl;
        }

    protected:
      int _random;
      int _copyOfRandom;

      Col<double> _genColVec;
      Col<double> _copyOfGenColVec;
      
      int _numElems;
      int _copyOfNumElems;

      void expectedColRandu() {
        cout << "- Compute expectedColRandu() ... ";

        _genColVec.randu(_numElems);
        Col<double> result = _genColVec;

        for(int n = 2; n <= _random; n++) {
          _genColVec.randu(_numElems);
          result = (result * n + _genColVec) / (n + 1);
        }

        save<double>("Col.randu", result);

        cout << "done." << endl;
      }

    void expectedColRandn() {
      cout << "- Compute expectedColRandn() ... ";

      _genColVec.randn(_numElems);
      Col<double> result = _genColVec;

      for(int n = 2; n <= _random; n++) {
        _genColVec.randn(_numElems);
        result = (result * n + _genColVec) / (n + 1);
      }

      save<double>("Col.randn", result);

      cout << "done." << endl;
    }
  };
}
