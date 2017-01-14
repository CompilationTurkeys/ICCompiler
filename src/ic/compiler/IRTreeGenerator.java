package ic.compiler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ic.ir.*;
import javax.management.RuntimeErrorException;
import ic.ast.*;

public class IRTreeGenerator implements Visitor<IR_SymbolTable, IR_Exp> {
		
	public static final String FP="$fp";
	public static final String SP="$sp";

	private IR_Exp irRoot;
	private IR_SymbolTable program;
	private AST_Node astRoot;
	
	// Map of string name to  string value for the the MIPS generation
	private Map<String, String> stringLabelsMap = new HashMap<>();

	// Maps the name of the classes in the program to their LIRClassAttributes
	private Map<String, IR_ClassAttribute> classMap = new HashMap<>();

	// Maps the name of the classes to the corresponding ClassDecl objects.
	//private HashMap<String, HashMap<String, FormalList>> classToMethodFormalsMap = new HashMap<>();

	// Map of the dispach tables of each class.
	// Each dispach table is a map of function name and the classname it was dispatched by
	private Map<String, Map<String,String>> dispachMethodsTablesMap = new HashMap<>();
	
	private Map<String, Map<String,String>> dispachFieldsTablesMap = new HashMap<>();
	private String mainClassName;
	
	public IRTreeGenerator (AST_Node root)
	{
		this.astRoot= root;
	}
	
	public IR_Exp generateIRTree() {
		this.program= new IR_SymbolTable(null, "");
		//Add types and basic funcs??
		
		irRoot.accept(this, program);
		return irRoot;
	}
	
	
	@Override
	public IR_Exp visit(AST_Exp expr, IR_SymbolTable symTable) {
		throw new UnsupportedOperationException("Unexepcted visit in Exp!");
	}

	@Override
	public IR_Exp visit(AST_ExpBinop expr, IR_SymbolTable symTable) {		
		IR_Exp leftExpResult = expr.leftExp.accept(this, symTable);
		IR_Exp rightExpResult = expr.rightExp.accept(this,symTable);
		
		switch (expr.OP){
		case PLUS:
		case MINUS:
		case DIVIDE:
		case TIMES:
			return new IR_Binop(leftExpResult, rightExpResult, expr.OP);
			
		case EQUALS:
		case NEQUALS:
		case LT:
		case LTE:
		case GT:
		case GTE:
			TempRegister newTemp = new TempRegister();
			TempLabel trueLabel = new TempLabel("T");
			TempLabel falseLabel = new TempLabel("F");
			TempLabel endLabel = new TempLabel("END");
			
			return new IR_Seq(
					new IR_Seq(
						new IR_Cjump(expr.OP,leftExpResult,rightExpResult,trueLabel,falseLabel),
						new IR_Seq(
							new IR_Seq(
								new IR_Label(trueLabel),
								new IR_Seq(
									new IR_Move(new IR_Temp(newTemp),new IR_Const(1)),
									new IR_JumpLabel(endLabel))),
							new IR_Seq(
								new IR_Label(falseLabel),
								new IR_Seq(
									new IR_Move(new IR_Temp(newTemp),new IR_Const(0)),
									new IR_JumpLabel(endLabel))))),
					new IR_Seq(new IR_Label(endLabel),new IR_Temp(newTemp)));
			
		}
	}

	@Override
	public IR_Exp visit(AST_ExpNewClass expr, IR_SymbolTable symTable) {
		  return new IR_NewObject(expr.className);
	}

	@Override
	public Attribute visit(AST_ExpNewTypeArray expr, SymbolTable symTable) {
		
		expr.arrayType.accept(this,symTable);
		//check if the type specified actually exists 
		if (!(expr.arrayType.getName().equals(PrimitiveDataTypes.INT.getName())) && !(expr.arrayType.getName().equals(PrimitiveDataTypes.STRING.getName()))){
			if (!program.getSymbols().containsKey(expr.arrayType)){
				throw new RuntimeException("Cannot instantiate unknown type: " + expr.arrayType.getName());
			}
		}
		
		if (expr.arrayType.getDimension() != 0){
			throw new RuntimeException("Cannot specify an array dimension after an empty dimension");	
		}
			
		//check if the expr inside the brackets returns int and is legal
		Attribute expAttr = expr.sizeExpression.accept(this, symTable);
		//check if the expr is evaluated to INT
		if(!expAttr.getType().isInt()){
			throw new RuntimeException("Expected array size of the type INT, the received size is of type: " + expr.arrayType.getName());
		}
		//creating a return Attribute
		//notice that according to the grammar the total size of array will be
		Attribute retAttr = new Attribute(new AST_Type(expr.arrayType.getName(), expr.arrayType.getDimension()+1));
		return retAttr;
	}

	@Override

