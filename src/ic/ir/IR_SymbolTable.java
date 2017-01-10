package ic.ir;
import java.util.HashMap;
import java.util.Map;

import ic.ast.AST_Node;

public class IR_SymbolTable {

	private IR_SymbolTable parent; //the scope is inside a parent scope
	private Map<String, IR_Attribute> symbols; //the symbols in the symbol table of the scope
	//atributes are relevant things for the tests (type, isInitialized...)
	private Map<AST_Node, IR_SymbolTable> children; //the scopes that are inside my scopes.
	
	public IR_SymbolTable(IR_SymbolTable parentTable)
	{
		this.parent= parentTable;
		this.symbols= new HashMap<String,IR_Attribute>();
		this.children=new HashMap<AST_Node, IR_SymbolTable>();
	}

	public void setSymbols(Map<String, IR_Attribute> symbols) {
		this.symbols = symbols;
	}

	public IR_SymbolTable getParentTable() {
		return parent;
	}
	
	public Map<String, IR_Attribute> getSymbols() {
		return symbols;
	}

	public Map<AST_Node, IR_SymbolTable> getChildren() {
		return children;
	}

}
