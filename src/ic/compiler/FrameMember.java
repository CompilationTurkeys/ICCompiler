package ic.compiler;
// wrapper for local vars/args
public class FrameMember {
	public int offset;
	public boolean isLocalVar;
	
	public FrameMember(int offset){
		this.offset = offset;
		this.isLocalVar = false;
	}
}
