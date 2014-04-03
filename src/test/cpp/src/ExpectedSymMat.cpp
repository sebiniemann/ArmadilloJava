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
using arma::eig_sym;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedSymMat : public Expected {
    public:
      ExpectedSymMat() {
        cout << "Compute ExpectedSymMat(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({InputClass::SymMat});

          for (vector<pair<string, void*>> input : inputs) {
            _fileSuffix = "";

            int n = 0;
            for (pair<string, void*> value : input) {
              switch (n) {
                case 0:
                  _fileSuffix += value.first;
                  _symMat = *static_cast<Mat<double>*>(value.second);
                  break;
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            expectedArmaEig_sym();
          }

          cout << "done." << endl;
        }

    protected:
      Mat<double> _symMat;

      void expectedArmaEig_sym() {
        cout << "- Compute expectedArmaEig_sym() ... ";

        Col<double> eigval;
        Mat<double> eigvec;

        eig_sym(eigval, eigvec, _symMat);

        save<double>("Arma.eig_symEigval", eigval);
        save<double>("Arma.eig_symEigvec", eigvec);

        cout << "done." << endl;
      }
  };
}
