/** Name: Renkai Ji and Zhuoyi Wang
* CS6301, Project 3
* Section 014
* Due Date: 2 AM, 10/07/13
* Functional description:
* 
* Extend project 2 and add the following features:

	1. Negative numbers
	2. Division operation: quotient (/) and remainder (%)
	3. Square root (just the integer part)

* The numbers will be represented in linked list;
* Each node contains 9 digit number, so the base is 1 billion;
*/

import java.util.*;

public class ArbitraryPrecisionArithmeticNegationDivisionSquareroot {
	
		/**
		 * get input from console and evaluate the expression;
		 * Each line is an arithmetic expression with numbers (in decimal), +, -, *, ^ (exponentiation), and parentheses. 
	Operator precedence: ^, *, {+,-} (same as in programming languages).
		 * Enter 0 will end the program;
		 */
	    public static void main(String[] args) throws Exception {
			// TODO Auto-generated method stub
	       Scanner input = null;
		 
	        while(true) {
	             input = new Scanner(System.in);
	             input.useDelimiter(System.getProperty("line.separator"));
	             System.out.println("Please input your expression(if you want to end, input 0):");
	             String expr = input.next();
		     //press "Enter" without input, continue
	             if (expr.length() == 0) {
	            	 continue;
	             }
		     //check the exit condition
	             else if(expr.length() == 1 && expr.charAt(0) == '0'){
	            	 System.out.println("Bye");
	            	 break;
	             }
	             else {
	                 try {
	                	 ArbitraryNumber result = caculateExpression(expr);
	                         System.out.println(result);
	                 } catch(Exception e) {
	                         System.out.println(e.getMessage());
	                 }
	             }

	         }
	        input.close(); 
	}
	   
