package ic.ir;

public class TempLabel extends Label {
	
	private static int counter = 0 ;
	private static final String LABEL_PREFIX = "Label_";

	
	public TempLabel(String name){
		_name =LABEL_PREFIX + ++counter + "_" + name + ":";
	}
	
	public TempLabel(String funcName, String className){
		_name = funcName + "_" + className + ":";
	}
}
