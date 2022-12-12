package org.knowledge4retail.camel.route;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.knowledge4retail.camel.xml.StoreScan;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;

@Slf4j
@Component
public class SendMessageToK4RRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        JaxbDataFormat jaxbDataFormat = new JaxbDataFormat();
        JAXBContext context = JAXBContext.newInstance(StoreScan.class);
        jaxbDataFormat.setContext(context);

        from("direct:sendMessageToK4R")
                .routeId("splitXmlRoute")
                .log("Split all Scan Data into many small dtos according to the datamodel \n")

                .log("Process to split complete data to shelfScans \n")
                .process("splitFile")
                .split(body())

                .process("sendRestToK4R")
                .log("Data has been sent to REST URL of DT API");
    }
}


