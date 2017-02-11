package ir.mipsgen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map;

import ic.ast.BinaryOpTypes;
import ic.compiler.DispatchAttribute;
import ic.compiler.Frame;
import ic.compiler.IRTreeGenerator;
import ic.ir.IR_Binop;
import ic.ir.IR_Call;
import ic.ir.IR_Cjump;
import ic.ir.IR_Const;
import ic.ir.IR_Epilogue;
import ic.ir.IR_Exp;
import ic.ir.IR_Function;
import ic.ir.IR_JumpLabel;
import ic.ir.IR_JumpRegister;
import ic.ir.IR_Label;
import ic.ir.IR_Mem;
import ic.ir.IR_Move;
import ic.ir.IR_NewArray;
import ic.ir.IR_NewObject;
import ic.ir.IR_Prologue;
import ic.ir.IR_Seq;
import ic.ir.IR_String;
import ic.ir.IR_Temp;
import ic.ir.Label;
import ic.ir.Register;
import ic.ir.SpecialLabel;
import ic.ir.SpecialRegister;
import ic.ir.TempLabel;
import ic.ir.TempRegister;

public class MipsGenerator implements IRVisitor<Register> {
	
	private IR_Exp irRoot;
	private PrintWriter fileWriter;
	
	private final static String EXIT_LABEL = "Label_0_Exit";
	private final static SpecialLabel STR_LEN_LABEL = new SpecialLabel("Label_0_StrLen");
	private static final SpecialLabel STR_CPY_LABEL = new SpecialLabel("Label_0_StrCpy");
	
	public MipsGenerator(IR_Exp root){
		this.irRoot = root;
	}
	
	public void generateCode(String outputFilename) throws FileNotFoundException{
		
		fileWriter = new PrintWriter(new File(outputFilename));
		
		MipsInit();
		
		irRoot.accept(this);
		
		MipsTerminate();
		
		generateAccessViolation();
		generateGetStringLength();
		generateStrCpy();
		generateExit();
		
		fileWriter.flush();
		
		fileWriter.close();
		
	}

	private void generateGetStringLength(){
		TempLabel loopStartLabel = new TempLabel("getLenLoop_Start");
		TempLabel loopEndLabel = new TempLabel("GetLenEnd");
		//count
		Register tmp = new TempRegister();
		fileWriter.format("%s:\n\n",STR_LEN_LABEL._name);
		fileWriter.format("%s\n\n" ,loopStartLabel.getName());
		fileWriter.format("\tli %s,0",tmp._name);
		fileWriter.format("%s\n\n", loopStartLabel.getNameWithoutDeclaration());
		fileWriter.write("\tlb $t0,0($a0)\n\n");
		//encountered a null byte ==> exit loop
		fileWriter.format("\tbeq  $t0,0 %s\n\n" ,loopEndLabel.getNameWithoutDeclaration());
		//count+=1
		fileWriter.format("\taddi %s,%s,1\n\n",tmp._name,tmp._name);		
		fileWriter.format("\taddi %s,%s,1\n\n","$a0","$a0");
		fileWriter.format("\tj %s\n\n" ,loopStartLabel.getNameWithoutDeclaration());
		
		fileWriter.format("%s\n\n" ,loopEndLabel.getName());
		fileWriter.format("\tmov %s,%s\n\n","$v0",tmp._name);
		fileWriter.write("\tjr $ra\n\n");
		
	}
	
	private void generateStrCpy(){
		//assuming $a0,$a1 are the two strings and $a2 is the new one
		TempLabel cpyFirstLabel = new TempLabel("cpy_first");
		TempLabel cpySecondLabel = new TempLabel( "cpy_second");
		TempLabel endLoopLabel = new TempLabel( "strcpy_end");
		
		fileWriter.write(STR_CPY_LABEL._name+":\n\n");
		fileWriter.format("%s\n\n", cpyFirstLabel.getName());
		fileWriter.write("\tlb $t0,0($a0)\n\n");
		//encountered a null byte ==> go to second string
		fileWriter.format("\tbeq  $t0,0 %s\n\n" ,cpySecondLabel.getNameWithoutDeclaration());
		//next byte
		fileWriter.format("\taddi %s,%s,1\n\n","$a0","$a0");
		fileWriter.format("\taddi %s,%s,1\n\n","$a2","$a2");		
		fileWriter.format("\tj %s\n\n" ,cpyFirstLabel.getNameWithoutDeclaration());
		
		//second string
		fileWriter.format("%s\n\n", cpySecondLabel.getName());
		fileWriter.write("\tlb $t0,0($a1)\n\n");
		//encountered a null byte ==> go to end loop
		fileWriter.format("\tbeq  $t0,0 %s\n\n" ,endLoopLabel.getNameWithoutDeclaration());
		//next byte
		fileWriter.format("\taddi %s,%s,1\n\n","$a1","$a1");
		fileWriter.format("\taddi %s,%s,1\n\n","$a2","$a2");
		
		fileWriter.format("%s\n\n" ,endLoopLabel.getName());
		fileWriter.format("\tsb 0,0(%s)\n\n","$a2");
		fileWriter.write("\tjr $ra\n\n");
		
	}

