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
using arma::ones;
using arma::zeros;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
	class ExpectedInPlaceGenRowVec : public Expected {
    public:
		ExpectedInPlaceGenRowVec() {
			cout << "Compute ExpectedInPlaceGenRowVec(): " << endl;
			
			vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
				InputClass::GenRowVec
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
					}
					++n;
				}
				
				cout << "Using input: " << _fileSuffix << endl;
				

				expectedRowOnes();
				expectedRowZeros();
				expectedRowInPlaceIncrement();
				expectedRowInPlaceDecrement();
			}
			
			cout << "done." << endl;
		}
	protected:
		Row<double> _genRowVec;

		void expectedRowOnes() {
			cout << "- Compute expectedRowOnes() ... ";
			Row<double> expected = _genRowVec;
			save<double>("Row.ones", (expected.ones()));
			cout << "done." << endl;
		}
		
		void expectedRowZeros() {
			cout << "- Compute expectedRowZeros() ... ";
			Row<double> expected = _genRowVec;
			save<double>("Row.zeros", (expected.zeros()));
			cout << "done." << endl;
		}
		
		void expectedRowInPlaceIncrement() {
			cout << "- Compute expectedRowInPlaceIncrement() ... ";
			Row<double> expected = _genRowVec;
			expected++;
			save<double>("Row.inPlaceIncrement", expected);
			cout << "done." << endl;
		}
		
		void expectedRowInPlaceDecrement() {
			cout << "- Compute expectedRowInPlaceDecrement() ... ";
			Row<double> expected = _genRowVec;
			expected--;
			save<double>("Row.inPlaceDecrement", expected);
			cout << "done." << endl;
		}
		
	
	};
}
