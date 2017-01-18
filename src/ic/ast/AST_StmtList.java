package ic.ast;
import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

import java.util.ArrayList;

public class AST_StmtList extends AST_Stmt
{
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((stmtList == null) ? 0 : stmtList.hashCode());
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
		AST_StmtList other = (AST_StmtList) obj;
		if (stmtList == null) {
			if (other.stmtList != null)
				return false;
		} else if (!stmtList.equals(other.stmtList))
			return false;
		return true;
	}

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