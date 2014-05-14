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
  class ExpectedInPlaceGenMatRowIndsColIndsGenMat : public Expected {
    public:
      ExpectedInPlaceGenMatRowIndsColIndsGenMat() {
        cout << "Compute ExpectedInPlaceGenMatRowIndsColIndsGenMat(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
          InputClass::GenMat,
          InputClass::RowInds,
          InputClass::ColInds,
          InputClass::GenMat
        });

        for (vector<pair<string, void*>> input : inputs) {
          _fileSuffix = "";

          int n = 0;
          for (pair<string, void*> value : input) {
            switch (n) {
              case 0:
                _fileSuffix += value.first;
                _genMatA = *static_cast<Mat<double>*>(value.second);
                break;
              case 1:
                _fileSuffix += "," + value.first;
                _rowInds = *static_cast<Col<uword>*>(value.second);
                break;
              case 2:
                _fileSuffix += "," + value.first;
                _colInds = *static_cast<Col<uword>*>(value.second);
                break;
              case 3:
                _fileSuffix += "," + value.first;
                _genMatB = *static_cast<Mat<double>*>(value.second);
                break;
            }
            ++n;
          }

          cout << "Using input: " << _fileSuffix << endl;

          _copyOfGenMatA = _genMatA;
          _copyOfRowInds = _rowInds;
          _copyOfGenMatB = _genMatB;

          expectedSubmatEqual();

          _genMatA = _copyOfGenMatA;
          _rowInds = _copyOfRowInds;
          _genMatB = _copyOfGenMatB;
          expectedSubmatPlus();

          _genMatA = _copyOfGenMatA;
          _rowInds = _copyOfRowInds;
          _genMatB = _copyOfGenMatB;
          expectedSubmatMinus();

          _genMatA = _copyOfGenMatA;
          _rowInds = _copyOfRowInds;
          _genMatB = _copyOfGenMatB;
          expectedSubmatElemTimes();

          _genMatA = _copyOfGenMatA;
          _rowInds = _copyOfRowInds;
          _genMatB = _copyOfGenMatB;
          expectedSubmatElemDivide();
        }

        cout << "done." << endl;
      }

    protected:
      Mat<double> _genMatA;
      Mat<double> _copyOfGenMatA;

      Col<uword> _rowInds;
      Col<uword> _copyOfRowInds;

      Col<uword> _colInds;
      Col<uword> _copyOfColInds;

      Mat<double> _genMatB;
      Mat<double> _copyOfGenMatB;

      void expectedSubmatEqual() {
        for(int i = 0; i < _rowInds.n_elem; i++) {
          for(int j = 0; j < _colInds.n_elem; j++) {
            if(!_genMatA.in_range(_rowInds.at(i), _colInds.at(j))) {
              return;
            }
          }
        }

        if(_genMatB.n_rows != _rowInds.n_elem) {
          return;
        }

        if(_genMatB.n_cols != _colInds.n_elem) {
          return;
        }

        cout << "- Compute expectedSubmatEqual() ... ";

        _genMatA.submat(_rowInds, _colInds) = _genMatB;
        save<double>("Mat.submatEqual", _genMatA);

        cout << "done." << endl;
      }

      void expectedSubmatPlus() {
        for(int i = 0; i < _rowInds.n_elem; i++) {
          for(int j = 0; j < _colInds.n_elem; j++) {
            if(!_genMatA.in_range(_rowInds.at(i), _colInds.at(j))) {
              return;
            }
          }
        }

        if(_genMatB.n_rows != _rowInds.n_elem) {
          return;
        }

        if(_genMatB.n_cols != _colInds.n_elem) {
          return;
        }

        cout << "- Compute expectedSubmatPlus() ... ";

        _genMatA.submat(_rowInds, _colInds) += _genMatB;
        save<double>("Mat.submatPlus", _genMatA);

        cout << "done." << endl;
      }

      void expectedSubmatMinus() {
        for(int i = 0; i < _rowInds.n_elem; i++) {
          for(int j = 0; j < _colInds.n_elem; j++) {
            if(!_genMatA.in_range(_rowInds.at(i), _colInds.at(j))) {
              return;
            }
          }
        }

        if(_genMatB.n_rows != _rowInds.n_elem) {
          return;
        }

        if(_genMatB.n_cols != _colInds.n_elem) {
          return;
        }

        cout << "- Compute expectedSubmatMinus() ... ";

        _genMatA.submat(_rowInds, _colInds) -= _genMatB;
        save<double>("Mat.submatMinus", _genMatA);

        cout << "done." << endl;
      }

      void expectedSubmatElemTimes() {
        for(int i = 0; i < _rowInds.n_elem; i++) {
          for(int j = 0; j < _colInds.n_elem; j++) {
            if(!_genMatA.in_range(_rowInds.at(i), _colInds.at(j))) {
              return;
            }
          }
        }

        if(_genMatB.n_rows != _rowInds.n_elem) {
          return;
        }

        if(_genMatB.n_cols != _colInds.n_elem) {
          return;
        }

        cout << "- Compute expectedSubmatElemTimes() ... ";

        _genMatA.submat(_rowInds, _colInds) %= _genMatB;
        save<double>("Mat.submatElemTimes", _genMatA);

        cout << "done." << endl;
      }

      void expectedSubmatElemDivide() {
        for(int i = 0; i < _rowInds.n_elem; i++) {
          for(int j = 0; j < _colInds.n_elem; j++) {
            if(!_genMatA.in_range(_rowInds.at(i), _colInds.at(j))) {
              return;
            }
          }
        }

        if(_genMatB.n_rows != _rowInds.n_elem) {
          return;
        }

        if(_genMatB.n_cols != _colInds.n_elem) {
          return;
        }

        cout << "- Compute expectedSubmatElemDivide() ... ";

        _genMatA.submat(_rowInds, _colInds) /= _genMatB;
        save<double>("Mat.submatElemDivide", _genMatA);

        cout << "done." << endl;
      }

  };
}
