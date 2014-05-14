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
using arma::Col;
using arma::uword;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedInPlaceGenMatRowIndsGenDouble : public Expected {
    public:
      ExpectedInPlaceGenMatRowIndsGenDouble() {
        cout << "Compute ExpectedInPlaceGenMatRowIndsGenDouble(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
          InputClass::GenMat,
          InputClass::RowInds,
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
                _rowInds = *static_cast<Col<uword>*>(value.second);
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
          _copyOfRowInds = _rowInds;
          _copyOfGenDouble = _genDouble;

          expectedRowsPlus();

          _genMat = _copyOfGenMat;
          _rowInds = _copyOfRowInds;
          _genDouble = _copyOfGenDouble;
          expectedRowsMinus();

          _genMat = _copyOfGenMat;
          _rowInds = _copyOfRowInds;
          _genDouble = _copyOfGenDouble;
          expectedRowsTimes();

          _genMat = _copyOfGenMat;
          _rowInds = _copyOfRowInds;
          _genDouble = _copyOfGenDouble;
          expectedRowsDivide();
        }

        cout << "done." << endl;
      }

    protected:
      Mat<double> _genMat;
      Mat<double> _copyOfGenMat;

      Col<uword> _rowInds;
      Col<uword> _copyOfRowInds;

      double _genDouble;
      double _copyOfGenDouble;

      void expectedRowsPlus() {
        for(int i = 0; i < _rowInds.n_elem; i++) {
          if(_rowInds.at(i) >= _genMat.n_rows) {
            return;
          }
        }

        cout << "- Compute expectedRowsPlus() ... ";

        _genMat.rows(_rowInds) += _genDouble;
        save<double>("Mat.rowsPlus", _genMat);

        cout << "done." << endl;
      }

      void expectedRowsMinus() {
        for(int i = 0; i < _rowInds.n_elem; i++) {
          if(_rowInds.at(i) >= _genMat.n_rows) {
            return;
          }
        }

        cout << "- Compute expectedRowsMinus() ... ";

        _genMat.rows(_rowInds) -= _genDouble;
        save<double>("Mat.rowsMinus", _genMat);

        cout << "done." << endl;
      }

      void expectedRowsTimes() {
        for(int i = 0; i < _rowInds.n_elem; i++) {
          if(_rowInds.at(i) >= _genMat.n_rows) {
            return;
          }
        }

        cout << "- Compute expectedRowsTimes() ... ";

        _genMat.rows(_rowInds) *= _genDouble;
        save<double>("Mat.rowsTimes", _genMat);

        cout << "done." << endl;
      }

      void expectedRowsDivide() {
        for(int i = 0; i < _rowInds.n_elem; i++) {
          if(_rowInds.at(i) >= _genMat.n_rows) {
            return;
          }
        }

        cout << "- Compute expectedRowsDivide() ... ";

        _genMat.rows(_rowInds) /= _genDouble;
        save<double>("Mat.rowsDivide", _genMat);

        cout << "done." << endl;
      }

  };
}
