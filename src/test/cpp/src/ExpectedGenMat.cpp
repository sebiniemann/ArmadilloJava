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
using arma::cond;
using arma::rank;
using arma::diagvec;
using arma::min;
using arma::max;
using arma::prod;
using arma::mean;
using arma::median;
using arma::stddev;
using arma::var;
using arma::cor;
using arma::cov;
using arma::cumsum;
using arma::fliplr;
using arma::flipud;
using arma::hist;
using arma::sort;
using arma::trans;
using arma::unique;
using arma::vectorise;
using arma::lu;
using arma::pinv;
using arma::princomp;
using arma::qr;
using arma::qr_econ;
using arma::svd;
using arma::svd_econ;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedGenMat : public Expected {
    public:
      ExpectedGenMat() {
        cout << "Compute ExpectedGenMat(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({InputClass::GenMat});

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
            expectedCond();
            expectedRank();
            expectedDiagvec();
            expectedMin();
            expectedMax();
            expectedProd();
            expectedMean();
            expectedMedian();
            expectedStddev();
            expectedVar();
            expectedCor();
            expectedCov();
            expectedCumsum();
            expectedFliplr();
            expectedFlipud();
            expectedHist();
            expectedSort();
            expectedTrans();
            expectedUnique();
            expectedVectorise();
            expectedLu();
            expectedPinv();
            expectedPrincomp();
            expectedQr();
            expectedQr_econ();
            expectedSvd();
            expectedSvd_econ();
            expectedNegate();
            expectedReciprocal();
          }

          cout << "done." << endl;
        }

    protected:
      Mat<double> _genMat;

      void expectedAbs() {
        cout << "- Compute expectedAbs() ... ";
        save("abs", abs(_genMat));
        cout << "done." << endl;
      }

      void expectedEps() {
        cout << "- Compute expectedAbs() ... ";
        save("eps", eps(_genMat));
        cout << "done." << endl;
      }

      void expectedExp() {
        cout << "- Compute expectedExp() ... ";
        save("exp", exp(_genMat));
        cout << "done." << endl;
      }

      void expectedExp2() {
        cout << "- Compute expectedExp2() ... ";
        save("exp2", exp2(_genMat));
        cout << "done." << endl;
      }

      void expectedExp10() {
        cout << "- Compute expectedExp10() ... ";
        save("exp10", exp10(_genMat));
        cout << "done." << endl;
      }

      void expectedTrunc_exp() {
        cout << "- Compute expectedTrunc_exp() ... ";
        save("trunc_exp", trunc_exp(_genMat));
        cout << "done." << endl;
      }

      void expectedLog() {
        cout << "- Compute expectedLog() ... ";
        save("log", log(_genMat));
        cout << "done." << endl;
      }

      void expectedLog2() {
        cout << "- Compute expectedLog2() ... ";
        save("log2", log2(_genMat));
        cout << "done." << endl;
      }

      void expectedLog10() {
        cout << "- Compute expectedLog10() ... ";
        save("log10", log10(_genMat));
        cout << "done." << endl;
      }

      void expectedTrunc_log() {
        cout << "- Compute expectedTrunc_log() ... ";
        save("trunc_log", trunc_log(_genMat));
        cout << "done." << endl;
      }

      void expectedSquare() {
        cout << "- Compute expectedSquare() ... ";
        save("square", square(_genMat));
        cout << "done." << endl;
      }

      void expectedFloor() {
        cout << "- Compute expectedFloor() ... ";
        save("floor", floor(_genMat));
        cout << "done." << endl;
      }

      void expectedCeil() {
        cout << "- Compute expectedCeil() ... ";
        save("ceil", ceil(_genMat));
        cout << "done." << endl;
      }

      void expectedRound() {
        cout << "- Compute expectedRound() ... ";
        save("round", round(_genMat));
        cout << "done." << endl;
      }

      void expectedSign() {
        cout << "- Compute expectedSign() ... ";
        save("sign", sign(_genMat));
        cout << "done." << endl;
      }

      void expectedSin() {
        cout << "- Compute expectedSin() ... ";
        save("sin", sin(_genMat));
        cout << "done." << endl;
      }

      void expectedAsin() {
        cout << "- Compute expectedAsin() ... ";
        save("asin", asin(_genMat));
        cout << "done." << endl;
      }

      void expectedSinh() {
        cout << "- Compute expectedSinh() ... ";
        save("sinh", sinh(_genMat));
        cout << "done." << endl;
      }

      void expectedAsinh() {
        cout << "- Compute expectedAsinh() ... ";
        save("asinh", asinh(_genMat));
        cout << "done." << endl;
      }

      void expectedCos() {
        cout << "- Compute expectedCos() ... ";
        save("cos", cos(_genMat));
        cout << "done." << endl;
      }

      void expectedAcos() {
        cout << "- Compute expectedAcos() ... ";
        save("acos", acos(_genMat));
        cout << "done." << endl;
      }

      void expectedCosh() {
        cout << "- Compute expectedCosh() ... ";
        save("cosh", cosh(_genMat));
        cout << "done." << endl;
      }

      void expectedAcosh() {
        cout << "- Compute expectedAcosh() ... ";
        save("acosh", acosh(_genMat));
        cout << "done." << endl;
      }

      void expectedTan() {
        cout << "- Compute expectedTan() ... ";
        save("tan", tan(_genMat));
        cout << "done." << endl;
      }

      void expectedAtan() {
        cout << "- Compute expectedAtan() ... ";
        save("atan", atan(_genMat));
        cout << "done." << endl;
      }

      void expectedTanh() {
        cout << "- Compute expectedTanh() ... ";
        save("tanh", tanh(_genMat));
        cout << "done." << endl;
      }

      void expectedAtanh() {
        cout << "- Compute expectedAtanh() ... ";
        save("atanh", atanh(_genMat));
        cout << "done." << endl;
      }

      void expectedCond() {
        cout << "- Compute expectedCond() ... ";
        save("cond", Mat<double>({cond(_genMat)}));
        cout << "done." << endl;
      }

      void expectedRank() {
        cout << "- Compute expectedRank() ... ";
        save("rank", Mat<double>({static_cast<double>(rank(_genMat))}));
        cout << "done." << endl;
      }

      void expectedDiagvec() {
        cout << "- Compute expectedDiagvec() ... ";
        save("diagvec", diagvec(_genMat));
        cout << "done." << endl;
      }

      void expectedMin() {
        cout << "- Compute expectedMin() ... ";
        save("min", min(_genMat));
        cout << "done." << endl;
      }

      void expectedMax() {
        cout << "- Compute expectedMax() ... ";
        save("max", max(_genMat));
        cout << "done." << endl;
      }

      void expectedProd() {
        cout << "- Compute expectedProd() ... ";
        save("prod", prod(_genMat));
        cout << "done." << endl;
      }

      void expectedMean() {
        cout << "- Compute expectedMean() ... ";
        save("mean", mean(_genMat));
        cout << "done." << endl;
      }

      void expectedMedian() {
        cout << "- Compute expectedMedian() ... ";
        save("median", median(_genMat));
        cout << "done." << endl;
      }

      void expectedStddev() {
        cout << "- Compute expectedStddev() ... ";
        save("stddev", stddev(_genMat));
        cout << "done." << endl;
      }

      void expectedVar() {
        cout << "- Compute expectedVar() ... ";
        save("var", var(_genMat));
        cout << "done." << endl;
      }

      void expectedCor() {
        cout << "- Compute expectedCor() ... ";
        save("cor", cor(_genMat));
        cout << "done." << endl;
      }

      void expectedCov() {
        cout << "- Compute expectedCov() ... ";
        save("cov", cov(_genMat));
        cout << "done." << endl;
      }

      void expectedCumsum() {
        cout << "- Compute expectedCumsum() ... ";
        save("cumsum", cumsum(_genMat));
        cout << "done." << endl;
      }

      void expectedFliplr() {
        cout << "- Compute expectedFliplr() ... ";
        save("fliplr", fliplr(_genMat));
        cout << "done." << endl;
      }

      void expectedFlipud() {
        cout << "- Compute expectedFlipud() ... ";
        save("flipud", flipud(_genMat));
        cout << "done." << endl;
      }

      void expectedHist() {
        cout << "- Compute expectedHist() ... ";
        // Unable to convert the result of hist(...) to Mat<double>
        Mat<uword> expected = hist(_genMat);
        expected.save("../data/expected/hist" + _fileSuffix + ".mat", raw_ascii);
        cout << "done." << endl;
      }

      void expectedSort() {
        cout << "- Compute expectedSort() ... ";
        save("sort", sort(_genMat));
        cout << "done." << endl;
      }

      void expectedTrans() {
        cout << "- Compute expectedTrans() ... ";
        save("trans", trans(_genMat));
        cout << "done." << endl;
      }

      void expectedUnique() {
        cout << "- Compute expectedUnique() ... ";
        save("unique", unique(_genMat));
        cout << "done." << endl;
      }

      void expectedVectorise() {
        cout << "- Compute expectedVectorise() ... ";
        save("vectorise", vectorise(_genMat));
        cout << "done." << endl;
      }

      void expectedLu() {
        cout << "- Compute expectedLu() ... ";

        Mat<double> L, U;

        if(lu(L, U, _genMat)) {
          save("lu", Mat<double>({1}));
        } else {
          save("lu", Mat<double>({0}));
        }

        cout << "done." << endl;
      }

      void expectedPinv() {
        cout << "- Compute expectedPinv() ... ";
        save("pinv", pinv(_genMat));
        cout << "done." << endl;
      }

      void expectedPrincomp() {
        cout << "- Compute expectedPrincomp() ... ";

        Mat<double> coeff, score;
        Col<double> latent, tsquared;

        princomp(coeff, score, latent, tsquared, _genMat);

        save("princompLatent", Mat<double>(latent));

        cout << "done." << endl;

      }

      void expectedQr() {
        cout << "- Compute expectedQr() ... ";

        Mat<double> Q, R;

        qr(Q, R, _genMat);

        save("qrQ", Q);
        save("qrR", R);

        cout << "done." << endl;
      }

      void expectedQr_econ() {
        cout << "- Compute expectedQr_econ() ... ";

        Mat<double> Q, R;

        qr_econ(Q, R, _genMat);

        save("qr_econQ", Q);
        save("qr_econR", R);

        cout << "done." << endl;
      }

      void expectedSvd() {
        cout << "- Compute expectedSvd() ... ";

        Mat<double> U, V;
        Col<double> s;

        svd(U, s, V, _genMat);
        save("svd", Mat<double>(s));

        cout << "done." << endl;
      }

      void expectedSvd_econ() {
        cout << "- Compute expectedSvd_econ() ... ";

        Mat<double> U, V;
        Col<double> s;

        svd_econ(U, s, V, _genMat);
        save("svd_econ", Mat<double>(s));

        cout << "done." << endl;
      }

      void expectedNegate() {
        cout << "- Compute expectedNegate() ... ";
        save("negate", -_genMat);
        cout << "done." << endl;
      }

      void expectedReciprocal() {
        cout << "- Compute expectedReciprocal() ... ";
        save("reciprocal", 1/_genMat);
        cout << "done." << endl;
      }
  };
}
