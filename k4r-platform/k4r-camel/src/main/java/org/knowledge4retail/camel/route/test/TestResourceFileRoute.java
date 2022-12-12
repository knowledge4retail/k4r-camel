package org.knowledge4retail.camel.route.test;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TestResourceFileRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        //from("file:\\C:\\Users\\mmalki\\Documents\\camel\\pasteManually")
        /*from("file:k4r-platform/k4r-camel/src/main/resources/xml?noop=true")
                .routeId("ResourceTestRoute")
                .log("Take file from resource and send to local folder result")
                .to("file:\\C:\\Users\\mmalki\\Documents\\camel\\result?filename=resourceFile.xml");*/
    }

}
