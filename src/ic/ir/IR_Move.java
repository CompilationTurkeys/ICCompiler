package ic.ir;
import ic.ir.IR_Exp;
import ir.mipsgen.IRVisitor;
import ic.ast.BinaryOpTypes;
import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

public class IR_Move extends IR_Exp{
	
	public IR_Exp left;
	public IR_Exp right;

	
	public IR_Move(IR_Exp left, IR_Exp right)
	{
		this.left = left;
		this.right = right;
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