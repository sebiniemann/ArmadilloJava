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
using arma::Row;
using arma::Mat;
using arma::cor;
using arma::cov;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedGenRowVecGenRowVecNormal : public Expected {
    public:
      ExpectedGenRowVecGenRowVecNormal() {
        cout << "Compute ExpectedGenRowVecGenRowVecNormal(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({InputClass::GenRowVec, InputClass::GenRowVec, InputClass::Normal});

        for (vector<pair<string, void*>> input : inputs) {
          _fileSuffix = "";

          int n = 0;
          for (pair<string, void*> value : input) {
            switch (n) {
              case 0:
                _fileSuffix += value.first;
                _genRowVecA = *static_cast<Row<double>*>(value.second);
                break;
              case 1:
                _fileSuffix += "," + value.first;
                _genRowVecB = *static_cast<Row<double>*>(value.second);
                break;
              case 2:
                _fileSuffix += "," + value.first;
                _normal = *static_cast<int*>(value.second);
                break;
            }
            ++n;
          }

          cout << "Using input: " << _fileSuffix << endl;

          expectedCor();
          expectedCov();
        }

        cout << "done." << endl;
      }

    protected:
      Row<double> _genRowVecA;
      Row<double> _genRowVecB;
      int _normal;

      void expectedCor() {
        if(_genRowVecA.n_elem != _genRowVecB.n_elem) {
          return;
        }

        cout << "- Compute expectedVar() ... ";
        save("cor", Mat<double>({cor(_genRowVecA, _genRowVecB, _normal)}));
        cout << "done." << endl;
      }

      void expectedCov() {
        if(_genRowVecA.n_elem != _genRowVecB.n_elem) {
          return;
        }

        cout << "- Compute expectedVar() ... ";
        save("cov", Mat<double>({cov(_genRowVecA, _genRowVecB, _normal)}));
        cout << "done." << endl;
      }

  };
}
