package ir.mipsgen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map;

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visit(IR_Const constant) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visit(IR_Epilogue epilogue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visit(IR_Function func) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visit(IR_JumpLabel jlabel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visit(IR_JumpRegister jreg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visit(IR_Label label) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visit(IR_Mem mem) {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visit(IR_Seq seq) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visit(IR_String str) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visit(IR_Temp temp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visit(IR_Binop binop) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
