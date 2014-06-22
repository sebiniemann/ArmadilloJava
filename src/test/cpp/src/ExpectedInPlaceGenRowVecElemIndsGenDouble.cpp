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
  class ExpectedInPlaceGenRowVecElemIndsGenDouble : public Expected {
    public:
      ExpectedInPlaceGenRowVecElemIndsGenDouble() {
        cout << "Compute ExpectedInPlaceGenRowVecElemIndsGenDouble(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
          InputClass::GenRowVec,
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
                _genRowVec = *static_cast<Row<double>*>(value.second);
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

          _copyOfGenRowVec = _genRowVec;
          _copyOfElemInds = _elemInds;
          _copyOfGenDouble = _genDouble;
          expectedElemPlus();

          _genRowVec = _copyOfGenRowVec;
          _elemInds = _copyOfElemInds;
          _genDouble = _copyOfGenDouble;
          expectedElemMinus();

          _genRowVec = _copyOfGenRowVec;
          _elemInds = _copyOfElemInds;
          _genDouble = _copyOfGenDouble;
          expectedElemTimes();

          _genRowVec = _copyOfGenRowVec;
          _elemInds = _copyOfElemInds;
          _genDouble = _copyOfGenDouble;
          expectedElemDivide();

          _genRowVec = _copyOfGenRowVec;
          _elemInds = _copyOfElemInds;
          _genDouble = _copyOfGenDouble;
          expectedColsPlus();

          _genRowVec = _copyOfGenRowVec;
          _elemInds = _copyOfElemInds;
          _genDouble = _copyOfGenDouble;
          expectedColsMinus();

          _genRowVec = _copyOfGenRowVec;
          _elemInds = _copyOfElemInds;
          _genDouble = _copyOfGenDouble;
          expectedColsTimes();

          _genRowVec = _copyOfGenRowVec;
          _elemInds = _copyOfElemInds;
          _genDouble = _copyOfGenDouble;
          expectedColsDivide();

        }

        cout << "done." << endl;
      }

    protected:
      Mat<double> _genRowVec;
      Mat<double> _copyOfGenRowVec;

      Col<uword> _elemInds;
      Col<uword> _copyOfElemInds;

      double _genDouble;
      double _copyOfGenDouble;

      void expectedElemPlus() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genRowVec.in_range(_elemInds.at(n))) {
            return;
          }
        }

        cout << "- Compute expectedElemPlus() ... ";

        _genRowVec.elem(_elemInds) += _genDouble;

        save<double>("row.elemPlus", _genRowVec);
        cout << "done." << endl;
      }

      void expectedElemMinus() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genRowVec.in_range(_elemInds.at(n))) {
            return;
          }
        }

        cout << "- Compute expectedElemMinus() ... ";

        _genRowVec.elem(_elemInds) -= _genDouble;

        save<double>("row.elemMinus", _genRowVec);
        cout << "done." << endl;
      }

      void expectedElemTimes() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genRowVec.in_range(_elemInds.at(n))) {
            return;
          }
        }

        cout << "- Compute expectedElemTimes() ... ";

        _genRowVec.elem(_elemInds) *= _genDouble;

        save<double>("row.elemTimes", _genRowVec);
        cout << "done." << endl;
      }

      void expectedElemDivide() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genRowVec.in_range(_elemInds.at(n))) {
            return;
          }
        }

        cout << "- Compute expectedElemDivide() ... ";

        _genRowVec.elem(_elemInds) /= _genDouble;

        save<double>("row.elemDivide", _genRowVec);
        cout << "done." << endl;
      }

      void expectedColsPlus() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genRowVec.in_range(_elemInds.at(n))) {
            return;
          }
        }

        cout << "- Compute expectedColsPlus() ... ";

        _genRowVec.cols(_elemInds) += _genDouble;

        save<double>("row.colsPlus", _genRowVec);
        cout << "done." << endl;
      }

      void expectedColsMinus() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genRowVec.in_range(_elemInds.at(n))) {
            return;
          }
        }

        cout << "- Compute expectedColsMinus() ... ";

        _genRowVec.cols(_elemInds) -= _genDouble;

        save<double>("row.colsMinus", _genRowVec);
        cout << "done." << endl;
      }

      void expectedColsTimes() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genRowVec.in_range(_elemInds.at(n))) {
            return;
          }
        }

        cout << "- Compute expectedColsTimes() ... ";

        _genRowVec.cols(_elemInds) *= _genDouble;

        save<double>("row.colsTimes", _genRowVec);
        cout << "done." << endl;
      }

      void expectedColsDivide() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genRowVec.in_range(_elemInds.at(n))) {
            return;
          }
        }

        cout << "- Compute expectedColsDivide() ... ";

        _genRowVec.cols(_elemInds) /= _genDouble;

        save<double>("row.colsDivide", _genRowVec);
        cout << "done." << endl;
      }

  };
}
