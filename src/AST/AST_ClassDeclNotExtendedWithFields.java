package AST;

import java.util.ArrayList;

public class AST_ClassDeclNotExtendedWithFields extends AST_ClassDecl
{
	public ArrayList<AST_FieldOrMethod> list;
	public String className;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_ClassDeclNotExtendedWithFields(String name, ArrayList<AST_FieldOrMethod> list)
	{
		this.className= name;
		this.list= list;
	}
}