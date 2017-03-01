package ic.compiler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ic.ast.*;

public class SemanticEvaluator implements Visitor<SymbolTable, Attribute> {
		
	private static SemanticEvaluator _instance;
	private AST_Node root;
	private SymbolTable program;
	public Map<AST_Exp, String> callingExpMap = new HashMap<>();
	
	public SemanticEvaluator (AST_Node root)
	{
		this.root= root;
		_instance = this;
	}
	
	public static SemanticEvaluator Get(){
		return _instance;
	}
	
	public void evaluate() {
		this.program= new SymbolTable(null, "");
		root.accept(this, program);
	}

	@Override
	public Attribute visit(AST_Exp expr, SymbolTable symTable) {
		throw new UnsupportedOperationException("Unexepcted visit in Exp!");
	}
	
	public Attribute visit(AST_ExpParen expr,SymbolTable symTable) {
		return expr.exp.accept(this,symTable);
	}
	
	

	@Override
	public Attribute visit(AST_ExpBinop expr, SymbolTable symTable) {		
		Attribute leftExpResult = expr.leftExp.accept(this, symTable);
		Attribute rightExpResult = expr.rightExp.accept(this,symTable);
		
		boolean properInheritedRTL = IsProperInheritance(rightExpResult.getType(), leftExpResult.getType());
		boolean properInheritedLTR = IsProperInheritance(leftExpResult.getType(), rightExpResult.getType());
		
		callingExpMap.put(expr.leftExp, leftExpResult.getType().getName());
		switch (expr.OP){
		case PLUS:
			if (!(properInheritedRTL || properInheritedLTR) || !leftExpResult.getType().isPrimitive() || leftExpResult.getType().isVoid()){
				throw new RuntimeException("Can't make " + expr.OP.getOpDescreption() 
				+" between " + leftExpResult.getType() + " and " + rightExpResult.getType() +  " vars");
			}
			Attribute attr = new Attribute(leftExpResult.getType());
			return attr;
		case MINUS:
		case DIVIDE:
		case TIMES:
			if (!(properInheritedRTL || properInheritedLTR) || !leftExpResult.getType().isInt()){
				throw new RuntimeException("Can't make " +expr.OP.getOpDescreption() + " between " 
				+ leftExpResult.getType() + " and " + rightExpResult.getType() +  " vars");
			}
			break;
		case EQUALS:
		case NEQUALS:
			if (!(properInheritedRTL || properInheritedLTR)){
				throw new RuntimeException("Can't compare between " 
						+ leftExpResult.getType() + " and " + rightExpResult.getType() +  " vars");
			}
			break;
		case LT:
		case LTE:
		case GT:
		case GTE:
			if (!leftExpResult.getType().isInt()){
				throw new RuntimeException("Can't compare between " 
						+ leftExpResult.getType() + " and " + rightExpResult.getType() +  " vars");
			}
			break;
		}
		Attribute attr = new Attribute(new AST_Type(PrimitiveDataTypes.INT));
		return attr;
	}

	@Override
	public Attribute visit(AST_ExpNewClass expr, SymbolTable symTable) {
		Attribute expNewClassAttr = null;
		
		//check if a class that is initialized exists
		if(program.getSymbols().containsKey(expr.className)){
			//class dimension is 0
			expNewClassAttr = new Attribute(new AST_Type(expr.className, null));
		}
		else{
			throw new RuntimeException("Trying to initialize an undeclared class: " + expr.className);
		}
		
		return expNewClassAttr;
	}

