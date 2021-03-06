package ic.ast;
import ic.ast.IR_Exp;
import ic.compiler.IRVisitor;
import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

public class IR_Binop extends IR_Exp{
	
	public BinaryOpTypes OP;
	public IR_Exp leftExp;
	public IR_Exp rightExp;
	
	public boolean isStringBinop;
	
	public IR_Binop(IR_Exp left,IR_Exp right,BinaryOpTypes OP)
	{
		this.leftExp = left;
		this.rightExp = right;
		this.OP = OP;
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