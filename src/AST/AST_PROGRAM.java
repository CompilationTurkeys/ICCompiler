package AST;

import java.util.ArrayList;

public class AST_Program extends AST_Node
{
	public ArrayList<AST_ClassDecl> classList;

	public AST_Program(ArrayList<AST_ClassDecl> lst)
	{
		this.classList = lst;
	}
}