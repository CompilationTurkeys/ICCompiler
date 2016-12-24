package ic.compiler;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public Attribute visit(AST_ExpFunctionCall expr, SymbolTable d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Attribute visit(AST_ExpNewClass expr, SymbolTable d) {
		Attribute expNewClassAttr = null;
		
		//check if a class that is initialized exists
		if(program.getSymbols().containsKey(expr.getClassName())){
			expr.setClassName(expr.getClassName());
			//class dimension is 0
			expNewClassAttr = new Attribute(new AST_Type(expr.getClassName(), 0));//null argument == 0 dimension
		}
		else{
			throw new RuntimeException("Trying to initialize an undeclared class: " + expr.getClassName());
		}
		
		return expNewClassAttr;
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
			
			if (expAttr.isMethod() && (stmt.assignedExp instanceof AST_Variable)){
				throw new RuntimeException("Cannot assign method to variable!");
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
	public Attribute visit(AST_StmtIf stmt, SymbolTable symTable) {
		//traverse the condition
		Attribute condAttr = stmt.cond.accept(this, symTable);
		
		//condition attribute is int in our case then we will check for type comaptibility appropriately
		if (!condAttr.getType().getName().equals("int") || condAttr.getType().getDimension() > 0) {
			throw new RuntimeException("Illegal IF statement: condition must be of type int but is of type" + condAttr.getType().getName());
		}
		//if the body of the statement is not null traverse it and create local scope table if necessary
		if (stmt.body != null) {
			
			if (stmt.body instanceof AST_StmtList) {
				stmt.body.accept(this, symTable);
			}
			else {
				MethodSymbolTable currTable = new MethodSymbolTable(symTable, ((MethodSymbolTable)symTable).getMethodName());
				symTable.getChildren().put(stmt.body, currTable);
				stmt.body.accept(this, currTable);
			}
			//after finishing with the local scope remove it from the parent table
			symTable.getChildren().remove(stmt.body);
		}

		return null;
	}

	@Override
	public Attribute visit(AST_StmtWhile stmt, SymbolTable symTable) {
		//traverse the condition of while
		Attribute condAttr = stmt.cond.accept(this, symTable);
		
		//condition attribute is int in our case then we will check for type comaptibility appropriately
		if (!condAttr.getType().getName().equals("int") || condAttr.getType().getDimension() > 0) {
			throw new RuntimeException("Illegal WHILE statement: condition must be of type int but is of type" + condAttr.getType().getName());
		}
		
		//if the body of the statement is not null traverse it and create local scope table if necessary
		if (stmt.body != null) {
						
			if (stmt.body instanceof AST_StmtList) {
				stmt.body.accept(this, symTable);
			}
			else {
				MethodSymbolTable currTable = new MethodSymbolTable(symTable, ((MethodSymbolTable)symTable).getMethodName());
				symTable.getChildren().put(stmt.body, currTable);
				stmt.body.accept(this, currTable);
			}
			//after finishing with the local scope remove it from the parent table
			symTable.getChildren().remove(stmt.body);
		}
		
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
	public Attribute visit(AST_Type type, SymbolTable st) {
		if (!type.isPrimitive() && !type.getName().equals("null") && !program.getSymbols().containsKey(type.getName())) {
			throw new RuntimeException("");
		}
		else if (type.getName().equals(PrimitiveDataTypes.VOID.getName())){
			throw new RuntimeException("");
		}
		return null;
		// TODO with implementation of AST_Literal
	}

	@Override
	public Attribute visit(AST_FuncArgument funcArg, SymbolTable st) {
		funcArg.getArgType().accept(this, st);
		Attribute attribute =  new Attribute(funcArg.getArgType());
		st.getSymbols().put(funcArg.getArgName(), attribute);
		funcArg.setClassName(attribute.getType().getName());
		return attribute;
	}

	@Override
	public Attribute visit(AST_Field field, SymbolTable st) {
		field.getType().accept(this, st);
		return null;
	}

	@Override
	public Attribute visit(AST_Method method, SymbolTable st) {
		SymbolTable methodSymbolTable = new MethodSymbolTable(st, method.getName());
		st.getChildren().put(method, methodSymbolTable);
		if (!method.getType().isPrimitive() && !program.getSymbols().containsKey(method.getType().getName())) {
			throw new RuntimeException("non primitive type of " + method.getType().getName() + "is not declared");
		}
		if (method.getArguments() != null) {
			method.getArguments().stream().forEach(arg -> arg.accept(this, methodSymbolTable));
		}
		if (method.getMethodStmtList() != null)
			method.getMethodStmtList().accept(this, methodSymbolTable);
		return null;
	}
	
	public Attribute visit(AST_Literal call, SymbolTable st) {
		//TODO finish visit 
		return null;
	}


	@Override
	public Attribute visit(AST_ClassDecl c, SymbolTable st) {
		SymbolTable classSymbolTable = new SymbolTable(st, c.getClassName()); // creating symbol table for class
		ClassAttribute ca = (ClassAttribute)st.getSymbols().get(c.getClassName());
		classSymbolTable.getSymbols().putAll(ca.getMethodMap());
		classSymbolTable.getSymbols().putAll(ca.getFieldMap());
		for (AST_Field f : c.getClassFields()){
			f.accept(this, classSymbolTable);
		}
		for (AST_Method m: c.getClassMethods()){
			m.accept(this, classSymbolTable);
		}
		return null;
	}

	@Override
	public Attribute visit(AST_Program program, SymbolTable st) {
		List<AST_ClassDecl> classDeclList = program.getClasses();
		for (AST_ClassDecl c : classDeclList){
			if (st.getSymbols().containsKey(c.getClassName())){
				throw new RuntimeException("class" + c.getClassName() +"already declared");
			}
			else {
				setClassAttribute(c);
			}
		}
		for (AST_ClassDecl c : classDeclList){
			c.accept(this, st);
		}
		
		int countMain = 0;
		for (Attribute attribute : st.getSymbols().values()){
			ClassAttribute classAtrr = (ClassAttribute) attribute;
			if (classAtrr.hasMainMethod()){
				countMain++;
			}
		}
		if (countMain != 1){
			throw new RuntimeException("Main method should appear only one in Program");
		}
		return null;
	}

	// non visit functions

	private void setClassAttribute(AST_ClassDecl c) {
		// TODO check if 'main' checks are necessary
//		MethodAttribute main = new MethodAttribute(new Type(PrimitiveTypes.VOID,0,-1), new FormalList(new Formal(new Type(PrimitiveTypes.STRING, 1, -1), "args",  -1), -1), true);
		Map<String, Attribute> fieldMap = new HashMap<>();
		Map<String, MethodAttribute> methodMap = new HashMap<>();
		String superClass = c.getExtendedClassName();
		
		//fill the fields map of c class symbol table
		for (AST_Field fld : c.getClassFields()){
			for (String name : fld.getFieldNamesList()){
				if (fieldMap.containsKey(name)){
					throw new RuntimeException("Duplicate decleration of field " + name);
				}
				Attribute attr = new Attribute(fld.getType());
				fieldMap.put(name, attr);
			}
		}
		
		// fill the methods map of c class symbol table
		for (AST_Method method : c.getClassMethods()){
			if (methodMap.containsKey(method.getName()) || fieldMap.containsKey(method.getName())){
				throw new RuntimeException("duplicate declerations");
				}
			MethodAttribute attr = new MethodAttribute(method.getType(), method.getArguments());
			
//			if (m.getName().equals("main") && attr.equals(main)){
//				hasMain = true;
//			}
			methodMap.put(method.getName(), attr);
		}

		ClassAttribute thisClassAttr = new ClassAttribute(fieldMap, methodMap);
//		thisClassAttr.setHasMain(hasMain);
		thisClassAttr.getAncestors().add(c.getClassName());
		program.getSymbols().put(c.getClassName(), thisClassAttr);
				
		if (superClass != null){  // if class doesn't extend a superClass
			if (superClass.equals(c.getClassName())){
				throw new RuntimeException("invalid class extension");
			}
			if (!program.getSymbols().containsKey(superClass)){
				throw new RuntimeException("class doesn't exist");
			}
			if (program.getSymbols().get(superClass) != null){ 
				addAttrsFromSuperClasses(c, (ClassAttribute)program.getSymbols().get(c.getClassName()), (ClassAttribute)program.getSymbols().get(superClass));
			}
			else{
				throw new RuntimeException("cannot extend from undecleared function");
			}
		}
	}

	private void addAttrsFromSuperClasses(AST_ClassDecl c, ClassAttribute currentClassAttribute, ClassAttribute superClassAttribute) {
		for (String key : superClassAttribute.getMethodMap().keySet()){
			MethodAttribute value = superClassAttribute.getMethodMap().get(key);
			if (currentClassAttribute.getMethodMap().containsKey(key) && !currentClassAttribute.getMethodMap().get(key).equals(value)){
				throw new RuntimeException("same methods in super and current classes cannot have different signatures");
			}
			currentClassAttribute.getMethodMap().put(key, value);
		}
		
		for (String key : superClassAttribute.getMethodMap().keySet()){
			MethodAttribute value = superClassAttribute.getMethodMap().get(key);
			if (currentClassAttribute.getFieldMap().containsKey(key) && !currentClassAttribute.getFieldMap().get(key).equals(value)){
				throw new RuntimeException("same fields in super and current classes cannot have different types");
			}
			currentClassAttribute.getFieldMap().put(key, value);
		}
		
		// Add all ancestors to current class
		currentClassAttribute.getAncestors().addAll(superClassAttribute.getAncestors());
	}

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
