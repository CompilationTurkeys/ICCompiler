package ic.compiler;
import ic.ast.*;

public interface PrinterVisitor {

		public void visit(AST_Exp expr);
		public void visit(AST_ExpBinop expr);
		public void visit(AST_ExpNewClass expr);
		public void visit(AST_ExpNewTypeArray expr);
		
		public void visit(AST_StmtVarAssignment stmt);
		public void visit(AST_StmtList stmts);
		public void visit(AST_Stmt stmt);
		public void visit(AST_StmtCall call);
		public void visit(AST_StmtVariableDeclaration stmt);
		public void visit(AST_StmtReturn stmt);
		public void visit(AST_StmtIf stmt);
		public void visit(AST_StmtWhile stmt);

		public void visit(AST_VirtualCall call);

		public void visit(AST_Variable var);
		public void visit(AST_VariableExpField var);
		public void visit(AST_VariableExpArray var);
		public void visit(AST_VariableID var);
		
		public void visit(AST_Type type);
		
		public void visit(AST_FuncArgument funcArg);
		
		public void visit(AST_Field field);
		
		public void visit(AST_Method method);
		
		public void visit(AST_ClassDecl classDecl);
		
		public void visit(AST_Program program);

}
	