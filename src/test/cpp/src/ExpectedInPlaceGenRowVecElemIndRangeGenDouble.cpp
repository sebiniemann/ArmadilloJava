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
  class ExpectedInPlaceGenRowVecElemIndRangeGenDouble: public Expected {
    public:
      ExpectedInPlaceGenRowVecElemIndRangeGenDouble() {
        cout << "Compute ExpectedInPlaceGenRowVecElemIndRangeGenDouble(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters( {
            InputClass::GenRowVec,
            InputClass::ElemIndRange,
            InputClass::GenDouble
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
                _elemIndRange = *static_cast<span*>(value.second);
                break;
              case 2:
                _fileSuffix += "," + value.first;
                _genDouble = *static_cast<double*>(value.second);
                break;
            }
            ++n;
          }

          cout << "Using input: " << _fileSuffix << endl;

          _copyOfGenRowVec = _genRowVec;
          _copyOfElemIndRange = _elemIndRange;
          _copyOfGenDouble = _genDouble;

          expectedRowVecColsPlus();

          _genRowVec = _copyOfGenRowVec;
          _elemIndRange = _copyOfElemIndRange;
          _genDouble = _copyOfGenDouble;
          expectedRowVecColsMinus();

          _genRowVec = _copyOfGenRowVec;
          _elemIndRange = _copyOfElemIndRange;
          _genDouble = _copyOfGenDouble;
          expectedRowVecColsTimes();

          _genRowVec = _copyOfGenRowVec;
          _elemIndRange = _copyOfElemIndRange;
          _genDouble = _copyOfGenDouble;
          expectedRowVecColsDivide();

          _genRowVec = _copyOfGenRowVec;
          _elemIndRange = _copyOfElemIndRange;
          _genDouble = _copyOfGenDouble;
          expectedRowVecSubvecPlus();

          _genRowVec = _copyOfGenRowVec;
          _elemIndRange = _copyOfElemIndRange;
          _genDouble = _copyOfGenDouble;
          expectedRowVecSubvecMinus();

          _genRowVec = _copyOfGenRowVec;
          _elemIndRange = _copyOfElemIndRange;
          _genDouble = _copyOfGenDouble;
          expectedRowVecSubvecTimes();

          _genRowVec = _copyOfGenRowVec;
          _elemIndRange = _copyOfElemIndRange;
          _genDouble = _copyOfGenDouble;
          expectedRowVecSubvecDivide();
        }

        cout << "done." << endl;
      }

    protected:
      Row<double> _genRowVec;
      Row<double> _copyOfGenRowVec;

      span _elemIndRange;
      span _copyOfElemIndRange;

      double _genDouble;
      double _copyOfGenDouble;

      void expectedRowVecColsPlus() {
        if(_elemIndRange.whole) {
          return;
        }

        if (!_genRowVec.in_range(_elemIndRange)) {
          return;
        }
        cout << "- Compute expectedRowVecColsPlus() ... ";

        _genRowVec.cols(_elemIndRange.a, _elemIndRange.b) += _genDouble;

        save<double>("Row.colsPlus", _genRowVec);
        cout << "done." << endl;
      }

      void expectedRowVecColsMinus() {
        if(_elemIndRange.whole) {
          return;
        }

        if (!_genRowVec.in_range(_elemIndRange)) {
          return;
        }
        cout << "- Compute expectedRowVecColsMinus() ... ";

        _genRowVec.cols(_elemIndRange.a, _elemIndRange.b) -= _genDouble;

        save<double>("Row.colsMinus", _genRowVec);
        cout << "done." << endl;
      }

      void expectedRowVecColsTimes() {
        if(_elemIndRange.whole) {
          return;
        }

        if (!_genRowVec.in_range(_elemIndRange)) {
          return;
        }

        cout << "- Compute expectedRowVecColsTimes() ... ";

        _genRowVec.cols(_elemIndRange.a, _elemIndRange.b) *= _genDouble;

        save<double>("Row.colsTimes", _genRowVec);
        cout << "done." << endl;
      }

      void expectedRowVecColsDivide() {
        if(_elemIndRange.whole) {
          return;
        }

        if (!_genRowVec.in_range(_elemIndRange)) {
          return;
        }

        cout << "- Compute expectedRowVecColsDivide() ... ";

        _genRowVec.cols(_elemIndRange.a, _elemIndRange.b) /= _genDouble;

        save<double>("Row.colsDivide", _genRowVec);
        cout << "done." << endl;
      }

      void expectedRowVecSubvecPlus() {
        if(_elemIndRange.whole) {
          return;
        }

        if (!_genRowVec.in_range(_elemIndRange)) {
          return;
        }

        cout << "- Compute expectedRowVecSubvecPlus() ... ";

        _genRowVec.subvec(_elemIndRange.a, _elemIndRange.b) += _genDouble;

        save<double>("Row.subvecPlus", _genRowVec);
        cout << "done." << endl;
      }

      void expectedRowVecSubvecMinus() {
        if(_elemIndRange.whole) {
          return;
        }

        if (!_genRowVec.in_range(_elemIndRange)) {
          return;
        }

        cout << "- Compute expectedRowVecSubvecMinus() ... ";

        _genRowVec.subvec(_elemIndRange.a, _elemIndRange.b) -= _genDouble;

        save<double>("Row.subvecMinus", _genRowVec);
        cout << "done." << endl;
      }

      void expectedRowVecSubvecTimes() {
        if(_elemIndRange.whole) {
          return;
        }

        if (!_genRowVec.in_range(_elemIndRange)) {
          return;
        }

        cout << "- Compute expectedRowVecSubvecTimes() ... ";

        _genRowVec.subvec(_elemIndRange.a, _elemIndRange.b) *= _genDouble;

        save<double>("Row.subvecTimes", _genRowVec);
        cout << "done." << endl;
      }

      void expectedRowVecSubvecDivide() {
        if(_elemIndRange.whole) {
          return;
        }

        if (!_genRowVec.in_range(_elemIndRange)) {
          return;
        }

        cout << "- Compute expectedRowVecSubvecDivide() ... ";

        _genRowVec.subvec(_elemIndRange.a, _elemIndRange.b) /= _genDouble;

        save<double>("Row.subvecDivide", _genRowVec);
        cout << "done." << endl;
      }

  };
}
