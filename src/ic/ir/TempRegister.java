package ic.ir;

public class TempRegister {

	public String _name;
	private static int counter = 0 ;
	private static final String REGISTER_PREFIX = "_t";
	
	public TempRegister(){
		_name = REGISTER_PREFIX + ++counter;
	}
}
