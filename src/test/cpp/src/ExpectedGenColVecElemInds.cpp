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

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedGenColVecElemInds : public Expected {
    public:
      ExpectedGenColVecElemInds() {
        cout << "Compute ExpectedGenColVecElemInds(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
          InputClass::GenColVec,
          InputClass::ElemInds
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
                _elemInds = *static_cast<Col<uword>*>(value.second);
                break;
            }
            ++n;
          }

          cout << "Using input: " << _fileSuffix << endl;

          expectedColElem();
          expectedColElem();
        }

        cout << "done." << endl;
      }

    protected:
      Mat<double> _genColVec;
      Col<uword> _elemInds;

      void expectedColElem() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genColVec.in_range(_elemInds.at(n))) {
            return;
          }
        }

        cout << "- Compute expectedColElem() ... ";
        save<double>("Col.elem", _genColVec.elem(_elemInds));
        cout << "done." << endl;
      }

      void expectedColRows() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genColVec.in_range(_elemInds.at(n))) {
            return;
          }
        }

        cout << "- Compute expectedColRows() ... ";
        save<double>("Col.rows", _genColVec.rows(_elemInds));
        cout << "done." << endl;
      }

  };
}
