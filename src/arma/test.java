package arma;

import java.io.IOException;

import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;

/**
 * @author test
 */
public class test {

  /**
   * @param args test
   * 
   * @throws IOException test
   */
  public static void main(String[] args) throws IOException {
//    System.out.println(Arma.inv(Arma.ones(2, 2)));
    System.out.println(Arma.rank(Arma.ones(2, 2)));
//    System.out.println(Arma.ones(2, 2).times(Arma.inv(Arma.ones(2, 2))));
    
    DenseMatrix64F matrix = new DenseMatrix64F(2, 2);
    CommonOps.fill(matrix, 1);
    System.out.println(matrix);
    DenseMatrix64F invert = new DenseMatrix64F(2, 2);
//    System.out.println(CommonOps.invert(matrix, invert));
//    System.out.println(invert);
    
    System.out.println(Arma.rank(Arma.zeros(1, 1)));
  }

}
