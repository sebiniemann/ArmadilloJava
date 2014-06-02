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
  class ExpectedInPlaceGenColVecNumElems: public Expected {
    public:
      ExpectedInPlaceGenColVecNumElems() {
        cout << "Compute ExpectedInPlaceGenColVecNumElems(): " << endl;

        vector < vector<pair<string, void*>>> inputs = Input::getTestParameters({
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
                _genColVec = *static_cast<Col<double>*>(value.second);
                break;
              case 1:
                _fileSuffix += "," + value.first;
                _numElems = *static_cast<int*>(value.second);
                break;
            }
            ++n;
          }

          cout << "Using input: " << _fileSuffix << endl;

          _copyOfGenColVec = _genColVec;
          _copyOfNumElems = _numElems;

          expectedColVecOnes();

          _genColVec = _copyOfGenColVec;
          _numElems = _copyOfNumElems;
          expectedColVecZeros();

          _genColVec = _copyOfGenColVec;
          _numElems = _copyOfNumElems;
          expectedColVecResize();
        }

        cout << "done." << endl;
      }

    protected:
      Col<double> _genColVec;
      Col<double> _copyOfGenColVec;

      int _numElems;
      int _copyOfNumElems;

      void expectedColVecOnes() {
        cout << "- Compute expectedColVecOnes() ... ";

        _genColVec.ones(_numElems);
        save<double>("Col.ones", _genColVec);

        cout << "done." << endl;
      }

      void expectedColVecZeros() {
        cout << "- Compute expectedColVecZeros() ... ";

        _genColVec.zeros(_numElems);
        save<double>("Col.zeros", _genColVec);

        cout << "done." << endl;
      }

      void expectedColVecResize() {
        cout << "- Compute expectedColVecResize() ... ";

        _genColVec.resize(_numElems);
        save<double>("Col.resize", _genColVec);

        cout << "done." << endl;
      }
  };
}
