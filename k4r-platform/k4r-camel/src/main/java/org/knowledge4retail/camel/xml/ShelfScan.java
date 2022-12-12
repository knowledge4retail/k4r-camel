package org.knowledge4retail.camel.xml;

import lombok.Data;

import java.util.List;

@Data
public class ShelfScan implements BasicXml {

    private Integer storeId;

    private String stamp;

    private ShelfSystem shelf;

    private List<ShelfLayer> shelfLayers;

    private List<PriceLabel> priceLabels;
}
