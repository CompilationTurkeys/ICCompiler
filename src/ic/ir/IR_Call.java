package ic.ir;
import java.util.ArrayList;

import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

public class IR_Call extends IR_Exp{
	
	public TempRegister tr;
	public ArrayList<IR_Exp> args;
	
	public IR_Call(TempRegister tr, ArrayList<IR_Exp> args)
	{
		this.tr = tr;
		this.args = args;
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