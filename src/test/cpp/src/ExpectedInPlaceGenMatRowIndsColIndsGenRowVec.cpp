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
using arma::Row;
using arma::uword;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedInPlaceGenMatRowIndsColIndsGenRowVec : public Expected {
    public:
      ExpectedInPlaceGenMatRowIndsColIndsGenRowVec() {
        cout << "Compute ExpectedInPlaceGenMatRowIndsColIndsGenRowVec(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
          InputClass::GenMat,
          InputClass::RowInds,
          InputClass::ColInds,
          InputClass::GenRowVec
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
                _colInds = *static_cast<Col<uword>*>(value.second);
                break;
              case 3:
                _fileSuffix += "," + value.first;
                _genRowVec = *static_cast<Row<double>*>(value.second);
                break;
            }
            ++n;
          }

          cout << "Using input: " << _fileSuffix << endl;

          _copyOfGenMat = _genMat;
          _copyOfRowInds = _rowInds;
          _copyOfGenRowVec = _genRowVec;

          expectedSubmatEqual();

          _genMat = _copyOfGenMat;
          _rowInds = _copyOfRowInds;
          _genRowVec = _copyOfGenRowVec;
          expectedSubmatPlus();

          _genMat = _copyOfGenMat;
          _rowInds = _copyOfRowInds;
          _genRowVec = _copyOfGenRowVec;
          expectedSubmatMinus();

          _genMat = _copyOfGenMat;
          _rowInds = _copyOfRowInds;
          _genRowVec = _copyOfGenRowVec;
          expectedSubmatElemTimes();

          _genMat = _copyOfGenMat;
          _rowInds = _copyOfRowInds;
          _genRowVec = _copyOfGenRowVec;
          expectedSubmatElemDivide();
        }

        cout << "done." << endl;
      }

    protected:
      Mat<double> _genMat;
      Mat<double> _copyOfGenMat;

      Col<uword> _rowInds;
      Col<uword> _copyOfRowInds;

      Col<uword> _colInds;
      Col<uword> _copyOfColInds;

      Row<double> _genRowVec;
      Row<double> _copyOfGenRowVec;

      void expectedSubmatEqual() {
        for(int i = 0; i < _rowInds.n_elem; i++) {
          for(int j = 0; j < _colInds.n_elem; j++) {
            if(!_genMat.in_range(_rowInds.at(i), _colInds.at(j))) {
              return;
            }
          }
        }

        if(_genRowVec.n_elem != _colInds.n_elem) {
          return;
        }

        if(_rowInds.n_elem != 1) {
          return;
        }

        cout << "- Compute expectedSubmatEqual() ... ";

        _genMat.submat(_rowInds, _colInds) = _genRowVec;
        save<double>("Mat.submatEqual", _genMat);

        cout << "done." << endl;
      }

      void expectedSubmatPlus() {
        for(int i = 0; i < _rowInds.n_elem; i++) {
          for(int j = 0; j < _colInds.n_elem; j++) {
            if(!_genMat.in_range(_rowInds.at(i), _colInds.at(j))) {
              return;
            }
          }
        }

        if(_genRowVec.n_elem != _colInds.n_elem) {
          return;
        }

        if(_rowInds.n_elem != 1) {
          return;
        }

        cout << "- Compute expectedSubmatPlus() ... ";

        _genMat.submat(_rowInds, _colInds) += _genRowVec;
        save<double>("Mat.submatPlus", _genMat);

        cout << "done." << endl;
      }

      void expectedSubmatMinus() {
        for(int i = 0; i < _rowInds.n_elem; i++) {
          for(int j = 0; j < _colInds.n_elem; j++) {
            if(!_genMat.in_range(_rowInds.at(i), _colInds.at(j))) {
              return;
            }
          }
        }

        if(_genRowVec.n_elem != _colInds.n_elem) {
          return;
        }

        if(_rowInds.n_elem != 1) {
          return;
        }

        cout << "- Compute expectedSubmatMinus() ... ";

        _genMat.submat(_rowInds, _colInds) -= _genRowVec;
        save<double>("Mat.submatMinus", _genMat);

        cout << "done." << endl;
      }

      void expectedSubmatElemTimes() {
        for(int i = 0; i < _rowInds.n_elem; i++) {
          for(int j = 0; j < _colInds.n_elem; j++) {
            if(!_genMat.in_range(_rowInds.at(i), _colInds.at(j))) {
              return;
            }
          }
        }

        if(_genRowVec.n_elem != _colInds.n_elem) {
          return;
        }

        if(_rowInds.n_elem != 1) {
          return;
        }

        cout << "- Compute expectedSubmatElemTimes() ... ";

        _genMat.submat(_rowInds, _colInds) %= _genRowVec;
        save<double>("Mat.submatElemTimes", _genMat);

        cout << "done." << endl;
      }

      void expectedSubmatElemDivide() {
        for(int i = 0; i < _rowInds.n_elem; i++) {
          for(int j = 0; j < _colInds.n_elem; j++) {
            if(!_genMat.in_range(_rowInds.at(i), _colInds.at(j))) {
              return;
            }
          }
        }

        if(_genRowVec.n_elem != _colInds.n_elem) {
          return;
        }

        if(_rowInds.n_elem != 1) {
          return;
        }

        cout << "- Compute expectedSubmatElemDivide() ... ";

        _genMat.submat(_rowInds, _colInds) /= _genRowVec;
        save<double>("Mat.submatElemDivide", _genMat);

        cout << "done." << endl;
      }

  };
}
