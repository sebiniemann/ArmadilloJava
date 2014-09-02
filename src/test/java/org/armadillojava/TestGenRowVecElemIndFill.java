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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;


@RunWith(Parameterized.class)
public class TestGenRowVecElemIndFill extends TestClass {

  @Parameters(name = "{index}: GenRowVec = {0}, Text = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.ElemInd);
    inputClasses.add(InputClass.Fill);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _elemIndString;

  @Parameter(1)
  public int    _elemInd;

  @Parameter(2)
  public String _fillString;

  @Parameter(3)
  public Fill _fill;

  @Before
  public void before() {
    _fileSuffix = _elemIndString + "," + _fillString;
  }
  
  @Test
  public void testRowElemIndFill() throws IOException {
    if(_fill == Fill.EYE || _fill == Fill.RANDN) return;
    Row row = new Row(_elemInd,_fill);
    
    assertMatEquals(row, load("Row.elemIndFill"));
  
  }
}
