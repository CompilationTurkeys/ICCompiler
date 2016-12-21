import AST.AST_Node;

public class SemanticEvaluator {
	
	//all the tests will be here
	// (for example: check if types are matching in binary operations, check if a variable is declared..etc)
	
	private AST_Node root;
	private SymbolTable program;
	
	public SemanticEvaluator (AST_Node root)
	{
		this.root= root;
	}
	
	public void evaluate() {
	
		this.program= new SymbolTable(null, "");
		//need to call "accept" function.
		// accept is a method of every AST_Node that calls Visitor.visit . (TODO)
	}
	
	
	//TODO:   "VISIT" methods, with the tests .
	
}
