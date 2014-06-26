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
  class ExpectedInPlaceGenRowVecElemIndGenRowVec : public Expected {
    public:
      ExpectedInPlaceGenRowVecElemIndGenRowVec() {
        cout << "Compute ExpectedInPlaceGenRowVecElemIndGenRowVec(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::GenRowVec,
            InputClass::ElemInd,
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
                  _elemInd = *static_cast<int*>(value.second);
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
            _copyOfElemInd = _elemInd;
            _copyOfGenRowVecB = _genRowVecB;
			  
			expectedRowVecColEqual();
			  
			_genRowVecA = _copyOfGenRowVecA;
			_elemInd = _copyOfElemInd;
			_genRowVecB = _copyOfGenRowVecB;
			  
			expectedRowVecColPlus();
			  
			_genRowVecA = _copyOfGenRowVecA;
			_elemInd = _copyOfElemInd;
			_genRowVecB = _copyOfGenRowVecB;
			  
			expectedRowVecColMinus();
			  
			_genRowVecA = _copyOfGenRowVecA;
			_elemInd = _copyOfElemInd;
			_genRowVecB = _copyOfGenRowVecB;
			  
			expectedRowVecInsertCols();
			  
          }

          cout << "done." << endl;
        }

    protected:
      Row<double> _genRowVecA;
      Row<double> _copyOfGenRowVecA;

      int _elemInd;
      int _copyOfElemInd;

      Row<double> _genRowVecB;
      Row<double> _copyOfGenRowVecB;

	  void expectedRowVecColEqual() {
		  if(_elemInd >= _genRowVecA.n_elem) {
			  return;
		  }
		  
		  if(!_genRowVecB.is_colvec() || _genRowVecA.n_rows != _genRowVecB.n_rows) {
			  return;
		  }
		  
		  cout << "- Compute expectedRowColEqual() ... ";
		  
		  _genRowVecA.col(_elemInd) = _genRowVecB;
		  save<double>("Row.colEqual", _genRowVecA);
		  
		  cout << "done." << endl;
      }
	  
      void expectedRowVecColPlus() {
		  if(_elemInd >= _genRowVecA.n_elem) {
			  return;
		  }
		  
		  if(!_genRowVecB.is_colvec() || _genRowVecA.n_rows != _genRowVecB.n_rows) {
			  return;
		  }
		  
		  cout << "- Compute expectedRowColPlus() ... ";
		  
		  _genRowVecA.col(_elemInd) += _genRowVecB;
		  save<double>("Row.colPlus", _genRowVecA);
		  
		  cout << "done." << endl;
      }
	  
      void expectedRowVecColMinus() {
		  if(_elemInd >= _genRowVecA.n_elem) {
			  return;
		  }
		  
		  if(!_genRowVecB.is_colvec() || _genRowVecA.n_rows != _genRowVecB.n_rows) {
			  return;
		  }
		  
		  cout << "- Compute expectedRowColMinus() ... ";
		  
		  _genRowVecA.col(_elemInd) -= _genRowVecB;
		  save<double>("Row.colMinus", _genRowVecA);
		  
		  cout << "done." << endl;
      }
	  
	  
	  void expectedRowVecInsertCols() {
		  if(_elemInd >= _genRowVecA.n_elem) {
			  return;
		  }
		  
		  if(!_genRowVecB.is_colvec() || _genRowVecA.n_rows != _genRowVecB.n_rows) {
			  return;
		  }
		  
		  cout << "- Compute expectedRowInsertCols() ... ";
		  
		  _genRowVecA.insert_cols(_elemInd, _genRowVecB);
		  save<double>("Row.insert_cols", _genRowVecA);
		  
		  cout << "done." << endl;
      }
	  
  };
}
