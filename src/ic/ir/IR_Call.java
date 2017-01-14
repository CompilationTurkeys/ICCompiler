package ic.ir;
import java.util.Arrays;
import java.util.List;

import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

public class IR_Call extends IR_Exp{
	
	public TempLabel label;
	public List<IR_Exp> args;
	
	public IR_Call(TempLabel label, IR_Exp... args)
	{
		this.label = label;
		if (args != null){
			this.args = Arrays.asList(args);
		}
		else{
			this.args = null;
		}
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