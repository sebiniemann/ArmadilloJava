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
  class ExpectedInPlaceGenRowVecElemInd : public Expected {
    public:
      ExpectedInPlaceGenRowVecElemInd() {
        cout << "Compute ExpectedInPlaceGenRowVecElemInd(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::GenRowVec,
            InputClass::ElemInd
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
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            _copyOfGenRowVec = _genRowVec;
            _copyOfElemInd = _elemInd;

            expectedRowVecAtIncrement();

            _genRowVec = _copyOfGenRowVec;
            _elemInd = _copyOfElemInd;
			  
            expectedRowVecAtDecrement();
			  
			_genRowVec = _copyOfGenRowVec;
			_elemInd = _copyOfElemInd;
			  
			  expectedRowVecShed_col();
          }

          cout << "done." << endl;
        }

    protected:
      Row<double> _genRowVec;
      Row<double> _copyOfGenRowVec;

      int _elemInd;
      int _copyOfElemInd;

      void expectedRowVecAtIncrement() {
        if(_elemInd >= _genRowVec.n_elem) {
          return;
        }

        _genRowVec.at(_elemInd)++;

        cout << "- Compute expectedRowVecAtIncrement() ... ";
        save<double>("Row.atIncrement", _genRowVec);
        cout << "done." << endl;
      }

      void expectedRowVecAtDecrement() {
        if(_elemInd >= _genRowVec.n_elem) {
          return;
        }

        _genRowVec.at(_elemInd)--;

        cout << "- Compute expectedRowVecAtDecrement() ... ";
        save<double>("Row.atDecrement", _genRowVec);
        cout << "done." << endl;
      }
	  
	  void expectedRowVecShed_col() {
		  if(_elemInd >= _genRowVec.n_cols) {
			  return;
		  }
		  
		  _genRowVec.shed_col(_elemInd);
		  
		  cout << "- Compute expectedRowVecShed_col() ... ";
		  save<double>("Row.shedCol", _genRowVec);
		  cout << "done." << endl;
      }
  };
}
