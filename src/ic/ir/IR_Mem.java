package ic.ir;
import ic.ast.AST_Exp;
import ic.ast.BinaryOpTypes;
import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

public class IR_Mem extends IR_Node{
	
	public BinaryOpTypes OP;
	public AST_Exp leftExp;
	public AST_Exp rightExp;
	
	public IR_Mem(IR_Exp exp)
	{
		this.exp = exp;
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