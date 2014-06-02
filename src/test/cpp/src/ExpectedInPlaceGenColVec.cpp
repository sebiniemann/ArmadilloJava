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
using arma::ones;
using arma::zeros;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
	class ExpectedInPlaceGenColVec : public Expected {
    public:
		ExpectedInPlaceGenColVec() {
			cout << "Compute ExpectedInPlaceGenColVec(): " << endl;
			
			vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
				InputClass::GenColVec
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
					}
					++n;
				}
				
				cout << "Using input: " << _fileSuffix << endl;
				

				expectedColOnes();
				expectedColZeros();
				expectedColReset();
				expectedColClear();
				expectedColInPlaceIncrement();
				expectedColInPlaceDecrement();
			}
			
			cout << "done." << endl;
		}
	protected:
		Col<double> _genColVec;

		void expectedColOnes() {
			cout << "- Compute expectedColOnes() ... ";
			Col<double> expected = _genColVec;
			save<double>("Col.ones", (expected.ones()));
			cout << "done." << endl;
		}
		
		void expectedColZeros() {
			cout << "- Compute expectedColZeros() ... ";
			Col<double> expected = _genColVec;
			save<double>("Col.zeros", (expected.zeros()));
			cout << "done." << endl;
		}
		
		void expectedColReset() {
			cout << "- Compute expectedColReset() ... ";
			Col<double> expected = _genColVec;
			expected.reset();
			save<double>("Col.reset", expected);
			cout << "done." << endl;
		}
		
		void expectedColClear() {
			cout << "- Compute expectedColClear() ... ";
			Col<double> expected = _genColVec;
			expected.clear();
			save<double>("Col.clear", expected);
			cout << "done." << endl;
		}
		
		void expectedColInPlaceIncrement() {
			cout << "- Compute expectedColInPlaceIncrement() ... ";
			Col<double> expected = _genColVec;
			expected++;
			save<double>("Col.inPlaceIncrement", expected);
			cout << "done." << endl;
		}
		
		void expectedColInPlaceDecrement() {
			cout << "- Compute expectedColInPlaceDecrement() ... ";
			Col<double> expected = _genColVec;
			expected--;
			save<double>("Col.inPlaceDecrement", expected);
			cout << "done." << endl;
		}
		
	
	};
}
