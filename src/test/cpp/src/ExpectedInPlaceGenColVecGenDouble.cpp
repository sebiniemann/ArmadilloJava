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
  class ExpectedInPlaceGenColVecGenDouble : public Expected {
    public:
      ExpectedInPlaceGenColVecGenDouble() {
        cout << "Compute ExpectedInPlaceGenColVecGenDouble(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::GenColVec,
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
                  _genDouble = *static_cast<double*>(value.second);
                  break;
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            _copyOfGenColVec = _genColVec;
            _copyOfGenDouble = _genDouble;

			expectedColVecFill();

			_genColVec = _copyOfGenColVec;
		    _genDouble = _copyOfGenDouble;
            expectedColInPlacePlus();

            _genColVec = _copyOfGenColVec;
            _genDouble = _copyOfGenDouble;
            expectedColInPlaceMinus();

			_genColVec = _copyOfGenColVec;
			_genDouble = _copyOfGenDouble;
			expectedColInPlaceTimes();

			_genColVec = _copyOfGenColVec;
            _genDouble = _copyOfGenDouble;
            expectedColInPlaceDivide();
                    
		  }

          cout << "done." << endl;
        }

    protected:
      Col<double> _genColVec;
      Col<double> _copyOfGenColVec;

      double _genDouble;
      double _copyOfGenDouble;

      void expectedColVecFill() {
        cout << "- Compute expectedColVecFill() ... ";

        _genColVec.fill(_genDouble);
        save<double>("Col.fill", _genColVec);

        cout << "done." << endl;
      }

      void expectedColInPlacePlus() {
        cout << "- Compute expectedColInPlacePlus() ... ";

        _genColVec += _genDouble;
        save<double>("Col.inPlacePlus", _genColVec);

        cout << "done." << endl;
      }

      void expectedColInPlaceMinus() {
        cout << "- Compute expectedColInPlaceMinus() ... ";

        _genColVec -= _genDouble;
        save<double>("Col.inPlaceMinus", _genColVec);

        cout << "done." << endl;
      }

      void expectedColInPlaceTimes() {
        cout << "- Compute expectedColInPlaceTimes() ... ";

        _genColVec *= _genDouble;
        save<double>("Col.inPlaceTimes", _genColVec);

        cout << "done." << endl;
      }

      void expectedColInPlaceDivide() {
        cout << "- Compute expectedColInPlaceDivide() ... ";

        _genColVec /= _genDouble;
        save<double>("Col.inPlaceDivide", _genColVec);

        cout << "done." << endl;
            }
	  
  };
}
