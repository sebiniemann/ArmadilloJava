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
using arma::det;
using arma::log_det;
using arma::trace;
using arma::diagmat;
using arma::symmatu;
using arma::symmatl;
using arma::trimatu;
using arma::trimatl;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedSquMat : public Expected {
    public:
      ExpectedSquMat() {
        cout << "Compute ExpectedSquMat(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({InputClass::SquMat});

          for (vector<pair<string, void*>> input : inputs) {
            _fileSuffix = "";

            int n = 0;
            for (pair<string, void*> value : input) {
              switch (n) {
                case 0:
                  _fileSuffix += value.first;
                  _genMat = *static_cast<Mat<double>*>(value.second);
                  break;
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            expectedDet();
            expectedLog_det();
            expectedTrace();
            expectedDiagmat();
            expectedSymmatu();
            expectedSymmatl();
            expectedTrimatu();
            expectedTrimatl();
          }

          cout << "done." << endl;
        }

    protected:
      Mat<double> _genMat;

      void expectedDet() {
        cout << "- Compute expectedDet() ... ";
        save("det", Mat<double>({det(_genMat)}));
        cout << "done." << endl;
      }

      void expectedLog_det() {
        cout << "- Compute expectedLog_det() ... ";

        double val, sign;

        log_det(val, sign, _genMat);

        save("log_detVal", Mat<double>({val}));
        save("log_detSign", Mat<double>({sign}));

        cout << "done." << endl;
      }

      void expectedTrace() {
        cout << "- Compute expectedTrace() ... ";
        save("trace", Mat<double>({trace(_genMat)}));
        cout << "done." << endl;
      }

      void expectedDiagmat() {
        cout << "- Compute expectedDiagmat() ... ";
        save("diagmat", diagmat(_genMat));
        cout << "done." << endl;
      }

      void expectedSymmatu() {
        cout << "- Compute expectedSymmatu() ... ";
        save("symmatu", symmatu(_genMat));
        cout << "done." << endl;
      }

      void expectedSymmatl() {
        cout << "- Compute expectedSymmatl() ... ";
        save("symmatl", symmatl(_genMat));
        cout << "done." << endl;
      }

      void expectedTrimatu() {
        cout << "- Compute expectedTrimatu() ... ";
        save("trimatu", trimatu(_genMat));
        cout << "done." << endl;
      }

      void expectedTrimatl() {
        cout << "- Compute expectedTrimatl() ... ";
        save("trimatl", trimatl(_genMat));
        cout << "done." << endl;
      }
  };
}
