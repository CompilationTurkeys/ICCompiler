package ir.mipsgen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map;

import ic.ast.BinaryOpTypes;
import ic.compiler.DispatchAttribute;
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
import ic.ir.IR_NewObject;
import ic.ir.IR_Prologue;
import ic.ir.IR_Seq;
import ic.ir.IR_String;
import ic.ir.IR_Temp;
import ic.ir.Register;
import ic.ir.SpecialRegister;
import ic.ir.TempRegister;

public class MipsGenerator implements IRVisitor<Register> {
	
	private IR_Exp irRoot;
	private PrintWriter fileWriter;
	
	public MipsGenerator(IR_Exp root){
		this.irRoot = root;
	}
	
	public void generateCode(String outputFilename) throws FileNotFoundException{
		
		fileWriter = new PrintWriter(new File(outputFilename));
		
		MipsInit();
		
		irRoot.accept(this);
		
		MipsTerminate();
		
		MipsExternalFuncs();
		
		addMethodDispatchTables();
		
		fileWriter.close();
		
	}


	private void MipsExternalFuncs() {
		
		generatePrintInt();
		generateArrayAlloc();
		generateObjectAlloc();
	}

	private void generateArrayAlloc() {
		// TODO Auto-generated method stub
		
	}

	private void generateObjectAlloc() {
		// TODO Auto-generated method stub
		
	}

	private void generatePrintInt() {

		fileWriter.write("Label_PrintInt:\n\n");
		//load argument to syscall
		fileWriter.write("\tlw $a0,0("+ IRTreeGenerator.SP +")\n\n"); 
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
		//return to caller
		fileWriter.write("\tjr $ra\n\n");
		
	}

	private void MipsTerminate() {
		
		fileWriter.write("\tli $v0,10\n");
		fileWriter.write("\tsyscall\n");
		
	}

	private void MipsInit() {
		//adding strings to mips
		addStrings();
		
		//starting of text segment
		fileWriter.write(".text\n");
		fileWriter.write("main:\n");
		
		//TODO : Create new object and pass it as 'this' to main
		
		fileWriter.write("\tjal main_" + IRTreeGenerator.Get().mainClassName +"\n");
				
	}

	private void addMethodDispatchTables() {
		
		for (Map.Entry<String,Map<String,DispatchAttribute>> dispatchEntry : IRTreeGenerator.Get().dispachMethodsTablesMap.entrySet()){
			fileWriter.write("vtable_for_"+dispatchEntry.getKey()+": .word ");
			int entriesCnt = 0;
			int mapSize = dispatchEntry.getValue().entrySet().size();
			
			for (Map.Entry<String, DispatchAttribute> methodsEntry: dispatchEntry.getValue().entrySet()){
				if (entriesCnt != mapSize - 1){
					fileWriter.write(methodsEntry.getKey() +"_"+methodsEntry.getValue().className+",");
				}
				else{
					fileWriter.write(methodsEntry.getKey() +"_"+methodsEntry.getValue().className);
				}
				entriesCnt++;
			}
			
			fileWriter.write("\n");
		}	
	}

	private void addStrings() {
		//strings are part of data segment
		fileWriter.write(".data\n");
		for (Map.Entry<String,String> strEntry : IRTreeGenerator.Get().stringLabelsMap.entrySet()){
			fileWriter.write("\t"+ strEntry.getKey() +" .asciiz " + strEntry.getValue() + "\n");
		}
	}

