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

#include <utility>
using std::pair;

#include <armadillo>
using arma::Mat;
using arma::Col;
using arma::uword;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedGenMatRowIndsColInds : public Expected {
    public:
      ExpectedGenMatRowIndsColInds() {
        cout << "Compute ExpectedGenMatRowIndsColInds(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
          InputClass::GenMat,
          InputClass::RowInds,
          InputClass::ColInds
        });

        for (vector<pair<string, void*>> input : inputs) {
          _fileSuffix = "";

          int n = 0;
          for (pair<string, void*> value : input) {
            switch (n) {
              case 0:
                _fileSuffix += value.first;
                _genMat = *static_cast<Mat<double>*>(value.second);
                break;
              case 1:
                _fileSuffix += "," + value.first;
                _rowInds = *static_cast<Col<uword>*>(value.second);
                break;
              case 2:
                _fileSuffix += "," + value.first;
                _colInds = *static_cast<Col<uword>*>(value.second);
                break;
            }
            ++n;
          }

          cout << "Using input: " << _fileSuffix << endl;

          expectedSubmat();
        }

        cout << "done." << endl;
      }

    protected:
      Mat<double> _genMat;
      Col<uword> _rowInds;
      Col<uword> _colInds;

      void expectedSubmat() {
        for(int i = 0; i < _rowInds.n_elem; i++) {
          for(int j = 0; j < _colInds.n_elem; j++) {
            if(!_genMat.in_range(_rowInds.at(i), _colInds.at(j))) {
              return;
            }
          }
        }

        cout << "- Compute expectedSubmat() ... ";
        save<double>("Mat.submat", _genMat.submat(_rowInds, _colInds));
        cout << "done." << endl;
      }

  };
}
