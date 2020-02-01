package bigint;

/**
 * This class encapsulates a BigInteger, i.e. a positive or negative integer with 
 * any number of digits, which overcomes the computer storage length limitation of 
 * an integer.
 * 
 */
public class BigInteger {

	/**
	 * True if this is a negative integer
	 */
	boolean negative;
	
	/**
	 * Number of digits in this integer
	 */
	int numDigits;
	
	/**
	 * Reference to the first node of this integer's linked list representation
	 * NOTE: The linked list stores the Least Significant Digit in the FIRST node.
	 * For instance, the integer 235 would be stored as:
	 *    5 --> 3  --> 2
	 *    
	 * Insignificant digits are not stored. So the integer 00235 will be stored as:
	 *    5 --> 3 --> 2  (No zeros after the last 2)        
	 */
	DigitNode front;
	
	/**
	 * Initializes this integer to a positive number with zero digits, in other
	 * words this is the 0 (zero) valued integer.
	 */
	public BigInteger() {
		negative = false;
		numDigits = 0;
		front = null;
	}
	
	/**
	 * Parses an input integer string into a corresponding BigInteger instance.
	 * A correctly formatted integer would have an optional sign as the first 
	 * character (no sign means positive), and at least one digit character
	 * (including zero). 
	 * Examples of correct format, with corresponding values
	 *      Format     Value
	 *       +0            0
	 *       -0            0
	 *       +123        123
	 *       1023       1023
	 *       0012         12  
	 *       0             0
	 *       -123       -123
	 *       -001         -1
	 *       +000          0
	 *       
	 * Leading and trailing spaces are ignored. So "  +123  " will still parse 
	 * correctly, as +123, after ignoring leading and trailing spaces in the input
	 * string.
	 * 
	 * Spaces between digits are not ignored. So "12  345" will not parse as
	 * an integer - the input is incorrectly formatted.
	 * 
	 * An integer with value 0 will correspond to a null (empty) list - see the BigInteger
	 * constructor
	 * 
	 * @param integer Integer string that is to be parsed
	 * @return BigInteger instance that stores the input integer.
	 * @throws IllegalArgumentException If input is incorrectly formatted
	 */
	public static BigInteger parse(String integer) 
	throws IllegalArgumentException {
		
		/* IMPLEMENT THIS METHOD */
		BigInteger bigInteger = new BigInteger();
		String numbers = "123456789";
		String newNumber = "";
		boolean zero = false;
		boolean hitNumber = false;
		

		String integer1 = integer.trim(); //.trim function to eliminate leading and trailing spaces
		if (numbers.indexOf(integer1.charAt(0)) == -1){
			//Accounts for Negative numbers
			if (integer1.charAt(0) == '-' && integer1.length()!= 1) {
				bigInteger.negative = true;
				if (integer1.charAt(1) == '0') {
					zero = true;
				}	
			} 
			//Accounts for positive numbers
			else if (integer1.charAt(0) == '+' && integer1.length()!= 1) {
				bigInteger.negative = false;
				if (integer1.charAt(1) == '0') {
					zero = true;
				}
			} 
			//Accounts for 0
			else if (integer1.charAt(0) == '0') {
				zero = true;
			} 
			//Accounts for all other cases such as letters, etc.
			else {
				throw new IllegalArgumentException("bad format");
			}
		}
		else {
			newNumber += integer1.charAt(0);
			hitNumber = true;
		}

		
		for (int i = 1; i < integer1.length(); i++) {
			if (zero == true && integer1.charAt(i) == '0' && hitNumber == false) {
				//do nothing
			} 
			else if (Character.isDigit(integer1.charAt(i))) { //.isDigit determines whether char is a digit
				hitNumber = true;
				newNumber += integer1.charAt(i);
			} 
			else {
				throw new IllegalArgumentException("bad format");
			}
		}

		bigInteger.numDigits = newNumber.length();

		for (int i=0; i < newNumber.length(); i++) {
			int dig = Character.getNumericValue(newNumber.charAt(i)); //.getNumericValue returns int value that specified unicode character represents
			bigInteger.front = new DigitNode(dig,bigInteger.front);
		}
		return bigInteger; 
	}
	
