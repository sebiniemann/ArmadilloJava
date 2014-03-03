/*******************************************************************************
 * Copyright 2013 Sebastian Niemann <niemann@sra.uni-hannover.de> and contributors.
 * 
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://opensource.org/licenses/MIT
 *******************************************************************************/

package org.armadillojava;

/**
 * Provides statistical measures that are updated iterative per sample vector.
 * <p>
 * Useful if the storage of individual samples is not possible or not required.
 * <p>
 * Besides the covariance, each measurement is calculated per dimension.
 * 
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 */
public class RunningStatVec {
  /**
   * Whether to calculate the covariance
   */
  protected final boolean _calculateCovariance;
  /**
   * The smallest value
   */
  protected Col           _min;
  /**
   * The largest value
   */
  protected Col           _max;
  /**
   * The amount of values
   */
  protected double        _count;
  /**
   * The mean of all values
   */
  protected Col           _mean;
  /**
   * The variance of all values
   */
  protected Col           _var;
  /**
   * The covariance of all values
   */
  protected Mat           _cov;

  /**
   * Initialises the statistical measures. Calculation of the covariance will be deactivated.
   */
  public RunningStatVec() {
    this(false);
  }

  /**
   * Initialises the statistical measures.
   * <p>
   * {@code calculateCovariance} must be set to true to activate the calculation of the covariance.
   * 
   * @param calculateCovariance Whether to calculate the covariance
   */
  public RunningStatVec(boolean calculateCovariance) {
    _calculateCovariance = calculateCovariance;

    reset();
  }

  /**
   * Recalculates the statistical values with inclusion of the sample vector.
   * 
   * @param samples The vector
   * 
   * @throws IllegalArgumentException NaN is not valid sample value for any element.
   * @throws UnsupportedOperationException No more than 2^53 (approx. 9 * 10^15) samples can be processed without loss
   *           of precision.
   */
  public void update(Col samples) throws IllegalArgumentException, UnsupportedOperationException {
    // TODO add non vector detection

    // if (!samples.is_number()) {
    // throw new IllegalArgumentException("NaN is not valid sample value for any element.");
    // }

    // samples.isEmptyDetection();
    // TODO fix

    if (_count > 0) {
      // AbstractMat.isNonEqualNumberOfElementsDetection(_max.n_elem, samples.n_elem);
      // TODO fix

      if (_count >= 9007199254740992.0) { // 2^53
        throw new UnsupportedOperationException("No more than 2^53 (approx. 9 * 10^15) samples can be processed without loss of precision.");
      }

      if (_calculateCovariance) {
        Col temp = samples.minus(_mean);
        _cov.inPlace(Op.ELEMTIMES, (_count - 1) / _count);
        _cov.inPlace(Op.PLUS, temp.times(temp.t()).elemDivide(_count + 1));
      }

      for (int n = 0; n < samples.n_elem; n++) {
        _max.at(n, Op.EQUAL, Math.max(_max.at(n), samples.at(n)));
        _min.at(n, Op.EQUAL, Math.min(_max.at(n), samples.at(n)));
      }

      _var = (Arma.pow(samples.minus(_mean), 2)).elemDivide(_count + 1).plus(_var.times((_count - 1) / _count));
      _mean = _mean.plus(samples.minus(_mean).elemDivide(_count + 1));

    } else {
      _cov = new Mat(samples.n_elem, samples.n_elem, Fill.ZEROS);
      _max = new Col(samples);
      _min = new Col(samples);
      _var = new Col(samples.n_elem, Fill.ZEROS);
      _mean = new Col(samples);
    }

    _count++;
  }

  /**
   * Returns the amount of samples.
   * 
   * @return The amount
   */
  public double count() {
    return _count;
  }

  /**
   * Returns the smallest value per dimension.
   * 
   * @return The minimum
   */
  public Col min() {
    return _min;
  }

  /**
   * Returns the largest value per dimension.
   * 
   * @return The maximum
   */
  public Col max() {
    return _max;
  }

  /**
   * Returns the mean of all samples per dimension.
   * 
   * @return The mean
   */
  public Col mean() {
    return _mean;
  }

  /**
   * Returns the variance of all samples per dimension with normalisation by {@link #count()} - 1.
   * 
   * @return The variance
   */
  public Col var() {
    return var(0);
  }

  /**
   * Returns the variance of all samples per dimension.
   * <p>
   * Performs either normalisation by {@link #count()} - 1 ({@code normType} = 0) or {@code #count()} ({@code normType}
   * = 1).
   * 
   * @param normType The normalisation
   * @return The variance
   * 
   * @throws IllegalArgumentException The normalisation type must be one of 0 or 1, but was: {@code normType}.
   */
  public Col var(int normType) throws IllegalArgumentException {
    if (normType == 0) {
      return _var;
    } else if (normType == 1) {
      if (_count > 0) {
        return _var.times((_count - 1) / _count);
      } else {
        return _var;
      }
    } else {
      throw new IllegalArgumentException("The normalisation type must be one of 0 or 1, but was:" + normType + ".");
    }
  }

  /**
   * Returns the standard deviation of all samples per dimension with normalisation by {@link #count()} - 1.
   * 
   * @return The standard deviation
   */
  public Col stddev() {
    return stddev(0);
  }

  /**
   * Returns the standard deviation of all samples per dimension.
   * <p>
   * Performs either normalisation by {@link #count()} - 1 ({@code normType} = 0) or {@code #count()} ({@code normType}
   * = 1).
   * 
   * @param normType The normalisation
   * @return The standard deviation
   * 
   * @throws IllegalArgumentException The normalisation type must be one of 0 or 1, but was: {@code normType}.
   */
  public Col stddev(int normType) throws IllegalArgumentException {
    return Arma.sqrt(var(normType));
  }

  /**
   * Returns the covariance of all observed samples per dimension with normalisation by {@link #count()} - 1.
   * *
   * 
   * @return The covariance
   * 
   * @throws IllegalAccessException The constructor must be invoked with calculateCovariance be set to true to activate
   *           the calculation of the covariance.
   */
  public Mat cov() throws IllegalAccessException {
    return cov(0);
  }

  /**
   * Returns the covariance of all observed samples per dimension.
   * <p>
   * Performs either normalisation by {@link #count()} - 1 ({@code normType} = 0) or {@code #count()} ({@code normType}
   * = 1).
   * 
   * @param normType The normalisation
   * @return The covariance
   * 
   * @throws IllegalAccessException The constructor must be invoked with calculateCovariance be set to true to activate
   *           the calculation of the covariance.
   * @throws IllegalArgumentException The normalisation type must be one of 0 or 1, but was: {@code normType}.
   */
  public Mat cov(int normType) throws IllegalAccessException, IllegalArgumentException {
    if (!_calculateCovariance) {
      throw new IllegalAccessException("The constructor must be invoked with calculateCovariance be set to true to activate the calculation of the covariance.");
    }

    if (normType == 0) {
      return _cov;
    } else if (normType == 1) {
      if (_count > 0) {
        return _cov.times((_count - 1) / _count);
      } else {
        return _cov;
      }
    } else {
      throw new IllegalArgumentException("The normalisation type must be one of 0 or 1, but was:" + normType + ".");
    }
  }

  /**
   * Resets all statistical values.
   */
  public void reset() {
    _max.reset();
    _min.reset();
    _mean.reset();
    _var.reset();
    _cov.reset();

    _count = 0;
  }
}
