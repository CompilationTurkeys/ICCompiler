package ic.ir;

import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

public class IR_Temp extends IR_Exp{
	
	TempRegister _register;
	
	public IR_Temp(TempRegister reg)
	{
		this._register = reg;
	}

	@Override
	public void accept(PrinterVisitor visitor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <ContextType, ResultType> ResultType accept(Visitor<ContextType, ResultType> visitor, ContextType context) {
		//TODO
		return null;
	}
	
}