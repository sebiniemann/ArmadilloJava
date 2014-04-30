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
using arma::Mat;
using arma::diagvec;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedGenMatRowInd : public Expected {
    public:
      ExpectedGenMatRowInd() {
        cout << "Compute ExpectedGenMatRowInd(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::GenMat,
            InputClass::RowInd
          });

          for (vector<pair<string, void*>> input : inputs) {
            _fileSuffix = "";

            int n = 0;
            for (pair<string, void*> value : input) {
              switch (n) {
                case 0:
                  _fileSuffix += value.first;
                  _genMat = *static_cast<Mat<double>*>(value.second);
                  break;
                case 1:
                  _fileSuffix += "," + value.first;
                  _rowInd = *static_cast<int*>(value.second);
                  break;
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            expectedArmaDiagvec();
            expectedMatRow();
          }

          cout << "done." << endl;
        }

    protected:
      Mat<double> _genMat;
      int _rowInd;

      void expectedArmaDiagvec() {
        if(_rowInd >= _genMat.n_rows) {
          return;
        }

        cout << "- Compute expectedArmaDiagvec() ... ";
        save<double>("Arma.diagvecSub", diagvec(_genMat, -_rowInd));
        cout << "done." << endl;
      }

      void expectedMatRow() {
        if(_rowInd >= _genMat.n_rows) {
          return;
        }

        cout << "- Compute expectedMatRow() ... ";
        save<double>("Mat.row", _genMat.row(_rowInd));
        cout << "done." << endl;
      }
  };
}
