package org.openlca.olcatdb.ilcd;

import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.ContextField.Type;

@Context(name = "class", parentName = "classification")
public class ILCDClass extends ContextObject implements Comparable<ILCDClass>{

	/**
	 * Unique identifier for the class. [Notes: If such identifiers are also
	 * defined in the referenced category file, they should be identical.
	 * Identifiers can be UUID's, but also other forms are allowed.]
	 * 
	 * @Attribute classId
	 * @DataType string
	 */
	@ContextField(
			name = "class", 
			parentName = "classification", 
			attributeName = "classId", 
			isAttribute = true)
	public String id;

	/**
	 * If more than one class is specified in a hierachical classification
	 * system, the hierarchy level (1,2,...) could be specified with this
	 * attribute of class.
	 * 
	 * @Attribute level
	 * @DataType LevelType
	 */
	@ContextField(
			name = "class", 
			parentName = "classification", 
			attributeName = "level", 
			isAttribute = true, 
			type = Type.Integer)
	public int level;

	/**
	 * Name of the class.
	 * 
	 * @Element class
	 * @DataType string
	 */
	@ContextField(
			name = "class", 
			parentName = "classification", 
			isAttribute = false)
	public String name;

	public ILCDClass() {		
	}
	
	public ILCDClass(int level, String name) {
		this.level = level;
		this.name = name;
	}
	
	public ILCDClass(String id, int level, String name) {
		this.id = id;
		this.level = level;
		this.name = name;
	}
	
	
	@Override
	public int compareTo(ILCDClass clazz) {
		int c = 0;
		if(clazz != null) {
			c = level - clazz.level;
		}		
		return c;
	}
	
}
