"""

This script creates the flow mapping for the elementary flows from 
EcoSpold 02 to ILCD.

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
	
def print_assign(esFlow, ilcdFlow, assign):
	"""
	Prints the given assignment.
	"""
	print "\"%s\",\"%s\",%s,%s" % (esFlow.id, ilcdFlow.id, assign.isProxy, 
			assign.factor)
		
def main ():
	# set the path to the directory of the CSV files
	data.dbpath = ".\\database\\"

	# load the data
	
	# EcoSpold 02 elementary flows
	esFlows = data.select(data.ES2ElemFlowRec)
	
	# ILCD elementary flows
	ilcdFlows = data.select(data.ILCDElemFlowRec)
	
	# ILCD compartments
	ilcdComps = data.select(data.ILCDCompartmentRec)
	
	# mapping between EcoSpold 02 and ILCD compartments
	compMap = data.select_map(data.ES2ToILCDCompartmentJoin)
	
	# mapping between EcoSpold and ILCD flow names and units
	nameAssigns = data.select(data.ESToILCDFlowNameJoin)
	
	# start the mapping
	for esFlow in esFlows:
		
		# find the matching ILCD compartment for the flow
		ilcdCompId = (compMap[esFlow.compartmentId] 
				if compMap.has_key(esFlow.compartmentId) else None) 
		
		if ilcdCompId != None:
			
			# find the corresponding ILCD name
			nameAssign = xfirst(nameAssigns, 'esName', esFlow.name)		
			if nameAssign != None:
			
				# filter the ILCD flows for the name
				potIlcdFlows = xfilter(ilcdFlows, 'name', nameAssign.ilcdName)				
				for ilcdFlow in potIlcdFlows:
					
					# filter the ILCD flows for the corresponding compartment
					ilcdComp = xfirst(ilcdComps, 'id', ilcdFlow.compartmentId)					
					if ilcdComp.id == ilcdCompId or ilcdComp.parentId == ilcdCompId:
						print_assign(esFlow, ilcdFlow, nameAssign)						
	
if __name__ == '__main__':
	main()

	
	