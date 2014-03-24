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
#include <iostream>
using std::cout;
using std::endl;

#include <utility>
using std::pair;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

#include <armadillo>
using arma::Mat;
using arma::abs;
using arma::eps;
using arma::exp;
using arma::exp2;
using arma::exp10;
using arma::trunc_exp;
using arma::log;
using arma::log2;
using arma::log10;
using arma::trunc_log;
using arma::sqrt;
using arma::square;
using arma::floor;
using arma::ceil;
using arma::round;
using arma::sign;
using arma::sin;
using arma::asin;
using arma::sinh;
using arma::asinh;
using arma::cos;
using arma::acos;
using arma::cosh;
using arma::acosh;
using arma::tan;
using arma::atan;
using arma::tanh;
using arma::atanh;

#include <Expected.hpp>
using armadilloJava::Expected;

namespace armadilloJava {
  class ExpectedGenMat : public Expected {
    public:
      ExpectedGenMat() {
          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({InputClass::GenMat});

          for (vector<pair<string, void*>> input : inputs) {
            _fileSuffix = "";

            int n = 0;
            for (pair<string, void*> value : input) {
              switch (n) {
                case 0:
                  _fileSuffix += value.first;
                  _GenMat = *static_cast<Mat<double>*>(value.second);
                  break;
              }
              ++n;
            }

            expectedAbs();
            expectedEps();
            expectedExp();
            expectedExp2();
            expectedExp10();
            expectedTrunc_exp();
            expectedLog();
            expectedLog2();
            expectedLog10();
            expectedTrunc_log();
            expectedSquare();
            expectedFloor();
            expectedCeil();
            expectedRound();
            expectedSign();
            expectedSin();
            expectedAsin();
            expectedSinh();
            expectedAsinh();
            expectedCos();
            expectedAcos();
            expectedCosh();
            expectedAcosh();
            expectedTan();
            expectedAtan();
            expectedTanh();
            expectedAtanh();
          }
        }

    protected:
      Mat<double> _GenMat;

      void expectedAbs() {
        save("abs", abs(_GenMat));
      }

      void expectedEps() {
        save("eps", eps(_GenMat));
      }

      void expectedExp() {
        save("exp", exp(_GenMat));
      }

      void expectedExp2() {
        save("exp2", exp2(_GenMat));
      }

      void expectedExp10() {
        save("exp10", exp10(_GenMat));
      }

      void expectedTrunc_exp() {
        save("trunc_exp", trunc_exp(_GenMat));
      }

      void expectedLog() {
        save("log", log(_GenMat));
      }

      void expectedLog2() {
        save("log2", log2(_GenMat));
      }

      void expectedLog10() {
        save("log10", log10(_GenMat));
      }

      void expectedTrunc_log() {
        save("trunc_log", trunc_log(_GenMat));
      }

      void expectedSquare() {
        save("square", square(_GenMat));
      }

      void expectedFloor() {
        save("floor", floor(_GenMat));
      }

      void expectedCeil() {
        save("ceil", ceil(_GenMat));
      }

      void expectedRound() {
        save("round", round(_GenMat));
      }

      void expectedSign() {
        save("sign", sign(_GenMat));
      }

      void expectedSin() {
        save("sin", sin(_GenMat));
      }

      void expectedAsin() {
        save("asin", asin(_GenMat));
      }

      void expectedSinh() {
        save("sinh", sinh(_GenMat));
      }

      void expectedAsinh() {
        save("asinh", asinh(_GenMat));
      }

      void expectedCos() {
        save("cos", cos(_GenMat));
      }

      void expectedAcos() {
        save("acos", acos(_GenMat));
      }

      void expectedCosh() {
        save("cosh", cosh(_GenMat));
      }

      void expectedAcosh() {
        save("acosh", acosh(_GenMat));
      }

      void expectedTan() {
        save("tan", tan(_GenMat));
      }

      void expectedAtan() {
        save("atan", atan(_GenMat));
      }

      void expectedTanh() {
        save("tanh", tanh(_GenMat));
      }

      void expectedAtanh() {
        save("atanh", atanh(_GenMat));
      }

  };
}
