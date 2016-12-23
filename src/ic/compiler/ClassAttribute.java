package ic.compiler;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ClassAttribute extends Attribute {
	
	private Map<String, Attribute> fieldMap;
	private Map<String, MethodAttribute> methodMap;
	private Set<String> classAncestors;
	
	public ClassAttribute(Map<String, Attribute> fieldMap, Map<String, MethodAttribute> methodMap)
	{
		super(null);
		this.fieldMap = fieldMap;
		this.methodMap = methodMap;
		this.classAncestors = new HashSet<>();
	}
	
	public Set<String> getAncestors() {
		return classAncestors;
	}
	
	public Map<String, Attribute> getFieldMap() {
		return fieldMap;
	}
	
	public Map<String, MethodAttribute> getMethodMap() {
		return methodMap;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((classAncestors == null) ? 0 : classAncestors.hashCode());
		result = prime * result + ((fieldMap == null) ? 0 : fieldMap.hashCode());
		result = prime * result + ((methodMap == null) ? 0 : methodMap.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClassAttribute other = (ClassAttribute) obj;
		if (classAncestors == null) {
			if (other.classAncestors != null)
				return false;
		} else if (!classAncestors.equals(other.classAncestors))
			return false;
		if (fieldMap == null) {
			if (other.fieldMap != null)
				return false;
		} else if (!fieldMap.equals(other.fieldMap))
			return false;
		if (methodMap == null) {
			if (other.methodMap != null)
				return false;
		} else if (!methodMap.equals(other.methodMap))
			return false;
		return true;
	}
	
}
