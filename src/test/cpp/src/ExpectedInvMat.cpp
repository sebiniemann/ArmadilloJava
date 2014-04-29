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
using arma::inv;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedInvMat : public Expected {
    public:
      ExpectedInvMat() {
        cout << "Compute ExpectedInvMat(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({InputClass::InvMat});

          for (vector<pair<string, void*>> input : inputs) {
            _fileSuffix = "";

            int n = 0;
            for (pair<string, void*> value : input) {
              switch (n) {
                case 0:
                  _fileSuffix += value.first;
                  _invMat = *static_cast<Mat<double>*>(value.second);
                  break;
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            expectedArmaInv();
            expectedMatI();
          }

          cout << "done." << endl;
        }

    protected:
      Mat<double> _invMat;

      void expectedArmaInv() {
        cout << "- Compute expectedArmaInv() ... ";
        save<double>("Arma.inv", inv(_invMat));
        cout << "done." << endl;
      }

      void expectedMatI() {
        cout << "- Compute expectedMatI() ... ";
        save<double>("Mat.i", _invMat.i());
        cout << "done." << endl;
      }
  };
}
