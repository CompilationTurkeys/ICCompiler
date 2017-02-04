package ic.ir;

public class StringLabel extends Label {
	
	private static int counter = 0 ;
	private static final String LABEL_PREFIX = "Str_";

	
	public StringLabel(){
		_name =LABEL_PREFIX + ++counter +":";
	}
	
}