	private void generateExit() {
		fileWriter.write(EXIT_LABEL+":\n\n");
		
		//exit
		fileWriter.write("\tli $v0,10\n\n");
		fileWriter.write("\tsyscall\n\n");
		
	}

	private void generateAccessViolation() {

		fileWriter.write("Label_0_Access_Violation:\n\n");
		int magicNum = 666;
		
		//pass int arg to syscall
		fileWriter.format("\tli $a0,%d\n\n", magicNum); 
		//pass syscall number which is print_int
		fileWriter.write("\tli $v0,1\n\n"); 
		fileWriter.write("\tsyscall\n\n");
		
		fileWriter.format("\tj %s\n\n", EXIT_LABEL);
		
		
		
	}

	private void generatePrintInt() {

		//load first argument to syscall
		fileWriter.write("\tlw $a0,8("+ IRTreeGenerator.FP +")\n\n"); 
		//print int syscall is 1
		fileWriter.write("\tli $v0,1\n\n"); 
		// invoke syscall
		fileWriter.write("\tsyscall\n\n"); 
		// load space character as argument
		fileWriter.write("\tli $a0,32\n\n"); 
		//invoke print char syscall
		fileWriter.write("\tli $v0,11\n\n"); 
		//invoke syscall
		fileWriter.write("\tsyscall\n\n"); 
		
	}

	private void MipsTerminate() {
		
		fileWriter.write("\tli $v0,10\n");
		fileWriter.write("\tsyscall\n\n");
		
	}

	private void MipsInit() {
		//adding strings and dispatch tables to data segment
		addStrings();
		addMethodDispatchTables();

		//starting of text segment
		fileWriter.write(".text\n\n");
		fileWriter.write("main:\n\n");
		
		
		//TODO: CHECK
		//fileWriter.write("\taddi $sp,$sp,-40\n\n");

		//CREATE DUMMY OBJECT FOR MAIN
		
		//get allocation size
		int objAllocSize = IRTreeGenerator.Get().classMap.get(IRTreeGenerator.Get().mainClassName).getAllocSize();
		String vtableName = "VFTable_" + IRTreeGenerator.Get().mainClassName;	
		
		
		//pass argument for syscall
		fileWriter.format("\tli $a0,%d\n", objAllocSize);
		//call sbrk syscall for memory allocation
		fileWriter.format("\tli $v0,9\n");
		//invoke syscall
		fileWriter.format("\tsyscall\n\n");
				
		//save vftable addr in an object
		Register vtablePtr = new TempRegister();
		//Register vtableReg = new TempRegister();

		fileWriter.format("\tla %s,%s\n\n", vtablePtr._name, vtableName);
		//fileWriter.format("\tlw %s,0(%s)\n\n", vtableReg._name, vtablePtr._name);
		fileWriter.format("\tsw %s,0(%s)\n\n", vtablePtr._name, "$v0");
		
		objectInitialization();

		//pass this to main as parameter
		PushToStack("$v0");
		
		String adjustedMainName = null;
		for (TempLabel funcLabel : IRTreeGenerator.Get().labelSet){
			if ( "main".equals(funcLabel.getFuncName()) 
				&& IRTreeGenerator.Get().mainClassName.equals(funcLabel.getClassName())){
				adjustedMainName = funcLabel._name.substring(0, funcLabel._name.length()-1);
				break;
			}
		}
		
		
		fileWriter.write("\tjal " + adjustedMainName +"\n\n");
			
	}