	public IR_Exp visit(AST_StmtVarAssignment stmt, IR_SymbolTable symTable) {
		IR_Exp assignExp = stmt.assignExp.accept(this, symTable);
		IR_Exp var = stmt.var.accept(this,symTable);
		return new IR_Move(var, assignExp);
	}

	@Override
	public IR_Exp visit(AST_StmtList stmts, IR_SymbolTable symTable) {
		IR_Exp result;
		
		if (stmts.stmtList == null){
			return null;
		}
		
		result = stmts.stmtList.remove(0).accept(this, symTable);					
		
		return new IR_Seq(result, stmts.accept(this, symTable));
	}	

	@Override
	public IR_Exp visit(AST_Stmt stmt, IR_SymbolTable symTable) {
		throw new UnsupportedOperationException("Unexepcted visit in Stmt!");
	}

	@Override
	public IR_Exp visit(AST_StmtCall call, IR_SymbolTable symTable) {
		return call.funcCall.accept(this, symTable);
	}

	@Override
	public IR_Exp visit(AST_StmtVariableDeclaration stmt, IR_SymbolTable symTable) {		
	
		int numOfLocals = ++symTable.getFrame().numOfLocalVars;
		symTable.getFrame().size += MethodFrame.WORD_SIZE;
		FrameMember newMem = new FrameMember(-(numOfLocals*MethodFrame.WORD_SIZE));
		symTable.getSymbols().put(stmt.varName, new IR_Attribute(newMem, stmt.varType));

		if (stmt.assignedExp != null){
			//traverse var expression
			return new IR_Move(IRExpAssign(newMem),
							   stmt.assignedExp.accept(this, symTable));		
		}
		
		return null;
	}
	
