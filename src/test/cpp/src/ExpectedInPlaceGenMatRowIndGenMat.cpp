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

#include <algorithm>
using std::min;

#include <utility>
using std::pair;

#include <armadillo>
using arma::Mat;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedInPlaceGenMatRowIndGenMat : public Expected {
    public:
      ExpectedInPlaceGenMatRowIndGenMat() {
        cout << "Compute ExpectedInPlaceGenMatRowIndGenMat(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::GenMat,
            InputClass::RowInd,
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
                  _rowInd = *static_cast<int*>(value.second);
                  break;
                case 2:
                  _fileSuffix += "," + value.first;
                  _genMatB = *static_cast<Mat<double>*>(value.second);
                  break;
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            _copyOfGenMatA = _genMatA;
            _copyOfRowInd = _rowInd;
            _copyOfGenMatB = _genMatB;

            expectedMatDiagEqual();

            _genMatA = _copyOfGenMatA;
            _rowInd = _copyOfRowInd;
            _genMatB = _copyOfGenMatB;
            expectedMatDiagPlus();

            _genMatA = _copyOfGenMatA;
            _rowInd = _copyOfRowInd;
            _genMatB = _copyOfGenMatB;
            expectedMatDiagMinus();

            _genMatA = _copyOfGenMatA;
            _rowInd = _copyOfRowInd;
            _genMatB = _copyOfGenMatB;
            expectedMatDiagElemTimes();

            _genMatA = _copyOfGenMatA;
            _rowInd = _copyOfRowInd;
            _genMatB = _copyOfGenMatB;
            expectedMatDiagElemDivide();

            _genMatA = _copyOfGenMatA;
            _rowInd = _copyOfRowInd;
            _genMatB = _copyOfGenMatB;
            expectedMatRowEqual();

            _genMatA = _copyOfGenMatA;
            _rowInd = _copyOfRowInd;
            _genMatB = _copyOfGenMatB;
            expectedMatRowPlus();

            _genMatA = _copyOfGenMatA;
            _rowInd = _copyOfRowInd;
            _genMatB = _copyOfGenMatB;
            expectedMatRowMinus();

            _genMatA = _copyOfGenMatA;
            _rowInd = _copyOfRowInd;
            _genMatB = _copyOfGenMatB;
            expectedMatRowElemTimes();

            _genMatA = _copyOfGenMatA;
            _rowInd = _copyOfRowInd;
            _genMatB = _copyOfGenMatB;
            expectedMatRowElemDivide();
          }

          cout << "done." << endl;
        }

    protected:
      Mat<double> _genMatA;
      Mat<double> _copyOfGenMatA;

      int _rowInd;
      int _copyOfRowInd;

      Mat<double> _genMatB;
      Mat<double> _copyOfGenMatB;

      void expectedMatDiagEqual() {
        if(_rowInd >= _genMatA.n_rows) {
          return;
        }

        if (!_genMatB.is_vec() || _genMatB.n_elem != min(_genMatA.n_rows - _rowInd, _genMatA.n_cols)) {
          return;
        }

        cout << "- Compute expectedMatDiagEqual() ... ";

        _genMatA.diag(-_rowInd) = _genMatB;
        save<double>("Mat.diagSubEqual", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatDiagPlus() {
        if(_rowInd >= _genMatA.n_rows) {
          return;
        }

        if (!_genMatB.is_vec() || _genMatB.n_elem != min(_genMatA.n_rows - _rowInd, _genMatA.n_cols)) {
          return;
        }

        cout << "- Compute expectedMatDiagPlus() ... ";

        _genMatA.diag(-_rowInd) += _genMatB;
        save<double>("Mat.diagSubPlus", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatDiagMinus() {
        if(_rowInd >= _genMatA.n_rows) {
          return;
        }

        if (!_genMatB.is_vec() || _genMatB.n_elem != min(_genMatA.n_rows - _rowInd, _genMatA.n_cols)) {
          return;
        }

        cout << "- Compute expectedMatDiagMinus() ... ";

        _genMatA.diag(-_rowInd) -= _genMatB;
        save<double>("Mat.diagSubMinus", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatDiagElemTimes() {
        if(_rowInd >= _genMatA.n_rows) {
          return;
        }

        if (!_genMatB.is_vec() || _genMatB.n_elem != min(_genMatA.n_rows - _rowInd, _genMatA.n_cols)) {
          return;
        }

        cout << "- Compute expectedMatDiagElemTimes() ... ";

        _genMatA.diag(-_rowInd) %= _genMatB;
        save<double>("Mat.diagSubElemTimes", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatDiagElemDivide() {
        if(_rowInd >= _genMatA.n_rows) {
          return;
        }

        if (!_genMatB.is_vec() || _genMatB.n_elem != min(_genMatA.n_rows - _rowInd, _genMatA.n_cols)) {
          return;
        }

        cout << "- Compute expectedMatDiagElemDivide() ... ";

        _genMatA.diag(-_rowInd) /= _genMatB;
        save<double>("Mat.diagSubElemDivide", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatRowEqual() {
        if(_rowInd >= _genMatA.n_rows) {
          return;
        }

        if(!_genMatB.is_rowvec() || _genMatA.n_cols != _genMatB.n_cols) {
          return;
        }

        cout << "- Compute expectedMatRowEqual() ... ";

        _genMatA.row(_rowInd) = _genMatB;
        save<double>("Mat.rowEqual", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatRowPlus() {
        if(_rowInd >= _genMatA.n_rows) {
          return;
        }

        if(!_genMatB.is_rowvec() || _genMatA.n_cols != _genMatB.n_cols) {
          return;
        }

        cout << "- Compute expectedMatRowPlus() ... ";

        _genMatA.row(_rowInd) += _genMatB;
        save<double>("Mat.rowPlus", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatRowMinus() {
        if(_rowInd >= _genMatA.n_rows) {
          return;
        }

        if(!_genMatB.is_rowvec() || _genMatA.n_cols != _genMatB.n_cols) {
          return;
        }

        cout << "- Compute expectedMatRowMinus() ... ";

        _genMatA.row(_rowInd) -= _genMatB;
        save<double>("Mat.rowMinus", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatRowElemTimes() {
        if(_rowInd >= _genMatA.n_rows) {
          return;
        }

        if(!_genMatB.is_rowvec() || _genMatA.n_cols != _genMatB.n_cols) {
          return;
        }

        cout << "- Compute expectedMatRowElemTimes() ... ";

        _genMatA.row(_rowInd) %= _genMatB;
        save<double>("Mat.rowElemTimes", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatRowElemDivide() {
        if(_rowInd >= _genMatA.n_rows) {
          return;
        }

        if(!_genMatB.is_rowvec() || _genMatA.n_cols != _genMatB.n_cols) {
          return;
        }

        cout << "- Compute expectedMatRowElemDivide() ... ";

        _genMatA.row(_rowInd) /= _genMatB;
        save<double>("Mat.rowElemDivide", _genMatA);

        cout << "done." << endl;
      }
  };
}
