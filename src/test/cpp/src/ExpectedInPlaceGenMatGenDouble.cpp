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
  class ExpectedInPlaceGenMatGenDouble : public Expected {
    public:
      ExpectedInPlaceGenMatGenDouble() {
        cout << "Compute ExpectedInPlaceGenMatGenDouble(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::GenMat,
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
                  _genDouble = *static_cast<double*>(value.second);
                  break;
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            _copyOfGenMat = _genMat;
            _copyOfGenDouble = _genDouble;

            expectedMatFill();

            _genMat = _copyOfGenMat;
            _genDouble = _copyOfGenDouble;
            expectedMatDiagPlus();

            _genMat = _copyOfGenMat;
            _genDouble = _copyOfGenDouble;
            expectedMatDiagMinus();

            _genMat = _copyOfGenMat;
            _genDouble = _copyOfGenDouble;
            expectedMatDiagTimes();

            _genMat = _copyOfGenMat;
            _genDouble = _copyOfGenDouble;
            expectedMatDiagDivide();

            _genMat = _copyOfGenMat;
            _genDouble = _copyOfGenDouble;
            expectedMatInPlacePlus();

            _genMat = _copyOfGenMat;
            _genDouble = _copyOfGenDouble;
            expectedMatInPlaceMinus();

            _genMat = _copyOfGenMat;
            _genDouble = _copyOfGenDouble;
            expectedMatInPlaceTimes();

            _genMat = _copyOfGenMat;
            _genDouble = _copyOfGenDouble;
            expectedMatInPlaceDivide();
          }

          cout << "done." << endl;
        }

    protected:
      Mat<double> _genMat;
      Mat<double> _copyOfGenMat;

      double _genDouble;
      double _copyOfGenDouble;

      void expectedMatFill() {
        cout << "- Compute expectedMatFill() ... ";

        _genMat.fill(_genDouble);
        save<double>("Mat.fill", _genMat);

        cout << "done." << endl;
      }

      void expectedMatDiagPlus() {
        cout << "- Compute expectedMatDiagPlus() ... ";

        _genMat.diag() += _genDouble;
        save<double>("Mat.diagPlus", _genMat);

        cout << "done." << endl;
      }

      void expectedMatDiagMinus() {
        cout << "- Compute expectedMatDiagMinus() ... ";

        _genMat.diag() -= _genDouble;
        save<double>("Mat.diagMinus", _genMat);

        cout << "done." << endl;
      }

      void expectedMatDiagTimes() {
        cout << "- Compute expectedMatDiagTimes() ... ";

        _genMat.diag() *= _genDouble;
        save<double>("Mat.diagTimes", _genMat);

        cout << "done." << endl;
      }

      void expectedMatDiagDivide() {
        cout << "- Compute expectedMatDiagDivide() ... ";

        _genMat.diag() /= _genDouble;
        save<double>("Mat.diagDivide", _genMat);

        cout << "done." << endl;
      }

      void expectedMatInPlacePlus() {
        cout << "- Compute expectedMatInPlacePlus() ... ";

        _genMat += _genDouble;
        save<double>("Mat.inPlacePlus", _genMat);

        cout << "done." << endl;
      }

      void expectedMatInPlaceMinus() {
        cout << "- Compute expectedMatInPlaceMinus() ... ";

        _genMat -= _genDouble;
        save<double>("Mat.inPlaceMinus", _genMat);

        cout << "done." << endl;
      }

      void expectedMatInPlaceTimes() {
        cout << "- Compute expectedMatInPlaceTimes() ... ";

        _genMat *= _genDouble;
        save<double>("Mat.inPlaceTimes", _genMat);

        cout << "done." << endl;
      }

      void expectedMatInPlaceDivide() {
        cout << "- Compute expectedMatInPlaceDivide() ... ";

        _genMat /= _genDouble;
        save<double>("Mat.inPlaceDivide", _genMat);

        cout << "done." << endl;
      }
  };
}
