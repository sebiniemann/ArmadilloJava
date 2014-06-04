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

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedGenRowVecGenDouble : public Expected {
    public:
      ExpectedGenRowVecGenDouble() {
        cout << "Compute ExpectedGenRowVecGenDouble(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
          InputClass::GenRowVec,
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
                _genDouble = *static_cast<double*>(value.second);
                break;
            }
            ++n;
          }

          cout << "Using input: " << _fileSuffix << endl;

          expectedRowPlus();
          expectedRowMinus();
          expectedRowTimes();
          expectedRowDivide();
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
      double _genDouble;

      void expectedRowPlus() {
        cout << "- Compute expectedRowPlus() ... ";
        save<double>("Row.plus", _genRowVec + _genDouble);
        cout << "done." << endl;
      }

      void expectedRowMinus() {
        cout << "- Compute expectedRowMinus() ... ";
        save<double>("Row.minus", _genRowVec - _genDouble);
        cout << "done." << endl;
      }

      void expectedRowTimes() {
        cout << "- Compute expectedRowTimes() ... ";
        save<double>("Row.times", _genRowVec * _genDouble);
        cout << "done." << endl;
      }

      void expectedRowDivide() {
        cout << "- Compute expectedRowElemDivide() ... ";
        save<double>("Row.Divide", _genRowVec / _genDouble);
        cout << "done." << endl;
      }

      void expectedRowEquals() {
        cout << "- Compute expectedRowEquals() ... ";

        Mat<uword> expected = _genRowVec == _genDouble;
        save<uword>("Row.equals", expected);

        cout << "done." << endl;
      }

      void expectedRowNonEquals() {
        cout << "- Compute expectedRowNonEquals() ... ";

        Mat<uword> expected = _genRowVec != _genDouble;
        save<uword>("Row.nonEquals", expected);

        cout << "done." << endl;
      }

      void expectedRowGreaterThan() {
        cout << "- Compute expectedRowGreaterThan() ... ";

        Mat<uword> expected = _genRowVec >= _genDouble;
        save<uword>("Row.greaterThan", expected);

        cout << "done." << endl;
      }

      void expectedRowLessThan() {
        cout << "- Compute expectedRowLessThan() ... ";

        Mat<uword> expected = _genRowVec <= _genDouble;
        save<uword>("Row.lessThan", expected);

        cout << "done." << endl;
      }

      void expectedRowStrictGreaterThan() {
        cout << "- Compute expectedRowStrictGreaterThan() ... ";

        Mat<uword> expected = _genRowVec > _genDouble;
        save<uword>("Row.strictGreaterThan", expected);

        cout << "done." << endl;
      }

      void expectedRowStrictLessThan() {
        cout << "- Compute expectedRowStrictLessThan() ... ";

        Mat<uword> expected = _genRowVec < _genDouble;
        save<uword>("Row.strictLessThan", expected);

        cout << "done." << endl;
      }

  };
}
