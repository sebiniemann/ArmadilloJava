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
  class ExpectedInPlaceGenColVecElemIndNumElems: public Expected {
    public:
      ExpectedInPlaceGenColVecElemIndNumElems() {
        cout << "Compute ExpectedInPlaceGenColVecElemIndNumElmes(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters( {
            InputClass::GenColVec,
            InputClass::ElemInd,
            InputClass::NumElems
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
                _numElems = *static_cast<int*>(value.second);
                break;
            }
            ++n;
          }

          cout << "Using input: " << _fileSuffix << endl;

          _copyOfGenColVec = _genColVec;
          _copyOfElemInd = _elemInd;
          _copyOfNumElems = _numElems;

          expectedColVecInsertRows();

          _genColVec = _copyOfGenColVec;
          _elemInd = _copyOfElemInd;
          _numElems = _copyOfNumElems;

          expectedColVecInsertRowsTrue();
          
          _genColVec = _copyOfGenColVec;
          _elemInd = _copyOfElemInd;
          _numElems = _copyOfNumElems;

          expectedColVecInsertRowsFalse();

          _genColVec = _copyOfGenColVec;
          _elemInd = _copyOfElemInd;
          _numElems = _copyOfNumElems;

          expectedColVecShedRows();
        }

        cout << "done." << endl;
      }

    protected:
      Col<double> _genColVec;
      Col<double> _copyOfGenColVec;

      int _elemInd;
      int _copyOfElemInd;

      int _numElems;
      int _copyOfNumElems;

      void expectedColVecInsertRows() {
        if (_elemInd >= _genColVec.n_elem) {
          return;
        }
        cout << "- Compute expectedColVecInsertRows() ... ";
        _genColVec.insert_rows(_elemInd, _numElems);

        save<double>("Col.insertRows", _genColVec);
        cout << "done." << endl;
      }
      
      void expectedColVecInsertRowsTrue() {
        if (_elemInd >= _genColVec.n_elem) {
          return;
        }
        cout << "- Compute expectedColVecInsertRowsTrue() ... ";
        _genColVec.insert_rows(_elemInd, _numElems, true);

        save<double>("Col.insertRowsTrue", _genColVec);
        cout << "done." << endl;
      }
      
      void expectedColVecInsertRowsFalse() {
        if (_elemInd >= _genColVec.n_elem) {
          return;
        }
        cout << "- Compute expectedColVecInsertRowsFalse() ... ";
        _genColVec.insert_rows(_elemInd, _numElems, false);

        save<double>("Col.insertRowsFalse", _genColVec);
        cout << "done." << endl;
      }

      void expectedColVecShedRows() {
        if (_elemInd >= _genColVec.n_elem) {
          return;
        }
        if (_numElems < _elemInd || _numElems >= _genColVec.n_elem) {
          return;
        }

        cout << "- Compute expectedColVecShedRows() ... ";
        _genColVec.shed_rows(_elemInd, _numElems);

        save<double>("Col.shedRows", _genColVec);
        cout << "done." << endl;
      }

  };
}
