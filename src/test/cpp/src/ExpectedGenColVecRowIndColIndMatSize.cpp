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
using arma::size;
using arma::SizeMat;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedGenColVecRowIndColIndMatSize : public Expected {
    public:
      ExpectedGenColVecRowIndColIndMatSize() {
        cout << "Compute ExpectedGenColVecRowIndColIndMatSize(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::GenColVec,
            InputClass::RowInd,
            InputClass::ColInd,
            InputClass::MatSize
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
                  _rowInd = *static_cast<int*>(value.second);
                  break;
                case 2:
                  _fileSuffix += "," + value.first;
                  _colInd = *static_cast<int*>(value.second);
                  break;
                case 3:
                  _fileSuffix += "," + value.first;
                  _matSize = *static_cast<pair<int, int>*>(value.second);
                  break;
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            expectedColIn_range();
          }

          cout << "done." << endl;
        }

    protected:
      Col<double> _genColVec;
      int _rowInd;
      int _colInd;
      pair<int, int> _matSize;

      void expectedColIn_range() {
        cout << "- Compute expectedColIn_range() ... ";

        if(_genColVec.in_range(_rowInd, _colInd, size(_matSize.first, _matSize.second))) {
          save<double>("Col.in_range", Mat<double>({1}));
        } else {
          save<double>("Col.in_range", Mat<double>({0}));
        }

        cout << "done." << endl;
      }
  };
}
