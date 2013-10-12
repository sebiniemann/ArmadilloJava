Syntax conversion table
=======================
Between Matlab, Armadillo C++ and ArmadilloJava

**Note:** Only real-valued dense matrices with double precision are currently supported.


Matrix initialisation / generation
----------------------------------

Matlab                 | Armadillo C++              | ArmadilloJava                   | Notes
-----------------------|----------------------------|---------------------------------|------
                       | A()                        | A()                             | 
                       | A(n, m)                    | A(n, m)                         | 
                       | A(n, m, fill::none)        | A(n, m, Fill.NONE)              | 
A = [...]              | A << ... << endr;          | A(new double[][]{ ... })        | 
                       | A(" ... ");                |                                 | *Not suppported*
zeros(n, m)            | A(n, m, fill::zeros)       | A(n, m, Fill.ZEROS)             | 
zeros(n, m)            | zeros(n, m)                | Mat.zeros(n, m)                 | 
ones(n, m)             | A(n, m, fill::ones)        | A(n, m, Fill.ONES)              | 
ones(n, m)             | ones(n, m)                 | Mat.ones(n, m)                  | 
eye(n, m)              | A(n, m, fill::eye)         | A(n, m, Fill.EYE)               | 
eye(n, m)              | eye(n, m)                  | Mat.eye(n, m)                   | 
randu(n, m)            | A(n, m, fill::randu)       | A(n, m, Fill.RANDU, rng)        | **Note:** rng is a random number generator.
randu(n, m)            | randu(n, m)                | Mat.randu(n, m, rng)            | 
randn(n, m)            | A(n, m, fill::randn)       | A(n, m, Fill.RANDN, rng)        | 
randn(n, m)            | randn(n, m)                | Mat.randn(n, m, rng)            | 
                       | repmat(A, n, m)            | Mat.repmat(A, n, m)             | 
