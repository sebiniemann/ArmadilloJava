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
  class ExpectedInPlaceGenColVecElemIndGenColVec : public Expected {
    public:
      ExpectedInPlaceGenColVecElemIndGenColVec() {
        cout << "Compute ExpectedInPlaceGenColVecElemIndGenColVec(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::GenColVec,
            InputClass::ElemInd,
            InputClass::GenColVec
          });

          for (vector<pair<string, void*>> input : inputs) {
            _fileSuffix = "";

            int n = 0;
            for (pair<string, void*> value : input) {
              switch (n) {
                case 0:
                  _fileSuffix += value.first;
                  _genColVecA = *static_cast<Col<double>*>(value.second);
                  break;
                case 1:
                  _fileSuffix += "," + value.first;
                  _elemInd = *static_cast<int*>(value.second);
                  break;
                case 2:
                  _fileSuffix += "," + value.first;
                  _genColVecB = *static_cast<Col<double>*>(value.second);
                  break;
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            _copyOfGenColVecA = _genColVecA;
            _copyOfElemInd = _elemInd;
            _copyOfGenColVecB = _genColVecB;
			  
			expectedColVecRowEqual();
			  
			_genColVecA = _copyOfGenColVecA;
			_elemInd = _copyOfElemInd;
			_genColVecB = _copyOfGenColVecB;
			  
			expectedColVecRowPlus();
			  
			_genColVecA = _copyOfGenColVecA;
			_elemInd = _copyOfElemInd;
			_genColVecB = _copyOfGenColVecB;
			  
			expectedColVecRowMinus();
			  
			_genColVecA = _copyOfGenColVecA;
			_elemInd = _copyOfElemInd;
			_genColVecB = _copyOfGenColVecB;
			  
			expectedColVecInsertRows();
			  
          }

          cout << "done." << endl;
        }

    protected:
      Col<double> _genColVecA;
      Col<double> _copyOfGenColVecA;

      int _elemInd;
      int _copyOfElemInd;

      Col<double> _genColVecB;
      Col<double> _copyOfGenColVecB;

	  void expectedColVecRowEqual() {
		  if(_elemInd >= _genColVecA.n_elem) {
			  return;
		  }
		  
		  if(!_genColVecB.is_rowvec() || _genColVecA.n_cols != _genColVecB.n_cols) {
			  return;
		  }
		  
		  cout << "- Compute expectedColRowEqual() ... ";
		  
		  _genColVecA.row(_elemInd) = _genColVecB;
		  save<double>("Col.rowEqual", _genColVecA);
		  
		  cout << "done." << endl;
      }
	  
      void expectedColVecRowPlus() {
		  if(_elemInd >= _genColVecA.n_elem) {
			  return;
		  }
		  
		  if(!_genColVecB.is_rowvec() || _genColVecA.n_cols != _genColVecB.n_cols) {
			  return;
		  }
		  
		  cout << "- Compute expectedColRowPlus() ... ";
		  
		  _genColVecA.row(_elemInd) += _genColVecB;
		  save<double>("Col.rowPlus", _genColVecA);
		  
		  cout << "done." << endl;
      }
	  
      void expectedColVecRowMinus() {
		  if(_elemInd >= _genColVecA.n_elem) {
			  return;
		  }
		  
		  if(!_genColVecB.is_rowvec() || _genColVecA.n_cols != _genColVecB.n_cols) {
			  return;
		  }
		  
		  cout << "- Compute expectedColRowMinus() ... ";
		  
		  _genColVecA.row(_elemInd) -= _genColVecB;
		  save<double>("Col.rowMinus", _genColVecA);
		  
		  cout << "done." << endl;
      }
	  
	  
	  void expectedColVecInsertRows() {
		  if(_elemInd >= _genColVecA.n_elem) {
			  return;
		  }
		  
		  if(!_genColVecB.is_rowvec() || _genColVecA.n_cols != _genColVecB.n_cols) {
			  return;
		  }
		  
		  cout << "- Compute expectedColInsertRows() ... ";
		  
		  _genColVecA.insert_rows(_elemInd, _genColVecB);
		  save<double>("Col.insertRows", _genColVecA);
		  
		  cout << "done." << endl;
      }
	  
  };
}
