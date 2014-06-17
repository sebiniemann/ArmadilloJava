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
 * Provides pre-defined constants.
 * 
 * @author Sebastian Niemann
 */
public class Datum {

  /**
   * The ratio of a circle's circumference to its diameter (PI)
   */
  public static final double pi        = Math.PI;

  /**
   * Infinity
   */
  public static final double inf       = Double.POSITIVE_INFINITY;

  /**
   * Not a number
   */
  public static final double nan       = Double.NaN;

  /**
   * The Euler number
   */
  public static final double e         = Math.E;

  /**
   * The square root of 2
   */
  public static final double sqrt2     = Math.sqrt(2);

  /**
   * The difference between the number 1.0 and the next larger representable number (also the machine epsilon)
   * <p>
   * <b>Note:</b> Machine dependent
   */
  public static final double eps       = Math.ulp(1.0d);

  /**
   * The logarithm of the smallest representable normal number
   * <p>
   * <b>Note:</b> Machine dependent
   */
  public static final double log_min   = Math.log(Double.MIN_NORMAL);

  /**
   * The logarithm of the smallest representable number
   * <p>
   * <b>Note:</b> Machine dependent
   */
  public static final double log_max   = Math.log(Double.MAX_VALUE);

  /**
   * The Euler-Mascheroni constant
   */
  public static final double euler     = 0.5772156649015329;

  /**
   * The golden ratio
   */
  public static final double gratio    = 1.6180339887498948;

  /**
   * The atomic mass constant (in kilogram)
   */
  public static final double m_u       = 1.660538782e-27;

  /**
   * The Avogadro constant
   */
  public static final double N_A       = 6.02214179e23;

  /**
   * The Boltzmann constant (in joules per kelvin)
   */
  public static final double k         = 1.3806504e-23;

  /**
   * The Boltzmann constant (in electron volt per kelvin)
   */
  public static final double k_evk     = 8.617343e-5;

  /**
   * The Bohr radius (in meters)
   */
  public static final double a_0       = 0.52917720859e-10;

  /**
   * The Bohr magneton
   */
  public static final double mu_B      = 927.400915e-26;

  /**
   * The characteristic impedance of vacuum (in ohms)
   */
  public static final double Z_0       = 3.76730313461771e-2;

  /**
   * The conductance quantum (in siemens)
   */
  public static final double G_0       = 7.7480917004e-5;

  /**
   * The Coulomb constant (in meters per farad)
   */
  public static final double k_e       = 8.9875517873681764e9;

  /**
   * The electric constant (in farads per meter)
   */
  public static final double eps_0     = 8.85418781762039e-12;

  /**
   * The electron mass (in kilogram)
   */
  public static final double m_e       = 9.10938215e-31;

  /**
   * The electron volt (in joules)
   */
  public static final double eV        = 1.602176487e-19;

  /**
   * The elementary charge (in coulombs)
   */
  public static final double ec        = 1.602176487e-19;

  /**
   * The elementary charge (in coulombs)
   */
  public static final double F         = 96485.3399;

  /**
   * The Faraday constant (in coulombs)
   */
  public static final double alpha     = 7.2973525376e-3;

  /**
   * The fine-structure constant
   */
  public static final double alpha_inv = 137.035999679;

  /**
   * The inverse fine-structure constant
   */
  public static final double K_J       = 483597.891e9;

  /**
   * The Josephson constant
   */
  public static final double mu_0      = 1.25663706143592e-06;

  /**
   * The magnetic constant (in henries per meter)
   */
  public static final double phi_0     = 2.067833667e-15;

  /**
   * The magnetic flux quantum (in webers)
   */
  public static final double R         = 8.314472;

  /**
   * The molar gas constant (in joules per mole kelvin)
   */
  public static final double G         = 6.67428e-11;

  /**
   * The Newtonian constant of gravitation (in newton square meters per kilogram squared)
   */
  public static final double h         = 6.62606896e-34;

  /**
   * The Planck constant (in joule seconds)
   */
  public static final double h_bar     = 1.054571628e-34;

  /**
   * The reduced Planck constant (in joule seconds)
   */
  public static final double m_p       = 1.672621637e-27;

  /**
   * The proton mass (in kilogram)
   */
  public static final double R_inf     = 10973731.568527;

  /**
   * The Rydberg constant (in reciprocal meters)
   */
  public static final double c_0       = 299792458.0;

  /**
   * The speed of light in vacuum (in meters per second)
   */
  public static final double sigma     = 5.670400e-8;

  /**
   * The von Klitzing constant (in ohms)
   */
  public static final double R_k       = 25812.807557;

  /**
   * The Wien wavelength displacement law constant
   */
  public static final double b         = 2.8977685e-3;
}
