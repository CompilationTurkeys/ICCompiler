package AST;

public class AST_ExpNewClass extends AST_Exp
{
	public String className;
	
	public AST_ExpNewClass(String name)
	{
		this.className = name;
	}
}