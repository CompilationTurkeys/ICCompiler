package AST;

import java.util.ArrayList;

public class AST_FieldList extends AST_FieldOrMethod
{
	public ArrayList<String> fieldNamesList;
	public AST_Type fieldsType;

	public AST_FieldList(AST_Type type, ArrayList<String> lst)
	{
		this.fieldNamesList = lst;
		this.fieldsType = type;
	}
}