	private void addMethodDispatchTables() {
		
		for (Map.Entry<String,Map<String,DispatchAttribute>> dispatchEntry : IRTreeGenerator.Get().dispachMethodsTablesMap.entrySet()){
			fileWriter.write("\tVFTable_"+dispatchEntry.getKey()+": .word ");
			int entriesCnt = 0;
			int mapSize = dispatchEntry.getValue().entrySet().size();
			
			for (Map.Entry<String, DispatchAttribute> methodsEntry: dispatchEntry.getValue().entrySet()){
				
				String adjustedFuncName = null;
				
				for (TempLabel funcLabel : IRTreeGenerator.Get().labelSet){
					if ( methodsEntry.getKey().equals(funcLabel.getFuncName()) 
						&& methodsEntry.getValue().className.equals(funcLabel.getClassName())){
						adjustedFuncName = funcLabel._name.substring(0, funcLabel._name.length()-1);
						break;
					}
				}
				
				if (entriesCnt != mapSize - 1){
					fileWriter.write(adjustedFuncName + ",");
				}
				else{
					fileWriter.write(adjustedFuncName);
				}
				entriesCnt++;
			}
			
			fileWriter.write("\n\n");
		}	
	}

	private void addStrings() {
		//strings are part of data segment
		fileWriter.write(".data\n");
		for (Map.Entry<String,String> strEntry : IRTreeGenerator.Get().stringLabelsMap.entrySet()){
			fileWriter.write("\t"+ strEntry.getKey() +" .asciiz " + strEntry.getValue() + "\n");
		}
	}

	public void PushToStack(String registerName){
		fileWriter.write("\taddi $sp,$sp,-4\n");
		fileWriter.format("\tsw %s,0($sp)\n\n", registerName);
	}
	
	public void PopFromStack(String registerName){
		fileWriter.format("\tlw %s,0($sp)\n", registerName);
		fileWriter.write("\taddi $sp,$sp,4\n\n");
	}
	
	@Override
	public Register visit(IR_Call call) {
		
		//TODO: saving registers by pushing them to the stack
		//for (int i=0;  i<=7 ; i++){
			PushToStack("$t7");
		//}
		
		//push arguments to stack
		if ( call.args != null ){
			for (IR_Exp arg : call.args){
				Register argValue = arg.accept(this);
				PushToStack(argValue._name);
			}
		}
				
		//get static class dispatch table
		Map<String, DispatchAttribute> classDispatchTable = IRTreeGenerator.Get().dispachMethodsTablesMap.get(call.label.getClassName());
		
		//get function Dispatch attribute by name
		DispatchAttribute funcDispatchAttr = classDispatchTable.get(call.label.getFuncName());
		
		//get func vtable offset
		int funcOffset = funcDispatchAttr.offset * Frame.WORD_SIZE;
		

		//Register that contains dispatch table addr
		String vtableReg = "$t0";
		fileWriter.format("\tla %s,%s\n\n", vtableReg, "VFTable_" + call.label.getClassName());
		//calculate vftable + offset
		fileWriter.format("\taddi %s,%s,%d\n\n", vtableReg, vtableReg, funcOffset);
		//func addr inside the vtable
		fileWriter.format("\tlw %s,0(%s)\n\n", vtableReg, vtableReg);
			
		//jump to function
		fileWriter.format("\tjalr %s\n\n", vtableReg);
	
		//pop function arguments if exists
		if ( call.args != null ){
			fileWriter.format("\taddi $sp,$sp,%d\n\n", call.args.size()*Frame.WORD_SIZE);
		}

		//TODO: restoring saved registers from the stack
		//for (int i=7;  i>=0 ; i--){
			PopFromStack("$t7");
		//}
		
		return new SpecialRegister("$v0");
	}

