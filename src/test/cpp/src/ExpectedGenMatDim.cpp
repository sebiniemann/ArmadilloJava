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
using arma::min;
using arma::max;
using arma::prod;
using arma::sum;
using arma::mean;
using arma::median;
using arma::cumsum;
using arma::vectorise;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedGenMatDim : public Expected {
    public:
      ExpectedGenMatDim() {
        cout << "Compute ExpectedGenMatDim(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({InputClass::GenMat, InputClass::Dim});

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
                _dim = *static_cast<int*>(value.second);
                break;
            }
            ++n;
          }

          cout << "Using input: " << _fileSuffix << endl;

          expectedMin();
          expectedMax();
          expectedProd();
          expectedSum();
          expectedMean();
          expectedMedian();
          expectedCumsum();
          expectedVectorise();
        }

        cout << "done." << endl;
      }

    protected:
      Mat<double> _genMat;
      int _dim;

      void expectedMin() {
        cout << "- Compute expectedMin() ... ";
        save("min", min(_genMat, _dim));
        cout << "done." << endl;
      }

      void expectedMax() {
        cout << "- Compute expectedMax() ... ";
        save("max", max(_genMat, _dim));
        cout << "done." << endl;
      }

      void expectedProd() {
        cout << "- Compute expectedProd() ... ";
        save("prod", prod(_genMat, _dim));
        cout << "done." << endl;
      }

      void expectedSum() {
        cout << "- Compute expectedSum() ... ";
        save("sum", sum(_genMat, _dim));
        cout << "done." << endl;
      }

      void expectedMean() {
        cout << "- Compute expectedMean() ... ";
        save("mean", mean(_genMat, _dim));
        cout << "done." << endl;
      }

      void expectedMedian() {
        cout << "- Compute expectedMedian() ... ";
        save("median", median(_genMat, _dim));
        cout << "done." << endl;
      }

      void expectedCumsum() {
        cout << "- Compute expectedCumsum() ... ";
        save("cumsum", cumsum(_genMat, _dim));
        cout << "done." << endl;
      }

      void expectedVectorise() {
        cout << "- Compute expectedVectorise() ... ";
        save("vectorise", vectorise(_genMat, _dim));
        cout << "done." << endl;
      }

  };
}
