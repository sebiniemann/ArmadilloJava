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
  class ExpectedInPlaceGenMatRowIndGenDouble : public Expected {
    public:
      ExpectedInPlaceGenMatRowIndGenDouble() {
        cout << "Compute ExpectedInPlaceGenMatRowIndGenDouble(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::GenMat,
            InputClass::RowInd,
            InputClass::GenDouble
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
                case 2:
                  _fileSuffix += "," + value.first;
                  _genDouble = *static_cast<double*>(value.second);
                  break;
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            _copyOfGenMat = _genMat;
            _copyOfRowInd = _rowInd;
            _copyOfGenDouble = _genDouble;

            expectedMatColPlus();

            _genMat = _copyOfGenMat;
            _rowInd = _copyOfRowInd;
            _genDouble = _copyOfGenDouble;
            expectedMatColMinus();

            _genMat = _copyOfGenMat;
            _rowInd = _copyOfRowInd;
            _genDouble = _copyOfGenDouble;
            expectedMatColTimes();

            _genMat = _copyOfGenMat;
            _rowInd = _copyOfRowInd;
            _genDouble = _copyOfGenDouble;
            expectedMatColDivide();
          }

          cout << "done." << endl;
        }

    protected:
      Mat<double> _genMat;
      Mat<double> _copyOfGenMat;

      int _rowInd;
      int _copyOfRowInd;

      double _genDouble;
      double _copyOfGenDouble;

      void expectedMatColPlus() {
        if(_rowInd >= _genMat.n_rows) {
          return;
        }

        cout << "- Compute expectedMatColPlus() ... ";

        _genMat.row(_rowInd) += _genDouble;
        save<double>("Mat.rowPlus", _genMat);

        cout << "done." << endl;
      }

      void expectedMatColMinus() {
        if(_rowInd >= _genMat.n_rows) {
          return;
        }

        cout << "- Compute expectedMatColMinus() ... ";

        _genMat.row(_rowInd) -= _genDouble;
        save<double>("Mat.rowMinus", _genMat);

        cout << "done." << endl;
      }

      void expectedMatColTimes() {
        if(_rowInd >= _genMat.n_rows) {
          return;
        }

        cout << "- Compute expectedMatColTimes() ... ";

        _genMat.row(_rowInd) *= _genDouble;
        save<double>("Mat.rowTimes", _genMat);

        cout << "done." << endl;
      }

      void expectedMatColDivide() {
        if(_rowInd >= _genMat.n_rows) {
          return;
        }

        cout << "- Compute expectedMatColDivide() ... ";

        _genMat.row(_rowInd) /= _genDouble;
        save<double>("Mat.rowDivide", _genMat);

        cout << "done." << endl;
      }
  };
}
