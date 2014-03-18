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
#include <ExpectedOOMat.hpp>
using armadilloJava::ExpectedOOMat;

#include <iostream>
using std::cout;
using std::endl;

#include <string>
using std::string;

#include <utility>
using std::pair;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  ExpectedOOMat::ExpectedOOMat() {
    //vector<vector<void*>> inputs = Input::getTestParameters({InputClass::OOMat});
    vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({InputClass::ElemInd});

    for (vector<pair<string, void*>> input : inputs) {
      int n = 0;
      for (pair<string, void*> value : input) {
        switch (n) {
          case 0:
//            _OOMat = *static_cast<Mat<double>*>(value);
//            _double = *static_cast<double*>(value.second);
              _int = *static_cast<int*>(value.second);
            break;
        }
      }

      as_scalar();
    }
  }

  void ExpectedOOMat::as_scalar() {
    cout << _int << endl;
  }
}
