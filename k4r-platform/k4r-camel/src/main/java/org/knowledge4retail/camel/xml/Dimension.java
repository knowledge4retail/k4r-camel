package org.knowledge4retail.camel.xml;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Dimension implements BasicXml {

    @XmlElement(namespace = "http://www.dm.de/eai/schema/intf00000107/v1")
    private Double width;

    @XmlElement(namespace = "http://www.dm.de/eai/schema/intf00000107/v1")
    private Double height;

    @XmlElement(namespace = "http://www.dm.de/eai/schema/intf00000107/v1")
    private Double depth;
}
