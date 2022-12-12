package org.knowledge4retail.camel.process;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.knowledge4retail.camel.dto.ScanDto;
import org.knowledge4retail.camel.kafka.Producer;
import org.knowledge4retail.camel.xml.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class SendKafkaMessageToK4R implements Processor {

    private final Producer producer;
    @Value("${org.knowledge4retail.api.listener.kafka.topics.ubicascan}")
    private String kafkaTopic;

    @Override
    @Builder
    public void process(Exchange exchange) throws Exception {

        ShelfScan shelfScan = exchange.getIn().getBody(ShelfScan.class);

        ScanDto storeDto = ScanDto.builder().
                id(shelfScan.getStoreId().toString()).
                timestamp(shelfScan.getStamp()).
                origin("ubica").
                entityType("store").
                build();
        producer.publishCreate(kafkaTopic, storeDto);

        ShelfSystem shelfSystem = shelfScan.getShelf();

        ScanDto shelfDto = ScanDto.builder().
                id(shelfSystem.getLayout().getDmShelfId()).
                timestamp(shelfSystem.getStamp()).
                entityType("shelf").
                origin("ubica").
                width(shelfSystem.getDimension().getWidth()).
                height(shelfSystem.getDimension().getHeight()).
                depth(shelfSystem.getDimension().getDepth()).
                positionX(shelfSystem.getTransform().getTranslation().getX()).
                positionY(shelfSystem.getTransform().getTranslation().getY()).
                positionZ(shelfSystem.getTransform().getTranslation().getZ()).
                orientationX(shelfSystem.getTransform().getRotation().getX()).
                orientationY(shelfSystem.getTransform().getRotation().getY()).
                orientationZ(shelfSystem.getTransform().getRotation().getZ()).
                orientationW(shelfSystem.getTransform().getRotation().getW()).
                externalReferenceId(storeDto.getId()).
                build();

        producer.publishCreate(kafkaTopic, shelfDto);


        List<ShelfLayer> shelfLayers = shelfScan.getShelfLayers();
        for (ShelfLayer shelfLayer : shelfLayers) {

            ScanDto shelfLayerDto = ScanDto.builder().
                    id(shelfSystem.getLayout().getDmShelfId()).
                    timestamp(shelfLayer.getStamp()).
                    entityType("shelfLayer").
                    origin("ubica").
                    width(shelfLayer.getDimension().getWidth()).
                    height(0.0).
                    depth(shelfLayer.getDimension().getDepth()).
                    positionX(shelfLayer.getTransform().getTranslation().getX()).
                    positionY(shelfLayer.getTransform().getTranslation().getY()).
                    positionZ(shelfLayer.getTransform().getTranslation().getZ()).
                    orientationX(shelfLayer.getTransform().getRotation().getX()).
                    orientationY(shelfLayer.getTransform().getRotation().getY()).
                    orientationZ(shelfLayer.getTransform().getRotation().getZ()).
                    orientationW(shelfLayer.getTransform().getRotation().getW()).
                    externalReferenceId(storeDto.getId()).
                    additionalInfo(shelfLayer.getRunningNumber().toString()).
                    build();

            producer.publishCreate(kafkaTopic, shelfLayerDto);
        }

        /*List<PriceLabel> priceLabels = shelfScan.getPriceLabels();
        for (PriceLabel priceLabel : priceLabels) {

            Integer facingId = null;
            String externalReferenceId = shelfScan.getPriceLabels().get(0).getShelfLayer().getUbicaExternalReferenceId();
            String gtin = "GTIN_1000";
            // get gtin from denodo

            try {

                facingId = facingIdRestRequest("SHELF_LAYER_ID", gtin);
                //facingId = sendRestRequest(externalReferenceId, gtin);

            } catch (HttpClientErrorException e) {

                log.info("facing not found in the database");
            }

            ScanDto labelDto = ScanDto.builder().
                    id(priceLabel.getBarcode().toString()).
                    timestamp(priceLabel.getStamp()).
                    entityType("label").
                    origin("ubica").
                    positionX(priceLabel.getTransform().getTranslation().getX()).
                    positionY(priceLabel.getTransform().getTranslation().getY()).
                    positionZ(priceLabel.getTransform().getTranslation().getZ()).
                    orientationX(priceLabel.getTransform().getRotation().getX()).
                    orientationY(priceLabel.getTransform().getRotation().getY()).
                    orientationZ(priceLabel.getTransform().getRotation().getZ()).
                    orientationW(priceLabel.getTransform().getRotation().getW()).
                    externalReferenceId().
                    build();

            producer.publishCreate(kafkaTopic, labelDto);
        }*/

        log.info(shelfScan.toString());
    }
}