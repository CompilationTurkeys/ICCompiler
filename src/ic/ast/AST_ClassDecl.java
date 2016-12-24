package ic.ast;
import java.util.ArrayList;
import ic.compiler.Visitor;
import ic.compiler.PrinterVisitor;


public class AST_ClassDecl extends AST_Node
{
	public ArrayList<AST_Field> classFields;

	public ArrayList<AST_Method> classMethods;

	public String className;
	public String extendedClassName;

	public AST_ClassDecl(String name,String otherName,ArrayList<AST_FieldOrMethod> list)
	{
		this.className = name;
		this.extendedClassName = otherName;
		classFields = new ArrayList<>();
		classMethods = new ArrayList<>();
		list.stream().filter(obj -> obj instanceof AST_Field)
		.forEach(obj -> classFields.add((AST_Field)obj));
		list.stream().filter(obj -> obj instanceof AST_Method)
		.forEach(obj -> classMethods.add((AST_Method)obj));
	}
	
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Override
	public <ContextType, ResultType> ResultType accept(
			Visitor<ContextType, ResultType>  visitor, ContextType context) {
		return visitor.visit(this, context);
	}
	
	public ArrayList<AST_Field> getClassFields() {
		return classFields;
	}
	
	@Override
	public void accept(PrinterVisitor visitor) {
		visitor.visit(this);
	}
	
	public String getExtendedClassName() {
		return extendedClassName;
	}
	
	public ArrayList<AST_Method> getClassMethods() {
		return classMethods;
	}
}