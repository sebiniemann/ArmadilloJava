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
using arma::Col;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedInPlaceGenMatExtColIndGenColVec : public Expected {
    public:
      ExpectedInPlaceGenMatExtColIndGenColVec() {
        cout << "Compute ExpectedInPlaceGenMatExtColIndGenColVec(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::GenMat,
            InputClass::ExtColInd,
            InputClass::GenColVec
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
                  _genColVec = *static_cast<Col<double>*>(value.second);
                  break;
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            _copyOfGenMat = _genMat;
            _copyOfExtColInd = _extColInd;
            _copyOfGenColVec = _genColVec;

            expectedMatInsert_cols();
          }

          cout << "done." << endl;
        }

    protected:
      Mat<double> _genMat;
      Mat<double> _copyOfGenMat;

      int _extColInd;
      int _copyOfExtColInd;

      Col<double> _genColVec;
      Col<double> _copyOfGenColVec;

      void expectedMatInsert_cols() {
        if(_extColInd > _genMat.n_cols) {
          return;
        }

        if(_genColVec.n_rows != _genMat.n_rows) {
          return;
        }

        cout << "- Compute expectedMatInsert_cols() ... ";

        _genMat.insert_cols(_extColInd, _genColVec);
        save<double>("Mat.insert_cols", _genMat);

        cout << "done." << endl;
      }
  };
}
