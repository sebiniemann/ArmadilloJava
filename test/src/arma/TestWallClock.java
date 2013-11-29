/**
 * 
 */
package arma;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

/**
 * @author Sebastian Niemann <niemann@sra.uni-hannovr.de>
 */
public class TestWallClock {

  /**
   * Test method for {@link arma.WallClock}.
   * 
   * @throws InterruptedException A thread is waiting, sleeping, or otherwise occupied, and the thread is interrupted,
   *           either before or during the activity.
   */
  @Test
  public void test() throws InterruptedException {
    for (int n = 100; n < 1000; n += 100) {
      WallClock.tic();
      TimeUnit.MILLISECONDS.sleep(n);
      assertEquals(n / 1000.0, WallClock.toc(), 0.02);
    }
  }
}