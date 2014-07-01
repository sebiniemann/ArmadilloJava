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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.armadillojava.TestUtil.assertMatEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assume.assumeThat;

@RunWith(Parameterized.class)
public class TestInPlaceGenMatRowInd extends TestClass {

  @Parameters(name = "{index}: GenMat = {0}, RowInd = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.RowInd);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String    _genMatString;

  @Parameter(1)
  public Mat       _genMat;

  protected Mat    _copyOfGenMat;

  @Parameter(2)
  public String    _rowIndString;

  @Parameter(3)
  public int      _rowInd;

  protected int   _copyOfRowInd;

  @Before
  public void before() {
    _fileSuffix = _genMatString + "," + _rowIndString;

    _copyOfGenMat = new Mat(_genMat);
    _copyOfRowInd = new Integer(_rowInd);
  }

  @After
  public void after() {
    _genMat.inPlace(Op.EQUAL, _copyOfGenMat);
    _rowInd = new Integer(_copyOfRowInd);
  }

  @Test
  public void testMatShedRows() throws IOException {
    assumeThat(_genMat.in_range(new Span(_rowInd), Span.all()), is(true));

    _genMat.shed_row(_rowInd);

    assertMatEquals(_genMat, load("Mat.shed_row"));
  }

}
