package ic.compiler;

import ic.ir.TempLabel;

public class MethodFrame extends Frame{

		public int numOfArguments;
		public int numOfLocalVars;
		
		public MethodFrame(TempLabel label, int numOfArguments){
			super(label);
			this.numOfArguments = numOfArguments;
		}
		
		public void incNumOfLocalVars(){
			numOfLocalVars++;
		}
		
		public int getFrameSize(){
			return numOfLocalVars * WORD_SIZE;
		}
}
