package ic.ast;

import ic.compiler.IRVisitor;
import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

public class IR_Epilogue extends IR_Exp{
	
	public int frameSize;
	
	public IR_Epilogue(int frameSize)
	{
		this.frameSize = frameSize;
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