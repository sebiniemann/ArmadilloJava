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
                  _elemIndRange = *static_cast<span*>(value.second);
                  break;
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            _copyOfGenColVec = _genColVec;
            _copyOfElemIndRange = _elemIndRange;

            expectedColSwapRows();

            _genColVec = _copyOfGenColVec;
            _elemIndRange = _copyOfElemIndRange;

            expectedColVecShedRows();
          }

          cout << "done." << endl;
        }

    protected:
      Col<double> _genColVec;
      Col<double> _copyOfGenColVec;

      span _elemIndRange;
      span _copyOfElemIndRange;

      void expectedColSwapRows() {
        if(_elemIndRange.whole) {
          return;
        }

        if(!_genColVec.in_range(_elemIndRange)) {
          return;
        }

        cout << "- Compute expectedColSwapRows() ... ";

        _genColVec.swap_rows(_elemIndRange.a, _elemIndRange.b);
        save<double>("Col.swap_rows", _genColVec);

        cout << "done." << endl;
      }

      void expectedColVecShedRows() {
        if(_elemIndRange.whole) {
          return;
        }

        if(!_genColVec.in_range(_elemIndRange)) {
          return;
        }

        cout << "- Compute expectedColVecShedRows() ... ";

        _genColVec.shed_rows(_elemIndRange.a, _elemIndRange.b);
        save<double>("Col.shed_rows", _genColVec);

        cout << "done." << endl;
      }
      
  };
}
