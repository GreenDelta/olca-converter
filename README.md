openLCA Format Converter
========================
This repository contains the source code of the openLCA format converter. The 
converter is a tool for converting LCA data sets from one LCA data format to 
another. It currently supports the EcoSpold 1, EcoSpold 2, ILCD, and SimaPro CSV
data format. For more information please see the documentation at 
[openLCA.org](http://openlca.org/).
 
Repository content
------------------
The converter is a plain Java application with a standard 
[Maven project layout](http://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html). 
It uses an embedded [HyperSQL](http://hsqldb.org/) database for mapping entities 
from one format to another. These data are stored as plain CSV text files and 
are contained in this repository. The data sets are converted using 
[Velocity](http://velocity.apache.org/) templates. Additionally, 
schemas, stylesheets, etc. of the different data formats are contained in this
repository. These additional resources can be found under 
[folder](./src/main/resources/org/openlca/olcatdb)).

Building
--------
To build this project you need to have a [Java Development Kit](http://www.oracle.com/technetwork/articles/javase/index-jsp-138363.html) 
\>= 7 and [Maven](http://maven.apache.org/) installed. With this just navigate to
this source code repository and type

	mvn package
	
This will create a single execuatable jar with all dependencies included in the 
target folder.

License
-------
Unless stated otherwise, all source code of the openLCA project is licensed under the 
[Mozilla Public License, v. 2.0](http://mozilla.org/MPL/2.0/). Please see the LICENSE.txt
file in the root directory of the source code.
