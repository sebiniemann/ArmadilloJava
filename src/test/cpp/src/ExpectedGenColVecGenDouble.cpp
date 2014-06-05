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
using arma::uword;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedGenColVecGenDouble : public Expected {
    public:
      ExpectedGenColVecGenDouble() {
        cout << "Compute ExpectedGenColVecGenDouble(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
          InputClass::GenColVec,
          InputClass::GenDouble
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
                _genDouble = *static_cast<double*>(value.second);
                break;
            }
            ++n;
          }

          cout << "Using input: " << _fileSuffix << endl;

          expectedColPlus();
          expectedColMinus();
          expectedColTimes();
          expectedColDivide();
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
      double _genDouble;

      void expectedColPlus() {
        cout << "- Compute expectedColPlus() ... ";
        save<double>("Col.plus", (_genColVec + _genDouble));
        cout << "done." << endl;
      }

      void expectedColMinus() {
        cout << "- Compute expectedColMinus() ... ";
        save<double>("Col.minus", (_genColVec - _genDouble));
        cout << "done." << endl;
      }

      void expectedColTimes() {
        cout << "- Compute expectedColTimes() ... ";
        save<double>("Col.times", (_genColVec * _genDouble));
        cout << "done." << endl;
      }

      void expectedColDivide() {
        cout << "- Compute expectedColDivide() ... ";
        save<double>("Col.divide", (_genColVec / _genDouble));
        cout << "done." << endl;
      }

      void expectedColEquals() {
        cout << "- Compute expectedColEquals() ... ";

        Col<uword> expected = _genColVec == _genDouble;
        save<uword>("Col.equals", expected);

        cout << "done." << endl;
      }

      void expectedColNonEquals() {
        cout << "- Compute expectedColNonEquals() ... ";

        Col<uword> expected = _genColVec != _genDouble;
        save<uword>("Col.nonEquals", expected);

        cout << "done." << endl;
      }

      void expectedColGreaterThan() {
        cout << "- Compute expectedColGreaterThan() ... ";

        Col<uword> expected = _genColVec >= _genDouble;
        save<uword>("Col.greaterThan", expected);

        cout << "done." << endl;
      }

      void expectedColLessThan() {
        cout << "- Compute expectedColLessThan() ... ";

        Col<uword> expected = _genColVec <= _genDouble;
        save<uword>("Col.lessThan", expected);

        cout << "done." << endl;
      }

      void expectedColStrictGreaterThan() {
        cout << "- Compute expectedColStrictGreaterThan() ... ";

        Col<uword> expected = _genColVec > _genDouble;
        save<uword>("Col.strictGreaterThan", expected);

        cout << "done." << endl;
      }

      void expectedColStrictLessThan() {
        cout << "- Compute expectedColStrictLessThan() ... ";

        Col<uword> expected = _genColVec < _genDouble;
        save<uword>("Col.strictLessThan", expected);

        cout << "done." << endl;
      }

  };
}
