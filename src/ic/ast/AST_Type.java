package ic.ast;
import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

public class AST_Type extends AST_Node
{
	private int dimension = 0;
	private String typeName;
	private boolean isPrimitive;
	
	public AST_Type(PrimitiveDataTypes typeName, Integer dimension) {
		this.typeName = typeName.getValue();
		this.dimension = (dimension == null) ? 0 : dimension.intValue();
		this.isPrimitive = true;
	}

	public AST_Type(String name, Integer dimension) {
		this.typeName = name;
		this.dimension = (dimension == null) ? 0 : dimension.intValue();
		this.isPrimitive = typeName.equals(PrimitiveDataTypes.INT.getValue()) ||
				typeName.equals(PrimitiveDataTypes.STRING.getValue()) || typeName.equals(PrimitiveDataTypes.VOID.getValue());
	}
	
	public AST_Type(PrimitiveDataTypes typeName) {
		this(typeName,0);
	}
	
	public String getName() {
		return typeName;
	}

	public int getDimension() {
		return dimension;
	}
	
	public void incDimension(){
		dimension++;
	}
	
	public boolean isPrimitive() {
		return isPrimitive;
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
		int result = 1;
		result = prime * result + dimension;
		result = prime * result + (isPrimitive ? 1231 : 1237);
		result = prime * result + ((typeName == null) ? 0 : typeName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AST_Type other = (AST_Type) obj;
		if (dimension != other.dimension)
			return false;
		if (isPrimitive != other.isPrimitive)
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