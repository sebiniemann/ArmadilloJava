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
  class ExpectedInPlaceGenMatElemInd : public Expected {
    public:
      ExpectedInPlaceGenMatElemInd() {
        cout << "Compute ExpectedInPlaceGenMatElemInd(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({InputClass::GenMat, InputClass::ElemInd});

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
                  _elemInd = *static_cast<int*>(value.second);
                  break;
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            _copyOfGenMat = _genMat;
            _copyOfElemInd = _elemInd;

            expectedMatAtIncrement();

            _genMat = _copyOfGenMat;
            _elemInd = _copyOfElemInd;
            expectedMatAtDecrement();
          }

          cout << "done." << endl;
        }

    protected:
      Mat<double> _genMat;
      Mat<double> _copyOfGenMat;

      int _elemInd;
      int _copyOfElemInd;

      void expectedMatAtIncrement() {
        if(_elemInd >= _genMat.n_elem) {
          return;
        }

        _genMat.at(_elemInd)++;

        cout << "- Compute expectedMatAtIncrement() ... ";
        save<double>("Mat.atIncrement", _genMat);
        cout << "done." << endl;
      }

      void expectedMatAtDecrement() {
        if(_elemInd >= _genMat.n_elem) {
          return;
        }

        _genMat.at(_elemInd)--;

        cout << "- Compute expectedMatAtDecrement() ... ";
        save<double>("Mat.atDecrement", _genMat);
        cout << "done." << endl;
      }
  };
}
