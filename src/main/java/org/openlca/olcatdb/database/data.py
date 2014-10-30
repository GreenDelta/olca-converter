"""
 A module for the access and management of the embedded database of
 the converter. 
 
 author: Michael Srocka

"""


import csv

# the path to the database (directory with the CSV files) 
dbpath = './data/'

def select(clazz, atts = None):

	"""
	Returns all instances of the given class from the database where the
	field values match the values of the given attribute map. If the map
	is empty or None all stored instances are returned.
	
	arguments:
	clazz -- the record class which instances should be returned
	atts -- the attribute filter as a map (default = None)
	"""
	
	# read the CSV file
	csvFile = dbpath + clazz.csv_file
	instances = []
	records = csv.reader(open(csvFile), delimiter=',', quotechar='\"')

	# create the records
	for record in records:
		instance = clazz()
		for i in range(0,len(record)):
			if clazz.csv_fields.has_key(i):
				field = clazz.csv_fields[i]
				setattr(instance, field, record[i])
		if match(instance, atts):
			instances.append(instance)
	return instances	

def match(obj, atts):

	"""
	Returns True if the attributes of the given object have the 
	same values as in the given attribute map.

	arguments
	obj -- the object which attribute values have to be tested
	atts -- the map with the attribute value pairs
	"""
	b = True
	if atts != None:
		for key in atts.keys():
			if getattr(obj, key) != atts[key]: 
				b = False
				break
	return b

## EcoSpold 01 data

def filter_records(records, atts):
	res = []
	for rec in records:
		if match(rec, atts):
			res.append(rec)
	return res

##
# The EcoSpold 01 data classes
##

class ES1CompartmentRec():

	"""
	The stored attributes of an EcoSpold 01 compartment.
	"""

	csv_file = 'ES1_COMPARTMENTS.csv'

	csv_fields = {0:'id', 1:'compartment', 2:'subCompartment'}	

	def __init__(self):
		self.id = -1
		self.compartment = ""
		self.subCompartment = ""

	def __str__(self):
		s = 'ES1CompartmentRec[%s,%s,%s]' % (self.id, self.compartment, self.subCompartment)
		return s


class ES1ElemFlowRec:
	"""
	The stored attributes of an EcoSpold 01 elementary flow. 
	"""

	csv_file = 'ES1_ELEM_FLOWS.csv'

	csv_fields = {0:'id', 1:'name', 2:'cas', 3:'formula', 4:'unit', 5:'compartmentId'}

	def __init__(self):
		self.id = -1
		self.name = ""
		self.cas = ""
		self.formula = ""
		self.unit = ""
		self.compartmentId = -1

	def __str__(self):
		s = 'ES1ElemFlowRec[%s,%s,%s,%s,%s,%s]' % (self.id, self.name, self.cas, self.formula, 
					self.unit, self.compartmentId)
		return s

## EcoSpold 02 data
		
# for EcoSpold 02 units
class ES2UnitRec:

	csv_file = 'ES2_UNITS.csv'

	csv_fields = {0:'id', 1:'name'}

	def __init__(self):
		self.id = ""
		self.name = ""

	def __str__(self):
		s = "ES2UnitRec[%s,%s]" % (self.id, self.name) 
		return s

# for EcoSpold 02 compartments
class ES2CompartmentRec:

	csv_file = 'ES2_COMPARTMENTS.csv'

	csv_fields = {0:'id', 1:'compartment', 2:'subCompartment'}

	def __init__(self):
		self.id = ""
		self.compartment = ""
		self.subCompartment = ""

	def __str__(self):
		s = 'ES2CompartmentRec[%s,%s,%s]' % (self.id, self.compartment, self.subCompartment)
		return s
		
# for EcoSpold 02 elementery flows
class ES2ElemFlowRec:
	
	csv_file = 'ES2_ELEM_FLOWS.csv'

	csv_fields = {0:'id', 1:'cas', 2:'formula', 3:'name', 4:'unitId', 5:'compartmentId'}

	def __init__(self):
		self.id = ""
		self.cas = ""
		self.formula = ""
		self.name = ""
		self.unitId = ""
		self.compartmentId = ""

	def __str__(self):
		s = 'ES2ElemFlowRec[%s,%s,%s,%s,%s,%s]' % (self.id, self.name, self.cas, 
						self.formula, self.unitId, self.compartmentId)
		return s
		
