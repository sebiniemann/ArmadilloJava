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

int main() {
  Mat<double> input;

	input << 0 << 1 << 0 << 123 << -1 << 0 << endr
	<< 0 << 1 << 1 << 234 << datum::eps << datum::inf << endr
	<< 0 << 1 << 0 << 345 << -1 << 0 << endr
	<< 0 << 1 << 1 << 456 << -datum::eps << -datum::inf << endr
	<< 0 << 1 << 0 << 567 << -1 << 0 << endr;
	input = join_horiz(input, -input);
	input.save("./input/logic.mat", raw_ascii);

  return EXIT_SUCCESS;
}
