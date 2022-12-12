package org.knowledge4retail.camel.xml;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class PriceLabel implements BasicXml {

    @XmlElement(namespace = "http://www.dm.de/eai/schema/intf00000107/v1")
    private Integer barcode;

    @XmlAttribute(namespace = "http://www.dm.de/eai/schema/intf00000107/v1")
    private String stamp;

    @XmlElement(name = "shelfLayer", namespace = "http://www.dm.de/eai/schema/intf00000107/v1")
    private ShelfLayerId shelfLayer;

    @XmlElement(name = "transform", namespace = "http://www.dm.de/eai/schema/intf00000107/v1")
    private Transform transform;

    private Product product;

    private String description;

    private String pictureUrl;
}
