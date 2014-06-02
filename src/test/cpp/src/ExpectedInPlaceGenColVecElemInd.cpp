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
  class ExpectedInPlaceGenColVecElemInd : public Expected {
    public:
      ExpectedInPlaceGenColVecElemInd() {
        cout << "Compute ExpectedInPlaceGenColVecElemInd(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::GenColVec,
            InputClass::ElemInd
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
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            _copyOfGenColVec = _genColVec;
            _copyOfElemInd = _elemInd;

            expectedColVecAtIncrement();

            _genColVec = _copyOfGenColVec;
            _elemInd = _copyOfElemInd;
			  
            expectedColVecAtDecrement();
			  
			_genColVec = _copyOfGenColVec;
			_elemInd = _copyOfElemInd;
			  
			  expectedColVecShed_row();
          }

          cout << "done." << endl;
        }

    protected:
      Col<double> _genColVec;
      Col<double> _copyOfGenColVec;

      int _elemInd;
      int _copyOfElemInd;

      void expectedColVecAtIncrement() {
        if(_elemInd >= _genColVec.n_elem) {
          return;
        }

        _genColVec.at(_elemInd)++;

        cout << "- Compute expectedColVecAtIncrement() ... ";
        save<double>("Col.atIncrement", _genColVec);
        cout << "done." << endl;
      }

      void expectedColVecAtDecrement() {
        if(_elemInd >= _genColVec.n_elem) {
          return;
        }

        _genColVec.at(_elemInd)--;

        cout << "- Compute expectedColVecAtDecrement() ... ";
        save<double>("Col.atDecrement", _genColVec);
        cout << "done." << endl;
      }
	  
	  void expectedColVecShed_row() {
		  if(_elemInd >= _genColVec.n_rows) {
			  return;
		  }
		  
		  _genColVec.shed_row(_elemInd);
		  
		  cout << "- Compute expectedColVecShed_row() ... ";
		  save<double>("Col.Shed_row", _genColVec);
		  cout << "done." << endl;
      }
  };
}
