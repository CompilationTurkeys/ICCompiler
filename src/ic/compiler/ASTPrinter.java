package ic.compiler;
import ic.ast.*;

public class ASTPrinter implements PrinterVisitor {
	
	protected final AST_Node root;
	protected int numberOfTabs;

	
	public ASTPrinter(AST_Node root) {
		this.root = root;
	}
	
	public void print() {
		root.accept(this);
	}

	
	//--------HELLO-KITTY---------------
	
	//´´´´´´´¶¶¶¶´´´´´´´´´´´´´´´´´´ 
	//´´´´´´¶¶´´´´¶¶¶¶¶´´¶¶¶¶´¶¶¶¶´´
	//´´´´´´¶´´´´´´´´´´¶¶¶¶´¶¶´´´´¶´
	//´´´´´´¶´´´´´´´´´´¶´¶¶¶¶¶¶´´´¶´
	//´´´´´¶´´´´´´´´´´¶¶¶¶¶´´´¶¶¶¶¶´
	//´´´´¶´´´´´´´´´´´´´´´´¶¶¶¶¶¶¶¶´
	//´´´¶´´´´´´´´´´´´´´´´´´´¶¶¶¶¶´´
	//´¶¶¶´´´´´¶´´´´´´´´´´´´´´´´´¶´´
	//´´´¶´´´´¶¶´´´´´´´´´´´´´´´´´¶´´
	//´´´¶¶´´´´´´´´´´´´´´´´¶¶´´´´¶´´
	//´´¶¶¶´´´´´´´´´¶¶¶´´´´¶¶´´´¶¶´´
	//´´´´´¶¶´´´´´´´´´´´´´´´´´´¶¶¶´´
	//´´´´´´´¶¶¶´´´´´´´´´´´´´¶¶¶´´´´
	//´´´¶¶¶¶¶´¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶´´´´´´
	//´´´¶´´´´¶¶¶¶¶´´´´¶¶¶¶´´´¶´´´´´
	//´´´¶´´´´¶¶¶´¶¶¶¶¶¶¶¶´´´¶¶¶´´´´
	//´´´¶¶¶¶¶¶¶¶¶¶¶¶¶´´¶¶¶¶¶´´´¶¶´´
	//´´¶´´´´´´¶¶¶¶¶¶¶¶¶¶¶´´´´´´´¶´´
	//´¶´´´´´´´´´¶¶¶¶¶¶¶¶´´´´´´´´¶´´
	//´´¶´´´´´´´´¶¶¶¶¶¶¶¶´´´´´´´´¶´´
	//´´¶¶´´´´´´´¶¶´´´´¶¶´´´´´´¶¶´´´
	//´´´´¶¶¶¶¶¶¶´´´´´´´´¶¶¶¶¶¶´´´´´

	

	@Override
	public void visit(AST_Exp expr) {
		//
	}

	@Override
	public void visit(AST_ExpBinop expr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AST_ExpFunctionCall expr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AST_ExpNewClass expr) {
		// TODO Auto-generated method stub
	
	}

	@Override
	public void visit(AST_ExpNewTypeArray expr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AST_StmtVarAssignment stmt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AST_StmtList stmts) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AST_Stmt stmt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AST_StmtCall call) {
		// TODO Auto-generated method stub
	
	}

	@Override
	public void visit(AST_StmtVariableDeclaration stmt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AST_StmtReturn stmt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AST_StmtIf stmt) {
		// TODO Auto-generated method stub
	
	}

	@Override
	public void visit(AST_StmtWhile stmt) {
		// TODO Auto-generated method stub
	
	}

	@Override
	public void visit(AST_VirtualCall call) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(AST_Variable var) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(AST_VariableExpField var) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(AST_VariableExpArray var) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(AST_VariableID var) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(AST_Type type) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(AST_FuncArgument funcArg) {
		// TODO Auto-generated method stub
	
	}

	@Override
	public void visit(AST_Field field) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(AST_Method method) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AST_ClassDecl classDecl) {
		// TODO Auto-generated method stub
	
	}

	@Override
	public void visit(AST_Program program) {
		// TODO Auto-generated method stub

	}

	
}


