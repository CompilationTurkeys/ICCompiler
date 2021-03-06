package ic.ast;

public class TempLabel extends Label {
	
	private static int counter = 0 ;
	private static final String LABEL_PREFIX = "Label_";
	private String className = null;
	private String funcName = null;
	
	public TempLabel(String name){
		_name =LABEL_PREFIX + ++counter + "_" + name + ":";
	}
	
	public TempLabel(String funcName, String className){
		this.className = className;
		this.funcName = funcName;
		_name = LABEL_PREFIX + ++counter + "_" +  funcName + "_" + className + ":";
	}
	
	public String getFuncName(){
		return funcName;
	}
	
	public String getClassName(){
		return className;
	}
	
	public String getNameWithoutDeclaration(){
		return _name.substring(0,_name.length()-1);
	}
}
