package ic.compiler;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ic.ast.*;

public class IRTreeGenerator implements Visitor<IR_SymbolTable, IR_Exp> {

	public static final String FP="$fp";
	public static final String SP="$sp";
	private static final int THIS_OFFSET = 4;
	public static final String MAIN_LABEL = "Label_0_Main:";
	public static final String PRINT_LABEL = "Label_0_PrintInt:";
	private static IRTreeGenerator _instance;

	private IR_Exp irRoot;
	private IR_SymbolTable program;
	private AST_Node astRoot;

	// Map of string name to  string value for the the MIPS generation
	public Map<String, String> stringLabelsMap = new HashMap<>();

	// Maps the name of the classes in the program to their IRClassAttributes
	public Map<String, IR_ClassAttribute> classMap = new HashMap<>();

	// Maps the name of the classes to the corresponding ClassDecl objects.
	//private HashMap<String, HashMap<String, FormalList>> classToMethodFormalsMap = new HashMap<>();

	/*Map of the dispach tables of each class.
	  Each dispatch table is a map of function name and the offset of the function in the class
	 */
	public Map<String, Map<String,DispatchAttribute>> dispachMethodsTablesMap = new HashMap<>();
	public Map<String, Map<String,String>> dispachFieldsTablesMap = new HashMap<>();
	public Set<Label> labelSet = new HashSet<Label>();  

	public String mainClassName;
	public String printClassName;

	public IRTreeGenerator (AST_Node root)
	{
		this.astRoot= root;
		_instance = this;
	}

	public static IRTreeGenerator Get(){
		return _instance;
	}

