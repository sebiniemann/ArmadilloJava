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

enum InputClass {
  ElemInd,
  ColInd,
  ExtColInd,
  RowInd,
  ExtRowInd,
  NumElems,
  NumCols,
  NumRows,
  Normal,
  Dim,
  Exp,
  MatNormInt,
  VecNormInt,
  GenDouble,
  SinValTol,
  ElemIndRange,
  ColIndRange,
  RowIndRange,
  MatSize,
  GenMat,
  SquMat,
  InvMat,
  SymMat,
  SymPDMat,
  LogicMat,
  OOMat,
  GenColVec,
  MonColVec,
  LogicColVec,
  OOColVec,
  GenRowVec,
  MonRowVec,
  LogicRowVec,
  OORowVec,
  ElemIndsAsColVec,
  ElemIndsAsRowVec,
  ColIndsAsColVec,
  ColIndsAsRowVec,
  RowIndsAsColVec,
  RowIndsAsRowVec,
  // Text, Untested
  FilePath,
  MatNormString,
  VecNormString,
  Sort,
  Search,
  SinValSel,
  UnOp,
  ElemWiseOp,
  BinOp,
  RelOp,
  OutputStream,
  InputStream,
  FileType,
  DistrParam,
  Fill,
  Random
}
