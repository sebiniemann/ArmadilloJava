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
using arma::Col;
using arma::vec;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedInPlaceGenColVecGenMat : public Expected {
    public:
      ExpectedInPlaceGenColVecGenMat() {
        cout << "Compute ExpectedInPlaceGenColVecGenMat(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::GenColVec,
            InputClass::GenMat
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
                  _genMat = *static_cast<Mat<double>*>(value.second);
                  break;
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            _copyOfGenColVec = _genColVec;
            _copyOfGenMat = _genMat;

            expectedColInPlaceEqual();

            _genColVec = _copyOfGenColVec;
            _genMat = _copyOfGenMat;
            expectedColInPlacePlus();

            _genColVec = _copyOfGenColVec;
            _genMat = _copyOfGenMat;
            expectedColInPlaceMinus();

            _genColVec = _copyOfGenColVec;
            _genMat = _copyOfGenMat;
            expectedColInPlaceTimes();

            _genColVec = _copyOfGenColVec;
            _genMat = _copyOfGenMat;
            expectedColInPlaceElemTimes();

            _genColVec = _copyOfGenColVec;
            _genMat = _copyOfGenMat;
            expectedColInPlaceElemDivide();

            _genColVec = _copyOfGenColVec;
            _genMat = _copyOfGenMat;
            expectedColSwap();

            _genColVec = _copyOfGenColVec;
            _genMat = _copyOfGenMat;
            expectedColCopy_size();

            _genColVec = _copyOfGenColVec;
            _genMat = _copyOfGenMat;
            expectedColCol();
          }

          cout << "done." << endl;
        }

    protected:
      Col<double> _genColVec;
      Col<double> _copyOfGenColVec;

      Mat<double> _genMat;
      Mat<double> _copyOfGenMat;

      void expectedColInPlaceEqual() {
    	if(_genColVec.n_rows != _genMat.n_rows || _genColVec.n_cols != _genMat.n_cols) {
          return;
        }
        cout << "- Compute expectedColInPlaceEqual() ... ";

        _genColVec = _genMat;
        save<double>("Col.inPlaceEqual", _genColVec);

        cout << "done." << endl;
      }

      void expectedColInPlacePlus() {
        if(_genColVec.n_rows != _genMat.n_rows || _genColVec.n_cols != _genMat.n_cols) {
          return;
        }

        cout << "- Compute expectedColInPlace() ... ";

        _genColVec += _genMat;
        save<double>("Col.inPlacePlus", _genColVec);

        cout << "done." << endl;
      }

      void expectedColInPlaceMinus() {
        if(_genColVec.n_rows != _genMat.n_rows || _genColVec.n_cols != _genMat.n_cols) {
          return;
        }

        cout << "- Compute expectedColInPlaceMinus() ... ";

        _genColVec -= _genMat;
        save<double>("Col.inPlaceMinus", _genColVec);

        cout << "done." << endl;
      }

      void expectedColInPlaceTimes() {
        if(_genColVec.n_cols != _genMat.n_rows) {
          return;
        }
        if(_genColVec.n_cols != _genMat.n_cols) {
          return;
        }


        cout << "- Compute expectedColInPlaceTimes() ... ";

        _genColVec *= _genMat;
        save<double>("Col.inPlaceTimes", _genColVec);

        cout << "done." << endl;
      }

      void expectedColInPlaceElemTimes() {
        if(_genColVec.n_rows != _genMat.n_rows || _genColVec.n_cols != _genMat.n_cols) {
          return;
        }

        cout << "- Compute expectedColInPlaceElemTimes() ... ";

        _genColVec %= _genMat;
        save<double>("Col.inPlaceElemTimes", _genColVec);

        cout << "done." << endl;
      }

      void expectedColInPlaceElemDivide() {
        if(_genColVec.n_rows != _genMat.n_rows || _genColVec.n_cols != _genMat.n_cols) {
          return;
        }

        cout << "- Compute expectedColInPlaceElemDivide() ... ";

        _genColVec /= _genMat;
        save<double>("Col.inPlaceElemDivide", _genColVec);

        cout << "done." << endl;
      }

      void expectedColSwap() {
        if(!_genMat.is_colvec()) {
          return;
        }

        cout << "- Compute expectedColSwap() ... ";

        _genColVec.swap(_genMat);
        save<double>("Col.swap", _genColVec);

        cout << "done." << endl;
      }

      void expectedColCopy_size() {
        if(!_genMat.is_colvec()) {
          return;
        }
        cout << "- Compute expectedColCopy_size() ... ";

        _genColVec.copy_size(_genMat);
        save<double>("Col.copySize", _genColVec);

        cout << "done." << endl;
      }

      void expectedColCol() {
        if(!_genMat.is_colvec()) {
          return;
        }

        cout << "- Compute expectedColCol() ... ";

        _genColVec = vec(_genMat);
        save<double>("Col.col", _genColVec);

        cout << "done." << endl;
      }
  };
}
