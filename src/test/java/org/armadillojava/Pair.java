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
