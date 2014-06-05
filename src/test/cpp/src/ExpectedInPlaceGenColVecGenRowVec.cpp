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
using arma::Col;
using arma::vec;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedInPlaceGenColVecGenRowVec : public Expected {
    public:
      ExpectedInPlaceGenColVecGenRowVec() {
        cout << "Compute ExpectedInPlaceGenColVecGenRowVec(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::GenColVec,
            InputClass::GenRowVec
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
                  _genRowVec = *static_cast<Row<double>*>(value.second);
                  break;
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            _copyOfGenColVec = _genColVec;
            _copyOfGenRowVec = _genRowVec;

            expectedColInPlaceEqual();

            _genColVec = _copyOfGenColVec;
            _genRowVec = _copyOfGenRowVec;
            expectedColInPlacePlus();

            _genColVec = _copyOfGenColVec;
            _genRowVec = _copyOfGenRowVec;
            expectedColInPlaceMinus();

            _genColVec = _copyOfGenColVec;
            _genRowVec = _copyOfGenRowVec;
            expectedColInPlaceTimes();

            _genColVec = _copyOfGenColVec;
            _genRowVec = _copyOfGenRowVec;
            expectedColInPlaceElemTimes();

            _genColVec = _copyOfGenColVec;
            _genRowVec = _copyOfGenRowVec;
            expectedColInPlaceElemDivide();

            _genColVec = _copyOfGenColVec;
            _genRowVec = _copyOfGenRowVec;
            expectedColSwap();

            _genColVec = _copyOfGenColVec;
            _genRowVec = _copyOfGenRowVec;
            expectedColCopy_size();

            _genColVec = _copyOfGenColVec;
            _genRowVec = _copyOfGenRowVec;
            expectedColCol();
          }

          cout << "done." << endl;
        }

    protected:
      Col<double> _genColVec;
      Col<double> _copyOfGenColVec;

      Row<double> _genRowVec;
      Row<double> _copyOfGenRowVec;

      void expectedColInPlaceEqual() {
    	if(_genColVec.n_rows != _genRowVec.n_rows || _genColVec.n_cols != _genRowVec.n_cols) {
          return;
        }
        cout << "- Compute expectedColInPlaceEqual() ... ";

        _genColVec = _genRowVec;
        save<double>("Col.inPlaceEqual", _genColVec);

        cout << "done." << endl;
      }

      void expectedColInPlacePlus() {
        if(_genColVec.n_rows != _genRowVec.n_rows || _genColVec.n_cols != _genRowVec.n_cols) {
          return;
        }

        cout << "- Compute expectedColInPlace() ... ";

        _genColVec += _genRowVec;
        save<double>("Col.inPlacePlus", _genColVec);

        cout << "done." << endl;
      }

      void expectedColInPlaceMinus() {
        if(_genColVec.n_rows != _genRowVec.n_rows || _genColVec.n_cols != _genRowVec.n_cols) {
          return;
        }

        cout << "- Compute expectedColInPlaceMinus() ... ";

        _genColVec -= _genRowVec;
        save<double>("Col.inPlaceMinus", _genColVec);

        cout << "done." << endl;
      }

      void expectedColInPlaceTimes() {
        if(_genColVec.n_cols != _genRowVec.n_rows) {
          return;
        }
        if(_genColVec.n_cols != _genRowVec.n_cols) {
          return;
        }


        cout << "- Compute expectedColInPlaceTimes() ... ";

        _genColVec *= _genRowVec;
        save<double>("Col.inPlaceTimes", _genColVec);

        cout << "done." << endl;
      }

      void expectedColInPlaceElemTimes() {
        if(_genColVec.n_rows != _genRowVec.n_rows || _genColVec.n_cols != _genRowVec.n_cols) {
          return;
        }

        cout << "- Compute expectedColInPlaceElemTimes() ... ";

        _genColVec %= _genRowVec;
        save<double>("Col.inPlaceElemTimes", _genColVec);

        cout << "done." << endl;
      }

      void expectedColInPlaceElemDivide() {
        if(_genColVec.n_rows != _genRowVec.n_rows || _genColVec.n_cols != _genRowVec.n_cols) {
          return;
        }

        cout << "- Compute expectedColInPlaceElemDivide() ... ";

        _genColVec /= _genRowVec;
        save<double>("Col.inPlaceElemDivide", _genColVec);

        cout << "done." << endl;
      }

      void expectedColSwap() {
        if(!_genRowVec.is_colvec()) {
          return;
        }
        if(!_genColVec.is_rowvec()) {
          return;
        }

        cout << "- Compute expectedColSwap() ... ";

        _genColVec.swap(_genRowVec);
        save<double>("Col.swap", _genColVec);

        cout << "done." << endl;
      }

      void expectedColCopy_size() {
        if(!_genRowVec.is_colvec()) {
          return;
        }
        cout << "- Compute expectedColCopy_size() ... ";

        _genColVec.copy_size(_genRowVec);
        save<double>("Col.copySize", _genColVec);

        cout << "done." << endl;
      }

      void expectedColCol() {
        if(!_genRowVec.is_colvec()) {
          return;
        }

        cout << "- Compute expectedColCol() ... ";

        _genColVec = vec(_genRowVec);
        save<double>("Col.col", _genColVec);

        cout << "done." << endl;
      }
  };
}
