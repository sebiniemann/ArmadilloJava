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
  class ExpectedInPlaceGenMatColIndsGenColVec : public Expected {
    public:
      ExpectedInPlaceGenMatColIndsGenColVec() {
        cout << "Compute ExpectedInPlaceGenMatColIndsGenColVec(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
          InputClass::GenMat,
          InputClass::ColInds,
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
                _colInds = *static_cast<Col<uword>*>(value.second);
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
          _copyOfColInds = _colInds;
          _copyOfGenColVec = _genColVec;

          expectedColsEqual();

          _genMat = _copyOfGenMat;
          _colInds = _copyOfColInds;
          _genColVec = _copyOfGenColVec;
          expectedColsPlus();

          _genMat = _copyOfGenMat;
          _colInds = _copyOfColInds;
          _genColVec = _copyOfGenColVec;
          expectedColsMinus();

          _genMat = _copyOfGenMat;
          _colInds = _copyOfColInds;
          _genColVec = _copyOfGenColVec;
          expectedColsElemTimes();

          _genMat = _copyOfGenMat;
          _colInds = _copyOfColInds;
          _genColVec = _copyOfGenColVec;
          expectedColsElemDivide();


          _genMat = _copyOfGenMat;
          _colInds = _copyOfColInds;
          _genColVec = _copyOfGenColVec;
          expectedEach_colEqual();

          _genMat = _copyOfGenMat;
          _colInds = _copyOfColInds;
          _genColVec = _copyOfGenColVec;
          expectedEach_colPlus();

          _genMat = _copyOfGenMat;
          _colInds = _copyOfColInds;
          _genColVec = _copyOfGenColVec;
          expectedEach_colMinus();

          _genMat = _copyOfGenMat;
          _colInds = _copyOfColInds;
          _genColVec = _copyOfGenColVec;
          expectedEach_colElemTimes();

          _genMat = _copyOfGenMat;
          _colInds = _copyOfColInds;
          _genColVec = _copyOfGenColVec;
          expectedEach_colElemDivide();
        }

        cout << "done." << endl;
      }

    protected:
      Mat<double> _genMat;
      Mat<double> _copyOfGenMat;

      Col<uword> _colInds;
      Col<uword> _copyOfColInds;

      Col<double> _genColVec;
      Col<double> _copyOfGenColVec;

      void expectedColsEqual() {
        for(int j = 0; j < _colInds.n_elem; j++) {
          if(_colInds.at(j) >= _genMat.n_cols) {
            return;
          }
        }

        if(_genColVec.n_rows != _genMat.n_rows) {
          return;
        }

        if(_colInds.n_elem != 1) {
          return;
        }

        cout << "- Compute expectedColsEqual() ... ";

        _genMat.cols(_colInds) = _genColVec;
        save<double>("Mat.colsEqual", _genMat);

        cout << "done." << endl;
      }

      void expectedColsPlus() {
        for(int j = 0; j < _colInds.n_elem; j++) {
          if(_colInds.at(j) >= _genMat.n_cols) {
            return;
          }
        }

        if(_genColVec.n_rows != _genMat.n_rows) {
          return;
        }

        if(_colInds.n_elem != 1) {
          return;
        }

        cout << "- Compute expectedColsPlus() ... ";

        _genMat.cols(_colInds) += _genColVec;
        save<double>("Mat.colsPlus", _genMat);

        cout << "done." << endl;
      }

      void expectedColsMinus() {
        for(int j = 0; j < _colInds.n_elem; j++) {
          if(_colInds.at(j) >= _genMat.n_cols) {
            return;
          }
        }

        if(_genColVec.n_rows != _genMat.n_rows) {
          return;
        }

        if(_colInds.n_elem != 1) {
          return;
        }

        cout << "- Compute expectedColsMinus() ... ";

        _genMat.cols(_colInds) -= _genColVec;
        save<double>("Mat.colsMinus", _genMat);

        cout << "done." << endl;
      }

      void expectedColsElemTimes() {
        for(int j = 0; j < _colInds.n_elem; j++) {
          if(_colInds.at(j) >= _genMat.n_cols) {
            return;
          }
        }

        if(_genColVec.n_rows != _genMat.n_rows) {
          return;
        }

        if(_colInds.n_elem != 1) {
          return;
        }

        cout << "- Compute expectedColsElemTimes() ... ";

        _genMat.cols(_colInds) %= _genColVec;
        save<double>("Mat.colsElemTimes", _genMat);

        cout << "done." << endl;
      }

      void expectedColsElemDivide() {
        for(int j = 0; j < _colInds.n_elem; j++) {
          if(_colInds.at(j) >= _genMat.n_cols) {
            return;
          }
        }

        if(_genColVec.n_rows != _genMat.n_rows) {
          return;
        }

        if(_colInds.n_elem != 1) {
          return;
        }

        cout << "- Compute expectedColsElemDivide() ... ";

        _genMat.cols(_colInds) /= _genColVec;
        save<double>("Mat.colsElemDivide", _genMat);

        cout << "done." << endl;
      }

      void expectedEach_colEqual() {
        for(int j = 0; j < _colInds.n_elem; j++) {
          if(_colInds.at(j) >= _genMat.n_cols) {
            return;
          }
        }

        if(_genColVec.n_rows != _genMat.n_rows) {
          return;
        }

        cout << "- Compute expectedEach_colEqual() ... ";

        _genMat.each_col(_colInds) = _genColVec;
        save<double>("Mat.each_colEqual", _genMat);

        cout << "done." << endl;
      }

      void expectedEach_colPlus() {
        for(int j = 0; j < _colInds.n_elem; j++) {
          if(_colInds.at(j) >= _genMat.n_cols) {
            return;
          }
        }

        if(_genColVec.n_rows != _genMat.n_rows) {
          return;
        }

        cout << "- Compute expectedEach_colPlus() ... ";

        _genMat.each_col(_colInds) += _genColVec;
        save<double>("Mat.each_colPlus", _genMat);

        cout << "done." << endl;
      }

      void expectedEach_colMinus() {
        for(int j = 0; j < _colInds.n_elem; j++) {
          if(_colInds.at(j) >= _genMat.n_cols) {
            return;
          }
        }

        if(_genColVec.n_rows != _genMat.n_rows) {
          return;
        }

        cout << "- Compute expectedEach_colMinus() ... ";

        _genMat.each_col(_colInds) -= _genColVec;
        save<double>("Mat.each_colMinus", _genMat);

        cout << "done." << endl;
      }

      void expectedEach_colElemTimes() {
        for(int j = 0; j < _colInds.n_elem; j++) {
          if(_colInds.at(j) >= _genMat.n_cols) {
            return;
          }
        }

        if(_genColVec.n_rows != _genMat.n_rows) {
          return;
        }

        cout << "- Compute expectedEach_colElemTimes() ... ";

        _genMat.each_col(_colInds) %= _genColVec;
        save<double>("Mat.each_colElemTimes", _genMat);

        cout << "done." << endl;
      }

      void expectedEach_colElemDivide() {
        for(int j = 0; j < _colInds.n_elem; j++) {
          if(_colInds.at(j) >= _genMat.n_cols) {
            return;
          }
        }

        if(_genColVec.n_rows != _genMat.n_rows) {
          return;
        }

        cout << "- Compute expectedEach_colElemDivide() ... ";

        _genMat.each_col(_colInds) /= _genColVec;
        save<double>("Mat.each_colElemDivide", _genMat);

        cout << "done." << endl;
      }

  };
}
