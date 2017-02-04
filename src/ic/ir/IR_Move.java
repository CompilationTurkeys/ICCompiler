package ic.ir;
import ic.ir.IR_Exp;
import ir.mipsgen.IRVisitor;
import ic.ast.BinaryOpTypes;
import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

public class IR_Move extends IR_Exp{
	
	public IR_Exp left;
	public IR_Exp right;

	private boolean isMemoryMove;
	
	public IR_Move(IR_Exp left, IR_Exp right,boolean isMemoryMove)
	{
		this.left = left;
		this.right = right;
		this.isMemoryMove = isMemoryMove;
	}

	@Override
	public void accept(PrinterVisitor visitor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <ResultType> ResultType accept(IRVisitor<ResultType> visitor) {
		return visitor.visit(this);
	}	
	
	public boolean isMemoryMove(){
		return isMemoryMove;
	}
	
	public boolean isRegistersMove(){
		return !isMemoryMove;
	}
}