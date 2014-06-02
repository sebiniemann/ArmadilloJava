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

#include <cmath>
using std::log;
using std::sqrt;
using std::pow;

#include <fstream>
using std::ofstream;

#include <streambuf>
using std::streambuf;

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
  class ExpectedGenColVec : public Expected {
    public:
      ExpectedGenColVec() {
        cout << "Compute ExpectedGenColVec(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
          InputClass::GenColVec
        });

        for (vector<pair<string, void*>> input : inputs) {
          _fileSuffix = "";

          int n = 0;
          for (pair<string, void*> value : input) {
            switch (n) {
              case 0:
                _fileSuffix += value.first;
                _genColVec = *static_cast<Col<double>*>(value.second);
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
          expectedArmaCumsum();
          expectedArmaHist();
          expectedArmaSort();
          expectedArmaSort_index();
          expectedArmaStable_sort_index();
          expectedArmaTrans();
          expectedArmaUnique();
          expectedArmaNegate();
          expectedArmaReciprocal();
          expectedArmaToeplitz();
          expectedArmaCirc_toeplitz();
          expectedArmaAccu();
          expectedArmaMin();
          expectedArmaMax();
          expectedArmaProd();
          expectedArmaSum();
          expectedArmaMean();
          expectedArmaMedian();
          expectedArmaStddev();
          expectedArmaVar();
          expectedArmaCor();
          expectedArmaCov();
          expectedArmaDiagmat();
          expectedArmaIs_finite();
          expectedMat();
			expectedColVecSize();
			expectedColVecT();
			expectedColVecPrint();
			expectedColVecRaw_print();
			
			
        }

        cout << "done." << endl;
      }

    protected:
      Col<double> _genColVec;

      void expectedArmaAbs() {
        cout << "- Compute expectedArmaAbs() ... ";
        save<double>("Arma.abs", abs(_genColVec));
        cout << "done." << endl;
      }

      void expectedArmaEps() {
        cout << "- Compute expectedArmaEps() ... ";
        save<double>("Arma.eps", eps(_genColVec));
        cout << "done." << endl;
      }

      void expectedArmaExp() {
        cout << "- Compute expectedArmaExp() ... ";
        save<double>("Arma.exp", exp(_genColVec));
        cout << "done." << endl;
      }

      void expectedArmaExp2() {
        cout << "- Compute expectedArmaExp2() ... ";
        save<double>("Arma.exp2", exp2(_genColVec));
        cout << "done." << endl;
      }

      void expectedArmaExp10() {
        cout << "- Compute expectedArmaExp10() ... ";
        save<double>("Arma.exp10", exp10(_genColVec));
        cout << "done." << endl;
      }

      void expectedArmaTrunc_exp() {
        cout << "- Compute expectedArmaTrunc_exp() ... ";
        save<double>("Arma.trunc_exp", trunc_exp(_genColVec));
        cout << "done." << endl;
      }

      void expectedArmaLog() {
        cout << "- Compute expectedArmaLog() ... ";
        save<double>("Arma.log", log(_genColVec));
        cout << "done." << endl;
      }

      void expectedArmaLog2() {
        cout << "- Compute expectedArmaLog2() ... ";
        save<double>("Arma.log2", log2(_genColVec));
        cout << "done." << endl;
      }

      void expectedArmaLog10() {
        cout << "- Compute expectedArmaLog10() ... ";
        save<double>("Arma.log10", log10(_genColVec));
        cout << "done." << endl;
      }

      void expectedArmaTrunc_log() {
        cout << "- Compute expectedArmaTrunc_log() ... ";
        save<double>("Arma.trunc_log", trunc_log(_genColVec));
        cout << "done." << endl;
      }

      void expectedArmaSqrt() {
        cout << "- Compute expectedArmaSqrt() ... ";
        save<double>("Arma.sqrt", sqrt(_genColVec));
        cout << "done." << endl;
      }

      void expectedArmaSquare() {
        cout << "- Compute expectedArmaSquare() ... ";
        save<double>("Arma.square", square(_genColVec));
        cout << "done." << endl;
      }

      void expectedArmaFloor() {
        cout << "- Compute expectedArmaFloor() ... ";
        save<double>("Arma.floor", floor(_genColVec));
        cout << "done." << endl;
      }

      void expectedArmaCeil() {
        cout << "- Compute expectedArmaCeil() ... ";
        save<double>("Arma.ceil", ceil(_genColVec));
        cout << "done." << endl;
      }

      void expectedArmaRound() {
        cout << "- Compute expectedArmaRound() ... ";
        save<double>("Arma.round", round(_genColVec));
        cout << "done." << endl;
      }

      void expectedArmaSign() {
        cout << "- Compute expectedArmaSign() ... ";
        save<double>("Arma.sign", sign(_genColVec));
        cout << "done." << endl;
      }

      void expectedArmaSin() {
        cout << "- Compute expectedArmaSin() ... ";
        save<double>("Arma.sin", sin(_genColVec));
        cout << "done." << endl;
      }

      void expectedArmaAsin() {
        cout << "- Compute expectedArmaAsin() ... ";
        save<double>("Arma.asin", asin(_genColVec));
        cout << "done." << endl;
      }

      void expectedArmaSinh() {
        cout << "- Compute expectedArmaSinh() ... ";
        save<double>("Arma.sinh", sinh(_genColVec));
        cout << "done." << endl;
      }

      void expectedArmaAsinh() {
        cout << "- Compute expectedArmaAsinh() ... ";
        save<double>("Arma.asinh", asinh(_genColVec));
        cout << "done." << endl;
      }

      void expectedArmaCos() {
        cout << "- Compute expectedArmaCos() ... ";
        save<double>("Arma.cos", cos(_genColVec));
        cout << "done." << endl;
      }

      void expectedArmaAcos() {
        cout << "- Compute expectedArmaAcos() ... ";
        save<double>("Arma.acos", acos(_genColVec));
        cout << "done." << endl;
      }

      void expectedArmaCosh() {
        cout << "- Compute expectedArmaCosh() ... ";
        save<double>("Arma.cosh", cosh(_genColVec));
        cout << "done." << endl;
      }

      void expectedArmaAcosh() {
        cout << "- Compute expectedArmaAcosh() ... ";

        /*
         * acosh behaves buggy on some systems, with acosh(inf) = nan instead of inf
         */
        //save<double>("Arma.acosh", acosh(_genColVec));

        Mat<double> expected = _genColVec;
        expected.transform([](double value) {
          return log(value + sqrt(pow(value, 2) - 1));
        });
        save<double>("Arma.acosh", expected);

        cout << "done." << endl;
      }

      void expectedArmaTan() {
        cout << "- Compute expectedArmaTan() ... ";
        save<double>("Arma.tan", tan(_genColVec));
        cout << "done." << endl;
      }

      void expectedArmaAtan() {
        cout << "- Compute expectedArmaAtan() ... ";
        save<double>("Arma.atan", atan(_genColVec));
        cout << "done." << endl;
      }

      void expectedArmaTanh() {
        cout << "- Compute expectedArmaTanh() ... ";
        save<double>("Arma.tanh", tanh(_genColVec));
        cout << "done." << endl;
      }

      void expectedArmaAtanh() {
        cout << "- Compute expectedArmaAtanh() ... ";
        save<double>("Arma.atanh", atanh(_genColVec));
        cout << "done." << endl;
      }

      void expectedArmaCumsum() {
        cout << "- Compute expectedArmaCumsum() ... ";
        save<double>("Arma.cumsum", cumsum(_genColVec));
        cout << "done." << endl;
      }

      void expectedArmaHist() {
        cout << "- Compute expectedArmaHist() ... ";
        save<uword>("Arma.hist", hist(_genColVec));
        cout << "done." << endl;
      }

      void expectedArmaSort() {
        if(!_genColVec.is_finite()) {
          return;
        }

        cout << "- Compute expectedArmaSort() ... ";
        save<double>("Arma.sort", sort(_genColVec));
        cout << "done." << endl;
      }

      void expectedArmaSort_index() {
        if(!_genColVec.is_finite()) {
          return;
        }

        cout << "- Compute expectedArmaSort_index() ... ";
        save<uword>("Arma.sort_index", sort_index(_genColVec));
        cout << "done." << endl;
      }

      void expectedArmaStable_sort_index() {
        if(!_genColVec.is_finite()) {
          return;
        }

        cout << "- Compute expectedArmaStable_sort_index() ... ";
        save<uword>("Arma.stable_sort_index", stable_sort_index(_genColVec));
        cout << "done." << endl;
      }

      void expectedArmaTrans() {
        cout << "- Compute expectedArmaTrans() ... ";
        save<double>("Arma.trans", trans(_genColVec));
        cout << "done." << endl;
      }

      void expectedArmaUnique() {
        cout << "- Compute expectedArmaUnique() ... ";
        save<double>("Arma.unique", unique(_genColVec));
        cout << "done." << endl;
      }

      void expectedArmaNegate() {
        cout << "- Compute expectedArmaNegate() ... ";
        save<double>("Arma.negate", -_genColVec);
        cout << "done." << endl;
      }

      void expectedArmaReciprocal() {
        cout << "- Compute expectedArmaReciprocal() ... ";
        save<double>("Arma.reciprocal", 1/_genColVec);
        cout << "done." << endl;
      }

      void expectedArmaToeplitz() {
        cout << "- Compute expectedArmaToeplitz() ... ";
        save<double>("Arma.toeplitz", toeplitz(_genColVec));
        cout << "done." << endl;
      }

      void expectedArmaCirc_toeplitz() {
        cout << "- Compute expectedArmaCirc_toeplitz() ... ";
        save<double>("Arma.circ_toeplitz", circ_toeplitz(_genColVec));
        cout << "done." << endl;
      }

      void expectedArmaAccu() {
        cout << "- Compute expectedArmaAccu() ... ";
        save<double>("Arma.accu", Mat<double>({accu(_genColVec)}));
        cout << "done." << endl;
      }

      void expectedArmaMin() {
        cout << "- Compute expectedArmaMin() ... ";
        save<double>("Arma.min", Mat<double>({min(_genColVec)}));
        cout << "done." << endl;
      }

      void expectedArmaMax() {
        cout << "- Compute expectedArmaMax() ... ";
        save<double>("Arma.max", Mat<double>({max(_genColVec)}));
        cout << "done." << endl;
      }

      void expectedArmaProd() {
        cout << "- Compute expectedArmaProd() ... ";
        save<double>("Arma.prod", Mat<double>({prod(_genColVec)}));
        cout << "done." << endl;
      }

      void expectedArmaSum() {
        cout << "- Compute expectedArmaSum() ... ";
        save<double>("Arma.sum", Mat<double>({sum(_genColVec)}));
        cout << "done." << endl;
      }

      void expectedArmaMean() {
        cout << "- Compute expectedArmaMean() ... ";
        save<double>("Arma.mean", Mat<double>({mean(_genColVec)}));
        cout << "done." << endl;
      }

      void expectedArmaMedian() {
        cout << "- Compute expectedArmaMedian() ... ";
        save<double>("Arma.median", Mat<double>({median(_genColVec)}));
        cout << "done." << endl;
      }

      void expectedArmaStddev() {
        cout << "- Compute expectedArmaStddev() ... ";
        save<double>("Arma.stddev", Mat<double>({stddev(_genColVec)}));
        cout << "done." << endl;
      }

      void expectedArmaVar() {
        cout << "- Compute expectedArmaVar() ... ";
        save<double>("Arma.var", Mat<double>({var(_genColVec)}));
        cout << "done." << endl;
      }

      void expectedArmaCor() {
        cout << "- Compute expectedArmaCor() ... ";
        save<double>("Arma.cor", Mat<double>({cor(_genColVec)}));
        cout << "done." << endl;
      }

      void expectedArmaCov() {
        cout << "- Compute expectedArmaCov() ... ";
        save<double>("Arma.cov", Mat<double>({cov(_genColVec)}));
        cout << "done." << endl;
      }

      void expectedArmaDiagmat() {
        cout << "- Compute expectedArmaDiagmat() ... ";
        save<double>("Arma.diagmat", diagmat(_genColVec));
        cout << "done." << endl;
      }

      void expectedArmaIs_finite() {
        cout << "- Compute expectedArmaIs_finite() ... ";

        if(is_finite(_genColVec)) {
          save<double>("Arma.is_finite", Mat<double>({1.0}));
        } else {
          save<double>("Arma.is_finite", Mat<double>({0.0}));
        }

        cout << "done." << endl;
      }

      void expectedMat() {
        cout << "- Compute expectedMat() ... ";
        save<double>("Mat", _genColVec);
        cout << "done." << endl;
      }
	  
	  void expectedColVecSize() {
		  cout << "- Compute expectedColVecSize() ... ";
		  save<double>("Col.size", Mat<double>({static_cast<double>(_genColVec.size())}));
		  cout << "done." << endl;
      }
	  
	  void expectedColVecT() {
		  cout << "- Compute expectedColVecT() ... ";
		  save<double>("Col.t", _genColVec.t());
		  cout << "done." << endl;
      }
	  
	  void expectedColVecPrint() {
		  cout << "- Compute expectedColVecPrint() ... ";
		  
		  ofstream expected(_filepath + "Col.print(" + _fileSuffix + ").txt");
		  streambuf* previousBuffer = cout.rdbuf(expected.rdbuf());
		  
		  _genColVec.print();
		  
		  cout.rdbuf(previousBuffer);
		  
		  cout << "done." << endl;
      }
	  
	  void expectedColVecRaw_print() {
		  cout << "- Compute expectedColVecRaw_print() ... ";
		  
		  ofstream expected(_filepath + "Col.raw_print(" + _fileSuffix + ").txt");
		  streambuf* previousBuffer = cout.rdbuf(expected.rdbuf());
		  
		  _genColVec.raw_print();
		  
		  cout.rdbuf(previousBuffer);
		  
		  cout << "done." << endl;
      }

  };
}
