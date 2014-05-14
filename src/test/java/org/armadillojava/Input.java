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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

class Input {

  protected static Collection<Object[]> getTestParameters(List<InputClass> testClasses) {
    List<List<Pair<String, Object>>> inputs = new ArrayList<>();

    for (InputClass testClass : testClasses) {
      switch (testClass) {
        case ElemInd:
          inputs.add(getElemInd());
          break;
        case ColInd:
          inputs.add(getColInd());
          break;
        case ExtColInd:
          inputs.add(getExtColInd());
          break;
        case RowInd:
          inputs.add(getRowInd());
          break;
        case ExtRowInd:
          inputs.add(getExtRowInd());
          break;
        case NumElems:
          inputs.add(getNumElems());
          break;
        case NumCols:
          inputs.add(getNumCols());
          break;
        case NumRows:
          inputs.add(getNumRows());
          break;
        case Normal:
          inputs.add(getNormal());
          break;
        case Dim:
          inputs.add(getDim());
          break;
        case Exp:
          inputs.add(getExp());
          break;
        case MatNormInt:
          inputs.add(getMatNormInt());
          break;
        case VecNormInt:
          inputs.add(getVecNormInt());
          break;
        case GenDouble:
          inputs.add(getGenDouble());
          break;
        case SinValTol:
          inputs.add(getSinValTol());
          break;
        case ElemIndRange:
          inputs.add(getElemIndRange());
          break;
        case ColIndRange:
          inputs.add(getColIndRange());
          break;
        case RowIndRange:
          inputs.add(getRowIndRange());
          break;
        case MatSize:
          inputs.add(getMatSize());
          break;
        case GenMat:
          inputs.add(getGenMat());
          break;
        case SquMat:
          inputs.add(getSquMat());
          break;
        case InvMat:
          inputs.add(getInvMat());
          break;
        case SymMat:
          inputs.add(getSymMat());
          break;
        case SymPDMat:
          inputs.add(getSymPDMat());
          break;
        case LogicMat:
          inputs.add(getLogicMat());
          break;
        case OOMat:
          inputs.add(getOOMat());
          break;
        case GenColVec:
          inputs.add(getGenColVec());
          break;
        case MonColVec:
          inputs.add(getMonColVec());
          break;
        case LogicColVec:
          inputs.add(getLogicColVec());
          break;
        case OOColVec:
          inputs.add(getOOColVec());
          break;
        case GenRowVec:
          inputs.add(getGenRowVec());
          break;
        case MonRowVec:
          inputs.add(getMonRowVec());
          break;
        case LogicRowVec:
          inputs.add(getLogicRowVec());
          break;
        case OORowVec:
          inputs.add(getOORowVec());
          break;
        case ElemInds:
          inputs.add(getElemInds());
          break;
        case ColInds:
          inputs.add(getColInds());
          break;
        case RowInds:
          inputs.add(getRowInds());
          break;
        case Text:
          inputs.add(getText());
          break;
        case MatNormString:
          inputs.add(getMatNormString());
          break;
        case VecNormString:
          inputs.add(getVecNormString());
          break;
        case Sort:
          inputs.add(getSort());
          break;
        case Search:
          inputs.add(getSearch());
          break;
        case SinValSel:
          inputs.add(getSinValSel());
          break;
        case DistrParam:
          inputs.add(getDistrParam());
          break;
        case Fill:
          inputs.add(getFill());
          break;
        case Random:
          inputs.add(getRandom());
          break;
        default:
          throw new RuntimeException("Unsupported test class requested.");
      }
    }

    return convertToJUnitTestParameters(cartesianProduct(inputs));
  }

  protected static Collection<Object[]> convertToJUnitTestParameters(List<List<Pair<String, Object>>> inputs) {
    Collection<Object[]> testParameters = new ArrayList<Object[]>();

    for (List<Pair<String, Object>> input : inputs) {
      Object[] testParameter = new Object[input.size() * 2];

      for (int n = 0; n < input.size(); n++) {
        testParameter[2 * n] = input.get(n).getFirst();
        testParameter[2 * n + 1] = input.get(n).getSecond();
      }

      testParameters.add(testParameter);
    }

    return testParameters;
  }

