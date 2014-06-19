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
using arma::Row;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedGenRowVecElemInd : public Expected {
    public:
      ExpectedGenRowVecElemInd() {
        cout << "Compute ExpectedGenRowVecElemInd(): " << endl;

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

            expectedRowVecAt();
            expectedRowVecIn_range();
			expectedRowVecCol();
          }

          cout << "done." << endl;
        }

    protected:
      Row<double> _genRowVec;
      int _elemInd;

      void expectedRowVecAt() {
        if(_elemInd >= _genRowVec.n_elem) {
          return;
        }

        cout << "- Compute expectedRowVecAt() ... ";
        save<double>("Row.at", Row<double>({_genRowVec.at(_elemInd)}));
        cout << "done." << endl;
      }

      void expectedRowVecIn_range() {
        cout << "- Compute expectedRowVecIn_range() ... ";

        if(_genRowVec.in_range(_elemInd)) {
          save<double>("Row.in_range", Row<double>({1}));
        } else {
          save<double>("Row.in_range", Row<double>({0}));
        }

        cout << "done." << endl;
      }
	  
	  void expectedRowVecCol() {
		  if(_elemInd >= _genRowVec.n_cols) {
			  return;
		  }
		  Row<double> expected;
		  expected = _genRowVec.col(_elemInd);
		  
		  cout << "- Compute expectedRowVecCol() ... ";
		  save<double>("Row.col", Row<double>(expected));
		  cout << "done." << endl;
      }
  };
}
