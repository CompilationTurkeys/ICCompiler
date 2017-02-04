package ir.mipsgen;
import ic.ir.*;

/** An interface for traversing the IR Tree received from the parser.
 * The visitor passes down objects of type void
 * and propagates up objects of type ContextType.
 */
public interface IRPrinterVisitor {

	public void visit(IR_Call call); 
	public void visit(IR_Cjump jump); 
	public void visit(IR_Const constant); 
	public void visit(IR_Epilogue epilogue); 
	public void visit(IR_Function func); 
	public void visit(IR_JumpLabel jlabel); 
	public void visit(IR_JumpRegister jreg); 
	public void visit(IR_Label label); 
	public void visit(IR_Mem mem); 
	public void visit(IR_Move move); 
	public void visit(IR_NewObject newobj); 
	public void visit(IR_Prologue prologue); 
	public void visit(IR_Seq seq); 
	public void visit(IR_String str); 
	public void visit(IR_Temp temp); 
	public void visit(IR_Binop binop); 

}