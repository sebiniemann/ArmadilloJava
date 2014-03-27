package org.armadillojava;

import static org.armadillojava.TestUtil.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TestGenMatExp extends TestClass {

  @Parameters(name = "{index}: GenMat = {0}, Exp = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.Exp);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genMatString;

  @Parameter(1)
  public Mat    _genMat;

  @Parameter(2)
  public String _expString;

  @Parameter(3)
  public double _exp;

  @Before
  public void before() {
    _fileSuffix = _genMatString + _expString;
  }

  @Test
  public void testPow() throws IOException {
    assertMatEquals(Arma.pow(_genMat, _exp), load("pow"));
  }

}
