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

#include <string>
using std::string;

#include <armadillo>
using arma::Mat;
using arma::uword;
using arma::raw_ascii;

namespace armadilloJava {
  class Expected {
    protected:
      string _filepath = "../data/expected/";
      string _fileSuffix;

      template<typename T>
      void save(const string& filename, const Mat<T>& expected) {
        string completeFilepath;
        if(_fileSuffix.length() > 0) {
          completeFilepath = _filepath + filename + "(" + _fileSuffix + ").mat";
        } else {
          completeFilepath = _filepath + filename + ".mat";
        }

        expected.save(completeFilepath, raw_ascii);
      }
  };
}
