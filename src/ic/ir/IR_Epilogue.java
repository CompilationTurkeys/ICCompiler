package ic.ir;

import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

public class IR_Epilogue extends IR_Exp{
	
	int frameSize;
	
	public IR_Epilogue(int frameSize)
	{
		this.frameSize = frameSize;
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