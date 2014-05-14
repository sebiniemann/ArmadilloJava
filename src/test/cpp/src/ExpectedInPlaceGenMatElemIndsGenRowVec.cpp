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
using arma::Row;
using arma::uword;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedInPlaceGenMatElemIndsGenRowVec : public Expected {
    public:
      ExpectedInPlaceGenMatElemIndsGenRowVec() {
        cout << "Compute ExpectedInPlaceGenMatElemIndsGenRowVec(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
          InputClass::GenMat,
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
                _genMat = *static_cast<Mat<double>*>(value.second);
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

          _copyOfGenMat = _genMat;
          _copyOfElemInds = _elemInds;
          _copyOfGenRowVec = _genRowVec;

          expectedElemEqual();

          _genMat = _copyOfGenMat;
          _elemInds = _copyOfElemInds;
          _genRowVec = _copyOfGenRowVec;
          expectedElemPlus();

          _genMat = _copyOfGenMat;
          _elemInds = _copyOfElemInds;
          _genRowVec = _copyOfGenRowVec;
          expectedElemMinus();

          _genMat = _copyOfGenMat;
          _elemInds = _copyOfElemInds;
          _genRowVec = _copyOfGenRowVec;
          expectedElemElemTimes();

          _genMat = _copyOfGenMat;
          _elemInds = _copyOfElemInds;
          _genRowVec = _copyOfGenRowVec;
          expectedElemElemDivide();
        }

        cout << "done." << endl;
      }

    protected:
      Mat<double> _genMat;
      Mat<double> _copyOfGenMat;

      Col<uword> _elemInds;
      Col<uword> _copyOfElemInds;

      Row<double> _genRowVec;
      Row<double> _copyOfGenRowVec;

      void expectedElemEqual() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genMat.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(_genRowVec.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemEqual() ... ";

        _genMat.elem(_elemInds) = _genRowVec;
        save<double>("Mat.elemEqual", _genMat);

        cout << "done." << endl;
      }

      void expectedElemPlus() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genMat.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(_genRowVec.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemPlus() ... ";

        _genMat.elem(_elemInds) += _genRowVec;
        save<double>("Mat.elemPlus", _genMat);

        cout << "done." << endl;
      }

      void expectedElemMinus() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genMat.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(_genRowVec.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemMinus() ... ";

        _genMat.elem(_elemInds) -= _genRowVec;
        save<double>("Mat.elemMinus", _genMat);

        cout << "done." << endl;
      }

      void expectedElemElemTimes() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genMat.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(_genRowVec.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemElemTimes() ... ";

        _genMat.elem(_elemInds) %= _genRowVec;
        save<double>("Mat.elemElemTimes", _genMat);

        cout << "done." << endl;
      }

      void expectedElemElemDivide() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genMat.in_range(_elemInds.at(n))) {
            return;
          }
        }

        if(_genRowVec.n_elem != _elemInds.n_elem) {
          return;
        }

        cout << "- Compute expectedElemElemDivide() ... ";

        _genMat.elem(_elemInds) /= _genRowVec;
        save<double>("Mat.elemElemDivide", _genMat);

        cout << "done." << endl;
      }

  };
}
