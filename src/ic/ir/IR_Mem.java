package ic.ir;

import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

public class IR_Mem extends IR_Exp{
	
	IR_Exp irNode;
	
	public IR_Mem(IR_Exp irNode)
	{
		this.irNode = irNode;
	}

	@Override
	public void accept(PrinterVisitor visitor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <ContextType, ResultType> ResultType accept(Visitor<ContextType, ResultType> visitor, ContextType context) {
		return null;
	}
	
}