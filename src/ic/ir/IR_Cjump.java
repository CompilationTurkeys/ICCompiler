package ic.ir;
import ic.ir.IR_Exp;
import ic.ast.BinaryOpTypes;
import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

public class IR_Cjump extends IR_Exp{

	public BinaryOpTypes OP;
	public IR_Exp left;
	public IR_Exp right;
	public TempLabel jumpHereIfTrue;
	public TempLabel jumpHereIfFalse;

	
	public IR_Cjump(BinaryOpTypes OP, IR_Exp left, IR_Exp right, TempLabel jumpHereIfTrue, TempLabel jumpHereIfFalse)
	{
		this.OP = OP;
		this.left = left;
		this.right = right;
		this.jumpHereIfTrue = jumpHereIfTrue;
		this.jumpHereIfFalse = jumpHereIfFalse;
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