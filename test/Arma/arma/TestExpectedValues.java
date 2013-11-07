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

	static double _pow = 100;
	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		testMatrixValuedElementWiseFunctionsTrigonometric();
		testMatrixValuedElementWiseFunctionsMiscellaneous();

	}

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
				/*for (int n = 0; n < testData.n_elem; n++){
					expected.at(n, Op.EQUAL, Math.asin(testData.at(n)));
				}
				expected.save("test/data/Arma/TestMatrixValuedElementWiseFunctions/Trigonometric.asin.mat");
				System.out.println("asin true");
				*/
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
				/*for (int n = 0; n < testData.n_elem; n++){
					expected.at(n, Op.EQUAL, Math.acos(testData.at(n)));
				}
				expected.save("test/data/Arma/TestMatrixValuedElementWiseFunctions/Trigonometric.acos.mat");
				System.out.println("acos true");
				*/
		//Function cosh()
				for (int n = 0; n < testData.n_elem; n++){
					expected.at(n, Op.EQUAL, Math.cosh(testData.at(n)));
				}
				expected.save("test/data/Arma/TestMatrixValuedElementWiseFunctions/Trigonometric.cosh.mat");
				System.out.println("cosh true");
				
		//Function acosh()
				/*for (int n = 0; n < testData.n_elem; n++){
					expected.at(n, Op.EQUAL, Math.log((testData.at(n)) + Math.sqrt((testData.at(n))*(testData.at(n)) - 1.0)));
				}
				expected.save("test/data/Arma/TestMatrixValuedElementWiseFunctions/Trigonometric.acosh.mat");
				System.out.println("acosh true");
				*/
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
				/*for (int n = 0; n < testData.n_elem; n++){
					expected.at(n, Op.EQUAL, 0.5*Math.log( ((testData.at(n)) + 1.0) / ((testData.at(n)) - 1.0)));
				}
				expected.save("test/data/Arma/TestMatrixValuedElementWiseFunctions/Trigonometric.atanh.mat");
				System.out.println("atanh true");
				*/
	}
	
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
			/*for (int n = 0; n < testData.n_elem; n++){
				expected.at(n, Op.EQUAL, Math.exp(testData.at(n)));
			}
			expected.save("test/data/Arma/TestMatrixValuedElementWiseFunctions/Miscellaneous.trunc_exp.mat");
			System.out.println("exp trunc_exp");
			*/
		//Function log()
			/*for (int n = 0; n < testData.n_elem; n++){
				expected.at(n, Op.EQUAL, (Math.log(testData.at(n))));
			}
			expected.save("test/data/Arma/TestMatrixValuedElementWiseFunctions/Miscellaneous.log.mat");
			System.out.println("log true");
			*/
		//Function log2()
			/*for (int n = 0; n < testData.n_elem; n++){
				expected.at(n, Op.EQUAL, (Math.log(testData.at(n))/Math.log(2)));
			}
			expected.save("test/data/Arma/TestMatrixValuedElementWiseFunctions/Miscellaneous.log2.mat");
			System.out.println("log2 true");
			*/
		//Function log10()
			/*for (int n = 0; n < testData.n_elem; n++){
				expected.at(n, Op.EQUAL, (Math.log10(testData.at(n))));
			}
			expected.save("test/data/Arma/TestMatrixValuedElementWiseFunctions/Miscellaneous.log10.mat");
			System.out.println("log10 true");
			*/
		//Function trunc_log()
			/*for (int n = 0; n < testData.n_elem; n++){
				expected.at(n, Op.EQUAL, (Math.log(testData.at(n))));
			}
			expected.save("test/data/Arma/TestMatrixValuedElementWiseFunctions/Miscellaneous.trunc_log.mat");
			System.out.println("trunc_log true");
			*/
		//Function pow()
			for (int n = 0; n < testData.n_elem; n++){
				expected.at(n, Op.EQUAL, (Math.pow(testData.at(n),_pow)));
			}
			expected.save("test/data/Arma/TestMatrixValuedElementWiseFunctions/Miscellaneous.pow.mat");
			System.out.println("pow true");
			
		//Function sqrt()
			/*for (int n = 0; n < testData.n_elem; n++){
				expected.at(n, Op.EQUAL, (Math.sqrt(testData.at(n))));
			}
			expected.save("test/data/Arma/TestMatrixValuedElementWiseFunctions/Miscellaneous.sqrt.mat");
			System.out.println("sqrt true");
			*/
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
}
