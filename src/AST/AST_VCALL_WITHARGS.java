package AST;

import java.util.ArrayList;

public class AST_VCALL_WITHARGS extends AST_VCALL
{
	public AST_Exp 	callingExp;
	public ArrayList<AST_Exp> argList;
	public String callingExpField;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_VCALL_WITHARGS(AST_Exp exp, String name, ArrayList<AST_Exp> expList)
	{
		this.argList = expList;
		this.callingExp = exp;
		this.callingExpField = name;
	}
}