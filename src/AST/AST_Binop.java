package AST;

public class AST_Binop extends AST_Node
{
	public BinaryOpTypes type;
	
	public AST_Binop(BinaryOpTypes type){
		this.type = type;
	}
}