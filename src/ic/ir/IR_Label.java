package ic.ir;

import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

public class IR_Label extends IR_Node{
	
	public TempLabel _label;
	
	public IR_Label(String name)
	{
		this._label = new TempLabel(name);
	}

	@Override
	public void accept(PrinterVisitor visitor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <ContextType, ResultType> ResultType accept(Visitor<ContextType, ResultType> visitor, ContextType context) {
		//TODO
	}
	
}