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

#include <stdexcept>
using std::runtime_error;

#include <armadillo>
using arma::Mat;
using arma::min;
using arma::max;
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
  class ExpectedGenMatGenMat : public Expected {
    public:
      ExpectedGenMatGenMat() {
        cout << "Compute ExpectedGenMatGenMat(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({InputClass::GenMat, InputClass::GenMat});

        for (vector<pair<string, void*>> input : inputs) {
          _fileSuffix = "";

          int n = 0;
          for (pair<string, void*> value : input) {
            switch (n) {
              case 0:
                _fileSuffix += value.first;
                _genMatA = *static_cast<Mat<double>*>(value.second);
                break;
              case 1:
                _fileSuffix += "," + value.first;
                _genMatB = *static_cast<Mat<double>*>(value.second);
                break;
            }
            ++n;
          }

          cout << "Using input: " << _fileSuffix << endl;

          expectedMin();
          expectedMax();
          expectedCor();
          expectedCov();
          expectedCross();
          expectedJoin_rows();
          expectedJoin_horiz();
          expectedJoin_cols();
          expectedJoin_vert();
          expectedKron();
          expectedSolve();
        }

        cout << "done." << endl;
      }

    protected:
      Mat<double> _genMatA;
      Mat<double> _genMatB;

      void expectedMin() {
        if(_genMatA.n_rows != _genMatB.n_rows) {
          return;
        }

        if(_genMatA.n_cols != _genMatB.n_cols) {
          return;
        }

        cout << "- Compute expectedMin() ... ";
        save("min", min(_genMatA, _genMatB));
        cout << "done." << endl;
      }

      void expectedMax() {
        if(_genMatA.n_rows != _genMatB.n_rows) {
          return;
        }

        if(_genMatA.n_cols != _genMatB.n_cols) {
          return;
        }

        cout << "- Compute expectedMax() ... ";
        save("max", max(_genMatA, _genMatB));
        cout << "done." << endl;
      }

      void expectedCor() {
        if(_genMatA.n_rows != _genMatB.n_rows) {
          return;
        }

        if(_genMatA.n_cols != _genMatB.n_cols) {
          return;
        }

        cout << "- Compute expectedCor() ... ";
        save("cor", cor(_genMatA, _genMatB));
        cout << "done." << endl;
      }

      void expectedCov() {
        if(_genMatA.n_rows != _genMatB.n_rows) {
          return;
        }

        if(_genMatA.n_cols != _genMatB.n_cols) {
          return;
        }

        cout << "- Compute expectedCov() ... ";
        save("cov", cov(_genMatA, _genMatB));
        cout << "done." << endl;
      }

      void expectedCross() {
        if(!_genMatA.is_vec()) {
          return;
        }

        if(_genMatA.n_elem != 3) {
          return;
        }

        if(!_genMatB.is_vec()) {
          return;
        }

        if(_genMatB.n_elem != 3) {
          return;
        }

        cout << "- Compute expectedCross() ... ";
        save("cross", cross(_genMatA, _genMatB));
        cout << "done." << endl;
      }

      void expectedJoin_rows() {
        if(_genMatA.n_rows != _genMatB.n_rows) {
          return;
        }

        cout << "- Compute expectedJoin_rows() ... ";
        save("join_rows", join_rows(_genMatA, _genMatB));
        cout << "done." << endl;
      }

      void expectedJoin_horiz() {
        if(_genMatA.n_rows != _genMatB.n_rows) {
          return;
        }

        cout << "- Compute expectedJoin_horiz() ... ";
        save("join_horiz", join_horiz(_genMatA, _genMatB));
        cout << "done." << endl;
      }

      void expectedJoin_cols() {
        if(_genMatA.n_cols != _genMatB.n_cols) {
          return;
        }

        cout << "- Compute expectedJoin_cols() ... ";
        save("join_cols", join_cols(_genMatA, _genMatB));
        cout << "done." << endl;
      }

      void expectedJoin_vert() {
        if(_genMatA.n_cols != _genMatB.n_cols) {
          return;
        }

        cout << "- Compute expectedJoin_vert() ... ";
        save("join_vert", join_cols(_genMatA, _genMatB));
        cout << "done." << endl;
      }

      void expectedKron() {
        cout << "- Compute expectedKron() ... ";
        save("kron", kron(_genMatA, _genMatB));
        cout << "done." << endl;
      }

      void expectedSolve() {
        if(_genMatA.n_rows != _genMatB.n_rows) {
          return;
        }

        cout << "- Compute expectedSolve() ... ";

        try {
          save("solve", solve(_genMatA, _genMatB));
        } catch(runtime_error e) {
          /*
           * Do nothing is the equation is not solvable.
           */
        }

        cout << "done." << endl;
      }

  };
}
