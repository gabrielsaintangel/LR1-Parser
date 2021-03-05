import java.util.LinkedList;

public class LR1 
{ 
	static LinkedList<Item> stack = new LinkedList<Item>();
	static LinkedList<Item> inputQueue;
	static boolean isValid = true;
	static boolean done = false;

public static void main(String[] args) throws IOException 
{
	
	if(args.length == 0) {
		System.out.println("You need to provide an argument, exiting");
		System.exit(0);
	}
	String input = args[0];
	input.replaceAll("\\s+","");// remove all whitespace
	 // remove whitespace from input
	inputQueue = parseInput(input); // remove 
	stack.add(new Item(0, '$')); // add the empty element into the stack so it knows when its done/knows previous state
	print();
	while(!done && isValid) {
		parse();
		if(done) break;
		print();
	}
	
	if(isValid) {
		System.out.println("\nValid Expression, value = " + stack.peekLast().getValue());
	}
	else {
		System.out.println("\nExecution stopped.  Not a valid expression");
	}
}

// puts front of queue in the stack, changes state to match rules
public static void shift(int state) {
	Item toPush = inputQueue.remove();
	toPush.setState(state);
	stack.add(toPush);
}

public static int getReducedState(int prevState, char curSymbol) {
	int returnVal = -1;
	if(prevState == 0) { 
		
		if(curSymbol == 'E') {
			returnVal = 1;
		}
		else if(curSymbol == 'T') {
			returnVal = 2;
		}
		else if(curSymbol == 'F') {
			returnVal = 3;
		}
	}
	else if(prevState == 4) {
		if(curSymbol == 'E') {
			returnVal = 8;
		}
		else if(curSymbol == 'T') {
			returnVal = 2;
		}
		else if(curSymbol == 'F') {
			returnVal = 3;
		}
	}
	else if(prevState == 6) {
		if(curSymbol == 'T') {
			returnVal = 9;
		}
		else if(curSymbol == 'F') {
			returnVal = 3;
		}
	}
	else if(prevState == 7) {
		if(curSymbol == 'F') {
			returnVal = 10;
		}
	}
	if(returnVal == -1) {
		isValid = false;
		done = true;
		System.out.println("Bro you dun goofed");
	}
	return returnVal;
}

public static void reduce(int rule) {
	//System.out.println("reducing " + rule);
	Item curr = stack.pollLast();
	if(rule == 1) {
	// rule 1 and 2 combined
		// E -> E + T
		// E -> E - T
		
		Item next = stack.pollLast(); // get next item for calculation, this should have the operator
		Item nextNext = stack.pollLast(); // get one more item, this should be E
		int prevState = stack.peekLast().getState();
		if(nextNext.getSymbol() != 'E') {
			isValid = false;
			System.out.println("Error");
		}
		//rule 1
		if(next.getSymbol() == '+') {
			int subtotal = nextNext.getValue() + curr.getValue();
			Item reduction = new Item('E', curr.getState(), subtotal);
			reduction.setState(getReducedState(prevState, 'E'));
			stack.add(reduction);
			
		}
		// rule 2
		else if(next.getSymbol() == '-') {
			int subtotal =  nextNext.getValue() - curr.getValue();
			Item reduction = new Item('E', curr.getState(), subtotal);
			reduction.setState(getReducedState(prevState, reduction.getSymbol()));
			stack.add(reduction);
		}
		//error
		else {
			isValid = false;
			done = true;
			//System.out.println("Error in reduce at 1");
		}
	}
	// rule 3
	// E-> T
	else if(rule == 3) {
		int prevState = stack.peekLast().getState();
		curr.setSymbol('E');
		curr.setState(getReducedState(prevState, curr.getSymbol()));
		stack.add(curr);
	}
	// 4 and 5 combined
	//T -> T * F
	//T -> T / F
	else if(rule == 4) {
		Item next = stack.pollLast(); // get next item for calculation, this should have the operator
		Item nextNext = stack.pollLast(); // get one more item, this should be T
		int prevState = stack.peekLast().getState();
		if(nextNext.getSymbol() != 'T') {
			isValid = false;
			}
		//rule 4
		if(next.getSymbol() == '*') {
			int subtotal = (nextNext.getValue() * curr.getValue());
			Item reduction = new Item('T', curr.getState(), subtotal);
			reduction.setState(getReducedState(prevState, reduction.getSymbol()));
			stack.add(reduction);
		}
		// rule 5
		else if(next.getSymbol() == '/') {
			int subtotal = (nextNext.getValue() / curr.getValue() );
			Item reduction = new Item('T', curr.getState(), subtotal);
			reduction.setState(getReducedState(prevState, reduction.getSymbol()));
			stack.add(reduction);
		}
		//error
		else {
			isValid = false;
			done = true;
			//System.out.println("Error in reduce at 4 5");
		}
	}
	else if(rule == 6) {
		int prevState = stack.peekLast().getState();
		curr.setSymbol('T');
		curr.setState(getReducedState(prevState, curr.getSymbol()));
		stack.add(curr);
	}
	else if(rule == 7) {
		Item next = stack.pollLast(); // get next item for calculation, this should have the operator
		Item nextNext = stack.pollLast(); // get one more item, this should be T
		int prevState = stack.peekLast().getState();
		//rule 4
		if(nextNext.getSymbol() == '(' && next.getSymbol() == 'E') {
			Item reduction = new Item('F', curr.getState(), next.getValue());
			reduction.setState(getReducedState(prevState, reduction.getSymbol()));
			stack.add(reduction);
		}
		//error
		else {
			isValid = false;
			done = true;
		}
	}
	else if(rule == 8) {
		int prevState = stack.peekLast().getState();
		curr.setSymbol('F');
		curr.setState(getReducedState(prevState, curr.getSymbol()));
		stack.add(curr);
	}
	else {
		isValid = false;
		done = true;
	}
	
	
}

public static void parse() {
	//System.out.println(stack.size());
	//Item stackItem = stack.pollLast();
	int curState = stack.peekLast().getState();
	char curSymbol = inputQueue.peek().getSymbol();
	if(curState == 0) {
		if(curSymbol == 'n') {
			shift(5);
		}
		else if(curSymbol == '(') {
			shift(4);
		}
		else {
			isValid = false;
			done = true;
		}
		
	}
	else if(curState == 1) {
		switch(curSymbol) {
		case '+':
		case '-':
			shift(6);
			break;
		case '$':
			done = true;
			break;
		default:
			isValid = false;
			done = true;
		}
	}
	else if(curState == 2) {
		switch(curSymbol) {
		case '+':
		case '-':
		case ')':
		case '$':
			reduce(3);
			break;
		case '*':
		case '/':
			shift(7);
			break;
		default:
			isValid = false;
			done = true;
	}
	}
	else if(curState == 3) {
		switch(curSymbol) {
		case '+':
		case '-':
		case '*':
		case '/':
		case ')':
		case '$':
			reduce(6);
			break;
		default:
			isValid = false;
			done = true;
	}
	}
	
	else if(curState == 4) {
		switch(curSymbol) {
		case 'n':
			shift(5);
			break;
		case '(':
			shift(4);
			break;
		default:
			isValid = false;
			done = true;
	}
	}
	else if(curState == 5) {
		//System.out.println(curSymbol == '+');
		switch(curSymbol) {
		case '+':
		case '-':
		case '*':
		case '/':
		case ')':
		case '$':
			reduce(8);
			break;
		default:
			isValid = false;
			done = true;
	}
	}
	else if(curState == 6) {
		switch(curSymbol) {
		case 'n':
			shift(5);
			break;
		case '(':
			shift(4);
			break;
		default:
			isValid = false;
			done = true;
	}
	}
	else if(curState == 7) {
		switch(curSymbol) {
		case 'n':
			shift(5);
			break;
		case '(':
			shift(4);
			break;
		default:
			isValid = false;
			done = true;
	}
		
	}
	else if(curState == 8) {
		switch(curSymbol) {
		case '+':
		case '-':
			shift(6);
			break;
		case ')':
			shift(11);
			break;
		default:
			isValid = false;
			done = true;
	}
		
		
	}
	else if(curState == 9) {
		
		switch(curSymbol) {
		case '+':
		case '-':
		case ')':
		case '$':
			reduce(1);
			break;
		case '*':
		case '/':
			shift(7);
			break;
		default:
			isValid = false;
			done = true;
	}
	}
	else if(curState == 10) {
		// combining rule 4 and 5 and handling them when I get to the reduce function
		switch(curSymbol) {
		case '+':
		case '-':
		case '*':
		case '/':
		case ')':
		case '$':
			reduce(4);
			break;
		default:
			isValid = false;
			done = true;
	}
	}
	else if(curState == 11) {
		// combining rule 1 and 2 when passing to reduce function, going to handle calculations and stuff there
		switch(curSymbol) {
		case '+':
		case '-':
		case '*':
		case '/':
		case ')':
		case '$':
			reduce(7);
			break;
		default:
			isValid = false;
			done = true;
	}
	}
	else {
		isValid = false;
		done = true;
	}
		
}

public static LinkedList<Item> parseInput(String input){
	String currentNumber = "";
	Item newItem;
	// Using a linked list as queue to for leftmost
	LinkedList<Item> tokenizedInput = new LinkedList<Item>();
	for (int i = 0; i < input.length(); i++) {
		char cur = input.charAt(i);
		// if the current character is a number or a decimal, its part of a number
		// add it to the current string
		if (Character.isDigit(cur) || cur == '.') {
			currentNumber += cur;
		}
		// if the current character is not a number or a '.', but the length
		// currentNumber > 0, put currentNumber in the arrayList
		else if (!Character.isDigit(cur) && cur != '.' && currentNumber.length() > 0) {
			if(tryParseInt(currentNumber)) {
				newItem = new Item('n', Integer.parseInt(currentNumber));
				tokenizedInput.add(newItem);
				currentNumber = "";
				newItem = new Item(cur);
				tokenizedInput.add(newItem);
			}
			else {
				System.out.println("Error with input " + currentNumber);
				System.exit(0);
			}
		}
		// this catches ( ) and operators that follow
		else{
			newItem = new Item(input.charAt(i));
			tokenizedInput.add(newItem);
		}
	}
	// if the currentNumber variable is not empty, push it into the list
	if (currentNumber.length() > 0) {
		newItem = new Item('n', Integer.parseInt(currentNumber));
		tokenizedInput.add(newItem); // add it to the list
	}
	newItem = new Item('$');
	tokenizedInput.add(newItem); // add it to the end of the list
//	for(int i = 0; i < tokenizedInput.size(); i++) {
//		System.out.println(tokenizedInput.get(i).getValue());
//	}
	return tokenizedInput;
	
	
}

// parses the int and verifies that it is less than 
static boolean tryParseInt(String value) {  
    try {  
        int temp = Integer.parseInt(value);  
        if(temp >= 0 && temp <= 32767) {
        	return true;
        }
        return false;
          
     } catch (NumberFormatException e) {  
        return false;  
     }  
}

static void print() {
	System.out.print("Stack:[");
	for(int i = 0; i < stack.size(); i++) {
		Item cur = stack.get(i);
		if(cur.getValue() == -1) {
			System.out.print("(" + cur.getSymbol() + ":" + cur.getState() + ")");
		}
		else
		{
			System.out.print("(" + cur.getSymbol() + "=" + cur.getValue() + ":" + cur.getState() + ")" );
		}
		if(i + 1 != stack.size()) System.out.print(" ");
	}
	System.out.print("]     ");
	System.out.print("Input Queue:[");
	for(int i = 0; i < inputQueue.size(); i++) {
		Item cur = inputQueue.get(i);
		if(cur.getValue() == -1) {
			System.out.print(inputQueue.get(i).getSymbol());
		}
		else {
			System.out.print(inputQueue.get(i).getValue());
		}
		if(i + 1 != inputQueue.size()) System.out.print(" ");
	}
	System.out.print("]\n");
}

}
