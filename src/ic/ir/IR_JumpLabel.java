package ic.ir;
import ic.ir.IR_Exp;
import ir.mipsgen.IRVisitor;
import ic.compiler.PrinterVisitor;

public class IR_JumpLabel extends IR_Exp{
	
	public TempLabel tl;

	
	public IR_JumpLabel(TempLabel tl)
	{
		this.tl = tl;
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