	/**
	 * Adds the first and second big integers, and returns the result in a NEW BigInteger object. 
	 * DOES NOT MODIFY the input big integers.
	 * 
	 * NOTE that either or both of the input big integers could be negative.
	 * (Which means this method can effectively subtract as well.)
	 * 
	 * @param first First big integer
	 * @param second Second big integer
	 * @return Result big integer
	 */
	public static BigInteger add(BigInteger first, BigInteger second) {
		
		/* IMPLEMENT THIS METHOD */
		BigInteger Int1 = new BigInteger();
		BigInteger Int2 = new BigInteger();
		DigitNode add1 = first.front;
		DigitNode add2 = second.front;
		
		boolean bNegativeResult = false;
		boolean bNegative1 = false;
		boolean bNegative2 = false;
		
		int length = (first.numDigits > second.numDigits) ? first.numDigits : second.numDigits; //ternary operator
		int carry = 0;
		int sum = 0;

		//Accounts for negative numbers
		if (first.negative != second.negative) {
			//one of two numbers is negative--find largest one
			if (first.numDigits == second.numDigits) {
				//find which is larger by comparing digits starting at MSD
				DigitNode Iterator1 = first.front;
				DigitNode Iterator2 = second.front;				
				BigInteger temp1 = new BigInteger();
				BigInteger temp2 = new BigInteger();
				int i = 0;
				
				for (i=0; i<length; i++) {
					temp1.front = new DigitNode(Iterator1.digit, temp1.front);
					Iterator1 = Iterator1.next;					
					temp1.numDigits++;
					temp2.front = new DigitNode(Iterator2.digit, temp2.front);
					Iterator2 = Iterator2.next;					
					temp2.numDigits++;
				}
				for (i=0; i<length; i++) {
					if (temp1.front.digit != temp2.front.digit){
						//first digit found which is different
						break;
					} 
					else {
						temp1.front = temp1.front.next;
						temp2.front = temp2.front.next;
					}
				}
				if (i < length) {
					//Which of the two number is bigger?

					if (temp1.front.digit > temp2.front.digit){			
						//first - second
						bNegative1 = false;
						bNegative2 = true; 
						bNegativeResult = first.negative;

					}
					else if (temp2.front.digit > temp1.front.digit){
						//second - first
						bNegative1 = true;
						bNegative2 = false; 
						bNegativeResult = second.negative;
					}
				}
				else {				
					//all the digit are the same so result is zero
					Int1.front = new DigitNode(0, Int2.front);
					Int1.numDigits = 1;
					return Int1;
				}

			}
			else if (first.numDigits > second.numDigits) {
				//subtract second from first
				bNegative1 = false;
				bNegative2 = true; 
				bNegativeResult = first.negative;

			}
			else {
				// subtract first from second
				bNegative1 = true;
				bNegative2 = false; 
				bNegativeResult = second.negative;

			}
		}
		else {
			bNegative1 = false;
			bNegative2 = false; 
			bNegativeResult = second.negative;	
		}

		for (int i=0; i<length; i++) {
			sum = carry;
			carry = 0;
			if (add1 != null) {
				if(bNegative1 == true) {
					add1.digit=add1.digit*(-1);
				}
				sum += add1.digit;
				add1 = add1.next;
			}
			if (add2 != null) {
				if (bNegative2 == true) {
					add2.digit = add2.digit*(-1);
				}
				sum += add2.digit;
				add2 = add2.next;
			}
			if (sum > 9) {
				sum = sum-10;
				carry = 1;
			}

			if (sum < 0) {
				sum = sum + 10;
				carry = -1;
			}

			Int1.front = new DigitNode(sum, Int1.front);
			Int1.numDigits++; //was missing
		}
		if (carry > 0) {
			Int1.front = new DigitNode(carry, Int1.front);
			Int1.numDigits++; //was missing
		}
		
		//remove leading zeros		
		for (int i=0; i < Int1.numDigits; i++) {
			// do not include the zeros on the left side of the number
			if (Int1.front.digit == 0) {
				Int1.front = Int1.front.next;
				Int1.numDigits--;				
			}
			else {
				break;
			}			
		}		
		for (int i = 0;  i < Int1.numDigits; i++) {			
			Int2.front = new DigitNode(Int1.front.digit, Int2.front);
			Int1.front = Int1.front.next;
			Int2.numDigits++;
		}
		
		Int2.negative = bNegativeResult;
		return Int2;
	}
	
	/**
	 * Returns the BigInteger obtained by multiplying the first big integer
	 * with the second big integer
	 * 
	 * This method DOES NOT MODIFY either of the input big integers
	 * 
	 * @param first First big integer
	 * @param second Second big integer
	 * @return A new BigInteger which is the product of the first and second big integers
	 */
	public static BigInteger multiply(BigInteger first, BigInteger second) {
		
		/* IMPLEMENT THIS METHOD */
		BigInteger result = new BigInteger();
		BigInteger temp1 = new BigInteger();
		BigInteger temp2 = new BigInteger();
		int product = 0;
		int carry = 0;
		int rem = 0;

		//checks for negative status
		boolean isNegative = first.negative != second.negative;

		DigitNode multiplicant = first.front;
		DigitNode multiplier = second.front;
		int numMultiplicantDigits = first.numDigits;
		int numMultiplierDigits = second.numDigits;

		if (first.numDigits < second.numDigits){
			multiplicant = second.front;
			multiplier = first.front;
			numMultiplicantDigits = second.numDigits;
			numMultiplierDigits = first.numDigits;
		}
		DigitNode originalMultiplicant = multiplicant;
		
		for (int i=0; i < numMultiplierDigits;i++) {
			temp1.front = null;
			temp1.numDigits = 0;				
			for (int k=0; k<i; k++) {
				temp1.front = new DigitNode(0,temp1.front);
				temp1.numDigits++;
			}
			for (int j=0; j<numMultiplicantDigits; j++) {
				product=(multiplicant.digit * multiplier.digit) + carry;
				rem = product%10;
				carry = product/10;
				temp1.front = new DigitNode(rem,temp1.front);  
				temp1.numDigits++;
				if (multiplicant.next!=null) {
					multiplicant=multiplicant.next;
				}
				else {
					break;
				}
			}
			if (carry > 0) {
				temp1.front = new DigitNode(carry,temp1.front);
				temp1.numDigits++;
				carry = 0;
			}
			//reverse temp1 
			for (int r = 0; r < temp1.numDigits; r++) {
				temp2.front = new DigitNode(temp1.front.digit,temp2.front);
				temp2.numDigits++;
				temp1.front = temp1.front.next;
			}
			result = add(result,temp2);
			temp2.front = null;
			temp2.numDigits = 0;
			multiplicant = originalMultiplicant;
			if(multiplier.next != null) {
				multiplier = multiplier.next;
			}
			else {
				break;
			}
		}
		result.negative = isNegative;
		return result; 
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (front == null) {
			return "0";
		}
		String retval = front.digit + "";
		for (DigitNode curr = front.next; curr != null; curr = curr.next) {
				retval = curr.digit + retval;
		}
		
		if (negative) {
			retval = '-' + retval;
		}
		return retval;
	}
}