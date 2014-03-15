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
// http://arma.sourceforge.net/docs.html#config_hpp
#define ARMA_USE_CXX11 // Use C++11 features, such as initialiser lists
#include <iostream>
// EXIT_SUCCESS
using std::cout;
using std::endl;

#include <string>
using std::to_string;

#include <cmath>
using std::pow;
using std::abs;

#include <armadillo>
using arma::Mat;
using arma::zeros;
using arma::ones;
using arma::eye;
using arma::raw_ascii;
using arma::endr;
using arma::datum;

Mat<double> generateHilbertMatrix(int numberOfRows, int numberOfColumns) {
  Mat<double> hilbertMatrix(numberOfRows, numberOfColumns);

  for (int j = 0; j < numberOfColumns; j++) {
    for (int i = 0; i < numberOfRows; i++) {
      hilbertMatrix.at(i, j) = 1.0 / (i + j + 1);
    }
  }

  return hilbertMatrix;
}

Mat<double> generateKMSMatrix(int numberOfRows, int numberOfColumns) {
  Mat<double> KMSMatrix(numberOfRows, numberOfColumns);

  for (int j = 0; j < numberOfColumns; j++) {
    for (int i = 0; i < numberOfRows; i++) {
    	KMSMatrix.at(i, j) = pow(2, abs(i - j));
    }
  }

  return KMSMatrix;
}

int main() {
  Mat<double> input;

  double NumElems[] = {1, 2, 3, 4, 5, 10, 100, 1000, 10000};
  double NumCols[] = {1, 2, 3, 4, 5, 10};
  double NumRows[] = {1, 2, 3, 4, 5, 10};

  for (int n_cols : NumCols) {
    for (int n_rows : NumRows) {
      input = zeros<Mat<double>>(n_rows, n_cols);
      input.save("./input/zeros." + to_string(n_rows) + "x" + to_string(n_cols) + ".mat", raw_ascii);

      input = ones<Mat<double>>(n_rows, n_cols);
      input.save("./input/ones." + to_string(n_rows) + "x" + to_string(n_cols) + ".mat", raw_ascii);

      input = eye<Mat<double>>(n_rows, n_cols);
      input.save("./input/eye." + to_string(n_rows) + "x" + to_string(n_cols) + ".mat", raw_ascii);

      input = generateHilbertMatrix(n_rows, n_cols);
      input.save("./input/hilbert." + to_string(n_rows) + "x" + to_string(n_cols) + ".mat", raw_ascii);

      input = generateHilbertMatrix(n_rows, n_cols) - 2.0 / (n_rows + n_cols + 2);
      input.save("./input/hilbertSub." + to_string(n_rows) + "x" + to_string(n_cols) + ".mat", raw_ascii);

      input = generateKMSMatrix(n_rows, n_cols);
      input.save("./input/ksm." + to_string(n_rows) + "x" + to_string(n_cols) + ".mat", raw_ascii);
    }
  }

  input = {0, 1, 2, datum::eps, datum::inf, 1/12.0 * datum::pi, 1/10.0 * datum::pi, 1/8.0 * datum::pi, 1/6.0 * datum::pi, 1/4.0 * datum::pi, 1/2.0 * datum::pi, datum::pi, 3/2.0 * datum::pi, 2 * datum::pi, 3 * datum::pi, 4 * datum::pi, 5 * datum::pi, 10 * datum::pi, 100 * datum::pi};
  input = join_vert(input, -input);
  input.save("./input/trigonometric.mat", raw_ascii);

  Mat<double> X = generateKMSMatrix(7, 7);
  cout << X << endl;
  cout << X.submat({0, 1}, {2, 3}) << endl;

  return EXIT_SUCCESS;
}
