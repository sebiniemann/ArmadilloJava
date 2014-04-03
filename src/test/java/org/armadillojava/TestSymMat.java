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
public class TestSymMat extends TestClass {

  @Parameters(name = "{index}: SymMat = {0}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.SymMat);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _symMatString;

  @Parameter(1)
  public Mat    _symMat;

  protected Mat _copyOfSymMat;

  @Before
  public void before() {
    _fileSuffix = _symMatString;
    
    _copyOfSymMat = new Mat(_symMat);
  }

  @After
  public void after() {
    assertMatEquals(_symMat, _copyOfSymMat, 0);
  }

  @Test
  public void testEig_symA() throws IOException {
    assertMatEquals(Arma.eig_sym(_symMat), load("Arma.eig_symEigval"));
  }

  @Test
  public void testEig_symB() throws IOException {
    Col eigval = new Col();
    
    Arma.eig_sym(eigval, _symMat);
    
    assertMatEquals(eigval, load("Arma.eig_symEigval"));
  }

  @Test
  public void testEig_symC() throws IOException {
    Row eigval = new Row();
    
    Arma.eig_sym(eigval, _symMat);

    assertMatEquals(eigval, load("Arma.eig_symEigval").t());
  }

  @Test
  public void testEig_symD() throws IOException {
    Col eigval = new Col();
    Mat eigvec = new Mat();
    
    Arma.eig_sym(eigval, eigvec, _symMat);

    assertMatEquals(eigval, load("Arma.eig_symEigval"));
    assertMatEquals(eigvec, load("Arma.eig_symEigvec"));
  }

  @Test
  public void testEig_symE() throws IOException {
    Row eigval = new Row();
    Mat eigvec = new Mat();
    
    Arma.eig_sym(eigval, eigvec, _symMat);

    assertMatEquals(eigval, load("Arma.eig_symEigval").t());
    assertMatEquals(eigvec, load("Arma.eig_symEigvec"));
  }

}
