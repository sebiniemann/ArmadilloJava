ArmadilloJava 4.000.0
=====================
[![Build Status](https://travis-ci.org/SebastianNiemann/ArmadilloJava.png?branch=master)](https://travis-ci.org/SebastianNiemann/ArmadilloJava)
[![Coverage Status](https://coveralls.io/repos/SebastianNiemann/ArmadilloJava/badge.png?branch=master)](https://coveralls.io/r/SebastianNiemann/ArmadilloJava?branch=master)
[![Stories in Ready](https://badge.waffle.io/sebastianniemann/armadillojava.png?label=waffle:todo&title=ToDo)](https://waffle.io/sebastianniemann/onlineoptimisation)

Provides a pure Java based linear algebra library with interfaces similar to the Armadillo C++ Algebra Library (Armadillo) by Conrad Sanderson et al., NICTA, Australia.

How to Contribute
-----------------

If you would like to contribute, please read the [guidelines for contributions](CONTRIBUTING.md).
Following these guidelines will help make your contributions easier to bring into the next release.

If you have any further questions, please do not hesitate to [contact us](mailto:niemann@sra.uni-hannover.de).

API Reference for 4.000
-----------------------

### Matrix & Vector Classes

- `Mat`: Dense, real-valued, double-precision matrix
- `Col`: Dense, real-valued, double-precision column vector
- `Row`: Dense, real-valued, double-precision row vector

### Member Functions & Variables

- **Supported:** `attributes`, `copy_size`,  `diag`, `each_col`, `each_row`, `element access`, `element initialisation`, `eye`, `fill`, `i (inverse)`, `insert_rows`, `insert_cols`,  `in_range`, `is_empty`, `is_finite`, `is_square`, `is_vec`, `memptr`, `min`, `max`, `ones`, `operators`, `print`, `raw_print`, `randi`, `randu`, `randn`, `reset`, `reshape`, `resize`, `save/load (matrices & cubes)`, `save/load (fields)`, `set_imag/real`, `set_size`, `shed_rows/cols/slices`, `STL container functions`, `submatrix views`, `swap`, `swap_rows`, `swap_cols`, `t (transpose)`, `zeros`
- **Unsupported:** `colptr`, `eval`, `imbue`, `C++-like iterators`, `transform`

### Other Classes

- `RunningStat`
- `RunningStatVector`
- `WallClock`

### Generated Vectors & Matrices

- **Supported:** `eye`, `linspace`, `ones`, `randu`, `randn`, `repmat`, `toeplitz`, `circ_toeplitz`, `zeros`
- **Unsupported:** _nothing_

### Functions Individually Applied to Each Element of a Matrix

- **Supported:** `abs`, `eps`, `exp`, `exp2`, `exp10`, `trunc_exp`, `log`, `log2`, `log10`, `trunc_log`, `pow`, `sqrt`, `square`, `floor`, `ceil`, `round`, `sign`, `sin`, `asin`, `sinh`, `asinh`, `cos`, `acos`, `cosh`, `acosh`, `tan`, `atan`, `tanh`, `atanh`
- **Unsupported:** `conv_to`

### Scalar Valued Functions of Vectors/Matrices/Cubes

- **Supported:** `accu`, `as_scalar`, `cond`, `det`, `dot`, `norm_dot`, `log_det`, `norm`, `rank`, `trace`
- **Unsupported:** _nothing_

### Scalar/Vector Valued Functions of Vectors/Matrices

- **Supported:** `diagvec`, `min/max`, `prod`, `sum`, `mean`, `median`, `stddev`, `var`
- **Unsupported:** _nothing_

### Vector/Matrix Valued Functions of Vectors/Matrices

- **Supported:** `all`, `any`, `conv`, `cor`, `cov`, `cross`, `cumsum`, `diagmat`, `find`, `fliplr`, `flipud`, `hist`, `histc`, `inplace_trans`, `join_rows`, `join_cols`, `kron`, `reshape`, `resize`, `shuffle`, `sort`, `sort_index`, `symmatu`, `symmatl`, `trans`, `trimatu`, `trimatl`, `unique`, `vectorise`
- **Unsupported:** _nothing_

### Decompositions, Factorisations, Inverses and Equation Solvers

- **Supported:** `chol`, `eig_sym`, `inv`, `lu`, `pinv`, `princomp`, `qr`, `qr_econ`, `solve`, `svd`, `svd_econ`, `syl`
- **Unsupported:** _nothing_

### Miscellaneous

- **Supported:** `is_finite`, `pre-defined constants (pi, inf, speed of light, ...)`
- **Unsupported:** _nothing_

Changelog
---------

### Version policy

### 4.000

### 3.920 (Intitial release)

License
-------

Distributed under [MIT license](http://opensource.org/licenses/MIT).

Copyright (c) 2013-2014 [Sebastian Niemann](mailto:niemann@sra.uni-hannover.de) and contributors.
