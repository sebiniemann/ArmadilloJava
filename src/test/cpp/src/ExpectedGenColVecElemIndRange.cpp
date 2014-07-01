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
	class ExpectedGenColVecElemIndRange : public Expected {
    public:
		ExpectedGenColVecElemIndRange() {
			cout << "Compute ExpectedGenColElemIndRange(): " << endl;
			
			vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
				InputClass::GenColVec,
				InputClass::ElemIndRange
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
					}
					++n;
				}
				
				cout << "Using input: " << _fileSuffix << endl;
				
				expectedColVecIn_range();
				expectedColVecRows();
				expectedColVecSubvec();
			}
			
			cout << "done." << endl;
        }
		
    protected:
		Col<double> _genColVec;
		span _elemIndRange;
		
		void expectedColVecIn_range() {
			cout << "- Compute expectedColIn_range() ... ";
			
			if(_genColVec.in_range(_elemIndRange)) {
				save<double>("Col.in_range", Col<double>({1}));
			} else {
				save<double>("Col.in_range", Col<double>({0}));
			}
			
			cout << "done." << endl;
		}

		void expectedColVecRows() {
            if(_elemIndRange.whole) {
              return;
            }

			cout << "- Compute expectedColRows() ... ";

			if(_genColVec.in_range(_elemIndRange)) {
				save<double>("Col.rows", _genColVec.rows(_elemIndRange.a, _elemIndRange.b));
			}

			cout << "done." << endl;
		}

		void expectedColVecSubvec() {
            if(_elemIndRange.whole) {
              return;
            }

			cout << "- Compute expectedColSubvec() ... ";

			if(_genColVec.in_range(_elemIndRange)) {
				save<double>("Col.subvec", _genColVec.subvec(_elemIndRange.a, _elemIndRange.b));
			}

			cout << "done." << endl;
		}
	};
}

