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
import static org.hamcrest.CoreMatchers.is;

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
public class TestGenMatDim extends TestClass {

  @Parameters(name = "{index}: GenMat = {0}, Dim = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.Dim);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genMatString;

  @Parameter(1)
  public Mat    _genMat;

  protected Mat _copyOfGenMat;

  @Parameter(2)
  public String _dimString;

  @Parameter(3)
  public int    _dim;

  protected int _copyOfDim;

  @Before
  public void before() {
    _fileSuffix = _genMatString + "," + _dimString;

    _copyOfGenMat = new Mat(_genMat);
    _copyOfDim = new Integer(_dim);
  }

  @After
  public void after() {
    assertMatEquals(_genMat, _copyOfGenMat, 0);
    assertThat(_dim, is(_copyOfDim));
  }

  @Test
  public void testArmaMin() throws IOException {
    if (_dim == 0) {
      assertMatEquals(Arma.min(Row.class, _genMat, _dim), load("Arma.min"));
      assertMatEquals(Arma.min(Col.class, _genMat, _dim), load("Arma.min").t());
    } else {
      assertMatEquals(Arma.min(Row.class, _genMat, _dim), load("Arma.min").t());
      assertMatEquals(Arma.min(Col.class, _genMat, _dim), load("Arma.min"));
    }
  }

  @Test
  public void testArmaMax() throws IOException {
    if (_dim == 0) {
      assertMatEquals(Arma.max(Row.class, _genMat, _dim), load("Arma.max"));
      assertMatEquals(Arma.max(Col.class, _genMat, _dim), load("Arma.max").t());
    } else {
      assertMatEquals(Arma.max(Row.class, _genMat, _dim), load("Arma.max").t());
      assertMatEquals(Arma.max(Col.class, _genMat, _dim), load("Arma.max"));
    }
  }

  @Test
  public void testArmaProd() throws IOException {
    if (_dim == 0) {
      assertMatEquals(Arma.prod(Row.class, _genMat, _dim), load("Arma.prod"));
      assertMatEquals(Arma.prod(Col.class, _genMat, _dim), load("Arma.prod").t());
    } else {
      assertMatEquals(Arma.prod(Row.class, _genMat, _dim), load("Arma.prod").t());
      assertMatEquals(Arma.prod(Col.class, _genMat, _dim), load("Arma.prod"));
    }
  }

  @Test
  public void testArmaSum() throws IOException {
    if (_dim == 0) {
      assertMatEquals(Arma.sum(Row.class, _genMat, _dim), load("Arma.sum"));
      assertMatEquals(Arma.sum(Col.class, _genMat, _dim), load("Arma.sum").t());
    } else {
      assertMatEquals(Arma.sum(Row.class, _genMat, _dim), load("Arma.sum").t());
      assertMatEquals(Arma.sum(Col.class, _genMat, _dim), load("Arma.sum"));
    }
  }

  @Test
  public void testArmaMean() throws IOException {
    if (_dim == 0) {
      assertMatEquals(Arma.mean(Row.class, _genMat, _dim), load("Arma.mean"));
      assertMatEquals(Arma.mean(Col.class, _genMat, _dim), load("Arma.mean").t());
    } else {
      assertMatEquals(Arma.mean(Row.class, _genMat, _dim), load("Arma.mean").t());
      assertMatEquals(Arma.mean(Col.class, _genMat, _dim), load("Arma.mean"));
    }
  }

  @Test
  public void testArmaMedian() throws IOException {
    if (_dim == 0) {
      assertMatEquals(Arma.median(Row.class, _genMat, _dim), load("Arma.median"));
      assertMatEquals(Arma.median(Col.class, _genMat, _dim), load("Arma.median").t());
    } else {
      assertMatEquals(Arma.median(Row.class, _genMat, _dim), load("Arma.median").t());
      assertMatEquals(Arma.median(Col.class, _genMat, _dim), load("Arma.median"));
    }
  }

  @Test
  public void testArmaCumsum() throws IOException {
    assertMatEquals(Arma.cumsum(_genMat, _dim), load("Arma.cumsum"));
  }

  @Test
  public void testArmaVectorise() throws IOException {
    if (_dim == 0) {
      assertMatEquals(Arma.vectorise(Col.class, _genMat, _dim), load("Arma.vectorise"));
      assertMatEquals(Arma.vectorise(Row.class, _genMat, _dim), load("Arma.vectorise").t());
    } else {
      assertMatEquals(Arma.vectorise(Col.class, _genMat, _dim), load("Arma.vectorise").t());
      assertMatEquals(Arma.vectorise(Row.class, _genMat, _dim), load("Arma.vectorise"));
    }
  }

}
