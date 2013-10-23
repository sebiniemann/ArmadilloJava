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
  private boolean _calculateCovariance;
  /**
   * The smallest provided value
   */
  private Mat     _min;
  /**
   * The largest provided value
   */
  private Mat     _max;
  /**
   * The amount of values
   */
  private double  _count;
  /**
   * The mean of all values
   */
  private Mat     _mean;
  /**
   * The variance of all values
   */
  private Mat     _var;
  /**
   * The covariance of all values
   */
  private Mat     _cov;

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
   * Recalculates the statistical values with inclusion of the provided sample vector.
   * 
   * @param samples The provided values.
   * 
   * @throws IllegalArgumentException <b>Non-canonical:</b> The sample vector must be a vector, but was a (
   *           {@code samples.n_rows}, {@code samples.n_cols})-matrix.
   * @throws IllegalArgumentException <b>Non-canonical:</b> NaN is not valid sample value for any element.
   * @throws IllegalArgumentException <b>Non-canonical:</b> The provided sample vector must have the same number of
   *           elements as the previous one. {@code samples.n_elem} was previously: {@code max.n_elem}, but is now:
   *           {@code samples.n_elem}.
   */
  public void update(Mat samples) throws IllegalArgumentException {
    if (!samples.is_vec()) {
      throw new IllegalArgumentException("The sample vector must be a vector, but was a (" + samples.n_rows + ", " + samples.n_cols + ")-matrix.");
    }
    
    if(!samples.is_number()) {
      throw new IllegalArgumentException("NaN is not valid sample value for any element.");
    }

    if (_count > 0) {
      if (_max.n_elem != samples.n_elem) {
        throw new IllegalArgumentException("The provided sample vector must have the same number of elements as the previous one. samples.n_elem was previously: " + _max.n_elem + ", but is now: " + samples.n_elem + ".");
      }

      if (_calculateCovariance) {
        Mat temp = samples.minus(_mean);

        if (samples.is_colvec()) {
          temp = temp.times(temp.t());
        } else {
          temp = temp.t().times(temp);
        }

        _cov.submat(Op.ELEMTIMES, (_count - 1) / _count);
        _cov.submat(Op.PLUS, temp.elemDivide(_count + 1));
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
  public Mat min() {
    return _min;
  }

  /**
   * Returns the largest value per dimension.
   * 
   * @return The maximum
   */
  public Mat max() {
    return _max;
  }

  /**
   * Returns the mean of all samples per dimension.
   * 
   * @return The mean
   */
  public Mat mean() {
    return _mean;
  }

  /**
   * Returns the variance of all samples per dimension with normalisation by {@link #count()} - 1.
   * 
   * @return The variance
   */
  public Mat var() {
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
  public Mat var(int normType) throws IllegalArgumentException {
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
  public Mat stddev() {
    return Arma.sqrt(var(0));
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
  public Mat stddev(int normType) throws IllegalArgumentException {
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
    _max = new Mat();
    _min = new Mat();
    _mean = new Mat();
    _var = new Mat();
    _cov = new Mat();

    _count = 0;
  }
}
