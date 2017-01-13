package ic.compiler;
import java.util.HashMap;
import java.util.Map;

import ic.ast.AST_Node;

public class IR_SymbolTable {

	private IR_SymbolTable parent; //the scope is inside a parent scope
	private Map<String, IR_Attribute> symbols; //the symbols in the symbol table of the scope
	private Map<AST_Node, IR_SymbolTable> children; //the scopes that are inside my scopes.
	private String className; //in what class are we
	private MethodFrame scopeFrame; //in what frame are we
	
	
	public IR_SymbolTable(IR_SymbolTable parentTable, String className)
	{
		this.parent= parentTable;
		this.className= className;
		this.symbols= new HashMap<String, IR_Attribute>();
		this.children=new HashMap<AST_Node, IR_SymbolTable>();
		this.scopeFrame= parentTable.getFrame();
	}
	
	public IR_SymbolTable(IR_SymbolTable parentTable, String className, MethodFrame currentFrame)
	{
		this.parent= parentTable;
		this.className= className;
		this.symbols= new HashMap<String, IR_Attribute>();
		this.children=new HashMap<AST_Node, IR_SymbolTable>();
		this.scopeFrame= currentFrame;
	}

	public String getClassName() {
		return className;
	}
	
	public MethodFrame getFrame() {
		return scopeFrame;
	}

	public void setClassName(String className) {
		this.className = className;
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
