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

          expectedArmaMin();
          expectedArmaMax();
          expectedArmaCor();
          expectedArmaCov();
          expectedArmaCross();
          expectedArmaJoin_rows();
          expectedArmaJoin_horiz();
          expectedArmaJoin_cols();
          expectedArmaJoin_vert();
          expectedArmaKron();
        }

        cout << "done." << endl;
      }

    protected:
      Mat<double> _genMatA;
      Mat<double> _genMatB;

      void expectedArmaMin() {
        if(_genMatA.n_rows != _genMatB.n_rows) {
          return;
        }

        if(_genMatA.n_cols != _genMatB.n_cols) {
          return;
        }

        cout << "- Compute expectedArmaMin() ... ";
        save<double>("Arma.min", min(_genMatA, _genMatB));
        cout << "done." << endl;
      }

      void expectedArmaMax() {
        if(_genMatA.n_rows != _genMatB.n_rows) {
          return;
        }

        if(_genMatA.n_cols != _genMatB.n_cols) {
          return;
        }

        cout << "- Compute expectedArmaMax() ... ";
        save<double>("Arma.max", max(_genMatA, _genMatB));
        cout << "done." << endl;
      }

      void expectedArmaCor() {
        if(_genMatA.n_rows != _genMatB.n_rows) {
          return;
        }

        if(_genMatA.n_cols != _genMatB.n_cols) {
          return;
        }

        cout << "- Compute expectedArmaCor() ... ";
        save<double>("Arma.cor", cor(_genMatA, _genMatB));
        cout << "done." << endl;
      }

      void expectedArmaCov() {
        if(_genMatA.n_rows != _genMatB.n_rows) {
          return;
        }

        if(_genMatA.n_cols != _genMatB.n_cols) {
          return;
        }

        cout << "- Compute expectedArmaCov() ... ";
        save<double>("Arma.cov", cov(_genMatA, _genMatB));
        cout << "done." << endl;
      }

      void expectedArmaCross() {
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

        cout << "- Compute expectedArmaCross() ... ";
        save<double>("Arma.cross", cross(_genMatA, _genMatB));
        cout << "done." << endl;
      }

      void expectedArmaJoin_rows() {
        if(_genMatA.n_rows != _genMatB.n_rows) {
          return;
        }

        cout << "- Compute expectedArmaJoin_rows() ... ";
        save<double>("Arma.join_rows", join_rows(_genMatA, _genMatB));
        cout << "done." << endl;
      }

      void expectedArmaJoin_horiz() {
        if(_genMatA.n_rows != _genMatB.n_rows) {
          return;
        }

        cout << "- Compute expectedArmaJoin_horiz() ... ";
        save<double>("Arma.join_horiz", join_horiz(_genMatA, _genMatB));
        cout << "done." << endl;
      }

      void expectedArmaJoin_cols() {
        if(_genMatA.n_cols != _genMatB.n_cols) {
          return;
        }

        cout << "- Compute expectedArmaJoin_cols() ... ";
        save<double>("Arma.join_cols", join_cols(_genMatA, _genMatB));
        cout << "done." << endl;
      }

      void expectedArmaJoin_vert() {
        if(_genMatA.n_cols != _genMatB.n_cols) {
          return;
        }

        cout << "- Compute expectedArmaJoin_vert() ... ";
        save<double>("Arma.join_vert", join_cols(_genMatA, _genMatB));
        cout << "done." << endl;
      }

      void expectedArmaKron() {
        cout << "- Compute expectedArmaKron() ... ";
        save<double>("Arma.kron", kron(_genMatA, _genMatB));
        cout << "done." << endl;
      }

  };
}