  protected static List<List<Pair<String, Object>>> cartesianProduct(List<List<Pair<String, Object>>> inputs) {
    List<List<Pair<String, Object>>> cartesianProduct = new ArrayList<>();
    cartesianProduct.add(new ArrayList<Pair<String, Object>>());
    for (List<Pair<String, Object>> input : inputs) {
      List<List<Pair<String, Object>>> tempProduct = new ArrayList<>();
      for (List<Pair<String, Object>> tempInput : cartesianProduct) {
        for (Pair<String, Object> keyValuePair : input) {
          tempProduct.add(new ArrayList<>(tempInput));
          tempProduct.get(tempProduct.size() - 1).add(keyValuePair);
        }
      }

      cartesianProduct = new ArrayList<>(tempProduct);
    }
    return cartesianProduct;
  }

  protected static List<Pair<String, Object>> vectorUnion(List<List<Pair<String, Object>>> inputs) {
    List<Pair<String, Object>> vectorUnion = inputs.get(0);

    Collections.sort(vectorUnion, new Comparator<Pair<String, Object>>() {
      @Override
      public int compare(Pair<String, Object> lhs, Pair<String, Object> rhs) {
        return lhs.getFirst().compareTo(rhs.getFirst());
      }
    });

    for (int n = 1; n < inputs.size(); n++) {
      List<Pair<String, Object>> input = inputs.get(n);
      Collections.sort(input, new Comparator<Pair<String, Object>>() {
        @Override
        public int compare(Pair<String, Object> lhs, Pair<String, Object> rhs) {
          return lhs.getFirst().compareTo(rhs.getFirst());
        }
      });

      if (input.size() > 0 && !vectorUnion.equals(input)) {
        vectorUnion.removeAll(input);
        vectorUnion.addAll(input);
      }
    }

    return vectorUnion;
  }

  protected static Mat getHilbertMatrix(int n_rows, int n_cols) {
    Mat hilbertMatrix = new Mat(n_rows, n_cols);

    for (int j = 0; j < n_cols; j++) {
      for (int i = 0; i < n_rows; i++) {
        hilbertMatrix.at(i, j, Op.EQUAL, 1.0 / (i + j + 1));
      }
    }

    return hilbertMatrix;
  }

  protected static Mat getKMSMatrix(int n_rows, int n_cols) {
    Mat kmsMatrix = new Mat(n_rows, n_cols);

    for (int j = 0; j < n_cols; j++) {
      for (int i = 0; i < n_rows; i++) {
        kmsMatrix.at(i, j, Op.EQUAL, Math.pow(2, Math.abs(i - j)));
      }
    }

    return kmsMatrix;
  }

  protected static List<Pair<String, Object>> getElemInd() {
    List<Pair<String, Object>> input = new ArrayList<>();

    input.add(new Pair<String, Object>("0", 0));
    input.add(new Pair<String, Object>("9", 9));

    return input;
  }

  protected static List<Pair<String, Object>> getColInd() {
    return getRowInd();
  }

  protected static List<Pair<String, Object>> getExtColInd() {
    return getExtRowInd();
  }

  protected static List<Pair<String, Object>> getRowInd() {
    List<Pair<String, Object>> input = new ArrayList<>();

    input.add(new Pair<String, Object>("0", 0));
    input.add(new Pair<String, Object>("1", 1));

    return input;
  }

  protected static List<Pair<String, Object>> getExtRowInd() {
    List<Pair<String, Object>> input = new ArrayList<>();

    input.add(new Pair<String, Object>("0", 0));
    input.add(new Pair<String, Object>("1", 1));
    input.add(new Pair<String, Object>("2", 2));

    return input;
  }

  protected static List<Pair<String, Object>> getNumElems() {
    List<Pair<String, Object>> input = new ArrayList<>();

    input.add(new Pair<String, Object>("1", 1));
    input.add(new Pair<String, Object>("10", 10));

    return input;
  }

  protected static List<Pair<String, Object>> getNumCols() {
    return getNumRows();
  }

  protected static List<Pair<String, Object>> getNumRows() {
    List<Pair<String, Object>> input = new ArrayList<>();

    input.add(new Pair<String, Object>("1", 1));
    input.add(new Pair<String, Object>("5", 5));

    return input;
  }

  protected static List<Pair<String, Object>> getNormal() {
    List<Pair<String, Object>> input = new ArrayList<>();

    input.add(new Pair<String, Object>("0", 0));
    input.add(new Pair<String, Object>("1", 1));

    return input;
  }

  protected static List<Pair<String, Object>> getDim() {
    List<Pair<String, Object>> input = new ArrayList<>();

    input.add(new Pair<String, Object>("0", 0));
    input.add(new Pair<String, Object>("1", 1));

    return input;
  }

