Syntax conversion table
=======================
Between Matlab, Armadillo C++ and ArmadilloJava

**Note:** Only real-valued dense matrices with double precision are supported.


Matrix initialisation
---------------------

Matlab        | Armadillo C++        | ArmadilloJava            | Notes
--------------|----------------------|--------------------------|------
              | A()                  | A()                      | 
              | A(n, m)              | A(n, m)                  | 
              | A(n, m, fill::none)  | A(n, m, Fill.NONE)       | 
A = [...]     | A << ... << endr;    | A(new double[][]{ ... }) | 
              | A(" ... ");          |                          | *Not suppported*
zeros(n, m)   | A(n, m, fill::zeros) | A(n, m, Fill.ZEROS)      | 
zeros(n, m)   | zeros(n, m)          | A(n, m, Fill.ZEROS)      | 
ones(n, m)    | A(n, m, fill::ones)  | A(n, m, Fill.ONES)       | 
ones(n, m)    | ones(n, m)           | A(n, m, Fill.ONES)       | 
eye(n, m)     | A(n, m, fill::eye)   | A(n, m, Fill.EYE)        | 
eye(n, m)     | eye(n, m)            | A(n, m, Fill.EYE)        | 
randu(n, m)   | A(n, m, fill::randu) | A(n, m, Fill.RANDU, rng) | **Note:** rng is a random number generator
randu(n, m)   | randu(n, m)          | A(n, m, Fill.RANDU, rng) | 
randn(n, m)   | A(n, m, fill::randn) | A(n, m, Fill.RANDN, rng) | 
randn(n, m)   | randn(n, m)          | A(n, m, Fill.RANDN, rng) | 


Matrix attributes
-----------------

Matlab        | Armadillo C++        | ArmadilloJava            | Notes
--------------|----------------------|--------------------------|------
size(A, 1)    | A.n_rows             | A.n_rows                 | 
size(A, 2)    | A.n_cols             | A.n_cols                 | 
numel(A)      | A.n_elem             | A.n_elem                 | 


Internal data access
---------------------------
**Not recommend unless you know what you are doing.**
ArmadilloJava uses [Efficient Java Matrix Library](https://code.google.com/p/efficient-java-matrix-library/) internally.

Matlab        | Armadillo C++        | ArmadilloJava            | Notes
--------------|----------------------|--------------------------|------
              | A.memptr()           | A.memptr()               | 
              | A.colptr()           |                          | *Not suppported*


Element access 
--------------

Matlab                               | Armadillo C++        | ArmadilloJava            | Notes
-------------------------------------|----------------------|--------------------------|------
A(i+1, j+1)                          | A(i, j)              | A(i, j)                  | 
A(i+1, j+1) = A(i+1, j+1) Operator b | A(i, j) Operator= b  | A(i, j, Op.Operator, b)  | 
A(n)                                 | A(n)                 | A(n)                     | 
A(n) = A(n) Operator b               | A(n) Operator= b     | A(n, Op.Operator, b)     | 

Submatrix views
---------------

Matlab        | Armadillo C++        | ArmadilloJava            | Notes
--------------|----------------------|--------------------------|------

Member functions
----------------

Matlab        | Armadillo C++        | ArmadilloJava            | Notes
--------------|----------------------|--------------------------|------

Non-member functions
--------------------

Matlab        | Armadillo C++        | ArmadilloJava            | Notes
--------------|----------------------|--------------------------|------

### Maxtrix-valued element-wise functions

Matlab        | Armadillo C++        | ArmadilloJava            | Notes
--------------|----------------------|--------------------------|------
abs(A)        | abs(A)               | Arma.abs(A)              | 
eps(A)        | eps(A)               | Arma.eps(A)              | 
exp(A)        | exp(A)               | Arma.exp(A)              | 
2 .^ A        | exp2(A)              | Arma.exp2(A)             | 
10 .^ A       | exp10(A)             | Arma.exp10(A)            | 
              | trunc_exp(A)         | Arma.trunc_exp(A)        | **Matlab:** B = exp(A); B(isinf(B)) = realmax
log(A)        | log(A)               | Arma.log(A)              | 
log2(A)       | log2(A)              | Arma.log2(A)             | 
log10(A)      | log10(A)             | Arma.log10(A)            | 
              | trunc_log(A)         | Arma.trunc_log(A)        | **Matlab:** B = A; B(isinf(B)) = realmax; B(find(not(B))) = realmin; log(B)
A .^ p        | pow(A, p)            | Arma.pow(A, p)           | 
sqrt(A)       | sqrt(A)              | Arma.sqrt(A)             | 
A .^ 2        | square(A)            | Arma.square(A)           | 
floor(A)      | floor(A)             | Arma.floor(A)            | 
ceil(A)       | ceil(A)              | Arma.ceil(A)             | 
round(A)      | round(A)             | Arma.round(A)            | 
sign(A)       | sign(A)              | Arma.sign(A)             | 

#### Trigonometric functions

Matlab        | Armadillo C++        | ArmadilloJava            | Notes
--------------|----------------------|--------------------------|------
sin(A)        | sin(A)               | Arma.sin(A)              | 
asin(A)       | asin(A)              | Arma.asin(A)             | 
asinh(A)      | asinh(A)             | Arma.asinh(A)            | 
cos(A)        | cos(A)               | Arma.cos(A)              | 
acos(A)       | acos(A)              | Arma.acos(A)             | 
acosh(A)      | acosh(A)             | Arma.acosh(A)            | 
tan(A)        | tan(A)               | Arma.tan(A)              | 
atan(A)       | atan(A)              | Arma.atan(A)             | 
atanh(A)      | atanh(A)             | Arma.atanh(A)            | 


### Scalar-valued functions

Matlab        | Armadillo C++        | ArmadilloJava            | Notes
--------------|----------------------|--------------------------|------
sum(A(:))     | accu(A)              | Arma.accu(A)             | 
              | as_scalar(A)         | Arma.accu(A)             | 
det(A)        | det(A)               | Arma.det(A)              | 
dot(A, B)     | dot(A, B)            | Arma.dot(A, B)           | **Note:** A, B are assumed to be vectors.
              | norm_dot(A, B)       | Arma.norm_dot(A, B)      | **Matlab:** dot(A, B) / sqrt(dot(A, A) * dot(B, B))
              | log_det(v, s, A)     | Arma.log_det(v, s, A)    | **Matlab:** d = det(A); v = log(abs(d)); s = sign(d) (very inefficient)
norm(A, p)    | norm(A, p)           | Arma.norm(A, p)          | 
rank(A [, t]) | rank(A [, t])        | Arma.rank(A [, t])       | **Default** t = max(size(A))*eps(norm(A))
trace(A)      | trace(A)             | Arma.trace(A)            | 


### Matrix-valued functions

Matlab        | Armadillo C++        | ArmadilloJava            | Notes
--------------|----------------------|--------------------------|------


### Decompositions

Matlab        | Armadillo C++        | ArmadilloJava            | Notes
--------------|----------------------|--------------------------|------


Others
------

Matlab        | Armadillo C++        | ArmadilloJava            | Notes
--------------|----------------------|--------------------------|------
