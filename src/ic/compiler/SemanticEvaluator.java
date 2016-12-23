package ic.compiler;
import ic.ast.*;

public class SemanticEvaluator implements Visitor<SymbolTable, Attribute> {
		
	private AST_Node root;
	private SymbolTable program;
	
	public SemanticEvaluator (AST_Node root)
	{
		this.root= root;
	}
	
	public void evaluate() {
		this.program= new SymbolTable(null, "");
		root.accept(this, program);
	}

	@Override
	public Attribute visit(AST_Exp expr, SymbolTable d) {
		throw new UnsupportedOperationException("Unxepcted visit in Exp!");
	}

	@Override
	public Attribute visit(AST_ExpBinop expr, SymbolTable sTable) {		
		Attribute leftExpResult = expr.leftExp.accept(this, sTable);
		Attribute rightExpResult = expr.rightExp.accept(this,sTable);
		
		if (!leftExpResult.getType().equals(rightExpResult.getType())){
			throw new RuntimeException("Can't make " + expr.OP.getOpDescreption() + " between different types: " + leftExpResult.getType() + "and " + rightExpResult.getType());
		}
		
		switch (expr.OP){
		case PLUS:
			if (!leftExpResult.getType().isPrimitive() || leftExpResult.equals(PrimitiveDataTypes.VOID)){
				throw new RuntimeException("Can't make " + expr.OP.getOpDescreption() 
				+" between two " + leftExpResult.getType() + " vars");
			}
			break;
		case MINUS:
		case DIVIDE:
		case TIMES:
			if (!leftExpResult.getType().equals(PrimitiveDataTypes.INT)){
				throw new RuntimeException("Can't make" +expr.OP.getOpDescreption() + " between two " + leftExpResult.getType() + " vars");
			}
			break;
		case EQUALS:
		case NEQUALS:
		case LT:
		case LTE:
		case GT:
		case GTE:
			if (!leftExpResult.getType().equals(PrimitiveDataTypes.INT)){
				throw new RuntimeException("Can't compare between two " + leftExpResult.getType() + " vars");
			}
			break;
		}
		expr.setClassName(leftExpResult.getType().getName());
		Attribute attr = new Attribute(leftExpResult.getType());
		return attr;
	}

	@Override
	public Attribute visit(AST_ExpVariable expr, SymbolTable d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Attribute visit(AST_ExpFunctionCall expr, SymbolTable d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Attribute visit(AST_ExpNewClass expr, SymbolTable d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Attribute visit(AST_ExpNewTypeArray expr, SymbolTable d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override

	public Attribute visit(AST_StmtVarAssignment stmt, SymbolTable st) {
		Attribute left = stmt.var.accept(this, st);
		left.getType().accept(this, scope);
		Attribute right = stmt.assignExp.accept(this, st);
		right.getType().accept(this, scope);

		if (!properInheritance(right.getType(), left.getType())) {
			throw new RuntimeException(); // incompatible types or inheritance (cannot assign right to left)
		}
		return null;

	}

	@Override
	public Attribute visit(AST_StmtList stmts, SymbolTable d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Attribute visit(AST_Stmt stmt, SymbolTable d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Attribute visit(AST_StmtCall call, SymbolTable d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Attribute visit(AST_StmtVariableDeclaration stmt, SymbolTable d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Attribute visit(AST_StmtReturn stmt, SymbolTable d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Attribute visit(AST_StmtIf stmt, SymbolTable d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Attribute visit(AST_StmtWhile stmt, SymbolTable d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Attribute visit(AST_VirtualCall call, SymbolTable d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Attribute visit(AST_Variable var, SymbolTable d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Attribute visit(AST_VariableExpField var, SymbolTable d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Attribute visit(AST_VariableExpArray var, SymbolTable d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Attribute visit(AST_VariableID var, SymbolTable d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Attribute visit(AST_Type type, SymbolTable d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Attribute visit(AST_FuncArgument funcArg, SymbolTable d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Attribute visit(AST_Field field, SymbolTable d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Attribute visit(AST_Method method, SymbolTable d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Attribute visit(AST_ClassDecl classDecl, SymbolTable d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Attribute visit(AST_Program program, SymbolTable d) {
		// TODO Auto-generated method stub
		return null;
	}

	// non visit functions

	private boolean properInheritance(Type right, Type left) {

		return ((right.getName().equals("null") && (left.getDefValue() == null || left.getDimension() > 0))
				|| ((right.isPrimitive() && (right.getName()).equals(left.getName()))
				|| (!right.isPrimitive() && inherit(right, left))));
	}

	private boolean inherit(Type right, Type left) {
		if (right.getName().equals("null")){
			return false;
		}
		return (((ClassAttribute)(program.getSymbols().get(subType.getName()))).getAncestors().contains(superType.getName())
				&& subType.getDimension() == 0 && superType.getDimension() == 0) || (subType.equals(superType));
		// TODO implement above return and change names according to code
	}
	
}
