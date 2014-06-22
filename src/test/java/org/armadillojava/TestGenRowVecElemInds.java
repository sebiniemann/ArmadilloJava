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
import static org.junit.Assume.assumeThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.lessThan;

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
public class TestGenRowVecElemInds extends TestClass {

  @Parameters(name = "{index}: GenRowVec = {0}, ElemInds = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenRowVec);
    inputClasses.add(InputClass.ElemInds);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genRowVecString;

  @Parameter(1)
  public Row    _genRowVec;

  protected Row _copyOfGenRowVec;

  @Parameter(2)
  public String _elemIndsString;

  @Parameter(3)
  public Col    _elemInds;

  protected Col _copyOfElemInds;
 

  @Before
  public void before() {
    _fileSuffix = _genRowVecString + "," + _elemIndsString ;

    _copyOfGenRowVec = new Row(_genRowVec);
    _copyOfElemInds = new Col(_elemInds);
  }

  @After
  public void after() {
    assertMatEquals(_genRowVec, _copyOfGenRowVec, 0);
    assertMatEquals(_elemInds, _copyOfElemInds, 0);
  }

  @Test
  public void testMatElem() throws IOException {
    for(int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat((int) _elemInds.at(n), is(lessThan(_genRowVec.n_elem)));
    }
    
    assertMatEquals(_genRowVec.elem(_elemInds), load("row.elem"));
  }
  
  @Test
  public void testMatCols() throws IOException {
    for(int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat((int) _elemInds.at(n), is(lessThan(_genRowVec.n_elem)));
    }
    
    assertMatEquals(_genRowVec.cols(_elemInds), load("row.cols"));
  }

}
