package ic.ir;
import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

public class IR_Seq extends IR_Exp{
	
	public IR_Exp leftExp;
	public IR_Exp rightExp;
	
	public IR_Seq(IR_Exp left,IR_Exp right)
	{
		this.leftExp = left;
		this.rightExp = right;
	}

	@Override
	public void accept(PrinterVisitor visitor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <ContextType, ResultType> ResultType accept(Visitor<ContextType, ResultType> visitor, ContextType context) {
		// TODO Auto-generated method stub
		return null;
	}
	
}