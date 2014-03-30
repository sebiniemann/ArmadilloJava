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
using arma::Col;
using arma::uword;
using arma::raw_ascii;
using arma::hist;
using arma::histc;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedGenRowVecMonColVec : public Expected {
    public:
      ExpectedGenRowVecMonColVec() {
        cout << "Compute ExpectedGenRowVecMonColVec(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({InputClass::GenRowVec, InputClass::MonColVec});

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
                _monColVec = *static_cast<Col<double>*>(value.second);
                break;
            }
            ++n;
          }

          cout << "Using input: " << _fileSuffix << endl;

          expectedHist();
          expectedHistc();
        }

        cout << "done." << endl;
      }

    protected:
      Row<double> _genRowVec;
      Col<double> _monColVec;

      void expectedHist() {
        cout << "- Compute expectedHist() ... ";
        // Unable to convert the result of hist(...) to Mat<double>
        Mat<uword> expected = hist(_genRowVec, _monColVec);
        expected.save("../data/expected/hist" + _fileSuffix + ".mat", raw_ascii);
        cout << "done." << endl;
      }

      void expectedHistc() {
        cout << "- Compute expectedHistc() ... ";
        // Unable to convert the result of histc(...) to Mat<double>
        Mat<uword> expected = histc(_genRowVec, _monColVec);
        expected.save("../data/expected/histc" + _fileSuffix + ".mat", raw_ascii);
        cout << "done." << endl;
      }

  };
}
