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
  class ExpectedInPlaceGenRowVecElemIndGenDouble: public Expected {
    public:
      ExpectedInPlaceGenRowVecElemIndGenDouble() {
        cout << "Compute ExpectedInPlaceGenRowVecElemIndGenDouble(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters( {
            InputClass::GenRowVec,
            InputClass::ElemInd,
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
                _elemInd = *static_cast<int*>(value.second);
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
          _copyOfElemInd = _elemInd;
          _copyOfGenDouble = _genDouble;

          expectedRowVecAtEqual();

          _genRowVec = _copyOfGenRowVec;
          _elemInd = _copyOfElemInd;
          _genDouble = _copyOfGenDouble;

          expectedRowVecAtPlus();

          _genRowVec = _copyOfGenRowVec;
          _elemInd = _copyOfElemInd;
          _genDouble = _copyOfGenDouble;

          expectedRowVecAtMinus();

          _genRowVec = _copyOfGenRowVec;
          _elemInd = _copyOfElemInd;
          _genDouble = _copyOfGenDouble;

          expectedRowVecAtTimes();

          _genRowVec = _copyOfGenRowVec;
          _elemInd = _copyOfElemInd;
          _genDouble = _copyOfGenDouble;

          expectedRowVecAtDivide();

          _genRowVec = _copyOfGenRowVec;
          _elemInd = _copyOfElemInd;
          _genDouble = _copyOfGenDouble;

          expectedRowVecColPlus();

          _genRowVec = _copyOfGenRowVec;
          _elemInd = _copyOfElemInd;
          _genDouble = _copyOfGenDouble;

          expectedRowVecColMinus();

          _genRowVec = _copyOfGenRowVec;
          _elemInd = _copyOfElemInd;
          _genDouble = _copyOfGenDouble;

          expectedRowVecColTimes();

          _genRowVec = _copyOfGenRowVec;
          _elemInd = _copyOfElemInd;
          _genDouble = _copyOfGenDouble;

          expectedRowVecColDivide();
        }

        cout << "done." << endl;
      }

    protected:
      Row<double> _genRowVec;
      Row<double> _copyOfGenRowVec;

      int _elemInd;
      int _copyOfElemInd;

      double _genDouble;
      double _copyOfGenDouble;

      void expectedRowVecAtEqual() {
        if (_elemInd >= _genRowVec.n_elem) {
          return;
        }

        _genRowVec.at(_elemInd) = _genDouble;

        cout << "- Compute expectedRowVecAtEqual() ... ";
        save<double>("Row.atEqual", _genRowVec);
        cout << "done." << endl;
      }

      void expectedRowVecAtPlus() {
        if (_elemInd >= _genRowVec.n_elem) {
          return;
        }

        _genRowVec.at(_elemInd) += _genDouble;

        cout << "- Compute expectedRowVecAtPlus() ... ";
        save<double>("Row.atPlus", _genRowVec);
        cout << "done." << endl;
      }

      void expectedRowVecAtMinus() {
        if (_elemInd >= _genRowVec.n_elem) {
          return;
        }

        _genRowVec.at(_elemInd) -= _genDouble;

        cout << "- Compute expectedRowVecAtMinus() ... ";
        save<double>("Row.atMinus", _genRowVec);
        cout << "done." << endl;
      }

      void expectedRowVecAtTimes() {
        if (_elemInd >= _genRowVec.n_elem) {
          return;
        }

        _genRowVec.at(_elemInd) *= _genDouble;

        cout << "- Compute expectedRowVecAtTimes() ... ";
        save<double>("Row.atTimes", _genRowVec);
        cout << "done." << endl;
      }

      void expectedRowVecAtDivide() {
        if (_elemInd >= _genRowVec.n_elem) {
          return;
        }

        _genRowVec.at(_elemInd) /= _genDouble;

        cout << "- Compute expectedRowVecAtDivide() ... ";
        save<double>("Row.atDivide", _genRowVec);
        cout << "done." << endl;
      }

      void expectedRowVecColPlus() {
        if (_elemInd >= _genRowVec.n_elem) {
          return;
        }

        _genRowVec.col(_elemInd) += _genDouble;

        cout << "- Compute expectedRowVecRowPlus() ... ";
        save<double>("Row.colPlus", _genRowVec);
        cout << "done." << endl;
      }

      void expectedRowVecColMinus() {
        if (_elemInd >= _genRowVec.n_elem) {
          return;
        }

        _genRowVec.col(_elemInd) -= _genDouble;

        cout << "- Compute expectedRowVecRowMinus() ... ";
        save<double>("Row.colMinus", _genRowVec);
        cout << "done." << endl;
      }

      void expectedRowVecColTimes() {
        if (_elemInd >= _genRowVec.n_elem) {
          return;
        }

        _genRowVec.col(_elemInd) *= _genDouble;

        cout << "- Compute expectedRowVecRowTimes() ... ";
        save<double>("Row.colTimes", _genRowVec);
        cout << "done." << endl;
      }

      void expectedRowVecColDivide() {
        if (_elemInd >= _genRowVec.n_elem) {
          return;
        }

        _genRowVec.col(_elemInd) /= _genDouble;

        cout << "- Compute expectedRowVecRowDivide() ... ";
        save<double>("Row.colDivide", _genRowVec);
        cout << "done." << endl;
      }

  };
}