	public IR_Exp generateIRTree() {
		this.program= new IR_SymbolTable(null, "");
		irRoot = astRoot.accept(this, program);
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
			String type = SemanticEvaluator.Get().callingExpMap.get(expr.leftExp);
			IR_Binop binop = new IR_Binop(leftExpResult, rightExpResult, expr.OP);
			binop.isStringBinop = type!=null && type.equals("string");
			return binop;
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
													new IR_Move(new IR_Temp(newTemp),new IR_Const(1),false),
													new IR_JumpLabel(endLabel))),
									new IR_Seq(
											new IR_Label(falseLabel),
											new IR_Seq(
													new IR_Move(new IR_Temp(newTemp),new IR_Const(0),false),
													new IR_JumpLabel(endLabel))))),
					new IR_Seq(new IR_Label(endLabel),new IR_Temp(newTemp)));
		default:
			return null;
		}
	}

	@Override
	public IR_Exp visit(AST_ExpNewClass expr, IR_SymbolTable symTable) {
		return new IR_NewObject(expr.className);
	}

	@Override
	public IR_Exp visit(AST_ExpNewTypeArray expr, IR_SymbolTable symTable) {
		IR_Exp size = expr.sizeExpression.accept(this, symTable);
		return new IR_NewArray(size);
	}

	@Override

	public IR_Exp visit(AST_StmtVarAssignment stmt, IR_SymbolTable symTable) {
		IR_Exp assignExp = stmt.assignExp.accept(this, symTable);
		stmt.var.isAssigned = false;
		IR_Exp var = stmt.var.accept(this,symTable);
		return new IR_Move(var, assignExp, true);
	}

	@Override
	public IR_Exp visit(AST_StmtList stmts, IR_SymbolTable symTable) {
		IR_Exp result;

		if (stmts.stmtList.isEmpty()){
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

		symTable.getFrame().incNumOfLocalVars();
		FrameMember newMem = new FrameMember(-(symTable.getFrame().numOfLocalVars*MethodFrame.WORD_SIZE));
		symTable.getSymbols().put(stmt.varName, new IR_Attribute(newMem, stmt.varType));

		if (stmt.assignedExp != null){
			//traverse var expression
			return new IR_Move(IRExpAssign(newMem),
					stmt.assignedExp.accept(this, symTable),true);		
		}

		return new IR_Move(IRExpAssign(newMem),new IR_Const(0),true);	

	}

	private IR_Binop IRExpAssign(FrameMember frameMember)
	{
		return new IR_Binop(new IR_Const(frameMember.offset), 
				new IR_Temp(new SpecialRegister(FP)),
				BinaryOpTypes.PLUS);
	}



	@Override
	public IR_Exp visit(AST_StmtReturn stmt, IR_SymbolTable symTable) {
		Register returnReg = new SpecialRegister("$v0");
		IR_Exp epilogue  = new IR_Epilogue(symTable.getFrame().getFrameSize());
		
		if (stmt.returnExp !=null) {
			IR_Exp returnExp = stmt.returnExp.accept(this, symTable);
			
			return new IR_Seq(new IR_Move(new IR_Temp(returnReg), returnExp,false),
								epilogue);
		}
		else{
			return epilogue;
		}
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

		IR_Exp condFirst = null, body = null, condSecond = null;

		TempLabel loop = new TempLabel("while_loop");
		TempLabel exitLoop = new TempLabel("while_exit_loop");

		condFirst = stmt.cond.accept(this, symTable);
		condSecond = stmt.cond.accept(this, symTable);
		
		if (stmt.body != null){
			IR_SymbolTable newScopeTable = new IR_SymbolTable(symTable, symTable.getClassName());
			symTable.addChild(stmt.body, newScopeTable);
			body = stmt.body.accept(this, newScopeTable);
			symTable.getChildren().remove(stmt.body);
		}

		return
				new IR_Seq(
						new IR_Seq(
								new IR_Cjump(
										BinaryOpTypes.NEQUALS,
										condFirst,
										new IR_Const(0),
										loop,
										exitLoop),
								new IR_Seq(
										new IR_Label(loop),
										new IR_Seq(
												body, 
												new IR_Cjump(
														BinaryOpTypes.NEQUALS,
														condSecond,
														new IR_Const(0),
														loop,
														exitLoop)))),
						new IR_Label(exitLoop));

	}


	@Override
	public IR_Exp visit(AST_VirtualCall call, IR_SymbolTable symTable) {
		String callingExpType;
		IR_Exp callingExp;

		if (call.callingExp != null){
			
			callingExpType = SemanticEvaluator.Get().callingExpMap.get(call.callingExp);
			callingExp = call.callingExp.accept(this, symTable);
		}
		else{
			callingExp = null;
			callingExpType = symTable.getClassName();
		}

		LinkedList<IR_Exp> argsList = new LinkedList<>();
		if (call.argList != null){
			call.argList.stream().forEach(arg -> argsList.addLast(arg.accept(this,symTable)));
		}

		IR_Exp boundariesCheck = null;

		if (callingExp == null){
			callingExp = new IR_Mem(
					new IR_Binop(
							new IR_Temp(new SpecialRegister(FP)),
							new IR_Const(THIS_OFFSET),
							BinaryOpTypes.PLUS)); 
		}
		else{

			Label access_violation_label= new SpecialLabel("Label_0_Access_Violation");
			Label accessViolationCallLabel = new TempLabel("AccessViolation");
			Label okLabel = new TempLabel("AllOK");
			IR_Exp checkInitialization = new IR_Cjump(BinaryOpTypes.EQUALS, callingExp, new IR_Const(0),
					accessViolationCallLabel, okLabel);
			if (!call.funcName.equals("printInt") && !call.funcName.equals("main")){
				boundariesCheck =	new IR_Seq(
						checkInitialization,
						new IR_Seq(
								new IR_Label(accessViolationCallLabel),
								new IR_Seq(
										new IR_JumpLabel(access_violation_label),
										new IR_Label(okLabel))));
				call.hasAccessViolationCheck = true;
				
			}
			callingExp = call.callingExp.accept(this, symTable);

		}
		
		
		argsList.addLast(callingExp);
		Label newFuncLabel;
		if (call.funcName.equals("main")){
			newFuncLabel = new SpecialLabel(MAIN_LABEL);
		}
		else if (call.funcName.equals("printInt")){
			newFuncLabel = new SpecialLabel(PRINT_LABEL);
		}
		else {
			newFuncLabel = new TempLabel(call.funcName, callingExpType);
		}
		return boundariesCheck == null ? new IR_Call(newFuncLabel, argsList) 
				: new IR_Seq(boundariesCheck, new IR_Call(newFuncLabel, argsList));

	}

	@Override
	public IR_Exp visit(AST_Variable var, IR_SymbolTable symTable) {
		throw new UnsupportedOperationException("Unexpected visit in Variable!");
	}

	@Override
	public IR_Exp visit(AST_VariableExpField var, IR_SymbolTable symTable) {

		String varExpType;
		IR_Exp varExp;

		var.hasAccessViolationCheck = true;
		varExpType = SemanticEvaluator.Get().callingExpMap.get(var.exp);
		
		//if (var.exp instanceof AST_Variable && (varExpType.equals("int") || varExpType.equals("string")) ){
		//	((AST_Variable)var.exp).isAssigned = false;
		//}
		IR_Exp varExp2;
		varExp = var.exp.accept(this, symTable);
		if (var.exp.hasAccessViolationCheck){
			 varExp2 = ((IR_Seq) varExp).rightExp;
		}
		else {
			varExp2 =  var.exp.accept(this,symTable);
		}
		
		Label access_violation_label= new SpecialLabel("Label_0_Access_Violation");
		Label okLabel = new TempLabel("AllOK");
		Label accessViolationCallLabel = new TempLabel("AccessViolation");

		IR_Exp checkInitialization = new IR_Cjump(BinaryOpTypes.EQUALS, varExp, new IR_Const(0),
				accessViolationCallLabel, okLabel);

		int fieldOffset = classMap.get(varExpType).getFieldOffset(var.fieldName);

		IR_Exp varSubTree = null;
		if (var.isAssigned){
			varSubTree = new IR_Mem(
					new IR_Binop(
							new IR_Const((fieldOffset)*MethodFrame.WORD_SIZE),
							varExp2,
							BinaryOpTypes.PLUS));
		}
		else{
			varSubTree = 	new IR_Binop(
					new IR_Const((fieldOffset)*MethodFrame.WORD_SIZE),
					varExp2,
					BinaryOpTypes.PLUS);
		}

		return new IR_Seq(
				new IR_Seq(
						checkInitialization,
						new IR_Seq(
								new IR_Label(accessViolationCallLabel),
								new IR_Seq(
										new IR_JumpLabel(access_violation_label),
										new IR_Label(okLabel)))),

				varSubTree);
	}

	@Override
	public IR_Exp visit(AST_VariableExpArray var, IR_SymbolTable symTable) {
		
		IR_Exp arrExp2 = null,arrExp3 =null ,arrExp = null;
		boolean isVarIDExp = false;
		if (var.arrayExp instanceof AST_Variable){
			
			if (var.arrayExp instanceof AST_VariableID){
				isVarIDExp = true;
			}
			else {
				((AST_Variable)var.arrayExp).isAssigned = false ;
			}
			
		}
		
		arrExp = var.arrayExp.accept(this, symTable);
		if (var.arrayExp instanceof AST_VariableExpArray || var.arrayExp.hasAccessViolationCheck){
			
			//arrExp1 = var.arrayExp.accept(this, symTable);
			arrExp2 = ((IR_Seq) arrExp).rightExp;
		}
		else{
			if (isVarIDExp){
				//((AST_Variable)var.arrayExp).isAssigned = false ;
				arrExp3 =  ((IR_Mem)arrExp).irNode; //var.arrayExp.accept(this, symTable);
			}
		}
		
		IR_Exp arrIndex = new IR_Binop(new IR_Binop(var.arraySize.accept(this, symTable),new IR_Const(1)
				,BinaryOpTypes.PLUS),new IR_Const(Integer.BYTES),BinaryOpTypes.TIMES);

		Label access_violation_label= new SpecialLabel("Label_0_Access_Violation");
		Label accessViolationCallLabel = new TempLabel("AccessViolation");
		
		IR_Exp initializationCheck = null;
		//Initialization check
		//if (arrExp2 == null){
		initializationCheck = new IR_Cjump(BinaryOpTypes.EQUALS,arrExp,new IR_Const(0)
					,accessViolationCallLabel,null);
		//}
		//else{
		//	initializationCheck = new IR_Cjump(BinaryOpTypes.EQUALS,arrExp1,new IR_Const(0)
		//			,accessViolationCallLabel,null);
		//}

		Label isOkLabel = new TempLabel("OK");
		Label isOkJumpLabel = new SpecialLabel(isOkLabel._name.substring(0, isOkLabel._name.length()-1));
		
		IR_Exp checkSubscriptGeZero = 
				new IR_Cjump(BinaryOpTypes.LT,arrIndex,new IR_Const(0),accessViolationCallLabel,null);

		
		IR_Exp checkArrayLeMemorySizeAllocated = null;
		if (arrExp2 == null){
			checkArrayLeMemorySizeAllocated = new IR_Cjump(BinaryOpTypes.GTE,
					arrIndex,new IR_Mem(arrExp),accessViolationCallLabel,null);
		}
		else{
			checkArrayLeMemorySizeAllocated = new IR_Cjump(BinaryOpTypes.GTE,
					arrIndex,new IR_Mem(arrExp2),accessViolationCallLabel,null);
		}


		IR_Exp boundariesChecks =  new IR_Seq(
				initializationCheck,
				new IR_Seq(
						checkSubscriptGeZero,
						new IR_Seq(
								checkArrayLeMemorySizeAllocated,
								new IR_Seq(
										new IR_JumpLabel(isOkJumpLabel),
										new IR_Seq(
												new IR_Label(accessViolationCallLabel),
												new IR_Seq(
														new IR_JumpLabel(access_violation_label),
														new IR_Label(isOkLabel)))))));

		IR_Exp varSubTree = null;
		
		if (var.isAssigned){
			if (arrExp2 == null){
				varSubTree = new IR_Mem(new IR_Binop(arrExp,arrIndex,BinaryOpTypes.PLUS));
			}
			else{
				arrExp2 = var.arrayExp instanceof AST_VariableExpArray ? new IR_Mem(arrExp2) : arrExp2;
				varSubTree = new IR_Mem(new IR_Binop(arrExp2,arrIndex,BinaryOpTypes.PLUS));
			}
		}
		else{
			if (arrExp2 == null){
				arrExp = var.arrayExp instanceof AST_VariableExpArray ? new IR_Mem(arrExp) : arrExp;
				varSubTree = new IR_Binop(arrExp,arrIndex,BinaryOpTypes.PLUS);
			}
			else{
				arrExp2 = var.arrayExp instanceof AST_VariableExpArray ? new IR_Mem(arrExp2) : arrExp2;
				varSubTree = new IR_Binop(arrExp2,arrIndex,BinaryOpTypes.PLUS);
			}
			
		}

		var.hasAccessViolationCheck = true;
		return new IR_Seq(boundariesChecks, varSubTree);

	}


	@Override
	public IR_Exp visit(AST_VariableID var, IR_SymbolTable symTable) {

		int varOffset;
		IR_Attribute varAttr = findVar(var.fieldName, symTable);
		AST_Type varType ;

		IR_Exp varSubTree = null;

		if (varAttr != null){
			varType = varAttr.frameMemberType;
			varOffset = varAttr.frameMember.offset;

			if (var.isAssigned){
				varSubTree = new IR_Mem(
						new IR_Binop(
								new IR_Const(varOffset),
								new IR_Temp(new SpecialRegister(FP)),
								BinaryOpTypes.PLUS));
			}
			else{
				
				varSubTree = new IR_Binop(
								new IR_Const(varOffset),
								new IR_Temp(new SpecialRegister(FP)),
								BinaryOpTypes.PLUS);
			}

			return varSubTree;
		}
		else{
			varOffset = classMap.get(symTable.getClassName()).getFieldOffset(var.fieldName);
			varType = classMap.get(symTable.getClassName()).getFieldType(var.fieldName);

			//$fp+4 == this location
			IR_Exp thisAddr = new IR_Mem(
					new IR_Binop(
							new IR_Const(THIS_OFFSET),
							new IR_Temp(new SpecialRegister(FP)),
							BinaryOpTypes.PLUS));

			
			if (var.isAssigned){
				varSubTree = new IR_Mem(
								new IR_Binop(
										new IR_Const(varOffset*Frame.WORD_SIZE),
										thisAddr,
										BinaryOpTypes.PLUS));
			}
			else{
				varSubTree = new IR_Binop(
								new IR_Const(varOffset*Frame.WORD_SIZE),
								thisAddr,
								BinaryOpTypes.PLUS);
			}

			return varSubTree;
			
		}
	}


	@Override
	public IR_Exp visit(AST_Type type, IR_SymbolTable symTable) {
		return null;
	}

	@Override
	public IR_Exp visit(AST_FuncArgument funcArg, IR_SymbolTable symTable) {
		return null;
	}

	@Override
	public IR_Exp visit(AST_Field field, IR_SymbolTable symTable) {
		return null;
	}

	@Override
	public IR_Exp visit(AST_Method method, IR_SymbolTable symTable) {
		Label funcLabel;
		if (method.methodName.equals("main")){
			funcLabel = new SpecialLabel(MAIN_LABEL);
		}
		else if(method.methodName.equals("printInt")){
			funcLabel = new SpecialLabel(PRINT_LABEL);
		}
		else {
			funcLabel = new TempLabel(method.methodName, symTable.getClassName());
		}
		labelSet.add(funcLabel);
		MethodFrame newFuncFrame = new MethodFrame(funcLabel, method.methodArgs.size()+1);

		IR_SymbolTable newSymTable = new IR_SymbolTable(symTable, symTable.getClassName(), newFuncFrame);
		symTable.addChild(method, newSymTable);

		if (method.methodArgs != null){
			// put args in symtable
			//newSymTable.put();

			int initialOffset = THIS_OFFSET + method.methodArgs.size()*MethodFrame.WORD_SIZE;

			for (AST_FuncArgument arg : method.methodArgs){
				IR_Attribute newArgAttr= new IR_Attribute(new FrameMember(initialOffset), arg.argType);
				newSymTable.getSymbols().put(arg.argName, newArgAttr);
				initialOffset-=Frame.WORD_SIZE;
			}

		}

		IR_Exp body = method.methodStmtList.accept(this, newSymTable);

		IR_Exp prologue = new IR_Prologue(newFuncFrame.getFrameSize());

		IR_Exp epilogue = new IR_Epilogue(newFuncFrame.getFrameSize());

		TempLabel skipFuncLabel = new TempLabel("skipFunc");
		return new IR_Seq(
				new IR_JumpLabel(skipFuncLabel),
				new IR_Seq(
						new IR_Function(prologue,body,epilogue,funcLabel),
						new IR_Label(skipFuncLabel)));
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

		symTable.getChildren().put(c, classSymbolTable);

		return methodDeclListVisit(c.getClassMethods(), classSymbolTable);
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
			Map<String,DispatchAttribute> methodDispatchTable = createMethodsDispachTable(classMap.get(c.className));
			if (!methodDispatchTable.isEmpty()){
				dispachMethodsTablesMap.put(c.className, methodDispatchTable);
			}
			dispachFieldsTablesMap.put(c.className,createFieldsDispachTable(classMap.get(c.className)));
		}
		List<AST_ClassDecl> lst = program.getClasses();
		return classDeclListVisit(lst, symTable);
	}

	private IR_Exp methodDeclListVisit(List<AST_Method> methodDeclList, IR_SymbolTable symTable){
		if (methodDeclList.size() == 1){
			return methodDeclList.get(0).accept(this, symTable);
		}
		else if (methodDeclList.size() == 0){
			return null;
		}
		AST_Method method = methodDeclList.remove(0);
		return new IR_Seq(method.accept(this, symTable),methodDeclListVisit(methodDeclList,symTable));
	}

	private IR_Exp classDeclListVisit(List<AST_ClassDecl> classDeclList, IR_SymbolTable symTable){
		if (classDeclList.size() == 1){
			return classDeclList.get(0).accept(this, symTable);
		}
		AST_ClassDecl cls = classDeclList.remove(0);
		return new IR_Seq(cls.accept(this, symTable),classDeclListVisit(classDeclList,symTable));
	}

	private Map<String, String> createFieldsDispachTable(IR_ClassAttribute classAttr) {
		Map<String,String> dispachTable = new LinkedHashMap<>();
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


	private Map<String,DispatchAttribute> createMethodsDispachTable(IR_ClassAttribute classAttr) {
		Map<String,DispatchAttribute> dispachTable = new LinkedHashMap<>();
		AST_ClassDecl currentClass = classAttr.getClassObject();


		for (String name : classAttr.getMethodOffsetMap().keySet()) {

			//get main class name
			if (name.equals("main") && mainClassName == null ){
				mainClassName = currentClass.className;
			}

			while (currentClass.extendedClassName != null && !currentClass.getMethodNames().contains(name)) {
				currentClass = classMap.get(currentClass.extendedClassName).getClassObject();
			}

			//main and printInt are static methods
			if (currentClass.getMethodNames().contains(name) && 
					!name.equals("main") && !name.equals("printInt")) {
					
				DispatchAttribute newAttr = new DispatchAttribute(currentClass.className, classAttr.getMethodOffset(name));
				dispachTable.put(name, newAttr);
				//dispatchOffset++;
			}

			currentClass = classAttr.getClassObject();
		}

		return dispachTable;
	}

	/**
	 * Non visit functions 
	 * */


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

	@Override
	public IR_Exp visit(AST_ExpParen expr, IR_SymbolTable symTable) {
		return expr.exp.accept(this,symTable);
	}

}
