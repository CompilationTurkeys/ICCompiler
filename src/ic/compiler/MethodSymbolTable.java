package ic.compiler;

public class MethodSymbolTable extends SymbolTable {

	private String methodName;

	public MethodSymbolTable(SymbolTable parent, String name) {
		super(parent, parent.getClassName());
		this.methodName = name;
	}
	
	public String getMethodName() {
		return methodName;
	}

}
