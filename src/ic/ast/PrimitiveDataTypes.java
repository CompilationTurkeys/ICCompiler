package ic.ast;

public enum PrimitiveDataTypes {
	INT(0, "int"),
	STRING(null, "string"),
	VOID(null, "void");
	
	private String value;
	private String defVal;

	private PrimitiveDataTypes(String value, String defVal) {
		this.value = value;
		this.defVal = defVal;
	}
	
	public String getValue() {
		return value;
	}

	public Object getDefaultValue() {
		return defVal;
	}
}
