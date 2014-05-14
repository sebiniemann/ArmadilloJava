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

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedInPlaceGenMatGenColVec : public Expected {
    public:
      ExpectedInPlaceGenMatGenColVec() {
        cout << "Compute ExpectedInPlaceGenMatGenColVec(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::GenMat,
            InputClass::GenColVec
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
                  _genColVec = *static_cast<Col<double>*>(value.second);
                  break;
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            _copyOfGenMat = _genMat;
            _copyOfGenColVec = _genColVec;

            expectedMatInPlaceEqual();

            _genMat = _copyOfGenMat;
            _genColVec = _copyOfGenColVec;
            expectedMatInPlacePlus();

            _genMat = _copyOfGenMat;
            _genColVec = _copyOfGenColVec;
            expectedMatInPlaceMinus();

            _genMat = _copyOfGenMat;
            _genColVec = _copyOfGenColVec;
            expectedMatInPlaceTimes();

            _genMat = _copyOfGenMat;
            _genColVec = _copyOfGenColVec;
            expectedMatInPlaceElemTimes();

            _genMat = _copyOfGenMat;
            _genColVec = _copyOfGenColVec;
            expectedMatInPlaceElemDivide();

            _genMat = _copyOfGenMat;
            _genColVec = _copyOfGenColVec;
            expectedMatSwap();

            _genMat = _copyOfGenMat;
            _genColVec = _copyOfGenColVec;
            expectedMatEach_colEqual();

            _genMat = _copyOfGenMat;
            _genColVec = _copyOfGenColVec;
            expectedMatEach_colPlus();

            _genMat = _copyOfGenMat;
            _genColVec = _copyOfGenColVec;
            expectedMatEach_colMinus();

            _genMat = _copyOfGenMat;
            _genColVec = _copyOfGenColVec;
            expectedMatEach_colElemTimes();

            _genMat = _copyOfGenMat;
            _genColVec = _copyOfGenColVec;
            expectedMatEach_colElemDivide();

            _genMat = _copyOfGenMat;
            _genColVec = _copyOfGenColVec;
            expectedMatEach_rowEqual();

            _genMat = _copyOfGenMat;
            _genColVec = _copyOfGenColVec;
            expectedMatEach_rowPlus();

            _genMat = _copyOfGenMat;
            _genColVec = _copyOfGenColVec;
            expectedMatEach_rowMinus();

            _genMat = _copyOfGenMat;
            _genColVec = _copyOfGenColVec;
            expectedMatEach_rowElemTimes();

            _genMat = _copyOfGenMat;
            _genColVec = _copyOfGenColVec;
            expectedMatEach_rowElemDivide();

            _genMat = _copyOfGenMat;
            _genColVec = _copyOfGenColVec;
            expectedMatDiagEqual();

            _genMat = _copyOfGenMat;
            _genColVec = _copyOfGenColVec;
            expectedMatDiagPlus();

            _genMat = _copyOfGenMat;
            _genColVec = _copyOfGenColVec;
            expectedMatDiagMinus();

            _genMat = _copyOfGenMat;
            _genColVec = _copyOfGenColVec;
            expectedMatDiagElemTimes();

            _genMat = _copyOfGenMat;
            _genColVec = _copyOfGenColVec;
            expectedMatDiagElemDivide();

            _genMat = _copyOfGenMat;
            _genColVec = _copyOfGenColVec;
            expectedMatCopy_size();
          }

          cout << "done." << endl;
        }

    protected:
      Mat<double> _genMat;
      Mat<double> _copyOfGenMat;

      Col<double> _genColVec;
      Col<double> _copyOfGenColVec;

      void expectedMatInPlaceEqual() {
        cout << "- Compute expectedMatInPlaceEqual() ... ";

        _genMat = _genColVec;
        save<double>("Mat.inPlaceEqual", _genMat);

        cout << "done." << endl;
      }

      void expectedMatInPlacePlus() {
        if(_genMat.n_rows != _genColVec.n_rows || _genMat.n_cols != _genColVec.n_cols) {
          return;
        }

        cout << "- Compute expectedMatInPlace() ... ";

        _genMat += _genColVec;
        save<double>("Mat.inPlacePlus", _genMat);

        cout << "done." << endl;
      }

      void expectedMatInPlaceMinus() {
        if(_genMat.n_rows != _genColVec.n_rows || _genMat.n_cols != _genColVec.n_cols) {
          return;
        }

        cout << "- Compute expectedMatInPlaceMinus() ... ";

        _genMat -= _genColVec;
        save<double>("Mat.inPlaceMinus", _genMat);

        cout << "done." << endl;
      }

      void expectedMatInPlaceTimes() {
        if(_genMat.n_cols != _genColVec.n_rows) {
          return;
        }

        cout << "- Compute expectedMatInPlaceTimes() ... ";

        _genMat *= _genColVec;
        save<double>("Mat.inPlaceTimes", _genMat);

        cout << "done." << endl;
      }

      void expectedMatInPlaceElemTimes() {
        if(_genMat.n_rows != _genColVec.n_rows || _genMat.n_cols != _genColVec.n_cols) {
          return;
        }

        cout << "- Compute expectedMatInPlaceElemTimes() ... ";

        _genMat %= _genColVec;
        save<double>("Mat.inPlaceElemTimes", _genMat);

        cout << "done." << endl;
      }

      void expectedMatInPlaceElemDivide() {
        if(_genMat.n_rows != _genColVec.n_rows || _genMat.n_cols != _genColVec.n_cols) {
          return;
        }

        cout << "- Compute expectedMatInPlaceElemDivide() ... ";

        _genMat /= _genColVec;
        save<double>("Mat.inPlaceElemDivide", _genMat);

        cout << "done." << endl;
      }

      void expectedMatSwap() {
        if(!_genMat.is_colvec()) {
          return;
        }

        cout << "- Compute expectedMatSwap() ... ";

        _genMat.swap(_genColVec);
        save<double>("Mat.swap", _genMat);

        cout << "done." << endl;
      }

      void expectedMatEach_colEqual() {
        if(_genMat.n_rows != _genColVec.n_rows) {
          return;
        }

        cout << "- Compute expectedMatEach_colEqual() ... ";

        _genMat.each_col() = _genColVec;
        save<double>("Mat.each_colEqual", _genMat);

        cout << "done." << endl;
      }

      void expectedMatEach_colPlus() {
        if(_genMat.n_rows != _genColVec.n_rows) {
          return;
        }

        cout << "- Compute expectedMatEach_colPlus() ... ";

        _genMat.each_col() += _genColVec;
        save<double>("Mat.each_colPlus", _genMat);

        cout << "done." << endl;
      }

      void expectedMatEach_colMinus() {
        if(_genMat.n_rows != _genColVec.n_rows) {
          return;
        }

        cout << "- Compute expectedMatEach_colMinus() ... ";

        _genMat.each_col() -= _genColVec;
        save<double>("Mat.each_colMinus", _genMat);

        cout << "done." << endl;
      }

      void expectedMatEach_colElemTimes() {
        if(_genMat.n_rows != _genColVec.n_rows) {
          return;
        }

        cout << "- Compute expectedMatEach_colElemTimes() ... ";

        _genMat.each_col() %= _genColVec;
        save<double>("Mat.each_colElemTimes", _genMat);

        cout << "done." << endl;
      }

      void expectedMatEach_colElemDivide() {
        if(_genMat.n_rows != _genColVec.n_rows) {
          return;
        }

        cout << "- Compute expectedMatEach_colElemDivide() ... ";

        _genMat.each_col() /= _genColVec;
        save<double>("Mat.each_colElemDivide", _genMat);

        cout << "done." << endl;
      }

      void expectedMatEach_rowEqual() {
        if(!_genColVec.is_rowvec() || _genMat.n_cols != _genColVec.n_cols) {
          return;
        }

        cout << "- Compute expectedMatEach_rowEqual() ... ";

        _genMat.each_row() = _genColVec;
        save<double>("Mat.each_rowEqual", _genMat);

        cout << "done." << endl;
      }

      void expectedMatEach_rowPlus() {
        if(!_genColVec.is_rowvec() || _genMat.n_cols != _genColVec.n_cols) {
          return;
        }

        cout << "- Compute expectedMatEach_rowPlus() ... ";

        _genMat.each_row() += _genColVec;
        save<double>("Mat.each_rowPlus", _genMat);

        cout << "done." << endl;
      }

      void expectedMatEach_rowMinus() {
        if(!_genColVec.is_rowvec() || _genMat.n_cols != _genColVec.n_cols) {
          return;
        }

        cout << "- Compute expectedMatEach_rowMinus() ... ";

        _genMat.each_row() -= _genColVec;
        save<double>("Mat.each_rowMinus", _genMat);

        cout << "done." << endl;
      }

      void expectedMatEach_rowElemTimes() {
        if(!_genColVec.is_rowvec() || _genMat.n_cols != _genColVec.n_cols) {
          return;
        }

        cout << "- Compute expectedMatEach_rowElemTimes() ... ";

        _genMat.each_row() %= _genColVec;
        save<double>("Mat.each_rowElemTimes", _genMat);

        cout << "done." << endl;
      }

      void expectedMatEach_rowElemDivide() {
        if(!_genColVec.is_rowvec() || _genMat.n_cols != _genColVec.n_cols) {
          return;
        }

        cout << "- Compute expectedMatEach_rowElemDivide() ... ";

        _genMat.each_row() /= _genColVec;
        save<double>("Mat.each_rowElemDivide", _genMat);

        cout << "done." << endl;
      }

      void expectedMatDiagEqual() {
        if(_genColVec.n_elem != min(_genMat.n_rows, _genMat.n_cols)) {
          return;
        }

        cout << "- Compute expectedMatDiagEqual() ... ";

        _genMat.diag() = _genColVec;
        save<double>("Mat.diagEqual", _genMat);

        cout << "done." << endl;
      }

      void expectedMatDiagPlus() {
        if(_genColVec.n_elem != min(_genMat.n_rows, _genMat.n_cols)) {
          return;
        }

        cout << "- Compute expectedMatDiagPlus() ... ";

        _genMat.diag() += _genColVec;
        save<double>("Mat.diagPlus", _genMat);

        cout << "done." << endl;
      }

      void expectedMatDiagMinus() {
        if(_genColVec.n_elem != min(_genMat.n_rows, _genMat.n_cols)) {
          return;
        }

        cout << "- Compute expectedMatDiagMinus() ... ";

        _genMat.diag() -= _genColVec;
        save<double>("Mat.diagMinus", _genMat);

        cout << "done." << endl;
      }

      void expectedMatDiagElemTimes() {
        if(_genColVec.n_elem != min(_genMat.n_rows, _genMat.n_cols)) {
          return;
        }

        cout << "- Compute expectedMatDiagElemTimes() ... ";

        _genMat.diag() %= _genColVec;
        save<double>("Mat.diagElemTimes", _genMat);

        cout << "done." << endl;
      }

      void expectedMatDiagElemDivide() {
        if(_genColVec.n_elem != min(_genMat.n_rows, _genMat.n_cols)) {
          return;
        }

        cout << "- Compute expectedMatDiagElemDivide() ... ";

        _genMat.diag() /= _genColVec;
        save<double>("Mat.diagElemDivide", _genMat);

        cout << "done." << endl;
      }

      void expectedMatCopy_size() {
        cout << "- Compute expectedMatCopy_size() ... ";

        _genMat.copy_size(_genColVec);
        save<double>("Mat.copy_size", _genMat);

        cout << "done." << endl;
      }
  };
}