# for EcoSpold 02 geographies
class ES2GeographyRec:
	
	csv_file = 'ES2_GEOGRAPHIES.csv'

	csv_fields = {0:'id', 1:'shortName'}

	def __init__(self):
		self.id = ""
		self.shortName = ""

	def __str__(self):
		s = 'ES2GeographyRec[%s,%s]' % (self.id, self.shortName)
		return s

##
# The ILCD data classes
##
		
class ILCDCompartmentRec:
	"""
	The stored attributes of the ILCD compartments (= elementary flow categories)
	"""

	csv_file = 'ILCD_COMPARTMENTS.csv'

	csv_fields = {0:'id', 1:'level0', 2:'level1', 3:'level2', 4:'parentId'}
	
	def __init__(self):
		self.id = ""
		self.level0 = ""
		self.level1 = ""
		self.level2 = ""
		self.parentId = ""

	def __str__(self):
		s = 'ILCDCompartmentRec[%s,%s,%s,%s,%s]' % (self.id, self.level0, 
					self.level1, self.level2, self.parentId)
		return s
		
# for ILCD unit groups
class ILCDUnitGroupRec:

	csv_file = 'ILCD_UNIT_GROUPS.csv'

	csv_fields = {0:'id', 1:'name', 2:'refUnit'}
	
	def __init__(self):
		self.id = ""
		self.name = ""
		self.refUnit = ""

	def __str__(self):
		s = 'ILCDUnitGroupRec[%s,%s,%s]' % (self.id, self.name, self.refUnit)
		return s

# for ILCD flow properties
class ILCDFlowPropertyRec:

	csv_file = 'ILCD_FLOW_PROPERTIES.csv'

	csv_fields = {0:'id', 1:'name', 2:'unitGroupId'}
	
	def __init__(self):
		self.id = ""
		self.name = ""
		self.unitGroupId = ""

	def __str__(self):
		s = 'ILCDFlowPropertyRec[%s,%s,%s]' % (self.id, self.name, self.unitGroupId)
		return s
		
# for ILCD elementary flows
class ILCDElemFlowRec:

	csv_file = 'ILCD_ELEM_FLOWS.csv'

	csv_fields = {0:'id', 1:'name', 2:'cas', 3:'formula', 4:'propertyId', 5:'compartmentId'}
	
	def __init__(self):
		self.id = ""
		self.name = ""
		self.cas = ""
		self.formula = ""
		self.propertyId = ""
		self.compartmentId = ""

	def __str__(self):
		s = 'ILCDElemFlowRec[%s,%s,%s,%s,%s,%s]' % (self.id, self.name, self.cas, self.formula, 
					self.propertyId, self.compartmentId)
		return s

##
# Data assignments and helper functions for data assignment.
##

def select_map(joinClazz):
		"""
		Creates a map for the stored records of the given join class
		where the source identifiers ('from') are the keys and the 
		target identifiers ('to') the values. A join class has two 
		fields: an ID of the source object and an ID of the target
		object. 
		"""
		
		m = {}
		csvFields = joinClazz.csv_fields
		joins = select(joinClazz)

		for join in joins:
			m[getattr(join, csvFields[0])] = getattr(join, csvFields[1])

		return m
			

##
# The compartment assignments.
##

class ES1ToES2CompartmentJoin:
	"""
	A stored assignment of an EcoSpold 01 to an EcoSpold 02 compartment.
	"""

	csv_file = 'COMPARTMENT_MAP_ES1_TO_ES2.csv'

	csv_fields = {0:'es1CompartmentId', 1:'es2CompartmentId'}

	def __init__(self):
		self.es1CompartmentId = ''
		self.es2CompartmentId = ''

	def __str__(self):
		s = 'ES1ToES2CompartmentJoin[%s,%s]' % (self.es1CompartmentId, self.es2CompartmentId)
		return s
			

