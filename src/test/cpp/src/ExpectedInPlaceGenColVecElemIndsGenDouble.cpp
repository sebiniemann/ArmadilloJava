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
using arma::uword;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedInPlaceGenColVecElemIndsGenDouble : public Expected {
    public:
      ExpectedInPlaceGenColVecElemIndsGenDouble() {
        cout << "Compute ExpectedInPlaceGenColVecElemIndsGenDouble(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
          InputClass::GenColVec,
          InputClass::ElemInds,
          InputClass::GenDouble
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
                _genDouble = *static_cast<double*>(value.second);
                break;
            }
            ++n;
          }

          cout << "Using input: " << _fileSuffix << endl;

          _copyOfGenColVec = _genColVec;
          _copyOfElemInds = _elemInds;
          _copyOfGenDouble = _genDouble;
          expectedElemPlus();

          _genColVec = _copyOfGenColVec;
          _elemInds = _copyOfElemInds;
          _genDouble = _copyOfGenDouble;
          expectedElemMinus();

          _genColVec = _copyOfGenColVec;
          _elemInds = _copyOfElemInds;
          _genDouble = _copyOfGenDouble;
          expectedElemTimes();

          _genColVec = _copyOfGenColVec;
          _elemInds = _copyOfElemInds;
          _genDouble = _copyOfGenDouble;
          expectedElemDivide();

          _genColVec = _copyOfGenColVec;
          _elemInds = _copyOfElemInds;
          _genDouble = _copyOfGenDouble;
          expectedRowsPlus();

          _genColVec = _copyOfGenColVec;
          _elemInds = _copyOfElemInds;
          _genDouble = _copyOfGenDouble;
          expectedRowsMinus();

          _genColVec = _copyOfGenColVec;
          _elemInds = _copyOfElemInds;
          _genDouble = _copyOfGenDouble;
          expectedRowsTimes();

          _genColVec = _copyOfGenColVec;
          _elemInds = _copyOfElemInds;
          _genDouble = _copyOfGenDouble;
          expectedRowsDivide();

        }

        cout << "done." << endl;
      }

    protected:
      Mat<double> _genColVec;
      Mat<double> _copyOfGenColVec;

      Col<uword> _elemInds;
      Col<uword> _copyOfElemInds;

      double _genDouble;
      double _copyOfGenDouble;

      void expectedElemPlus() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genColVec.in_range(_elemInds.at(n))) {
            return;
          }
        }

        cout << "- Compute expectedElemPlus() ... ";

        _genColVec.elem(_elemInds) += _genDouble;

        save<double>("Col.elemPlus", _genColVec);
        cout << "done." << endl;
      }

      void expectedElemMinus() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genColVec.in_range(_elemInds.at(n))) {
            return;
          }
        }

        cout << "- Compute expectedElemMinus() ... ";

        _genColVec.elem(_elemInds) -= _genDouble;

        save<double>("Col.elemMinus", _genColVec);
        cout << "done." << endl;
      }

      void expectedElemTimes() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genColVec.in_range(_elemInds.at(n))) {
            return;
          }
        }

        cout << "- Compute expectedElemTimes() ... ";

        _genColVec.elem(_elemInds) *= _genDouble;

        save<double>("Col.elemTimes", _genColVec);
        cout << "done." << endl;
      }

      void expectedElemDivide() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genColVec.in_range(_elemInds.at(n))) {
            return;
          }
        }

        cout << "- Compute expectedElemDivide() ... ";

        _genColVec.elem(_elemInds) /= _genDouble;

        save<double>("Col.elemDivide", _genColVec);
        cout << "done." << endl;
      }

      void expectedRowsPlus() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genColVec.in_range(_elemInds.at(n))) {
            return;
          }
        }

        cout << "- Compute expectedRowsPlus() ... ";

        _genColVec.rows(_elemInds) += _genDouble;

        save<double>("Col.rowsPlus", _genColVec);
        cout << "done." << endl;
      }

      void expectedRowsMinus() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genColVec.in_range(_elemInds.at(n))) {
            return;
          }
        }

        cout << "- Compute expectedRowsMinus() ... ";

        _genColVec.rows(_elemInds) -= _genDouble;

        save<double>("Col.rowsMinus", _genColVec);
        cout << "done." << endl;
      }

      void expectedRowsTimes() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genColVec.in_range(_elemInds.at(n))) {
            return;
          }
        }

        cout << "- Compute expectedRowsTimes() ... ";

        _genColVec.rows(_elemInds) *= _genDouble;

        save<double>("Col.rowsTimes", _genColVec);
        cout << "done." << endl;
      }

      void expectedRowsDivide() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genColVec.in_range(_elemInds.at(n))) {
            return;
          }
        }

        cout << "- Compute expectedRowsDivide() ... ";

        _genColVec.rows(_elemInds) /= _genDouble;

        save<double>("Col.rowsDivide", _genColVec);
        cout << "done." << endl;
      }

  };
}