  protected static List<Pair<String, Object>> getExp() {
    List<Pair<String, Object>> input = new ArrayList<>();

    input.add(new Pair<String, Object>("0.5", 0.5));
    input.add(new Pair<String, Object>("1.0", 1.0));
    input.add(new Pair<String, Object>("2.0", 2.0));
    input.add(new Pair<String, Object>("3.0", 3.0));

    return input;
  }

  protected static List<Pair<String, Object>> getMatNormInt() {
    List<Pair<String, Object>> input = new ArrayList<>();

    input.add(new Pair<String, Object>("1", 1));
    input.add(new Pair<String, Object>("2", 2));

    return input;
  }

  protected static List<Pair<String, Object>> getVecNormInt() {
    List<Pair<String, Object>> input = new ArrayList<>();

    input.add(new Pair<String, Object>("1", 1));
    input.add(new Pair<String, Object>("2", 2));
    input.add(new Pair<String, Object>("3", 3));
    input.add(new Pair<String, Object>("4", 4));

    return input;
  }

  protected static List<Pair<String, Object>> getGenDouble() {
    List<Pair<String, Object>> input = new ArrayList<>();

    input.add(new Pair<String, Object>("-inf", -Datum.inf));
    input.add(new Pair<String, Object>("-2.0", -2.0));
    input.add(new Pair<String, Object>("0.0", 0.0));
    input.add(new Pair<String, Object>("machine_epsilon", Datum.eps));
    input.add(new Pair<String, Object>("0.5", 0.5));
    input.add(new Pair<String, Object>("1.0", 1.0));
    input.add(new Pair<String, Object>("euler_number", Datum.e));
    input.add(new Pair<String, Object>("pi", Datum.pi));
    input.add(new Pair<String, Object>("inf", Datum.inf));

    return input;
  }

  protected static List<Pair<String, Object>> getSinValTol() {
    List<Pair<String, Object>> input = new ArrayList<>();

    input.add(new Pair<String, Object>("0.0", 0));
    input.add(new Pair<String, Object>("1.0", 1));

    return input;
  }

  protected static List<Pair<String, Object>> getElemIndRange() {
    List<Pair<String, Object>> input = new ArrayList<>();

    input.add(new Pair<String, Object>("span(0)", new Span(0)));
    input.add(new Pair<String, Object>("span(0,9)", new Span(0, 9)));
    input.add(new Pair<String, Object>("span(1,4)", new Span(1, 4)));

    return input;
  }

  protected static List<Pair<String, Object>> getColIndRange() {
    return getRowIndRange();
  }

  protected static List<Pair<String, Object>> getRowIndRange() {
    List<Pair<String, Object>> input = new ArrayList<>();

    input.add(new Pair<String, Object>("span.all", Span.all()));
    input.add(new Pair<String, Object>("span(0)", new Span(0)));
    input.add(new Pair<String, Object>("span(0,4)", new Span(0, 4)));
    input.add(new Pair<String, Object>("span(1,2)", new Span(1, 2)));

    return input;
  }

  protected static List<Pair<String, Object>> getMatSize() {
    List<Pair<String, Object>> input = new ArrayList<>();

    input.add(new Pair<String, Object>("size(1,1)", new Size(1, 1)));
    input.add(new Pair<String, Object>("size(1,5)", new Size(1, 5)));
    input.add(new Pair<String, Object>("size(2,5)", new Size(2, 5)));
    input.add(new Pair<String, Object>("size(5,1)", new Size(5, 1)));
    input.add(new Pair<String, Object>("size(5,2)", new Size(5, 2)));
    input.add(new Pair<String, Object>("size(5,5)", new Size(5, 5)));

    return input;
  }

  protected static List<Pair<String, Object>> getGenMat() {
    List<Pair<String, Object>> input = new ArrayList<>();

    for (Pair<String, Object> keyValuePair : getMatSize()) {
      Size size = (Size) keyValuePair.getSecond();

      input.add(new Pair<String, Object>("Mat(zeros(" + size.n_rows + "," + size.n_cols + "))", new Mat(Arma.zeros(size.n_rows, size.n_cols))));
      input.add(new Pair<String, Object>("Mat(ones(" + size.n_rows + "," + size.n_cols + "))", new Mat(Arma.ones(size.n_rows, size.n_cols))));
      input.add(new Pair<String, Object>("Mat(hilbert(" + size.n_rows + "," + size.n_cols + "))", new Mat(getHilbertMatrix(size.n_rows, size.n_cols))));
   }

    Mat infinite = new Mat(new double[]{Datum.inf, -Datum.inf, -Datum.inf, Datum.inf});
    infinite.reshape(2, 2);
    input.add(new Pair<String, Object>("Mat({{inf,-inf},{-inf,inf}})", new Mat(infinite)));

    return input;
  }

