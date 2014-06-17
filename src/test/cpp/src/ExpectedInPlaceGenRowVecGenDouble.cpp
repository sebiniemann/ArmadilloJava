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
  class ExpectedInPlaceGenRowVecGenDouble : public Expected {
    public:
      ExpectedInPlaceGenRowVecGenDouble() {
        cout << "Compute ExpectedInPlaceGenRowVecGenDouble(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::GenRowVec,
            InputClass::GenDouble
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
                  _genDouble = *static_cast<double*>(value.second);
                  break;
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            _copyOfGenRowVec = _genRowVec;
            _copyOfGenDouble = _genDouble;

			expectedRowVecFill();

			_genRowVec = _copyOfGenRowVec;
		    _genDouble = _copyOfGenDouble;
            expectedRowInPlacePlus();

            _genRowVec = _copyOfGenRowVec;
            _genDouble = _copyOfGenDouble;
            expectedRowInPlaceMinus();

			_genRowVec = _copyOfGenRowVec;
			_genDouble = _copyOfGenDouble;
			expectedRowInPlaceTimes();

			_genRowVec = _copyOfGenRowVec;
            _genDouble = _copyOfGenDouble;
            expectedRowInPlaceDivide();
                    
		  }

          cout << "done." << endl;
        }

    protected:
      Row<double> _genRowVec;
      Row<double> _copyOfGenRowVec;

      double _genDouble;
      double _copyOfGenDouble;

      void expectedRowVecFill() {
        cout << "- Compute expectedRowVecFill() ... ";

        _genRowVec.fill(_genDouble);
        save<double>("Row.fill", _genRowVec);

        cout << "done." << endl;
      }

      void expectedRowInPlacePlus() {
        cout << "- Compute expectedRowInPlacePlus() ... ";

        _genRowVec += _genDouble;
        save<double>("Row.inPlacePlus", _genRowVec);

        cout << "done." << endl;
      }

      void expectedRowInPlaceMinus() {
        cout << "- Compute expectedRowInPlaceMinus() ... ";

        _genRowVec -= _genDouble;
        save<double>("Row.inPlaceMinus", _genRowVec);

        cout << "done." << endl;
      }

      void expectedRowInPlaceTimes() {
        cout << "- Compute expectedRowInPlaceTimes() ... ";

        _genRowVec *= _genDouble;
        save<double>("Row.inPlaceTimes", _genRowVec);

        cout << "done." << endl;
      }

      void expectedRowInPlaceDivide() {
        cout << "- Compute expectedRowInPlaceDivide() ... ";

        _genRowVec /= _genDouble;
        save<double>("Row.inPlaceDivide", _genRowVec);

        cout << "done." << endl;
            }
	  
  };
}
