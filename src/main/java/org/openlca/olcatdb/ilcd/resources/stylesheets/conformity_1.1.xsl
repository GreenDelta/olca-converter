<?xml version="1.0" encoding="UTF-8"?>
<!-- ILCD Format Version 1.1 Tools Build 985 -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:categories="http://lca.jrc.it/ILCD/Categories"
	xmlns:locations="http://lca.jrc.it/ILCD/Locations"
	xmlns:process="http://lca.jrc.it/ILCD/Process"
	xmlns:lciamethod="http://lca.jrc.it/ILCD/LCIAMethod" xmlns:flow="http://lca.jrc.it/ILCD/Flow"
	xmlns:flowproperty="http://lca.jrc.it/ILCD/FlowProperty"
	xmlns:unitgroup="http://lca.jrc.it/ILCD/UnitGroup" xmlns:source="http://lca.jrc.it/ILCD/Source"
	xmlns:contact="http://lca.jrc.it/ILCD/Contact" xmlns:common="http://lca.jrc.it/ILCD/Common"
	xmlns:xs="http://www.w3.org/2001/XMLSchema">

	<xsl:import href="common.xsl"/>

	<xsl:output indent="no" method="text"/>


	<xsl:variable name="requiredComplianceSystem" select="'ILCD compliance 1.0.1'"/>
	<xsl:variable name="requiredComplianceSystemDataSetUUID"
		select="'9c85162d-6c43-4bc7-bbb3-03e0c8b262b9'"/>


	<xsl:variable name="categoriesFile" select="'ILCDCategories_Reference_Compliance_1.0.1.xml'"/>
	<xsl:variable name="validCategories"
		select="document($categoriesFile)/categories:ILCDCategories"/>

	<xsl:variable name="locationsFile" select="/*/@locations"/>
	<xsl:variable name="validLocations" select="document($locationsFile, /)"/>

	<xsl:param name="enumerationValuesSchemaFile"
		select="'../../schemas/ILCD_Common_EnumerationValues.xsd'"/>
	<xsl:variable name="enumerationValues" select="document($enumerationValuesSchemaFile, /)"/>

	
	
	<!-- process dataset-specific compliance -->
	<xsl:template match="/process:processDataSet">

		<xsl:call-template name="checkReferenceToComplianceSystem"/>

		<xsl:call-template name="process-documentationCompliance"/>
		<xsl:call-template name="process-methodologicalCompliance"/>
		<xsl:call-template name="process-nomenclatureAndHierarchyCompliance"/>
		<!--	review compliance enforcement has been postponed to a later release
		<xsl:call-template name="process-reviewCompliance"/>-->
		<xsl:call-template name="overallCompliance"/>

	</xsl:template>



	<!-- flow dataset-specific compliance -->
	<xsl:template match="/flow:flowDataSet">
		<xsl:call-template name="checkReferenceToComplianceSystem"/>

		<xsl:call-template name="flow-documentationCompliance"/>
		<xsl:call-template name="flow-nomenclatureAndHierarchyCompliance"/>
		<xsl:call-template name="overallCompliance"/>

	</xsl:template>



	<!-- flow property dataset-specific compliance -->
	<xsl:template match="/flowproperty:flowPropertyDataSet">
		<xsl:call-template name="checkReferenceToComplianceSystem"/>

		<xsl:call-template name="flowproperty-documentationCompliance"/>
		<xsl:call-template name="flowproperty-nomenclatureAndHierarchyCompliance"/>
		<xsl:call-template name="overallCompliance"/>

	</xsl:template>


	<!-- unit group dataset-specific compliance -->
	<xsl:template match="/unitgroup:unitGroupDataSet">
		<xsl:call-template name="checkReferenceToComplianceSystem"/>

		<xsl:call-template name="unitgroup-documentationCompliance"/>
		<xsl:call-template name="unitgroup-nomenclatureAndHierarchyCompliance"/>
		<xsl:call-template name="overallCompliance"/>

	</xsl:template>


	<!-- source dataset-specific compliance -->
	<xsl:template match="/source:sourceDataSet">

		<xsl:call-template name="source-documentationCompliance"/>
		<!--	enforcement of this rule has been postponed to a later release
		<xsl:call-template name="overallCompliance"/>
		-->

	</xsl:template>


	<!-- contact dataset-specific compliance -->
	<xsl:template match="/contact:contactDataSet">

		<xsl:call-template name="contact-documentationCompliance"/>
		<!--	enforcement of this rule has been postponed to a later release
		<xsl:call-template name="overallCompliance"/>
		-->

	</xsl:template>



	<xsl:template name="checkReferenceToComplianceSystem">

		<!-- detect position of reference to ILCD compliance system -->
		<xsl:variable name="referencePosition">
			<xsl:for-each
			   select="/*/*[local-name()='modellingAndValidation']/*[local-name()='complianceDeclarations']/*[local-name()='compliance']">
				<xsl:variable name="matchRefObjectId">
					<xsl:call-template name="equalsUUID">
						<xsl:with-param name="uuid1"
							select="*[local-name()='referenceToComplianceSystem']/@refObjectId"/>
						<xsl:with-param name="uuid2" select="$requiredComplianceSystemDataSetUUID"/>
					</xsl:call-template>
				</xsl:variable>
				<xsl:variable name="matchURI">
					<xsl:value-of
						select="contains(translate(*[local-name()='referenceToComplianceSystem']/@uri, 'abcdef', 'ABCDEF'), translate($requiredComplianceSystemDataSetUUID, 'abcdef', 'ABCDEF'))"
					/>
				</xsl:variable>
				<xsl:if test="$matchRefObjectId='true' or $matchURI='true'">
					<xsl:value-of select="position()"/>
				</xsl:if>
			</xsl:for-each>
		</xsl:variable>

		<xsl:if test="string(number($referencePosition))='NaN'">
			<xsl:message>
				<xsl:text>General compliance: Could not find proper reference to compliance system </xsl:text>
				<xsl:value-of select="$requiredComplianceSystem"/>
				<xsl:text>.</xsl:text>
			</xsl:message>
		</xsl:if>

		<!-- try to open referenced compliance system dataset -->
		<xsl:for-each
		   select="/*/*[local-name()='modellingAndValidation']/*[local-name()='complianceDeclarations']/*[local-name()='compliance'][number($referencePosition)]/*[local-name()='referenceToComplianceSystem']">
			<xsl:choose>
				<xsl:when test="@uri and not(@uri='')">
					<xsl:variable name="uriExists">
						<xsl:call-template name="checkFileExistence">
							<xsl:with-param name="uri" select="@uri"/>
							<xsl:with-param name="quiet" select="true()"/>
						</xsl:call-template>
					</xsl:variable>
					<xsl:choose>
						<xsl:when test="$uriExists='true'">
							<xsl:variable name="complianceSystem"
								select="document(@uri, /)/source:sourceDataSet/source:sourceInformation/source:dataSetInformation/common:shortName[@xml:lang='en']"/>
							<xsl:if test="not($complianceSystem=$requiredComplianceSystem)">
								<xsl:message>
									<xsl:text>General compliance: referenceToComplianceSystem: expected '</xsl:text>
									<xsl:value-of select="$requiredComplianceSystem"/>
									<xsl:text>', found '</xsl:text>
									<xsl:value-of select="$complianceSystem"/>
									<xsl:text>'</xsl:text>
									<xsl:value-of select="@uri"/>
								</xsl:message>
							</xsl:if>
						</xsl:when>
						<xsl:otherwise>
							<xsl:message>
								<xsl:text>General compliance: Reference to compliance system cannot	be resolved.</xsl:text>
							</xsl:message>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
				<xsl:when
					test="@refObjectId and @version and not(@refObjectId='') and not(@version='')">
					<xsl:variable name="uri"
						select="concat('../sources/', translate(concat($requiredComplianceSystem, ' ', 'Compliance systems '), ' .-', '___'), @refObjectId, '_', @version, '.xml')"/>

					<xsl:variable name="uriExists">
						<xsl:call-template name="checkFileExistence">
							<xsl:with-param name="uri" select="$uri"/>
							<xsl:with-param name="quiet" select="true()"/>
						</xsl:call-template>
					</xsl:variable>
					<xsl:choose>
						<xsl:when test="$uriExists='true'">
							<xsl:variable name="complianceSystem"
							   select="document($uri, /)/source:sourceDataSet/source:sourceInformation/source:dataSetInformation/common:shortName[@xml:lang='en']"/>
							<xsl:if test="not($complianceSystem=$requiredComplianceSystem)">
								<xsl:message><xsl:text>General compliance: referenceToComplianceSystem: expected '</xsl:text><xsl:value-of
										select="$requiredComplianceSystem"
										/><xsl:text>',	found '</xsl:text><xsl:value-of
										select="$complianceSystem"/>'</xsl:message>
							</xsl:if>
						</xsl:when>
						<xsl:otherwise>
							<xsl:message>
								<xsl:text>General compliance: Reference to compliance system cannot	be resolved.</xsl:text>
							</xsl:message>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
				<xsl:otherwise>
					<xsl:message>
						<xsl:text>General compliance: Reference to compliance system cannot be resolved.</xsl:text>
					</xsl:message>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:for-each>

	</xsl:template>


	<!-- process documentationCompliance -->
	<xsl:template name="process-documentationCompliance">

		<xsl:variable name="msgPrefix" select="'Documentation compliance'"/>

		<xsl:call-template name="checkPresence">
			<xsl:with-param name="element" select="//common:dataSetValidUntil"/>
			<xsl:with-param name="elementDesc" select="'dataSetValidUntil'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>
		<xsl:call-template name="checkPresence">
			<xsl:with-param name="element"
				select="//process:classificationInformation/common:class[@level=0]"/>
			<xsl:with-param name="elementDesc" select="'category with level 0'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>

		<xsl:call-template name="checkPresence">
			<xsl:with-param name="element"
			   select="//process:classificationInformation/common:class[@level=1]"/>
			<xsl:with-param name="elementDesc" select="'category with level 1'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>

		<xsl:call-template name="checkPresence">
			<xsl:with-param name="element"
				select="//process:locationOfOperationSupplyOrProduction/process:descriptionOfRestrictions"/>
			<xsl:with-param name="elementDesc"
				select="'locationOfOperationSupplyOrProduction/descriptionOfRestrictions'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>
		<xsl:call-template name="checkPresence">
			<xsl:with-param name="element"
				select="//process:technologyDescriptionAndIncludedProcesses"/>
			<xsl:with-param name="elementDesc"
				select="'technologyDescriptionAndIncludedProcesses'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>
		<xsl:call-template name="checkPresence">
			<xsl:with-param name="element" select="//process:technologicalApplicability"/>
			<xsl:with-param name="elementDesc" select="'technologicalApplicability'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>
		<xsl:call-template name="checkPresence">
			<xsl:with-param name="element"
				select="//process:referenceToTechnologyFlowDiagrammOrPicture"/>
			<xsl:with-param name="elementDesc" select="'referenceToTechnologyFlowDiagrammOrPicture'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>
		<xsl:call-template name="checkPresence">
			<xsl:with-param name="element" select="//process:deviationsFromLCIMethodPrinciple"/>
			<xsl:with-param name="elementDesc" select="'deviationsFromLCIMethodPrinciple'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>
		<xsl:call-template name="checkPresence">
			<xsl:with-param name="element" select="//process:modellingConstants"/>
			<xsl:with-param name="elementDesc" select="'modellingConstants'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>
		<xsl:call-template name="checkPresence">
			<xsl:with-param name="element" select="//process:deviationsFromModellingConstants"/>
			<xsl:with-param name="elementDesc" select="'deviationsFromModellingConstants'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>
		<xsl:call-template name="checkPresence">
			<xsl:with-param name="element" select="//process:dataCutOffAndCompletenessPrinciples"/>
			<xsl:with-param name="elementDesc" select="'dataCutOffAndCompletenessPrinciples'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>
		<xsl:call-template name="checkPresence">
			<xsl:with-param name="element"
				select="//process:deviationsFromdataCutOffAndCompletenessPrinciples"/>
			<xsl:with-param name="elementDesc" select="'deviationsFromdataCutOffAndCompletenessPrinciples'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>
		<xsl:call-template name="checkPresence">
			<xsl:with-param name="element" select="//process:dataSelectionAndCombinationPrinciples"/>
			<xsl:with-param name="elementDesc" select="'dataSelectionAndCombinationPrinciples'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>
		<xsl:call-template name="checkPresence">
			<xsl:with-param name="element"
				select="//process:deviationsFromDataSelectionAndCombinationPrinciples"/>
			<xsl:with-param name="elementDesc"
				select="'deviationsFromDataSelectionAndCombinationPrinciples'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>
		<xsl:call-template name="checkPresence">
			<xsl:with-param name="element"
				select="//process:dataTreatmentAndExtrapolationsPrinciples"/>
			<xsl:with-param name="elementDesc" select="'dataTreatmentAndExtrapolationsPrinciples'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>
		<xsl:call-template name="checkPresence">
			<xsl:with-param name="element"
				select="//process:deviationsFromDataTreatmentAndExtrapolationsPrinciples"/>
			<xsl:with-param name="elementDesc"
				select="'deviationsFromDataTreatmentAndExtrapolationsPrinciples'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>
		<xsl:call-template name="checkPresence">
			<xsl:with-param name="element"
				select="//process:dataSourcesTreatmentAndRepresentativeness/process:referenceToDataSource"/>
			<xsl:with-param name="elementDesc" select="'referenceToDataSource'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>
		<!--		<xsl:call-template name="checkPresence">
			<xsl:with-param name="element" select="//process:percentageSupplyOrProductionCovered"/>
			<xsl:with-param name="elementDesc" select="'percentageSupplyOrProductionCovered'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>
