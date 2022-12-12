package org.knowledge4retail.camel.xml;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Product implements BasicXml {

    private Double width;
    private Double height;
    private Double depth;
}
