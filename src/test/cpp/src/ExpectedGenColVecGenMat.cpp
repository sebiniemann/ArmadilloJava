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
          expectedColPlus();
          expectedColMinus();
          expectedColTimes();
          expectedColElemTimes();
          expectedColElemDivide();
          expectedColEquals();
          expectedColNonEquals();
          expectedColGreaterThan();
          expectedColLessThan();
          expectedColStrictGreaterThan();
          expectedColStrictLessThan();
        }

        cout << "done." << endl;
      }

    protected:
      Col<double> _genColVec;
      Mat<double> _genMat;

      void expectedArmaCross() {
        cout << "- Compute expectedArmaCross() ... ";

        Col<double> tempGenColVec = Col<double>(_genColVec);
        tempGenColVec.resize(3);
        Mat<double> tempGenMat = Mat<double>(_genMat);
        tempGenMat.resize(3, 1);

        save<double>("Arma.cross", cross(tempGenColVec, tempGenMat));

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
	  
      void expectedColPlus() {
        if(_genMat.n_rows != _genColVec.n_rows) {
          return;
        }

        if(_genMat.n_cols != _genColVec.n_cols) {
          return;
        }

        cout << "- Compute expectedColPlus() ... ";
        save<double>("Col.plus", _genColVec + _genMat);
        cout << "done." << endl;
      }

      void expectedColMinus() {
        if(_genMat.n_rows != _genColVec.n_rows) {
          return;
        }

        if(_genMat.n_cols != _genColVec.n_cols) {
          return;
        }

        cout << "- Compute expectedColMinus() ... ";
        save<double>("Col.minus", _genColVec - _genMat);
        cout << "done." << endl;
      }

      void expectedColTimes() {
        if(_genMat.n_rows != _genColVec.n_cols) {
          return;
        }

        cout << "- Compute expectedColTimes() ... ";
        save<double>("Col.times", _genColVec * _genMat);
        cout << "done." << endl;
      }

      void expectedColElemTimes() {
        if(_genMat.n_rows != _genColVec.n_rows) {
          return;
        }

        if(_genMat.n_cols != _genColVec.n_cols) {
          return;
        }

        cout << "- Compute expectedColElemTimes() ... ";
        save<double>("Col.elemTimes", _genColVec % _genMat);
        cout << "done." << endl;
      }

      void expectedColElemDivide() {
        if(_genMat.n_rows != _genColVec.n_rows) {
          return;
        }

        if(_genMat.n_cols != _genColVec.n_cols) {
          return;
        }

        cout << "- Compute expectedColElemDivide() ... ";
        save<double>("Col.elemDivide", _genColVec / _genMat);
        cout << "done." << endl;
      }

      void expectedColEquals() {
        if(_genMat.n_rows != _genColVec.n_rows) {
          return;
        }

        if(_genMat.n_cols != _genColVec.n_cols) {
          return;
        }

        cout << "- Compute expectedColEquals() ... ";

        Col<uword> expected = _genColVec == _genMat;
        save<uword>("Col.equals", expected);

        cout << "done." << endl;
      }

      void expectedColNonEquals() {
        if(_genMat.n_rows != _genColVec.n_rows) {
          return;
        }

        if(_genMat.n_cols != _genColVec.n_cols) {
          return;
        }

        cout << "- Compute expectedColVecNonEquals() ... ";

        Col<uword> expected = _genColVec != _genMat;
        save<uword>("Col.nonEquals", expected);

        cout << "done." << endl;
      }

      void expectedColGreaterThan() {
        if(_genMat.n_rows != _genColVec.n_rows) {
          return;
        }

        if(_genMat.n_cols != _genColVec.n_cols) {
          return;
        }

        cout << "- Compute expectedColGreaterThan() ... ";

        Col<uword> expected = _genColVec >= _genMat;
        save<uword>("Col.greaterThan", expected);

        cout << "done." << endl;
      }

      void expectedColLessThan() {
        if(_genMat.n_rows != _genColVec.n_rows) {
          return;
        }

        if(_genMat.n_cols != _genColVec.n_cols) {
          return;
        }

        cout << "- Compute expectedColLessThan() ... ";

        Col<uword> expected = _genColVec <= _genMat;
        save<uword>("Col.lessThan", expected);

        cout << "done." << endl;
      }

      void expectedColStrictGreaterThan() {
        if(_genMat.n_rows != _genColVec.n_rows) {
          return;
        }

        if(_genMat.n_cols != _genColVec.n_cols) {
          return;
        }

        cout << "- Compute expectedColStrictGreaterThan() ... ";

        Col<uword> expected = _genColVec > _genMat;
        save<uword>("Col.strictGreaterThan", expected);

        cout << "done." << endl;
      }

      void expectedColStrictLessThan() {
        if(_genMat.n_rows != _genColVec.n_rows) {
          return;
        }

        if(_genMat.n_cols != _genColVec.n_cols) {
          return;
        }

        cout << "- Compute expectedColElemDivide() ... ";

        Col<uword> expected = _genColVec < _genMat;
        save<uword>("Col.strictLessThan", expected);

        cout << "done." << endl;
      }
  };
}
