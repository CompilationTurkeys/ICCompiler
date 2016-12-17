package AST;

public class AST_LITERAL_STRING extends AST_LITERAL
{
	public String str;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_LITERAL_STRING(String str)
	{
		this.str = str;
	}
}