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
using arma::toeplitz;
using arma::dot;
using arma::norm_dot;
using arma::conv;
using arma::cor;
using arma::cov;
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
  class ExpectedGenColVecGenColVec : public Expected {
    public:
      ExpectedGenColVecGenColVec() {
        cout << "Compute ExpectedGenColVecGenColVec(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({InputClass::GenColVec, InputClass::GenColVec});

        for (vector<pair<string, void*>> input : inputs) {
          _fileSuffix = "";

          int n = 0;
          for (pair<string, void*> value : input) {
            switch (n) {
              case 0:
                _fileSuffix += value.first;
                _genColVecA = *static_cast<Col<double>*>(value.second);
                break;
              case 1:
                _fileSuffix += "," + value.first;
                _genColVecB = *static_cast<Col<double>*>(value.second);
                break;
            }
            ++n;
          }

          cout << "Using input: " << _fileSuffix << endl;

          expectedToeplitz();
          expectedDot();
          expectedNorm_dot();
          expectedConv();
          expectedCor();
          expectedCov();
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
      Col<double> _genColVecA;
      Col<double> _genColVecB;

      void expectedToeplitz() {
        cout << "- Compute expectedToeplitz() ... ";
        save("toeplitz", toeplitz(_genColVecA, _genColVecB));
        cout << "done." << endl;
      }

      void expectedDot() {
        if(_genColVecA.n_elem != _genColVecB.n_elem) {
          return;
        }

        cout << "- Compute expectedDot() ... ";
        save("dot", Mat<double>({dot(_genColVecA, _genColVecB)}));
        cout << "done." << endl;
      }

      void expectedNorm_dot() {
        if(_genColVecA.n_elem != _genColVecB.n_elem) {
          return;
        }

        cout << "- Compute expectedNorm_dot() ... ";
        save("norm_dot", Mat<double>({norm_dot(_genColVecA, _genColVecB)}));
        cout << "done." << endl;
      }

      void expectedConv() {
        cout << "- Compute expectedConv() ... ";
        save("conv", conv(_genColVecA, _genColVecB));
        cout << "done." << endl;
      }

      void expectedCor() {
        if(_genColVecA.n_rows != _genColVecB.n_rows) {
          return;
        }

        if(_genColVecA.n_cols != _genColVecB.n_cols) {
          return;
        }

        cout << "- Compute expectedCor() ... ";
        save("cor", cor(_genColVecA, _genColVecB));
        cout << "done." << endl;
      }

      void expectedCov() {
        if(_genColVecA.n_rows != _genColVecB.n_rows) {
          return;
        }

        if(_genColVecA.n_cols != _genColVecB.n_cols) {
          return;
        }

        cout << "- Compute expectedCov() ... ";
        save("cov", cov(_genColVecA, _genColVecB));
        cout << "done." << endl;
      }

      void expectedCross() {
        if(!_genColVecA.is_vec()) {
          return;
        }

        if(_genColVecA.n_elem != 3) {
          return;
        }

        if(!_genColVecB.is_vec()) {
          return;
        }

        if(_genColVecB.n_elem != 3) {
          return;
        }

        cout << "- Compute expectedCross() ... ";
        save("cross", cross(_genColVecA, _genColVecB));
        cout << "done." << endl;
      }

      void expectedJoin_rows() {
        if(_genColVecA.n_rows != _genColVecB.n_rows) {
          return;
        }

        cout << "- Compute expectedJoin_rows() ... ";
        save("join_rows", join_rows(_genColVecA, _genColVecB));
        cout << "done." << endl;
      }

      void expectedJoin_horiz() {
        if(_genColVecA.n_rows != _genColVecB.n_rows) {
          return;
        }

        cout << "- Compute expectedJoin_horiz() ... ";
        save("join_horiz", join_horiz(_genColVecA, _genColVecB));
        cout << "done." << endl;
      }

      void expectedJoin_cols() {
        if(_genColVecA.n_cols != _genColVecB.n_cols) {
          return;
        }

        cout << "- Compute expectedJoin_cols() ... ";
        save("join_cols", join_cols(_genColVecA, _genColVecB));
        cout << "done." << endl;
      }

      void expectedJoin_vert() {
        if(_genColVecA.n_cols != _genColVecB.n_cols) {
          return;
        }

        cout << "- Compute expectedJoin_vert() ... ";
        save("join_vert", join_cols(_genColVecA, _genColVecB));
        cout << "done." << endl;
      }

      void expectedKron() {
        cout << "- Compute expectedKron() ... ";
        save("kron", kron(_genColVecA, _genColVecB));
        cout << "done." << endl;
      }

  };
}
