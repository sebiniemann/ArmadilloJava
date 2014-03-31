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
using arma::Row;
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
  class ExpectedGenMatGenRowVec : public Expected {
    public:
      ExpectedGenMatGenRowVec() {
        cout << "Compute ExpectedGenMatGenRowVec(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({InputClass::GenMat, InputClass::GenRowVec});

        for (vector<pair<string, void*>> input : inputs) {
          _fileSuffix = "";

          int n = 0;
          for (pair<string, void*> value : input) {
            switch (n) {
              case 0:
                _fileSuffix += value.first;
                _genMat = *static_cast<Mat<double>*>(value.second);
                break;
              case 1:
                _fileSuffix += "," + value.first;
                _genRowVec = *static_cast<Row<double>*>(value.second);
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
      Mat<double> _genMat;
      Row<double> _genRowVec;

      void expectedCross() {
        if(!_genMat.is_vec()) {
          return;
        }

        if(_genMat.n_elem != 3) {
          return;
        }

        if(!_genRowVec.is_vec()) {
          return;
        }

        if(_genRowVec.n_elem != 3) {
          return;
        }

        cout << "- Compute expectedCross() ... ";
        save("cross", cross(_genMat, _genRowVec));
        cout << "done." << endl;
      }

      void expectedJoin_rows() {
        if(_genMat.n_rows != _genRowVec.n_rows) {
          return;
        }

        cout << "- Compute expectedJoin_rows() ... ";
        save("join_rows", join_rows(_genMat, _genRowVec));
        cout << "done." << endl;
      }

      void expectedJoin_horiz() {
        if(_genMat.n_rows != _genRowVec.n_rows) {
          return;
        }

        cout << "- Compute expectedJoin_horiz() ... ";
        save("join_horiz", join_horiz(_genMat, _genRowVec));
        cout << "done." << endl;
      }

      void expectedJoin_cols() {
        if(_genMat.n_cols != _genRowVec.n_cols) {
          return;
        }

        cout << "- Compute expectedJoin_cols() ... ";
        save("join_cols", join_cols(_genMat, _genRowVec));
        cout << "done." << endl;
      }

      void expectedJoin_vert() {
        if(_genMat.n_cols != _genRowVec.n_cols) {
          return;
        }

        cout << "- Compute expectedJoin_vert() ... ";
        save("join_vert", join_cols(_genMat, _genRowVec));
        cout << "done." << endl;
      }

      void expectedKron() {
        cout << "- Compute expectedKron() ... ";
        save("kron", kron(_genMat, _genRowVec));
        cout << "done." << endl;
      }

  };
}
