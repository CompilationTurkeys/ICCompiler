package ic.ast;
import ic.ast.IR_Exp;
import ic.compiler.IRVisitor;
import ic.compiler.PrinterVisitor;

public class IR_JumpLabel extends IR_Exp{
	
	public Label tl;

	
	public IR_JumpLabel(Label tl)
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