class ES1ToILCDCompartmentJoin:
	"""
	A stored assignment of an EcoSpold 01 to an ILCD compartment.
	"""

	csv_file = 'COMPARTMENT_MAP_ES1_TO_ILCD.csv'

	csv_fields = {0:'es1CompartmentId', 1:'ilcdCompartmentId'}

	def __init__(self):
		self.es1CompartmentId = ''
		self.ilcdCompartmentId = ''

	def __str__(self):
		s = 'ES1ToILCDCompartmentJoin[%s,%s]' % (self.es1CompartmentId, self.ilcdCompartmentId)
		return s


class ES2ToILCDCompartmentJoin:
	"""
	A stored assignment of an EcoSpold 02 to an ILCD compartment.
	"""

	csv_file = 'COMPARTMENT_MAP_ES2_TO_ILCD.csv'

	csv_fields = {0:'es2CompartmentId', 1:'ilcdCompartmentId'}

	def __init__(self):
		self.es2CompartmentId = ''
		self.ilcdCompartmentId = ''

	def __str__(self):
		s = 'ES2ToILCDCompartmentJoin[%s,%s]' % (self.es2CompartmentId, self.ilcdCompartmentId)
		return s


class ILCDToES1CompartmentJoin:
	"""
	A stored assignment of an ILCD to an EcoSpold 01 compartment.
	"""

	csv_file = 'COMPARTMENT_MAP_ILCD_TO_ES1.csv'

	csv_fields = {0:'ilcdCompartmentId',1:'es1CompartmentId'}

	def __init__(self):
		self.ilcdCompartmentId = ''
		self.es1CompartmentId = ''

	def __str__(self):
		s = 'ILCDToES1CompartmentJoin[%s,%s]' % (self.ilcdCompartmentId, self.es1CompartmentId)
		return s


class ILCDToES2CompartmentJoin:
	"""
	A stored assignment of an ILCD to an EcoSpold 02 compartment.
	"""

	csv_file = 'COMPARTMENT_MAP_ILCD_TO_ES2.csv'

	csv_fields = {0:'ilcdCompartmentId',1:'es2CompartmentId'}

	def __init__(self):
		self.ilcdCompartmentId = ''
		self.es2CompartmentId = ''

	def __str__(self):
		s = 'ILCDToES2CompartmentJoin[%s,%s]' % (self.ilcdCompartmentId, self.es2CompartmentId)
		return s


##
# Flow name assignments
##

class ESToILCDFlowNameJoin:
	"""
	A stored assignment of an EcoSpold flow name to an ILCD flow name.
	"""

	csv_file = 'FLOW_NAME_MAP_ES_TO_ILCD.csv'	

	csv_fields = {0:'esName', 1:'esUnit', 2:'ilcdName', 3:'ilcdUnit', 4:'isProxy', 5:'factor'}

	def __init__(self):
		self.esName = ''
		self.esUnit = ''
		self.ilcdName = ''
		self.ilcdUnit = ''
		self.isProxy = ''
		self.factor = ''

	def __str__(self):
		s = 'ESToILCDFlowNameJoin[%s,%s,%s,%s,%s,%s]' % (self.esName, self.esUnit, self.ilcdName, 
					self.ilcdUnit, self.isProxy, self.factor)
		return s

		
class ILCDToESFlowNameJoin:
	"""
	A stored assignment of an ILCD flow name to an EcoSpold flow name.
	"""
	
	csv_file = 'FLOW_NAME_MAP_ILCD_TO_ES.csv'
	
	csv_fields = {0:'ilcdName', 1:'ilcdUnit', 2:'esName', 3:'esUnit', 4:'isProxy', 5:'factor'}
	
	def __init__(self):
		self.ilcdName = ''
		self.ilcdUnit = ''
		self.esName = ''
		self.esUnit = ''		
		self.isProxy = ''
		self.factor = ''
	
	def __str__(self):
		s = 'ILCDToESFlowNameJoin[%s,%s,%s,%s,%s,%s]' % (self.ilcdName, self.ilcdUnit,self.esName, 
					self.esUnit,  self.isProxy, self.factor)
		return s