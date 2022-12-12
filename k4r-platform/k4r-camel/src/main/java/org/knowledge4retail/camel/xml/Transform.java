package org.knowledge4retail.camel.xml;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Transform implements BasicXml {

    @XmlElement(name = "translation", namespace = "http://www.dm.de/eai/schema/intf00000107/v1")
    private Translation translation;

    @XmlElement(name = "rotation", namespace = "http://www.dm.de/eai/schema/intf00000107/v1")
    private Rotation rotation;
}
