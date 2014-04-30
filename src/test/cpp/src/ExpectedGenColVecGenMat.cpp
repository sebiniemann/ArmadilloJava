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
using arma::uword;
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

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
          InputClass::GenColVec,
          InputClass::GenMat
        });

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

          expectedArmaCross();
          expectedArmaJoin_rows();
          expectedArmaJoin_horiz();
          expectedArmaJoin_cols();
          expectedArmaJoin_vert();
          expectedArmaKron();
		      expectedColEquals();
        }

        cout << "done." << endl;
      }

    protected:
      Col<double> _genColVec;
      Mat<double> _genMat;

      void expectedArmaCross() {
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

        cout << "- Compute expectedArmaCross() ... ";
        save<double>("Arma.cross", cross(_genColVec, _genMat));
        cout << "done." << endl;
      }

      void expectedArmaJoin_rows() {
        if(_genColVec.n_rows != _genMat.n_rows) {
          return;
        }

        cout << "- Compute expectedArmaJoin_rows() ... ";
        save<double>("Arma.join_rows", join_rows(_genColVec, _genMat));
        cout << "done." << endl;
      }

      void expectedArmaJoin_horiz() {
        if(_genColVec.n_rows != _genMat.n_rows) {
          return;
        }

        cout << "- Compute expectedArmaJoin_horiz() ... ";
        save<double>("Arma.join_horiz", join_horiz(_genColVec, _genMat));
        cout << "done." << endl;
      }

      void expectedArmaJoin_cols() {
        if(_genColVec.n_cols != _genMat.n_cols) {
          return;
        }

        cout << "- Compute expectedArmaJoin_cols() ... ";
        save<double>("Arma.join_cols", join_cols(_genColVec, _genMat));
        cout << "done." << endl;
      }

      void expectedArmaJoin_vert() {
        if(_genColVec.n_cols != _genMat.n_cols) {
          return;
        }

        cout << "- Compute expectedArmaJoin_vert() ... ";
        save<double>("Arma.join_vert", join_cols(_genColVec, _genMat));
        cout << "done." << endl;
      }

      void expectedArmaKron() {
        cout << "- Compute expectedArmaKron() ... ";
        save<double>("Arma.kron", kron(_genColVec, _genMat));
        cout << "done." << endl;
      }
	  
      void expectedColEquals() {
        if(_genColVec.n_rows != _genMat.n_rows || _genColVec.n_cols != _genMat.n_cols) {
          return;
        }

        cout << "- Compute expectedColEquals() ... ";

        Col<uword> expected = _genColVec == _genMat;
        save<uword> ("Col.equals", expected);

        cout << "done." << endl;
      }
  };
}
