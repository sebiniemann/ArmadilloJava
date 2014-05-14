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
  class ExpectedInPlaceGenMatElemIndsGenMat : public Expected {
    public:
      ExpectedInPlaceGenMatElemIndsGenMat() {
        cout << "Compute ExpectedInPlaceGenMatElemIndsGenMat(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
          InputClass::GenMat,
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
                _genMatA = *static_cast<Mat<double>*>(value.second);
                break;
              case 1:
                _fileSuffix += "," + value.first;
                _elemInds = *static_cast<Col<uword>*>(value.second);
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
          _copyOfElemInds = _elemInds;
          _copyOfGenMatB = _genMatB;

          expectedElemEqual();

          _genMatA = _copyOfGenMatA;
          _elemInds = _copyOfElemInds;
          _genMatB = _copyOfGenMatB;
          expectedElemPlus();

          _genMatA = _copyOfGenMatA;
          _elemInds = _copyOfElemInds;
          _genMatB = _copyOfGenMatB;
          expectedElemMinus();

          _genMatA = _copyOfGenMatA;
          _elemInds = _copyOfElemInds;
          _genMatB = _copyOfGenMatB;
          expectedElemElemTimes();

          _genMatA = _copyOfGenMatA;
          _elemInds = _copyOfElemInds;
          _genMatB = _copyOfGenMatB;
          expectedElemElemDivide();
        }

        cout << "done." << endl;
      }

    protected:
      Mat<double> _genMatA;
      Mat<double> _copyOfGenMatA;

      Col<uword> _elemInds;
      Col<uword> _copyOfElemInds;

      Mat<double> _genMatB;
      Mat<double> _copyOfGenMatB;

      void expectedElemEqual() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genMatA.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(!_genMatB.is_vec()) {
          return;
        }

        if(_genMatB.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemEqual() ... ";

        _genMatA.elem(_elemInds) = _genMatB;
        save<double>("Mat.elemEqual", _genMatA);

        cout << "done." << endl;
      }

      void expectedElemPlus() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genMatA.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(!_genMatB.is_vec()) {
          return;
        }

        if(_genMatB.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemPlus() ... ";

        _genMatA.elem(_elemInds) += _genMatB;
        save<double>("Mat.elemPlus", _genMatA);

        cout << "done." << endl;
      }

      void expectedElemMinus() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genMatA.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(!_genMatB.is_vec()) {
          return;
        }

        if(_genMatB.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemMinus() ... ";

        _genMatA.elem(_elemInds) -= _genMatB;
        save<double>("Mat.elemMinus", _genMatA);

        cout << "done." << endl;
      }

      void expectedElemElemTimes() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genMatA.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(!_genMatB.is_vec()) {
          return;
        }

        if(_genMatB.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemElemTimes() ... ";

        _genMatA.elem(_elemInds) %= _genMatB;
        save<double>("Mat.elemElemTimes", _genMatA);

        cout << "done." << endl;
      }

      void expectedElemElemDivide() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genMatA.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(!_genMatB.is_vec()) {
          return;
        }

        if(_genMatB.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemElemDivide() ... ";

        _genMatA.elem(_elemInds) /= _genMatB;
        save<double>("Mat.elemElemDivide", _genMatA);

        cout << "done." << endl;
      }

  };
}
