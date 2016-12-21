package AST;

import java.util.ArrayList;

public class AST_Field extends AST_FieldOrMethod
{
	public ArrayList<String> fieldNamesList;
	public AST_Type fieldsType;

	public AST_Field(AST_Type type, ArrayList<String> lst)
	{
		this.fieldNamesList = lst;
		this.fieldsType = type;
	}
}