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
 * Sebastian Niemann - Lead developer
 * Daniel Kiechle - Unit testing
 ******************************************************************************/
package org.armadillojava;

import static org.armadillojava.TestUtil.assertMatEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TestGenMatNumElems extends TestClass {

  @Parameters(name = "{index}: GenMat = {0}, NumElems = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.NumElems);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genMatString;

  @Parameter(1)
  public Mat    _genMat;

  protected Mat _copyOfGenMat;

  @Parameter(2)
  public String _numElemsString;

  @Parameter(3)
  public int    _numElems;

  protected int _copyOfNumElems;

  @Before
  public void before() {
    _fileSuffix = _genMatString + "," + _numElemsString;

    _copyOfGenMat = new Mat(_genMat);
    _copyOfNumElems = new Integer(_numElems);
  }

  @After
  public void after() {
    assertMatEquals(_genMat, _copyOfGenMat, 0);
    assertThat(_numElems, is(_copyOfNumElems));
  }

  @Test
  public void testHist() throws IOException {
    assumeThat(_fileSuffix, is(not("Mat(hilbert(2,5)),25"))); // Avoided because of differences in rounding between the Java and C++ implementation.
    assumeThat(_fileSuffix, is(not("Mat(hilbert(5,2)),25"))); // Avoided because of differences in rounding between the Java and C++ implementation.
    
    assertMatEquals(Arma.hist(_genMat, _numElems), load("Arma.hist"));
  }

}
