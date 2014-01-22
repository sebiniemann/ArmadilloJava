package org.armadillojava;

public class Mat extends AbstractMat {

  public Mat() {
    
  }
  
  public Mat(int n_rows, int n_cols) {
    
  }
  
  public Mat(int n_rows, int n_cols, FileType fill_type) {
    
  }
  
  public Mat(Mat mat) {
    
  }
  
  public Mat(Col vec) {
    
  }
  
  public Mat(Row row) {
    
  }
  
  public Mat(double[] array) {
    
  }

  public Mat diag() {
    
  }

  public void diag(Op unary_operator) {
    
  }

  public void diag(Op binary_operator, double operand) {
    
  }

  public void diag(Op binary_operator, AbstractMat operand) {
    
  }

  public Mat diag(int k) {
    
  }

  public void diag(int k, Op unary_operator) {
    
  }

  public void diag(int k, Op binary_operator, double operand) {
    
  }

  public void diag(int k, Op binary_operator, AbstractMat operand) {
    
  }

  public void each_col(Op unary_operator) {
    
  }

  public void each_col(Op binary_operator, double operand) {
    
  }

  public void each_col(Op binary_operator, AbstractMat operand) {
    
  }

  public void each_col(AbstractMat vector_of_indices, Op unary_operator) {
    
  }

  public void each_col(AbstractMat vector_of_indices, Op binary_operator, double operand) {
    
  }

  public void each_col(AbstractMat vector_of_indices, Op binary_operator, AbstractMat operand) {
    
  }

  public void each_row(Op unary_operator) {
    
  }

  public void each_row(Op binary_operator, double operand) {
    
  }

  public void each_row(Op binary_operator, AbstractMat operand) {
    
  }

  public void each_row(AbstractMat vector_of_indices, Op unary_operator) {
    
  }

  public void each_row(AbstractMat vector_of_indices, Op binary_operator, double operand) {
    
  }

  public void each_row(AbstractMat vector_of_indices, Op binary_operator, AbstractMat operand) {
    
  }
  
  public double at(int i, int j) {
    
  }
  
  public void eye() {
    
  }
  
  public void eye(int n_rows, int n_cols) {
    
  }
  
  public Mat i() {
    
  }
  
  public boolean is_square() {
    
  }
  
  public boolean is_vec() {
    
  }
  
  public boolean is_colvec() {
    
  }
  
  public boolean is_rowvec() {
    
  }
  
  public void insert_rows(int row_number, AbstractMat X) {
  
  }
  
  public void insert_rows(int row_number, int number_of_rows) {
  
  }
  
  public void insert_rows(int row_number, int number_of_rows, boolean set_to_zero) {
  
  }
  
  public void insert_cols(int row_number, AbstractMat X) {
  
  }
  
  public void insert_cols(int row_number, int number_of_rows) {
  
  }
  
  public void insert_cols(int row_number, int number_of_rows, boolean set_to_zero) {
  
  }
  
  public double min(int[] row_of_min_val, int[] col_of_min_val) {
    
  }
  
  public double max(int[] row_of_max_val, int[] col_of_max_val) {
    
  }
  
  public void ones(int n_rows, int n_cols) {
    
  }
  
  public void randu(int n_rows, int n_cols) {
    
  }
  
  public void randn(int n_rows, int n_cols) {
    
  }
  
  public void zeros(int n_rows, int n_cols) {
    
  }
  
  public void reshape(int n_rows, int n_cols) {
    
  }
  
  public void resize(int n_rows, int n_cols) {
    
  }
  
  public void set_size(int n_rows, int n_cols) {
    
  }
  
  public void shed_row(int row_number) {
    
  }
  
  public void shed_rows(int first_row, int last_row) {
    
  }
  
  public void shed_col(int column_number) {
    
  }
  
  public void shed_cols(int first_column, int last_column) {
    
  }

  @Override
  public Col col(int col_number) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Row row(int row_number) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Mat cols(int first_col, int last_col) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Mat rows(int first_row, int last_row) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Col col(Span span, int col_number) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Row row(int row_number, Span span) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Mat submat(int first_row, int first_col, int last_row, int last_col) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Mat submat(Span row_span, Span col_span) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Mat submat(int first_row, int first_col, Size size) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Col elem(AbstractMat vector_of_indices) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Mat cols(AbstractMat vector_of_column_indices) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Mat rows(AbstractMat vector_of_row_indices) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Mat submat(AbstractMat vector_of_row_indices, AbstractMat vector_of_column_indices) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void swap(AbstractMat X) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public AbstractMat t() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void copy_size(AbstractMat A) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public Mat plus(double X) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Mat plus(AbstractMat X) throws RuntimeException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Mat minus(double X) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Mat minus(AbstractMat X) throws RuntimeException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Mat elemDivide(double X) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Mat elemDivide(AbstractMat X) throws RuntimeException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public AbstractMat times(Col X) throws RuntimeException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public AbstractMat times(Row X) throws RuntimeException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public AbstractMat times(Mat X) throws RuntimeException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Mat elemTimes(double X) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Mat elemTimes(AbstractMat X) throws RuntimeException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Mat equal(double X) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Mat equal(AbstractMat X) throws RuntimeException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Mat nonEqual(double X) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Mat nonEqual(AbstractMat X) throws RuntimeException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Mat greaterThan(double X) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Mat greaterThan(AbstractMat X) throws RuntimeException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Mat lessThan(double X) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Mat lessThan(AbstractMat X) throws RuntimeException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Mat strictGreaterThan(double X) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Mat strictGreaterThan(AbstractMat X) throws RuntimeException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Mat strictLessThan(double X) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Mat strictLessThan(AbstractMat X) throws RuntimeException {
    // TODO Auto-generated method stub
    return null;
  }
  
}
