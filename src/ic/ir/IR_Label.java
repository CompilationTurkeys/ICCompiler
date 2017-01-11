package ic.ir;

import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

public class IR_Label extends IR_Exp{
	
	public TempLabel _label;
	
	public IR_Label(TempLabel label)
	{
		this._label = label;
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