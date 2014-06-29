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
using arma::span;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedInPlaceGenColVecElemIndRange : public Expected {
    public:
      ExpectedInPlaceGenColVecElemIndRange() {
        cout << "Compute ExpectedInPlaceGenColVecElemIndRange(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::GenColVec,
            InputClass::ElemIndRange
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
                  _indRange = *static_cast<span*>(value.second);
                  break;
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            _copyOfGenColVec = _genColVec;
            _copyOfIndRange = _indRange;

            expectedColSwapRows();
            
          }

          cout << "done." << endl;
        }

    protected:
      Col<double> _genColVec;
      Col<double> _copyOfGenColVec;

      span _indRange;
      span _copyOfIndRange;

      void expectedColSwapRows() {
        if(!_genColVec.in_range(_indRange)) {
          return;
        }

        cout << "- Compute expectedColSwapRows() ... ";

        _genColVec.swap_rows(_indRange.a, _indRange.b);
        save<double>("Col.swapRows", _genColVec);

        cout << "done." << endl;
      }
      
  };
}
