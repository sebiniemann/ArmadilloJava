package arma;

/**
 * <p>
 * Provides constants for the following operators.
 * <p>
 * Arithmetic operators: <br>
 * {@link #PLUS}, {@link #MINUS}, {@link #TIMES}, {@link #ELEMTIMES}, {@link #DIVIDE}, {@link #ELEMDIVIDE}
 * <p>
 * Relational operators: <br>
 * {@link #EQUAL}, {@link #NOT_EQUAL}, {@link #STRICT_LESS}, {@link #LESS}, {@link #STRICT_GREATER} , {@link #GREATER}
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
   * Equality (relational operator)
   */
  EQUAL,
  /**
   * Inequality (relational operator)
   */
  NOT_EQUAL,
  /**
   * Strict less than (relational operator)
   */
  STRICT_LESS,
  /**
   * Non-strict less than (relational operator)
   */
  LESS,
  /**
   * Strict greater than (relational operator)
   */
  STRICT_GREATER,
  /**
   * Non-strict greater than (relational operator)
   */
  GREATER
}
