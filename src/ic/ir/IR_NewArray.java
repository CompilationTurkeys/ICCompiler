package ic.ir;

import ic.compiler.PrinterVisitor;
import ic.ir.IR_Exp;
import ir.mipsgen.IRVisitor;

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
