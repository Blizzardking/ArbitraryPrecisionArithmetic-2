/* 
* Author: Renkai Ji
* CS6301, Project 3
* Section 014
*/



I. Purpose
----------

 
 Extend project 2 and add the following features:

	1. Negative numbers
	2. Division operation: quotient (/) and remainder (%)
	3. Square root (just the integer part)

 The numbers will be represented in linked list;
 Each node contains 9 digit number, so the base is 1 billion;

II.Description of our program
   1. I adopt the same convention from python in terms of handling negative. So any odd number of negative sign, with or withought parentheses, before one number means negative. And any even number of  negative sign or any number of plus sign will mean positive. so 2--3 = 2 - (-3) and 2 ---3 = 2 -(-(-3)). This is very convinient so that you don't need put every parentheses around negative number.
   2. Our program could handle a far-range of arbitrary precision interger division at a very fast speed. No matter how large the dividend is, you can get the result promptly.
   3. By Newton-Raphson method and a clever way to set up the initial estimate, our program could calculate out the closest integer to the root square of any arbitrary radicand. Notably, the speed for very large radicand is decreasing since it use multiple division.

III. File list

--------------
ArbitraryPrecisionArithmeticNegationDivisionSquareroot.java



IV. Compiling and Executing on command line
---------------------------------------------

To compile, run:
javac ArbitraryPrecisionArithmeticNegationDivisionSquareroot.java

To execute, run:
java ArbitraryPrecisionArithmeticNegationDivisionSquareroot
