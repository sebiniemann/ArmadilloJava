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
public class TestInPlaceGenMatGenDouble extends TestClass {

  @Parameters(name = "{index}: GenMat = {0}, GenDouble = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.GenDouble);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genMatString;

  @Parameter(1)
  public Mat    _genMat;

  protected Mat _copyOfGenMat;

  @Parameter(2)
  public String _genDoubleString;

  @Parameter(3)
  public double    _genDouble;

  protected double _copyOfGenDouble;

  @Before
  public void before() {
    _fileSuffix = _genMatString + "," + _genDoubleString;

    _copyOfGenMat = new Mat(_genMat);
    _copyOfGenDouble = new Double(_genDouble);
  }

  @After
  public void after() {
    _genMat.inPlace(Op.EQUAL, _copyOfGenMat);
    _genDouble = new Double(_copyOfGenDouble);
  }

  @Test
  public void testMatFill() throws IOException {
    _genMat.fill(_genDouble);
    
    assertMatEquals(_genMat, load("Mat.fill"));
  }

  @Test
  public void testMatDiagPlus() throws IOException {
    _genMat.diag(Op.PLUS, _genDouble);
    
    assertMatEquals(_genMat, load("Mat.diagPlus"));
  }

  @Test
  public void testMatDiagMinus() throws IOException {
    _genMat.diag(Op.MINUS, _genDouble);
    
    assertMatEquals(_genMat, load("Mat.diagMinus"));
  }

  @Test
  public void testMatDiagTimes() throws IOException {
    _genMat.diag(Op.TIMES, _genDouble);
    
    assertMatEquals(_genMat, load("Mat.diagTimes"));
  }

  @Test
  public void testMatDiagDivide() throws IOException {
    _genMat.diag(Op.DIVIDE, _genDouble);
    
    assertMatEquals(_genMat, load("Mat.diagDivide"));
  }

  @Test
  public void testMatInPlacePlus() throws IOException {
    _genMat.inPlace(Op.PLUS, _genDouble);
    
    assertMatEquals(_genMat, load("Mat.inPlacePlus"));
  }

  @Test
  public void testMatInPlaceMinus() throws IOException {
    _genMat.inPlace(Op.MINUS, _genDouble);
    
    assertMatEquals(_genMat, load("Mat.inPlaceMinus"));
  }

  @Test
  public void testMatInPlaceTimes() throws IOException {
    _genMat.inPlace(Op.TIMES, _genDouble);
    
    assertMatEquals(_genMat, load("Mat.inPlaceTimes"));
  }

  @Test
  public void testMatInPlaceDivide() throws IOException {
    _genMat.inPlace(Op.DIVIDE, _genDouble);
    
    assertMatEquals(_genMat, load("Mat.inPlaceDivide"));
  }

}
