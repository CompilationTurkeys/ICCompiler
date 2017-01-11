package ic.compiler;

import java.util.ArrayList;

import ic.ir.TempLabel;

public class ClassFrame extends Frame{

		public int numOfFields;
		public ArrayList<FrameMember> fieldOffsets;
		
		public ClassFrame(TempLabel label, int numOfFields){
			super(label);
			this.numOfFields = numOfFields;
			this.fieldOffsets = new ArrayList<>();
			initializeFrame();
		}
		
		public void initializeFrame(){
			size = (numOfFields+1)*WORD_SIZE;
			for (int i=0; i <= numOfFields; i++){
				fieldOffsets.add(new FrameMember(i*WORD_SIZE));
			}
		}
}
