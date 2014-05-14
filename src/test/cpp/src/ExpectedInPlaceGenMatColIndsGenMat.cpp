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
  class ExpectedInPlaceGenMatColIndsGenMat : public Expected {
    public:
      ExpectedInPlaceGenMatColIndsGenMat() {
        cout << "Compute ExpectedInPlaceGenMatColIndsGenMat(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
          InputClass::GenMat,
          InputClass::ColInds,
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
                _colInds = *static_cast<Col<uword>*>(value.second);
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
          _copyOfColInds = _colInds;
          _copyOfGenMatB = _genMatB;

          expectedColsEqual();

          _genMatA = _copyOfGenMatA;
          _colInds = _copyOfColInds;
          _genMatB = _copyOfGenMatB;
          expectedColsPlus();

          _genMatA = _copyOfGenMatA;
          _colInds = _copyOfColInds;
          _genMatB = _copyOfGenMatB;
          expectedColsMinus();

          _genMatA = _copyOfGenMatA;
          _colInds = _copyOfColInds;
          _genMatB = _copyOfGenMatB;
          expectedColsElemTimes();

          _genMatA = _copyOfGenMatA;
          _colInds = _copyOfColInds;
          _genMatB = _copyOfGenMatB;
          expectedColsElemDivide();

          _genMatA = _copyOfGenMatA;
          _colInds = _copyOfColInds;
          _genMatB = _copyOfGenMatB;
          expectedEach_colEqual();

          _genMatA = _copyOfGenMatA;
          _colInds = _copyOfColInds;
          _genMatB = _copyOfGenMatB;
          expectedEach_colPlus();

          _genMatA = _copyOfGenMatA;
          _colInds = _copyOfColInds;
          _genMatB = _copyOfGenMatB;
          expectedEach_colMinus();

          _genMatA = _copyOfGenMatA;
          _colInds = _copyOfColInds;
          _genMatB = _copyOfGenMatB;
          expectedEach_colElemTimes();

          _genMatA = _copyOfGenMatA;
          _colInds = _copyOfColInds;
          _genMatB = _copyOfGenMatB;
          expectedEach_colElemDivide();
        }

        cout << "done." << endl;
      }

    protected:
      Mat<double> _genMatA;
      Mat<double> _copyOfGenMatA;

      Col<uword> _colInds;
      Col<uword> _copyOfColInds;

      Mat<double> _genMatB;
      Mat<double> _copyOfGenMatB;

      void expectedColsEqual() {
        for(int j = 0; j < _colInds.n_elem; j++) {
          if(_colInds.at(j) >= _genMatA.n_cols) {
            return;
          }
        }

        if(_genMatB.n_rows != _genMatA.n_rows) {
          return;
        }

        if(_genMatB.n_cols != _colInds.n_elem) {
          return;
        }

        cout << "- Compute expectedColsEqual() ... ";

        _genMatA.cols(_colInds) = _genMatB;
        save<double>("Mat.colsEqual", _genMatA);

        cout << "done." << endl;
      }

      void expectedColsPlus() {
        for(int j = 0; j < _colInds.n_elem; j++) {
          if(_colInds.at(j) >= _genMatA.n_cols) {
            return;
          }
        }

        if(_genMatB.n_rows != _genMatA.n_rows) {
          return;
        }

        if(_genMatB.n_cols != _colInds.n_elem) {
          return;
        }

        cout << "- Compute expectedColsPlus() ... ";

        _genMatA.cols(_colInds) += _genMatB;
        save<double>("Mat.colsPlus", _genMatA);

        cout << "done." << endl;
      }

      void expectedColsMinus() {
        for(int j = 0; j < _colInds.n_elem; j++) {
          if(_colInds.at(j) >= _genMatA.n_cols) {
            return;
          }
        }

        if(_genMatB.n_rows != _genMatA.n_rows) {
          return;
        }

        if(_genMatB.n_cols != _colInds.n_elem) {
          return;
        }

        cout << "- Compute expectedColsMinus() ... ";

        _genMatA.cols(_colInds) -= _genMatB;
        save<double>("Mat.colsMinus", _genMatA);

        cout << "done." << endl;
      }

      void expectedColsElemTimes() {
        for(int j = 0; j < _colInds.n_elem; j++) {
          if(_colInds.at(j) >= _genMatA.n_cols) {
            return;
          }
        }

        if(_genMatB.n_rows != _genMatA.n_rows) {
          return;
        }

        if(_genMatB.n_cols != _colInds.n_elem) {
          return;
        }

        cout << "- Compute expectedColsElemTimes() ... ";

        _genMatA.cols(_colInds) %= _genMatB;
        save<double>("Mat.colsElemTimes", _genMatA);

        cout << "done." << endl;
      }

      void expectedColsElemDivide() {
        for(int j = 0; j < _colInds.n_elem; j++) {
          if(_colInds.at(j) >= _genMatA.n_cols) {
            return;
          }
        }

        if(_genMatB.n_rows != _genMatA.n_rows) {
          return;
        }

        if(_genMatB.n_cols != _colInds.n_elem) {
          return;
        }

        cout << "- Compute expectedColsElemDivide() ... ";

        _genMatA.cols(_colInds) /= _genMatB;
        save<double>("Mat.colsElemDivide", _genMatA);

        cout << "done." << endl;
      }

      void expectedEach_colEqual() {
        for(int j = 0; j < _colInds.n_elem; j++) {
          if(_colInds.at(j) >= _genMatA.n_cols) {
            return;
          }
        }

        if(!_genMatB.is_colvec()) {
          return;
        }

        if(_genMatB.n_rows != _genMatA.n_rows) {
          return;
        }

        cout << "- Compute expectedEach_colEqual() ... ";

        _genMatA.each_col(_colInds) = _genMatB;
        save<double>("Mat.each_colEqual", _genMatA);

        cout << "done." << endl;
      }

      void expectedEach_colPlus() {
        for(int j = 0; j < _colInds.n_elem; j++) {
          if(_colInds.at(j) >= _genMatA.n_cols) {
            return;
          }
        }

        if(!_genMatB.is_colvec()) {
          return;
        }

        if(_genMatB.n_rows != _genMatA.n_rows) {
          return;
        }

        cout << "- Compute expectedEach_colPlus() ... ";

        _genMatA.each_col(_colInds) += _genMatB;
        save<double>("Mat.each_colPlus", _genMatA);

        cout << "done." << endl;
      }

      void expectedEach_colMinus() {
        for(int j = 0; j < _colInds.n_elem; j++) {
          if(_colInds.at(j) >= _genMatA.n_cols) {
            return;
          }
        }

        if(!_genMatB.is_colvec()) {
          return;
        }

        if(_genMatB.n_rows != _genMatA.n_rows) {
          return;
        }

        cout << "- Compute expectedEach_colMinus() ... ";

        _genMatA.each_col(_colInds) -= _genMatB;
        save<double>("Mat.each_colMinus", _genMatA);

        cout << "done." << endl;
      }

      void expectedEach_colElemTimes() {
        for(int j = 0; j < _colInds.n_elem; j++) {
          if(_colInds.at(j) >= _genMatA.n_cols) {
            return;
          }
        }

        if(!_genMatB.is_colvec()) {
          return;
        }

        if(_genMatB.n_rows != _genMatA.n_rows) {
          return;
        }

        cout << "- Compute expectedEach_colElemTimes() ... ";

        _genMatA.each_col(_colInds) %= _genMatB;
        save<double>("Mat.each_colElemTimes", _genMatA);

        cout << "done." << endl;
      }

      void expectedEach_colElemDivide() {
        for(int j = 0; j < _colInds.n_elem; j++) {
          if(_colInds.at(j) >= _genMatA.n_cols) {
            return;
          }
        }

        if(!_genMatB.is_colvec()) {
          return;
        }

        if(_genMatB.n_rows != _genMatA.n_rows) {
          return;
        }

        cout << "- Compute expectedEach_colElemDivide() ... ";

        _genMatA.each_col(_colInds) /= _genMatB;
        save<double>("Mat.each_colElemDivide", _genMatA);

        cout << "done." << endl;
      }

  };
}
