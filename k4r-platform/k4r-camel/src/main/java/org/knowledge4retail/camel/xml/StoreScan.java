package org.knowledge4retail.camel.xml;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "storeScan", namespace = "http://www.dm.de/eai/schema/intf00000107/v1")
public class StoreScan implements BasicXml {

    @XmlAttribute(namespace = "http://www.dm.de/eai/schema/intf00000107/v1")
    private Integer storeId;

    @XmlAttribute(namespace = "http://www.dm.de/eai/schema/intf00000107/v1")
    private String stamp;

    @XmlAttribute(namespace = "http://www.dm.de/eai/schema/intf00000107/v1")
    private String customerId;

    @XmlElement(name = "shelfSystem" ,namespace = "http://www.dm.de/eai/schema/intf00000107/v1")
    private List<ShelfSystem> shelfSystems;

    @XmlElement(name = "shelfLayer" ,namespace = "http://www.dm.de/eai/schema/intf00000107/v1")
    private List<ShelfLayer> shelfLayers;

    @XmlElement(name = "priceLabel" ,namespace = "http://www.dm.de/eai/schema/intf00000107/v1")
    private List<PriceLabel> priceLabels;
}
