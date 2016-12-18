package AST;

import java.util.ArrayList;

public class AST_ProgramClassDeclList extends AST_Program
{
	public ArrayList<AST_ClassDecl> classList;

	public AST_ProgramClassDeclList(ArrayList<AST_ClassDecl> lst)
	{
		this.classList = lst;
	}
}