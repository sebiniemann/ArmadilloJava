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
 * are not relevant. Besides the covariance, each measurement is performed per dimension.
 * 
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 */
public class RunningStatVec {

  /**
   * Whether to calculate the covariance.
   */
  private boolean _calculateCovariance;
  /**
   * The smallest observed value.
   */
  private Mat     _min;
  /**
   * The largest observed value.
   */
  private Mat     _max;
  /**
   * The amount of observed values.
   */
  private int     _count;
  /**
   * The mean of all observed values.
   */
  private Mat     _mean;
  /**
   * The variance of all observed values.
   */
  private Mat     _var;
  /**
   * The covariance of all observed values.
   */
  private Mat     _cov;

  /**
   * Initialises the statistical measures.
   * <p>
   * Set {@code calculateCovariance} to true if the covariance should be calculated.
   * 
   * @param calculateCovariance Whether to calculate the covariance.
   */
  public RunningStatVec(boolean calculateCovariance) {
    _calculateCovariance = calculateCovariance;

    _mean = new Mat();
    _var = new Mat();
    _max = new Mat();
    _min = new Mat();

    reset();
  }

  /**
   * Recalculates the statistical measures with the provided values.
   * <p>
   * <b>Non-canonical:</b> An {@code IllegalArgumentException} exception is thrown if the size of the provided samples
   * did not match previous observations. Use {@link #reset()} to reuse this instance for new sample sizes.
   * 
   * @param samples The provided values. 
   * 
   * @throws IllegalArgumentException <b>Non-canonical:</b> Thrown if the size of the provided samples did not match
   *           previous observations. Use {code reset()} to reuse this instance for new sample sizes.
   */
  public void update(Mat samples) throws IllegalArgumentException {
    if (_count > 0) {
      if (_max.n_elem != samples.n_elem) {
        throw new IllegalArgumentException("The provided samples did not match previous observations. samples.n_elem was previously: " + _max.n_elem + ", but is now: " + samples.n_elem);
      }

      if (_calculateCovariance) {
        Mat temp = samples.minus(_mean);

        if (samples.is_colvec()) {
          temp = temp.times(temp.t());
        } else {
          temp = temp.t().times(temp);
        }

        _cov.subvec(Op.ELEMTIMES, (_count - 1) / _count);
        _cov.subvec(Op.PLUS, temp.elemDivide(_count + 1));
      }

      for (int n = 0; n < samples.n_elem; n++) {
        _max.at(n, Op.EQUAL, Math.max(_max.at(n), samples.at(n)));
        _min.at(n, Op.EQUAL, Math.min(_max.at(n), samples.at(n)));
      }

      _var = (Arma.pow(samples.minus(_mean), 2)).elemDivide(_count + 1).plus(_var.times((_count - 1) / _count));
      _mean = _mean.plus(samples.minus(_mean).elemDivide(_count + 1));

    } else {
      int numberOfRows = samples.n_rows;
      int numberOfColumns = samples.n_cols;

      _cov = new Mat(numberOfRows, numberOfColumns, Fill.ZEROS);
      _max = new Mat(samples);
      _min = new Mat(samples);
      _var = new Mat(numberOfRows, numberOfColumns, Fill.ZEROS);
      _mean = new Mat(samples);
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
   * Returns the smallest observed value per dimension.
   * 
   * @return The minimum.
   */
  public Mat min() {
    return _min;
  }

  /**
   * Returns the largest observed value per dimension.
   * 
   * @return The maximum.
   */
  public Mat max() {
    return _max;
  }

  /**
   * Returns the mean of all observed samples per dimension.
   * 
   * @return The mean.
   */
  public Mat mean() {
    return _mean;
  }

  /**
   * Returns the variance of all observed samples per dimension with normalisation by {@link #count()} - 1.
   * <p>
   * <b>Non-canonical:</b> An {@code IllegalArgumentException} exception is thrown if the normalisation type is not one
   * of 0 or 1.
   * 
   * @return The variance.
   */
  public Mat var() {
    return var(0);
  }

  /**
   * Returns the variance of all observed samples per dimension.
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
  public Mat var(int normType) {
    if (normType == 0) {
      return _var;
    } else if (normType == 1) {
      if (_count > 0) {
        return _var.times((_count - 1) / _count);
      } else {
        return _var;
      }
    } else {
      throw new IllegalArgumentException("The normalisation type must be one of 0 or 1, but was :" + normType);
    }
  }

  /**
   * Returns the standard deviation of all observed samples per dimension with normalisation by {@link #count()} - 1.
   * <p>
   * <b>Non-canonical:</b> An {@code IllegalArgumentException} exception is thrown if the normalisation type is not one
   * of 0 or 1.
   * 
   * @return The standard deviation.
   */
  public Mat stddev() {
    return Arma.sqrt(var(0));
  }

  /**
   * Returns the standard deviation of all observed samples per dimension.
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
  public Mat stddev(int normType) {
    return Arma.sqrt(var(normType));
  }

  /**
   * Returns the covariance of all observed samples per dimension with normalisation by {@link #count()} - 1.
   * <p>
   * <b>Non-canonical:</b> An {@code IllegalAccessException} exception is thrown if the calculation of the covariance
   * was not initial requested.
   * 
   * @return The covariance.
   * 
   * @throws IllegalAccessException Thrown if the calculation of the covariance was not initial requested.
   */
  public Mat cov() throws IllegalAccessException {
    return cov(0);
  }

  /**
   * Returns the covariance of all observed samples per dimension.
   * <p>
   * Performs either normalisation by {@link #count()} - 1 if {@code normType = 0} or by {@link #count()}.
   * <p>
   * <b>Non-canonical:</b>
   * <ul>
   * <li>An {@code IllegalAccessException} exception is thrown if the calculation of the covariance was not initial
   * requested.
   * <li>An {@code IllegalArgumentException} exception is thrown if the normalisation type is not one of 0 or 1.
   * </ul>
   * 
   * @param normType The normalisation to be used.
   * @return The covariance.
   * 
   * @throws IllegalAccessException Thrown if the calculation of the covariance was not initial requested.
   * @throws IllegalArgumentException Thrown if the normalisation type is not one of 0 or 1.
   */
  public Mat cov(int normType) throws IllegalAccessException, IllegalArgumentException {
    if (!_calculateCovariance) {
      throw new IllegalAccessException("The calculation of the covariance was not initial requested.");
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
      throw new IllegalArgumentException("The normalisation type must be one of 0 or 1, but was :" + normType);
    }
  }

  /**
   * Resets the observation.
   */
  public void reset() {
    _max = new Mat();
    _min = new Mat();
    _mean = new Mat();
    _var = new Mat();
    _cov = new Mat();

    _count = 0;
  }
}
