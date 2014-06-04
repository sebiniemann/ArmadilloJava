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
  class ExpectedGenRowVecGenMat : public Expected {
    public:
      ExpectedGenRowVecGenMat() {
        cout << "Compute ExpectedGenRowVecGenMat(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
          InputClass::GenRowVec,
          InputClass::GenMat
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
      Mat<double> _genMat;

      void expectedArmaCross() {
        cout << "- Compute expectedArmaCross() ... ";

        Row<double> tempGenRowVec = Row<double>(_genRowVec);
        tempGenRowVec.resize(3);
        Mat<double> tempGenMat = Mat<double>(_genMat);
        tempGenMat.resize(3, 1);

        save<double>("Arma.cross", cross(tempGenRowVec, tempGenMat));

        cout << "done." << endl;
      }

      void expectedArmaJoin_rows() {
        if(_genRowVec.n_rows != _genMat.n_rows) {
          return;
        }

        cout << "- Compute expectedArmaJoin_rows() ... ";
        save<double>("Arma.join_rows", join_rows(_genRowVec, _genMat));
        cout << "done." << endl;
      }

      void expectedArmaJoin_horiz() {
        if(_genRowVec.n_rows != _genMat.n_rows) {
          return;
        }

        cout << "- Compute expectedArmaJoin_horiz() ... ";
        save<double>("Arma.join_horiz", join_horiz(_genRowVec, _genMat));
        cout << "done." << endl;
      }

      void expectedArmaJoin_cols() {
        if(_genRowVec.n_cols != _genMat.n_cols) {
          return;
        }

        cout << "- Compute expectedArmaJoin_cols() ... ";
        save<double>("Arma.join_cols", join_cols(_genRowVec, _genMat));
        cout << "done." << endl;
      }

      void expectedArmaJoin_vert() {
        if(_genRowVec.n_cols != _genMat.n_cols) {
          return;
        }

        cout << "- Compute expectedArmaJoin_vert() ... ";
        save<double>("Arma.join_vert", join_cols(_genRowVec, _genMat));
        cout << "done." << endl;
      }

      void expectedArmaKron() {
        cout << "- Compute expectedArmaKron() ... ";
        save<double>("Arma.kron", kron(_genRowVec, _genMat));
        cout << "done." << endl;
      }

      void expectedRowPlus() {
        if(_genRowVec.n_rows != _genMat.n_rows) {
          return;
        }

        if(_genRowVec.n_cols != _genMat.n_cols) {
          return;
        }

        cout << "- Compute expectedRowPlus() ... ";
        save<double>("Row.plus", _genRowVec + _genMat);
        cout << "done." << endl;
      }

      void expectedRowMinus() {
        if(_genRowVec.n_rows != _genMat.n_rows) {
          return;
        }

        if(_genRowVec.n_cols != _genMat.n_cols) {
          return;
        }

        cout << "- Compute expectedRowMinus() ... ";
        save<double>("Row.minus", _genRowVec - _genMat);
        cout << "done." << endl;
      }

      void expectedRowTimes() {
        if(_genRowVec.n_cols != _genMat.n_rows) {
          return;
        }

        cout << "- Compute expectedRowTimes() ... ";
        save<double>("Row.times", _genRowVec * _genMat);
        cout << "done." << endl;
      }

      void expectedRowElemTimes() {
        if(_genRowVec.n_rows != _genMat.n_rows) {
          return;
        }

        if(_genRowVec.n_cols != _genMat.n_cols) {
          return;
        }

        cout << "- Compute expectedRowElemTimes() ... ";
        save<double>("Row.elemTimes", _genRowVec % _genMat);
        cout << "done." << endl;
      }

      void expectedRowElemDivide() {
        if(_genRowVec.n_rows != _genMat.n_rows) {
          return;
        }

        if(_genRowVec.n_cols != _genMat.n_cols) {
          return;
        }

        cout << "- Compute expectedRowElemDivide() ... ";
        save<double>("Row.elemDivide", _genRowVec / _genMat);
        cout << "done." << endl;
      }

      void expectedRowEquals() {
        if(_genRowVec.n_rows != _genMat.n_rows) {
          return;
        }

        if(_genRowVec.n_cols != _genMat.n_cols) {
          return;
        }

        cout << "- Compute expectedRowEquals() ... ";

        Mat<uword> expected = _genRowVec == _genMat;
        save<uword>("Row.equals", expected);

        cout << "done." << endl;
      }

      void expectedRowNonEquals() {
        if(_genRowVec.n_rows != _genMat.n_rows) {
          return;
        }

        if(_genRowVec.n_cols != _genMat.n_cols) {
          return;
        }

        cout << "- Compute expectedRowNonEquals() ... ";

        Mat<uword> expected = _genRowVec != _genMat;
        save<uword>("Row.nonEquals", expected);

        cout << "done." << endl;
      }

      void expectedRowGreaterThan() {
        if(_genRowVec.n_rows != _genMat.n_rows) {
          return;
        }

        if(_genRowVec.n_cols != _genMat.n_cols) {
          return;
        }

        cout << "- Compute expectedRowGreaterThan() ... ";

        Mat<uword> expected = _genRowVec >= _genMat;
        save<uword>("Row.greaterThan", expected);

        cout << "done." << endl;
      }

      void expectedRowLessThan() {
        if(_genRowVec.n_rows != _genMat.n_rows) {
          return;
        }

        if(_genRowVec.n_cols != _genMat.n_cols) {
          return;
        }

        cout << "- Compute expectedRowLessThan() ... ";

        Mat<uword> expected = _genRowVec <= _genMat;
        save<uword>("Row.lessThan", expected);

        cout << "done." << endl;
      }

      void expectedRowStrictGreaterThan() {
        if(_genRowVec.n_rows != _genMat.n_rows) {
          return;
        }

        if(_genRowVec.n_cols != _genMat.n_cols) {
          return;
        }

        cout << "- Compute expectedRowStrictGreaterThan() ... ";

        Mat<uword> expected = _genRowVec > _genMat;
        save<uword>("Row.strictGreaterThan", expected);

        cout << "done." << endl;
      }

      void expectedRowStrictLessThan() {
        if(_genRowVec.n_rows != _genMat.n_rows) {
          return;
        }

        if(_genRowVec.n_cols != _genMat.n_cols) {
          return;
        }

        cout << "- Compute expectedRowStrictLessThan() ... ";

        Mat<uword> expected = _genRowVec < _genMat;
        save<uword>("Row.strictLessThan", expected);

        cout << "done." << endl;
      }

  };
}
