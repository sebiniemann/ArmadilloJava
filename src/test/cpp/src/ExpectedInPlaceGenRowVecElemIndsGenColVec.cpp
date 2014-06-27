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
using arma::Col;
using arma::Row;
using arma::uword;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedInPlaceGenRowVecElemIndsGenColVec : public Expected {
    public:
      ExpectedInPlaceGenRowVecElemIndsGenColVec() {
        cout << "Compute ExpectedInPlaceGenRowVecElemIndsGenColVec(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
          InputClass::GenRowVec,
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
                _genRowVec = *static_cast<Row<double>*>(value.second);
                break;
              case 1:
                _fileSuffix += "," + value.first;
                _elemInds = *static_cast<Col<uword>*>(value.second);
                break;
              case 2:
                _fileSuffix += "," + value.first;
                _genColVec = *static_cast<Col<double>*>(value.second);
                break;
            }
            ++n;
          }

          cout << "Using input: " << _fileSuffix << endl;

          _copyOfGenRowVec = _genRowVec;
          _copyOfElemInds = _elemInds;
          _copyOfGenColVec = _genColVec;

          expectedElemEqual();

          _genRowVec = _copyOfGenRowVec;
          _elemInds = _copyOfElemInds;
          _genColVec = _copyOfGenColVec;
          expectedElemPlus();

          _genRowVec = _copyOfGenRowVec;
          _elemInds = _copyOfElemInds;
          _genColVec = _copyOfGenColVec;
          expectedElemMinus();

          _genRowVec = _copyOfGenRowVec;
          _elemInds = _copyOfElemInds;
          _genColVec = _copyOfGenColVec;
          expectedElemElemTimes();

          _genRowVec = _copyOfGenRowVec;
          _elemInds = _copyOfElemInds;
          _genColVec = _copyOfGenColVec;
          expectedElemElemDivide();

          _genRowVec = _copyOfGenRowVec;
          _elemInds = _copyOfElemInds;
          _genColVec = _copyOfGenColVec;
          expectedColsEqual();

          _genRowVec = _copyOfGenRowVec;
          _elemInds = _copyOfElemInds;
          _genColVec = _copyOfGenColVec;
          expectedColsPlus();

          _genRowVec = _copyOfGenRowVec;
          _elemInds = _copyOfElemInds;
          _genColVec = _copyOfGenColVec;
          expectedColsMinus();

          _genRowVec = _copyOfGenRowVec;
          _elemInds = _copyOfElemInds;
          _genColVec = _copyOfGenColVec;
          expectedColsElemTimes();

          _genRowVec = _copyOfGenRowVec;
          _elemInds = _copyOfElemInds;
          _genColVec = _copyOfGenColVec;
          expectedColsElemDivide();
        }

        cout << "done." << endl;
      }

    protected:
      Row<double> _genRowVec;
      Row<double> _copyOfGenRowVec;

      Col<uword> _elemInds;
      Col<uword> _copyOfElemInds;

      Col<double> _genColVec;
      Col<double> _copyOfGenColVec;

      void expectedElemEqual() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genRowVec.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(_genColVec.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemEqual() ... ";

        _genRowVec.elem(_elemInds) = _genColVec;
        save<double>("Row.elemEqual", _genRowVec);

        cout << "done." << endl;
      }

      void expectedElemPlus() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genRowVec.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(_genColVec.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemPlus() ... ";

        _genRowVec.elem(_elemInds) += _genColVec;
        save<double>("Row.elemPlus", _genRowVec);

        cout << "done." << endl;
      }

      void expectedElemMinus() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genRowVec.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(_genColVec.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemMinus() ... ";

        _genRowVec.elem(_elemInds) -= _genColVec;
        save<double>("Row.elemMinus", _genRowVec);

        cout << "done." << endl;
      }

      void expectedElemElemTimes() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genRowVec.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(_genColVec.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemElemTimes() ... ";

        _genRowVec.elem(_elemInds) %= _genColVec;
        save<double>("Row.elemElemTimes", _genRowVec);

        cout << "done." << endl;
      }

      void expectedElemElemDivide() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genRowVec.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(_genColVec.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemElemDivide() ... ";

        _genRowVec.elem(_elemInds) /= _genColVec;
        save<double>("Row.elemElemDivide", _genRowVec);

        cout << "done." << endl;
      }

      void expectedColsEqual() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genRowVec.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(_genColVec.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemEqual() ... ";

        _genRowVec.cols(_elemInds) = _genColVec;
        save<double>("Row.colsEqual", _genRowVec);

        cout << "done." << endl;
      }

      void expectedColsPlus() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genRowVec.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(_genColVec.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemPlus() ... ";

        _genRowVec.cols(_elemInds) += _genColVec;
        save<double>("Row.colsPlus", _genRowVec);

        cout << "done." << endl;
      }

      void expectedColsMinus() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genRowVec.in_range(_elemInds.at(n))) {
            return;
          }
        }


        if(_genColVec.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemMinus() ... ";

        _genRowVec.cols(_elemInds) -= _genColVec;
        save<double>("Row.colsMinus", _genRowVec);

        cout << "done." << endl;
      }

      void expectedColsElemTimes() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genRowVec.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(_genColVec.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemElemTimes() ... ";

        _genRowVec.cols(_elemInds) %= _genColVec;
        save<double>("Row.colsElemTimes", _genRowVec);

        cout << "done." << endl;
      }

      void expectedColsElemDivide() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genRowVec.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(_genColVec.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemElemDivide() ... ";

        _genRowVec.cols(_elemInds) /= _genColVec;
        save<double>("Row.colsElemDivide", _genRowVec);

        cout << "done." << endl;
      }

  };
}
