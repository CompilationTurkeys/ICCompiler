package ic.compiler;

public class DispatchAttribute { 
	  public final String className; 
	  public final int offset;
	  
	  public DispatchAttribute(String className, int offset) { 
	    this.className = className; 
	    this.offset = offset; 
	  } 
	} 