	@Override
	public Attribute visit(AST_ExpNewTypeArray expr, SymbolTable symTable) {
		
		expr.arrayType.accept(this,symTable);
		//check if the type specified actually exists 
		if (!(expr.arrayType.getName().equals(PrimitiveDataTypes.INT.getName())) && !(expr.arrayType.getName().equals(PrimitiveDataTypes.STRING.getName()))){
			if (!program.getSymbols().containsKey(expr.arrayType.getName())){
				throw new RuntimeException("Cannot instantiate unknown type: " + expr.arrayType.getName());
			}
		}
			
		//check if the expr inside the brackets returns int and is legal
		Attribute expAttr = expr.sizeExpression.accept(this, symTable);
		//check if the expr is evaluated to INT
		if(!expAttr.getType().isInt()){
			throw new RuntimeException("Expected array size of the type INT, the received size is of type: " + expAttr.getType());
		}
		//creating a return Attribute
		//notice that according to the grammar the total size of array will be
		Attribute retAttr = new Attribute(new AST_Type(expr.arrayType.getName(), expr.arrayType.getDimension()+1));
		return retAttr;
	}

	@Override

	public Attribute visit(AST_StmtVarAssignment stmt, SymbolTable symTable) {
		Attribute left = stmt.var.accept(this, symTable);
		//left.getType().accept(this, symTable);
		Attribute right = stmt.assignExp.accept(this, symTable);
		//right.getType().accept(this, symTable);

		if (!IsProperInheritance(right.getType(), left.getType())) {
			// incompatible types or inheritance (cannot assign right to left)
			throw new RuntimeException("Incompatible types, cannot assign type " + right.getType().getName() + " to type "+ left.getType().getName());
		}
		
		return null;

	}

