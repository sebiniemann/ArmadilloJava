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

#include <Input.hpp>
using armadilloJava::Input;

#include <stdexcept>
using std::runtime_error;

#include <armadillo>
using arma::datum;

namespace armadilloJava {
  vector<vector<void*>> Input::getTestParameters(const vector<InputClass>& testClasses) {
    vector<vector<void*>> inputs;

    for (InputClass testClass : testClasses) {
      switch (testClass) {
        case InputClass::OOMat:
          inputs.push_back(getOOMat());
          break;
        default:
          throw new runtime_error("Unsupported test class requested.");
      }
    }

    return cartesianProduct(inputs);
  }

  vector<vector<void*>> Input::cartesianProduct(const vector<vector<void*>>& inputs) {
    vector<vector<void*>> cartesianProduct = {{}};
    for (vector<void*> input : inputs) {
      vector<vector<void*>> tempProduct;
      for (vector<void*> tempInput : cartesianProduct) {
        for (void* value : input) {
          tempProduct.push_back(tempInput);
          tempProduct.back().push_back(value);
        }
      }
      cartesianProduct.swap(tempProduct);
    }
    return cartesianProduct;
  }

  Mat<double> Input::getHilbertMatrix(int n_rows, int n_cols) {
    Mat<double> hilbertMatrix(n_rows, n_cols);

    for (int j = 0; j < n_cols; j++) {
      for (int i = 0; i < n_rows; i++) {
        hilbertMatrix.at(i, j) = 1.0 / (i + j + 1);
      }
    }

    return hilbertMatrix;
  }

  Mat<double> Input::getKMSMatrix(int n_rows, int n_cols) {
    Mat<double> KMSMatrix(n_rows, n_cols);

    for (int j = 0; j < n_cols; j++) {
      for (int i = 0; i < n_rows; i++) {
        KMSMatrix.at(i, j) = pow(2, abs(i - j));
      }
    }

    return KMSMatrix;
  }

  vector<void*> Input::getOOMat() {
    return {
      new double(3)
    };
  }

  vector<void*> Input::getGenDouble() {
    return {
      new double(-datum::pi)
    };
  }

  vector<void*> Input::getTriDouble() {
    return {
      new double(-datum::eps)
    };
  }
}