	    public static ArbitraryNumber caculateExpression(String ep) throws Exception{
		    //stack1 for storing operands;
		    Stack<ArbitraryNumber> stack1 = new Stack<ArbitraryNumber>(); 
		    //stack2 for storing operators;
		    Stack<String> stack2 = new Stack<String>(); 
		    //get operands and operators;
		    StringTokenizer tokens = new StringTokenizer(ep, "()+-*^/%r",true);
		    //handle negative tokens;
		    ArrayList<String> tokenList = validateTokens(tokens);
		  //  System.out.println(tokenList);
		    LinkedList<String> postfixList = new LinkedList<String>();
		    for(String temp: tokenList) {
		    	//for "+,-", we get left associativity by push "+,-" on top of operators of lower precedence
		    	if (temp.charAt(0) == '+' || temp.charAt(0) == '-'){
		    		while(!stack2.isEmpty()&&(stack2.peek().charAt(0) == '+'||
		    				stack2.peek().charAt(0) == '-'||
		    				stack2.peek().charAt(0) == '*'||
		    				stack2.peek().charAt(0) == '/'||
		    				stack2.peek().charAt(0) == '%'||
		    				stack2.peek().charAt(0) == '^'||
		    				stack2.peek().charAt(0) == 'r' )) {
		    			postfixList.add(stack2.pop());
		    		}
		    		stack2.push(temp);
		    	} 
		    	//for '*' and '/', we get left associativity by push '*' on top of operators of lower precedence
		    	else if (temp.charAt(0) == '*' || temp.charAt(0) == '/'||temp.charAt(0) == '%') {
		    		while(!stack2.isEmpty()&&(stack2.peek().charAt(0) == '*'||
		    				stack2.peek().charAt(0) == '/'||
		    				stack2.peek().charAt(0) == '%'||
		    				stack2.peek().charAt(0) == '^'||
		    				stack2.peek().charAt(0) == 'r' )) {
		    			postfixList.add(stack2.pop());
		    		}
		    		stack2.push(temp);
		    	} 
		    	else if(temp.charAt(0) == '^') {
		    		while(!stack2.isEmpty()&&(stack2.peek().charAt(0) == 'r'))
		    			postfixList.add(stack2.pop());
		    		stack2.push(temp);
		    	}
		    	//for '^', we get right associativity by push '^' on top of operators of lower and equal precedence
		    	else if(temp.charAt(0) == '(' ||
		    			temp.charAt(0) == 'r') {
		    		stack2.push(temp);
		    	} 
		    	//on seeing close parenthesis, pop stack until open parenthesis;
		    	else if (temp.charAt(0) == ')') {
		    		while(!stack2.isEmpty()&&stack2.peek().charAt(0) != '(') {
		    			postfixList.add(stack2.pop());
		    		}
		    		if(stack2.isEmpty()) {
		    			throw new Exception("Unmatched parenthesis!");
		    		}
		    		stack2.pop();	
		    	}
		    	else {
		    		postfixList.add(temp);
		    	}
		    }
		    while(!stack2.isEmpty()) {
		    	postfixList.add(stack2.pop());
		    }
		 //   System.out.println(postfixList);
		    //Evaluate postfix expression
		    HashSet<Character> operatorSet = new HashSet<Character>();
		    operatorSet.add('+');
		    operatorSet.add('-');
		    operatorSet.add('*');
		    operatorSet.add('/');
		    operatorSet.add('%');
		    operatorSet.add('^');
		    operatorSet.add('r');
		    for(String item: postfixList) {
		    	//on seeing operands, push them on stack
		    	if(item.length() > 1||!operatorSet.contains(item.charAt(0))) {
		    		if(item.charAt(0) == '(') {
		    			throw new Exception("Unmatched parenthesis!");
		    		}
		    		else {
		    			stack1.push(new ArbitraryNumber(item));
		    		}
		    	}
		    	//on seeing operators, pop enough operands, apply operation, push output onto stack
		    	else {
		    		performOperation(stack1,item.charAt(0));
		    	}
		    }
		    return stack1.pop();
		}
	    //Generate a list of validate tokens; including handle negative number;
	    public static ArrayList<String> validateTokens(StringTokenizer tokens) {
	    	ArrayList<String> result = new ArrayList<String>();
	    	String currentToken = null;
	    	String previousToken = null;
	    	//using signSet variable to determine whether currentToken is negative.
	    	//if signSet = 1 and currentToken is a number, then currentToken is negative
	    	int signSet = 0;
	    	while(tokens.hasMoreTokens()) {
	    		currentToken = tokens.nextToken().trim();
		    	if(currentToken.equals(""))
		    	    continue;
		    	else if((currentToken.charAt(0)=='-') && 
		    		(previousToken == null || isOperator(previousToken)) ) {
		    		if(signSet==1) {
		    			signSet = 0;
		    		}
		    		else
		    			signSet = 1;
		    	}
		    	else if((currentToken.charAt(0)=='+') && 
			    	(previousToken == null || isOperator(previousToken)) ) {
			    		signSet = 2;
			    }
		    	else if (Character.isDigit(currentToken.charAt(0)) && signSet == 1) {
		    		result.add(currentToken + '-');
		    		previousToken = currentToken;
		    		if(signSet==2) {
		    			signSet = 0;
		    		}
		    		else
		    			signSet = 2;
		    	}
		    	else if (Character.isDigit(currentToken.charAt(0)) && signSet == 2) {
		    		result.add(currentToken);
		    		previousToken = currentToken;
		    		signSet = 0;
		    	}
		    	else {
		    		result.add(currentToken);
		    		previousToken = currentToken;
		    	}
	    	}
	    	
	    	return result;
	    }
	    //To determine whether token is an operator
	    private static boolean isOperator(String token) {
		    char ops = token.charAt(0);
		    if(ops == '+' || ops == '-'|| ops == '*'||ops == '/'||ops == '%'||ops == '^'||ops == 'r'||ops == '('){
		    	return true;
		    }
		    else
		    	return false;
		}

		
		public static void performOperation(Stack<ArbitraryNumber> stack1,
				char operator) throws Exception{
			ArbitraryNumber ex1;
			ArbitraryNumber ex2;
			if (operator == 'r') {
				try {
					ex1 = stack1.pop();
					stack1.push(ex1.rootSquare());
					return;
				} catch(EmptyStackException e) {
					throw new Exception("Syntax Error 2");
				}
			}
				
			try {
				ex1 = stack1.pop();
				ex2 = stack1.pop();
			} catch(EmptyStackException e) {
				throw new Exception("Syntax Error 2");
			}
			
			switch(operator) {
				case '+': 
					stack1.push(ex2.add(ex1));break;
				case '-':
					stack1.push(ex2.substract(ex1));break;
				case '*':
					stack1.push(ex2.multiply(ex1));break;
				case '/':
					stack1.push(ex2.divide(ex1));break;
				case 'r':
					stack1.push(ex2.rootSquare());break;
				case '%':
					stack1.push(ex2.remainder(ex1));break;
				case '^':
					stack1.push(ex2.power(ex1));break;
				default:
					throw new Exception("Syntax error: unexpected operator");
			}
			
		}
	}
	class ArbitraryNumber implements Comparable<ArbitraryNumber> {
		
		private Node header = new Node();
		private Node tail = header;
		private final long NUMBER_BASE = 1000000000L;
		
		public ArbitraryNumber() {
			
		}
		public ArbitraryNumber(String s) throws Exception{
			 
			String s1 = s.trim();
			if (s.length()==0) return;
			int higherBound = s1.length() - 1;
			if(s1.charAt(higherBound) == '-'){
				header.digit = 1;
				higherBound = s1.length() - 2;
			}
			else if(s1.charAt(higherBound) == '+'){
				header.digit = 0;
				higherBound = s1.length() - 2;
			}
			else
				header.digit = 0;
			Node current = header;
			// using j to check # of digits <= 9 each node;
			StringBuffer sb = new StringBuffer();
			for(int i = higherBound; i >= 0; i--) {
				
				if (s1.charAt(i) == 32) 
					continue;
				else if (s1.charAt(i)<48 || s1.charAt(i) > 57) {
					throw new Exception("Syntax error 1: Unexpected character in the expression");
				}
				sb.append(s1.charAt(i));
				if(sb.length() == 9) {
					sb.reverse();
					Node node = new Node(Long.parseLong(sb.toString()));
					current.next = node;
					current = current.next;
					sb.delete(0, 9);
				}
			}
			if(sb.length()!=0) {
				sb.reverse();
				Node node = new Node(Long.parseLong(sb.toString()));
				current.next = node;
				current = current.next;
			}
			tail = current;
			tailor();
		}
		/*this.divide(n);
		 * @param this is the dividend;
		 * @param n is the divisor;
		 * @return the quotient of this/n;
		 */
		public ArbitraryNumber divide(ArbitraryNumber n) throws Exception{
			if(n.header == n.tail) {
				throw new Exception("Divide by zero exception!");
			}
			String dividend = this.toString();
			String divisor = n.toString();
			ArbitraryNumber maxLong = new ArbitraryNumber(Long.toString(Long.MAX_VALUE));
			ArbitraryNumber minLong = new ArbitraryNumber(Long.toString(Long.MIN_VALUE).substring(1) + '-');
			if(n.compareTo(maxLong)<0 && n.compareTo(minLong)>0) {
				return new ArbitraryNumber(divide1(dividend,divisor));
			}
			else 
				return this.divide2(n);
			
		}
		/*Internal method to implement small divisor( divisor could be represented as long type)
		 *@param d1 is the dividend;
		 *@param d2 is the divisor;
		 *@return the result d1/d2, represented as a string;
		 */
		private String divide1(String d1, String d2) throws Exception{
			StringBuffer quotient = new StringBuffer(0);
			String dividend = d1;
			long divisor = Long.parseLong(d2); 
			if(d1.charAt(0) == '-') {
				quotient.append('-');
				dividend = d1.substring(1);
				if(d2.charAt(0) == '-') {
					quotient.deleteCharAt(0);
					divisor = Long.parseLong(d2.substring(1));
				}
			}
			else {
				if(d2.charAt(0) == '-') {
					quotient.append('-');
					divisor = Long.parseLong(d2.substring(1));
				}
			}
			int dividendLength = d1.length();
			StringBuffer processedDividend = new StringBuffer();
			long temp = 0;
			int i = 0;
			//long division;
			while(i<dividendLength){
				processedDividend.append(dividend.charAt(i));
				temp = Long.parseLong(processedDividend.toString());
				while(i + 1<dividendLength && temp < divisor) {	
					i++;
					quotient.append(0);
					processedDividend.append(dividend.charAt(i));
					temp = Long.parseLong(processedDividend.toString());
					
				}
				if(i+1 == dividendLength && temp < divisor) {
					quotient.append(0);
				}
				long j;
				if (temp  >= divisor) {
					for(int k = 1; k<10; k++) {
						  j = divisor*k;
						  if(j <= temp && (j+divisor)>temp) {
							  ArbitraryNumber m = new ArbitraryNumber(processedDividend.toString()).substract(
									  new ArbitraryNumber(Long.toString(j)));
						//	  temp = Long.parseLong(m.toString());
							  processedDividend.replace(0, processedDividend.length(), m.toString());
							  quotient.append(k);
							  break;
						  }
					}
				}
				i++;
			}
			
			return quotient.toString();
		}
		/*Internal method to implement big divisor( divisor could not be represented as long type)
		 *@param this is the dividend;
		 *@param n is the divisor;
		 *@return the result this/n, an arbitrary number;
		 */
		private ArbitraryNumber divide1(ArbitraryNumber n) throws Exception{
			Node dividendHeader = null;
			Node current = this.header.next;
			while(current !=null) {
				Node temp = new Node(current.digit);
				temp.next = dividendHeader;
				dividendHeader = temp;
				current = current.next;
			}
			current = dividendHeader;
			ArbitraryNumber quotient = new ArbitraryNumber();
			ArbitraryNumber processedDividend = new ArbitraryNumber();
			while(current != null) {
				processedDividend.insertFront(current.digit);
				while(current.next != null&&processedDividend.compareTo(n) < 0) {
					current = current.next;
					quotient.insertFront(0);
					processedDividend.insertFront(current.digit);
				}
				ArbitraryNumber j = null;
				if (processedDividend.compareTo(n) >= 0) {
					for(int i = 1; i<1000000000; i++) {
						  j = n.multiply(i);
						  if(j.compareTo(processedDividend)>0) {
							  j = j.substract(n);
							  processedDividend = processedDividend.substract(j);
							  i--;
							  quotient.insertFront(i);
							  break;
						  }
					}		
					current =current.next;	  
				}
			}
			quotient.tailor();
			return quotient;
			
		}
		/*Another internal method to implement big divisor( divisor could not be represented as long type)
		 *@param this is the dividend;
		 *@param n is the divisor;
		 *@return the result this/n, an arbitrary number;
		 */
		public ArbitraryNumber divide2(ArbitraryNumber n) throws Exception{	
			ArbitraryNumber quotient = new ArbitraryNumber("1");
			ArbitraryNumber dividend = new ArbitraryNumber();
			ArbitraryNumber divisor = new ArbitraryNumber();
			dividend = this;
			divisor = n;
			int dividendLength = dividend.toString().length();
			int divisorLength = divisor.toString().length();
			if(dividendLength - divisorLength <= 2){
				return dividend.divide1(n);
			} else{
				ArbitraryNumber ten = new ArbitraryNumber("10");
				ArbitraryNumber tempDivisor = new ArbitraryNumber();
				tempDivisor = divisor;
				for(int i = 0; i < dividendLength - divisorLength - 1; i++){
					tempDivisor = tempDivisor.multiply(ten);
					quotient = quotient.multiply(ten);
				}
				ArbitraryNumber tempQuotient = new ArbitraryNumber();
				tempQuotient = dividend.divide1(tempDivisor);
				quotient = tempQuotient.multiply(quotient);
				ArbitraryNumber tempDividend = new ArbitraryNumber();
				tempDividend = dividend.substract(tempDivisor.multiply(tempQuotient));
				quotient = quotient.add(tempDividend.divide(divisor));
			}
			
			return quotient;
		}
		/*Method to implement remainder;
		 *@param this is the dividend;
		 *@param n is the divisor;
		 *@return the result this%n, an arbitrary number;
		 */
		public ArbitraryNumber remainder(ArbitraryNumber n) throws Exception{
			if(n.header == n.tail) {
				throw new Exception("remainder by zero exception!");
			}
			ArbitraryNumber remainder = null;
			ArbitraryNumber dividend = this;
			ArbitraryNumber divisor = n;
			remainder = dividend.substract(n.multiply(dividend.divide(divisor)));
			return remainder;
		}
		
		/* Newton-Raphson method. g1 = (g0 + a/g0)/2
		 * @param 'this' is the radicand;
		 * @return the closest integer to the root square of the radicand;
		 */
		public ArbitraryNumber rootSquare() throws Exception{
			if(this.header.digit ==1) {
				throw new Exception("No root square arithmetic for negative number");
			}
			ArbitraryNumber g0 = null;//base number
			ArbitraryNumber g1 = null; //base number
			ArbitraryNumber two = new ArbitraryNumber("2");
			g0 = this.getInitialg();
			g1 = g0.add(this.divide(g0));
			g1 = g1.divide(two);
			int i = 0;
			while(g0.compareTo(g1)!=0 && i<1000){
				g0 = g1;
				g1 = g0.add(this.divide(g0));
				g1 = g1.divide(two);
				i++;
			}
			//return the result;
			return g1;
			
		}
		//get the length of input number
		//format the number into S = a * 10^2n (here S defines the radicand);
		//then we can get rSqr(S) = rSqr(a) * 10^n 
		//a should belongs to [1,100)
		//if a falls into [1,10), then let g0 = 2 * 10^n
		//if a falls into [10,100), then let g0 = 6 * 10^n
		private ArbitraryNumber getInitialg() throws Exception{
			
			StringBuffer g0 = new StringBuffer();
			String temp = this.toString();
			int length = temp.length();
			double n;		
			if(length % 2 == 0){	
				n = (length - 2)/2;
				g0.append(6);
				for(int i = 0; i <n; i++) {
					g0.append(0);
				}
			} else{
				n = (length - 1)/2;
				g0.append(3);
				for(int i = 0; i <n; i++) {
					g0.append(0);
				}
			}
			return new ArbitraryNumber(g0.toString());
		}
		
		public ArbitraryNumber add(ArbitraryNumber n){
			ArbitraryNumber n3 = new ArbitraryNumber();
			if(this.header.digit == n.header.digit) {
				Node p1 = this.header.next;
				Node p2 = n.header.next;
				long carry = 0;
				long temp;
			
				while(p1 != null && p2!=null) {
					if(carry != 0) {
						temp = p1.digit + p2.digit + carry; 
					}
					else {
						temp = p1.digit + p2.digit;
					}
					n3.append(temp % NUMBER_BASE);
					if (temp >= NUMBER_BASE)
						carry = 1;
					else
						carry = 0;
					p1 = p1.next;
					p2 = p2.next;
				}
			
				while(p1 != null) {
					if(carry != 0) {
						temp = p1.digit + carry; 
					}
					else {
						temp = p1.digit;
					}
					n3.append(temp % NUMBER_BASE);
					if (temp >= NUMBER_BASE)
						carry = 1;
					else
						carry = 0;
					p1 = p1.next;
				}
			
				while(p2 != null) {
					if(carry != 0) {
						temp = p2.digit + carry; 
					}
					else {
						temp = p2.digit;
					}
					n3.append(temp % NUMBER_BASE);
					if (temp >= NUMBER_BASE)
						carry = 1;
					else
						carry = 0;
					p2 = p2.next;
				}
			
				if(carry != 0) {
					n3.append(carry); 
				}
				n3.header.digit = this.header.digit;
				return n3;
				}
			else {
				if(this.header.digit == 1 && n.header.digit ==0 ) {
					this.header.digit = 0;
					n3 = n.substract(this);
				}
				else{
					n.header.digit = 0;
					n3 = this.substract(n);
				}
				return n3;
			}
		}
		
		public ArbitraryNumber substract(ArbitraryNumber n) {
			ArbitraryNumber n3 = new ArbitraryNumber();
			if(this.header.digit == n.header.digit) {
				if(this.header.digit == 0 &&this.compareTo(n) >=0) {
					Node p1 = this.header.next;
					Node p2 = n.header.next;
					long borrow = 0;
					long temp;
					long minuend;
					while(p1 != null && p2!=null) {
						minuend = p1.digit - borrow;
						if((minuend - p2.digit) >= 0) {
							temp = minuend - p2.digit;
							borrow = 0;
						}
						else {
							temp = NUMBER_BASE + minuend - p2.digit;
							borrow = 1;
						}
						n3.append(temp);
						p1 = p1.next;
						p2 = p2.next;
					}
					while(p1!=null) {
						minuend = p1.digit - borrow;
						if(minuend >= 0) {
							temp = minuend ;
							borrow = 0;
						}
						else {
							temp = NUMBER_BASE + minuend;
							borrow = 1;
						}
						n3.append(temp);
						p1 = p1.next;
					}
					
					/*if(p2!=null || borrow ==1){
						throw new Exception("Negative numbers are not supported."); // negative result;
					}*/
					n3.header.digit = 0;
				}
				else if(this.header.digit == 1 && this.compareTo(n) >=0){
					n.header.digit = 0;
					this.header.digit = 0;
					n3 = n.substract(this);
					n3.header.digit = 0;
				}
				else if (this.header.digit == 0 && this.compareTo(n) <0) {
						n3 = n.substract(this);
						n3.header.digit = 1;
					}
				else {
						n.header.digit = 0;
						this.header.digit = 0;
						n3 = n.substract(this);
						n3.header.digit = 1;
					}
				n3.tailor();
				return n3;
			}
			else {
				if(this.header.digit == 1){
					n.header.digit =1;
					n3 = this.add(n);
				}
				
				else{
					n.header.digit = 0;
					n3 =  this.add(n);
				}
				return n3;
			}
		}
		
		public ArbitraryNumber power(ArbitraryNumber n) throws Exception {
			if(n.compareTo(new ArbitraryNumber())<=0) {
				return new ArbitraryNumber();
			}
			ArbitraryNumber maxLong = new ArbitraryNumber(Long.toString(Long.MAX_VALUE));
			if(n.compareTo(maxLong)>0)
			{
				ArbitraryNumber n1 = new ArbitraryNumber("1");
				ArbitraryNumber i = new ArbitraryNumber();
				for(; i.compareTo(n) < 0; i.increaseByOne()) {
					n1 = n1.multiply(this);
				}
				return n1;
			}
			else {
				return power(this, Long.parseLong(n.toString())); 
			}
				
		}

		private ArbitraryNumber power(ArbitraryNumber n, long p) {
			if(p==1) {
				return n;
			}
			if(p%2==0) {
				return power(n.multiply(n), p/2);
			}
			else {
				return power(n.multiply(n), p/2).multiply(n);
			}
		}
		public ArbitraryNumber multiply(int n) throws Exception {
			return this.multiply(new ArbitraryNumber(Integer.toString(n)));
		}
		public ArbitraryNumber multiply(ArbitraryNumber n) {
			ArbitraryNumber n3 = new ArbitraryNumber();
			Node p1 = this.header.next;
			Node p2 = n.header.next;
			long number = 0;
			while(p2 != null) {
				long carry = 0;
				ArbitraryNumber temp = new ArbitraryNumber();
				while(p1 != null) {
					temp.append(((p1.digit * p2.digit)% NUMBER_BASE) + carry);
					carry = p1.digit * p2.digit / NUMBER_BASE;
					p1 = p1.next;
				}
				if(carry != 0){
					temp.append(carry);
				}
				
				temp.shitLeft(number);
				n3 = n3.add(temp);
				p2 = p2.next;
				p1 = this.header.next;
				number++;
			}
			if((this.header.digit ^ n.header.digit) == 1) {
				n3.header.digit = 1;
			}
			return n3;
		}
		private ArbitraryNumber increaseByOne() {
			/* this private method is an auxiliary function called by power method;*/
			Node current = header.next;
			current.digit += 1;
			while(current.next !=null && current.digit == NUMBER_BASE ) { 
				current.digit = 0;
				current = current.next;
				current.digit += 1;
			}
			if(current.next ==null && current.digit == NUMBER_BASE) {
				current.digit = 0;
				current.next = new Node((byte)1);
			}
			return this;
		}
		private ArbitraryNumber shitLeft(long n) {
			/* this private method is an auxiliary function called by multiply method; 
			 * add a zero to units digit. */
			long i = 0;
			while(i < n) {
				Node temp = new Node(0);
				temp.next = header.next;
				header.next = temp;
				i++;
			}
			return this;
		}
		@Override
		public int compareTo(ArbitraryNumber n) {
			String s1 = this.toString();
			String s2 = n.toString();
			if(this.header.digit > n.header.digit) {
				return -1;
			}
			else if (this.header.digit < n.header.digit) {
				return 1;
			}
			else if (this.header.digit == n.header.digit && this.header.digit == 0) {
				if(s1.length() > s2.length()) {
					return 1;
				}
				else if(s1.length() < s2.length()) {
					return -1;
				}
				else
					return s1.compareTo(s2);
			}
			else {
				if(s1.length() < s2.length()) {
					return 1;
				}
				else if(s1.length() > s2.length()) {
					return -1;
				}
				else {
					if(s1.compareTo(s2) > 0)
						return -1;
					else if(s1.compareTo(s2) <0) {
						return 1;
					}
					else
						return 0;
				}
			}
		}
		private ArbitraryNumber insertFront(long b) {
			
			Node p = new Node(b);
			p.next = header.next;
			header.next = p;
			if(p.next == null) {
				tail = p;
			}
			return this;
		}
		private ArbitraryNumber append(long b) {
			tail.next = new Node(b);
			tail = tail.next;
			return this;
		}
		
		
		private void tailor() {
			/* this method is used to deleted meaningless most signifant zeros,
			 * ex.: 0000019929 to 19929*/
			Node r = null;
			Node current = this.header;
			int n = 0;
			while(current.next != null) {
				if(current.next.digit == 0 && n==0) {
					r = current;
					n++;
				}
				else if(current.next.digit == 0 && n!=0) {
					n++;
				}
				else {
					n = 0;
					r = null;
				}
				current = current.next;
			}
			if(r!=null){
				tail = r;
				r.next = null;
			}
		}
		
		
		@Override
		public String toString(){
			StringBuffer sb = new StringBuffer();//using StringBuffer object to store temp output
			if(tail == header) return "0";
		       // reverse the digit to output the number	
			Node printHeader = null;
			Node current = header.next;
			while(current !=null) {
				Node temp = new Node(current.digit);
				temp.next = printHeader;
				printHeader = temp;
				current = current.next;
			}
			int i = 0;
			if(header.digit == 1)
				sb.append('-');
			while(printHeader !=null) {
				if(i == 0) {
					sb.append(printHeader.digit);
				}
				else
					sb.append(String.format("%9s",printHeader.digit).replace(' ' ,'0'));
				printHeader = printHeader.next;
				i++;
			}
			return sb.toString();
		}

		private class Node {
			/*using long type to store 9 digits of arbitrary number, Base: 1 billion
			 *from least significant digit to most significant digit;*/
			long digit; 
			Node next;
			//two overloading constructor
			public Node() {
				digit = 0;
				next = null;
			}
			public Node(long d) {
				digit = d;
				next = null;
			}
		}
	}