	@Override
	public Attribute visit(AST_StmtList stmts, SymbolTable symTable) {
		SymbolTable scope;
		if (symTable instanceof MethodSymbolTable)
		{
			scope = new MethodSymbolTable(symTable, ((MethodSymbolTable)symTable).getMethodName());
			symTable.getChildren().put(stmts, scope);
		}
		else{
			scope = symTable;
		}
		
		
		
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

		if (symTable.getSymbols().containsKey(stmt.varName)){
			throw new RuntimeException("Var name " + stmt.varName + " has already been used!");
		}
		
		if (stmt.assignedExp != null){
			Attribute expAttr = stmt.assignedExp.accept(this, symTable);
			
			//checks if value assigned from exprAtrr to var is proper inherited
			if (!IsProperInheritance(expAttr.getType(),stmt.varType)){
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
		
		//check if the method exists as part of the class and get its type
		if (((ClassAttribute)(program.getSymbols().get(methodST.getClassName())))
		    .getMethodMap().containsKey(methodST.getMethodName())){
			expectedRetType = ((ClassAttribute) (program.getSymbols().get(methodST.getClassName())))
				.getMethodMap().get(methodST.getMethodName()).getType();

		}
		//check void type compatibility
		if (expectedRetType.isVoid()){
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
		if (!IsProperInheritance(exprAttr.getType(), expectedRetType)){
			throw new RuntimeException("Can not return type "+ exprAttr.getType() + "from method " + methodST.getMethodName() + " with type "+ expectedRetType.getName());
		}
		
		return exprAttr;
	}

	@Override
	public Attribute visit(AST_StmtIf stmt, SymbolTable symTable) {
		//traverse the condition
		Attribute condAttr = stmt.cond.accept(this, symTable);
		
		//condition attribute is int in our case then we will check for type comaptibility appropriately
		if (!condAttr.getType().isInt() ) {
			throw new RuntimeException("Illegal IF statement: condition must be of type int but is of type " + condAttr.getType().getName());
		}
		//if the body of the statement is not null traverse it and create local scope table if necessary
		if (stmt.body != null) {
			
			//if (stmt.body instanceof AST_StmtList) {
			//	stmt.body.accept(this, symTable);
			//}
			//else {
				MethodSymbolTable currTable = new MethodSymbolTable(symTable, ((MethodSymbolTable)symTable).getMethodName());
				symTable.getChildren().put(stmt.body, currTable);
				stmt.body.accept(this, currTable);
			//}
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
		if (!condAttr.getType().isInt()) {
			throw new RuntimeException("Illegal WHILE statement: condition must be of type int but is of type" + condAttr.getType().getName());
		}
		
		//if the body of the statement is not null traverse it and create local scope table if necessary
		if (stmt.body != null) {
						
			//if (stmt.body instanceof AST_StmtList) {
			//	stmt.body.accept(this, symTable);
			//}
			//else {
				MethodSymbolTable currTable = new MethodSymbolTable(symTable, ((MethodSymbolTable)symTable).getMethodName());
				symTable.getChildren().put(stmt.body, currTable);
				stmt.body.accept(this, currTable);
			//}
			//after finishing with the local scope remove it from the parent table
			symTable.getChildren().remove(stmt.body);
		}
		
		return null;
	}

	@Override
	public Attribute visit(AST_VirtualCall call, SymbolTable symTable) {
		String callExpName = null;
		Attribute callExpAttr = null;
		//expression exists (method is a field of a different class), not an explicit call, evaluate the exp
		if (call.getCallingExpression() != null) {
			callExpAttr = call.getCallingExpression().accept(this, symTable);
			callingExpMap.put(call.callingExp,callExpAttr.getType().getName() );
			
			if (callExpAttr.getType().isPrimitive()) {
				throw new RuntimeException("Primitive type" + callExpAttr.getType() + " can't have a member function " + call.getFuncName());
			}
			
			if (callExpAttr.isNull()) {
				throw new RuntimeException("Null pointer exception, cannot access member function of null");
			}
			
			if (callExpAttr.getType().getDimension() > 0) {
				throw new RuntimeException("Array type of " + callExpAttr.getType() + " can't have a member function " + call.getFuncName());
			}
			callExpName = callExpAttr.getType().getName();
		}
		else { // method is defined in current class
			callExpName = symTable.getClassName();
		}
				
		MethodAttribute funcAttr = ((ClassAttribute)(program.getSymbols().get(callExpName)))
					   .getMethodMap().get(call.getFuncName());
		
		//check if the function exists
		if (funcAttr == null) {
			throw new RuntimeException("Class " + callExpName + " does not have a method named " + call.getFuncName());
		}
		
		//check that the parameters are from the right types
		List<AST_Exp> actualArgs = call.getArguments();
		List<AST_FuncArgument> formalArgs = funcAttr.getParameters();
		String funcName = call.getFuncName();
		
		//no actual and formal parameters
		if (actualArgs == null && formalArgs == null) {
			return funcAttr;
		}
		
		//num of formal and actual parameters must be equal
		if (actualArgs.size() != formalArgs.size()) {
			throw new RuntimeException("Illegal arguments passed to function " 
					+ funcName + ": number of actual arguments is " 
					+ actualArgs.size() + " while the number of parameters should be " + formalArgs.size());
		}
		
		//check types and inheritance
		for (int i = 0; i < actualArgs.size(); i++) {
			//evaluate actual arguments and check validity
			Attribute argAttr = actualArgs.get(i).accept(this, symTable);
			argAttr.getType().accept(this, symTable);
			if (!IsProperInheritance(argAttr.getType(), formalArgs.get(i).getArgType())) {
				throw new RuntimeException("Illegal arguments to method " 
						+ funcName + ": was expecting " + formalArgs.get(i).getArgType().getName()
						+ " but recieved " + argAttr.getType().getName());
			}
		}
		return funcAttr;
	}
	
	@Override
	public Attribute visit(AST_Variable var, SymbolTable symTable) {
		throw new UnsupportedOperationException("Unexpected visit in Variable!");
	}

	@Override
	public Attribute visit(AST_VariableExpField var, SymbolTable symTable) {
		//traverse the expression and evaluate it
		Attribute expAttr = var.exp.accept(this, symTable);

		if (expAttr.isNull()) {
			throw new RuntimeException("Null pointer exception, trying to access field of null expression.");
		}
		
		callingExpMap.put(var.exp, expAttr.getType().getName());

		//check expression type
		expAttr.getType().accept(this, symTable);
		
		if (expAttr.getType().isPrimitive()){
			throw new RuntimeException("Expression is of primitive type and doesn't have fields.");
		}
		
		//check if the field exists in this expression type
		Attribute expFieldAttr = ((ClassAttribute) (program.getSymbols().get(expAttr.getType().getName()))).getFieldMap().get(var.fieldName);
		
		if (expFieldAttr == null){
			throw new RuntimeException("Field " + var.fieldName + " does not exist in type " + expAttr.getType().getName());
		}
		
		return expFieldAttr;
	}

	@Override
	public Attribute visit(AST_VariableExpArray var, SymbolTable symTable) {
		//evaluate the size and the expression of the array
		Attribute arrExpAttr = var.arrayExp.accept(this, symTable);
		Attribute arrIndexAttr = var.arraySize.accept(this, symTable);
		
		if (var.arrayExp instanceof AST_ExpNewTypeArray){
			 throw new RuntimeException("Illegal declaration of array.");
		}
		
		if (arrExpAttr.isNull()){
			throw new RuntimeException("Null pointer exception, trying to access index of null expression.");
		}

		if (arrExpAttr.getType().getDimension() == 0){
			throw new RuntimeException("Expression of type " + arrExpAttr.getType().getName() + " is not an array.");
		}
		// check validity of expression type
		arrExpAttr.getType().accept(this, symTable);
		
		if (!arrIndexAttr.getType().isInt()){
			throw new RuntimeException("Array index expression must be integer value");
		}
		
		int dimension = arrExpAttr.getType().getDimension()-1;
		//returns new attribute, with same type as array expression but with lower dimension(the internal index was already computed)
		Attribute resultAttr =  new Attribute(new AST_Type(arrExpAttr.getType().getName(), dimension));
		return resultAttr;
	}

	@Override
	public Attribute visit(AST_VariableID var, SymbolTable symTable) {
		
		Attribute varAttr = findVar(var.fieldName, symTable);
		//if function returned null then variable was not found in any of the parent copes
		if (varAttr == null) {
			throw new RuntimeException("Variable name " + var.fieldName + " does not exist");
		}
		return varAttr;
	}


	@Override
	public Attribute visit(AST_Type type, SymbolTable symTable) {
		//check for non existent types
		if (!type.checkTypePrimitive() && !type.getName().equals("null") && !program.getSymbols().containsKey(type.getName())) {
			throw new RuntimeException("Unknown type: " + type.getName());
		}
		else if (type.getName().equals(PrimitiveDataTypes.VOID.getName())){
			throw new RuntimeException("Variable cannot be declared as void type.");
		}
		return null;
	}

	@Override
	public Attribute visit(AST_FuncArgument funcArg, SymbolTable symTable) {
		funcArg.getArgType().accept(this, symTable);
		Attribute attribute =  new Attribute(funcArg.getArgType());
		symTable.getSymbols().put(funcArg.getArgName(), attribute);
		return attribute;
	}

	@Override
	public Attribute visit(AST_Field field, SymbolTable symTable) {
		field.getType().accept(this, symTable);
		return null;
	}

	@Override
	public Attribute visit(AST_Method method, SymbolTable symTable) {
		SymbolTable methodSymbolTable = new MethodSymbolTable(symTable, method.getName());
		symTable.getChildren().put(method, methodSymbolTable);
		if (!method.getType().checkTypePrimitive() && !program.getSymbols().containsKey(method.getType().getName())) {
			throw new RuntimeException("non primitive type of " + method.getType().getName() + " is not declared");
		}
		if (method.getArguments() != null) {
			method.getArguments().stream().forEach(arg -> arg.accept(this, methodSymbolTable));
		}
		int size = method.getMethodStmtList().stmtList.size();
		if (size>0 && ! (method.getMethodStmtList().stmtList.get(size-1) instanceof AST_StmtReturn)){
			MethodSymbolTable methodST = (MethodSymbolTable)methodSymbolTable;

			//check if the method exists as part of the class and get its type
			if (((ClassAttribute)(program.getSymbols().get(methodST.getClassName())))
			    .getMethodMap().containsKey(methodST.getMethodName())){
				
				AST_Type expectedRetType = ((ClassAttribute) (program.getSymbols()
						.get(methodST.getClassName())))
						.getMethodMap().get(methodST.getMethodName())
						.getType();
				
				if (!expectedRetType.isVoid()){
					throw new RuntimeException("Method " + method.getName() + " should have a return statement of type " + expectedRetType);
				}

			}

			
		}
		if (method.getMethodStmtList() != null)
			method.getMethodStmtList().accept(this, methodSymbolTable);
		return null;
	}
	
	public Attribute visit(AST_Literal literal, SymbolTable symTable) {
		Attribute litAttr = null;
		//check type of the literal between allowed types
		if (literal.isInteger()){
			litAttr = new Attribute(new AST_Type(PrimitiveDataTypes.INT));
		}
		else if (literal.isString()){
			litAttr = new Attribute(new AST_Type(PrimitiveDataTypes.STRING));
		}
		else if (literal.isNull()){
			litAttr = new Attribute(new AST_Type("null", null));
		}
		else{
			throw new RuntimeException("Unexpected literal type: " + literal.value.toString());
		}
		
		return litAttr;
	}


	@Override
	public Attribute visit(AST_ClassDecl c, SymbolTable symTable) {
		SymbolTable classSymbolTable = new SymbolTable(symTable, c.getClassName()); // creating symbol table for class
		ClassAttribute ca = (ClassAttribute)symTable.getSymbols().get(c.getClassName());
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
	public Attribute visit(AST_Program program, SymbolTable symTable) {
		List<AST_ClassDecl> classDeclList = program.getClasses();
		for (AST_ClassDecl c : classDeclList){
			if (symTable.getSymbols().containsKey(c.getClassName())){
				throw new RuntimeException("class" + c.getClassName() +"already declared");
			}
			else {
				setClassAttribute(c);
			}
		}
		for (AST_ClassDecl c : classDeclList){
			c.accept(this, symTable);
		}
		
		int countMain = 0;
		for (Attribute attribute : symTable.getSymbols().values()){
			ClassAttribute classAtrr = (ClassAttribute) attribute;
			if (classAtrr.hasMainMethod()){
				countMain++;
			}
		}
		if (countMain < 1){
			throw new RuntimeException("The program does not have a valid main method!");
		}
		else if (countMain > 1){
			throw new RuntimeException("Main method should appear only once in a Program!");
		}
		return null;
	}

	/**
	 * Non visit functions 
	 * */

	private void setClassAttribute(AST_ClassDecl cl) {
		//create main method attribute
		ArrayList<AST_FuncArgument> mainArgs = new ArrayList<AST_FuncArgument>(); 
		mainArgs.add(new AST_FuncArgument(new AST_Type(PrimitiveDataTypes.STRING, 1), "args"));
		MethodAttribute main = new MethodAttribute(new AST_Type(PrimitiveDataTypes.VOID,0), mainArgs);
		
		Map<String, Attribute> fieldMap = new HashMap<>();
		Map<String, MethodAttribute> methodMap = new HashMap<>();
		String superClass = cl.getExtendedClassName();
		boolean hasMain = false;
		
		//fill the fields map of cl class symbol table
		for (AST_Field fld : cl.getClassFields()){
			for (String name : fld.getFieldNamesList()){
				if (fieldMap.containsKey(name)){
					throw new RuntimeException("Duplicate decleration of field " + name);
				}
				Attribute attr = new Attribute(fld.getType());
				fieldMap.put(name, attr);
			}
		}
		
		//fill the methods map of cl class symbol table
		for (AST_Method method : cl.getClassMethods()){
			if (methodMap.containsKey(method.getName()) || fieldMap.containsKey(method.getName())){
				throw new RuntimeException("duplicate declerations");
			}
			
			MethodAttribute methodAttr = new MethodAttribute(method.getType(), method.getArguments());
			
			if (method.getName().equals("main") && methodAttr.equals(main)){
				hasMain = true;
			}
			
			methodMap.put(method.getName(), methodAttr);
		}

		ClassAttribute thisClassAttr = new ClassAttribute(fieldMap, methodMap);
		thisClassAttr.setHasMainMethod(hasMain);
		thisClassAttr.getAncestors().add(cl.getClassName());
		program.getSymbols().put(cl.getClassName(), thisClassAttr);
		
		// if class doesn't extend a superClass	
		if (superClass != null){
			if (superClass.equals(cl.getClassName())){
				throw new RuntimeException("invalid class extension");
			}
			if (!program.getSymbols().containsKey(superClass)){
				throw new RuntimeException("class " + superClass + " has not been declared yet");
			}
			
			addAttrsFromSuperClasses(cl, 
					(ClassAttribute)program.getSymbols().get(cl.getClassName()), 
					(ClassAttribute)program.getSymbols().get(superClass));

			//if (program.getSymbols().get(superClass) != null){ 
			//}
			//else{
			//	throw new RuntimeException("cannot extend from undecleared function");
			//}
		}
	}

	private void addAttrsFromSuperClasses(AST_ClassDecl c, ClassAttribute currentClassAttribute, ClassAttribute superClassAttribute) {
		for (String key : superClassAttribute.getMethodMap().keySet()){
			MethodAttribute value = superClassAttribute.getMethodMap().get(key);
			if (currentClassAttribute.getMethodMap().containsKey(key) &&
					!currentClassAttribute.getMethodMap().get(key).equals(value)){
				throw new RuntimeException("same methods in super and current classes cannot have different signatures");
			}
			currentClassAttribute.getMethodMap().put(key, value);
		}
		
		for (String key : superClassAttribute.getFieldMap().keySet()){
			Attribute value = superClassAttribute.getFieldMap().get(key);
			if (currentClassAttribute.getFieldMap().containsKey(key)){
				throw new RuntimeException("same fields in super and current classes cannot have different types");
			}
			currentClassAttribute.getFieldMap().put(key, value);
		}
		
		// Add all ancestors to current class
		currentClassAttribute.getAncestors().addAll(superClassAttribute.getAncestors());
	}
	
	private boolean IsProperInheritance(AST_Type subType, AST_Type superType) {
		
		if (subType.getDimension() != superType.getDimension()){
			return false;
		}
		if (!((subType.getName().equals("null") && (superType.getDefVal() == null || superType.getDimension() > 0)))){
			
			if (!subType.checkTypePrimitive() || !superType.checkTypePrimitive() || !subType.getName().equals(superType.getName() )){
			
				if (! (!subType.checkTypePrimitive() && inherit(subType, superType)) ){
					return false;
				}
			}
		}
		return true;
		
				
		/*return   right.getDimension() == left.getDimension() && 
				((right.getName().equals("null") && (left.getDefVal() == null || left.getDimension() > 0))
				|| ((right.checkTypePrimitive() && left.checkTypePrimitive() && (right.getName()).equals(left.getName()))
				|| (!right.checkTypePrimitive() && inherit(right, left))));*/
				
			
	}

	private boolean inherit(AST_Type subType, AST_Type superType) {
		if (subType.getName().equals("null")){
			return false;
		}
		
		boolean isAncestor = (((ClassAttribute)(program.getSymbols().get(subType.getName()))).getAncestors().contains(superType.getName()));
		boolean isSameDim = (subType.getDimension() == 0 && superType.getDimension() == 0);
		
		return (subType.equals(superType)) || (isAncestor && isSameDim);
	}
	
	private Attribute findVar(String varName, SymbolTable symTable) {
		SymbolTable st = symTable;
		//iterate over hierarchy of scopes and look for varname, return null if not found
		while (st != program){
			if (st.getSymbols().containsKey(varName)){
				return st.getSymbols().get(varName);
			}
			st = st.getParentTable();
		}
		return null;
	}
	
}
