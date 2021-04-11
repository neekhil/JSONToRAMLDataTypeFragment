package main;

public class DTFragmentBuilder {

	private StringBuilder result = new StringBuilder();
	private int spaceIndent = 0; //space counter
	
	public StringBuilder getResult() {
		return result;
	}
	
	public DTFragmentBuilder() {
		result.append("#%RAML 1.0 DataType\n\n");
	}
	
	/**
	 * Add spaces at every newline for indentation
	 */
	private void indent() {
		for(int spaces = 0; spaces < spaceIndent; spaces++)
			result.append(" ");
	}
	
	public void backIndent() {
		spaceIndent -= 2;
	}	

	public void addObject() {
		indent();
		result.append("type: object \n");
		indent();
		result.append("properties:\n");
		spaceIndent += 2;
	}
	
	public void addArray() {
		indent();
		result.append("type: array \n");
		indent();
		result.append("items: \n");
		spaceIndent += 2;
	}

	public void addKeyName(String keyName) {
		indent();
		result.append(keyName + ": \n");
		spaceIndent += 2;
	}

	public void addString() {
		indent();
		result.append("type: string\n");
		backIndent();
	}

	public void addNumber() {
		indent();
		result.append("type: number\n");
		backIndent();
	}

	public void addBoolean() {
		indent();
		result.append("type: boolean\n");
		backIndent();
	}

	public void addNull() {
		indent();
		result.append("type: nil | string\n");
		backIndent();
	}	
}