toeplitz(A [, B])      | toeplitz(A [, B])          | Mat.toeplitz(A [, B])           | 
                       | circ_toeplitz(A)           | Mat.circ_toeplitz(A)            | **Matlab:** [Matlab Central File Excange - Circulant matrix](http://www.mathworks.com/matlabcentral/fileexchange/22858)
linspace(a, b [, n])   | linspace(a, b [, n])       | Mat.linspace(a, b [, n])        | **Note:** Generates a column vector. **Default:** n = 100 


Matrix attributes
-----------------

Matlab                 | Armadillo C++              | ArmadilloJava                   | Notes
-----------------------|----------------------------|---------------------------------|------
size(A, 1)             | A.n_rows                   | A.n_rows                        | 
size(A, 2)             | A.n_cols                   | A.n_cols                        | 
numel(A)               | A.n_elem                   | A.n_elem                        | 


Element access 
--------------

Matlab                               | Armadillo C++        | ArmadilloJava           | Notes
-------------------------------------|----------------------|-------------------------|------
A(i+1, j+1)                          | A(i, j)              | A(i, j)                 | 
A(i+1, j+1) = A(i+1, j+1) Operator b | A(i, j) Operator= b  | A(i, j, Op.Operator, b) | 
A(n)                                 | A(n)                 | A(n)                    | 
A(n) = A(n) Operator b               | A(n) Operator= b     | A(n, Op.Operator, b)    | 


Member functions
----------------

Matlab                 | Armadillo C++              | ArmadilloJava                   | Notes
-----------------------|----------------------------|---------------------------------|------


### Submatrix views

Matlab                 | Armadillo C++              | ArmadilloJava                   | Notes
-----------------------|----------------------------|---------------------------------|------
col
row
cols
rows
subvec
elem
submat
unsafe_col


### Internal data access
**Not recommend unless you know what you are doing.**
ArmadilloJava uses [Efficient Java Matrix Library](https://code.google.com/p/efficient-java-matrix-library/) internally.

Matlab                 | Armadillo C++              | ArmadilloJava                   | Notes
-----------------------|----------------------------|---------------------------------|------
                       | A.memptr()                 | A.memptr()                      | 
                       | A.colptr()                 |                                 | *Not suppported*


Non-member functions
--------------------

### Maxtrix-valued element-wise functions

Matlab                 | Armadillo C++              | ArmadilloJava                   | Notes
-----------------------|----------------------------|---------------------------------|------
abs(A)                 | abs(A)                     | Arma.abs(A)                     | 
eps(A)                 | eps(A)                     | Arma.eps(A)                     | 
exp(A)                 | exp(A)                     | Arma.exp(A)                     | 
2 .^ A                 | exp2(A)                    | Arma.exp2(A)                    | 
10 .^ A                | exp10(A)                   | Arma.exp10(A)                   | 
                       | trunc_exp(A)               | Arma.trunc_exp(A)               | **Matlab:** B = exp(A); B(isinf(B)) = realmax
log(A)                 | log(A)                     | Arma.log(A)                     | 
log2(A)                | log2(A)                    | Arma.log2(A)                    | 
log10(A)               | log10(A)                   | Arma.log10(A)                   | 
                       | trunc_log(A)               | Arma.trunc_log(A)               | **Matlab:** B = A; B(isinf(B)) = realmax; B(find(not(B))) = realmin; log(B)
A .^ p                 | pow(A, p)                  | Arma.pow(A, p)                  | 
sqrt(A)                | sqrt(A)                    | Arma.sqrt(A)                    | 
A .^ 2                 | square(A)                  | Arma.square(A)                  | 
floor(A)               | floor(A)                   | Arma.floor(A)                   | 
ceil(A)                | ceil(A)                    | Arma.ceil(A)                    | 
round(A)               | round(A)                   | Arma.round(A)                   | 
sign(A)                | sign(A)                    | Arma.sign(A)                    | 

#### Trigonometric functions

Matlab                 | Armadillo C++              | ArmadilloJava                   | Notes
-----------------------|----------------------------|---------------------------------|------
sin(A)                 | sin(A)                     | Arma.sin(A)                     | 
asin(A)                | asin(A)                    | Arma.asin(A)                    | 
asinh(A)               | asinh(A)                   | Arma.asinh(A)                   | 
cos(A)                 | cos(A)                     | Arma.cos(A)                     | 
acos(A)                | acos(A)                    | Arma.acos(A)                    | 
acosh(A)               | acosh(A)                   | Arma.acosh(A)                   | 
tan(A)                 | tan(A)                     | Arma.tan(A)                     | 
atan(A)                | atan(A)                    | Arma.atan(A)                    | 
atanh(A)               | atanh(A)                   | Arma.atanh(A)                   | 


### Scalar-valued functions of vectors/matrices

Matlab                 | Armadillo C++              | ArmadilloJava                   | Notes
-----------------------|----------------------------|---------------------------------|------
sum(A(:))              | accu(A)                    | Arma.accu(A)                    | 
                       | as_scalar(A)               | Arma.accu(A)                    | 
det(A)                 | det(A)                     | Arma.det(A)                     | 
dot(A, B)              | dot(A, B)                  | Arma.dot(A, B)                  | **Note:** A, B must be vectors.
                       | norm_dot(A, B)             | Arma.norm_dot(A, B)             | **Matlab:** dot(A, B) / sqrt(dot(A, A) * dot(B, B))
                       | log_det(v, s, A)           | Arma.log_det(v, s, A)           | **Matlab:** d = det(A); v = log(abs(d)); s = sign(d) (very inefficient)
norm(A, p)             | norm(A, p)                 | Arma.norm(A, p)                 | 
rank(A [, t])          | rank(A [, t])              | Arma.rank(A [, t])              | **Default:** t = max(size(A))*eps(norm(A))
trace(A)               | trace(A)                   | Arma.trace(A)                   | 


### Scalar/vector-valued functions of vectors/matrices

Matlab                 | Armadillo C++              | ArmadilloJava                   | Notes
-----------------------|----------------------------|---------------------------------|------
diag(A [, k])          | diagvec(A [, k])           | Arma.diagvec(A [, k])           | **Default:** k = 0 **Matlab:** Use A(k) if A is a vector.
prod(A [, d])          | prod(A [, d])              | Arma.prod(A [, d])              | **Note:** d is omitted if A is a vector. **Default:** d = 0
sum(A [, d])           | sum(A [, d])               | Arma.sum(A [, d])               | **Note:** d is omitted if A is a vector. **Default:** d = 0


#### Statistics
Matlab                 | Armadillo C++              | ArmadilloJava                   | Notes
-----------------------|----------------------------|---------------------------------|------
min(A [, d])           | min(A [, d])               | Arma.min(A [, d])               | **Note:** d is omitted if A is a vector. **Default:** d = 0
max(A [, d])           | max(A [, d])               | Arma.max(A [, d])               | **Note:** d is omitted if A is a vector. **Default:** d = 0
mean(A [, d])          | mean(A [, d])              | Arma.mean(A [, d])              | **Note:** d is omitted if A is a vector. **Default:** d = 0
median(A [, d])        | median(A [, d])            | Arma.median(A [, d])            | **Note:** d is omitted if A is a vector. **Default:** d = 0
std(A [, n] [, d])     | stddev(A [, n] [, d])      | Arma.stddev(A [, n] [, d])      | **Note:** d is omitted if A is a vector. **Default:** n = 0, d = 0
var(A [, n] [, d])     | var(A [, n] [, d])         | Arma.var(A [, n] [, d])         | **Note:** d is omitted if A is a vector. **Default:** n = 0, d = 0


### Matrix-valued functions of vectors/matrices

Matlab                 | Armadillo C++              | ArmadilloJava                   | Notes
-----------------------|----------------------------|---------------------------------|------
all(A [, d])           | all(A [, d])               | Arma.all(A [, d])               | **Default:** d = 0
any(A [, d])           | any(A [, d])               | Arma.any(A [, d])               | **Default:** d = 0
find(A [, k] [, s])    | find(A [, k] [, s])        | Arma.find(A [, k] [, s])        | **Default:** k = 0, s = "first"
conv(A, B)             | conv(A, B)                 | Arma.conv(A, B)                 | **Note:** A, B must be vectors.
                       | cor(A [, B] [, t])         | Arma.cor(A [, B] [, t])         | **Default:** t = 0
cov(A [, B] [, t])     | cov(A [, B] [, t])         | Arma.cov(A [, B] [, t])         | **Default:** t = 0
cross(A, B)            | cross(A, B)                | Arma.cross(A, B)                | **Note:** A, B must be 3-dimensional vectors.
cumsum(A [, d])        | cumsum(A [, d])            | Arma.cumsum(A [, d])            | 
diag(A)                | diagmat(A)                 | Arma.diagmat(A)                 | **Matlab:** Use diag(digag(A)) if A is not a vector.
fliplr(A)              | fliplr(A)                  | Arma.fliplr(A)                  | 
flipud(A)              | flipud(A)                  | Arma.flipud(A)                  | 
                       | hist(X [, n [, d]])        | Arma.hist(X [, n [, d]])        | **Default:** n = 10, d = 0
                       | hist(X, c [, d])           | Arma.hist(X, c [, d])           | **Default:** d = 0
                       | histc(X, e [, d])          | Arma.histc(X, e [, d])          | **Default:** d = 0
[A B]                  | join_rows(A, B)            | Arma.join_rows(A, B)            | 
[A B]                  | join_horiz(A, B)           | Arma.join_horiz(A, B)           | 
[A; B]                 | join_cols(A, B)            | Arma.join_cols(A, B)            | 
[A; B]                 | join_vert(A, B)            | Arma.join_vert(A, B)            | 
kron(A, B)             | kron(A, B)                 | Arma.kron(A, B)                 | 
                       | reshape(A, n, m [, d])     | Arma.reshape(A, n, m [, d])     | **Default:** d = 0
                       | resize(A, n, m)            | Arma.resize(A, n, m)            | 
                       | shuffle(A [, d])           | Arma.shuffle(A [, d])           | **Default:** d = 0 **Matlab:** A(randperm(size(A, 1)), :) (with d = 0)
sort(A [, d] [, t])    | sort(A [, t] [, d])        | Arma.sort(A [, t] [, d])        | **Default:** t = 0 ('ascend' in Matlab), d = 0
[~, I] = sort(A [, t]) | sort_index(A [, t])        | Arma.sort_index(A [, t])        | **Note:** A must be a vector. **Default:** t = 0 ('ascend' in Matlab)
[~, I] = sort(A [, t]) | stable_sort_index(A [, t]) | Arma.stable_sort_index(A [, t]) | **Note:** A must be a vector. **Default:** t = 0 ('ascend' in Matlab)
                       | symmatu(A)                 | Arma.symmatu(A)                 | **Matlab:** B = triu(A); B + B' - diag(diag(A))
                       | symmatl(A)                 | Arma.symmatl(A)                 | **Matlab:** B = tril(A); B + B' - diag(diag(A))
A'                     | trans(A)                   | Arma.trans(A)                   | 
triu(A)                | trimatu(A)                 | Arma.trimatu(A)                 | 
tril(A)                | trimatl(A)                 | Arma.trimatl(A)                 | 
unique(A)              | unique(A)                  | Arma.unique(A)                  | 
                       | vectorise(A [, d])         | Arma.vectorise(A [, d])         | **Default** d = 0 **Matlab:** Use A(:) for d = 0 and A(:)' otherwise.


### Decompositions

Matlab                 | Armadillo C++              | ArmadilloJava                   | Notes
-----------------------|----------------------------|---------------------------------|------
                       | chol                       |                                 | 
                       | eig_sym                    |                                 | 
                       | fft                        |                                 | 
                       | inv                        |                                 | 
                       | lu                         |                                 | 
                       | pinv                       |                                 | 
                       | princomp                   |                                 | 
                       | qr                         |                                 | 
                       | qr_econ                    |                                 | 
                       | solve                      |                                 | 
                       | svd                        |                                 | 
                       | svd_econ                   |                                 | 
                       | syl                        |                                 | 

                     
Others
------

### Statistics
Matlab                 | Armadillo C++              | ArmadilloJava                   | Notes
-----------------------|----------------------------|---------------------------------|------


### Pre-defined constants
Matlab                 | Armadillo C++              | ArmadilloJava                   | Notes
-----------------------|----------------------------|---------------------------------|------


