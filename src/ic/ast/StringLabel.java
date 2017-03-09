package ic.ast;

public class StringLabel extends Label {
	
	private static int counter = 0 ;
	private static final String LABEL_PREFIX = "String_";

	
	public StringLabel(){
		_name =LABEL_PREFIX + ++counter +":";
	}
	
}
