/*******************************************************************************
 * Copyright 2013 Sebastian Niemann <niemann@sra.uni-hannover.de> and contributors.
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
// http://arma.sourceforge.net/docs.html#config_hpp
#define ARMA_USE_CXX11 // Use C++11 features, such as initialiser lists

#include <iostream>
// EXIT_SUCCESS
using std::cout;
using std::endl;

#include <array>
using std::array;

#include <string>
using std::string;
using std::to_string;

#include <armadillo>
using arma::Mat;
using arma::Col;
using arma::uword;
using arma::raw_ascii;
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
using arma::pow;
using arma::sqrt;
using arma::square;
using arma::floor;
using arma::ceil;
using arma::round;
using arma::sign;
using arma::accu;
using arma::dot;
using arma::norm_dot;
using arma::det;
using arma::log_det;
using arma::norm;
using arma::rank;
using arma::trace;
using arma::min;
using arma::max;
using arma::mean;
using arma::median;
using arma::stddev;
using arma::var;
using arma::inv;
using arma::eig_sym;
using arma::pinv;
using arma::svd;
using arma::cor;
using arma::cov;
using arma::fliplr;
using arma::flipud;
using arma::diagmat;
using arma::trimatu;
using arma::trimatl;
using arma::symmatu;
using arma::symmatl;
using arma::join_horiz;
using arma::join_vert;
using arma::sum;
using arma::prod;
using arma::sort;
using arma::stable_sort_index;
using arma::is_finite;
using arma::trans;
using arma::hist;
using arma::histc;
using arma::repmat;
using arma::toeplitz;
using arma::circ_toeplitz;
using arma::linspace;
using arma::find;
using arma::cumsum;
using arma::conv;
using arma::kron;
using arma::unique;
using arma::any;
using arma::all;
using arma::datum;

bool isInvertable(const Mat<double>& matrix) {
  return (matrix.is_square() && rank(matrix) == matrix.n_rows);
}

bool isSymmetric(const Mat<double>& matrix) {
  int n = 0;
  for (unsigned int j = 0; j < matrix.n_cols; j++) {
    for (unsigned int i = 0; i < matrix.n_rows; i++) {
      if (i != j && matrix.at(n) != matrix.at(j, i)) {
        return false;
      }
      n++;
    }
  }

  return true;
}

void testArmaMatrixValuedElementWiseFunctionsTrigonometric() {
  Mat<double> input;
  input.load("./input/trigonometric.mat");

  Mat<double> expected;

  // sine
  expected = sin(input);
  expected.save("./expected/TestArmaMatrixValuedElementWiseFunctionsTrigonometric/testSin.mat", raw_ascii);
  expected = sinh(input);
  expected.save("./expected/TestArmaMatrixValuedElementWiseFunctionsTrigonometric/testSinh.mat", raw_ascii);
  expected = asinh(input);
  expected.save("./expected/TestArmaMatrixValuedElementWiseFunctionsTrigonometric/testAsinh.mat", raw_ascii);

  // cosine
  expected = cos(input);
  expected.save("./expected/TestArmaMatrixValuedElementWiseFunctionsTrigonometric/testCos.mat", raw_ascii);
  expected = cosh(input);
  expected.save("./expected/TestArmaMatrixValuedElementWiseFunctionsTrigonometric/testCosh.mat", raw_ascii);

  // tangent
  expected = tan(input);
  expected.save("./expected/TestArmaMatrixValuedElementWiseFunctionsTrigonometric/testTan.mat", raw_ascii);
  expected = atan(input);
  expected.save("./expected/TestArmaMatrixValuedElementWiseFunctionsTrigonometric/testAtan.mat", raw_ascii);
  expected = tanh(input);
  expected.save("./expected/TestArmaMatrixValuedElementWiseFunctionsTrigonometric/testTanh.mat", raw_ascii);
  
  input.load("./input/trigonometric.acosh.mat");
  expected = acosh(input);
  expected.save("./expected/TestArmaMatrixValuedElementWiseFunctionsTrigonometric/testAcosh.mat", raw_ascii);

  input.load("./input/trigonometric.acos.asin.mat");
  expected = acos(input);
  expected.save("./expected/TestArmaMatrixValuedElementWiseFunctionsTrigonometric/testAcos.mat", raw_ascii);
  expected = asin(input);
  expected.save("./expected/TestArmaMatrixValuedElementWiseFunctionsTrigonometric/testAsin.mat", raw_ascii);

  input.load("./input/trigonometric.atanh.mat");
  expected = atanh(input);
  expected.save("./expected/TestArmaMatrixValuedElementWiseFunctionsTrigonometric/testAtanh.mat", raw_ascii);


}

void testArmaMatrixValuedElementWiseFunctionsMiscellaneous() {
  Mat<double> input;
  input.load("./input/miscellaneous.mat");

  std::array<double, 23> powers = {-10, -5, -4, -3, -2, -1, -1 / 2.0, -1 / 3.0, -1 / 4.0, -1 / 5.0, -1 / 10.0, 0, 1 / 10.0, 1 / 5.0, 1 / 4.0, 1 / 3.0, 1 / 2.0, 1, 2, 3, 4, 5, 10};

  Mat<double> expected;

  expected = abs(input);
  expected.save("./expected/TestArmaMatrixValuedElementWiseFunctionsMiscellaneous/testAbs.mat", raw_ascii);
  expected = eps(input);
  expected.save("./expected/TestArmaMatrixValuedElementWiseFunctionsMiscellaneous/testEps.mat", raw_ascii);
  expected = exp(input);
  expected.save("./expected/TestArmaMatrixValuedElementWiseFunctionsMiscellaneous/testExp.mat", raw_ascii);
  expected = exp2(input);
  expected.save("./expected/TestArmaMatrixValuedElementWiseFunctionsMiscellaneous/testExp2.mat", raw_ascii);
  expected = exp10(input);
  expected.save("./expected/TestArmaMatrixValuedElementWiseFunctionsMiscellaneous/testExp10.mat", raw_ascii);
  expected = trunc_exp(input);
  expected.save("./expected/TestArmaMatrixValuedElementWiseFunctionsMiscellaneous/testTrunc_exp.mat", raw_ascii);
  expected = square(input);
  expected.save("./expected/TestArmaMatrixValuedElementWiseFunctionsMiscellaneous/testSquare.mat", raw_ascii);
  expected = floor(input);
  expected.save("./expected/TestArmaMatrixValuedElementWiseFunctionsMiscellaneous/testFloor.mat", raw_ascii);
  expected = ceil(input);
  expected.save("./expected/TestArmaMatrixValuedElementWiseFunctionsMiscellaneous/testCeil.mat", raw_ascii);
  expected = round(input);
  expected.save("./expected/TestArmaMatrixValuedElementWiseFunctionsMiscellaneous/testRound.mat", raw_ascii);
  
  input.load("./input/miscellaneous.log.pow.mat");
  expected = log(input);
  expected.save("./expected/TestArmaMatrixValuedElementWiseFunctionsMiscellaneous/testLog.mat", raw_ascii);
  expected = log2(input);
  expected.save("./expected/TestArmaMatrixValuedElementWiseFunctionsMiscellaneous/testLog2.mat", raw_ascii);
  expected = log10(input);
  expected.save("./expected/TestArmaMatrixValuedElementWiseFunctionsMiscellaneous/testLog10.mat", raw_ascii);
  expected = trunc_log(input);
  expected.save("./expected/TestArmaMatrixValuedElementWiseFunctionsMiscellaneous/testTrunc_log.mat", raw_ascii);

  for (double power : powers) {
    expected = pow(input, power);
    expected.save("./expected/TestArmaMatrixValuedElementWiseFunctionsMiscellaneous/testPow.p" + to_string(power) + ".mat", raw_ascii);
  }

  input.load("./input/miscellaneous.sqrt.mat");
  expected = sqrt(input);
  expected.save("./expected/TestArmaMatrixValuedElementWiseFunctionsMiscellaneous/testSqrt.mat", raw_ascii);
  
  input.load("./input/miscellaneous.sign.mat");
  expected = sign(input);
  expected.save("./expected/TestArmaMatrixValuedElementWiseFunctionsMiscellaneous/testSign.mat", raw_ascii);
}

void testArmaScalarValuedFunctionsOfVectorsMatrices() {
  std::array<double, 7> dimensions = {1, 2, 3, 4, 5, 10, 100};
  std::array<string, 5> matrices = {"zeros", "ones", "eye", "hankel", "hilbert"};

  string filename;
  Mat<double> input;

  Mat<double> expected;

  for (int numberOfRows : dimensions) {
    for (int numberOfColumns : dimensions) {
      for (string matrix : matrices) {
        if (matrix != "hilbert" || (numberOfRows < 10 && numberOfColumns < 10)) {
          filename = matrix + "." + to_string(numberOfRows) + "x" + to_string(numberOfColumns) + ".mat";
          input.load("./input/" + filename);

          expected = {accu(input)};
          expected.save("./expected/TestArmaScalarValuedFunctionsOfVectorsMatrices/testAccu." + filename, raw_ascii);

          if (input.is_square()) {
            expected = {det(input)};
            expected.save("./expected/TestArmaScalarValuedFunctionsOfVectorsMatrices/testDet." + filename, raw_ascii);

            expected = {trace(input)};
            expected.save("./expected/TestArmaScalarValuedFunctionsOfVectorsMatrices/testTrace." + filename, raw_ascii);
          }

          expected = {norm(input, 1)};
          expected.save("./expected/TestArmaScalarValuedFunctionsOfVectorsMatrices/testNorm.p1." + filename, raw_ascii);
          expected = {norm(input, 2)};
          expected.save("./expected/TestArmaScalarValuedFunctionsOfVectorsMatrices/testNorm.p2." + filename, raw_ascii);
          expected = {norm(input, "inf")};
          expected.save("./expected/TestArmaScalarValuedFunctionsOfVectorsMatrices/testNorm.pinf." + filename, raw_ascii);
          expected = {norm(input, "fro")};
          expected.save("./expected/TestArmaScalarValuedFunctionsOfVectorsMatrices/testNorm.pfro." + filename, raw_ascii);
          if (input.is_vec()) {
            expected = {norm(input, "-inf")};
            expected.save("./expected/TestArmaScalarValuedFunctionsOfVectorsMatrices/testNorm.p-inf." + filename, raw_ascii);
          }

          expected = {(double) rank(input)};
          expected.save("./expected/TestArmaScalarValuedFunctionsOfVectorsMatrices/testRank." + filename, raw_ascii);
        }
      }
    }
  }

  Mat<double> inputA;
  Mat<double> inputB;
  string matrixA;
  string matrixB;

  for (int dimension : dimensions) {
    for (unsigned int a = 0; a < matrices.size(); a++) {
      for (unsigned int b = a; b < matrices.size(); b++) {
        matrixA = matrices.at(a);
        matrixB = matrices.at(b);

        if ((matrixA != "hilbert" && matrixB != "hilbert") || (dimension < 10)) {
          filename = matrixA + "." + to_string(1) + "x" + to_string(dimension) + ".mat";
          inputA.load("./input/" + filename);

          filename = matrixB + "." + to_string(1) + "x" + to_string(dimension) + ".mat";
          inputB.load("./input/" + filename);

          expected = {dot(inputA, inputB)};
          expected.save("./expected/TestArmaScalarValuedFunctionsOfVectorsMatrices/testDot." + matrixA + "." + matrixB + ".1x" + to_string(dimension) + ".mat", raw_ascii);
          expected = {norm_dot(inputA, inputB)};
          expected.save("./expected/TestArmaScalarValuedFunctionsOfVectorsMatrices/testNorm_dot." + matrixA + "." + matrixB + ".1x" + to_string(dimension) + ".mat", raw_ascii);
        }
      }
    }
  }
}

void testArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic() {
  Mat<double> input;
  input.load("./input/statistics.mat");

  Mat<double> expected;

  expected = min(input, 0);
  expected.save("./expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testMin.d0.mat", raw_ascii);
  expected = min(input, 1);
  expected.save("./expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testMin.d1.mat", raw_ascii);
  expected = max(input, 0);
  expected.save("./expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testMax.d0.mat", raw_ascii);
  expected = max(input, 1);
  expected.save("./expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testMax.d1.mat", raw_ascii);
  expected = mean(input, 0);
  expected.save("./expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testMean.d0.mat", raw_ascii);
  expected = mean(input, 1);
  expected.save("./expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testMean.d1.mat", raw_ascii);
  expected = median(input, 0);
  expected.save("./expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testMedian.d0.mat", raw_ascii);
  expected = median(input, 1);
  expected.save("./expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testMedian.d1.mat", raw_ascii);
  expected = stddev(input, 0, 0);
  expected.save("./expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testStddev.n0.d0.mat", raw_ascii);
  expected = stddev(input, 1, 0);
  expected.save("./expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testStddev.n1.d0.mat", raw_ascii);
  expected = stddev(input, 0, 1);
  expected.save("./expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testStddev.n0.d1.mat", raw_ascii);
  expected = stddev(input, 1, 1);
  expected.save("./expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testStddev.n1.d1.mat", raw_ascii);
  expected = var(input, 0, 0);
  expected.save("./expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testVar.n0.d0.mat", raw_ascii);
  expected = var(input, 1, 0);
  expected.save("./expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testVar.n1.d0.mat", raw_ascii);
  expected = var(input, 0, 1);
  expected.save("./expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testVar.n0.d1.mat", raw_ascii);
  expected = var(input, 1, 1);
  expected.save("./expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testVar.n1.d1.mat", raw_ascii);
}

void testArmaDecomposition() {
  std::array<double, 7> dimensions = {1, 2, 3, 4, 5, 10, 100};
  std::array<string, 5> matrices = {"zeros", "ones", "eye", "hankel", "hilbert"};

  string filename;
  Mat<double> input;

  Col<double> eigenValues;
  Mat<double> eigenVectors;
  Mat<double> inverse;
  Mat<double> pseudoInverse;
  Col<double> singularValues;

  for (int numberOfRows : dimensions) {
    for (int numberOfColumns : dimensions) {
      for (string matrix : matrices) {
        if (matrix != "hilbert" || (numberOfRows < 10 && numberOfColumns < 10)) {
          filename = matrix + "." + to_string(numberOfRows) + "x" + to_string(numberOfColumns) + ".mat";
          input.load("./input/" + filename);

          if (matrix != "zeros") {
            pinv(pseudoInverse, input);
            pseudoInverse.save("./expected/TestArmaDecomposition/testPinv." + filename, raw_ascii);
          }

          svd(singularValues, input);
          singularValues.save("./expected/TestArmaDecomposition/testSvd." + filename, raw_ascii);

          if (input.is_square() && isSymmetric(input)) {
            eig_sym(eigenValues, eigenVectors, input);
            eigenValues.save("./expected/TestArmaDecomposition/testEig_sym.eigenValues." + filename, raw_ascii);
            eigenVectors.save("./expected/TestArmaDecomposition/testEig_sym.eigenVectors." + filename, raw_ascii);
          }

          if (isInvertable(input)) {
            inv(inverse, input);
            inverse.save("./expected/TestArmaDecomposition/testInv." + filename, raw_ascii);
          }
        }
      }
    }
  }
}

void testArmaMatrixValuedFunctionsOfVectorsMatricesSwap() {
  std::array<double, 7> dimensions = {1, 2, 3, 4, 5, 10, 100};
  std::array<string, 1> matrices = {"numbered"};

  string filename;
  Mat<double> input;

  Mat<double> expected;

  for (int numberOfRows : dimensions) {
    for (int numberOfColumns : dimensions) {
      for (string matrix : matrices) {
        filename = matrix + "." + to_string(numberOfRows) + "x" + to_string(numberOfColumns) + ".mat";
        input.load("./input/" + filename);

        expected = fliplr(input);
        expected.save("./expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesSwap/testFliplr." + filename, raw_ascii);
        expected = flipud(input);
        expected.save("./expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesSwap/testFlipud." + filename, raw_ascii);
      }
    }
  }
}

void testArmaMatrixValuedFunctionsOfVectorsMatricesReinterpret() {
  std::array<double, 7> dimensions = {1, 2, 3, 4, 5, 10, 100};
  std::array<string, 1> matrices = {"numbered"};

  string filename;
  Mat<double> input;

  Mat<double> expected;

  for (int numberOfRows : dimensions) {
    for (int numberOfColumns : dimensions) {
      for (string matrix : matrices) {
        filename = matrix + "." + to_string(numberOfRows) + "x" + to_string(numberOfColumns) + ".mat";
        input.load("./input/" + filename);

        if (input.is_square() || input.is_vec()) {
          expected = diagmat(input);
          expected.save("./expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesReinterpret/testDiagmat." + filename, raw_ascii);
        }

        if (input.is_square()) {
          expected = trimatu(input);
          expected.save("./expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesReinterpret/testTrimatu." + filename, raw_ascii);
          expected = trimatl(input);
          expected.save("./expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesReinterpret/testTrimatl." + filename, raw_ascii);
          expected = symmatu(input);
          expected.save("./expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesReinterpret/testSymmatu." + filename, raw_ascii);
          expected = symmatl(input);
          expected.save("./expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesReinterpret/testSymmatl." + filename, raw_ascii);
        }
      }
    }
  }
}

void testArmaMatrixValuedFunctionsOfVectorsMatricesMatrixJoins() {
  std::array<double, 7> dimensions = {1, 2, 3, 4, 5, 10, 100};
  std::array<string, 1> matrices = {"numbered"};

  string filename;

  string filenameA;
  string filenameB;
  Mat<double> inputA;
  Mat<double> inputB;

  Mat<double> expected;

  for (int numberOfRowsA : dimensions) {
    for (int numberOfColumnsA : dimensions) {
      for (string matrix : matrices) {
        filenameA = matrix + "." + to_string(numberOfRowsA) + "x" + to_string(numberOfColumnsA) + ".mat";
        inputA.load("./input/" + filenameA);

        for (int numberOfColumnsB : dimensions) {
          filenameB = matrix + "." + to_string(numberOfRowsA) + "x" + to_string(numberOfColumnsB) + ".mat";
          inputB.load("./input/" + filenameB);

          filename = matrix + "." + to_string(numberOfRowsA) + "x" + to_string(numberOfColumnsA) + "." + matrix + "." + to_string(numberOfRowsA) + "x" + to_string(numberOfColumnsB) + ".mat";

          expected = join_horiz(inputA, inputB);
          expected.save("./expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesMatrixJoins/testJoin_horiz." + filename, raw_ascii);
        }

        for (int numberOfRowsB : dimensions) {
          filenameB = matrix + "." + to_string(numberOfRowsB) + "x" + to_string(numberOfColumnsA) + ".mat";
          inputB.load("./input/" + filenameB);

          filename = matrix + "." + to_string(numberOfRowsA) + "x" + to_string(numberOfColumnsA) + "." + matrix + "." + to_string(numberOfRowsB) + "x" + to_string(numberOfColumnsA) + ".mat";

          expected = join_vert(inputA, inputB);
          expected.save("./expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesMatrixJoins/testJoin_vert." + filename, raw_ascii);
        }
      }
    }
  }
}

void testArmaMatrixValuedFunctionsOfVectorsMatricesReshapeResize() {
  std::array<double, 7> dimensions = {1, 2, 3, 4, 5, 10, 100};

  Mat<double> expected;

  Mat<double> input;
  input.load("./input/numbered.10x10.mat");

  string filename;

  for (int numberOfRows : dimensions) {
    for (int numberOfColumns : dimensions) {
      filename = "numbered.10x10." + to_string(numberOfRows) + "x" + to_string(numberOfColumns) + ".mat";

      expected = reshape(input, numberOfRows, numberOfColumns, 0);
      expected.save("./expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesReshapeResize/testReshape.d0." + filename, raw_ascii);
      expected = reshape(input, numberOfRows, numberOfColumns, 1);
      expected.save("./expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesReshapeResize/testReshape.d1." + filename, raw_ascii);
      expected = resize(input, numberOfRows, numberOfColumns);
      expected.save("./expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesReshapeResize/testResize." + filename, raw_ascii);
    }
  }
}

void testArmaMatrixValuedFunctionsOfVectorsMatricesStatistic() {
  Mat<double> input;
  input.load("./input/statistics.mat");

  Mat<double> expected;

  expected = cor(input, input, 0);
  expected.save("./expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesStatistic/testCor.n0.mat", raw_ascii);
  expected = cor(input, input, 1);
  expected.save("./expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesStatistic/testCor.n1.mat", raw_ascii);
  expected = cov(input, input, 0);
  expected.save("./expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesStatistic/testCov.n0.mat", raw_ascii);
  expected = cov(input, input, 1);
  expected.save("./expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesStatistic/testCov.n1.mat", raw_ascii);
}

void testArmaScalarVectorValuedFunctionsOfVectorsMatricesMiscellaneous() {
  Mat<double> input;
  input.load("./input/series.mat");

  Mat<double> expected;

  expected = prod(input, 0);
  expected.save("./expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesMiscellaneous/testProd.d0.mat", raw_ascii);
  expected = prod(input, 1);
  expected.save("./expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesMiscellaneous/testProd.d1.mat", raw_ascii);
  expected = sum(input, 0);
  expected.save("./expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesMiscellaneous/testSum.d0.mat", raw_ascii);
  expected = sum(input, 1);
  expected.save("./expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesMiscellaneous/testSum.d1.mat", raw_ascii);
}

void testArmaMatrixValuedFunctionsOfVectorsMatricesSort() {
  Mat<double> input;
  input.load("./input/series.mat");
  for (unsigned int n = 0; n < input.n_elem; n++) {
    double value = input.at(n);
    if (!is_finite(value)) {
      input.at(n) = 0;
    }
  }

  Mat<double> expected;

  expected = sort(input, 0, 0);
  expected.save("./expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesSort/testSort.t0.d0.mat", raw_ascii);
  expected = sort(input, 1, 0);
  expected.save("./expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesSort/testSort.t1.d0.mat", raw_ascii);
  expected = sort(input, 0, 1);
  expected.save("./expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesSort/testSort.t0.d1.mat", raw_ascii);
  expected = sort(input, 1, 1);
  expected.save("./expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesSort/testSort.t1.d1.mat", raw_ascii);

  input = vectorise(input);

  Col<uword> expectedVector;
  expectedVector = stable_sort_index(input, 0);
  expectedVector.save("./expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesSort/testStable_sort_index.t0.mat", raw_ascii);
  expectedVector = stable_sort_index(input, 1);
  expectedVector.save("./expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesSort/testStable_sort_index.t1.mat", raw_ascii);
}

void testArmaMatrixValuedFunctionsOfVectorsMatricesMiscellaneousParameterised() {
  std::array<double, 7> dimensions = {1, 2, 3, 4, 5, 10, 100};
  std::array<string, 1> matrices = {"numbered"};

  string filename;
  Mat<double> input;

  Mat<double> expected;

  for (int numberOfRows : dimensions) {
    for (int numberOfColumns : dimensions) {
      for (string matrix : matrices) {
        filename = matrix + "." + to_string(numberOfRows) + "x" + to_string(numberOfColumns) + ".mat";
        input.load("./input/" + filename);

        expected = trans(input);
        expected.save("./expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesMiscellaneousParameterised/testTrans." + filename, raw_ascii);

        for (int k = -numberOfRows + 1; k < numberOfColumns; k++) {
          expected = diagvec(input, k);
          expected.save("./expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesMiscellaneousParameterised/testDiagvec.k" + to_string(k) + "." + filename, raw_ascii);
        }

        expected = vectorise(input, 0);
        expected.save("./expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesMiscellaneousParameterised/testVectorise.d0." + filename, raw_ascii);
        expected = vectorise(input, 1);
        expected.save("./expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesMiscellaneousParameterised/testVectorise.d1." + filename, raw_ascii);
      }
    }
  }
}

void testArmaMatrixValuedFunctionsOfVectorsMatricesHistogramm() {
  Mat<double> input;
  input.load("./input/series.mat");

  std::array<int, 15> numberOfBins = {1, 2, 3, 4, 5, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
  Mat<double> centers = {-90, -45, -22.5, -9, -4.5, -1.8, -0.9, -0.27, -0.009, 0, 0.01, 0.3, 1, 2, 5, 10, 25, 50, 100};

  Mat<uword> expected;

  expected = hist(input, centers, 0);
  expected.save("./expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesHistogramm/testHist.centers.d0.mat", raw_ascii);
  expected = hist(input, centers, 1);
  expected.save("./expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesHistogramm/testHist.centers.d1.mat", raw_ascii);

  // uses the matrix centers as edges
  expected = histc(input, centers, 0);
  expected.save("./expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesHistogramm/testHistc.d0.mat", raw_ascii);
  expected = histc(input, centers, 1);
  expected.save("./expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesHistogramm/testHistc.d1.mat", raw_ascii);

  input = vectorise(input);

  for (int bins : numberOfBins) {
    expected = hist(input, bins);
    expected.save("./expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesHistogramm/testHist.n" + to_string(bins) + ".mat", raw_ascii);
  }
}

void testArmaMatrixGenerationMatrix() {
  std::array<double, 6> dimensions = {1, 2, 3, 4, 5, 10};

  Mat<double> expected;

  Mat<double> input;
  input.load("./input/numbered.10x10.mat");

  string filename;

  for (int numberOfRows : dimensions) {
    for (int numberOfColumns : dimensions) {
      filename = "numbered.10x10." + to_string(10 * numberOfRows) + "x" + to_string(10 * numberOfColumns) + ".mat";

      expected = repmat(input, numberOfRows, numberOfColumns);
      expected.save("./expected/TestArmaMatrixGenerationMatrix/testRepmat." + filename, raw_ascii);
    }
  }
}

void testArmaMatrixGenerationToeplitz() {
  std::array<double, 7> dimensions = {1, 2, 3, 4, 5, 10, 100};

  Mat<double> input;
  Mat<double> expected;
  string filename;

  for (int numberOfElements : dimensions) {
    filename = "numbered." + to_string(numberOfElements) + "x" + to_string(1) + ".mat";
    input.load("./input/" + filename);

    expected = toeplitz(input);
    expected.save("./expected/TestArmaMatrixGenerationToeplitz/testToeplitz." + filename, raw_ascii);
    expected = circ_toeplitz(input);
    expected.save("./expected/TestArmaMatrixGenerationToeplitz/testCirc_toeplitz." + filename, raw_ascii);

    filename = "numbered." + to_string(numberOfElements) + "x" + to_string(2) + ".mat";
    input.load("./input/" + filename);

    expected = toeplitz(input.col(0), input.col(1));
    expected.save("./expected/TestArmaMatrixGenerationToeplitz/testToeplitz." + filename, raw_ascii);
  }
}

void testArmaMatrixGenerationLinspace() {
  std::array<double, 9> elements = {1, 2, 3, 4, 5, 10, 100, 1000, 10000};

  Mat<double> expected;

  for (int numberOfElements : elements) {
    expected = linspace(-12345.6789, 9876.54321, numberOfElements);
    expected.save("./expected/TestArmaMatrixGenerationLinspace/testLinspace." + to_string(numberOfElements) + ".mat", raw_ascii);
  }
}

void testArmaMatrixValuedFunctionsOfVectorsMatricesMiscellaneousFind() {
  std::array<double, 9> elements = {0, 1, 2, 3, 4, 5, 10};

  Mat<double> input;
  input.load("./input/series.mat");

  Mat<uword> expected;

  for (int numberOfElements : elements) {
    expected = find(input, numberOfElements, "first");
    expected.save("./expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesMiscellaneousFind/testFind.k" + to_string(numberOfElements) + ".first.mat", raw_ascii);
    expected = find(input, numberOfElements, "last");
    expected.save("./expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesMiscellaneousFind/testFind.k" + to_string(numberOfElements) + ".last.mat", raw_ascii);
  }
}

void testArmaMatrixValuedFunctionsOfVectorsMatricesMiscellaneous() {
  Mat<double> input;
  input.load("./input/series.mat");

  Mat<double> expected;

  expected = cumsum(input, 0);
  expected.save("./expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesMiscellaneous/testCumsum.d0.mat", raw_ascii);
  expected = cumsum(input, 1);
  expected.save("./expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesMiscellaneous/testCumsum.d1.mat", raw_ascii);
  expected = conv(input.col(0), input.col(1));
  expected.save("./expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesMiscellaneous/testConv.mat", raw_ascii);
  expected = unique(input);
  expected.save("./expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesMiscellaneous/testUnique.mat", raw_ascii);

  Mat<double> inputA;
  inputA.load("./input/numbered.10x10.mat");

  Mat<double> inputB;
  string filename;

  int dimensions[] = {1, 2, 3, 4, 5, 10};

  for (int numberOfRows : dimensions) {
    for (int numberOfColumns : dimensions) {
      filename = "numbered." + to_string(numberOfRows) + "x" + to_string(numberOfColumns) + ".mat";
      inputB.load("./input/" + filename);

      expected = kron(inputA, inputB);
      expected.save("./expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesMiscellaneous/testKron.numbered.10x10." + filename, raw_ascii);
    }
  }

  std::array<string, 5> matrices = {"zeros", "ones", "eye", "hankel", "hilbert"};

  string filenameA;
  string filenameB;

  for (string matrixA : matrices) {
    for (string matrixB : matrices) {
      filenameA = matrixA + ".3x1.mat";
      inputA.load("./input/" + filenameA);

      filenameB = matrixB + ".3x1.mat";
      inputB.load("./input/" + filenameB);

      expected = cross(inputA, inputB);
      expected.save("./expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesMiscellaneous/testCross." + matrixA + "." + matrixB + ".mat", raw_ascii);
    }
  }
}

void testArmaScalarVectorValuedFunctionsOfVectorsMatricesLogic() {
  Mat<double> input;
  input.load("./input/logic.mat");

  Mat<uword> expected;

  expected = any(input, 0);
  expected.save("./expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesLogic/testAny.d0.mat", raw_ascii);
  expected = any(input, 1);
  expected.save("./expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesLogic/testAny.d1.mat", raw_ascii);
  expected = all(input, 0);
  expected.save("./expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesLogic/testAll.d0.mat", raw_ascii);
  expected = all(input, 1);
  expected.save("./expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesLogic/testAll.d1.mat", raw_ascii);
}

void testDatum() {
  Mat<double> expected = {
      datum::pi,
      datum::e,
      datum::euler,
      datum::gratio,
      datum::sqrt2,
      datum::eps,
      datum::log_min,
      datum::log_max,
      datum::nan,
      datum::inf,
      datum::m_u,
      datum::N_A,
      datum::k,
      datum::k_evk,
      datum::a_0,
      datum::mu_B,
      datum::Z_0,
      datum::G_0,
      datum::k_e,
      datum::eps_0,
      datum::m_e,
      datum::eV,
      datum::ec,
      datum::F,
      datum::alpha,
      datum::alpha_inv,
      datum::K_J,
      datum::mu_0,
      datum::phi_0,
      datum::R,
      datum::G,
      datum::h,
      datum::h_bar,
      datum::m_p,
      datum::R_inf,
      datum::c_0,
      datum::sigma,
      datum::R_k,
      datum::b
  };
  expected.save("./expected/TestDatum/datum.mat", raw_ascii);
}

void testMat(){
	std::array<double, 7> dimensions = {1, 2, 3, 4, 5, 10, 100};	
	string filename;
	
	string filenameA;
	string filenameB;
	Mat<double> inputA;
	Mat<double> inputB;
	
	Mat<double> expected;
	
	for (int numberOfColumnsA : dimensions) {
			filenameA = "ones.1x" + to_string(numberOfColumnsA) + ".mat";
			
			for (int numberOfColumnsB : dimensions) {
				if(numberOfColumnsB>numberOfColumnsA)continue;
				filenameB = "zeros.1x" + to_string(numberOfColumnsB) + ".mat";
				
				for(int i = 0;i < numberOfColumnsA-1;i++){
					filename = "ones.1x" + to_string(numberOfColumnsA) + ".zeros.1x" + to_string(numberOfColumnsB) + ".addOn"+ to_string(i) +".mat";
					inputA.load("./input/" + filenameA);
					inputB.load("./input/" + filenameB);
					inputA.insert_cols(i,inputB);
					inputA.save("./expected/TestMat/testInsert_cols." + filename, raw_ascii);
				}
			}
	}
	for (int numberOfRowsA : dimensions) {
		filenameA = "ones." + to_string(numberOfRowsA) + "x1.mat";
		
		for (int numberOfRowsB : dimensions) {
			if(numberOfRowsB>numberOfRowsA)continue;
			filenameB = "zeros." + to_string(numberOfRowsB) + "x1.mat";
			
			for(int i = 0;i < numberOfRowsA-1;i++){
				filename = "ones." + to_string(numberOfRowsA) + "x1.zeros." + to_string(numberOfRowsB) + "x1.addOn"+ to_string(i) +".mat";
				inputA.load("./input/" + filenameA);
				inputB.load("./input/" + filenameB);
				inputA.insert_rows(i,inputB);
				inputA.save("./expected/TestMat/testInsert_rows." + filename, raw_ascii);
			}
		}
	}
	
}

int main() {
  testArmaMatrixValuedElementWiseFunctionsTrigonometric();
  testArmaMatrixValuedElementWiseFunctionsMiscellaneous();
  testArmaScalarValuedFunctionsOfVectorsMatrices();
  testArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic();
  testArmaDecomposition();
  testArmaMatrixValuedFunctionsOfVectorsMatricesSwap();
  testArmaMatrixValuedFunctionsOfVectorsMatricesReinterpret();
  testArmaMatrixValuedFunctionsOfVectorsMatricesMatrixJoins();
  testArmaMatrixValuedFunctionsOfVectorsMatricesReshapeResize();
  testArmaMatrixValuedFunctionsOfVectorsMatricesStatistic();
  testArmaScalarVectorValuedFunctionsOfVectorsMatricesMiscellaneous();
  testArmaMatrixValuedFunctionsOfVectorsMatricesSort();
  testArmaMatrixValuedFunctionsOfVectorsMatricesMiscellaneousParameterised();
  testArmaMatrixValuedFunctionsOfVectorsMatricesHistogramm();
  testArmaMatrixGenerationMatrix();
  testArmaMatrixGenerationToeplitz();
  testArmaMatrixGenerationLinspace();
  testArmaMatrixValuedFunctionsOfVectorsMatricesMiscellaneousFind();
  testArmaMatrixValuedFunctionsOfVectorsMatricesMiscellaneous();
  testArmaScalarVectorValuedFunctionsOfVectorsMatricesLogic();
  testDatum();
  testMat();

  return EXIT_SUCCESS;
}
