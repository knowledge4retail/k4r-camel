package org.knowledge4retail.camel.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ListenToUbicaRoute extends RouteBuilder {

    @Value("${ubica.kafka.topic}")
    private String topicName;
    @Value("${ubica.kafka.server}")
    private String kafkaServer;
    @Value("${ubica.kafka.group}")
    private String kafkaGroup;
    @Value("${ubica.kafka.fromBeginning}")
    private String fromBeginning;
    @Value("${ubica.kafka.security}")
    private String securityProtocol;


    @Override
    public void configure() throws Exception {

        /*String kafkaListener = new StringBuilder().append("kafka:").append(topicName).append("?brokers=").append(kafkaServer).append("&groupId=")
                .append(kafkaGroup).append("&autoOffsetReset=").append(fromBeginning).append("&securityProtocol=").append(securityProtocol).toString();

        JaxbDataFormat jaxbDataFormat = new JaxbDataFormat();
        JAXBContext context = JAXBContext.newInstance(StoreScan.class);
        jaxbDataFormat.setContext(context);

        from(kafkaListener).
                routeId("kafkaRoute").
                log("Initial Data: Listen from Ubica Kafka \n").

                unmarshal(jaxbDataFormat).
                log("data after unmarshal \n").

                multicast().
                //.to("direct:sendJsonToHetida", "direct:sendMessageToK4R");
                to("direct:sendMessageToK4R");*/
    }
}
