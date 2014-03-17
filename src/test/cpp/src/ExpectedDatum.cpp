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
 *   Sebastian Niemann - Lead developer
 *   Daniel Kiechle - Unit testing
 ******************************************************************************/
#include <ExpectedDatum.hpp>
using armadilloJava::ExpectedDatum;

#include <armadillo>
using arma::datum;

namespace armadilloJava {
  ExpectedDatum::ExpectedDatum() {
    Mat<double> expected = {
      datum::pi,
      datum::e,
      datum::euler,
      datum::gratio,
      datum::sqrt2,
      datum::eps,
      datum::log_min,
      datum::log_max,
      datum::nan,
      datum::inf,
      datum::m_u,
      datum::N_A,
      datum::k,
      datum::k_evk,
      datum::a_0,
      datum::mu_B,
      datum::Z_0,
      datum::G_0,
      datum::k_e,
      datum::eps_0,
      datum::m_e,
      datum::eV,
      datum::ec,
      datum::F,
      datum::alpha,
      datum::alpha_inv,
      datum::K_J,
      datum::mu_0,
      datum::phi_0,
      datum::R,
      datum::G,
      datum::h,
      datum::h_bar,
      datum::m_p,
      datum::R_inf,
      datum::c_0,
      datum::sigma,
      datum::R_k,
      datum::b
    };
    save("datum.mat", expected);
  }
}
