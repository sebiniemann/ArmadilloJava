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
  private int     _first;
  /**
   * End of the span.
   */
  private int     _last;

  /**
   * Indicates whether a:b (false) or only : (true) was provided.
   */
  private boolean _isEntireRange;

  /**
   * Creates a span like Matlab's a:b but without creating a new matrix.
   * 
   * @param first The position a.
   * @param last The position b.
   */
  public Span(int first, int last) {
    if (last < first) {
      throw new IllegalArgumentException("The first value needs to be less than equal the last but were " + first + " and " + last + ".");
    }

    _first = first;
    _last = last;

    _isEntireRange = false;
  }

  /**
   * Creates a span like Matlab's a:a but without creating a new matrix.
   * 
   * @param position The position a.
   */
  public Span(int position) {
    _first = position;
    _last = position;

    _isEntireRange = false;
  }

  /**
   * Creates a span like Matlab's : but without creating a new matrix.
   */
  public Span() {
    _isEntireRange = true;
  }

  /**
   * Creates a span like Matlab's : but without creating a new matrix.
   * 
   * @return The created span.
   */
  public static Span all() {
    return new Span();
  }

  /**
   * Return the start of the span.
   * 
   * @return The start.
   */
  int getFirst() {
    return _first;
  }

  /**
   * Return the start of the span.
   * 
   * @return The end.
   */
  int getLast() {
    return _last;
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