	@Override
	public Register visit(IR_Cjump jump) {
		
		Register leftOper = jump.left.accept(this);
		Register rightOper = jump.right.accept(this);

		String leftOperName = leftOper._name;
		String rightOperName = rightOper._name;
		
		String jumpToHereIfTrue = jump.jumpHereIfTrue._name.substring(0, jump.jumpHereIfTrue._name.length()-1);
		String jumpToHereIfFalse = null;
		
		if (jump.jumpHereIfFalse != null){
			jumpToHereIfFalse = jump.jumpHereIfFalse._name.substring(0, jump.jumpHereIfFalse._name.length()-1);
		}
		
		String opDescription = jump.OP.getOpDescreption();
		
		if (opDescription.equals(BinaryOpTypes.EQUALS.getOpDescreption())){
			fileWriter.format("\tbeq %s,%s,%s\n\n",leftOperName,rightOperName,jumpToHereIfTrue);
		}
		else if (opDescription.equals(BinaryOpTypes.NEQUALS.getOpDescreption())){
			fileWriter.format("\tbne %s,%s,%s\n\n",leftOperName,rightOperName,jumpToHereIfTrue);
		}
		else if (opDescription.equals(BinaryOpTypes.LT.getOpDescreption())){
			fileWriter.format("\tblt %s,%s,%s\n\n",leftOperName,rightOperName,jumpToHereIfTrue);
		}
		else if (opDescription.equals(BinaryOpTypes.GT.getOpDescreption())){
			fileWriter.format("\tbgt %s,%s,%s\n\n",leftOperName,rightOperName,jumpToHereIfTrue);
		}
		else if (opDescription.equals(BinaryOpTypes.LTE.getOpDescreption())){
			fileWriter.format("\tble %s,%s,%s\n\n",leftOperName,rightOperName,jumpToHereIfTrue);
		}
		else if (opDescription.equals(BinaryOpTypes.GTE.getOpDescreption())){
			fileWriter.format("\tbge %s,%s,%s\n\n",leftOperName,rightOperName,jumpToHereIfTrue);
		}
		
		if (jumpToHereIfFalse != null){
			fileWriter.format("\tj %s\n\n", jumpToHereIfFalse);	
		}
		return null;
	}

	@Override
	public Register visit(IR_Const constant) {
		//create register and store there a constant
		Register reg = new TempRegister();
		fileWriter.write("\tli "+ reg._name +"," + constant.value + "\n");
		return reg;
	}

	@Override
	public Register visit(IR_Epilogue epilogue) {

		//Save $ra in $t7
		fileWriter.write("\tmov $ra,$t7\n");
		//Restore stack pointer
		fileWriter.write("\tmov $sp,$fp\n");
		//Restore ebp
		PopFromStack("$fp");
		//Return to caller using saved return address
		fileWriter.write("\tjr $ra\n\n");
		return null;
		
	}

	@Override
	public Register visit(IR_Function func) {
		Register returnedValueReg = null;
		Register funcReturnReg = null;
		//print func label
		fileWriter.format("%s\n\n", func.tl._name);

		//generate prologue
		func.prologue.accept(this);
		
		
		//check if the func is PrintInt
		if (func.tl._name.toLowerCase().contains("printint")){
			generatePrintInt();
		}
		else{
			//traverse the body of the function
			returnedValueReg = func.body.accept(this);
			if (returnedValueReg != null)
			{
				//move return value to $v0
				fileWriter.format("\taddi $v0,%s,0\n\n", returnedValueReg);
				//return value in $v0
				funcReturnReg = new SpecialRegister("$v0");
			}
		}
		
		func.epilogue.accept(this);
		return funcReturnReg;
	}

	@Override
	public Register visit(IR_JumpLabel jlabel) {
		
		if (jlabel.tl == null){
			return null;
		}
		
		if (jlabel.tl instanceof SpecialLabel){
			fileWriter.format("\tj %s\n\n", jlabel.tl._name);
		}
		else{
			fileWriter.format("\tj %s\n\n", jlabel.tl._name.substring(0, jlabel.tl._name.length()-1));
		}
		return null;
	}

	@Override
	public Register visit(IR_JumpRegister jreg) {
		fileWriter.format("\tjr %s\n\n", jreg.tr._name);
		return null;
	}

	@Override
	public Register visit(IR_Label label) {
		//write label and return
		if (label._label != null){
			fileWriter.write(label._label._name + "\n\n");
		}
		return null;
	}

	@Override
	public Register visit(IR_Mem mem) {
		//register to store the loaded word from memory
		Register reg = new TempRegister();
		
		//compute mem address and place it in a register
		Register memReg = mem.irNode.accept(this);

		//get mem contents from address in memReg
		fileWriter.format("\tlw %s,0(%s)\n\n", reg._name, memReg._name);

		return reg;
	}

