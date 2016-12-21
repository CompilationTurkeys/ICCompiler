package AST;

public enum PrimitiveDataTypes {
	INT("int"),
	STRING("string"),
	VOID("void");
	
	private String value;
	
	private PrimitiveDataTypes(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
