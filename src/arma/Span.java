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
   * <p>
   * <b>Non-canonical:</b>
   * <ul>
   * <li>A {@code IllegalArgumentException} exception is thrown if the first provided value is larger than the last one.
   * <li>A {@code IllegalArgumentException} exception is thrown if any value negative.
   * <li>A {@code IllegalArgumentException} exception is thrown if any value is NaN or infinity.
   * </ul>
   * 
   * @param first The position a.
   * @param last The position b.
   * 
   * @throws IllegalArgumentException Thrown if the first provided value is larger than the last one or if any value is
   *           NaN or infinity or any value is negative.
   */
  public Span(int first, int last) throws IllegalArgumentException {
    if (last < first) {
      throw new IllegalArgumentException("The first value needs to be less than equal the last but were " + first + " and " + last + ".");
    }

    if (Double.isInfinite(first) || Double.isNaN(first) || Double.isInfinite(last) || Double.isNaN(last)) {
      throw new IllegalArgumentException("NaN or infinity is not supported.");
    }

    if (first < 0) {
      throw new IllegalArgumentException("All values must be non negative.");
    }

    _first = first;
    _last = last;

    _isEntireRange = false;
  }

  /**
   * Creates a span like Matlab's a:a but without creating a new matrix.
   * <p>
   * <b>Non-canonical:</b>
   * <ul>
   * <li>A {@code IllegalArgumentException} exception is thrown if the first provided value is larger than the last one.
   * <li>A {@code IllegalArgumentException} exception is thrown if any value is NaN or infinity.
   * <li>A {@code IllegalArgumentException} exception is thrown if any value is NaN or infinity.
   * </ul>
   * 
   * @param position The position a.
   * 
   * @throws IllegalArgumentException Thrown if the first provided value is larger than the last one or if any value is
   *           NaN or infinity or any value is negative.
   */
  public Span(int position) throws IllegalArgumentException {
    this(position, position);
  }

  /**
   * Creates a span like Matlab's : but without creating a new matrix.
   */
  public Span() {
    _first = -1; // Illegal value by design. Should result in an exception if used.
    _last = -1; // Illegal value by design. Should result in an exception if used.

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
