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

  @Parameters(name = "{index}: GenMat = {0}, Dim = {3}")
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

    _copyOfGenMat = _genMat;
    _copyOfDim = _dim;
  }

  @After
  public void after() {
    assertMatEquals(_genMat, _copyOfGenMat, 0);
    assertThat(_dim, is(_copyOfDim));
  }

  @Test
  public void testMin() throws IOException {
    if(_dim == 0) {
      assertMatEquals(Arma.min(Row.class, _genMat, _dim), load("min"));
      assertMatEquals(Arma.min(Col.class, _genMat, _dim), load("min").t());
    } else {
      assertMatEquals(Arma.min(Row.class, _genMat, _dim), load("min").t());
      assertMatEquals(Arma.min(Col.class, _genMat, _dim), load("min"));
    }
  }

  @Test
  public void testMax() throws IOException {
    if(_dim == 0) {
      assertMatEquals(Arma.max(Row.class, _genMat, _dim), load("max"));
      assertMatEquals(Arma.max(Col.class, _genMat, _dim), load("max").t());
    } else {
      assertMatEquals(Arma.max(Row.class, _genMat, _dim), load("max").t());
      assertMatEquals(Arma.max(Col.class, _genMat, _dim), load("max"));
    }
  }

  @Test
  public void testProd() throws IOException {
    if(_dim == 0) {
      assertMatEquals(Arma.prod(Row.class, _genMat, _dim), load("prod"));
      assertMatEquals(Arma.prod(Col.class, _genMat, _dim), load("prod").t());
    } else {
      assertMatEquals(Arma.prod(Row.class, _genMat, _dim), load("prod").t());
      assertMatEquals(Arma.prod(Col.class, _genMat, _dim), load("prod"));
    }
  }

  @Test
  public void testSum() throws IOException {
    if(_dim == 0) {
      assertMatEquals(Arma.sum(Row.class, _genMat, _dim), load("sum"));
      assertMatEquals(Arma.sum(Col.class, _genMat, _dim), load("sum").t());
    } else {
      assertMatEquals(Arma.sum(Row.class, _genMat, _dim), load("sum").t());
      assertMatEquals(Arma.sum(Col.class, _genMat, _dim), load("sum"));
    }
  }

  @Test
  public void testMean() throws IOException {
    if(_dim == 0) {
      assertMatEquals(Arma.mean(Row.class, _genMat, _dim), load("mean"));
      assertMatEquals(Arma.mean(Col.class, _genMat, _dim), load("mean").t());
    } else {
      assertMatEquals(Arma.mean(Row.class, _genMat, _dim), load("mean").t());
      assertMatEquals(Arma.mean(Col.class, _genMat, _dim), load("mean"));
    }
  }

  @Test
  public void testMedian() throws IOException {
    if(_dim == 0) {
      assertMatEquals(Arma.median(Row.class, _genMat, _dim), load("median"));
      assertMatEquals(Arma.median(Col.class, _genMat, _dim), load("median").t());
    } else {
      assertMatEquals(Arma.median(Row.class, _genMat, _dim), load("median").t());
      assertMatEquals(Arma.median(Col.class, _genMat, _dim), load("median"));
    }
  }

  @Test
  public void testCumsum() throws IOException {
    assertMatEquals(Arma.cumsum(_genMat, _dim), load("cumsum"));
  }

  @Test
  public void testVectorise() throws IOException {
    if(_dim == 0) {
      assertMatEquals(Arma.vectorise(Col.class, _genMat, _dim), load("vectorise"));
      assertMatEquals(Arma.vectorise(Row.class, _genMat, _dim), load("vectorise").t());
    } else {
      assertMatEquals(Arma.vectorise(Col.class, _genMat, _dim), load("vectorise").t());
      assertMatEquals(Arma.vectorise(Row.class, _genMat, _dim), load("vectorise"));
    }
  }

}
