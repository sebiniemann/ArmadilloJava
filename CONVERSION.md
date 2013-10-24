Syntax conversion table
=======================
Between Matlab, Armadillo C++ and ArmadilloJava

**Note:** Currently, only real-valued dense matrices with double precision are supported.


Matrix initialisation
----------------------------------

Matlab                     | Armadillo C++              | ArmadilloJava                   | Notes
---------------------------|----------------------------|---------------------------------|------
                           | A()                        | A()                             | 
A = [ ... ]                | A << ... << endr;          | A(new double[]{ ... })          | **Note:** Results in a column vector.
A = [ ... ]                | A << ... << endr;          | A(new double[][]{ ... })        | 
A = B                      | A(B);                      | A(B)                            | 
                           | A(" ... ");                |                                 | *Not suppported*
                           | A(n)                       | A(n)                            | **Note:** Results in a column vector. **Matlab:** Use zeros(n, 1)
                           | A(n, m)                    | A(n, m)                         | **Matlab:** Use zeros(n, m)
                           | A(n, fill::none)           | A(n, Fill.NONE)                 | **Note:** Results in a column vector. **Matlab:** Use zeros(n, 1)
                           | A(n, m, fill::none)        | A(n, m, Fill.NONE)              | **Matlab:** Use zeros(n, m)
zeros(n, 1)                | A(n, fill::zeros)          | A(n, Fill.ZEROS)                | **Note:** Results in a column vector.
zeros(n, m)                | A(n, m, fill::zeros)       | A(n, m, Fill.ZEROS)             | 
ones(n, 1)                 | A(n, fill::ones)           | A(n, Fill.ONES)                 | **Note:** Results in a column vector.
ones(n, m)                 | A(n, m, fill::ones)        | A(n, m, Fill.ONES)              | 
eye(n, m)                  | A(n, m, fill::eye)         | A(n, m, Fill.EYE)               | 
randu(n, 1)                | A(n, fill::randu)          | A(n, Fill.RANDU, rng)           | **Note:** Results in a column vector. **Note:** rng is a random number generator.
randu(n, m)                | A(n, m, fill::randu)       | A(n, m, Fill.RANDU, rng)        | **Note:** rng is a random number generator.
randn(n, 1)                | A(n, fill::randn)          | A(n, Fill.RANDN, rng)           | **Note:** Results in a column vector. **Note:** rng is a random number generator.
randn(n, m)                | A(n, m, fill::randn)       | A(n, m, Fill.RANDN, rng)        | **Note:** rng is a random number generator.


Matrix generation
----------------------------------

