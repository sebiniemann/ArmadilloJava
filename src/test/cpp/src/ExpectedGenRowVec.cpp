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
using arma::Row;
using arma::uword;
using arma::raw_ascii;
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
using arma::cumsum;
using arma::hist;
using arma::sort;
using arma::sort_index;
using arma::stable_sort_index;
using arma::trans;
using arma::unique;
using arma::toeplitz;
using arma::circ_toeplitz;
using arma::accu;
using arma::min;
using arma::max;
using arma::prod;
using arma::sum;
using arma::mean;
using arma::median;
using arma::stddev;
using arma::var;
using arma::cor;
using arma::cov;
using arma::diagmat;
using arma::is_finite;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedGenRowVec : public Expected {
    public:
      ExpectedGenRowVec() {
        cout << "Compute ExpectedGenRowVec(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({InputClass::GenRowVec});

        for (vector<pair<string, void*>> input : inputs) {
          _fileSuffix = "";

          int n = 0;
          for (pair<string, void*> value : input) {
            switch (n) {
              case 0:
                _fileSuffix += value.first;
                _genRowVec = *static_cast<Row<double>*>(value.second);
                break;
            }
            ++n;
          }

          cout << "Using input: " << _fileSuffix << endl;

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
          expectedCumsum();
          expectedHist();
          expectedSort();
          expectedSort_index();
          expectedStable_sort_index();
          expectedTrans();
          expectedUnique();
          expectedNegate();
          expectedReciprocal();
          expectedToeplitz();
          expectedCirc_toeplitz();
          expectedAccu();
          expectedMin();
          expectedMax();
          expectedProd();
          expectedSum();
          expectedMean();
          expectedMedian();
          expectedStddev();
          expectedVar();
          expectedCor();
          expectedCov();
          expectedDiagmat();
          expectedIs_finite();
        }

        cout << "done." << endl;
      }

    protected:
      Row<double> _genRowVec;

      void expectedAbs() {
        cout << "- Compute expectedAbs() ... ";
        save("abs", abs(_genRowVec));
        cout << "done." << endl;
      }

      void expectedEps() {
        cout << "- Compute expectedAbs() ... ";
        save("eps", eps(_genRowVec));
        cout << "done." << endl;
      }

      void expectedExp() {
        cout << "- Compute expectedExp() ... ";
        save("exp", exp(_genRowVec));
        cout << "done." << endl;
      }

      void expectedExp2() {
        cout << "- Compute expectedExp2() ... ";
        save("exp2", exp2(_genRowVec));
        cout << "done." << endl;
      }

      void expectedExp10() {
        cout << "- Compute expectedExp10() ... ";
        save("exp10", exp10(_genRowVec));
        cout << "done." << endl;
      }

      void expectedTrunc_exp() {
        cout << "- Compute expectedTrunc_exp() ... ";
        save("trunc_exp", trunc_exp(_genRowVec));
        cout << "done." << endl;
      }

      void expectedLog() {
        cout << "- Compute expectedLog() ... ";
        save("log", log(_genRowVec));
        cout << "done." << endl;
      }

      void expectedLog2() {
        cout << "- Compute expectedLog2() ... ";
        save("log2", log2(_genRowVec));
        cout << "done." << endl;
      }

      void expectedLog10() {
        cout << "- Compute expectedLog10() ... ";
        save("log10", log10(_genRowVec));
        cout << "done." << endl;
      }

      void expectedTrunc_log() {
        cout << "- Compute expectedTrunc_log() ... ";
        save("trunc_log", trunc_log(_genRowVec));
        cout << "done." << endl;
      }

      void expectedSquare() {
        cout << "- Compute expectedSquare() ... ";
        save("square", square(_genRowVec));
        cout << "done." << endl;
      }

      void expectedFloor() {
        cout << "- Compute expectedFloor() ... ";
        save("floor", floor(_genRowVec));
        cout << "done." << endl;
      }

      void expectedCeil() {
        cout << "- Compute expectedCeil() ... ";
        save("ceil", ceil(_genRowVec));
        cout << "done." << endl;
      }

      void expectedRound() {
        cout << "- Compute expectedRound() ... ";
        save("round", round(_genRowVec));
        cout << "done." << endl;
      }

      void expectedSign() {
        cout << "- Compute expectedSign() ... ";
        save("sign", sign(_genRowVec));
        cout << "done." << endl;
      }

      void expectedSin() {
        cout << "- Compute expectedSin() ... ";
        save("sin", sin(_genRowVec));
        cout << "done." << endl;
      }

      void expectedAsin() {
        cout << "- Compute expectedAsin() ... ";
        save("asin", asin(_genRowVec));
        cout << "done." << endl;
      }

      void expectedSinh() {
        cout << "- Compute expectedSinh() ... ";
        save("sinh", sinh(_genRowVec));
        cout << "done." << endl;
      }

      void expectedAsinh() {
        cout << "- Compute expectedAsinh() ... ";
        save("asinh", asinh(_genRowVec));
        cout << "done." << endl;
      }

      void expectedCos() {
        cout << "- Compute expectedCos() ... ";
        save("cos", cos(_genRowVec));
        cout << "done." << endl;
      }

      void expectedAcos() {
        cout << "- Compute expectedAcos() ... ";
        save("acos", acos(_genRowVec));
        cout << "done." << endl;
      }

      void expectedCosh() {
        cout << "- Compute expectedCosh() ... ";
        save("cosh", cosh(_genRowVec));
        cout << "done." << endl;
      }

      void expectedAcosh() {
        cout << "- Compute expectedAcosh() ... ";
        save("acosh", acosh(_genRowVec));
        cout << "done." << endl;
      }

      void expectedTan() {
        cout << "- Compute expectedTan() ... ";
        save("tan", tan(_genRowVec));
        cout << "done." << endl;
      }

      void expectedAtan() {
        cout << "- Compute expectedAtan() ... ";
        save("atan", atan(_genRowVec));
        cout << "done." << endl;
      }

      void expectedTanh() {
        cout << "- Compute expectedTanh() ... ";
        save("tanh", tanh(_genRowVec));
        cout << "done." << endl;
      }

      void expectedAtanh() {
        cout << "- Compute expectedAtanh() ... ";
        save("atanh", atanh(_genRowVec));
        cout << "done." << endl;
      }

      void expectedCumsum() {
        cout << "- Compute expectedCumsum() ... ";
        save("cumsum", cumsum(_genRowVec));
        cout << "done." << endl;
      }

      void expectedHist() {
        cout << "- Compute expectedHist() ... ";
        // Unable to convert the result of hist(...) to Mat<double>
        Mat<uword> expected = hist(_genRowVec);
        expected.save("../data/expected/hist" + _fileSuffix + ".mat", raw_ascii);
        cout << "done." << endl;
      }

      void expectedSort() {
        cout << "- Compute expectedSort() ... ";
        save("sort", sort(_genRowVec));
        cout << "done." << endl;
      }

      void expectedSort_index() {
        cout << "- Compute expectedSort_index() ... ";
        // Unable to convert the result of sort_index(...) to Mat<double>
        Mat<uword> expected = sort_index(_genRowVec);
        expected.save("../data/expected/sort_index" + _fileSuffix + ".mat", raw_ascii);
        cout << "done." << endl;
      }

      void expectedStable_sort_index() {
        cout << "- Compute expectedStable_sort_index() ... ";
        // Unable to convert the result of stable_sort_index(...) to Mat<double>
        Mat<uword> expected = stable_sort_index(_genRowVec);
        expected.save("../data/expected/stable_sort_index" + _fileSuffix + ".mat", raw_ascii);
        cout << "done." << endl;
      }

      void expectedTrans() {
        cout << "- Compute expectedTrans() ... ";
        save("trans", trans(_genRowVec));
        cout << "done." << endl;
      }

      void expectedUnique() {
        cout << "- Compute expectedUnique() ... ";
        save("unique", unique(_genRowVec));
        cout << "done." << endl;
      }

      void expectedNegate() {
        cout << "- Compute expectedNegate() ... ";
        save("negate", -_genRowVec);
        cout << "done." << endl;
      }

      void expectedReciprocal() {
        cout << "- Compute expectedReciprocal() ... ";
        save("reciprocal", 1/_genRowVec);
        cout << "done." << endl;
      }

      void expectedToeplitz() {
        cout << "- Compute expectedToeplitz() ... ";
        save("toeplitz", toeplitz(_genRowVec));
        cout << "done." << endl;
      }

      void expectedCirc_toeplitz() {
        cout << "- Compute expectedCirc_toeplitz() ... ";
        save("circ_toeplitz", circ_toeplitz(_genRowVec));
        cout << "done." << endl;
      }

      void expectedAccu() {
        cout << "- Compute expectedAccu() ... ";
        save("accu", Mat<double>({accu(_genRowVec)}));
        cout << "done." << endl;
      }

      void expectedMin() {
        cout << "- Compute expectedMin() ... ";
        save("min", Mat<double>({min(_genRowVec)}));
        cout << "done." << endl;
      }

      void expectedMax() {
        cout << "- Compute expectedMax() ... ";
        save("max", Mat<double>({max(_genRowVec)}));
        cout << "done." << endl;
      }

      void expectedProd() {
        cout << "- Compute expectedProd() ... ";
        save("prod", Mat<double>({prod(_genRowVec)}));
        cout << "done." << endl;
      }

      void expectedSum() {
        cout << "- Compute expectedSum() ... ";
        save("sum", Mat<double>({sum(_genRowVec)}));
        cout << "done." << endl;
      }

      void expectedMean() {
        cout << "- Compute expectedMean() ... ";
        save("mean", Mat<double>({mean(_genRowVec)}));
        cout << "done." << endl;
      }

      void expectedMedian() {
        cout << "- Compute expectedMedian() ... ";
        save("median", Mat<double>({median(_genRowVec)}));
        cout << "done." << endl;
      }

      void expectedStddev() {
        cout << "- Compute expectedStddev() ... ";
        save("stddev", Mat<double>({stddev(_genRowVec)}));
        cout << "done." << endl;
      }

      void expectedVar() {
        cout << "- Compute expectedVar() ... ";
        save("var", Mat<double>({var(_genRowVec)}));
        cout << "done." << endl;
      }

      void expectedCor() {
        cout << "- Compute expectedCor() ... ";
        save("cor", Mat<double>({cor(_genRowVec)}));
        cout << "done." << endl;
      }

      void expectedCov() {
        cout << "- Compute expectedCov() ... ";
        save("cov", Mat<double>({cov(_genRowVec)}));
        cout << "done." << endl;
      }

      void expectedDiagmat() {
        cout << "- Compute expectedDiagmat() ... ";
        save("diagmat", diagmat(_genRowVec));
        cout << "done." << endl;
      }

      void expectedIs_finite() {
        cout << "- Compute expectedIs_finite() ... ";

        if(is_finite(_genRowVec)) {
          save("is_finite", Mat<double>({1.0}));
        } else {
          save("is_finite", Mat<double>({0.0}));
        }

        cout << "done." << endl;
      }

  };
}
