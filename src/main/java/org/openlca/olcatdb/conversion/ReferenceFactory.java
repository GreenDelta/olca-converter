package org.openlca.olcatdb.conversion;

import org.openlca.olcatdb.datatypes.DataSetReference;
import org.openlca.olcatdb.datatypes.LangString;

public abstract class ReferenceFactory {

	public static final ReferenceFactory ILCD_FORMAT = new ILCDFormatReference();

	public static final ReferenceFactory ILCD_COMPLIANCE = new ILCDComplianceReference();
	
	public static final ReferenceFactory ECOSPOLD_FORMAT = new EcoSpoldFormatReference();
	
	public static final ReferenceFactory ECOINVENT_METHODIC = new EcoinventMethodicReference();
	
	/** ILCD contact reference for the ecoinvent center. */
	public static final ReferenceFactory ECOINVENT_CENTER = new EcoinventCenterReference();
	
	// flow properties
	
	
	
	
	public abstract DataSetReference createReference();

	private static class ILCDFormatReference extends ReferenceFactory {

		@Override
		public DataSetReference createReference() {
			DataSetReference reference = new DataSetReference();
			reference.setRefObjectId("a97a0155-0234-4b87-b4ce-a45da52f2a40");
			reference.setType("source data set");
			reference
					.setUri("../sources/ILCD_Format_a97a0155-0234-4b87-b4ce-a45da52f2a40.xml");
			reference.setVersion("01.00.000");
			reference.getDescription().add(new LangString("ILCD Format"));
			return reference;
		}

	}
		
	private static class ILCDComplianceReference extends ReferenceFactory {

		@Override
		public DataSetReference createReference() {
			DataSetReference reference = new DataSetReference();
			reference.setRefObjectId("88d4f8d9-60f9-43d1-9ea3-329c10d7d727");
			reference.setType("source data set");
			reference
					.setUri("../sources/ILCD_Compliance_88d4f8d9-60f9-43d1-9ea3-329c10d7d727.xml");
			reference.setVersion("01.00.000");
			reference.getDescription().add(new LangString("ILCD Data Network compliance"));
			return reference;
		}

	}

	private static class EcoSpoldFormatReference extends ReferenceFactory {

		@Override
		public DataSetReference createReference() {
			DataSetReference reference = new DataSetReference();
			reference.setRefObjectId("cada7914-53c3-47ec-ac27-659b21240a99");
			reference.setType("source data set");
			reference
					.setUri("../sources/EcoSpold_Format_cada7914-53c3-47ec-ac27-659b21240a99.xml");
			reference.setVersion("01.00.000");
			reference.getDescription().add(new LangString("EcoSpold Format"));
			return reference;
		}

	}
	
	
	private static class EcoinventMethodicReference extends ReferenceFactory {
		
		@Override
		public DataSetReference createReference() {
			DataSetReference reference = new DataSetReference();
			reference.setRefObjectId("0d388ade-52ab-4ca6-8a9b-f06df45d880c");
			reference.setType("source data set");
			reference
					.setUri("../sources/ecoinvent_overview_and_methodology.xml");
			reference.setVersion("01.00.000");
			reference.getDescription().add(new LangString("Ecoinvent: Overview and Methodology"));
			return reference;
		}
		
	}

	private static class EcoinventCenterReference extends ReferenceFactory {
		
		@Override
		public DataSetReference createReference() {
			DataSetReference reference = new DataSetReference();
			reference.setRefObjectId("631b917e-eb39-4d0f-aae6-98c805513b2f");
			reference.setType("contact data set");
			reference
					.setUri("../contacts/631b917e-eb39-4d0f-aae6-98c805513b2f.xml");
			reference.setVersion("01.00.000");
			reference.getDescription().add(new LangString("Ecoinvent Centre"));
			return reference;
		}
		
	}
	
	
	
	
}
