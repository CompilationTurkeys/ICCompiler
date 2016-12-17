package AST;

import java.util.ArrayList;

public class AST_PROGRAM_CLASS_DECL_LIST extends AST_PROGRAM
{
	public ArrayList<AST_ClassDecl> lst;

	public AST_PROGRAM_CLASS_DECL_LIST(ArrayList<AST_ClassDecl> lst)
	{
		this.lst = lst;
	}
}