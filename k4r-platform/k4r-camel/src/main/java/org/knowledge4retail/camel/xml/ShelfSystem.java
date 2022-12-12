package org.knowledge4retail.camel.xml;

import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class ShelfSystem implements BasicXml {

    @XmlAttribute(name = "id", namespace = "http://www.dm.de/eai/schema/intf00000107/v1")
    private String ubicaExternalReferenceId;

    @XmlAttribute(namespace = "http://www.dm.de/eai/schema/intf00000107/v1")
    private String stamp;

    @XmlElement(name = "dimensions", namespace = "http://www.dm.de/eai/schema/intf00000107/v1")
    private Dimension dimension;

    @XmlElement(name = "transform", namespace = "http://www.dm.de/eai/schema/intf00000107/v1")
    private Transform transform;

    @XmlElement(name = "layout", namespace = "http://www.dm.de/eai/schema/intf00000107/v1")
    private Layout layout;
}
