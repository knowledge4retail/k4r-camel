package org.knowledge4retail.camel.xml;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class ShelfLayer implements BasicXml {

    @XmlAttribute(name = "id", namespace = "http://www.dm.de/eai/schema/intf00000107/v1")
    private String ubicaExternalReferenceId;

    @XmlAttribute(namespace = "http://www.dm.de/eai/schema/intf00000107/v1")
    private String stamp;

    @XmlAttribute(name ="runningNumber", namespace = "http://www.dm.de/eai/schema/intf00000107/v1")
    private Integer runningNumber;

    @XmlElement(name = "dimensions", namespace = "http://www.dm.de/eai/schema/intf00000107/v1")
    private Dimension dimension;

    @XmlElement(name = "transform", namespace = "http://www.dm.de/eai/schema/intf00000107/v1")
    private Transform transform;

    @XmlElement(name = "shelfSystem", namespace = "http://www.dm.de/eai/schema/intf00000107/v1")
    private ShelfSystemId shelfSystem;
}
