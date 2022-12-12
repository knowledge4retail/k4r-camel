package org.knowledge4retail.camel.xml;

import lombok.Data;

import java.util.List;

@Data
public class ShelfLayerScan implements BasicXml {

    private ShelfLayer shelfLayer;

    private List<PriceLabel> priceLabels;
}
