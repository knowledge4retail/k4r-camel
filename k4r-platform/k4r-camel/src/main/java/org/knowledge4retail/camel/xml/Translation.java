package org.knowledge4retail.camel.xml;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Translation implements BasicXml {

    @XmlAttribute(name = "x", namespace = "http://www.dm.de/eai/schema/intf00000107/v1")
    private Double x;

    @XmlAttribute(name = "y", namespace = "http://www.dm.de/eai/schema/intf00000107/v1")
    private Double y;

    @XmlAttribute(name = "z", namespace = "http://www.dm.de/eai/schema/intf00000107/v1")
    private Double z;
}