  protected static List<Pair<String, Object>> getSquMat() {
    List<List<Pair<String, Object>>> inputs = new ArrayList<>();

    inputs.add(getInvMat());
    inputs.add(getSymMat());

    return vectorUnion(inputs);
  }

  protected static List<Pair<String, Object>> getInvMat() {
    List<Pair<String, Object>> input = new ArrayList<>();

    for (Pair<String, Object> keyValuePair : getMatSize()) {
      Size size = (Size) keyValuePair.getSecond();

      if (size.n_rows == size.n_cols) {
        input.add(new Pair<String, Object>("Mat(eye(" + size.n_rows + "," + size.n_cols + "))", new Mat(Arma.eye(size.n_rows, size.n_cols))));
        input.add(new Pair<String, Object>("Mat(kms(" + size.n_rows + "," + size.n_cols + "))", new Mat(getKMSMatrix(size.n_rows, size.n_cols))));
      }
    }

    return input;
  }

  protected static List<Pair<String, Object>> getSymMat() {
    List<List<Pair<String, Object>>> inputs = new ArrayList<>();
    inputs.add(getSymPDMat());

    for (Pair<String, Object> keyValuePair : getMatSize()) {
      Size size = (Size) keyValuePair.getSecond();

      if (size.n_rows == size.n_cols) {
        List<Pair<String, Object>> input = new ArrayList<>();
        input.add(new Pair<String, Object>("Mat(zeros(" + size.n_rows + "," + size.n_cols + "))", new Mat(Arma.zeros(size.n_rows, size.n_cols))));
        input.add(new Pair<String, Object>("Mat(ones(" + size.n_rows + "," + size.n_cols + "))", new Mat(Arma.ones(size.n_rows, size.n_cols))));
        inputs.add(input);
      }
    }

    return vectorUnion(inputs);
  }

  protected static List<Pair<String, Object>> getSymPDMat() {
    List<Pair<String, Object>> input = new ArrayList<>();

    for (Pair<String, Object> keyValuePair : getMatSize()) {
      Size size = (Size) keyValuePair.getSecond();

      if (size.n_rows == size.n_cols) {
        input.add(new Pair<String, Object>("Mat(eye(" + size.n_rows + "," + size.n_cols + "))", new Mat(Arma.eye(size.n_rows, size.n_cols))));
        input.add(new Pair<String, Object>("Mat(hilbert(" + size.n_rows + "," + size.n_cols + "))", new Mat(getHilbertMatrix(size.n_rows, size.n_cols))));
      }
    }

    return input;
  }

  protected static List<Pair<String, Object>> getLogicMat() {
    List<Pair<String, Object>> input = new ArrayList<>();

    for (Pair<String, Object> keyValuePair : getMatSize()) {
      Size size = (Size) keyValuePair.getSecond();

      input.add(new Pair<String, Object>("Mat(zeros(" + size.n_rows + "," + size.n_cols + "))", new Mat(Arma.zeros(size.n_rows, size.n_cols))));
      input.add(new Pair<String, Object>("Mat(ones(" + size.n_rows + "," + size.n_cols + "))", new Mat(Arma.ones(size.n_rows, size.n_cols))));
      input.add(new Pair<String, Object>("Mat(eye(" + size.n_rows + "," + size.n_cols + "))", new Mat(Arma.eye(size.n_rows, size.n_cols))));
    }

    return input;
  }

  protected static List<Pair<String, Object>> getOOMat() {
    List<Pair<String, Object>> input = new ArrayList<>();

    for (Pair<String, Object> keyValuePair : getGenDouble()) {
      double genDouble = (double) keyValuePair.getSecond();

      input.add(new Pair<String, Object>("Mat({" + keyValuePair.getFirst() + "})", new Mat(new double[]{genDouble})));
    }

    return input;
  }

