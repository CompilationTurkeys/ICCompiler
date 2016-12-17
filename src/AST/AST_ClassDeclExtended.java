package AST;

public class AST_ClassDeclExtended extends AST_ClassDecl
{
	public String className;
	public String extendedClassName;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_ClassDeclExtended(String name,String otherName)
	{
		this.className = name;
		this.extendedClassName = otherName;
	}
}