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

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedInPlaceGenColVecElemIndGenDouble : public Expected {
    public:
      ExpectedInPlaceGenColVecElemIndGenDouble() {
        cout << "Compute ExpectedInPlaceGenColVecElemIndGenDouble(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::GenColVec,
            InputClass::ElemInd,
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
                  _elemInd = *static_cast<int*>(value.second);
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
            _copyOfElemInd = _elemInd;
            _copyOfGenDouble = _genDouble;

            expectedColVecAtEqual();

            _genColVec = _copyOfGenColVec;
            _elemInd = _copyOfElemInd;
            _genDouble = _copyOfGenDouble;
            
			expectedColVecAtPlus();

            _genColVec = _copyOfGenColVec;
            _elemInd = _copyOfElemInd;
            _genDouble = _copyOfGenDouble;
            
			expectedColVecAtMinus();

            _genColVec = _copyOfGenColVec;
            _elemInd = _copyOfElemInd;
            _genDouble = _copyOfGenDouble;
            
			expectedColVecAtTimes();

            _genColVec = _copyOfGenColVec;
            _elemInd = _copyOfElemInd;
            _genDouble = _copyOfGenDouble;
            
			expectedColVecAtDivide();
          }

          cout << "done." << endl;
        }

    protected:
      Col<double> _genColVec;
      Col<double> _copyOfGenColVec;

      int _elemInd;
      int _copyOfElemInd;

      double _genDouble;
      double _copyOfGenDouble;

      void expectedColVecAtEqual() {
        if(_elemInd >= _genColVec.n_elem) {
          return;
        }

        _genColVec.at(_elemInd) = _genDouble;

        cout << "- Compute expectedColVecAtEqual() ... ";
        save<double>("Col.atEqual", _genColVec);
        cout << "done." << endl;
      }

      void expectedColVecAtPlus() {
        if(_elemInd >= _genColVec.n_elem) {
          return;
        }

        _genColVec.at(_elemInd) += _genDouble;

        cout << "- Compute expectedColVecAtPlus() ... ";
        save<double>("Col.atPlus", _genColVec);
        cout << "done." << endl;
      }

      void expectedColVecAtMinus() {
        if(_elemInd >= _genColVec.n_elem) {
          return;
        }

        _genColVec.at(_elemInd) -= _genDouble;

        cout << "- Compute expectedColVecAtMinus() ... ";
        save<double>("Col.atMinus", _genColVec);
        cout << "done." << endl;
      }

      void expectedColVecAtTimes() {
        if(_elemInd >= _genColVec.n_elem) {
          return;
        }

        _genColVec.at(_elemInd) *= _genDouble;

        cout << "- Compute expectedColVecAtTimes() ... ";
        save<double>("Col.atTimes", _genColVec);
        cout << "done." << endl;
      }

      void expectedColVecAtDivide() {
        if(_elemInd >= _genColVec.n_elem) {
          return;
        }

        _genColVec.at(_elemInd) /= _genDouble;

        cout << "- Compute expectedColVecAtDivide() ... ";
        save<double>("Col.atDivide", _genColVec);
        cout << "done." << endl;
      }
  };
}
