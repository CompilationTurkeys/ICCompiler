package ic.ast;
import ic.ast.IR_Exp;
import ic.compiler.IRVisitor;
import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

public class IR_Cjump extends IR_Exp{

	public BinaryOpTypes OP;
	public IR_Exp left;
	public IR_Exp right;
	public Label jumpHereIfTrue;
	public Label jumpHereIfFalse;

	
	public IR_Cjump(BinaryOpTypes OP, IR_Exp left, IR_Exp right, Label jumpHereIfTrue, Label jumpHereIfFalse)
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
	public <ResultType> ResultType accept(IRVisitor<ResultType> visitor) {
		return visitor.visit(this);
	}

}