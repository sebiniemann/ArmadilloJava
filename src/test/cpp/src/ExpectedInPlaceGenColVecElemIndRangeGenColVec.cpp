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
using arma::span;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedInPlaceGenColVecElemIndRangeGenColVec: public Expected {
    public:
      ExpectedInPlaceGenColVecElemIndRangeGenColVec() {
        cout << "Compute ExpectedInPlaceGenColVecElemIndRangeGenColVec(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters( {
            InputClass::GenColVec,
            InputClass::ElemIndRange,
            InputClass::GenColVec
          });

        for (vector<pair<string, void*>> input : inputs) {
          _fileSuffix = "";

          int n = 0;
          for (pair<string, void*> value : input) {
            switch (n) {
              case 0:
                _fileSuffix += value.first;
                _genColVecA = *static_cast<Col<double>*>(value.second);
                break;
              case 1:
                _fileSuffix += "," + value.first;
                _elemIndRange = *static_cast<span*>(value.second);
                break;
              case 2:
                _fileSuffix += "," + value.first;
                _genColVecB = *static_cast<Col<double>*>(value.second);
                break;
            }
            ++n;
          }

          cout << "Using input: " << _fileSuffix << endl;

          _copyOfGenColVecA = _genColVecA;
          _copyOfElemIndRange = _elemIndRange;
          _copyOfGenColVecB = _genColVecB;

          expectedColVecRowsEqual();

          _genColVecA = _copyOfGenColVecA;
          _elemIndRange = _copyOfElemIndRange;
          _genColVecB = _copyOfGenColVecB;
          expectedColVecRowsPlus();

          _genColVecA = _copyOfGenColVecA;
          _elemIndRange = _copyOfElemIndRange;
          _genColVecB = _copyOfGenColVecB;
          expectedColVecRowsMinus();

          _genColVecA = _copyOfGenColVecA;
          _elemIndRange = _copyOfElemIndRange;
          _genColVecB = _copyOfGenColVecB;
          expectedColVecRowsElemTimes();

          _genColVecA = _copyOfGenColVecA;
          _elemIndRange = _copyOfElemIndRange;
          _genColVecB = _copyOfGenColVecB;
          expectedColVecRowsElemDivide();

          _genColVecA = _copyOfGenColVecA;
          _elemIndRange = _copyOfElemIndRange;
          _genColVecB = _copyOfGenColVecB;
          expectedColVecSubvecEqual();

          _genColVecA = _copyOfGenColVecA;
          _elemIndRange = _copyOfElemIndRange;
          _genColVecB = _copyOfGenColVecB;
          expectedColVecSubvecPlus();

          _genColVecA = _copyOfGenColVecA;
          _elemIndRange = _copyOfElemIndRange;
          _genColVecB = _copyOfGenColVecB;
          expectedColVecSubvecMinus();

          _genColVecA = _copyOfGenColVecA;
          _elemIndRange = _copyOfElemIndRange;
          _genColVecB = _copyOfGenColVecB;
          expectedColVecSubvecElemTimes();

          _genColVecA = _copyOfGenColVecA;
          _elemIndRange = _copyOfElemIndRange;
          _genColVecB = _copyOfGenColVecB;
          expectedColVecSubvecElemDivide();
        }

        cout << "done." << endl;
      }

    protected:
      Col<double> _genColVecA;
      Col<double> _copyOfGenColVecA;

      span _elemIndRange;
      span _copyOfElemIndRange;

      Col<double> _genColVecB;
      Col<double> _copyOfGenColVecB;

      void expectedColVecRowsEqual() {
        if(_elemIndRange.whole) {
          return;
        }

        if (!_genColVecA.in_range(_elemIndRange)) {
          return;
        }

        if (_genColVecB.n_elem != _elemIndRange.b - _elemIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedColVecRowsEqual() ... ";

        _genColVecA.rows(_elemIndRange.a, _elemIndRange.b) = _genColVecB;

        save<double>("Col.rowsEqual", _genColVecA);
        cout << "done." << endl;
      }

      void expectedColVecRowsPlus() {
        if(_elemIndRange.whole) {
          return;
        }

        if (!_genColVecA.in_range(_elemIndRange)) {
          return;
        }

        if (_genColVecB.n_elem != _elemIndRange.b - _elemIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedColVecRowsPlus() ... ";

        _genColVecA.rows(_elemIndRange.a, _elemIndRange.b) += _genColVecB;

        save<double>("Col.rowsPlus", _genColVecA);
        cout << "done." << endl;
      }

      void expectedColVecRowsMinus() {
        if(_elemIndRange.whole) {
          return;
        }

        if (!_genColVecA.in_range(_elemIndRange)) {
          return;
        }

        if (_genColVecB.n_elem != _elemIndRange.b - _elemIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedColVecRowsMinus() ... ";

        _genColVecA.rows(_elemIndRange.a, _elemIndRange.b) -= _genColVecB;

        save<double>("Col.rowsMinus", _genColVecA);
        cout << "done." << endl;
      }

      void expectedColVecRowsElemTimes() {
        if(_elemIndRange.whole) {
          return;
        }

        if (!_genColVecA.in_range(_elemIndRange)) {
          return;
        }

        if (_genColVecB.n_elem != _elemIndRange.b - _elemIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedColVecRowsElemTimes() ... ";

        _genColVecA.rows(_elemIndRange.a, _elemIndRange.b) %= _genColVecB;

        save<double>("Col.rowsElemTimes", _genColVecA);
        cout << "done." << endl;
      }

      void expectedColVecRowsElemDivide() {
        if(_elemIndRange.whole) {
          return;
        }

        if (!_genColVecA.in_range(_elemIndRange)) {
          return;
        }

        if (_genColVecB.n_elem != _elemIndRange.b - _elemIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedColVecRowsElemDivide() ... ";

        _genColVecA.rows(_elemIndRange.a, _elemIndRange.b) /= _genColVecB;

        save<double>("Col.rowsElemDivide", _genColVecA);
        cout << "done." << endl;
      }

      void expectedColVecSubvecEqual() {
        if(_elemIndRange.whole) {
          return;
        }

        if (!_genColVecA.in_range(_elemIndRange)) {
          return;
        }

        if (_genColVecB.n_elem != _elemIndRange.b - _elemIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedColVecSubvecEqual() ... ";

        _genColVecA.subvec(_elemIndRange.a, _elemIndRange.b) = _genColVecB;

        save<double>("Col.subvecEqual", _genColVecA);
        cout << "done." << endl;
      }

      void expectedColVecSubvecPlus() {
        if(_elemIndRange.whole) {
          return;
        }

        if (!_genColVecA.in_range(_elemIndRange)) {
          return;
        }

        if (_genColVecB.n_elem != _elemIndRange.b - _elemIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedColVecSubvecPlus() ... ";

        _genColVecA.subvec(_elemIndRange.a, _elemIndRange.b) += _genColVecB;

        save<double>("Col.subvecPlus", _genColVecA);
        cout << "done." << endl;
      }

      void expectedColVecSubvecMinus() {
        if(_elemIndRange.whole) {
          return;
        }

        if (!_genColVecA.in_range(_elemIndRange)) {
          return;
        }

        if (_genColVecB.n_elem != _elemIndRange.b - _elemIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedColVecSubvecMinus() ... ";

        _genColVecA.subvec(_elemIndRange.a, _elemIndRange.b) -= _genColVecB;

        save<double>("Col.subvecMinus", _genColVecA);
        cout << "done." << endl;
      }

      void expectedColVecSubvecElemTimes() {
        if(_elemIndRange.whole) {
          return;
        }

        if (!_genColVecA.in_range(_elemIndRange)) {
          return;
        }

        if (_genColVecB.n_elem != _elemIndRange.b - _elemIndRange.a + 1) {
          return;
        }

        cout << "- Compute expectedColVecSubvecElemTimes() ... ";

        _genColVecA.subvec(_elemIndRange.a, _elemIndRange.b) %= _genColVecB;

        save<double>("Col.subvecElemTimes", _genColVecA);
        cout << "done." << endl;
      }

      void expectedColVecSubvecElemDivide() {
        if(_elemIndRange.whole) {
          return;
        }

        if (!_genColVecA.in_range(_elemIndRange) || _genColVecB.n_elem != (_elemIndRange.b - _elemIndRange.a) + 1) {
          return;
        }

        cout << "- Compute expectedColVecSubvecElemDivide() ... ";

        _genColVecA.subvec(_elemIndRange.a, _elemIndRange.b) /= _genColVecB;

        save<double>("Col.subvecElemDivide", _genColVecA);
        cout << "done." << endl;
      }

  };
}
