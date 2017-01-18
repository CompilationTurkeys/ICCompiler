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
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((fieldNamesList == null) ? 0 : fieldNamesList.hashCode());
		result = prime * result + ((fieldsType == null) ? 0 : fieldsType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		AST_Field other = (AST_Field) obj;
		if (fieldNamesList == null) {
			if (other.fieldNamesList != null)
				return false;
		} else if (!fieldNamesList.equals(other.fieldNamesList))
			return false;
		if (fieldsType == null) {
			if (other.fieldsType != null)
				return false;
		} else if (!fieldsType.equals(other.fieldsType))
			return false;
		return true;
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