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
using arma::Col;
using arma::uword;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedInPlaceGenMatRowIndsGenMat : public Expected {
    public:
      ExpectedInPlaceGenMatRowIndsGenMat() {
        cout << "Compute ExpectedInPlaceGenMatRowIndsGenMat(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
          InputClass::GenMat,
          InputClass::RowInds,
          InputClass::GenMat
        });

        for (vector<pair<string, void*>> input : inputs) {
          _fileSuffix = "";

          int n = 0;
          for (pair<string, void*> value : input) {
            switch (n) {
              case 0:
                _fileSuffix += value.first;
                _genMatA = *static_cast<Mat<double>*>(value.second);
                break;
              case 1:
                _fileSuffix += "," + value.first;
                _rowInds = *static_cast<Col<uword>*>(value.second);
                break;
              case 2:
                _fileSuffix += "," + value.first;
                _genMatB = *static_cast<Mat<double>*>(value.second);
                break;
            }
            ++n;
          }

          cout << "Using input: " << _fileSuffix << endl;

          _copyOfGenMatA = _genMatA;
          _copyOfRowInds = _rowInds;
          _copyOfGenMatB = _genMatB;

          expectedRowsEqual();

          _genMatA = _copyOfGenMatA;
          _rowInds = _copyOfRowInds;
          _genMatB = _copyOfGenMatB;
          expectedRowsPlus();

          _genMatA = _copyOfGenMatA;
          _rowInds = _copyOfRowInds;
          _genMatB = _copyOfGenMatB;
          expectedRowsMinus();

          _genMatA = _copyOfGenMatA;
          _rowInds = _copyOfRowInds;
          _genMatB = _copyOfGenMatB;
          expectedRowsElemTimes();

          _genMatA = _copyOfGenMatA;
          _rowInds = _copyOfRowInds;
          _genMatB = _copyOfGenMatB;
          expectedRowsElemDivide();

          _genMatA = _copyOfGenMatA;
          _rowInds = _copyOfRowInds;
          _genMatB = _copyOfGenMatB;
          expectedEach_rowEqual();

          _genMatA = _copyOfGenMatA;
          _rowInds = _copyOfRowInds;
          _genMatB = _copyOfGenMatB;
          expectedEach_rowPlus();

          _genMatA = _copyOfGenMatA;
          _rowInds = _copyOfRowInds;
          _genMatB = _copyOfGenMatB;
          expectedEach_rowMinus();

          _genMatA = _copyOfGenMatA;
          _rowInds = _copyOfRowInds;
          _genMatB = _copyOfGenMatB;
          expectedEach_rowElemTimes();

          _genMatA = _copyOfGenMatA;
          _rowInds = _copyOfRowInds;
          _genMatB = _copyOfGenMatB;
          expectedEach_rowElemDivide();
        }

        cout << "done." << endl;
      }

    protected:
      Mat<double> _genMatA;
      Mat<double> _copyOfGenMatA;

      Col<uword> _rowInds;
      Col<uword> _copyOfRowInds;

      Mat<double> _genMatB;
      Mat<double> _copyOfGenMatB;

      void expectedRowsEqual() {
        for(int i = 0; i < _rowInds.n_elem; i++) {
          if(_rowInds.at(i) >= _genMatA.n_rows) {
            return;
          }
        }

        if(_genMatB.n_rows != _rowInds.n_elem) {
          return;
        }

        if(_genMatB.n_cols != _genMatA.n_cols) {
          return;
        }

        cout << "- Compute expectedRowsEqual() ... ";

        _genMatA.rows(_rowInds) = _genMatB;
        save<double>("Mat.rowsEqual", _genMatA);

        cout << "done." << endl;
      }

      void expectedRowsPlus() {
        for(int i = 0; i < _rowInds.n_elem; i++) {
          if(_rowInds.at(i) >= _genMatA.n_rows) {
            return;
          }
        }

        if(_genMatB.n_rows != _rowInds.n_elem) {
          return;
        }

        if(_genMatB.n_cols != _genMatA.n_cols) {
          return;
        }

        cout << "- Compute expectedRowsPlus() ... ";

        _genMatA.rows(_rowInds) += _genMatB;
        save<double>("Mat.rowsPlus", _genMatA);

        cout << "done." << endl;
      }

      void expectedRowsMinus() {
        for(int i = 0; i < _rowInds.n_elem; i++) {
          if(_rowInds.at(i) >= _genMatA.n_rows) {
            return;
          }
        }

        if(_genMatB.n_rows != _rowInds.n_elem) {
          return;
        }

        if(_genMatB.n_cols != _genMatA.n_cols) {
          return;
        }

        cout << "- Compute expectedRowsMinus() ... ";

        _genMatA.rows(_rowInds) -= _genMatB;
        save<double>("Mat.rowsMinus", _genMatA);

        cout << "done." << endl;
      }

      void expectedRowsElemTimes() {
        for(int i = 0; i < _rowInds.n_elem; i++) {
          if(_rowInds.at(i) >= _genMatA.n_rows) {
            return;
          }
        }

        if(_genMatB.n_rows != _rowInds.n_elem) {
          return;
        }

        if(_genMatB.n_cols != _genMatA.n_cols) {
          return;
        }

        cout << "- Compute expectedRowsElemTimes() ... ";

        _genMatA.rows(_rowInds) %= _genMatB;
        save<double>("Mat.rowsElemTimes", _genMatA);

        cout << "done." << endl;
      }

      void expectedRowsElemDivide() {
        for(int i = 0; i < _rowInds.n_elem; i++) {
          if(_rowInds.at(i) >= _genMatA.n_rows) {
            return;
          }
        }

        if(_genMatB.n_rows != _rowInds.n_elem) {
          return;
        }

        if(_genMatB.n_cols != _genMatA.n_cols) {
          return;
        }

        cout << "- Compute expectedRowsElemDivide() ... ";

        _genMatA.rows(_rowInds) /= _genMatB;
        save<double>("Mat.rowsElemDivide", _genMatA);

        cout << "done." << endl;
      }

      void expectedEach_rowEqual() {
        for(int i = 0; i < _rowInds.n_elem; i++) {
          if(_rowInds.at(i) >= _genMatA.n_rows) {
            return;
          }
        }

        if(!_genMatB.is_rowvec()) {
          return;
        }

        if(_genMatB.n_cols != _genMatA.n_cols) {
          return;
        }

        cout << "- Compute expectedEach_rowEqual() ... ";

        _genMatA.each_row(_rowInds) = _genMatB;
        save<double>("Mat.each_rowEqual", _genMatA);

        cout << "done." << endl;
      }

      void expectedEach_rowPlus() {
        for(int i = 0; i < _rowInds.n_elem; i++) {
          if(_rowInds.at(i) >= _genMatA.n_rows) {
            return;
          }
        }

        if(!_genMatB.is_rowvec()) {
          return;
        }

        if(_genMatB.n_cols != _genMatA.n_cols) {
          return;
        }

        cout << "- Compute expectedEach_rowPlus() ... ";

        _genMatA.each_row(_rowInds) += _genMatB;
        save<double>("Mat.each_rowPlus", _genMatA);

        cout << "done." << endl;
      }

      void expectedEach_rowMinus() {
        for(int i = 0; i < _rowInds.n_elem; i++) {
          if(_rowInds.at(i) >= _genMatA.n_rows) {
            return;
          }
        }

        if(!_genMatB.is_rowvec()) {
          return;
        }

        if(_genMatB.n_cols != _genMatA.n_cols) {
          return;
        }

        cout << "- Compute expectedEach_rowMinus() ... ";

        _genMatA.each_row(_rowInds) -= _genMatB;
        save<double>("Mat.each_rowMinus", _genMatA);

        cout << "done." << endl;
      }

      void expectedEach_rowElemTimes() {
        for(int i = 0; i < _rowInds.n_elem; i++) {
          if(_rowInds.at(i) >= _genMatA.n_rows) {
            return;
          }
        }

        if(!_genMatB.is_rowvec()) {
          return;
        }

        if(_genMatB.n_cols != _genMatA.n_cols) {
          return;
        }

        cout << "- Compute expectedEach_rowElemTimes() ... ";

        _genMatA.each_row(_rowInds) %= _genMatB;
        save<double>("Mat.each_rowElemTimes", _genMatA);

        cout << "done." << endl;
      }

      void expectedEach_rowElemDivide() {
        for(int i = 0; i < _rowInds.n_elem; i++) {
          if(_rowInds.at(i) >= _genMatA.n_rows) {
            return;
          }
        }

        if(!_genMatB.is_rowvec()) {
          return;
        }

        if(_genMatB.n_cols != _genMatA.n_cols) {
          return;
        }

        cout << "- Compute expectedEach_rowElemDivide() ... ";

        _genMatA.each_row(_rowInds) /= _genMatB;
        save<double>("Mat.each_rowElemDivide", _genMatA);

        cout << "done." << endl;
      }

  };
}
