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
using arma::span;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedInPlaceGenColVecElemIndRangeGenDouble : public Expected {
    public:
      ExpectedInPlaceGenColVecElemIndRangeGenDouble() {
        cout << "Compute ExpectedInPlaceGenColVecElemIndRangeGenDouble(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::GenColVec,
            InputClass::ElemIndRange,
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
                  _elemIndRange = *static_cast<span*>(value.second);
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
            _copyOfElemIndRange = _elemIndRange;
            _copyOfGenDouble = _genDouble;

            
			expectedColVecRowsPlus();

            _genColVec = _copyOfGenColVec;
            _elemIndRange = _copyOfElemIndRange;
            _genDouble = _copyOfGenDouble;
            
			expectedColVecRowsMinus();

            _genColVec = _copyOfGenColVec;
            _elemIndRange = _copyOfElemIndRange;
            _genDouble = _copyOfGenDouble;
            
			expectedColVecRowsTimes();

            _genColVec = _copyOfGenColVec;
            _elemIndRange = _copyOfElemIndRange;
            _genDouble = _copyOfGenDouble;
            
			expectedColVecRowsDivide();

			_genColVec = _copyOfGenColVec;
			_elemIndRange = _copyOfElemIndRange;
			_genDouble = _copyOfGenDouble;

			expectedColVecSubvecPlus();

			_genColVec = _copyOfGenColVec;
			_elemIndRange = _copyOfElemIndRange;
			_genDouble = _copyOfGenDouble;

			expectedColVecSubvecMinus();

			_genColVec = _copyOfGenColVec;
			_elemIndRange = _copyOfElemIndRange;
			_genDouble = _copyOfGenDouble;

			expectedColVecSubvecTimes();
			  
			_genColVec = _copyOfGenColVec;
			_elemIndRange = _copyOfElemIndRange;
			_genDouble = _copyOfGenDouble;
			  
			expectedColVecSubvecDivide();

			_genColVec = _copyOfGenColVec;
			_elemIndRange = _copyOfElemIndRange;
			_genDouble = _copyOfGenDouble;

			expectedColVecSubvecSpanPlus();
			  
			_genColVec = _copyOfGenColVec;
			_elemIndRange = _copyOfElemIndRange;
			_genDouble = _copyOfGenDouble;
			  
			expectedColVecSubvecSpanMinus();
			  
			_genColVec = _copyOfGenColVec;
			_elemIndRange = _copyOfElemIndRange;
			_genDouble = _copyOfGenDouble;
			  
			expectedColVecSubvecSpanTimes();
			  
			_genColVec = _copyOfGenColVec;
			_elemIndRange = _copyOfElemIndRange;
			_genDouble = _copyOfGenDouble;
			  
			expectedColVecSubvecSpanDivide();
          }

          cout << "done." << endl;
        }

    protected:
      Col<double> _genColVec;
      Col<double> _copyOfGenColVec;

      span _elemIndRange;
      span _copyOfElemIndRange;

      double _genDouble;
      double _copyOfGenDouble;


      void expectedColVecRowsPlus() {
        if(!_genColVec.in_range(_elemIndRange)) {
          return;
        }
        cout << "- Compute expectedColVecRowsPlus() ... ";

        _genColVec.rows(_elemIndRange.a, _elemIndRange.b) += _genDouble;

        save<double>("Col.rowsPlus", _genColVec);
        cout << "done." << endl;
      }

      void expectedColVecRowsMinus() {
        if(!_genColVec.in_range(_elemIndRange)) {
          return;
        }
        cout << "- Compute expectedColVecRowsMinus() ... ";

        _genColVec.rows(_elemIndRange.a,_elemIndRange.b) -= _genDouble;

        save<double>("Col.rowsMinus", _genColVec);
        cout << "done." << endl;
      }

      void expectedColVecRowsTimes() {
        if(!_genColVec.in_range(_elemIndRange)) {
          return;
        }

        cout << "- Compute expectedColVecRowsTimes() ... ";

        _genColVec.rows(_elemIndRange.a, _elemIndRange.b) *= _genDouble;

        save<double>("Col.rowsTimes", _genColVec);
        cout << "done." << endl;
      }

      void expectedColVecRowsDivide() {
        if(!_genColVec.in_range(_elemIndRange)) {
          return;
        }

        cout << "- Compute expectedColVecRowsDivide() ... ";

        _genColVec.rows(_elemIndRange.a, _elemIndRange.b) /= _genDouble;

        save<double>("Col.rowsDivide", _genColVec);
        cout << "done." << endl;
      }

      void expectedColVecSubvecPlus() {
		  if(!_genColVec.in_range(_elemIndRange)) {
			  return;
		  }
		  
		  cout << "- Compute expectedColVecSubvecPlus() ... ";

		  _genColVec.subvec(_elemIndRange.a, _elemIndRange.b) += _genDouble;
		  
		  save<double>("Col.subvecPlus", _genColVec);
		  cout << "done." << endl;
      }
	  
      void expectedColVecSubvecMinus() {
		  if(!_genColVec.in_range(_elemIndRange)) {
			  return;
		  }
		  
		  cout << "- Compute expectedColVecSubvecMinus() ... ";

		  _genColVec.subvec(_elemIndRange.a, _elemIndRange.b) -= _genDouble;
		  

		  save<double>("Col.subvecMinus", _genColVec);
		  cout << "done." << endl;
      }
	  
      void expectedColVecSubvecTimes() {
		  if(!_genColVec.in_range(_elemIndRange)) {
			  return;
		  }
		  
		  cout << "- Compute expectedColVecSubvecTimes() ... ";

		  _genColVec.subvec(_elemIndRange.a, _elemIndRange.b) *= _genDouble;
		  
		  save<double>("Col.subvecTimes", _genColVec);
		  cout << "done." << endl;
      }
	  
      void expectedColVecSubvecDivide() {
		  if(!_genColVec.in_range(_elemIndRange)) {
			  return;
		  }
		  
		  cout << "- Compute expectedColVecSubvecDivide() ... ";

		  _genColVec.subvec(_elemIndRange.a, _elemIndRange.b) /= _genDouble;
		  

		  save<double>("Col.subvecDivide", _genColVec);
		  cout << "done." << endl;
      }

       void expectedColVecSubvecSpanPlus() {
 		  if(!_genColVec.in_range(_elemIndRange)) {
 			  return;
 		  }

 		  cout << "- Compute expectedColVecSubvecSpanPlus() ... ";

 		  _genColVec.subvec(_elemIndRange) += _genDouble;

 		  save<double>("Col.subvecSpanPlus", _genColVec);
 		  cout << "done." << endl;
       }

       void expectedColVecSubvecSpanMinus() {
 		  if(!_genColVec.in_range(_elemIndRange)) {
 			  return;
 		  }

 		  cout << "- Compute expectedColVecSubvecSpanMinus() ... ";

 		  _genColVec.subvec(_elemIndRange) -= _genDouble;

 		  save<double>("Col.subvecSpanMinus", _genColVec);
 		  cout << "done." << endl;
       }

       void expectedColVecSubvecSpanTimes() {
 		  if(!_genColVec.in_range(_elemIndRange)) {
 			  return;
 		  }

		  cout << "- Compute expectedColVecSubvecSpanTimes() ... ";

 		  _genColVec.subvec(_elemIndRange) *= _genDouble;

 		  save<double>("Col.subvecSpanTimes", _genColVec);
 		  cout << "done." << endl;
       }

       void expectedColVecSubvecSpanDivide() {
 		  if(!_genColVec.in_range(_elemIndRange)) {
 			  return;
 		  }

 		  cout << "- Compute expectedColVecSubvecSpanDivide() ... ";

 		  _genColVec.subvec(_elemIndRange) /= _genDouble;

 		  save<double>("Col.subvecSpanDivide", _genColVec);
 		  cout << "done." << endl;
       }
	  
  };
}