-->
		<xsl:call-template name="checkPresence">
			<xsl:with-param name="element" select="//process:useAdviceForDataSet"/>
			<xsl:with-param name="elementDesc" select="'useAdviceForDataSet'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>
		<xsl:call-template name="checkPresence">
			<xsl:with-param name="element" select="//process:completenessProductModel"/>
			<xsl:with-param name="elementDesc" select="'completenessProductModel'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>
		<!--	enforcement of this rule has been postponed to a later release 
		<xsl:call-template name="checkPresence">
			<xsl:with-param name="element"
				select="//process:referenceToSupportedImpactAssessmentMethods"/>
			<xsl:with-param name="elementDesc"
				select="'referenceToSupportedImpactAssessmentMethods'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>
		-->

		<!--	enforcement of this rule has been postponed to a later release
		<xsl:variable name="typesPresent">
			<xsl:for-each select="//process:completenessElementaryFlows">
				<xsl:text>%%</xsl:text>
				<xsl:value-of select="@type"/>
				<xsl:if test="position() = last()">
					<xsl:text>%%</xsl:text>
				</xsl:if>
			</xsl:for-each>
		</xsl:variable>

		<xsl:for-each
			select="$enumerationValues/xs:schema/xs:simpleType[@name='CompletenessTypeValues']//xs:enumeration">
			<xsl:if test="not(contains($typesPresent, concat('%%', ./@value, '%%')))">
				<xsl:message><xsl:value-of select="$msgPrefix"/>: completenessElementaryFlows of
					type "<xsl:value-of select="@value"/>" must be present.</xsl:message>
			</xsl:if>
		</xsl:for-each>
