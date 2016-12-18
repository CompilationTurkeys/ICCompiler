package AST;

import java.util.ArrayList;

public class AST_VirtualCall extends AST_Node
{
	public AST_Exp 	callingExp;
	public ArrayList<AST_Exp> argList;
	public String callingExpField;

	public AST_VirtualCall(AST_Exp exp, String name, ArrayList<AST_Exp> expList)
	{
		this.argList = expList;
		this.callingExp = exp;
		this.callingExpField = name;
	}
}