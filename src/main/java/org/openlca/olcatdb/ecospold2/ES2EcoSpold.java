package org.openlca.olcatdb.ecospold2;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.SubContext;

/**
 * The data set container for EcoSpold 2 data sets.
 * 
 * @ContentModel (activityDataset*, namespace:uri="##other")
 */
@Context(name = "ecoSpold")
public class ES2EcoSpold extends ContextObject {

	private boolean fitDatasets = false;

	// context fields and sub-contexts

	@SubContext(isMultiple = true, contextClass = ES2Dataset.class)
	private List<ES2Dataset> datasets = new ArrayList<ES2Dataset>();

	public List<ES2Dataset> getDatasets() {
		if (!fitDatasets)
			fitDataSets();
		return datasets;
	}

	@SubContext(isMultiple = true, contextClass = ES2ChildDataset.class)
	private List<ES2ChildDataset> childDatasets = new ArrayList<ES2ChildDataset>();

	// constructors

	public ES2EcoSpold() {
	}

	// factory methods

	/**
	 * Creates a new activity data set and adds it to this EcoSpold type.
	 */
	public ES2Dataset makeDataset() {
		ES2Dataset d = new ES2Dataset();
		this.datasets.add(d);
		return d;
	}

	private void fitDataSets() {
		List<ES2Dataset> es2Datasets = new ArrayList<ES2Dataset>();
		es2Datasets.addAll(datasets);
		for (ES2ChildDataset childDataset : childDatasets) {
			childDataset.isChildDataSet = true;
			es2Datasets.add((ES2Dataset) childDataset);
		}
		datasets.clear();
		datasets.addAll(es2Datasets);
		fitDatasets = true;
	}

}
