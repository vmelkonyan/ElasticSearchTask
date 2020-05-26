package com.lilas.elasticsearchtask.models;

import com.lilas.elasticsearchtask.constants.KeyConstants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "product")
@XmlAccessorType(XmlAccessType.FIELD)
@Document(indexName = KeyConstants.DOCUMENT_INDEX_NAME, type = "product")
@Getter
@Setter
public class Product {
    @XmlElement
    private String price;
    @XmlElement
    private String name;
    @XmlElement
    private String count;
    @XmlElement
    private String id;
    @XmlElement
    private String currency_code;
}
