package org.knowledge4retail.camel.xml;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class ShelfLayerId implements BasicXml {

    @XmlAttribute(name = "id", namespace = "http://www.dm.de/eai/schema/intf00000107/v1")
    private String ubicaExternalReferenceId;
}
