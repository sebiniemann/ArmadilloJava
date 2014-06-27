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

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedGenRowVecElemInds : public Expected {
    public:
      ExpectedGenRowVecElemInds() {
        cout << "Compute ExpectedGenRowVecElemInds(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
          InputClass::GenRowVec,
          InputClass::ElemInds
        });

        for (vector<pair<string, void*>> input : inputs) {
          _fileSuffix = "";

          int n = 0;
          for (pair<string, void*> value : input) {
            switch (n) {
              case 0:
                _fileSuffix += value.first;
                _genRowVec = *static_cast<Row<double>*>(value.second);
                break;
              case 1:
                _fileSuffix += "," + value.first;
                _elemInds = *static_cast<Col<uword>*>(value.second);
                break;
            }
            ++n;
          }

          cout << "Using input: " << _fileSuffix << endl;

          expectedElem();
          expectedCols();
        }

        cout << "done." << endl;
      }

    protected:
      Mat<double> _genRowVec;
      Col<uword> _elemInds;

      void expectedElem() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genRowVec.in_range(_elemInds.at(n))) {
            return;
          }
        }

        cout << "- Compute expectedElem() ... ";
        save<double>("Row.elem", _genRowVec.elem(_elemInds));
        cout << "done." << endl;
      }

      void expectedCols() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genRowVec.in_range(_elemInds.at(n))) {
            return;
          }
        }

        cout << "- Compute expectedCols() ... ";
        save<double>("Row.cols", _genRowVec.cols(_elemInds));
        cout << "done." << endl;
      }

  };
}
