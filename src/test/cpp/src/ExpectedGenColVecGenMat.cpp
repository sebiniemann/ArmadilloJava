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
using arma::Col;
using arma::Mat;
using arma::cross;
using arma::join_rows;
using arma::join_horiz;
using arma::join_cols;
using arma::join_vert;
using arma::kron;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedGenColVecGenMat : public Expected {
    public:
      ExpectedGenColVecGenMat() {
        cout << "Compute ExpectedGenColVecGenMat(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({InputClass::GenColVec, InputClass::GenMat});

        for (vector<pair<string, void*>> input : inputs) {
          _fileSuffix = "";

          int n = 0;
          for (pair<string, void*> value : input) {
            switch (n) {
              case 0:
                _fileSuffix += value.first;
                _genColVec = *static_cast<Col<double>*>(value.second);
                break;
              case 1:
                _fileSuffix += "," + value.first;
                _genMat = *static_cast<Mat<double>*>(value.second);
                break;
            }
            ++n;
          }

          cout << "Using input: " << _fileSuffix << endl;

          expectedCross();
          expectedJoin_rows();
          expectedJoin_horiz();
          expectedJoin_cols();
          expectedJoin_vert();
          expectedKron();
        }

        cout << "done." << endl;
      }

    protected:
      Col<double> _genColVec;
      Mat<double> _genMat;

      void expectedCross() {
        if(!_genColVec.is_vec()) {
          return;
        }

        if(_genColVec.n_elem != 3) {
          return;
        }

        if(!_genMat.is_vec()) {
          return;
        }

        if(_genMat.n_elem != 3) {
          return;
        }

        cout << "- Compute expectedCross() ... ";
        save("cross", cross(_genColVec, _genMat));
        cout << "done." << endl;
      }

      void expectedJoin_rows() {
        if(_genColVec.n_rows != _genMat.n_rows) {
          return;
        }

        cout << "- Compute expectedJoin_rows() ... ";
        save("join_rows", join_rows(_genColVec, _genMat));
        cout << "done." << endl;
      }

      void expectedJoin_horiz() {
        if(_genColVec.n_rows != _genMat.n_rows) {
          return;
        }

        cout << "- Compute expectedJoin_horiz() ... ";
        save("join_horiz", join_horiz(_genColVec, _genMat));
        cout << "done." << endl;
      }

      void expectedJoin_cols() {
        if(_genColVec.n_cols != _genMat.n_cols) {
          return;
        }

        cout << "- Compute expectedJoin_cols() ... ";
        save("join_cols", join_cols(_genColVec, _genMat));
        cout << "done." << endl;
      }

      void expectedJoin_vert() {
        if(_genColVec.n_cols != _genMat.n_cols) {
          return;
        }

        cout << "- Compute expectedJoin_vert() ... ";
        save("join_vert", join_cols(_genColVec, _genMat));
        cout << "done." << endl;
      }

      void expectedKron() {
        cout << "- Compute expectedKron() ... ";
        save("kron", kron(_genColVec, _genMat));
        cout << "done." << endl;
      }

  };
}
