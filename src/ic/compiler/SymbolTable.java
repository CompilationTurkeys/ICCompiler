package ic.compiler;
import java.util.HashMap;
import java.util.Map;

import ic.ast.AST_Node;

public class SymbolTable {

	private SymbolTable parent; //the scope is inside a parent scope
	private Map<String, Attribute> symbols; //the symbols in the symbol table of the scope
	//atributes are relevant things for the tests (type, isInitialized...)
	private String className; //in what class are we
	private Map<AST_Node, SymbolTable> children; //the scopes that are inside my scopes.
	
	
	public SymbolTable(SymbolTable parentTable, String className)
	{
		this.parent= parentTable;
		this.className= className;
		this.symbols= new HashMap<String,Attribute>();
		this.children=new HashMap<AST_Node, SymbolTable>();
	}


	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void setSymbols(Map<String, Attribute> symbols) {
		this.symbols = symbols;
	}

	public SymbolTable getParentTable() {
		return parent;
	}
	
	public Map<String, Attribute> getSymbols() {
		return symbols;
	}

	public Map<AST_Node, SymbolTable> getChildren() {
		return children;
	}

}
