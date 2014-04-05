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
using arma::accu;

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

            expectedArmaAbs();
            expectedArmaEps();
            expectedArmaExp();
            expectedArmaExp2();
            expectedArmaExp10();
            expectedArmaTrunc_exp();
            expectedArmaLog();
            expectedArmaLog2();
            expectedArmaLog10();
            expectedArmaTrunc_log();
            expectedArmaSqrt();
            expectedArmaSquare();
            expectedArmaFloor();
            expectedArmaCeil();
            expectedArmaRound();
            expectedArmaSign();
            expectedArmaSin();
            expectedArmaAsin();
            expectedArmaSinh();
            expectedArmaAsinh();
            expectedArmaCos();
            expectedArmaAcos();
            expectedArmaCosh();
            expectedArmaAcosh();
            expectedArmaTan();
            expectedArmaAtan();
            expectedArmaTanh();
            expectedArmaAtanh();
            expectedArmaCond();
            expectedArmaRank();
            expectedArmaDiagvec();
            expectedArmaMin();
            expectedArmaMax();
            expectedArmaProd();
            expectedArmaMean();
            expectedArmaMedian();
            expectedArmaStddev();
            expectedArmaVar();
            expectedArmaCor();
            expectedArmaCov();
            expectedArmaCumsum();
            expectedArmaFliplr();
            expectedArmaFlipud();
            expectedArmaHist();
            expectedArmaSort();
            expectedArmaTrans();
            expectedArmaUnique();
            expectedArmaVectorise();
            expectedArmaLu();
            expectedArmaPinv();
            expectedArmaPrincomp();
            expectedArmaQr();
            expectedArmaQr_econ();
            expectedArmaSvd();
            expectedArmaSvd_econ();
            expectedArmaNegate();
            expectedArmaReciprocal();
            expectedArmaAccu();
          }

          cout << "done." << endl;
        }

    protected:
      Mat<double> _genMat;

      void expectedArmaAbs() {
        cout << "- Compute expectedArmaAbs() ... ";
        save<double>("Arma.abs", abs(_genMat));
        cout << "done." << endl;
      }

      void expectedArmaEps() {
        cout << "- Compute expectedArmaAbs() ... ";
        save<double>("Arma.eps", eps(_genMat));
        cout << "done." << endl;
      }

      void expectedArmaExp() {
        cout << "- Compute expectedArmaExp() ... ";
        save<double>("Arma.exp", exp(_genMat));
        cout << "done." << endl;
      }

      void expectedArmaExp2() {
        cout << "- Compute expectedArmaExp2() ... ";
        save<double>("Arma.exp2", exp2(_genMat));
        cout << "done." << endl;
      }

      void expectedArmaExp10() {
        cout << "- Compute expectedArmaExp10() ... ";
        save<double>("Arma.exp10", exp10(_genMat));
        cout << "done." << endl;
      }

      void expectedArmaTrunc_exp() {
        cout << "- Compute expectedArmaTrunc_exp() ... ";
        save<double>("Arma.trunc_exp", trunc_exp(_genMat));
        cout << "done." << endl;
      }

      void expectedArmaLog() {
        cout << "- Compute expectedArmaLog() ... ";
        save<double>("Arma.log", log(_genMat));
        cout << "done." << endl;
      }

      void expectedArmaLog2() {
        cout << "- Compute expectedArmaLog2() ... ";
        save<double>("Arma.log2", log2(_genMat));
        cout << "done." << endl;
      }

      void expectedArmaLog10() {
        cout << "- Compute expectedArmaLog10() ... ";
        save<double>("Arma.log10", log10(_genMat));
        cout << "done." << endl;
      }

      void expectedArmaTrunc_log() {
        cout << "- Compute expectedArmaTrunc_log() ... ";
        save<double>("Arma.trunc_log", trunc_log(_genMat));
        cout << "done." << endl;
      }

      void expectedArmaSqrt() {
        cout << "- Compute expectedArmaSqrt() ... ";
        save<double>("Arma.sqrt", sqrt(_genMat));
        cout << "done." << endl;
      }

      void expectedArmaSquare() {
        cout << "- Compute expectedArmaSquare() ... ";
        save<double>("Arma.square", square(_genMat));
        cout << "done." << endl;
      }

      void expectedArmaFloor() {
        cout << "- Compute expectedArmaFloor() ... ";
        save<double>("Arma.floor", floor(_genMat));
        cout << "done." << endl;
      }

      void expectedArmaCeil() {
        cout << "- Compute expectedArmaCeil() ... ";
        save<double>("Arma.ceil", ceil(_genMat));
        cout << "done." << endl;
      }

      void expectedArmaRound() {
        cout << "- Compute expectedArmaRound() ... ";
        save<double>("Arma.round", round(_genMat));
        cout << "done." << endl;
      }

      void expectedArmaSign() {
        cout << "- Compute expectedArmaSign() ... ";
        save<double>("Arma.sign", sign(_genMat));
        cout << "done." << endl;
      }

      void expectedArmaSin() {
        cout << "- Compute expectedArmaSin() ... ";
        save<double>("Arma.sin", sin(_genMat));
        cout << "done." << endl;
      }

      void expectedArmaAsin() {
        cout << "- Compute expectedArmaAsin() ... ";
        save<double>("Arma.asin", asin(_genMat));
        cout << "done." << endl;
      }

      void expectedArmaSinh() {
        cout << "- Compute expectedArmaSinh() ... ";
        save<double>("Arma.sinh", sinh(_genMat));
        cout << "done." << endl;
      }

      void expectedArmaAsinh() {
        cout << "- Compute expectedArmaAsinh() ... ";
        save<double>("Arma.asinh", asinh(_genMat));
        cout << "done." << endl;
      }

      void expectedArmaCos() {
        cout << "- Compute expectedArmaCos() ... ";
        save<double>("Arma.cos", cos(_genMat));
        cout << "done." << endl;
      }

      void expectedArmaAcos() {
        cout << "- Compute expectedArmaAcos() ... ";
        save<double>("Arma.acos", acos(_genMat));
        cout << "done." << endl;
      }

      void expectedArmaCosh() {
        cout << "- Compute expectedArmaCosh() ... ";
        save<double>("Arma.cosh", cosh(_genMat));
        cout << "done." << endl;
      }

      void expectedArmaAcosh() {
        cout << "- Compute expectedArmaAcosh() ... ";
        save<double>("Arma.acosh", acosh(_genMat));
        cout << "done." << endl;
      }

      void expectedArmaTan() {
        cout << "- Compute expectedArmaTan() ... ";
        save<double>("Arma.tan", tan(_genMat));
        cout << "done." << endl;
      }

      void expectedArmaAtan() {
        cout << "- Compute expectedArmaAtan() ... ";
        save<double>("Arma.atan", atan(_genMat));
        cout << "done." << endl;
      }

      void expectedArmaTanh() {
        cout << "- Compute expectedArmaTanh() ... ";
        save<double>("Arma.tanh", tanh(_genMat));
        cout << "done." << endl;
      }

      void expectedArmaAtanh() {
        cout << "- Compute expectedArmaAtanh() ... ";
        save<double>("Arma.atanh", atanh(_genMat));
        cout << "done." << endl;
      }

      void expectedArmaCond() {
        cout << "- Compute expectedArmaCond() ... ";
        save<double>("Arma.cond", Mat<double>({cond(_genMat)}));
        cout << "done." << endl;
      }

      void expectedArmaRank() {
        cout << "- Compute expectedArmaRank() ... ";
        save<double>("Arma.rank", Mat<double>({static_cast<double>(rank(_genMat))}));
        cout << "done." << endl;
      }

      void expectedArmaDiagvec() {
        cout << "- Compute expectedArmaDiagvec() ... ";
        save<double>("Arma.diagvec", diagvec(_genMat));
        cout << "done." << endl;
      }

      void expectedArmaMin() {
        cout << "- Compute expectedArmaMin() ... ";
        save<double>("Arma.min", min(_genMat));
        cout << "done." << endl;
      }

      void expectedArmaMax() {
        cout << "- Compute expectedArmaMax() ... ";
        save<double>("Arma.max", max(_genMat));
        cout << "done." << endl;
      }

      void expectedArmaProd() {
        cout << "- Compute expectedArmaProd() ... ";
        save<double>("Arma.prod", prod(_genMat));
        cout << "done." << endl;
      }

      void expectedArmaMean() {
        cout << "- Compute expectedArmaMean() ... ";
        save<double>("Arma.mean", mean(_genMat));
        cout << "done." << endl;
      }

      void expectedArmaMedian() {
        cout << "- Compute expectedArmaMedian() ... ";
        save<double>("Arma.median", median(_genMat));
        cout << "done." << endl;
      }

      void expectedArmaStddev() {
        cout << "- Compute expectedArmaStddev() ... ";
        save<double>("Arma.stddev", stddev(_genMat));
        cout << "done." << endl;
      }

      void expectedArmaVar() {
        cout << "- Compute expectedArmaVar() ... ";
        save<double>("Arma.var", var(_genMat));
        cout << "done." << endl;
      }

      void expectedArmaCor() {
        cout << "- Compute expectedArmaCor() ... ";
        save<double>("Arma.cor", cor(_genMat));
        cout << "done." << endl;
      }

      void expectedArmaCov() {
        cout << "- Compute expectedArmaCov() ... ";
        save<double>("Arma.cov", cov(_genMat));
        cout << "done." << endl;
      }

      void expectedArmaCumsum() {
        cout << "- Compute expectedArmaCumsum() ... ";
        save<double>("Arma.cumsum", cumsum(_genMat));
        cout << "done." << endl;
      }

      void expectedArmaFliplr() {
        cout << "- Compute expectedArmaFliplr() ... ";
        save<double>("Arma.fliplr", fliplr(_genMat));
        cout << "done." << endl;
      }

      void expectedArmaFlipud() {
        cout << "- Compute expectedArmaFlipud() ... ";
        save<double>("Arma.flipud", flipud(_genMat));
        cout << "done." << endl;
      }

      void expectedArmaHist() {
        cout << "- Compute expectedArmaHist() ... ";
        save<uword>("Arma.hist", hist(_genMat));
        cout << "done." << endl;
      }

      void expectedArmaSort() {
        cout << "- Compute expectedArmaSort() ... ";
        save<double>("Arma.sort", sort(_genMat));
        cout << "done." << endl;
      }

      void expectedArmaTrans() {
        cout << "- Compute expectedArmaTrans() ... ";
        save<double>("Arma.trans", trans(_genMat));
        cout << "done." << endl;
      }

      void expectedArmaUnique() {
        cout << "- Compute expectedArmaUnique() ... ";
        save<double>("Arma.unique", unique(_genMat));
        cout << "done." << endl;
      }

      void expectedArmaVectorise() {
        cout << "- Compute expectedArmaVectorise() ... ";
        save<double>("Arma.vectorise", vectorise(_genMat));
        cout << "done." << endl;
      }

      void expectedArmaLu() {
        cout << "- Compute expectedArmaLu() ... ";

        Mat<double> L, U;

        if(lu(L, U, _genMat)) {
          save<double>("Arma.lu", Mat<double>({1}));
        } else {
          save<double>("Arma.lu", Mat<double>({0}));
        }

        cout << "done." << endl;
      }

      void expectedArmaPinv() {
        cout << "- Compute expectedArmaPinv() ... ";
        save<double>("Arma.pinv", pinv(_genMat));
        cout << "done." << endl;
      }

      void expectedArmaPrincomp() {
        cout << "- Compute expectedArmaPrincomp() ... ";

        Mat<double> coeff, score;
        Col<double> latent, tsquared;

        princomp(coeff, score, latent, tsquared, _genMat);

        save<double>("Arma.princompLatent", Mat<double>(latent));

        cout << "done." << endl;

      }

      void expectedArmaQr() {
        cout << "- Compute expectedArmaQr() ... ";

        Mat<double> Q, R;

        qr(Q, R, _genMat);

        save<double>("Arma.qrQ", Q);
        save<double>("Arma.qrR", R);

        cout << "done." << endl;
      }

      void expectedArmaQr_econ() {
        cout << "- Compute expectedArmaQr_econ() ... ";

        Mat<double> Q, R;

        qr_econ(Q, R, _genMat);

        save<double>("Arma.qr_econQ", Q);
        save<double>("Arma.qr_econR", R);

        cout << "done." << endl;
      }

      void expectedArmaSvd() {
        cout << "- Compute expectedArmaSvd() ... ";

        Mat<double> U, V;
        Col<double> s;

        svd(U, s, V, _genMat);
        save<double>("Arma.svd", Mat<double>(s));

        cout << "done." << endl;
      }

      void expectedArmaSvd_econ() {
        cout << "- Compute expectedArmaSvd_econ() ... ";

        Mat<double> U, V;
        Col<double> s;

        svd_econ(U, s, V, _genMat);
        save<double>("Arma.svd_econ", Mat<double>(s));

        cout << "done." << endl;
      }

      void expectedArmaNegate() {
        cout << "- Compute expectedArmaNegate() ... ";
        save<double>("Arma.negate", -_genMat);
        cout << "done." << endl;
      }

      void expectedArmaReciprocal() {
        cout << "- Compute expectedArmaReciprocal() ... ";
        save<double>("Arma.reciprocal", 1/_genMat);
        cout << "done." << endl;
      }

      void expectedArmaAccu() {
        cout << "- Compute expectedArmaAccu() ... ";
        save<double>("Arma.accu", Mat<double>({accu(_genMat)}));
        cout << "done." << endl;
      }
  };
}
