package ic.ast;

public enum PrimitiveDataTypes {
	INT(0, "int"),
	STRING(null, "string"),
	VOID(null, "void");
	
	private String name;
	private Object defVal;

	private PrimitiveDataTypes(Object defVal, String name) {
		this.name = name;
		this.defVal = defVal;
	}
	
	public String getName() {
		return name;
	}

	public Object getDefaultValue() {
		return defVal;
	}
}
