package ic.ast;
import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

public class AST_Type extends AST_Node
{
	private int dimension = 0;
	private String typeName;
	private Object defVal;
	
	public AST_Type(PrimitiveDataTypes typeName, Integer dimension) {
		this.typeName = typeName.getName();
		this.dimension = (dimension == null) ? 0 : dimension.intValue();
		this.defVal = typeName.getDefaultValue();
	}

	public AST_Type(String typeName, Integer dimension) {
		this.typeName = typeName;
		this.dimension = (dimension == null) ? 0 : dimension.intValue();
		this.defVal = setDefVal(typeName);
	}

	public AST_Type(PrimitiveDataTypes typeName) {
		this(typeName,0);
	}

	public Object setDefVal(String typeName) {
		Object defVal = null;
		switch(typeName) {
			case "int":
				defVal = PrimitiveDataTypes.INT.getDefaultValue();
				break;
			case "string":
				defVal = PrimitiveDataTypes.STRING.getDefaultValue();
				break;
			case "void":
				defVal = PrimitiveDataTypes.VOID.getDefaultValue();
				break;
		}
		return defVal;
	}
	
	public String getName() {
		return typeName;
	}

	public Object getDefVal() { return defVal; }

	public int getDimension() {
		return dimension;
	}
	
	public void incDimension(){
		dimension++;
	}
	
	public boolean isPrimitive() {
		return  dimension == 0 && checkTypePrimitive() ;
	}
	
	public boolean checkTypePrimitive() {
		return ( typeName.equals(PrimitiveDataTypes.INT.getName()) ||
				typeName.equals(PrimitiveDataTypes.STRING.getName()) || 
				typeName.equals(PrimitiveDataTypes.VOID.getName()) );
	}
	
	public boolean isInt() {
		return  isPrimitive() && typeName.equals(PrimitiveDataTypes.INT.getName()) ;
	}
	
	public boolean isVoid() {
		return  isPrimitive() && typeName.equals(PrimitiveDataTypes.VOID.getName()) ;
	}
	
	public boolean isString() {
		return  isPrimitive() && typeName.equals(PrimitiveDataTypes.STRING.getName()) ;
	}
	
	@Override
	public String toString() {
		String s = typeName;
		for (int i = 0; i < dimension; i++) {
			s = s + "[]";
		}
		return s;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((defVal == null) ? 0 : defVal.hashCode());
		result = prime * result + dimension;
		result = prime * result + ((typeName == null) ? 0 : typeName.hashCode());
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
		AST_Type other = (AST_Type) obj;
		if (defVal == null) {
			if (other.defVal != null)
				return false;
		} else if (!defVal.equals(other.defVal))
			return false;
		if (dimension != other.dimension)
			return false;
		if (typeName == null) {
			if (other.typeName != null)
				return false;
		} else if (!typeName.equals(other.typeName))
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
	
}
