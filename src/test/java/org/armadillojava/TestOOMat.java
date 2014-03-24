package org.armadillojava;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.number.IsCloseTo.*;

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
public class TestOOMat extends TestClass {

  @Parameters(name = "{index}: _OOMat = {0}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();
    
    inputClasses.add(InputClass.OOMat);
    
    return Input.getTestParameters(inputClasses);
  }
  
  @Parameter(0)
  public String _OOMatString;
  
  @Parameter(1)
  public Mat _OOMat;
  
  @Before
  public void before() {
    _fileSuffix = _OOMatString;
  }

  @Test
  public void testAs_scalar() throws IOException {
    double expected = load("as_scalar")._data[0];
    if(Double.isInfinite(expected)) {
      assertThat(Arma.as_scalar(_OOMat), is(expected));
    } else {
      assertThat(Arma.as_scalar(_OOMat), is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }
}
