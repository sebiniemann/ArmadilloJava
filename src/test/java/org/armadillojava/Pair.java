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
 *   Sebastian Niemann - Lead developer
 *   Daniel Kiechle - Unit testing
 ******************************************************************************/
package org.armadillojava;

public class Pair<T1, T2> {
  protected T1 _first;
  protected T2 _second;

  public Pair(T1 first, T2 second) {
    _first = first;
    _second = second;
  }

  protected T1 getFirst() {
    return _first;
  }

  protected T2 getSecond() {
    return _second;
  }
}
