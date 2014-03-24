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