	@Override
	public Register visit(IR_Move move) {
		
		// if a move is a memory move
		if (move.isMemoryMove()){
			//calculate left expression and place it in destination
			Register dst = move.left.accept(this);

			//calculate right expression and place it in source
			Register src = move.right.accept(this);

			fileWriter.format("\tsw %s,0(%s)\n\n", src._name, dst._name);
		}
		//else its a register move
		else{
			//calculate left expression and place it in destination
			Register dst = move.left.accept(this);

			//calculate right expression and place it in source
			Register src = move.right.accept(this);

			//move one register to another
			fileWriter.format("\taddi %s,%s,0\n\n", dst._name, src._name);
		}

		return null;
	}

	private void objectInitialization(){
		
		//initialize object to zeroes
		Register indexReg = new TempRegister();
		//starting from 4
		fileWriter.format("\tli  %s,4\n\n", indexReg._name);
		
		Register upperLimReg = new TempRegister();
		
		Register sizeReg = new TempRegister();
		//upper limit of for loop
		fileWriter.format("\taddi  %s,%s,-4\n\n", sizeReg._name,  "$a0");

		fileWriter.format("\tmov  %s,%s\n\n", upperLimReg._name,  sizeReg._name);

		Label tempForLabel = new TempLabel("init_for");
		Label tempEndForLabel = new TempLabel("init_end_for");

		fileWriter.format("%s\n\n \tbgt %s,%s,%s\n\n", tempForLabel._name, indexReg._name, upperLimReg._name, 
				tempEndForLabel._name.substring(0,tempEndForLabel._name.length()-1));

		Register objElement = new TempRegister();
		fileWriter.format("\taddi  %s,%s,4\n\n", objElement._name, "$v0");
		Register nullValue = new TempRegister();
		fileWriter.format("\tli %s,0\n\n", nullValue._name);
		
		fileWriter.format("\tsw %s,0(%s)\n\n", nullValue._name, objElement._name);

		fileWriter.format("\taddi  %s,%s,4\n\n", indexReg._name,  indexReg._name);
		fileWriter.format("\tj  %s\n\n", tempForLabel._name.substring(0,tempForLabel._name.length()-1));

		fileWriter.format("%s\n\n", tempEndForLabel._name);
	}
	
	@Override
	public Register visit(IR_NewObject newobj) {
		//get tree generator instance
		IRTreeGenerator treeGenInstance = IRTreeGenerator.Get();
		//get allocation size
		int objAllocSize = treeGenInstance.classMap.get(newobj.className).getAllocSize();
		//Map<String,DispatchAttribute> classDispatchTable = treeGenInstance.dispachMethodsTablesMap.get(newobj.className);
		
		//set vtable name
		String vtableName = "VFTable_" + newobj.className;		
		//pass argument for syscall
		fileWriter.format("\tli $a0,%d\n\n", objAllocSize);
		//call sbrk syscall for memory allocation
		fileWriter.format("\tli $v0,9\n\n");
		//invoke syscall
		fileWriter.format("\tsyscall\n\n");
		
		Register vtablePtr = new TempRegister();
		//save the address of the relevant dispatch table as a first word
		fileWriter.format("\tla %s,%s\n\n", vtablePtr._name, vtableName);
		//fileWriter.format("\tlw %s,0(%s)\n\n", vtableReg._name, vtablePtr._name);
		fileWriter.format("\tsw %s,0(%s)\n\n", vtablePtr._name, "$v0");
		
		objectInitialization();
		
		return new SpecialRegister("$v0");
	}

	@Override
	public Register visit(IR_Prologue prologue) {
		//Save $ra in $t7
		fileWriter.write("\tmov $t7,$ra\n");
		//Save old frame pointer
		PushToStack("$fp");
		//Set new frame pointer
		fileWriter.write("\tmov $fp,$sp\n");
		//Allocate size for the frame
		fileWriter.write("\taddi $sp,$sp,-" + prologue.frameSize+"\n\n");
		return null;
	}

	@Override
	public Register visit(IR_Seq seq) {
		//traverse left son and return the result of right son
		seq.leftExp.accept(this);
		
		if (seq.rightExp == null){
			return null;
		}
		
		return seq.rightExp.accept(this);
	}

	@Override
	public Register visit(IR_String str) {
		//remove the ":" character from the label
		String strLabelName = str.label._name.substring(0,str.label._name.length()-1);
		//load the string address to a new register
		Register newReg = new TempRegister();
		fileWriter.format("\tla %s,%s\n\n", newReg._name, strLabelName);
		return newReg;
	}

