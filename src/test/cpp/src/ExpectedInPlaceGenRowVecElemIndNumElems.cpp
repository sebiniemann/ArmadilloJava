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

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedInPlaceGenRowVecElemIndNumElems: public Expected {
    public:
      ExpectedInPlaceGenRowVecElemIndNumElems() {
        cout << "Compute ExpectedInPlaceGenRowVecElemIndNumElmes(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters( {
            InputClass::GenRowVec,
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
                _genRowVec = *static_cast<Row<double>*>(value.second);
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

          _copyOfGenRowVec = _genRowVec;
          _copyOfElemInd = _elemInd;
          _copyOfNumElems = _numElems;

          expectedRowVecInsertCols();

          _genRowVec = _copyOfGenRowVec;
          _elemInd = _copyOfElemInd;
          _numElems = _copyOfNumElems;

          expectedRowVecInsertColsTrue();
          
          _genRowVec = _copyOfGenRowVec;
          _elemInd = _copyOfElemInd;
          _numElems = _copyOfNumElems;

          expectedRowVecInsertColsFalse();

          _genRowVec = _copyOfGenRowVec;
          _elemInd = _copyOfElemInd;
          _numElems = _copyOfNumElems;

          expectedRowVecShedCols();
        }

        cout << "done." << endl;
      }

    protected:
      Row<double> _genRowVec;
      Row<double> _copyOfGenRowVec;

      int _elemInd;
      int _copyOfElemInd;

      int _numElems;
      int _copyOfNumElems;

      void expectedRowVecInsertCols() {
        if (_elemInd >= _genRowVec.n_elem) {
          return;
        }
        cout << "- Compute expectedRowVecInsertCols() ... ";
        _genRowVec.insert_cols(_elemInd, _numElems);

        save<double>("row.insertCols", _genRowVec);
        cout << "done." << endl;
      }
      
      void expectedRowVecInsertColsTrue() {
        if (_elemInd >= _genRowVec.n_elem) {
          return;
        }
        cout << "- Compute expectedRowVecInsertColsTrue() ... ";
        _genRowVec.insert_cols(_elemInd, _numElems, true);

        save<double>("row.insertColsTrue", _genRowVec);
        cout << "done." << endl;
      }
      
      void expectedRowVecInsertColsFalse() {
        if (_elemInd >= _genRowVec.n_elem) {
          return;
        }
        cout << "- Compute expectedRowVecInsertColsFalse() ... ";
        _genRowVec.insert_cols(_elemInd, _numElems, false);

        save<double>("row.insertColsFalse", _genRowVec);
        cout << "done." << endl;
      }

      void expectedRowVecShedCols() {
        if (_elemInd >= _genRowVec.n_elem) {
          return;
        }
        if (_numElems < _elemInd || _numElems >= _genRowVec.n_elem) {
          return;
        }

        cout << "- Compute expectedRowVecShedCols() ... ";
        _genRowVec.shed_cols(_elemInd, _numElems);

        save<double>("row.shedCols", _genRowVec);
        cout << "done." << endl;
      }

  };
}
