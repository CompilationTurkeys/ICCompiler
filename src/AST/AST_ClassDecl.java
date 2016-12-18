
package AST;

import java.util.ArrayList;

public class AST_ClassDecl extends AST_Node
{
	public ArrayList<AST_FieldOrMethod> classFieldsAndMethods;
	public String className;
	public String extendedClassName;

	public AST_ClassDecl(String name,String otherName,ArrayList<AST_FieldOrMethod> list)
	{
		this.className = name;
		this.extendedClassName = otherName;
		this.classFieldsAndMethods = list;
	}
}