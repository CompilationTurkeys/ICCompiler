package ic.ir;

public class IR_Attribute {
	// same attributes as in c file MIPS_Frame struct F_access
	public int offset;

	public IR_Attribute(int offset)
	{
		this.offset = offset;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + offset;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IR_Attribute other = (IR_Attribute) obj;
		if (offset != other.offset)
			return false;
		return true;
	}

}