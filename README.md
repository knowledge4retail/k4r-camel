# K4R Camel Adapter

- [K4R Camel Adapter](#k4r-camel-adapter)
    - [Relation to K4R Project](#relation-to-k4r-project)
    - [Instruction and Hints](#instruction-and-hints)
    - [Application](#application)
    - [Outlook](#outlook)

## Relation to K4R Project

The purpose of the K4R camel adapter is to create an ELT-pipeline. The main task was to extract the required
information from the robotics scan and to upload them to the K4R platform.

## Instruction and Hints

The current status corresponds to a temporary solution, where the data is pushed manually into the platform.
To automate the process, the adapter must have permanent access to the scan data.
Since only a few specific scans were provided, the adapter was used manually.

After manually starting the camel process, the program will observe all files which are stored in the folder
defined in:
> application-build.properties &#8594; org.knowledge4retail.camel.scanFolder

So in order to upload the scan data into the K4R platform, one have to paste the xml scan data into the folder.

Note that the camel adapter does not directly insert the data into the database. The camel adapter
uses the REST Interface of K4R for the sake of control and traceability. The URL of the K4R REST Interface is given by
> application-build.properties &#8594; k4r.host

For instance: https://dt-api.dev.knowledge4retail.org/k4r/.

Since the REST interface is secured by a certification, a valid key must also be passed.
The p12-Key is provided from the platform owner and should then be loaded from a local folder which is specified in:
> application-build.properties &#8594; The auth.ssl.keyLocation

## Application

By starting the application, the scan data is separated at shelf level. Each shelf is then sent as a REST Request to
the scan entity. The scan service will split up the data and store the input data into the corresponding entities and
the scan entity. The robotics scan will provide input data for the following entities:

- store
- shelf
- shelf_layer
- barcode
- product_gtin

Note that facing is currently not provided in the robotics scan, so that the facing entity will not upload with data.
The identifications given in the robotics scans will be passed as external reference ids for a permanent assignment.

The barcode contains only an external reference so that the corresponding GTIN is not provided. Up to now, the link
from the product GTIN entity to the product unit entity will be added afterwards.

The length unit in the complete scan data are given in meter and should continue to be processed as such. The length
unit is not explicitly passed in the REST request. The length unit id is provided by the scan service from the
K4R platform in the digital twin.

## Outlook

In order to automate the K4R adapter, permanent access to the scan data must be enabled. This can be done be
enabling access to a specific volume or by using kafka. The kafka route is an alternative opportunity to
get the scan data. The kafka server and further settings can be set in the application.properties file.

A docker and docker-compose file are already provided for a containerized framework. The camel adapter has
enabled hawtio in order to manage the camel process and applying a readiness check.
