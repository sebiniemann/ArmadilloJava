// http://arma.sourceforge.net/docs.html#config_hpp
#define ARMA_USE_CXX11 // Use C++11 features, such as initialiser lists

#include <iostream>
// EXIT_SUCCESS

#include <string>
using std::to_string;

#include <armadillo>
using arma::Mat;
using arma::zeros;
using arma::ones;
using arma::eye;
using arma::raw_ascii;
using arma::endr;
using arma::join_horiz;
using arma::join_vert;
using arma::datum;

Mat<double> getHankelMatrix(int numberOfRows, int numberOfColumns) {
	Mat<double> hankelMatrix(numberOfRows, numberOfColumns);

	for(int j = 0; j < numberOfColumns; j++) {
		for(int i = 0; i < numberOfRows; i++) {
			hankelMatrix.at(i, j) = i + j + 1;
		}
	}

	return hankelMatrix;
}

Mat<double> getHilbertMatrix(int numberOfRows, int numberOfColumns) {
	Mat<double> hilbertMatrix(numberOfRows, numberOfColumns);

	for(int j = 0; j < numberOfColumns; j++) {
		for(int i = 0; i < numberOfRows; i++) {
			hilbertMatrix.at(i, j) = 1.0 / (i + j + 1);
		}
	}

	return hilbertMatrix;
}

int main() {
  Mat<double> input;

  double dimensions[] = {1, 2, 3, 4, 5, 10, 100};

  for(int numberOfRows : dimensions) {
	  for(int numberOfColumns : dimensions) {
		  input = zeros<Mat<double>>(numberOfRows, numberOfColumns);
		  input.save("./input/zeros." + to_string(numberOfRows) + "x" + to_string(numberOfColumns) + ".mat", raw_ascii);

		  input = ones<Mat<double>>(numberOfRows, numberOfColumns);
		  input.save("./input/ones." + to_string(numberOfRows) + "x" + to_string(numberOfColumns) + ".mat", raw_ascii);

		  input = eye<Mat<double>>(numberOfRows, numberOfColumns);
		  input.save("./input/eye." + to_string(numberOfRows) + "x" + to_string(numberOfColumns) + ".mat", raw_ascii);

		  input = getHankelMatrix(numberOfRows, numberOfColumns);
		  input.save("./input/hankel." + to_string(numberOfRows) + "x" + to_string(numberOfColumns) + ".mat", raw_ascii);

		  if(numberOfRows < 10 && numberOfColumns < 10) {
			  input = getHilbertMatrix(numberOfRows, numberOfColumns);
			  input.save("./input/hilbert." + to_string(numberOfRows) + "x" + to_string(numberOfColumns) + ".mat", raw_ascii);
		  }
	  }
  }

  input = {0, 1, 2, datum::eps, datum::inf, 1/12.0 * datum::pi, 1/10.0 * datum::pi, 1/8.0 * datum::pi, 1/6.0 * datum::pi, 1/4.0 * datum::pi, 1/2.0 * datum::pi, datum::pi, 3/2.0 * datum::pi, 2 * datum::pi, 3 * datum::pi, 4 * datum::pi, 5 * datum::pi, 10 * datum::pi, 100 * datum::pi};
  input = join_vert(input, -input);
  input.save("./input/elementwise.trigonometric.mat", raw_ascii);

  input = {0, 1, 2, datum::eps, datum::inf, datum::e, 12, 123, 1234, 12345, 123456, 1234567, 12345678, 123456789, 0.9, 0.89, 0.789, 0.6789, 0.56789, 0.456789, 0.3456789, 0.23456789, 0.123456789};
  input = join_vert(input, -input);
  input.save("./input/elementwise.miscellaneous.mat", raw_ascii);

  input << 0 << 1 << 2 << 10 <<   5 <<           0 << endr
		<< 0 << 1 << 2 << 50 << -55 <<           1 << endr
		<< 0 << 1 << 2 << 20 <<  15 <<          -1 << endr
		<< 0 << 1 << 2 << 40 << -45 <<  datum::eps << endr
		<< 0 << 1 << 2 << 30 <<  30 << -datum::eps << endr;
  input = join_horiz(input, -input);
  input.save("./input/statistics.mat", raw_ascii);

  return EXIT_SUCCESS;
}
