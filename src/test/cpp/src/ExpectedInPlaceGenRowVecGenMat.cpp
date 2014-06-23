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
using arma::Mat;
using arma::Row;
using arma::rowvec;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedInPlaceGenRowVecGenMat : public Expected {
    public:
      ExpectedInPlaceGenRowVecGenMat() {
        cout << "Compute ExpectedInPlaceGenRowVecGenMat(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::GenRowVec,
            InputClass::GenMat
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
                  _genMat = *static_cast<Mat<double>*>(value.second);
                  break;
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            _copyOfGenRowVec = _genRowVec;
            _copyOfGenMat = _genMat;

            expectedRowInPlaceEqual();

            _genRowVec = _copyOfGenRowVec;
            _genMat = _copyOfGenMat;
            expectedRowInPlacePlus();

            _genRowVec = _copyOfGenRowVec;
            _genMat = _copyOfGenMat;
            expectedRowInPlaceMinus();

            _genRowVec = _copyOfGenRowVec;
            _genMat = _copyOfGenMat;
            expectedRowInPlaceTimes();

            _genRowVec = _copyOfGenRowVec;
            _genMat = _copyOfGenMat;
            expectedRowInPlaceElemTimes();

            _genRowVec = _copyOfGenRowVec;
            _genMat = _copyOfGenMat;
            expectedRowInPlaceElemDivide();

            _genRowVec = _copyOfGenRowVec;
            _genMat = _copyOfGenMat;
            expectedRowSwap();

            _genRowVec = _copyOfGenRowVec;
            _genMat = _copyOfGenMat;
            expectedRowCopy_size();

            _genRowVec = _copyOfGenRowVec;
            _genMat = _copyOfGenMat;
            expectedRowRow();
          }

          cout << "done." << endl;
        }

    protected:
      Row<double> _genRowVec;
      Row<double> _copyOfGenRowVec;

      Mat<double> _genMat;
      Mat<double> _copyOfGenMat;

      void expectedRowInPlaceEqual() {
    	if(_genRowVec.n_cols != _genMat.n_cols || _genRowVec.n_rows != _genMat.n_rows) {
          return;
        }
        cout << "- Compute expectedRowInPlaceEqual() ... ";

        _genRowVec = _genMat;
        save<double>("row.inPlaceEqual", _genRowVec);

        cout << "done." << endl;
      }

      void expectedRowInPlacePlus() {
        if(_genRowVec.n_cols != _genMat.n_cols || _genRowVec.n_rows != _genMat.n_rows) {
          return;
        }

        cout << "- Compute expectedRowInPlace() ... ";

        _genRowVec += _genMat;
        save<double>("row.inPlacePlus", _genRowVec);

        cout << "done." << endl;
      }

      void expectedRowInPlaceMinus() {
        if(_genRowVec.n_cols != _genMat.n_cols || _genRowVec.n_rows != _genMat.n_rows) {
          return;
        }

        cout << "- Compute expectedRowInPlaceMinus() ... ";

        _genRowVec -= _genMat;
        save<double>("row.inPlaceMinus", _genRowVec);

        cout << "done." << endl;
      }

      void expectedRowInPlaceTimes() {
        if(_genRowVec.n_cols != _genMat.n_rows) {
          return;
        }
        if(_genRowVec.n_cols != _genMat.n_cols) {
          return;
        }


        cout << "- Compute expectedRowInPlaceTimes() ... ";

        _genRowVec *= _genMat;
        save<double>("row.inPlaceTimes", _genRowVec);

        cout << "done." << endl;
      }

      void expectedRowInPlaceElemTimes() {
        if(_genRowVec.n_cols != _genMat.n_cols || _genRowVec.n_rows != _genMat.n_rows) {
          return;
        }

        cout << "- Compute expectedRowInPlaceElemTimes() ... ";

        _genRowVec %= _genMat;
        save<double>("row.inPlaceElemTimes", _genRowVec);

        cout << "done." << endl;
      }

      void expectedRowInPlaceElemDivide() {
        if(_genRowVec.n_cols != _genMat.n_cols || _genRowVec.n_rows != _genMat.n_rows) {
          return;
        }

        cout << "- Compute expectedRowInPlaceElemDivide() ... ";

        _genRowVec /= _genMat;
        save<double>("row.inPlaceElemDivide", _genRowVec);

        cout << "done." << endl;
      }

      void expectedRowSwap() {
        if(!_genMat.is_rowvec()) {
          return;
        }

        cout << "- Compute expectedRowSwap() ... ";

        _genRowVec.swap(_genMat);
        save<double>("row.swap", _genRowVec);

        cout << "done." << endl;
      }

      void expectedRowCopy_size() {
        if(!_genMat.is_rowvec()) {
          return;
        }
        cout << "- Compute expectedRowCopy_size() ... ";

        _genRowVec.copy_size(_genMat);
        save<double>("row.copySize", _genRowVec);

        cout << "done." << endl;
      }

      void expectedRowRow() {
        if(!_genMat.is_rowvec()) {
          return;
        }

        cout << "- Compute expectedRowRow() ... ";

        _genRowVec = rowvec(_genMat);
        save<double>("row.row", _genRowVec);

        cout << "done." << endl;
      }
  };
}
