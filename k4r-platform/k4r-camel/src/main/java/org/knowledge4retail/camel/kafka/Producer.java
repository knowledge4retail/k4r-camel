package org.knowledge4retail.camel.kafka;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.camel.dto.BasicDto;
import org.knowledge4retail.camel.kafka.model.CRUDAction;
import org.knowledge4retail.camel.kafka.model.KafkaMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class Producer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${org.knowledge4retail.camel.kafka.enabled}")
    private boolean kafkaEnabled;

    public void publishCreate(String topic, BasicDto dto)
    {

        String message = buildMessage(CRUDAction.CREATE, dto, null);
        sendMessage(topic, message);
    }

    public void sendMessage(String topic, String message)
    {

        if (topic == null || topic.isEmpty()) {

            IllegalArgumentException e = new IllegalArgumentException("No topics provided");
            log.error(e.toString());
            throw e;
        }

        Message<String> message1 = MessageBuilder
                .withPayload(message)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .setHeader(KafkaHeaders.MESSAGE_KEY, "test_executionissuer_1")
                .setHeader(KafkaHeaders.PARTITION_ID, 0)
                .setHeader("X-Custom-Header", "Sending Custom Header with Spring Kafka")
                .build();

        if (kafkaEnabled) {

            log.debug(String.format("KafkaProducer: message %1$s sent to topic %2$s", message, topic));

            kafkaTemplate.send(message1);
        } else {

            log.debug("Skipping Kafka send operation. Disabled in ${org.knowledge4retail.camel.kafka.enabled}");
        }
    }

    public String buildMessage(CRUDAction action, BasicDto dto, BasicDto oldDto)
    {

        if (dto == null) {

            IllegalArgumentException e = new IllegalArgumentException("dto must not be null");
            log.error(e.toString());
            throw e;
        }

        String result = null;
        KafkaMessage message = KafkaMessage.builder().
                action(action).
                object(dto).
                oldObject(oldDto).
                build();

        try {

            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); // do not include oldObject if it's null
            result = mapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {

            log.error(String.format("Error creating KafkaMessage from payload %s", message.toString()), e);
        }

        return result;
    }
}
