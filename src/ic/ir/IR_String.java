package ic.ir;

import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

public class IR_String extends IR_Exp{
	StringLabel label;
	String value;
	
	public IR_String(StringLabel label, String value)
	{
		this.value = value;
		this.label = label;
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