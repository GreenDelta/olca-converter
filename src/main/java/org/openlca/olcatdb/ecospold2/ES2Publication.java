package org.openlca.olcatdb.ecospold2;

import org.openlca.olcatdb.ecospold2.masterdata.ES2CompanyList;
import org.openlca.olcatdb.ecospold2.masterdata.ES2PersonList;
import org.openlca.olcatdb.ecospold2.masterdata.ES2SourceList;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * Contains information about the generator of the dataset in the database,
 * whether the dataset has been published (and how) and about copyright and the
 * accessibility of the dataset (public or restricted to ETH domain, ECOINVENT,
 * or a particular institute of ECOINVENT).
 * 
 * @Element dataGeneratorAndPublication
 * @ContentModel (pageNumbers?, namespace:uri="##other")
 * 
 */
@Context(name = "dataGeneratorAndPublication", parentName = "administrativeInformation")
public class ES2Publication extends ContextObject {

	@ContextField(name = "dataGeneratorAndPublication", parentName = "administrativeInformation", isAttribute = true, attributeName = "personId", isMaster = true, classGroup = ES2PersonList.class)
	public String personId;

	@ContextField(name = "dataGeneratorAndPublication", parentName = "administrativeInformation", isAttribute = true, attributeName = "personContextId")
	public String personContextId;

	@ContextField(name = "dataGeneratorAndPublication", parentName = "administrativeInformation", isAttribute = true, attributeName = "personName", classGroup = ES2PersonList.class)
	public String personName;

	@ContextField(name = "dataGeneratorAndPublication", parentName = "administrativeInformation", isAttribute = true, attributeName = "personEmail", classGroup = ES2PersonList.class)
	public String personEmail;

	/**
	 * The publication status of the data set: 0 = not, 1 = partly, 2 =
	 * entirely.
	 */
	@ContextField(name = "dataGeneratorAndPublication", parentName = "administrativeInformation", isAttribute = true, attributeName = "dataPublishedIn", type = Type.Integer)
	public Integer dataPublishedIn;

	@ContextField(name = "dataGeneratorAndPublication", parentName = "administrativeInformation", isAttribute = true, attributeName = "accessRestrictedTo", type = Type.Integer)
	public Integer accessRestrictedTo;

	@ContextField(name = "dataGeneratorAndPublication", parentName = "administrativeInformation", isAttribute = true, attributeName = "companyCode", classGroup = ES2CompanyList.class)
	public String companyCode;

	@ContextField(name = "dataGeneratorAndPublication", parentName = "administrativeInformation", isAttribute = true, attributeName = "companyId", isMaster = true, classGroup = ES2CompanyList.class, overwrittenByChild = "companyIdOverwrittenByChild")
	public String companyId;

	@ContextField(name = "dataGeneratorAndPublication", parentName = "administrativeInformation", isAttribute = true, attributeName = "companyIdOverwrittenByChild", type = Type.Boolean)
	public boolean companyIdOverwrittenByChild;

	@ContextField(name = "dataGeneratorAndPublication", parentName = "administrativeInformation", isAttribute = true, attributeName = "companyContextId", isMaster = true)
	public String companyContextId;

	@ContextField(name = "dataGeneratorAndPublication", parentName = "administrativeInformation", isAttribute = true, attributeName = "isCopyrightProtected", type = Type.Boolean)
	public boolean isCopyrightProtected;

	@ContextField(name = "dataGeneratorAndPublication", parentName = "administrativeInformation", isAttribute = true, attributeName = "publishedSourceId", isMaster = true, classGroup = ES2SourceList.class, overwrittenByChild = "publishedSourceIdOverwrittenByChild")
	public String publishedSourceId;

	@ContextField(name = "dataGeneratorAndPublication", parentName = "administrativeInformation", isAttribute = true, attributeName = "publishedSourceIdOverwrittenByChild", type = Type.Boolean)
	public boolean publishedSourceIdOverwrittenByChild;

	@ContextField(name = "dataGeneratorAndPublication", parentName = "administrativeInformation", isAttribute = true, attributeName = "publishedSourceContextId", isMaster = true)
	public String publishedSourceContextId;

	@ContextField(name = "dataGeneratorAndPublication", parentName = "administrativeInformation", isAttribute = true, attributeName = "publishedSourceFirstAuthor", classGroup = ES2SourceList.class)
	public String publishedSourceFirstAuthor;

	@ContextField(name = "pageNumbers", parentName = "dataGeneratorAndPublication", isAttribute = true, attributeName = "pageNumbers", classGroup = ES2SourceList.class)
	public String pageNumbers;

	@ContextField(name = "pageNumbers", parentName = "dataGeneratorAndPublication", isAttribute = true, attributeName = "publishedSourceYear", classGroup = ES2SourceList.class)
	public String publishedSourceYear;
}
