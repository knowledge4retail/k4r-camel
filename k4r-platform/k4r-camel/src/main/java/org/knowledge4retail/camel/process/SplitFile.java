package org.knowledge4retail.camel.process;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.knowledge4retail.camel.xml.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
@Component
public class SplitFile implements Processor {

    @Value("${org.knowledge4retail.camel.kafka.enabled}")
    private String kafkaEnabled;

    @Override
    @Builder
    public void process(Exchange exchange) throws Exception {

        StoreScan data = exchange.getIn().getBody(StoreScan.class);

        log.info("sort priceLabels to shelfLayers");
        HashMap<String, ShelfLayerScan> shelfLayerScanMap = new HashMap<>();
        for(ShelfLayer shelfLayer : data.getShelfLayers()){

            ShelfLayerScan shelfLayerScan = new ShelfLayerScan();
            shelfLayerScan.setShelfLayer(shelfLayer);
            shelfLayerScan.setPriceLabels(new ArrayList<>());
            shelfLayerScanMap.put(shelfLayer.getUbicaExternalReferenceId(), shelfLayerScan);
        }

        for(PriceLabel priceLabel : data.getPriceLabels()){

            try{

                String id = priceLabel.getShelfLayer().getUbicaExternalReferenceId();
                shelfLayerScanMap.get(id).getPriceLabels().add(priceLabel);
            } catch (NullPointerException e) {

                log.info(String.format("priceLabel with barcode %d cannot be directed to shelfLayer", priceLabel.getBarcode()));
            }
        }

        log.info("sort shelfLayers to ShelfSystems");
        HashMap<String, ShelfScan> shelfScanHashMap = new HashMap<>();
        for(ShelfSystem shelf : data.getShelfSystems()){

            ShelfScan shelfScan = new ShelfScan();
            shelfScan.setShelf(shelf);
            shelfScan.setStoreId(data.getStoreId());
            shelfScan.setStamp(data.getStamp());
            shelfScan.setShelfLayers(new ArrayList<>());
            shelfScan.setPriceLabels(new ArrayList<>());
            shelfScanHashMap.put(shelf.getUbicaExternalReferenceId(), shelfScan);
        }

        for(ShelfLayerScan shelfLayerScan : shelfLayerScanMap.values()) {

            String id = shelfLayerScan.getShelfLayer().getShelfSystem().getUbicaExternalReferenceId();
            shelfScanHashMap.get(id).getShelfLayers().add(shelfLayerScan.getShelfLayer());
            shelfScanHashMap.get(id).getPriceLabels().addAll(shelfLayerScan.getPriceLabels());
        }

        exchange.getMessage().setBody(shelfScanHashMap.values());
        exchange.getMessage().setHeader("kafkaEnabled", kafkaEnabled);
    }
}