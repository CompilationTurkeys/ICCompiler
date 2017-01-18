package ic.compiler;

import java.util.ArrayList;

import ic.ir.TempLabel;

public class MethodFrame extends Frame{

		public int numOfArguments;
		public int numOfLocalVars;
		public ArrayList<FrameMember> argOffsets;
		
		public MethodFrame(TempLabel label, int numOfArguments){
			super(label);
			this.numOfArguments = numOfArguments;
			this.argOffsets = new ArrayList<>();
			initializeFrame();
		}
		
		public void initializeFrame(){
			numOfLocalVars = 0;
			for (int i=0; i < numOfArguments; i++){
				argOffsets.add(new FrameMember((numOfArguments+1-i)*WORD_SIZE));
			}
		}
		
		public void incNumOfLocalVars(){
			numOfLocalVars++;
		}
		
		public int getFrameSize(){
			return numOfLocalVars * WORD_SIZE;
		}
}
