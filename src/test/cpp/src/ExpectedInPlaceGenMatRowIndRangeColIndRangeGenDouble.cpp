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
using arma::span;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedInPlaceGenMatRowIndRangeColIndRangeGenDouble : public Expected {
    public:
      ExpectedInPlaceGenMatRowIndRangeColIndRangeGenDouble() {
        cout << "Compute ExpectedInPlaceGenMatRowIndRangeColIndRangeGenDouble(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::GenMat,
            InputClass::RowIndRange,
            InputClass::ColIndRange,
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
                  _rowIndRange = *static_cast<span*>(value.second);
                  break;
                case 2:
                  _fileSuffix += "," + value.first;
                  _colIndRange = *static_cast<span*>(value.second);
                  break;
                case 3:
                  _fileSuffix += "," + value.first;
                  _genDouble = *static_cast<double*>(value.second);
                  break;
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            _copyOfGenMatA = _genMat;
            _copyOfRowIndRange = _rowIndRange;
            _copyOfGenMatB = _colIndRange;
            _copyOfGenDouble = _genDouble;

            expectedMatSubmatPlus();

            _genMat = _copyOfGenMatA;
            _rowIndRange = _copyOfRowIndRange;
            _colIndRange = _copyOfGenMatB;
            _genDouble = _copyOfGenDouble;
            expectedMatSubmatMinus();

            _genMat = _copyOfGenMatA;
            _rowIndRange = _copyOfRowIndRange;
            _colIndRange = _copyOfGenMatB;
            _genDouble = _copyOfGenDouble;
            expectedMatSubmatTimes();

            _genMat = _copyOfGenMatA;
            _rowIndRange = _copyOfRowIndRange;
            _colIndRange = _copyOfGenMatB;
            _genDouble = _copyOfGenDouble;
            expectedMatSubmatDivide();
          }

          cout << "done." << endl;
        }

    protected:
      Mat<double> _genMat;
      Mat<double> _copyOfGenMatA;

      span _rowIndRange;
      span _copyOfRowIndRange;

      span _colIndRange;
      span _copyOfGenMatB;

      double _genDouble;
      double _copyOfGenDouble;

      void expectedMatSubmatPlus() {
        if(!_genMat.in_range(_rowIndRange, _colIndRange)) {
          return;
        }

        cout << "- Compute expectedMatSubmatPlus() ... ";

        _genMat.submat(_rowIndRange, _colIndRange) += _genDouble;
        save<double>("Mat.submatPlus", _genMat);

        cout << "done." << endl;
      }

      void expectedMatSubmatMinus() {
        if(!_genMat.in_range(_rowIndRange, _colIndRange)) {
          return;
        }

        cout << "- Compute expectedMatSubmatMinus() ... ";

        _genMat.submat(_rowIndRange, _colIndRange) -= _genDouble;
        save<double>("Mat.submatMinus", _genMat);

        cout << "done." << endl;
      }

      void expectedMatSubmatTimes() {
        if(!_genMat.in_range(_rowIndRange, _colIndRange)) {
          return;
        }

        cout << "- Compute expectedMatSubmatTimes() ... ";

        _genMat.submat(_rowIndRange, _colIndRange) *= _genDouble;
        save<double>("Mat.submatTimes", _genMat);

        cout << "done." << endl;
      }

      void expectedMatSubmatDivide() {
        if(!_genMat.in_range(_rowIndRange, _colIndRange)) {
          return;
        }

        cout << "- Compute expectedMatSubmatDivide() ... ";

        _genMat.submat(_rowIndRange, _colIndRange) /= _genDouble;
        save<double>("Mat.submatDivide", _genMat);

        cout << "done." << endl;
      }
  };
}
