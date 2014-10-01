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
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TestGenRowVec extends TestClass {

  @Parameters(name = "{index}: GenRowVec = {0}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenRowVec);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genRowVecString;

  @Parameter(1)
  public Row    _genRowVec;

  protected Row _copyOfGenRowVec;
  
  protected static File _testpath;
  protected File _file;
  
  @BeforeClass
  public static void createFile() throws IOException {
    _testpath = new File("testFiles");
    _testpath.mkdir();
  }
  @AfterClass
  public static void deleteFile() {
    _testpath.delete();
}

  @Before
  public void before() {
    _fileSuffix = _genRowVecString;

    _copyOfGenRowVec = new Row(_genRowVec);
    _file = new File(_testpath+File.separator+_fileSuffix);
  }

  @After
  public void after() {
    assertMatEquals(_genRowVec, _copyOfGenRowVec, 0);
    _file.delete();
  }

  @Test
  public void testArmaAbs() throws IOException {
    assertMatEquals(Arma.abs(_genRowVec), load("Arma.abs"));
  }

  @Test
  public void testArmaEps() throws IOException {
    assertMatEquals(Arma.eps(_genRowVec), load("Arma.eps"));
  }

  @Test
  public void testArmaExp() throws IOException {
    assertMatEquals(Arma.exp(_genRowVec), load("Arma.exp"));
  }

  @Test
  public void testArmaExp2() throws IOException {
    assertMatEquals(Arma.exp2(_genRowVec), load("Arma.exp2"));
  }

  @Test
  public void testArmaExp10() throws IOException {
    assertMatEquals(Arma.exp10(_genRowVec), load("Arma.exp10"));
  }

  @Test
  public void testArmaTrunc_exp() throws IOException {
    assertMatEquals(Arma.trunc_exp(_genRowVec), load("Arma.trunc_exp"));
  }

  @Test
  public void testArmaLog() throws IOException {
    assertMatEquals(Arma.log(_genRowVec), load("Arma.log"));
  }

  @Test
  public void testArmaLog2() throws IOException {
    assertMatEquals(Arma.log2(_genRowVec), load("Arma.log2"));
  }

  @Test
  public void testArmaLog10() throws IOException {
    assertMatEquals(Arma.log10(_genRowVec), load("Arma.log10"));
  }

  @Test
  public void testArmaTrunc_log() throws IOException {
    assertMatEquals(Arma.trunc_log(_genRowVec), load("Arma.trunc_log"));
  }

  @Test
  public void testArmaSqrt() throws IOException {
    assertMatEquals(Arma.sqrt(_genRowVec), load("Arma.sqrt"));
  }

  @Test
  public void testArmaSquare() throws IOException {
    assertMatEquals(Arma.square(_genRowVec), load("Arma.square"));
  }

  @Test
  public void testArmaFloor() throws IOException {
    assertMatEquals(Arma.floor(_genRowVec), load("Arma.floor"));
  }

  @Test
  public void testArmaCeil() throws IOException {
    assertMatEquals(Arma.ceil(_genRowVec), load("Arma.ceil"));
  }

  @Test
  public void testArmaRound() throws IOException {
    assertMatEquals(Arma.round(_genRowVec), load("Arma.round"));
  }

  @Test
  public void testArmaSign() throws IOException {
    assertMatEquals(Arma.sign(_genRowVec), load("Arma.sign"));
  }

  @Test
  public void testArmaSin() throws IOException {
    assertMatEquals(Arma.sin(_genRowVec), load("Arma.sin"));
  }

  @Test
  public void testArmaAsin() throws IOException {
    assertMatEquals(Arma.asin(_genRowVec), load("Arma.asin"));
  }

  @Test
  public void testArmaSinh() throws IOException {
    assertMatEquals(Arma.sinh(_genRowVec), load("Arma.sinh"));
  }

  @Test
  public void testArmaAsinh() throws IOException {
    assertMatEquals(Arma.asinh(_genRowVec), load("Arma.asinh"));
  }

  @Test
  public void testArmaCos() throws IOException {
    assertMatEquals(Arma.cos(_genRowVec), load("Arma.cos"));
  }

  @Test
  public void testArmaAcos() throws IOException {
    assertMatEquals(Arma.acos(_genRowVec), load("Arma.acos"));
  }

  @Test
  public void testArmaCosh() throws IOException {
    assertMatEquals(Arma.cosh(_genRowVec), load("Arma.cosh"));
  }

  @Test
  public void testArmaAcosh() throws IOException {
    assertMatEquals(Arma.acosh(_genRowVec), load("Arma.acosh"));
  }

  @Test
  public void testArmaTan() throws IOException {
    assertMatEquals(Arma.tan(_genRowVec), load("Arma.tan"));
  }

  @Test
  public void testArmaAtan() throws IOException {
    assertMatEquals(Arma.atan(_genRowVec), load("Arma.atan"));
  }

  @Test
  public void testArmaTanh() throws IOException {
    assertMatEquals(Arma.tanh(_genRowVec), load("Arma.tanh"));
  }

  @Test
  public void testArmaAtanh() throws IOException {
    assertMatEquals(Arma.atanh(_genRowVec), load("Arma.atanh"));
  }

  @Test
  public void testArmaCumsum() throws IOException {
    assertMatEquals(Arma.cumsum(_genRowVec), load("Arma.cumsum"));
  }

  @Test
  public void testArmaHist() throws IOException {
    assertMatEquals(Arma.hist(_genRowVec), load("Arma.hist"));
  }

  @Test
  public void testArmaSort() throws IOException {
    assumeThat(_genRowVec.is_finite(), is(true));

    assertMatEquals(Arma.sort(_genRowVec), load("Arma.sort"));
  }

  @Test
  public void testArmaSort_index() throws IOException {
    assumeThat(_genRowVec.is_finite(), is(true));

    assertMatEquals(Arma.sort_index(_genRowVec), load("Arma.sort_index"));
  }

  @Test
  public void testArmaStable_sort_index() throws IOException {
    assumeThat(_genRowVec.is_finite(), is(true));

    assertMatEquals(Arma.stable_sort_index(_genRowVec), load("Arma.stable_sort_index"));
  }

  @Test
  public void testArmaTrans() throws IOException {
    assertMatEquals(Arma.trans(_genRowVec), load("Arma.trans"));
  }

  @Test
  public void testArmaUnique() throws IOException {
    assertMatEquals(Arma.unique(_genRowVec), load("Arma.unique"));
  }

  @Test
  public void testArmaNegate() throws IOException {
    assertMatEquals(Arma.negate(_genRowVec), load("Arma.negate"));
  }

  @Test
  public void testArmaReciprocal() throws IOException {
    assertMatEquals(Arma.reciprocal(_genRowVec), load("Arma.reciprocal"));
  }

  @Test
  public void testArmaToeplitz() throws IOException {
    assertMatEquals(Arma.toeplitz(_genRowVec), load("Arma.toeplitz"));
  }

  @Test
  public void testArmaCirc_toeplitz() throws IOException {
    assertMatEquals(Arma.circ_toeplitz(_genRowVec), load("Arma.circ_toeplitz"));
  }

  @Test
  public void testArmaAccu() throws IOException {
    double expected = load("Arma.accu")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(Arma.accu(_genRowVec), is(expected));
    } else {
      assertThat(Arma.accu(_genRowVec), is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testArmaMin() throws IOException {
    double expected = load("Arma.min")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(Arma.min(_genRowVec), is(expected));
    } else {
      assertThat(Arma.min(_genRowVec), is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testArmaMax() throws IOException {
    double expected = load("Arma.max")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(Arma.max(_genRowVec), is(expected));
    } else {
      assertThat(Arma.max(_genRowVec), is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testArmaProd() throws IOException {
    double expected = load("Arma.prod")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(Arma.prod(_genRowVec), is(expected));
    } else {
      assertThat(Arma.prod(_genRowVec), is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testArmaSum() throws IOException {
    double expected = load("Arma.sum")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(Arma.sum(_genRowVec), is(expected));
    } else {
      assertThat(Arma.sum(_genRowVec), is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testArmaMean() throws IOException {
    double expected = load("Arma.mean")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(Arma.mean(_genRowVec), is(expected));
    } else {
      assertThat(Arma.mean(_genRowVec), is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testArmaMedian() throws IOException {
    double expected = load("Arma.median")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(Arma.median(_genRowVec), is(expected));
    } else {
      assertThat(Arma.median(_genRowVec), is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testArmaStddev() throws IOException {
    double expected = load("Arma.stddev")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(Arma.stddev(_genRowVec), is(expected));
    } else {
      assertThat(Arma.stddev(_genRowVec), is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testArmaVar() throws IOException {
    double expected = load("Arma.var")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(Arma.var(_genRowVec), is(expected));
    } else {
      assertThat(Arma.var(_genRowVec), is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testArmaCor() throws IOException {
    double expected = load("Arma.cor")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(Arma.cor(_genRowVec), is(expected));
    } else {
      assertThat(Arma.cor(_genRowVec), is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testArmaCov() throws IOException {
    double expected = load("Arma.cov")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(Arma.cov(_genRowVec), is(expected));
    } else {
      assertThat(Arma.cov(_genRowVec), is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testArmaDiagmat() throws IOException {
    assertMatEquals(Arma.diagmat(_genRowVec), load("Arma.diagmat"));
  }

  @Test
  public void testArmaIs_finite() throws IOException {
    int expected = (int) load("Arma.is_finite")._data[0];
    if (Arma.is_finite(_genRowVec)) {
      assertThat(1, is(expected));
    } else {
      assertThat(0, is(expected));
    }
  }
  
  @Test
  public void testMat() throws IOException {
    assertMatEquals(new Mat(_genRowVec), load("Mat"));
  }
  
  @Test
  public void testRowVecEmpty() throws IOException {
    assertThat(_genRowVec.empty(), is(false));
  }
  
  @Test
  public void testRowVecSize() throws IOException {
    assertThat(_genRowVec.size(), is((int) load("Row.size")._data[0]));
  }
  
  @Test
  public void testRowVecT() throws IOException {
    assertMatEquals(_genRowVec.t(), load("Row.t"));
  }
  
  @Test
  public void testRowVecToString() throws IOException {
    OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(byteArrayOutputStream);
    PrintStream previousStream = System.out;

    System.setOut(printStream);

    System.out.println(_genRowVec);
    System.out.flush();
    
    System.setOut(previousStream);
    
    assertThat(byteArrayOutputStream.toString().replaceAll("\\s+", " "), containsString(new String(Files.readAllBytes(Paths.get(_filepath + "Row.print(" + _fileSuffix + ").txt")), StandardCharsets.UTF_8).replaceAll("\\s+", " "))); 
  }
  
  @Test
  public void testRowVecPrint() throws IOException {
    OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(byteArrayOutputStream);
    PrintStream previousStream = System.out;

    System.setOut(printStream);

    _genRowVec.print();
    System.out.flush();
    
    System.setOut(previousStream);
    
    assertThat(byteArrayOutputStream.toString().replaceAll("\\s+", " "), containsString(new String(Files.readAllBytes(Paths.get(_filepath + "Row.print(" + _fileSuffix + ").txt")), StandardCharsets.UTF_8).replaceAll("\\s+", " ")));
  }
  
  @Test
  public void testRowVecRaw_print() throws IOException {
    OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(byteArrayOutputStream);
    PrintStream previousStream = System.out;

    System.setOut(printStream);

    _genRowVec.raw_print();
    System.out.flush();
    
    System.setOut(previousStream);
    
    assertThat(byteArrayOutputStream.toString().replaceAll("\\s+", " "), containsString(new String(Files.readAllBytes(Paths.get(_filepath + "Row.raw_print(" + _fileSuffix + ").txt")), StandardCharsets.UTF_8).replaceAll("\\s+", " "))); 
    
  }
  
  @Test
  public void testRowIs_finite() throws IOException {
    int expected = (int) load("Row.is_finite")._data[0];
    if (_genRowVec.is_finite()) {
      assertThat(1, is(expected));
    } else {
      assertThat(0, is(expected));
    }
  }
  
  @Test
  public void testRowMinA() throws IOException {
    double expected = load("Row.minA")._data[0];
    
    assertThat(_genRowVec.min(), is(expected));
  }

  @Test
  public void testRowMaxA() throws IOException {
    double expected = load("Row.maxA")._data[0];
    assertThat(_genRowVec.max(), is(expected));

  }
  
  @Test
  public void testRowMinB() throws IOException {
    int expected = (int) load("Row.minB")._data[0];
    int[] value = new int [1];
    _genRowVec.min(value);
    assertThat(value[0], is(expected));
  }

  @Test
  public void testRowMaxB() throws IOException {
    int expected = (int) load("Row.maxB")._data[0];
	int[] value = new int[1];
	_genRowVec.max(value);
	assertThat(value[0], is(expected));
  }
  
  @Test
  public void testRowIs_empty() throws IOException {
    int expected = (int) load("Row.is_empty")._data[0];
    if (_genRowVec.is_empty()) {
      assertThat(1, is(expected));
    } else {
      assertThat(0, is(expected));
    }
  }
  
  @Test
  public void testRowSaveLoadString() throws IOException {
    String _RowVecString = _genRowVec.toString();
    
    _genRowVec.save(_file.getAbsolutePath());
    
    Row expected = new Row();
    expected.load(_file.getAbsolutePath());
    
    assertEquals(_RowVecString, expected.toString());
  }
  
  @Test
  public void testRowSaveLoadStringFileType() throws IOException {
    String _RowVecString = _genRowVec.toString();
    
    _genRowVec.save(_file.getAbsolutePath(),FileType.RAW_ASCII);
    
    Row expected = new Row();
    expected.load(_file.getAbsolutePath(),FileType.RAW_ASCII);
    
    assertEquals(_RowVecString, expected.toString());
  }
  
  @Test
  public void testRowSaveLoadOutputInputStream() throws IOException {
    String _RowVecString = _genRowVec.toString();
    
    OutputStream os = new FileOutputStream(_file);
    _genRowVec.save(os);
    
    InputStream is = new FileInputStream(_file);
    Row expected = new Row();
    expected.load(is);
    
    assertEquals(_RowVecString, expected.toString());
  }
  
  @Test
  public void testRowSaveLoadOutputInputStreamFileType() throws IOException {
    String _RowVecString = _genRowVec.toString();
    
    OutputStream os = new FileOutputStream(_file);
    _genRowVec.save(os,FileType.RAW_ASCII);
    
    InputStream is = new FileInputStream(_file);
    Row expected = new Row();
    expected.load(is, FileType.RAW_ASCII);
    
    assertEquals(_RowVecString, expected.toString());
  }
  
  @Test
  public void testRowQuietSaveLoadString() throws IOException {
    String _RowVecString = _genRowVec.toString();
    
    _genRowVec.quiet_save(_file.getAbsolutePath());
    
    Row expected = new Row();
    expected.quiet_load(_file.getAbsolutePath());
    
    assertEquals(_RowVecString, expected.toString());
  }
  
  @Test
  public void testRowQuietLoadStringFileType() throws IOException {
    String _RowVecString = _genRowVec.toString();
    
    _genRowVec.quiet_save(_file.getAbsolutePath(),FileType.RAW_ASCII);
    
    Row expected = new Row();
    expected.quiet_load(_file.getAbsolutePath(),FileType.RAW_ASCII);
    
    assertEquals(_RowVecString, expected.toString());
  }
  
  @Test
  public void testRowQuietSaveLoadOutputInputStream() throws IOException {
    String _RowVecString = _genRowVec.toString();
    
    OutputStream os = new FileOutputStream(_file);
    _genRowVec.quiet_save(os);
    
    InputStream is = new FileInputStream(_file);
    Row expected = new Row();
    expected.quiet_load(is);
    
    assertEquals(_RowVecString, expected.toString());
  }
  
  @Test
  public void testRowQuietSaveLoadOutputInputStreamFileType() throws IOException {
    String _RowVecString = _genRowVec.toString();
    
    OutputStream os = new FileOutputStream(_file);
    _genRowVec.quiet_save(os,FileType.RAW_ASCII);
    
    InputStream is = new FileInputStream(_file);
    Row expected = new Row();
    expected.quiet_load(is, FileType.RAW_ASCII);
    
    assertEquals(_RowVecString, expected.toString());
  }
}
