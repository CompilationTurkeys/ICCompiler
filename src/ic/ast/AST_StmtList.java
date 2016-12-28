package ic.ast;
import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

import java.util.ArrayList;

public class AST_StmtList extends AST_Stmt
{
	public ArrayList<AST_Stmt> stmtList;

	public AST_StmtList(ArrayList<AST_Stmt> body){
		this.stmtList = body == null ? new ArrayList<>() : body;
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

	@Override
	public String toString() {
		return "AST_StmtList [stmtList=" + stmtList + "]";
	}
	
	
}