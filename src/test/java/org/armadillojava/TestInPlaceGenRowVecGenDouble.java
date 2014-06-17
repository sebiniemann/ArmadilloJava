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
public class TestInPlaceGenRowVecGenDouble extends TestClass {

  @Parameters(name = "{index}: GenRowVec = {0}, GenDouble = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenRowVec);
    inputClasses.add(InputClass.GenDouble);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String    _genRowVecString;

  @Parameter(1)
  public Row       _genRowVec;

  protected Row    _copyOfGenRowVec;

  @Parameter(2)
  public String    _genDoubleString;

  @Parameter(3)
  public double    _genDouble;

  protected double _copyOfGenDouble;

  @Before
  public void before() {
    _fileSuffix = _genRowVecString + "," + _genDoubleString;

    _copyOfGenRowVec = new Row(_genRowVec);
    _copyOfGenDouble = new Double(_genDouble);
  }

  @After
  public void after() {
    _genRowVec.inPlace(Op.EQUAL, _copyOfGenRowVec);
    _genDouble = new Double(_copyOfGenDouble);
  }
  
  @Test
  public void testColFill() throws IOException {
    _genRowVec.fill(_genDouble);

    assertMatEquals(_genRowVec, load("Row.fill"));
  }
  
  @Test
  public void testColInPlacePlus() throws IOException {
    _genRowVec.inPlace(Op.PLUS, _genDouble);

    assertMatEquals(_genRowVec, load("Row.inPlacePlus"));
  }

  @Test
  public void testColInPlaceMinus() throws IOException {
    _genRowVec.inPlace(Op.MINUS, _genDouble);

    assertMatEquals(_genRowVec, load("Row.inPlaceMinus"));
  }

  @Test
  public void testColInPlaceTimes() throws IOException {
    _genRowVec.inPlace(Op.TIMES, _genDouble);

    assertMatEquals(_genRowVec, load("Row.inPlaceTimes"));
  }

  @Test
  public void testMatInPlaceDivide() throws IOException {
    _genRowVec.inPlace(Op.DIVIDE, _genDouble);

    assertMatEquals(_genRowVec, load("Row.inPlaceDivide"));
  }

}