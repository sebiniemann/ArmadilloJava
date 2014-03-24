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

#include <armadillo>
using arma::Row;
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

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

#include <Expected.hpp>
using armadilloJava::Expected;

namespace armadilloJava {
  class ExpectedGenRowVec : public Expected {
    public:
      ExpectedGenRowVec() {
          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({InputClass::GenRowVec});

          for (vector<pair<string, void*>> input : inputs) {
            _fileSuffix = "";

            int n = 0;
            for (pair<string, void*> value : input) {
              switch (n) {
                case 0:
                  _fileSuffix += value.first;
                  _GenRowVec = *static_cast<Row<double>*>(value.second);
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
      Mat<double> _GenRowVec;

      void expectedAbs() {
        save("abs", abs(_GenRowVec));
      }

      void expectedEps() {
        save("eps", eps(_GenRowVec));
      }

      void expectedExp() {
        save("exp", exp(_GenRowVec));
      }

      void expectedExp2() {
        save("exp2", exp2(_GenRowVec));
      }

      void expectedExp10() {
        save("exp10", exp10(_GenRowVec));
      }

      void expectedTrunc_exp() {
        save("trunc_exp", trunc_exp(_GenRowVec));
      }

      void expectedLog() {
        save("log", log(_GenRowVec));
      }

      void expectedLog2() {
        save("log2", log2(_GenRowVec));
      }

      void expectedLog10() {
        save("log10", log10(_GenRowVec));
      }

      void expectedTrunc_log() {
        save("trunc_log", trunc_log(_GenRowVec));
      }

      void expectedSquare() {
        save("square", square(_GenRowVec));
      }

      void expectedFloor() {
        save("floor", floor(_GenRowVec));
      }

      void expectedCeil() {
        save("ceil", ceil(_GenRowVec));
      }

      void expectedRound() {
        save("round", round(_GenRowVec));
      }

      void expectedSign() {
        save("sign", sign(_GenRowVec));
      }

      void expectedSin() {
        save("sin", sin(_GenRowVec));
      }

      void expectedAsin() {
        save("asin", asin(_GenRowVec));
      }

      void expectedSinh() {
        save("sinh", sinh(_GenRowVec));
      }

      void expectedAsinh() {
        save("asinh", asinh(_GenRowVec));
      }

      void expectedCos() {
        save("cos", cos(_GenRowVec));
      }

      void expectedAcos() {
        save("acos", acos(_GenRowVec));
      }

      void expectedCosh() {
        save("cosh", cosh(_GenRowVec));
      }

      void expectedAcosh() {
        save("acosh", acosh(_GenRowVec));
      }

      void expectedTan() {
        save("tan", tan(_GenRowVec));
      }

      void expectedAtan() {
        save("atan", atan(_GenRowVec));
      }

      void expectedTanh() {
        save("tanh", tanh(_GenRowVec));
      }

      void expectedAtanh() {
        save("atanh", atanh(_GenRowVec));
      }

  };
}
