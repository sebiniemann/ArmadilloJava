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
        case TriDouble:
          inputs.add(getTriDouble());
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
        case ElemIndsAsColVec:
          inputs.add(getElemIndsAsColVec());
          break;
        case ElemIndsAsRowVec:
          inputs.add(getElemIndsAsRowVec());
          break;
        case ColIndsAsColVec:
          inputs.add(getColIndsAsColVec());
          break;
        case ColIndsAsRowVec:
          inputs.add(getColIndsAsRowVec());
          break;
        case RowIndsAsColVec:
          inputs.add(getRowIndsAsColVec());
          break;
        case RowIndsAsRowVec:
          inputs.add(getRowIndsAsRowVec());
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

      
      if(input.size() > 0 && !vectorUnion.equals(input)) {
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
    List<List<Pair<String, Object>>> inputs = new ArrayList<>();

    List<Pair<String, Object>> input = new ArrayList<>();
    input.add(new Pair<String, Object>("0", 0));
    input.add(new Pair<String, Object>("1", 1));
    inputs.add(input);

    for (Pair<String, Object> keyValuePair : getNumElems()) {
      int numElems = (int) (keyValuePair.getSecond());

      input.clear();
      input.add(new Pair<String, Object>(Integer.toString(numElems - 1), numElems - 1));
      inputs.add(input);
    }

    return vectorUnion(inputs);
  }

  protected static List<Pair<String, Object>> getColInd() {
    return getRowInd();
  }

  protected static List<Pair<String, Object>> getExtColInd() {
    return getExtRowInd();
  }

  protected static List<Pair<String, Object>> getRowInd() {
    List<List<Pair<String, Object>>> inputs = new ArrayList<>();

    List<Pair<String, Object>> input = new ArrayList<>();
    input.add(new Pair<String, Object>("0", 0));
    input.add(new Pair<String, Object>("1", 1));
    inputs.add(input);

    for (Pair<String, Object> keyValuePair : getNumRows()) {
      int numRows = (int) (keyValuePair.getSecond());

      input.clear();
      input.add(new Pair<String, Object>(Integer.toString(numRows - 1), numRows - 1));
      inputs.add(input);
    }
    
    return vectorUnion(inputs);
  }

  protected static List<Pair<String, Object>> getExtRowInd() {
    List<List<Pair<String, Object>>> inputs = new ArrayList<>();

    inputs.add(getRowInd());
    inputs.add(getNumRows());

    return vectorUnion(inputs);
  }

  protected static List<Pair<String, Object>> getNumElems() {
    List<Pair<String, Object>> input = new ArrayList<>();

    input.add(new Pair<String, Object>("1", 1));
    input.add(new Pair<String, Object>("2", 2));
    input.add(new Pair<String, Object>("25", 25));

    return input;
  }

  protected static List<Pair<String, Object>> getNumCols() {
    return getNumRows();
  }

  protected static List<Pair<String, Object>> getNumRows() {
    List<Pair<String, Object>> input = new ArrayList<>();

    input.add(new Pair<String, Object>("1", 1));
    input.add(new Pair<String, Object>("2", 2));
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
    input.add(new Pair<String, Object>("3.0", 3.0));
    input.add(new Pair<String, Object>("4.0", 4.0));
    input.add(new Pair<String, Object>("inf", Datum.inf));

    List<List<Pair<String, Object>>> inputs = new ArrayList<>();

    inputs.add(input);
    inputs.add(getTriDouble());

    return vectorUnion(inputs);
  }

  protected static List<Pair<String, Object>> getTriDouble() {
    List<Pair<String, Object>> input = new ArrayList<>();

    input.add(new Pair<String, Object>("-inf", -Datum.inf));
    input.add(new Pair<String, Object>("-2.0", -2.0));
    input.add(new Pair<String, Object>("0.0", 0.0));
    input.add(new Pair<String, Object>("machine_epsilon", Datum.eps));
    input.add(new Pair<String, Object>("1.0", 1.0));
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
    List<List<Pair<String, Object>>> inputs = new ArrayList<>();

    List<Pair<String, Object>> input = new ArrayList<>();
    input.add(new Pair<String, Object>("span(0,0)", new Span(0, 0)));
    inputs.add(input);

    for (Pair<String, Object> keyValuePair : getNumElems()) {
      int numElems = (int) keyValuePair.getSecond();

      input.clear();
      input.add(new Pair<String, Object>("span(0,0)", new Span(0, 0)));
      input.add(new Pair<String, Object>("span(0," + (numElems - 1) + ")", new Span(0, numElems - 1)));
      input.add(new Pair<String, Object>("span(" + (numElems / 2 - 1) + "," + (numElems / 2 + 1) + ")", new Span(numElems / 2 - 1, numElems / 2 + 1)));
      inputs.add(input);
    }

    return vectorUnion(inputs);
  }

  protected static List<Pair<String, Object>> getColIndRange() {
    return getRowIndRange();
  }

  protected static List<Pair<String, Object>> getRowIndRange() {
    List<List<Pair<String, Object>>> inputs = new ArrayList<>();

    List<Pair<String, Object>> input = new ArrayList<>();
    input.add(new Pair<String, Object>("span(0,0)", new Span(0, 0)));
    inputs.add(input);

    for (Pair<String, Object> keyValuePair : getNumRows()) {
      int numRows = (int) keyValuePair.getSecond();

      input.clear();
      input.add(new Pair<String, Object>("span(0,0)", new Span(0, 0)));
      input.add(new Pair<String, Object>("span(0," + (numRows - 1) + ")", new Span(0, numRows - 1)));
      input.add(new Pair<String, Object>("span(" + (numRows / 2 - 1) + "," + (numRows / 2 + 1) + ")", new Span(numRows / 2 - 1, numRows / 2 + 1)));
      inputs.add(input);
    }

    return vectorUnion(inputs);
  }

  protected static List<Pair<String, Object>> getMatSize() {
    List<Pair<String, Object>> input = new ArrayList<>();

    for (Pair<String, Object> keyValuePairA : getNumRows()) {
      int numRows = (int) keyValuePairA.getSecond();

      for (Pair<String, Object> keyValuePairB : getNumCols()) {
        int numCols = (int) keyValuePairB.getSecond();

        input.add(new Pair<String, Object>("size(" + numRows + "," + numCols + ")", new Size(numRows, numCols)));
        input.add(new Pair<String, Object>("size(Mat(" + numRows + "," + numCols + "))", new Size(new Mat(numRows, numCols))));
      }
    }

    return input;
  }

  protected static List<Pair<String, Object>> getGenMat() {
    List<Pair<String, Object>> input = new ArrayList<>();

    for (Pair<String, Object> keyValuePairA : getNumRows()) {
      int numRows = (int) keyValuePairA.getSecond();

      for (Pair<String, Object> keyValuePairB : getNumCols()) {
        int numCols = (int) keyValuePairB.getSecond();

        input.add(new Pair<String, Object>("Mat(zeros(" + numRows + "," + numCols + "))", new Mat(Arma.zeros(numRows, numCols))));
        input.add(new Pair<String, Object>("Mat(ones(" + numRows + "," + numCols + "))", new Mat(Arma.ones(numRows, numCols))));
        input.add(new Pair<String, Object>("Mat(eye(" + numRows + "," + numCols + "))", new Mat(Arma.eye(numRows, numCols))));
        input.add(new Pair<String, Object>("Mat(hilbert(" + numRows + "," + numCols + "))", new Mat(getHilbertMatrix(numRows, numCols))));
        input.add(new Pair<String, Object>("Mat(kms(" + numRows + "," + numCols + "))", new Mat(getKMSMatrix(numRows, numCols))));
      }
    }

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

    for (Pair<String, Object> keyValuePairA : getNumRows()) {
      int numRows = (int) keyValuePairA.getSecond();

      for (Pair<String, Object> keyValuePairB : getNumCols()) {
        int numCols = (int) keyValuePairB.getSecond();

        if (numRows == numCols) {
          input.add(new Pair<String, Object>("Mat(eye(" + numRows + "," + numCols + "))", new Mat(Arma.eye(numRows, numCols))));
          input.add(new Pair<String, Object>("Mat(kms(" + numRows + "," + numCols + "))", new Mat(getKMSMatrix(numRows, numCols))));
        }
      }
    }

    return input;
  }

  protected static List<Pair<String, Object>> getSymMat() {
    List<List<Pair<String, Object>>> inputs = new ArrayList<>();
    inputs.add(getSymPDMat());

    for (Pair<String, Object> keyValuePairA : getNumRows()) {
      int numRows = (int) keyValuePairA.getSecond();

      for (Pair<String, Object> keyValuePairB : getNumCols()) {
        int numCols = (int) keyValuePairB.getSecond();

        if (numRows == numCols) {
          List<Pair<String, Object>> input = new ArrayList<>();
          input.add(new Pair<String, Object>("Mat(zeros(" + numRows + "," + numCols + "))", new Mat(Arma.zeros(numRows, numCols))));
          input.add(new Pair<String, Object>("Mat(ones(" + numRows + "," + numCols + "))", new Mat(Arma.ones(numRows, numCols))));
          inputs.add(input);
        }
      }
    }

    return vectorUnion(inputs);
  }

  protected static List<Pair<String, Object>> getSymPDMat() {
    List<Pair<String, Object>> input = new ArrayList<>();

    for (Pair<String, Object> keyValuePairA : getNumRows()) {
      int numRows = (int) keyValuePairA.getSecond();

      for (Pair<String, Object> keyValuePairB : getNumCols()) {
        int numCols = (int) keyValuePairB.getSecond();

        if (numRows == numCols) {
          input.add(new Pair<String, Object>("Mat(eye(" + numRows + "," + numCols + "))", new Mat(Arma.eye(numRows, numCols))));
          input.add(new Pair<String, Object>("Mat(hilbert(" + numRows + "," + numCols + "))", new Mat(getHilbertMatrix(numRows, numCols))));
        }
      }
    }

    return input;
  }

  protected static List<Pair<String, Object>> getLogicMat() {
    List<Pair<String, Object>> input = new ArrayList<>();

    for (Pair<String, Object> keyValuePairA : getNumRows()) {
      int numRows = (int) keyValuePairA.getSecond();

      for (Pair<String, Object> keyValuePairB : getNumCols()) {
        int numCols = (int) keyValuePairB.getSecond();

        input.add(new Pair<String, Object>("Mat(zeros(" + numRows + "," + numCols + "))", new Mat(Arma.zeros(numRows, numCols))));
        input.add(new Pair<String, Object>("Mat(ones(" + numRows + "," + numCols + "))", new Mat(Arma.ones(numRows, numCols))));
        input.add(new Pair<String, Object>("Mat(hilbertSub(" + numRows + "," + numCols + "))", new Mat(getHilbertMatrix(numRows, numCols).minus(2.0 / (numRows + numCols + 2)))));
      }
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

    for (Pair<String, Object> keyValuePairA : getNumRows()) {
      int numRows = (int) keyValuePairA.getSecond();

      input.add(new Pair<String, Object>("Col(zeros(" + numRows + ",1))", new Col(Arma.zeros(Col.class, numRows))));
      input.add(new Pair<String, Object>("Col(ones(" + numRows + ",1))", new Col(Arma.ones(Col.class, numRows))));

      for (Pair<String, Object> keyValuePairB : getNumCols()) {
        int numCols = (int) keyValuePairB.getSecond();

        Mat identity = Arma.eye(numRows, numCols);
        Mat hilbert = getHilbertMatrix(numRows, numCols);
        Mat kms = getKMSMatrix(numRows, numCols);

        input.add(new Pair<String, Object>("Col(eye(" + numRows + "," + numCols + ").col(0))", new Col(identity.col(0))));
        input.add(new Pair<String, Object>("Col(hilbert(" + numRows + "," + numCols + ").col(0))", new Col(hilbert.col(0))));
        input.add(new Pair<String, Object>("Col(kms(" + numRows + "," + numCols + ").col(0))", new Col(kms.col(0))));
      }
    }

    return input;
  }

  protected static List<Pair<String, Object>> getMonColVec() {
    List<Pair<String, Object>> input = new ArrayList<>();

    input.add(new Pair<String, Object>("Col({0,1,...,n})", new Col(new double[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10})));
    input.add(new Pair<String, Object>("Col({0,0.1,...,1})", new Col(new double[]{0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1})));
    input.add(new Pair<String, Object>("Col({-10,-5,10})", new Col(new double[]{-10, -5, 10})));
    input.add(new Pair<String, Object>("Col({-inf,0,inf})", new Col(new double[]{Datum.inf, 0, -Datum.inf})));
    input.add(new Pair<String, Object>("Col({0})", new Col(new double[]{0})));
    input.add(new Pair<String, Object>("Col({-inf})", new Col(new double[]{-Datum.inf})));
    input.add(new Pair<String, Object>("Col({inf})", new Col(new double[]{Datum.inf})));

    return input;
  }

  protected static List<Pair<String, Object>> getLogicColVec() {
    List<Pair<String, Object>> input = new ArrayList<>();

    for (Pair<String, Object> keyValuePairA : getNumRows()) {
      int numRows = (int) keyValuePairA.getSecond();

      input.add(new Pair<String, Object>("Col(zeros(" + numRows + ",1))", new Col(Arma.zeros(Col.class, numRows))));
      input.add(new Pair<String, Object>("Col(ones(" + numRows + ",1))", new Col(Arma.ones(Col.class, numRows))));

      for (Pair<String, Object> keyValuePairB : getNumCols()) {
        int numCols = (int) keyValuePairB.getSecond();

        Mat identity = Arma.eye(numRows, numCols);
        Mat hilbertSub = getHilbertMatrix(numRows, numCols).minus(2.0 / (numRows + numCols + 2));

        input.add(new Pair<String, Object>("Col(eye(" + numRows + "," + numCols + ").col(0))", new Col(identity.col(0))));
        input.add(new Pair<String, Object>("Col(hilbertSub(" + numRows + "," + numCols + ").col(0))", new Col(hilbertSub.col(0))));
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

    for (Pair<String, Object> keyValuePairA : getNumCols()) {
      int numCols = (int) keyValuePairA.getSecond();

      input.add(new Pair<String, Object>("Row(zeros(1," + numCols + "))", new Row(Arma.zeros(Row.class, numCols))));
      input.add(new Pair<String, Object>("Row(ones(1," + numCols + "))", new Row(Arma.ones(Row.class, numCols))));

      for (Pair<String, Object> keyValuePairB : getNumRows()) {
        int numRows = (int) keyValuePairB.getSecond();

        Mat identity = Arma.eye(numRows, numCols);
        Mat hilbert = getHilbertMatrix(numRows, numCols);
        Mat kms = getKMSMatrix(numRows, numCols);

        input.add(new Pair<String, Object>("Row(eye(" + numRows + "," + numCols + ").row(0))", new Row(identity.row(0))));
        input.add(new Pair<String, Object>("Row(hilbert(" + numRows + "," + numCols + ").row(0))", new Row(hilbert.row(0))));
        input.add(new Pair<String, Object>("Row(kms(" + numRows + "," + numCols + ").row(0))", new Row(kms.row(0))));
      }
    }

    return input;
  }

  protected static List<Pair<String, Object>> getMonRowVec() {
    List<Pair<String, Object>> input = new ArrayList<>();

    input.add(new Pair<String, Object>("Row({0,1,...,n})", new Row(new double[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10})));
    input.add(new Pair<String, Object>("Row({0,0.1,...,1})", new Row(new double[]{0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1})));
    input.add(new Pair<String, Object>("Row({-10,-5,10})", new Row(new double[]{-10, -5, 10})));
    input.add(new Pair<String, Object>("Row({-inf,0,inf})", new Row(new double[]{Datum.inf, 0, -Datum.inf})));
    input.add(new Pair<String, Object>("Row({0})", new Row(new double[]{0})));
    input.add(new Pair<String, Object>("Row({-inf})", new Row(new double[]{-Datum.inf})));
    input.add(new Pair<String, Object>("Row({inf})", new Row(new double[]{Datum.inf})));

    return input;
  }

  protected static List<Pair<String, Object>> getLogicRowVec() {
    List<Pair<String, Object>> input = new ArrayList<>();

    for (Pair<String, Object> keyValuePairA : getNumCols()) {
      int numCols = (int) keyValuePairA.getSecond();

      input.add(new Pair<String, Object>("Row(zeros(1," + numCols + "))", new Row(Arma.zeros(Row.class, numCols))));
      input.add(new Pair<String, Object>("Row(ones(1," + numCols + "))", new Row(Arma.ones(Row.class, numCols))));

      for (Pair<String, Object> keyValuePairB : getNumRows()) {
        int numRows = (int) keyValuePairB.getSecond();

        Mat identity = Arma.eye(numRows, numCols);
        Mat hilbertSub = getHilbertMatrix(numRows, numCols).minus(2.0 / (numRows + numCols + 2));

        input.add(new Pair<String, Object>("Row(eye(" + numRows + "," + numCols + ").row(0))", new Row(identity.row(0))));
        input.add(new Pair<String, Object>("Row(hilbertSub(" + numRows + "," + numCols + ").row(0))", new Row(hilbertSub.row(0))));
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

  protected static List<Pair<String, Object>> getElemIndsAsColVec() {
    List<List<Pair<String, Object>>> inputs = new ArrayList<>();

    List<Pair<String, Object>> input = new ArrayList<>();
    input.add(new Pair<String, Object>("Col({0})", new Col(new double[]{0})));
    input.add(new Pair<String, Object>("Col({1,1,1,1,1})", new Col(new double[]{1, 1, 1, 1, 1})));
    inputs.add(input);

    for (Pair<String, Object> keyValuePair : getNumElems()) {
      int numElems = (Integer) (keyValuePair.getSecond());

      input.clear();
      input.add(new Pair<String, Object>("Col({" + (numElems - 1) + "})", new Col(new double[]{numElems - 1})));
      input.add(new Pair<String, Object>("Col({" + (numElems / 2 - 1) + "," + (numElems / 2) + "," + (numElems / 2 + 1) + "})", new Col(new double[]{numElems / 2 - 1, numElems / 2, numElems / 2 + 1})));
      inputs.add(input);

      double[] sequenceA = new double[numElems];
      double[] sequenceB = new double[numElems];
      for (int n = 0; n < numElems; n++) {
        sequenceA[n] = n;
        sequenceB[n] = ((n % 2 == 0) ? n : numElems - n);
      }

      input.clear();
      input.add(new Pair<String, Object>("Col({0,1,...,n})", new Col(sequenceA)));
      input.add(new Pair<String, Object>("Col({0,n,1,n-1,...})", new Col(sequenceB)));
      inputs.add(input);
    }

    return vectorUnion(inputs);
  }

  protected static List<Pair<String, Object>> getElemIndsAsRowVec() {
    List<List<Pair<String, Object>>> inputs = new ArrayList<>();

    List<Pair<String, Object>> input = new ArrayList<>();
    input.add(new Pair<String, Object>("Row({0})", new Row(new double[]{0})));
    input.add(new Pair<String, Object>("Row({1,1,1,1,1})", new Row(new double[]{1, 1, 1, 1, 1})));
    inputs.add(input);

    for (Pair<String, Object> keyValuePair : getNumElems()) {
      int numElems = (Integer) (keyValuePair.getSecond());

      input.clear();
      input.add(new Pair<String, Object>("Row({" + (numElems - 1) + "})", new Row(new double[]{numElems - 1})));
      input.add(new Pair<String, Object>("Row({" + (numElems / 2 - 1) + "," + (numElems / 2) + "," + (numElems / 2 + 1) + "})", new Row(new double[]{numElems / 2 - 1, numElems / 2, numElems / 2 + 1})));
      inputs.add(input);

      double[] sequenceA = new double[numElems];
      double[] sequenceB = new double[numElems];
      for (int n = 0; n < numElems; n++) {
        sequenceA[n] = n;
        sequenceB[n] = ((n % 2 == 0) ? n : numElems - n);
      }

      input.clear();
      input.add(new Pair<String, Object>("Row({0,1,...,n})", new Row(sequenceA)));
      input.add(new Pair<String, Object>("Row({0,n,1,n-1,...})", new Row(sequenceB)));
      inputs.add(input);
    }

    return vectorUnion(inputs);
  }

  protected static List<Pair<String, Object>> getColIndsAsColVec() {
    List<List<Pair<String, Object>>> inputs = new ArrayList<>();

    List<Pair<String, Object>> input = new ArrayList<>();
    input.add(new Pair<String, Object>("Col({0})", new Col(new double[]{0})));
    input.add(new Pair<String, Object>("Col({1,1,1,1,1})", new Col(new double[]{1, 1, 1, 1, 1})));
    inputs.add(input);

    for (Pair<String, Object> keyValuePair : getNumCols()) {
      int numCols = (Integer) (keyValuePair.getSecond());

      input.clear();
      input.add(new Pair<String, Object>("Col({" + (numCols - 1) + "})", new Col(new double[]{numCols - 1})));
      input.add(new Pair<String, Object>("Col({" + (numCols / 2 - 1) + "," + (numCols / 2) + "," + (numCols / 2 + 1) + "})", new Col(new double[]{numCols / 2 - 1, numCols / 2, numCols / 2 + 1})));
      inputs.add(input);

      double[] sequenceA = new double[numCols];
      double[] sequenceB = new double[numCols];
      for (int n = 0; n < numCols; n++) {
        sequenceA[n] = n;
        sequenceB[n] = ((n % 2 == 0) ? n : numCols - n);
      }

      input.clear();
      input.add(new Pair<String, Object>("Col({0,1,...,n})", new Col(sequenceA)));
      input.add(new Pair<String, Object>("Col({0,n,1,n-1,...})", new Col(sequenceB)));
      inputs.add(input);
    }

    return vectorUnion(inputs);
  }

  protected static List<Pair<String, Object>> getColIndsAsRowVec() {
    List<List<Pair<String, Object>>> inputs = new ArrayList<>();

    List<Pair<String, Object>> input = new ArrayList<>();
    input.add(new Pair<String, Object>("Row({0})", new Row(new double[]{0})));
    input.add(new Pair<String, Object>("Row({1,1,1,1,1})", new Row(new double[]{1, 1, 1, 1, 1})));
    inputs.add(input);

    for (Pair<String, Object> keyValuePair : getNumCols()) {
      int numCols = (Integer) (keyValuePair.getSecond());

      input.clear();
      input.add(new Pair<String, Object>("Row({" + (numCols - 1) + "})", new Row(new double[]{numCols - 1})));
      input.add(new Pair<String, Object>("Row({" + (numCols / 2 - 1) + "," + (numCols / 2) + "," + (numCols / 2 + 1) + "})", new Row(new double[]{numCols / 2 - 1, numCols / 2, numCols / 2 + 1})));
      inputs.add(input);

      double[] sequenceA = new double[numCols];
      double[] sequenceB = new double[numCols];
      for (int n = 0; n < numCols; n++) {
        sequenceA[n] = n;
        sequenceB[n] = ((n % 2 == 0) ? n : numCols - n);
      }

      input.clear();
      input.add(new Pair<String, Object>("Row({0,1,...,n})", new Row(sequenceA)));
      input.add(new Pair<String, Object>("Row({0,n,1,n-1,...})", new Row(sequenceB)));
      inputs.add(input);
    }

    return vectorUnion(inputs);
  }

  protected static List<Pair<String, Object>> getRowIndsAsColVec() {
    List<List<Pair<String, Object>>> inputs = new ArrayList<>();

    List<Pair<String, Object>> input = new ArrayList<>();
    input.add(new Pair<String, Object>("Col({0})", new Col(new double[]{0})));
    input.add(new Pair<String, Object>("Col({1,1,1,1,1})", new Col(new double[]{1, 1, 1, 1, 1})));
    inputs.add(input);

    for (Pair<String, Object> keyValuePair : getNumRows()) {
      int numRows = (Integer) (keyValuePair.getSecond());

      input.clear();
      input.add(new Pair<String, Object>("Col({" + (numRows - 1) + "})", new Col(new double[]{numRows - 1})));
      input.add(new Pair<String, Object>("Col({" + (numRows / 2 - 1) + "," + (numRows / 2) + "," + (numRows / 2 + 1) + "})", new Col(new double[]{numRows / 2 - 1, numRows / 2, numRows / 2 + 1})));
      inputs.add(input);

      double[] sequenceA = new double[numRows];
      double[] sequenceB = new double[numRows];
      for (int n = 0; n < numRows; n++) {
        sequenceA[n] = n;
        sequenceB[n] = ((n % 2 == 0) ? n : numRows - n);
      }

      input.clear();
      input.add(new Pair<String, Object>("Col({0,1,...,n})", new Col(sequenceA)));
      input.add(new Pair<String, Object>("Col({0,n,1,n-1,...})", new Col(sequenceB)));
      inputs.add(input);
    }

    return vectorUnion(inputs);
  }

  protected static List<Pair<String, Object>> getRowIndsAsRowVec() {
    List<List<Pair<String, Object>>> inputs = new ArrayList<>();

    List<Pair<String, Object>> input = new ArrayList<>();
    input.add(new Pair<String, Object>("Row({0})", new Row(new double[]{0})));
    input.add(new Pair<String, Object>("Row({1,1,1,1,1})", new Row(new double[]{1, 1, 1, 1, 1})));
    inputs.add(input);

    for (Pair<String, Object> keyValuePair : getNumRows()) {
      int numRows = (Integer) (keyValuePair.getSecond());

      input.clear();
      input.add(new Pair<String, Object>("Row({" + (numRows - 1) + "})", new Row(new double[]{numRows - 1})));
      input.add(new Pair<String, Object>("Row({" + (numRows / 2 - 1) + "," + (numRows / 2) + "," + (numRows / 2 + 1) + "})", new Row(new double[]{numRows / 2 - 1, numRows / 2, numRows / 2 + 1})));
      inputs.add(input);

      double[] sequenceA = new double[numRows];
      double[] sequenceB = new double[numRows];
      for (int n = 0; n < numRows; n++) {
        sequenceA[n] = n;
        sequenceB[n] = ((n % 2 == 0) ? n : numRows - n);
      }

      input.clear();
      input.add(new Pair<String, Object>("Row({0,1,...,n})", new Row(sequenceA)));
      input.add(new Pair<String, Object>("Row({0,n,1,n-1,...})", new Row(sequenceB)));
      inputs.add(input);
    }

    return vectorUnion(inputs);
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
