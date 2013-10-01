package arma;

/**
 * <p>
 * Provides constants for the following operators.
 * <p>
 * Arithmetic operators: <br>
 * {@link #PLUS}, {@link #MINUS}, {@link #TIMES}, {@link #ELEMTIMES}, {@link #DIVIDE},
 * {@link #ELEMDIVIDE}
 * <p>
 * Comparison operators: <br>
 * {@link #EQUAL}, {@link #NOT_EQUAL}, {@link #STRICT_LESS}, {@link #LESS}, {@link #STRICT_GREATER}
 * , {@link #GREATER}
 * 
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 * @version 0.9
 */
public enum Op {
  /**
   * Summation (arithmetic operator)
   */
  PLUS,
  /**
   * Subtraction (arithmetic operator)
   */
  MINUS,
  /**
   * Multiplication (arithmetic operator)
   */
  TIMES,
  /**
   * Element-wise multiplication (arithmetic operator)
   */
  ELEMTIMES,
  /**
   * Division (arithmetic operator)
   */
  DIVIDE,
  /**
   * Element-wise division (arithmetic operator)
   */
  ELEMDIVIDE,
  /**
   * Equality (comparison operator)
   */
  EQUAL,
  /**
   * Inequality (comparison operator)
   */
  NOT_EQUAL,
  /**
   * Strict less than (comparison operator)
   */
  STRICT_LESS,
  /**
   * Non-strict less than (comparison operator)
   */
  LESS,
  /**
   * Strict greater than (comparison operator)
   */
  STRICT_GREATER,
  /**
   * Non-strict greater than (comparison operator)
   */
  GREATER
}
