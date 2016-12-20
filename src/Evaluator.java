import AST.AST_Node;

public class Evaluator {
	
	//all the tests will be here
	// (for example: check if types are matching in binary operations, check if a variable is declared..etc)
	
	private AST_Node root;
	private ScopeNode program;
	
	public Evaluator (AST_Node root)
	{
		this.root= root;
	}
	
	public void evaluate() {
	
		this.program= new ScopeNode(null, "");
		//need to call "accept" function.
		// accept is a method of every AST_Node that calls Visitor.visit . (TODO)
	}
	
	
	//TODO:   "VISIT" methods, with the tests .
	
}
