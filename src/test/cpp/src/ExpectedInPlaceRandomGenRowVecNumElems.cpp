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
using arma::shuffle;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedInPlaceRandomGenRowVecNumElems : public Expected {
    public:
      ExpectedInPlaceRandomGenRowVecNumElems() {
        cout << "Compute ExpectedInPlaceRandomGenRowVecNumElems(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::Random,
            InputClass::GenRowVec,
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
                  _genRowVec = *static_cast<Row<double>*>(value.second);
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
            _copyOfGenRowVec = _genRowVec;
            _copyOfNumElems = _numElems;

            expectedRowRandu();

            _random = _copyOfRandom;
            _genRowVec = _copyOfGenRowVec;
            _numElems = _copyOfNumElems;
            expectedRowRandn();
          }

          cout << "done." << endl;
        }

    protected:
      int _random;
      int _copyOfRandom;

      Row<double> _genRowVec;
      Row<double> _copyOfGenRowVec;
      
      int _numElems;
      int _copyOfNumElems;

      void expectedRowRandu() {
        cout << "- Compute expectedRowRandu() ... ";

        _genRowVec.randu(_numElems);
        Row<double> result = _genRowVec;

        for(int n = 2; n <= _random; n++) {
          _genRowVec.randu(_numElems);
          result = (result * n + _genRowVec) / (n + 1);
        }

        save<double>("row.randu", result);

        cout << "done." << endl;
      }

    void expectedRowRandn() {
      cout << "- Compute expectedRowRandn() ... ";

      _genRowVec.randn(_numElems);
      Row<double> result = _genRowVec;

      for(int n = 2; n <= _random; n++) {
        _genRowVec.randn(_numElems);
        result = (result * n + _genRowVec) / (n + 1);
      }

      save<double>("row.randn", result);

      cout << "done." << endl;
    }
  };
}
