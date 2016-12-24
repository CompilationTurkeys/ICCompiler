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
		throw new UnsupportedOperationException("Unexepcted visit in Exp!");
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

	public Attribute visit(AST_StmtVarAssignment stmt, SymbolTable symTable) {
		Attribute left = stmt.var.accept(this, symTable);
		//left.getType().accept(this, symTable);
		Attribute right = stmt.assignExp.accept(this, symTable);
		//right.getType().accept(this, symTable);

		if (!properInheritance(right.getType(), left.getType())) {
			// incompatible types or inheritance (cannot assign right to left)
			throw new RuntimeException("Incompatible types, cannot assign type " + right.getType().getName() + " to type "+ left.getType().getName());
		}
		return null;

	}

	@Override
	public Attribute visit(AST_StmtList stmts, SymbolTable symTable) {
		for (AST_Stmt stmt : stmts.stmtList){
			stmt.accept(this, symTable);
		}
		return null;
	}

	@Override
	public Attribute visit(AST_Stmt stmt, SymbolTable symTable) {
		throw new UnsupportedOperationException("Unexepcted visit in Stmt!");
	}

	@Override
	public Attribute visit(AST_StmtCall call, SymbolTable symTable) {
		call.funcCall.accept(this, symTable);
		return null;
	}

	@Override
	public Attribute visit(AST_StmtVariableDeclaration stmt, SymbolTable symTable) {
		//check if var type is valid
		stmt.varType.accept(this, symTable);
		//if the variable name has already been used
		if (symTable.getSymbols().containsKey(stmt.varName)){
			throw new RuntimeException("Var name " + stmt.varName + " has already been used!");
		}
		if (stmt.assignedExp != null){
			Attribute expAttr = stmt.assignedExp.accept(this, symTable);
			//check proper inheritance
			if (!properInheritance(expAttr.getType(), stmt.varType)){
				throw new RuntimeException("Incompatible types, cannot assign type " + expAttr.getType().getName() + " to type "+ stmt.varType.getName());
			}
			
			if (expAttr.isMethod() && (stmt.assignedExp instanceof AST_ExpVariable)){
				throw new RuntimeException("Can not assign method to variable!");
			}
		}
		
		Attribute newVar = new Attribute(stmt.varType);
		symTable.getSymbols().put(stmt.varName, newVar);
		return null;
	}
	
	@Override
	public Attribute visit(AST_StmtReturn stmt, SymbolTable symTable) {
		AST_Type expectedRetType = null;
		
		if (!(symTable instanceof MethodSymbolTable)){
			throw new RuntimeException("Encountered return out of method scope.");
		}
		
		MethodSymbolTable methodST = (MethodSymbolTable)symTable;
		//check if the method does actually exist as part of the class and get its type
		if (((ClassAttribute)(program.getSymbols().get(methodST.getClassName()))).getMethodMap().containsKey(methodST.getMethodName())){
			expectedRetType = ((ClassAttribute)(program.getSymbols().get(methodST.getClassName()))).getMethodMap().get(methodST.getMethodName()).getType();

		}
		//check void type compatibility
		if (expectedRetType.getName().equals(PrimitiveDataTypes.VOID.getName())){
			if (stmt.returnExp != null) {
				throw new RuntimeException("Method " + methodST.getMethodName() + " is void and cannot return a value");
			}
			else {
				return null;
			}
		}
		else if (stmt.returnExp == null) {
			throw new RuntimeException("Method " + methodST.getMethodName() + " is missing a return value "+ " return type is " + expectedRetType.getName());
		}
		//traverse the expression
		Attribute exprAttr = stmt.returnExp.accept(this, symTable);
		//check if method return type is subtype of method return type
		if (!properInheritance(exprAttr.getType(), expectedRetType)){
			throw new RuntimeException("Can not return type "+ exprAttr.getType() + "from method " + methodST.getMethodName() + " with type "+ expectedRetType.getName());
		}
		
		stmt.setClassName(exprAttr.getType().getName());
		return exprAttr;
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

	private boolean properInheritance(AST_Type right, AST_Type left) {

		return ((right.getName().equals("null") && (left.getDefVal() == null || left.getDimension() > 0))
				|| ((right.isPrimitive() && (right.getName()).equals(left.getName()))
				|| (!right.isPrimitive() && inherit(right, left))));
	}

	private boolean inherit(AST_Type subType, AST_Type superType) {
		if (subType.getName().equals("null")){
			return false;
		}
		
		boolean isAncestor = (((ClassAttribute)(program.getSymbols().get(subType.getName()))).getAncestors().contains(superType.getName()));
		boolean isSameDim = (subType.getDimension() == 0 && superType.getDimension() == 0);
		
		return (subType.equals(superType)) || (isAncestor && isSameDim);
	}
	
}
