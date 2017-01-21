package ir.mipsgen;
import ic.ir.*;

/** An interface for traversing the AST received from the parser.
 * The visitor passes down objects of type ResultType
 * and propagates up objects of type ContextType.
 */
public interface IRVisitor<ResultType> {

	public ResultType visit(IR_Call call); 
	public ResultType visit(IR_Cjump jump); 
	public ResultType visit(IR_Const constant); 
	public ResultType visit(IR_Epilogue epilogue); 
	public ResultType visit(IR_Function func); 
	public ResultType visit(IR_JumpLabel jlabel); 
	public ResultType visit(IR_JumpRegister jreg); 
	public ResultType visit(IR_Label label); 
	public ResultType visit(IR_Mem mem); 
	public ResultType visit(IR_Move move); 
	public ResultType visit(IR_NewObject newobj); 
	public ResultType visit(IR_Prologue prologue); 
	public ResultType visit(IR_Seq seq); 
	public ResultType visit(IR_String str); 
	public ResultType visit(IR_Temp temp); 
	public ResultType visit(IR_Binop binop); 

}