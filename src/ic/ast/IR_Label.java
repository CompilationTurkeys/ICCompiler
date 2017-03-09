package ic.ast;

import ic.compiler.IRVisitor;
import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

public class IR_Label extends IR_Exp{
	
	public Label _label;
	
	public IR_Label(Label label)
	{
		this._label = label;
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