package ic.compiler;

import ic.ir.TempLabel;

public class Frame {
	public int size;
	public TempLabel label;
	public static final int WORD_SIZE = 4;
	
	public Frame(TempLabel label){
		this.label = label;
	}
}
