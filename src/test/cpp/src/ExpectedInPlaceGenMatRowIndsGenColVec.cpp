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
using arma::Row;
using arma::Col;
using arma::uword;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedInPlaceGenMatRowIndsGenColVec : public Expected {
    public:
      ExpectedInPlaceGenMatRowIndsGenColVec() {
        cout << "Compute ExpectedInPlaceGenMatRowIndsGenColVec(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
          InputClass::GenMat,
          InputClass::RowInds,
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
                _rowInds = *static_cast<Col<uword>*>(value.second);
                break;
              case 2:
                _fileSuffix += "," + value.first;
                _genColVec = *static_cast<Col<double>*>(value.second);
                break;
            }
            ++n;
          }

          cout << "Using input: " << _fileSuffix << endl;

          _copyOfGenMat = _genMat;
          _copyOfRowInds = _rowInds;
          _copyOfGenColVec = _genColVec;

          expectedRowsEqual();

          _genMat = _copyOfGenMat;
          _rowInds = _copyOfRowInds;
          _genColVec = _copyOfGenColVec;
          expectedRowsPlus();

          _genMat = _copyOfGenMat;
          _rowInds = _copyOfRowInds;
          _genColVec = _copyOfGenColVec;
          expectedRowsMinus();

          _genMat = _copyOfGenMat;
          _rowInds = _copyOfRowInds;
          _genColVec = _copyOfGenColVec;
          expectedRowsElemTimes();

          _genMat = _copyOfGenMat;
          _rowInds = _copyOfRowInds;
          _genColVec = _copyOfGenColVec;
          expectedRowsElemDivide();

          _genMat = _copyOfGenMat;
          _rowInds = _copyOfRowInds;
          _genColVec = _copyOfGenColVec;
          expectedEach_rowEqual();

          _genMat = _copyOfGenMat;
          _rowInds = _copyOfRowInds;
          _genColVec = _copyOfGenColVec;
          expectedEach_rowPlus();

          _genMat = _copyOfGenMat;
          _rowInds = _copyOfRowInds;
          _genColVec = _copyOfGenColVec;
          expectedEach_rowMinus();

          _genMat = _copyOfGenMat;
          _rowInds = _copyOfRowInds;
          _genColVec = _copyOfGenColVec;
          expectedEach_rowElemTimes();

          _genMat = _copyOfGenMat;
          _rowInds = _copyOfRowInds;
          _genColVec = _copyOfGenColVec;
          expectedEach_rowElemDivide();
        }

        cout << "done." << endl;
      }

    protected:
      Mat<double> _genMat;
      Mat<double> _copyOfGenMat;

      Col<uword> _rowInds;
      Col<uword> _copyOfRowInds;

      Col<double> _genColVec;
      Col<double> _copyOfGenColVec;

      void expectedRowsEqual() {
        for(int i = 0; i < _rowInds.n_elem; i++) {
          if(_rowInds.at(i) >= _genMat.n_rows) {
            return;
          }
        }

        if(_genColVec.n_rows != _rowInds.n_elem) {
          return;
        }

        if(_genMat.n_cols != 1) {
          return;
        }

        cout << "- Compute expectedRowsEqual() ... ";

        _genMat.rows(_rowInds) = _genColVec;
        save<double>("Mat.rowsEqual", _genMat);

        cout << "done." << endl;
      }

      void expectedRowsPlus() {
        for(int i = 0; i < _rowInds.n_elem; i++) {
          if(_rowInds.at(i) >= _genMat.n_rows) {
            return;
          }
        }

        if(_genColVec.n_rows != _rowInds.n_elem) {
          return;
        }

        if(_genMat.n_cols != 1) {
          return;
        }

        cout << "- Compute expectedRowsPlus() ... ";

        _genMat.rows(_rowInds) += _genColVec;
        save<double>("Mat.rowsPlus", _genMat);

        cout << "done." << endl;
      }

      void expectedRowsMinus() {
        for(int i = 0; i < _rowInds.n_elem; i++) {
          if(_rowInds.at(i) >= _genMat.n_rows) {
            return;
          }
        }

        if(_genColVec.n_rows != _rowInds.n_elem) {
          return;
        }

        if(_genMat.n_cols != 1) {
          return;
        }

        cout << "- Compute expectedRowsMinus() ... ";

        _genMat.rows(_rowInds) -= _genColVec;
        save<double>("Mat.rowsMinus", _genMat);

        cout << "done." << endl;
      }

      void expectedRowsElemTimes() {
        for(int i = 0; i < _rowInds.n_elem; i++) {
          if(_rowInds.at(i) >= _genMat.n_rows) {
            return;
          }
        }

        if(_genColVec.n_rows != _rowInds.n_elem) {
          return;
        }

        if(_genMat.n_cols != 1) {
          return;
        }

        cout << "- Compute expectedRowsElemTimes() ... ";

        _genMat.rows(_rowInds) %= _genColVec;
        save<double>("Mat.rowsElemTimes", _genMat);

        cout << "done." << endl;
      }

      void expectedRowsElemDivide() {
        for(int i = 0; i < _rowInds.n_elem; i++) {
          if(_rowInds.at(i) >= _genMat.n_rows) {
            return;
          }
        }

        if(_genColVec.n_rows != _rowInds.n_elem) {
          return;
        }

        if(_genMat.n_cols != 1) {
          return;
        }

        cout << "- Compute expectedRowsElemDivide() ... ";

        _genMat.rows(_rowInds) /= _genColVec;
        save<double>("Mat.rowsElemDivide", _genMat);

        cout << "done." << endl;
      }

      void expectedEach_rowEqual() {
        for(int i = 0; i < _rowInds.n_elem; i++) {
          if(_rowInds.at(i) >= _genMat.n_rows) {
            return;
          }
        }

        if(!_genColVec.is_rowvec()) {
          return;
        }

        if(_genMat.n_cols != 1) {
          return;
        }

        cout << "- Compute expectedEach_rowEqual() ... ";

        _genMat.each_row(_rowInds) = _genColVec;
        save<double>("Mat.each_rowEqual", _genMat);

        cout << "done." << endl;
      }

      void expectedEach_rowPlus() {
        for(int i = 0; i < _rowInds.n_elem; i++) {
          if(_rowInds.at(i) >= _genMat.n_rows) {
            return;
          }
        }

        if(!_genColVec.is_rowvec()) {
          return;
        }

        if(_genMat.n_cols != 1) {
          return;
        }

        cout << "- Compute expectedEach_rowPlus() ... ";

        _genMat.each_row(_rowInds) += _genColVec;
        save<double>("Mat.each_rowPlus", _genMat);

        cout << "done." << endl;
      }

      void expectedEach_rowMinus() {
        for(int i = 0; i < _rowInds.n_elem; i++) {
          if(_rowInds.at(i) >= _genMat.n_rows) {
            return;
          }
        }

        if(!_genColVec.is_rowvec()) {
          return;
        }

        if(_genMat.n_cols != 1) {
          return;
        }

        cout << "- Compute expectedEach_rowMinus() ... ";

        _genMat.each_row(_rowInds) -= _genColVec;
        save<double>("Mat.each_rowMinus", _genMat);

        cout << "done." << endl;
      }

      void expectedEach_rowElemTimes() {
        for(int i = 0; i < _rowInds.n_elem; i++) {
          if(_rowInds.at(i) >= _genMat.n_rows) {
            return;
          }
        }

        if(!_genColVec.is_rowvec()) {
          return;
        }

        if(_genMat.n_cols != 1) {
          return;
        }

        cout << "- Compute expectedEach_rowElemTimes() ... ";

        _genMat.each_row(_rowInds) %= _genColVec;
        save<double>("Mat.each_rowElemTimes", _genMat);

        cout << "done." << endl;
      }

      void expectedEach_rowElemDivide() {
        for(int i = 0; i < _rowInds.n_elem; i++) {
          if(_rowInds.at(i) >= _genMat.n_rows) {
            return;
          }
        }

        if(!_genColVec.is_rowvec()) {
          return;
        }

        if(_genMat.n_cols != 1) {
          return;
        }

        cout << "- Compute expectedEach_rowElemDivide() ... ";

        _genMat.each_row(_rowInds) /= _genColVec;
        save<double>("Mat.each_rowElemDivide", _genMat);

        cout << "done." << endl;
      }

  };
}
