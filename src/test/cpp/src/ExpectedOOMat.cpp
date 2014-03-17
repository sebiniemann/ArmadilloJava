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

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  ExpectedOOMat::ExpectedOOMat() {
    cout << "Yeah!" << endl;
    Input::getTestParameters({InputClass::OOMat});
  }
}
