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
  class ExpectedInPlaceGenMatElemIndsGenDouble : public Expected {
    public:
      ExpectedInPlaceGenMatElemIndsGenDouble() {
        cout << "Compute ExpectedInPlaceGenMatElemIndsGenDouble(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
          InputClass::GenMat,
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
                _genMat = *static_cast<Mat<double>*>(value.second);
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

          _copyOfGenMat = _genMat;
          _copyOfElemInds = _elemInds;
          _copyOfGenDouble = _genDouble;

          expectedElemPlus();

          _genMat = _copyOfGenMat;
          _elemInds = _copyOfElemInds;
          _genDouble = _copyOfGenDouble;
          expectedElemMinus();

          _genMat = _copyOfGenMat;
          _elemInds = _copyOfElemInds;
          _genDouble = _copyOfGenDouble;
          expectedElemTimes();

          _genMat = _copyOfGenMat;
          _elemInds = _copyOfElemInds;
          _genDouble = _copyOfGenDouble;
          expectedElemDivide();
        }

        cout << "done." << endl;
      }

    protected:
      Mat<double> _genMat;
      Mat<double> _copyOfGenMat;

      Col<uword> _elemInds;
      Col<uword> _copyOfElemInds;

      double _genDouble;
      double _copyOfGenDouble;

      void expectedElemPlus() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genMat.in_range(_elemInds.at(n))) {
            return;
          }
        }

        cout << "- Compute expectedElemPlus() ... ";

        _genMat.elem(_elemInds) += _genDouble;
        save<double>("Mat.elemPlus", _genMat);

        cout << "done." << endl;
      }

      void expectedElemMinus() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genMat.in_range(_elemInds.at(n))) {
            return;
          }
        }

        cout << "- Compute expectedElemMinus() ... ";

        _genMat.elem(_elemInds) -= _genDouble;
        save<double>("Mat.elemMinus", _genMat);

        cout << "done." << endl;
      }

      void expectedElemTimes() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genMat.in_range(_elemInds.at(n))) {
            return;
          }
        }

        cout << "- Compute expectedElemTimes() ... ";

        _genMat.elem(_elemInds) *= _genDouble;
        save<double>("Mat.elemTimes", _genMat);

        cout << "done." << endl;
      }

      void expectedElemDivide() {
        for(int n = 0; n < _elemInds.n_elem; n++) {
          if(!_genMat.in_range(_elemInds.at(n))) {
            return;
          }
        }

        cout << "- Compute expectedElemDivide() ... ";

        _genMat.elem(_elemInds) /= _genDouble;
        save<double>("Mat.elemDivide", _genMat);

        cout << "done." << endl;
      }

  };
}
