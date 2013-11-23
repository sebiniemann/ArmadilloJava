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
 * Provides support for Matlab's index expression range a:b.
 * 
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 */
public class Span {
  /**
   * First position of the span
   */
  private int     _first;
  /**
   * Last position of the span
   */
  private int     _last;
  /**
   * Whether the span includes the entire range
   */
  private boolean _isEntireRange;

  /**
   * Creates a span similar to Matlab's index expression range a:b, but without creating a vector.
   * 
   * @param first The first element of the span (a)
   * @param last The last element of the span (b)
   * 
   * @throws IllegalArgumentException The first position needs to be less than equal the last but were {@code first} and
   *           {@code last}.
   * @throws IllegalArgumentException All position must be non-negative.
   * @throws IllegalArgumentException NaN and infinity are not valid element positions.
   */
  public Span(int first, int last) throws IllegalArgumentException {
    if (last < first) {
      throw new IllegalArgumentException("The first position needs to be less than equal the last but were " + first + " and " + last + ".");
    }

    if (first < 0) {
      throw new IllegalArgumentException("All position must be non-negative.");
    }

    if (Double.isInfinite(first) || Double.isNaN(first) || Double.isInfinite(last) || Double.isNaN(last)) {
      throw new IllegalArgumentException("NaN and infinity are not valid positions value.");
    }

    _first = first;
    _last = last;

    _isEntireRange = false;
  }

  /**
   * Creates a span similar to Matlab's index expression range a:a, but without creating a vector.
   * 
   * @param position The position (a)
   * 
   * @throws IllegalArgumentException All position must be non-negative.
   * @throws IllegalArgumentException NaN and infinity are not valid element positions.
   */
  public Span(int position) throws IllegalArgumentException {
    this(position, position);
  }

  /**
   * Creates a span similar to Matlab's index expression special range :, but without creating a vector.
   */
  public Span() {
    // Illegal values by design. Should result in an exception if used together with other provided methods/classes.
    _first = -1;
    _last = -1;

    _isEntireRange = true;
  }

  /**
   * Creates a span similar to Matlab's index expression special range :, but without creating a vector.
   * 
   * @return The span
   */
  public static Span all() {
    return new Span();
  }

  /**
   * Return the last position of the span.
   * 
   * @return The position
   */
  protected int getFirst() {
    return _first;
  }

  /**
   * Return the first position of the span.
   * 
   * @return The position
   */
  protected int getLast() {
    return _last;
  }

  /**
   * Returns whether the span should include the entire range (true) or a range from the first to the last position
   * (false).
   * 
   * @return Whether the span includes the entire range
   */
  protected boolean isEntireRange() {
    return _isEntireRange;
  }
}
