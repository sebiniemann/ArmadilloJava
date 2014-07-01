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
using arma::Mat;
using arma::span;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedInPlaceGenMatColIndRange : public Expected {
    public:
      ExpectedInPlaceGenMatColIndRange() {
        cout << "Compute ExpectedInPlaceGenMatColIndRange(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::GenMat,
            InputClass::ColIndRange
          });

          for (vector<pair<string, void*>> input : inputs) {
            _fileSuffix = "";

            int n = 0;
            for (pair<string, void*> value : input) {
              switch (n) {
                case 0:
                  _fileSuffix += value.first;
                  _genMat = *static_cast<Mat<double>*>(value.second);
                  break;
                case 1:
                  _fileSuffix += "," + value.first;
                  _colIndRange = *static_cast<span*>(value.second);
                  break;
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            _copyOfGenMat = _genMat;
            _copyOfColIndRange = _colIndRange;

            expectedMatSwapCols();

            _genMat = _copyOfGenMat;
            _colIndRange = _copyOfColIndRange;

            expectedMatShedCols();
          }

          cout << "done." << endl;
        }

    protected:
      Mat<double> _genMat;
      Mat<double> _copyOfGenMat;

      span _colIndRange;
      span _copyOfColIndRange;

      void expectedMatSwapCols() {
        if(_colIndRange.whole) {
          return;
        }

        if(!_genMat.in_range(span::all, _colIndRange)) {
          return;
        }

        cout << "- Compute expectedMatSwapCols() ... ";

        _genMat.swap_cols(_colIndRange.a, _colIndRange.b);
        save<double>("Mat.swap_cols", _genMat);

        cout << "done." << endl;
      }

      void expectedMatShedCols() {
        if(_colIndRange.whole) {
          return;
        }

        if(!_genMat.in_range(span::all, _colIndRange)) {
          return;
        }

        cout << "- Compute expectedMatShedCols() ... ";

        _genMat.shed_cols(_colIndRange.a, _colIndRange.b);
        save<double>("Mat.shed_cols", _genMat);

        cout << "done." << endl;
      }
  };
}
