package ic.compiler;
import ic.ast.*;

public class SemanticEvaluator implements Visitor<SymbolTable, Attribute> {
	
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
		root.accept(this, program);
		//need to call "accept" function.
		// accept is a method of every AST_Node that calls Visitor.visit . (TODO)
	}

	public ResultType visit(AST_Exp expr, ContextType d) {
		if (expr instanceof AST_Exp )
	}


	//TODO:   "VISIT" methods, with the tests .
	
}
