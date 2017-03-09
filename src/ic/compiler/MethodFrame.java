package ic.compiler;

import ic.ast.Label;

public class MethodFrame extends Frame{

		public int numOfArguments;
		public int numOfLocalVars;
		
		public MethodFrame(Label label, int numOfArguments){
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
