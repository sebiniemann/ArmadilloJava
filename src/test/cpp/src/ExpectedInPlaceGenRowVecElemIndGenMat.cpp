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
using arma::Mat;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedInPlaceGenRowVecElemIndGenMat : public Expected {
    public:
      ExpectedInPlaceGenRowVecElemIndGenMat() {
        cout << "Compute ExpectedInPlaceGenRowVecElemIndGenDouble(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::GenRowVec,
            InputClass::ElemInd,
            InputClass::GenMat
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
                  _genMat = *static_cast<Mat<double>*>(value.second);
                  break;
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            _copyOfGenRowVec = _genRowVec;
            _copyOfElemInd = _elemInd;
            _copyOfGenMat = _genMat;
			  
			expectedRowVecColEqual();
			  
			_genRowVec = _copyOfGenRowVec;
			_elemInd = _copyOfElemInd;
			_genMat = _copyOfGenMat;
			  
			expectedRowVecColPlus();
			  
			_genRowVec = _copyOfGenRowVec;
			_elemInd = _copyOfElemInd;
			_genMat = _copyOfGenMat;
			  
			expectedRowVecColMinus();
			  
			_genRowVec = _copyOfGenRowVec;
			_elemInd = _copyOfElemInd;
			_genMat = _copyOfGenMat;
			  
			expectedRowVecInsertCols();
			  
          }

          cout << "done." << endl;
        }

    protected:
      Row<double> _genRowVec;
      Row<double> _copyOfGenRowVec;

      int _elemInd;
      int _copyOfElemInd;

      Mat<double> _genMat;
      Mat<double> _copyOfGenMat;

	  void expectedRowVecColEqual() {
		  if(_elemInd >= _genRowVec.n_elem) {
			  return;
		  }
		  
		  if(!_genMat.is_colvec() || _genRowVec.n_rows != _genMat.n_rows) {
			  return;
		  }
		  
		  cout << "- Compute expectedRowColEqual() ... ";
		  
		  _genRowVec.col(_elemInd) = _genMat;
		  save<double>("Row.colEqual", _genRowVec);
		  
		  cout << "done." << endl;
      }
	  
      void expectedRowVecColPlus() {
		  if(_elemInd >= _genRowVec.n_elem) {
			  return;
		  }
		  
		  if(!_genMat.is_colvec() || _genRowVec.n_rows != _genMat.n_rows) {
			  return;
		  }
		  
		  cout << "- Compute expectedRowColPlus() ... ";
		  
		  _genRowVec.col(_elemInd) += _genMat;
		  save<double>("Row.colPlus", _genRowVec);
		  
		  cout << "done." << endl;
      }
	  
      void expectedRowVecColMinus() {
		  if(_elemInd >= _genRowVec.n_elem) {
			  return;
		  }
		  
		  if(!_genMat.is_colvec() || _genRowVec.n_rows != _genMat.n_rows) {
			  return;
		  }
		  
		  cout << "- Compute expectedRowColMinus() ... ";
		  
		  _genRowVec.col(_elemInd) -= _genMat;
		  save<double>("Row.colMinus", _genRowVec);
		  
		  cout << "done." << endl;
      }
	  
	  
	  void expectedRowVecInsertCols() {
		  if(_elemInd >= _genRowVec.n_elem) {
			  return;
		  }
		  
		  if(!_genMat.is_colvec() || _genRowVec.n_rows != _genMat.n_rows) {
			  return;
		  }
		  
		  cout << "- Compute expectedRowInsertCols() ... ";
		  
		  _genRowVec.insert_cols(_elemInd, _genMat);
		  save<double>("Row.insertCols", _genRowVec);
		  
		  cout << "done." << endl;
      }
	  
  };
}
