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

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedInPlaceGenMatExtColIndNumCols : public Expected {
    public:
      ExpectedInPlaceGenMatExtColIndNumCols() {
        cout << "Compute ExpectedInPlaceGenMatExtColIndNumCols(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::GenMat,
            InputClass::ExtColInd,
            InputClass::NumCols
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
                  _extColInd = *static_cast<int*>(value.second);
                  break;
                case 2:
                  _fileSuffix += "," + value.first;
                  _numCols = *static_cast<int*>(value.second);
                  break;
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            _copyOfGenMat = _genMat;
            _copyOfExtColInd = _extColInd;
            _copyOfNumCols = _numCols;

            expectedMatInsert_colsA();

            _genMat = _copyOfGenMat;
            _extColInd = _copyOfExtColInd;
            _numCols = _copyOfNumCols;
            expectedMatInsert_colsB();
          }

          cout << "done." << endl;
        }

    protected:
      Mat<double> _genMat;
      Mat<double> _copyOfGenMat;

      int _extColInd;
      int _copyOfExtColInd;

      int _numCols;
      int _copyOfNumCols;

      void expectedMatInsert_colsA() {
        if(_extColInd > _genMat.n_cols) {
          return;
        }

        cout << "- Compute expectedMatInsert_colsA() ... ";

        _genMat.insert_cols(_extColInd, _numCols, true);
        save<double>("Mat.insert_colsTrue", _genMat);

        cout << "done." << endl;
      }

      void expectedMatInsert_colsB() {
        if(_extColInd > _genMat.n_cols) {
          return;
        }

        cout << "- Compute expectedMatInsert_colsB() ... ";

        _genMat.insert_cols(_extColInd, _numCols, false);
        save<double>("Mat.insert_colsFalse", _genMat);

        cout << "done." << endl;
      }
  };
}
