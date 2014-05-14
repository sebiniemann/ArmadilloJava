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
  class ExpectedInPlaceGenMatColIndsGenDouble : public Expected {
    public:
      ExpectedInPlaceGenMatColIndsGenDouble() {
        cout << "Compute ExpectedInPlaceGenMatColIndsGenDouble(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
          InputClass::GenMat,
          InputClass::ColInds,
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
                _colInds = *static_cast<Col<uword>*>(value.second);
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
          _copyOfColInds = _colInds;
          _copyOfGenDouble = _genDouble;

          expectedColsPlus();

          _genMat = _copyOfGenMat;
          _colInds = _copyOfColInds;
          _genDouble = _copyOfGenDouble;
          expectedColsMinus();

          _genMat = _copyOfGenMat;
          _colInds = _copyOfColInds;
          _genDouble = _copyOfGenDouble;
          expectedColsTimes();

          _genMat = _copyOfGenMat;
          _colInds = _copyOfColInds;
          _genDouble = _copyOfGenDouble;
          expectedColsDivide();
        }

        cout << "done." << endl;
      }

    protected:
      Mat<double> _genMat;
      Mat<double> _copyOfGenMat;

      Col<uword> _colInds;
      Col<uword> _copyOfColInds;

      double _genDouble;
      double _copyOfGenDouble;

      void expectedColsPlus() {
        for(int j = 0; j < _colInds.n_elem; j++) {
          if(_colInds.at(j) >= _genMat.n_cols) {
            return;
          }
        }

        cout << "- Compute expectedColsPlus() ... ";

        _genMat.cols(_colInds) += _genDouble;
        save<double>("Mat.colsPlus", _genMat);

        cout << "done." << endl;
      }

      void expectedColsMinus() {
        for(int j = 0; j < _colInds.n_elem; j++) {
          if(_colInds.at(j) >= _genMat.n_cols) {
            return;
          }
        }

        cout << "- Compute expectedColsMinus() ... ";

        _genMat.cols(_colInds) -= _genDouble;
        save<double>("Mat.colsMinus", _genMat);

        cout << "done." << endl;
      }

      void expectedColsTimes() {
        for(int j = 0; j < _colInds.n_elem; j++) {
          if(_colInds.at(j) >= _genMat.n_cols) {
            return;
          }
        }

        cout << "- Compute expectedColsTimes() ... ";

        _genMat.cols(_colInds) *= _genDouble;
        save<double>("Mat.colsTimes", _genMat);

        cout << "done." << endl;
      }

      void expectedColsDivide() {
        for(int j = 0; j < _colInds.n_elem; j++) {
          if(_colInds.at(j) >= _genMat.n_cols) {
            return;
          }
        }

        cout << "- Compute expectedColsDivide() ... ";

        _genMat.cols(_colInds) /= _genDouble;
        save<double>("Mat.colsDivide", _genMat);

        cout << "done." << endl;
      }

  };
}
