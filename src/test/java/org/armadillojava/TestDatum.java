package arma;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

/**
 * @author Sebastian Niemann <niemann@sra.uni-hannovr.de>
 */
public class TestDatum {

  /**
   * Test method for {@link arma.Datum}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void test() throws IOException {
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestDatum/datum.mat");

    assertEquals(expected.at(0), Datum.pi, Math.abs(expected.at(0)) * 1e-12);
    assertEquals(expected.at(1), Datum.e, Math.abs(expected.at(1)) * 1e-12);
    assertEquals(expected.at(2), Datum.euler, Math.abs(expected.at(2)) * 1e-12);
    assertEquals(expected.at(3), Datum.gratio, Math.abs(expected.at(3)) * 1e-12);
    assertEquals(expected.at(4), Datum.sqrt2, Math.abs(expected.at(4)) * 1e-12);
    assertEquals(expected.at(5), Datum.eps, Math.abs(expected.at(5)) * 1e-12);
    assertEquals(expected.at(6), Datum.log_min, Math.abs(expected.at(6)) * 1e-12);
    assertEquals(expected.at(7), Datum.log_max, Math.abs(expected.at(7)) * 1e-12);
    assertEquals(expected.at(8), Datum.nan, Math.abs(expected.at(8)) * 1e-12);
    assertEquals(expected.at(9), Datum.inf, Math.abs(expected.at(9)) * 1e-12);
    assertEquals(expected.at(10), Datum.m_u, Math.abs(expected.at(10)) * 1e-12);
    assertEquals(expected.at(11), Datum.N_A, Math.abs(expected.at(11)) * 1e-12);
    assertEquals(expected.at(12), Datum.k, Math.abs(expected.at(12)) * 1e-12);
    assertEquals(expected.at(13), Datum.k_evk, Math.abs(expected.at(13)) * 1e-12);
    assertEquals(expected.at(14), Datum.a_0, Math.abs(expected.at(14)) * 1e-12);
    assertEquals(expected.at(15), Datum.mu_B, Math.abs(expected.at(15)) * 1e-12);
    assertEquals(expected.at(16), Datum.Z_0, Math.abs(expected.at(16)) * 1e-12);
    assertEquals(expected.at(17), Datum.G_0, Math.abs(expected.at(17)) * 1e-12);
    assertEquals(expected.at(18), Datum.k_e, Math.abs(expected.at(18)) * 1e-12);
    assertEquals(expected.at(19), Datum.eps_0, Math.abs(expected.at(19)) * 1e-12);
    assertEquals(expected.at(20), Datum.m_e, Math.abs(expected.at(20)) * 1e-12);
    assertEquals(expected.at(21), Datum.eV, Math.abs(expected.at(21)) * 1e-12);
    assertEquals(expected.at(22), Datum.ec, Math.abs(expected.at(22)) * 1e-12);
    assertEquals(expected.at(23), Datum.F, Math.abs(expected.at(23)) * 1e-12);
    assertEquals(expected.at(24), Datum.alpha, Math.abs(expected.at(24)) * 1e-12);
    assertEquals(expected.at(25), Datum.alpha_inv, Math.abs(expected.at(25)) * 1e-12);
    assertEquals(expected.at(26), Datum.K_J, Math.abs(expected.at(26)) * 1e-12);
    assertEquals(expected.at(27), Datum.mu_0, Math.abs(expected.at(27)) * 1e-12);
    assertEquals(expected.at(28), Datum.phi_0, Math.abs(expected.at(28)) * 1e-12);
    assertEquals(expected.at(29), Datum.R, Math.abs(expected.at(29)) * 1e-12);
    assertEquals(expected.at(30), Datum.G, Math.abs(expected.at(30)) * 1e-12);
    assertEquals(expected.at(31), Datum.h, Math.abs(expected.at(31)) * 1e-12);
    assertEquals(expected.at(32), Datum.h_bar, Math.abs(expected.at(32)) * 1e-12);
    assertEquals(expected.at(33), Datum.m_p, Math.abs(expected.at(33)) * 1e-12);
    assertEquals(expected.at(34), Datum.R_inf, Math.abs(expected.at(34)) * 1e-12);
    assertEquals(expected.at(35), Datum.c_0, Math.abs(expected.at(35)) * 1e-12);
    assertEquals(expected.at(36), Datum.sigma, Math.abs(expected.at(36)) * 1e-12);
    assertEquals(expected.at(37), Datum.R_k, Math.abs(expected.at(37)) * 1e-12);
    assertEquals(expected.at(38), Datum.b, Math.abs(expected.at(38)) * 1e-12);
  }

}
