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
using arma::Row;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedInPlaceGenColVecElemIndGenRowVec : public Expected {
    public:
      ExpectedInPlaceGenColVecElemIndGenRowVec() {
        cout << "Compute ExpectedInPlaceGenColVecElemIndGenRowVec(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::GenColVec,
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
                  _genColVec = *static_cast<Col<double>*>(value.second);
                  break;
                case 1:
                  _fileSuffix += "," + value.first;
                  _elemInd = *static_cast<int*>(value.second);
                  break;
                case 2:
                  _fileSuffix += "," + value.first;
                  _genRowVec = *static_cast<Row<double>*>(value.second);
                  break;
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            _copyOfGenColVec = _genColVec;
            _copyOfElemInd = _elemInd;
            _copyOfGenRowVec = _genRowVec;
			  
			expectedColVecRowEqual();
			  
			_genColVec = _copyOfGenColVec;
			_elemInd = _copyOfElemInd;
			_genRowVec = _copyOfGenRowVec;
			  
			expectedColVecRowPlus();
			  
			_genColVec = _copyOfGenColVec;
			_elemInd = _copyOfElemInd;
			_genRowVec = _copyOfGenRowVec;
			  
			expectedColVecRowMinus();
			  
			_genColVec = _copyOfGenColVec;
			_elemInd = _copyOfElemInd;
			_genRowVec = _copyOfGenRowVec;
			  
			expectedColVecInsertRows();
			  
          }

          cout << "done." << endl;
        }

    protected:
      Col<double> _genColVec;
      Col<double> _copyOfGenColVec;

      int _elemInd;
      int _copyOfElemInd;

      Row<double> _genRowVec;
      Row<double> _copyOfGenRowVec;

	  void expectedColVecRowEqual() {
		  if(_elemInd >= _genColVec.n_elem) {
			  return;
		  }
		  
		  if(_genColVec.n_cols != _genRowVec.n_cols) {
			  return;
		  }
		  
		  cout << "- Compute expectedColRowEqual() ... ";
		  
		  _genColVec.row(_elemInd) = _genRowVec;
		  save<double>("Col.rowEqual", _genColVec);
		  
		  cout << "done." << endl;
      }
	  
      void expectedColVecRowPlus() {
		  if(_elemInd >= _genColVec.n_elem) {
			  return;
		  }
		  
		  if(_genColVec.n_cols != _genRowVec.n_cols) {
			  return;
		  }
		  
		  cout << "- Compute expectedColRowPlus() ... ";
		  
		  _genColVec.row(_elemInd) += _genRowVec;
		  save<double>("Col.rowPlus", _genColVec);
		  
		  cout << "done." << endl;
      }
	  
      void expectedColVecRowMinus() {
		  if(_elemInd >= _genColVec.n_elem) {
			  return;
		  }
		  
		  if(_genColVec.n_cols != _genRowVec.n_cols) {
			  return;
		  }
		  
		  cout << "- Compute expectedColRowMinus() ... ";
		  
		  _genColVec.row(_elemInd) -= _genRowVec;
		  save<double>("Col.rowMinus", _genColVec);
		  
		  cout << "done." << endl;
      }
	  
	  
	  void expectedColVecInsertRows() {
		  if(_elemInd >= _genColVec.n_elem) {
			  return;
		  }
		  
		  if(_genColVec.n_cols != _genRowVec.n_cols) {
			  return;
		  }
		  
		  cout << "- Compute expectedColInsertRows() ... ";
		  
		  _genColVec.insert_rows(_elemInd, _genRowVec);
		  save<double>("Col.insertRows", _genColVec);
		  
		  cout << "done." << endl;
      }
	  
  };
}
