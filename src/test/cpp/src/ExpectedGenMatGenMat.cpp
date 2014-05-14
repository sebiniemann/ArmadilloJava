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

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
          InputClass::GenMat,
          InputClass::GenMat
        });

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
        cout << "- Compute expectedArmaCross() ... ";

        Mat<double> tempGenMatA = Mat<double>(_genMatA);
        tempGenMatA.resize(3, 1);
        Mat<double> tempGenMatB = Mat<double>(_genMatB);
        tempGenMatB.resize(3, 1);

        save<double>("Arma.cross", cross(tempGenMatA, tempGenMatB));

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

      void expectedMatPlus() {
        if(_genMatA.n_rows != _genMatB.n_rows) {
          return;
        }

        if(_genMatA.n_cols != _genMatB.n_cols) {
          return;
        }

        cout << "- Compute expectedMatPlus() ... ";
        save<double>("Mat.plus", _genMatA + _genMatB);
        cout << "done." << endl;
      }

      void expectedMatMinus() {
        if(_genMatA.n_rows != _genMatB.n_rows) {
          return;
        }

        if(_genMatA.n_cols != _genMatB.n_cols) {
          return;
        }

        cout << "- Compute expectedMatMinus() ... ";
        save<double>("Mat.minus", _genMatA - _genMatB);
        cout << "done." << endl;
      }

      void expectedMatTimes() {
        if(_genMatA.n_cols != _genMatB.n_rows) {
          return;
        }

        cout << "- Compute expectedMatTimes() ... ";
        save<double>("Mat.times", _genMatA * _genMatB);
        cout << "done." << endl;
      }

      void expectedMatElemTimes() {
        if(_genMatA.n_rows != _genMatB.n_rows) {
          return;
        }

        if(_genMatA.n_cols != _genMatB.n_cols) {
          return;
        }

        cout << "- Compute expectedMatElemTimes() ... ";
        save<double>("Mat.elemTimes", _genMatA % _genMatB);
        cout << "done." << endl;
      }

      void expectedMatElemDivide() {
        if(_genMatA.n_rows != _genMatB.n_rows) {
          return;
        }

        if(_genMatA.n_cols != _genMatB.n_cols) {
          return;
        }

        cout << "- Compute expectedMatElemDivide() ... ";
        save<double>("Mat.elemDivide", _genMatA / _genMatB);
        cout << "done." << endl;
      }

      void expectedMatEquals() {
        if(_genMatA.n_rows != _genMatB.n_rows) {
          return;
        }

        if(_genMatA.n_cols != _genMatB.n_cols) {
          return;
        }

        cout << "- Compute expectedMatEquals() ... ";

        Mat<uword> expected = _genMatA == _genMatB;
        save<uword>("Mat.equals", expected);

        cout << "done." << endl;
      }

      void expectedMatNonEquals() {
        if(_genMatA.n_rows != _genMatB.n_rows) {
          return;
        }

        if(_genMatA.n_cols != _genMatB.n_cols) {
          return;
        }

        cout << "- Compute expectedMatNonEquals() ... ";

        Mat<uword> expected = _genMatA != _genMatB;
        save<uword>("Mat.nonEquals", expected);

        cout << "done." << endl;
      }

      void expectedMatGreaterThan() {
        if(_genMatA.n_rows != _genMatB.n_rows) {
          return;
        }

        if(_genMatA.n_cols != _genMatB.n_cols) {
          return;
        }

        cout << "- Compute expectedMatGreaterThan() ... ";

        Mat<uword> expected = _genMatA >= _genMatB;
        save<uword>("Mat.greaterThan", expected);

        cout << "done." << endl;
      }

      void expectedMatLessThan() {
        if(_genMatA.n_rows != _genMatB.n_rows) {
          return;
        }

        if(_genMatA.n_cols != _genMatB.n_cols) {
          return;
        }

        cout << "- Compute expectedMatLessThan() ... ";

        Mat<uword> expected = _genMatA <= _genMatB;
        save<uword>("Mat.LessThan", expected);

        cout << "done." << endl;
      }

      void expectedMatStrictGreaterThan() {
        if(_genMatA.n_rows != _genMatB.n_rows) {
          return;
        }

        if(_genMatA.n_cols != _genMatB.n_cols) {
          return;
        }

        cout << "- Compute expectedMatStrictGreaterThan() ... ";

        Mat<uword> expected = _genMatA > _genMatB;
        save<uword>("Mat.strictGreaterThan", expected);

        cout << "done." << endl;
      }

      void expectedMatStrictLessThan() {
        if(_genMatA.n_rows != _genMatB.n_rows) {
          return;
        }

        if(_genMatA.n_cols != _genMatB.n_cols) {
          return;
        }

        cout << "- Compute expectedMatElemDivide() ... ";

        Mat<uword> expected = _genMatA < _genMatB;
        save<uword>("Mat.strictLessThan", expected);

        cout << "done." << endl;
      }

  };
}
