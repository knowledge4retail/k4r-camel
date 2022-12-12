package org.knowledge4retail.camel.route;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.knowledge4retail.camel.xml.StoreScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;

@Slf4j
@Component
public class ListenToLocalFolderRoute extends RouteBuilder {

    @Value("${org.knowledge4retail.camel.scanFolder}")
    private String initFolder;

    @Override
    public void configure() throws Exception {

        JaxbDataFormat jaxbDataFormat = new JaxbDataFormat();
        JAXBContext context = JAXBContext.newInstance(StoreScan.class);
        jaxbDataFormat.setContext(context);

        from("file:" + initFolder + "?noop=true").
                routeId("localRoute").
                log("Initial Data: paste file into folder to insert data \n").

                unmarshal(jaxbDataFormat).
                log("data after unmarshal \n").

                //multicast().
                //.to("direct:sendJsonToHetida", "direct:sendMessageToK4R");
                to("direct:sendMessageToK4R");
    }
}


