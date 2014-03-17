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
#pragma once

#include <vector>
using std::vector;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <armadillo>
using arma::Mat;

namespace armadilloJava {
  class Input {
    public:
      static vector<vector<void*>> getTestParameters(const vector<InputClass>& testClasses);

    protected:
      static vector<vector<void*>> cartesianProduct(const vector<vector<void*>>& inputs);

      Mat<double> getHilbertMatrix(int n_rows, int n_cols);
      Mat<double> getKMSMatrix(int n_rows, int n_cols);

      static vector<void*> getOOMat();
      static vector<void*> getGenDouble();
      static vector<void*> getTriDouble();
  };
}