-->
		<xsl:call-template name="checkPresence">
			<xsl:with-param name="element" select="//common:referenceToCommissioner"/>
			<xsl:with-param name="elementDesc" select="'referenceToCommissioner'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>
		<xsl:call-template name="checkPresence">
			<xsl:with-param name="element" select="//common:intendedApplications"/>
			<xsl:with-param name="elementDesc" select="'intendedApplications'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>
		<xsl:call-template name="checkPresence">
			<xsl:with-param name="element" select="//common:referenceToDataSetUseApproval"/>
		   <xsl:with-param name="elementDesc" select="'referenceToDataSetUseApproval'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>
		<xsl:call-template name="checkPresence">
			<xsl:with-param name="element" select="//common:referenceToOwnershipOfDataSet"/>
			<xsl:with-param name="elementDesc" select="'referenceToOwnershipOfDataSet'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>
		<xsl:call-template name="checkPresence">
			<xsl:with-param name="element" select="//process:dataSourceType"/>
			<xsl:with-param name="elementDesc" select="'dataSourceType'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>


		<xsl:if test="not(count(//process:mathematicalRelations/*)=0)">
			<xsl:call-template name="checkDependency">
				<xsl:with-param name="elementDesc" select="'any field in mathematicalRelations'"/>
				<xsl:with-param name="dependsOn"
					select="//process:mathematicalRelations/process:modelDescription"/>
				<xsl:with-param name="dependsOnDesc" select="'modelDescription'"/>
				<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
			</xsl:call-template>

			<xsl:for-each select="//process:mathematicalRelations/process:variableParameter">
				<xsl:call-template name="checkDependency">
					<xsl:with-param name="elementDesc" select="'any field in mathematicalRelations'"/>
					<xsl:with-param name="dependsOn" select="@meanValue"/>
					<xsl:with-param name="dependsOnDesc"
						select="'on each variableParameter element a meanValue attribute'"/>
					<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
				</xsl:call-template>
				<xsl:call-template name="checkDependency">
					<xsl:with-param name="elementDesc" select="'any field in mathematicalRelations'"/>
					<xsl:with-param name="dependsOn" select="@variableParameter"/>
					<xsl:with-param name="dependsOnDesc"
						select="'on each variableParameter element a variableParameter attribute'"/>
					<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
				</xsl:call-template>
				<xsl:call-template name="checkDependency">
					<xsl:with-param name="elementDesc" select="'any field in mathematicalRelations'"/>
					<xsl:with-param name="dependsOn" select="@comment"/>
					<xsl:with-param name="dependsOnDesc"
						select="'on each variableParameter element a comment attribute'"/>
					<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
				</xsl:call-template>
			</xsl:for-each>
		</xsl:if>

		<xsl:if
			test="//process:typeOfDataSet='Unit process, not pre-allocated' or //process:typeOfDataSet='Pre-allocated unit process'">
			<xsl:call-template name="checkDependency">
				<xsl:with-param name="elementDesc">process type 'Unit process, not pre-allocated' or
					'Pre-allocated unit process'</xsl:with-param>
				<xsl:with-param name="dependsOn" select="//process:samplingProcedure"/>
				<xsl:with-param name="dependsOnDesc" select="'samplingProcedure'"/>
				<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
			</xsl:call-template>
			<xsl:call-template name="checkDependency">
				<xsl:with-param name="elementDesc">process type 'Unit process, not pre-allocated' or
					'Pre-allocated unit process'</xsl:with-param>
				<xsl:with-param name="dependsOn" select="//process:dataCollectionPeriod"/>
				<xsl:with-param name="dependsOnDesc" select="'dataCollectionPeriod'"/>
				<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
			</xsl:call-template>
			<xsl:call-template name="checkDependency">
				<xsl:with-param name="elementDesc">process type 'Unit process, not pre-allocated' or
					'Pre-allocated unit process'</xsl:with-param>
				<xsl:with-param name="dependsOn" select="//process:uncertaintyAdjustments"/>
				<xsl:with-param name="dependsOnDesc" select="'uncertaintyAdjustments'"/>
				<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
			</xsl:call-template>

		</xsl:if>

		<xsl:call-template name="checkPresence">
		   <xsl:with-param name="element" select="//common:dataSetVersion"/>
			<xsl:with-param name="elementDesc" select="'dataSetVersion'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>

		<xsl:call-template name="checkPresence">
		   <xsl:with-param name="element" select="//common:permanentDataSetURI"/>
			<xsl:with-param name="elementDesc" select="'permanentDataSetURI'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>

		<xsl:call-template name="checkPresence">
			<xsl:with-param name="element" select="//common:accessRestrictions"/>
			<xsl:with-param name="elementDesc" select="'accessRestrictions'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>


		<xsl:call-template name="checkValue">
			<xsl:with-param name="element" select="//process:documentationCompliance"/>
			<xsl:with-param name="elementDesc" select="'documentationCompliance'"/>
			<xsl:with-param name="value1" select="'Fully conform'"/>
			<xsl:with-param name="value2" select="'Sufficiently conform'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>
	</xsl:template>
	<!-- end process-documentationCompliance -->


	<!-- process-methodologicalCompliance -->
	<xsl:template name="process-methodologicalCompliance">
		<xsl:variable name="msgPrefix" select="'Methodological compliance'"/>

		<xsl:call-template name="checkValue">
			<xsl:with-param name="element" select="//common:methodologicalCompliance"/>
			<xsl:with-param name="elementDesc" select="'methodologicalCompliance'"/>
			<xsl:with-param name="value1" select="'Not defined'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>

	</xsl:template>
	<!-- end process-methodologicalCompliance -->


	<!-- process-nomenclatureAndHierarchyCompliance -->
	<xsl:template name="process-nomenclatureAndHierarchyCompliance">
		<xsl:variable name="msgPrefix" select="'Nomenclature and hierarchy compliance'"/>

		<xsl:call-template name="checkPresence">
			<xsl:with-param name="element"
			   select="//process:classificationInformation/common:class[@level=0]"/>
			<xsl:with-param name="elementDesc" select="'category with level 0'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>

		<xsl:call-template name="checkPresence">
			<xsl:with-param name="element"
			   select="//process:classificationInformation/common:class[@level=1]"/>
			<xsl:with-param name="elementDesc" select="'category with level 1'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>

		<xsl:for-each select="//process:classificationInformation">
			<xsl:call-template name="check_hierarchy">
				<xsl:with-param name="tree" select="$validCategories/categories:processCategories"/>
				<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
			</xsl:call-template>
		</xsl:for-each>

		<xsl:call-template name="checkValue">
			<xsl:with-param name="element" select="//process:nomenclatureAndHierarchyCompliance"/>
			<xsl:with-param name="elementDesc" select="'nomenclatureAndHierarchyCompliance'"/>
			<xsl:with-param name="value1" select="'Fully conform'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>

	</xsl:template>
	<!-- end process-nomenclatureAndHierarchyCompliance -->


	<!-- process-reviewCompliance -->
	<xsl:template name="process-reviewCompliance">
		<xsl:variable name="msgPrefix" select="'Review compliance'"/>

		<xsl:call-template name="checkPresence">
			<xsl:with-param name="element"
				select="//process:review[@type='Independent review panel']"/>
			<xsl:with-param name="elementDesc">review of type 'Independent review panel'</xsl:with-param>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>

		<xsl:call-template name="checkPresence">
			<xsl:with-param name="element"
				select="//process:review[@type='Independent review panel']/common:scope"/>
			<xsl:with-param name="elementDesc">scope for review of type 'Independent review panel'</xsl:with-param>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>

		<xsl:call-template name="checkPresence">
			<xsl:with-param name="element"
				select="//process:review[@type='Independent review panel']/common:scope[@name='Unit process(es)']"/>
			<xsl:with-param name="elementDesc">scope 'Unit process(es)' for review of type
				'Independent review panel'</xsl:with-param>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>

		<xsl:call-template name="checkPresence">
			<xsl:with-param name="element"
				select="//process:review[@type='Independent review panel']/common:scope[@name='Unit process(es)']/common:method[@name='Compliance with ISO 14040 to 14044']"/>
			<xsl:with-param name="elementDesc">method 'Compliance with ISO 14040 to 14044' for scope
				'Unit process(es)' for review of type 'Independent review panel'</xsl:with-param>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>

		<xsl:call-template name="checkPresence">
			<xsl:with-param name="element"
				select="//process:review[@type='Independent review panel']/common:scope[@name='Unit process(es)']/common:method[@name='Energy balance']"/>
			<xsl:with-param name="elementDesc">method 'Energy balance' for scope 'Unit process(es)'
				for review of type 'Independent review panel'</xsl:with-param>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>

		<xsl:call-template name="checkPresence">
			<xsl:with-param name="element"
				select="//process:review[@type='Independent review panel']/common:scope[@name='Unit process(es)']/common:method[@name='Element balance']"/>
			<xsl:with-param name="elementDesc">method 'Element balance' for scope 'Unit process(es)'
				for review of type 'Independent review panel'</xsl:with-param>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>

		<xsl:call-template name="checkPresence">
			<xsl:with-param name="element"
				select="//process:review[@type='Independent review panel']/common:scope[@name='Unit process(es)']/common:method[@name='Cross-check with other dataset' or @name='Compliance with legal limits' or @name='Cross-check with other source']"/>
			<xsl:with-param name="elementDesc">method 'Cross-check with other dataset', 'Compliance
				with legal limits' or 'Cross-check with other source' for scope 'Unit process(es)'
				for review of type 'Independent review panel'</xsl:with-param>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>

		<xsl:call-template name="checkPresence">
			<xsl:with-param name="element"
				select="//process:review[@type='Independent review panel']/common:scope[@name='LCI method']/common:method[@name='Compliance with ISO 14040 to 14044']"/>
			<xsl:with-param name="elementDesc">method 'Compliance with ISO 14040 to 14044' for scope
				'LCI method' for review of type 'Independent review panel'</xsl:with-param>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>

		<xsl:call-template name="checkPresence">
			<xsl:with-param name="element"
				select="//process:review[@type='Independent review panel']/common:scope[@name='Documentation']/common:method[@name='Expert judgement']"/>
			<xsl:with-param name="elementDesc">method 'Expert judgement' for scope 'Documentation'
				for review of type 'Independent review panel'</xsl:with-param>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>

		<xsl:for-each select="//process:review[@type='Independent review panel']">
			<xsl:call-template name="checkPresence">
				<xsl:with-param name="element" select="process:reviewDetailsOnTechnicalContent"/>
				<xsl:with-param name="elementDesc">reviewDetailsOnTechnicalContent for review of
					type 'Independent review panel'</xsl:with-param>
				<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
			</xsl:call-template>
			<xsl:call-template name="checkPresence">
				<xsl:with-param name="element" select="process:reviewDetailsOnImpactCoverage"/>
				<xsl:with-param name="elementDesc">reviewDetailsOnImpactCoverage for review of type
					'Independent review panel'</xsl:with-param>
				<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
			</xsl:call-template>
			<xsl:call-template name="checkPresence">
				<xsl:with-param name="element" select="process:reviewDetailsOnLCIAMethod"/>
				<xsl:with-param name="elementDesc">reviewDetailsOnLCIAMethod for review of type
					'Independent review panel'</xsl:with-param>
				<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
			</xsl:call-template>
			<xsl:call-template name="checkPresence">
				<xsl:with-param name="element"
					select="common:referenceToNameOfReviewerAndInstitution"/>
				<xsl:with-param name="elementDesc">referenceToNameOfReviewerAndInstitution for
					review of type 'Independent review panel'</xsl:with-param>
				<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
			</xsl:call-template>
			<xsl:call-template name="checkPresence">
				<xsl:with-param name="element"
				   select="common:referenceToNameOfReviewerAndInstitution/common:shortDescription"/>
				<xsl:with-param name="elementDesc">shortDescription for
					referenceToNameOfReviewerAndInstitution for review of type 'Independent review
					panel'</xsl:with-param>
				<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
			</xsl:call-template>
			<xsl:call-template name="checkPresence">
				<xsl:with-param name="element" select="common:referenceToCompleteReviewReport"/>
				<xsl:with-param name="elementDesc">referenceToCompleteReviewReport for review of
					type 'Independent review panel'</xsl:with-param>
				<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
			</xsl:call-template>
		</xsl:for-each>

		<xsl:call-template name="checkValue">
			<xsl:with-param name="element" select="//process:reviewCompliance"/>
			<xsl:with-param name="elementDesc" select="'reviewCompliance'"/>
			<xsl:with-param name="value1" select="'Fully conform'"/>
			<xsl:with-param name="value2" select="'Sufficiently conform'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>
	</xsl:template>
	<!-- end process-reviewCompliance -->



	<!-- flow-documentationCompliance -->
	<xsl:template name="flow-documentationCompliance">
		<xsl:variable name="msgPrefix" select="'Documentation compliance'"/>

		<xsl:variable name="typeOfDataset"
			select="flow:modellingAndValidation/flow:LCIMethod/flow:typeOfDataSet/text()"/>

		<xsl:call-template name="checkPresence">
			<xsl:with-param name="element" select="//flow:dataSetVersion"/>
			<xsl:with-param name="elementDesc" select="'dataSetVersion'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>

		<xsl:call-template name="checkPresence">
			<xsl:with-param name="element" select="//common:permanentDataSetURI"/>
			<xsl:with-param name="elementDesc" select="'permanentDataSetURI'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>

		<xsl:call-template name="checkValue">
			<xsl:with-param name="element" select="//flow:documentationCompliance"/>
			<xsl:with-param name="elementDesc" select="'documentationCompliance'"/>
			<xsl:with-param name="value1" select="'Fully conform'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>

	</xsl:template>
	<!-- end flow-documentationCompliance -->


	<!-- flow-nomenclatureAndHierarchyCompliance -->
	<xsl:template name="flow-nomenclatureAndHierarchyCompliance">
		<xsl:variable name="msgPrefix" select="'Nomenclature and hierarchy compliance'"/>

		<xsl:call-template name="checkPresence">
			<xsl:with-param name="element"
			   select="//flow:classificationInformation/common:class[@level=0]"/>
			<xsl:with-param name="elementDesc" select="'category with level 0'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>

		<xsl:for-each select="//flow:classificationInformation">
			<xsl:call-template name="check_hierarchy">
				<xsl:with-param name="tree" select="$validCategories/categories:flowCategories"/>
				<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
			</xsl:call-template>
		</xsl:for-each>

		<xsl:call-template name="checkValue">
			<xsl:with-param name="element" select="//flow:nomenclatureAndHierarchyCompliance"/>
			<xsl:with-param name="elementDesc" select="'nomenclatureAndHierarchyCompliance'"/>
			<xsl:with-param name="value1" select="'Fully conform'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>

	</xsl:template>
	<!-- end flow-nomenclatureAndHierarchyCompliance -->



	<!-- flowproperty-documentationCompliance -->
	<xsl:template name="flowproperty-documentationCompliance">
		<xsl:variable name="msgPrefix" select="'Documentation compliance'"/>

		<xsl:call-template name="checkPresence">
			<xsl:with-param name="element" select="//flowproperty:dataSetVersion"/>
			<xsl:with-param name="elementDesc" select="'dataSetVersion'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>

		<xsl:call-template name="checkPresence">
			<xsl:with-param name="element" select="//common:permanentDataSetURI"/>
			<xsl:with-param name="elementDesc" select="'permanentDataSetURI'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>

		<xsl:call-template name="checkValue">
			<xsl:with-param name="element" select="//flowproperty:documentationCompliance"/>
			<xsl:with-param name="elementDesc" select="'documentationCompliance'"/>
			<xsl:with-param name="value1" select="'Fully conform'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>

	</xsl:template>
	<!-- end flowproperty-documentationCompliance -->


	<!-- flowproperty-nomenclatureAndHierarchyCompliance -->
	<xsl:template name="flowproperty-nomenclatureAndHierarchyCompliance">
		<xsl:variable name="msgPrefix" select="'Nomenclature and hierarchy compliance'"/>

		<xsl:call-template name="checkPresence">
			<xsl:with-param name="element"
			   select="//flowproperty:classificationInformation/common:class[@level=0]"/>
			<xsl:with-param name="elementDesc" select="'category with level 0'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>

		<xsl:for-each select="//flowproperty:classificationInformation">
			<xsl:call-template name="check_hierarchy">
				<xsl:with-param name="tree"
					select="$validCategories/categories:flowPropertyCategories"/>
				<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
			</xsl:call-template>
		</xsl:for-each>

		<xsl:call-template name="checkValue">
			<xsl:with-param name="element"
				select="//flowproperty:nomenclatureAndHierarchyCompliance"/>
			<xsl:with-param name="elementDesc" select="'nomenclatureAndHierarchyCompliance'"/>
			<xsl:with-param name="value1" select="'Fully conform'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>

	</xsl:template>
	<!-- end flowproperty-nomenclatureAndHierarchyCompliance -->



	<!-- unitgroup-documentationCompliance -->
	<xsl:template name="unitgroup-documentationCompliance">
		<xsl:variable name="msgPrefix" select="'Documentation compliance'"/>


		<xsl:call-template name="checkPresence">
			<xsl:with-param name="element" select="//unitgroup:dataSetVersion"/>
			<xsl:with-param name="elementDesc" select="'dataSetVersion'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>

		<xsl:call-template name="checkPresence">
			<xsl:with-param name="element" select="//common:permanentDataSetURI"/>
			<xsl:with-param name="elementDesc" select="'permanentDataSetURI'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>

		<!--	enforcement of this rule has been postponed to a later release
		<xsl:call-template name="checkValue">
			<xsl:with-param name="element" select="//unitgroup:documentationCompliance"/>
			<xsl:with-param name="elementDesc" select="'documentationCompliance'"/>
			<xsl:with-param name="value1" select="'Fully conform'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>
		-->
	</xsl:template>
	<!-- end unitgroup-documentationCompliance -->


	<!-- unitgroup-nomenclatureAndHierarchyCompliance -->
	<xsl:template name="unitgroup-nomenclatureAndHierarchyCompliance">
		<xsl:variable name="msgPrefix" select="'Nomenclature and hierarchy compliance'"/>

		<xsl:call-template name="checkValue">
			<xsl:with-param name="element" select="//unitgroup:nomenclatureAndHierarchyCompliance"/>
			<xsl:with-param name="elementDesc" select="'nomenclatureAndHierarchyCompliance'"/>
			<xsl:with-param name="value1" select="'Fully conform'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>

	</xsl:template>
	<!-- end unitgroup-nomenclatureAndHierarchyCompliance -->



	<!-- source-documentationCompliance -->
	<xsl:template name="source-documentationCompliance">
		<xsl:variable name="msgPrefix" select="'Documentation compliance'"/>

		<xsl:call-template name="checkPresence">
			<xsl:with-param name="element" select="//source:dataSetVersion"/>
			<xsl:with-param name="elementDesc" select="'dataSetVersion'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>

		<xsl:call-template name="checkPresence">
			<xsl:with-param name="element" select="//common:permanentDataSetURI"/>
			<xsl:with-param name="elementDesc" select="'permanentDataSetURI'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>

		<!--	enforcement of this rule has been postponed to a later release
		<xsl:call-template name="checkValue">
			<xsl:with-param name="element" select="//source:documentationCompliance"/>
			<xsl:with-param name="elementDesc" select="'documentationCompliance'"/>
			<xsl:with-param name="value1" select="'Fully conform'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>
		-->

	</xsl:template>
	<!-- end source-documentationCompliance -->


	<!-- contact-documentationCompliance -->
	<xsl:template name="contact-documentationCompliance">
		<xsl:variable name="msgPrefix" select="'Documentation compliance'"/>

		<xsl:call-template name="checkPresence">
			<xsl:with-param name="element" select="//contact:dataSetVersion"/>
			<xsl:with-param name="elementDesc" select="'dataSetVersion'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>

		<xsl:call-template name="checkPresence">
		   <xsl:with-param name="element" select="//common:permanentDataSetURI"/>
			<xsl:with-param name="elementDesc" select="'permanentDataSetURI'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>

		<!--	enforcement of this rule has been postponed to a later release
		<xsl:call-template name="checkValue">
			<xsl:with-param name="element" select="//contact:documentationCompliance"/>
			<xsl:with-param name="elementDesc" select="'documentationCompliance'"/>
			<xsl:with-param name="value1" select="'Fully conform'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>
		-->

	</xsl:template>
	<!-- end contact-documentationCompliance -->






	<!-- overallCompliance -->
	<xsl:template name="overallCompliance">
		<xsl:variable name="msgPrefix" select="'Overall compliance'"/>

		<xsl:call-template name="checkValue">
			<xsl:with-param name="element" select="//*[local-name()='approvalOfOverallCompliance']"/>
			<xsl:with-param name="elementDesc" select="'approvalOfOverallCompliance'"/>
			<xsl:with-param name="value1" select="'Fully conform'"/>
			<xsl:with-param name="messagePrefix" select="$msgPrefix"/>
		</xsl:call-template>

	</xsl:template>
	<!-- end overallCompliance -->



	<xsl:template match="*|@*">
		<xsl:apply-templates select="*|@*"/>
	</xsl:template>

	<xsl:template match="text()"/>

</xsl:stylesheet>
