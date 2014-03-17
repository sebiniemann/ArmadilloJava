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

#include <armadillo>
using arma::Mat;

#include <string>
using std::string;

namespace armadilloJava {
  class Expected {
    protected:
      void save(const string& filename, const Mat<double>& result);
  };
}