  protected static List<Pair<String, Object>> getGenColVec() {
    List<Pair<String, Object>> input = new ArrayList<>();

    for (Pair<String, Object> keyValuePair : getMatSize()) {
      Size size = (Size) keyValuePair.getSecond();

      if (size.n_cols == 1) {
        input.add(new Pair<String, Object>("Col(zeros(" + size.n_rows + ",1))", new Col(Arma.zeros(Col.class, size.n_rows))));
        input.add(new Pair<String, Object>("Col(ones(" + size.n_rows + ",1))", new Col(Arma.ones(Col.class, size.n_rows))));
        input.add(new Pair<String, Object>("Col(hilbert(" + size.n_rows + ",1))", new Col(getHilbertMatrix(size.n_rows, 1))));
      }
    }
    input.add(new Pair<String, Object>("Col({inf,-inf})", new Col(new double[]{Datum.inf, -Datum.inf})));

    return input;
  }

  protected static List<Pair<String, Object>> getMonColVec() {
    List<Pair<String, Object>> input = new ArrayList<>();

    input.add(new Pair<String, Object>("Col({-10,-5,0.5,10})", new Col(new double[]{-10, -5, 0.5, 10})));
    input.add(new Pair<String, Object>("Col({-inf,0,inf})", new Col(new double[]{Datum.inf, 0, -Datum.inf})));
    input.add(new Pair<String, Object>("Col({0})", new Col(new double[]{0})));
    input.add(new Pair<String, Object>("Col({-inf})", new Col(new double[]{-Datum.inf})));
    input.add(new Pair<String, Object>("Col({inf})", new Col(new double[]{Datum.inf})));

    return input;
  }

  protected static List<Pair<String, Object>> getLogicColVec() {
    List<Pair<String, Object>> input = new ArrayList<>();

    for (Pair<String, Object> keyValuePair : getMatSize()) {
      Size size = (Size) keyValuePair.getSecond();

      if (size.n_cols == 1) {
        input.add(new Pair<String, Object>("Col(zeros(" + size.n_rows + ",1))", new Col(Arma.zeros(Col.class, size.n_rows))));
        input.add(new Pair<String, Object>("Col(ones(" + size.n_rows + ",1))", new Col(Arma.ones(Col.class, size.n_rows))));
        input.add(new Pair<String, Object>("Col(eye(" + size.n_rows + ",1))", new Col(Arma.eye(size.n_rows, 1))));
      }
    }

    return input;
  }

  protected static List<Pair<String, Object>> getOOColVec() {
    List<Pair<String, Object>> input = new ArrayList<>();

    for (Pair<String, Object> keyValuePair : getGenDouble()) {
      double genDouble = (double) keyValuePair.getSecond();

      input.add(new Pair<String, Object>("Col({" + keyValuePair.getFirst() + "})", new Col(new double[]{genDouble})));
    }

    return input;
  }

  protected static List<Pair<String, Object>> getGenRowVec() {
    List<Pair<String, Object>> input = new ArrayList<>();

    for (Pair<String, Object> keyValuePair : getMatSize()) {
      Size size = (Size) keyValuePair.getSecond();

      if (size.n_rows == 1) {
        input.add(new Pair<String, Object>("Row(zeros(1," + size.n_cols + "))", new Row(Arma.zeros(Row.class, size.n_cols))));
        input.add(new Pair<String, Object>("Row(ones(1," + size.n_cols + "))", new Row(Arma.ones(Row.class, size.n_cols))));
        input.add(new Pair<String, Object>("Row(hilbert(1," + size.n_cols + "))", new Row(getHilbertMatrix(1, size.n_cols))));
      }
    }
    input.add(new Pair<String, Object>("Row({inf,-inf})", new Row(new double[]{Datum.inf, -Datum.inf})));

    return input;
  }

  protected static List<Pair<String, Object>> getMonRowVec() {
    List<Pair<String, Object>> input = new ArrayList<>();

    input.add(new Pair<String, Object>("Row({-10,-5,0.5,10})", new Row(new double[]{-10, -5, 0.5, 10})));
    input.add(new Pair<String, Object>("Row({-inf,0,inf})", new Row(new double[]{Datum.inf, 0, -Datum.inf})));
    input.add(new Pair<String, Object>("Row({0})", new Row(new double[]{0})));
    input.add(new Pair<String, Object>("Row({-inf})", new Row(new double[]{-Datum.inf})));
    input.add(new Pair<String, Object>("Row({inf})", new Row(new double[]{Datum.inf})));

    return input;
  }

  protected static List<Pair<String, Object>> getLogicRowVec() {
    List<Pair<String, Object>> input = new ArrayList<>();

    for (Pair<String, Object> keyValuePair : getMatSize()) {
      Size size = (Size) keyValuePair.getSecond();

      if (size.n_rows == 1) {
        input.add(new Pair<String, Object>("Row(zeros(1," + size.n_cols + "))", new Row(Arma.zeros(Row.class, size.n_cols))));
        input.add(new Pair<String, Object>("Row(ones(1," + size.n_cols + "))", new Row(Arma.ones(Row.class, size.n_cols))));
        input.add(new Pair<String, Object>("Row(eye(1," + size.n_cols + "))", new Row(Arma.eye(1, size.n_cols))));
      }
    }

    return input;
  }

