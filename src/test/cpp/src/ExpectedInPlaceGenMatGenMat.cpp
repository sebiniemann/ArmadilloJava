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
using arma::inplace_trans;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedInPlaceGenMatGenMat : public Expected {
    public:
      ExpectedInPlaceGenMatGenMat() {
        cout << "Compute ExpectedInPlaceGenMatGenMat(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({InputClass::GenMat, InputClass::GenMat});

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
                  _genMatB = *static_cast<Mat<double>*>(value.second);
                  break;
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            _copyOfGenMatA = _genMatA;
            _copyOfGenMatB = _genMatB;

            expectedMatInPlaceEqual();

            _genMatA = _copyOfGenMatA;
            _genMatB = _copyOfGenMatB;
            expectedMatInPlacePlus();

            _genMatA = _copyOfGenMatA;
            _genMatB = _copyOfGenMatB;
            expectedMatInPlaceMinus();

            _genMatA = _copyOfGenMatA;
            _genMatB = _copyOfGenMatB;
            expectedMatInPlaceTimes();

            _genMatA = _copyOfGenMatA;
            _genMatB = _copyOfGenMatB;
            expectedMatInPlaceElemTimes();

            _genMatA = _copyOfGenMatA;
            _genMatB = _copyOfGenMatB;
            expectedMatInPlaceElemDivide();

            _genMatA = _copyOfGenMatA;
            _genMatB = _copyOfGenMatB;
            expectedMatSwap();

            _genMatA = _copyOfGenMatA;
            _genMatB = _copyOfGenMatB;
            expectedMatEach_colEqual();

            _genMatA = _copyOfGenMatA;
            _genMatB = _copyOfGenMatB;
            expectedMatEach_colPlus();

            _genMatA = _copyOfGenMatA;
            _genMatB = _copyOfGenMatB;
            expectedMatEach_colMinus();

            _genMatA = _copyOfGenMatA;
            _genMatB = _copyOfGenMatB;
            expectedMatEach_colElemTimes();

            _genMatA = _copyOfGenMatA;
            _genMatB = _copyOfGenMatB;
            expectedMatEach_colElemDivide();

            _genMatA = _copyOfGenMatA;
            _genMatB = _copyOfGenMatB;
            expectedMatEach_rowEqual();

            _genMatA = _copyOfGenMatA;
            _genMatB = _copyOfGenMatB;
            expectedMatEach_rowPlus();

            _genMatA = _copyOfGenMatA;
            _genMatB = _copyOfGenMatB;
            expectedMatEach_rowMinus();

            _genMatA = _copyOfGenMatA;
            _genMatB = _copyOfGenMatB;
            expectedMatEach_rowElemTimes();

            _genMatA = _copyOfGenMatA;
            _genMatB = _copyOfGenMatB;
            expectedMatEach_rowElemDivide();
          }

          cout << "done." << endl;
        }

    protected:
      Mat<double> _genMatA;
      Mat<double> _copyOfGenMatA;

      Mat<double> _genMatB;
      Mat<double> _copyOfGenMatB;

      void expectedMatInPlaceEqual() {
        cout << "- Compute expectedMatInPlaceEqual() ... ";

        _genMatA = _genMatB;
        save<double>("Mat.inPlaceEqual", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatInPlacePlus() {
        if(_genMatA.n_rows != _genMatB.n_rows || _genMatA.n_cols != _genMatB.n_cols) {
          return;
        }

        cout << "- Compute expectedMatInPlace() ... ";

        _genMatA += _genMatB;
        save<double>("Mat.inPlacePlus", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatInPlaceMinus() {
        if(_genMatA.n_rows != _genMatB.n_rows || _genMatA.n_cols != _genMatB.n_cols) {
          return;
        }

        cout << "- Compute expectedMatInPlaceMinus() ... ";

        _genMatA -= _genMatB;
        save<double>("Mat.inPlaceMinus", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatInPlaceTimes() {
        if(_genMatA.n_cols != _genMatB.n_rows) {
          return;
        }

        cout << "- Compute expectedMatInPlaceTimes() ... ";

        _genMatA *= _genMatB;
        save<double>("Mat.inPlaceTimes", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatInPlaceElemTimes() {
        if(_genMatA.n_rows != _genMatB.n_rows || _genMatA.n_cols != _genMatB.n_cols) {
          return;
        }

        cout << "- Compute expectedMatInPlaceElemTimes() ... ";

        _genMatA %= _genMatB;
        save<double>("Mat.inPlaceElemTimes", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatInPlaceElemDivide() {
        if(_genMatA.n_rows != _genMatB.n_rows || _genMatA.n_cols != _genMatB.n_cols) {
          return;
        }

        cout << "- Compute expectedMatInPlaceElemDivide() ... ";

        _genMatA /= _genMatB;
        save<double>("Mat.inPlaceElemDivide", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatSwap() {
        cout << "- Compute expectedMatSwap() ... ";

        _genMatA.swap(_genMatB);
        save<double>("Mat.swap", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatEach_colEqual() {
        if(!_genMatB.is_colvec() || _genMatA.n_rows != _genMatB.n_rows) {
          return;
        }

        cout << "- Compute expectedMatEach_colEqual() ... ";

        _genMatA.each_col() = _genMatB;
        save<double>("Mat.each_colEqual", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatEach_colPlus() {
        if(!_genMatB.is_colvec() || _genMatA.n_rows != _genMatB.n_rows) {
          return;
        }

        cout << "- Compute expectedMatEach_colPlus() ... ";

        _genMatA.each_col() += _genMatB;
        save<double>("Mat.each_colPlus", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatEach_colMinus() {
        if(!_genMatB.is_colvec() || _genMatA.n_rows != _genMatB.n_rows) {
          return;
        }

        cout << "- Compute expectedMatEach_colMinus() ... ";

        _genMatA.each_col() -= _genMatB;
        save<double>("Mat.each_colMinus", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatEach_colElemTimes() {
        if(!_genMatB.is_colvec() || _genMatA.n_rows != _genMatB.n_rows) {
          return;
        }

        cout << "- Compute expectedMatEach_colElemTimes() ... ";

        _genMatA.each_col() %= _genMatB;
        save<double>("Mat.each_colElemTimes", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatEach_colElemDivide() {
        if(!_genMatB.is_colvec() || _genMatA.n_rows != _genMatB.n_rows) {
          return;
        }

        cout << "- Compute expectedMatEach_colElemDivide() ... ";

        _genMatA.each_col() /= _genMatB;
        save<double>("Mat.each_colElemDivide", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatEach_rowEqual() {
        if(!_genMatB.is_rowvec() || _genMatA.n_cols != _genMatB.n_cols) {
          return;
        }

        cout << "- Compute expectedMatEach_rowEqual() ... ";

        _genMatA.each_row() = _genMatB;
        save<double>("Mat.each_rowEqual", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatEach_rowPlus() {
        if(!_genMatB.is_rowvec() || _genMatA.n_cols != _genMatB.n_cols) {
          return;
        }

        cout << "- Compute expectedMatEach_rowPlus() ... ";

        _genMatA.each_row() += _genMatB;
        save<double>("Mat.each_rowPlus", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatEach_rowMinus() {
        if(!_genMatB.is_rowvec() || _genMatA.n_cols != _genMatB.n_cols) {
          return;
        }

        cout << "- Compute expectedMatEach_rowMinus() ... ";

        _genMatA.each_row() -= _genMatB;
        save<double>("Mat.each_rowMinus", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatEach_rowElemTimes() {
        if(!_genMatB.is_rowvec() || _genMatA.n_cols != _genMatB.n_cols) {
          return;
        }

        cout << "- Compute expectedMatEach_rowElemTimes() ... ";

        _genMatA.each_row() %= _genMatB;
        save<double>("Mat.each_rowElemTimes", _genMatA);

        cout << "done." << endl;
      }

      void expectedMatEach_rowElemDivide() {
        if(!_genMatB.is_rowvec() || _genMatA.n_cols != _genMatB.n_cols) {
          return;
        }

        cout << "- Compute expectedMatEach_rowElemDivide() ... ";

        _genMatA.each_row() /= _genMatB;
        save<double>("Mat.each_rowElemDivide", _genMatA);

        cout << "done." << endl;
      }
  };
}
