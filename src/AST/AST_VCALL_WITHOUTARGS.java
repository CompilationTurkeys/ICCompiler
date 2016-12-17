package AST;


public class AST_VCALL_WITHOUTARGS extends AST_VCALL
{
	public String callingExpField;
	public AST_Exp callingExp;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_VCALL_WITHOUTARGS(String name, AST_Exp callingExp)
	{
		this.callingExpField = name;
		this.callingExp = callingExp;
	}
}