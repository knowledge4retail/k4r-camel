# k4r-camel

## Add data to K4R dt api

This solution is only a temporary solution, where the data is pushed manually in to the platform. 
The used ubica scan data is loaded locally from the folder defined in application-build.properties &#8594; org.knowledge4retail.camel.scanFolder

application.properties define the used properties for the data input.

k4r.host correspond to the url of the k4r instance (for instance: https://dt-api.dev.knowledge4retail.org/k4r/)
The auth.ssl.keyLocation has to be loaded from a local folder. The p12-Key is provided from the platform owner.

Alternatively, one may also apply the kafka route, which is also defined in the application.properties.

## Applikation 

By starting the application, the ubica scan is separated at shelf level. Each shelf is then send as a REST Request to the scan entity.