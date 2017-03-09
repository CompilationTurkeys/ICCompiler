package ic.ast;

import ic.compiler.IRVisitor;
import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

public class IR_Mem extends IR_Exp{
	
	public IR_Exp irNode;
	
	public IR_Mem(IR_Exp irNode)
	{
		this.irNode = irNode;
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