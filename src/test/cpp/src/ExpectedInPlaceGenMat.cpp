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
using arma::inplace_trans;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedInPlaceGenMat : public Expected {
    public:
      ExpectedInPlaceGenMat() {
        cout << "Compute ExpectedInPlaceGenMat(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::GenMat
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
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            _copyOfGenMat = _genMat;

            expectedArmaInplace_trans();

            _genMat = _copyOfGenMat;
            expectedMatOnes();

            _genMat = _copyOfGenMat;
            expectedMatZeros();

            _genMat = _copyOfGenMat;
            expectedMatInPlaceIncrement();

            _genMat = _copyOfGenMat;
            expectedMatInPlaceDecrement();

            _genMat = _copyOfGenMat;
            expectedMatEye();
          }

          cout << "done." << endl;
        }

    protected:
      Mat<double> _genMat;
      Mat<double> _copyOfGenMat;

      void expectedArmaInplace_trans() {
        cout << "- Compute expectedArmaInplace_trans() ... ";

        inplace_trans(_genMat);
        save<double>("Arma.inplace_trans", _genMat);

        cout << "done." << endl;
      }

      void expectedMatOnes() {
        cout << "- Compute expectedMatOnes() ... ";

        _genMat.ones();
        save<double>("Mat.ones", _genMat);

        cout << "done." << endl;
      }

      void expectedMatZeros() {
        cout << "- Compute expectedMatZeros() ... ";

        _genMat.zeros();
        save<double>("Mat.zeros", _genMat);

        cout << "done." << endl;
      }

      void expectedMatInPlaceIncrement() {
        cout << "- Compute expectedMatInPlaceIncrement() ... ";

        _genMat++;
        save<double>("Mat.inPlaceIncrement", _genMat);

        cout << "done." << endl;
      }

      void expectedMatInPlaceDecrement() {
        cout << "- Compute expectedMatInPlaceDecrement() ... ";

        _genMat--;
        save<double>("Mat.inPlaceDecrement", _genMat);

        cout << "done." << endl;
      }

      void expectedMatEye() {
        cout << "- Compute expectedMatEye() ... ";

        _genMat.eye();
        save<double>("Mat.eye", _genMat);

        cout << "done." << endl;
      }
  };
}
