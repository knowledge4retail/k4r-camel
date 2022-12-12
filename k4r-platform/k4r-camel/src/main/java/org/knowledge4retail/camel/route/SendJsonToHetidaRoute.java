package org.knowledge4retail.camel.route;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SendJsonToHetidaRoute extends RouteBuilder {

    @Value("${org.knowledge4retail.camel.resultFolder}")
    private String resultFolder;

    @Override
    public void configure() throws Exception {

        /*String kafkaServer = "?brokers=10.0.0.100:9092";
        String kafkaGroup = "groupId=dm.k4r";
        String kafkaProducer = new StringBuilder().append("kafka:").append(kafkaTopic).append(kafkaServer).append("&").append(kafkaGroup).toString();*/

        from("direct:sendJsonToHetida").
                routeId("sendJsonToHetida").
                //log("Body \n ${body}").

                log("process to split complete data to shelfScans \n").
                process("splitFile").
                split(body()).

                log("add dimension, description and marshal\n").
                //process("getData").

                choice().
                    when(header("kafkaEnabled").isEqualTo(true)).
                        log("send Kafka messages to Hetida designer").
                        process("sendKafkaMessageToHetida").
                        //.to(kafkaProducer);
                        endChoice().
                    otherwise().
                        marshal().json(JsonLibrary.Jackson).
                        to("file:" + resultFolder + "?fileName=${header.shelfId}.json");
    }
}


