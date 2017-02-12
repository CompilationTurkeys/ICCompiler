package ic.ir;
import ic.ir.IR_Exp;
import ir.mipsgen.IRVisitor;
import ic.ast.BinaryOpTypes;
import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

public class IR_Function extends IR_Exp{
	
	public IR_Exp prologue;
	public IR_Exp body;
	public IR_Exp epilogue;
	public Label tl;

	
	public IR_Function(IR_Exp prologue, IR_Exp body, IR_Exp epilogue, Label funcLabel)
	{
		this.prologue = prologue;
		this.body = body;
		this.epilogue = epilogue;
		this.tl = funcLabel;
	}

	@Override
	public void accept(PrinterVisitor visitor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <ResultType> ResultType accept(IRVisitor<ResultType> visitor) {
		return visitor.visit(this);
	}
	
}