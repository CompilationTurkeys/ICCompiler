package AST;

public class AST_ClassDeclNotExtended extends AST_ClassDecl
{
	public String className;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_ClassDeclNotExtended(String name)
	{
		this.className = name;
	}
}