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
using arma::span;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedInPlaceGenRowVecIndRange : public Expected {
    public:
      ExpectedInPlaceGenRowVecIndRange() {
        cout << "Compute ExpectedInPlaceGenRowVecIndRange(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::GenRowVec,
            InputClass::ElemIndRange
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
                  _indRange = *static_cast<span*>(value.second);
                  break;
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            _copyOfGenRowVec = _genRowVec;
            _copyOfIndRange = _indRange;

            expectedRowSwapCols();
            
          }

          cout << "done." << endl;
        }

    protected:
      Row<double> _genRowVec;
      Row<double> _copyOfGenRowVec;

      span _indRange;
      span _copyOfIndRange;

      void expectedRowSwapCols() {
        if(!_genRowVec.in_range(_indRange)) {
          return;
        }

        cout << "- Compute expectedRowSwapCols() ... ";

        _genRowVec.swap_cols(_indRange.a, _indRange.b);
        save<double>("row.swapCols", _genRowVec);

        cout << "done." << endl;
      }
      
  };
}
