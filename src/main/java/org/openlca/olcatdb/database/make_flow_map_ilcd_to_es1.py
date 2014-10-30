"""

This script creates the flow mapping for the elementary flows from 
ILCD to EcoSpold 01.

author: Michael Srocka

"""

import data

def xfirst(aList, field, value):
	"""
	Returns the first object of the list where the field with the given name
	has the given value.
	"""
	result = xfilter(aList, field, value)
	if len(result) > 0:
		return result[0]

def xfilter(aList, field, value):
	"""
	Returns a list of objects where the field with the given name has the 
	given value.
	"""
	result = filter(lambda (x): getattr(x, field) == value, aList)
	return result
	
def print_assign(ilcdFlow, esFlow, assign):
	"""
	Prints the given assignment.
	"""
	print "\"%s\",%s,%s,%s" % (ilcdFlow.id, esFlow.id, assign.isProxy, 
			assign.factor)
		
def main ():
	# set the path to the directory of the CSV files
	data.dbpath = ".\\database\\"

	# load the data
	
	# ILCD elementary flows
	ilcdFlows = data.select(data.ILCDElemFlowRec)
	
	# EcoSpold 01 elementary flows
	esFlows = data.select(data.ES1ElemFlowRec)
				
	# mapping between ILCD and EcoSpold 01 compartments
	compMap = data.select_map(data.ILCDToES1CompartmentJoin)
	
	# mapping between ILCD and EcoSpold flow names and units
	nameAssigns = data.select(data.ILCDToESFlowNameJoin)
	
	# start the mapping
	for ilcdFlow in ilcdFlows:
		
		# find the matching EcoSpold 01 compartment for the flow
		esCompId = (compMap[ilcdFlow.compartmentId] 
				if compMap.has_key(ilcdFlow.compartmentId) else None) 
		
		if esCompId != None:
			
			# find the corresponding EcoSpold name
			nameAssign = xfirst(nameAssigns, 'ilcdName', ilcdFlow.name)		
			if nameAssign != None:
			
				# filter the EcoSpold flows for the name
				potEsFlows = xfilter(esFlows, 'name', nameAssign.esName)				
				for esFlow in potEsFlows:
					
					# filter the EcoSpold flows for the corresponding compartment
					if esFlow.compartmentId == esCompId:
						print_assign(ilcdFlow, esFlow, nameAssign)						
	
if __name__ == '__main__':
	main()

	
	