package ic.compiler;

import ic.ast.AST_Type;

public class IR_Attribute {
	
	public FrameMember frameMember;
	public AST_Type frameMemberType;
	public boolean isInitialized;

	

	public IR_Attribute(FrameMember member, AST_Type memberType)
	{
		this.frameMember = member;
		this.frameMemberType = memberType;
	}
	
	
}