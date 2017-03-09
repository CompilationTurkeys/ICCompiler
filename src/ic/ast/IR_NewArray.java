package ic.ast;

import ic.ast.IR_Exp;
import ic.compiler.IRVisitor;
import ic.compiler.PrinterVisitor;

public class IR_NewArray extends IR_Exp {

	public IR_Exp arraySize;
	
	public IR_NewArray(IR_Exp size){
		this.arraySize = size;
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
