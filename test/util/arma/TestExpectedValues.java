/*******************************************************************************
 * Copyright 2013 Sebastian Niemann <niemann@sra.uni-hannover.de> and contributors.
 * 
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://opensource.org/licenses/MIT
 *******************************************************************************/

package arma;

import java.io.*;

/**
 * @author Daniel Kiechle <kiechle@sra.uni-hannover.de>
 *
 */
public class TestExpectedValues {

	/**
	 * 
	 */
	static double _pow = 100;
	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		testMatrixValuedElementWiseFunctionsTrigonometric();
		testMatrixValuedElementWiseFunctionsMiscellaneous();
		testScalarVectorValuedFunctionsOfVectorsMatricesStatistic();

	}

	/**
	 * @throws FileNotFoundException
	 */
	protected static void testMatrixValuedElementWiseFunctionsTrigonometric() throws FileNotFoundException {
		Mat testData = (Mat) TestMatrixValuedElementWiseFunctionsTrigonometric.getTestData().iterator().next()[0];
		Mat expected = new Mat (testData.n_rows, testData.n_cols);
		
		//Function sign()
				for (int n = 0; n < testData.n_elem; n++){
					expected.at(n, Op.EQUAL, Math.signum(testData.at(n)));
				}
				expected.save("test/data/Arma/TestMatrixValuedElementWiseFunctions/Trigonometric.sign.mat");
				System.out.println("signum true");
		
		//Function sin()
				for (int n = 0; n < testData.n_elem; n++){
					expected.at(n, Op.EQUAL, Math.sin(testData.at(n)));
				}
				expected.save("test/data/Arma/TestMatrixValuedElementWiseFunctions/Trigonometric.sin.mat");
				System.out.println("sin true");
		
		//Function asin()
				for (int n = 0; n < testData.n_elem; n++){
					expected.at(n, Op.EQUAL, Math.asin(testData.at(n)));
				}
				expected.save("test/data/Arma/TestMatrixValuedElementWiseFunctions/Trigonometric.asin.mat");
				System.out.println("asin true");
				
		//Function sinh()
				for (int n = 0; n < testData.n_elem; n++){
					expected.at(n, Op.EQUAL, Math.sinh(testData.at(n)));
				}
				expected.save("test/data/Arma/TestMatrixValuedElementWiseFunctions/Trigonometric.sinh.mat");
				System.out.println("sinh true");
				
		//Function asinh()
				for (int n = 0; n < testData.n_elem; n++){
					expected.at(n, Op.EQUAL, Math.log((testData.at(n)) + Math.sqrt((testData.at(n))*(testData.at(n)) + 1.0)));
				}
				expected.save("test/data/Arma/TestMatrixValuedElementWiseFunctions/Trigonometric.asinh.mat");
				System.out.println("asinh true");
				
		//Function cos()
				for (int n = 0; n < testData.n_elem; n++){
					expected.at(n, Op.EQUAL, Math.cos(testData.at(n)));
				}
				expected.save("test/data/Arma/TestMatrixValuedElementWiseFunctions/Trigonometric.cos.mat");
				System.out.println("cos true");
				
		//Function acos()
				for (int n = 0; n < testData.n_elem; n++){
					expected.at(n, Op.EQUAL, Math.acos(testData.at(n)));
				}
				expected.save("test/data/Arma/TestMatrixValuedElementWiseFunctions/Trigonometric.acos.mat");
				System.out.println("acos true");
				
		//Function cosh()
				for (int n = 0; n < testData.n_elem; n++){
					expected.at(n, Op.EQUAL, Math.cosh(testData.at(n)));
				}
				expected.save("test/data/Arma/TestMatrixValuedElementWiseFunctions/Trigonometric.cosh.mat");
				System.out.println("cosh true");
				
		//Function acosh()
				for (int n = 0; n < testData.n_elem; n++){
					expected.at(n, Op.EQUAL, Math.log((testData.at(n)) + Math.sqrt((testData.at(n))*(testData.at(n)) - 1.0)));
				}
				expected.save("test/data/Arma/TestMatrixValuedElementWiseFunctions/Trigonometric.acosh.mat");
				System.out.println("acosh true");
			 
		//Function tan()
				for (int n = 0; n < testData.n_elem; n++){
					expected.at(n, Op.EQUAL, Math.tan(testData.at(n)));
				}
				expected.save("test/data/Arma/TestMatrixValuedElementWiseFunctions/Trigonometric.tan.mat");
				System.out.println("tan true");
				
		//Function atan()
				for (int n = 0; n < testData.n_elem; n++){
					expected.at(n, Op.EQUAL, Math.atan(testData.at(n)));
				}
				expected.save("test/data/Arma/TestMatrixValuedElementWiseFunctions/Trigonometric.atan.mat");
				System.out.println("atan true");
				
		//Function tanh()
				for (int n = 0; n < testData.n_elem; n++){
					expected.at(n, Op.EQUAL, Math.tanh(testData.at(n)));
				}
				expected.save("test/data/Arma/TestMatrixValuedElementWiseFunctions/Trigonometric.tanh.mat");
				System.out.println("tanh true");
				
		//Function atanh()
				for (int n = 0; n < testData.n_elem; n++){
					expected.at(n, Op.EQUAL, 0.5*Math.log( (1 + testData.at(n)) / (1 - testData.at(n))));
				}
				expected.save("test/data/Arma/TestMatrixValuedElementWiseFunctions/Trigonometric.atanh.mat");
				System.out.println("atanh true");
	}
	
	/**
	 * @throws FileNotFoundException
	 */
	protected static void testMatrixValuedElementWiseFunctionsMiscellaneous() throws FileNotFoundException {
		Mat testData = (Mat) TestMatrixValuedElementWiseFunctionsMiscellaneous.getTestData().iterator().next()[0];
		Mat expected = new Mat (testData.n_rows, testData.n_cols);
		double _pow = 100;
	
		//Function abs()
			for (int n = 0; n < testData.n_elem; n++){
				expected.at(n, Op.EQUAL, Math.abs(testData.at(n)));
			}
			expected.save("test/data/Arma/TestMatrixValuedElementWiseFunctions/Miscellaneous.abs.mat");
			System.out.println("abs true");
		
		//Function eps()
			for (int n = 0; n < testData.n_elem; n++){
				expected.at(n, Op.EQUAL, Math.ulp(testData.at(n)));
			}
			expected.save("test/data/Arma/TestMatrixValuedElementWiseFunctions/Miscellaneous.eps.mat");
			System.out.println("eps true");
			
		//Function exp()
			for (int n = 0; n < testData.n_elem; n++){
				expected.at(n, Op.EQUAL, Math.exp(testData.at(n)));
			}
			expected.save("test/data/Arma/TestMatrixValuedElementWiseFunctions/Miscellaneous.exp.mat");
			System.out.println("exp true");
			
		//Function exp2()
			for (int n = 0; n < testData.n_elem; n++){
				expected.at(n, Op.EQUAL, Math.pow(2,(testData.at(n))));
			}
			expected.save("test/data/Arma/TestMatrixValuedElementWiseFunctions/Miscellaneous.exp2.mat");
			System.out.println("exp2 true");
			
		//Function exp10()
			for (int n = 0; n < testData.n_elem; n++){
				expected.at(n, Op.EQUAL, Math.pow(10,(testData.at(n))));
			}
			expected.save("test/data/Arma/TestMatrixValuedElementWiseFunctions/Miscellaneous.exp10.mat");
			System.out.println("exp10 true");
			
		//Function trunc_exp
			for (int n = 0; n < testData.n_elem; n++){
			  double element = Math.exp(testData.at(n));
			  if(Double.isInfinite(element)) {
			    expected.at(n, Op.EQUAL, Double.MAX_VALUE);
			  } else {
			    expected.at(n, Op.EQUAL, element);
			  }
			}
			expected.save("test/data/Arma/TestMatrixValuedElementWiseFunctions/Miscellaneous.trunc_exp.mat");
			System.out.println("exp trunc_exp");
			
		//Function log()
			for (int n = 0; n < testData.n_elem; n++){
				expected.at(n, Op.EQUAL, (Math.log(testData.at(n))));
			}
			expected.save("test/data/Arma/TestMatrixValuedElementWiseFunctions/Miscellaneous.log.mat");
			System.out.println("log true");
			
		//Function log2()
			for (int n = 0; n < testData.n_elem; n++){
				expected.at(n, Op.EQUAL, (Math.log(testData.at(n))/Math.log(2)));
			}
			expected.save("test/data/Arma/TestMatrixValuedElementWiseFunctions/Miscellaneous.log2.mat");
			System.out.println("log2 true");
			
		//Function log10()
			for (int n = 0; n < testData.n_elem; n++){
				expected.at(n, Op.EQUAL, (Math.log10(testData.at(n))));
			}
			expected.save("test/data/Arma/TestMatrixValuedElementWiseFunctions/Miscellaneous.log10.mat");
			System.out.println("log10 true");
			
		//Function trunc_log()
			for (int n = 0; n < testData.n_elem; n++){
				double element = testData.at(n);
				if(Double.isInfinite(element)) {
					element = Double.MAX_VALUE;
				} else {
					element = Double.MIN_NORMAL;
				}
				expected.at(n, Op.EQUAL, Math.log(element));
			}
			expected.save("test/data/Arma/TestMatrixValuedElementWiseFunctions/Miscellaneous.trunc_log.mat");
			System.out.println("trunc_log true");
			
		//Function pow()
			for (int n = 0; n < testData.n_elem; n++){
				expected.at(n, Op.EQUAL, (Math.pow(testData.at(n),_pow)));
			}
			expected.save("test/data/Arma/TestMatrixValuedElementWiseFunctions/Miscellaneous.pow.mat");
			System.out.println("pow true");
			
		//Function sqrt()
			for (int n = 0; n < testData.n_elem; n++){
				expected.at(n, Op.EQUAL, (Math.sqrt(testData.at(n))));
			}
			expected.save("test/data/Arma/TestMatrixValuedElementWiseFunctions/Miscellaneous.sqrt.mat");
			System.out.println("sqrt true");
			
		//Function square()
			for (int n = 0; n < testData.n_elem; n++){
				expected.at(n, Op.EQUAL, (Math.pow(testData.at(n),2)));
			}
			expected.save("test/data/Arma/TestMatrixValuedElementWiseFunctions/Miscellaneous.square.mat");
			System.out.println("square true");
			
		//Function floor()
			for (int n = 0; n < testData.n_elem; n++){
				expected.at(n, Op.EQUAL, (Math.floor(testData.at(n))));
			}
			expected.save("test/data/Arma/TestMatrixValuedElementWiseFunctions/Miscellaneous.floor.mat");
			System.out.println("floor true");
			
		//Function ceil()
			for (int n = 0; n < testData.n_elem; n++){
				expected.at(n, Op.EQUAL, (Math.ceil(testData.at(n))));
			}
			expected.save("test/data/Arma/TestMatrixValuedElementWiseFunctions/Miscellaneous.ceil.mat");
			System.out.println("ceil true");
			
		//Function round()
			for (int n = 0; n < testData.n_elem; n++){
				expected.at(n, Op.EQUAL, (Math.round(testData.at(n))));
			}
			expected.save("test/data/Arma/TestMatrixValuedElementWiseFunctions/Miscellaneous.round.mat");
			System.out.println("round true");
	}
	
	/**
	 * @throws FileNotFoundException
	 */
	protected static void testScalarVectorValuedFunctionsOfVectorsMatricesStatistic() throws FileNotFoundException {
		Mat testData = (Mat) TestScalarVectorValuedFunctionsOfVectorsMatricesStatistic.getTestData().iterator().next()[0];
		Mat expectedrows =  new Mat(testData.n_rows, 1); // use for Dimension = 0
		Mat expectedcols =  new Mat(testData.n_cols, 1); // use for Dimension = 1
		
			
		//Function min() and minMat(..,0);
			for(int j = 0; j < testData.n_cols; j++) {
				Mat column = testData.col(j);

				double minimum = column.at(0);
				for(int n = 1; n < column.n_elem; n++) {
					minimum = Math.min(minimum, column.at(n));
				}

				expectedcols.at(j, Op.EQUAL, minimum);
			}
			expectedcols.save("test/data/Arma/TestScalarVectorValuedFunctionsOfVectorsMatrices/Statistic.min.d0.mat");
			System.out.println("min,minMat0 true");
			
		//Function minMat(..,1)
			for(int i = 0; i < testData.n_rows; i++) {
				Mat rows = testData.row(i);

				double minimum = rows.at(0);
				for(int n = 1; n < rows.n_elem; n++) {
			   	  minimum = Math.min(minimum, rows.at(n));
				}

				expectedrows.at(i, Op.EQUAL, minimum);
		    }
			expectedcols.save("test/data/Arma/TestScalarVectorValuedFunctionsOfVectorsMatrices/Statistic.min.d1.mat");
			System.out.println("minMat1 true");
				
		//Function max() and maxMat(..,0);
			for(int j = 0; j < testData.n_cols; j++) {
				Mat column = testData.col(j);

				double maximum = column.at(0);
				for(int n = 1; n < column.n_elem; n++) {
					maximum = Math.max(maximum, column.at(n));
				}

				expectedcols.at(j, Op.EQUAL, maximum);
			}
			expectedcols.save("test/data/Arma/TestScalarVectorValuedFunctionsOfVectorsMatrices/Statistic.max.d0.mat");
			System.out.println("max,maxMat0 true");
				
		//Function maxMat(..,1)
			for(int i = 0; i < testData.n_rows; i++) {
				Mat rows = testData.row(i);

				double maximum = rows.at(0);
				for(int n = 1; n < rows.n_elem; n++) {
		    	  maximum = Math.max(maximum, rows.at(n));
				}

				expectedrows.at(i, Op.EQUAL, maximum);
		    }
			expectedcols.save("test/data/Arma/TestScalarVectorValuedFunctionsOfVectorsMatrices/Statistic.max.d1.mat");
			System.out.println("maxMat1 true");
					
		//Function mean() and meanMat(..,0);
			for(int j = 0; j < testData.n_cols; j++) {
				Mat column = testData.col(j);
						
				double mean = 0;
				for(int n = 0; n < column.n_elem; n++) {
					mean = mean + column.at(n) ;
				}
				mean = mean / column.n_elem;
				expectedcols.at(j, Op.EQUAL, mean);
			}
			expectedcols.save("test/data/Arma/TestScalarVectorValuedFunctionsOfVectorsMatrices/Statistic.mean.d0.mat");
			System.out.println("mean,meanMat0 true");
					
		//Function meanMat(..,1)
			for(int i = 0; i < testData.n_rows; i++) {
				Mat rows = testData.row(i);

				double mean = 0;
				for(int n = 1; n < rows.n_elem; n++) {
			  	  mean = mean + rows.at(n);
				}
				mean = mean / rows.n_elem;
							
				expectedrows.at(i, Op.EQUAL, mean);
			}
			expectedcols.save("test/data/Arma/TestScalarVectorValuedFunctionsOfVectorsMatrices/Statistic.mean.d1.mat");
			System.out.println("meanMat1 true");
			
		//Function median() and medianMat(..,0);
			for(int j = 0; j < testData.n_cols; j++) {
				Mat column = testData.col(j);
				
				double median;		
				if(column.n_elem % 2 ==1){
					median = column.at(column.getElementIndex(column.n_elem / 2));
				}else{
					median = (((column.at(column.n_elem / 2))+(column.at((column.n_elem / 2)-1))) * 1/2);
				}
				expectedcols.at(j, Op.EQUAL, median);
			}
			expectedcols.save("test/data/Arma/TestScalarVectorValuedFunctionsOfVectorsMatrices/Statistic.median.d0.mat");
			System.out.println("median,medianMat0 true");
					
		//Function medianMat(..,1)
			for(int i = 0; i < testData.n_rows; i++) {
				Mat rows = testData.row(i);
				double median;
				if(rows.n_elem % 2 ==1){
					median = rows.at(rows.getElementIndex(rows.n_elem / 2));
				}else{
					median = (((rows.at(rows.n_elem / 2))+(rows.at((rows.n_elem / 2)-1))) * 1/2);
				}
							
				expectedrows.at(i, Op.EQUAL, median);
			}
			expectedcols.save("test/data/Arma/TestScalarVectorValuedFunctionsOfVectorsMatrices/Statistic.median.d1.mat");
			System.out.println("medianMat1 true");
				
		//Function stddev(), stddev(..,0), stddevMat(), stddevMat(..,0) and stddevMat(..,0,0)
			for(int j = 0; j < testData.n_cols; j++) {
				Mat column = testData.col(j);
						
				double x = 0;
				for (int i =0; i < column.n_elem; i++){
					x = x + column.at(i);
				}
				x = x / (1/column.n_elem);
				
				double stddev = 0;
				for(int n = 0; n < column.n_elem; n++){
					stddev= (column.at(n)-x)*(column.at(n)-x);
				}
				stddev = Math.sqrt(stddev*(1/(column.n_elem - 1)));
				
				expectedcols.at(j, Op.EQUAL, stddev);
			}
			expectedcols.save("test/data/Arma/TestScalarVectorValuedFunctionsOfVectorsMatrices/Statistic.stddev.n0.d0.mat");
			System.out.println("stddev(), stddev(..,0), stddevMat(), stddevMat(..,0) and stddevMat(..,0,0) true");
			
		//Function stddev(..,1), stddevMat(..,1) and stddevMat(..,1,0) 
			for(int j = 0; j < testData.n_cols; j++) {
				Mat column = testData.col(j);
				
				double x = 0;
				for (int i =0; i < column.n_elem; i++){
					x = x + column.at(i);
				}
				x = x / (1/column.n_elem);
		
				double stddev = 0;
				for(int n = 0; n < column.n_elem; n++){
					stddev= (column.at(n)-x)*(column.at(n)-x);
				}
				stddev = Math.sqrt(stddev*(1/column.n_elem ));
		
				expectedcols.at(j, Op.EQUAL, stddev);
			}
			expectedcols.save("test/data/Arma/TestScalarVectorValuedFunctionsOfVectorsMatrices/Statistic.stddev.n1.d0.mat");
			System.out.println("stddev(..,1), stddevMat(..,1) and stddevMat(..,1,0)  true");
					
		//Function stddevMat(..,0,1)
			for(int i = 0; i < testData.n_rows; i++) {
				Mat rows = testData.row(i);
				
				double x = 0;
				for (int j =0; j < rows.n_elem; j++){
					x = x + rows.at(j);
				}
				x = x / (1/rows.n_elem);
		
				double stddev = 0;
				for(int n = 0; n < rows.n_elem; n++){
					stddev= (rows.at(n)-x)*(rows.at(n)-x);
				}
				stddev = Math.sqrt(stddev*(1/(rows.n_elem - 1)));
					
				expectedrows.at(i, Op.EQUAL, stddev);
			}
			expectedcols.save("test/data/Arma/TestScalarVectorValuedFunctionsOfVectorsMatrices/Statistic.stddev.n0.d1.mat");
			System.out.println("stddevMat(..,0,1) true");
			
		//Function stddevMat(..,1,1)
			for(int i = 0; i < testData.n_rows; i++) {
				Mat rows = testData.row(i);
				
				double x = 0;
				for (int j =0; j < rows.n_elem; j++){
					x = x + rows.at(j);
				}
				x = x / (1/rows.n_elem);
		
				double stddev = 0;
				for(int n = 0; n < rows.n_elem; n++){
					stddev= (rows.at(n)-x)*(rows.at(n)-x);
				}
				stddev = Math.sqrt(stddev*(1/rows.n_elem));
					
				expectedrows.at(i, Op.EQUAL, stddev);
			}
			expectedcols.save("test/data/Arma/TestScalarVectorValuedFunctionsOfVectorsMatrices/Statistic.stdev.n1.d1.mat");
			System.out.println("stddevMat(..,1,1) true");
			
		//Function var(), var(..,0), varMat(), varMat(..,0) and varMat(..,0,0)
			for(int j = 0; j < testData.n_cols; j++) {
				Mat column = testData.col(j);
						
				double x = 0;
				for (int i =0; i < column.n_elem; i++){
					x = x + column.at(i);
				}
				x = x / (1/column.n_elem);
				
				double var = 0;
				for(int n = 0; n < column.n_elem; n++){
					var= (column.at(n)-x)*(column.at(n)-x);
				}
				var = Math.pow((Math.sqrt(var*(1/(column.n_elem - 1)))),(2));
				
				expectedcols.at(j, Op.EQUAL, var);
			}
			expectedcols.save("test/data/Arma/TestScalarVectorValuedFunctionsOfVectorsMatrices/Statistic.var.n0.d0.mat");
			System.out.println("var(), var(..,0), varMat(), varMat(..,0) and varMat(..,0,0) true");
			
		//Function var(..,1), varMat(..,1) and varMat(..,1,0) 
			for(int j = 0; j < testData.n_cols; j++) {
				Mat column = testData.col(j);
				
				double x = 0;
				for (int i =0; i < column.n_elem; i++){
					x = x + column.at(i);
				}
				x = x / (1/column.n_elem);
		
				double var = 0;
				for(int n = 0; n < column.n_elem; n++){
					var= (column.at(n)-x)*(column.at(n)-x);
				}
				var = Math.pow((Math.sqrt(var*(1/column.n_elem ))),(2));
		
				expectedcols.at(j, Op.EQUAL, var);
			}
			expectedcols.save("test/data/Arma/TestScalarVectorValuedFunctionsOfVectorsMatrices/Statistic.var.n1.d0.mat");
			System.out.println("var(..,1), varMat(..,1) and varMat(..,1,0)  true");
					
		//Function varMat(..,0,1)
			for(int i = 0; i < testData.n_rows; i++) {
				Mat rows = testData.row(i);
				
				double x = 0;
				for (int j =0; j < rows.n_elem; j++){
					x = x + rows.at(j);
				}
				x = x / (1/rows.n_elem);
		
				double var = 0;
				for(int n = 0; n < rows.n_elem; n++){
					var= (rows.at(n)-x)*(rows.at(n)-x);
				}
				var = Math.pow((Math.sqrt(var*(1/(rows.n_elem - 1)))),(2));
					
				expectedrows.at(i, Op.EQUAL, var);
			}
			expectedcols.save("test/data/Arma/TestScalarVectorValuedFunctionsOfVectorsMatrices/Statistic.var.n0.d1.mat");
			System.out.println("varMat(..,0,1) true");
			
		//Function varMat(..,1,1)
			for(int i = 0; i < testData.n_rows; i++) {
				Mat rows = testData.row(i);
				
				double x = 0;
				for (int j =0; j < rows.n_elem; j++){
					x = x + rows.at(j);
				}
				x = x / (1/rows.n_elem);
		
				double var = 0;
				for(int n = 0; n < rows.n_elem; n++){
					var= (rows.at(n)-x)*(rows.at(n)-x);
				}
				var = Math.pow((Math.sqrt(var*(1/rows.n_elem))),(2));
					
				expectedrows.at(i, Op.EQUAL, var);
			}
			expectedcols.save("test/data/Arma/TestScalarVectorValuedFunctionsOfVectorsMatrices/Statistic.var.n1.d1.mat");
			System.out.println("varMat(..,1,1) true");
	}
	
	protected static void testScalarVectorValuedFunctionsOfVectorsMatricesLogic() throws FileNotFoundException {
		Mat testData = (Mat) TestScalarVectorValuedFunctionsOfVectorsMatricesLogic.getTestData().iterator().next()[0];
		Mat expected =  new Mat(testData.n_rows, 1);
		
		
	}	
}
