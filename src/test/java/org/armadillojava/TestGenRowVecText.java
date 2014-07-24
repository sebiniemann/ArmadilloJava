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

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.both;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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
public class TestGenRowVecText extends TestClass {

  @Parameters(name = "{index}: GenRowVec = {0}, Text = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenRowVec);
    inputClasses.add(InputClass.Text);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genRowVecString;

  @Parameter(1)
  public Row    _genRowVec;

  @Parameter(2)
  public String _textString;

  @Parameter(3)
  public String _text;

  
  @Before
  public void before() throws IOException {
   
    _fileSuffix = _genRowVecString;
 
  }


  @Test
  public void testRowPrintString() throws IOException {
    OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(byteArrayOutputStream);
    PrintStream previousStream = System.out;
  
    System.setOut(printStream);

    _genRowVec.print(_text);
    System.out.flush();
    
    System.setOut(previousStream);
    
    assertThat(byteArrayOutputStream.toString().replaceAll("\\s+", " "), both(containsString(new String(Files.readAllBytes(Paths.get(_filepath + "Row.print(" + _fileSuffix + ").txt")), StandardCharsets.UTF_8).replaceAll("\\s+", " "))).and(containsString(_text)));
  
  }
  
  @Test
  public void testRowPrintOutputStream() throws IOException {
    OutputStream stream = new ByteArrayOutputStream() ;

    _genRowVec.print(stream);
    
    assertThat(stream.toString().replaceAll("\\s+", " "), containsString(new String(Files.readAllBytes(Paths.get(_filepath + "Row.print(" + _fileSuffix + ").txt")), StandardCharsets.UTF_8).replaceAll("\\s+", " ")));
  }
  
  @Test
  public void testRowPrintOutputStreamString() throws IOException {
    OutputStream stream = new ByteArrayOutputStream() ;

    _genRowVec.print(stream,_text);

    assertThat(stream.toString().replaceAll("\\s+", " "), both(containsString(new String(Files.readAllBytes(Paths.get(_filepath + "Row.print(" + _fileSuffix + ").txt")), StandardCharsets.UTF_8).replaceAll("\\s+", " "))).and(containsString(_text))); 
  }
  
  @Test
  public void testRowRawPrintString() throws IOException {
    
  }
  
  @Test
  public void testRowRawPrintOutputStream() throws IOException {
    
  }
  
  @Test
  public void testRowRawPrintStringOutputStream() throws IOException {
    
  }

}
