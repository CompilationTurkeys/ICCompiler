package ic.ir;
import ic.ir.IR_Exp;
import ic.ast.BinaryOpTypes;
import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

public class IR_JumpRegister extends IR_Exp{
	
	public TempRegister tr;

	
	public IR_JumpRegister(TempRegister tr)
	{
		this.tr = tr;
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