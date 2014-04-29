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
using arma::uword;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedGenMatGenDouble : public Expected {
    public:
      ExpectedGenMatGenDouble() {
        cout << "Compute ExpectedGenMatGenDouble(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
          InputClass::GenMat,
          InputClass::GenDouble
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
                _genDouble = *static_cast<double*>(value.second);
                break;
            }
            ++n;
          }

          cout << "Using input: " << _fileSuffix << endl;

          expectedMatPlus();
          expectedMatMinus();
          expectedMatTimes();
          expectedMatDivide();
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
      double _genDouble;

      void expectedMatPlus() {
        cout << "- Compute expectedMatPlus() ... ";
        save<double>("Mat.plus", (_genMat + _genDouble));
        cout << "done." << endl;
      }

      void expectedMatMinus() {
        cout << "- Compute expectedMatMinus() ... ";
        save<double>("Mat.minus", (_genMat - _genDouble));
        cout << "done." << endl;
      }

      void expectedMatTimes() {
        cout << "- Compute expectedMatTimes() ... ";
        save<double>("Mat.times", (_genMat * _genDouble));
        cout << "done." << endl;
      }

      void expectedMatDivide() {
        cout << "- Compute expectedMatDivide() ... ";
        save<double>("Mat.divide", (_genMat / _genDouble));
        cout << "done." << endl;
      }

      void expectedMatEquals() {
        cout << "- Compute expectedMatEquals() ... ";

        Mat<uword> expected = _genMat == _genDouble;
        save<uword>("Mat.equals", expected);

        cout << "done." << endl;
      }

      void expectedMatNonEquals() {
        cout << "- Compute expectedMatNonEquals() ... ";

        Mat<uword> expected = _genMat != _genDouble;
        save<uword>("Mat.nonEquals", expected);

        cout << "done." << endl;
      }

      void expectedMatGreaterThan() {
        cout << "- Compute expectedMatGreaterThan() ... ";

        Mat<uword> expected = _genMat >= _genDouble;
        save<uword>("Mat.greaterThan", expected);

        cout << "done." << endl;
      }

      void expectedMatLessThan() {
        cout << "- Compute expectedMatLessThan() ... ";

        Mat<uword> expected = _genMat <= _genDouble;
        save<uword>("Mat.lessThan", expected);

        cout << "done." << endl;
      }

      void expectedMatStrictGreaterThan() {
        cout << "- Compute expectedMatStrictGreaterThan() ... ";

        Mat<uword> expected = _genMat > _genDouble;
        save<uword>("Mat.strictGreaterThan", expected);

        cout << "done." << endl;
      }

      void expectedMatStrictLessThan() {
        cout << "- Compute expectedMatStrictLessThan() ... ";

        Mat<uword> expected = _genMat < _genDouble;
        save<uword>("Mat.strictLessThan", expected);

        cout << "done." << endl;
      }

  };
}
