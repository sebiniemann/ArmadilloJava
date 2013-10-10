Syntax conversion table between Matlab, Armadillo C++ and Armadillo Java.

Note: Variable A is a matrix.

Initialisation

<table>
  <tr>
    <th>Matlab</th><th>Armadillo C++</th><th>ArmadilloJava</th><th>Notes</th>
  </tr>
  <tr>
    <td></td><td>A()</td><td>A()</td><td></td>
  </tr>
  <tr>
    <td></td><td>A(n, m)</td><td>A(n, m)</td><td></td>
  </tr>
  <tr>
    <td></td><td>A(n, m, fill::none)</td><td>A(n, m, Fill.NONE)</td><td></td>
  </tr>
  <tr>
    <td>zeros(n, m)</td><td>A(n, m, fill:zeros)</td><td>A(n, m, Fill.ZEROS)</td><td></td>
  </tr>
  <tr>
    <td>zeros(n, m)</td><td>zeros(n, m)</td><td>Mat.zeros(n, m)</td><td></td>
  </tr>
  <tr>
    <td>ones(n, m)</td><td>A(n, m, fill:ones)</td><td>A(n, m, Fill.ONES)</td><td></td>
  </tr>
  <tr>
    <td>ones(n, m)</td><td>ones(n, m)</td><td>Mat.ones(n, m)</td><td></td>
  </tr>
  <tr>
    <td>eye(n, m)</td><td>A(n, m, fill:eye)</td><td>A(n, m, Fill.EYE)</td><td></td>
  </tr>
  <tr>
    <td>eye(n, m)</td><td>eye(n, m)</td><td>Mat.eye(n, m)</td><td></td>
  </tr>
</table>

Element access

<table>
  <tr>
    <th>Matlab</th><th>Armadillo C++</th><th>ArmadilloJava</th><th>Notes</th>
  </tr>
  <tr>
    <td>sin(A)</td><td>arma::sin(A)</td><td>Arma.sin(A)</td><td></td>
  </tr>
</table>

Submatrix views

<table>
  <tr>
    <th>Matlab</th><th>Armadillo C++</th><th>ArmadilloJava</th><th>Notes</th>
  </tr>
  <tr>
    <td>sin(A)</td><td>arma::sin(A)</td><td>Arma.sin(A)</td><td></td>
  </tr>
</table>

Member attributes

<table>
  <tr>
    <th>Matlab</th><th>Armadillo C++</th><th>ArmadilloJava</th><th>Notes</th>
  </tr>
  <tr>
    <td>sin(A)</td><td>arma::sin(A)</td><td>Arma.sin(A)</td><td></td>
  </tr>
</table>

Member functions

<table>
  <tr>
    <th>Matlab</th><th>Armadillo C++</th><th>ArmadilloJava</th><th>Notes</th>
  </tr>
  <tr>
    <td>sin(A)</td><td>arma::sin(A)</td><td>Arma.sin(A)</td><td></td>
  </tr>
</table>

Non-member functions

<table>
  <tr>
    <th>Matlab</th><th>Armadillo C++</th><th>ArmadilloJava</th><th>Notes</th>
  </tr>
  <tr>
    <td>sin(A)</td><td>arma::sin(A)</td><td>Arma.sin(A)</td><td></td>
  </tr>
</table>

Maxtrix-valued element-wise functions

<table>
  <tr>
    <th>Matlab</th><th>Armadillo C++</th><th>ArmadilloJava</th><th>Notes</th>
  </tr>
  <tr>
    <td>sin(A)</td><td>arma::sin(A)</td><td>Arma.sin(A)</td><td></td>
  </tr>
</table>

Trigonometric functions

<table>
  <tr>
    <th>Matlab</th><th>Armadillo C++</th><th>ArmadilloJava</th><th>Notes</th>
  </tr>
  <tr>
    <td>sin(A)</td><td>arma::sin(A)</td><td>Arma.sin(A)</td><td></td>
  </tr>
  <tr>
    <td>asin(A)</td><td>arma::asin(A)</td><td>Arma.asin(A)</td><td></td>
  </tr>
  <tr>
    <td>asinh(A)</td><td>arma::asinh(A)</td><td>Arma.asinh(A)</td><td></td>
  </tr>
  <tr>
    <td>cos(A)</td><td>arma::cos(A)</td><td>Arma.cos(A)</td><td></td>
  </tr>
  <tr>
    <td>acos(A)</td><td>arma::acos(A)</td><td>Arma.acos(A)</td><td></td>
  </tr>
  <tr>
    <td>acosh(A)</td><td>arma::acosh(A)</td><td>Arma.acosh(A)</td><td></td>
  </tr>
  <tr>
    <td>tan(A)</td><td>arma::tan(A)</td><td>Arma.tan(A)</td><td></td>
  </tr>
  <tr>
    <td>atan(A)</td><td>arma::atan(A)</td><td>Arma.atan(A)</td><td></td>
  </tr>
  <tr>
    <td>atanh(A)</td><td>arma::atanh(A)</td><td>Arma.atanh(A)</td><td></td>
  </tr>
</table>

Scalar-valued functions

<table>
  <tr>
    <th>Matlab</th><th>Armadillo C++</th><th>ArmadilloJava</th><th>Notes</th>
  </tr>
  <tr>
    <td>sin(A)</td><td>arma::sin(A)</td><td>Arma.sin(A)</td><td></td>
  </tr>
</table>

Matrix-valued functions

<table>
  <tr>
    <th>Matlab</th><th>Armadillo C++</th><th>ArmadilloJava</th><th>Notes</th>
  </tr>
  <tr>
    <td>sin(A)</td><td>arma::sin(A)</td><td>Arma.sin(A)</td><td></td>
  </tr>
</table>

Decompositions

<table>
  <tr>
    <th>Matlab</th><th>Armadillo C++</th><th>ArmadilloJava</th><th>Notes</th>
  </tr>
  <tr>
    <td>sin(A)</td><td>arma::sin(A)</td><td>Arma.sin(A)</td><td></td>
  </tr>
</table>

Others

<table>
  <tr>
    <th>Matlab</th><th>Armadillo C++</th><th>ArmadilloJava</th><th>Notes</th>
  </tr>
  <tr>
    <td>sin(A)</td><td>arma::sin(A)</td><td>Arma.sin(A)</td><td></td>
  </tr>
</table>
