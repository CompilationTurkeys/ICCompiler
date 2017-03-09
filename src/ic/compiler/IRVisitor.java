package ic.compiler;
import ic.ast.IR_Binop;
import ic.ast.IR_Call;
import ic.ast.IR_Cjump;
import ic.ast.IR_Const;
import ic.ast.IR_Epilogue;
import ic.ast.IR_Function;
import ic.ast.IR_JumpLabel;
import ic.ast.IR_JumpRegister;
import ic.ast.IR_Label;
import ic.ast.IR_Mem;
import ic.ast.IR_Move;
import ic.ast.IR_NewArray;
import ic.ast.IR_NewObject;
import ic.ast.IR_Prologue;
import ic.ast.IR_Seq;
import ic.ast.IR_String;
import ic.ast.IR_Temp;

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
	public ResultType visit(IR_NewArray array); 
}