public class Item {
	char symbol;
	int value;
	int state;
	
	Item(){
		this.symbol = ' ';
		this.state = 0;
		this.value = -1;
	}
	Item(char symbol){
		this.symbol = symbol;
		this.value = -1;
		this.state = 0;
	}
	Item(char symbol, int value){
		this.symbol = symbol;
		this.value = value;
		this.state = 0;
	}
	Item(int state, char symbol){
		this.symbol = symbol;
		this.state = state;
		this.value = -1;
	}
	
	Item(char symbol, int state, int value){
		this.symbol = symbol;
		this.state = state;
		this.value = value;
	}

	public char getSymbol() {
		return symbol;
	}

	public void setSymbol(char symbol) {
		this.symbol = symbol;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
	
	public void print() {
		System.out.println("Symbol" + symbol);
		System.out.println("State" + state);
		System.out.println("value" + value);
	}
}
