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
using arma::span;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedInPlaceGenRowVecElemIndRangeGenRowVec: public Expected {
    public:
      ExpectedInPlaceGenRowVecElemIndRangeGenRowVec() {
        cout << "Compute ExpectedInPlaceGenRowVecElemIndRangeGenRowVec(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters( {
            InputClass::GenRowVec,
            InputClass::ElemIndRange,
            InputClass::GenRowVec
          });

        for (vector<pair<string, void*>> input : inputs) {
          _fileSuffix = "";

          int n = 0;
          for (pair<string, void*> value : input) {
            switch (n) {
              case 0:
                _fileSuffix += value.first;
                _genRowVecA = *static_cast<Row<double>*>(value.second);
                break;
              case 1:
                _fileSuffix += "," + value.first;
                _elemIndRange = *static_cast<span*>(value.second);
                break;
              case 2:
                _fileSuffix += "," + value.first;
                _genRowVecB = *static_cast<Row<double>*>(value.second);
                break;
            }
            ++n;
          }

          cout << "Using input: " << _fileSuffix << endl;

          _copyOfGenRowVecA = _genRowVecA;
          _copyOfElemIndRange = _elemIndRange;
          _copyOfGenRowVecB = _genRowVecB;

          expectedRowVecColsEqual();

          _genRowVecA = _copyOfGenRowVecA;
          _elemIndRange = _copyOfElemIndRange;
          _genRowVecB = _copyOfGenRowVecB;
          expectedRowVecColsPlus();

          _genRowVecA = _copyOfGenRowVecA;
          _elemIndRange = _copyOfElemIndRange;
          _genRowVecB = _copyOfGenRowVecB;
          expectedRowVecColsMinus();

          _genRowVecA = _copyOfGenRowVecA;
          _elemIndRange = _copyOfElemIndRange;
          _genRowVecB = _copyOfGenRowVecB;
          expectedRowVecColsElemTimes();

          _genRowVecA = _copyOfGenRowVecA;
          _elemIndRange = _copyOfElemIndRange;
          _genRowVecB = _copyOfGenRowVecB;
          expectedRowVecColsElemDivide();

          _genRowVecA = _copyOfGenRowVecA;
          _elemIndRange = _copyOfElemIndRange;
          _genRowVecB = _copyOfGenRowVecB;
          expectedRowVecSubvecEqual();

          _genRowVecA = _copyOfGenRowVecA;
          _elemIndRange = _copyOfElemIndRange;
          _genRowVecB = _copyOfGenRowVecB;
          expectedRowVecSubvecPlus();

          _genRowVecA = _copyOfGenRowVecA;
          _elemIndRange = _copyOfElemIndRange;
          _genRowVecB = _copyOfGenRowVecB;
          expectedRowVecSubvecMinus();

          _genRowVecA = _copyOfGenRowVecA;
          _elemIndRange = _copyOfElemIndRange;
          _genRowVecB = _copyOfGenRowVecB;
          expectedRowVecSubvecElemTimes();

          _genRowVecA = _copyOfGenRowVecA;
          _elemIndRange = _copyOfElemIndRange;
          _genRowVecB = _copyOfGenRowVecB;
          expectedRowVecSubvecElemDivide();
        }

        cout << "done." << endl;
      }

    protected:
      Row<double> _genRowVecA;
      Row<double> _copyOfGenRowVecA;

      span _elemIndRange;
      span _copyOfElemIndRange;

      Row<double> _genRowVecB;
      Row<double> _copyOfGenRowVecB;

      void expectedRowVecColsEqual() {
        if (!_genRowVecA.in_range(_elemIndRange)) {
          return;
        }

        if (_genRowVecB.n_elem != _elemIndRange.b - _elemIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedRowVecColsEqual() ... ";

        _genRowVecA.cols(_elemIndRange.a, _elemIndRange.b) = _genRowVecB;

        save<double>("Row.colsEqual", _genRowVecA);
        cout << "done." << endl;
      }

      void expectedRowVecColsPlus() {
        if (!_genRowVecA.in_range(_elemIndRange)) {
          return;
        }

        if (_genRowVecB.n_elem != _elemIndRange.b - _elemIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedRowVecColsPlus() ... ";

        _genRowVecA.cols(_elemIndRange.a, _elemIndRange.b) += _genRowVecB;

        save<double>("Row.colsPlus", _genRowVecA);
        cout << "done." << endl;
      }

      void expectedRowVecColsMinus() {
        if (!_genRowVecA.in_range(_elemIndRange)) {
          return;
        }

        if (_genRowVecB.n_elem != _elemIndRange.b - _elemIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedRowVecColsMinus() ... ";

        _genRowVecA.cols(_elemIndRange.a, _elemIndRange.b) -= _genRowVecB;

        save<double>("Row.colsMinus", _genRowVecA);
        cout << "done." << endl;
      }

      void expectedRowVecColsElemTimes() {
        if (!_genRowVecA.in_range(_elemIndRange)) {
          return;
        }

        if (_genRowVecB.n_elem != _elemIndRange.b - _elemIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedRowVecColsElemTimes() ... ";

        _genRowVecA.cols(_elemIndRange.a, _elemIndRange.b) %= _genRowVecB;

        save<double>("Row.colsElemTimes", _genRowVecA);
        cout << "done." << endl;
      }

      void expectedRowVecColsElemDivide() {
        if (!_genRowVecA.in_range(_elemIndRange)) {
          return;
        }

        if (_genRowVecB.n_elem != _elemIndRange.b - _elemIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedRowVecColsElemDivide() ... ";

        _genRowVecA.cols(_elemIndRange.a, _elemIndRange.b) /= _genRowVecB;

        save<double>("Row.colsElemDivide", _genRowVecA);
        cout << "done." << endl;
      }

      void expectedRowVecSubvecEqual() {
        if (!_genRowVecA.in_range(_elemIndRange)) {
          return;
        }

        if (_genRowVecB.n_elem != _elemIndRange.b - _elemIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedRowVecSubvecEqual() ... ";

        _genRowVecA.subvec(_elemIndRange.a, _elemIndRange.b) = _genRowVecB;

        save<double>("Row.subvecEqual", _genRowVecA);
        cout << "done." << endl;
      }

      void expectedRowVecSubvecPlus() {
        if (!_genRowVecA.in_range(_elemIndRange)) {
          return;
        }

        if (_genRowVecB.n_elem != _elemIndRange.b - _elemIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedRowVecSubvecPlus() ... ";

        _genRowVecA.subvec(_elemIndRange.a, _elemIndRange.b) += _genRowVecB;

        save<double>("Row.subvecPlus", _genRowVecA);
        cout << "done." << endl;
      }

      void expectedRowVecSubvecMinus() {
        if (!_genRowVecA.in_range(_elemIndRange)) {
          return;
        }

        if (_genRowVecB.n_elem != _elemIndRange.b - _elemIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedRowVecSubvecMinus() ... ";

        _genRowVecA.subvec(_elemIndRange.a, _elemIndRange.b) -= _genRowVecB;

        save<double>("Row.subvecMinus", _genRowVecA);
        cout << "done." << endl;
      }

      void expectedRowVecSubvecElemTimes() {
        if (!_genRowVecA.in_range(_elemIndRange)) {
          return;
        }

        if (_genRowVecB.n_elem != _elemIndRange.b - _elemIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedRowVecSubvecElemTimes() ... ";

        _genRowVecA.subvec(_elemIndRange.a, _elemIndRange.b) %= _genRowVecB;

        save<double>("Row.subvecElemTimes", _genRowVecA);
        cout << "done." << endl;
      }

      void expectedRowVecSubvecElemDivide() {
        if (!_genRowVecA.in_range(_elemIndRange) || _genRowVecB.n_elem != (_elemIndRange.b - _elemIndRange.a) + 1) {
          return;
        }

        cout << "- Compute expectedRowVecSubvecElemDivide() ... ";

        _genRowVecA.subvec(_elemIndRange.a, _elemIndRange.b) /= _genRowVecB;

        save<double>("Row.subvecElemDivide", _genRowVecA);
        cout << "done." << endl;
      }

  };
}
