/*******************************************************************************
 * Copyright 2013 Sebastian Niemann <niemann@sra.uni-hannover.de> and contributors.
 * 
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package arma;

/**
 * Provides support for Matlab's a:b
 * 
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 */
public class Span {
  /**
   * Start of the span.
   */
  private int     _start;
  /**
   * End of the span.
   */
  private int     _end;

  /**
   * Indicates whether a:b (false) or only : (true) was provided.
   */
  private boolean _isEntireRange;

  /**
   * Creates a span like Matlab's a:b but without creating a new matrix.
   * 
   * @param start The position a.
   * @param end The position b.
   */
  public Span(int start, int end) {
    if(end < start) {
      throw new IllegalArgumentException("The start needs to be less than equal the end but were " + start + " and " + end + ".");
    }
    
    _start = start;
    _end = end;

    _isEntireRange = false;
  }

  /**
   * Creates a span like Matlab's a:a but without creating a new matrix.
   * 
   * @param position The position a.
   */
  public Span(int position) {
    _start = position;
    _end = position;

    _isEntireRange = false;
  }

  /**
   * Creates a span like Matlab's : but without creating a new matrix.
   */
  public Span() {
    _isEntireRange = true;
  }

  /**
   * Return the start of the span.
   * 
   * @return The start.
   */
  int getStart() {
    return _start;
  }

  /**
   * Return the start of the span.
   * 
   * @return The end.
   */
  int getEnd() {
    return _end;
  }

  /**
   * Indicates whether a:b (false) or only : (true) was provided.
   * 
   * @return The boolean value.
   */
  boolean isEntireRange() {
    return _isEntireRange;
  }
}
