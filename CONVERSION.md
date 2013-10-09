The functions below are all directly called in Matlab, in the namespace "arma" in C++ and in the class "Arma" in Java. 

det, abs, qr, floor, accu, square, sqrt, find, any, norm, cos, acos, cosh, acosh, sin, asin, sinh, asinh, tan, atan, tanh, atanh, sum, mean, join_rows, join_cols, join_horiz, join_vert, as_scalar, solve, svd

See the calculation of the determinant as an example:

<table>
  <tr>
    <th>Matlab</th><th>Armadillo C++</th><th>ArmadilloJava</th>
  </tr>
  <tr>
    <td>det(A)</td><td>arma::det(A)</td><td>Arma.det(A)</td>
  </tr>
</table>
