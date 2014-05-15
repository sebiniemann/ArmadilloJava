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

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedInPlaceGenMatExtRowIndNumRows: public Expected {
    public:
      ExpectedInPlaceGenMatExtRowIndNumRows() {
        cout << "Compute ExpectedInPlaceGenMatExtRowIndNumRows(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters( {
            InputClass::GenMat,
            InputClass::ExtRowInd,
            InputClass::NumRows
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
                _extRowInd = *static_cast<int*>(value.second);
                break;
              case 2:
                _fileSuffix += "," + value.first;
                _numRows = *static_cast<int*>(value.second);
                break;
            }
            ++n;
          }

          cout << "Using input: " << _fileSuffix << endl;

          _copyOfGenMat = _genMat;
          _copyOfExtRowInd = _extRowInd;
          _copyOfGenNumRows = _numRows;

          expectedMatInsert_rowsA();

          _genMat = _copyOfGenMat;
          _extRowInd = _copyOfExtRowInd;
          _numRows = _copyOfGenNumRows;
          expectedMatInsert_rowsB();
        }

        cout << "done." << endl;
      }

    protected:
      Mat<double> _genMat;
      Mat<double> _copyOfGenMat;

      int _extRowInd;
      int _copyOfExtRowInd;

      int _numRows;
      int _copyOfGenNumRows;

      void expectedMatInsert_rowsA() {
        if (_extRowInd > _genMat.n_rows) {
          return;
        }

        cout << "- Compute expectedMatInsert_rows() ... ";

        _genMat.insert_rows(_extRowInd, _numRows, true);
        save<double>("Mat.insert_rowsTrue", _genMat);

        cout << "done." << endl;
      }

      void expectedMatInsert_rowsB() {
        if (_extRowInd > _genMat.n_rows) {
          return;
        }

        cout << "- Compute expectedMatInsert_rows() ... ";

        _genMat.insert_rows(_extRowInd, _numRows, false);
        save<double>("Mat.insert_rowsFalse", _genMat);

        cout << "done." << endl;
      }
  };
}
