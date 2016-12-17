
package AST;

import java.util.ArrayList;

public class AST_ClassDeclExtendedWithFields extends AST_ClassDecl
{
	public ArrayList<AST_FieldOrMethod> classFieldsAndMethods;
	public String className;
	public String extendedClassName;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_ClassDeclExtendedWithFields(String name,String otherName,ArrayList<AST_FieldOrMethod> list)
	{
		this.className = name;
		this.extendedClassName = otherName;
		this.classFieldsAndMethods = list;
	}
}