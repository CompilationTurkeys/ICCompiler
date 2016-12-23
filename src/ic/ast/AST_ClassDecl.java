package ic.ast;
import java.util.ArrayList;
import ic.compiler.Visitor;
import ic.compiler.PrinterVisitor;


public class AST_ClassDecl extends AST_Node
{
	public ArrayList<AST_FieldOrMethod> classFieldsAndMethods;
	public String className;
	public String extendedClassName;

	public AST_ClassDecl(String name,String otherName,ArrayList<AST_FieldOrMethod> list)
	{
		this.className = name;
		this.extendedClassName = otherName;
		this.classFieldsAndMethods = list;
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