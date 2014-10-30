package org.openlca.olcatdb.datatypes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openlca.olcatdb.ilcd.ILCDDataSetType;

/**
 * Representation of the ILCD common reference type.
 */
public class DataSetReference {

	/**
	 * A name for the data set reference.
	 */
	private String name;

	/**
	 * Possible extensions of the data set reference type.
	 */
	private Map<String, Object> extensions = new HashMap<String, Object>();

	/**
	 * UUID of the referenced object
	 * 
	 * @Attribute refObjectId
	 * @DataType UUID
	 */
	private String refObjectId;

	/**
	 * Indicates the type of the referenced data set / file. One of
	 * GlobalReferenceTypeValues.
	 * 
	 * @Attribute type
	 * @DataType GlobalReferenceTypeValues
	 */
	private String type;

	/**
	 * URI of the referenced object
	 * 
	 * @Attribute uri
	 * @DataType anyURI
	 */
	private String uri;

	/**
	 * Version number of the referenced object.
	 * 
	 * @Attribute version
	 * @DataType Version
	 */
	private String version;

	/**
	 * Valid only for references of type "source data set". Allows to make
	 * references to sections, pages etc. within a source.
	 * 
	 * @Element subReference*
	 * @DataType String
	 */
	private List<String> subReferences = new ArrayList<String>();

	/**
	 * Short, clear-text summary of the referenced object that can be used as a
	 * hint what to expect behind the reference in cases where it cannot be
	 * resolved.
	 * 
	 * @Element shortDescription
	 * @DataType ST
	 */
	private List<LangString> description = new ArrayList<LangString>();

	/**
	 * Get the name of the data set reference.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of the data set reference.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * UUID of the referenced object
	 * 
	 * @Attribute refObjectId
	 * @DataType UUID
	 */
	public String getRefObjectId() {
		return refObjectId;
	}

	/**
	 * UUID of the referenced object
	 * 
	 * @Attribute refObjectId
	 * @DataType UUID
	 */
	public void setRefObjectId(String refObjectId) {
		this.refObjectId = refObjectId;
	}

	/**
	 * Indicates the type of the referenced data set / file. One of
	 * GlobalReferenceTypeValues.
	 * 
	 * @Attribute type
	 * @DataType GlobalReferenceTypeValues
	 */
	public String getType() {
		return type;
	}

	/**
	 * Indicates the type of the referenced data set / file. One of
	 * GlobalReferenceTypeValues.
	 * 
	 * @Attribute type
	 * @DataType GlobalReferenceTypeValues
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * URI of the referenced object
	 * 
	 * @Attribute uri
	 * @DataType anyURI
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * URI of the referenced object
	 * 
	 * @Attribute uri
	 * @DataType anyURI
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}

	/**
	 * Version number of the referenced object.
	 * 
	 * @Attribute version
	 * @DataType Version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Version number of the referenced object.
	 * 
	 * @Attribute version
	 * @DataType Version
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * Valid only for references of type "source data set". Allows to make
	 * references to sections, pages etc. within a source.
	 * 
	 * @Element subReference*
	 * @DataType String
	 */
	public List<String> getSubReferences() {
		return subReferences;
	}

	/**
	 * Short, clear-text summary of the referenced object that can be used as a
	 * hint what to expect behind the reference in cases where it cannot be
	 * resolved.
	 * 
	 * @Element shortDescription
	 * @DataType ST
	 */
	public List<LangString> getDescription() {
		return description;
	}

	/**
	 * Get possible extensions of the data set reference type.
	 */
	public Map<String, Object> getExtensions() {
		return extensions;
	}

	@Override
	public String toString() {
		return "DataSetReference [name=" + name + ", refObjectId="
				+ refObjectId + ", type=" + type + ", uri=" + uri
				+ ", version=" + version + "]";
	}

	/**
	 * Creates a deep copy of the reference.
	 */
	public DataSetReference copy() {
		DataSetReference copy = new DataSetReference();
		copy.description.addAll(this.description);
		copy.extensions.putAll(this.extensions);
		copy.name = name;
		copy.refObjectId = this.refObjectId;
		copy.subReferences.addAll(this.subReferences);
		copy.type = this.type;
		copy.uri = this.uri;
		copy.version = this.version;
		return copy;
	}

	/**
	 * Creates a comparator that compares data set references by their type and
	 * name. The aim of this comparator is to provide an order where references
	 * of more important types are ranked higher (->they get a lower comparison
	 * index in order to be listed before other types). The type importance is
	 * defined in the following form: processes < flows < flow properties < unit
	 * groups < sources < contacts < other or null. If the types are equal, the
	 * references are compared by their names (ignoring the case).
	 */
	public static Comparator<DataSetReference> comparator() {

		Comparator<DataSetReference> comparator = new Comparator<DataSetReference>() {

			private int rank(ILCDDataSetType type) {
				switch (type) {
				case Process:
					return 0;
				case Flow:
					return 1;
				case FlowProperty:
					return 2;
				case UnitGroup:
					return 3;
				case Source:
					return 4;
				case Contact:
					return 5;
				default:
					return 6;
				}
			}

			@Override
			public int compare(DataSetReference ref1, DataSetReference ref2) {
				int c = 0;

				// compare the references by type
				ILCDDataSetType type1 = ILCDDataSetType.forName(ref1.getType());
				ILCDDataSetType type2 = ILCDDataSetType.forName(ref2.getType());

				if (type1 == null && type2 != null) {
					// ref2 is more important than ref2: ref1 > ref2
					c = 1;
				} else if (type1 != null && type2 == null) {
					// ref1 is more important than ref2: ref1 < ref2
					c = -1;
				} else if (type1 != null && type2 != null) {
					c = rank(type1) - rank(type2);
				}

				if (c == 0) {

					// the types are equal -> compare the names

					if (ref1.name == null && ref2.name != null) {
						// ref2 is more important than ref2: ref1 > ref2
						c = 1;
					} else if (ref1.name != null && ref2.name == null) {
						// ref1 is more important than ref2: ref1 < ref2
						c = -1;
					} else if (ref1.name != null && ref2.name != null) {
						c = ref1.name.compareToIgnoreCase(ref2.name);
					}

				}

				return c;
			}
		};

		return comparator;
	}
}
