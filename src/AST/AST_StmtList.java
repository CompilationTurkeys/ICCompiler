package AST;

import java.util.ArrayList;

public class AST_StmtList extends AST_Stmt
{
	public ArrayList<AST_Stmt> stmtList;

	public AST_StmtList(ArrayList<AST_Stmt> body){
		this.stmtList = body;
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