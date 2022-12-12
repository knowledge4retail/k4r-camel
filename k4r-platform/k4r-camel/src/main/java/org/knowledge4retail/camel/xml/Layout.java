package org.knowledge4retail.camel.xml;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Layout implements BasicXml {

    @XmlElement(name = "dmShelfId", namespace = "http://www.dm.de/eai/schema/intf00000107/v1")
    private String dmShelfId;

    @XmlElement(name = "baustein", namespace = "http://www.dm.de/eai/schema/intf00000107/v1")
    private Integer baustein;

    @XmlElement(name = "meter", namespace = "http://www.dm.de/eai/schema/intf00000107/v1")
    private Integer meter;
}
