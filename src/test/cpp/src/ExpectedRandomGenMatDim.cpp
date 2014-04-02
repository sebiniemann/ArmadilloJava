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
  class ExpectedRandomGenMatDim : public Expected {
    public:
      ExpectedRandomGenMatDim() {
        cout << "Compute ExpectedRandomGenMatDim(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({InputClass::Random, InputClass::GenMat, InputClass::Dim});

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
                case 2:
                  _fileSuffix += "," + value.first;
                  _dim = *static_cast<int*>(value.second);
                  break;
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            expectedShuffle();
          }

          cout << "done." << endl;
        }

    protected:
      int _random;
      Mat<double> _genMat;
      int _dim;

      void expectedShuffle() {
        cout << "- Compute expectedShuffle() ... ";

        Mat<double> result = shuffle(_genMat, _dim);
        for(int n = 2; n <= _random; n++) {
          result = (result * n + shuffle(_genMat, _dim)) / (n + 1);
        }
        save("shuffle", result);

        cout << "done." << endl;
      }
  };
}