Matlab                     | Armadillo C++              | ArmadilloJava                   | Notes
---------------------------|----------------------------|---------------------------------|------
zeros(n, 1)                | zeros(n)                   | Arma.zeros(n)                   | **Note:** Results in a column vector.
zeros(n, m)                | zeros(n, m)                | Arma.zeros(n, m)                | 
ones(n, 1)                 | ones(n)                    | Arma.ones(n)                    | **Note:** Results in a column vector.
ones(n, m)                 | ones(n, m)                 | Arma.ones(n, m)                 | 
eye(n, m)                  | eye(n, m)                  | Arma.eye(n, m)                  | 
randu(n, 1)                | randu(n)                   | Arma.randu(n, rng)              | **Note:** Results in a column vector.
randu(n, m)                | randu(n, m)                | Arma.randu(n, m, rng)           | 
randn(n, 1)                | randn(n)                   | Arma.randn(n, rng)              | **Note:** Results in a column vector.
randn(n, m)                | randn(n, m)                | Arma.randn(n, m, rng)           | 
repmat(A, n, m)            | repmat(A, n, m)            | Arma.repmat(A, n, m)            | 
toeplitz(A [, B])          | toeplitz(A [, B])          | Arma.toeplitz(A [, B])          | 
                           | circ_toeplitz(A)           | Arma.circ_toeplitz(A)           | **Matlab:** [Matlab Central File Excange - Circulant matrix](http://www.mathworks.com/matlabcentral/fileexchange/22858)
linspace(a, b [, n])       | linspace(a, b [, n])       | Arma.linspace(a, b [, n])       | **Note:** Generates a column vector. **Default:** n = 100 


Matrix attributes
-----------------

Matlab                     | Armadillo C++              | ArmadilloJava                   | Notes
---------------------------|----------------------------|---------------------------------|------
size(A, 2)                 | A.n_cols                   | A.n_cols                        | 
size(A, 1)                 | A.n_rows                   | A.n_rows                        | 
numel(A)                   | A.n_elem                   | A.n_elem                        | 


Element access 
--------------


Matlab                     | Armadillo C++              | ArmadilloJava                   | Notes
---------------------------|----------------------------|---------------------------------|------
A(i+1, j+1)                | A(i, j)                    | A(i, j)                         | 
A(n+1)                     | A(n)                       | A(n)                            | 


Member functions
----------------

### Submatrix views

#### Contiguous views

Matlab                     | Armadillo C++                        | ArmadilloJava                   | Notes
---------------------------|--------------------------------------|---------------------------------|------
A(:, :)                    | A.submat(span::all, span::all)       | A                               | 
A(:, :)                    | A(span::all, span::all)              | A                               | 
A(:, j)                    | A.col(j)                             | A.col(j)                        | 
A(:, j)                    | A(span::all, j)                      | A.col(j)                        | 
A(a:b, j)                  | A(span(a, b), j)                     | A.col(a, b, j)                  | 
A(:, a:b)                  | A.cols(a, b)                         | A.cols(a, b)                    | 
A(:, a:b)                  | A.submat(span::all, span(a, b))      | A.cols(a, b)                    | 
A(:, a:b)                  | A(span::all, span(a, b))             | A.cols(a, b)                    | 
                           | A.unsafe_col(j)                      |                                 | *Not suppported*
A(i, :)                    | A.row(i)                             | A.row(i)                        | 
A(i, :)                    | A(i, span::all)                      | A.row(i)                        | 
A(i, a:b)                  | A(i, span(a, b))                     | A.row(a, b, i)                  | 
A(a:b, :)                  | A.rows(a, b)                         | A.rows(a, b)                    | 
A(a:b, :)                  | A.submat(span(a, b), span::all)      | A.rows(a, b)                    | 
A(a:b, :)                  | A(span(a, b), span::all)             | A.rows(a, b)                    | 
A(ai:bi, aj:bj)            | A.submat(ai, bi, aj, bj)             | A.submat(ai, bi, aj, bj)        | 
A(ai:bi, aj:bj)            | A.submat(span(ai, bi), span(aj, bj)) | A.submat(ai, bi, aj, bj)        | 
A(ai:bi, aj:bj)            | A(span(ai, bi), span(aj, bj))        | A.submat(ai, bi, aj, bj)        | 
A(:)                       | A(span::all)                         | A                               | **Note:** A is a vector.
A(a:b)                     | A.subvec(a, b)                       | A.subvec(a, b)                  | **Note:** A is a vector.
A(a:b)                     | A(span(a, b))                        | A.subvec(a, b)                  | **Note:** A is a vector.

                           
#### Non-contiguous views

Matlab                     | Armadillo C++              | ArmadilloJava                   | Notes
---------------------------|----------------------------|---------------------------------|------
A(I)                       | A.elem(I)                  | A.elem(I)                       | **Note:** I is a vector.
A(I)                       | A(I)                       | A.elem(I)                       | **Note:** I is a vector.
A(:, J)                    | A.cols(J)                  | A.cols(J)                       | **Note:** J is a vector.
A(I, :)                    | A.rows(I)                  | A.rows(I)                       | **Note:** I is a vector.
A(I, J)                    | A.submat(I, J)             | A.submat(I, J)                  | **Note:** I, J are vectors.
A(I, J)                    | A(I, J)                    | A.submat(I, J)                  | **Note:** I, J are vectors.


#### Miscellaneous views

Matlab                     | Armadillo C++              | ArmadilloJava                   | Notes
---------------------------|----------------------------|---------------------------------|------
                           | A.diag([k])                | A.diag([k])                     | **Default:** k = 0
                           | A.each_col([I])            | A.each_col([I])                 | **Note:** I is a vector. **Matlab:** Use bsxfun
                           | A.each_row([I])            | A.each_row([I])                 | **Note:** I is a vector. **Matlab:** Use bsxfun
                           

### Arithmetic operations
              
Operation           | Matlab | Armadillo C++ | ArmadilloJava
--------------------|--------|---------------|--------------
Plus                | A + B  | A + B         | A.plus(B)
Minus               | A - B  | A -B          | A.minus(B)
Times               | A * B  | A * B         | A.times(B)
Element-wise times  | A .* B | A % B         | A.elemTimes(B)
Element-wise divide | A ./ B | A / B         | A.elemDivide(B)
Negate              | -A     | -A            | A.negate()


#### Inplace operations

**ArmadilloJava:** A.view(... is a placeholder for any of the specific views given above. Use A.submat(Op. ...) for inplace operations on all elements.

Operation           | Matlab     | Armadillo C++ | ArmadilloJava
--------------------|------------|---------------|------------------------------------
Increment           | A = A + 1  | A++           | A.view(..., Op.INCREMENT)
Decrement           | A = A - 1  | A--           | A.view(..., Op.DECREMENT)
Plus                | A = A + B  | A += B        | A.view(..., Op.PLUS, B)
Minus               | A = A - B  | A -= B        | A.view(..., Op.MINUS, B)
Element-wise times  | A = A .* B | A %= B        | A.view(..., Op.ELEMTIMES, B)
Element-wise divide | A = A ./ B | A /= B        | A.view(..., Op.ELEMDIVIDE, B)
Equal               | A = B      | A = B         | A.view(..., Op.EQUAL, B)


### Relational operations

Operation               | Matlab  | Armadillo C++ | ArmadilloJava
------------------------|---------|---------------|--------------
Equal                   | A == B  | A == B        | Op.evaluate(A, Op.EQUAL, B)
Not equal               | A != B  | A != B        | Op.evaluate(A, Op.NOT_EQUAL, B)
Strict less than        | A < B   | A < B         | Op.evaluate(A, Op.STRICT_LESS, B)
Non-strict less than    | A <= B  | A <= B        | Op.evaluate(A, Op.LESS, B)
Non-strict greater than | A => B  | A >= B        | Op.evaluate(A, Op.GREATER, B)
Strict greater than     | A > B   | A < B         | Op.evaluate(A, Op.STRICT_GREATER, B)
              
              
### Internal data access
**Not recommend unless you know what you are doing.**
ArmadilloJava uses [Efficient Java Matrix Library](https://code.google.com/p/efficient-java-matrix-library/) internally.

Matlab                     | Armadillo C++              | ArmadilloJava                   | Notes
---------------------------|----------------------------|---------------------------------|------
                           | A.memptr()                 | A.memptr()                      | 
                           | A.colptr()                 |                                 | *Not suppported*

                           
### Inline initilisation

Matlab                     | Armadillo C++              | ArmadilloJava                   | Notes
---------------------------|----------------------------|---------------------------------|------
A = zeros(size(A))         | A.zeros()                  | A.zeros()                       | 
A = zeros(n, 1)            | A.zeros(n)                 | A.zeros(n)                      | 
A = zeros(n, m)            | A.zeros(n, m)              | A.zeros(n , m)                  | 
A = ones(size(A))          | A.ones()                   | A.ones()                        | 
A = ones(n, 1)             | A.ones(n )                 | A.ones(n)                       | 
A = ones(n, m)             | A.ones(n, m)               | A.ones(n, m)                    | 
A = eye(size(A))           | A.eye()                    | A.eye()                         | 
A = eye(n, m)              | A.eye(n, m)                | A.eye(n, m)                     | 
A = randu(size(A))         | A.randu()                  | A.randu(rng)                    | **Note:** rng is a random number generator.
A = randu(n, 1)            | A.randu(n)                 | A.randu(n, rng)                 | **Note:** rng is a random number generator.
A = randu(n, m)            | A.randu(n, m)              | A.randu(n, m, rng)              | **Note:** rng is a random number generator.
A = randn(size(A))         | A.randn()                  | A.randn(rng)                    | **Note:** rng is a random number generator.
A = randn(n, 1)            | A.randn(n)                 | A.randn(n, rng)                 | **Note:** rng is a random number generator.
A = randn(n, m)            | A.randn(n, m)              | A.randn(n, m, rng)              | **Note:** rng is a random number generator.
A(:) = v                   | A.fill(v)                  | A.fill(v)                       | 
                           
                           
### Row/Column insert/remove

Matlab                                                 | Armadillo C++              | ArmadilloJava                   | Notes
-------------------------------------------------------|----------------------------|---------------------------------|------
A = [A(:, 1:j-1) B A(end:j+1, :)]                      | A.insert_cols(j, B)        | A.insert_cols(j, B)             | **Note:** B is a vector.
A = [A(:, 1:j-1) zeros(size(A, 1), n); A(end.j+1, :)]  | A.insert_cols(j, n [, z])  | A.insert_cols(j, n [, z])       | **Default:** z = true
A = [A(1:i-1, :); B; A(i+1:end, :)]                    | A.insert_rows(i, B)        | A.insert_rows(i, B)             | **Note:** B is a vector.
A = [A(1:i-1, :); zeros(n, size(A, 2)); A(i+1:end, :)] | A.insert_rows(i, n [, z])  | A.insert_rows(i, n [, z])       | **Default:** z = true
A = [A(:, 1:j-1) A(end.j+1, :)]                        | A.shed_col(j)              | A.shed_col(j)                   | 
A = [A(:, 1:a-1) A(end.b+1, :)]                        | A.shed_cols(a, b)          | A.shed_cols(a, b)               | 
A = [A(1:i-1, :); A(i+1:end, :)]                       | A.shed_row(i)              | A.shed_row(i)                   | 
A = [A(1:a-1, :); A(b+1:end, :)]                       | A.shed_rows(a, b)          | A.shed_rows(a, b)               | 
                           
                           
### Swap functions
Matlab                     | Armadillo C++              | ArmadilloJava                   | Notes
---------------------------|----------------------------|---------------------------------|------
A(:) = B(:)                | A.swap(B)                  | A.swap(B)                       | 
A(:, [b a]) = A(:, [a b])  | A.swap_cols(a, b)          | A.swap_cols(a, b)               | 
A([b a], :) = A([a b], :)  | A.swap_rows(a, b)          | A.swap_rows(a, b)               | 

                           
### Is functions

Matlab                     | Armadillo C++              | ArmadilloJava                   | Notes
---------------------------|----------------------------|---------------------------------|------
isempty(A)                 | A.is_empty()               | A.is_empty()                    | 
isfinite(A)                | A.is_finite()              | A.is_finite()                   | 
                           | A.is_square()              | A.is_square()                   | **Matlab:** S = size(A); (length(S) < 3) && (S(1) == S(2))
isvector(A)                | A.is_vec()                 | A.is_vec()                      | 
iscolumn(A)                | A.is_colvec()              | A.is_colvec()                   | 
isrow(A)                   | A.is_rowvec()              | A.is_rowvec()                   | 
                           
                           
### Bound checks

Matlab                     | Armadillo C++                          | ArmadilloJava                          | Notes
---------------------------|----------------------------------------|----------------------------------------|------
                           | A.in_range(n)                          | A.in_range(n)                          | **Matlab:** n > 0 && numel(A) <= n
                           | A.in_range(span(a, b))                 | A.in_range(Span(a, b))                 | **Matlab:** a > 0 && numel(A) <= b
                           | A.in_range(i, j)                       | A.in_range(i, j)                       | **Matlab:** i > 0 && j > 0 && size(A, 1) <= i && size(A, 2) <= j
                           | A.in_range(span::all, span(a, b))      | A.in_range(Span(), Span(a, b))         | **Matlab:** a > 0 && size(A, 2) <= b
                           | A.in_range(span(a, b), span::all)      | A.in_range(Span(a, b), Span())         | **Matlab:** a > 0 && size(A, 1) <= b
                           | A.in_range(span(ai, bi), span(aj, bj)) | A.in_range(Span(ai, bi), Span(aj, bj)) | **Matlab:** ai > 0 && aj > 0 && size(A, 1) <= bi && size(A, 2) <= bj
                           
                           
### Inline statistics

Matlab                     | Armadillo C++              | ArmadilloJava                   | Notes
---------------------------|----------------------------|---------------------------------|------
min(A)                     | A.min()                    | A.min()                         | 
[v, n] = min(A)            | A.min(n)                   | A.min(n)                        | 
[v, n] = min(A)            | A.min(i, j)                | A.min(i, j)                     | **Matlab:** [i, j] = ind2sub(size(A), n)
max(A)                     | A.max()                    | A.max()                         | 
[v, n] = max(A)            | A.max(n)                   | A.max(n)                        | 
[v, n] = max(A)            | A.max(i, j)                | A.max(i, j)                     | **Matlab:** [i, j] = ind2sub(size(A), n)
                           
                           
### Reshape/resize
Matlab                     | Armadillo C++              | ArmadilloJava                   | Notes
---------------------------|----------------------------|---------------------------------|------
                           | A.set_size(n [, m])        | A.set_size(n [, m])             | 
A = reshape(A, n, m)       | A.reshape(n, m [, d])      | A.reshape(n, m [, d])           | **Default:** d = 0
                           | A.resize(n [, m])          | A.resize(n [, m])               | 
                           
                           
### Text output functions

Matlab                     | Armadillo C++              | ArmadilloJava                   | Notes
---------------------------|----------------------------|---------------------------------|------
A                          | A.print([h])               | A.print([h])                    | **Default:** h = '' **Note:** Output may differ
A                          | A.print(s [, h])           | A.print(s [, h])                | **Default:** h = '' **Note:** Output may differ
A                          | A.raw_print([h])           | A.raw_print([h])                | **Default:** h = '' **Note:** Output may differ
A                          | A.raw_print(s [, h])       | A.raw_print(s [, h])            | **Default:** h = '' **Note:** Output may differ
                           
                           
### File input/output

Matlab                     | Armadillo C++              | ArmadilloJava                   | Notes
---------------------------|----------------------------|---------------------------------|------
save(n, 'A', '-ascii')     | A.save(n [, t])            | A.save(n [, t])                 | **Note:** n is a string **Default:** t = 'arma_binary'
                           | A.save(s [, t])            | A.save(s [, t])                 | **Note:** s is a stream **Default:** t = 'arma_binary'
A = load(n)                | A.load(n [, t])            | A.load(n [, t])                 | **Note:** n is a string **Default:** t = 'auto_detect'
                           | A.load(s [, t])            | A.load(s [, t])                 | **Note:** s is a stream **Default:** t = 'auto_detect'
                           | A.quiet_save(n [, t])      |                                 | *Not suppported*
                           | A.quiet_save(s [, t])      |                                 | *Not suppported*
                           | A.quiet_load(n [, t])      |                                 | *Not suppported*
                           | A.quiet_load(s [, t])      |                                 | *Not suppported*
                           
                           
### Inline function operations

Matlab                     | Armadillo C++              | ArmadilloJava                   | Notes
---------------------------|----------------------------|---------------------------------|------
                           | A.transform(f)             |                                 | *Not suppported* **Note:** f is a function. 
                           | A.transform(l)             |                                 | *Not suppported* **Note:** l is a lambda function. 
                           | A.imbue(f)                 |                                 | *Not suppported* **Note:** f is a function. 
                           | A.imbue(l)                 |                                 | *Not suppported* **Note:** l is a lambda function. 
                           
                           
### C++ STL container functions

Matlab                     | Armadillo C++              | ArmadilloJava                   | Notes
---------------------------|----------------------------|---------------------------------|------
A = []                     | A.clear()                  | A.clear()                       | 
isempty(A)                 | A.empty()                  | A.empty()                       | 
numel(A)                   | A.size()                   | A.size()                        | 
                           
                           
### Miscellaneous
                           
Matlab                     | Armadillo C++              | ArmadilloJava                   | Notes
---------------------------|----------------------------|---------------------------------|------
inv(A)                     | A.i([s])                   | A.i()                           | 
A'                         | A.t()                      | A.t()                           | 
A = []                     | A.reset()                  | A.reset()                       | 
A = reshape(A, size(B))    | A.copy_size(B)             | A.copy_size(B)                  | 
                           | A.eval()                   | A.eval()                        | *Not suppported*


Non-member functions
--------------------

### Maxtrix-valued element-wise functions

Matlab                     | Armadillo C++              | ArmadilloJava                   | Notes
---------------------------|----------------------------|---------------------------------|------
abs(A)                     | abs(A)                     | Arma.abs(A)                     | 
eps(A)                     | eps(A)                     | Arma.eps(A)                     | 
exp(A)                     | exp(A)                     | Arma.exp(A)                     | 
2 .^ A                     | exp2(A)                    | Arma.exp2(A)                    | 
10 .^ A                    | exp10(A)                   | Arma.exp10(A)                   | 
                           | trunc_exp(A)               | Arma.trunc_exp(A)               | **Matlab:** B = exp(A); B(isinf(B)) = realmax
log(A)                     | log(A)                     | Arma.log(A)                     | 
log2(A)                    | log2(A)                    | Arma.log2(A)                    | 
log10(A)                   | log10(A)                   | Arma.log10(A)                   | 
                           | trunc_log(A)               | Arma.trunc_log(A)               | **Matlab:** B = A; B(isinf(B)) = realmax; B(find(not(B))) = realmin; log(B)
A .^ p                     | pow(A, p)                  | Arma.pow(A, p)                  | 
sqrt(A)                    | sqrt(A)                    | Arma.sqrt(A)                    | 
A .^ 2                     | square(A)                  | Arma.square(A)                  | 
floor(A)                   | floor(A)                   | Arma.floor(A)                   | 
ceil(A)                    | ceil(A)                    | Arma.ceil(A)                    | 
round(A)                   | round(A)                   | Arma.round(A)                   | 
sign(A)                    | sign(A)                    | Arma.sign(A)                    | 

#### Trigonometric functions

Matlab                     | Armadillo C++              | ArmadilloJava                   | Notes
---------------------------|----------------------------|---------------------------------|------
sin(A)                     | sin(A)                     | Arma.sin(A)                     | 
asin(A)                    | asin(A)                    | Arma.asin(A)                    | 
asinh(A)                   | asinh(A)                   | Arma.asinh(A)                   | 
cos(A)                     | cos(A)                     | Arma.cos(A)                     | 
acos(A)                    | acos(A)                    | Arma.acos(A)                    | 
acosh(A)                   | acosh(A)                   | Arma.acosh(A)                   | 
tan(A)                     | tan(A)                     | Arma.tan(A)                     | 
atan(A)                    | atan(A)                    | Arma.atan(A)                    | 
atanh(A)                   | atanh(A)                   | Arma.atanh(A)                   | 


### Scalar-valued functions of vectors/matrices

Matlab                     | Armadillo C++              | ArmadilloJava                   | Notes
---------------------------|----------------------------|---------------------------------|------
sum(A(:))                  | accu(A)                    | Arma.accu(A)                    | 
dot(A, B)                  | dot(A, B)                  | Arma.dot(A, B)                  | **Note:** A, B are vectors.
                           | norm_dot(A, B)             | Arma.norm_dot(A, B)             | **Matlab:** dot(A, B) / sqrt(dot(A, A) * dot(B, B))
det(A)                     | det(A)                     | Arma.det(A)                     | 
                           | log_det(v, s, A)           | Arma.log_det(v, s, A)           | **Matlab:** d = det(A); v = log(abs(d)); s = sign(d) (very inefficient)
norm(A, p)                 | norm(A, p)                 | Arma.norm(A, p)                 | 
rank(A [, t])              | rank(A [, t])              | Arma.rank(A [, t])              | **Default:** t = max(size(A))*eps(norm(A))
trace(A)                   | trace(A)                   | Arma.trace(A)                   | 
                           | as_scalar(A)               | Arma.accu(A)                    | 


### Scalar/vector-valued functions of vectors/matrices

Matlab                     | Armadillo C++              | ArmadilloJava                   | Notes
---------------------------|----------------------------|---------------------------------|------
diag(A [, k])              | diagvec(A [, k])           | Arma.diagvec(A [, k])           | **Default:** k = 0 **Matlab:** Use A(k) if A is a vector.
prod(A [, d])              | prod(A [, d])              | Arma.prod(A [, d])              | **Note:** d is omitted if A is a vector. **Default:** d = 0
sum(A [, d])               | sum(A [, d])               | Arma.sum(A [, d])               | **Note:** d is omitted if A is a vector. **Default:** d = 0


#### Statistics
Matlab                     | Armadillo C++              | ArmadilloJava                   | Notes
---------------------------|----------------------------|---------------------------------|------
min(A [, d])               | min(A [, d])               | Arma.min(A [, d])               | **Note:** d is omitted if A is a vector. **Default:** d = 0
max(A [, d])               | max(A [, d])               | Arma.max(A [, d])               | **Note:** d is omitted if A is a vector. **Default:** d = 0
mean(A [, d])              | mean(A [, d])              | Arma.mean(A [, d])              | **Note:** d is omitted if A is a vector. **Default:** d = 0
median(A [, d])            | median(A [, d])            | Arma.median(A [, d])            | **Note:** d is omitted if A is a vector. **Default:** d = 0
std(A [, n] [, d])         | stddev(A [, n] [, d])      | Arma.stddev(A [, n] [, d])      | **Note:** d is omitted if A is a vector. **Default:** n = 0, d = 0
var(A [, n] [, d])         | var(A [, n] [, d])         | Arma.var(A [, n] [, d])         | **Note:** d is omitted if A is a vector. **Default:** n = 0, d = 0


### Matrix-valued functions of vectors/matrices

#### Matrix joins
Matlab                     | Armadillo C++              | ArmadilloJava                   | Notes
---------------------------|----------------------------|---------------------------------|------
[A; B]                     | join_cols(A, B)            | Arma.join_cols(A, B)            | 
[A; B]                     | join_vert(A, B)            | Arma.join_vert(A, B)            | 
[A B]                      | join_rows(A, B)            | Arma.join_rows(A, B)            | 
[A B]                      | join_horiz(A, B)           | Arma.join_horiz(A, B)           | 


#### Logic operations

Matlab                     | Armadillo C++              | ArmadilloJava                   | Notes
---------------------------|----------------------------|---------------------------------|------
any(A [, d])               | any(A [, d])               | Arma.any(A [, d])               | **Default:** d = 0
all(A [, d])               | all(A [, d])               | Arma.all(A [, d])               | **Default:** d = 0

                           
#### Sort functions
Matlab                     | Armadillo C++              | ArmadilloJava                   | Notes
---------------------------|----------------------------|---------------------------------|------
sort(A [, d] [, t])        | sort(A [, t] [, d])        | Arma.sort(A [, t] [, d])        | **Default:** t = 0 ('ascend' in Matlab), d = 0
[~, I] = sort(A [, t])     | sort_index(A [, t])        | Arma.sort_index(A [, t])        | **Note:** A is a vector. **Default:** t = 0 ('ascend' in Matlab)
[~, I] = sort(A [, t])     | stable_sort_index(A [, t]) | Arma.stable_sort_index(A [, t]) | **Note:** A is a vector. **Default:** t = 0 ('ascend' in Matlab)
                           

#### Reshape/resize
Matlab                     | Armadillo C++              | ArmadilloJava                   | Notes
---------------------------|----------------------------|---------------------------------|------ 
reshape(A, n, m)           | reshape(A, n, m [, d])     | Arma.reshape(A, n, m [, d])     | **Default:** d = 0
                           | resize(A, n, m)            | Arma.resize(A, n, m)            | 


#### Statistics

Matlab                     | Armadillo C++              | ArmadilloJava                   | Notes
---------------------------|----------------------------|---------------------------------|------
                           | cor(A [, B] [, t])         | Arma.cor(A [, B] [, t])         | **Default:** t = 0
cov(A [, B] [, t])         | cov(A [, B] [, t])         | Arma.cov(A [, B] [, t])         | **Default:** t = 0
                           
                           
#### Histogramm functions
Matlab                     | Armadillo C++              | ArmadilloJava                   | Notes
---------------------------|----------------------------|---------------------------------|------ 
                           | hist(X [, n [, d]])        | Arma.hist(X [, n [, d]])        | **Default:** n = 10, d = 0
                           | hist(X, c [, d])           | Arma.hist(X, c [, d])           | **Default:** d = 0
                           | histc(X, e [, d])          | Arma.histc(X, e [, d])          | **Default:** d = 0
                           

#### Swap functions

Matlab                     | Armadillo C++              | ArmadilloJava                   | Notes
---------------------------|----------------------------|---------------------------------|------
fliplr(A)                  | fliplr(A)                  | Arma.fliplr(A)                  | 
flipud(A)                  | flipud(A)                  | Arma.flipud(A)                  | 


#### Reinterpret functions

Matlab                     | Armadillo C++              | ArmadilloJava                   | Notes
---------------------------|----------------------------|---------------------------------|------
diag(A)                    | diagmat(A)                 | Arma.diagmat(A)                 | **Matlab:** Use diag(digag(A)) if A is not a vector.
triu(A)                    | trimatu(A)                 | Arma.trimatu(A)                 | 
tril(A)                    | trimatl(A)                 | Arma.trimatl(A)                 | 
                           | symmatu(A)                 | Arma.symmatu(A)                 | **Matlab:** B = triu(A); B + B' - diag(diag(A))
                           | symmatl(A)                 | Arma.symmatl(A)                 | **Matlab:** B = tril(A); B + B' - diag(diag(A))
                           
                    
#### Miscellaneous

Matlab                     | Armadillo C++              | ArmadilloJava                   | Notes
---------------------------|----------------------------|---------------------------------|------
A'                         | trans(A)                   | Arma.trans(A)                   | 
find(A [, k] [, s])        | find(A [, k] [, s])        | Arma.find(A [, k] [, s])        | **Default:** k = 0, s = "first"
cumsum(A [, d])            | cumsum(A [, d])            | Arma.cumsum(A [, d])            | 
conv(A, B)                 | conv(A, B)                 | Arma.conv(A, B)                 | **Note:** A, B are vectors.
cross(A, B)                | cross(A, B)                | Arma.cross(A, B)                | **Note:** A, B are 3-dimensional vectors.
kron(A, B)                 | kron(A, B)                 | Arma.kron(A, B)                 | 
                           | shuffle(A [, d])           | Arma.shuffle(A [, d])           | **Default:** d = 0 **Matlab:** A(randperm(size(A, 1)), :) (for d = 0)
unique(A)                  | unique(A)                  | Arma.unique(A)                  | 
                           | vectorise(A [, d])         | Arma.vectorise(A [, d])         | **Default** d = 0 **Matlab:** Use A(:) for d = 0 and A(:)' otherwise.
                    
                    
### Decompositions

Matlab                     | Armadillo C++              | ArmadilloJava                   | Notes
---------------------------|----------------------------|---------------------------------|------
chol(X)                    | chol(X)                    | Arma.chol(X)                    | 
R = chol(X)                | chol(R, X)                 | Arma.chol(R, X)                 | 
eig(X)                     | eig_sym(X)                 | Arma.eig_sym(X)                 | 
V = eig(X)                 | eig_sym(V, X)              | Arma.eig_sym(V, X)              | 
[V, E] = eig(X)            | eig_sym(V, E, X [, m])     | Arma.eig_sym(V, E, X)           | **Matlab:** E = diag(E)
inv(A)                     | inv(A [, s])               | Arma.inv(A)                     | 
B = inv(A)                 | inv(B, A)                  | Arma.inv(B, A)                  | 
[L, U] = lu(X)             | lu(L, U, X)                | Arma.lu(L, U, X)                | 
[L, U, P] = lu(X)          | lu(L, U, P, X)             | Arma.lu(L, U, P, X)             | 
pinv(A [, t])              | pinv(A [, t])              | Arma.pinv(A [, t])              | **Default:** t = max(size(A))*norm(A)*eps
B = pinv(A [, t])          | pinv(B, A [, t])           | Arma.pinv(B, A [, t])           | **Default:** t = max(size(A))*norm(A)*eps
princomp(X)                | princomp(X)                | Arma.princomp(X)                | 
C = princomp(X)            | princomp(C, X)             | Arma.princomp(C, X)             | 
[C, S] = princomp(X)       | princomp(C, S, X)          | Arma.princomp(C, S, X)          | 
[C, S, L] = princomp(X)    | princomp(C, S, L, X)       | Arma.princomp(C, S, L, X)       | 
[C, S, L, T] = princomp(X) | princomp(C, S, L, T, X)    | Arma.princomp(C, S, L, T, X)    | 
[Q, R] = qr(X)             | qr(Q, R, X)                | Arma.qr(Q, R, X)                | 
[Q, R] = qr(A, 0)          | qr_econ(Q, R, X)           | Arma.qr_econ(Q, R, X)           | 
A \ B                      | solve(A, B [, s])          | Arma.solve(A, B)                | 
X = A \ B                  | solve(X, A, B [, s])       | Arma.solve(X, A, B)             | 
svd(X)                     | svd(X)                     | Arma.svd(X)                     | 
[~, S, ~] = svd(X)         | svd(S, X)                  | Arma.svd(S, X)                  | 
[U, S, V] = svd(X)         | svd(U, X, V, X [, m])      | Arma.svd(U, X, V, X)            | 
                           | svd_econ(U, X, V, X [, m]) | Arma.svd_econ(U, X, V, X)       | 
                           | syl(A, B, C)               | Arma.syl(A, B, C)               | **Matlab:** See [Matlab solution 1-35X8UD](http://www.mathworks.com/support/solutions/en/data/1-35X8UD/?product=ML&solution=1-35X8UD)
                           | syl(X, A, B, C)            | Arma.syl(X, A, B, C)            | **Matlab:** See [Matlab solution 1-35X8UD](http://www.mathworks.com/support/solutions/en/data/1-35X8UD/?product=ML&solution=1-35X8UD)

                     
Miscellaneous
-------------

Matlab                     | Armadillo C++              | ArmadilloJava                   | Notes
---------------------------|----------------------------|---------------------------------|------
isfinite(A)                | is_finite(A)               | Arma.is_finite(A)               | **Note:** A may be a matrix or a scalar

### Statistics 

**Armadillo C++:** running_stat<double> S

**ArmadilloJava:** RunningStat S

Matlab                     | Armadillo C++              | ArmadilloJava                   | Notes
---------------------------|----------------------------|---------------------------------|------
                           | S(v)                       | S.update(v)                     | 
                           | S.count()                  | S.count()                       | 
                           | S.min()                    | S.min()                         | 
                           | S.max()                    | S.max()                         | 
                           | S.mean()                   | S.mean()                        | 
                           | S.var([n])                 | S.var([n])                      | **Default:** n = 0
                           | S.stddev([n])              | S.stddev([n])                   | **Default:** n = 0
                           | S.reset()                  | S.reset()                       | 


**Armadillo C++:** running_stat_vec<double> S([c]) **Default:** c = true

**ArmadilloJava:** RunningStatVec S([c]) **Default:** c = true

Matlab                     | Armadillo C++              | ArmadilloJava                   | Notes
---------------------------|----------------------------|---------------------------------|------
                           | S(v)                       | S.update(v)                     | 
                           | S.count()                  | S.count()                       | 
                           | S.min()                    | S.min()                         | 
                           | S.max()                    | S.max()                         | 
                           | S.mean()                   | S.mean()                        | 
                           | S.var([n])                 | S.var([n])                      | **Default:** n = 0
                           | S.stddev([n])              | S.stddev([n])                   | **Default:** n = 0
                           | S.cov([n])                 | S.cov([n])                      | **Note:** c must be true **Default:** n = 0
                           | S.reset()                  | S.reset()                       | 

                      
### Wall clock

**Armadillo C++:** wall_clock W

**ArmadilloJava:** WallClock W

Matlab                     | Armadillo C++              | ArmadilloJava                   | Notes
---------------------------|----------------------------|---------------------------------|------
tic                        | W.tic()                    | W.tic()                         | 
toc                        | W.toc()                    | W.toc()                         | 
                       

### Pre-defined constants
Matlab                     | Armadillo C++              | ArmadilloJava                   | Notes
---------------------------|----------------------------|---------------------------------|------
pi                         | datum::pi                  | Datum.pi                        | 
Inf                        | datum::inf                 | Datum.inf                       | 
NaN                        | datum::nan                 | Datum.nan                       | 
exp(1)                     | datum::e                   | Datum.e                         | 
sqrt(2)                    | datum::sqrt2               | Datum.sqrt2                     | 
eps                        | datum::eps                 | Datum.eps                       | **Note:** Machine dependant
log(realmin)               | datum::log_min             | Datum.log_min                   | **Note:** Machine dependant
log(realmax)               | datum::log_max             | Datum.log_max                   | **Note:** Machine dependant
0.5772156649015329         | datum::euler               | Datum.euler                     | 
1.6180339887498948         | datum::gratio              | Datum.gratio                    | 
1.660538782e-27            | datum::m_u                 | Datum.m_u                       | 
6.02214179e23              | datum::N_A                 | Datum.N_A                       | 
1.3806504e-23              | datum::k                   | Datum.k                         | 
8.617343e-5                | datum::k_evk               | Datum.k_evk                     | 
0.52917720859e-10          | datum::a_0                 | Datum.a_0                       | 
927.400915e-26             | datum::mu_B                | Datum.mu_B                      | 
3.76730313461771e-2        | datum::Z_0                 | Datum.Z_0                       | 
7.7480917004e-5            | datum::G_0                 | Datum.G_0                       | 
8.9875517873681764e9       | datum::k_e                 | Datum.k_e                       | 
8.85418781762039e-12       | datum::eps_0               | Datum.eps_0                     | 
9.10938215e-31             | datum::m_e                 | Datum.m_e                       | 
1.602176487e-19            | datum::eV                  | Datum.eV                        | 
1.602176487e-19            | datum::ec                  | Datum.ec                        | 
96485.3399                 | datum::F                   | Datum.F                         | 
7.2973525376e-3            | datum::alpha               | Datum.alpha                     | 
137.035999679              | datum::alpha_inv           | Datum.alpha_inv                 | 
483597.891e9               | datum::K_J                 | Datum.K_J                       | 
1.25663706143592e-06       | datum::mu_0                | Datum.mu_0                      | 
2.067833667e-15            | datum::phi_0               | Datum.phi_0                     | 
8.314472                   | datum::R                   | Datum.R                         | 
6.67428e-11                | datum::G                   | Datum.G                         | 
6.62606896e-34             | datum::h                   | Datum.h                         | 
1.054571628e-34            | datum::h_bar               | Datum.h_bar                     | 
1.672621637e-27            | datum::m_p                 | Datum.m_p                       | 
10973731.568527            | datum::R_inf               | Datum.R_inf                     | 
299792458.0                | datum::c_0                 | Datum.c_0                       | 
5.670400e-8                | datum::sigma               | Datum.sigma                     | 
25812.807557               | datum::R_k                 | Datum.R_k                       | 
2.8977685e-3               | datum::b                   | Datum.b                         | 