  protected static List<Pair<String, Object>> getOORowVec() {
    List<Pair<String, Object>> input = new ArrayList<>();

    for (Pair<String, Object> keyValuePair : getGenDouble()) {
      double genDouble = (double) keyValuePair.getSecond();

      input.add(new Pair<String, Object>("Row({" + keyValuePair.getFirst() + "})", new Row(new double[]{genDouble})));
    }

    return input;
  }

  protected static List<Pair<String, Object>> getElemInds() {
    List<Pair<String, Object>> input = new ArrayList<>();

    input.add(new Pair<String, Object>("Col({0})", new Col(new double[]{0})));
    input.add(new Pair<String, Object>("Col({9})", new Col(new double[]{9})));
    input.add(new Pair<String, Object>("Col({9,0,1})", new Col(new double[]{9, 0, 1})));
    input.add(new Pair<String, Object>("Col({1,1,1})", new Col(new double[]{1, 1, 1})));

    return input;
  }

  protected static List<Pair<String, Object>> getColInds() {
    return getRowInds();
  }

  protected static List<Pair<String, Object>> getRowInds() {
    List<Pair<String, Object>> input = new ArrayList<>();

    input.add(new Pair<String, Object>("Col({0})", new Col(new double[]{0})));
    input.add(new Pair<String, Object>("Col({4})", new Col(new double[]{4})));
    input.add(new Pair<String, Object>("Col({4,0,1})", new Col(new double[]{4, 0, 1})));
    input.add(new Pair<String, Object>("Col({1,1,1})", new Col(new double[]{1, 1, 1})));

    return input;
  }

  protected static List<Pair<String, Object>> getText() {
    List<Pair<String, Object>> input = new ArrayList<>();

    input.add(new Pair<String, Object>("'Test_string'", new String("Test string")));

    return input;
  }

  protected static List<Pair<String, Object>> getMatNormString() {
    List<Pair<String, Object>> input = new ArrayList<>();

    input.add(new Pair<String, Object>("'inf'", new String("inf")));
    input.add(new Pair<String, Object>("'fro'", new String("fro")));

    return input;
  }

  protected static List<Pair<String, Object>> getVecNormString() {
    List<Pair<String, Object>> input = new ArrayList<>();

    input.add(new Pair<String, Object>("'inf'", new String("inf")));
    input.add(new Pair<String, Object>("'-inf'", new String("-inf")));
    input.add(new Pair<String, Object>("'fro'", new String("fro")));

    return input;
  }

  protected static List<Pair<String, Object>> getSort() {
    List<Pair<String, Object>> input = new ArrayList<>();

    input.add(new Pair<String, Object>("'ascend'", new String("ascend")));
    input.add(new Pair<String, Object>("'descend'", new String("descend")));

    return input;
  }

  protected static List<Pair<String, Object>> getSearch() {
    List<Pair<String, Object>> input = new ArrayList<>();

    input.add(new Pair<String, Object>("'first'", new String("first")));
    input.add(new Pair<String, Object>("'last'", new String("last")));

    return input;
  }

  protected static List<Pair<String, Object>> getSinValSel() {
    List<Pair<String, Object>> input = new ArrayList<>();

    input.add(new Pair<String, Object>("'left'", new String("left")));
    input.add(new Pair<String, Object>("'right'", new String("right")));
    input.add(new Pair<String, Object>("'both'", new String("both")));

    return input;
  }

  protected static List<Pair<String, Object>> getDistrParam() {
    List<Pair<String, Object>> input = new ArrayList<>();

    input.add(new Pair<String, Object>("distr_param(0,10)", new DistrParam(0, 10)));
    input.add(new Pair<String, Object>("distr_param(1,1)", new DistrParam(1, 1)));
    input.add(new Pair<String, Object>("distr_param(-5,6)", new DistrParam(-5, 6)));

    return input;
  }

  protected static List<Pair<String, Object>> getFill() {
    return null;
  }

  protected static List<Pair<String, Object>> getRandom() {
    List<Pair<String, Object>> input = new ArrayList<>();

    input.add(new Pair<String, Object>("1000", 1000));

    return input;
  }
}
