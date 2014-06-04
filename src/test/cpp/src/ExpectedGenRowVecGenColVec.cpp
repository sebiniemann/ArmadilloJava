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
using arma::Col;
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
  class ExpectedGenRowVecGenColVec : public Expected {
    public:
      ExpectedGenRowVecGenColVec() {
        cout << "Compute ExpectedGenRowVecGenColVec(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
          InputClass::GenRowVec,
          InputClass::GenColVec
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
            }
            ++n;
          }

          cout << "Using input: " << _fileSuffix << endl;

          expectedArmaToeplitz();
          expectedArmaDot();
          expectedArmaNorm_dot();
          expectedArmaConv();
          expectedArmaCor();
          expectedArmaCov();
          expectedArmaCross();
          expectedArmaJoin_rows();
          expectedArmaJoin_horiz();
          expectedArmaJoin_cols();
          expectedArmaJoin_vert();
          expectedArmaKron();
          expectedRowPlus();
          expectedRowMinus();
          expectedRowTimes();
          expectedRowElemTimes();
          expectedRowElemDivide();
          expectedRowEquals();
          expectedRowNonEquals();
          expectedRowGreaterThan();
          expectedRowLessThan();
          expectedRowStrictGreaterThan();
          expectedRowStrictLessThan();
        }

        cout << "done." << endl;
      }

    protected:
      Row<double> _genRowVec;
      Col<double> _genColVec;

      void expectedArmaToeplitz() {
        cout << "- Compute expectedArmaToeplitz() ... ";
        save<double>("Arma.toeplitz", toeplitz(_genRowVec, _genColVec));
        cout << "done." << endl;
      }

      void expectedArmaDot() {
        if(_genRowVec.n_elem != _genColVec.n_elem) {
          return;
        }

        cout << "- Compute expectedArmaDot() ... ";
        save<double>("Arma.dot", Mat<double>({dot(_genRowVec, _genColVec)}));
        cout << "done." << endl;
      }

      void expectedArmaNorm_dot() {
        if(_genRowVec.n_elem != _genColVec.n_elem) {
          return;
        }

        cout << "- Compute expectedArmaNorm_dot() ... ";
        save<double>("Arma.norm_dot", Mat<double>({norm_dot(_genRowVec, _genColVec)}));
        cout << "done." << endl;
      }

      void expectedArmaConv() {
        cout << "- Compute expectedArmaConv() ... ";
        save<double>("Arma.conv", conv(_genRowVec, _genColVec));
        cout << "done." << endl;
      }

      void expectedArmaCor() {
        if(_genRowVec.n_rows != _genColVec.n_rows) {
          return;
        }

        if(_genRowVec.n_cols != _genColVec.n_cols) {
          return;
        }

        cout << "- Compute expectedArmaCor() ... ";
        save<double>("Arma.cor", cor(_genRowVec, _genColVec));
        cout << "done." << endl;
      }

      void expectedArmaCov() {
        if(_genRowVec.n_rows != _genColVec.n_rows) {
          return;
        }

        if(_genRowVec.n_cols != _genColVec.n_cols) {
          return;
        }

        cout << "- Compute expectedArmaCov() ... ";
        save<double>("Arma.cov", cov(_genRowVec, _genColVec));
        cout << "done." << endl;
      }

      void expectedArmaCross() {
        cout << "- Compute expectedArmaCross() ... ";

        Row<double> tempGenRowVec = Row<double>(_genRowVec);
        tempGenRowVec.resize(3);
        Col<double> tempGenColVec = Col<double>(_genColVec);
        tempGenColVec.resize(3);

        save<double>("Arma.cross", cross(tempGenRowVec, tempGenColVec));

        cout << "done." << endl;
      }

      void expectedArmaJoin_rows() {
        if(_genRowVec.n_rows != _genColVec.n_rows) {
          return;
        }

        cout << "- Compute expectedArmaJoin_rows() ... ";
        save<double>("Arma.join_rows", join_rows(_genRowVec, _genColVec));
        cout << "done." << endl;
      }

      void expectedArmaJoin_horiz() {
        if(_genRowVec.n_rows != _genColVec.n_rows) {
          return;
        }

        cout << "- Compute expectedArmaJoin_horiz() ... ";
        save<double>("Arma.join_horiz", join_horiz(_genRowVec, _genColVec));
        cout << "done." << endl;
      }

      void expectedArmaJoin_cols() {
        if(_genRowVec.n_cols != _genColVec.n_cols) {
          return;
        }

        cout << "- Compute expectedArmaJoin_cols() ... ";
        save<double>("Arma.join_cols", join_cols(_genRowVec, _genColVec));
        cout << "done." << endl;
      }

      void expectedArmaJoin_vert() {
        if(_genRowVec.n_cols != _genColVec.n_cols) {
          return;
        }

        cout << "- Compute expectedArmaJoin_vert() ... ";
        save<double>("Arma.join_vert", join_cols(_genRowVec, _genColVec));
        cout << "done." << endl;
      }

      void expectedArmaKron() {
        cout << "- Compute expectedArmaKron() ... ";
        save<double>("Arma.kron", kron(_genRowVec, _genColVec));
        cout << "done." << endl;
      }

      void expectedRowPlus() {
        if(_genRowVec.n_rows != _genColVec.n_rows) {
          return;
        }

        if(_genRowVec.n_cols != _genColVec.n_cols) {
          return;
        }

        cout << "- Compute expectedRowPlus() ... ";
        save<double>("Row.plus", _genRowVec + _genColVec);
        cout << "done." << endl;
      }

      void expectedRowMinus() {
        if(_genRowVec.n_rows != _genColVec.n_rows) {
          return;
        }

        if(_genRowVec.n_cols != _genColVec.n_cols) {
          return;
        }

        cout << "- Compute expectedRowMinus() ... ";
        save<double>("Row.minus", _genRowVec - _genColVec);
        cout << "done." << endl;
      }

      void expectedRowTimes() {
        if(_genRowVec.n_cols != _genColVec.n_rows) {
          return;
        }

        cout << "- Compute expectedRowTimes() ... ";
        save<double>("Row.times", _genRowVec * _genColVec);
        cout << "done." << endl;
      }

      void expectedRowElemTimes() {
        if(_genRowVec.n_rows != _genColVec.n_rows) {
          return;
        }

        if(_genRowVec.n_cols != _genColVec.n_cols) {
          return;
        }

        cout << "- Compute expectedRowElemTimes() ... ";
        save<double>("Row.elemTimes", _genRowVec % _genColVec);
        cout << "done." << endl;
      }

      void expectedRowElemDivide() {
        if(_genRowVec.n_rows != _genColVec.n_rows) {
          return;
        }

        if(_genRowVec.n_cols != _genColVec.n_cols) {
          return;
        }

        cout << "- Compute expectedRowElemDivide() ... ";
        save<double>("Row.elemDivide", _genRowVec / _genColVec);
        cout << "done." << endl;
      }

      void expectedRowEquals() {
        if(_genRowVec.n_rows != _genColVec.n_rows) {
          return;
        }

        if(_genRowVec.n_cols != _genColVec.n_cols) {
          return;
        }

        cout << "- Compute expectedRowEquals() ... ";

        Mat<uword> expected = _genRowVec == _genColVec;
        save<uword>("Row.equals", expected);

        cout << "done." << endl;
      }

      void expectedRowNonEquals() {
        if(_genRowVec.n_rows != _genColVec.n_rows) {
          return;
        }

        if(_genRowVec.n_cols != _genColVec.n_cols) {
          return;
        }

        cout << "- Compute expectedRowNonEquals() ... ";

        Mat<uword> expected = _genRowVec != _genColVec;
        save<uword>("Row.nonEquals", expected);

        cout << "done." << endl;
      }

      void expectedRowGreaterThan() {
        if(_genRowVec.n_rows != _genColVec.n_rows) {
          return;
        }

        if(_genRowVec.n_cols != _genColVec.n_cols) {
          return;
        }

        cout << "- Compute expectedRowGreaterThan() ... ";

        Mat<uword> expected = _genRowVec >= _genColVec;
        save<uword>("Row.greaterThan", expected);

        cout << "done." << endl;
      }

      void expectedRowLessThan() {
        if(_genRowVec.n_rows != _genColVec.n_rows) {
          return;
        }

        if(_genRowVec.n_cols != _genColVec.n_cols) {
          return;
        }

        cout << "- Compute expectedRowLessThan() ... ";

        Mat<uword> expected = _genRowVec <= _genColVec;
        save<uword>("Row.lessThan", expected);

        cout << "done." << endl;
      }

      void expectedRowStrictGreaterThan() {
        if(_genRowVec.n_rows != _genColVec.n_rows) {
          return;
        }

        if(_genRowVec.n_cols != _genColVec.n_cols) {
          return;
        }

        cout << "- Compute expectedRowStrictGreaterThan() ... ";

        Mat<uword> expected = _genRowVec > _genColVec;
        save<uword>("Row.strictGreaterThan", expected);

        cout << "done." << endl;
      }

      void expectedRowStrictLessThan() {
        if(_genRowVec.n_rows != _genColVec.n_rows) {
          return;
        }

        if(_genRowVec.n_cols != _genColVec.n_cols) {
          return;
        }

        cout << "- Compute expectedRowStrictLessThan() ... ";

        Mat<uword> expected = _genRowVec < _genColVec;
        save<uword>("Row.strictLessThan", expected);

        cout << "done." << endl;
      }

  };
}
