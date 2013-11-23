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

#include <string>
using std::string;
using std::to_string;

#include <armadillo>
using arma::Mat;
using arma::Col;
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

bool isInvertable(const Mat<double>& matrix) {
  return (matrix.is_square() && rank(matrix) == matrix.n_rows);
}

bool isSymmetric(const Mat<double>& matrix) {
  int n = 0;
  for (int j = 0; j < matrix.n_cols; j++) {
    for (int i = 0; i < matrix.n_rows; i++) {
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
  input.load("./input/elementwise.trigonometric.mat");

  Mat<double> expected;

  // sine
  expected = sin(input);
  expected.save("./expected/TestArmaMatrixValuedElementWiseFunctionsTrigonometric/testSin.mat", raw_ascii);
  expected = asin(input);
  expected.save("./expected/TestArmaMatrixValuedElementWiseFunctionsTrigonometric/testAsin.mat", raw_ascii);
  expected = sinh(input);
  expected.save("./expected/TestArmaMatrixValuedElementWiseFunctionsTrigonometric/testSinh.mat", raw_ascii);
  expected = asinh(input);
  expected.save("./expected/TestArmaMatrixValuedElementWiseFunctionsTrigonometric/testAsinh.mat", raw_ascii);

  // cosine
  expected = cos(input);
  expected.save("./expected/TestArmaMatrixValuedElementWiseFunctionsTrigonometric/testCos.mat", raw_ascii);
  expected = acos(input);
  expected.save("./expected/TestArmaMatrixValuedElementWiseFunctionsTrigonometric/testAcos.mat", raw_ascii);
  expected = cosh(input);
  expected.save("./expected/TestArmaMatrixValuedElementWiseFunctionsTrigonometric/testCosh.mat", raw_ascii);
  expected = acosh(input);
  expected.save("./expected/TestArmaMatrixValuedElementWiseFunctionsTrigonometric/testAcosh.mat", raw_ascii);

  // tangent
  expected = tan(input);
  expected.save("./expected/TestArmaMatrixValuedElementWiseFunctionsTrigonometric/testTan.mat", raw_ascii);
  expected = atan(input);
  expected.save("./expected/TestArmaMatrixValuedElementWiseFunctionsTrigonometric/testAtan.mat", raw_ascii);
  expected = tanh(input);
  expected.save("./expected/TestArmaMatrixValuedElementWiseFunctionsTrigonometric/testTanh.mat", raw_ascii);
  expected = atanh(input);
  expected.save("./expected/TestArmaMatrixValuedElementWiseFunctionsTrigonometric/testAtanh.mat", raw_ascii);
}

void testArmaMatrixValuedElementWiseFunctionsMiscellaneous() {
  Mat<double> input;
  input.load("./input/elementwise.miscellaneous.mat");

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

  expected = sqrt(input);
  expected.save("./expected/TestArmaMatrixValuedElementWiseFunctionsMiscellaneous/testSqrt.mat", raw_ascii);
  expected = square(input);
  expected.save("./expected/TestArmaMatrixValuedElementWiseFunctionsMiscellaneous/testSquare.mat", raw_ascii);
  expected = floor(input);
  expected.save("./expected/TestArmaMatrixValuedElementWiseFunctionsMiscellaneous/testFloor.mat", raw_ascii);
  expected = ceil(input);
  expected.save("./expected/TestArmaMatrixValuedElementWiseFunctionsMiscellaneous/testCeil.mat", raw_ascii);
  expected = round(input);
  expected.save("./expected/TestArmaMatrixValuedElementWiseFunctionsMiscellaneous/testRound.mat", raw_ascii);
  expected = sign(input);
  expected.save("./expected/TestArmaMatrixValuedElementWiseFunctionsMiscellaneous/testSign.mat", raw_ascii);
}

void testArmaScalarValuedFunctionsOfVectorsMatrices() {
  std::array<double, 7> dimensions = {1, 2, 3, 4, 5, 10, 100};
  std::array<string, 5> matrices = {"zeros", "ones", "eye", "hankel", "hilbert"};

  string filename;
  Mat<double> input;

  Mat<double> expected;
  double value;
  double sign;

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

#pragma GCC diagnostic ignored "-Wnarrowing"
          expected = {rank(input)};
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
    for (int a = 0; a < matrices.size(); a++) {
      for (int b = a; b < matrices.size(); b++) {
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

int main() {
  testArmaMatrixValuedElementWiseFunctionsTrigonometric();
  testArmaMatrixValuedElementWiseFunctionsMiscellaneous();
  testArmaScalarValuedFunctionsOfVectorsMatrices();
  testArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic();
  testArmaDecomposition();
  testArmaMatrixValuedFunctionsOfVectorsMatricesStatistic();

  return EXIT_SUCCESS;
}
