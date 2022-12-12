package org.knowledge4retail.camel.process;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.knowledge4retail.camel.dto.ScanDto;
import org.knowledge4retail.camel.xml.PriceLabel;
import org.knowledge4retail.camel.xml.ShelfLayer;
import org.knowledge4retail.camel.xml.ShelfScan;
import org.knowledge4retail.camel.xml.ShelfSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class SendRestToK4R implements Processor {

    @Value("${k4r.host}")
    private String url;

    @Autowired
    private RestTemplate restTemplate;

    @ConditionalOnProperty(name="auth.useSslAuthenticatedCalls", havingValue="true")
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


        String path = "api/v0/scans";
        HttpEntity<ScanDto> storeEntity = new HttpEntity<>(storeDto);
        restTemplate.postForObject(url + path, storeEntity, ScanDto.class);

        ShelfSystem shelfSystem = shelfScan.getShelf();

        try {
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
                    additionalInfo(shelfSystem.getLayout().getMeter().toString() + "_" + shelfSystem.getLayout().getBaustein().toString()).
                    build();

            HttpEntity<ScanDto> shelfEntity = new HttpEntity<>(shelfDto);
            restTemplate.postForObject(url + path, shelfEntity, ScanDto.class);

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

                HttpEntity<ScanDto> shelfLayerEntity = new HttpEntity<>(shelfLayerDto);
                restTemplate.postForObject(url + path, shelfLayerEntity, ScanDto.class);

                List<PriceLabel> priceLabels = shelfScan.getPriceLabels();
                for (PriceLabel priceLabel : priceLabels) {

                    if(shelfLayer.getUbicaExternalReferenceId().equals(priceLabel.getShelfLayer().getUbicaExternalReferenceId()) ) {

                        ScanDto priceLabelDto = ScanDto.builder().
                                id(String.format("%d", priceLabel.getBarcode())).
                                timestamp(priceLabel.getStamp()).
                                entityType("barcode").
                                origin("ubica").
                                width(null).
                                height(null).
                                depth(null).
                                positionX(priceLabel.getTransform().getTranslation().getX()).
                                positionY(priceLabel.getTransform().getTranslation().getY()).
                                positionZ(priceLabel.getTransform().getTranslation().getZ()).
                                orientationX(null).
                                orientationY(null).
                                orientationZ(null).
                                orientationW(null).
                                externalReferenceId(shelfSystem.getLayout().getDmShelfId()).
                                additionalInfo(shelfLayer.getRunningNumber().toString()).
                                build();

                        HttpEntity<ScanDto> priceLabelEntity = new HttpEntity<>(priceLabelDto);
                        restTemplate.postForObject(url + path, priceLabelEntity, ScanDto.class);
                    }
                }
            }
        } catch (NullPointerException e){

            log.info(String.format("shelf with externalReferenceId %s cannot assigned due to missing data", shelfSystem.getUbicaExternalReferenceId()));
        }

        log.info(shelfScan.toString());
    }
}