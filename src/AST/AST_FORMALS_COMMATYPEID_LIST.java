package AST;

import java.util.ArrayList;

public class AST_FORMALS_COMMATYPEID_LIST extends AST_FORMALS
{
	public ArrayList<AST_FuncArgument> list;
	public String name;
	public AST_TYPE type;
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_FORMALS_COMMATYPEID_LIST(String name, AST_TYPE type, ArrayList<AST_FuncArgument> list)
	{
		this.name = name;
		this.type = type;
		this.list = list;
	}
}