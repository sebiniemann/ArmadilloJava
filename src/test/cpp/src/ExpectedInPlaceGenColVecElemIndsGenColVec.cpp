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
  class ExpectedInPlaceGenColVecElemIndsGenColVec : public Expected {
    public:
      ExpectedInPlaceGenColVecElemIndsGenColVec() {
        cout << "Compute ExpectedInPlaceGenColVecElemIndsGenColVec(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
          InputClass::GenColVec,
          InputClass::ElemInds,
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
                _elemInds = *static_cast<Col<uword>*>(value.second);
                break;
              case 2:
                _fileSuffix += "," + value.first;
                _genColVecB = *static_cast<Col<double>*>(value.second);
                break;
            }
            ++n;
          }

          cout << "Using input: " << _fileSuffix << endl;

          _copyOfGenColVecA = _genColVecA;
          _copyOfElemInds = _elemInds;
          _copyOfGenColVecB = _genColVecB;

          expectedElemEqual();

          _genColVecA = _copyOfGenColVecA;
          _elemInds = _copyOfElemInds;
          _genColVecB = _copyOfGenColVecB;
          expectedElemPlus();

          _genColVecA = _copyOfGenColVecA;
          _elemInds = _copyOfElemInds;
          _genColVecB = _copyOfGenColVecB;
          expectedElemMinus();

          _genColVecA = _copyOfGenColVecA;
          _elemInds = _copyOfElemInds;
          _genColVecB = _copyOfGenColVecB;
          expectedElemElemTimes();

          _genColVecA = _copyOfGenColVecA;
          _elemInds = _copyOfElemInds;
          _genColVecB = _copyOfGenColVecB;
          expectedElemElemDivide();

          _genColVecA = _copyOfGenColVecA;
          _elemInds = _copyOfElemInds;
          _genColVecB = _copyOfGenColVecB;
          expectedRowsEqual();

          _genColVecA = _copyOfGenColVecA;
          _elemInds = _copyOfElemInds;
          _genColVecB = _copyOfGenColVecB;
          expectedRowsPlus();

          _genColVecA = _copyOfGenColVecA;
          _elemInds = _copyOfElemInds;
          _genColVecB = _copyOfGenColVecB;
          expectedRowsMinus();

          _genColVecA = _copyOfGenColVecA;
          _elemInds = _copyOfElemInds;
          _genColVecB = _copyOfGenColVecB;
          expectedRowsElemTimes();

          _genColVecA = _copyOfGenColVecA;
          _elemInds = _copyOfElemInds;
          _genColVecB = _copyOfGenColVecB;
          expectedRowsElemDivide();
        }

        cout << "done." << endl;
      }

    protected:
      Col<double> _genColVecA;
      Col<double> _copyOfGenColVecA;

      Col<uword> _elemInds;
      Col<uword> _copyOfElemInds;

      Col<double> _genColVecB;
      Col<double> _copyOfGenColVecB;

      void expectedElemEqual() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genColVecA.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(_genColVecB.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemEqual() ... ";

        _genColVecA.elem(_elemInds) = _genColVecB;
        save<double>("Col.elemEqual", _genColVecA);

        cout << "done." << endl;
      }

      void expectedElemPlus() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genColVecA.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(_genColVecB.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemPlus() ... ";

        _genColVecA.elem(_elemInds) += _genColVecB;
        save<double>("Col.elemPlus", _genColVecA);

        cout << "done." << endl;
      }

      void expectedElemMinus() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genColVecA.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(_genColVecB.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemMinus() ... ";

        _genColVecA.elem(_elemInds) -= _genColVecB;
        save<double>("Col.elemMinus", _genColVecA);

        cout << "done." << endl;
      }

      void expectedElemElemTimes() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genColVecA.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(_genColVecB.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemElemTimes() ... ";

        _genColVecA.elem(_elemInds) %= _genColVecB;
        save<double>("Col.elemElemTimes", _genColVecA);

        cout << "done." << endl;
      }

      void expectedElemElemDivide() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genColVecA.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(_genColVecB.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemElemDivide() ... ";

        _genColVecA.elem(_elemInds) /= _genColVecB;
        save<double>("Col.elemElemDivide", _genColVecA);

        cout << "done." << endl;
      }

      void expectedRowsEqual() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genColVecA.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(_genColVecB.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemEqual() ... ";

        _genColVecA.rows(_elemInds) = _genColVecB;
        save<double>("Col.rowsEqual", _genColVecA);

        cout << "done." << endl;
      }

      void expectedRowsPlus() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genColVecA.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(_genColVecB.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemPlus() ... ";

        _genColVecA.rows(_elemInds) += _genColVecB;
        save<double>("Col.rowsPlus", _genColVecA);

        cout << "done." << endl;
      }

      void expectedRowsMinus() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genColVecA.in_range(_elemInds.at(n))) {
            return;
          }
        }


        if(_genColVecB.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemMinus() ... ";

        _genColVecA.rows(_elemInds) -= _genColVecB;
        save<double>("Col.rowsMinus", _genColVecA);

        cout << "done." << endl;
      }

      void expectedRowsElemTimes() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genColVecA.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(_genColVecB.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemElemTimes() ... ";

        _genColVecA.rows(_elemInds) %= _genColVecB;
        save<double>("Col.rowsElemTimes", _genColVecA);

        cout << "done." << endl;
      }

      void expectedRowsElemDivide() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genColVecA.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(_genColVecB.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemElemDivide() ... ";

        _genColVecA.rows(_elemInds) /= _genColVecB;
        save<double>("Col.rowsElemDivide", _genColVecA);

        cout << "done." << endl;
      }

  };
}
