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
package org.armadillojava;

import java.io.IOException;

public class TestClass {
  protected String _fileSuffix = "";

  protected Mat load(String filename) throws IOException {
    Mat expected = new Mat();
    expected.load("./src/test/data/expected/" + filename + _fileSuffix + ".mat", FileType.RAW_ASCII);
    return expected;
  }
}
