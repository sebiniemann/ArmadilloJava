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
using arma::Col;
using arma::Mat;
using arma::cor;
using arma::cov;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedGenRowVecGenColVecNormal : public Expected {
    public:
      ExpectedGenRowVecGenColVecNormal() {
        cout << "Compute ExpectedGenRowVecGenColVecNormal(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
          InputClass::GenRowVec,
          InputClass::GenColVec,
          InputClass::Normal
        });

        for (vector<pair<string, void*>> input : inputs) {
          _fileSuffix = "";

          int n = 0;
          for (pair<string, void*> value : input) {
            switch (n) {
              case 0:
                _fileSuffix += value.first;
                _genRowVec = *static_cast<Row<double>*>(value.second);
                break;
              case 1:
                _fileSuffix += "," + value.first;
                _genColVec = *static_cast<Col<double>*>(value.second);
                break;
              case 2:
                _fileSuffix += "," + value.first;
                _normal = *static_cast<int*>(value.second);
                break;
            }
            ++n;
          }

          cout << "Using input: " << _fileSuffix << endl;

          expectedArmaCor();
          expectedArmaCov();
        }

        cout << "done." << endl;
      }

    protected:
      Row<double> _genRowVec;
      Col<double> _genColVec;
      int _normal;

      void expectedArmaCor() {
        if(_genRowVec.n_elem != _genColVec.n_elem) {
          return;
        }

        cout << "- Compute expectedArmaCor() ... ";
        save<double>("Arma.cor", Mat<double>({cor(_genRowVec, _genColVec, _normal)}));
        cout << "done." << endl;
      }

      void expectedArmaCov() {
        if(_genRowVec.n_elem != _genColVec.n_elem) {
          return;
        }

        cout << "- Compute expectedArmaCov() ... ";
        save<double>("Arma.cov", Mat<double>({cov(_genRowVec, _genColVec, _normal)}));
        cout << "done." << endl;
      }

  };
}
