package AST;

import java.util.ArrayList;

public class AST_FieldList extends AST_Node
{
	public ArrayList<String> fieldNamesList;
	public AST_TYPE fieldsType;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_FieldList(AST_TYPE type, ArrayList<String> lst)
	{
		this.fieldNamesList = lst;
		this.fieldsType = type;
	}
}