	private IR_Mem IRExpAssign(FrameMember frameMember)
	{
		return new IR_Mem(
				new IR_Binop(new IR_Const(frameMember.offset), 
						new IR_Temp(new SpecialRegister(FP)),
						BinaryOpTypes.PLUS));
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
	public IR_Exp visit(AST_StmtIf stmt, IR_SymbolTable symTable) {
		
		IR_Exp body = null;
		IR_Exp condAttr = stmt.cond.accept(this, symTable);
		
		if (stmt.body != null){
			IR_SymbolTable newScopeTable = new IR_SymbolTable(symTable, symTable.getClassName());
			symTable.getChildren().put(stmt.body, newScopeTable);
			body = stmt.body.accept(this, newScopeTable);
			symTable.getChildren().remove(stmt.body);
		}

		TempLabel jumpHereIfTrue = new TempLabel("if_cond_T");
		TempLabel jumpHereIfFalse = new TempLabel("if_cond_F");
		
		return
			new IR_Seq(
				new IR_Seq(
					new IR_Cjump(
							BinaryOpTypes.NEQUALS,
							condAttr,
							new IR_Const(0),
							jumpHereIfTrue,
							jumpHereIfFalse),
					new IR_Seq(
							new IR_Label(jumpHereIfTrue),
							body)),
				new IR_Label(jumpHereIfFalse));
		
		
	}
	
	@Override
	public IR_Exp visit(AST_StmtWhile stmt, IR_SymbolTable symTable) {
		
		IR_Exp cond = null, body = null;

		TempLabel loop = new TempLabel("while_loop");
		TempLabel exitLoop = new TempLabel("while_exit_loop");

		cond = stmt.cond.accept(this, symTable);
		
		if (stmt.body != null){
			IR_SymbolTable newScopeTable = new IR_SymbolTable(symTable, symTable.getClassName());
			symTable.getChildren().put(stmt.body, newScopeTable);
			body = stmt.body.accept(this, newScopeTable);
			symTable.getChildren().remove(stmt.body);
		}
		
		return
				new IR_Seq(
					new IR_Seq(
						new IR_Cjump(
								BinaryOpTypes.NEQUALS,
								cond,
								new IR_Const(0),
								loop,
								exitLoop),
						new IR_Seq(
								new IR_Label(loop),
								new IR_Seq(
										body, 
										new IR_Cjump(
												BinaryOpTypes.NEQUALS,
												cond,
												new IR_Const(0),
												loop,
												exitLoop)))),
					new IR_Label(exitLoop));

	}

	@Override
	public Attribute visit(AST_VirtualCall call, SymbolTable symTable) {
		String callExpName = null;
		Attribute callExpAttr = null;
		//expression exists (method is a field of a different class), not an explicit call, evaluate the exp
		if (call.getCallingExpression() != null) {
			callExpAttr = call.getCallingExpression().accept(this, symTable);
			
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
	public IR_Exp visit(AST_Variable var, IR_SymbolTable symTable) {
		throw new UnsupportedOperationException("Unexpected visit in Variable!");
	}

	@Override
	public Attribute visit(AST_VariableExpField var, SymbolTable symTable) {
		//traverse the expression and evaluate it
		Attribute expAttr = var.exp.accept(this, symTable);

		if (expAttr.isNull()) {
			throw new RuntimeException("Null pointer exception, trying to access field of null expression.");
		}
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
		
		if (var.arrayExp instanceof AST_ExpNewTypeArray || 
		   (var.arrayExp instanceof AST_VariableExpArray && ((AST_VariableExpArray) var.arrayExp).isDeclarationExp)){
			var.isDeclarationExp = true;
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
		
		int dimension = var.isDeclarationExp ? arrExpAttr.getType().getDimension()+1 : arrExpAttr.getType().getDimension()-1;
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
	
	public IR_Exp visit(AST_Literal literal, IR_SymbolTable symTable) {
		IR_Exp litAttr = null;
		//Create the appropriate IR node according to the literal type
		if (literal.isInteger()){
			litAttr = new IR_Const((Integer)literal.value);
		}
		else if (literal.isString()){
			StringLabel newLabel = new StringLabel();
			litAttr = new IR_String(newLabel);
			stringLabelsMap.put(newLabel.getName(), (String)literal.value);	
		}
		else {
			//null is actually a zero value
			litAttr = new IR_Const(0);
		}
		
		return litAttr;
	}


	@Override
	public IR_Exp visit(AST_ClassDecl c, IR_SymbolTable symTable) {
		IR_SymbolTable classSymbolTable = new IR_SymbolTable(symTable, c.getClassName()); // creating symboltable for class

	
		return methodDeclListVisit(c.getClassMethods(), symTable);
	}

	@Override
	public IR_Exp visit(AST_Program program, IR_SymbolTable symTable) {
		
		for (AST_ClassDecl c : program.getClasses()) {
			if (c.extendedClassName == null) {
				classMap.put(c.className, new IR_ClassAttribute(c));
			} else {
				// if c extends a superclass, then the super must appear before
				// c in the list
				classMap.put(c.className, new IR_ClassAttribute(c, classMap.get(c.extendedClassName)));
			}
			dispachMethodsTablesMap.put(c.className, createMethodsDispachTable(classMap.get(c.className)));
			dispachFieldsTablesMap.put(c.className,createFieldsDispachTable(classMap.get(c.className)));
		}
		
		return classDeclListVisit(program.getClasses(), symTable);
	}
	
	private IR_Exp methodDeclListVisit(List<AST_Method> methodDeclList, IR_SymbolTable symTable){
		if (methodDeclList.size() == 1){
			return methodDeclList.get(0).accept(this, symTable);
		}
		AST_Method cls = methodDeclList.remove(0);
		return new IR_Seq(cls.accept(this, symTable),methodDeclListVisit(methodDeclList,symTable));
	}

	private IR_Exp classDeclListVisit(List<AST_ClassDecl> classDeclList,IR_SymbolTable symTable){
		if (classDeclList.size() == 1){
			return classDeclList.get(0).accept(this, symTable);
		}
		AST_ClassDecl cls = classDeclList.remove(0);
		return new IR_Seq(cls.accept(this, symTable),classDeclListVisit(classDeclList,symTable));
		
	}
	
	private Map<String, String> createFieldsDispachTable(IR_ClassAttribute classAttr) {
		Map<String,String> dispachTable = new HashMap<>();
		AST_ClassDecl currentClass = classAttr.getClassObject();
		
		for (String name : classAttr.getFieldOffsetMap().keySet()) {
			
			while (currentClass.extendedClassName != null && !currentClass.getFieldsNames().contains(name)) {
				currentClass = classMap.get(currentClass.extendedClassName).getClassObject();
			}
			
			if (currentClass.getFieldsNames().contains(name)) {
				dispachTable.put(name, currentClass.className);
			}
			
			currentClass = classAttr.getClassObject();
		}
		
		return dispachTable;
	}

	
	private Map<String,String> createMethodsDispachTable(IR_ClassAttribute classAttr) {
		Map<String,String> dispachTable = new HashMap<>();
		AST_ClassDecl currentClass = classAttr.getClassObject();
		
		for (String name : classAttr.getMethodOffsetMap().keySet()) {
			
			while (currentClass.extendedClassName != null && !currentClass.getMethodNames().contains(name)) {
				currentClass = classMap.get(currentClass.extendedClassName).getClassObject();
			}
			
			if (currentClass.getMethodNames().contains(name)) {
				dispachTable.put(name, currentClass.className);
			}
			
			currentClass = classAttr.getClassObject();
		}
		
		return dispachTable;
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
			
			addAttrsFromSuperClasses(cl, (ClassAttribute)program.getSymbols().get(cl.getClassName()), (ClassAttribute)program.getSymbols().get(superClass));

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
	
	
	private IR_Attribute findVar(String varName, IR_SymbolTable symTable) {
		IR_SymbolTable st = symTable;
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
