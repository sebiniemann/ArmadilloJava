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
  class ExpectedInPlaceGenMatExtColIndGenMat : public Expected {
    public:
      ExpectedInPlaceGenMatExtColIndGenMat() {
        cout << "Compute ExpectedInPlaceGenMatExtColIndGenMat(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::GenMat,
            InputClass::ExtColInd,
            InputClass::GenMat
          });

          for (vector<pair<string, void*>> input : inputs) {
            _fileSuffix = "";

            int n = 0;
            for (pair<string, void*> value : input) {
              switch (n) {
                case 0:
                  _fileSuffix += value.first;
                  _genMatA = *static_cast<Mat<double>*>(value.second);
                  break;
                case 1:
                  _fileSuffix += "," + value.first;
                  _extColInd = *static_cast<int*>(value.second);
                  break;
                case 2:
                  _fileSuffix += "," + value.first;
                  _genMatB = *static_cast<Mat<double>*>(value.second);
                  break;
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            _copyOfGenMatA = _genMatA;
            _copyOfExtColInd = _extColInd;
            _copyOfGenMatB = _genMatB;

            expectedMatInsert_cols();
          }

          cout << "done." << endl;
        }

    protected:
      Mat<double> _genMatA;
      Mat<double> _copyOfGenMatA;

      int _extColInd;
      int _copyOfExtColInd;

      Mat<double> _genMatB;
      Mat<double> _copyOfGenMatB;

      void expectedMatInsert_cols() {
        if(_extColInd > _genMatA.n_cols) {
          return;
        }

        if(_genMatB.n_rows != _genMatA.n_rows) {
          return;
        }

        cout << "- Compute expectedMatInsert_cols() ... ";

        _genMatA.insert_cols(_extColInd, _genMatB);
        save<double>("Mat.insert_cols", _genMatA);

        cout << "done." << endl;
      }
  };
}
