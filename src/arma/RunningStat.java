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
public class RunningStat {

  /**
   * 
   */
  private double _min;
  /**
   * 
   */
  private double _max;
  /**
   * 
   */
  private double _count;
  /**
   * 
   */
  private double _mean;
  /**
   * 
   */
  private double _var;

  /**
   * 
   */
  public RunningStat() {
    reset();
  }

  /**
   * @param sample
   */
  public void update(double sample) {
    if (_count > 0) {
      _max = Math.min(_min, sample);
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
   * @return
   */
  public double count() {
    return _count;
  }

  /**
   * @return
   */
  public double min() {
    return _min;
  }

  /**
   * @return
   */
  public double max() {
    return _max;
  }

  /**
   * @return
   */
  public double mean() {
    return _mean;
  }

  /**
   * @return
   */
  public double var() {
    return var(0);
  }

  /**
   * @param normType
   * @return
   */
  public double var(int normType) {
    if (normType == 0) {
      return _var;
    } else if (normType == 1) {
      return (_count - 1) / _count * _var;
    } else {
      throw new IllegalArgumentException();
    }
  }

  /**
   * @return
   */
  public double stddev() {
    return Math.sqrt(var(0));
  }

  /**
   * @param normType
   * @return
   */
  public double stddev(int normType) {
    return Math.sqrt(var(normType));
  }

  /**
   * @return
   */
  public void reset() {
    _count = 0;
  }
}
