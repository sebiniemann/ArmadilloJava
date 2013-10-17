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
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 */
public class RunningStatVec {

  /**
   * 
   */
  private boolean _calculateCovariance;
  /**
   * 
   */
  private Mat     _mean;
  /**
   * 
   */
  private Mat     _var;
  /**
   * 
   */
  private Mat     _min;
  /**
   * 
   */
  private Mat     _max;
  /**
   * 
   */
  private Mat     _cov;
  /**
   * 
   */
  private double  _count;

  /**
   * @param calculateCovariance
   * 
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
   * @param samples
   */
  public void update(Mat samples) {
    if (_count > 0) {
      if (_calculateCovariance) {
        Mat temp = samples.minus(_mean);

        if (samples.is_colvec()) {
          temp = temp.times(temp.t());
        } else {
          temp = temp.t().times(temp);
        }

        _cov.subvec(Op.ELEMTIMES, (_count - 1) / _count);
        _cov.subvec(Op.PLUS, temp.divide(_count + 1));
      }
      
      for(int n = 0; n < samples.n_elem; n++) {
        _max.at(n, Op.EQUAL, Math.max(_max.at(n), samples.at(n)));
        _min.at(n, Op.EQUAL, Math.min(_max.at(n), samples.at(n)));
      }
      
      _var = (Arma.pow(samples.minus(_mean), 2)).divide(_count + 1).plus(_var.times((_count - 1) / _count));
      _mean = _mean.plus(samples.minus(_mean).divide(_count + 1));

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
   * @return
   */
  public Mat mean() {
    return _mean;
  }

  /**
   * @return
   */
  public Mat var() {
    return var(0);
  }

  /**
   * @param normType
   * @return
   */
  public Mat var(int normType) {
    if (normType == 0) {
      return _var;
    } else if (normType == 1) {
      return _var.times((_count - 1) / _count);
    } else {
      throw new IllegalArgumentException();
    }
  }

  /**
   * @return
   */
  public Mat stddev() {
    return Arma.sqrt(var(0));
  }

  /**
   * @param normType
   * @return
   */
  public Mat stddev(int normType) {
    return Arma.sqrt(var(normType));
  }

  /**
   * @return
   */
  public Mat min() {
    return _min;
  }

  /**
   * @return
   */
  public Mat max() {
    return _max;
  }

  /**
   * @return
   */
  public Mat cov() {
    return cov(0);
  }

  /**
   * @param normType 
   * @return
   */
  public Mat cov(int normType) {
    return _cov;
  }

  /**
   * @return
   */
  public void reset() {
    _count = 0;
  }

  /**
   * @return
   */
  public double count() {
    return _count;
  }
}
