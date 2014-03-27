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

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.number.IsCloseTo.*;

import java.io.IOException;

import org.junit.Test;

/**
 * @author Sebastian Niemann <niemann@sra.uni-hannovr.de>
 */
public class TestDatum extends TestClass {

  /**
   * Test method for {@link arma.is(closeTo(Datum}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void test() throws IOException {
    Mat expected = load("datum");

    double delta = 1e-12;

    assertThat(Datum.pi, is(closeTo(expected.at(0), Math.abs(expected.at(0)) * delta)));
    assertThat(Datum.e, is(closeTo(expected.at(1), Math.abs(expected.at(1)) * delta)));
    assertThat(Datum.euler, is(closeTo(expected.at(2), Math.abs(expected.at(2)) * delta)));
    assertThat(Datum.gratio, is(closeTo(expected.at(3), Math.abs(expected.at(3)) * delta)));
    assertThat(Datum.sqrt2, is(closeTo(expected.at(4), Math.abs(expected.at(4)) * delta)));
    assertThat(Datum.eps, is(closeTo(expected.at(5), Math.abs(expected.at(5)) * delta)));
    assertThat(Datum.log_min, is(closeTo(expected.at(6), Math.abs(expected.at(6)) * delta)));
    assertThat(Datum.log_max, is(closeTo(expected.at(7), Math.abs(expected.at(7)) * delta)));
    assertThat(Datum.nan, is(expected.at(8)));
    assertThat(Datum.inf, is(expected.at(9)));
    assertThat(Datum.m_u, is(closeTo(expected.at(10), Math.abs(expected.at(10)) * delta)));
    assertThat(Datum.N_A, is(closeTo(expected.at(11), Math.abs(expected.at(11)) * delta)));
    assertThat(Datum.k, is(closeTo(expected.at(12), Math.abs(expected.at(12)) * delta)));
    assertThat(Datum.k_evk, is(closeTo(expected.at(13), Math.abs(expected.at(13)) * delta)));
    assertThat(Datum.a_0, is(closeTo(expected.at(14), Math.abs(expected.at(14)) * delta)));
    assertThat(Datum.mu_B, is(closeTo(expected.at(15), Math.abs(expected.at(15)) * delta)));
    assertThat(Datum.Z_0, is(closeTo(expected.at(16), Math.abs(expected.at(16)) * delta)));
    assertThat(Datum.G_0, is(closeTo(expected.at(17), Math.abs(expected.at(17)) * delta)));
    assertThat(Datum.k_e, is(closeTo(expected.at(18), Math.abs(expected.at(18)) * delta)));
    assertThat(Datum.eps_0, is(closeTo(expected.at(19), Math.abs(expected.at(19)) * delta)));
    assertThat(Datum.m_e, is(closeTo(expected.at(20), Math.abs(expected.at(20)) * delta)));
    assertThat(Datum.eV, is(closeTo(expected.at(21), Math.abs(expected.at(21)) * delta)));
    assertThat(Datum.ec, is(closeTo(expected.at(22), Math.abs(expected.at(22)) * delta)));
    assertThat(Datum.F, is(closeTo(expected.at(23), Math.abs(expected.at(23)) * delta)));
    assertThat(Datum.alpha, is(closeTo(expected.at(24), Math.abs(expected.at(24)) * delta)));
    assertThat(Datum.alpha_inv, is(closeTo(expected.at(25), Math.abs(expected.at(25)) * delta)));
    assertThat(Datum.K_J, is(closeTo(expected.at(26), Math.abs(expected.at(26)) * delta)));
    assertThat(Datum.mu_0, is(closeTo(expected.at(27), Math.abs(expected.at(27)) * delta)));
    assertThat(Datum.phi_0, is(closeTo(expected.at(28), Math.abs(expected.at(28)) * delta)));
    assertThat(Datum.R, is(closeTo(expected.at(29), Math.abs(expected.at(29)) * delta)));
    assertThat(Datum.G, is(closeTo(expected.at(30), Math.abs(expected.at(30)) * delta)));
    assertThat(Datum.h, is(closeTo(expected.at(31), Math.abs(expected.at(31)) * delta)));
    assertThat(Datum.h_bar, is(closeTo(expected.at(32), Math.abs(expected.at(32)) * delta)));
    assertThat(Datum.m_p, is(closeTo(expected.at(33), Math.abs(expected.at(33)) * delta)));
    assertThat(Datum.R_inf, is(closeTo(expected.at(34), Math.abs(expected.at(34)) * delta)));
    assertThat(Datum.c_0, is(closeTo(expected.at(35), Math.abs(expected.at(35)) * delta)));
    assertThat(Datum.sigma, is(closeTo(expected.at(36), Math.abs(expected.at(36)) * delta)));
    assertThat(Datum.R_k, is(closeTo(expected.at(37), Math.abs(expected.at(37)) * delta)));
    assertThat(Datum.b, is(closeTo(expected.at(38), Math.abs(expected.at(38)) * delta)));
  }

}
