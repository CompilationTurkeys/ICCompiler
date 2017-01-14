package ic.ir;
import ic.ir.IR_Exp;
import ic.ast.BinaryOpTypes;
import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

public class IR_NewObject extends IR_Exp{
	
	public String className;

	
	public IR_NewObject(String className)
	{
		this.className = className;
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