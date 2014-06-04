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
using arma::Row;
using arma::Col;
using arma::uword;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedInPlaceGenColVecElemIndsGenRowVec : public Expected {
    public:
      ExpectedInPlaceGenColVecElemIndsGenRowVec() {
        cout << "Compute ExpectedInPlaceGenColVecElemIndsGenRowVec(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
          InputClass::GenColVec,
          InputClass::ElemInds,
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
                _elemInds = *static_cast<Col<uword>*>(value.second);
                break;
              case 2:
                _fileSuffix += "," + value.first;
                _genRowVec = *static_cast<Row<double>*>(value.second);
                break;
            }
            ++n;
          }

          cout << "Using input: " << _fileSuffix << endl;

          _copyOfGenColVec = _genColVec;
          _copyOfElemInds = _elemInds;
          _copyOfGenRowVec = _genRowVec;

          expectedElemEqual();

          _genColVec = _copyOfGenColVec;
          _elemInds = _copyOfElemInds;
          _genRowVec = _copyOfGenRowVec;
          expectedElemPlus();

          _genColVec = _copyOfGenColVec;
          _elemInds = _copyOfElemInds;
          _genRowVec = _copyOfGenRowVec;
          expectedElemMinus();

          _genColVec = _copyOfGenColVec;
          _elemInds = _copyOfElemInds;
          _genRowVec = _copyOfGenRowVec;
          expectedElemElemTimes();

          _genColVec = _copyOfGenColVec;
          _elemInds = _copyOfElemInds;
          _genRowVec = _copyOfGenRowVec;
          expectedElemElemDivide();

          _genColVec = _copyOfGenColVec;
          _elemInds = _copyOfElemInds;
          _genRowVec = _copyOfGenRowVec;
          expectedRowsEqual();

          _genColVec = _copyOfGenColVec;
          _elemInds = _copyOfElemInds;
          _genRowVec = _copyOfGenRowVec;
          expectedRowsPlus();

          _genColVec = _copyOfGenColVec;
          _elemInds = _copyOfElemInds;
          _genRowVec = _copyOfGenRowVec;
          expectedRowsMinus();

          _genColVec = _copyOfGenColVec;
          _elemInds = _copyOfElemInds;
          _genRowVec = _copyOfGenRowVec;
          expectedRowsElemTimes();

          _genColVec = _copyOfGenColVec;
          _elemInds = _copyOfElemInds;
          _genRowVec = _copyOfGenRowVec;
          expectedRowsElemDivide();
        }

        cout << "done." << endl;
      }

    protected:
      Col<double> _genColVec;
      Col<double> _copyOfGenColVec;

      Col<uword> _elemInds;
      Col<uword> _copyOfElemInds;

      Row<double> _genRowVec;
      Row<double> _copyOfGenRowVec;

      void expectedElemEqual() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genColVec.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(_genRowVec.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemEqual() ... ";

        _genColVec.elem(_elemInds) = _genRowVec;
        save<double>("Col.elemEqual", _genColVec);

        cout << "done." << endl;
      }

      void expectedElemPlus() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genColVec.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(_genRowVec.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemPlus() ... ";

        _genColVec.elem(_elemInds) += _genRowVec;
        save<double>("Col.elemPlus", _genColVec);

        cout << "done." << endl;
      }

      void expectedElemMinus() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genColVec.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(_genRowVec.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemMinus() ... ";

        _genColVec.elem(_elemInds) -= _genRowVec;
        save<double>("Col.elemMinus", _genColVec);

        cout << "done." << endl;
      }

      void expectedElemElemTimes() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genColVec.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(_genRowVec.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemElemTimes() ... ";

        _genColVec.elem(_elemInds) %= _genRowVec;
        save<double>("Col.elemElemTimes", _genColVec);

        cout << "done." << endl;
      }

      void expectedElemElemDivide() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genColVec.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(_genRowVec.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemElemDivide() ... ";

        _genColVec.elem(_elemInds) /= _genRowVec;
        save<double>("Col.elemElemDivide", _genColVec);

        cout << "done." << endl;
      }

      void expectedRowsEqual() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genColVec.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(_genRowVec.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemEqual() ... ";

        _genColVec.rows(_elemInds) = _genRowVec;
        save<double>("Col.rowsEqual", _genColVec);

        cout << "done." << endl;
      }

      void expectedRowsPlus() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genColVec.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(_genRowVec.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemPlus() ... ";

        _genColVec.rows(_elemInds) += _genRowVec;
        save<double>("Col.rowsPlus", _genColVec);

        cout << "done." << endl;
      }

      void expectedRowsMinus() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genColVec.in_range(_elemInds.at(n))) {
            return;
          }
        }


        if(_genRowVec.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemMinus() ... ";

        _genColVec.rows(_elemInds) -= _genRowVec;
        save<double>("Col.rowsMinus", _genColVec);

        cout << "done." << endl;
      }

      void expectedRowsElemTimes() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genColVec.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(_genRowVec.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemElemTimes() ... ";

        _genColVec.rows(_elemInds) %= _genRowVec;
        save<double>("Col.rowsElemTimes", _genColVec);

        cout << "done." << endl;
      }

      void expectedRowsElemDivide() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genColVec.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(_genRowVec.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemElemDivide() ... ";

        _genColVec.rows(_elemInds) /= _genRowVec;
        save<double>("Col.rowsElemDivide", _genColVec);

        cout << "done." << endl;
      }

  };
}
