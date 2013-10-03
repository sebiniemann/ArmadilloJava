import java.util.Random;

import arma.Mat;

public class Testutils {
  public static final double      DELTA             = 1e+15;
  public static final double      MAXIMAL_DIMENSION = 10;

  private static java.util.Random _RNG              = new Random();

  public static java.util.Random getRNG() {
    return _RNG;
  }
}
