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
public class TestGenColVecSort extends TestClass {

  @Parameters(name = "{index}: GenColVec = {0}, Sort = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenColVec);
    inputClasses.add(InputClass.Sort);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String    _genColVecString;

  @Parameter(1)
  public Col       _genColVec;

  protected Col    _copyOfGenColVec;

  @Parameter(2)
  public String    _sortString;

  @Parameter(3)
  public String    _sort;

  protected String _copyOfSort;

  @Before
  public void before() {
    _fileSuffix = _genColVecString + "," + _sortString;

    _copyOfGenColVec = new Col(_genColVec);
    _copyOfSort = new String(_sort);
  }

  @After
  public void after() {
    assertMatEquals(_genColVec, _copyOfGenColVec, 0);
    assertThat(_sort, is(_copyOfSort));
  }

  @Test
  public void testArmaSort() throws IOException {
    assertMatEquals(Arma.sort(_genColVec, _sort), load("Arma.sort"));
  }

  @Test
  public void testArmaSort_index() throws IOException {
    assertMatEquals(_genColVec.elem(Arma.sort_index(_genColVec, _sort)), _genColVec.elem(new Col(load("Arma.sort_index"))));
  }

  @Test
  public void testArmaStable_sort_index() throws IOException {
    assertMatEquals(Arma.stable_sort_index(_genColVec, _sort), load("Arma.stable_sort_index"));
  }

}
