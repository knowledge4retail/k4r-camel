package org.knowledge4retail.camel.route.test;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ListenToK4RRoute extends RouteBuilder {

    @Value("${org.knowledge4retail.api.listener.kafka.topics.scan}")
    private String topicName;

    @Override
    public void configure() throws Exception {

        /*String kafkaServer = "?brokers=10.0.0.100:9092";  // broker

        String kafkaListener = new StringBuilder().append("kafka:").append(topicName).append(kafkaServer)
                .toString();

        //String kafkaExample = "kafka:Topic?Broker=localhost:8090&group-id=abc&......";
        String toPath = "file:\\C:\\Users\\mmalki\\Documents";//?fileName=listenFile.txt";

        from(kafkaListener).
                log("listen to scan from K4R: Body \n${body}");
                //to(toPath);*/
    }
}
