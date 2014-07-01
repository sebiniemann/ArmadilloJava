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

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedInPlaceGenRowVecExtElemIndNumElmes: public Expected {
    public:
      ExpectedInPlaceGenRowVecExtElemIndNumElmes() {
        cout << "Compute ExpectedInPlaceGenRowVecExtElemIndNumElmes(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters( {
            InputClass::GenRowVec,
            InputClass::ExtElemInd,
            InputClass::NumElems
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
                _extElemInd = *static_cast<int*>(value.second);
                break;
              case 2:
                _fileSuffix += "," + value.first;
                _numElems = *static_cast<int*>(value.second);
                break;
            }
            ++n;
          }

          cout << "Using input: " << _fileSuffix << endl;

          _copyOfGenRowVec = _genRowVec;
          _copyOfExtElemInd = _extElemInd;
          _copyOfNumElems = _numElems;

          expectedRowVecInsertCols();
        }

        cout << "done." << endl;
      }

    protected:
      Row<double> _genRowVec;
      Row<double> _copyOfGenRowVec;

      int _extElemInd;
      int _copyOfExtElemInd;

      int _numElems;
      int _copyOfNumElems;

      void expectedRowVecInsertCols() {
        if (_extElemInd > _genRowVec.n_elem) {
          return;
        }

        cout << "- Compute expectedRowVecInsertCols() ... ";

        _genRowVec.insert_cols(_extElemInd, _numElems);
        save<double>("Row.insert_cols", _genRowVec);

        cout << "done." << endl;
      }

  };
}
