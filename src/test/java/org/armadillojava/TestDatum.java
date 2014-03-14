/*******************************************************************************
 * Copyright 2014 Sebastian Niemann <niemann@sra.uni-hannover.de>.
 * 
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://opensource.org/licenses/MIT
 * 
 * Developers:
 *   Sebastian Niemann <niemann@sra.uni-hannover.de> - Lead developer
 *   Daniel Kiechle - Unit testing
 ******************************************************************************/
package org.armadillojava;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.number.IsCloseTo.*;

import java.io.IOException;

import org.junit.Test;

/**
 * @author Sebastian Niemann <niemann@sra.uni-hannovr.de>
 */
public class TestDatum {

  /**
   * Test method for {@link arma.is(closeTo(Datum}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void test() throws IOException {
    Mat expected = new Mat();
    expected.load("./src/test/data/expected/TestDatum/datum.mat");

    assertThat(expected.at(0), is(closeTo(Datum.pi, Math.abs(expected.at(0)) * 1e-12)));
    assertThat(expected.at(1), is(closeTo(Datum.e, Math.abs(expected.at(1)) * 1e-12)));
    assertThat(expected.at(2), is(closeTo(Datum.euler, Math.abs(expected.at(2)) * 1e-12)));
    assertThat(expected.at(3), is(closeTo(Datum.gratio, Math.abs(expected.at(3)) * 1e-12)));
    assertThat(expected.at(4), is(closeTo(Datum.sqrt2, Math.abs(expected.at(4)) * 1e-12)));
    assertThat(expected.at(5), is(closeTo(Datum.eps, Math.abs(expected.at(5)) * 1e-12)));
    assertThat(expected.at(6), is(closeTo(Datum.log_min, Math.abs(expected.at(6)) * 1e-12)));
    assertThat(expected.at(7), is(closeTo(Datum.log_max, Math.abs(expected.at(7)) * 1e-12)));
    assertThat(expected.at(8), is(Datum.nan));
    assertThat(expected.at(9), is(Datum.inf));
    assertThat(expected.at(10), is(closeTo(Datum.m_u, Math.abs(expected.at(10)) * 1e-12)));
    assertThat(expected.at(11), is(closeTo(Datum.N_A, Math.abs(expected.at(11)) * 1e-12)));
    assertThat(expected.at(12), is(closeTo(Datum.k, Math.abs(expected.at(12)) * 1e-12)));
    assertThat(expected.at(13), is(closeTo(Datum.k_evk, Math.abs(expected.at(13)) * 1e-12)));
    assertThat(expected.at(14), is(closeTo(Datum.a_0, Math.abs(expected.at(14)) * 1e-12)));
    assertThat(expected.at(15), is(closeTo(Datum.mu_B, Math.abs(expected.at(15)) * 1e-12)));
    assertThat(expected.at(16), is(closeTo(Datum.Z_0, Math.abs(expected.at(16)) * 1e-12)));
    assertThat(expected.at(17), is(closeTo(Datum.G_0, Math.abs(expected.at(17)) * 1e-12)));
    assertThat(expected.at(18), is(closeTo(Datum.k_e, Math.abs(expected.at(18)) * 1e-12)));
    assertThat(expected.at(19), is(closeTo(Datum.eps_0, Math.abs(expected.at(19)) * 1e-12)));
    assertThat(expected.at(20), is(closeTo(Datum.m_e, Math.abs(expected.at(20)) * 1e-12)));
    assertThat(expected.at(21), is(closeTo(Datum.eV, Math.abs(expected.at(21)) * 1e-12)));
    assertThat(expected.at(22), is(closeTo(Datum.ec, Math.abs(expected.at(22)) * 1e-12)));
    assertThat(expected.at(23), is(closeTo(Datum.F, Math.abs(expected.at(23)) * 1e-12)));
    assertThat(expected.at(24), is(closeTo(Datum.alpha, Math.abs(expected.at(24)) * 1e-12)));
    assertThat(expected.at(25), is(closeTo(Datum.alpha_inv, Math.abs(expected.at(25)) * 1e-12)));
    assertThat(expected.at(26), is(closeTo(Datum.K_J, Math.abs(expected.at(26)) * 1e-12)));
    assertThat(expected.at(27), is(closeTo(Datum.mu_0, Math.abs(expected.at(27)) * 1e-12)));
    assertThat(expected.at(28), is(closeTo(Datum.phi_0, Math.abs(expected.at(28)) * 1e-12)));
    assertThat(expected.at(29), is(closeTo(Datum.R, Math.abs(expected.at(29)) * 1e-12)));
    assertThat(expected.at(30), is(closeTo(Datum.G, Math.abs(expected.at(30)) * 1e-12)));
    assertThat(expected.at(31), is(closeTo(Datum.h, Math.abs(expected.at(31)) * 1e-12)));
    assertThat(expected.at(32), is(closeTo(Datum.h_bar, Math.abs(expected.at(32)) * 1e-12)));
    assertThat(expected.at(33), is(closeTo(Datum.m_p, Math.abs(expected.at(33)) * 1e-12)));
    assertThat(expected.at(34), is(closeTo(Datum.R_inf, Math.abs(expected.at(34)) * 1e-12)));
    assertThat(expected.at(35), is(closeTo(Datum.c_0, Math.abs(expected.at(35)) * 1e-12)));
    assertThat(expected.at(36), is(closeTo(Datum.sigma, Math.abs(expected.at(36)) * 1e-12)));
    assertThat(expected.at(37), is(closeTo(Datum.R_k, Math.abs(expected.at(37)) * 1e-12)));
    assertThat(expected.at(38), is(closeTo(Datum.b, Math.abs(expected.at(38)) * 1e-12)));
  }

}
