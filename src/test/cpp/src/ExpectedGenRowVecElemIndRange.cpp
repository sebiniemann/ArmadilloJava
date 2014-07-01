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
using arma::span;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
	class ExpectedGenRowVecElemIndRange : public Expected {
    public:
		ExpectedGenRowVecElemIndRange() {
			cout << "Compute ExpectedGenRowElemIndRange(): " << endl;
			
			vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
				InputClass::GenRowVec,
				InputClass::ElemIndRange
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
							_elemIndRange = *static_cast<span*>(value.second);
							break;
					}
					++n;
				}
				
				cout << "Using input: " << _fileSuffix << endl;
				
				expectedRowVecIn_range();
				expectedRowVecCols();
				expectedRowVecSubvec();
			}
			
			cout << "done." << endl;
        }
		
    protected:
		Row<double> _genRowVec;
		span _elemIndRange;
		
		void expectedRowVecIn_range() {
			cout << "- Compute expectedRowIn_range() ... ";
			
			if(_genRowVec.in_range(_elemIndRange)) {
				save<double>("Row.in_range", Row<double>({1}));
			} else {
				save<double>("Row.in_range", Row<double>({0}));
			}
			
			cout << "done." << endl;
		}

		void expectedRowVecCols() {
            if(_elemIndRange.whole) {
              return;
            }

			cout << "- Compute expectedRowCols() ... ";

			if(_genRowVec.in_range(_elemIndRange)) {
				save<double>("Row.cols", _genRowVec.cols(_elemIndRange.a, _elemIndRange.b));
			}

			cout << "done." << endl;
		}

		void expectedRowVecSubvec() {
            if(_elemIndRange.whole) {
              return;
            }

			cout << "- Compute expectedRowSubvec() ... ";

			if(_genRowVec.in_range(_elemIndRange)) {
				save<double>("Row.subvec", _genRowVec.subvec(_elemIndRange.a, _elemIndRange.b));
			}

			cout << "done." << endl;
		}
	};
}

