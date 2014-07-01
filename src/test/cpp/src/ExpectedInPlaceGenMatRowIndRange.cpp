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
  class ExpectedInPlaceGenMatRowIndRange : public Expected {
    public:
      ExpectedInPlaceGenMatRowIndRange() {
        cout << "Compute ExpectedInPlaceGenMatRowIndRange(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::GenMat,
            InputClass::RowIndRange
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
                  _rowIndRange = *static_cast<span*>(value.second);
                  break;
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            _copyOfGenMat = _genMat;
            _copyOfRowIndRange = _rowIndRange;

            expectedMatSwapRows();

            _genMat = _copyOfGenMat;
            _rowIndRange = _copyOfRowIndRange;

            expectedMatShedRows();
          }

          cout << "done." << endl;
        }

    protected:
      Mat<double> _genMat;
      Mat<double> _copyOfGenMat;

      span _rowIndRange;
      span _copyOfRowIndRange;

      void expectedMatSwapRows() {
        if(_rowIndRange.whole) {
          return;
        }

        if(!_genMat.in_range(_rowIndRange, span::all)) {
          return;
        }

        cout << "- Compute expectedMatSwapRows() ... ";

        _genMat.swap_rows(_rowIndRange.a, _rowIndRange.b);
        save<double>("Mat.swap_rows", _genMat);

        cout << "done." << endl;
      }

      void expectedMatShedRows() {
        if(_rowIndRange.whole) {
          return;
        }

        if(!_genMat.in_range(_rowIndRange, span::all)) {
          return;
        }

        cout << "- Compute expectedMatShedRows() ... ";

        _genMat.shed_rows(_rowIndRange.a, _rowIndRange.b);
        save<double>("Mat.shed_rows", _genMat);

        cout << "done." << endl;
      }
  };
}
