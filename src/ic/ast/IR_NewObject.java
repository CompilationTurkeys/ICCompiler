package ic.ast;
import ic.ast.IR_Exp;
import ic.compiler.IRVisitor;
import ic.compiler.PrinterVisitor;

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
	public <ResultType> ResultType accept(IRVisitor<ResultType> visitor) {
		return visitor.visit(this);
	}

}