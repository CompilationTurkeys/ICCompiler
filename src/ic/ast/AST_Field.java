package ic.ast;
import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

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
	
	@Override
	public <ContextType, ResultType> ResultType accept(
			Visitor<ContextType, ResultType>  visitor, ContextType context) {
		return visitor.visit(this, context);
	}
	
	@Override
	public void accept(PrinterVisitor visitor) {
		visitor.visit(this);
	}
	
	public ArrayList<String> getFieldNamesList() {
		return fieldNamesList;
	}
	

	public AST_Type getType() {
		return fieldsType;
	}

	@Override
	public String toString() {
		return "AST_Field [fieldNamesList=" + fieldNamesList + ", fieldsType=" + fieldsType + ", fieldMethod="
				+ fieldMethod + "]";
	}

	
	
	
}