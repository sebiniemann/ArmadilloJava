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

/**
 * Provides statistical measures that are updated iterative per sample.
 * <p>
 * Useful if the storage of individual samples is not possible or not required.
 * 
 * @author Sebastian Niemann
 */
public class RunningStat {
  /**
   * The smallest sample
   */
  protected double _min;
  /**
   * The largest sample
   */
  protected double _max;
  /**
   * The amount of samples
   */
  protected double _count;
  /**
   * The mean of all samples
   */
  protected double _mean;
  /**
   * The variance of all samples
   */
  protected double _var;

  /**
   * Initialises the statistical measures.
   */
  public RunningStat() {
    reset();
  }

  /**
   * Recalculates the statistical values with inclusion of the sample.
   * 
   * @param sample The sample
   * 
   * @throws IllegalArgumentException NaN is not valid sample.
   * @throws UnsupportedOperationException No more than 2^53 (approx. 9 * 10^15) samples can be processed without loss
   *           of precision.
   */
  public void update(double sample) throws IllegalArgumentException, UnsupportedOperationException {
    if (Double.isNaN(sample)) {
      throw new IllegalArgumentException("NaN is not valid sample value.");
    }

    if (_count > 0) {
      if (_count >= 9007199254740992.0) { // 2^53
        throw new UnsupportedOperationException("No more than 2^53 (approx. 9 * 10^15) samples can be processed without loss of precision.");
      }

      _max = Math.max(_max, sample);
      _min = Math.min(_min, sample);
      _var = (_count - 1) / _count * _var + (Math.pow(sample - _mean, 2)) / (_count + 1);
      _mean = _mean + (sample - _mean) / (_count + 1);
    } else {
      _max = sample;
      _min = sample;
      _var = 0;
      _mean = sample;
    }

    _count++;
  }

  /**
   * Returns the amount of samples.
   * <p>
   * <b>Note:</b> Returns a double. Therefore, no more than 2^53 (approx. 9 * 10^15) samples can be processed before the
   * stored amount of samples suffers a loss of precision due to gaps between representable integers as double.
   * 
   * @return The amount
   */
  public double count() {
    return _count;
  }

  /**
   * Returns the smallest sample.
   * 
   * @return The minimum
   */
  public double min() {
    return _min;
  }

  /**
   * Returns the largest sample.
   * 
   * @return The maximum
   */
  public double max() {
    return _max;
  }

  /**
   * Returns the mean of all samples.
   * 
   * @return The mean
   */
  public double mean() {
    return _mean;
  }

  /**
   * Returns the variance of all samples with normalisation by {@link #count()} - 1.
   * 
   * @return The variance
   */
  public double var() {
    return var(0);
  }

  /**
   * Returns the variance of all samples.
   * <p>
   * Performs either normalisation by {@link #count()} - 1 ({@code normType} = 0) or {@code #count()} ({@code normType}
   * = 1).
   * 
   * @param normType The normalisation
   * @return The variance
   * 
   * @throws IllegalArgumentException The normalisation type must be one of 0 or 1, but was: {@code normType}.
   */
  public double var(int normType) throws IllegalArgumentException {
    if (normType == 0) {
      return _var;
    } else if (normType == 1) {
      if (_count > 0) {
        return (_count - 1) / _count * _var;
      } else {
        return _var;
      }
    } else {
      throw new IllegalArgumentException("The normalisation type must be one of 0 or 1, but was:" + normType + ".");
    }
  }

  /**
   * Returns the standard deviation of all samples with normalisation by {@link #count()} - 1.
   * 
   * @return The standard deviation.
   */
  public double stddev() {
    return Math.sqrt(var(0));
  }

  /**
   * Returns the standard deviation of all samples.
   * <p>
   * Performs either normalisation by {@link #count()} - 1 ({@code normType} = 0) or {@code #count()} ({@code normType}
   * = 1).
   * 
   * @param normType The normalisation
   * @return The standard deviation
   * 
   * @throws IllegalArgumentException The normalisation type must be one of 0 or 1, but was: {@code normType}.
   */
  public double stddev(int normType) throws IllegalArgumentException {
    return Math.sqrt(var(normType));
  }

  /**
   * Resets all statistical values.
   */
  public void reset() {
    _max = Double.NaN;
    _min = Double.NaN;
    _mean = Double.NaN;
    _var = Double.NaN;

    _count = 0;
  }
}
