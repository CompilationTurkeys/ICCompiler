package AST;

public class AST_TypePrimitive extends AST_Type
{
	public PrimitiveDataTypes type;
	
	public AST_TypePrimitive(PrimitiveDataTypes type){
		this.type = type;
	}
}