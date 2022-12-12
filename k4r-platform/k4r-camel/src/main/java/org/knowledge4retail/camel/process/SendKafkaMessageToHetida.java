package org.knowledge4retail.camel.process;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.knowledge4retail.camel.kafka.Producer;
import org.knowledge4retail.camel.xml.PriceLabel;
import org.knowledge4retail.camel.xml.ShelfScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@RequiredArgsConstructor
@Component
public class SendKafkaMessageToHetida implements Processor {

    private final Producer producer;

    @Value("${org.knowledge4retail.hetida.producer.kafka.topics}")
    private String kafkaTopic;

    @Override
    @Builder
    public void process(Exchange exchange) throws Exception {

        ShelfScan shelfScan = exchange.getIn().getBody(ShelfScan.class);

        for (PriceLabel priceLabel : shelfScan.getPriceLabels()){

            try {

                log.info("send shelfScan");
                sendShelfScan(shelfScan, priceLabel.getBarcode());
            } catch (HttpClientErrorException e) {

                log.info("failed to send kafka message");
                e.printStackTrace();
            }
        }
    }

    private void sendShelfScan(ShelfScan data, Integer barcode) {

        ObjectMapper mapper = new ObjectMapper();
        String escapedJson = null;

        try {

            String json = mapper.writeValueAsString(data);
            escapedJson = json.replace("\"","\\\"");
            log.info("hier   " + escapedJson);
        } catch (JsonProcessingException e) {

            e.printStackTrace();
        }

        String workflowId = "\"workflowId\":\"3Cff38c4-a53d-42d3-a5fa-41e5f48b6a23\"";

        String configuration = "\"configuration\":{" +
                "\"engine\":\"plain\"," +
                "\"name\":\"3Cff38c4-a53d-42d3-a5fa-41e5f48b6a23\"," +
                "\"run_pure_plot_operators\":true" +
                "}";

        String workflowWiring = "\"workflow_wiring\":{" +
                "\"input_wirings\":[{" +
                "\"workflow_input_name\":\"DAN\"," +
                "\"adapter_id\":\"direct_provisioning\"," +
                "\"filters\":{\"value\":\"" + barcode.toString() + "\"}" +
                "},{" +
                "\"workflow_input_name\":\"input\"," +
                "\"adapter_id\":\"direct_provisioning\"," +
                "\"filters\":{\"value\":\"" + escapedJson + "\"}}],\"output_wirings\":[]}";

        String completeWorkflowString = "{" + workflowId + "," + configuration + "," + workflowWiring + "}";
        producer.sendMessage(kafkaTopic, completeWorkflowString);

        //String test = "{\"workflowId\":\"806df1b9-2fc8-4463-943f-3d258c569663\",\"configuration\":{\"engine\":\"plain\", \"name\":\"806df1b9-2fc8-4463-943f-3d258c569663\", \"run_pure_plot_operators\":false}, \"workflow_wiring\": {\"input_wirings\":[{\"workflow_input_name\":\"inp_series\",\"adapter_id\":\"direct_provisioning\",\"filters\":{\"value\":\"{\\\"2020-05-01T00:00:00.000Z\\\":2.5340945967,\\\"2020-06-11T11:00:00.000Z\\\":1.7531690232,\\\"2020-06-11T12:00:00.000Z\\\":1.7563962807,\\\"2020-06-11T13:00:00.000Z\\\":1.7337006137,\\\"2020-06-11T14:00:00.000Z\\\":1.7567559875,\\\"2020-06-11T15:00:00.000Z\\\":1.7314396428}\"}},{\"workflow_input_name\":\"limit\",\"adapter_id\":\"direct_provisioning\",\"filters\":{\"value\":\"1.3\"}},{\"workflow_input_name\":\"num_days_forecast\",\"adapter_id\":\"direct_provisioning\",\"filters\":{\"value\":\"30\"}}],\"output_wirings\":[{\"workflow_output_name\":\"intercept\",\"adapter_id\":\"direct_provisioning\"},{\"workflow_output_name\":\"limit_violation_timestamp\",\"adapter_id\":\"direct_provisioning\"},{\"workflow_output_name\":\"rul_regression_result_plot\",\"adapter_id\":\"direct_provisioning\"},{\"workflow_output_name\":\"slope\",\"adapter_id\":\"direct_provisioning\"}]}}";
        //producer.sendMessage(kafkaTopic, test);
    }
}