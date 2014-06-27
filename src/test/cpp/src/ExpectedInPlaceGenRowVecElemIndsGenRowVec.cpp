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
  class ExpectedInPlaceGenRowVecElemIndsGenRowVec : public Expected {
    public:
      ExpectedInPlaceGenRowVecElemIndsGenRowVec() {
        cout << "Compute ExpectedInPlaceGenRowVecElemIndsGenRowVec(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
          InputClass::GenRowVec,
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
                _genRowVecA = *static_cast<Row<double>*>(value.second);
                break;
              case 1:
                _fileSuffix += "," + value.first;
                _elemInds = *static_cast<Col<uword>*>(value.second);
                break;
              case 2:
                _fileSuffix += "," + value.first;
                _genRowVecB = *static_cast<Row<double>*>(value.second);
                break;
            }
            ++n;
          }

          cout << "Using input: " << _fileSuffix << endl;

          _copyOfGenRowVecA = _genRowVecA;
          _copyOfElemInds = _elemInds;
          _copyOfGenRowVecB = _genRowVecB;

          expectedElemEqual();

          _genRowVecA = _copyOfGenRowVecA;
          _elemInds = _copyOfElemInds;
          _genRowVecB = _copyOfGenRowVecB;
          expectedElemPlus();

          _genRowVecA = _copyOfGenRowVecA;
          _elemInds = _copyOfElemInds;
          _genRowVecB = _copyOfGenRowVecB;
          expectedElemMinus();

          _genRowVecA = _copyOfGenRowVecA;
          _elemInds = _copyOfElemInds;
          _genRowVecB = _copyOfGenRowVecB;
          expectedElemElemTimes();

          _genRowVecA = _copyOfGenRowVecA;
          _elemInds = _copyOfElemInds;
          _genRowVecB = _copyOfGenRowVecB;
          expectedElemElemDivide();

          _genRowVecA = _copyOfGenRowVecA;
          _elemInds = _copyOfElemInds;
          _genRowVecB = _copyOfGenRowVecB;
          expectedColsEqual();

          _genRowVecA = _copyOfGenRowVecA;
          _elemInds = _copyOfElemInds;
          _genRowVecB = _copyOfGenRowVecB;
          expectedColsPlus();

          _genRowVecA = _copyOfGenRowVecA;
          _elemInds = _copyOfElemInds;
          _genRowVecB = _copyOfGenRowVecB;
          expectedColsMinus();

          _genRowVecA = _copyOfGenRowVecA;
          _elemInds = _copyOfElemInds;
          _genRowVecB = _copyOfGenRowVecB;
          expectedColsElemTimes();

          _genRowVecA = _copyOfGenRowVecA;
          _elemInds = _copyOfElemInds;
          _genRowVecB = _copyOfGenRowVecB;
          expectedColsElemDivide();
        }

        cout << "done." << endl;
      }

    protected:
      Row<double> _genRowVecA;
      Row<double> _copyOfGenRowVecA;

      Col<uword> _elemInds;
      Col<uword> _copyOfElemInds;

      Row<double> _genRowVecB;
      Row<double> _copyOfGenRowVecB;

      void expectedElemEqual() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genRowVecA.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(_genRowVecB.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemEqual() ... ";

        _genRowVecA.elem(_elemInds) = _genRowVecB;
        save<double>("Row.elemEqual", _genRowVecA);

        cout << "done." << endl;
      }

      void expectedElemPlus() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genRowVecA.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(_genRowVecB.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemPlus() ... ";

        _genRowVecA.elem(_elemInds) += _genRowVecB;
        save<double>("Row.elemPlus", _genRowVecA);

        cout << "done." << endl;
      }

      void expectedElemMinus() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genRowVecA.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(_genRowVecB.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemMinus() ... ";

        _genRowVecA.elem(_elemInds) -= _genRowVecB;
        save<double>("Row.elemMinus", _genRowVecA);

        cout << "done." << endl;
      }

      void expectedElemElemTimes() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genRowVecA.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(_genRowVecB.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemElemTimes() ... ";

        _genRowVecA.elem(_elemInds) %= _genRowVecB;
        save<double>("Row.elemElemTimes", _genRowVecA);

        cout << "done." << endl;
      }

      void expectedElemElemDivide() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genRowVecA.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(_genRowVecB.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemElemDivide() ... ";

        _genRowVecA.elem(_elemInds) /= _genRowVecB;
        save<double>("Row.elemElemDivide", _genRowVecA);

        cout << "done." << endl;
      }

      void expectedColsEqual() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genRowVecA.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(_genRowVecB.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemEqual() ... ";

        _genRowVecA.cols(_elemInds) = _genRowVecB;
        save<double>("Row.colsEqual", _genRowVecA);

        cout << "done." << endl;
      }

      void expectedColsPlus() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genRowVecA.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(_genRowVecB.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemPlus() ... ";

        _genRowVecA.cols(_elemInds) += _genRowVecB;
        save<double>("Row.colsPlus", _genRowVecA);

        cout << "done." << endl;
      }

      void expectedColsMinus() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genRowVecA.in_range(_elemInds.at(n))) {
            return;
          }
        }


        if(_genRowVecB.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemMinus() ... ";

        _genRowVecA.cols(_elemInds) -= _genRowVecB;
        save<double>("Row.colsMinus", _genRowVecA);

        cout << "done." << endl;
      }

      void expectedColsElemTimes() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genRowVecA.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(_genRowVecB.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemElemTimes() ... ";

        _genRowVecA.cols(_elemInds) %= _genRowVecB;
        save<double>("Row.colsElemTimes", _genRowVecA);

        cout << "done." << endl;
      }

      void expectedColsElemDivide() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genRowVecA.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(_genRowVecB.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemElemDivide() ... ";

        _genRowVecA.cols(_elemInds) /= _genRowVecB;
        save<double>("Row.colsElemDivide", _genRowVecA);

        cout << "done." << endl;
      }

  };
}
