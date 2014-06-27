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
  class ExpectedInPlaceGenRowVecElemIndsGenMat : public Expected {
    public:
      ExpectedInPlaceGenRowVecElemIndsGenMat() {
        cout << "Compute ExpectedInPlaceGenRowVecElemIndsGenMat(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
          InputClass::GenRowVec,
          InputClass::ElemInds,
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
                _elemInds = *static_cast<Col<uword>*>(value.second);
                break;
              case 2:
                _fileSuffix += "," + value.first;
                _genMat = *static_cast<Mat<double>*>(value.second);
                break;
            }
            ++n;
          }

          cout << "Using input: " << _fileSuffix << endl;

          _copyOfGenRowVec = _genRowVec;
          _copyOfElemInds = _elemInds;
          _copyOfGenMat = _genMat;

          expectedElemEqual();

          _genRowVec = _copyOfGenRowVec;
          _elemInds = _copyOfElemInds;
          _genMat = _copyOfGenMat;
          expectedElemPlus();

          _genRowVec = _copyOfGenRowVec;
          _elemInds = _copyOfElemInds;
          _genMat = _copyOfGenMat;
          expectedElemMinus();

          _genRowVec = _copyOfGenRowVec;
          _elemInds = _copyOfElemInds;
          _genMat = _copyOfGenMat;
          expectedElemElemTimes();

          _genRowVec = _copyOfGenRowVec;
          _elemInds = _copyOfElemInds;
          _genMat = _copyOfGenMat;
          expectedElemElemDivide();

          _genRowVec = _copyOfGenRowVec;
          _elemInds = _copyOfElemInds;
          _genMat = _copyOfGenMat;
          expectedColsEqual();

          _genRowVec = _copyOfGenRowVec;
          _elemInds = _copyOfElemInds;
          _genMat = _copyOfGenMat;
          expectedColsPlus();

          _genRowVec = _copyOfGenRowVec;
          _elemInds = _copyOfElemInds;
          _genMat = _copyOfGenMat;
          expectedColsMinus();

          _genRowVec = _copyOfGenRowVec;
          _elemInds = _copyOfElemInds;
          _genMat = _copyOfGenMat;
          expectedColsElemTimes();

          _genRowVec = _copyOfGenRowVec;
          _elemInds = _copyOfElemInds;
          _genMat = _copyOfGenMat;
          expectedColsElemDivide();
        }

        cout << "done." << endl;
      }

    protected:
      Row<double> _genRowVec;
      Row<double> _copyOfGenRowVec;

      Col<uword> _elemInds;
      Col<uword> _copyOfElemInds;

      Mat<double> _genMat;
      Mat<double> _copyOfGenMat;

      void expectedElemEqual() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genRowVec.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(!_genMat.is_rowvec()) {
          return;
        }

        if(_genMat.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemEqual() ... ";

        _genRowVec.elem(_elemInds) = _genMat;
        save<double>("Row.elemEqual", _genRowVec);

        cout << "done." << endl;
      }

      void expectedElemPlus() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genRowVec.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(!_genMat.is_rowvec()) {
          return;
        }

        if(_genMat.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemPlus() ... ";

        _genRowVec.elem(_elemInds) += _genMat;
        save<double>("Row.elemPlus", _genRowVec);

        cout << "done." << endl;
      }

      void expectedElemMinus() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genRowVec.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(!_genMat.is_rowvec()) {
          return;
        }

        if(_genMat.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemMinus() ... ";

        _genRowVec.elem(_elemInds) -= _genMat;
        save<double>("Row.elemMinus", _genRowVec);

        cout << "done." << endl;
      }

      void expectedElemElemTimes() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genRowVec.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(!_genMat.is_rowvec()) {
          return;
        }

        if(_genMat.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemElemTimes() ... ";

        _genRowVec.elem(_elemInds) %= _genMat;
        save<double>("Row.elemElemTimes", _genRowVec);

        cout << "done." << endl;
      }

      void expectedElemElemDivide() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genRowVec.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(!_genMat.is_rowvec()) {
          return;
        }

        if(_genMat.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemElemDivide() ... ";

        _genRowVec.elem(_elemInds) /= _genMat;
        save<double>("Row.elemElemDivide", _genRowVec);

        cout << "done." << endl;
      }

      void expectedColsEqual() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genRowVec.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(!_genMat.is_rowvec()) {
          return;
        }

        if(_genMat.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemEqual() ... ";

        _genRowVec.cols(_elemInds) = _genMat;
        save<double>("Row.colsEqual", _genRowVec);

        cout << "done." << endl;
      }

      void expectedColsPlus() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genRowVec.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(!_genMat.is_rowvec()) {
          return;
        }

        if(_genMat.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemPlus() ... ";

        _genRowVec.cols(_elemInds) += _genMat;
        save<double>("Row.colsPlus", _genRowVec);

        cout << "done." << endl;
      }

      void expectedColsMinus() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genRowVec.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(!_genMat.is_rowvec()) {
          return;
        }

        if(_genMat.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemMinus() ... ";

        _genRowVec.cols(_elemInds) -= _genMat;
        save<double>("Row.colsMinus", _genRowVec);

        cout << "done." << endl;
      }

      void expectedColsElemTimes() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genRowVec.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(!_genMat.is_rowvec()) {
          return;
        }

        if(_genMat.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemElemTimes() ... ";

        _genRowVec.cols(_elemInds) %= _genMat;
        save<double>("Row.colsElemTimes", _genRowVec);

        cout << "done." << endl;
      }

      void expectedColsElemDivide() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genRowVec.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(!_genMat.is_rowvec()) {
          return;
        }

        if(_genMat.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemElemDivide() ... ";

        _genRowVec.cols(_elemInds) /= _genMat;
        save<double>("Row.colsElemDivide", _genRowVec);

        cout << "done." << endl;
      }

  };
}
