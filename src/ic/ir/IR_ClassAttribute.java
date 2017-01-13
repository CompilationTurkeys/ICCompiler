package ic.ir;

import java.util.HashMap;
import java.util.Map;

import ic.ast.AST_ClassDecl;
import ic.ast.AST_Field;
import ic.ast.AST_Method;

public class IR_ClassAttribute {

	private AST_ClassDecl cls;
	private Map<String, Integer> fieldOffset = new HashMap<String, Integer>(); // for temporary.field_offset
	private Map<String, Integer> methodOffset = new HashMap<String, Integer>();//for virtual table
	private int fieldCounter = 1; // 1 for the virtual table in place 0
	private int methodCounter = 0;

	public IR_ClassAttribute(AST_ClassDecl cls) {

		this.cls = cls;
		
		for (AST_Method method : cls.classMethods) {
			methodOffset.put(method.getName(), methodCounter++);
		}
		
		for (AST_Field field : cls.classFields) {
			for (String name : field.fieldNamesList){
				fieldOffset.put(name, fieldCounter++);
			}
		}
	}


	public IR_ClassAttribute(AST_ClassDecl cls, IR_ClassAttribute superCls) {
		this.cls = cls;
		fieldOffset.putAll(superCls.getFieldOffsetMap());
		methodOffset.putAll(superCls.getMethodOffsetMap());
		fieldCounter += fieldOffset.size();	
		methodCounter += methodOffset.size();
		
		for (AST_Method method : cls.classMethods) {
			boolean overriden = false;

			for (String m : methodOffset.keySet()) {
				
				if (method.getName().equals(m)) {
					int offset = methodOffset.remove(m);
					methodOffset.put(method.getName(), offset);
					overriden = true;
					break;
				}
			}
			if (!overriden)
				methodOffset.put(method.getName(), methodCounter++);
			// TODO check if overriden flag is neccessary?
		}
		
		for (AST_Field field : cls.classFields) { 
			for (String name : field.fieldNamesList){
				fieldOffset.put(name, fieldCounter++);
			}
		}
	}

	public AST_ClassDecl getClassObject() {
		return this.cls;
	}

	public String getClassName() {
		return this.cls.className;
	}

	public Integer getFieldOffset(AST_Field f) {
		return fieldOffset.get(f);
	}
	
	public Map<String, Integer> getMethodOffsetMap() {
		return this.methodOffset;
	}

	public Integer getMethodOffset(String method) {
		return methodOffset.get(method);
	}

	public Map<String, Integer> getFieldOffsetMap() {
		return this.fieldOffset;
	}

	public int getAllocSize() {
		final int BYTE_SIZE = 4;
		return (fieldOffset.size() + 1) * BYTE_SIZE;
	}

	public void addFieldToOffsetMap(AST_Field f, Integer offset) {
		for (String name : f.fieldNamesList){
			fieldOffset.put(name, offset++);		
		}
	}

	public void addMethodToOffsetMap(AST_Method m, Integer offset) {
		methodOffset.put(m.getName(), offset);
	}
}
