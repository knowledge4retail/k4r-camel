package org.knowledge4retail.camel.route.test;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TestXSLTRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        /*from("file:k4r-platform/k4r-camel/src/main/resources/xml?noop=true")
                .routeId("testXSLT")
                .to("xslt:xslt\\change.xsl")
                .log("paste file into folder to get data ${body}");

        from("direct:toK4RListener")
          .to("log:org.knowledge4retail.camel");*/
    }

}
