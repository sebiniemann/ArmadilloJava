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
 * Provides statistical measures for large observations where the individual observations cannot be stored completely or
 * are not relevant.
 * 
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 */
public class RunningStat {

  /**
   * The smallest observed value.
   */
  private double _min;
  /**
   * The largest observed value.
   */
  private double _max;
  /**
   * The amount of observed samples.
   */
  private double _count;
  /**
   * The mean of all observed samples.
   */
  private double _mean;
  /**
   * The variance of all observed samples.
   */
  private double _var;

  /**
   * Initialises the statistical measures.
   */
  public RunningStat() {
    reset();
  }

  /**
   * Recalculates the statistical measures with the provided value.
   * 
   * @param sample The provided value.
   */
  public void update(double sample) {
    if (_count > 0) {
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
   * Returns the amount of observed samples.
   * 
   * @return The amount.
   */
  public double count() {
    return _count;
  }

  /**
   * Returns the smallest observed value.
   * 
   * @return The minimum.
   */
  public double min() {
    return _min;
  }

  /**
   * Returns the largest observed value.
   * 
   * @return The maximum.
   */
  public double max() {
    return _max;
  }

  /**
   * Returns the mean of all observed samples.
   * 
   * @return The mean.
   */
  public double mean() {
    return _mean;
  }

  /**
   * Returns the variance of all observed samples with normalisation by {@link #count()} - 1.
   * <p>
   * <b>Non-canonical:</b> An {@code IllegalArgumentException} exception is thrown if the normalisation type is not one
   * of 0 or 1.
   * 
   * @return The variance.
   */
  public double var() {
    return var(0);
  }

  /**
   * Returns the variance of all observed samples.
   * <p>
   * Performs either normalisation by {@link #count()} - 1 if {@code normType = 0} or by {@link #count()}.
   * <p>
   * <b>Non-canonical:</b> An {@code IllegalArgumentException} exception is thrown if the normalisation type is not one
   * of 0 or 1.
   * 
   * @param normType The normalisation to be used.
   * @return The variance.
   * 
   * @throws IllegalArgumentException Thrown if the normalisation type is not one of 0 or 1.
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
      throw new IllegalArgumentException("The normalisation type must be one of 0 or 1, but was :" + normType);
    }
  }

  /**
   * Returns the standard deviation of all observed samples with normalisation by {@link #count()} - 1.
   * <p>
   * <b>Non-canonical:</b> An {@code IllegalArgumentException} exception is thrown if the normalisation type is not one
   * of 0 or 1.
   * 
   * @return The standard deviation.
   */
  public double stddev() {
    return Math.sqrt(var(0));
  }

  /**
   * Returns the standard deviation of all observed samples.
   * <p>
   * Performs either normalisation by {@link #count()} - 1 if {@code normType = 0} or by {@link #count()}.
   * <p>
   * <b>Non-canonical:</b> An {@code IllegalArgumentException} exception is thrown if the normalisation type is not one
   * of 0 or 1.
   * 
   * @param normType The normalisation to be used.
   * @return The standard deviation.
   * 
   * @throws IllegalArgumentException Thrown if the normalisation type is not one of 0 or 1.
   */
  public double stddev(int normType) throws IllegalArgumentException {
    return Math.sqrt(var(normType));
  }

  /**
   * Resets the observation.
   */
  public void reset() {
    _max = Double.NaN;
    _min = Double.NaN;
    _mean = Double.NaN;
    _var = Double.NaN;

    _count = 0;
  }
}