	@Override
	public Register visit(IR_Call call) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visit(IR_Cjump jump) {
		
		Register leftOper = jump.left.accept(this);
		Register rightOper = jump.right.accept(this);

		String leftOperName = leftOper._name;
		String rightOperName = rightOper._name;

		String jumpToHereIfTrue = jump.jumpHereIfTrue._name;
		String jumpToHereIfFalse = jump.jumpHereIfFalse._name;
		
		String opDescription = jump.OP.getOpDescreption();
		
		if (opDescription.equals(BinaryOpTypes.EQUALS.getOpDescreption())){
			fileWriter.format("\tbeq %s, %s, %s\n\n",leftOperName,rightOperName,jumpToHereIfTrue);
			fileWriter.format("\tbne %s, %s, %s\n\n",leftOperName,rightOperName,jumpToHereIfFalse);
		}
		else if (opDescription.equals(BinaryOpTypes.NEQUALS.getOpDescreption())){
			fileWriter.format("\tbne %s, %s, %s\n\n",leftOperName,rightOperName,jumpToHereIfTrue);
			fileWriter.format("\tbeq %s, %s, %s\n\n",leftOperName,rightOperName,jumpToHereIfTrue);
		}
		else if (opDescription.equals(BinaryOpTypes.LT.getOpDescreption())){
			fileWriter.format("\tblt %s, %s, %s\n\n",leftOperName,rightOperName,jumpToHereIfTrue);
			fileWriter.format("\tbge %s, %s, %s\n\n",leftOperName,rightOperName,jumpToHereIfTrue);
		}
		else if (opDescription.equals(BinaryOpTypes.GT.getOpDescreption())){
			fileWriter.format("\tbgt %s, %s, %s\n\n",leftOperName,rightOperName,jumpToHereIfTrue);
			fileWriter.format("\tble %s, %s, %s\n\n",leftOperName,rightOperName,jumpToHereIfTrue);
		}
		else if (opDescription.equals(BinaryOpTypes.LTE.getOpDescreption())){
			fileWriter.format("\tble %s, %s, %s\n\n",leftOperName,rightOperName,jumpToHereIfTrue);
			fileWriter.format("\tbgt %s, %s, %s\n\n",leftOperName,rightOperName,jumpToHereIfTrue);
		}
		else if (opDescription.equals(BinaryOpTypes.GTE.getOpDescreption())){
			fileWriter.format("\tbge %s, %s, %s\n\n",leftOperName,rightOperName,jumpToHereIfTrue);
			fileWriter.format("\tblt %s, %s, %s\n\n",leftOperName,rightOperName,jumpToHereIfTrue);
		}
		
		return null;
	}

	@Override
	public Register visit(IR_Const constant) {
		//create register and store there a constant
		Register reg = new TempRegister();
		fileWriter.write("\tli "+ reg._name +", " + constant.value + "\n");
		return reg;
	}

	@Override
	public Register visit(IR_Epilogue epilogue) {
		
		//Remove the current local variables frame
		fileWriter.write("addi $sp, $sp, +" + epilogue.frameSize +"\n");
		//Load return address from stack
		fileWriter.write("\tlw $ra, 8($sp)\n");
		//Restore old frame pointer from stack
		fileWriter.write("\tlw $fp, 4($sp)\n");
		//Reset stack pointer
		fileWriter.write("\tadd $sp, $sp, 8\n");
		//Return to caller using saved return address
		fileWriter.write("\tjr $ra\n");
		return null;
	}

	@Override
	public Register visit(IR_Function func) {
		Register returnedValueReg;
		//print func label
		fileWriter.format("%s:\n\n", func.tl._name);

		//generate prologue
		func.prologue.accept(this);
		
		//traverse the body of the function
		returnedValueReg = func.body.accept(this);

		if (returnedValueReg != null)
		{
			//move return value to $v0
			fileWriter.format("\taddi $v0,%s,0\n\n", returnedValueReg);
			//generate epilogue
			func.epilogue.accept(this);
			//return value in $v0
			return new SpecialRegister("$v0");
		}
		else
		{
			//generate epilogue
			func.epilogue.accept(this);
			return null;
		}
	}

	@Override
	public Register visit(IR_JumpLabel jlabel) {
		fileWriter.format("\tj %s\n\n", jlabel.tl._name.substring(0, jlabel.tl._name.length()-1));
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
		fileWriter.write(label._label._name + ":\n\n");
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visit(IR_NewObject newobj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visit(IR_Prologue prologue) {
		//Set new stack pointer
		fileWriter.write("\tsub $sp, $sp, 8\n");
		//Save return address
		fileWriter.write("\tsw $ra, 8($sp)\n");
		//Save old frame pointer
		fileWriter.write("\tsw $fp, 4($sp)\n");
		//Set new frame pointer
		fileWriter.write("\tadd $fp, $sp, 8\n");
		//Allocate size for the frame
		fileWriter.write("addi $sp, $sp, -" + prologue.frameSize+"\n");
		return null;
	}

	@Override
	public Register visit(IR_Seq seq) {
		//traverse left son and return the result of right son
		seq.leftExp.accept(this);
	 	return seq.rightExp.accept(this);
	}

	@Override
	public Register visit(IR_String str) {
		//remove the ":" character from the label
		String strLabelName = str.label._name.substring(0,str.label._name.length()-1);
		//load the string address to a new register
		Register newReg = new TempRegister();
		fileWriter.format("\tla %s, %s\n\n", newReg._name, strLabelName);
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
		
		//generate binop asm operation
		fileWriter.format("\t%s %s,%s,%s\n\n",
				ConvertIRtoASMBinops(binop.OP),
				res._name,
				tLeft._name,
				tRight._name);

		return res;
	}
	
	
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
	
}
