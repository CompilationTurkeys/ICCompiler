import java.util.HashMap;
import java.util.Map;

import AST.AST_Node;

public class ScopeNode {

	private ScopeNode parentScope; //the scope is inside a parent scope
	private Map<String,Attributes> symbols; //the symbols in the symbol table of the scope
	//atributes are relevant things for the tests (type, isInitialized...)
	private String className; //in what class are we
	private Map<AST_Node, ScopeNode> children; //the scopes that are inside my scopes.
	
	
	public ScopeNode(ScopeNode parent_scope, String class_name)
	{
		this.parentScope= parent_scope;
		this.className= class_name;
		this.symbols= new HashMap<String,Attributes>();
		this.children=new HashMap<AST_Node, ScopeNode>();
	}
}
