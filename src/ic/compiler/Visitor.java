package ic.compiler;
import ic.ast.*;

/** An interface for traversing the AST received from the parser.
 * The visitor passes down objects of type ResultType
 * and propagates up objects of type ContextType.
 */
public interface Visitor<ContextType, ResultType> {

	public ResultType visit(AST_Exp expr, ContextType symTable); // V
	public ResultType visit(AST_ExpBinop expr, ContextType symTable); // V
	public ResultType visit(AST_ExpNewClass expr, ContextType symTable); // V
	public ResultType visit(AST_ExpNewTypeArray expr, ContextType symTable); // V
	
	public ResultType visit(AST_StmtVarAssignment stmt, ContextType symTable); // V
	public ResultType visit(AST_StmtList stmts, ContextType symTable); // V
	public ResultType visit(AST_Stmt stmt, ContextType symTable); // V
	public ResultType visit(AST_StmtCall call, ContextType symTable); // V
	public ResultType visit(AST_StmtVariableDeclaration stmt, ContextType symTable); // V
	public ResultType visit(AST_StmtReturn stmt, ContextType symTable); // V
	public ResultType visit(AST_StmtIf stmt, ContextType symTable); // V
	public ResultType visit(AST_StmtWhile stmt, ContextType symTable); // V

	public ResultType visit(AST_VirtualCall call, ContextType symTable); // V

	public ResultType visit(AST_Variable var,  ContextType symTable); // V
	public ResultType visit(AST_VariableExpField var,  ContextType symTable); // V
	public ResultType visit(AST_VariableExpArray var,  ContextType symTable); // V
	public ResultType visit(AST_VariableID var,  ContextType symTable); // V
	
	public ResultType visit(AST_Type type, ContextType symTable); // V
	
	public ResultType visit(AST_FuncArgument funcArg, ContextType symTable); // V
	
	public ResultType visit(AST_Field field, ContextType symTable); // V
	
	public ResultType visit(AST_Method method, ContextType symTable); //  V
	
	public ResultType visit(AST_Literal literal, ContextType symTable); //  V
	
	public ResultType visit(AST_ClassDecl classDecl, ContextType symTable); // V
	
	public ResultType visit(AST_Program program, ContextType symTable); // V

}