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

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
          InputClass::GenMat,
          InputClass::GenRowVec
        });

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

          expectedArmaCross();
          expectedArmaJoin_rows();
          expectedArmaJoin_horiz();
          expectedArmaJoin_cols();
          expectedArmaJoin_vert();
          expectedArmaKron();
          expectedMatPlus();
          expectedMatMinus();
          expectedMatTimes();
          expectedMatElemTimes();
          expectedMatElemDivide();
          expectedMatEquals();
          expectedMatNonEquals();
          expectedMatGreaterThan();
          expectedMatLessThan();
          expectedMatStrictGreaterThan();
          expectedMatStrictLessThan();
        }

        cout << "done." << endl;
      }

    protected:
      Mat<double> _genMat;
      Row<double> _genRowVec;

      void expectedArmaCross() {
        cout << "- Compute expectedArmaCross() ... ";

        Mat<double> tempGenMat = Mat<double>(_genMat);
        tempGenMat.resize(3, 1);
        Row<double> tempGenRowVec = Row<double>(_genRowVec);
        tempGenRowVec.resize(3);

        save<double>("Arma.cross", cross(tempGenMat, tempGenRowVec));

        cout << "done." << endl;
      }

      void expectedArmaJoin_rows() {
        if(_genMat.n_rows != _genRowVec.n_rows) {
          return;
        }

        cout << "- Compute expectedArmaJoin_rows() ... ";
        save<double>("Arma.join_rows", join_rows(_genMat, _genRowVec));
        cout << "done." << endl;
      }

      void expectedArmaJoin_horiz() {
        if(_genMat.n_rows != _genRowVec.n_rows) {
          return;
        }

        cout << "- Compute expectedArmaJoin_horiz() ... ";
        save<double>("Arma.join_horiz", join_horiz(_genMat, _genRowVec));
        cout << "done." << endl;
      }

      void expectedArmaJoin_cols() {
        if(_genMat.n_cols != _genRowVec.n_cols) {
          return;
        }

        cout << "- Compute expectedArmaJoin_cols() ... ";
        save<double>("Arma.join_cols", join_cols(_genMat, _genRowVec));
        cout << "done." << endl;
      }

      void expectedArmaJoin_vert() {
        if(_genMat.n_cols != _genRowVec.n_cols) {
          return;
        }

        cout << "- Compute expectedArmaJoin_vert() ... ";
        save<double>("Arma.join_vert", join_cols(_genMat, _genRowVec));
        cout << "done." << endl;
      }

      void expectedArmaKron() {
        cout << "- Compute expectedArmaKron() ... ";
        save<double>("Arma.kron", kron(_genMat, _genRowVec));
        cout << "done." << endl;
      }

      void expectedMatPlus() {
        if(_genMat.n_rows != _genRowVec.n_rows) {
          return;
        }

        if(_genMat.n_cols != _genRowVec.n_cols) {
          return;
        }

        cout << "- Compute expectedMatPlus() ... ";
        save<double>("Mat.plus", _genMat + _genRowVec);
        cout << "done." << endl;
      }

      void expectedMatMinus() {
        if(_genMat.n_rows != _genRowVec.n_rows) {
          return;
        }

        if(_genMat.n_cols != _genRowVec.n_cols) {
          return;
        }

        cout << "- Compute expectedMatMinus() ... ";
        save<double>("Mat.minus", _genMat - _genRowVec);
        cout << "done." << endl;
      }

      void expectedMatTimes() {
        if(_genMat.n_cols != _genRowVec.n_rows) {
          return;
        }

        cout << "- Compute expectedMatTimes() ... ";
        save<double>("Mat.times", _genMat * _genRowVec);
        cout << "done." << endl;
      }

      void expectedMatElemTimes() {
        if(_genMat.n_rows != _genRowVec.n_rows) {
          return;
        }

        if(_genMat.n_cols != _genRowVec.n_cols) {
          return;
        }

        cout << "- Compute expectedMatElemTimes() ... ";
        save<double>("Mat.elemTimes", _genMat % _genRowVec);
        cout << "done." << endl;
      }

      void expectedMatElemDivide() {
        if(_genMat.n_rows != _genRowVec.n_rows) {
          return;
        }

        if(_genMat.n_cols != _genRowVec.n_cols) {
          return;
        }

        cout << "- Compute expectedMatElemDivide() ... ";
        save<double>("Mat.elemDivide", _genMat / _genRowVec);
        cout << "done." << endl;
      }

      void expectedMatEquals() {
        if(_genMat.n_rows != _genRowVec.n_rows) {
          return;
        }

        if(_genMat.n_cols != _genRowVec.n_cols) {
          return;
        }

        cout << "- Compute expectedMatEquals() ... ";

        Mat<uword> expected = _genMat == _genRowVec;
        save<uword>("Mat.equals", expected);

        cout << "done." << endl;
      }

      void expectedMatNonEquals() {
        if(_genMat.n_rows != _genRowVec.n_rows) {
          return;
        }

        if(_genMat.n_cols != _genRowVec.n_cols) {
          return;
        }

        cout << "- Compute expectedMatNonEquals() ... ";

        Mat<uword> expected = _genMat != _genRowVec;
        save<uword>("Mat.nonEquals", expected);

        cout << "done." << endl;
      }

      void expectedMatGreaterThan() {
        if(_genMat.n_rows != _genRowVec.n_rows) {
          return;
        }

        if(_genMat.n_cols != _genRowVec.n_cols) {
          return;
        }

        cout << "- Compute expectedMatGreaterThan() ... ";

        Mat<uword> expected = _genMat >= _genRowVec;
        save<uword>("Mat.greaterThan", expected);

        cout << "done." << endl;
      }

      void expectedMatLessThan() {
        if(_genMat.n_rows != _genRowVec.n_rows) {
          return;
        }

        if(_genMat.n_cols != _genRowVec.n_cols) {
          return;
        }

        cout << "- Compute expectedMatLessThan() ... ";

        Mat<uword> expected = _genMat <= _genRowVec;
        save<uword>("Mat.LessThan", expected);

        cout << "done." << endl;
      }

      void expectedMatStrictGreaterThan() {
        if(_genMat.n_rows != _genRowVec.n_rows) {
          return;
        }

        if(_genMat.n_cols != _genRowVec.n_cols) {
          return;
        }

        cout << "- Compute expectedMatStrictGreaterThan() ... ";

        Mat<uword> expected = _genMat > _genRowVec;
        save<uword>("Mat.strictGreaterThan", expected);

        cout << "done." << endl;
      }

      void expectedMatStrictLessThan() {
        if(_genMat.n_rows != _genRowVec.n_rows) {
          return;
        }

        if(_genMat.n_cols != _genRowVec.n_cols) {
          return;
        }

        cout << "- Compute expectedMatElemDivide() ... ";

        Mat<uword> expected = _genMat < _genRowVec;
        save<uword>("Mat.strictLessThan", expected);

        cout << "done." << endl;
      }

  };
}
