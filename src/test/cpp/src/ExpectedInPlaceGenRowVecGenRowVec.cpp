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

#include <algorithm>
using std::min;

#include <utility>
using std::pair;

#include <armadillo>
using arma::Row;
using arma::rowvec;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedInPlaceGenRowVecGenRowVec : public Expected {
    public:
      ExpectedInPlaceGenRowVecGenRowVec() {
        cout << "Compute ExpectedInPlaceGenRowVecGenRowVec(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::GenRowVec,
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
                  _genRowVecB = *static_cast<Row<double>*>(value.second);
                  break;
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            _copyOfGenRowVecA = _genRowVecA;
            _copyOfGenRowVecB = _genRowVecB;

            expectedRowInPlaceEqual();

            _genRowVecA = _copyOfGenRowVecA;
            _genRowVecB = _copyOfGenRowVecB;
            expectedRowInPlacePlus();

            _genRowVecA = _copyOfGenRowVecA;
            _genRowVecB = _copyOfGenRowVecB;
            expectedRowInPlaceMinus();

            _genRowVecA = _copyOfGenRowVecA;
            _genRowVecB = _copyOfGenRowVecB;
            expectedRowInPlaceTimes();

            _genRowVecA = _copyOfGenRowVecA;
            _genRowVecB = _copyOfGenRowVecB;
            expectedRowInPlaceElemTimes();

            _genRowVecA = _copyOfGenRowVecA;
            _genRowVecB = _copyOfGenRowVecB;
            expectedRowInPlaceElemDivide();

            _genRowVecA = _copyOfGenRowVecA;
            _genRowVecB = _copyOfGenRowVecB;
            expectedRowSwap();

            _genRowVecA = _copyOfGenRowVecA;
            _genRowVecB = _copyOfGenRowVecB;
            expectedRowCopy_size();

            _genRowVecA = _copyOfGenRowVecA;
            _genRowVecB = _copyOfGenRowVecB;
            expectedRowRow();
          }

          cout << "done." << endl;
        }

    protected:
      Row<double> _genRowVecA;
      Row<double> _copyOfGenRowVecA;

      Row<double> _genRowVecB;
      Row<double> _copyOfGenRowVecB;

      void expectedRowInPlaceEqual() {
    	if(_genRowVecA.n_cols != _genRowVecB.n_cols || _genRowVecA.n_rows != _genRowVecB.n_rows) {
          return;
        }
        cout << "- Compute expectedRowInPlaceEqual() ... ";

        _genRowVecA = _genRowVecB;
        save<double>("Row.inPlaceEqual", _genRowVecA);

        cout << "done." << endl;
      }

      void expectedRowInPlacePlus() {
        if(_genRowVecA.n_cols != _genRowVecB.n_cols || _genRowVecA.n_rows != _genRowVecB.n_rows) {
          return;
        }

        cout << "- Compute expectedRowInPlace() ... ";

        _genRowVecA += _genRowVecB;
        save<double>("Row.inPlacePlus", _genRowVecA);

        cout << "done." << endl;
      }

      void expectedRowInPlaceMinus() {
        if(_genRowVecA.n_cols != _genRowVecB.n_cols || _genRowVecA.n_rows != _genRowVecB.n_rows) {
          return;
        }

        cout << "- Compute expectedRowInPlaceMinus() ... ";

        _genRowVecA -= _genRowVecB;
        save<double>("Row.inPlaceMinus", _genRowVecA);

        cout << "done." << endl;
      }

      void expectedRowInPlaceTimes() {
        if(_genRowVecA.n_cols != _genRowVecB.n_rows) {
          return;
        }
        if(_genRowVecA.n_cols != _genRowVecB.n_cols) {
          return;
        }


        cout << "- Compute expectedRowInPlaceTimes() ... ";

        _genRowVecA *= _genRowVecB;
        save<double>("Row.inPlaceTimes", _genRowVecA);

        cout << "done." << endl;
      }

      void expectedRowInPlaceElemTimes() {
        if(_genRowVecA.n_cols != _genRowVecB.n_cols || _genRowVecA.n_rows != _genRowVecB.n_rows) {
          return;
        }

        cout << "- Compute expectedRowInPlaceElemTimes() ... ";

        _genRowVecA %= _genRowVecB;
        save<double>("Row.inPlaceElemTimes", _genRowVecA);

        cout << "done." << endl;
      }

      void expectedRowInPlaceElemDivide() {
        if(_genRowVecA.n_cols != _genRowVecB.n_cols || _genRowVecA.n_rows != _genRowVecB.n_rows) {
          return;
        }

        cout << "- Compute expectedRowInPlaceElemDivide() ... ";

        _genRowVecA /= _genRowVecB;
        save<double>("Row.inPlaceElemDivide", _genRowVecA);

        cout << "done." << endl;
      }

      void expectedRowSwap() {
        if(!_genRowVecB.is_rowvec()) {
          return;
        }

        cout << "- Compute expectedRowSwap() ... ";

        _genRowVecA.swap(_genRowVecB);
        save<double>("Row.swap", _genRowVecA);

        cout << "done." << endl;
      }

      void expectedRowCopy_size() {
        if(!_genRowVecB.is_rowvec()) {
          return;
        }
        cout << "- Compute expectedRowCopy_size() ... ";

        _genRowVecA.copy_size(_genRowVecB);
        save<double>("Row.copy_size", _genRowVecA);

        cout << "done." << endl;
      }

      void expectedRowRow() {
        if(!_genRowVecB.is_rowvec()) {
          return;
        }

        cout << "- Compute expectedRowRow() ... ";

        _genRowVecA = rowvec(_genRowVecB);
        save<double>("Row.row", _genRowVecA);

        cout << "done." << endl;
      }
  };
}
