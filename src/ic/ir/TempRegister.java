package ic.ir;

public class TempRegister extends Register {

	private static int counter = 0 ;
	private static final String REGISTER_PREFIX = "Temp_";
	
	public TempRegister(){
		super(REGISTER_PREFIX + ++counter);
	}
}
