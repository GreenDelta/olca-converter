package org.openlca.olcatdb.conversion.es2tocsv;

import java.util.ArrayList;
import java.util.List;

import com.greendeltatc.simapro.csv.model.SPUnit;

/**
 * 
 * @author Imo Graf
 *
 */
public class ActivityEntry {

	String fileName;
	String id;
	ActivityType activityType = ActivityType.PROCESS;
	String refFlowId;
	String refFlowName;
	SPUnit refFlowUnit;
	String refFlowAmount;
	List<String> changeProducts = new ArrayList<String>();
	
}
