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
using arma::Col;
using arma::vec;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedInPlaceGenColVecGenColVec : public Expected {
    public:
      ExpectedInPlaceGenColVecGenColVec() {
        cout << "Compute ExpectedInPlaceGenColVecGenColVec(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::GenColVec,
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
                  _genColVecB = *static_cast<Col<double>*>(value.second);
                  break;
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            _copyOfGenColVecA = _genColVecA;
            _copyOfGenColVecB = _genColVecB;

            expectedColInPlaceEqual();

            _genColVecA = _copyOfGenColVecA;
            _genColVecB = _copyOfGenColVecB;
            expectedColInPlacePlus();

            _genColVecA = _copyOfGenColVecA;
            _genColVecB = _copyOfGenColVecB;
            expectedColInPlaceMinus();

            _genColVecA = _copyOfGenColVecA;
            _genColVecB = _copyOfGenColVecB;
            expectedColInPlaceTimes();

            _genColVecA = _copyOfGenColVecA;
            _genColVecB = _copyOfGenColVecB;
            expectedColInPlaceElemTimes();

            _genColVecA = _copyOfGenColVecA;
            _genColVecB = _copyOfGenColVecB;
            expectedColInPlaceElemDivide();

            _genColVecA = _copyOfGenColVecA;
            _genColVecB = _copyOfGenColVecB;
            expectedColSwap();

            _genColVecA = _copyOfGenColVecA;
            _genColVecB = _copyOfGenColVecB;
            expectedColCopy_size();

            _genColVecA = _copyOfGenColVecA;
            _genColVecB = _copyOfGenColVecB;
            expectedColCol();
          }

          cout << "done." << endl;
        }

    protected:
      Col<double> _genColVecA;
      Col<double> _copyOfGenColVecA;

      Col<double> _genColVecB;
      Col<double> _copyOfGenColVecB;

      void expectedColInPlaceEqual() {
    	if(_genColVecA.n_rows != _genColVecB.n_rows || _genColVecA.n_cols != _genColVecB.n_cols) {
          return;
        }
        cout << "- Compute expectedColInPlaceEqual() ... ";

        _genColVecA = _genColVecB;
        save<double>("Col.inPlaceEqual", _genColVecA);

        cout << "done." << endl;
      }

      void expectedColInPlacePlus() {
        if(_genColVecA.n_rows != _genColVecB.n_rows || _genColVecA.n_cols != _genColVecB.n_cols) {
          return;
        }

        cout << "- Compute expectedColInPlace() ... ";

        _genColVecA += _genColVecB;
        save<double>("Col.inPlacePlus", _genColVecA);

        cout << "done." << endl;
      }

      void expectedColInPlaceMinus() {
        if(_genColVecA.n_rows != _genColVecB.n_rows || _genColVecA.n_cols != _genColVecB.n_cols) {
          return;
        }

        cout << "- Compute expectedColInPlaceMinus() ... ";

        _genColVecA -= _genColVecB;
        save<double>("Col.inPlaceMinus", _genColVecA);

        cout << "done." << endl;
      }

      void expectedColInPlaceTimes() {
        if(_genColVecA.n_cols != _genColVecB.n_rows) {
          return;
        }
        if(_genColVecA.n_cols != _genColVecB.n_cols) {
          return;
        }


        cout << "- Compute expectedColInPlaceTimes() ... ";

        _genColVecA *= _genColVecB;
        save<double>("Col.inPlaceTimes", _genColVecA);

        cout << "done." << endl;
      }

      void expectedColInPlaceElemTimes() {
        if(_genColVecA.n_rows != _genColVecB.n_rows || _genColVecA.n_cols != _genColVecB.n_cols) {
          return;
        }

        cout << "- Compute expectedColInPlaceElemTimes() ... ";

        _genColVecA %= _genColVecB;
        save<double>("Col.inPlaceElemTimes", _genColVecA);

        cout << "done." << endl;
      }

      void expectedColInPlaceElemDivide() {
        if(_genColVecA.n_rows != _genColVecB.n_rows || _genColVecA.n_cols != _genColVecB.n_cols) {
          return;
        }

        cout << "- Compute expectedColInPlaceElemDivide() ... ";

        _genColVecA /= _genColVecB;
        save<double>("Col.inPlaceElemDivide", _genColVecA);

        cout << "done." << endl;
      }

      void expectedColSwap() {
        if(!_genColVecB.is_colvec()) {
          return;
        }

        cout << "- Compute expectedColSwap() ... ";

        _genColVecA.swap(_genColVecB);
        save<double>("Col.swap", _genColVecA);

        cout << "done." << endl;
      }

      void expectedColCopy_size() {
        if(!_genColVecB.is_colvec()) {
          return;
        }
        cout << "- Compute expectedColCopy_size() ... ";

        _genColVecA.copy_size(_genColVecB);
        save<double>("Col.copySize", _genColVecA);

        cout << "done." << endl;
      }

      void expectedColCol() {
        if(!_genColVecB.is_colvec()) {
          return;
        }

        cout << "- Compute expectedColCol() ... ";

        _genColVecA = vec(_genColVecB);
        save<double>("Col.col", _genColVecA);

        cout << "done." << endl;
      }
  };
}
