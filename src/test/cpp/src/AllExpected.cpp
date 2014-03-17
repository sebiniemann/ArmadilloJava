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

#include <ExpectedOOMat.hpp>
using armadilloJava::ExpectedOOMat;

#include <ExpectedDatum.hpp>
using armadilloJava::ExpectedDatum;

int main() {
	ExpectedOOMat();
  ExpectedDatum();

	return EXIT_SUCCESS;
}
