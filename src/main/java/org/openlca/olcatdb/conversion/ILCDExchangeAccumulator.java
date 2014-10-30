package org.openlca.olcatdb.conversion;

import java.util.HashMap;
import java.util.Map;

import org.openlca.olcatdb.ilcd.ILCDExchange;

public class ILCDExchangeAccumulator {

	public void accumulate(ILCDExchange exchange1, ILCDExchange exchange2) {
		if (exchange1.uncertaintyDistribution == null && exchange2.uncertaintyDistribution == null){
			exchange1.meanAmount += exchange2.meanAmount;
			exchange1.resultingAmount += exchange2.resultingAmount;
		}
		// TODO accumulate uncertainty distributed exchanges
	}

}