	@Override
	public Register visit(IR_Temp temp) {
		return temp._register;
	}

	@Override
	public Register visit(IR_Binop binop) {
		// new temp for the result
		Register res = new TempRegister();
		
		// move left operand to t2  temporary
		Register tLeft = binop.leftExp.accept(this);
		
		// move right operand to t3  temporary
		Register tRight = binop.rightExp.accept(this);
		
		if (binop.isStringBinop){
			Register newStrSize = new TempRegister();
			//str len of str1
			fileWriter.format("\tla %s,%s\n\n", "$a0",tLeft._name);			
			fileWriter.format("\tmov %s,%s\n\n","$t5","$ra");
			fileWriter.format("\tjal %s\n\n", STR_LEN_LABEL._name);	
			fileWriter.format("\tmov %s,%s\n\n","$ra","$t5");
			fileWriter.format("\tmov %s,%s\n\n",newStrSize._name,"$v0");
			
			//str len of str2
			fileWriter.format("\tla %s,%s\n\n", "$a0",tRight._name);			
			fileWriter.format("\tmov %s,%s\n\n","$t5","$ra");
			fileWriter.format("\tjal %s\n\n", STR_LEN_LABEL._name);	
			fileWriter.format("\tmov %s,%s\n\n","$ra","$t5");
			fileWriter.format("\tadd %s,%s,%s\n\n",newStrSize._name,newStrSize._name,"$v0");
			
			//allocate space for new string
			
			//pass argument for syscall
			fileWriter.format("\tlw %s,0(%s)\n\n","$a0",newStrSize._name );
			//call sbrk syscall for memory allocation
			fileWriter.format("\tli $v0,9\n\n");
			//invoke syscall
			fileWriter.format("\tsyscall\n\n");
			
			
			//Concatenate two strings
			
			Register concatString = new TempRegister();
			
			fileWriter.format("\tmov %s,%s\n\n",concatString._name,"$v0");			

			fileWriter.format("\tla %s,%s\n\n", "$a0",tLeft._name);			
			fileWriter.format("\tla %s,%s\n\n", "$a1",tRight._name);
			fileWriter.format("\tla %s,%s\n\n", "$a2",concatString._name);			
			fileWriter.format("\tmov %s,%s\n\n","$t5","$ra");
			fileWriter.format("\tjal %s\n\n", STR_CPY_LABEL._name);	
			fileWriter.format("\tmov %s,%s\n\n","$ra","$t5");

			return concatString;
			
		}
		
		//generate binop asm operation
		fileWriter.format("\t%s %s,%s,%s\n\n",
				ConvertIRtoASMBinops(binop.OP),
				res._name,
				tLeft._name,
				tRight._name);
		
		return res;
	}
	
	//conversion of IR binops to ASM binops
	public String ConvertIRtoASMBinops(BinaryOpTypes binop)
	{
		String asmOpName = null;
		
		if (binop.getOpDescreption().equals(BinaryOpTypes.PLUS.getOpDescreption())){
			asmOpName = "add";
		}
		else if (binop.getOpDescreption().equals(BinaryOpTypes.TIMES.getOpDescreption())){
			asmOpName = "mul";
		}
		else if (binop.getOpDescreption().equals(BinaryOpTypes.DIVIDE.getOpDescreption())){
			asmOpName = "div";
		}
		else if (binop.getOpDescreption().equals(BinaryOpTypes.MINUS.getOpDescreption())){
			asmOpName = "sub";
		}
		
		return asmOpName;
	}

	@Override
	public Register visit(IR_NewArray array) {	
		
		//get allocation size
		Register arraySizeReg = array.arraySize.accept(this);
		//increment size by one (for access violation)
		fileWriter.format("\taddi %s,%s,1\n\n", arraySizeReg._name, arraySizeReg._name);
		//pass argument for syscall
		fileWriter.format("\tmov $a0,%s\n\n", arraySizeReg._name);
		//call sbrk syscall for memory allocation
		fileWriter.format("\tli $v0,9\n\n");
		//invoke syscall
		fileWriter.format("\tsyscall\n\n");
		
		//put the array size in the first place
		fileWriter.format("\tsw %s,0(%s)\n\n", arraySizeReg._name, "$v0");

		objectInitialization();
		
		//return array pointer
		return new SpecialRegister("$v0");
	}
	
}
