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
using arma::shuffle;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedInPlaceRandomGenMat : public Expected {
    public:
      ExpectedInPlaceRandomGenMat() {
        cout << "Compute ExpectedInPlaceRandomGenMat(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({InputClass::Random, InputClass::GenMat});

          for (vector<pair<string, void*>> input : inputs) {
            _fileSuffix = "";

            int n = 0;
            for (pair<string, void*> value : input) {
              switch (n) {
                case 0:
                  _fileSuffix += value.first;
                  _random = *static_cast<int*>(value.second);
                  break;
                case 1:
                  _fileSuffix += "," + value.first;
                  _genMat = *static_cast<Mat<double>*>(value.second);
                  break;
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            _copyOfRandom = _random;
            _copyOfGenMat = _genMat;

            expectedMatRandu();

            _random = _copyOfRandom;
            _genMat = _copyOfGenMat;
            expectedMatRandn();
          }

          cout << "done." << endl;
        }

    protected:
      int _random;
      int _copyOfRandom;

      Mat<double> _genMat;
      Mat<double> _copyOfGenMat;

      void expectedMatRandu() {
        cout << "- Compute expectedMatRandu() ... ";

        _genMat.randu();
        Mat<double> result = _genMat;

        for(int n = 2; n <= _random; n++) {
          _genMat.randu();
          result = (result * n + _genMat) / (n + 1);
        }

        save<double>("Mat.randu", result);

        cout << "done." << endl;
      }

    void expectedMatRandn() {
      cout << "- Compute expectedMatRandn() ... ";

      _genMat.randn();
      Mat<double> result = _genMat;

      for(int n = 2; n <= _random; n++) {
        _genMat.randn();
        result = (result * n + _genMat) / (n + 1);
      }

      save<double>("Mat.randn", result);

      cout << "done." << endl;
    }
  };
}
